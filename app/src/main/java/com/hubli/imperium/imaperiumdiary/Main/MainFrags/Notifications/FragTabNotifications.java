package com.hubli.imperium.imaperiumdiary.Main.MainFrags.Notifications;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hubli.imperium.imaperiumdiary.Data.SPData;
import com.hubli.imperium.imaperiumdiary.Events.Events;
import com.hubli.imperium.imaperiumdiary.Interface.IVolleyResponse;
import com.hubli.imperium.imaperiumdiary.Main.MainActivity;
import com.hubli.imperium.imaperiumdiary.Main.MainTabAdaptor;
import com.hubli.imperium.imaperiumdiary.R;
import com.hubli.imperium.imaperiumdiary.TimeTable.TimeTableFragment;
import com.hubli.imperium.imaperiumdiary.Utility.GenericMethods;
import com.hubli.imperium.imaperiumdiary.Utility.MyVolley;
import com.hubli.imperium.imaperiumdiary.Utility.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragTabNotifications extends Fragment {


    private ProgressBar progressBar;
    private TextView noNotifications;

    public FragTabNotifications() {
        // Required empty public constructor
    }
    private List<NotificationData> items = new ArrayList<NotificationData>();
    private MyListAdapter myListAdapter;
    SPData spData= new SPData();

    //types


    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_frag_tab_notifications, container, false);
        ListView list = (ListView) view.findViewById(R.id.list);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        noNotifications = (TextView) view.findViewById(R.id.no_notifications);
        myListAdapter = new MyListAdapter();
        list.setAdapter(myListAdapter);
        getData();
        setItemClickListner(list);
        return view;
    }

    private void setItemClickListner(ListView list) {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String type = items.get(i).getType();

                if(type.contains(NotificationData.EVENT)){
                    replaceFragment(new Events());
                }else if(type.contains(NotificationData.TIME_TABLE)){
                    replaceFragment(new TimeTableFragment());
                }
            }
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_con, fragment);
        transaction.addToBackStack(MainActivity.BACK_STACK_MAIN);
        transaction.commit();
    }

    public void getData() {
        items.clear();
        progressBar.setVisibility(View.VISIBLE);
        noNotifications.setVisibility(View.GONE);
        if(spData.getNotificationCount() > 0 || spData.getNotificationData() == null) {
            Log.e("count",spData.getNotificationCount()+":::"+spData.getNotificationData());
            new MyVolley(getActivity().getApplicationContext(), new IVolleyResponse() {
                @Override
                public void volleyResponse(String result) {
                    parseJson(result);
                    spData.resetNotificationCount();
                    spData.storeNotificationData(result);
                    getActivity().sendBroadcast(new Intent(MainTabAdaptor.NOTIFICATION_BC_FILTER));
                }

                @Override
                public void volleyError() {
                    progressBar.setVisibility(View.GONE);
                    noNotifications.setVisibility(View.VISIBLE);
                }
            }).setUrl(URL.NOTIFICATION)
                    .setParams("cd_id", spData.getUserData(SPData.CLASS_DIVISION_ID))
                    .connect();
        }else{
            parseJson(spData.getNotificationData());
        }
    }

    private void parseJson(final String notificationData) {
        progressBar.setVisibility(View.GONE);
        try {
            JSONArray jsonArray = new JSONArray(notificationData);
            for(int i=0;i<jsonArray.length();i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if(spData.getIdentification() != SPData.TEACHER) {
                    items.add(new NotificationData(jsonObject.getString("notification_id"),
                            jsonObject.getString("notification_type"),
                            jsonObject.getString("cd_id"),
                            jsonObject.getString("notification_text"),
                            jsonObject.getString("date")));
                }else{
                    if(!jsonObject.getString("notification_type").contains(NotificationData.HOME_WORK)){
                        items.add(new NotificationData(jsonObject.getString("notification_id"),
                                jsonObject.getString("notification_type"),
                                jsonObject.getString("cd_id"),
                                jsonObject.getString("notification_text"),
                                jsonObject.getString("date")));
                    }
                }
                Collections.sort(items,new MyComparator());
                myListAdapter.notifyDataSetChanged();

            }
        } catch (JSONException e) {
            e.printStackTrace();
            noNotifications.setVisibility(View.VISIBLE);
        }
    }


    private class MyListAdapter extends ArrayAdapter<NotificationData> {

        public MyListAdapter() {
            super(getActivity().getApplicationContext(), R.layout.notification_item, items);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if(itemView == null){
                itemView = getActivity().getLayoutInflater().inflate(R.layout.notification_item,parent,false);
            }
            NotificationData current = items.get(position);
            TextView title = (TextView) itemView.findViewById(R.id.title);
            TextView date = (TextView) itemView.findViewById(R.id.time);
            TextView text = (TextView) itemView.findViewById(R.id.text);
            String title_text;
            if(current.getType().contentEquals(NotificationData.MODEL_QP)){
                title_text = "MODEL QUESTION PAPER";
            }else{
                title_text = current.getType();
            }
            title_text = title_text.replace("_"," ");
            title.setText(title_text);
            date.setText(GenericMethods.getTimeString(current.getDate()));
            text.setText(current.getNotificationText());
            return itemView;
        }
    }

    private class MyComparator implements Comparator<NotificationData> {

        @Override
        public int compare(NotificationData lhs, NotificationData rhs) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(GenericMethods.DATE_FORMAT);
            try {
                Date date1 = dateFormat.parse(lhs.getDate());
                Date date2 = dateFormat.parse(rhs.getDate());
                return date2.compareTo(date1);
            } catch (ParseException e) {
                e.printStackTrace();
                return 0;
            }
        }
    }
}
