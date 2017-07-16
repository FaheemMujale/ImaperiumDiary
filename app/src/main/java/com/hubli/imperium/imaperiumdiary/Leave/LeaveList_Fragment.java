package com.hubli.imperium.imaperiumdiary.Leave;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hubli.imperium.imaperiumdiary.Data.SPData;
import com.hubli.imperium.imaperiumdiary.Interface.IVolleyResponse;
import com.hubli.imperium.imaperiumdiary.R;
import com.hubli.imperium.imaperiumdiary.Utility.MyVolley;
import com.hubli.imperium.imaperiumdiary.Utility.URL;
import com.hubli.imperium.imaperiumdiary.models.LeaveInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Rafiq Ahmad on 6/6/2017.
 */

public class LeaveList_Fragment extends Fragment {
    private SPData spdata;
    public static final String INTENTFILTER ="intent_filter_l";
    private View mView;
    private String usertype;
    private LeaveListAdapter mAdapter;
    private ArrayList<LeaveInfo> items ;
    private static final String BACK_STACK_MAIN = "main_tag";
    private ProgressBar mProgBar;
    private FloatingActionButton mFab;
    public LeaveList_Fragment(){}

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.leave_list, container, false);
        spdata = new SPData();
        getActivity().setTitle("Leave Applications");
        RecyclerView mRecy = (RecyclerView) mView.findViewById(R.id.rv_ff);
        mProgBar = (ProgressBar) mView.findViewById(R.id.prog_bar_ff);
        mRecy.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        mAdapter = new LeaveListAdapter(getActivity().getApplicationContext());
        mRecy.setAdapter(mAdapter);
            mFab= (FloatingActionButton) mView.findViewById(R.id.fab);
        if(spdata.getIdentification() == SPData.TEACHER){
            mFab.setVisibility(View.GONE);
        }

            mFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Leaveapp_form tabAdaptor = new Leaveapp_form();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.main_con,tabAdaptor);
                    transaction.addToBackStack(BACK_STACK_MAIN);
                    transaction.commit();
                }
            });
//        }
        getLeaveList();
        return mView;
    }
    public static LeaveList_Fragment newInstance() {
        LeaveList_Fragment fragment = new LeaveList_Fragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }





    private void getLeaveList(){
       MyVolley volley = new MyVolley(getActivity().getApplicationContext(), new IVolleyResponse() {
            @Override
            public void volleyResponse(String result) {
                    mProgBar.setVisibility(View.GONE);
                    parsejson(result);

            }

            @Override
            public void volleyError() {
               Toast.makeText(getActivity().getApplicationContext(),"Connection Error",Toast.LENGTH_LONG).show();
            }
        });
        volley.setUrl(URL.LEAVE_LIST);
        if (spdata.getIdentification() == SPData.TEACHER) {
            volley.setParams(SPData.CLASS_DIVISION_ID,spdata.getUserData(SPData.CLASS_DIVISION_ID));
        }
        volley.setParams(SPData.USER_NUMBER,spdata.getUserData(SPData.USER_NUMBER));
        volley.connect();
    }

    private void parsejson(String response)  {
        try {
            JSONArray json = new JSONArray(response);
            Log.e("json ", response);
            items = new ArrayList<>();
            for (int i = 0; i <= json.length() - 1; i++) {
                JSONObject jsonobj = json.getJSONObject(i);
                items.add(new LeaveInfo( jsonobj.getInt("leave_id"),jsonobj.getString("first_name")+" "+jsonobj.getString("last_name"),
                        jsonobj.getString("todate"), jsonobj.getString("fromdate"), jsonobj.getString("status"),
                        jsonobj.getString("detail"),jsonobj.getString("file_link")));
//                items.add(new LeaveInfo("Shann", "21-2-2017", "15-2-2017", "Pending", "Health Issue"));
            }
            mAdapter.addItems(items);

        }catch (JSONException e){
            e.printStackTrace();
        }


    }
}
