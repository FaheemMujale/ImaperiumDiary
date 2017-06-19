package com.hubli.imperium.imaperiumdiary.Attendance.Teacher.GiveAttendance;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hubli.imperium.imaperiumdiary.Data.SPData;
import com.hubli.imperium.imaperiumdiary.Interface.IVolleyResponse;
import com.hubli.imperium.imaperiumdiary.R;
import com.hubli.imperium.imaperiumdiary.Utility.MyVolley;
import com.hubli.imperium.imaperiumdiary.Utility.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GiveAttendance extends AppCompatActivity {

    private ProgressBar progressBar;
    private MyAdaptor adapter;
    private ProgressDialog progressDialog;

    static List<ListData> items = new ArrayList<>();

    public static final String PRESENT = "P";
    public static final String LEAVE = "L";
    public static final String ABSENT = "A";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_attendance);
        setTitle("Attendance");


        adapter = new MyAdaptor();
        ListView list = (ListView) findViewById(R.id.list);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        TextView noSubs = (TextView) findViewById(R.id.no_students);
        Button submit = (Button) findViewById(R.id.button_submit);

        getStudentData(items, submit, noSubs, list);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final JSONArray jsonArray=new JSONArray();
                for(int i = 0 ; i< items.size();i++){
                    try {
                        JSONObject jsonObject=new JSONObject();
                        jsonObject.put(SPData.USER_NUMBER,items.get(i).getUserNumber());
                        jsonObject.put("attendance",items.get(i).getAttendance());
                        jsonArray.put(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(GiveAttendance.this);
                alert.setTitle("Alert");
                alert.setMessage("Confirm Attendance submission");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendAttendanceData(jsonArray);
                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.show();
            }
        });
    }

    public void getStudentData(final List<ListData> items, final Button submit, final TextView noSubs, final ListView list){
        Intent intent = getIntent();
        String sClass = intent.getStringExtra("class");
        String sDivision = intent.getStringExtra("division");
        progressBar.setVisibility(View.VISIBLE);
        items.clear();
        new MyVolley(getApplicationContext(), new IVolleyResponse() {

            @Override
            public void volleyResponse(String result) {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        items.add(new ListData(jsonObject.getString("user_number"),jsonObject.getString("roll_number"),
                                jsonObject.getString("first_name")+" "+jsonObject.getString("last_name"),"P"));
                    }
                    Collections.sort(items,new MyComparator());
                    list.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    submit.setVisibility(View.VISIBLE);
                    noSubs.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                    submit.setVisibility(View.GONE);
                    noSubs.setVisibility(View.VISIBLE);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void volleyError() {
                progressBar.setVisibility(View.GONE);
                submit.setVisibility(View.GONE);
                noSubs.setVisibility(View.VISIBLE);
            }
        })
        .setUrl(URL.ATTENDANCE)
        .setParams("class",sClass)
        .setParams("division",sDivision)
        .connect();
    }

    private class MyComparator implements Comparator<ListData> {

        @Override
        public int compare(ListData lhs, ListData rhs) {
            return lhs.getRollNumber() - rhs.getRollNumber();
        }
    }

    public void sendAttendanceData(JSONArray jsonArray){
        progressDialog = new ProgressDialog(GiveAttendance.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        new MyVolley(getApplicationContext(), new IVolleyResponse() {
            @Override
            public void volleyResponse(String result) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Successfully Submitted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void volleyError() {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Some problem occurred please try again", Toast.LENGTH_SHORT).show();
            }
        })
        .setUrl(URL.ATTENDANCE)
        .setParams("class", getIntent().getStringExtra("class"))
        .setParams("division",getIntent().getStringExtra("division"))
        .setParams("jsonString", jsonArray.toString())
        .connect();
        Log.e("atten",jsonArray.toString());
    }

   private class MyAdaptor extends ArrayAdapter<ListData> {

        public MyAdaptor() {
            super(getApplicationContext(), R.layout.attendance_item,items);
        }


        @NonNull
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.attendance_item, parent, false);
            }

            TextView text_name = (TextView) itemView.findViewById(R.id.textView_name);
            TextView text_roll = (TextView) itemView.findViewById(R.id.textView_roll);

            RadioGroup optGroup = (RadioGroup) itemView.findViewById(R.id.optGroup);
            RadioButton rPresent = (RadioButton)itemView.findViewById(R.id.radio_buttonP);
            RadioButton rAbsent =(RadioButton) itemView.findViewById(R.id.radio_buttonA);
            RadioButton rLeave = (RadioButton)itemView.findViewById(R.id.radio_buttonL);

            final ListData currentItem = items.get(position);

            text_roll.setText(currentItem.getRollNumber()+"");
            text_name.setText(currentItem.getStudentName());

            switch (currentItem.getAttendance()){
                case PRESENT:
                    rPresent.setChecked(true);
                    break;
                case LEAVE:
                    rLeave.setChecked(true);
                    break;
                case ABSENT:
                    rAbsent.setChecked(true);
                    break;
            }

            optGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                    switch (i){
                        case R.id.radio_buttonP:
                            currentItem.setAttendance(PRESENT);
                            break;
                        case R.id.radio_buttonL:
                            currentItem.setAttendance(LEAVE);
                            break;
                        case R.id.radio_buttonA:
                            currentItem.setAttendance(ABSENT);
                            break;
                    }
                }
            });
            return itemView;
        }
    }

}

