package com.multunus.onemdm.usage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.multunus.onemdm.util.Logger;

public class ScreenStatus extends BroadcastReceiver {
    static boolean SCREEN_ON = false;

    public ScreenStatus() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.debug("Screen status on = "+intent.getAction().equals(Intent.ACTION_SCREEN_ON));
        Logger.debug("Screen status off = "+intent.getAction().equals(Intent.ACTION_SCREEN_OFF));
        if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
            SCREEN_ON = true;
        }else if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
            SCREEN_ON = false;
        }
    }
}