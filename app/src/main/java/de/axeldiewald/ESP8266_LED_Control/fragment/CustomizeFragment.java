package de.axeldiewald.ESP8266_LED_Control.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import de.axeldiewald.ESP8266_LED_Control.bundle.ColorBundle;
import de.axeldiewald.ESP8266_LED_Control.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class CustomizeFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {

    // declare buttons,TextViews and SeekBars
    private TextView textViewRedValue, textViewGreenValue, textViewBlueValue;
    private SeekBar seekBarRed, seekBarGreen, seekBarBlue;
    private TextView spaceColor;
    // declare Settings
    SharedPreferences sharedPreferences;

    public CustomizeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Button ButtonSend, ButtonSave;
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customize, container, false);
        // assign buttons & OnClickListener
        ButtonSend = (Button) view.findViewById(R.id.buttonSend);
        ButtonSend.setOnClickListener(buttonSendClickHandler);
        ButtonSave = (Button) view.findViewById(R.id.buttonSave);
        ButtonSave.setOnClickListener(buttonSaveClickHandler);
        // assign TextViews
        textViewRedValue = (TextView) view.findViewById(R.id.textViewCurRedValue);
        textViewGreenValue = (TextView) view.findViewById(R.id.textViewCurGreenValue);
        textViewBlueValue = (TextView) view.findViewById(R.id.textViewCurBlueValue);
        // assign SeekBars & OnSeekBarChangeListeners
        seekBarRed = (SeekBar) view.findViewById(R.id.seekBarRed);
        seekBarGreen = (SeekBar) view.findViewById(R.id.seekBarGreen);
        seekBarBlue = (SeekBar) view.findViewById(R.id.seekBarBlue);
        seekBarRed.setOnSeekBarChangeListener(this);
        seekBarGreen.setOnSeekBarChangeListener(this);
        seekBarBlue.setOnSeekBarChangeListener(this);
        // assign Space
        spaceColor = (TextView) view.findViewById(R.id.spaceShowColor);
        return view;
    }


    View.OnClickListener buttonSendClickHandler = new View.OnClickListener() {
        public void onClick(View view) {
            ColorBundle colorBundleInst = new ColorBundle(view.getContext(), seekBarRed.getProgress(), seekBarGreen.getProgress(), seekBarBlue.getProgress());
            colorBundleInst.SendToLedStrip();

        }
    };

    View.OnClickListener buttonSaveClickHandler = new View.OnClickListener() {
        public void onClick(View view) {
            ColorBundle colorBundleInst = new ColorBundle(view.getContext(), seekBarRed.getProgress(), seekBarGreen.getProgress(), seekBarBlue.getProgress());
            // Create Dialog for Saving a new Favourite
            SaveFavouriteDialogFragment dialog = new SaveFavouriteDialogFragment();
            dialog.setColorBundle(colorBundleInst);
            dialog.show(getFragmentManager(), "SaveFavouriteDialog");
        }
    };


    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        textViewRedValue.setText(String.valueOf(seekBarRed.getProgress()));
        textViewGreenValue.setText(String.valueOf(seekBarGreen.getProgress()));
        textViewBlueValue.setText(String.valueOf(seekBarBlue.getProgress()));
        // set Preview Color Field
        int red_c = seekBarRed.getProgress() * 256 * 256;
        int green_c = seekBarGreen.getProgress() * 256;
        int blue_c = seekBarBlue.getProgress();
        int color_c = 255 * 256 * 256 * 256 + red_c + green_c + blue_c;
        spaceColor.setBackgroundColor(color_c);
    }
}
