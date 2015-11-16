package com.multunus.onemdm;

import android.app.Application;

import com.rollbar.android.Rollbar;

public class OneMDMApplication extends Application{

    @Override
    public void onCreate()
    {
        super.onCreate();
        if(!BuildConfig.DEBUG) {
            Rollbar.init(this, "c096aaa23042478fafb34be8f937ad5c", "production");
        }
    }
}
