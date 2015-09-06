package de.axeldiewald.ESP8266_LED_Control;


import android.graphics.Color;
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
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        gridView = (GridView) view.findViewById(R.id.favouritegridview);
        gridViewAdapter = new GridViewAdapter(getActivity(), R.layout.button_favourite, myList);
        gridView.setAdapter(gridViewAdapter);
        // register the GridView for ContextMenu
        registerForContextMenu(gridView);
        return view;
    }

    public void addFavouriteButton(final ColorBundle colorBundleInst) {
        gridViewAdapter.addButton(colorBundleInst);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
       /*if (v.getId() == R.id.favouritegridview){
            GridView gv = (GridView) v;
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
            GridViewAdapter gva = (GridViewAdapter) gv.getAdapter();
            ColorBundle colorBundleInst = (ColorBundle) gv.getItemAtPosition(acmi.position);
        }*/
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
