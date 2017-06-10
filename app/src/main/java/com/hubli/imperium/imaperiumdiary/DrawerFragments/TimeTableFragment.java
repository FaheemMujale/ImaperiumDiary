package com.hubli.imperium.imaperiumdiary.DrawerFragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import static java.awt.font.TextAttribute.WEIGHT;

/**
 * Created by Rafiq Ahmad on 5/16/2017.
 */

public class TimeTableFragment extends Fragment   {

    public TimeTableFragment() {}

    private View mView;
    private Context mContext;
    private String employeeName, projectName;
    private FloatingActionButton button;
    private LinearLayout time;
    int z=0;
    private String sub;
    private String teacher;
    private EditText subject;
    private EditText teachername;
    private Button submit;
    FloatingActionButton fab;
    private SPData spdata;
    private String usertype;
    int ids[] = {R.id.time123,R.id.horizonta1,R.id.horizonta2,R.id.horizonta3,R.id.horizonta4,R.id.horizonta5,R.id.horizonta6};
    private String[] days = {"time","mon","tue","wed","thu","fri","sat"};
    /**
     * Initialize instance variables with data from bundle
     */




    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.timetable_studentt, container, false);
        spdata = new SPData();
        usertype = spdata.getUserData("USERTYPE");
        getTimeTableData();
//        if(usertype.contentEquals("teacher")) {
            button = (FloatingActionButton) mView.findViewById(R.id.addTimetable);
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), TimeTable_Teacher.class);
                    startActivity(intent);
                }
            });
//        }
        return mView;
    }


    private void parsejson(String response) {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(response);
   //     classList = new ArrayList<>();
         //   for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                String t = jsonObject.optString("time");
                String mon = jsonObject.optString("mon");
                String tue = jsonObject.optString("tue");
                String wed = jsonObject.optString("wed");
                String thu = jsonObject.optString("thu");
                String fri = jsonObject.optString("fri");
                String sat = jsonObject.optString("sat");
            String[] timearray = t.split(",");
            String[] monarray = mon.split(",");
            String[] tuearray = tue.split(",");
            String[] wedarray = wed.split(",");
            String[] thuarray = thu.split(",");
            String[] friarray = fri.split(",");
            String[] satarray = sat.split(",");
        ArrayList<String []>test = new ArrayList<>();
            test.add(timearray);
            test.add(monarray);
            test.add(tuearray);
            test.add(wedarray);
            test.add(thuarray);
            test.add(friarray);
            test.add(satarray);

            for(int i=0; i<test.size(); i++){

                for ( int y=0; y<test.get(i).length;y++){
                    TextView tv = new TextView(getActivity().getApplicationContext());
                    tv.setText(test.get(i)[y]);
                    LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(
                            100,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    rlp.setMargins(10,10,10,10);
                    tv.setLayoutParams(rlp);
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    tv.setTypeface(null, Typeface.BOLD);
                    tv.setId(View.generateViewId());
                    tv.setGravity(Gravity.CENTER);
                    tv.setTextColor(getResources().getColor(R.color.black));
                    LinearLayout lv = (LinearLayout)mView.findViewById(ids[i]);
                    lv.addView(tv);
                }
            }
                   //     }



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().registerReceiver(receiver,new IntentFilter(TimeTable_Teacher.INTENT_FILTER));
    }





    @Override
    public void onStop() {
        super.onStop();

        try{
            getActivity().unregisterReceiver(receiver);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle b = intent.getExtras();
            String data = b.getString("data");
            parsejson(data);

        }
    };

    private void  getTimeTableData(){
        new MyVolley(getActivity().getApplicationContext(), new IVolleyResponse() {
            @Override
            public void volleyResponse(String result) {
//                if(result.contentEquals("DONE")){
                    parsejson(result);
//                }
            }

            @Override
            public void volleyError() {

            }
        }).setUrl(URL.UPLOAD_TIMETABLE)
                .setParams("cd_id","3")
                .connect();
    }






}
