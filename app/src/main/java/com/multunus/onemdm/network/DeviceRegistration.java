package com.multunus.onemdm.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.multunus.onemdm.config.Config;
import com.multunus.onemdm.model.Device;
import com.multunus.onemdm.ui.OneMDMActivity;
import com.multunus.onemdm.util.Logger;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yedhukrishnan on 15/10/15.
 */
public class DeviceRegistration {
    Context context;
    RequestQueue requestQueue;

    public DeviceRegistration(Context context) {
        this.context = context;
        requestQueue = Volley.newRequestQueue(this.context);
    }

    public void sendRegistrationRequestToServer() {
        JsonObjectRequest deviceRegistrationRequest = new JsonObjectRequest(
                Request.Method.POST,
                Config.REGISTRATION_URL,
                getJsonPayload(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        saveSettings(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.debug(error.toString());
                    }
                }
        );
        requestQueue.add(deviceRegistrationRequest);
    }

    private void saveSettings(JSONObject response) {
        String accessToken = "";
        SharedPreferences.Editor editor = this.context.getSharedPreferences(
                OneMDMActivity.ONEMDM_SHARED_PREFERENCE, Context.MODE_PRIVATE).edit();
        try {
            accessToken = response.getString("access_token");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        editor.putString("access_token", accessToken);
        editor.putBoolean("device_registered", true);
        editor.commit();
    }

    public boolean isRegistered() {
        return this.context.getSharedPreferences(OneMDMActivity.ONEMDM_SHARED_PREFERENCE,
                Context.MODE_PRIVATE).getBoolean("device_registered", false);
    }

    private JSONObject getJsonPayload() {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        JSONObject deviceData = new JSONObject();
        try {
            deviceData.put("device", new JSONObject(gson.toJson(getDevice())));
        } catch (JSONException e) {
            e.printStackTrace();
            Logger.debug(e.getMessage());
        }
        return deviceData;
    }

    private Device getDevice() {
        Device device = new Device();
        device.setModel(getDeviceModel());
        device.setImeiNumber(getImeiNumber());
        device.setUniqueId(getAndroidId());
        return device;
    }

    private String getDeviceModel() {
        return Build.MANUFACTURER + " - " + Build.MODEL;
    }

    private String getImeiNumber() {
        TelephonyManager telephonyManager =
                (TelephonyManager) this.context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    private String getAndroidId() {
        return Settings.Secure.getString(
                this.context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}
