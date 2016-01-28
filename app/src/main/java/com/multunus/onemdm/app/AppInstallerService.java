package com.multunus.onemdm.app;

import android.app.DownloadManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;

import com.multunus.onemdm.R;
import com.multunus.onemdm.config.Config;
import com.multunus.onemdm.model.App;
import com.multunus.onemdm.util.Logger;

import java.util.UUID;

public class AppInstallerService extends IntentService {

    private Context context;
    private String apkURL = "";
    private App app;

    public AppInstallerService() {
        super("AppInstallerService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Logger.debug("AppInstallerService  started");
        this.context = getApplicationContext();
        this.app = intent.getParcelableExtra(Config.APP_DATA);
        this.apkURL = app.getApkUrl();
        Logger.debug("APP URL "+apkURL);
        installOrDownloadApp(apkURL);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void installOrDownloadApp(String apkURL){
        Logger.debug("APK url " + apkURL);
        if(apkURL.equals("")){
            createActionForInstall();
        }
        else {
            downloadAndShowInstallNotification();
        }
    }

    private void downloadAndShowInstallNotification() {
        final DownloadManager downloadManager = (DownloadManager)
                context.getSystemService(Context.DOWNLOAD_SERVICE);
        final long downloadId = enqueueDownload(downloadManager);
        final BroadcastReceiver receiver = configureDownloadCompleteBroadcastReceiver(
                downloadManager, downloadId);
        IntentFilter intentFilter
                = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        context.registerReceiver(receiver, intentFilter);
    }

    private long enqueueDownload(DownloadManager downloadManager) {
        Uri uri = Uri.parse(apkURL);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setDescription("Downloading...");
        request.setTitle(getString(R.string.app_name));
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "onemdm.apk");
        return downloadManager.enqueue(request);
    }

    private BroadcastReceiver configureDownloadCompleteBroadcastReceiver(
            final DownloadManager downloadManager, final long downloadId) {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(downloadId);
                Cursor cursor = downloadManager.query(query);

                if (cursor.moveToFirst()) {
                    int columnIndex = cursor
                            .getColumnIndex(DownloadManager.COLUMN_STATUS);
                    if (DownloadManager.STATUS_SUCCESSFUL == cursor
                            .getInt(columnIndex)) {
                        Logger.debug(" download successfully completed");
//                        updateAppDownloadedStatus(appId);
                        context.unregisterReceiver(this);
                        showAppInstallNotification();
                    }
                    cursor.close();
                }
            }
        };
    }

    private void showAppInstallNotification() {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent resultPendingIntent = createActionForInstallAfterDownload();

        createNotificationForInstallAndSaveToPreferences(notificationManager, resultPendingIntent);
    }

    private void createActionForInstall(){
        final Uri marketUri = Uri.parse("market://details?id=" + app.getPackageName());
        Intent intent = new Intent(Intent.ACTION_VIEW, marketUri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                getUniqueId(),
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
        );
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        createNotificationForInstallAndSaveToPreferences(notificationManager, pendingIntent);
    }

    private PendingIntent createActionForInstallAfterDownload() {
        Intent pendingIntent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
        pendingIntent.setData(Uri.fromFile(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS + "/onemdm.apk")));
        pendingIntent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
        pendingIntent.putExtra(Intent.EXTRA_INSTALLER_PACKAGE_NAME, app.getPackageName());
        return PendingIntent.getActivity(
                context,
                getUniqueId(),
                pendingIntent,
                PendingIntent.FLAG_CANCEL_CURRENT
        );
    }


    private void createNotificationForInstallAndSaveToPreferences(NotificationManager notificationManager,
                                                                  PendingIntent resultPendingIntent) {
        Notification.Builder notificationBuilder = new Notification.Builder(context)
                .setContentTitle(app.getName())
                .setSmallIcon(R.drawable.cast_ic_notification_0)
                .setContentText("Click to Install ")
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true)
                .setOngoing(true);
        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            notification = notificationBuilder.build();
        }
        else{
            notification = notificationBuilder.getNotification();
        }

        notificationManager.notify(getUniqueId(), notification);
        saveApptoPreferences();
    }

    private void saveApptoPreferences(){
        SharedPreferences.Editor editor = this.context.getSharedPreferences(
                Config.PREFERENCE_TAG, Context.MODE_PRIVATE).edit();
        editor.putLong(app.getPackageName(), app.getId());
        editor.apply();
    }

    protected int getUniqueId(){
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 ){
//            return View.generateViewId();
//        }
        return UUID.randomUUID().hashCode();
    }
}
