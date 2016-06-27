package com.softdesign.devintensive.ui.view;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewRobotoMedium extends TextView {

    public TextViewRobotoMedium(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (!isInEditMode()) {
            createFont();
        }
    }

    public TextViewRobotoMedium(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            createFont();
        }
    }

    public TextViewRobotoMedium(Context context) {
        super(context);
        if (!isInEditMode()) {
            createFont();
        }
    }

    public void createFont() {
        AssetManager assets = getContext().getAssets();
        Typeface font = Typeface.createFromAsset(assets, "fonts/Roboto-Medium.ttf");
        setTypeface(font);
    }
}
