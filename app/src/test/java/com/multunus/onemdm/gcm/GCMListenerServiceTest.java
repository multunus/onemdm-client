package com.multunus.onemdm.gcm;

import android.content.Intent;
import android.os.Bundle;

import com.multunus.onemdm.BuildConfig;
import com.multunus.onemdm.app.AppInstallerService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowLog;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class,sdk = 21)
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

    @Test
    public void registerGCMListener(){

        Intent intent = new Intent("com.google.android.c2dm.intent.RECEIVE");
        ShadowApplication application = ShadowApplication.getInstance();
        assertTrue("GCM Listener not registered ",
                application.hasReceiverForIntent(intent));
    }

}
