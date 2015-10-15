package com.multunus.onemdm.util;

import android.util.Log;

/**
 * Created by yedhukrishnan on 15/10/15.
 */
public class Logger {
    private static final String LOG_TAG = "onemdm";

    public static void debug(String message) {
        Log.d(LOG_TAG, message);
    }
}
