package de.axeldiewald.ESP8266_LED_Control.fragment;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import java.util.ArrayList;

import de.axeldiewald.ESP8266_LED_Control.SQLite.mySQLHelper;
import de.axeldiewald.ESP8266_LED_Control.bundle.ColorBundle;
import de.axeldiewald.ESP8266_LED_Control.R;
import de.axeldiewald.ESP8266_LED_Control.adapter.GridViewAdapter;

public class FavouriteFragment extends Fragment {

    private static GridView gridView;
    private static GridViewAdapter gridViewAdapter;
    private static ArrayList<ColorBundle> favouriteList = new ArrayList<>();
    private static mySQLHelper myDBHelper;
    private static final int MENU_CONTEXT_DELETE_ID = 0;
    private static final int FRAGMENT_GROUP_ID = 1;


    public FavouriteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        gridViewAdapter = new GridViewAdapter(getActivity(), favouriteList);
        restoreFavouriteButtons();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Button ButtonTurnOff;
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        gridView = (GridView) view.findViewById(R.id.favouritegridview);
        gridView.setAdapter(gridViewAdapter);
        // register the GridView for ContextMenu
        registerForContextMenu(gridView);
        ButtonTurnOff = (Button) view.findViewById(R.id.buttonTurnOff);
        ButtonTurnOff.setOnClickListener(buttonTurnOffClickHandler);
        return view;
    }

    public void setSQLHelper(mySQLHelper pDBHelper){
        myDBHelper = pDBHelper;
    }

    View.OnClickListener buttonTurnOffClickHandler = new View.OnClickListener() {
        public void onClick(View view) {
            ColorBundle colorBundleInst = new ColorBundle(view.getContext(), 0, 0, 0);
            colorBundleInst.SendToLedStrip();
        }
    };

    public void addFavouriteButton(final ColorBundle colorBundleInst) {
        gridViewAdapter.addButton(colorBundleInst);
        // add to SQL Database
        long id = myDBHelper.createRecord("FavouritesTable",
                new String[]{"redValue", "greenValue", "blueValue"},
                colorBundleInst.getName(), colorBundleInst.arg);
        colorBundleInst.setId((int) id);
    }

    public void restoreFavouriteButtons(){
        Cursor cursor = myDBHelper.getAllRecords("Favourite");
        gridViewAdapter.restoreBundles(cursor);
        Log.w("RESTORED", "Bundles have been restored");
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
        ColorBundle colorBundleInst = (ColorBundle) gridViewAdapter.getItem(acmi.position);
        String title = colorBundleInst.getName();
        menu.setHeaderTitle(title);
        menu.add(FRAGMENT_GROUP_ID, MENU_CONTEXT_DELETE_ID, Menu.NONE, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getGroupId() == FRAGMENT_GROUP_ID) {
            switch (item.getItemId()) {
                case MENU_CONTEXT_DELETE_ID:
                    AdapterView.AdapterContextMenuInfo acmi =
                            (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                    ColorBundle colorBundleInst = (ColorBundle) gridViewAdapter.getItem(acmi.position);
                    gridViewAdapter.removeButton(colorBundleInst);
                    // delete from SQL Database
                    myDBHelper.deleteRecord("FavouritesTable", colorBundleInst.getId());
                    return true;
                default:
                    return super.onContextItemSelected(item);
            }
        }
        return super.onContextItemSelected(item);
    }


}
