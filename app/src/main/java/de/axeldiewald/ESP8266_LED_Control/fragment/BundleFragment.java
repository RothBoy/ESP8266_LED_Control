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
import android.widget.Toast;

import java.util.ArrayList;
import de.axeldiewald.ESP8266_LED_Control.SQLite.mySQLHelper;
import de.axeldiewald.ESP8266_LED_Control.adapter.ViewAdapter;
import de.axeldiewald.ESP8266_LED_Control.bundle.ParentBundle;

public class BundleFragment extends Fragment {

    public ViewAdapter viewAdapter;
    public ArrayList<ParentBundle> list = new ArrayList<>();
    public static mySQLHelper myDBHelper;
    public final int MENU_CONTEXT_DELETE_ID = 0;
    public int FRAGMENT_GROUP_ID;
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
        viewAdapter = new ViewAdapter(getContext(), buttonResource, buttonId, BUNDLE_CLASSNAME, list);
        restoreButtons();
    }

    public static void setSQLHelper(mySQLHelper pDBHelper){
        myDBHelper = pDBHelper;
    }

    public void addButton(ParentBundle parentBundleInst) {
        viewAdapter.addButton(parentBundleInst);
        // add to SQL Database
        long id = myDBHelper.createRecord(TABLE_NAME, parentBundleInst.getName(),
                BUNDLE_VALUE_NAME, parentBundleInst.arg);
        parentBundleInst.setId((int) id);
        // Toast as Confirmation
        Toast.makeText(getActivity().getApplicationContext(), "Saved as " + BUNDLE_CLASSNAME,
                Toast.LENGTH_SHORT).show();
    }

    public void removeButton(MenuItem item) {
        AdapterView.AdapterContextMenuInfo acmi =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        ParentBundle parentBundleInst = (ParentBundle) viewAdapter.getItem(acmi.position);
        viewAdapter.removeButton(parentBundleInst);
        // delete from SQL Database
        myDBHelper.deleteRecord(TABLE_NAME, parentBundleInst.getId());
        // Toast as Confirmation
        Toast.makeText(getActivity().getApplicationContext(), BUNDLE_CLASSNAME + " deleted",
                Toast.LENGTH_SHORT).show();
    }

    public void restoreButtons(){
        viewAdapter.restoreBundles(myDBHelper.getAllRecords(BUNDLE_CLASSNAME));
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
        ParentBundle parentBundleInst = (ParentBundle) viewAdapter.getItem(acmi.position);
        menu.setHeaderTitle(parentBundleInst.getName());
        menu.add(FRAGMENT_GROUP_ID, MENU_CONTEXT_DELETE_ID, Menu.NONE, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getGroupId() == FRAGMENT_GROUP_ID) {
            switch (item.getItemId()) {
                case MENU_CONTEXT_DELETE_ID:
                    removeButton(item);
                    return true;
                default:
                    return super.onContextItemSelected(item);
            }
        }
        return super.onContextItemSelected(item);
    }


}