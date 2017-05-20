package com.hubli.imperium.imaperiumdiary.Main.MainFrags.Questions;

import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.hubli.imperium.imaperiumdiary.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Faheem on 18-05-2017.
 */

public class MyQaAdaptor extends RecyclerView.Adapter<MyQaAdaptor.MyViewHolder> {

    List<QAData> items = new ArrayList<>();
    RecyclerView recyclerView;
    private static int mExpandedPosition = -1;

    MyQaAdaptor(List<QAData> items, RecyclerView recyclerView){
        this.items = items;
        this.recyclerView = recyclerView;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        QAData data = items.get(position);
        holder.hTitle.setText("Level "+data.getLevel()+" "+data.getType()+" Question");

        String firstletters = String.valueOf(data.getType().substring(0,1));
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getColor(data);
        TextDrawable drawable = TextDrawable.builder().buildRound(firstletters.toUpperCase(),color);
        holder.hImg.setImageDrawable(drawable);

        holder.bQuestionText.setText(data.getQuestionText());
        holder.optA.setText(data.getOpt1());
        holder.optB.setText(data.getOpt2());
        holder.optC.setText(data.getOpt3());
        holder.optD.setText(data.getOpt4());

        holder.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



        final boolean isExpanded = position==mExpandedPosition;
        holder.body.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.header.setVisibility(isExpanded?View.GONE:View.VISIBLE);
        holder.itemView.setActivated(isExpanded);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandedPosition = isExpanded ? -1:position;
                TransitionManager.beginDelayedTransition(recyclerView);
                holder.header.setVisibility(isExpanded?View.VISIBLE:View.GONE);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        View header, body;
        TextView hTitle, bQuestionText, bTimeout;
        ImageView hImg;
        Button submit;
        RadioGroup bOptions;
        RadioButton optA, optB, optC, optD;
        public MyViewHolder(View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.qHeader);
            hTitle = (TextView) itemView.findViewById(R.id.qhTitle);
            hImg = (ImageView) itemView.findViewById(R.id.qhImage);

            body = itemView.findViewById(R.id.qBody);
            bQuestionText = (TextView) itemView.findViewById(R.id.qbQuestionText);
            bTimeout = (TextView) itemView.findViewById(R.id.qbTimeOut);

            bOptions = (RadioGroup) itemView.findViewById(R.id.qbOptions);
            optA = (RadioButton) itemView.findViewById(R.id.opta);
            optB = (RadioButton) itemView.findViewById(R.id.optb);
            optC = (RadioButton) itemView.findViewById(R.id.optc);
            optD = (RadioButton) itemView.findViewById(R.id.optd);

            submit = (Button) itemView.findViewById(R.id.qbSubmit);

        }
    }
}
