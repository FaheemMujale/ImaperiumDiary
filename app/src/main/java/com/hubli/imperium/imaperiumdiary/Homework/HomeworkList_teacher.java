package com.hubli.imperium.imaperiumdiary.Homework;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.hubli.imperium.imaperiumdiary.Data.SPData;
import com.hubli.imperium.imaperiumdiary.Interface.IVolleyResponse;
import com.hubli.imperium.imaperiumdiary.R;
import com.hubli.imperium.imaperiumdiary.Utility.MyVolley;
import com.hubli.imperium.imaperiumdiary.Utility.URL;
import com.ramotion.foldingcell.FoldingCell;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.hubli.imperium.imaperiumdiary.Data.SPData.HOMEWORKDATE;
import static com.hubli.imperium.imaperiumdiary.Data.SPData.HOMEWORK_CONTENTS;
import static com.hubli.imperium.imaperiumdiary.Data.SPData.HOMEWORK_NUMBER;
import static com.hubli.imperium.imaperiumdiary.Data.SPData.HOMEWORK_TITLE;
import static com.hubli.imperium.imaperiumdiary.Data.SPData.LASTDATE_SUBMISSION;
import static com.hubli.imperium.imaperiumdiary.Data.SPData.NUMBER_USER;
import static com.hubli.imperium.imaperiumdiary.Data.SPData.SUBJECT;
import static com.hubli.imperium.imaperiumdiary.Homework.StudentDiary_DatePicker.INTENTFILTER;

public class HomeworkList_teacher extends AppCompatActivity implements IVolleyResponse {

    private FloatingActionButton button;
    private FoldingCellListAdapter adapter;
    private String className;
    private String divisionName;
    private String subjectName;
    private String subjectid;
    private String cdid;
    private ListView list;
    private ArrayList<Item> items_homework =  new ArrayList<>();
    private MyVolley myVolley;
    private SPData userDataSp;
    private ProgressBar progressBar;
    private TextView notAvailable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework_list_teacher);
        userDataSp = new SPData(this);
        progressBar = (ProgressBar)findViewById(R.id.homeworkProgress);
        notAvailable = (TextView)findViewById(R.id.homeworkNotAvailable);
        myVolley = new MyVolley(getApplicationContext(),this);
        Intent intent = getIntent();
        className = intent.getStringExtra(SPData.CLASS);
        divisionName = intent.getStringExtra(SPData.DIVISION);
        subjectName = intent.getStringExtra("subject");
        subjectid = intent.getStringExtra("subjectid");
        cdid = intent.getStringExtra("cd_id");
        setTitle("HOME WORK");
        list = (ListView)findViewById(R.id.homeworkList);
        button = (FloatingActionButton)findViewById(R.id.addHomeWork);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), StudentDiary_DatePicker.class);
                intent.putExtra("class", className);
                intent.putExtra("division", divisionName);
                intent.putExtra("subjectid", subjectid);
                startActivity(intent);
            }
        });

        getHomeWorkList();
//        items_homework.add(new Item("hw1","content","lastdate","sub","date","1","1"));
        adapter = new FoldingCellListAdapter(HomeworkList_teacher.this, items_homework);
        adapter.sortData();
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                // toggle clicked cell state
                ((FoldingCell) view).toggle(false);
                // register in adapter that state for selected cell is toggled
                adapter.registerToggle(pos);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        //registerReceiver(receiver, new IntentFilter(INTENTFILTER));
    }

//    private BroadcastReceiver receiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//           adapter.addItemNew(new Item(intent.getStringExtra(HOMEWORK_TITLE),intent.getStringExtra(HOMEWORK_CONTENTS)
//                   ,intent.getStringExtra(LASTDATE_SUBMISSION),intent.getStringExtra(SUBJECT),
//                   intent.getStringExtra(HOMEWORKDATE), intent.getStringExtra(NUMBER_USER),
//                   intent.getStringExtra(HOMEWORK_NUMBER)));
//            adapter.notifyDataSetChanged();
//        }
//    };

    @Override
    public void onDestroy() {
        super.onDestroy();
       // unregisterReceiver(receiver);
    }



    public void getHomeWorkList()
    {
        progressBar.setVisibility(View.VISIBLE);
        myVolley.setUrl(URL.HOMEWORK_FETCH);
        myVolley.setParams("cd_id", cdid);
        myVolley.setParams("subject_id", subjectid);
        myVolley.connect();

    }
    @Override
    public void volleyResponse(String result) {
        try {
            notAvailable.setVisibility(View.GONE);
            Log.e("res....", result);
            getJsonHome(result);
        } catch (JSONException e) {
            e.printStackTrace();
            notAvailable.setVisibility(View.VISIBLE);
        }
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void volleyError() {

    }

    private void getJsonHome(String re) throws JSONException {
        JSONArray json = new JSONArray(re);
        Log.e("json ", re);
          items_homework = new ArrayList<>();
        for (int i = 0; i <= json.length() - 1; i++) {
            JSONObject jsonobj = json.getJSONObject(i);
            items_homework.add(new Item(jsonobj.getString("hw_title"), jsonobj.getString("hw_content"), jsonobj.getString("hw_lastdate"), jsonobj.getString("subject"), jsonobj.getString("hw_givendate"), jsonobj.getString(SPData.USER_NUMBER), jsonobj.getString("hw_number")));
        }

        adapter = new FoldingCellListAdapter(HomeworkList_teacher.this, items_homework);
        adapter.sortData();
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                // toggle clicked cell state
                ((FoldingCell) view).toggle(false);
                // register in adapter that state for selected cell is toggled
                adapter.registerToggle(pos);
            }
        });

    }
}
