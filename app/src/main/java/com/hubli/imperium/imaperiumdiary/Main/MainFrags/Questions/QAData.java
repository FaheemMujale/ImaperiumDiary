package com.hubli.imperium.imaperiumdiary.Main.MainFrags.Questions;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.hubli.imperium.imaperiumdiary.Data.MySqlDB;

/**
 * Created by Faheem on 18-05-2017.
 */

public class QAData {

    private String type, questionText, optA, optB, optC, optD, answer;
    private int id, qid,level;
    private boolean watched,answered;
    private MySqlDB mySqlDB;

    public QAData(MySqlDB mySqlDB, Cursor data) {
        this.mySqlDB = mySqlDB;
        id = data.getInt(0);
        qid = data.getInt(1);
        questionText = data.getString(2);
        optA = data.getString(3);
        optB = data.getString(4);
        optC = data.getString(5);
        optD = data.getString(6);
        answer = data.getString(7);
        level = data.getInt(8);
        type = data.getString(9);
        watched = data.getInt(10)==1;
        answered = data.getInt(11)!=0;
    }

    public String getTypeForDiaplay() {
        String  t = type.replace("q-","");
        t = t.replace("_"," ");
        return t;
    }
    public String getType() {
        return type;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getOptA() {
        return optA;
    }

    public String getOptB() {
        return optB;
    }

    public String getOptC() {
        return optC;
    }

    public String getOptD() {
        return optD;
    }

    public String getAnswer() {
        return answer.toUpperCase();
    }

    public int getQid() {
        return qid;
    }

    public void setWatched() {
        mySqlDB.updateQuestionData(id,MySqlDB.WATCHED,"9");
    }

    public void setAnswered(String a) {
        mySqlDB.updateQuestionData(id,MySqlDB.ANSWERED,"'"+a+"'");
        this.answered = true;
    }

    public int getLevel() {
        return level;
    }

    public boolean isWatched() {
        Cursor c = mySqlDB.getQuestions(MySqlDB.WATCHED,id);
        c.moveToNext();
        return c.getInt(0) == 9;
    }

    public String getAnswered() {
        Cursor c = mySqlDB.getQuestions(MySqlDB.ANSWERED,id);
        c.moveToNext();
        return c.getString(0);
    }

    public void updateMarks(int marks) {
        mySqlDB.updateQuestionData(id,MySqlDB.MARKS,marks+"");
    }

}
