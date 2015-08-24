package de.axeldiewald.ESP8266_LED_Control;


import android.app.Activity;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment2 extends Fragment implements SeekBar.OnSeekBarChangeListener {

    // declare buttons,TextViews and SeekBars
    private Button ButtonSend, ButtonSave;
    private TextView textViewRedValue, textViewGreenValue, textViewBlueValue;
    private SeekBar seekBarRed, seekBarGreen, seekBarBlue;
    private TextView spaceColor;
    // declare Settings
    SharedPreferences sharedPreferences;
    //
    OnNewFavouriteListener mCallback;

    public Fragment2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_2, container, false);
        // Get Settings
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (OnNewFavouriteListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnNewFavouriteListener");
        }
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
            // send to Parent Activity
            mCallback.newFavourite(colorBundleInst);
        }
    };

    public interface OnNewFavouriteListener {
        public void newFavourite(ColorBundle colorBundleInst);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
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
