package com.findme.my_app_android.models;

import com.google.gson.annotations.Expose;

import java.sql.Date;

public class Device {
    @Expose(serialize = false)
    private int id;
    @Expose
    private String phoneNumber;
    @Expose
    private String connectionName;
    @Expose
    private String deviceName;
    @Expose(deserialize = false)
    private String startConnection;
    @Expose(deserialize = false)
    private Location actualLocation;
    @Expose
    private User owner;


    public Device(int id, String phoneNumber, String connectionName, String deviceName, String startConnection, Location actualLocation, User owner) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.connectionName = connectionName;
        this.deviceName = deviceName;
        this.startConnection = startConnection;
        this.actualLocation = actualLocation;
        this.owner = owner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getConnectionName() {
        return connectionName;
    }

    public void setConnectionName(String connectionName) {
        this.connectionName = connectionName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getStartConnection() {
        return startConnection;
    }

    public void setStartConnection(String startConnection) { this.startConnection = startConnection;
    }

    public Location getActualLocation() {
        return actualLocation;
    }

    public void setActualLocation(Location actualLocation) {
        this.actualLocation = actualLocation;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
