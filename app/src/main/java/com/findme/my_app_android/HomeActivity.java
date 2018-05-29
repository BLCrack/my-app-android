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
import com.findme.my_app_android.security.TokenHolder;
import com.google.gson.Gson;

import org.json.JSONObject;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        startConnectionButton = findViewById(R.id.startConnectionButton);
        startConnectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    public void openConnectionActivity(){
        Intent intent = new Intent(this, ConnectionActivity.class);
        startActivity(intent);
    }

    public void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
