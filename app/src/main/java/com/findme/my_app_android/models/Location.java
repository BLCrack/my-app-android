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
    private Date date;
    @Expose(deserialize = false)
    private Time time;
    @Expose
    private Device device;

    public Location(int id, float gpsLatitude, float gpsLongitude, Date date, Time time, Device device) {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
}
