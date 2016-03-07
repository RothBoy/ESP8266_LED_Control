package de.axeldiewald.ESP8266_LED_Control.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import de.axeldiewald.ESP8266_LED_Control.HttpGetRequest;
import de.axeldiewald.ESP8266_LED_Control.R;


public class AlarmFragment extends BundleFragment {

    public ListView listView;
    public int fragmentLayoutResource = R.layout.fragment_alarm;
    public int listViewId = R.id.alarmlistview;

    public AlarmFragment() {
        super();
        FRAGMENT_GROUP_ID = 2;
        buttonResource = R.layout.button_alarm;
        buttonId = R.id.buttonAlarm;
        TABLE_NAME = "AlarmsTable";
        BUNDLE_CLASSNAME = "Alarm";
        BUNDLE_VALUE_NAME = new String[] {"hour", "minute", "second"};
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Inflate the layout for this fragment
        View view = inflater.inflate(fragmentLayoutResource, container, false);
        listView = (ListView)view.findViewById(listViewId);
        listView.setAdapter(viewAdapter);
        // register the GridView for ContextMenu
        registerForContextMenu(listView);

        //View view = inflater.inflate(fragmentLayoutResource, container, false);
        Button ButtonUnsetAlarm, ButtonNewAlarm, ButtonStopAlarm;
        ButtonUnsetAlarm = (Button) view.findViewById(R.id.buttonUnsetAlarm);
        ButtonUnsetAlarm.setOnClickListener(buttonUnsetAlarmClickHandler);
        ButtonNewAlarm = (Button) view.findViewById(R.id.buttonNewAlarm);
        ButtonNewAlarm.setOnClickListener(buttonNewAlarmClickHandler);
        ButtonStopAlarm = (Button) view.findViewById(R.id.buttonStopAlarm);
        ButtonStopAlarm.setOnClickListener(buttonStopAlarmClickHandler);

        return view;
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

    View.OnClickListener buttonStopAlarmClickHandler = new View.OnClickListener() {
        public void onClick(View view) {
            int[] args = {};
            String path = "stopalarm";
            new HttpGetRequest(view.getContext(), args, path).execute();
        }
    };

}
