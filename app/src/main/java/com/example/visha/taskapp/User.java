package com.example.visha.taskapp;


public class User {

    private String name;
    private String email;
    private String password;
    private Integer age;

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

    public void setAge(int age){
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }
}
