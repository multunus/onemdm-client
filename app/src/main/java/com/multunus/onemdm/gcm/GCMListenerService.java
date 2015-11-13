package com.multunus.onemdm.gcm;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;
import com.multunus.onemdm.config.Config;
import com.multunus.onemdm.service.AppInstallerService;
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
        intent.putExtra(Config.APP_URL,"https://s3.amazonaws.com/onemdm/onemdm.apk");
        startService(intent);
    }
}