package com.hubli.imperium.imaperiumdiary.Profile;


import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.hubli.imperium.imaperiumdiary.Attendance.Teacher.GiveAttendance.GiveAttendance;
import com.hubli.imperium.imaperiumdiary.Data.SPData;
import com.hubli.imperium.imaperiumdiary.Interface.IVolleyResponse;
import com.hubli.imperium.imaperiumdiary.R;
import com.hubli.imperium.imaperiumdiary.Utility.MyVolley;
import com.hubli.imperium.imaperiumdiary.Utility.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileContent extends Fragment {


    public ProfileContent() {
        // Required empty public constructor
    }

    private List<QAMarksData> items = new ArrayList<>();
    private View rootView;
    private SPData spData;
    private static boolean loadData = true;
    ExpandableHeightListView listView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_profile_content, container, false);
        spData = new SPData();
        populateQAList(rootView);
        attendanceChart(rootView);

        return rootView;
    }


    private void populateQAList(View rootView) {
        listView = (ExpandableHeightListView) rootView.findViewById(R.id.qaList);
        if(spData.getQAMarksData() == null || loadData){
            new MyVolley(getActivity().getApplicationContext(), new IVolleyResponse() {
                @Override
                public void volleyResponse(String result) {
                    result = result.replace("[{","").replace("}]","").replace("\"","");
                    String data[] = result.split(",");
                    int questionNumber = Integer.parseInt(data[0].split(":")[1]);

                    for(int i = 2; i<data.length;i++){
                        String qField = data[i].split(":")[0];
                        int qMarks = Integer.parseInt(data[i].split(":")[1]);
                        if(qMarks != 0) {
                            items.add(new QAMarksData(qField, qMarks, questionNumber * 10));
                        }
                    }
                    items.add(new QAMarksData("Grand Total",QAMarksData.getGrandMarks(),QAMarksData.getGrandTotal()));
                    MyAdaptor adaptor = new MyAdaptor();
                    listView.setExpanded(true);
                    listView.setAdapter(adaptor);
                }

                @Override
                public void volleyError() {

                }
            }).setUrl(URL.QA_MARKS_DATA)
                    .setParams(SPData.USER_NUMBER,spData.getUserData(SPData.USER_NUMBER)).connect();
        }
    }



    private class MyAdaptor extends ArrayAdapter<QAMarksData>{

        public MyAdaptor() {
            super(getActivity().getApplicationContext(), R.layout.qa_marks_item,items);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View v = convertView;
            if(v == null) {
                v = getActivity().getLayoutInflater().inflate(R.layout.qa_marks_item, parent, false);
            }
            TextView title = (TextView) v.findViewById(R.id.fieldName);
            final TextView marks = (TextView) v.findViewById(R.id.marks);

            final QAMarksData data = items.get(position);
            title.setText(data.getTableName());
            if(data.getMarks() < (data.getTotal()%30)){
                marks.setTextColor(Color.RED);
            }else{
                marks.setTextColor(Color.GREEN);
            }
            marks.setText(data.getMarks()+"/"+data.getTotal());
            return v;
        }
    }

    private void attendanceChart(View rootView){
        final PieChart pieChart = (PieChart) rootView.findViewById(R.id.attendancePi);
        final TextView loading = (TextView) rootView.findViewById(R.id.noticeTextAttendance);
        pieChart.setCenterText("Overall Attendance");
        pieChart.setDescription("");
        if(spData.getAttendanceData() == null || loadData){
            new MyVolley(getActivity().getApplicationContext(), new IVolleyResponse() {
                @Override
                public void volleyResponse(String result) {
                    spData.storeAttendanceData(result);
                    parseJson(result,pieChart, loading);
                    loadData = false;
                }

                @Override
                public void volleyError() {

                }
            }).setUrl(URL.ATTENDANCE_FETCH)
                    .setParams(SPData.USER_NUMBER,spData.getUserData(SPData.USER_NUMBER))
                    .connect();
        }else{
            parseJson(spData.getAttendanceData(), pieChart,loading);
        }
    }

    private void parseJson(String result, PieChart pieChart, TextView loading){
        try {
            JSONArray json = new JSONArray(result);
            int p = 0, l = 0, a = 0;
            for (int i = 0; i <= json.length() - 1; i++) {
                JSONObject jsonobj = json.getJSONObject(i);
                String attendance = jsonobj.getString("attendance");
                switch (attendance){
                    case GiveAttendance.PRESENT:
                        p++;
                        break;
                    case GiveAttendance.LEAVE:
                        l++;
                        break;
                    case GiveAttendance.ABSENT:
                        a++;
                        break;
                }
            }
            List<Entry> entries = new ArrayList<>();
            entries.add(new Entry(p,0));
            entries.add(new Entry(l,1));
            entries.add(new Entry(a,2));

            List<String> labels = new ArrayList<>();
            labels.add("Present");
            labels.add("Leave");
            labels.add("Absent");
            PieDataSet pieDataSet = new PieDataSet(entries,"");

            int[] COLORFUL_COLORS = {
                    Color.GREEN,Color.YELLOW,Color.RED
            };

            pieDataSet.setColors(COLORFUL_COLORS);
            PieData data = new PieData(labels,pieDataSet);
            pieChart.setData(data);
            pieChart.animateY(3000);
            loading.setVisibility(View.GONE);
        } catch (JSONException e) {
            e.printStackTrace();
            loading.setVisibility(View.GONE);
        }
    }

}
