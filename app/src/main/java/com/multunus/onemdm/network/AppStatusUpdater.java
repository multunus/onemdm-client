package com.multunus.onemdm.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.multunus.onemdm.config.Config;
import com.multunus.onemdm.util.Logger;

import org.json.JSONObject;

public class AppStatusUpdater {

    private Context context;
    private RequestQueue requestQueue;
    private long appInstallationId;

    public AppStatusUpdater(Context context,long appInstallationId) {
        this.context = context;
        requestQueue = Volley.newRequestQueue(this.context);
        this.appInstallationId = appInstallationId;
    }

    public void updateAppInstallationStatus() {

        JsonObjectRequest request = new CustomJsonObjectRequest(
                Request.Method.POST,
                Config.APP_INSTALLED_URL+"?id="+appInstallationId,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                        }
                        catch (Exception ex){

                        }
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
