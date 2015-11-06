package com.multunus.onemdm.util;

import android.util.Log;

import com.multunus.onemdm.BuildConfig;

/**
 * Created by yedhukrishnan on 15/10/15.
 */
public class Logger {
    public static final String LOG_TAG = "onemdm";
    private static final boolean DEBUG = BuildConfig.DEBUG;

    public static void debug(String message) {
//        if(DEBUG) {
            Log.d(LOG_TAG, message);
//        }
    }
    public static void warning(String message) {
//        if(DEBUG) {
            Log.w(LOG_TAG, message);
//        }
    }
    public static void error(Throwable throwable) {
//        if(DEBUG) {
            Log.e(LOG_TAG, throwable.getMessage(),throwable);
//        }
    }
}
