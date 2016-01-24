package de.axeldiewald.ESP8266_LED_Control.bundle;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import de.axeldiewald.ESP8266_LED_Control.FavouriteButtonView;
import de.axeldiewald.ESP8266_LED_Control.HttpGetRequest;
import de.axeldiewald.ESP8266_LED_Control.R;

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
        //new HttpGetRequest(context, redValueString, greenValueString, blueValueString, path).execute();
        new HttpGetRequest(context, arg, path).execute();
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
            // TODO highlight the button clicked & make sure it remains highlighted when switching tabs
            if (!(v instanceof FavouriteButtonView)) {
                Button alarmButton = (Button) v;
                alarmButton.setBackgroundResource(R.color.colorAlarmSet);
            }
            SendToLedStrip();
        }
    };
}
