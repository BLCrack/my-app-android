package com.findme.my_app_android;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit ourInstance;

    public static Retrofit getInstance(){
        if(ourInstance==null) {
            Gson gson = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();

            ourInstance = new Retrofit.Builder()
                    .baseUrl("http://172.20.10.2:8080")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }

        return ourInstance;
    }
}
