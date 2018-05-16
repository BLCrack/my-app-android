package com.findme.my_app_android.interfaces;

import com.findme.my_app_android.models.User;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface RESTAPInterface {

    @GET("/users/all")
    Observable<List<User>> getUsers();
}
