package de.axeldiewald.ESP8266_LED_Control.fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

import de.axeldiewald.ESP8266_LED_Control.HttpGetRequest;
import de.axeldiewald.ESP8266_LED_Control.bundle.ColorBundle;
import de.axeldiewald.ESP8266_LED_Control.R;

public class FavouriteFragment extends BundleFragment {

    public GridView gridView;
    public int fragmentLayoutResource = R.layout.fragment_favourite;
    public int gridViewId = R.id.favouritegridview;

    public FavouriteFragment() {
        super();
        FRAGMENT_GROUP_ID = 1;
        buttonResource = R.layout.button_favourite;
        buttonId = R.id.buttonFavourite;
        TABLE_NAME = "FavouritesTable";
        BUNDLE_CLASSNAME = "Favourite";
        BUNDLE_VALUE_NAME = new String[] {"red", "green", "blue"};
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Inflate the layout for this fragment
        View view = inflater.inflate(fragmentLayoutResource, container, false);
        gridView = (GridView)view.findViewById(gridViewId);
        gridView.setAdapter(viewAdapter);
        // register the GridView for ContextMenu
        registerForContextMenu(gridView);

        //View view = inflater.inflate(fragmentLayoutResource, container, false);
        Button ButtonTurnOff;
        ButtonTurnOff = (Button) view.findViewById(R.id.buttonTurnOff);
        ButtonTurnOff.setOnClickListener(buttonTurnOffClickHandler);

        return view;
    }

    View.OnClickListener buttonTurnOffClickHandler = new View.OnClickListener() {
        public void onClick(View view) {
            int[] args = {};
            String path = "unsetpwm";
            new HttpGetRequest(view.getContext(), args, path).execute();
            // TODO unhighlight the favourite set
        }
    };


}
