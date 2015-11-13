package com.multunus.onemdm.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import com.multunus.onemdm.network.DeviceRegistration;

public class RegistrationService extends IntentService {

    public RegistrationService() {
        super("RegistrationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        DeviceRegistration deviceRegistration = new DeviceRegistration(this);
        deviceRegistration.sendRegistrationRequestToServer();
    }

}
