package com.mu.compet.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.mu.compet.MyApplication;

/**
 * Created by Tacademy on 2016-08-23.
 */
public class PropertyManager {

    private static PropertyManager instance;
    public static PropertyManager getInstance() {
        if (instance == null) {
            instance = new PropertyManager();
        }
        return instance;
    }

    SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;

    private static final String KEY_USER_NUM = "userNum";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_NICK_NAME = "userNick";
    private static final String KEY_USER_TYPE = "userType";
    private static final String KEY_COOKIE = "cookie";


    private PropertyManager() {
        Context context = MyApplication.getContext();
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        mEditor = mPrefs.edit();
    }

    public void setUserNum(String userNum) {
        mEditor.putString(KEY_USER_NUM, userNum);
        mEditor.commit();
    }

    public String getUserNum() {
        return mPrefs.getString(KEY_USER_NUM, "");
    }

    public void setPassword(String password) {
        mEditor.putString(KEY_PASSWORD, password);
        mEditor.commit();
    }

    public String getPassword() {
        return mPrefs.getString(KEY_PASSWORD, "");
    }


    public void setUserNick(String nickName) {
        mEditor.putString(KEY_NICK_NAME, nickName);
        mEditor.commit();

    }
    public String getUserNick() {
        return mPrefs.getString(KEY_NICK_NAME, "");
    }

    public String getKeyCookie() {
        return mPrefs.getString(KEY_COOKIE, "");
    }
    public void setKeyCookie(String cookie) {
        mEditor.putString(KEY_COOKIE, cookie);
        mEditor.commit();

    }

    public void setUserType(String userType) {
        mEditor.putString(KEY_USER_TYPE, userType);
        mEditor.commit();

    }
    public String getUserType() {
        return mPrefs.getString(KEY_USER_TYPE, "");
    }
}