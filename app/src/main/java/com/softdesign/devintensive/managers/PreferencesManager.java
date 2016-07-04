package com.softdesign.devintensive.managers;

import android.content.SharedPreferences;

import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.DevIntensiveApplication;

import java.util.ArrayList;
import java.util.List;

public class PreferencesManager {
    private SharedPreferences mSharedPreferences;

    private static final String[] USER_FIELDS = {
            ConstantManager.USER_PHONE_KEY,
            ConstantManager.USER_EMAIL_KEY,
            ConstantManager.USER_VK_KEY,
            ConstantManager.USER_GITHUB_KEY,
            ConstantManager.USER_BIO_KEY
    };

    public PreferencesManager() {
        mSharedPreferences = DevIntensiveApplication.getSharedPreferences();
    }

    public void saveUserProfileData(List<String> userData) {
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        for (int i = 0; i < USER_FIELDS.length; i++) {
            edit.putString(USER_FIELDS[i], userData.get(i));
        }
        edit.apply();
    }

    public List<String> loadUserProfileData() {
        ArrayList<String> userData = new ArrayList<>();
        for (int i = 0; i < USER_FIELDS.length; i++) {
            userData.add(mSharedPreferences.getString(USER_FIELDS[i], "null"));
        }
        return  userData;
    }
}
