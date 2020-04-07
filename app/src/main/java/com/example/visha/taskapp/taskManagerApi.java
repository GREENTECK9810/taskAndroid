package com.example.visha.taskapp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface taskManagerApi {

    @POST("/users/login")
    Call<userAndToken> login(@Body User user);

}
