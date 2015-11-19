package com.multunus.onemdm.gcm;

import android.content.ComponentName;
import android.content.Intent;

import com.multunus.onemdm.device.RegistrationService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowLog;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class GCMInstanceIDListenerServiceTest {

    @Before
    public void setUp() throws Exception {
        ShadowLog.stream = System.out;
    }


    @Test
    public void registerDeviceGCMTokenOnGCMTokenRefresh(){
        Intent intent = new Intent(RuntimeEnvironment.application, RegistrationService.class);
        GCMInstanceIDListenerService gcmInstanceIDListenerService = new GCMInstanceIDListenerService();
        gcmInstanceIDListenerService.onTokenRefresh();
        ShadowApplication shadowApplication = shadowOf(RuntimeEnvironment.application);
        Intent serviceIntent = shadowApplication.getNextStartedService();
        assertNotNull(serviceIntent);
        assertEquals(serviceIntent.getComponent().getClassName(),RegistrationService.class.getName());
    }
}
