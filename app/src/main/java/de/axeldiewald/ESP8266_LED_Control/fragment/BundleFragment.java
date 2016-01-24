package de.axeldiewald.ESP8266_LED_Control.fragment;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;

import de.axeldiewald.ESP8266_LED_Control.R;
import de.axeldiewald.ESP8266_LED_Control.SQLite.mySQLHelper;
import de.axeldiewald.ESP8266_LED_Control.adapter.ViewAdapter;
import de.axeldiewald.ESP8266_LED_Control.bundle.ColorBundle;
import de.axeldiewald.ESP8266_LED_Control.bundle.ParentBundle;

public class BundleFragment extends Fragment {

    public GridView gridView;
    public ListView listView;
    public AbsListView absListView;
    public ViewAdapter viewAdapter;
    public ArrayList<ParentBundle> list = new ArrayList<>();
    public mySQLHelper myDBHelper;
    public final int MENU_CONTEXT_DELETE_ID = 0;
    public int FRAGMENT_GROUP_ID = 1;
    public int fragmentLayoutResource;
    public int buttonResource;
    public int buttonId;
    public String TABLE_NAME;
    public String BUNDLE_CLASSNAME;
    public String[] BUNDLE_VALUE_NAME;


    public BundleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewAdapter = new ViewAdapter(getActivity(), buttonResource, buttonId, list);
        restoreButtons();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Button ButtonTurnOff;
        // Inflate the layout for this fragment
        View view = inflater.inflate(fragmentLayoutResource, container, false);
        absListView = (AbsListView)view.findViewById(R.id.favouritegridview);
        absListView.setAdapter(viewAdapter);
        // register the GridView for ContextMenu
        registerForContextMenu(absListView);
        ButtonTurnOff = (Button) view.findViewById(R.id.buttonTurnOff);
        ButtonTurnOff.setOnClickListener(buttonTurnOffClickHandler);
        return view;
    }

    public void setSQLHelper(mySQLHelper pDBHelper){
        myDBHelper = pDBHelper;
    }

    View.OnClickListener buttonTurnOffClickHandler = new View.OnClickListener() {
        public void onClick(View view) {
            ColorBundle colorBundleInst = new ColorBundle(view.getContext(), 0, 0, 0);
            colorBundleInst.SendToLedStrip();
        }
    };

    public void addButton(final ParentBundle parentBundleInst) {
        viewAdapter.addButton(parentBundleInst);
        // add to SQL Database
        long id = myDBHelper.createRecord(TABLE_NAME, BUNDLE_VALUE_NAME,
                parentBundleInst.getName(), parentBundleInst.arg);
        parentBundleInst.setId((int) id);
    }

    public void restoreButtons(){
        Cursor cursor = myDBHelper.getAllRecords(BUNDLE_CLASSNAME);
        viewAdapter.restoreBundles(cursor);
        Log.w("RESTORED", "Bundles have been restored");
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
        ParentBundle parentBundleInst = (ParentBundle) viewAdapter.getItem(acmi.position);
        String title = parentBundleInst.getName();
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
                    ParentBundle parentBundleInst = (ParentBundle) viewAdapter.getItem(acmi.position);
                    viewAdapter.removeButton(parentBundleInst);
                    // delete from SQL Database
                    myDBHelper.deleteRecord(TABLE_NAME, parentBundleInst.getId());
                    return true;
                default:
                    return super.onContextItemSelected(item);
            }
        }
        return super.onContextItemSelected(item);
    }


}