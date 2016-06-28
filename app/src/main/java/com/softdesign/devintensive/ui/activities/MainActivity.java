package com.softdesign.devintensive.ui.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.managers.DataManager;
import com.softdesign.devintensive.utils.ConstantManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = ConstantManager.TAG_PREFIX + "MainActivity";

    private CoordinatorLayout mCoordinatorLayout;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private FloatingActionButton mFab;
    private EditText mPhone, mEmail, mVk, mGithub, mBio;
    private int mEditMode;

    private List<EditText> mUserInfoViews;
    private DataManager mDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate()");

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setupToolBar();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer);
        setupDrawer();

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator_container);

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(this);

        mPhone = (EditText) findViewById(R.id.phone_edit);
        mEmail = (EditText) findViewById(R.id.email_edit);
        mVk = (EditText) findViewById(R.id.vk_edit);
        mGithub = (EditText) findViewById(R.id.github_edit);
        mBio = (EditText) findViewById(R.id.about_myself_edit);

        mUserInfoViews = new ArrayList<>();
        mUserInfoViews.add(mPhone);
        mUserInfoViews.add(mEmail);
        mUserInfoViews.add(mVk);
        mUserInfoViews.add(mGithub);
        mUserInfoViews.add(mBio);

        mDataManager = DataManager.getInstance();
        loadUserInfoValue();

        List<String> test = mDataManager.getPreferencesManager().loadUserProfileData();


        if (savedInstanceState == null) {
            showSnackBar("Активити запускается впервые");
        } else {
            showSnackBar("Активити уже запускалось");
            mEditMode = savedInstanceState.getInt(ConstantManager.EDIT_MODE_KEY, 0);
            changeEditMode(mEditMode);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                showSnackBar("fab");
                if (mEditMode == 0) {
                    changeEditMode(1);
                } else {
                    changeEditMode(0);
                    saveUserInfoValue();
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    private void runWithDelay() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideProgress();
            }
        }, 5000);
    }

    public void showSnackBar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void setupToolBar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupDrawer() {
        final NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                    showSnackBar(item.getTitle().toString());
                    item.setChecked(true);
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                    return false;
                }
            });
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ConstantManager.EDIT_MODE_KEY, mEditMode);
    }

    /**
     * Переключает режим редактирования
     * @param mode если 1 - режим редактирования, если 1 - режим просмотра
     */
    private void changeEditMode(int mode) {
        mEditMode = mode;
        boolean enableEdit = (mode == 1);
        for (View edit : mUserInfoViews) {
            edit.setFocusable(enableEdit);
            edit.setEnabled(enableEdit);
            edit.setFocusableInTouchMode(enableEdit);
        }
        if (enableEdit) {
            mFab.setImageResource(R.drawable.ic_done_black_24dp);
        } else {
            mFab.setImageResource(R.drawable.ic_create_black_24dp);
        }
    }

    private void loadUserInfoValue() {
        List<String> userData = mDataManager.getPreferencesManager().loadUserProfileData();
        for (int i = 0; i < userData.size(); i++) {
            ((EditText) mUserInfoViews.get(i)).setText(userData.get(i));
        }
    }

    private void saveUserInfoValue() {
        ArrayList<String> userData = new ArrayList<>();
        for (EditText et : mUserInfoViews) {
            userData.add(et.getText().toString());
        }
        mDataManager.getPreferencesManager().saveUserProfileData(userData);
    }
}
