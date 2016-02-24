package com.multunus.onemdm.usage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.multunus.onemdm.config.Config;
import com.multunus.onemdm.util.Logger;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ScreenStatusTracker extends BroadcastReceiver {
    ScheduledFuture scheduledFuture = null;
    ScheduledExecutorService scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);

    public ScreenStatusTracker() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.debug(" received action " + intent.getAction());
        if(intent.getAction().equals(Intent.ACTION_USER_PRESENT)){
            Logger.debug("Screen and keyboard on ");
            Logger.debug("starting tracking service");
            scheduledFuture = scheduledThreadPoolExecutor.scheduleAtFixedRate(new AppUsageCollector(context), 1,
                    Config.USAGE_COLLECTION_TRACKING_INTERVAL_IN_SECONDS, TimeUnit.SECONDS);

        }else if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
            Logger.debug("stopping tracking service");
            if(scheduledFuture != null){
                scheduledFuture.cancel(true);
                scheduledFuture = null;
            }
        }
    }

}