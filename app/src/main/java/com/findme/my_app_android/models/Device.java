package com.findme.my_app_android.models;

import java.sql.Date;

public class Device {
    private int id;
    private String connectionName;
    private String deviceName;
    private int historyDays;
    private Date startConnection;
    private Location actualLocation;
    private User owner;


    public Device(int id, String connectionName, String deviceName, int historyDays, Date startConnection, Location actualLocation, User owner) {
        this.id = id;
        this.connectionName = connectionName;
        this.deviceName = deviceName;
        this.historyDays = historyDays;
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

    public int getHistoryDays() {
        return historyDays;
    }

    public void setHistoryDays(int historyDays) {
        this.historyDays = historyDays;
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
