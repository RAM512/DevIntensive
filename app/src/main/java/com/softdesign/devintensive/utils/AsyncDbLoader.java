package com.softdesign.devintensive.utils;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDao;

import java.util.ArrayList;
import java.util.List;

public class AsyncDbLoader extends AsyncTaskLoader<List<User>> {

    public AsyncDbLoader(Context context) {
        super(context);
    }

    @Override
    public List<User> loadInBackground() {
        List<User> userList = new ArrayList<>();
        try {
            userList = DevIntensiveApplication.getDaoSession().queryBuilder(User.class)
                    .where(UserDao.Properties.CodeLines.gt(0))
                    .orderDesc(UserDao.Properties.CodeLines)
                    .build()
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        super.onStartLoading();
    }
}
