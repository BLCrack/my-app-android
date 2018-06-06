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
import com.findme.my_app_android.models.Location;
import com.findme.my_app_android.models.User;
import com.findme.my_app_android.models.UserCredentials;
import com.findme.my_app_android.security.TokenHolder;

import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddDeviceActivity extends AppCompatActivity {

    private Button newDeviceAcceptButton;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private RESTAPInterface restAPI;
    private List<Device> devices = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private User actualUser = new User(1, "admin", "admin");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);

        //Init API
        Retrofit retrofit = RetrofitClient.getInstance();
        restAPI = retrofit.create(RESTAPInterface.class);

        newDeviceAcceptButton = findViewById(R.id.newDeviceAcceptButton);
        newDeviceAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //zczytanie pol tekstowych, sprawdzenie wszystkich urządzen czy nie ma takiego numeru telefonu w bazie juz, jesli nie, to dodanie urzadzenia
                EditText phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
                EditText connectionNameEditText = findViewById(R.id.connectionNameEditText);
                EditText deviceNameEditText = findViewById(R.id.deviceNameEditText);
                String phoneNumber = phoneNumberEditText.getText().toString();
                String connectionName = connectionNameEditText.getText().toString();
                String deviceName = deviceNameEditText.getText().toString();

                //get user info
                getUserInfo();

                //pobranie wszystkich urządzen z bazy
                getAllDevices();
                //spr czy takie istnieje juz po numerze tel
                boolean isExsist = false;
                for(Device d: devices){
                    if(d.getPhoneNumber().equals(phoneNumber))
                        isExsist = true;
                }

                if(isExsist)
                    openDeviceExistAlert();
                else{
                    //data and time
                    Date date = new Date(Calendar.getInstance().getTime().getTime());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String actualDate = simpleDateFormat.format(date);
                    DateFormat df = new SimpleDateFormat("HH:mm:ss");
                    String actualTime = df.format(Calendar.getInstance().getTime());
                    Log.d("time:", actualTime);

                    //add device
                    Device device = new Device(1, phoneNumber,connectionName,deviceName, actualDate,null,actualUser);
                    addDevice(device);

                    //location
                    Location location = new Location(1, 0,0,actualDate,actualTime, device);
                    addLocation(location);
                    
                    Toast.makeText(AddDeviceActivity.this, "Dodano poprawnie urządzenie", Toast.LENGTH_LONG).show();
                    openHomeActivity();
                }

            }
        });
    }

    public void openHomeActivity(){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    public void getUserInfo(){
        compositeDisposable.add(restAPI.getUsers("Bearer " + TokenHolder.getInstance().getToken())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<List<User>>() {
            @Override
            public void accept(List<User> users) throws Exception {
                Log.d("rozmiar:", "users=>"+ users.size());
                for(User usr: users){
                    if(UserCredentials.getInstance().getUsername().equals(usr.getLogin())) {
                        actualUser = new User(usr.getId(), usr.getLogin(), usr.getPassword());
                        break;
                    }
                }
            }
        }));
    }

    public void getAllDevices(){
        compositeDisposable.add(restAPI.getAllDevices("Bearer " + TokenHolder.getInstance().getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Device>>() {
                    @Override
                    public void accept(List<Device> devices) throws Exception {
                        initializeDevices(devices);
                    }
                }));
    }

    public void addDevice(Device device){
        Call<Device> call = restAPI.addDevice(device, "Bearer " + TokenHolder.getInstance().getToken());
        call.enqueue(new Callback<Device>() {
            @Override
            public void onResponse(Call<Device> call, Response<Device> response) {

            }

            @Override
            public void onFailure(Call<Device> call, Throwable t) {

            }
        });
    }
    public void addLocation(Location location){
        Call<Location> call = restAPI.addLocation(location, "Bearer " + TokenHolder.getInstance().getToken());
        call.enqueue(new Callback<Location>() {
            @Override
            public void onResponse(Call<Location> call, Response<Location> response) {

            }

            @Override
            public void onFailure(Call<Location> call, Throwable t) {

            }
        });
    }
    public void initializeDevices(List<Device> devices){
        this.devices = devices;
    }

    public void openDeviceExistAlert(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Urządzenie o tym numerze już istnieje!");
        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(AddDeviceActivity.this,"Zmień dane urządzenia",Toast.LENGTH_LONG).show();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
