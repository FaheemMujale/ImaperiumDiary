package com.hubli.imperium.imaperiumdiary.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Faheem on 20-05-2017.
 */

public class MySqlDB extends SQLiteOpenHelper {

    private String ID = "ID";

    private String QUESTION_TABLE = "QUESTION_TABLE";
    private String QID = "QID";
    private String QUESTION_TEXT = "QUESTION_TEXT";
    private String OPT_A = "OPTA";
    private String OPT_B = "OPTB";
    private String OPT_C = "OPTC";
    private String OPT_D = "OPTD";
    private String ANSWER = "ANSWER";
    private String LEVEL = "LEVEL";
    private String TYPE = "TYPE";
    public static String WATCHED = "WATCHED";
    public static String ANSWERED = "ANSWERED";
    public static String MARKS = "MARKS";
    private String CREATE_QUESTION_TABLE = "create table "+QUESTION_TABLE+" ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
            QID+" INTEGER(8), "+QUESTION_TEXT+" VARCHAR(500), "+OPT_A+" VARCHAR(100), "+OPT_B+" VARCHAR(100), "+OPT_C+" VARCHAR(100), "+
            OPT_D+" VARCHAR(100), "+ANSWER+" VARCHAR(2), "+LEVEL+" INTEGER(2), "+TYPE+" VARCHAR(32), "+WATCHED+" INTEGER(1)," +
            " "+ANSWERED+" VARCHAR(3), "+MARKS+" INTEGER(3))";

    SQLiteDatabase db;
    public MySqlDB(Context context) {
        super(context, "Edupline.db", null, 1);
        this.db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_QUESTION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+QUESTION_TABLE);
    }

    public void insertQuestions(int qid, String questionText,String type,String optA,String optB,
                                String optC,String optD,String answer,int level){
        ContentValues contentValues = new ContentValues();
        contentValues.put(QID,qid);
        contentValues.put(QUESTION_TEXT,questionText);
        contentValues.put(OPT_A,optA);
        contentValues.put(OPT_B,optB);
        contentValues.put(OPT_C,optC);
        contentValues.put(OPT_D,optD);
        contentValues.put(ANSWER,answer);
        contentValues.put(LEVEL,level);
        contentValues.put(TYPE,type);
        contentValues.put(WATCHED,0);
        contentValues.put(ANSWERED,0);
        contentValues.put(MARKS,0);
        db.insert(QUESTION_TABLE,null,contentValues);
    }
    public void updateQuestionData(int id,String name, String val){
         db.execSQL("Update "+QUESTION_TABLE+" SET "+name+" = "+val+" Where "+ID+" = "+id);
    }
    public Cursor getQuestions(String params, int id){
        String condition = "";
        if(id != -1){
            condition = " Where "+ID+" = "+id;
        }
        return db.rawQuery("select "+params+" from "+QUESTION_TABLE+condition,null);
    }
    public void clearQuestionData(){
        db.execSQL("DROP TABLE IF EXISTS " +QUESTION_TABLE);
        db.execSQL(CREATE_QUESTION_TABLE);
    }

}