package com.example.visha.taskapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface taskManagerApi {

    @POST("/users/login")
    Call<userAndToken> login(@Body User user);


    @GET("/users/me")
    Call<User> profile(@Header("Authorization") String token);

    @GET("/tasks")
    Call<List<Tasks>> tasks(@Header("Authorization") String token);

}
