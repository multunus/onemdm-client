package com.multunus.onemdm.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.multunus.onemdm.R;
import com.multunus.onemdm.network.DeviceRegistration;
import com.multunus.onemdm.util.Logger;

public class OneMDMActivity extends AppCompatActivity {
    public static String ONEMDM_SHARED_PREFERENCE = "onemdm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_mdm);
        registerDeviceIfNotRegistered();
    }

    private void registerDeviceIfNotRegistered() {
        DeviceRegistration deviceRegistration = new DeviceRegistration(this);
        if(deviceRegistration.isRegistered()) {
            deviceRegistration.sendRegistrationRequestToServer();
        }
    }

}
