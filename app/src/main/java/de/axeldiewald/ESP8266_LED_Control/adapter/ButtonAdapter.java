package de.axeldiewald.ESP8266_LED_Control.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.axeldiewald.ESP8266_LED_Control.ColorBundle;
import de.axeldiewald.ESP8266_LED_Control.R;

/**
 * Created by Axel on 28.08.2015.
 */
public class ButtonAdapter<ColorBundleClass> extends ArrayAdapter<ColorBundle> {
    private LayoutInflater mInflater;
    private int mResource;
    List<ColorBundle> items = new ArrayList<>();

    public ButtonAdapter(Context context, int layoutResourceId, List<ColorBundle> objects) {
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

    /*
    // whenever you need to set the list of items just use this method.
    // call it when you have the data ready and want to display it
    public void setModel(List<T> items){
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items!=null?items.size():0;
    }
    @Override
    public T getItem(int position) {
        return items!=null?items.get(position):null;
    }
    /*@Override
    public long getItemId(int position) {
        return items!=null?items.get(position).id:-1;
    }*/

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = mInflater.inflate(mResource, parent, false);
        }
        final ColorBundle colorBundleInst = items.get(position);
        Button button = (Button)convertView.findViewById(R.id.favouritebutton);
        button.setText(colorBundleInst.getName());
        button.setBackgroundColor(Color.rgb(colorBundleInst.redValue, colorBundleInst.greenValue, colorBundleInst.blueValue));
        button.setOnClickListener(colorBundleInst.clickListener);
        return convertView;
    }
}