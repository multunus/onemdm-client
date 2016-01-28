package com.multunus.onemdm.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.multunus.onemdm.config.Config;
import com.multunus.onemdm.network.AppStatusUpdater;
import com.multunus.onemdm.util.Logger;

public class AppInstallListener extends BroadcastReceiver {
    private AppStatusUpdater appStatusUpdater;
    public AppInstallListener() {
        this.appStatusUpdater = new AppStatusUpdater();
    }

    AppInstallListener(AppStatusUpdater appStatusUpdater){
        this.appStatusUpdater = appStatusUpdater;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String packageName = intent.getData().getSchemeSpecificPart();
        Logger.debug("installed package: " + packageName);
        updateAppInstallationStatus(context,packageName);
    }

    private void updateAppInstallationStatus(Context context,String packageName){
        long appInstallationId = context.getSharedPreferences(Config.PREFERENCE_TAG,
                Context.MODE_PRIVATE).getLong(packageName,-1);
        if(appInstallationId != -1){
            Logger.debug("Found app for "+packageName+" with installation ID "+appInstallationId);
            appStatusUpdater.updateAppInstallationStatus(context, appInstallationId);
        }
    }
}