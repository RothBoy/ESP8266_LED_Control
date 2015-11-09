package de.axeldiewald.ESP8266_LED_Control.adapter;

/**
 * Created by Axel on 27.07.2015.
 */


import de.axeldiewald.ESP8266_LED_Control.fragment.FavouriteFragment;
import de.axeldiewald.ESP8266_LED_Control.fragment.CustomizeFragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Favourite fragment activity
                return new FavouriteFragment();
            case 1:
                // Customize fragment activity
                return new CustomizeFragment();

        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 2;
    }

}