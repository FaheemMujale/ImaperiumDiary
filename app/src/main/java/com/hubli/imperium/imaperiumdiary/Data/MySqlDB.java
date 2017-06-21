package com.hubli.imperium.imaperiumdiary.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Faheem on 20-05-2017.
 */

public class MySqlDB extends SQLiteOpenHelper {

    protected String ID = "ID";

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



    protected String MARKS_TABLE = "MARKS_TABLE";
    protected String SUBJECT_NAME = "subject_name";
    protected String EXAM_NAME = "exam_name";
    protected String TOTAL_MARKS = "total_marks";
    protected String MIN_MARKS = "min_marks";
    protected String OBT_MARKS = "obt_marks";
    protected String DATE = "date";


    private String CREATE_MARKS_TABLE = "create table "+MARKS_TABLE+" ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
            SUBJECT_NAME+" VARCHAR(150), "+EXAM_NAME+" VARCHAR(200), "+TOTAL_MARKS+" DECIMAL(5,1), "+MIN_MARKS+" DECIMAL(5,1), "
            +OBT_MARKS+" DECIMAL(5,1), "+DATE+" VARCHAR(50))";



    protected String MESSAGING_TABLE = "MESSAGING_TABLE";
    protected String TIME = "time";
    protected String MESSAGE = "message";
    protected String STATUS = "status";


    private String CREATE_MESSAGING_TABLE = "create table "+MESSAGING_TABLE+" ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
            SPData.USER_NUMBER+" VARCHAR(8), "+SPData.FIRST_NAME+" VARCHAR(200), "+SPData.LAST_NAME+" VARCHAR(200), "+SPData.PROPIC_URL+" VARCHAR(500), "
            +TIME+" VARCHAR(500), "+MESSAGE+" VARCHAR(1000), "+STATUS+" INTEGER(1) )";

    protected SQLiteDatabase db;

    public MySqlDB(Context context) {
        super(context, "Edupline.db", null, 1);
        this.db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_QUESTION_TABLE);
        db.execSQL(CREATE_MARKS_TABLE);
        db.execSQL(CREATE_MESSAGING_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+QUESTION_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+MARKS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+MESSAGING_TABLE);

        onCreate(db);
    }

    public void refreshDB(){
        onUpgrade(db,1,1);
    }
    public void insertQuestions(int qid, String questionText,String type,String optA,String optB,
                                String optC,String optD,String answer){
        String tableName = type.substring(0,type.length()-2);
        int level = Integer.parseInt(type.substring(type.length()-1,type.length()));
        ContentValues contentValues = new ContentValues();
        contentValues.put(QID,qid);
        contentValues.put(QUESTION_TEXT,questionText);
        contentValues.put(OPT_A,optA);
        contentValues.put(OPT_B,optB);
        contentValues.put(OPT_C,optC);
        contentValues.put(OPT_D,optD);
        contentValues.put(ANSWER,answer);
        contentValues.put(LEVEL,level);
        contentValues.put(TYPE,tableName);
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
    public void clearMarksData(){
        db.execSQL("DROP TABLE IF EXISTS " +MARKS_TABLE);
        db.execSQL(CREATE_MARKS_TABLE);
    }
}
