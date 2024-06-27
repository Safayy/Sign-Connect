package com.safa.signconnect.model;

public class LoggedInUser {
    String name;
    String email;
    public LoggedInUser(String name, String email) {
        name = name;
        email = email;
    }

    public String getDisplayName() {
        return name;
    }
}