package de.axeldiewald.ESP8266_LED_Control.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import de.axeldiewald.ESP8266_LED_Control.R;
import de.axeldiewald.ESP8266_LED_Control.bundle.AlarmBundle;


public class NewAlarmDialogFragment extends DialogFragment {

    NewAlarmDialogListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder alerDialogBuilder = new AlertDialog.Builder(getActivity());
        // set layout, text and ButtonActions of the dialog
        final View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_newalarmdialog, null);
        final TimePicker TimePickerAlarm = (TimePicker) view.findViewById(R.id.alarmtimepicker);
        final EditText alarmName = (EditText) view.findViewById(R.id.alarmname);
        alerDialogBuilder
                .setView(view)
                .setMessage("New Alarm")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AlarmBundle alarmBundleInst = new AlarmBundle(view.getContext(), TimePickerAlarm.getHour(), TimePickerAlarm.getMinute(), 0);
                        alarmBundleInst.setName(alarmName.getText().toString().trim()
                                + "   " + String.valueOf(TimePickerAlarm.getHour()).trim()
                                + ":"  + String.valueOf(TimePickerAlarm.getMinute()).trim());
                        mListener.onNewAlarmDialogPositiveClick(NewAlarmDialogFragment.this, alarmBundleInst);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onNewAlarmDialogNegativeClick(NewAlarmDialogFragment.this);
                    }
                });
        // Create the AlertDialog object and return it
        return alerDialogBuilder.create();
    }

    public interface NewAlarmDialogListener {
        public void onNewAlarmDialogPositiveClick(DialogFragment dialog, AlarmBundle alarmBundleInst);
        public void onNewAlarmDialogNegativeClick(DialogFragment dialog);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (NewAlarmDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    public void setAlarmBundle(AlarmBundle alarmBundle){

    }

}
