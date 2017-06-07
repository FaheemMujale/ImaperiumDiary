package com.hubli.imperium.imaperiumdiary.ProgressReport;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.hubli.imperium.imaperiumdiary.R;

import java.util.ArrayList;
import java.util.List;

public class SingleSubjectGraph extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_subject_graph);
        LineChart chart = (LineChart) findViewById(R.id.chart);

        String subjectName = getIntent().getStringExtra("subject");

        List<MarksData> data = SingleSubjectMarksList.data;
        List<Entry> entries = new ArrayList<Entry>();
        List<String> examNames = new ArrayList<>();
        if (!data.isEmpty()) {
            for (int i = 0; i < data.size(); i++) {
                MarksData currentItem = data.get(i);
                entries.add(new Entry(currentItem.getPercentage(), i));
                examNames.add(currentItem.getExam_name() + "");
            }
            LineDataSet dataset = new LineDataSet(entries, subjectName);
            dataset.disableDashedLine();
            dataset.setValueTextSize(12);
            dataset.setValueTextColor(Color.BLACK);
            dataset.setCircleSize(4);
            dataset.setCircleColorHole(Color.BLUE);
            dataset.getValueTypeface();
            dataset.setColor(Color.BLACK);
            dataset.setDrawFilled(true);
            dataset.setLineWidth(1f);
            dataset.setDrawCircleHole(true);
            dataset.setFillColor(Color.GREEN);
            dataset.setFillAlpha(70);
            LineData linedata = new LineData(examNames, dataset);
            chart.setData(linedata);
            chart.animateXY(00, 1500);
            chart.invalidate();
        }
    }
}
