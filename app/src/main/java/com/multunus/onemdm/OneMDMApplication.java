package com.multunus.onemdm;

import android.app.Application;

import com.rollbar.android.Rollbar;

public class OneMDMApplication extends Application{

    @Override
    public void onCreate(){
        super.onCreate();
        if(!BuildConfig.DEBUG) {
            Rollbar.init(this, "d992c2d5219c4ccca3ead5622afa65c5", "production");
        }
    }
}
