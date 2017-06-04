package com.hubli.imperium.imaperiumdiary.Events;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.tibolte.agendacalendarview.models.DayItem;
import com.hubli.imperium.imaperiumdiary.Data.SPData;
import com.hubli.imperium.imaperiumdiary.Interface.IVolleyResponse;
import com.hubli.imperium.imaperiumdiary.R;
import com.hubli.imperium.imaperiumdiary.Utility.MyVolley;
import com.hubli.imperium.imaperiumdiary.Utility.URL;

import java.util.Calendar;
import java.util.Date;

public class EventDetailsDialog extends DialogFragment implements View.OnClickListener{

    public EventDetailsDialog() {
        // Required empty public constructor
    }

    private View rootView;
    private TextView eventTitle;
    private EditText eventPlace;
    private TextView startDate;
    private TextView endDate;
    private TextView time;

    private int year_h;
    private int month_h;
    private int day_h;

    Calendar startCal,endCal;
    private String sStartDate;
    private String sEndDate;

    public static final String EVENT_BROADCAST = "EVENT_BROADCAST";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_event_details_dialog, container, false);
        eventTitle = (EditText) rootView.findViewById(R.id.event_title);
        eventPlace = (EditText) rootView.findViewById(R.id.event_place);
        startDate = (TextView) rootView.findViewById(R.id.start_date);
        endDate = (TextView) rootView.findViewById(R.id.end_date);
        time = (TextView) rootView.findViewById(R.id.time);
        getDialog().setTitle("New Event");

        ((ImageView)rootView.findViewById(R.id.sdateBtn)).setOnClickListener(this);
        ((ImageView)rootView.findViewById(R.id.stdateBtn)).setOnClickListener(this);
        ((ImageView)rootView.findViewById(R.id.timeBtn)).setOnClickListener(this);
        ((Button)rootView.findViewById(R.id.submitBtn)).setOnClickListener(this);

        Bundle arguments = getArguments();
        Date date = ((DayItem)arguments.getParcelable("date")).getDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        year_h = calendar.get(Calendar.YEAR);
        month_h= calendar.get(Calendar.MONTH) +1;
        day_h = calendar.get(Calendar.DAY_OF_MONTH);

        startCal = calendar;
        endCal = calendar;

        endCal = Calendar.getInstance();

        sStartDate = day_h+"/"+month_h+"/"+year_h;
        startDate.setText("Start Date: "+sStartDate);
        endDate.setText("End Date:  "+sStartDate);
        startDate.setGravity(Gravity.CENTER);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        switch (id){
            case R.id.sdateBtn:
                datePickerDialogStartDate(startDate);
                break;
            case R.id.stdateBtn:
               datePickerDialogEndDate(endDate);
                break;

            case R.id.timeBtn:
                timePickerDialog(time);
                break;
            case R.id.submitBtn:
                uploadEvent();
                break;
        }
    }

    private void uploadEvent() {
        SPData spData = new SPData();
        if(eventTitle.getText().toString().length() > 0 && eventPlace.getText().toString().length() >0) {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            new MyVolley(getActivity().getApplicationContext(), new IVolleyResponse() {
                @Override
                public void volleyResponse(String result) {
                    Intent intent = new Intent(EVENT_BROADCAST);
                    intent.putExtra("data",result);
                    getActivity().sendBroadcast(intent);
                    progressDialog.dismiss();
                    getDialog().dismiss();

                }

                @Override
                public void volleyError() {
                    progressDialog.dismiss();
                }
            }).setUrl(URL.EVENTS_INSERT)
                    .setParams(SPData.USER_NUMBER,spData.getUserData(SPData.USER_NUMBER))
                    .setParams("title",eventTitle.getText().toString())
                    .setParams("place",eventPlace.getText().toString())
                    .setParams("time",time.getText().toString().split("time:")[1])
                    .setParams("startDate",startDate.getText().toString().split("Date: ")[1].trim())
                    .setParams("endDate",endDate.getText().toString().split("Date: ")[1].trim())
                    .connect();
        }else{
            Toast.makeText(getActivity().getApplicationContext(),"Please fill the event details",Toast.LENGTH_SHORT).show();
        }
    }

    private void datePickerDialogStartDate(final TextView textView){

        new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener()
        {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                year_h = year;
                month_h = monthOfYear+1;
                day_h = dayOfMonth;
                startCal.set(year,monthOfYear,dayOfMonth);

                if (endCal.getTimeInMillis() >= startCal.getTimeInMillis()){
                    textView.setText("Start Date: "+day_h+"/"+month_h+"/"+year_h);
                }else {
                    Toast.makeText(getActivity().getApplicationContext(),"Start Date Should be before End Date",Toast.LENGTH_SHORT).show();
                }

            }
        }, year_h, month_h-1, day_h).show();
    }


    private void datePickerDialogEndDate(final TextView textView){

        new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener()
        {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                year_h = year;
                month_h = monthOfYear+1;
                day_h = dayOfMonth;
                endCal.set(year,monthOfYear,dayOfMonth);
                if (endCal.getTimeInMillis() >= startCal.getTimeInMillis() ){
                    textView.setText("End Date: "+day_h+"/"+month_h+"/"+year_h);
                }else {
                    Toast.makeText(getActivity().getApplicationContext(),"End Date Should be after Start Date",Toast.LENGTH_SHORT).show();
                }

            }
        }, year_h, month_h-1, day_h).show();
    }

    private void timePickerDialog(final TextView textView){

        new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int h, int m) {
                String time;
                Log.e(h+"",m+"");
                if((h - 12)>= 0){
                    time = (h-12)+":"+m+" PM";
                }else{
                    time = h+":"+m+" PM";
                }
                textView.setText("Start time: "+time);
            }
        },0,0,false).show();

    }
}
