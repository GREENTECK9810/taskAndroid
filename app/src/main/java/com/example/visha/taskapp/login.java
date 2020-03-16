package com.example.visha.taskapp;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class login extends Fragment {

    Button loginButton;

    public login() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.fragment_login, container, false);

        loginButton = (Button) mView.findViewById(R.id.fragment_login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), HomePage.class);
                v.getContext().startActivity(intent);
            }
        });
        // Inflate the layout for this fragment
        return mView;




    }

}
