package de.axeldiewald.ESP8266_LED_Control.bundle;

import android.content.Context;

import de.axeldiewald.ESP8266_LED_Control.helper.MqttHelper;

public class ColorBundle extends ParentBundle {

    public Integer red, green, blue;

    public ColorBundle(Context context, Integer pRed, Integer pGreen, Integer pBlue){
        super(context, new int[]{pRed, pGreen, pBlue}, MqttHelper.MQTT_TOPIC_PWM_COMMAND);

        red = pRed;
        green = pGreen;
        blue = pBlue;
    }

}
