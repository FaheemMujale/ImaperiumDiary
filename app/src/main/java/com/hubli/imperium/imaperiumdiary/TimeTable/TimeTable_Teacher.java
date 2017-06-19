package com.hubli.imperium.imaperiumdiary.TimeTable;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.hubli.imperium.imaperiumdiary.Data.SPData;
import com.hubli.imperium.imaperiumdiary.Interface.IVolleyResponse;
import com.hubli.imperium.imaperiumdiary.R;
import com.hubli.imperium.imaperiumdiary.Utility.MyVolley;
import com.hubli.imperium.imaperiumdiary.Utility.URL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Rafiq Ahmad on 5/16/2017.
 */

public class TimeTable_Teacher extends AppCompatActivity {
    int z=0;
    public static final String INTENT_FILTER = "brod_intent";
    private String sub;
    private String teacher;
    private EditText subject;
    private EditText teachername;
    private Button submit;
    private SPData spData;
    int i=0;
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
        spData = new SPData();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(z!=0){
                    submit();
                }else{
                    Toast.makeText(TimeTable_Teacher.this,"Please complete the TimeTable_Teacher",Toast.LENGTH_LONG).show();
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
        final Dialog dialog = new Dialog(TimeTable_Teacher.this);
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
                TextView tv = new TextView(TimeTable_Teacher.this);
                tv.setText(sub+"-"+teacher);
                int px = convertDpToPixel(80, getApplicationContext());
                LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(px,px);
               // rlp.setMargins(10,10,10,10);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                tv.setLayoutParams(rlp);
                ColorGenerator generator = ColorGenerator.MATERIAL;
                int color = generator.getColor(i);
                tv.setTypeface(null, Typeface.BOLD);
                tv.setId(View.generateViewId());
                tv.setBackgroundColor(color);
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
        final Dialog dialog = new Dialog(TimeTable_Teacher.this);
        dialog.setContentView(R.layout.add_subject);
        dialog.setTitle("Hello");
        subject = (EditText) dialog.findViewById(R.id.subject);
        submit = (Button) dialog.findViewById(R.id.finish);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sub = subject.getText().toString();
                TextView tv = new TextView(TimeTable_Teacher.this);
                tv.setText(sub);
                int px = convertDpToPixel(80, getApplicationContext());
                LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(px,px);
              //  rlp.setMargins(10,10,10,10);
                tv.setLayoutParams(rlp);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                tv.setTypeface(null, Typeface.BOLD);
                tv.setId(View.generateViewId());
                ColorGenerator generator = ColorGenerator.MATERIAL;
                int color = generator.getColor(i);
                tv.setBackgroundColor(color);
                tv.setGravity(Gravity.CENTER);
                tv.setOnClickListener(onclicklistener);
                lvlayout.addView(tv);
                dialog.dismiss();
                i++;
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

    View.OnClickListener onclicklistener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            int id1 = v.getId();
            final TextView tv = (TextView) findViewById(id1);
            int pid = ((View) tv.getParent()).getId();
            if(pid==R.id.time123){
                final Dialog dialog = new Dialog(TimeTable_Teacher.this);
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
                final Dialog dialog = new Dialog(TimeTable_Teacher.this);
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

            Log.d("jsoncreated",timetable1.toString());
            uploadTimeTableData(timetable1.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void uploadTimeTableData(final String data){
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
        }).setUrl(URL.UPLOAD_TIMETABLE)
                .setParams("cd_id",spData.getUserData(SPData.CLASS_DIVISION_ID))
                .setParams("timetable_data",data)
                .connect();
    }

}
