package com.hubli.imperium.imaperiumdiary.Messaging;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hubli.imperium.imaperiumdiary.Data.SPData;
import com.hubli.imperium.imaperiumdiary.Interface.IVolleyResponse;
import com.hubli.imperium.imaperiumdiary.R;
import com.hubli.imperium.imaperiumdiary.Utility.GenericMethods;
import com.hubli.imperium.imaperiumdiary.Utility.MyVolley;
import com.hubli.imperium.imaperiumdiary.Utility.URL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassMessaging extends AppCompatActivity {

    List<ChatData> items = new ArrayList<>();
    private MyAdaptor adaptor;
    private EditText messageText;
    private SPData spData;
    private MessagingDB db;
    private  ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_messaging);
        listView = (ListView) findViewById(R.id.chat_list);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        messageText = (EditText) findViewById(R.id.message_text);
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
            messageText.setBackgroundResource(R.drawable.round_corner);
        }
        spData = new SPData();
        setTitle(R.string.message);
        db = new MessagingDB(getApplicationContext());
        adaptor = new MyAdaptor();
        getItems(-1);
        listView.setAdapter(adaptor);
        listLoadMore();
    }

    private void listLoadMore() {
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView,int firstVisibleItem, int visibleItemCount, int totalItemCount) {
               // if(firstVisibleItem <=2 && items.size()>0) {
                    //getItems(items.get(0).getChatId());Log.e("new ","items");
                  //  adaptor.notifyDataSetChanged();
               // }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver,new IntentFilter("NEW_MESSAGE"));
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getItems(-1);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        try{
            unregisterReceiver(receiver);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void SendMessage(View v){
        final String s = messageText.getText().toString();
        if(s.length()>0) {
            Map<String, String> data = new HashMap<>();
            data.put(SPData.USER_NUMBER, spData.getUserData(SPData.USER_NUMBER));
            data.put(SPData.FIRST_NAME, "");
            data.put(SPData.LAST_NAME, "");
            data.put(SPData.PROPIC_URL, "");
            data.put("time", GenericMethods.getCurrentTimeString());
            data.put("message", s);
            data.put(SPData.PROPIC_URL, data.get(SPData.PROPIC_URL));
            db.insertNewMessage(data);
            getItems(-1);
            messageText.setText("");


            if (s.length() > 0) {
                new MyVolley(getApplicationContext(), new IVolleyResponse() {
                    @Override
                    public void volleyResponse(String result) {
                        if (result.contains("message_id")) {
                            int id = Integer.parseInt(result.split("~")[1]);
                            updateData(id, 1);
                        } else {
                            int id = Integer.parseInt(result.split("~")[1]);
                            updateData(id, 0);
                        }
                    }

                    @Override
                    public void volleyError() {

                    }
                }).setUrl(URL.MESSAGING).setRETRY_NUM(3)
                        .setParams(SPData.USER_NUMBER, spData.getUserData(SPData.USER_NUMBER))
                        .setParams("topic",spData.getUserData(SPData.CLASS_DIVISION_ID))
                        .setParams("message", s)
                        .setParams("id", (items.size() - 1) + "")
                        .connect();
            }
        }
    }

    private void updateData(int id,int status) {
        ChatData item = items.get(id);
        db.updateStatus(item.getChatId(),status);
        item.setStatus(status);
        adaptor.notifyDataSetChanged();
    }

    public void getItems(int id) {
        Cursor messages = db.getMessages(id);
        if(messages.getCount() > 0){
            messages.moveToLast();
            if(id == -1) {
                items.clear();
                do{
                    items.add(new ChatData(messages.getInt(0), messages.getString(1), messages.getString(2),
                            messages.getString(3), messages.getString(4), messages.getString(5), messages.getString(6),messages.getInt(7)));
                }while (messages.moveToPrevious());
                adaptor.notifyDataSetChanged();
            }else{
                do{
                    items.add(new ChatData(messages.getInt(0), messages.getString(1), messages.getString(2),
                            messages.getString(3), messages.getString(4), messages.getString(5), messages.getString(6),messages.getInt(7)));
                }while (messages.moveToPrevious());
                adaptor.notifyDataSetChanged();
            }
        }
    }


    private class MyAdaptor extends ArrayAdapter<ChatData>{
        public MyAdaptor() {
            super(getApplicationContext(), R.layout.chat_item,items);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View v = convertView;
            if(v==null){
                v = getLayoutInflater().inflate(R.layout.chat_item,parent,false);
            }
            ChatData currentItem = items.get(position);
            View sv = v.findViewById(R.id.card_send);
            View rv = v.findViewById(R.id.card_rec);
            if(currentItem.getFrom() == 0){
                rv.setVisibility(View.GONE);
                sv.setVisibility(View.VISIBLE);
                TextView sMessage = (TextView) v.findViewById(R.id.sMessage);
                TextView sTime = (TextView) v.findViewById(R.id.sTime);
                ImageView status = (ImageView) v.findViewById(R.id.status);

                if (currentItem.getStatus() != 1) {
                    status.setImageResource(R.drawable.ic_done);
                }else{
                    status.setImageResource(R.drawable.ic_done_all);
                }
                sMessage.setText(currentItem.getMessage());
                sTime.setText(GenericMethods.getTimeString(currentItem.getTime()));
            }else{
                sv.setVisibility(View.GONE);
                rv.setVisibility(View.VISIBLE);
                TextView rMessage = (TextView) v.findViewById(R.id.rMessage);
                TextView rTime = (TextView) v.findViewById(R.id.rTime);
                TextView rName = (TextView) v.findViewById(R.id.rName);

                rName.setText(currentItem.getFirstName()+" "+currentItem.getLastName());
                rMessage.setText(currentItem.getMessage());
                rTime.setText(GenericMethods.getTimeString(currentItem.getTime()));
            }
            return v;
        }
    }
}
