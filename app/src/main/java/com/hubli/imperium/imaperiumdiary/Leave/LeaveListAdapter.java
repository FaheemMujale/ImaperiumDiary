package com.hubli.imperium.imaperiumdiary.Leave;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.hubli.imperium.imaperiumdiary.Data.SPData;
import com.hubli.imperium.imaperiumdiary.Interface.IVolleyResponse;
import com.hubli.imperium.imaperiumdiary.R;
import com.hubli.imperium.imaperiumdiary.Utility.MyVolley;
import com.hubli.imperium.imaperiumdiary.Utility.PhotoViewer;
import com.hubli.imperium.imaperiumdiary.Utility.URL;
import com.hubli.imperium.imaperiumdiary.models.LeaveInfo;

import java.util.ArrayList;

/**
 * Created by Rafiq Ahmad on 6/6/2017.
 */

public class LeaveListAdapter extends RecyclerView.Adapter<LeaveListAdapter.FeedViewHolder> {
        private final LayoutInflater mLayoutInflater;
        private ArrayList<LeaveInfo> mItems = new ArrayList<>();
    public static final String IMG_URL = "image_url";
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
            if(mItems.get(position).getFilelink()!=null){
                holder.attach.setVisibility(View.VISIBLE);
                holder.attach.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mItems.get(position).getFilelink().contains(".pdf")){
                            String link = URL.SERVER_URL + "/Leave/" + mItems.get(position).getFilelink();
                        String pdfLink = link.replace(" ", "%20");
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.parse(pdfLink), "*/*");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        try {
                            mcontext.startActivity(intent);
                        } catch (Exception e) {
                            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL.GOOGLE_DRIVE_VIEWER + pdfLink));
                            mcontext.startActivity(intent);
                        }
                    }else{
                            Intent intent = new Intent(mcontext, PhotoViewer.class);
                            String link = URL.SERVER_URL + "/Leave/" + mItems.get(position).getFilelink();
                            String pdfLink = link.replace(" ", "%20");
                            intent.putExtra(IMG_URL,pdfLink);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mcontext.startActivity(intent);
                        }
                    }
                });
            }
            holder.studname.setText(mItems.get(position).getStudent_name());
            holder.detail.setText(mItems.get(position).getDetail());
            holder.fromdate.setText(mItems.get(position).getFromdate());
            holder.todate.setText(mItems.get(position).getTodate());
            holder.status.setText(mItems.get(position).getStatus());
            if(mItems.get(position).getStatus().contentEquals("Approved") ||
                    mItems.get(position).getStatus().contentEquals("Rejected") || spData.getIdentification() != SPData.TEACHER)
            {
                holder.accept.setVisibility(View.GONE);
                holder.reject.setVisibility(View.GONE);
            }
            holder.reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItems.get(position).setStatus("Rejected");
                    holder.status.setText("Rejected");
                    updateleave(mItems.get(position).getStatus(),mItems.get(position).getId());
                    holder.accept.setVisibility(View.GONE);
                    holder.reject.setVisibility(View.GONE);
                }
            });


            holder.accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItems.get(position).setStatus("Approved");
                    holder.status.setText("Approved");
                    updateleave(mItems.get(position).getStatus(),mItems.get(position).getId());
                    holder.accept.setVisibility(View.GONE);
                    holder.reject.setVisibility(View.GONE);
                }
            });
        }

    private void updateleave(String status,int leave_id) {

        new MyVolley(mcontext, new IVolleyResponse() {
            @Override
            public void volleyResponse(String result) {
                Toast.makeText(mcontext, "Done", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void volleyError() {
            }
        }).setUrl(URL.LEAVE_LIST)
//                .setParams(SPData.USER_NUMBER,spdata.getUserData(SPData.USER_NUMBER))
                .setParams("leave_id", String.valueOf(leave_id))
                .setParams("status",status)
                .connect();
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
            ImageButton attach;

            public FeedViewHolder(View itemView) {
                super(itemView);
                mCv = (CardView) itemView.findViewById(R.id.cv_fil);
                attach = (ImageButton) itemView.findViewById(R.id.attachment);
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
