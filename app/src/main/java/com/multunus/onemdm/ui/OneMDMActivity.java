package com.multunus.onemdm.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.multunus.onemdm.BuildConfig;
import com.multunus.onemdm.R;
import com.multunus.onemdm.network.DeviceRegistration;
import com.multunus.onemdm.util.Logger;
import io.fabric.sdk.android.Fabric;

public class OneMDMActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
        }
        setContentView(R.layout.activity_one_mdm);
        if(isNetworkAvailable()) {
            registerDevice();
        }
        else{
            notifyFailure();
        }
    }


    private void registerDevice() {
        DeviceRegistration deviceRegistration = new DeviceRegistration(this);
        deviceRegistration.sendRegistrationRequestToServer();
    }

    private boolean isNetworkAvailable(){
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    private void notifyFailure() {
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage("Make sure that you are connected to the internet and then retry")
                .setTitle("Connectivity Issue")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

}
