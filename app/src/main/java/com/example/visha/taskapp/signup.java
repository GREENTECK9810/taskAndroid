package com.example.visha.taskapp;


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

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class signup extends Fragment {

    sessionManager sm;
    EditText name;
    EditText email;
    EditText password;
    EditText age;
    Button sign_up;
    taskManagerApi taskapi;
    private String token;


    public signup() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_signup, container, false);

        name = (EditText) mView.findViewById(R.id.name);
        email = (EditText) mView.findViewById(R.id.email);
        password = (EditText) mView.findViewById(R.id.password);
        age = (EditText) mView.findViewById(R.id.age);
        sign_up = (Button) mView.findViewById(R.id.sign_up);
        sm = new sessionManager(getActivity().getApplicationContext());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://green-task-manager-api.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        taskapi = retrofit.create(taskManagerApi.class);

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        return mView;
    }

    private void signUp() {

        if(!isValid()){
            return;
        }

        User user = new User(email.getText().toString(), password.getText().toString());
        user.setName(name.getText().toString());

        Call<userAndToken> call = taskapi.createUser(user);

        call.enqueue(new Callback<userAndToken>() {
            @Override
            public void onResponse(Call<userAndToken> call, Response<userAndToken> response) {

                if(!response.isSuccessful()){

                    JSONObject jsonObject = null;

                    try {

                        jsonObject = new JSONObject(response.errorBody().string());
                        String code = jsonObject.getString("code");

                        if(code.equals("11000")){
                            Toast.makeText(getActivity().getApplicationContext(),"Account already exist",Toast.LENGTH_SHORT).show();
                            return;
                        }

                    }catch (JSONException | IOException e){
                        e.printStackTrace();
                    }



                    Toast.makeText(getActivity().getApplicationContext(),"Cannot signup",Toast.LENGTH_SHORT).show();
                    return;
                }

                token = response.body().getToken();
                sm.StoreData(token);

                Toast.makeText(getActivity().getApplicationContext(),"Account Created",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity().getApplicationContext(),HomePage.class);
                startActivity(intent);

            }

            @Override
            public void onFailure(Call<userAndToken> call, Throwable t) {

            }
        });
    }

    private boolean isValid(){
        if(name.getText().toString() == ""){
            Toast.makeText(getActivity().getApplicationContext(),"Name cannot be empty",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(email.getText().toString() == ""){
            Toast.makeText(getActivity().getApplicationContext(),"email cannot be empty",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(password.getText().toString() == ""){
            Toast.makeText(getActivity().getApplicationContext(),"password cannot be empty",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(password.getText().toString().length() < 7){
            Toast.makeText(getActivity().getApplicationContext(),"Password length must be greater than 7",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

}
