package com.hubli.imperium.imaperiumdiary.ProgressReport;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.hubli.imperium.imaperiumdiary.Data.SPData;
import com.hubli.imperium.imaperiumdiary.Interface.IVolleyResponse;
import com.hubli.imperium.imaperiumdiary.R;
import com.hubli.imperium.imaperiumdiary.Utility.MyVolley;
import com.hubli.imperium.imaperiumdiary.Utility.ServerConnect;
import com.hubli.imperium.imaperiumdiary.Utility.URL;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubjectList extends Fragment {

    private SPData spData;
    private View rootView;
    private MarksDB marksDB;
    private MyAdaptor myAdaptor;

    private ProgressBar progressBar;
    private TextView notAvailable;
    public Button btn1;
    ArrayList<String> subjects = new ArrayList<>();
    private static boolean backPressed = false;
    public SubjectList() {
        backPressed = false;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_subject_list, container, false);
        btn1=(Button)rootView.findViewById(R.id.button_prog);
        spData =new SPData();
        marksDB = new MarksDB(getActivity().getApplicationContext());
        getActivity().setTitle("Progress Report");
        progressBar = (ProgressBar)rootView.findViewById(R.id.report_progress);
        notAvailable = (TextView)rootView.findViewById(R.id.reportnot_available);
        ListView listView = (ListView) rootView.findViewById(R.id.list);
        myAdaptor = new MyAdaptor();
        listView.setAdapter(myAdaptor);
        subjects.clear();
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            getActivity().startActivity(new Intent(getActivity().getApplicationContext(),AllSubjectGraph.class));
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                backPressed = true;
                SingleSubjectMarksList singleSubjectMarksList = new SingleSubjectMarksList();
                Bundle bundle = new Bundle();
                bundle.putString("subject",subjects.get(i));
                singleSubjectMarksList.setArguments(bundle);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_con, singleSubjectMarksList);
                transaction.addToBackStack("subjectList");
                transaction.commit();
            }
        });
        getDataFromServer();
        return rootView;
    }

    private void getDataFromServer() {
        if(ServerConnect.checkInternetConenction(getActivity()) && !backPressed){
            progressBar.setVisibility(View.VISIBLE);
            new MyVolley(getActivity().getApplicationContext(), new IVolleyResponse() {
                @Override
                public void volleyResponse(String result) {
                    marksDB.clearMarksData();
                    marksDB.insertMarksData(result);
                    populateList();
                    progressBar.setVisibility(View.GONE);
                    notAvailable.setVisibility(View.GONE);
                }

                @Override
                public void volleyError() {
                    progressBar.setVisibility(View.GONE);
                    notAvailable.setVisibility(View.VISIBLE);
                }
            }).setUrl(URL.MARKS).setParams(SPData.USER_NUMBER,spData.getUserData(SPData.USER_NUMBER)).connect();
        }else{
            populateList();
        }
    }

    private void populateList(){
        Cursor distinctSubjects = marksDB.getDistinctSubjects();
        if(distinctSubjects.getCount() > 0){
            while (distinctSubjects.moveToNext()){
                subjects.add(distinctSubjects.getString(0));
            }
            myAdaptor.notifyDataSetChanged();

        }else{
            notAvailable.setVisibility(View.VISIBLE);
        }
    }
    private class MyAdaptor extends ArrayAdapter<String> {

        public MyAdaptor() {
            super(getActivity().getApplicationContext(),R.layout.subjects_item, subjects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View ItemView = convertView;
            if (ItemView == null) {
                ItemView = getActivity().getLayoutInflater().inflate(R.layout.subjects_item, parent, false);
            }

            TextView class_text=(TextView)ItemView.findViewById(R.id.sub_name) ;
            class_text.setText(subjects.get(position));

            String firstletter = String.valueOf(subjects.get(position).charAt(0));
            ColorGenerator generator = ColorGenerator.MATERIAL;
            int color = generator.getColor(getItem(position));
            TextDrawable drawable = TextDrawable.builder().buildRoundRect(firstletter.toUpperCase(),color,20);
            ((ImageView) ItemView.findViewById(R.id.sub_image)).setImageDrawable(drawable);
            return ItemView;
        }
    }

}
