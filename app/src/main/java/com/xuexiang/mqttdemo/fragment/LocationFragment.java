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

package com.xuexiang.mqttdemo.fragment;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xuexiang.mqttdemo.R;
import com.xuexiang.mqttdemo.core.BaseFragment;
import com.xuexiang.mqttdemo.location.GetLocation;
import com.xuexiang.mqttdemo.utils.MMKVUtils;
import com.xuexiang.mqttdemo.utils.XToastUtils;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpush.mqtt.agent.entity.MqttOptions;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xutil.common.StringUtils;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;

/**
 * @author xuexiang
 * @since 2019-07-08 00:52
 */
@Page(name = "获取位置信息\n获取手机经纬度信息")
public class LocationFragment extends BaseFragment {

    private Location mLocation;

    @BindView(R.id.location)
    TextView locationText;


    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_location;
    }


    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        return titleBar;
    }


    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
//       String str = GetLocation.getLocationIns().getLocation(getActivity());
//       if (str != null){
//           location.setText(str);
//       }
        getLocation();
    }

    private void getLocation()
    {
        //获取系统的LocationManager对象
        final LocationManager mLocationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        try
        {
            mLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            updateLocation(mLocation);
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, new LocationListener() {
                @Override
                public void onLocationChanged(Location location)
                {
                    updateLocation(location);
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
                        updateLocation(mLocationManager.getLastKnownLocation(provider));
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
        }
    }

    private void updateLocation(Location location)
    {
        if (location != null)
        {
            StringBuffer sb = new StringBuffer();
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
            locationText.setText(sb.toString());
        }
    }


}
