package com.hubli.imperium.imaperiumdiary.Utility;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

/**
 * Created by Faheem on 09-05-2017.
 */

public class ServerConnect {

    private static boolean toastShow = true;

    public static boolean checkInternetConenction(Activity activity) {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec = (ConnectivityManager) activity.getSystemService(activity.CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
            return true;
        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            noInterent(activity);
            return false;
        }else {
            noInterent(activity);
            return false;
        }
    }

    //Toast message for no internet. Will not show 2nd time within 5 sec
    private static void noInterent(final Activity activity){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(toastShow){
                    toastShow = false;
                    Toast.makeText(activity.getApplicationContext(),"No Internet Connection",Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            toastShow = true;
                        }
                    },5000);
                }
            }
        });
    }

    //returns error message string
    public static String connectionError(VolleyError volleyError){
        String message = null;
        if (volleyError instanceof NetworkError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (volleyError instanceof ServerError) {
            message = "The server could not be found. Please try again after some time!!";
        } else if (volleyError instanceof AuthFailureError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (volleyError instanceof ParseError) {
            message = "Parsing error! Please try again after some time!!";
        } else if (volleyError instanceof NoConnectionError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (volleyError instanceof TimeoutError) {
            message = "Connection TimeOut! Please check your internet connection.";
        }
        return message;
    }
}
