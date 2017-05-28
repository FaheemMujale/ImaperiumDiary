package com.hubli.imperium.imaperiumdiary.models;

import java.util.ArrayList;

/**
 * Created by Rafiq Ahmad on 5/23/2017.
 */

public class Timetableinfo {


    private String name;

    private ArrayList<String> data;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getData() {
        return data;
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
    }
}
