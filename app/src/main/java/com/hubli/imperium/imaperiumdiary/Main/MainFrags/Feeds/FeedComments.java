package com.hubli.imperium.imaperiumdiary.Main.MainFrags.Feeds;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.hubli.imperium.imaperiumdiary.Data.SPData;
import com.hubli.imperium.imaperiumdiary.R;

import java.util.ArrayList;

public class FeedComments extends AppCompatActivity {

    private ImageView post_img,propic;
    private TextView name,time,text,commentNoComment;
    private EditText commentText;
    private ProgressBar progressBar;
    private ExpandableHeightListView commentsList;
    private SPData spData;
    private ArrayList<PostCommentData> items = new ArrayList<>();

    public static String NAME = "name";
    public static String TIME = "time";
    public static String TEXT = "text";
    public static String PROPIC_URL = "propic_URL";
    public static String FEED_IMAGE_URL = "feed_image_url";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_comments);
        getSupportActionBar().setTitle("Comment");
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
        spData = new SPData(getApplicationContext());
        commentText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(ScrollView.FOCUS_UP);
                    }
                });
                return false;
            }

        });
        populateViews(getIntent().getExtras());

        post_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(activity.getApplicationContext(),PhotoViewActivity.class);
//                intent.putExtra(PhotoViewActivity.IMAGE_PATH,getIntent().getExtras().getString(PhotoViewActivity.IMAGE_PATH));
//                activity.startActivity(intent);
            }
        });

        commentsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                String myName = spData.getUserData(SPData.FIRST_NAME)+" "+spData.getUserData(SPData.LAST_NAME);
                if(myName.contentEquals(items.get(position).getName()) ||
                        postAnimData.getItem().getStudentNum() == Integer.parseInt(spData.getUserData(SPData.NUMBER_USER))) {
                    final CharSequence[] item = {"Delete"};
                    AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
                    dialog.setItems(item, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            connect(Utils.DELETE, position);
                        }
                    });
                    dialog.show();
                }
                return false;
            }
        });

    }

    private void populateViews(Bundle extras){
        propic.setImageDrawable(((ImageView) postAnimData.getPropic()).getDrawable());
        name.setText(extras.getString(NAME));
        time.setText(extras.getString(TIME));
        text.setText(extras.getString(TEXT));


        if(postAnimData.isImageAvailable()){
            post_img.setVisibility(View.VISIBLE);
            post_img.setImageBitmap(postAnimData.getPostImageBitmap());
        }else{
            post_img.setVisibility(View.GONE);
        }
        connect(Utils.COMMENTS,0);
    }
}
