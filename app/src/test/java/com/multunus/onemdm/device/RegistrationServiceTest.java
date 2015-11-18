package com.multunus.onemdm.device;

import android.content.Intent;

import com.multunus.onemdm.network.DeviceRegistration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)

public class RegistrationServiceTest {

    @Test
    public void testRegistration(){
        Intent intent =  new Intent(RuntimeEnvironment.application,RegistrationService.class);
        DeviceRegistration deviceRegistration = mock(DeviceRegistration.class);
        RegistrationService registrationService = new RegistrationService(deviceRegistration);
        registrationService.onHandleIntent(intent);
    }
}
