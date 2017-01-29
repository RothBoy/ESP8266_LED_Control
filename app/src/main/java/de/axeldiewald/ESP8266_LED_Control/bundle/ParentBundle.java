package de.axeldiewald.ESP8266_LED_Control.bundle;

import android.content.Context;
import android.view.View;

import de.axeldiewald.ESP8266_LED_Control.activity.MainActivity;
import de.axeldiewald.ESP8266_LED_Control.helper.MqttHelper;


public class ParentBundle {

    public int[] arg;
    public String path;
    public Context context;
    public String name;
    public int id;


    public ParentBundle(Context pContext, int[] pArgs, String pPath){
        context = pContext;
        arg = pArgs;
        path = pPath;
    }

    public void SendToLedStrip(){
        MainActivity.mqttHelper.publish(arg, path);
    }

    public void setName(String pName){
        name = pName;
    }

    public String getName(){
        return name;
    }

    public void setId(int pId){
        id = pId;
    }

    public int getId(){
        return id;
    }

    public View.OnClickListener clickListener = new View.OnClickListener(){
        public void onClick(View v) {
            SendToLedStrip();
        }
    };
}
