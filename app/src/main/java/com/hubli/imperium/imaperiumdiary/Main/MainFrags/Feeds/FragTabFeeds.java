package com.hubli.imperium.imaperiumdiary.Main.MainFrags.Feeds;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hubli.imperium.imaperiumdiary.Data.SPData;
import com.hubli.imperium.imaperiumdiary.Interface.IVolleyResponse;
import com.hubli.imperium.imaperiumdiary.R;
import com.hubli.imperium.imaperiumdiary.Utility.MyVolley;
import com.hubli.imperium.imaperiumdiary.Utility.ServerConnect;
import com.hubli.imperium.imaperiumdiary.Utility.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragTabFeeds extends Fragment implements MyFeedsAdaptor.OnLoadMore, SwipeRefreshLayout.OnRefreshListener{


    public FragTabFeeds() {
        // Required empty public constructor
    }

    private View view;
    private ArrayList<FeedsData> itemlist = new ArrayList<FeedsData>();;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MyFeedsAdaptor postAdaptor;
    private SPData spData;
    private JSONArray jsonPost,jsonLikes;
    boolean backPressed = false;
    boolean noMorePost = false;
    private ProgressBar progressBar;
    private TextView noFeeds;
    private String POST_MIN = "0";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_frag_tab_feeds, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        noFeeds = (TextView) view.findViewById(R.id.no_feeds);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rvList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        postAdaptor = new MyFeedsAdaptor(this, getActivity());
        postAdaptor.setLinearLayoutManager(linearLayoutManager);
        postAdaptor.setRecyclerView(recyclerView);
        recyclerView.setAdapter(postAdaptor);
        recyclerView.getItemAnimator().setChangeDuration(0);
        spData = new SPData();
        swipeRefreshLayout.setColorSchemeResources(
                R.color.colorAccent,
                R.color.colorPrimary,
                R.color.colorPrimaryDark);
        swipeRefreshLayout.setOnRefreshListener(this);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(receiver,new IntentFilter("NEW_FEED"));
        itemlist.clear();
        noMorePost = false;
        if(ServerConnect.checkInternetConenction(getActivity())&& !backPressed){
            downloadData(getActivity());
        }else{
            if(spData.getFeedsData()!=""){
                swipeRefreshLayout.setRefreshing(false);
                postAdaptor.addAll(getJsonData(spData.getFeedsData()));
            }
        }
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String data = intent.getStringExtra("data");
            itemlist.clear();

            try {
                JSONArray jsonArray = new JSONArray(data);
                JSONObject jsonObject = jsonArray.getJSONObject(0);

                itemlist.add(new FeedsData(spData.getUserData(SPData.USER_NUMBER)
                        ,spData.getUserData(SPData.PROPIC_URL)
                        ,spData.getUserData(SPData.FIRST_NAME)
                        ,spData.getUserData(SPData.LAST_NAME)
                        , jsonObject.getString("feed_id")
                        ,jsonObject.getString("feed_text")
                        ,jsonObject.getString("image_src_link")
                        ,jsonObject.getString("date")
                        ,false
                        ,new ArrayList<String>()
                        ,"0"));
                
                postAdaptor.addItemAtTop(itemlist);
                prefixItemData( jsonObject.getString("feed_id"),
                        jsonObject.getString("feed_text"),
                        jsonObject.getString("image_src_link"),
                        jsonObject.getString("date"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            
        }
    };

    private void prefixItemData(String postid, String text, String link, String dateString) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(SPData.USER_NUMBER,spData.getUserData(SPData.USER_NUMBER));
            jsonObject.put(SPData.FIRST_NAME,spData.getUserData(SPData.FIRST_NAME));
            jsonObject.put(SPData.LAST_NAME,spData.getUserData(SPData.LAST_NAME));
            jsonObject.put("feed_id",postid);
            jsonObject.put("feed_text",text);
            jsonObject.put("image_src_link",link);
            jsonObject.put("date",dateString);
            jsonObject.put("like",JSONObject.NULL);
            jsonObject.put("total_comments",0);

            JSONArray jsonArray = new JSONArray();
            jsonArray.put(jsonObject);

            spData.prefixToFeedsData(jsonArray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        try{
            getActivity().unregisterReceiver(receiver);
        }catch (Exception e){

        }
        backPressed = true;
    }

    @Override
    public void onLoadMore() {
        if(ServerConnect.checkInternetConenction(getActivity()) && !noMorePost){
            postDataDownload(POST_MIN);
            postAdaptor.setProgressMore(true);
        }
    }

    @Override
    public void onRefresh() {
        POST_MIN = "0";
        noMorePost = false;
        postAdaptor.setMoreLoading(false);
        downloadData(getActivity());
    }

    public void downloadData(Activity activity) {
        if(ServerConnect.checkInternetConenction(activity)){
            progressBar.setVisibility(View.VISIBLE);
            POST_MIN = "0";
            postDataDownload(POST_MIN);
        }
    }

    private void postDataDownload(String post_min) {
        noFeeds.setVisibility(View.GONE);
        new MyVolley(getActivity().getApplicationContext(), new IVolleyResponse() {
            @Override
            public void volleyResponse(String volleyResponse) {
                try {
                    storeData(volleyResponse);
                } catch (JSONException e) {
                    noFeeds.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                }
                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void volleyError() {
                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                postAdaptor.setProgressMore(false);
                if(postAdaptor.getItemCount()==0) {
                    noFeeds.setVisibility(View.VISIBLE);
                }

            }
        }).setUrl(URL.FEEDS_DOWNLOAD)
                .setParams("min",post_min)
                .connect();
    }

    private void storeData(String s) throws JSONException {
        Log.e("dara",s);
        if(s.contains(SPData.USER_NUMBER)) {
            itemlist.clear();
            if (POST_MIN == "0") {
                swipeRefreshLayout.setRefreshing(false);
                postAdaptor.addAll(getJsonData(s));
                spData.storeFeedsData(s);
            } else {
                postAdaptor.setProgressMore(false);
                postAdaptor.addItemMore(getJsonData(s));
                postAdaptor.setMoreLoading(false);
                spData.appendToFeedsData(s);
            }

        }else{
            noMorePost = true;
            postAdaptor.setProgressMore(false);
            postAdaptor.setMoreLoading(false);
        }
    }


    private ArrayList<FeedsData> getJsonData(String jsonString){

        JSONObject jsonPostObj = null;
        JSONObject jsonLikeObj = null;
        ArrayList<String> likedStudents = new ArrayList<>();
        try {
            jsonPost = new JSONArray(jsonString);
            for (int i = 0; i <= jsonPost.length() - 1; i++) {
                boolean isLiked = false;
                jsonPostObj = jsonPost.getJSONObject(i);
                //get likes list
                likedStudents = new ArrayList<>();
                if(jsonPostObj.getString("like") != "null"){
                    jsonLikes = new JSONArray(jsonPostObj.getString("like"));
                    for(int j = 0 ; j < jsonLikes.length();j++){
                        jsonLikeObj = jsonLikes.getJSONObject(j);
                        String sNo = jsonLikeObj.getString(SPData.USER_NUMBER);
                        if(Integer.parseInt(sNo) == Integer.parseInt(spData.getUserData(SPData.USER_NUMBER)))
                        {
                            isLiked = true;
                        }
                        likedStudents.add(sNo);
                    }
                }

                itemlist.add(new FeedsData(jsonPostObj.getString(SPData.USER_NUMBER),jsonPostObj.getString(SPData.PROPIC_URL)
                        ,jsonPostObj.getString(SPData.FIRST_NAME), jsonPostObj.getString(SPData.LAST_NAME),
                        jsonPostObj.getString("feed_id"), jsonPostObj.getString("feed_text"),
                        jsonPostObj.getString("image_src_link"), jsonPostObj.getString("date"),isLiked,likedStudents,
                        jsonPostObj.getString("total_comments")));

                if (i == jsonPost.length() - 1) {
                    POST_MIN = jsonPostObj.getString("feed_id");
                }
            }
            return itemlist;
        } catch (JSONException e) {
            e.printStackTrace();
            itemlist.clear();
            return itemlist;
        }
    }

}
