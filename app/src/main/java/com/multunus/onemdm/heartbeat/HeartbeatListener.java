package com.multunus.onemdm.heartbeat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.multunus.onemdm.config.Config;
import com.multunus.onemdm.util.Logger;

public class HeartbeatListener extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        Logger.debug("broadcast received for alarm manager");
        HeartbeatRecorder heartbeatRecorder = new HeartbeatRecorder(context);
        heartbeatRecorder.sendHeartbeatToServer();
    }
}
