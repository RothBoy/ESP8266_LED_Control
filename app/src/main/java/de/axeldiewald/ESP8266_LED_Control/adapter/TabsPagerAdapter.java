package de.axeldiewald.ESP8266_LED_Control.adapter;


import de.axeldiewald.ESP8266_LED_Control.fragment.AlarmFragment;
import de.axeldiewald.ESP8266_LED_Control.fragment.FavouriteFragment;
import de.axeldiewald.ESP8266_LED_Control.fragment.CustomizeFragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
    CustomizeFragment customizeFragment;
    FavouriteFragment favouriteFragment;
    AlarmFragment alarmFragment;


    // Build a Constructor and assign the passed Values to appropriate values in the class
    public TabsPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);
        customizeFragment = new CustomizeFragment();
        favouriteFragment = new FavouriteFragment();
        alarmFragment = new AlarmFragment();

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Customize fragment activity
                return customizeFragment;
            case 1:
                // Favourite fragment activity
                return favouriteFragment;
            case 2:
                // Customize fragment activity
                return alarmFragment;
        }
        return null;
    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }

}
