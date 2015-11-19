package com.multunus.onemdm.gcm;


import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;
import com.multunus.onemdm.device.RegistrationService;

public class GCMInstanceIDListenerService extends InstanceIDListenerService {
    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. This call is initiated by the
     * InstanceID provider.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        Intent intent = new Intent(this, RegistrationService.class);
        startService(intent);

    }
}