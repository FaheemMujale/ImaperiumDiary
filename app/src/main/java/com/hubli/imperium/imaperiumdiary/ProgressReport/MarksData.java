package com.hubli.imperium.imaperiumdiary.ProgressReport;

/**
 * Created by Naseem on 12-11-2016.
 */

public class MarksData{

    private String date;
    private String exam_name;
    private float total_max;
    private float obtained_max;
    private float percentage;
    private float minMarks;

    public MarksData(String date, String exam_name, float total_max, float min_marks, float obtained_max, float percentage) {
        this.date = date;
        this.exam_name = exam_name;
        this.total_max = total_max;
        this.obtained_max = obtained_max;
        this.minMarks = min_marks;
        this.percentage = percentage;
    }

    public float getMinMarks() {
        return minMarks;
    }

    public String getDate() {
        return date;
    }

    public String getExam_name() {
        return exam_name;
    }

    public float getTotal_max() {
        return total_max;
    }

    public float getObtained_max() {
        return obtained_max;
    }

    public float getPercentage() {
        return percentage;
    }

}
