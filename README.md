# XPush-MQTT

MQTT在Android上的使用，目前已集成了[XPush](https://github.com/xuexiangjys/XPush)


## MQTT服务器

> 在运行Demo之前请先安装MQTT服务器，并在客户端中配置好服务器的地址和端口号。

* [ActiveMQ](http://activemq.apache.org/)

* [EMQ](https://www.emqx.io/)

## MQTT服务器配置

请在AndroidManifest.xml中修改你的服务器配置:

```
<meta-data
    android:name="MQTT_HOST"
    android:value="192.168.0.154" />
<meta-data
    android:name="MQTT_PORT"
    android:value="1883" />
```
