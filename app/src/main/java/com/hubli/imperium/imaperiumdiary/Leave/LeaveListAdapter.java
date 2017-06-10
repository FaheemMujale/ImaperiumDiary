package com.hubli.imperium.imaperiumdiary.Leave;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hubli.imperium.imaperiumdiary.Data.SPData;
import com.hubli.imperium.imaperiumdiary.R;
import com.hubli.imperium.imaperiumdiary.models.LeaveInfo;

import java.util.ArrayList;

/**
 * Created by Rafiq Ahmad on 6/6/2017.
 */

public class LeaveListAdapter extends RecyclerView.Adapter<LeaveListAdapter.FeedViewHolder> {
        private final LayoutInflater mLayoutInflater;
        private ArrayList<LeaveInfo> mItems = new ArrayList<>();
        private String usertype;
        SPData spData;
    Context mcontext;
        private String mUserEmail;


        public LeaveListAdapter(Context context) {
            mcontext=context;
            mLayoutInflater = LayoutInflater.from(context);
        }

        public void addItems(ArrayList<LeaveInfo> items) {//provide arraylist of Offers
            int count = mItems.size();
            mItems.addAll(items);
            notifyItemRangeChanged(count, items.size());
        }

        @Override
        public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View mView = mLayoutInflater.inflate(R.layout.single_leave_layout, parent, false);
            return new FeedViewHolder(mView);
        }

        @Override
        public void onBindViewHolder(final FeedViewHolder holder, final int position) {
            spData = new SPData();
            usertype = spData.getUserData("USERTYPE");
            holder.studname.setText(mItems.get(position).getStudent_name());
            holder.detail.setText(mItems.get(position).getDetail());
            holder.fromdate.setText(mItems.get(position).getFromdate());
            holder.todate.setText(mItems.get(position).getTodate());
            holder.status.setText(mItems.get(position).getStatus());
            if(usertype.contentEquals("student")){
                holder.accept.setVisibility(View.GONE);
                holder.reject.setVisibility(View.GONE);
            }
            holder.reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItems.get(position).setStatus("Rejected");
                    holder.status.setText("Rejected");
                }
            });


            holder.accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItems.get(position).setStatus("Approved");
                    holder.status.setText("Approved");
                }
            });
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        public static class FeedViewHolder extends RecyclerView.ViewHolder {
            TextView studname;
            TextView detail;
            TextView fromdate;
            TextView todate;
            TextView status;
            Button accept;
            Button reject;
            CardView mCv;

            public FeedViewHolder(View itemView) {
                super(itemView);
                mCv = (CardView) itemView.findViewById(R.id.cv_fil);
                studname = (TextView) itemView.findViewById(R.id.stud_name);
                detail = (TextView) itemView.findViewById(R.id.detail);
                fromdate = (TextView) itemView.findViewById(R.id.fromdate);
                todate = (TextView) itemView.findViewById(R.id.todate);
                status = (TextView) itemView.findViewById(R.id.status);
                accept = (Button) itemView.findViewById(R.id.accept);
                reject = (Button) itemView.findViewById(R.id.reject);
            }
        }
    }
