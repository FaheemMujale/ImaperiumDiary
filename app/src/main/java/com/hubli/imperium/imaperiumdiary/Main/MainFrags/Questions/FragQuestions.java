package com.hubli.imperium.imaperiumdiary.Main.MainFrags.Questions;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hubli.imperium.imaperiumdiary.Data.MySqlDB;
import com.hubli.imperium.imaperiumdiary.Data.SPData;
import com.hubli.imperium.imaperiumdiary.Interface.IVolleyResponse;
import com.hubli.imperium.imaperiumdiary.R;
import com.hubli.imperium.imaperiumdiary.Utility.GenericMethods;
import com.hubli.imperium.imaperiumdiary.Utility.MyVolley;
import com.hubli.imperium.imaperiumdiary.Utility.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragQuestions extends Fragment {
    private TextView noQuestions;
    private RecyclerView recyclerView;
    private View view;
    private MyQaAdaptor myQaAdaptor;
    private MySqlDB myDb;
    private SPData spData;
    private ProgressBar progressBar;

    public FragQuestions() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_frag_questions, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rcView);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        noQuestions = (TextView) view.findViewById(R.id.no_questions);

        myDb = new MySqlDB(getActivity().getApplicationContext());
        spData = new SPData();
        myQaAdaptor = new MyQaAdaptor(recyclerView,getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(myQaAdaptor);
        if((myDb.getQuestions("*",-1).getCount()==0) || !spData.isQuestionToday(GenericMethods.getDateSum())) {
            getQuestionData();
        }else {
            populateList();
        }
        return  view;
    }


    private void getQuestionData(){
        progressBar.setVisibility(View.VISIBLE);
        new MyVolley(getActivity().getApplicationContext(), new IVolleyResponse() {
            @Override
            public void volleyResponse(String result) {
                String data[] = result.toString().split("~");
                int dateSum = Integer.parseInt(data[0]);
                String questionData = data[1];
                questionData = questionData.replace("][",",");
                spData.setQuestionDate(dateSum);
                parseJson(questionData.toString());
            }

            @Override
            public void volleyError() {
                progressBar.setVisibility(View.GONE);
                noQuestions.setVisibility(View.VISIBLE);
            }
        })
        .setUrl(URL.FETCH_QUESTIONS)
        .setParams(SPData.LEVEL,"1")
        .connect();
    }

    private void parseJson(String s){
        myDb.clearQuestionData();
        try {
            JSONArray jsonArray = new JSONArray(s);
            for(int i=0;i<jsonArray.length();i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                myDb.insertQuestions(Integer.parseInt(jsonObject.getString("qid")),jsonObject.getString("question_text"),
                        jsonObject.getString("type"),
                        jsonObject.getString("optA"),jsonObject.getString("optB"),
                        jsonObject.getString("optC"),jsonObject.getString("optD"),
                        jsonObject.getString("answer"));
            }
            populateList();
        } catch (JSONException e) {
            if(s.contains("question_text")) {
                s = s.substring(0, s.length() - 2) + "]";
                parseJson(s);
            }else {
                noQuestions.setVisibility(View.VISIBLE);
                e.printStackTrace();
            }
        }
    }
    private void populateList(){
        progressBar.setVisibility(View.GONE);
        List<QAData> items = new ArrayList<>();
        Cursor data = myDb.getQuestions("*",-1);
        if(data.getCount()>0){
            while (data.moveToNext()){
                items.add(new QAData(myDb,data));
            }
            myQaAdaptor.setItems(items);
        }
    }
}
