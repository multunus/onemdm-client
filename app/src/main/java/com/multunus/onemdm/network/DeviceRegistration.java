package com.multunus.onemdm.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.multunus.onemdm.config.Config;
import com.multunus.onemdm.model.Device;
import com.multunus.onemdm.util.Helper;
import com.multunus.onemdm.util.Logger;
import com.rollbar.android.Rollbar;

import org.json.JSONException;
import org.json.JSONObject;

public class DeviceRegistration {

    public void sendRegistrationRequestToServer(final Context context) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest deviceRegistrationRequest = new JsonObjectRequest(
                Request.Method.POST,
                Config.REGISTRATION_URL,
                getJsonPayload(context),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        saveSettings(context,response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.warning(error.toString());
                    }
                }
        );
        requestQueue.add(deviceRegistrationRequest);
    }

    private void saveSettings(Context context,JSONObject response) {
        String accessToken = "";
        long nextHeartbeatTime = System.currentTimeMillis();
        SharedPreferences.Editor editor = context.getSharedPreferences(
                Config.PREFERENCE_TAG, Context.MODE_PRIVATE).edit();
        try {
            accessToken = response.getString(Config.ACCESS_TOKEN);
            nextHeartbeatTime = response.getLong("next_heartbeat_time") * 1000;
        } catch (JSONException e) {
            Logger.warning("Exception while registering",e);
        }
        editor.putString(Config.ACCESS_TOKEN, accessToken);
        editor.apply();
        new HeartbeatRecorder().configureNextHeartbeatWithMilliSeconds(context,nextHeartbeatTime);
    }

    private JSONObject getJsonPayload(Context context) {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        JSONObject deviceData = new JSONObject();
        try {
            deviceData.put("device", new JSONObject(gson.toJson(getDevice(context))));
        } catch (Exception e) {
            Logger.error(e);
            Rollbar.reportException(e);
        }
        Logger.debug("Device data to be send " + deviceData);
        return deviceData;
    }

    private Device getDevice(Context context) throws Exception{
        Device device = new Device();
        device.setModel(getDeviceModel());
        device.setImeiNumber(getImeiNumber(context));
        device.setUniqueId(Helper.getAndroidId(context));
        InstanceID instanceID = InstanceID.getInstance(context);
        String gcmToken = instanceID.getToken(Config.GCM_SENDER_ID,
                GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
        Logger.debug("GCM Registration Token: " + gcmToken);
        device.setGcmToken(gcmToken);
        device.setClientVersion(getAppVersion(context));
        device.setOsVersion(Build.VERSION.RELEASE);
        return device;
    }

    private String getDeviceModel() {
        return Build.MANUFACTURER + " - " + Build.MODEL;
    }

    private String getAppVersion(Context context) {
        try{
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            return info.versionName + " - " + info.versionCode;
        }
        catch (Exception ex){
            Rollbar.reportException(ex);
            Logger.error(ex);
        }
        return "";
    }
    private String getImeiNumber(Context context) {
        try {
            TelephonyManager telephonyManager =
                    (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return telephonyManager.getDeviceId();
        }
        catch (Exception ex){
            //Ignoring as its a known bug in Marshmellow
        }
        return "";
    }
}
