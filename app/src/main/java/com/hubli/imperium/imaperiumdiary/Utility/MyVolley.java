package com.hubli.imperium.imaperiumdiary.Utility;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hubli.imperium.imaperiumdiary.Interface.IVolleyResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Faheem on 09-05-2017.
 */

public class MyVolley {
    private String url;
    private HashMap<String,String> params;
    private Context context;
    private int RETRY_NUM = DefaultRetryPolicy.DEFAULT_MAX_RETRIES;
    private IVolleyResponse iVolleyResponse;
    private static boolean toastShow = true;

    public MyVolley(Context context, IVolleyResponse iVolleyResponse) {
        this.context = context;
        this.iVolleyResponse = iVolleyResponse;
        params = new HashMap<>();
    }

    public MyVolley setParams(String KEY, String VALUE) {
        params.put(KEY,VALUE);
        return this;
    }

    public MyVolley setUrl(String url) {
        this.url = url;
        return this;
    }

    public MyVolley setRETRY_NUM(int RETRY_NUM) {
        this.RETRY_NUM = RETRY_NUM;
        return this;
    }

    public void connect(){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                clear();
                Log.e("res",response);
                response = response.trim();
                try {
                    if (response == null || response.isEmpty() || response.contains("ERROR") || response.contains("ERROR[]")) {
                        iVolleyResponse.volleyError();
                    } else {
                        iVolleyResponse.volleyResponse(response);
                    }
                }catch (Exception e){
                    iVolleyResponse.volleyError();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                clear();
                iVolleyResponse.volleyError();
                if(toastShow){
                    toastShow = false;
                    Toast.makeText(context,ServerConnect.connectionError(error),Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            toastShow = true;
                        }
                    },5000);
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        RetryPolicy retryPolicy = new DefaultRetryPolicy(2000,RETRY_NUM,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(retryPolicy);
        requestQueue.add(request);
    }
    public void clear(){
        params = new HashMap<String,String>();
        url = null;
    }
}
