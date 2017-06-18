package com.hubli.imperium.imaperiumdiary.Messaging;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_messaging);
        ListView listView  = (ListView) findViewById(R.id.chat_list);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        messageText = (EditText) findViewById(R.id.message_text);
        spData = new SPData();
        setTitle("Messaging");
        db = new MessagingDB(getApplicationContext());
        adaptor = new MyAdaptor();
        getItems();
        listView.setAdapter(adaptor);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver,new IntentFilter("NEW_MESSAGE"));
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getItems();
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
        if(s.length() > 0){
            new MyVolley(getApplicationContext(), new IVolleyResponse() {
                @Override
                public void volleyResponse(String result) {
                    if(result.contains("message_id")){
                        Map<String, String > data = new HashMap<>();
                        data.put(SPData.USER_NUMBER,spData.getUserData(SPData.USER_NUMBER));
                        data.put(SPData.FIRST_NAME,"");
                        data.put(SPData.LAST_NAME,"");
                        data.put(SPData.PROPIC_URL,"");
                        data.put("time",GenericMethods.getCurrentTimeString());
                        data.put("message",s);
                        data.put(SPData.PROPIC_URL,data.get(SPData.PROPIC_URL));
                        db.insertNewMessage(data);
                        getItems();
                    }
                    messageText.setText("");
                }

                @Override
                public void volleyError() {
                   // messageText.setText("");

                }
            }).setUrl(URL.MESSAGING)
                    .setParams(SPData.USER_NUMBER,spData.getUserData(SPData.USER_NUMBER))
                    .setParams("topic",spData.getInstituteID()+spData.getUserData(SPData.CLASS_DIVISION_ID))
                    .setParams("message",s)
                    .connect();
        }
    }
    public void getItems() {
        items.clear();
        Cursor messages = db.getMessages(-1);
        if(messages.getCount() > 0){
            while (messages.moveToNext()){
                items.add(new ChatData(messages.getInt(0),messages.getString(1),messages.getString(2),
                        messages.getString(3),messages.getString(4),messages.getString(5),messages.getString(6)));
            }
            adaptor.notifyDataSetChanged();
        }
    }


    private class MyAdaptor extends ArrayAdapter<ChatData>{
        public MyAdaptor() {
            super(getApplicationContext(), R.layout.chat_item_send,items);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View v = convertView;
            if(v==null){
                v = getLayoutInflater().inflate(R.layout.chat_item_send,parent,false);
            }
            ChatData currentItem = items.get(position);
            View sv = v.findViewById(R.id.card_send);
            View rv = v.findViewById(R.id.card_rec);
            if(currentItem.getFrom() == 0){
                rv.setVisibility(View.GONE);
                sv.setVisibility(View.VISIBLE);
                TextView sMessage = (TextView) v.findViewById(R.id.sMessage);
                TextView sTime = (TextView) v.findViewById(R.id.sTime);

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
