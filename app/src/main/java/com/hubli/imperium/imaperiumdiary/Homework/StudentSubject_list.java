package com.hubli.imperium.imaperiumdiary.Homework;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.hubli.imperium.imaperiumdiary.Data.SPData;
import com.hubli.imperium.imaperiumdiary.Interface.IVolleyResponse;
import com.hubli.imperium.imaperiumdiary.R;
import com.hubli.imperium.imaperiumdiary.Utility.MyVolley;
import com.hubli.imperium.imaperiumdiary.Utility.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class StudentSubject_list extends AppCompatActivity implements IVolleyResponse {

    private SPData userDataSP;
    private MyVolley myVolley;
    private ListView list_subject;
    private String className;
    private String divisionName;
    private ProgressBar progressBar;
    private TextView noSubs;
    private ArrayList<String> subjects = new ArrayList<>();
    private String cd_id;
    private ArrayList<String> subjectids = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_subject_list);
        Intent intent = getIntent();
        className = intent.getStringExtra("class");
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        divisionName = intent.getStringExtra("division");
        list_subject=(ListView)findViewById(R.id.list_subject);
        noSubs = (TextView) findViewById(R.id.no_subs);
        userDataSP=new SPData();
        myVolley = new MyVolley(getApplicationContext(), this);
        getSubjectData();

    }

    public void getSubjectData(){
        progressBar.setVisibility(View.VISIBLE);
        myVolley.setUrl(URL.CLASSES_DIVISIONS_SUBJECTS);
        myVolley.setParams("class", className);
        myVolley.setParams("division", divisionName);
        myVolley.connect();
    }
    @Override
    public void volleyResponse(String result) {
        try {
            noSubs.setVisibility(View.GONE);
            getJsonData(result);
        } catch (JSONException e) {
            e.printStackTrace();
            noSubs.setVisibility(View.VISIBLE);
        }
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void volleyError() {

    }

    private void getJsonData(String re) throws JSONException {
        JSONArray json = new JSONArray(re);
        subjects.clear();
        for (int i = 0; i < json.length(); i++) {
            JSONObject jsonobj = json.getJSONObject(i);
            subjects.add(jsonobj.getString("subject_name"));
            subjectids.add(jsonobj.getString("subject_id"));
            cd_id=(jsonobj.getString("cd_id"));

        }
        list_subject.setAdapter(new MyAdaptor());
        list_subject.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(view.getContext(), HomeworkList_teacher.class);
                intent.putExtra("class", className);
                intent.putExtra("division", divisionName);
                intent.putExtra("subject", subjects.get(position));
                intent.putExtra("subjectid", subjectids.get(position));
                intent.putExtra("cd_id", cd_id);
                startActivity(intent);
            }
        });
    }
    class MyAdaptor extends ArrayAdapter<String> {

        public MyAdaptor() {
            super(getApplicationContext(), R.layout.class_div,subjects);
        }
        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View ItemView = convertView;
            if (ItemView == null) {
                ItemView = getLayoutInflater().inflate(R.layout.class_div, parent, false);
            }
            TextView class_text=(TextView)ItemView.findViewById(R.id.class_id) ;
            class_text.setText(subjects.get(position));
            return ItemView;
        }
    }
}
