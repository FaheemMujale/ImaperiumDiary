package com.hubli.imperium.imaperiumdiary.Messaging;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.hubli.imperium.imaperiumdiary.Data.MySqlDB;
import com.hubli.imperium.imaperiumdiary.Data.SPData;

import java.util.Map;

/**
 * Created by Faheem on 17-06-2017.
 */

public class MessagingDB extends MySqlDB {
    public MessagingDB(Context context) {
        super(context);
    }

    public void insertNewMessage(Map<String, String> data){
        ContentValues contentValues = new ContentValues();
        contentValues.put(SPData.USER_NUMBER,data.get(SPData.USER_NUMBER));
        contentValues.put(SPData.FIRST_NAME,data.get(SPData.FIRST_NAME));
        contentValues.put(SPData.LAST_NAME,data.get(SPData.LAST_NAME));
        contentValues.put(SPData.PROPIC_URL,data.get(SPData.PROPIC_URL));
        contentValues.put(TIME,data.get(TIME));
        contentValues.put(MESSAGE,data.get(MESSAGE));
        contentValues.put(SPData.PROPIC_URL,data.get(SPData.PROPIC_URL));
        contentValues.put(STATUS,-1);
        db.insert(MESSAGING_TABLE,null,contentValues);
    }

    public  void updateStatus(int id,int status){
        ContentValues contentValues = new ContentValues();
        contentValues.put(STATUS,status);
        db.update(MESSAGING_TABLE,contentValues,ID +" = "+id,null);
    }
    public Cursor getMessages(int id){
        if(id == -1){
            return db.rawQuery("SELECT * FROM "+MESSAGING_TABLE+" ORDER BY ID DESC LIMIT 100",null);
        }else{
            return db.rawQuery("SELECT * FROM "+MESSAGING_TABLE+" WHERE ID < "+id+" ORDER BY ID DESC LIMIT 10",null);
        }
    }


}
