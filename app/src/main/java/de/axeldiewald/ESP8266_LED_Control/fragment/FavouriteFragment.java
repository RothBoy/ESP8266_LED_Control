package de.axeldiewald.ESP8266_LED_Control.fragment;


import android.os.Bundle;
import android.app.Fragment;
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

import de.axeldiewald.ESP8266_LED_Control.ColorBundle;
import de.axeldiewald.ESP8266_LED_Control.R;
import de.axeldiewald.ESP8266_LED_Control.adapter.GridViewAdapter;

public class FavouriteFragment extends Fragment {

    private static GridView gridView;
    private static GridViewAdapter gridViewAdapter;
    private static ArrayList<ColorBundle> myList = new ArrayList<>();
    private static final int MENU_CONTEXT_DELETE_ID = 0;


    public FavouriteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Button ButtonTurnOff;
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        gridView = (GridView) view.findViewById(R.id.favouritegridview);
        gridViewAdapter = new GridViewAdapter(getActivity(), R.layout.button_favourite, myList);
        gridView.setAdapter(gridViewAdapter);
        // register the GridView for ContextMenu
        registerForContextMenu(gridView);
        ButtonTurnOff = (Button) view.findViewById(R.id.buttonTurnOff);
        ButtonTurnOff.setOnClickListener(buttonTurnOffClickHandler);
        return view;
    }

    View.OnClickListener buttonTurnOffClickHandler = new View.OnClickListener() {
        public void onClick(View view) {
            ColorBundle colorBundleInst = new ColorBundle(view.getContext(), 0, 0, 0);
            colorBundleInst.SendToLedStrip();
        }
    };

    public void addFavouriteButton(final ColorBundle colorBundleInst) {
        gridViewAdapter.addButton(colorBundleInst);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
        ColorBundle colorBundleInst = (ColorBundle) gridViewAdapter.getItem(acmi.position);
        String title = colorBundleInst.getName();
        menu.setHeaderTitle(title);
        menu.add(Menu.NONE, MENU_CONTEXT_DELETE_ID, Menu.NONE, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_CONTEXT_DELETE_ID:
                AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                ColorBundle colorBundleInst = (ColorBundle) gridViewAdapter.getItem(acmi.position);
                gridViewAdapter.removeButton(colorBundleInst);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


}
