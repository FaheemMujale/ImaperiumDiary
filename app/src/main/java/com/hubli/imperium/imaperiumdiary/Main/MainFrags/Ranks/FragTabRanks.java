package com.hubli.imperium.imaperiumdiary.Main.MainFrags.Ranks;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hubli.imperium.imaperiumdiary.Data.SPData;
import com.hubli.imperium.imaperiumdiary.Interface.IVolleyResponse;
import com.hubli.imperium.imaperiumdiary.R;
import com.hubli.imperium.imaperiumdiary.Utility.GenericMethods;
import com.hubli.imperium.imaperiumdiary.Utility.MyVolley;
import com.hubli.imperium.imaperiumdiary.Utility.URL;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragTabRanks extends Fragment {


    public FragTabRanks() {
        // Required empty public constructor
    }

    private View rootView;
    private SPData spData;
    private MyAdaptor adaptor;
    private List<RanksData> items = new ArrayList<>();
    private ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_frag_tab_ranks, container, false);
        spData = new SPData();
        ListView list = (ListView) rootView.findViewById(R.id.rankList);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        adaptor = new MyAdaptor();
        list.setAdapter(adaptor);
        String ranksData = spData.getRanksData();
        if(ranksData == null){
            getDataFromServer();
        }else{
            Calendar cal = Calendar.getInstance();
            int day = cal.get(Calendar.DAY_OF_YEAR);
            if((day % 7) != 1 && day != Integer.parseInt(ranksData.split("~")[1])){
                getDataFromServer();
            }else{
                parseJson(spData.getRanksData());
            }
        }
        return rootView;
    }

    private void parseJson(String ranksData) {
        try {
            items.clear();
            JSONArray jsonArray = new JSONArray(ranksData);
            for(int i=0;i<jsonArray.length();i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                items.add(new RanksData(i+1,json.getString(SPData.USER_NUMBER),json.getString(SPData.FIRST_NAME),
                        json.getString(SPData.LAST_NAME),json.getString(SPData.CLASS),json.getString(SPData.DIVISION),
                        json.getString(SPData.PROPIC_URL),json.getString("total")));
            }
            adaptor.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getDataFromServer() {
        progressBar.setVisibility(View.VISIBLE);
        new MyVolley(getActivity().getApplicationContext(), new IVolleyResponse() {
            @Override
            public void volleyResponse(String result) {
                spData.setRanksData(result);
                parseJson(result);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void volleyError() {
                    //no data
                progressBar.setVisibility(View.GONE);
            }
        }).setUrl(URL.FETCH_RANKS).connect();
    }

    private class MyAdaptor extends ArrayAdapter<RanksData>{

        public MyAdaptor() {
            super(getActivity().getApplicationContext(), R.layout.ranks_item,items);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View v = convertView;
            if(v == null) {
                v = getActivity().getLayoutInflater().inflate(R.layout.ranks_item, parent, false);
            }

            TextView name = (TextView) v.findViewById(R.id.rName);
            TextView class_division = (TextView) v.findViewById(R.id.class_division);
            TextView rank = (TextView) v.findViewById(R.id.rank);
            TextView points = (TextView) v.findViewById(R.id.points);
            ImageView propic = (ImageView) v.findViewById(R.id.propic);

            RanksData data = items.get(position);
            name.setText(data.getName());
            class_division.setText("Class: "+data.getvClass()+" "+data.getDivision());
            rank.setText(data.getRank()+"");
            points.setText(data.getTotal());
            Log.e("link",URL.SERVER_URL+GenericMethods.getThumbNailURL(data.getPropic_link()));
            Picasso.with(getActivity().getApplicationContext())
                    .load(URL.PROPIC_BASE_URL+GenericMethods.getThumbNailURL(data.getPropic_link()))
                    .placeholder(R.drawable.defaultpropic)
                    .into(propic);
            return v;

        }
    }
}
