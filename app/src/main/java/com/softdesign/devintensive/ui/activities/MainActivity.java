package com.softdesign.devintensive.ui.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.managers.DataManager;
import com.softdesign.devintensive.utils.ConstantManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = ConstantManager.TAG_PREFIX + "MainActivity";

    private CoordinatorLayout mCoordinatorLayout;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private FloatingActionButton mFab;
    private EditText mPhone, mEmail, mVk, mGithub, mBio;
    private ImageView mAvatar;
    private int mEditMode;
    private RelativeLayout mProfilePlaceholder;
    private CollapsingToolbarLayout mCollapsingToolbar;
    private int mScrollFlags;

    private List<EditText> mUserInfoViews;
    private DataManager mDataManager;

    private AppBarLayout mAppBarLayout;
    private AppBarLayout.LayoutParams mAppBarParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate()");

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        setupToolBar();

        mAppBarLayout = (AppBarLayout)findViewById(R.id.appbar_layout);

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

        mProfilePlaceholder = (RelativeLayout) findViewById(R.id.profile_placeholder);
        mProfilePlaceholder.setOnClickListener(this);

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

            case R.id.profile_placeholder:
                // TODO: 04.07.2016 сделать выбор откуда загружать фото
                showDialog(ConstantManager.LOAD_PROFILE_PHOTO);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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

        mAppBarParams = (AppBarLayout.LayoutParams) mCollapsingToolbar.getLayoutParams();
        mScrollFlags = mAppBarParams.getScrollFlags();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupDrawer() {
        final NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (navigationView != null) {
            mAvatar = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.user_avatar);
            Bitmap ava = BitmapFactory.decodeResource(getResources(), R.drawable.ava);
            RoundedBitmapDrawable roundedAva = RoundedBitmapDrawableFactory.create(getResources(), ava);
            roundedAva.setCircular(true);
            mAvatar.setImageDrawable(roundedAva);

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

    /**
     * Получение данных из другой активности (из камеры или галереи)
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);

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
            showProfilePlaceholder();
            lockToolbar();
            mCollapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);
        } else {
            mFab.setImageResource(R.drawable.ic_create_black_24dp);
            hideProfilePlaceholder();
            unlockToolbar();
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

    private void loadPhotoFromCamera() {

    }

    private void loadPhotoFromGalery() {

    }

    private void hideProfilePlaceholder() {
        mProfilePlaceholder.setVisibility(View.GONE);
    }

    private void showProfilePlaceholder() {
        mProfilePlaceholder.setVisibility(View.VISIBLE);
    }

    private void lockToolbar () {
        // TODO: 04.07.2016 разобраться с дерганой анимацией раскрытия
        mAppBarLayout.setExpanded(true, true);
        mAppBarParams.setScrollFlags(0);
//        mCollapsingToolbar.setLayoutParams(mAppBarParams); //TODO нужна ли эта комманда?
    }
    public void unlockToolbar () {
        mAppBarParams.setScrollFlags(mScrollFlags);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case ConstantManager.LOAD_PROFILE_PHOTO:
                String[] selectItems = {getString(R.string.user_profile_dialog_galery),
                        getString(R.string.user_profile_dialog_photo),
                        getString(R.string.user_profile_dialog_cancel)};

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.user_profile_dialog_title))
                        .setItems(selectItems, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0: // TODO: 04.07.2016 загрузка из галереи
                                        loadPhotoFromGalery();
                                        showSnackBar("Загрузить из галереи");
                                        break;
                                    case 1: // TODO: 04.07.2016 сделать снимок
                                        loadPhotoFromCamera();
                                        showSnackBar("Сделать снимок");
                                        break;
                                    case 2: // TODO: 04.07.2016 отмена
                                        dialog.cancel();
                                        showSnackBar("Отмена");
                                }
                            }
                        });
                return builder.create();

            default:
                return null;
        }
    }

    private File createImageFile() throws IOException {

    }
}
