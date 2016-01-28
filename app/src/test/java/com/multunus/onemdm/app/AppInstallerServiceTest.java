package com.multunus.onemdm.app;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import com.multunus.onemdm.config.Config;
import com.multunus.onemdm.model.App;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class AppInstallerServiceTest {

    App app = new App();

    @Before
    public void setUp(){
        app.setId(45);
        app.setName("Swasthya Samvedana Sena");
        app.setPackageName("com.harvest.activity");
    }

    @Test
    public void showInstallDialog(){
        app.setApkUrl("");

        AppInstallerService appInstallerService = new AppInstallerServiceMock();
        Intent intent = new Intent();
        intent.putExtra(Config.APP_DATA,app);
        appInstallerService.onHandleIntent(intent);
        NotificationManager notificationManager =
                (NotificationManager) RuntimeEnvironment.application.getSystemService(Context.NOTIFICATION_SERVICE);
        assertTrue(shadowOf(notificationManager).size() == 1);

    }

    public class AppInstallerServiceMock extends AppInstallerService{
        protected int getUniqueId(){
            return 123;
        }
    }
}
