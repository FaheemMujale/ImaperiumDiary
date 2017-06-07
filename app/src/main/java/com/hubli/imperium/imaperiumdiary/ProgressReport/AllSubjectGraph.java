package com.hubli.imperium.imaperiumdiary.ProgressReport;

import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.hubli.imperium.imaperiumdiary.R;

import java.util.ArrayList;

public class AllSubjectGraph extends AppCompatActivity {
    private BarChart barChart;
    private BarData barData;
    private MarksDB marksDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_subject_graph);
        marksDB = new MarksDB(getApplicationContext());
        barChart = (BarChart) findViewById(R.id.chart);
        barChart.animateY(2000);
        barChart.invalidate();

        ArrayList<String> exams = getExamNames();
        ArrayList<BarDataSet> subjectData = getSubjectData(exams);


        if ((exams!= null) && (subjectData!=null)){
            barData = new BarData(exams,subjectData);
            barChart.setData(barData);
        }
    }

    private ArrayList<String> getExamNames() {
        Cursor distinctExams = marksDB.getDistinctExams();
        ArrayList<String> exams = new ArrayList<>();
        if (distinctExams.getCount() > 0) {
            while (distinctExams.moveToNext()) {
                exams.add(distinctExams.getString(0));
            }
            return exams;
        } else {
            return null;
        }
    }


    private ArrayList<BarDataSet> getSubjectData(ArrayList<String> examNames){
        if(examNames != null){
            ArrayList<BarDataSet> dataSets = new ArrayList<>();

            Cursor distinctSubjects = marksDB.getDistinctSubjects();
            if (distinctSubjects.getCount() > 0) {
                int j = 0;
                while (distinctSubjects.moveToNext()) {
                    ArrayList<BarEntry> valueSet = new ArrayList<>();
                    String subjectName = distinctSubjects.getString(0);
                    BarEntry barEntry;
                    for (int i =0; i<examNames.size();i++) {
                        Cursor marks = marksDB.getMarksFor(subjectName, examNames.get(i));
                        if(marks.getCount()>0){
                            marks.moveToFirst();//need while logic
                            float obtMarks = marks.getFloat(0);
                            float totalMarks = marks.getFloat(1);
                            float m = (obtMarks / totalMarks )* 100;
                            barEntry = new BarEntry(m, i);
                            valueSet.add(barEntry);
                        }else{
                            barEntry = new BarEntry(0, i);
                            valueSet.add(barEntry);
                        }
                    }
                    j++;
                    BarDataSet barDataSet = new BarDataSet(valueSet, subjectName);
                    barDataSet.setColor(Color.rgb(j*20, j*40, j*80));
                    barDataSet.setValueTextSize(10);
                    barDataSet.setValueTextColor(Color.BLUE);
                    dataSets.add(barDataSet);
                }
                return dataSets;
            } else {
                return null;
            }
        }else {
            return null;
        }
    }
}
