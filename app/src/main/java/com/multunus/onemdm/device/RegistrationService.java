package com.multunus.onemdm.device;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.multunus.onemdm.config.Config;
import com.multunus.onemdm.network.DeviceRegistration;
import com.multunus.onemdm.usage.ScreenStatus;
import com.multunus.onemdm.usage.UsageTrackingService;

public class RegistrationService extends IntentService {

    private final DeviceRegistration deviceRegistration;

    public RegistrationService() {
        super("RegistrationService");
        this.deviceRegistration = new DeviceRegistration();
    }

    RegistrationService(DeviceRegistration deviceRegistration) {
        super("RegistrationService");
        this.deviceRegistration = deviceRegistration;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        deviceRegistration.sendRegistrationRequestToServer(getApplicationContext());
        Log.d(Config.PREFERENCE_TAG, "inside RegistrationService.onHandleIntent");
        if(Config.TRACK_APP_USAGE) {

            startService(new Intent(this, UsageTrackingService.class));
        }
    }

}
