package com.softdesign.devintensive.ui.behaviors;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.utils.UiHelper;

public class ProgrammerStatusBehavior<V extends LinearLayout> extends AppBarLayout.ScrollingViewBehavior {
    private final int mMaxAppbarHeight;
    private final int mMinAppbarHeight;
    private final int mMaxProgrammerStatusHeight;
    private final int mMinProgrammerStatusHeight;

    public ProgrammerStatusBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.ProgrammerStatusBehavior);
        mMinProgrammerStatusHeight = a.getDimensionPixelSize(
                R.styleable.ProgrammerStatusBehavior_behavior_min_height, 56);
        a.recycle();
        mMinAppbarHeight = UiHelper.getActionBarHeight(); // 80dp
        mMaxAppbarHeight = context.getResources().getDimensionPixelSize(R.dimen.profile_image_size); // 256dp
        mMaxProgrammerStatusHeight = context.getResources().getDimensionPixelSize(R.dimen.size_user_info); // 112dp
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        float currentFriction = UiHelper.currentFriction(mMinAppbarHeight, mMaxAppbarHeight, dependency.getBottom());
        int currentHeight = UiHelper.lerp(mMinProgrammerStatusHeight, mMaxProgrammerStatusHeight, currentFriction);

        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        lp.height = currentHeight;
        child.setLayoutParams(lp);

        return super.onDependentViewChanged(parent, child, dependency);
    }
}