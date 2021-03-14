package com.asmaa.firestore;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db;
    String TAG = "asmaa";
    private EditText mNameET;
    private EditText mPhoneET;
    private EditText mAddressET;
    private Button mAddBTN;
    private ListView mUsersLV;
    ArrayList<User>users;
    UserAdapter adapter;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNameET = findViewById(R.id.nameET);
        mPhoneET = findViewById(R.id.phoneET);
        mAddressET = findViewById(R.id.addressET);
        mAddBTN = findViewById(R.id.addBTN);
        mUsersLV = findViewById(R.id.usersLV);
        db = FirebaseFirestore.getInstance();
        users = new ArrayList<>();
        getData();
        mAddBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()){
                    dialog = new ProgressDialog(MainActivity.this);
                    dialog.setMessage("Adding data, please wait.");
                    dialog.show();
                    User user=new User(mNameET.getText().toString(),
                            mPhoneET.getText().toString(),
                            mAddressET.getText().toString());
                    addToFireStore(user);
                }
            }
        });
    }
    private void getData(){
        users.clear();
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for(DocumentSnapshot userSnapshot : task.getResult()){
                                String name = (String) userSnapshot.get("name");
                                String phone = (String) userSnapshot.get("phone");
                                String address = (String) userSnapshot.get("address");
                                User user = new User(name,phone,address);
                                users.add(user);
                            }
                            adapter=new UserAdapter(users,MainActivity.this);
                            mUsersLV.setAdapter(adapter);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
    private void addToFireStore(User user){
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        getData();
                        dialog.dismiss();
                        mNameET.setText("");
                        mPhoneET.setText("");
                        mAddressET.setText("");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        mNameET.setError("Error adding document");
                    }
                });
    }
    private boolean validation(){
        if (mNameET.getText().toString().isEmpty()){
            mNameET.setError("Empty Name");
            return false;
        } else  if (mPhoneET.getText().toString().isEmpty()){
            mPhoneET.setError("Empty PhoneNumber");
            return false;
        } else  if (mAddBTN.getText().toString().isEmpty()){
            mAddBTN.setError("Empty Address");
            return false;
        }

        return true;
    }
}
