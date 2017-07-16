package com.hubli.imperium.imaperiumdiary.Attendance.StudentParent;


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
import com.hubli.imperium.imaperiumdiary.Utility.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class   AttendanceSubjectWise extends Fragment {


    public AttendanceSubjectWise() {
        // Required empty public constructor
    }

    private View rootView;
    private ArrayList<SubjectData> subjects = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_attendance_subject_wise, container, false);
        ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.progress);
        ListView listView = (ListView) rootView.findViewById(R.id.list);
        MyAdaptor myAdaptor = new MyAdaptor();
        listView.setAdapter(myAdaptor);
        getSubjectsFromServer(myAdaptor,progressBar);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AttendanceDisplay attendanceDisplay = new AttendanceDisplay();
                Bundle bundle = new Bundle();
                bundle.putString("subject_id",subjects.get(i).getSubjectID()+"");
                attendanceDisplay.setArguments(bundle);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_con,attendanceDisplay);
                transaction.addToBackStack("attendance");
                transaction.commit();
            }
        });
        return rootView;
    }

    private void getSubjectsFromServer(final MyAdaptor myAdaptor, final ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
        new MyVolley(getActivity().getApplicationContext(), new IVolleyResponse() {
            @Override
            public void volleyResponse(String result) {
                try {
                    JSONArray array = new JSONArray(result);
                    for (int i=0;i<array.length();i++) {
                        JSONObject json = array.getJSONObject(i);
                        subjects.add(new SubjectData(json.getString("subject_id"),json.getString("subject_name")));
                    }
                    myAdaptor.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void volleyError() {
                progressBar.setVisibility(View.GONE);
            }
        }).setUrl(URL.CLASSES_DIVISIONS_SUBJECTS)
                .setParams("cd_id",new SPData().getUserData(SPData.CLASS_DIVISION_ID))
                .connect();
    }

    private class MyAdaptor extends ArrayAdapter<SubjectData>{

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
            class_text.setText(subjects.get(position).getSubjectName());

            String firstletter = String.valueOf(subjects.get(position).getSubjectName().charAt(0));
            ColorGenerator generator = ColorGenerator.MATERIAL;
            int color = generator.getColor(getItem(position));
            TextDrawable drawable = TextDrawable.builder().buildRoundRect(firstletter.toUpperCase(),color,20);
            ((ImageView) ItemView.findViewById(R.id.sub_image)).setImageDrawable(drawable);
            return ItemView;
        }
    }

}