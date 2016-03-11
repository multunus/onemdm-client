package com.multunus.onemdm;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.multunus.onemdm.config.Config;
import com.multunus.onemdm.usage.AppUsageDataSyncer;
import com.multunus.onemdm.usage.ScreenStatusTracker;
import com.multunus.onemdm.util.Logger;

public class OneMDMService extends Service {
    public OneMDMService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if(Config.TRACK_APP_USAGE) {

            IntentFilter screenIntentFilter = new IntentFilter(Intent.ACTION_USER_PRESENT);
            screenIntentFilter.addAction(Intent.ACTION_SCREEN_OFF);
            BroadcastReceiver screenStatus = new ScreenStatusTracker();
            registerReceiver(screenStatus, screenIntentFilter);
            Logger.debug("started screen status broadcast receiver");

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, AppUsageDataSyncer.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    this, 0, intent, 0);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis(), Config.USAGE_SYNCING_INTERVAL,
                    pendingIntent);

            Logger.debug("registered alarmManager");
        }
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Logger.debug("inside  OneMDMService.onStartCommand");
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.onemdm_running_text))
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
        startForeground(400, notification);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.debug("inside  OneMDMService.onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
