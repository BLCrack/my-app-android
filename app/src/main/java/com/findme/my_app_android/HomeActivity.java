package com.findme.my_app_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.findme.my_app_android.interfaces.RESTAPInterface;
import com.findme.my_app_android.models.Device;
import com.findme.my_app_android.models.Location;
import com.findme.my_app_android.models.User;
import com.findme.my_app_android.models.UserCredentials;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class HomeActivity extends AppCompatActivity {

    private Button startConnectionButton;
    private Button signOutButton;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private RESTAPInterface restAPI;
    private List<Device> devices = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Init API
        Retrofit retrofit = RetrofitClient.getInstance();
        restAPI = retrofit.create(RESTAPInterface.class);

        startConnectionButton = findViewById(R.id.startConnectionButton);
        startConnectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pobieranie urządzeń użytkownika
                getAllDevicesForCurrentUser();

                openConnectionActivity();
            }
        });

        signOutButton = findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });
    }

    public void getAllDevicesForCurrentUser(){
        compositeDisposable.add(restAPI.getDevices()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Device>>() {
                    @Override
                    public void accept(List<Device> devices) throws Exception {
                        for(Device d: devices)
                            Log.d("device:" , d.getConnectionName());
                    }
                }));

    }

    public void openConnectionActivity(){
        Intent intent = new Intent(this, ConnectionActivity.class);
        startActivity(intent);
    }

    public void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
