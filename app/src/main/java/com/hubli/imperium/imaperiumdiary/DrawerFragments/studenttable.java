package com.hubli.imperium.imaperiumdiary.DrawerFragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hubli.imperium.imaperiumdiary.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Rafiq Ahmad on 5/16/2017.
 */

public class studenttable extends AppCompatActivity {
    private View mView;
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
        sendserverreq();

    }

    private void sendserverreq() {
        onresp();
    }

    private void onresp() {
        Intent intent = getIntent();
        String response = intent.getStringExtra("RESPONSE");
        JSONArray jsonArray = null;
        try {
       //     jsonArray = new JSONArray(response);

   //     classList = new ArrayList<>();
         //   for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = new JSONObject(response);
                String t = jsonObject.optString("time");
                String mon = jsonObject.optString("mon");
                String tue = jsonObject.optString("tue");
                String wed = jsonObject.optString("wed");
                String thu = jsonObject.optString("thu");
                String fri = jsonObject.optString("fri");
                String sat = jsonObject.optString("sat");
            String[] timearray = t.split(",");
            String[] monarray = t.split(",");
            String[] tuearray = t.split(",");
            String[] wedarray = t.split(",");
            String[] thuarray = t.split(",");
            String[] friarray = t.split(",");
            String[] satarray = t.split(",");
        ArrayList<String []>test = new ArrayList<>();
        ArrayList<Integer>test1 = new ArrayList<>();
            test1.add(R.id.time);
            test1.add(R.id.horizonta1);
            test1.add(R.id.horizonta2);
            test1.add(R.id.horizonta3);
            test1.add(R.id.horizonta4);
            test1.add(R.id.horizonta5);
            test1.add(R.id.horizonta6);
            test.add(timearray);
            test.add(monarray);
            test.add(tuearray);
            test.add(wedarray);
            test.add(thuarray);
            test.add(friarray);
            test.add(satarray);

            for(int i=0; i<test.size(); i++){

                for ( int y=0; y<test.get(i).length;y++){
                    TextView tv = new TextView(this);
                    tv.setText(test.get(i)[y]);
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
                    LinearLayout lv = (LinearLayout) findViewById(test1.get(i));
                    lv.addView(tv);
                }
            }
                   //     }



        } catch (JSONException e) {
            e.printStackTrace();
        }

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
}
