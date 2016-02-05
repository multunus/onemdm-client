package com.multunus.onemdm.usage;

import android.content.Intent;

import com.multunus.onemdm.BuildConfig;
import com.multunus.onemdm.heartbeat.HeartbeatListener;
import com.multunus.onemdm.network.AppUsageRecorder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class,sdk = 21)

public class AppUsageDataSyncerServiceTest {
    @Test
    public void sendHeartbeat(){
        Intent intent =  new Intent(RuntimeEnvironment.application,HeartbeatListener.class);
        AppUsageRecorder appUsageRecorder = mock(AppUsageRecorder.class);
        AppUsageDataSyncer appUsageDataSyncer = new AppUsageDataSyncer(appUsageRecorder);
        appUsageDataSyncer.onReceive(RuntimeEnvironment.application, intent);
        verify(appUsageRecorder).sendAppUsageDataToServer(RuntimeEnvironment.application);
    }

    @Test
    public void sendHeartbeatOnReboot(){

        Intent intent = new Intent(Intent.ACTION_BOOT_COMPLETED);
        ShadowApplication application = ShadowApplication.getInstance();
        assertTrue("Heartbeat not registered for on Reboot ",
                application.hasReceiverForIntent(intent));
    }
}
