package com.hubli.imperium.imaperiumdiary.Profile;

import android.util.Log;

/**
 * Created by Faheem on 10-06-2017.
 */

public class QAMarksData {
    private String tableName;
    private int marks;
    private int total;
    private static int grandMarks = 0, grandTotal = 0;

    public QAMarksData(String tableName, int marks, int total) {
        Log.e("dat",tableName);
        this.tableName = tableName.replace("q-","").replace("_"," ");
        this.marks = marks;
        this.total = total;
        this.grandMarks += marks;
        this.grandTotal += total;
    }

    public static int getGrandMarks() {
        return grandMarks;
    }

    public static int getGrandTotal() {
        return grandTotal;
    }

    public String getTableName() {
        return tableName;
    }

    public int getTotal() {
        return total;
    }

    public int getMarks() {
        return marks;
    }
}
