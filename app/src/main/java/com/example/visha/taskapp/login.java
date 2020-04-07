package com.example.visha.taskapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class login extends Fragment {

    Button loginButton;
    EditText email;
    EditText password;
    private taskManagerApi taskapi;

    public login() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.fragment_login, container, false);

        loginButton = (Button) mView.findViewById(R.id.fragment_login_button);
        email = (EditText) mView.findViewById(R.id.email);
        password = (EditText) mView.findViewById(R.id.password);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://green-task-manager-api.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        taskapi = retrofit.create(taskManagerApi.class);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                doLogin();
                //Intent intent = new Intent(v.getContext(), HomePage.class);
                //v.getContext().startActivity(intent);
            }
        });
        // Inflate the layout for this fragment



        return mView;

    }

    private void doLogin(){
        //user object created with values email and password
        User user = new User(email.getText().toString(), password.getText().toString());

        //POST request with body containg user object
        Call<userAndToken> call = taskapi.login(user);

        call.enqueue(new Callback<userAndToken>() {
            @Override
            public void onResponse(Call<userAndToken> call, Response<userAndToken> response) {


                if(!response.isSuccessful()){

                    Toast.makeText(getActivity().getApplicationContext(),"Incorrect email or password",Toast.LENGTH_SHORT).show();
                    return;
                }

                //store token in sharedpref if response is successfull
                sessionManager sm = new sessionManager(getActivity());
                sm.StoreData(response.body().getToken());
                Toast.makeText(getActivity().getApplicationContext(),response.body().getToken(),Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<userAndToken> call, Throwable t) {

            }
        });

    }

}
