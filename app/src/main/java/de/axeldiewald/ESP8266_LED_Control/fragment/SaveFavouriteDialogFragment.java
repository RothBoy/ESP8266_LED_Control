package de.axeldiewald.ESP8266_LED_Control.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import de.axeldiewald.ESP8266_LED_Control.bundle.ColorBundle;
import de.axeldiewald.ESP8266_LED_Control.R;


public class SaveFavouriteDialogFragment extends DialogFragment {

    ColorBundle colorBundleInst;
    SaveFavouriteDialogListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder alerDialogBuilder = new AlertDialog.Builder(getActivity());
        // set layout, text and ButtonActions of the dialog
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_savefavouritedialog, null);
        ImageView favouriteColor = (ImageView) view.findViewById(R.id.favouritecolor);
        favouriteColor.setBackgroundColor(Color.rgb(colorBundleInst.red, colorBundleInst.green, colorBundleInst.blue));
        final EditText favouriteName = (EditText) view.findViewById(R.id.favouritename);
        alerDialogBuilder
                .setView(view)
                .setMessage("Save your new Favourite")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        colorBundleInst.setName(favouriteName.getText().toString().trim());
                        mListener.onSaveFavouriteDialogPositiveClick(SaveFavouriteDialogFragment.this, colorBundleInst);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onSaveFavouriteDialogNegativeClick(SaveFavouriteDialogFragment.this);
                    }
                });
        // Create the AlertDialog object and return it
        return alerDialogBuilder.create();
    }

    public interface SaveFavouriteDialogListener {
        public void onSaveFavouriteDialogPositiveClick(DialogFragment dialog, ColorBundle colorBundleInst);
        public void onSaveFavouriteDialogNegativeClick(DialogFragment dialog);
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