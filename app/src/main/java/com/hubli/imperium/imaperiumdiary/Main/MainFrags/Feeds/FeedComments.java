package com.hubli.imperium.imaperiumdiary.Main.MainFrags.Feeds;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.hubli.imperium.imaperiumdiary.Data.SPData;
import com.hubli.imperium.imaperiumdiary.Interface.IVolleyResponse;
import com.hubli.imperium.imaperiumdiary.R;
import com.hubli.imperium.imaperiumdiary.Utility.GenericMethods;
import com.hubli.imperium.imaperiumdiary.Utility.MyVolley;
import com.hubli.imperium.imaperiumdiary.Utility.PhotoViewer;
import com.hubli.imperium.imaperiumdiary.Utility.ServerConnect;
import com.hubli.imperium.imaperiumdiary.Utility.URL;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FeedComments extends AppCompatActivity {

    private ImageView post_img,propic;
    private TextView name,time,text,commentNoComment;
    private EditText commentText;
    private ProgressBar progressBar;
    private ExpandableHeightListView commentsList;
    private SPData spData;
    private ArrayList<PostCommentData> items = new ArrayList<>();
    private MyAdaptor adaptor;
    private Bundle extras;
    private boolean canLike = true;

    public static String NAME = "name";
    public static String TIME = "time";
    public static String TEXT = "text";
    public static String PROPIC_URL = "propic_URL";
    public static String FEED_IMAGE_URL = "feed_image_url";
    public static String USER_NUMBER = "user_number";
    public static String FEED_IMAGE_AVAILABLE = "feed_image_available";
    public static String FEED_ID = "feed_id";

    public static String COMMENT_CHANGE_BRODCAST = "COMMENT_CHANGE_BRODCAST";
    public static String NUMBER_OF_COMMENTS = "NUMBER_OF_COMMENTS";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_comments);
//        getSupportActionBar().setTitle("Comment");
        propic = (ImageView) findViewById(R.id.propic);
        post_img = (ImageView) findViewById(R.id.postimage);
        name = (TextView) findViewById(R.id.title);
        time = (TextView) findViewById(R.id.time_rcv);
        text = (TextView) findViewById(R.id.text);
        commentsList = (ExpandableHeightListView) findViewById(R.id.comment_list);
        commentText = (EditText) findViewById(R.id.comment_text);
        final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView2);
        commentNoComment = (TextView) findViewById(R.id.comment_nocomment);
        progressBar = (ProgressBar) findViewById(R.id.comment_progress);
        spData = new SPData();
        commentText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }

        });
        extras = getIntent().getExtras();

        populateViews();

        post_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PhotoViewer.class);
                intent.putExtra(PhotoViewer.IMG_URL,extras.getString(FEED_IMAGE_URL));
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptionsCompat activityOptionsCompat;
                    Pair<View, String> pair = Pair.create((View) post_img, post_img.getTransitionName());
                    activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), pair);
                    startActivity(intent, activityOptionsCompat.toBundle());
                }else{
                    startActivity(intent);
                }
            }
        });

        commentsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                String myName = spData.getUserData(SPData.FIRST_NAME)+" "+spData.getUserData(SPData.LAST_NAME);
                if(myName.contentEquals(items.get(position).getName()) ||
                        extras.getInt(USER_NUMBER) == Integer.parseInt(spData.getUserData(SPData.USER_NUMBER))) {
                    android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(getActivity());
                    alert.setTitle("Alert");
                    alert.setMessage("Are you sure to delete");
                    alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            connect(URL.DELETE_FROM_FEEDS, position);
                        }
                    });
                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alert.show();
                }
                return false;
            }
        });

    }

    private void populateViews(){
        Picasso.with(getApplicationContext())
                .load(extras.getString(PROPIC_URL))
                .error(R.drawable.defaultpropic)
                .into(propic);
        name.setText(extras.getString(NAME));
        time.setText(extras.getString(TIME));
        text.setText(extras.getString(TEXT));

        if(extras.getBoolean(FEED_IMAGE_AVAILABLE)){
            post_img.setVisibility(View.VISIBLE);
            Picasso.with(getApplicationContext())
                    .load(extras.getString(FEED_IMAGE_URL))
                    .into(post_img);
        }else{
            post_img.setVisibility(View.GONE);
        }
        connect(URL.FETCH_COMMENTS,0);
    }


    private void connect(final String url, final int position){
        MyVolley volley = new MyVolley(getApplicationContext(), new IVolleyResponse() {
            @Override
            public void volleyResponse(String result) {
                    if(url == URL.FETCH_COMMENTS){
                        items.clear();
                        try {
                            JSONArray jsonArray = new JSONArray(result);
                            for(int i = 0 ;i<jsonArray.length();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                items.add(new PostCommentData(jsonObject.getString("student_name"),
                                        jsonObject.getString(spData.PROPIC_URL),
                                        GenericMethods.getTimeString(jsonObject.getString("date")),
                                        jsonObject.getString("comment_text"),jsonObject.getString("like"),
                                        jsonObject.getString("comment_id"),jsonObject.getBoolean("user_liked")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
              //              commentNoComment.setText("No comments");
                        }
                        adaptor = new MyAdaptor();
                        commentsList.setAdapter(adaptor);
                        commentsList.setExpanded(true);
                    }else if(url == URL.DELETE_FROM_FEEDS){
                        if(result.contains("DONE")){
                            items.remove(position);
                            adaptor.notifyDataSetChanged();
                            //commentChanged();
                        }
                    }
                updateCommentStatus();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void volleyError() {
           //     commentNoComment.setText("No comments");
                progressBar.setVisibility(View.GONE);
                updateCommentStatus();
            }

            private void updateCommentStatus() {
                if(items.size()>0){
                    commentNoComment.setText("Comments");
                }else{
                    commentNoComment.setText("No comments");
                }
            }
        });
        volley.setUrl(url);
        if(url == URL.FETCH_COMMENTS){
            volley.setParams("feed_id", extras.getString(FEED_ID));
            volley.setParams(SPData.USER_NUMBER, spData.getUserData(spData.USER_NUMBER));
        } else if(url == URL.DELETE_FROM_FEEDS){
            volley.setParams("comment_id", items.get(position).getComment_id());
        }
        volley.connect();
    }


    class MyAdaptor extends ArrayAdapter<PostCommentData> {

        public MyAdaptor() {
            super(getApplicationContext(), R.layout.feeds_item,items);
        }
        @NonNull
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if(itemView==null){
                itemView = getLayoutInflater().inflate(R.layout.feed_comment_item,parent,false);
            }
            ImageView propic = (ImageView) itemView.findViewById(R.id.propic);
            TextView comment_name = (TextView) itemView.findViewById(R.id.title);
            TextView time = (TextView) itemView.findViewById(R.id.time_rcv);
            TextView text = (TextView) itemView.findViewById(R.id.text);
            final TextView likes = (TextView) itemView.findViewById(R.id.comment_likes_num);
            final LinearLayout likeView = (LinearLayout) itemView.findViewById(R.id.comments_likes);
            final TextView likeBtn = (TextView) itemView.findViewById(R.id.like);

            final PostCommentData currentItem = items.get(position);

            if(currentItem.getProfilePic_link() != null && currentItem.getProfilePic_link().contains("jpeg")){
                Picasso.with(getActivity().getApplicationContext()).load("propic url")
                        .networkPolicy(ServerConnect.checkInternetConenction(getActivity()) ?
                                NetworkPolicy.NO_CACHE : NetworkPolicy.OFFLINE)
                        .into(propic);
            }else{
                propic.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.defaultpropic));
            }
            comment_name.setText(currentItem.getName());
            time.setText(currentItem.getTime());
            text.setText(currentItem.getText());
            likes.setText(currentItem.getLikes()+"");
            if(!currentItem.hasUser_liked()){
                likeBtn.setText("Like");
                likeBtn.setPadding(10,5,15,0);
            }else {
                likeBtn.setText("Unlike");
                likeBtn.setPadding(10,5,8,0);
            }

            setLikeView(currentItem.getLikes(),likes,likeView);

            likeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(!currentItem.hasUser_liked()) {
                        connectServer(URL.LIKE_COMMENT);
                    }else{
                        connectServer(URL.DELETE_FROM_FEEDS);
                        Log.e("del","del");
                    }
                }

                private void connectServer(final String url) {
                    if (canLike) {
                        canLike = false;
                        new MyVolley(getActivity().getApplicationContext(), new IVolleyResponse() {
                            @Override
                            public void volleyResponse(String result) {
                                canLike = true;
                                if (result != null && result.contains("DONE")) {
                                    if (url == URL.LIKE_COMMENT) {
                                        likeBtn.setText("Unlike");
                                        likeBtn.setPadding(10,5,8,0);
                                        currentItem.setUser_liked(true);
                                        currentItem.setLikes(currentItem.getLikes() + 1);
                                    } else if (url == URL.DELETE_FROM_FEEDS) {
                                        likeBtn.setText("Like");
                                        likeBtn.setPadding(10,5,15,0);
                                        currentItem.setUser_liked(false);
                                        currentItem.setLikes(currentItem.getLikes() - 1);
                                    }
                                }
                                setLikeView(currentItem.getLikes(), likes, likeView);
                            }

                            @Override
                            public void volleyError() {
                                canLike = true;
                            }
                        }).setUrl(url)
                                .setParams("comment_id", currentItem.getComment_id())
                                .setParams(SPData.USER_NUMBER, spData.getUserData(SPData.USER_NUMBER))
                                .connect();
                    }
                }
            });
            return itemView;
        }
        private void setLikeView(int likeNum,TextView likes,LinearLayout likeView){
            if(likeNum > 0){
                likes.setText(likeNum+"");
                likeView.setVisibility(View.VISIBLE);
            }else{
                likeView.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void commentBtn(View v){
        if(commentText.getText().length()>0){
            new MyVolley(getApplicationContext(), new IVolleyResponse() {
                @Override
                public void volleyResponse(String result) {
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(result);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        items.add(new PostCommentData(spData.getUserData(SPData.FIRST_NAME)+" "+
                                spData.getUserData(SPData.FIRST_NAME),spData.getUserData(SPData.PROPIC_URL),
                                jsonObject.getString("date"),jsonObject.getString("comment_text"),"null",
                                jsonObject.getString("comment_id"),false));
                        adaptor.notifyDataSetChanged();
                        //commentChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    commentText.setText("");
                    Snackbar.make(findViewById(android.R.id.content),"Long press to delete your comment",Snackbar.LENGTH_LONG)
                            .show();
                    commentNoComment.setText("Comments");
                }

                @Override
                public void volleyError() {

                }
            }).setUrl(URL.LIKE_COMMENT)
                    .setParams(SPData.USER_NUMBER,spData.getUserData(SPData.USER_NUMBER))
                    .setParams("feed_id",extras.getString(FEED_ID))
                    .setParams("comment_text",commentText.getText().toString())
                    .connect();
        }else{
            Toast.makeText(getActivity().getApplicationContext(),"No Comment text...",Toast.LENGTH_SHORT).show();
        }
    }

//    private void commentChanged(){
//        Intent intent = new Intent(COMMENT_CHANGE_BRODCAST);
//        intent.putExtra(NUMBER_OF_COMMENTS,items.size());
//        intent.putExtra(FEED_ID,extras.getString(FEED_ID));
//        getActivity().sendBroadcast(intent);
//    }
    private Activity getActivity(){return  this;}
}
