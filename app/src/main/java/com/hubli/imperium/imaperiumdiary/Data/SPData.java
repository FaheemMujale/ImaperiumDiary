package com.hubli.imperium.imaperiumdiary.Data;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Faheem on 09-05-2017.
 */

public class SPData {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public static final String USER_NUMBER = "user_number";
    public static final String INSTITUTE_NUMBER = "institute_number";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String CONTACT = "contact";
    public static final String ADDRESS = "address";
    public static final String IDENTIFICATION = "identification";
    public static final String EMAIL = "email";
    public static final String PROPIC_URL = "pp_url";
    public static final String RESP = "resp";

    public SPData(Context context) {
        sharedPreferences = context.getSharedPreferences("USER_SP",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void storeUserData(String jsonString){
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            editor.putString(USER_NUMBER,jsonObject.getString(USER_NUMBER));
            editor.putString(INSTITUTE_NUMBER,jsonObject.getString(INSTITUTE_NUMBER));
            editor.putString(USERNAME,jsonObject.getString(PROPIC_URL));
            editor.putString(PASSWORD,jsonObject.getString(IDENTIFICATION));
            editor.putString(FIRST_NAME,jsonObject.getString(FIRST_NAME));
            editor.putString(LAST_NAME,jsonObject.getString(LAST_NAME));
            editor.putString(CONTACT,jsonObject.getString(CONTACT));
            editor.putString(ADDRESS,jsonObject.getString(ADDRESS));
            editor.putString(IDENTIFICATION,jsonObject.getString(IDENTIFICATION));
            editor.putString(EMAIL,jsonObject.getString(EMAIL));
            editor.putString(PROPIC_URL,jsonObject.getString(PROPIC_URL));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getResp(String key){return  sharedPreferences.getString(key,"");}
    public String getUserData(String key){
        return sharedPreferences.getString(key,"");
    }

    public boolean isStudent(){
        return sharedPreferences.getString(IDENTIFICATION,"").contentEquals("STUDENT");
    }


}
