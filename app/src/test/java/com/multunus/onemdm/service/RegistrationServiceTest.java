package com.multunus.onemdm.service;

import android.content.Intent;

import com.multunus.onemdm.device.RegistrationService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

@RunWith(RobolectricTestRunner.class)

public class RegistrationServiceTest {

    @Test
    public void testRegistration(){
//        NotificationManager notificationManager = (NotificationManager) RuntimeEnvironment.application
//                .getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent =  new Intent(RuntimeEnvironment.application,RegistrationServiceMock.class);
        RegistrationServiceMock registrationServiceMock = new RegistrationServiceMock();
        registrationServiceMock.onCreate();
//        registrationServiceMock.onHandleIntent(intent);
//        Assert.assertEquals("Expected no notifications", 1, Shadows.shadowOf(notificationManager).size());

    }

    public class RegistrationServiceMock extends RegistrationService {

        public void onHandleIntent( Intent intent ){
            super.onHandleIntent( intent );
        }
    }
}
