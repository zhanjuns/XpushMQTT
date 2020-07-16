package com.xuexiang.mqttdemo.bean;

import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Devices implements Serializable {
    private String deviceId;
    ArrayList<Services> services;

    private static final long serialVersionUID = -684979667710L;

    @Override
    public String toString() {
        return "Devices{" +
                "deviceId='" + deviceId + '\'' +
                ", services=" + services +
                '}';
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public ArrayList<Services> getServices() {
        return services;
    }

    public void setServices(ArrayList<Services> services) {
        this.services = services;
    }

    public Devices(String deviceId, ArrayList<Services> services) {
        this.deviceId = deviceId;
        this.services = services;
    }

    public Devices() {
    }
}
