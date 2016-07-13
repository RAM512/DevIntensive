package com.softdesign.devintensive.data.managers;

import android.content.SharedPreferences;
import android.net.Uri;

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

    private static final String[] USER_VALUES = {
            ConstantManager.USER_RATING_VALUE,
            ConstantManager.USER_CODE_LINES_VALUE,
            ConstantManager.USER_PROJECTS_VALUE
    };

    public PreferencesManager() {
        mSharedPreferences = DevIntensiveApplication.getSharedPreferences();
    }

    /**
     * Сохраняет набор данных в долгосрочную память.
     * @param userData
     */
    public void saveUserProfileData(List<String> userData) {
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        for (int i = 0; i < USER_FIELDS.length; i++) {
            edit.putString(USER_FIELDS[i], userData.get(i));
        }
        edit.apply();
    }

    /**
     * Загружает набор данных из долгосрочной памяти.
     * @return
     */
    public List<String> loadUserProfileData() {
        ArrayList<String> userData = new ArrayList<>();
        for (int i = 0; i < USER_FIELDS.length; i++) {
            userData.add(mSharedPreferences.getString(USER_FIELDS[i], "null"));
        }
        return  userData;
    }

    /**
     * Сохраняет путь к пользовательскому изображению в долгосрочную память.
     * @param uri
     */
    public void saveUserPhoto(Uri uri) {
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.putString(ConstantManager.USER_PHOTO_KEY, uri.toString());
        edit.apply();
    }

    /**
     * Загружает путь к пользовательскому изображению из долгосрочной памяти.
     * @return
     */
    public Uri loadUserPhoto() {
        String stringUri = mSharedPreferences.getString(ConstantManager.USER_PHOTO_KEY,
                "android.resource://com.softdesign.devintensive/drawable/full");
        Uri uri = Uri.parse(stringUri);
        return uri;
    }

    public void saveUserAvatar(Uri uri) {
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.putString(ConstantManager.USER_AVATAR_KEY, uri.toString());
        edit.apply();
    }

    /**
     * Загружает путь к пользовательскому изображению из долгосрочной памяти.
     * @return
     */
    public Uri loadUserAvatar() {
        String stringUri = mSharedPreferences.getString(ConstantManager.USER_AVATAR_KEY,
                "android.resource://com.softdesign.devintensive/drawable/ava");
        Uri uri = Uri.parse(stringUri);
        return uri;
    }

    /**
     * Сохраняет токен авторизованного подключения
     * @param authToken
     */
    public void saveAuthToken(String authToken) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.AUTH_TOKEN_KEY, authToken);
        editor.apply();
    }

    /**
     * Возвращает сохраненный токен подключения
     * @return
     */
    public String getAuthToken() {
        return mSharedPreferences.getString(ConstantManager.AUTH_TOKEN_KEY, "null");
    }

    /**
     * Сохраняет токен авторизованного подключения
     * @param userId
     */
    public void saveUserId(String userId) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_ID_KEY, userId);
        editor.apply();
    }

    /**
     * Возвращает сохраненный токен подключения
     * @return
     */
    public String getUserId() {
        return mSharedPreferences.getString(ConstantManager.USER_ID_KEY, "null");
    }

    public void saveUserProfileValues(int[] userValues) {
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        for (int i = 0; i < USER_VALUES.length; i++) {
            edit.putString(USER_VALUES[i], String.valueOf(userValues[i]));
        }
        edit.apply();
    }

    public List<String> loadUserProfileValues() {
        ArrayList<String> userValues = new ArrayList<>();
        for (int i = 0; i < USER_VALUES.length; i++) {
            userValues.add(mSharedPreferences.getString(USER_VALUES[i], "0"));
        }
        return  userValues;
    }

    public void saveUserName(String userName) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_NAME_KEY, userName);
        editor.apply();
    }

    public String loadUserName() {
        return mSharedPreferences.getString(ConstantManager.USER_NAME_KEY, "null");
    }
}
