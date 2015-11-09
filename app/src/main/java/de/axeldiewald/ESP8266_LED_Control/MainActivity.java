package de.axeldiewald.ESP8266_LED_Control;

import android.app.ActionBar;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import de.axeldiewald.ESP8266_LED_Control.SQLite.mySQLHelper;
import de.axeldiewald.ESP8266_LED_Control.adapter.TabsPagerAdapter;
import de.axeldiewald.ESP8266_LED_Control.fragment.FavouriteFragment;
import de.axeldiewald.ESP8266_LED_Control.fragment.SaveFavouriteDialogFragment;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener, SaveFavouriteDialogFragment.SaveFavouriteDialogListener {

    // declare Handles
    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    // Tab titles
    private String[] tabs = { "Favourites", "Customize"};
    // declare Settings
    SharedPreferences sharedPreferences;
    // declare SQLite Database
    mySQLHelper myDBHelper;

    // TODO Check capability of landscape mode


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Handle Initialisation
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        mAdapter = new TabsPagerAdapter(getFragmentManager());

        viewPager.setAdapter(mAdapter);
        //actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Adding Tabs
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }

        // on swiping the viewpager make respective tab selected
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        // load Settings
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        // create Writable Database
        myDBHelper = new mySQLHelper(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }


    @Override
    public void onDialogPositiveClick(DialogFragment dialog, ColorBundle colorBundleInst) {
        // get Instance of FavouriteFragment
        FavouriteFragment favouriteFragment = (FavouriteFragment) mAdapter.getItem(0);
        // add Button to FavouriteFragment
        favouriteFragment.addFavouriteButton(colorBundleInst);
        // add to SQL Database
        long id = myDBHelper.createRecord(colorBundleInst.getName(),
                colorBundleInst.redValue, colorBundleInst.greenValue, colorBundleInst.blueValue);
        colorBundleInst.setId((int) id);
        // Toast as Confirmation
        Toast.makeText(this, "Saved as Favourite", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
    }

    @Override
    protected void onStop() {
        super.onStop();
        myDBHelper.close();
        Log.w(mySQLHelper.class.getName(), "Database has been closed");
    }

}

