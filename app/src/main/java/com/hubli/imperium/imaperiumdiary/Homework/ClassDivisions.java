package com.hubli.imperium.imaperiumdiary.Homework;

/**
 * Created by Faheem on 03-06-2017.
 */

public class ClassDivisions {

    private String classes;
    private String divisions[];

    public ClassDivisions(String classes, String divisions) {
        this.classes = classes;
        this.divisions = divisions.replace("[\"","").replace("\"]","").split("\",\"");
    }

    public String getClassName() {
        return classes;
    }

    public String[] getDivisions() {
        return divisions;
    }
}
