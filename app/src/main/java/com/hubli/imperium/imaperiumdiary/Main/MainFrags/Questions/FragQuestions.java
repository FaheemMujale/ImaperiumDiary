package com.hubli.imperium.imaperiumdiary.Main.MainFrags.Questions;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hubli.imperium.imaperiumdiary.Data.SPData;
import com.hubli.imperium.imaperiumdiary.Interface.IVolleyResponse;
import com.hubli.imperium.imaperiumdiary.R;
import com.hubli.imperium.imaperiumdiary.Utility.MyVolley;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragQuestions extends Fragment {
    public FragQuestions() {}

    private RecyclerView recyclerView;
    private View view;
    private MyQaAdaptor myQaAdaptor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_frag_questions, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rcView);

        List<QAData> list = new ArrayList<>();
        list.add(new QAData("GK","Whats is this","a","b","c","d","a",3));
        list.add(new QAData("GK","Whats is that","a","b","c","d","a",2));
        myQaAdaptor = new MyQaAdaptor(list, recyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(myQaAdaptor);
        return  view;
    }


    private List<QAData> getQuestionData(){
        MyVolley volley = new MyVolley(getActivity().getApplicationContext(), new IVolleyResponse() {
            @Override
            public void volleyResponse(String result) {

            }

            @Override
            public void volleyError() {

            }
        });
        volley.setParams(SPData.LEVEL,"2");
        volley.setParams(SPData.INSTITUTE_NUMBER,"1");
        volley.connect();
        return null;
    }
}
