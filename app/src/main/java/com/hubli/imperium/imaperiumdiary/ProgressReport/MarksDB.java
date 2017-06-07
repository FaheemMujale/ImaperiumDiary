package com.hubli.imperium.imaperiumdiary.ProgressReport;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.hubli.imperium.imaperiumdiary.Data.MySqlDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Faheem on 06-06-2017.
 */

public class MarksDB extends MySqlDB {

    public MarksDB(Context context) {
        super(context);
    }

    public void insertMarksData(String jsonData){
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);

                ContentValues con = new ContentValues();
                con.put(SUBJECT_NAME,json.getString(SUBJECT_NAME));
                con.put(EXAM_NAME,json.getString(EXAM_NAME));
                con.put(TOTAL_MARKS,Float.parseFloat(json.getString(TOTAL_MARKS)));
                con.put(MIN_MARKS,Float.parseFloat(json.getString(MIN_MARKS)));
                con.put(OBT_MARKS,Float.parseFloat(json.getString(OBT_MARKS)));
                con.put(DATE,json.getString(DATE));
                db.insert(MARKS_TABLE,null,con);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Cursor getDistinctSubjects(){
        return db.rawQuery("SELECT DISTINCT "+SUBJECT_NAME+" FROM "+MARKS_TABLE,null);
    }
    public Cursor getDistinctExams(){
        return db.rawQuery("SELECT DISTINCT "+EXAM_NAME+" FROM "+MARKS_TABLE,null);
    }

    public Cursor getMarksFor(String subject, String exam){
        return db.rawQuery("SELECT "+OBT_MARKS+" , "+TOTAL_MARKS+" FROM "+MARKS_TABLE +
                " WHERE "+SUBJECT_NAME+" = '"+subject+"' AND "+ EXAM_NAME +" = '"+exam+"'",null);
    }

    public Cursor getMarksData(String subject){
        return db.rawQuery("SELECT "+DATE+" , "+EXAM_NAME+" , "+TOTAL_MARKS+" , "+MIN_MARKS+" , "+OBT_MARKS+
                " FROM "+MARKS_TABLE + " WHERE "+SUBJECT_NAME+" = '"+subject+"'",null);
    }
}
