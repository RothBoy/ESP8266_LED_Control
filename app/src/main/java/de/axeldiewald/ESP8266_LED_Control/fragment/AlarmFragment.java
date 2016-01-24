package de.axeldiewald.ESP8266_LED_Control.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;

import de.axeldiewald.ESP8266_LED_Control.HttpGetRequest;
import de.axeldiewald.ESP8266_LED_Control.SQLite.mySQLHelper;
import de.axeldiewald.ESP8266_LED_Control.adapter.GridViewAdapter;
import de.axeldiewald.ESP8266_LED_Control.adapter.ListViewAdapter;
import de.axeldiewald.ESP8266_LED_Control.adapter.ViewAdapter;
import de.axeldiewald.ESP8266_LED_Control.bundle.AlarmBundle;
import de.axeldiewald.ESP8266_LED_Control.R;
import de.axeldiewald.ESP8266_LED_Control.bundle.ColorBundle;


public class AlarmFragment extends Fragment {

    private static ListView listView;
    private static ListViewAdapter listViewAdapter;
    private static ArrayList<AlarmBundle> alarmList = new ArrayList<>();
    private static mySQLHelper myDBHelper;
    private static final int MENU_CONTEXT_DELETE_ID = 0;
    private static final int FRAGMENT_GROUP_ID = 2;

    public AlarmFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listViewAdapter = new ListViewAdapter(getActivity(), alarmList);
        restoreAlarmButtons();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Button ButtonUnsetAlarm, ButtonNewAlarm;
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);

        ButtonUnsetAlarm = (Button) view.findViewById(R.id.buttonSetAlarm);
        ButtonUnsetAlarm.setOnClickListener(buttonUnsetAlarmClickHandler);
        ButtonNewAlarm = (Button) view.findViewById(R.id.buttonNewAlarm);
        ButtonNewAlarm.setOnClickListener(buttonNewAlarmClickHandler);

        listView = (ListView) view.findViewById(R.id.alarmlistview);
        listView.setAdapter(listViewAdapter);
        // register the GridView for ContextMenu
        registerForContextMenu(listView);

        return view;
    }

    public void setSQLHelper(mySQLHelper pDBhelper){
        myDBHelper = pDBhelper;
    }

    View.OnClickListener buttonNewAlarmClickHandler = new View.OnClickListener() {
        public void onClick(View view) {
            // Create Dialog for creating new Alarm
            NewAlarmDialogFragment dialog = new NewAlarmDialogFragment();
            dialog.show(getFragmentManager(), "NewAlarmDialog");
        }
    };

    View.OnClickListener buttonUnsetAlarmClickHandler = new View.OnClickListener() {
        public void onClick(View view) {
            int[] args = {};
            String path = "unsetalarm";
            new HttpGetRequest(view.getContext(), args, path).execute();
            // TODO unhighlight the alarm set
        }
    };

    public void addAlarmButton(final AlarmBundle alarmBundleInst) {
        listViewAdapter.addButton(alarmBundleInst);
        // add to SQL Database
        long id = myDBHelper.createRecord("AlarmsTable", new String[]{"hour", "minute", "second"},
                alarmBundleInst.getName(), alarmBundleInst.arg);
        alarmBundleInst.setId((int) id);
    }

    public void restoreAlarmButtons(){
        Cursor cursor = myDBHelper.getAllRecords("Alarm");
        listViewAdapter.restoreBundles(cursor);
        Log.w("RESTORED", "Bundles have been restored");
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
        AlarmBundle alarmBundleInst = (AlarmBundle) listViewAdapter.getItem(acmi.position);
        String title = alarmBundleInst.getName();
        menu.setHeaderTitle(title);
        menu.add(FRAGMENT_GROUP_ID, MENU_CONTEXT_DELETE_ID, Menu.NONE, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getGroupId() == FRAGMENT_GROUP_ID) {
            switch (item.getItemId()) {
                case MENU_CONTEXT_DELETE_ID:
                    AdapterView.AdapterContextMenuInfo acmi =
                            (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                    AlarmBundle alarmBundleInst = (AlarmBundle) listViewAdapter.getItem(acmi.position);
                    listViewAdapter.removeButton(alarmBundleInst);
                    // delete from SQL Database
                    myDBHelper.deleteRecord("AlarmsTable", alarmBundleInst.getId());
                    return true;
                default:
                    return super.onContextItemSelected(item);
            }
        }
        return super.onContextItemSelected(item);
    }
}
