package com.multunus.onemdm.network;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.multunus.onemdm.config.Config;
import com.multunus.onemdm.util.Logger;

import java.util.HashMap;
import java.util.Map;

public class CustomJsonObjectRequest extends JsonObjectRequest
{
    private Context context;
    public CustomJsonObjectRequest(int method, String url,
                                   Response.Listener listener,
                                   Response.ErrorListener errorListener,
                                   Context context)
    {
        super(method, url, listener, errorListener);
        this.context = context;
    }

    @Override
    public Map getHeaders() throws AuthFailureError {
        String token =  context.getSharedPreferences(Config.PREFERENCE_TAG,
                Context.MODE_PRIVATE).getString(Config.ACCESS_TOKEN, "");
        Map headers = new HashMap();
        headers.put("Authorization", "Token token="+token);
        Logger.debug("added header " + token);
        return headers;
    }

}
