package com.hubli.imperium.imaperiumdiary.Main.MainFrags.QA;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.hubli.imperium.imaperiumdiary.Data.SPData;
import com.hubli.imperium.imaperiumdiary.R;
import com.hubli.imperium.imaperiumdiary.Utility.GenericMethods;
import com.hubli.imperium.imaperiumdiary.Utility.ServerConnect;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Faheem on 09-05-2017.
 */

public class MyAdaptor extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private ArrayList<QaData> itemList;
    private OnLoadMoreListener onLoadMoreListener;
    private LinearLayoutManager mLinearLayoutManager;
    private Activity activity;
    private SPData spData;

    private boolean isMoreLoading = false;
    private int visibleThreshold = 1;
    int firstVisibleItem, visibleItemCount, totalItemCount;

    public interface OnLoadMoreListener{
        void onLoadMore();
        void onAnswerClick(QaAnimData qaAnimData);
    }

    public MyAdaptor(OnLoadMoreListener onLoadMoreListener, Activity activity) {
        this.onLoadMoreListener=onLoadMoreListener;
        itemList = new ArrayList<>();
        this.activity = activity;
        spData = new SPData(activity.getApplicationContext());
    }

    public void setLinearLayoutManager(LinearLayoutManager linearLayoutManager){
        this.mLinearLayoutManager=linearLayoutManager;
    }

    public void setRecyclerView(RecyclerView mView){
        mView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = mLinearLayoutManager.getItemCount();
                firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
                if (!isMoreLoading && (totalItemCount - visibleItemCount)<= (firstVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
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
            return new StudentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.qa_item_view, parent, false));
        } else {
            return new ProgressViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_view, parent, false));
        }

    }

    public void addAll(List<QaData> lst){
        itemList.clear();
        itemList.addAll(lst);
        notifyDataSetChanged();
    }

    public void addItemMore(List<QaData> lst){
        itemList.addAll(lst);
        notifyItemRangeChanged(0,itemList.size());
    }
    public void addItemAtTop(List<QaData> lst){
        itemList.addAll(0,lst);
        notifyItemRangeChanged(0,itemList.size());
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof StudentViewHolder) {
            final QaData singleItem = itemList.get(position);
            final ImageView propic = (ImageView) ((StudentViewHolder) holder).v.findViewById(R.id.profile_image);
            if(GenericMethods.checkImageUrl(singleItem.getProfilePic_link())){
                Picasso.with(activity.getApplicationContext())
                        .load(GenericMethods.getThumbNailURL(singleItem.getProfilePic_link()))
                        .networkPolicy(ServerConnect.checkInternetConenction(activity) ?
                                NetworkPolicy.NO_CACHE : NetworkPolicy.OFFLINE)
                        .into(propic);
            }else{
                propic.setImageDrawable(activity.getResources().getDrawable(R.drawable.defaultpropic));
            }
            ((StudentViewHolder) holder).name.setText(singleItem.getName());
            ((StudentViewHolder) holder).time.setText(singleItem.getTime());
            ((StudentViewHolder) holder).question_text.setText(singleItem.getQuestionText());
            ((StudentViewHolder) holder).answerBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onLoadMoreListener.onAnswerClick(new QaAnimData((
                            (StudentViewHolder) holder).name
                            ,((StudentViewHolder) holder).time
                            ,((StudentViewHolder) holder).question_text
                            ,propic
                            ,singleItem.getqNum()));
                }
            });

            if(Integer.parseInt(itemList.get(position).getUser_number()) ==
                    Integer.parseInt(spData.getUserData(SPData.USER_NUMBER)) || !spData.isStudent()){
                ((StudentViewHolder) holder).answerDelete.setVisibility(View.VISIBLE);
            }else{
                ((StudentViewHolder) holder).answerDelete.setVisibility(View.GONE);
            }

            ((StudentViewHolder) holder).answerDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final CharSequence[] item = { "Delete"};
                    AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
                    dialog.setItems(item, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // connect
                        }
                    });
                    dialog.show();
                }
            });
        }
    }

    public void setMoreLoading(boolean isMoreLoading) {
        this.isMoreLoading=isMoreLoading;
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
            if(itemList.size()>0){
                itemList.remove(itemList.size() - 1);
                notifyItemRemoved(itemList.size());
            }
        }
    }

    static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView name, time, question_text;
        Button answerBtn;
        ImageButton answerDelete;
        View v;

        public StudentViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.title);
            time = (TextView) v.findViewById(R.id.time_rcv);
            question_text = (TextView) v.findViewById(R.id.text);
            answerBtn = (Button) v.findViewById(R.id.answer_btn);
            answerDelete = (ImageButton) v.findViewById(R.id.delete_answer);
            this.v = v;
        }
    }

    static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar pBar;
        public ProgressViewHolder(View v) {
            super(v);
            pBar = (ProgressBar) v.findViewById(R.id.pBar);
        }
    }
}
