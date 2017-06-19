package com.hubli.imperium.imaperiumdiary.Main.MainFrags.Notifications;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hubli.imperium.imaperiumdiary.R;
import com.hubli.imperium.imaperiumdiary.Utility.GenericMethods;

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


    private Object data;

    public FragTabNotifications() {
        // Required empty public constructor
    }
    private List<NotificationData> items = new ArrayList<NotificationData>();
    private MyListAdapter myListAdapter;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_frag_tab_notifications, container, false);
        ListView list = (ListView) view.findViewById(R.id.list);
        myListAdapter = new MyListAdapter();
        list.setAdapter(myListAdapter);
        getData();
        return view;
    }

    public void getData() {
        items.clear();
        items.add(new NotificationData("1","2017-06-17 12:24:20","","","New Time Table Update","Notice"));
        items.add(new NotificationData("2","2017-06-12 1:25:20","","","Exam Marks Uploaded For Maths","Progress Report"));
        items.add(new NotificationData("3","2017-06-9 4:52:20","","","College fest event added","Event"));
        items.add(new NotificationData("4","2017-06-8 2:32:20","","","Chapter 6 questions in science","HomeWork"));
        items.add(new NotificationData("5","2017-06-2 11:12:20","","","Holiday For curfew","Alert"));
        Collections.sort(items,new MyComparator());
        myListAdapter.notifyDataSetChanged();
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
