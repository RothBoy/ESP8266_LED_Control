package de.axeldiewald.ESP8266_LED_Control.bundle;

import android.content.Context;

public class ColorBundle extends ParentBundle {

    public Integer red, green, blue;

    public ColorBundle(Context context, Integer pRed, Integer pGreen, Integer pBlue){
        super(context, new int[]{pRed, pGreen, pBlue}, "setpwm");

        red = pRed;
        green = pGreen;
        blue = pBlue;
    }

}
