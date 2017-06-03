package com.hubli.imperium.imaperiumdiary.Firebase;


import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.hubli.imperium.imaperiumdiary.Data.SPData;

/**
 * Created by Faheem on 29-05-2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.i("Firebase Token ",refreshedToken);
        new SPData(getApplicationContext()).setFirebaseToken(refreshedToken);
    }
}
