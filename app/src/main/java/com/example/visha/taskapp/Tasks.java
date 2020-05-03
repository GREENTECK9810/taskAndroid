package com.example.visha.taskapp;

public class Tasks {

    private String description;
    private Boolean completed;
    private String _id;

    public Tasks(String description) {
        this.description = description;
        this.completed = false;
    }

    public Tasks(Boolean completed) {
        this.completed = completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public String get_id() {
        return _id;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getCompleted() {
        return completed;
    }
}
