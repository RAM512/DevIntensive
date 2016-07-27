package com.softdesign.devintensive.utils;

import okhttp3.HttpUrl;

public interface AppConfig {
    String BASE_URL = "http://devintensive.softdesign-apps.ru/api/";
    long MAX_CONNECT_TIMEOUT = 5000;
    long MAX_READ_TIMEOUT = 5000;
    int START_DELAY = 1500;
}
