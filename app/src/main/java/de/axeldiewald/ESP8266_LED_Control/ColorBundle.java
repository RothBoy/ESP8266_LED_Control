package de.axeldiewald.ESP8266_LED_Control;

import android.content.Context;
import android.view.View;

/**
 * Created by Axel on 23.08.2015.
 */
public class ColorBundle {

    public Integer redValue, greenValue, blueValue;
    public String redValueString, greenValueString, blueValueString;
    private Context context;
    public String name;
    public int id;

    public ColorBundle(Context context, Integer red, Integer green, Integer blue){

        this.context = context;

        redValue = red;
        greenValue = green;
        blueValue = blue;
        redValueString = String.valueOf(red).trim();
        greenValueString = String.valueOf(green).trim();
        blueValueString = String.valueOf(blue).trim();
        // TODO Implement Method to set String length to 3
        String[] colorValues = {redValueString, greenValueString, blueValueString};
        int i = 0;
        for (String colorValue_name : colorValues) {
            switch(colorValue_name.length()){
                case 2:
                    colorValue_name = "0" + colorValue_name;
                    break;
                case 1:
                    colorValue_name = "00" + colorValue_name;
                    break;
                default:
                    break;
            }
            colorValues[i] = colorValue_name;
            i += 1;
        }
        redValueString = colorValues[0];
        greenValueString = colorValues[1];
        blueValueString = colorValues[2];
    }

    public void SendToLedStrip(){
        new HttpGetRequest(context, redValueString, greenValueString, blueValueString).execute();
    }

    public void setName(String pname){
        name = pname;
    }

    public String getName(){
        return name;
    }

    public View.OnClickListener clickListener = new View.OnClickListener(){
        public void onClick(View v) {
            SendToLedStrip();
        }
    };
}
