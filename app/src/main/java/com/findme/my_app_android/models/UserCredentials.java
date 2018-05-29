package com.findme.my_app_android.models;

import com.google.gson.annotations.Expose;

public class UserCredentials {
    private static UserCredentials instance = new UserCredentials();
    @Expose
    private String username;
    @Expose
    private String password;

    public static UserCredentials getInstance() {
        return instance;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
