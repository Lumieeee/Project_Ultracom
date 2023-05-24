package com.example.project_ultracom;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ProfileFragment extends Fragment {
    SharedPreferences sharedPreferences;
    private TextView username,email;
    private Button editProfile, logout;

    public ProfileFragment(){
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        username = view.findViewById(R.id.tv_username);
        email = view.findViewById(R.id.tv_email);
        editProfile = view.findViewById(R.id.btn_editprofile);
        logout = view.findViewById(R.id.btn_logout);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfile.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("username");
                editor.apply();
                Intent intent = new Intent(getActivity(), LoginPage.class);
                startActivity(intent);
            }
        });

        sharedPreferences = getActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        username.setText(sharedPreferences.getString("username", null));
        email.setText(sharedPreferences.getString("email", null));
        return view;
    }
}