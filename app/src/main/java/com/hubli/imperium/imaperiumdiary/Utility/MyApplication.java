package com.hubli.imperium.imaperiumdiary.Utility;

import android.app.Application;
import android.content.Context;

/**
 * Created by Faheem on 03-06-2017.
 */

public class MyApplication extends Application{

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
