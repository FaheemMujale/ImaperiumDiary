package com.hubli.imperium.imaperiumdiary.Attendance.Teacher;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.hubli.imperium.imaperiumdiary.Attendance.StudentParent.AttendanceSubjectWise;
import com.hubli.imperium.imaperiumdiary.Interface.IVolleyResponse;
import com.hubli.imperium.imaperiumdiary.R;
import com.hubli.imperium.imaperiumdiary.Utility.MyVolley;
import com.hubli.imperium.imaperiumdiary.Utility.URL;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Selector extends Fragment {


    public Selector() {
        // Required empty public constructor
    }

    List<String> items = new ArrayList<>();

    private View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_selector, container, false);
        ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.progress);
        ListView listView = (ListView) rootView.findViewById(R.id.list);
        MyAdaptor myAdaptor = new MyAdaptor();
        listView.setAdapter(myAdaptor);
        getDateFromServer(myAdaptor);
        return rootView;
    }

    private void getDateFromServer(MyAdaptor myAdaptor) {
//        new MyVolley(getActivity().getApplicationContext(), new IVolleyResponse() {
//            @Override
//            public void volleyResponse(String result) {
//
//            }
//
//            @Override
//            public void volleyError() {
//
//            }
//        }).setUrl(URL)
    }


    private class MyAdaptor extends ArrayAdapter<String>{

        public MyAdaptor() {
            super(getActivity().getApplicationContext(), R.layout.selector_item,items);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View ItemView = convertView;
            if (ItemView == null) {
                ItemView = getActivity().getLayoutInflater().inflate(R.layout.selector_item, parent, false);
            }

            String[] s = items.get(position).split("~");
            TextView class_text=(TextView)ItemView.findViewById(R.id.selector_id) ;
            class_text.setText(s[0]);

            String s1 = String.valueOf(s[1]);
            ColorGenerator generator = ColorGenerator.MATERIAL;
            int color = generator.getColor(getItem(position));
            TextDrawable drawable = TextDrawable.builder().buildRoundRect(s1.toUpperCase(),color,20);
            ((ImageView) ItemView.findViewById(R.id.selector_image)).setImageDrawable(drawable);
            return ItemView;
        }
    }
}
