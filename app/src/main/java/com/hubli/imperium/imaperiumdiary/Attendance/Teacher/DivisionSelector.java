package com.hubli.imperium.imaperiumdiary.Attendance.Teacher;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.hubli.imperium.imaperiumdiary.Attendance.Teacher.GiveAttendance.GiveAttendance;
import com.hubli.imperium.imaperiumdiary.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DivisionSelector extends Fragment {


    public DivisionSelector() {
        // Required empty public constructor
    }
    List<String> divisions = new ArrayList<>();

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_selector, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.list);
        final Bundle arguments = getArguments();
        divisions = Arrays.asList(arguments.getStringArray("divisions"));
        getActivity().setTitle(R.string.select_division);
        Collections.sort(divisions,String.CASE_INSENSITIVE_ORDER);
        MyDivisionAdaptor myAdaptor = new MyDivisionAdaptor();
        listView.setAdapter(myAdaptor);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getActivity().getApplicationContext(), GiveAttendance.class);
                intent.putExtra("class",arguments.getString("class"));
                intent.putExtra("division",divisions.get(position));
                getActivity().startActivity(intent);
            }
        });
        return rootView;
    }

    private class MyDivisionAdaptor extends ArrayAdapter<String> {

        public MyDivisionAdaptor() {
            super(getActivity().getApplicationContext(), R.layout.selector_item, divisions);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View ItemView = convertView;
            if (ItemView == null) {
                ItemView = getActivity().getLayoutInflater().inflate(R.layout.selector_item, parent, false);
            }

            TextView class_text=(TextView)ItemView.findViewById(R.id.selector_id) ;
            class_text.setText("Division ");

            String s1 = String.valueOf(divisions.get(position));
            ColorGenerator generator = ColorGenerator.MATERIAL;
            int color = generator.getColor(getItem(position));
            TextDrawable drawable = TextDrawable.builder().buildRoundRect(s1.toUpperCase(),color,20);
            ((ImageView) ItemView.findViewById(R.id.selector_image)).setImageDrawable(drawable);
            return ItemView;
        }
    }
}
