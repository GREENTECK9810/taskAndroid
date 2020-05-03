package com.example.visha.taskapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.IntentService;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class tasksPage extends AppCompatActivity {

    ArrayList<Tasks> tasks = new ArrayList<>();
    taskManagerApi taskapi;
    sessionManager sm;
    String token;
    ProgressBar progressBar;
    Dialog dialog;
    FloatingActionButton fab;
    ImageView closePopup, loadingIcon;
    Button add;
    EditText task;
    Retrofit retrofit;
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_page);

        sm = new sessionManager(getApplicationContext());
        progressBar = (ProgressBar) findViewById(R.id.progress);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        dialog = new Dialog(this);



        retrofit = new Retrofit.Builder()
                .baseUrl("https://green-task-manager-api.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        taskapi = retrofit.create(taskManagerApi.class);
        token = sm.getData();


        initRecyclerView();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogue();
            }
        });
    }

    //method to show dialogue to add new task
    private void showDialogue() {
        dialog.setContentView(R.layout.dialogue_add_task);
        closePopup = (ImageView) dialog.findViewById(R.id.close);
        loadingIcon = (ImageView) dialog.findViewById(R.id.loading);
        add = (Button) dialog.findViewById(R.id.addbutton);
        task = (EditText) dialog.findViewById(R.id.tasktextbox);

        add.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                add.setVisibility(View.GONE);

                createTask(task.getText().toString());
                //animateLoadingIcon();
            }
        });

        closePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }




    //method for loading animatioon
    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void animateLoadingIcon(){
        Drawable d = loadingIcon.getDrawable();
        //final AnimatedVectorDrawable avd = (AnimatedVectorDrawable) d;

        if(d instanceof AnimatedVectorDrawableCompat){
            AnimatedVectorDrawableCompat avd = (AnimatedVectorDrawableCompat) d;

            avd.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                @Override
                public void onAnimationEnd(Drawable drawable) {
                    super.onAnimationEnd(drawable);
                    avd.start();
                }
            });
            avd.start();
        }else if(d instanceof AnimatedVectorDrawable){
            AnimatedVectorDrawable avd = (AnimatedVectorDrawable) d;

            avd.registerAnimationCallback(new Animatable2.AnimationCallback() {
                @Override
                public void onAnimationEnd(Drawable drawable) {
                    super.onAnimationEnd(drawable);
                    avd.start();
                }
            });

            avd.start();
        }
    }


    //prepare data when activity starts
    private class prepareData extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            Call<List<Tasks>> tasksCall = taskapi.tasks(token);

            try {
                Response<List<Tasks>> response = tasksCall.execute();

                if(!response.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Please authenticate",Toast.LENGTH_SHORT).show();
                    finish();
                }

                for(int i = 0; i < response.body().size(); i++){
                    tasks.add(response.body().get(i));
                }

            } catch (IOException e) {
                Toast.makeText(getApplicationContext(),"Network Failure",Toast.LENGTH_SHORT).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressBar.setVisibility(View.GONE);
            super.onPostExecute(aVoid);
        }
    }


    //method to initialize recyclerview on activity start
    private void initRecyclerView() {
        new prepareData().execute();
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new RecyclerViewAdapter(tasks, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter.setOnItemClickListener(new RecyclerViewAdapter.onItemClickListener() {
            @Override
            public void onGarbageClick(int position) {

                deleteTask(tasks.get(position).get_id());
                tasks.remove(position);
                adapter.notifyItemRemoved(position);

            }

            @Override
            public void onClickCheckbox(int position, Boolean completed) {
                tasks.get(position).setCompleted(completed);
                updateTask(tasks.get(position).get_id(), completed);
            }
        });
    }

    //method to update task
    private void updateTask(String id, Boolean completed) {

        Tasks task = new Tasks(completed);
        Call<Tasks> call = taskapi.updateTask(id, task, token);

        call.enqueue(new Callback<Tasks>() {
            @Override
            public void onResponse(Call<Tasks> call, Response<Tasks> response) {

                if(!response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Unable to update", Toast.LENGTH_SHORT).show();
                }

                Toast.makeText(getApplicationContext(), "Updated successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Tasks> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Network failure", Toast.LENGTH_SHORT).show();
            }
        });
    }


    //method to create new task
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void createTask(String text) {
        Tasks task = new Tasks(text);

        Call<Tasks> call = taskapi.createTask(task, token);
        animateLoadingIcon();
        call.enqueue(new Callback<Tasks>() {
            @Override
            public void onResponse(Call<Tasks> call, Response<Tasks> response) {

                if(!response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Unable to add task", Toast.LENGTH_SHORT).show();
                    return;
                }

                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Task added", Toast.LENGTH_SHORT).show();

                tasks.add(response.body());
                adapter.notifyItemInserted(tasks.size() - 1);
            }

            @Override
            public void onFailure(Call<Tasks> call, Throwable t) {

            }
        });

    }

    //method to delete task
    private void deleteTask(String task_id) {

        Call<Tasks> call = taskapi.deleteTask(task_id, token);

        call.enqueue(new Callback<Tasks>() {
            @Override
            public void onResponse(Call<Tasks> call, Response<Tasks> response) {

                if(!response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Unable to delete task", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(getApplicationContext(), "Task Deleted", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<Tasks> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Network failure", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
