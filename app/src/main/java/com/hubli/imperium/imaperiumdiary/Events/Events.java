package com.hubli.imperium.imaperiumdiary.Events;


import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.github.tibolte.agendacalendarview.AgendaCalendarView;
import com.github.tibolte.agendacalendarview.CalendarPickerController;
import com.github.tibolte.agendacalendarview.models.BaseCalendarEvent;
import com.github.tibolte.agendacalendarview.models.CalendarEvent;
import com.github.tibolte.agendacalendarview.models.DayItem;
import com.hubli.imperium.imaperiumdiary.Data.SPData;
import com.hubli.imperium.imaperiumdiary.Interface.IVolleyResponse;
import com.hubli.imperium.imaperiumdiary.R;
import com.hubli.imperium.imaperiumdiary.Utility.MyVolley;
import com.hubli.imperium.imaperiumdiary.Utility.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class Events extends Fragment {


    public Events() {
        // Required empty public constructor
    }

    private View rootView;
    private SPData spData;
    private Calendar maxDate;
    private Calendar minDate;
    private AgendaCalendarView calendarView;
    private List<CalendarEvent> calendarEvents;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_events, container, false);

        spData = new SPData(getActivity().getApplicationContext());

        calendarView = (AgendaCalendarView) rootView.findViewById(R.id.agenda_calendar_view);
        minDate = Calendar.getInstance();
        maxDate = Calendar.getInstance();

        minDate.add(Calendar.MONTH, -2);
        minDate.set(Calendar.DAY_OF_MONTH, 1);
        maxDate.add(Calendar.YEAR, 1);

        getActivity().registerReceiver(receiver,new IntentFilter(EventDetailsDialog.EVENT_BROADCAST));
        connectToServer();

        return rootView;
    }


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            calendarEvents.addAll(parseJson(intent.getStringExtra("data")));
            //calendarView.removeAllViews();
            initEventView(calendarEvents);

        }
    };

    @Override
    public void onPause() {
        super.onPause();
        try{
            getActivity().unregisterReceiver(receiver);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void connectToServer() {

        new MyVolley(getActivity().getApplicationContext(), new IVolleyResponse() {
            @Override
            public void volleyResponse(String result) {
                if (result.contains("event_id")) {
                    spData.storeEventsData(result);
                    calendarEvents = parseJson(result);
                    initEventView(calendarEvents);
                }
            }

            @Override
            public void volleyError() {

            }
        }).setUrl(URL.EVENTS_FETCH)
                .setParams(SPData.USER_NUMBER, spData.getUserData(SPData.USER_NUMBER))
                .setParams(SPData.INSTITUTE_NUMBER, spData.getUserData(SPData.INSTITUTE_NUMBER))
                .connect();
    }

    private List<CalendarEvent> parseJson(String result) {
        List<CalendarEvent> eventList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);

                ColorGenerator generator = ColorGenerator.MATERIAL;
                int color = generator.getColor(json.get("event_id"));

                String startData[] = json.getString("start_date").split("/");
                String endData[] = json.getString("end_date").split("/");
                Calendar start = Calendar.getInstance();
                start.set(Integer.parseInt(startData[2]), Integer.parseInt(startData[1]) - 1, Integer.parseInt(startData[0]));
                Calendar end = Calendar.getInstance();
                end.set(Integer.parseInt(endData[2]), Integer.parseInt(endData[1]) - 1, Integer.parseInt(endData[0]));

                String placeTime = json.getString("event_place") + " at " + json.getString("start_time");

                eventList.add(new BaseCalendarEvent(json.getString("title"), "", placeTime, color, start, end, false));
            }
            return eventList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void initEventView(List<CalendarEvent> calendarEvents){
        calendarView.init(calendarEvents, minDate, maxDate, Locale.getDefault(), new CalendarPickerController() {
            @Override
            public void onDaySelected(DayItem dayItem) {
                try {
                    Log.e("year",dayItem.getDate().getYear()+"");
                    android.app.FragmentManager fragmentManager = getActivity().getFragmentManager();
                    EventDetailsDialog dialog = new EventDetailsDialog();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("date", dayItem);
                    dialog.setArguments(bundle);
                    dialog.show(fragmentManager, "tag");
                }catch(Exception e){}
            }

            @Override
            public void onEventSelected(CalendarEvent event) {
            }

            @Override
            public void onScrollToDate(Calendar calendar) {

            }
        });
    }
}
