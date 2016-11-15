package com.mu.compet;

import android.app.Application;
import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by Tacademy on 2016-08-29.
 */
public class MyApplication extends Application {
    static Context context;
    private Tracker mTracker;


    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }


    public static Context getContext() {
        return context;
    }
}
