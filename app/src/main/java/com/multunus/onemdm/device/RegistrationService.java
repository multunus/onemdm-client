package com.multunus.onemdm.device;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.multunus.onemdm.OneMDMService;
import com.multunus.onemdm.config.Config;
import com.multunus.onemdm.network.DeviceRegistration;

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
        startService(new Intent(this.getApplicationContext(), OneMDMService.class));
    }

}
