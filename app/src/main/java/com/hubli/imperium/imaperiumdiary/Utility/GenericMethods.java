package com.hubli.imperium.imaperiumdiary.Utility;

import android.os.SystemClock;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Faheem on 11-05-2017.
 */

public class GenericMethods {

    public static String getThumbNailURL(String str){
        return URL.SERVER_URL+str.replace("profilepic","propic_thumb");
    }
    public static boolean checkImageUrl(String url){
        return (url != null && url.contains("jpeg")) ? true:false;
    }

    public static int getDateSum(){
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.DAY_OF_MONTH) + (c.get(Calendar.MONTH) + 1) + c.get(Calendar.YEAR);
    }
}
