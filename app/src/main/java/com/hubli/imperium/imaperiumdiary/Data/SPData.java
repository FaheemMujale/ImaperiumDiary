package com.hubli.imperium.imaperiumdiary.Data;

import android.content.Context;
import android.content.SharedPreferences;

import com.hubli.imperium.imaperiumdiary.Utility.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Faheem on 09-05-2017.
 */

public class SPData {


    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public static final String USER_NUMBER = "user_number";
    public static final String INSTITUTE_NUMBER = "institute_number";
    public static final String INSTITUTE_TYPE = "institute_type";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String CONTACT = "contact";
    public static final String ADDRESS = "address";
    public static final String IDENTIFICATION = "identification";
    public static final String EMAIL = "email";
    public static final String PROPIC_URL = "profilePic_link";
    public static final String LEVEL = "level"; // has to be calculated

    public static final String TEACHER_NUMBER = "teacher_number";
    public static final String CLASS_DIVISION_ID = "cd_id";

    public static final String CLASS = "class";
    public static final String DIVISION = "division";
    public static final String TEACHER_DESIGNATION = "designation";
    public static final String TEACHER_QUALIFICATION = "qualifications";

    public static final String STUDENT_NUMBER = "student_number";
    public static final String ROLL_NUMBER = "roll_number";
    public static final String PARENT_ID = "parent_id";

    // from rafiq
    public static final String SUBJECTS = "subjects";

    public static final int STUDENT = 0;
    public static final int TEACHER = 1;
    public static final int PARENT  = 2;


    public boolean isStudent(){
        return sharedPreferences.getString(IDENTIFICATION,"").contentEquals("student");
    }
    public SPData() {
        sharedPreferences = MyApplication.getContext().getSharedPreferences("USER_SP",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setInstituteID(String userName){
        editor.putString(INSTITUTE_NUMBER, userName.substring(0,2));
        editor.commit();
    }
    public String getInstituteID(){
        return sharedPreferences.getString(INSTITUTE_NUMBER,"");
    }


    public void storeUserData(String jsonString) {
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            editor.putString(USER_NUMBER, jsonObject.getString(USER_NUMBER));
            editor.putString(INSTITUTE_TYPE, jsonObject.getString(INSTITUTE_TYPE));
            editor.putString(USERNAME, jsonObject.getString(PROPIC_URL));
            editor.putString(PASSWORD, jsonObject.getString(IDENTIFICATION));
            editor.putString(FIRST_NAME, jsonObject.getString(FIRST_NAME));
            editor.putString(LAST_NAME, jsonObject.getString(LAST_NAME));
            editor.putString(CONTACT, jsonObject.getString(CONTACT));
            editor.putString(ADDRESS, jsonObject.getString(ADDRESS));
            editor.putString(IDENTIFICATION, jsonObject.getString(IDENTIFICATION));
            editor.putString(EMAIL, jsonObject.getString(EMAIL));
            editor.putString(PROPIC_URL, jsonObject.getString(PROPIC_URL));
            editor.commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




    public void storeClasses(String s){
        editor.putString("mqpclasses",s);
        editor.commit();
    }

    public String getClassesMQP(){
        return sharedPreferences.getString("mqpclasses","");
    }

    public void storeStudentData(String jsonString){
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            editor.putString(STUDENT_NUMBER,jsonObject.getString(STUDENT_NUMBER));
            editor.putString(ROLL_NUMBER,jsonObject.getString(ROLL_NUMBER));
            editor.putString(PARENT_ID,jsonObject.getString(PARENT_ID));
            editor.putString(CLASS,jsonObject.getString(CLASS));
            editor.putString(DIVISION,jsonObject.getString(DIVISION));
            editor.putString(CLASS_DIVISION_ID,jsonObject.getString(CLASS_DIVISION_ID));
            editor.commit();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void storeTeacherData(String jsonString){
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            editor.putString(TEACHER_NUMBER,jsonObject.getString(TEACHER_NUMBER));
            editor.putString(TEACHER_DESIGNATION,jsonObject.getString(TEACHER_DESIGNATION));
            editor.putString(TEACHER_QUALIFICATION,jsonObject.getString(TEACHER_QUALIFICATION));
            editor.putString(CLASS,jsonObject.getString(CLASS));
            editor.putString(DIVISION,jsonObject.getString(DIVISION));
            editor.putString(CLASS_DIVISION_ID,jsonObject.getString(CLASS_DIVISION_ID));
            editor.commit();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean isSchool(){
        return sharedPreferences.getString(INSTITUTE_TYPE,"").toUpperCase().contentEquals("SCHOOL");
    }

    public String getUserData(String key){
        return sharedPreferences.getString(key,"");
    }

    public int getIdentification(){
        if(sharedPreferences.getString(IDENTIFICATION,"").toUpperCase().contentEquals("STUDENT")){
            return STUDENT;
        }else if(sharedPreferences.getString(IDENTIFICATION,"").toUpperCase().contentEquals("TEACHER")){
            return TEACHER;
        }else {
            return PARENT;
        }
    }


//    public void setFirebaseToken(String token){
//        editor.putString(FIREBASE_TOKEN,token);
//        editor.commit();
//    }

//
//    public void setQuestionTables(Set<String> set){
//        editor.putStringSet("QUESTION_TABLES",set);
//        editor.commit();
//    }
//    public Set<String> getQuestionTables(){
//        Set<String> set = new HashSet<>();
//        set.add("q-General_knowledge");
//        set.add("q-Science_And_Technology");
//        return sharedPreferences.getStringSet("QUESTION_TABLES",set);
//    }

    public void setQuestionDate(int day){
        editor.putInt("DAY",day);
        editor.commit();
    }
    public boolean isQuestionToday(int currentDay){
        int storedDay = sharedPreferences.getInt("DAY",0);
        return storedDay == currentDay;
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

    public String getEventsData(){
        return   sharedPreferences.getString("EVENTS_DATA","");
    }

    public void storeEventsData(String events){
        editor.putString("EVENTS_DATA",events);
        editor.commit();
    }
    public void appendToEventsData(String addendString){
        String data = getFeedsData() + addendString;
        data = data.replace("][",",");
        editor.putString("EVENTS_DATA","");
        editor.putString("EVENTS_DATA",data);
        editor.commit();
    }

    public void storeAttendanceData(String data){
        editor.putString("ATTENDANCE_DATA",data);
        editor.commit();
    }

    public String getAttendanceData(){
        return sharedPreferences.getString("ATTENDANCE_DATA",null);
    }

    public void clearData(){
        editor.clear();
        editor.commit();
    }

    public void setQAMarksData(String data){
        editor.putString("QA_MARKS_DATA",data);
        editor.commit();
    }
    public String getQAMarksData() {
        return sharedPreferences.getString("QA_MARKS_DATA",null);
    }

    public void setRanksData(String data){
        editor.putString("RANKS_DATA",data);
        editor.commit();
    }
    public String getRanksData() {
        return sharedPreferences.getString("RANKS_DATA", null);
    }

    public void updateNotificationCount(){
        editor.putInt("NOTIFICATION_COUNT",sharedPreferences.getInt("NOTIFICATION_COUNT",0)+1);
        editor.commit();
    }
    public void resetNotificationCount(){
        editor.putInt("NOTIFICATION_COUNT",0);
        editor.commit();
    }

    public int getNotificationCount(){
        return  sharedPreferences.getInt("NOTIFICATION_COUNT",0);
    }
    public void storeNotificationData(String s){
        editor.putString("NOTIFICATION_DATA",s);
        editor.commit();
    }
    public String getNotificationData(){
        return sharedPreferences.getString("NOTIFICATION_DATA",null);
    }

}
