package com.multunus.onemdm.heartbeat;

import android.content.Intent;

import com.multunus.onemdm.BuildConfig;
import com.multunus.onemdm.network.HeartbeatRecorder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowLog;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class,sdk = 21)
public class HeartbeatListenerTest {

    @Before
    public void setUp() throws Exception {
        ShadowLog.stream = System.out;
    }

    @Test
    public void sendHeartbeat(){
        Intent intent =  new Intent(RuntimeEnvironment.application,HeartbeatListener.class);
        HeartbeatRecorder heartbeatRecorder = mock(HeartbeatRecorder.class);
        HeartbeatListener heartbeatListener = new HeartbeatListener(heartbeatRecorder);
        heartbeatListener.onReceive(RuntimeEnvironment.application,intent);
        verify(heartbeatRecorder).sendHeartbeatToServer(RuntimeEnvironment.application);
    }

    @Test
    public void sendHeartbeatOnReboot(){

        Intent intent = new Intent(Intent.ACTION_BOOT_COMPLETED);
        ShadowApplication application = ShadowApplication.getInstance();
        assertTrue("Heartbeat not registered for on Reboot ",
                application.hasReceiverForIntent(intent));
    }
}
