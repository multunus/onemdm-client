package com.multunus.onemdm;

import android.content.Context;

import com.multunus.onemdm.util.Logger;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;

/**
 * Created by leena on 04/03/16.
 */
public class MQTTConnector {

    private final Context context;

    private static MQTTConnector self = null;
    private MqttAndroidClient client;

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

        try {
            String clientId = MqttClient.generateClientId();
            Logger.debug("clientId "+clientId);
            client =
                    new MqttAndroidClient(context, "tcp://test.mosquitto.org:1883",
                            clientId);
            Logger.debug("client object created");
            IMqttToken token = client.connect();
            Logger.debug("client connected");
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Logger.debug(" MQTT.connect onSuccess");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Logger.warning(" MQTT.connect onFailure", exception);

                }
            });

        }
        catch (Exception ex){
            Logger.warning("exception while connecting to MQTT Broker",ex);
        }

    }

    public void publish(){
        try {
            IMqttDeliveryToken deliveryToken = client.publish("device/heartbeat", " heartbeat with MQTT".getBytes("UTF-8"), 0, true);
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
}
