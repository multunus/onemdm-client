package com.multunus.onemdm.heartbeat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.multunus.onemdm.network.HeartbeatRecorder;
import com.multunus.onemdm.util.Logger;

public class HeartbeatListener extends BroadcastReceiver {

    private HeartbeatRecorder heartbeatRecorder;

    public HeartbeatListener(){
         this.heartbeatRecorder = new HeartbeatRecorder();
    }

    HeartbeatListener(HeartbeatRecorder heartbeatRecorder){
        this.heartbeatRecorder = heartbeatRecorder;
    }
    @Override
    public void onReceive(final Context context, Intent intent) {
        Logger.debug("broadcast received for alarm manager");

        heartbeatRecorder.sendHeartbeatToServer(context);
    }
}
