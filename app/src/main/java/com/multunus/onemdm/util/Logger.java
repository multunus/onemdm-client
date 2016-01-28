package com.multunus.onemdm.util;

import android.util.Log;

import com.multunus.onemdm.BuildConfig;
import com.rollbar.android.Rollbar;

/**
 * Created by yedhukrishnan on 15/10/15.
 */
public final class Logger {
    public static final String LOG_TAG = "onemdm";
    private static final boolean DEBUG = BuildConfig.DEBUG;

    private Logger() {

    }
    public static void debug(String message) {
        if(DEBUG) {
            Log.d(LOG_TAG, message);
        }
    }
    public static void warning(String message) {
        if(DEBUG) {
            Log.w(LOG_TAG, message);
        }
    }
    public static void warning(String message,Throwable throwable) {
        if(DEBUG) {
            Log.w(LOG_TAG, message,throwable);
        }
        Rollbar.reportException(throwable);
    }
    public static void error(Throwable throwable) {
        if(DEBUG) {
            Log.e(LOG_TAG, throwable.getMessage(),throwable);
        }
        Rollbar.reportException(throwable);
    }
}
