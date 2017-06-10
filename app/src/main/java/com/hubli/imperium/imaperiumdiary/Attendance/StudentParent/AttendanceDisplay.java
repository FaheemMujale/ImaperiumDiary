package com.hubli.imperium.imaperiumdiary.Attendance.StudentParent;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.hubli.imperium.imaperiumdiary.Attendance.Teacher.GiveAttendance.GiveAttendance;
import com.hubli.imperium.imaperiumdiary.Data.SPData;
import com.hubli.imperium.imaperiumdiary.Interface.IVolleyResponse;
import com.hubli.imperium.imaperiumdiary.R;
import com.hubli.imperium.imaperiumdiary.Utility.MyVolley;
import com.hubli.imperium.imaperiumdiary.Utility.ServerConnect;
import com.hubli.imperium.imaperiumdiary.Utility.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class AttendanceDisplay extends Fragment {


    private SPData spData;
    private ProgressBar progressBar;
    private TextView notAvailable;
    private List<AttendanceData> attendance = new ArrayList<>();
    private SimpleDateFormat dateFormatForMonth;
    public final int RED= 0xc8ff0000;
    public final int ORANGE = 0xc8ff7d03;
    private CompactCalendarView calendarView;
    private TextView presentView, absentView, leavesView;

    public AttendanceDisplay() {
        // Required empty public constructor
    }


    private View rootView;
    PieChart pieChart;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_attandance_display, container, false);
        spData = new SPData();
        calendarView = (CompactCalendarView) rootView.findViewById(R.id.compactcalendar_view);
         pieChart = (PieChart) rootView.findViewById(R.id.attendancePi);

        getActivity().setTitle("Attendance");

        dateFormatForMonth = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());
        getActivity().setTitle(dateFormatForMonth.format(calendarView.getFirstDayOfCurrentMonth()));
        progressBar = (ProgressBar) rootView.findViewById(R.id.attendance_loading);
        notAvailable = (TextView) rootView.findViewById(R.id.attendanceStdNotAvailable);
        checkInternetConnection();
        return rootView;
    }

    public void checkInternetConnection() {
        if(ServerConnect.checkInternetConenction(getActivity())) {
            getAttendanceData();
        } else {
            parseJson(spData.getAttendanceData());
        }
    }

    private void getAttendanceData() {
        MyVolley volley = new MyVolley(getActivity().getApplicationContext(), new IVolleyResponse() {
            @Override
            public void volleyResponse(String result) {
                spData.storeAttendanceData(result);
                parseJson(result);
            }

            @Override
            public void volleyError() {
                notAvailable.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        }).setUrl(URL.ATTENDANCE_FETCH)
                .setParams(SPData.USER_NUMBER,spData.getUserData(SPData.USER_NUMBER));
        try {Log.e("id",getArguments().getString("subject_id"));
            volley.setParams("subject_id",getArguments().getString("subject_id"));
        }catch (Exception e){}
            volley.connect();
    }


    private void parseJson(String result){
        try {
            progressBar.setVisibility(View.GONE);
            notAvailable.setVisibility(View.GONE);
            JSONArray json = new JSONArray(result);
            for (int i = 0; i <= json.length() - 1; i++) {
                JSONObject jsonobj = json.getJSONObject(i);
                attendance.add(new AttendanceData(jsonobj.getString("year"),
                        jsonobj.getString("day"), jsonobj.getString("month"),
                        jsonobj.getString("attendance")));
            }
            getDataValues();
        } catch (JSONException e) {
            e.printStackTrace();
            notAvailable.setVisibility(View.VISIBLE);
        }
    }

    private void getDataValues() {
        Event e;
        for(int i=0; i<attendance.size(); i++) {
            AttendanceData allAttendance = attendance.get(i);
            if(allAttendance.getAttendance().contains(GiveAttendance.ABSENT)) {
                e = new Event(RED, allAttendance.getTimeInMili(), allAttendance.getAttendance());
                calendarView.addEvent(e);
            }
            else if(allAttendance.getAttendance().contains(GiveAttendance.LEAVE)) {
                e = new Event(ORANGE, allAttendance.getTimeInMili(), allAttendance.getAttendance());
                calendarView.addEvent(e);
            }
            else if(allAttendance.getAttendance().contains(GiveAttendance.PRESENT)){
                e = new Event(Color.GREEN,allAttendance.getTimeInMili(), allAttendance.getAttendance());
                calendarView.addEvent(e);
            }
        }

        calendarView.setVisibility(View.VISIBLE);
        calendarView.showCalendar();
        List<Event> list = (List<Event>) calendarView.getEventsForMonth(calendarView.getFirstDayOfCurrentMonth());
        getMonthData(list);

        calendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                getActivity().setTitle(dateFormatForMonth.format(firstDayOfNewMonth.getTime())+"");
                List<Event> li= calendarView.getEventsForMonth(firstDayOfNewMonth);
                getMonthData(li);
            }
        });

    }
    private void getMonthData(List<Event> li) {
        int present=0, absent=0, leaves=0;
        for(int j=0; j<li.size(); j++)
        {
            if(li.get(j).getColor()==RED) {
                absent++;
            }
            else if(li.get(j).getColor()==ORANGE) {
                leaves++;
            }
            else {
                present++;
            }
        }
        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(present,0));
        entries.add(new Entry(leaves,1));
        entries.add(new Entry(absent,2));

        List<String> labels = new ArrayList<>();
        labels.add("Present");
        labels.add("Leave");
        labels.add("Absent");
        PieDataSet pieDataSet = new PieDataSet(entries,"");

        int[] COLORFUL_COLORS = {Color.GREEN,Color.YELLOW,Color.RED};

        pieDataSet.setColors(COLORFUL_COLORS);
        PieData data = new PieData(labels,pieDataSet);
        pieChart.setData(data);
        pieChart.animateY(3000);

    }
}