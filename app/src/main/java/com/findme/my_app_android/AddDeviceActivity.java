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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
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

    private ProgressBar progressBar;
    private RelativeLayout relativeLayoutContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);

        this.progressBar = findViewById(R.id.connectionProgressBar);
        this.relativeLayoutContainer = findViewById(R.id.connectionContainer);

        //Init API
        Retrofit retrofit = RetrofitClient.getInstance();
        restAPI = retrofit.create(RESTAPInterface.class);

        this.lockForLoad(true);
        this.getUserInfo();

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

                //spr czy takie istnieje juz po numerze tel
                boolean isExsist = false;
                for (Device d : devices) {
                    if (d.getPhoneNumber().equals(phoneNumber))
                        isExsist = true;
                }

                if (isExsist)
                    openDeviceExistAlert();
                else {
                    //data and time
                    Date date = new Date(Calendar.getInstance().getTime().getTime());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String actualDate = simpleDateFormat.format(date);
                    DateFormat df = new SimpleDateFormat("HH:mm:ss");
                    String actualTime = df.format(Calendar.getInstance().getTime());
                    Log.d("time:", actualTime);

                    //add device
                    Device device = new Device(1, phoneNumber, connectionName, deviceName, actualDate, null, actualUser);
                    addDevice(device);
                }

            }
        });
    }

    public void openHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    public void getUserInfo() {
        this.restAPI.getUsers("Bearer " + TokenHolder.getInstance().getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<User>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<User> users) {
                        Log.d("rozmiar:", "users=>" + users.size());
                        for (User usr : users) {
                            if (UserCredentials.getInstance().getUsername().equals(usr.getLogin())) {
                                actualUser = new User(usr.getId(), usr.getLogin(), usr.getPassword());

                                getAllDevices();

                                break;
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(AddDeviceActivity.this, "Błąd pobierania danych.", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public void getAllDevices() {
        this.restAPI.getAllDevices("Bearer " + TokenHolder.getInstance().getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Device>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Device> devices) {
                        initializeDevices(devices);
                        lockForLoad(false);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void addDevice(final Device device) {
        this.restAPI.addDevice(device, "Bearer " + TokenHolder.getInstance().getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Device>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Device device) {
                        Date date = new Date(Calendar.getInstance().getTime().getTime());
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String actualDate = simpleDateFormat.format(date);
                        DateFormat df = new SimpleDateFormat("HH:mm:ss");
                        String actualTime = df.format(Calendar.getInstance().getTime());

                        Location location = new Location(device.getId(), 0, 0, actualDate, actualTime, null);
                        addLocation(location);

                        ((FindMeApplication) getApplication()).setDevice(device);
                    }

                    @Override
                    public void onError(Throwable e) {
                        addDevice(device);
                        Toast.makeText(AddDeviceActivity.this, "Nie udało się dodać urządzenia. Spróbuj ponownie.", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void putDeviceData(Location location) {
        Device device = ((FindMeApplication) getApplication()).getDevice();
        device.setActualLocation(location);
        this.restAPI.putDevice(device, "Bearer " + TokenHolder.getInstance().getToken(), device.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Device>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Device device) {
                        ((FindMeApplication) getApplication()).setDevice(device);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(AddDeviceActivity.this, "Dodano poprawnie urządzenie", Toast.LENGTH_LONG).show();
                        openHomeActivity();
                    }
                });
    }

    public void addLocation(final Location location) {
        this.restAPI.addLocation(location, "Bearer " + TokenHolder.getInstance().getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Location>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Location location) {
                        if (location != null) {
                            ((FindMeApplication) getApplication()).setLocationId(location.getId());
                            putDeviceData(location);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        addLocation(location);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void initializeDevices(List<Device> devices) {
        this.devices = devices;
    }

    public void openDeviceExistAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Urządzenie o tym numerze już istnieje!");
        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(AddDeviceActivity.this, "Zmień dane urządzenia", Toast.LENGTH_LONG).show();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void lockForLoad(boolean loading) {
        int loaderVisibility = loading ? View.VISIBLE : View.GONE;
        int containerVisibility = loading ? View.GONE : View.VISIBLE;
        this.progressBar.setVisibility(loaderVisibility);
        this.relativeLayoutContainer.setVisibility(containerVisibility);
    }
}
