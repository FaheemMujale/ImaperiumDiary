package com.hubli.imperium.imaperiumdiary.Exams;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.hubli.imperium.imaperiumdiary.R;
import com.hubli.imperium.imaperiumdiary.Utility.ImperiumConstants;
import com.hubli.imperium.imaperiumdiary.models.Exam;

import java.util.ArrayList;

/**
 * Created by Sandeep on 23-Apr-16.
 */
public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.FeedViewHolder> {
    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private ArrayList<Exam> mItems = new ArrayList<>();
    private String imageURL;
    private String mUserEmail;


    public ExamAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void addItems(ArrayList<Exam> items) {//provide arraylist of Offers
        int count = mItems.size();
        mItems.addAll(items);
        notifyItemRangeChanged(count, items.size());
    }

    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = mLayoutInflater.inflate(R.layout.single_event_layout, parent, false);
        return new FeedViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final FeedViewHolder holder, final int position) {
        holder.title.setText(mItems.get(position).getExam_title());
        holder.date.setText(mItems.get(position).getDate());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Insert_Exam.class);
                intent.putExtra(ImperiumConstants.EXAM_DATA,mItems.get(position).getExam_data());
                intent.putExtra(ImperiumConstants.EXAM_TITLE,mItems.get(position).getExam_title());
                intent.putExtra(ImperiumConstants.EXAM_DIV,mItems.get(position).getExam_div());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class FeedViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView date;

        public FeedViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.exam_title);
            date = (TextView) itemView.findViewById(R.id.date);
        }
    }
}