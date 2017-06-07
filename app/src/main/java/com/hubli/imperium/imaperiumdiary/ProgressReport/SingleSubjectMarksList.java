package com.hubli.imperium.imaperiumdiary.ProgressReport;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.hubli.imperium.imaperiumdiary.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SingleSubjectMarksList extends Fragment {


    public SingleSubjectMarksList() {
        // Required empty public constructor
    }


    private View rootView;
    private MyAdaptor myAdaptor;
    public static List<MarksData> data = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_single_subject_marks_list, container, false);
        getActivity().setTitle(getArguments().getString("subject"));

        ListView marksList = (ListView) rootView.findViewById(R.id.marks);
        Button button = (Button) rootView.findViewById(R.id.btn);
        myAdaptor = new MyAdaptor();
        marksList.setAdapter(myAdaptor);
        populateList();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(data.size() > 1) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), SingleSubjectGraph.class);
                    intent.putExtra("subject", getArguments().getString("subject"));
                    getActivity().startActivity(intent);
                }else{
                    Toast.makeText(getActivity().getApplicationContext(),"Cant show graph for single subject",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return rootView;

    }

    private void populateList() {
        MarksDB marksDB = new MarksDB(getActivity().getApplicationContext());
        Cursor marksData = marksDB.getMarksData(getArguments().getString("subject"));
        data.clear();
        if(marksData.getCount() > 0){
            while (marksData.moveToNext()){
                float total = marksData.getFloat(2);
                float obt = marksData.getFloat(4);
                float percentage = (obt/total)*100;
                data.add(new MarksData(marksData.getString(0),marksData.getString(1),
                        total,marksData.getFloat(3),obt,percentage));
            }
            myAdaptor.notifyDataSetChanged();
        }

    }

    class MyAdaptor extends ArrayAdapter<MarksData> {

        public MyAdaptor() {
            super(getActivity().getApplicationContext(), R.layout.marks_item, data);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View ItemView = convertView;
            if (ItemView == null) {
                ItemView = getActivity().getLayoutInflater().inflate(R.layout.marks_item, parent, false);
            }
            MarksData currentItem = data.get(position);
            TextView examName = (TextView) ItemView.findViewById(R.id.exam_name);
            TextView totalMarks = (TextView) ItemView.findViewById(R.id.total_marks);
            TextView obtMarks = (TextView) ItemView.findViewById(R.id.obtained_marks);
            TextView min = (TextView) ItemView.findViewById(R.id.min_marks);
            TextView percentage = (TextView) ItemView.findViewById(R.id.percentage_marks);
            TextView examDate = (TextView) ItemView.findViewById(R.id.date_exam);
            examDate.setText(currentItem.getDate());
            examName.setText(currentItem.getExam_name());
            min.setText(currentItem.getMinMarks()+"");
            totalMarks.setText(currentItem.getTotal_max()+"");
            if(currentItem.getObtained_max() < currentItem.getMinMarks()){
                obtMarks.setTextColor(Color.RED);
                percentage.setTextColor(Color.RED);
            }else{
                obtMarks.setTextColor(Color.BLACK);
                percentage.setTextColor(Color.BLACK);
            }
            obtMarks.setText(currentItem.getObtained_max()+"");
            percentage.setText(currentItem.getPercentage()+"");
            return ItemView;
        }
    }

}
