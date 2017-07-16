package com.hubli.imperium.imaperiumdiary.Exams;

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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.hubli.imperium.imaperiumdiary.models.Exam;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Rafiq Ahmad on 5/16/2017.
 */

public class ExamList extends Fragment   {

    public ExamList() {}

    private View mView;
    private ExamAdapter mAdapter;
    private SPData spdata;
    private TextView nodatatv;
    ProgressDialog progressDialog;
    private ArrayList<Exam> items;
    /**
     * Initialize instance variables with data from bundle
     */




    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.exam_list, container, false);
        spdata = new SPData();
        getActivity().setTitle(R.string.exams);
         progressDialog = new ProgressDialog(getActivity());
        RecyclerView mRecy = (RecyclerView)mView.findViewById(R.id.rv_cn);
        nodatatv = (TextView)mView.findViewById(R.id.handler);
        mRecy.setLayoutManager(new LinearLayoutManager(getActivity()));
        if(spdata.getUserData(SPData.IDENTIFICATION).contains(ImperiumConstants.TEACHER)) {
            FloatingActionButton floatingActionButton = (FloatingActionButton) mView.findViewById(R.id.fab);
            floatingActionButton.setVisibility(View.VISIBLE);
            floatingActionButton.setEnabled(true);
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), Insert_Exam.class);
                    startActivity(intent);
                }
            });
        }


        /* Common toolbar setup */


        mAdapter = new ExamAdapter(getActivity());
        mRecy.setAdapter(mAdapter);

        listexams();

        return mView;
    }



    private void parsejson(String response) {
        try {

            JSONArray json = new JSONArray(response);
            Log.e("json ", response);
            items = new ArrayList<>();
            for (int i = 0; i <= json.length() - 1; i++) {
                JSONObject jsonobj = json.getJSONObject(i);
                if(jsonobj.getString("divison").contains("0") || jsonobj.getString("divison").contains(SPData.DIVISION)) {
                    items.add(new Exam(jsonobj.getInt("exam_id"), jsonobj.getString("exam_title"), jsonobj.getString("exam_data"),
                            jsonobj.getString("class"), jsonobj.getString("divison"), jsonobj.getString("added_date")));
//                items.add(new LeaveInfo("Shann", "21-2-2017", "15-2-2017", "Pending", "Health Issue"));
                }
            }
            mAdapter.addItems(items);

            progressDialog.dismiss();


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(receiver,new IntentFilter(Insert_Exam.INTENT_FILTER));
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

    private void  listexams(){
        String classname = spdata.getUserData(SPData.CLASS);
        if(classname==null || classname.contains(""))
            classname = "10";
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
                progressDialog.dismiss();


               nodatatv.setVisibility(View.VISIBLE);
//                LottieAnimationView animationView = (LottieAnimationView) findViewById(R.id.animation_view);
//                animationView.setAnimation("hello-world.json");
//                animationView.loop(true);
//                animationView.playAnimation();
                Toast.makeText(getActivity(),"fail",Toast.LENGTH_LONG).show();
            }
        }).setUrl(URL.EXAM_LIST)
                .setParams(SPData.CLASS,classname)
                .connect();
    }






}
