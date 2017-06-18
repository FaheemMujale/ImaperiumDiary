package com.hubli.imperium.imaperiumdiary.Main.MainFrags.Feeds;

/**
 * Created by Faheem on 23-05-2017.
 */

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.List;

public class MyFeedsAdaptor extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private static ArrayList<FeedsData> itemList;

    private OnLoadMore onLoadMore;
    private LinearLayoutManager mLinearLayoutManager;
    private Context context;
    private Activity activity;
    private SPData spData;
    private boolean canClickLike = true;
    private boolean isMoreLoading = false;
    private int visibleThreshold = 1;
    private int firstVisibleItem, visibleItemCount, totalItemCount;

    interface OnLoadMore{
        void onLoadMore();
    }

    public MyFeedsAdaptor(OnLoadMore onLoadMore, Activity activity) {
        this.onLoadMore = onLoadMore;
        this.activity = activity;
        this.context = activity.getApplicationContext();
        spData = new SPData();
        itemList = new ArrayList<>();
    }

    public void setLinearLayoutManager(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    public void setRecyclerView(RecyclerView mView) {
        mView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = mLinearLayoutManager.getItemCount();
                firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
                if (!isMoreLoading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    if (onLoadMore != null) {
                        onLoadMore.onLoadMore();
                    }
                    isMoreLoading = true;
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return itemList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM) {
            return new StudentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.feeds_item, parent, false));
        } else {
            return new ProgressViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_item, parent, false));
        }

    }

    public void addAll(List<FeedsData> lst) {
        itemList.clear();
        itemList.addAll(lst);
        notifyDataSetChanged();
    }

    public void addItemMore(List<FeedsData> lst) {
        itemList.addAll(lst);
        notifyItemRangeChanged(0, itemList.size());
    }

    public void addItemAtTop(List<FeedsData> lst){
        itemList.addAll(0,lst);
        notifyItemRangeChanged(0,itemList.size());
    }

    public void deleteItem(int position){
        itemList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(0,itemList.size());
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof StudentViewHolder) {
            final FeedsData itemData =  itemList.get(position);
            final StudentViewHolder h = (StudentViewHolder) holder;
            boolean feedImageAvailable = false;

            if(itemData.getPropicLink() != null && itemData.getPropicLink().contains("jpeg")){
                Picasso.with(context).load(GenericMethods.getThumbNailURL(URL.PROPIC_BASE_URL+itemData.getPropicLink()))
                        .networkPolicy(ServerConnect.checkInternetConenction(activity) ?
                                NetworkPolicy.NO_CACHE : NetworkPolicy.OFFLINE)
                        .placeholder(R.drawable.defaultpropic)
                        .into(h.propic);
            }else{
                h.propic.setImageDrawable(activity.getResources().getDrawable(R.drawable.defaultpropic));
            }
            h.name.setText(itemData.getFirst_name() + " " + itemData.getLast_name());
            h.time.setText(GenericMethods.getTimeString(itemData.getTimeString()));
            h.text.setText(itemData.getText_message());
            h.postImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, PhotoViewer.class);
                    intent.putExtra(PhotoViewer.IMG_URL,itemData.getSrc_link());
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptionsCompat activityOptionsCompat;
                        Pair<View, String> pair = Pair.create( (View)h.postImage, h.postImage.getTransitionName());
                        activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, pair);
                        activity.startActivity(intent, activityOptionsCompat.toBundle());
                    }else{
                        activity.startActivity(intent);
                    }
                }
            });
            if (itemData.getSrc_link().length()>10) {
                h.postImage.setVisibility(View.VISIBLE);
                Picasso.with(context)
                        .load(itemData.getSrc_link())
                        .into(((StudentViewHolder) holder).postImage);
                feedImageAvailable = true;
            } else {
                feedImageAvailable = false;
                h.postImage.setVisibility(View.GONE);
            }

            h.like_num.setText(itemData.numLikes());

            if(itemData.isLiked()){
                h.like_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_liked));
            }else{
                h.like_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_not_liked));
            }

            h.like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(canClickLike){
                        canClickLike = false;
                        if(itemData.isLiked()){
                            new MyVolley(context, new IVolleyResponse() {
                                @Override
                                public void volleyResponse(String result) {
                                    canClickLike = true;
                                    if(result.contains("DONE")){
                                        itemData.setLiked(false,spData.getUserData(SPData.USER_NUMBER));
                                        h.like_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_not_liked));
                                        h.like_num.setText(itemData.numLikes());
                                    }else {
                                        Toast.makeText(context.getApplicationContext(),"Sorry some problem occurred..",Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void volleyError() {
                                    canClickLike = true;
                                }
                            })
                            .setUrl(URL.DELETE_FROM_FEEDS)
                            .setParams("feed_id",itemData.getId())
                            .setParams(SPData.USER_NUMBER,spData.getUserData(SPData.USER_NUMBER))
                            .connect();
                        }else{
                            new MyVolley(context, new IVolleyResponse() {
                                @Override
                                public void volleyResponse(String result) {
                                    canClickLike =true;
                                    if(result.contains("DONE")){
                                        itemData.setLiked(true,spData.getUserData(SPData.USER_NUMBER));
                                        h.like_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_liked));
                                        h.like_num.setText(itemData.numLikes());
                                    }else {
                                        Toast.makeText(context.getApplicationContext(),"Sorry some problem occurred..",Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void volleyError() {
                                    canClickLike =true;
                                }
                            })
                            .setUrl(URL.LIKE_COMMENT)
                            .setParams("feed_id",itemData.getId())
                            .setParams(SPData.USER_NUMBER,spData.getUserData(SPData.USER_NUMBER))
                            .connect();
                        }
                    }
                }
            });


            h.comment_num.setText(itemData.getCommentsNum()+"");

            final boolean finalFeedImageAvailable = feedImageAvailable;

            h.comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity,FeedComments.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(FeedComments.NAME, h.name.getText().toString());
                    bundle.putString(FeedComments.TIME, h.time.getText().toString());
                    bundle.putString(FeedComments.TEXT, h.text.getText().toString());
                    bundle.putString(FeedComments.PROPIC_URL, itemData.getPropicLink());
                    bundle.putBoolean(FeedComments.FEED_IMAGE_AVAILABLE, finalFeedImageAvailable);
                    bundle.putInt(FeedComments.USER_NUMBER,itemData.getUserNumber());
                    bundle.putString(FeedComments.FEED_ID,itemData.getId());


                    ActivityOptionsCompat activityOptionsCompat = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        Pair<View,String> p1 = Pair.create((View) h.name, h.name.getTransitionName());
                        Pair<View,String> p2 = Pair.create((View)h.time, h.time.getTransitionName());
                        Pair<View,String> p3 = Pair.create((View)h.text, h.text.getTransitionName());
                        Pair<View,String> p4 = Pair.create((View)h.propic, h.propic.getTransitionName());
                        if(finalFeedImageAvailable){
                            Pair<View,String> p5 = Pair.create((View)h.postImage, h.postImage.getTransitionName());
                            activityOptionsCompat = ActivityOptionsCompat
                                    .makeSceneTransitionAnimation(activity,p1,p2,p3,p4,p5);
                            bundle.putString(FeedComments.FEED_IMAGE_URL,itemData.getSrc_link());
                        }else{
                            activityOptionsCompat = ActivityOptionsCompat
                                    .makeSceneTransitionAnimation(activity,p1,p2,p3,p4);
                        }
                        intent.putExtras(bundle);
                        activity.startActivity(intent,activityOptionsCompat.toBundle());
                    }else{
                        intent.putExtras(bundle);
                        activity.startActivity(intent);
                    }
                }
            });

            if(itemData.getUserNumber() == Integer.parseInt(spData.getUserData(SPData.USER_NUMBER))
                    || (spData.getIdentification() != SPData.STUDENT)){

                h.deleteBtn.setVisibility(View.VISIBLE);
                h.deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //delete post

                        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(activity);
                        alert.setTitle("Alert");
                        alert.setMessage("Are you sure to delete");
                        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new MyVolley(context, new IVolleyResponse() {
                                    @Override
                                    public void volleyResponse(String result) {
                                        deleteItem(position);
                                    }

                                    @Override
                                    public void volleyError() {

                                    }
                                }).setUrl(URL.DELETE_FROM_FEEDS)
                                        .setParams("feed_id",itemData.getId())
                                        .connect();
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
                });
            }else{
                h.deleteBtn.setVisibility(View.GONE);
            }

        }
    }

    //    public static void updateCommentNum(TextView comment_num, int position, int a){
//        itemList.get(position).setCommentsNum(itemList.get(position).getCommentsNum() + a);
//        commentNum.setText(itemList.get(position).getCommentsNum()+"");
//    }

    public void setMoreLoading(boolean isMoreLoading) {
        this.isMoreLoading = isMoreLoading;
    }
    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setProgressMore(final boolean isProgress) {
        if (isProgress) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    itemList.add(null);
                    notifyItemInserted(itemList.size() - 1);
                }
            });
        } else {
            if(itemList.size() != 0){
                itemList.remove(itemList.size() - 1);
            }
            notifyItemRemoved(itemList.size());
        }
    }

    static class StudentViewHolder extends RecyclerView.ViewHolder {
        public TextView name, time, text, like_text, like_num, comment_num;
        public ImageView postImage, like_icon , propic;
        public LinearLayout like,comment,share;
        public CardView postCardView;
        public ImageButton deleteBtn;
        public View v;

        public StudentViewHolder(View v) {
            super(v);
            this.v = v;
            propic = (ImageView) v.findViewById(R.id.propic);
            postCardView = (CardView) v.findViewById(R.id.post_card_view);
            name = (TextView) v.findViewById(R.id.title);
            time = (TextView) v.findViewById(R.id.time_rcv);
            text = (TextView) v.findViewById(R.id.text);
            postImage = (ImageView) v.findViewById(R.id.postimage);

            like = (LinearLayout) v.findViewById(R.id.like);
            like_text = (TextView) v.findViewById(R.id.like_text);
            like_num = (TextView) v.findViewById(R.id.like_num);
            like_icon = (ImageView) v.findViewById(R.id.like_icon);

            comment = (LinearLayout) v.findViewById(R.id.comment);
            comment_num = (TextView) v.findViewById(R.id.comment_num);

//            share = (LinearLayout) v.findViewById(R.id.share);

            postCardView = (CardView) v.findViewById(R.id.post_card_view);

            deleteBtn = (ImageButton) v.findViewById(R.id.delete);

        }
    }

    static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar pBar;

        public ProgressViewHolder(View v) {
            super(v);
            pBar = (ProgressBar) v.findViewById(R.id.pBar);
        }
    }



//    public static BroadcastReceiver receiver = new BroadcastReceiver() {
//         @Override
//         public void onReceive(Context context, Intent intent) {
//             for (int i = 0; i<itemList.size();i++){
//                 if(itemList.get(i).getId().contentEquals(intent.getStringExtra(FeedComments.FEED_ID))){
//                     itemList.get(i).setCommentsNum(intent.getIntExtra(
//                             FeedComments.NUMBER_OF_COMMENTS,itemList.get(i).getCommentsNum())
//                     );
//                 }
//             }
//         }
//     };
}