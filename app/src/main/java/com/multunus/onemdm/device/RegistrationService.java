package com.multunus.onemdm.device;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import com.multunus.onemdm.network.DeviceRegistration;

public class RegistrationService extends IntentService {

    private final DeviceRegistration deviceRegistration;

    public RegistrationService() {
        super("RegistrationService");
        this.deviceRegistration = new DeviceRegistration(this);
    }

    RegistrationService(DeviceRegistration deviceRegistration) {
        super("RegistrationService");
        this.deviceRegistration = deviceRegistration;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        deviceRegistration.sendRegistrationRequestToServer();
    }

}
