package com.hubli.imperium.imaperiumdiary.Main.MainFrags.Questions;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.hubli.imperium.imaperiumdiary.Data.SPData;
import com.hubli.imperium.imaperiumdiary.Interface.IVolleyResponse;
import com.hubli.imperium.imaperiumdiary.Utility.MyVolley;
import com.hubli.imperium.imaperiumdiary.Utility.ServerConnect;
import com.hubli.imperium.imaperiumdiary.Utility.URL;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Faheem on 21-05-2017.
 */

public class UploadMarks {

    private SPData spData;
    private Context context;

    public UploadMarks(Context activity){
        this.context = activity;
        spData = new SPData(activity.getApplicationContext());
    }

    public void uploadTempMarks(){
        String data = spData.getTempStoreMarks();
        Log.e("data",data);
        if(data.length() > 0 ){
            String type = "";
            String marks = "";
            String[] entry = data.split(",");
            for (String e:entry) {
                String[] d = e.split("~");
                type += d[0]+"~";
                marks += d[1]+"~";
            }
            uploadMarksToServer(type,marks);
        }
    }

    public void uploadMarksToServer(String type,String marks){
        MyVolley volley = new MyVolley(context, new IVolleyResponse() {
            @Override
            public void volleyResponse(String result) {
                if(result.contentEquals("1")){
                    spData.clearTempStoredMarkes();
                }
            }

            @Override
            public void volleyError() {

            }
        });
        volley.setUrl(URL.QA_MARKS);
        volley.setParams(SPData.USER_NUMBER,1+""); //change
        volley.setParams("type",type);
        volley.setParams("marks",marks);
        volley.connect();
    }
}
