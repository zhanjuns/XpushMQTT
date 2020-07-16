/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
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

package com.xuexiang.mqttdemo.widget;

import android.content.Context;
import android.location.Location;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xuexiang.mqttdemo.ActionRequest;
import com.xuexiang.mqttdemo.R;
import com.xuexiang.mqttdemo.bean.Data;
import com.xuexiang.mqttdemo.bean.Devices;
import com.xuexiang.mqttdemo.bean.Services;
import com.xuexiang.mqttdemo.utils.LocationUtil;
import com.xuexiang.xpush.mqtt.agent.MqttPersistence;
import com.xuexiang.xpush.mqtt.core.entity.PublishMessage;
import com.xuexiang.xui.utils.KeyboardUtils;
import com.xuexiang.xui.widget.dialog.materialdialog.CustomMaterialDialog;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.xuexiang.mqttdemo.fragment.OperationFragment.TOPIC_PROTOBUF;

/**
 * @author xuexiang
 * @since 2019-12-15 23:29
 */
public class PublishDialog extends CustomMaterialDialog {

    private MaterialEditText metTopic;
    private MaterialEditText metMessage;

    private OnPublishListener mListener;

    /**
     * 构造窗体
     *
     * @param context
     */
    public PublishDialog(Context context, OnPublishListener listener) {
        super(context);
        mListener = listener;
    }

    @Override
    protected MaterialDialog.Builder getDialogBuilder(Context context) {
        return new MaterialDialog.Builder(context)
                .title("发布主题")
                .customView(R.layout.dialog_publish, false)
                .positiveText("发送")
                .onPositive((dialog, which) -> {
                    if (metTopic.validate() && metMessage.validate()) {
                        KeyboardUtils.forceCloseKeyboard(metMessage);


//                        MqttPersistence.getClientId()
                        if (mListener != null) {
                            String topic = metTopic.getEditValue();

                            if (topic.contains("datas")) {
                                ArrayList<Devices> devicesArrayList = new ArrayList<>();
                                ArrayList<Services> servicesArrayList = new ArrayList<>();

                                Data data = LocationUtil.getLocationUtil(context).getData();
                                //先创建Services
                                Services services = new Services(data, getEventTime(), "suibian");
                                servicesArrayList.add(services);

                                String deviceId = MqttPersistence.getClientId();
                                Devices devices = new Devices(deviceId, servicesArrayList);

                                devicesArrayList.add(devices);

                                Map map = new HashMap();
                                map.put("devices", devicesArrayList);

                                Gson gson = new GsonBuilder().create();
                                String mapJson = gson.toJson(map);

                                metMessage.setText(mapJson);
                            }

                            //D199688r3VH7
                            /*if ("/v1/devices/palm-PAD/datas".equals(topic)) {
                                metMessage.setText("{\"devices\": [{ \"deviceId\": \"D5943893zaecD\", \"services\":" +
                                        " [{ \"data\": { \"X\": \"1000\" }, \"eventTime\": \"20191023T173625Z\"," +
                                        "\"serviceId\":\"serviceName\"}] }] }");
                            }*/

                            if ("/v1/devices/palm-PAD/topo/update".equals(topic)) {
                                metMessage.setText("{ \"deviceStatuses\": [{ \"deviceId\": \"D5943893zaecD\", \"status\": \"ONLINE\" }], \"mid\": 9 }");
                            }

                            /*if ("/v1/devices/hw-simulate-test-02/datas".equals(topic)) {
                                metMessage.setText("{\"devices\": [{ \"deviceId\": \"D199688r3VH7\", \"services\":" +
                                        " [{ \"data\": { \"X\": \"1000\" }, \"eventTime\": \"20191023T173625Z\"," +
                                        "\"serviceId\":\"serviceName\"}] }] }");
                            }

                            if ("/v1/devices/YGtest01/datas".equals(topic)) {
                                metMessage.setText("{\"devices\": [{ \"deviceId\": \"D349686cWDvK\", \"services\":" +
                                        " [{ \"data\": { \"X\": \"1000\" }, \"eventTime\": \"20191023T173625Z\"," +
                                        "\"serviceId\":\"serviceName\"}] }] }");
                            }*/

                            if (TOPIC_PROTOBUF.equals(topic)) {
                                //发布主题的内容--上面判断如果主题为protobuf--则会又一个绑定
                                ActionRequest actionRequest = ActionRequest.newBuilder().setMessage(metMessage.getEditValue()).build();
                                mListener.onPublish(PublishMessage.wrap(topic, actionRequest.toByteArray()));
                            } else {
                                //主要是这条线路
                                mListener.onPublish(PublishMessage.wrap(topic, metMessage.getEditValue()));
                            }
                        }
                        dialog.dismiss();
                    }
                })
                .negativeText("取消")
                .onNegative((dialog, which) -> dialog.dismiss())
                .autoDismiss(false)
                .cancelable(false);
    }

    /**
     * 获取时间字符串
     *
     * @return
     */
    private String getEventTime() {
        SimpleDateFormat formatterY = new SimpleDateFormat("yyyyMMdd");
        Date curDate = new Date(System.currentTimeMillis());
        //获取当前时间
        String year = formatterY.format(curDate);
        SimpleDateFormat formatter = new SimpleDateFormat("HHmmss");
        //获取当前时间
        String hour = formatter.format(curDate);
        return year + "T" + hour + "Z";
    }


    @Override
    protected void initViews(Context context) {
        metTopic = findViewById(R.id.met_topic);
        metMessage = findViewById(R.id.met_message);
    }


    public interface OnPublishListener {

        void onPublish(PublishMessage message);
    }
}
