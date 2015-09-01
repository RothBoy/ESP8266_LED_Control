package de.axeldiewald.ESP8266_LED_Control;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;

import de.axeldiewald.ESP8266_LED_Control.adapter.ButtonAdapter;

public class Fragment1 extends Fragment {

    private static GridView gridView;
    private static ButtonAdapter buttonAdapter;
    private static ArrayList<ColorBundle> myList = new ArrayList<>();

    public Fragment1() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_1, container, false);
        gridView = (GridView) view.findViewById(R.id.fragment1gridview);
        buttonAdapter = new ButtonAdapter(getActivity(), R.layout.button_favourite, myList);
        gridView.setAdapter(buttonAdapter);
        return view;
    }

    public void addFavouriteButton(Context context, final ColorBundle colorBundleInst) {
        buttonAdapter.addButton(colorBundleInst);
    }


}
