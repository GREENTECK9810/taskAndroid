package com.example.visha.taskapp;

import android.content.Context;
import android.content.SharedPreferences;

public class sessionManager {

    private String token;
    Context _context;
    private static final String name = "MySharedPref";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int PRIVATE_MODE = 0;

    sessionManager(Context _context){
        this._context = _context;
        sharedPreferences = _context.getSharedPreferences(this.name,PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void StoreData(String token){

        editor.putString("token", token);
        editor.commit();

    }

    public String getData(){

        this.token = sharedPreferences.getString("token", null);

        return token;
    }
}
