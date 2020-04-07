package com.example.visha.taskapp;


public class User {

    private String name;
    private String email;
    private String password;
    private int age;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }
}
