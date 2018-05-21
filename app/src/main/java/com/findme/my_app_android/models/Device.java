package com.findme.my_app_android.models;

import java.sql.Date;

public class Device {
    private int id;
    private int phoneNumber;
    private String connectionName;
    private String deviceName;
    private Date startConnection;
    private Location actualLocation;
    private User owner;


    public Device(int id, int phoneNumber, String connectionName, String deviceName, Date startConnection, Location actualLocation, User owner) {
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

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
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

    public Date getStartConnection() {
        return startConnection;
    }

    public void setStartConnection(Date startConnection) {
        this.startConnection = startConnection;
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
