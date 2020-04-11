package com.example.visha.taskapp;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomePage extends AppCompatActivity {

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    RelativeLayout relativeLayout;
    TextView name;
    TextView taskCount;
    private taskManagerApi taskapi;
    private String token;
    User user;
    Integer tcount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        relativeLayout = (RelativeLayout) findViewById(R.id.loaddata);
        name = (TextView) findViewById(R.id.name);
        taskCount = (TextView) findViewById(R.id.task);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://green-task-manager-api.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        taskapi = retrofit.create(taskManagerApi.class);

        //get the token from sharedprefs
        getToken();

        new PrepareData().execute();
        setUpToolbar();

    }

    private void getToken() {
        sessionManager sm = new sessionManager(getApplicationContext());
        token = sm.getData();
    }

    private void setUpToolbar(){

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        toolbar = (Toolbar) findViewById(R.id.homePageToolbar);
        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private class PrepareData extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            relativeLayout.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            //synchronous call to load data on homepage

            Call<User> uesrsCall = taskapi.profile(token);
            Call<List<Tasks>> tasksCall = taskapi.tasks(token);

            try {
                Response<User> response = uesrsCall.execute();

                if(!response.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Please authenticate",Toast.LENGTH_SHORT).show();
                    finish();
                }

                user = response.body();

            } catch (IOException e) {
                Toast.makeText(getApplicationContext(),"Network Failure",Toast.LENGTH_SHORT).show();
            }

            try {
                Response<List<Tasks>> response = tasksCall.execute();

                if(!response.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Please authenticate",Toast.LENGTH_SHORT).show();
                    finish();
                }

                tcount = response.body().size();

            } catch (IOException e) {
                Toast.makeText(getApplicationContext(),"Network Failure",Toast.LENGTH_SHORT).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            if(user != null && tcount != null){
                name.setText("Hi, " + user.getName().substring(0,1).toUpperCase() + user.getName().substring(1));
                taskCount.setText("You have " + tcount.toString() + " tasks today");
            }

            relativeLayout.setVisibility(View.GONE);
        }
    }
}


