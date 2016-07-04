package com.softdesign.devintensive.ui.view.behaviors;

import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.LinearLayout;

public class ProgrammerStatusBehavior extends CoordinatorLayout.Behavior<LinearLayout> {
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, LinearLayout child, View dependency) {
        return dependency instanceof NestedScrollView;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, LinearLayout child, View dependency) {
//        child.getLayoutParams().height = 1;
//        child.requestLayout();
        return false;
    }
}