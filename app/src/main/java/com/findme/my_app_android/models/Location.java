package com.findme.my_app_android.models;

import com.google.gson.annotations.Expose;

import java.sql.Date;
import java.sql.Time;

public class Location {
    @Expose
    private int id;
    @Expose
    private float gpsLatitude;
    @Expose
    private float gpsLongitude;
    @Expose(deserialize = false)
    private String date;
    @Expose(deserialize = false)
    private String time;
    @Expose
    private Device device;

    public Location(int id, float gpsLatitude, float gpsLongitude, String date, String time, Device device) {
        this.id = id;
        this.gpsLatitude = gpsLatitude;
        this.gpsLongitude = gpsLongitude;
        this.date = date;
        this.time = time;
        this.device = device;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getGpsLatitude() {
        return gpsLatitude;
    }

    public void setGpsLatitude(float gpsLatitude) {
        this.gpsLatitude = gpsLatitude;
    }

    public float getGpsLongitude() {
        return gpsLongitude;
    }

    public void setGpsLongitude(float gpsLongitude) {
        this.gpsLongitude = gpsLongitude;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
}
