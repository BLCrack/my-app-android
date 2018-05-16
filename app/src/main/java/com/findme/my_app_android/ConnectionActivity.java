package com.findme.my_app_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.findme.my_app_android.models.Location;

public class ConnectionActivity extends AppCompatActivity {

    Button stopConnectionButton;
    LocationAPI locationAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        stopConnectionButton = findViewById(R.id.stopConnectionButton);
        stopConnectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHomeActivity();
            }
        });

        //check location
        locationAPI = new LocationAPI(ConnectionActivity.this);

        if(locationAPI.isCanGetLocation()){
            double longitude = locationAPI.getLongitude();
            double latitude = locationAPI.getLatitude();
            Log.d("long: ", String.valueOf(longitude));
            Log.d("lat: ", String.valueOf(latitude));
        }
        else {
            locationAPI.showSettingsAlert();
        }
    }

    public void openHomeActivity(){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}
