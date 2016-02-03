package com.multunus.onemdm;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;

import com.multunus.onemdm.usage.ScreenStatus;
import com.multunus.onemdm.util.Logger;
import com.rollbar.android.Rollbar;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class OneMDMApplication extends Application{

    @Override
    public void onCreate()
    {
        super.onCreate();
        if(!BuildConfig.DEBUG) {
            Rollbar.init(this, "c096aaa23042478fafb34be8f937ad5c", "production");
        }
        RealmConfiguration config = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(config);
        IntentFilter screenIntentFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        screenIntentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        BroadcastReceiver screenStatus = new ScreenStatus();
        registerReceiver(screenStatus, screenIntentFilter);
        Logger.debug("registered the receiver");
    }
}
