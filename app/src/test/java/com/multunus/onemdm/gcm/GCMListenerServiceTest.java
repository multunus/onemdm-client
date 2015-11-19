package com.multunus.onemdm.gcm;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

import com.multunus.onemdm.app.AppInstallerService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowLog;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class GCMListenerServiceTest {

    @Before
    public void setUp() throws Exception {
        ShadowLog.stream = System.out;
    }

    @Test
    public void installAppOnGCMPushReceived(){
        GCMListenerService gcmListenerService = new GCMListenerService();
        Bundle data = new Bundle();
        String json = "{\"id\":45,\"name\":\"Swasthya Samvedana Sena\",\"package_name\":\"com.harvest.activity\",\"apk_url\":\"\"}";
        data.putString("message",json);
        gcmListenerService.onMessageReceived("RandomInstanceID", data);
        ShadowApplication shadowApplication = shadowOf(RuntimeEnvironment.application);
        Intent intent = shadowApplication.getNextStartedService();
        assertNotNull(intent);
        assertEquals(intent.getComponent().getClassName(), AppInstallerService.class.getName());
        assertNotNull(intent.getParcelableExtra(
                com.multunus.onemdm.config.Config.APP_DATA));
    }
}
