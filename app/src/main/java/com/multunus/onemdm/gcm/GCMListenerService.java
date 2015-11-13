package com.multunus.onemdm.gcm;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;
import com.multunus.onemdm.util.Logger;
import com.rollbar.android.Rollbar;

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
        try {
//            Intent intent = new Intent(this, AppInstallerService.class);
//            startService(intent);
        } catch (Exception ex) {
            Logger.warning("Exception while parsing JSON or installing app",ex);
            Rollbar.reportException(ex);
        }

    }
}