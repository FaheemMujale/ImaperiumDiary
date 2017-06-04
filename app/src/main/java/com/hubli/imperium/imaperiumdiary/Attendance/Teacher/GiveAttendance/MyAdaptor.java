package com.hubli.imperium.imaperiumdiary.Attendance.Teacher.GiveAttendance;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hubli.imperium.imaperiumdiary.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Faheem on 04-06-2017.
 */

public class MyAdaptor extends ArrayAdapter<ListData> {

    private final Activity context;
    public List<ListData> items;
    public static final String PRESENT = "P";
    public static final String LEAVE = "L";
    public static final String ABSENT = "A";


    public class ListViewHolder {
        TextView text_name;
        TextView text_roll;
        RadioGroup optGroup;
        RadioButton rPresent;
        RadioButton rAbsent;
        RadioButton rLeave;
    }

    public MyAdaptor( Activity context, List<ListData> items) {
        super(context, R.layout.attendance_item,items);
        this.items = items;
        this.context = context;
    }

    public List<ListData> getItems(){
        return this.items;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View rowView;
        final ListViewHolder listViewHolder;
        if(convertView == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = layoutInflater.inflate(R.layout.attendance_item,parent,false);
            listViewHolder = new ListViewHolder();
            listViewHolder.text_name = (TextView) rowView.findViewById(R.id.textView_name);
            listViewHolder.text_roll = (TextView) rowView.findViewById(R.id.textView_roll);

            listViewHolder.optGroup = (RadioGroup) rowView.findViewById(R.id.optGroup);
            listViewHolder.rPresent = (RadioButton)rowView.findViewById(R.id.radio_buttonP);
            listViewHolder.rAbsent =(RadioButton) rowView.findViewById(R.id.radio_buttonA);
            listViewHolder.rLeave = (RadioButton)rowView.findViewById(R.id.radio_buttonL);
            rowView.setTag(listViewHolder);
        }else{
            rowView = convertView;
            listViewHolder = (ListViewHolder) rowView.getTag();
        }

        final ListData currentItem = items.get(position);

        listViewHolder.text_roll.setText(currentItem.getRollNumber());
        listViewHolder.text_name.setText(currentItem.getStudentName());

        switch (currentItem.getAttendance()){
            case PRESENT:
                listViewHolder.rPresent.setChecked(true);
                break;
            case LEAVE:
                listViewHolder.rLeave.setChecked(true);
                break;
            case ABSENT:
                listViewHolder.rAbsent.setChecked(true);
                break;
        }

        listViewHolder.optGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i){
                    case R.id.radio_buttonP:
                        currentItem.setAttendance(PRESENT);
                        break;
                    case R.id.radio_buttonL:
                        currentItem.setAttendance(LEAVE);
                        break;
                    case R.id.radio_buttonA:
                        currentItem.setAttendance(ABSENT);
                        break;
                }
            }
        });
        return rowView;
    }
}
