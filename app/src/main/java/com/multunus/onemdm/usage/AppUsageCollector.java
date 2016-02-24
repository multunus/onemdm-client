package com.multunus.onemdm.usage;

import android.app.ActivityManager;
import android.content.Context;
import android.support.annotation.NonNull;

import com.multunus.onemdm.config.Config;
import com.multunus.onemdm.model.AppUsage;
import com.multunus.onemdm.util.Logger;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmResults;

public class AppUsageCollector implements Runnable{

    private final Context context;

    public AppUsageCollector(Context context){
        this.context = context;
    }
    @Override
    public void run() {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        try {
            Realm realm = Realm.getDefaultInstance();
            String runningApp = activityManager.getRunningTasks(1).get(0).topActivity.getPackageName();
            Logger.debug("Currently running app " + runningApp);

            String today = today();
            updateAppUsageForTodayForTheApp(runningApp, today, realm);
        } catch (Exception ex) {
            Logger.warning("Exception while reading the running tasks", ex);
        }
    }
    private void updateAppUsageForTodayForTheApp(String runningApp, String today,Realm realm) {
        RealmResults<AppUsage> results = getAppUsageForAppForToday(
                realm, runningApp, today);
        AppUsage appUsage;
        realm.beginTransaction();

        if(results.size() > 0){
            Logger.debug("found the record from DB");
            appUsage = results.first();
        }
        else{
            Logger.debug("not found the record, so creating");
            appUsage = realm.createObject(AppUsage.class);
            appUsage.setId(Calendar.getInstance().getTimeInMillis());
        }

        appUsage.setPackageName(runningApp);
        appUsage.setAppUsedOn(today);
        appUsage.setAppUsageDurationPerDayInSeconds(
                appUsage.getAppUsageDurationPerDayInSeconds()
                        + Config.USAGE_COLLECTION_TRACKING_INTERVAL_IN_SECONDS);
        Logger.debug("app usage for " + runningApp + " is "
                + appUsage.getAppUsageDurationPerDayInSeconds());
        realm.commitTransaction();

    }

    private RealmResults<AppUsage> getAppUsageForAppForToday(Realm realm, String runningApp, String today) {
        return realm.where(AppUsage.class)
                .equalTo("appUsedOn", today)
                .equalTo("packageName", runningApp)
                .findAll();
    }

    @NonNull
    private String today() {
        return new SimpleDateFormat("yyyy-MM-dd").
                format(Calendar.getInstance().getTime());
    }
}