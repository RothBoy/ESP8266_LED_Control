package de.axeldiewald.esp8266_2.adapter;

/**
 * Created by Axel on 27.07.2015.
 */


import de.axeldiewald.esp8266_2.Fragment1;
import de.axeldiewald.esp8266_2.Fragment2;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Top Rated fragment activity
                return new Fragment1();
            case 1:
                // Games fragment activity
                return new Fragment2();

        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 2;
    }

}