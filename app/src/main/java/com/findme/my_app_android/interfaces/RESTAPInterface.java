package com.findme.my_app_android.interfaces;

import com.findme.my_app_android.models.Device;
import com.findme.my_app_android.models.Location;
import com.findme.my_app_android.models.User;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RESTAPInterface {

    //users
    @GET("/users/all")
    Observable<List<User>> getUsers();

    //devices
    @GET("/devices/all")
    Observable<List<Device>> getDevices();

    @POST("/devices/add")
    Call<Device> addDevice(@Body Device device);

    //locations
    @POST("/locations/add")
    Call<Location> updateLocation(@Body Location location);
}
