package com.hubli.imperium.imaperiumdiary.Attendance.StudentParent;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hubli.imperium.imaperiumdiary.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AttendanceDisplay extends Fragment {


    public AttendanceDisplay() {
        // Required empty public constructor
    }


    private View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_attandance_display, container, false);

        return rootView;
    }

}
