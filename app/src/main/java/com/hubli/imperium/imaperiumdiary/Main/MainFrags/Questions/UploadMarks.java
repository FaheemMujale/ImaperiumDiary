package com.hubli.imperium.imaperiumdiary.Main.MainFrags.Questions;

import android.content.Context;
import android.util.Log;

import com.hubli.imperium.imaperiumdiary.Data.SPData;
import com.hubli.imperium.imaperiumdiary.Interface.IVolleyResponse;
import com.hubli.imperium.imaperiumdiary.Utility.MyVolley;
import com.hubli.imperium.imaperiumdiary.Utility.URL;

/**
 * Created by Faheem on 21-05-2017.
 */

public class UploadMarks {

    private SPData spData;
    private Context context;

    public UploadMarks(Context activity){
        this.context = activity;
        spData = new SPData();
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
        new MyVolley(context, new IVolleyResponse() {
            @Override
            public void volleyResponse(String result) {
                if(result.contentEquals("1")){
                    spData.clearTempStoredMarkes();
                }
            }

            @Override
            public void volleyError() {

            }
        })
        .setUrl(URL.QA_MARKS)
        .setParams(SPData.USER_NUMBER,1+"") //change
        .setParams("type",type)
        .setParams("marks",marks)
        .connect();
    }
}
