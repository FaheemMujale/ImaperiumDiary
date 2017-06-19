package com.hubli.imperium.imaperiumdiary.Exams;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.hubli.imperium.imaperiumdiary.Data.SPData;
import com.hubli.imperium.imaperiumdiary.R;
import com.hubli.imperium.imaperiumdiary.TimeTable.TimeTable_Teacher;

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
    private Button adddate;
    EditText subject;
    EditText teachername;
    Button submit;
    LinearLayout date;
    public Insert_Exam(){}

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_addexam);
        spdata = new SPData();
        adddate = (Button) findViewById(R.id.add);
        date = (LinearLayout) findViewById(R.id.lv1);
        adddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID);
            }
        });
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
            TextView tv = new TextView(Insert_Exam.this);
            tv.setText(day_h+"/"+month_h+"/"+year_h);
            int px = convertDpToPixel(80, getApplicationContext());
            LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(px,px);
            rlp.setMargins(10,10,10,10);
            tv.setLayoutParams(rlp);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            tv.setTypeface(null, Typeface.BOLD);
            tv.setId(View.generateViewId());
            ColorGenerator generator = ColorGenerator.MATERIAL;
            Random rand = new Random();
            int  n = rand.nextInt(10) + 1;
            int color = generator.getColor(n);
            tv.setBackgroundColor(color);
            tv.setGravity(Gravity.CENTER);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            date.addView(tv);
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
                int px = convertDpToPixel(80, getApplicationContext());
                LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(px,px);
                rlp.setMargins(10,10,10,10);
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
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                lvlayout.addView(tv);
                dialog.dismiss();
            }
        });
        dialog.show();

    }


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
                int px = convertDpToPixel(80, getApplicationContext());
                LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(px,px);
                rlp.setMargins(10,10,10,10);
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
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
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
