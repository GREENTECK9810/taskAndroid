package com.example.visha.taskapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface taskManagerApi {

    @POST("/users/login")
    Call<userAndToken> login(@Body User user);


    @GET("/users/me")
    Call<User> profile(@Header("Authorization") String token);

    @GET("/tasks")
    Call<List<Tasks>> tasks(@Header("Authorization") String token);

    @POST("/users")
    Call<userAndToken> createUser(@Body User user);

    @POST("/tasks")
    Call<Tasks> createTask(@Body Tasks task, @Header("Authorization") String token);

    @DELETE("/tasks/{id}")
    Call<Tasks> deleteTask(@Path("id") String id, @Header("Authorization") String token);

    @PATCH("/tasks/{id}")
    Call<Tasks> updateTask(@Path("id") String id,@Body Tasks task, @Header("Authorization") String token);

}
