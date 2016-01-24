package de.axeldiewald.ESP8266_LED_Control.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;
import java.util.zip.Inflater;

import de.axeldiewald.ESP8266_LED_Control.FavouriteButtonView;
import de.axeldiewald.ESP8266_LED_Control.R;
import de.axeldiewald.ESP8266_LED_Control.bundle.ColorBundle;
import de.axeldiewald.ESP8266_LED_Control.bundle.ParentBundle;

public class GridViewAdapter<ColorBundleClass> extends ViewAdapter<ParentBundle> {

    private static int buttonResource = R.layout.button_favourite;
    private static int buttonId = R.id.favouritebutton;

    public GridViewAdapter(Context context, List<ParentBundle> objects) {
        super(context, buttonResource, buttonId, objects);
        BUNDLE_CLASSNAME = "Favourite";
    }
}