package de.axeldiewald.ESP8266_LED_Control;

import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import de.axeldiewald.ESP8266_LED_Control.SQLite.mySQLHelper;
import de.axeldiewald.ESP8266_LED_Control.adapter.TabsPagerAdapter;
import de.axeldiewald.ESP8266_LED_Control.fragment.FavouriteFragment;
import de.axeldiewald.ESP8266_LED_Control.fragment.SaveFavouriteDialogFragment;

// TODO Special Effects (z.B. Strobe-Mode o.ä.)

public class MainActivity extends AppCompatActivity implements SaveFavouriteDialogFragment.SaveFavouriteDialogListener {

    // declare Handles
    private TabsPagerAdapter mAdapter;
    SlidingTabLayout tabLayout;
    // Tab titles
    private String[] tabs = {"Customize", "Favourites", "Extra"};
    // declare Settings
    SharedPreferences sharedPreferences;
    // declare SQLite Database
    mySQLHelper myDBHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewPager viewPager;

        // Handle Initialisation
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        mAdapter = new TabsPagerAdapter(getFragmentManager(), tabs, tabs.length);
        viewPager.setAdapter(mAdapter);
        tabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setDistributeEvenly(true);
        tabLayout.setViewPager(viewPager);
        viewPager.setCurrentItem(1); // set starting Page

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
    public void onDialogPositiveClick(DialogFragment dialog, ColorBundle colorBundleInst) {
        // get Instance of FavouriteFragment
        FavouriteFragment favouriteFragment = (FavouriteFragment) mAdapter.getItem(1);
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

