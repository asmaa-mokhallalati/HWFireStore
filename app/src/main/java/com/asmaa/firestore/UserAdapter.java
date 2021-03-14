package com.asmaa.firestore;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
public class UserAdapter extends BaseAdapter {
    ArrayList<User> users = new ArrayList<>();
    Activity activity;
    private TextView mNameTV;
    private TextView mPhoneTV;
    private TextView mAddressTV;

    public UserAdapter(ArrayList<User> users, Activity activity) {
        this.users = users;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = LayoutInflater.from(activity).inflate(R.layout.user_layout, null);
        mNameTV = v.findViewById(R.id.nameTV);
        mPhoneTV = v.findViewById(R.id.phoneTV);
        mAddressTV = v.findViewById(R.id.addressTV);

        mNameTV.setText(users.get(position).getName());
        mPhoneTV.setText(users.get(position).getPhone());
        mAddressTV.setText(users.get(position).getAddress());
        return v;

    }
}
