package com.findme.my_app_android.security;

import com.google.gson.annotations.Expose;

public class TokenHolder {
    private static TokenHolder instance = new TokenHolder();

    @Expose
    private String token = null;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static TokenHolder getInstance() {
        return instance;
    }
}
