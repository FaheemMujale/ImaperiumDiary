package com.hubli.imperium.imaperiumdiary.Exams;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.hubli.imperium.imaperiumdiary.Data.SPData;
import com.hubli.imperium.imaperiumdiary.Interface.IVolleyResponse;
import com.hubli.imperium.imaperiumdiary.R;
import com.hubli.imperium.imaperiumdiary.TimeTable.TimeTable_Teacher;
import com.hubli.imperium.imaperiumdiary.Utility.ImperiumConstants;
import com.hubli.imperium.imaperiumdiary.Utility.MyVolley;
import com.hubli.imperium.imaperiumdiary.Utility.URL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Rafiq Ahmad on 6/19/2017.
 */

public class Insert_Exam extends AppCompatActivity {

    private static final int DIALOG_ID = 0;
    private int year_h;
    private int month_h;
    private int day_h;
    private SPData spdata;
    private String sub;
    private String teacher;
    private Button adddate;
    private Button addmarks;
    private Button addsub;
    private Button addtime;
    public static final String INTENT_FILTER = "exam_intent";
    EditText subject;
    EditText teachername;
    Button submit;
    String divison;
    LinearLayout date;
    EditText exam_title;
    LinearLayout lv4;
    CheckBox checkBox;
    int dateid =0;
    FloatingActionButton fab;
    int ids[] = {R.id.lv1,R.id.lv2,R.id.lv3,R.id.lv4};
    private String[] days = {"date","subject","timing","marks"};
    public Insert_Exam(){}

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_addexam);
        spdata = new SPData();
        exam_title = (EditText) findViewById(R.id.title);
        date = (LinearLayout) findViewById(R.id.lv1);
        lv4 = (LinearLayout) findViewById(R.id.lv4);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.insert_exam);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(spdata.getUserData(SPData.IDENTIFICATION).contains(ImperiumConstants.TEACHER)) {
            fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setVisibility(View.VISIBLE);
            divison = spdata.getUserData(SPData.DIVISION);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                if(z!=0){
                    submitexam();
//                }else{
//                    Toast.makeText(Insert_Exam.this,"Please complete the TimeTable_Teacher",Toast.LENGTH_LONG).show();
//                }
                }
            });

            adddate= (Button) findViewById(R.id.add);
            addsub= (Button) findViewById(R.id.sub);
            addmarks= (Button) findViewById(R.id.marks);
            addtime= (Button) findViewById(R.id.time);
            addsub.setVisibility(View.VISIBLE);
            addmarks.setVisibility(View.VISIBLE);
            addtime.setVisibility(View.VISIBLE);
            adddate.setVisibility(View.VISIBLE);
            adddate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(DIALOG_ID);
                }
            });
            checkBox = (CheckBox) findViewById(R.id.common);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked)
                        divison = "0";
                    else
                        divison=spdata.getUserData(SPData.DIVISION);
                }


            });
        }

        Intent intent = getIntent();
        String data = intent.getStringExtra(ImperiumConstants.EXAM_DATA);
        if(data!=null) {
            exam_title.setText(intent.getStringExtra(ImperiumConstants.EXAM_TITLE));
            divison = intent.getStringExtra(ImperiumConstants.EXAM_DIV);
            checkBox = (CheckBox) findViewById(R.id.common);
                if (divison.contains("0")) {
                    checkBox.setChecked(true);
                    if (spdata.getUserData(SPData.IDENTIFICATION).contains(ImperiumConstants.STUDENT))
                        checkBox.setClickable(false);
                }else{
                    if (spdata.getUserData(SPData.IDENTIFICATION).contains(ImperiumConstants.STUDENT))
                        checkBox.setClickable(false);
                }


            parseresult(data);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void parseresult(String data) {

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(data);
            String date = jsonObject.optString("date");
            String subject = jsonObject.optString("subject");
            String timing = jsonObject.optString("timing");
            String marks = jsonObject.optString("marks");
            String[] datearray = date.split(",");
            String[] subarray = subject.split(",");
            String[] timearray = timing.split(",");
            String[] marksarray = marks.split(",");

            ArrayList<String []>test = new ArrayList<>();
            test.add(datearray);
            test.add(subarray);
            test.add(timearray);
            test.add(marksarray);

            for(int i=0; i<test.size(); i++){

                for ( int y=0; y<test.get(i).length;y++){
                    TextView tv = new TextView(getApplicationContext());
                    tv.setText(test.get(i)[y].replace("[","").replace("]",""));
                    int px = convertDpToPixel(80, getApplicationContext());
                    LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,px);
                    rlp.setMargins(0,0,3,15);
                    //tv.setPadding(10,10,10,10);
                    tv.setLayoutParams(rlp);
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    tv.setTypeface(null, Typeface.BOLD);
                    tv.setId(View.generateViewId());
                    tv.setGravity(Gravity.CENTER);
                    ColorGenerator generator = ColorGenerator.MATERIAL;
                    int color = generator.getColor(y);
                    tv.setBackgroundColor(color);
                    if(spdata.getUserData(SPData.IDENTIFICATION).contains(ImperiumConstants.TEACHER)) {
                        if (i == 0) {
                            tv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dateid = v.getId();
                                    showDialog(DIALOG_ID);
                                }
                            });
                        } else {
                            tv.setOnClickListener(onclicklistener);
                        }
                    }
//                    tv.setBackgroundColor(Color.parseColor("#00BCD4"));
                    LinearLayout lv = (LinearLayout)findViewById(ids[i]);
                    lv.addView(tv);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String t = jsonObject.optString("time");
    }

    private void submitexam() {

        JSONObject timetable1 = new JSONObject();

        try {
            for(int i=0; i<ids.length; i ++){

                ArrayList<String> data = new ArrayList<>();

                LinearLayout lv = (LinearLayout) findViewById(ids[i]);
                for(int y=1; y<((ViewGroup)lv).getChildCount(); y++) {
                    View nextChild = ((ViewGroup)lv).getChildAt(y);
                    TextView textView = (TextView) findViewById(nextChild.getId());
                        String text = textView.getText().toString();
                        data.add(text);


                }
                timetable1.put(days[i],data);


            }

            Log.d("jsoncreated",timetable1.toString());
            uploadExamTableData(timetable1.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void uploadExamTableData(final String data){
        new MyVolley(getApplicationContext(), new IVolleyResponse() {
            @Override
            public void volleyResponse(String result) {

                Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(INTENT_FILTER);
                intent.putExtra("data",result);
                getApplicationContext().sendBroadcast(intent);
                finish();

            }

            @Override
            public void volleyError() {

            }
        }).setUrl(URL.EXAM_INSERT)
                .setParams("class",spdata.getUserData(SPData.CLASS))
                .setParams("divison",divison)
                .setParams("exam_data",data)
                .setParams("exam_title",exam_title.getText().toString())
                .connect();
    }

    @Override
    protected Dialog onCreateDialog(int id)
    {
        if(id==DIALOG_ID)
            return new DatePickerDialog(this, dpickerListener, year_h, month_h-1, day_h);
        return null;
    }
    private DatePickerDialog.OnDateSetListener dpickerListener = new DatePickerDialog.OnDateSetListener()
    {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            year_h = year;
            month_h = monthOfYear+1;
            day_h = dayOfMonth;
            if(dateid==0) {
                TextView tv = new TextView(Insert_Exam.this);
                tv.setText(day_h + "/" + month_h + "/" + year_h);
                int px = convertDpToPixel(70, getApplicationContext());
                LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, px);
                rlp.setMargins(0,0,3,15);
                tv.setLayoutParams(rlp);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                tv.setTypeface(null, Typeface.BOLD);
                tv.setId(View.generateViewId());
                ColorGenerator generator = ColorGenerator.MATERIAL;
                Random rand = new Random();
                int n = rand.nextInt(10) + 1;
                int color = generator.getColor(n);
                tv.setBackgroundColor(color);
                tv.setGravity(Gravity.CENTER);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dateid = v.getId();
                        showDialog(DIALOG_ID);

                    }
                });
                date.addView(tv);
            }else{
                TextView tv = (TextView) findViewById(dateid);
                tv.setText(day_h + "/" + month_h + "/" + year_h);
                dateid =0;
            }
        }
    };



    public void addsubject(View view){
        int id = view.getId();
        Button btn = (Button) findViewById(id);
        int pid = ((View) btn.getParent()).getId();
        final LinearLayout lvlayout = (LinearLayout) findViewById(pid);
        final Dialog dialog = new Dialog(Insert_Exam.this);
        dialog.setContentView(R.layout.add_subject);
        dialog.setTitle("Hello");
        subject = (EditText) dialog.findViewById(R.id.subject);
        submit = (Button) dialog.findViewById(R.id.finish);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sub = subject.getText().toString();
                TextView tv = new TextView(Insert_Exam.this);
                tv.setText(sub);
                int px = convertDpToPixel(70, getApplicationContext());
                LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,px);
                rlp.setMargins(0,0,3,15);
                tv.setLayoutParams(rlp);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                tv.setTypeface(null, Typeface.BOLD);
                tv.setId(View.generateViewId());
                Random rand = new Random();
                int  n = rand.nextInt(10) + 1;
                ColorGenerator generator = ColorGenerator.MATERIAL;
                int color = generator.getColor(n);
                tv.setBackgroundColor(color);
                tv.setGravity(Gravity.CENTER);
                tv.setOnClickListener(onclicklistener);
                lvlayout.addView(tv);
                dialog.dismiss();
            }
        });
        dialog.show();

    }


    public void addMarks(View view){
        int id = view.getId();
        Button btn = (Button) findViewById(id);
        int pid = ((View) btn.getParent()).getId();
        final LinearLayout lvlayout = (LinearLayout) findViewById(pid);
        final Dialog dialog = new Dialog(Insert_Exam.this);
        dialog.setContentView(R.layout.add_marks);
        dialog.setTitle("Hello");
        subject = (EditText) dialog.findViewById(R.id.subject);
        submit = (Button) dialog.findViewById(R.id.finish);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sub = subject.getText().toString();
                TextView tv = new TextView(Insert_Exam.this);
                tv.setText(sub);
                int px = convertDpToPixel(70, getApplicationContext());
                LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,px);
                rlp.setMargins(0,0,3,15);
                tv.setLayoutParams(rlp);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                tv.setTypeface(null, Typeface.BOLD);
                tv.setId(View.generateViewId());
                Random rand = new Random();
                int  n = rand.nextInt(10) + 1;
                ColorGenerator generator = ColorGenerator.MATERIAL;
                int color = generator.getColor(n);
                tv.setBackgroundColor(color);
                tv.setGravity(Gravity.CENTER);
                tv.setOnClickListener(onclicklistener);
                lv4.addView(tv);
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    View.OnClickListener onclicklistener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            int id1 = v.getId();
            final TextView tv = (TextView) findViewById(id1);
            int pid = ((View) tv.getParent()).getId();
            if(pid==R.id.lv3){
                final Dialog dialog = new Dialog(Insert_Exam.this);
                dialog.setContentView(R.layout.add_time);
                dialog.setTitle("Hello");
                subject = (EditText) dialog.findViewById(R.id.subject);
                teachername = (EditText) dialog.findViewById(R.id.teachername);
                submit = (Button) dialog.findViewById(R.id.finish);
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sub = subject.getText().toString();
                        teacher = teachername.getText().toString();
                        tv.setText(sub+"-"+teacher);
                        dialog.dismiss();

                    }
                });
                dialog.show();
            }  else if(pid==R.id.lv2){
                final Dialog dialog = new Dialog(Insert_Exam.this);
                dialog.setContentView(R.layout.add_subject);
                dialog.setTitle("Hello");
                subject = (EditText) dialog.findViewById(R.id.subject);
                submit = (Button) dialog.findViewById(R.id.finish);
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sub = subject.getText().toString();
                        tv.setText(sub);
                        dialog.dismiss();

                    }
                });
                dialog.show();
            }
            else{
                final Dialog dialog = new Dialog(Insert_Exam.this);
                dialog.setContentView(R.layout.add_marks);
                dialog.setTitle("Hello");
                subject = (EditText) dialog.findViewById(R.id.subject);
                submit = (Button) dialog.findViewById(R.id.finish);
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sub = subject.getText().toString();
                        tv.setText(sub);
                        dialog.dismiss();

                    }
                });
                dialog.show();

            }
        }
    };

    public void addTime(View view){
        int id = view.getId();
        Button btn = (Button) findViewById(id);
        int pid = ((View) btn.getParent()).getId();
        final LinearLayout lvlayout = (LinearLayout) findViewById(pid);
        final Dialog dialog = new Dialog(Insert_Exam.this);
        dialog.setContentView(R.layout.add_time);
        dialog.setTitle("Hello");
        subject = (EditText) dialog.findViewById(R.id.subject);
        teachername = (EditText) dialog.findViewById(R.id.teachername);
        submit = (Button) dialog.findViewById(R.id.finish);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sub = subject.getText().toString();
               String teacher = teachername.getText().toString();
                TextView tv = new TextView(Insert_Exam.this);
                tv.setText(sub+"-"+teacher);
                int px = convertDpToPixel(70, getApplicationContext());
                LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,px);
                rlp.setMargins(0,0,3,15);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                tv.setLayoutParams(rlp);
                Random rand = new Random();
                int  n = rand.nextInt(10) + 1;
                ColorGenerator generator = ColorGenerator.MATERIAL;
                int color = generator.getColor(n);
                tv.setTypeface(null, Typeface.BOLD);
                tv.setId(View.generateViewId());
                tv.setBackgroundColor(color);
                tv.setGravity(Gravity.CENTER);
                tv.setOnClickListener(onclicklistener);
                lvlayout.addView(tv);
                dialog.dismiss();
            }
        });
        dialog.show();

    }
    public static int convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int px = (int) (dp * (metrics.densityDpi / 160f));
        return px;
    }

}
