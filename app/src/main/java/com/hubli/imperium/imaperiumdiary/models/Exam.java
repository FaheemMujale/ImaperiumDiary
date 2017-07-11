package com.hubli.imperium.imaperiumdiary.models;

/**
 * Created by Rafiq Ahmad on 7/4/2017.
 */

public class Exam {

    private int id;
    private String exam_title;
    private String exam_data;
    private String exam_class;
    private String exam_div;
    private String date;

    public Exam(int id, String title,String data,String exam_class,String exam_div ,String date){
        this.id = id;
        this.exam_title = title;
        this.exam_data = data;
        this.exam_class = exam_class;
        this.exam_div = exam_div;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExam_title() {
        return exam_title;
    }

    public void setExam_title(String exam_title) {
        this.exam_title = exam_title;
    }

    public String getExam_data() {
        return exam_data;
    }

    public void setExam_data(String exam_data) {
        this.exam_data = exam_data;
    }

    public String getExam_class() {
        return exam_class;
    }

    public void setExam_class(String exam_class) {
        this.exam_class = exam_class;
    }

    public String getExam_div() {
        return exam_div;
    }

    public void setExam_div(String exam_div) {
        this.exam_div = exam_div;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
