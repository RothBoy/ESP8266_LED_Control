package de.axeldiewald.ESP8266_LED_Control;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;


public class SaveFavouriteDialogFragment extends DialogFragment {

    ColorBundle colorBundleInst;
    // Use this instance of the interface to deliver action events
    SaveFavouriteDialogListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // set layout, text and ButtonActions of the dialog
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_savefavouritedialog, null);
        ImageView favouriteColor = (ImageView) view.findViewById(R.id.favouritecolor);
        favouriteColor.setBackgroundColor(Color.rgb(colorBundleInst.redValue, colorBundleInst.greenValue, colorBundleInst.blueValue));
        final EditText favouriteName = (EditText) view.findViewById(R.id.favouritename);
        builder
                .setView(view)
                .setMessage("Save your new Favourite")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        colorBundleInst.setName(favouriteName.getText().toString().trim());
                        mListener.onDialogPositiveClick(SaveFavouriteDialogFragment.this, colorBundleInst);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick(SaveFavouriteDialogFragment.this);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public interface SaveFavouriteDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, ColorBundle colorBundleInst);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (SaveFavouriteDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    public void setColorBundle(ColorBundle colorBundle){
        colorBundleInst = colorBundle;
    }

}