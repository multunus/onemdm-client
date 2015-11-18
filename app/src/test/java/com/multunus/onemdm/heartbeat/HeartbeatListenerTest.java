package com.multunus.onemdm.heartbeat;

import android.content.Intent;

import com.multunus.onemdm.device.RegistrationService;
import com.multunus.onemdm.heartbeat.HeartbeatListener;
import com.multunus.onemdm.network.HeartbeatRecorder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class HeartbeatListenerTest {

    @Test
    public void sendHeartbeat(){
        Intent intent =  new Intent(RuntimeEnvironment.application,RegistrationService.class);
        HeartbeatRecorder heartbeatRecorder = mock(HeartbeatRecorder.class);
        HeartbeatListener heartbeatListener = new HeartbeatListener(heartbeatRecorder);
        heartbeatListener.onReceive(RuntimeEnvironment.application,intent);
        verify(heartbeatRecorder).sendHeartbeatToServer(RuntimeEnvironment.application);
    }
}
