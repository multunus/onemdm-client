package com.multunus.onemdm.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import com.multunus.onemdm.config.Config;
import com.multunus.onemdm.network.AppStatusUpdater;

import org.hamcrest.core.AnyOf;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.fakes.RoboSharedPreferences;
import org.robolectric.shadows.ShadowLog;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)

public class AppInstallListenerTest {
    String packageName = "com.multunus.onemdm";
    long appInstallationId = 1;


    @Before
    public void setUp() throws Exception {
        ShadowLog.stream = System.out;
        SharedPreferences.Editor editor = RuntimeEnvironment.application.getSharedPreferences(Config.PREFERENCE_TAG,
                Context.MODE_PRIVATE).edit();

        editor.putLong(packageName, appInstallationId);
        editor.apply();
    }

    @Test
    public void updateInstallationStatusIfAppWasPushed(){
        AppStatusUpdater appStatusUpdater = mock(AppStatusUpdater.class);
        AppInstallListener appInstallListener = new AppInstallListener(appStatusUpdater);
        Intent intent = new Intent();
        intent.setData(Uri.parse("package:" + packageName));

        appInstallListener.onReceive(RuntimeEnvironment.application,intent);

        verify(appStatusUpdater).updateAppInstallationStatus(RuntimeEnvironment.application,appInstallationId);
    }

    @Test
    public void noUpdateofInstallStatusIfNotPushed(){
        AppStatusUpdater appStatusUpdater = mock(AppStatusUpdater.class);
        AppInstallListener appInstallListener = new AppInstallListener(appStatusUpdater);
        Intent intent = new Intent();
        intent.setData(Uri.parse("package:com.facebook"));
        appInstallListener.onReceive(RuntimeEnvironment.application, intent);
        verify(appStatusUpdater,never()).updateAppInstallationStatus(RuntimeEnvironment.application,-1);
    }
}
