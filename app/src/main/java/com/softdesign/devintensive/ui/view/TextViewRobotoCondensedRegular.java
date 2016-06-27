package com.softdesign.devintensive.ui.view;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewRobotoCondensedRegular extends TextView {

    public TextViewRobotoCondensedRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (!isInEditMode()) {
            createFont();
        }
    }

    public TextViewRobotoCondensedRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            createFont();
        }
    }

    public TextViewRobotoCondensedRegular(Context context) {
        super(context);
        if (!isInEditMode()) {
            createFont();
        }
    }

    public void createFont() {
        AssetManager assets = getContext().getAssets();
        Typeface font = Typeface.createFromAsset(assets, "fonts/RobotoCondensed-Regular.ttf");
        setTypeface(font);
    }
}
