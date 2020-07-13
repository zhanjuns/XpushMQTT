/*
 * Copyright (C) 2020 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.xuexiang.mqttdemo.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class GetLocation {

    private Location mLocation;
    private String locationStr;
    private static GetLocation oneLocation;

    private GetLocation() {
    }

    /**
     * 单例
     * @return
     */
    public static GetLocation getLocationIns(){
        if (oneLocation==null){
            synchronized (GetLocation.class){
                if (oneLocation==null){
                    oneLocation = new GetLocation();
                }
            }
        }
        return oneLocation;
    }

    public String getLocation(Context context)
    {
        //获取系统的LocationManager对象
        final LocationManager mLocationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        try
        {
//            mLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//            locationStr = updateLocation(mLocation);
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, new LocationListener() {

                @Override
                public void onLocationChanged(Location location)
                {
                    String str = updateLocation(location);
                    if (str!=null){
                        locationStr=str;
                    }

                }
                @Override
                public void onStatusChanged(String provider, int status, Bundle extras)
                {
                }
                @Override
                public void onProviderEnabled(String provider)
                {
                    try
                    {
                        String str = updateLocation(mLocationManager.getLastKnownLocation(provider));
                        if (str!=null){
                            locationStr=str;
                        }
                    }
                    catch (SecurityException e)
                    {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onProviderDisabled(String provider)
                {

                }
            });
        }
        catch (SecurityException e)
        {
            e.printStackTrace();
            return locationStr;
        }

        return locationStr;

    }

    private String updateLocation(Location location)
    {
        if (location != null)
        {
            StringBuffer sb = new StringBuffer();
            sb.append("位置信息：\n");
            sb.append("经度： ");
            sb.append(location.getLongitude());
            sb.append("\n纬度： ");
            sb.append(location.getLatitude());
            sb.append("\n高度： ");
            sb.append(location.getAltitude());
            sb.append("\n速度： ");
            sb.append(location.getSpeed());
            sb.append("\n方向： ");
            sb.append(location.getBearing());
            return new String(sb);
        }
        return null;
    }

}
