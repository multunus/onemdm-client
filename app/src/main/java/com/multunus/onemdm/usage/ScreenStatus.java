package com.multunus.onemdm.usage;

import android.app.KeyguardManager;
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
        if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)
                && !isScreenLocked(context)){
            Logger.debug("Screen and keyboard on ");
            SCREEN_ON = true;
        }else{
            SCREEN_ON = false;
        }
        Logger.debug("Screen_ON = "+SCREEN_ON);
    }

    private boolean isScreenLocked(Context context) {
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(
                Context.KEYGUARD_SERVICE);
        return keyguardManager.inKeyguardRestrictedInputMode();
    }

}