package de.axeldiewald.ESP8266_LED_Control.helper;

import android.content.Context;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

import static de.axeldiewald.ESP8266_LED_Control.activity.MainActivity.mqttHelper;

public class MqttHelper implements MqttCallback {

    public static final String MQTT_TOPIC_PWM_COMMAND = "/lights/ledstrip/pwm/command";
    public static final String MQTT_TOPIC_PWM_STATE = "/lights/ledstrip/pwm/state";
    public static final String MQTT_TOPIC_ALARM_COMMAND = "/lights/ledstrip/alarm/command";
    public static final String MQTT_TOPIC_ALARM_STATE = "/lights/ledstrip/alarm/state";

    public static final String MQTT_BROKER_IP = "tcp://192.168.178.34:1883";

    private MqttAndroidClient mqttClient;
    // declare variables needed
    private Context context;
    private Toast toast;
    private String[] values;

    public MqttHelper(Context context) {
        this.context = context;
        toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
    }

    public void connect() {
        String clientId = MqttClient.generateClientId();
        mqttClient =
                new MqttAndroidClient(context.getApplicationContext(), MQTT_BROKER_IP, clientId);

        try {
            IMqttToken token = mqttClient.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    mqttHelper.subscribe();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void subscribe() {
        mqttClient.setCallback(MqttHelper.this);
        int qos = 1;
        String[] topics = new String[] {MQTT_TOPIC_PWM_STATE, MQTT_TOPIC_ALARM_STATE};
        for (String topic : topics) {
            try {
                IMqttToken subToken = mqttClient.subscribe(topic, qos);
                subToken.setActionCallback(new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken,
                                          Throwable exception) {
                    }
                });
            } catch (MqttException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    public void publish(String[] pArgs, String topic) {
        String message = "";
        for (String value : pArgs) {
            message += value + ";";
        }
        message = message.substring(0, message.length() - 1);

        byte[] encodedPayload = new byte[0];
        try {
            encodedPayload = message.getBytes("UTF-8");
            MqttMessage mqttMessage = new MqttMessage(encodedPayload);
            mqttClient.publish(topic, mqttMessage);
        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
        }
    }

    public void publish(int[] pArgs, String topic) {
        values = new String[pArgs.length];
        for (int i = 0; i < pArgs.length; i++) {
            values[i] = String.valueOf(pArgs[i]).trim();
        }
        publish(values, topic);
    }

    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        toast.setText(topic + ": " + message.toString());
        toast.show();
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

}