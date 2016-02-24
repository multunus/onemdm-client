package com.multunus.onemdm;

import android.app.Application;
import com.rollbar.android.Rollbar;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class OneMDMApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        if(!BuildConfig.DEBUG) {
            Rollbar.init(this, "c096aaa23042478fafb34be8f937ad5c", "production");
        }
        RealmConfiguration config = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(config);
    }
}
