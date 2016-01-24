package de.axeldiewald.ESP8266_LED_Control;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

public class FavouriteButtonView extends Button {

    public FavouriteButtonView(Context context) {
        super(context);
    }

    public FavouriteButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FavouriteButtonView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}