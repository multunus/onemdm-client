package com.multunus.onemdm.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.multunus.onemdm.config.Config;
import com.multunus.onemdm.model.AppUsage;
import com.multunus.onemdm.util.Logger;
import com.rollbar.android.Rollbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class AppUsageRecorder {

    private long lastIdOfAppDataToBeSynced;
    private long firstIdOfDataToBeSynced;

    public void sendAppUsageDataToServer(final Context context) {
        final Iterator<AppUsage> appUsages = getAppUsage();
        if(appUsages.hasNext()) {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            JsonObjectRequest request = new CustomJsonObjectRequest(
                    Request.Method.POST,
                    Config.APP_USAGE_TRACKING_URL,
                    getJsonPayload(appUsages),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Logger.debug("Successfully synced data for date ");
                            removeSyncedData();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Logger.warning(error.toString());
                        }
                    },
                    context
            );
            requestQueue.add(request);
        }
    }

    private JSONObject getJsonPayload(Iterator<AppUsage> appUsages) {
        JSONArray appUsageJson = new JSONArray();

        int ctr = 0;
        while (appUsages.hasNext() && ctr < Config.MAX_COUNT_OF_DATA_TO_BE_SYNCED){
            try {
                AppUsage appUsage = appUsages.next();
                JSONObject appUsageData = new JSONObject();
                appUsageData.put("package_name",appUsage.getPackageName());
                appUsageData.put("usage_duration_in_seconds",
                        appUsage.getAppUsageDurationPerDayInSeconds());
                appUsageData.put("used_on",appUsage.getAppUsedOn());
                appUsageJson.put(appUsageData);
                ctr++;
                if(ctr == 1){
                    this.lastIdOfAppDataToBeSynced = appUsage.getId();
                }
                else{
                    this.firstIdOfDataToBeSynced = appUsage.getId();
                }
            }
            catch (Exception e) {
                Logger.error(e);
                Rollbar.reportException(e);
            }
        }
        JSONObject data = new JSONObject();
        try {
            data.put("app_usage", appUsageJson);
        }
        catch (Exception e) {
            Logger.error(e);
            Rollbar.reportException(e);
        }
        Logger.debug("No of of app usage data to be synced " + appUsageJson.length());
        return data;
    }

    private Iterator<AppUsage> getAppUsage() {
        RealmResults<AppUsage> results = Realm.getDefaultInstance().where(AppUsage.class).
                findAllSorted("id", Sort.DESCENDING);
        return results.iterator();
    }

    private void removeSyncedData(){
        Logger.debug("data to be deleted is between " + firstIdOfDataToBeSynced + "and " + lastIdOfAppDataToBeSynced);
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        realm.where(AppUsage.class)
                .between("id",firstIdOfDataToBeSynced,lastIdOfAppDataToBeSynced).findAll().clear();
        realm.commitTransaction();
        Logger.debug("No of records pending to be synced = " + realm.where(AppUsage.class).count());

    }
}
