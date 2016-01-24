package de.axeldiewald.ESP8266_LED_Control.bundle;

import android.content.Context;

public class AlarmBundle extends ParentBundle {

    public Integer hour, minute, second;
    public String name;
    public int id;

    public AlarmBundle(Context context, Integer pHour, Integer pMinute, Integer pSecond){
        super(context, new int[]{pHour, pMinute, pSecond}, "setalarm");

        hour = pHour;
        minute = pMinute;
        second = pSecond;
    }

}
