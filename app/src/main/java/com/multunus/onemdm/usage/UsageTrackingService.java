package com.multunus.onemdm.usage;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;

import com.multunus.onemdm.config.Config;
import com.multunus.onemdm.model.AppUsage;
import com.multunus.onemdm.util.Logger;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmResults;

public class UsageTrackingService extends Service {
    AppUsageCollector appUsageCollector = new AppUsageCollector();
    public UsageTrackingService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Logger.debug("inside  UsageTrackingService.onStartCommand");
        appUsageCollector.collectUsageData();
        return START_STICKY;
    }

    public class AppUsageCollector{
        public void collectUsageData() {
            ScheduledExecutorService scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
            scheduledThreadPoolExecutor.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
//                    Logger.debug("inside  UsageTrackingService.scheduledThreadPoolExecutor.run with screen status "
//                            +ScreenStatus.SCREEN_ON);
                    ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                    try {
                        Realm realm = Realm.getDefaultInstance();

                        if (ScreenStatus.SCREEN_ON) {
                            String runningApp = activityManager.getRunningTasks(1).get(0).topActivity.getPackageName();
                            Logger.debug("Currently running app " + runningApp);

                            String today = today();
                            updateAppUsageForTodayForTheApp(runningApp, today, realm);
                        }
                    } catch (Exception ex) {
                        Logger.warning("Exception while reading the running tasks", ex);
//                        realm.cancelTransaction();
                    }
                }

            }, 1, Config.USAGE_COLLECTION_TRACKING_INTERVAL_IN_SECONDS, TimeUnit.SECONDS);
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
                    appUsage.getAppUsageDurationPerDayInSeconds() +
                            Config.USAGE_COLLECTION_TRACKING_INTERVAL_IN_SECONDS);
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
            return new SimpleDateFormat("yyyy-MM-dd").format
                                            (Calendar.getInstance().getTime());
        }

        private boolean isUsageStatsPermissionGranted() {
            int currentAPIVersion = android.os.Build.VERSION.SDK_INT;
            if (currentAPIVersion >= Build.VERSION_CODES.LOLLIPOP) {
                List<UsageStats> usageStats = getUsageStats();
                return !usageStats.isEmpty();
            } else {
                return true;
            }

        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        private List<UsageStats> getUsageStats() {
            long endTime = System.currentTimeMillis();
            long oneDay = 48 * 60 * 60 * 1000;
            UsageStatsManager mUsageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
            return mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST,
                    endTime - oneDay, endTime);


        }

//        private boolean isScreenLocked() {
//            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
//            boolean locked = keyguardManager.inKeyguardRestrictedInputMode();
//            return locked;
//        }
    }
}