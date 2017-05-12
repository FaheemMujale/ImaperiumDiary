package com.hubli.imperium.imaperiumdiary.Utility;

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
}
