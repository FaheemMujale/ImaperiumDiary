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
import com.hubli.imperium.imaperiumdiary.Utility.ImperiumConstants;
import com.hubli.imperium.imaperiumdiary.Utility.MyVolley;
import com.hubli.imperium.imaperiumdiary.Utility.URL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Rafiq Ahmad on 5/16/2017.
 */

public class TimeTableFragment extends Fragment   {
    private String data;
    public TimeTableFragment() {}

    private View mView;
    private FloatingActionButton button;
    int z=0;
    private SPData spdata;
    ProgressDialog progressDialog;
    int ids[] = {R.id.time123,R.id.horizonta1,R.id.horizonta2,R.id.horizonta3,R.id.horizonta4,R.id.horizonta5,R.id.horizonta6};
    /**
     * Initialize instance variables with data from bundle
     */




    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.timetable_studentt, container, false);
        spdata = new SPData();
        getActivity().setTitle("Time Table");
         progressDialog = new ProgressDialog(getActivity());
        getTimeTableData();
        if(spdata.getUserData(SPData.IDENTIFICATION).contains(ImperiumConstants.TEACHER)) {
            button = (FloatingActionButton) mView.findViewById(R.id.addTimetable);
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), TimeTable_Teacher.class);
                    intent.putExtra(ImperiumConstants.TIME_TABLE_DATA,data);
                    startActivity(intent);
                }
            });
        }
        return mView;
    }


    private void parsejson(String response) {
        try {
        data =response;
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
                    int px = convertDpToPixel(100, getActivity().getApplicationContext());
                    int px1 = convertDpToPixel(60, getActivity().getApplicationContext());
                    LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(px,px1);
                    rlp.setMargins(0,0,0,15);
                    //tv.setPadding(10,10,10,10);
                    tv.setLayoutParams(rlp);
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    tv.setTypeface(null, Typeface.BOLD);
                    tv.setId(View.generateViewId());
                    tv.setGravity(Gravity.CENTER);
                    ColorGenerator generator = ColorGenerator.MATERIAL;
                    int color =0;
                    if(i==0)
                     color = generator.getColor(1);
                    else{
                        Random rn = new Random();
                        int num = rn.nextInt(11 - 2 + 2) + 1;
                        color = generator.getColor(num);
                    }

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
    public void onResume() {
        super.onResume();
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
            String data = intent.getStringExtra("data");
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
                .setParams("cd_id",spdata.getUserData(SPData.CLASS_DIVISION_ID))
                .connect();
    }






}
