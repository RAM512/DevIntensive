package com.softdesign.devintensive.ui.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Alex on 26.06.2016.
 */
public class EditTextRoboto extends EditText {

    public EditTextRoboto(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        createFont();
    }

    public EditTextRoboto(Context context, AttributeSet attrs) {
        super(context, attrs);
        createFont();
    }

    public EditTextRoboto(Context context) {
        super(context);
        createFont();
    }

    public void createFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/RobotoCondensed-Regular.ttf");
        setTypeface(font);
    }
}
