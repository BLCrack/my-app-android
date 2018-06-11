package com.findme.my_app_android.interfaces;

import com.findme.my_app_android.models.Device;
import com.findme.my_app_android.models.Location;
import com.findme.my_app_android.models.User;
import com.findme.my_app_android.models.UserCredentials;
import com.findme.my_app_android.security.TokenHolder;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RESTAPInterface {

    //users
    @GET("/users/all")
    Observable<List<User>> getUsers(@Header("Authorization") String token);

    //devices
    @GET("/devices/all")
    Observable<List<Device>> getAllDevices(@Header("Authorization") String token);

    @GET("/devices/forcurrentuser")
    Observable<List<Device>> getDevicesForCurrentUser(@Header("Authorization") String token);

    @POST("/devices/add")
    Observable<Device> addDevice(@Body Device device, @Header("Authorization") String token);

    //locations
    @POST("/locations/add")
    Observable<Location> addLocation(@Body Location location, @Header("Authorization") String token);

    //security authentication
    @POST("/login")
    Call<TokenHolder> login(@Body UserCredentials userCredentials);

    //updating locations
    @PUT("/locations/update/{id}")
    Observable<Location> putLocation(@Body Location location, @Header("Authorization") String token, @Path("id") int locationId);
}
