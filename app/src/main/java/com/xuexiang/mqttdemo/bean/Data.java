package com.xuexiang.mqttdemo.bean;

import java.io.Serializable;

public class Data implements Serializable {
    private String longitude;
    private String latitude;


    private static final long serialVersionUID = -68497944667710L;

    @Override
    public String toString() {
        return "Data{" +
                "longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                '}';
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public Data(String longitude, String latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Data() {
    }
}
