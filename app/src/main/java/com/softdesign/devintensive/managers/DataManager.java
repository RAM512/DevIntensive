package com.softdesign.devintensive.managers;

/**
 * Created by Alex on 28.06.2016.
 */
public class DataManager {
    private static DataManager dataManagerInstanse;
    private PreferencesManager mPreferencesManager;

    private DataManager() {
        mPreferencesManager = new PreferencesManager();
    }

    public static DataManager getInstance() {
        if (dataManagerInstanse == null) {
            dataManagerInstanse = new DataManager();
        }
        return dataManagerInstanse;
    }

    public PreferencesManager getPreferencesManager() {
        return mPreferencesManager;
    }
}
