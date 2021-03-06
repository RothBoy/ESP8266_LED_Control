package de.axeldiewald.ESP8266_LED_Control.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import java.util.ArrayList;
import de.axeldiewald.ESP8266_LED_Control.bundle.AlarmBundle;
import de.axeldiewald.ESP8266_LED_Control.bundle.ColorBundle;
import de.axeldiewald.ESP8266_LED_Control.bundle.ParentBundle;

public class ViewAdapter<ParentBundleClass> extends ArrayAdapter<ParentBundle> {

    public static LayoutInflater mInflater;
    public int buttonResource;
    public int buttonId;
    ArrayList<ParentBundle> items = new ArrayList<>();
    public String BUNDLE_CLASSNAME;

    public ViewAdapter(Context context, int pButtonResource, int pButtonId, String pBundleClassname,
                       ArrayList<ParentBundle> objects) {
        super(context, pButtonResource, objects);
        buttonResource = pButtonResource;
        buttonId = pButtonId;
        BUNDLE_CLASSNAME = pBundleClassname;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        items = objects;
    }

    public void addButton(ParentBundle parentBundle){
        items.add(parentBundle);
        notifyDataSetChanged();
    }

    public void removeButton(ParentBundle pParentBundle){
        items.remove(pParentBundle);
        notifyDataSetChanged();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = mInflater.inflate(buttonResource, parent, false);
        }
        final ParentBundle parentBundleInst = items.get(position);
        Button button = (Button)convertView.findViewById(buttonId);
        button.setText(parentBundleInst.getName());
        if (parentBundleInst instanceof ColorBundle) {
            button.setBackgroundColor(Color.rgb(parentBundleInst.arg[0], parentBundleInst.arg[1],
                    parentBundleInst.arg[2]));
        }
        button.setOnClickListener(parentBundleInst.clickListener);
        button.setLongClickable(true);
        return convertView;
    }

    public void restoreBundles(Cursor cursor){
        if (cursor != null  && cursor.getCount() > 0 && cursor.moveToFirst()) {
            do {
                // create new Bundle with data from SQL Database
                ParentBundle restoredParentBundle = new ParentBundle(getContext(), new int[]{}, "");
                switch (BUNDLE_CLASSNAME){
                    case "Favourite":
                        restoredParentBundle = new ColorBundle(getContext(),
                                cursor.getInt(2), cursor.getInt(3), cursor.getInt(4));
                        break;
                    case "Alarm":
                        restoredParentBundle = new AlarmBundle(getContext(),
                                cursor.getInt(2), cursor.getInt(3), cursor.getInt(4));
                        break;
                    default:
                        break;
                }
                restoredParentBundle.setName(cursor.getString(1).trim());
                restoredParentBundle.setId(cursor.getInt(0));
                // add Button to GridView
                items.add(restoredParentBundle);
                notifyDataSetChanged();
            } while (cursor.moveToNext());
        }
    }
}