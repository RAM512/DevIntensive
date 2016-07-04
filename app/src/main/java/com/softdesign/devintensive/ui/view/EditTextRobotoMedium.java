package com.softdesign.devintensive.ui.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

public class EditTextRobotoMedium extends EditText {

    public EditTextRobotoMedium(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode()) {
            createFont();
        }
    }

    public EditTextRobotoMedium(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            createFont();
        }
    }

    public EditTextRobotoMedium(Context context) {
        super(context);
        if (!isInEditMode()) {
            createFont();
        }
    }

    public void createFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Medium.ttf");
        setTypeface(font);
    }
}
