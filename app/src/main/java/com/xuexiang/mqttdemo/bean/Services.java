package com.xuexiang.mqttdemo.bean;

import java.io.Serializable;

public class Services implements Serializable {
    private Data data;
    private String eventTime;
    private String serviceId;

    private static final long serialVersionUID = -6849794410L;

    public Services() {
    }

    @Override
    public String toString() {
        return "Services{" +
                "data=" + data +
                ", eventTime='" + eventTime + '\'' +
                ", serviceId='" + serviceId + '\'' +
                '}';
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public Services(Data data, String eventTime, String serviceId) {
        this.data = data;
        this.eventTime = eventTime;
        this.serviceId = serviceId;
    }
}
