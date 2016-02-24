package com.multunus.onemdm.usage;

import android.content.Intent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class ScreenStatusTest {

    ScreenStatusTracker screenStatus;
    @Before
    public void setUp(){
        screenStatus = new ScreenStatusTracker();
    }

    @Test
    public void screenStatusToBeOnWhenScreenIsOn(){
        screenStatus.onReceive(RuntimeEnvironment.application,new Intent(Intent.ACTION_USER_PRESENT));
        assertTrue("Should start tracking usage", screenStatus.scheduledFuture != null);
    }

    @Test
    public void screenStatusToBeOffWhenScreenIsOff(){
        screenStatus.onReceive(RuntimeEnvironment.application,new Intent(Intent.ACTION_USER_PRESENT));
        screenStatus.onReceive(RuntimeEnvironment.application,new Intent(Intent.ACTION_SCREEN_OFF));
        assertTrue("Should stop tracking app usage", screenStatus.scheduledFuture == null);
    }
}