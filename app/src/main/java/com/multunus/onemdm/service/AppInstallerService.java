package com.multunus.onemdm.service;

import android.app.DownloadManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.view.View;

import com.multunus.onemdm.R;
import com.multunus.onemdm.config.Config;
import com.multunus.onemdm.model.App;
import com.multunus.onemdm.util.Logger;

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
        downloadAndShowInstallNotification();
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

    private BroadcastReceiver configureDownloadCompleteBroadcastReceiver
            (final DownloadManager downloadManager, final long downloadId) {
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
        PendingIntent resultPendingIntent = createNotificationActionForInstall();

        createNotificationForInstall(notificationManager, resultPendingIntent);
    }

    private void createNotificationForInstall(NotificationManager notificationManager,
                                              PendingIntent resultPendingIntent) {
        Notification notification = new Notification.Builder(context)
                .setContentTitle(getString(R.string.app_name))
                .setSmallIcon(R.drawable.cast_ic_notification_0)
                .setContentText("Click to Install ")
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true)
                .setOngoing(true)
                .build();
        notificationManager.notify(View.generateViewId(), notification);
    }

    private PendingIntent createNotificationActionForInstall() {
        Intent pendingIntent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
        pendingIntent.setData(Uri.fromFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/onemdm.apk")));
//        pendingIntent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
        pendingIntent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
        pendingIntent.putExtra(Intent.EXTRA_INSTALLER_PACKAGE_NAME, app.getPackageName());
        return PendingIntent.getActivity(
                context,
                View.generateViewId(),
                pendingIntent,
                PendingIntent.FLAG_CANCEL_CURRENT
        );
    }

}
