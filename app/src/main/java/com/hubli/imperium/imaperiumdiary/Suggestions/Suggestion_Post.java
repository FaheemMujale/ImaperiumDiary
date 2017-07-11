package com.hubli.imperium.imaperiumdiary.Suggestions;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.hubli.imperium.imaperiumdiary.Data.SPData;
import com.hubli.imperium.imaperiumdiary.Interface.IVolleyResponse;
import com.hubli.imperium.imaperiumdiary.R;
import com.hubli.imperium.imaperiumdiary.Utility.MyVolley;
import com.hubli.imperium.imaperiumdiary.Utility.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Suggestion_Post extends DialogFragment {
    public static final String INTENTFILTER_SUGGESTION ="intent_filter_suggestion";
    public static final String SC_ID = "sc_id";
    public static final String SUGGESTER_FIRSTNAME = "first_name_suggestion";
    public static final String SUGGESTER_LASTNAME = "last_name_suggestion";
    public static final String SUGGESTER_CLASS = "class_suggestion";
    public static final String SUGGESTER_DIVISION = "division_suggestion";
    public static final String SUGGESTER_PROFILEPIC = "pic_suggestion";
    public static final String SUGGESTION_TITLE = "title_suggestion";
    public static final String SUGGESTION_CONTENT = "content_suggestion";
    public static final String SUGGESTION_DATE = "date_suggestion";
    private MyVolley myVolley;
    EditText edit_content;
    EditText edit_title;
    public Button sugg_post;
    SPData spData;

    View rootview;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootview = inflater.inflate(R.layout.suggestion_post, null);

        sugg_post=(Button)rootview.findViewById(R.id.button_post);
        edit_title=(EditText)rootview.findViewById(R.id.editText_title);
        edit_content=(EditText)rootview.findViewById(R.id.editText_content);
        spData=new SPData();
        getDialog().setTitle("New Suggestion");
        sugg_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "Processing.....", Toast.LENGTH_LONG).show();
                 InsertData();
            }
        });
        return rootview;
    }

    private void InsertData() {
        myVolley = new MyVolley(getActivity().getApplicationContext(), new IVolleyResponse() {
            @Override
            public void volleyResponse(String result) {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String date = jsonObject.getString("date").split(" ")[0];
                    if (!result.equals("null")){
                        parseDataSuggestion(jsonObject.getString("sc_id"),
                                spData.getUserData(SPData.FIRST_NAME),
                                spData.getUserData(SPData.LAST_NAME),
                                spData.getUserData(SPData.CLASS),
                                spData.getUserData(SPData.DIVISION),
                                spData.getUserData(SPData.PROPIC_URL),
                                edit_title.getText().toString(),
                                edit_content.getText().toString(), date);
                        Toast.makeText(getActivity().getApplicationContext(),"Done", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void volleyError() {

            }
        });
        myVolley.setUrl(URL.SUGGESTION_COMPLAIN);
        myVolley.setParams("title",edit_title.getText().toString());
        myVolley.setParams("content",edit_content.getText().toString());
        myVolley.setParams(SPData.USER_NUMBER,spData.getUserData(SPData.USER_NUMBER));
        myVolley.connect();
        getDialog().dismiss();
    }

    public void parseDataSuggestion(String sc_id, String firstName, String lastName, String class_user, String division_user, String user_pic, String s_title, String s_content, String s_date)
    {
        Intent intent = new Intent(INTENTFILTER_SUGGESTION);
        intent.putExtra(SC_ID,sc_id);
        intent.putExtra(SUGGESTER_FIRSTNAME, firstName);
        intent.putExtra(SUGGESTER_LASTNAME, lastName);
        intent.putExtra(SUGGESTER_CLASS, class_user);
        intent.putExtra(SUGGESTER_DIVISION, division_user);
        intent.putExtra(SUGGESTER_PROFILEPIC, user_pic);
        intent.putExtra(SUGGESTION_TITLE, s_title);
        intent.putExtra(SUGGESTION_CONTENT, s_content);
        intent.putExtra(SUGGESTION_DATE, s_date);
        getActivity().sendBroadcast(intent);

    }



}
