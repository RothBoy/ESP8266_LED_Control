package de.axeldiewald.ESP8266_LED_Control.activity;

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

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Arrays;

import de.axeldiewald.ESP8266_LED_Control.R;
import de.axeldiewald.ESP8266_LED_Control.helper.SqlHelper;
import de.axeldiewald.ESP8266_LED_Control.SlidingTabLayout;
import de.axeldiewald.ESP8266_LED_Control.adapter.TabsPagerAdapter;
import de.axeldiewald.ESP8266_LED_Control.bundle.AlarmBundle;
import de.axeldiewald.ESP8266_LED_Control.bundle.ColorBundle;
import de.axeldiewald.ESP8266_LED_Control.fragment.AlarmFragment;
import de.axeldiewald.ESP8266_LED_Control.fragment.BundleFragment;
import de.axeldiewald.ESP8266_LED_Control.fragment.FavouriteFragment;
import de.axeldiewald.ESP8266_LED_Control.fragment.NewAlarmDialogFragment;
import de.axeldiewald.ESP8266_LED_Control.fragment.SaveFavouriteDialogFragment;
import de.axeldiewald.ESP8266_LED_Control.helper.MqttHelper;

// TODO Parent Classes for DialogFragments

public class MainActivity extends AppCompatActivity implements
        SaveFavouriteDialogFragment.SaveFavouriteDialogListener,
        NewAlarmDialogFragment.NewAlarmDialogListener, MqttCallback{

    SlidingTabLayout tabLayout;
    // Tab titles
    private String[] tabs = {"Customize", "Favourites", "Alarms"};
    // declare Settings
    SharedPreferences sharedPreferences;
    // declare SQLite Database
    SqlHelper myDBHelper;

    FavouriteFragment favouriteFragment;
    AlarmFragment alarmFragment;

    public static MqttHelper mqttHelper; // TODO remove


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewPager viewPager;
        TabsPagerAdapter tabsPagerAdapter;

        // Tabs Initialisation
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabsPagerAdapter = new TabsPagerAdapter(getFragmentManager(), tabs, tabs.length);
        viewPager.setAdapter(tabsPagerAdapter);
        tabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setDistributeEvenly(true);
        tabLayout.setViewPager(viewPager);
        viewPager.setCurrentItem(Arrays.asList(tabs).indexOf("Favourites")); // set starting Page

        // load Settings
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // get instances of Fragments
        favouriteFragment = (FavouriteFragment)
                tabsPagerAdapter.getItem(Arrays.asList(tabs).indexOf("Favourites"));
        alarmFragment = (AlarmFragment)
                tabsPagerAdapter.getItem(Arrays.asList(tabs).indexOf("Alarms"));

        // SQLite Initialisation
        myDBHelper = new SqlHelper(this);
        BundleFragment.setSQLHelper(myDBHelper);

        mqttHelper = new MqttHelper(getApplicationContext());
        mqttHelper.connect();

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
    public void onSaveFavouriteDialogPositiveClick(DialogFragment dialog, ColorBundle colorBundleInst) {
        // add Button to FavouriteFragment
        favouriteFragment.addButton(colorBundleInst);
    }

    @Override
    public void onSaveFavouriteDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
    }

    @Override
    public void onNewAlarmDialogPositiveClick(DialogFragment dialog, AlarmBundle alarmBundleInst){
        // add Button to AlarmFragment
        alarmFragment.addButton(alarmBundleInst);
    }

    @Override
    public void onNewAlarmDialogNegativeClick(DialogFragment dialog){
        // User touched the dialog's negative button
    }

    @Override
    protected void onStop() {
        super.onStop();
        myDBHelper.close();
        Log.w(SqlHelper.class.getName(), "Database has been closed");
    }

    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        Toast.makeText(MainActivity.this, topic + ": " + message.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

}

