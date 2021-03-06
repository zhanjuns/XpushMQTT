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

package com.xuexiang.xpush.mqtt.agent;

import android.content.Context;
import android.text.TextUtils;

import com.xuexiang.xpush.mqtt.agent.entity.MqttOptions;
import com.xuexiang.xpush.mqtt.core.MqttCore;
import com.xuexiang.xpush.mqtt.core.callback.OnMqttActionListener;
import com.xuexiang.xpush.mqtt.core.callback.OnMqttEventListener;
import com.xuexiang.xpush.mqtt.core.entity.MqttAction;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

import java.util.Set;

/**
 * mqtt 推送代理
 *
 * @author xuexiang
 * @since 2019-12-15 22:41
 */
public final class MqttPushAgent {

    private static volatile MqttPushAgent sInstance = null;

    /**
     * MQTT核心类
     */
    private MqttCore mMqttCore;

    private Context mContext;

    private MqttPushAgent() {

    }

    /**
     * 获取单例
     *
     * @return
     */
    public static MqttPushAgent getInstance() {
        if (sInstance == null) {
            synchronized (MqttPushAgent.class) {
                if (sInstance == null) {
                    sInstance = new MqttPushAgent();
                }
            }
        }
        return sInstance;
    }


    /**
     * 初始化
     *
     * @param builder
     */
    public void init(MqttCore.Builder builder) {
        mMqttCore = builder.build();
        mContext = mMqttCore.getContext();
        MqttPersistence.init(mContext);
    }

    /**
     * 初始化
     *
     * @param mqttCore
     */
    public void init(MqttCore mqttCore) {
        mMqttCore = mqttCore;
        mContext = mMqttCore.getContext();
        MqttPersistence.init(mContext);
    }

    /**
     * 初始化
     *
     * @param context
     */
//    public void init(Context context, String host, int port) {
    public void init(Context context) {
        mContext = context.getApplicationContext();
        MqttPersistence.init(mContext);
//        MqttPersistence.setServerHost(host);
//        MqttPersistence.setServerPort(port);

    }

    /**
     * @return 是否初始化过
     */
    public static boolean isInitialized() {
        return getInstance().mMqttCore != null;
    }

    /**
     * 注册
     *
     * @param onMqttActionListener 动作监听器
     */
    public boolean register(OnMqttActionListener onMqttActionListener) {
        if (mMqttCore == null) {
            if (TextUtils.isEmpty(MqttPersistence.getServerHost())) {
                return false;
//                throw new IllegalArgumentException("Mqtt push host is not init," +
//                        "please call MqttPushAgent.getInstance().init to set host.");
            }
            MqttOptions options = MqttPersistence.getMqttOptions();
            mMqttCore = buildMqttCoreByOption(options);
            //订阅信息
            mMqttCore.setSubscriptions(options.getSubscriptions());
        }

        mMqttCore.setOnMqttActionListener(onMqttActionListener);

        return mMqttCore.connect();
    }

    /**
     * 构建MqttCore
     *
     * @param option
     * @return
     */
    private MqttCore buildMqttCoreByOption(MqttOptions option) {
        return MqttCore.Builder(getContext(), option.getHost())
                .setClientId(option.getClientId())
//                .setClientId("D199688r3VH7")
                .setPort(option.getPort())
                .setUserName(option.getUserName())
                .setPassWord(option.getPassword())
                .setTimeout(option.getTimeout())
                .setKeepAlive(option.getKeepAlive())
                .setAutomaticReconnect(true)
                .build();
    }

    /**
     * 注销
     */
    public boolean unRegister() {
        if (mMqttCore != null) {
            if (mMqttCore.isConnected()) {
                mMqttCore.disconnect(new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        mMqttCore.onActionSuccess(MqttAction.DISCONNECT, asyncActionToken);
                        mMqttCore.setOnMqttActionListener(null);
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        mMqttCore.onActionFailure(MqttAction.DISCONNECT, asyncActionToken, exception);
                        mMqttCore.setOnMqttActionListener(null);
                    }
                });
                return true;
            }
        }
        return false;
    }


    /**
     * 增加标签
     *
     * @param tags
     */
    public boolean addTags(String... tags) {
        if (isMqttReady()) {
            if (tags == null || tags.length == 0) {
                return false;
            }
            for (String tag : tags) {
                mMqttCore.registerSubscription(tag);
            }
            return true;
        }
        return false;
    }

    public boolean isMqttReady() {
        return mMqttCore != null && mMqttCore.isConnected();
    }

    /**
     * 删除标签
     *
     * @param tags
     */
    public boolean deleteTags(String... tags) {
        if (isMqttReady()) {
            if (tags == null || tags.length == 0) {
                return false;
            }
            for (String tag : tags) {
                mMqttCore.unregisterSubscription(tag);
            }
            return true;
        }
        return false;
    }

    /**
     * 更新标签信息
     */
    public void updateTags() {
        if (mMqttCore != null) {
            MqttPersistence.setSubscriptions(mMqttCore.getSubscriptionList());
        }
    }

    /**
     * @return 标签信息
     */
    public Set<String> getTags() {
        if (mMqttCore != null) {
            return MqttPersistence.getSubscriptionSet(mMqttCore.getSubscriptionList());
        } else {
            return MqttPersistence.getSubscriptionSet();
        }
    }


    /**
     * 设置MQTT事件监听器
     *
     * @param listener
     * @return
     */
    public MqttPushAgent setOnMqttEventListener(OnMqttEventListener listener) {
        if (mMqttCore != null) {
            mMqttCore.setOnMqttEventListener(listener);
        }
        return this;
    }


    /**
     * 设置MQTT动作监听器
     *
     * @param onMqttActionListener
     * @return
     */
    public MqttPushAgent setOnMqttActionListener(OnMqttActionListener onMqttActionListener) {
        if (mMqttCore != null) {
            mMqttCore.setOnMqttActionListener(onMqttActionListener);
        }
        return this;
    }

    public static Context getContext() {
        return getInstance().mContext;
    }

    public MqttCore getMqttCore() {
        return mMqttCore;
    }

    /**
     * 更新Token信息
     */
    public void updateToken() {
        if (mMqttCore != null) {
            MqttPersistence.setClientId(mMqttCore.getClientId());
        }
    }

    public static void savePushToken(String token) {
        MqttPersistence.setClientId(token);
    }

    public static String getPushToken() {
        return MqttPersistence.getClientId();
    }

}
