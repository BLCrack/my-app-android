package com.findme.my_app_android;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.findme.my_app_android.interfaces.RESTAPInterface;
import com.findme.my_app_android.models.Device;
import com.findme.my_app_android.models.Location;
import com.findme.my_app_android.security.TokenHolder;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class FindMeApplication extends Application {

    private Disposable disposableUpdate;
    private Disposable disposableSend;

    private LocationAPI locationAPI;

    private Device device;
    private int locationId;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    public void updateLocation() {
        if (this.disposableUpdate != null && !this.disposableUpdate.isDisposed()) {
            this.disposableUpdate.dispose();
        }

        this.disposableUpdate = Observable.just(true)
                .delay(60, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        sendLocation();
                    }
                });
    }


    public void sendLocation() {
        locationAPI = new LocationAPI(this);
        double longitude = locationAPI.getLongitude();
        double latitude = locationAPI.getLatitude();

        Date date = new Date(Calendar.getInstance().getTime().getTime());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String actualDate = simpleDateFormat.format(date);
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        String actualTime = df.format(Calendar.getInstance().getTime());

        this.locationId = device.getId();
        Location location = new Location(this.locationId, (float) latitude, (float) longitude, actualDate, actualTime, device);

        Log.e("Location", "latitude: " + latitude);
        Log.e("Location", "longitude: " + longitude);

        if (this.disposableSend != null && !this.disposableSend.isDisposed()) {
            this.disposableSend.dispose();
        }

        RetrofitClient.getInstance().create(RESTAPInterface.class)
                .putLocation(location, "Bearer " + TokenHolder.getInstance().getToken(), device.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Location>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposableSend = d;
                    }

                    @Override
                    public void onNext(Location location) {
                        updateLocation();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        sendLocation();
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    public void stopSendingLocation() {
        if (this.disposableSend != null && !this.disposableSend.isDisposed()) {
            this.disposableSend.dispose();
        }

        if (this.disposableUpdate != null && !this.disposableUpdate.isDisposed()) {
            this.disposableUpdate.dispose();
        }
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public void setLocationId(int id) {
        this.locationId = id;
    }

}
