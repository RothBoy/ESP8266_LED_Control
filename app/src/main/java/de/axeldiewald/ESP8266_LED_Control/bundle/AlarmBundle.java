package de.axeldiewald.ESP8266_LED_Control.bundle;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.preference.PreferenceManager;
import android.view.View;

import de.axeldiewald.ESP8266_LED_Control.HttpGetRequest;
import de.axeldiewald.ESP8266_LED_Control.activity.SettingsActivity;

public class AlarmBundle extends ParentBundle {

    public Integer hour, minute, second;
    public String name;
    public int id;
    // Declare Settings
    SharedPreferences sharedPreferences;

    public AlarmBundle(Context context, Integer pHour, Integer pMinute, Integer pSecond){
        super(context, new int[]{pHour, pMinute, pSecond}, "setalarm");

        hour = pHour;
        minute = pMinute;
        second = pSecond;

        clickListener = new View.OnClickListener(){
            public void onClick(View v) {
                // TODO make sure it remains highlighted when switching tabs

                /*Button alarmButton = (Button) v;
                alarmButton.setBackgroundResource(R.color.colorAlarmSet);*/
                GradientDrawable gd = new GradientDrawable();
                /*gd.setColor(0xFF19edff);
                gd.setCornerRadius(50);*/
                gd.setStroke(10, 0xFFFFFFFF);
                v.setBackground(gd);

                SendToLedStrip();
            }
        };
    }

    @Override
    public void SendToLedStrip(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(
                context.getApplicationContext());
        int wakeDuration = Integer.parseInt(
                sharedPreferences.getString(SettingsActivity.PREF_WAKE, "15"));
        int[] newArg = {0, 0, wakeDuration};
        System.arraycopy(arg, 0, newArg, 0, 2);
        new HttpGetRequest(context, newArg, path).execute();
    }
}
