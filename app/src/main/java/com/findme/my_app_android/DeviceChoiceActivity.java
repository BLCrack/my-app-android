package com.findme.my_app_android;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.findme.my_app_android.interfaces.RESTAPInterface;
import com.findme.my_app_android.models.Device;
import com.findme.my_app_android.security.TokenHolder;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class DeviceChoiceActivity extends AppCompatActivity {

   private Button acceptNumberButton;
   private Button addDeviceButton;
   private CompositeDisposable compositeDisposable = new CompositeDisposable();
   private RESTAPInterface restAPI;
   private List<Device> devices = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_choice);

        //Init API
        Retrofit retrofit = RetrofitClient.getInstance();
        restAPI = retrofit.create(RESTAPInterface.class);
        //pobieranie urządzeń użytkownika
        getAllDevicesForCurrentUser();

        acceptNumberButton = findViewById(R.id.acceptButton);
        acceptNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText phoneNumberEditText = findViewById(R.id.phoneNumberEditText2);
                String phoneNumber = phoneNumberEditText.getText().toString();
                boolean isExsist = false;

                for(Device d: devices)
                {
                    Log.d("phone:", d.getPhoneNumber());
                    if(d.getPhoneNumber().equals(phoneNumber))
                        isExsist = true;
                }

                if(isExsist)
                    openHomeActivity();
                else
                    openDeviceNoFoundAlert();
            }
        });

        addDeviceButton = findViewById(R.id.addDeviceButton);
        addDeviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddDeviceActivity();
            }
        });
    }

    public void getAllDevicesForCurrentUser(){
        compositeDisposable.add(restAPI.getDevicesForCurrentUser("Bearer " + TokenHolder.getInstance().getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Device>>() {
                    @Override
                    public void accept(List<Device> devices) throws Exception {
                        initializeDevices(devices);
                    }
                }));
    }



    public void initializeDevices(List<Device> devices){
        this.devices = devices;
    }

    public void openHomeActivity(){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    public void openAddDeviceActivity(){
        Intent intent = new Intent(this, AddDeviceActivity.class);
        startActivity(intent);
    }

    public void openDeviceNoFoundAlert(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Nie ma takiego urządzenia lub nie należy do Ciebie!");
        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(DeviceChoiceActivity.this,"Możesz dodać nowe urządzenie",Toast.LENGTH_LONG).show();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
