package com.softdesign.devintensive.utils;

/**
 * Created by Alex on 22.06.2016.
 */
public interface ConstantManager {
    String TAG_PREFIX = "DEV ";
    String EDIT_MODE_KEY = "EditMode";

    String USER_PHONE_KEY = "UserPhone";
    String USER_EMAIL_KEY = "UserEmail";
    String USER_VK_KEY = "UserVk";
    String USER_GITHUB_KEY = "USerGithub";
    String USER_BIO_KEY = "UserBio";
    String USER_PHOTO_KEY = "UserPhoto";
    String USER_AVATAR_KEY = "USER_AVATAR_KEY";
    String AUTH_TOKEN_KEY = "AUTH_TOKEN_KEY";
    String USER_ID_KEY = "USER_ID_KEY";
    String USER_RATING_VALUE = "USER_RATING_VALUE";
    String USER_CODE_LINES_VALUE = "USER_CODE_LINES_VALUE";
    String USER_PROJECTS_VALUE = "USER_PROJECTS_VALUE";
    String USER_NAME_KEY = "USER_NAME_KEY";

    int LOAD_PROFILE_PHOTO = 1;
    int REQUEST_CAMERA_PICTURE = 99;
    int REQUEST_GALLERY_PICTURE = 100;
    int PERMISSION_REQUEST_SETTINGS_CODE = 101;
    int CAMERA_REQUEST_PERMISSION_CODE = 102;
    int CALL_PHONE_REQUEST_PERMISSION_CODE = 103;

    String PARCELABLE_KEY = "PARCELABLE_KEY";
    int SEARCH_DELAY = 1500;
}
