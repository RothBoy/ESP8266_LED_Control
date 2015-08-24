package de.axeldiewald.ESP8266_LED_Control;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class Fragment1 extends Fragment {

    private static LinearLayout linearLayout;

    public Fragment1() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_1, container, false);
        linearLayout = (LinearLayout) view.findViewById(R.id.layout1);

        return view;
    }

    public void addFavouriteFragment(Context context, final ColorBundle colorBundleInst){
        Button newButton = new Button(context);
        LinearLayout.LayoutParams params =  new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        newButton.setLayoutParams(params);
        newButton.setText("button");
        newButton.setBackgroundColor(Color.rgb(colorBundleInst.redValue, colorBundleInst.greenValue, colorBundleInst.blueValue));
        //newButton.myColorBundle = colorBundleInst;
        newButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //Toast.makeText(view.getContext(), "Button clicked index", Toast.LENGTH_SHORT).show();
                colorBundleInst.SendToLedStrip();
            }
        });
        linearLayout.addView(newButton);

        /*FavouriteFragment newFragment = new FavouriteFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment1container, newFragment, "Frag1").commit();
        transaction.addToBackStack(null);
        Context context1 = context;
        String text = "SAVE A NEW FAVOURITE: \nRot: " + String.valueOf(colorBundleInst.redValue)
                + " Gruen: " + String.valueOf(colorBundleInst.greenValue)
                + " Blau: " + String.valueOf(colorBundleInst.blueValue);
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context1, text, duration);
        toast.show();*/
    }


}
