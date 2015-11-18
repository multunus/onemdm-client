package com.multunus.onemdm.gcm;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.multunus.onemdm.config.Config;
import com.multunus.onemdm.model.App;
import com.multunus.onemdm.app.AppInstallerService;
import com.multunus.onemdm.util.Logger;

public class GCMListenerService extends GcmListenerService {

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        Logger.debug("Message: " + message);
        Intent intent = new Intent(this, AppInstallerService.class);
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        App app = gson.fromJson(message,App.class);
        Logger.debug(" app ID"+app.getId());
        Logger.debug(" app package name "+app.getPackageName());
        Logger.debug(" APK URL = "+app.getApkUrl());
        intent.putExtra(Config.APP_DATA,app);
//        intent.putExtra(Config.APP_URL,"http://192.168.2.92:3000/onemdm-debug-v1.01.apk");
        startService(intent);
    }
}