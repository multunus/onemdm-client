package com.multunus.onemdm.usage;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class ScreenStatusTest {

    ScreenStatus screenStatus;
    @Before
    public void setUp(){
        screenStatus = new ScreenStatus();
    }

    @Test
    public void defaultStatusToBeFalse(){
        assertFalse("Screen should be on", ScreenStatus.SCREEN_ON);
    }

    @Test
    public void screenStatusToBeOnWhenScreenIsOn(){
        screenStatus.onReceive(RuntimeEnvironment.application,new Intent(Intent.ACTION_SCREEN_ON));
        assertTrue("Screen should be on", ScreenStatus.SCREEN_ON);
    }

    @Test
    public void screenStatusToBeOffKeyboardIsLocked(){
        KeyguardManager mgr = (KeyguardManager) RuntimeEnvironment.application.
                getSystemService(Context.KEYGUARD_SERVICE);
        shadowOf(mgr).setinRestrictedInputMode(true);
        screenStatus.onReceive(RuntimeEnvironment.application, new Intent(Intent.ACTION_SCREEN_ON));
        assertFalse("Screen should be off", ScreenStatus.SCREEN_ON);
    }

    @Test
    public void screenStatusToBeOffWhenScreenIsOff(){
        screenStatus.onReceive(RuntimeEnvironment.application,new Intent(Intent.ACTION_SCREEN_ON));
        screenStatus.onReceive(RuntimeEnvironment.application,new Intent(Intent.ACTION_SCREEN_OFF));
        assertFalse("Screen should be off", ScreenStatus.SCREEN_ON);
    }
}