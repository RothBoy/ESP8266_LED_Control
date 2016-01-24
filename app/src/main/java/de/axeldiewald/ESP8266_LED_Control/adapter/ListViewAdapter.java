package de.axeldiewald.ESP8266_LED_Control.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.axeldiewald.ESP8266_LED_Control.FavouriteButtonView;
import de.axeldiewald.ESP8266_LED_Control.R;
import de.axeldiewald.ESP8266_LED_Control.SQLite.mySQLHelper;
import de.axeldiewald.ESP8266_LED_Control.bundle.AlarmBundle;
import de.axeldiewald.ESP8266_LED_Control.bundle.ColorBundle;
import de.axeldiewald.ESP8266_LED_Control.bundle.ParentBundle;

public class ListViewAdapter<AlarmBundleClass> extends ViewAdapter<ParentBundle> {

    private static int buttonResource = R.layout.button_alarm;
    private static int buttonId = R.id.alarmbutton;

    public ListViewAdapter(Context context, List<ParentBundle> objects) {
        super(context, buttonResource, buttonId, objects);
        BUNDLE_CLASSNAME = "Alarm";
    }
}