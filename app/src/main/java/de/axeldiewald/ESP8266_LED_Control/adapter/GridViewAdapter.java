package de.axeldiewald.ESP8266_LED_Control.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.axeldiewald.ESP8266_LED_Control.ColorBundle;
import de.axeldiewald.ESP8266_LED_Control.FavouriteButtonView;
import de.axeldiewald.ESP8266_LED_Control.R;

/**
 * Created by Axel on 28.08.2015.
 */
public class GridViewAdapter<ColorBundleClass> extends ArrayAdapter<ColorBundle> {

    private LayoutInflater mInflater;
    private int mResource;
    List<ColorBundle> items = new ArrayList<>();

    public GridViewAdapter(Context context, int layoutResourceId, List<ColorBundle> objects) {
        super(context, layoutResourceId, objects);
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mResource = R.layout.button_favourite;
        items = objects;
    }

    public void addButton(ColorBundle colorBundle){
        items.add(colorBundle);
        notifyDataSetChanged();
        Toast.makeText(getContext(), "Saved as Favourite", Toast.LENGTH_SHORT).show();
    }

    public void removeButton(ColorBundle colorBundle){
        items.remove(colorBundle);
        notifyDataSetChanged();
        Toast.makeText(getContext(), "Favourite deleted", Toast.LENGTH_SHORT).show();
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = mInflater.inflate(mResource, parent, false);
        }
        final ColorBundle colorBundleInst = items.get(position);
        FavouriteButtonView button = (FavouriteButtonView)convertView.findViewById(R.id.favouritebutton);
        button.setText(colorBundleInst.getName());
        button.setBackgroundColor(Color.rgb(colorBundleInst.redValue, colorBundleInst.greenValue, colorBundleInst.blueValue));
        button.setOnClickListener(colorBundleInst.clickListener);
        button.setLongClickable(true);
        return convertView;
    }
}