package com.multunus.onemdm.usage;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowLog;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class UsageTrackingServiceTest extends TestCase {

    @Before
    public void setUp() throws Exception {
        ShadowLog.stream = System.out;
    }

    @Test
    public void startUsageTracking(){
        Application application = RuntimeEnvironment.application;
        UsageTrackingService usageTrackingService = new UsageTrackingService();
        usageTrackingService.appUsageCollector = mock(UsageTrackingService.AppUsageCollector.class);
        usageTrackingService.onStartCommand(new Intent(application,UsageTrackingService.class),0,10);
        verify(usageTrackingService.appUsageCollector).collectUsageData();
    }
}