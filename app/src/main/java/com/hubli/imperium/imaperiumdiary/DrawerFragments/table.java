package com.hubli.imperium.imaperiumdiary.DrawerFragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hubli.imperium.imaperiumdiary.Data.SPData;
import com.hubli.imperium.imaperiumdiary.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Rafiq Ahmad on 5/16/2017.
 */

public class table extends AppCompatActivity {
    private Context mContext;
    private String employeeName, projectName;
    private LinearLayout hv1;
    private LinearLayout hv2;
    private LinearLayout hv3;
    private LinearLayout hv4;
    private LinearLayout hv5;
    private LinearLayout hv6;
    private LinearLayout time;
    int z=0;
    private String sub;
    private String teacher;
    private EditText subject;
    private EditText teachername;
    private Button submit;
    FloatingActionButton fab;
    int ids[] = {R.id.time123,R.id.horizonta1,R.id.horizonta2,R.id.horizonta3,R.id.horizonta4,R.id.horizonta5,R.id.horizonta6};
    private String[] days = {"time","mon","tue","wed","thu","fri","sat"};
    /**
     * Initialize instance variables with data from bundle
     */




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetable_fragment);
        hv1 = (LinearLayout) findViewById(R.id.horizonta1);
        hv2 = (LinearLayout) findViewById(R.id.horizonta2);
        hv3 = (LinearLayout) findViewById(R.id.horizonta3);
        hv4 = (LinearLayout) findViewById(R.id.horizonta4);
        hv5 = (LinearLayout) findViewById(R.id.horizonta5);
        hv6 = (LinearLayout) findViewById(R.id.horizonta6);
        time = (LinearLayout) findViewById(R.id.time123);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(z!=0){
                    submit();
                }else{
                    Toast.makeText(table.this,"Please complete the table",Toast.LENGTH_LONG).show();
                }
            }
        });
//        hv8 = (LinearLayout) mView.findViewById(R.id.horizonta8);


       // return mView;
    }



    /**
     * Updates the order of mListView onResume to handle sortOrderChanges properly
     */
    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * Cleanup the adapter when activity is paused.
     */
    @Override
    public void onPause() {
        super.onPause();

    }




    public void addTime(View view){
        int id = view.getId();
        Button btn = (Button) findViewById(id);
        int pid = ((View) btn.getParent()).getId();
        final LinearLayout lvlayout = (LinearLayout) findViewById(pid);
        final Dialog dialog = new Dialog(table.this);
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
                TextView tv = new TextView(table.this);
                tv.setText(sub+"-"+teacher);
                LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                rlp.setMargins(10,10,10,10);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                tv.setLayoutParams(rlp);
                tv.setTypeface(null, Typeface.BOLD);
                tv.setBackground(getResources().getDrawable(R.drawable.bordertv));
                tv.setId(View.generateViewId());
                tv.setGravity(Gravity.CENTER);
                tv.setOnClickListener(onclicklistener);
                lvlayout.addView(tv);
                dialog.dismiss();
                z++;
            }
        });
        dialog.show();

    }
    public void addsubject(View view){
        int id = view.getId();
        Button btn = (Button) findViewById(id);
        int pid = ((View) btn.getParent()).getId();
        final LinearLayout lvlayout = (LinearLayout) findViewById(pid);
        final Dialog dialog = new Dialog(table.this);
        dialog.setContentView(R.layout.add_subject);
        dialog.setTitle("Hello");
        subject = (EditText) dialog.findViewById(R.id.subject);
        teachername = (EditText) dialog.findViewById(R.id.teachername);
        submit = (Button) dialog.findViewById(R.id.finish);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sub = subject.getText().toString();
                teacher = teachername.getText().toString();
                TextView tv = new TextView(table.this);
                tv.setText(sub);
                LinearLayout lv = new LinearLayout(table.this);
                LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                rlp.setMargins(10,10,10,10);
                tv.setLayoutParams(rlp);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                tv.setTypeface(null, Typeface.BOLD);
                tv.setId(View.generateViewId());
                tv.setBackground(getResources().getDrawable(R.drawable.bordertv));
                tv.setGravity(Gravity.CENTER);
                tv.setOnClickListener(onclicklistener);
                lvlayout.addView(tv);
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    View.OnClickListener onclicklistener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            int id1 = v.getId();
            final TextView tv = (TextView) findViewById(id1);
            int pid = ((View) tv.getParent()).getId();
            if(pid==R.id.time123){
                final Dialog dialog = new Dialog(table.this);
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
            }else{
                final Dialog dialog = new Dialog(table.this);
                dialog.setContentView(R.layout.add_subject);
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
            }

        }
    };


    public void submit(){
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

            timetable1.put("id", "3");
            Log.d("jsoncreated",timetable1.toString());
            Intent i = new Intent(this,studenttable.class);
            i.putExtra("RESPONSE",timetable1.toString());
            startActivity(i);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
