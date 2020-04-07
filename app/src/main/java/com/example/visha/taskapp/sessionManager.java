package com.example.visha.taskapp;

import android.content.Context;
import android.content.SharedPreferences;

public class sessionManager {

    String token;
    Context _context;
    String TEXT = "MySharedPref";

    sessionManager(Context _context){
        this._context = _context.getApplicationContext();
    }

    public void StoreData(String token){
        SharedPreferences sharedPreferences = _context.getSharedPreferences(this.TEXT,_context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("token", token);

    }

    public String getData(){
        SharedPreferences sharedPreferences = _context.getSharedPreferences(this.TEXT,_context.MODE_PRIVATE);

        this.token = sharedPreferences.getString("token","");

        return token;
    }
}
