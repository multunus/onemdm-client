package com.multunus.onemdm.util;

import android.content.Context;
import android.provider.Settings;

/**
 * Created by sreenath on 16/03/16.
 */
public class Helper {
    public static String getAndroidId(Context context) {
        return Settings.Secure.getString(
                context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}
