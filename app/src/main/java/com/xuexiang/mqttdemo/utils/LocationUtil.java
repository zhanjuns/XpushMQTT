package com.xuexiang.mqttdemo.utils;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

import com.xuexiang.mqttdemo.bean.Data;

import java.security.Provider;
import java.util.List;

public class LocationUtil {

    private Context context;
    private Location mLocation;
    private StringBuffer sb = new StringBuffer();
    private static LocationUtil locationUtil;
    private Data data;

    private LocationUtil(Context context){
        this.context = context;
    }

    public static LocationUtil getLocationUtil(Context context) {
        if (locationUtil == null){
            synchronized (context){
                if (locationUtil == null){
                    locationUtil = new LocationUtil(context);
                }
            }
        }
        return locationUtil;
    }

    public void getLocation() {
        //获取系统的LocationManager对象
        final LocationManager mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
//        String provider = mLocationManager.getProvider(LocationManager.GPS_PROVIDER);
        if (!mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            XToastUtils.info("请打开使用GPS和使用网络定位以提高精度");
            context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
        try {
            mLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            updateLocation(mLocation);
            mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            updateLocation(mLocation);
            mLocation = mLocationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            updateLocation(mLocation);
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, new LocationListener() {
                @Override
                public void onLocationChanged(android.location.Location location) {
                    updateLocation(location);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                @Override
                public void onProviderEnabled(String provider) {
                    try {
                        updateLocation(mLocationManager.getLastKnownLocation(provider));
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void updateLocation(android.location.Location location) {
        if (location != null) {
            data = new Data("" + location.getLongitude(), "" + location.getLatitude());
        }
    }

    public Data getData() {
        getLocation();
        return data;
    }

}
