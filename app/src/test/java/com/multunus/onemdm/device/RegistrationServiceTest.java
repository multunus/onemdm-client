package com.multunus.onemdm.device;

import android.content.Intent;

import com.multunus.onemdm.config.Config;
import com.multunus.onemdm.network.DeviceRegistration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowLog;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)

public class RegistrationServiceTest {

    @Before
    public void setUp() throws Exception {
        ShadowLog.stream = System.out;
    }

    @Test
    public void testRegistration(){
        Config.TRACK_APP_USAGE = true;
        Intent intent =  new Intent(RuntimeEnvironment.application,RegistrationService.class);
        DeviceRegistration deviceRegistration = mock(DeviceRegistration.class);
        RegistrationService registrationService = new RegistrationService(deviceRegistration);
        registrationService.onHandleIntent(intent);
        verify(deviceRegistration).sendRegistrationRequestToServer(RuntimeEnvironment.application);
        ShadowApplication shadowApplication = shadowOf(RuntimeEnvironment.application);
        assertNotNull(" Tracking service not started ",shadowApplication.getNextStartedService());
    }
}
