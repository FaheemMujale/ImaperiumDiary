package com.hubli.imperium.imaperiumdiary.Data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Faheem on 09-05-2017.
 */

public class SPData {

    public static final String SCHOOL_NUMBER ="num" ;
    public static final String NUMBER_USER = "usernum";
    public static final String SUBJECT = "sub";
    public static final String HOMEWORK_CONTENTS = "detail";
    public static final String HOMEWORK_TITLE = "title";
    public static final String LASTDATE_SUBMISSION = "lastdate";
    public static final String HOMEWORKDATE = "dategiven";
    public static final String HOMEWORK_NUMBER = "hwnu";
    public static final String CLASS = "class";
    public static final String DIVISION = "div";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;



    public static final String USER_NUMBER = "user_number";
    public static final String INSTITUTE_NUMBER = "institute_number";
    public static final String USERNAME = "username";
    public static final String USERTYPE = "usertype";
    public static final String PASSWORD = "password";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String CONTACT = "contact";
    public static final String ADDRESS = "address";
    public static final String IDENTIFICATION = "identification";
    public static final String EMAIL = "email";
    public static final String PROPIC_URL = "profilePic_link";
    public static final String LEVEL = "level"; // has to be calculated

    public static final int STUDENT = 0;
    public static final int TEACHER = 1;
    public static final int PARENT  = 2;


    public SPData(Context context) {
        sharedPreferences = context.getSharedPreferences("USER_SP",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void storeUserData(String jsonString){
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            Set<String> a = new HashSet<>();
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
            editor.putString(USERTYPE,jsonObject.getString(USERTYPE));
            editor.putString(PROPIC_URL,jsonObject.getString(PROPIC_URL));
            editor.commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    //temp
    public void tempstore(String s){
        editor.putString(USER_NUMBER,s);
        editor.commit();
    }

    public String getUserData(String key){
        return sharedPreferences.getString(key,"");
    }

    public int getIdentification(){
        if(sharedPreferences.getString(IDENTIFICATION,"").contentEquals("STUDENT")){
            return STUDENT;
        }else if(sharedPreferences.getString(IDENTIFICATION,"").contentEquals("TEACHER")){
            return TEACHER;
        }else {
            return PARENT;
        }
    }

    public void setQuestionTables(Set<String> set){
        editor.putStringSet("QUESTION_TABLES",set);
        editor.commit();
    }
    public Set<String> getQuestionTables(){
        Set<String> set = new HashSet<>();
        set.add("q-General_knowledge");
        set.add("q-Science_And_Technology");
        return sharedPreferences.getStringSet("QUESTION_TABLES",set);
    }

    public void setQuestionDate(int day){
        editor.putInt("DAY",day);
        editor.commit();
    }
    public boolean isQuestionToday(int currentDay){
        int storedDay = sharedPreferences.getInt("DAY",0);
        return storedDay != currentDay;
    }

    public void tempStoreMarks(String type, String marks) {
        String entry = type+"~"+marks;
        editor.putString("TEMP_MARKS",getTempStoreMarks()+","+entry);
        editor.commit();
    }
    public String getTempStoreMarks() {
        String s = sharedPreferences.getString("TEMP_MARKS","");
        if(s.length() >0) {
            return s.substring(1, s.length());
        }else{
            return "";
        }
    }
    public void clearTempStoredMarkes(){
        editor.remove("TEMP_MARKS");
    }


    // sp store for feeds data as a string
    public String getFeedsData(){
        return   sharedPreferences.getString("FEEDS_DATA","");
    }
    public void storeFeedsData(String postData){
        editor.putString("FEEDS_DATA",postData);
        editor.commit();
    }
    public void appendToFeedsData(String addendString){
        String data = getFeedsData() + addendString;
        data = data.replace("][",",");
        editor.putString("FEEDS_DATA","");
        editor.putString("FEEDS_DATA",data);
        editor.commit();
    }

    public void prefixToFeedsData(String prefixData){
        String d = prefixData + getFeedsData();
        d = d.replace("][",",");
        editor.putString("FEEDS_DATA","");
        editor.putString("FEEDS_DATA",d);
        editor.commit();
    }
}
