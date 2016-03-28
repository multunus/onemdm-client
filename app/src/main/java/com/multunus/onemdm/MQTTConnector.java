package com.multunus.onemdm;

import android.content.Context;
import android.util.Log;

import com.multunus.onemdm.config.Config;
import com.multunus.onemdm.util.Helper;
import com.multunus.onemdm.util.Logger;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by leena on 04/03/16.
 */
public class MQTTConnector {

    private final Context context;

    private static MQTTConnector self = null;
    private MqttAndroidClient client;
    private IMqttToken token;

    private MQTTConnector(Context context){
        this.context = context;
    }

    public static MQTTConnector getInstance(Context context){
        if(self == null) {
            self = new MQTTConnector(context);
        }
        return self;
    }

    public void connect(){
        Logger.debug("connecting to MQTT Broker");
        String clientId = MqttClient.generateClientId();
        client =
                new MqttAndroidClient(context, Config.MQTT_HOST,
                        clientId);
        connectToMqttServer();
    }

    public void publish(){
        if (self.client != null && self.client.isConnected()) {
            self.dispatchMessage();
        } else {
            Logger.warning("MQTT.publish no connection available");
            connectToMqttServer();
        }
    }

    private void dispatchMessage() {
        try {
            String deviceId = Helper.getAndroidId(context);
            String timestamp = ((Long) System.currentTimeMillis()).toString();
            String data = "{\"deviceId\" : \""+ deviceId +"\", \"createdAt\" : \""+ timestamp +"\"}";
            IMqttDeliveryToken deliveryToken = client.publish("device/heartbeat", data.getBytes("UTF-8"), 0, true);
            deliveryToken.setActionCallback(new IMqttActionListener() {

                @Override
                public void onSuccess(IMqttToken iMqttToken) {
                    Logger.debug(" MQTT.publish onSuccess");
                    Logger.debug("client published");
                }

                @Override
                public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                    Logger.warning(" Exception while MQTT.publish ", throwable);
                }
            });

        } catch (Exception ex) {
            Logger.warning(" Exception while MQTT.publish ", ex);
        }
    }

    private void connectToMqttServer() {
        try {
            Logger.debug("mqtt client trying to connect");
            token = client.connect();
            Logger.debug("Mqtt pending tokens " + String.valueOf(client.getPendingDeliveryTokens().length));
        }
        catch (Exception ex){
            Logger.warning("exception while connecting to MQTT Broker",ex);
        }
    }
}
