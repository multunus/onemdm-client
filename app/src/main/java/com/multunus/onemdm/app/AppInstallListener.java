package com.multunus.onemdm.app;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.multunus.onemdm.config.Config;
import com.multunus.onemdm.network.AppStatusUpdater;
import com.multunus.onemdm.util.Logger;

public class AppInstallListener extends BroadcastReceiver {
    public AppInstallListener() {
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
            new AppStatusUpdater(context,appInstallationId).updateAppInstallationStatus();
        }
    }
}