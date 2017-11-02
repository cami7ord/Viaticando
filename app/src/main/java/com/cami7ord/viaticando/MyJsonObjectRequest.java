package com.cami7ord.viaticando;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyJsonObjectRequest extends JsonObjectRequest {


    public MyJsonObjectRequest(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
        setupRetries(method);
    }

    private void setupRetries(int method) {
        if (method == Method.POST || method == Method.PUT || method == Method.DELETE) {
            setShouldCache(false);
            setRetryPolicy(new DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }
    }

    @Override
    public String getBodyContentType() {
        return "application/json";
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String,String> params = new HashMap<>();
        params.put("Accept", "application/json");
        params.put("Content-Type", "application/json");
        //params.put(BuildConfig.HEADER_TOKEN, MySharedPrefs.getInstance().getString(Constants.SharedPrefsKeys.TOKEN_KEY));
        return params;
    }

}
