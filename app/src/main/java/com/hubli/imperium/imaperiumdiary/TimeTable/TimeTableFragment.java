package com.hubli.imperium.imaperiumdiary.TimeTable;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class TimeTableFragment extends Fragment   {

    public TimeTableFragment() {}

    private View mView;
    private FloatingActionButton button;
    int z=0;
    private SPData spdata;
    ProgressDialog progressDialog;
    private String usertype;
    int ids[] = {R.id.time123,R.id.horizonta1,R.id.horizonta2,R.id.horizonta3,R.id.horizonta4,R.id.horizonta5,R.id.horizonta6};
    private String[] days = {"time","mon","tue","wed","thu","fri","sat"};
    /**
     * Initialize instance variables with data from bundle
     */




    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.timetable_studentt, container, false);
        spdata = new SPData(getActivity().getApplicationContext());
        usertype = spdata.getUserData("USERTYPE");
          progressDialog = new ProgressDialog(getActivity());
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
        try {

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
                    tv.setText(test.get(i)[y].replace("[","").replace("]",""));
                    int px = convertDpToPixel(90, getActivity().getApplicationContext());
                    LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(px,px);
                    rlp.setMargins(10,10,10,10);
                    tv.setLayoutParams(rlp);
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    tv.setTypeface(null, Typeface.BOLD);
                    tv.setId(View.generateViewId());
                    tv.setGravity(Gravity.CENTER);
                    ColorGenerator generator = ColorGenerator.MATERIAL;
                    int color = generator.getColor(y);
                    tv.setBackgroundColor(color);
//                    tv.setBackgroundColor(Color.parseColor("#00BCD4"));
                    LinearLayout lv = (LinearLayout)mView.findViewById(ids[i]);
                    lv.addView(tv);
                }
            }
                   //     }
progressDialog.dismiss();


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().registerReceiver(receiver,new IntentFilter(TimeTable_Teacher.INTENT_FILTER));
    }

    public static int convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int px = (int) (dp * (metrics.densityDpi / 160f));
        return px;
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
        progressDialog.show();
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
