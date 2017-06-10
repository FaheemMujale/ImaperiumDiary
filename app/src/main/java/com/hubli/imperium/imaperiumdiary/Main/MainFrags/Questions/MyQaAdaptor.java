package com.hubli.imperium.imaperiumdiary.Main.MainFrags.Questions;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.hubli.imperium.imaperiumdiary.Data.SPData;
import com.hubli.imperium.imaperiumdiary.R;
import com.hubli.imperium.imaperiumdiary.Utility.ServerConnect;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Faheem on 18-05-2017.
 */

public class MyQaAdaptor extends RecyclerView.Adapter<MyQaAdaptor.MyViewHolder> {

    private List<QAData> items = new ArrayList<>();
    private RecyclerView recyclerView;
    private Activity activity;
    private Context context;
    private static int mExpandedPosition = -1;
    private Set<Integer> timerOn = new HashSet<>();
    private static CountDownTimer countDownTimer;

    MyQaAdaptor(RecyclerView recyclerView,Activity activity){
        this.recyclerView = recyclerView;
        this.activity = activity;
        this.context = activity.getApplicationContext();
    }

    public void setItems(List<QAData> items){
        this.items.clear();
        this.items = items;
        notifyDataSetChanged();
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final QAData data = items.get(position);
        holder.hTitle.setText(data.getTypeForDiaplay());
        String firstletters = String.valueOf(data.getTypeForDiaplay().charAt(0));
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getColor(data);
        TextDrawable drawable = TextDrawable.builder().buildRound(firstletters.toUpperCase(),color);
        holder.hImg.setImageDrawable(drawable);
        holder.level.setText("Level : "+data.getLevel());
        holder.bQuestionText.setText(data.getQuestionText());
        holder.optA.setText(" A. "+data.getOptA());
        holder.optB.setText(" B. "+data.getOptB());
        holder.optC.setText(" C. "+data.getOptC());
        holder.optD.setText(" D. "+data.getOptD());

        holder.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int option = holder.bOptions.getCheckedRadioButtonId();
                holder.optA.setBackgroundColor(context.getResources().getColor(R.color.cardview_light_background));
                holder.optB.setBackgroundColor(context.getResources().getColor(R.color.cardview_light_background));
                holder.optC.setBackgroundColor(context.getResources().getColor(R.color.cardview_light_background));
                holder.optD.setBackgroundColor(context.getResources().getColor(R.color.cardview_light_background));
                holder.submit.setVisibility(View.GONE);
                switch (option){
                    case -1:
                        Toast.makeText(context,"No Option Selected",Toast.LENGTH_SHORT).show();
                        holder.submit.setVisibility(View.VISIBLE);
                        break;
                    case R.id.opta:
                        answered("A",holder.optA);
                        break;
                    case R.id.optb:
                        answered("B",holder.optB);
                        break;
                    case R.id.optc:
                        answered("C",holder.optC);
                        break;
                    case R.id.optd:
                        answered("D",holder.optD);
                        break;
                }
            }

            private void answered(String a, RadioButton opt) {
                stopCountDownTimer();
                heilightCorrectAnswer(data.getAnswer(),holder);
                if(a.contentEquals(data.getAnswer())){
                    data.updateMarks(10);
                    uploadMarks(data.getType(),10);
                    holder.timer.setTextColor(context.getResources().getColor(R.color.green));
                    holder.timer.setText("Correct Answer you gain 10 marks");
                }else{
                    data.updateMarks(-5);
                    uploadMarks(data.getType(),-5);
                    opt.setBackgroundColor(context.getResources().getColor(R.color.red_highlight));
                    holder.timer.setTextColor(context.getResources().getColor(R.color.red));
                    holder.timer.setText("oops Wring Answer you lost 5 marks");
                }
                data.setAnswered(a);
            }

            private void uploadMarks(String type, int i) {
                if(ServerConnect.checkInternetConenction(activity)){
                    new UploadMarks(context).uploadMarksToServer(type,i+"");
                }else {
                    new SPData().tempStoreMarks(type,i+"");
                }
            }

        });

        final boolean isExpanded = position==mExpandedPosition;
        holder.body.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.itemView.setActivated(isExpanded);
        if(!timerOn.contains(position) && data.isWatched()){
            holder.timer.setText("You have already attempted this question");
            heilightCorrectAnswer(data.getAnswer(),holder);
            String a = data.getAnswered();
            if(a.contentEquals("A")){
                holder.optA.setBackgroundColor(context.getResources().getColor(R.color.red_highlight));
            }else if(a.contentEquals("B")){
                holder.optB.setBackgroundColor(context.getResources().getColor(R.color.red_highlight));
            }else if(a.contentEquals("C")){
                holder.optC.setBackgroundColor(context.getResources().getColor(R.color.red_highlight));
            }else if(a.contentEquals("D")){
                holder.optD.setBackgroundColor(context.getResources().getColor(R.color.red_highlight));
            }
            holder.optA.setEnabled(false);
            holder.optB.setEnabled(false);
            holder.optC.setEnabled(false);
            holder.optD.setEnabled(false);
            holder.submit.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandedPosition = isExpanded ? -1:position;
                TransitionManager.beginDelayedTransition(recyclerView);
                if(!data.isWatched()){
                    timerOn.add(position);
                    startCountDownTimer(holder.timer,position);
                }
                data.setWatched();
                notifyDataSetChanged();
            }


        });
    }

    private void startCountDownTimer(final TextView timer, final int position) {
        countDownTimer = new CountDownTimer(20*1000,1000){
            @Override
            public void onTick(long l) {
                int sec = (int) (l/1000);
                timer.setText(sec+" sec remaining");
                if(sec > 10){
                    timer.setTextColor(Color.GREEN);
                }else{
                    timer.setTextColor(Color.RED);
                }
            }

            @Override
            public void onFinish() {
                timerOn.remove(position);
                timer.setText("Time's up");
            }
        }.start();
    }

    private void stopCountDownTimer(){
        if(countDownTimer != null){
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }


    private void heilightCorrectAnswer(String answer, MyViewHolder holder) {

        if(answer.contentEquals("A")){ Log.e("ANSWER","A");
            holder.optA.setBackgroundColor(context.getResources().getColor(R.color.green_highlight));
        }else if(answer.contentEquals("B")){ Log.e("ANSWER","B");
            holder.optB.setBackgroundColor(context.getResources().getColor(R.color.green_highlight));
        }else if(answer.contentEquals("C")){ Log.e("ANSWER","C");
            holder.optC.setBackgroundColor(context.getResources().getColor(R.color.green_highlight));
        }else if(answer.contentEquals("D")){ Log.e("ANSWER","D");
            holder.optD.setBackgroundColor(context.getResources().getColor(R.color.green_highlight));
        }
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        View header, body;
        TextView hTitle, bQuestionText, level, timer;
        ImageView hImg;
        Button submit;
        RadioGroup bOptions;
        RadioButton optA, optB, optC, optD;
        public MyViewHolder(View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.qHeader);
            hTitle = (TextView) itemView.findViewById(R.id.qhTitle);
            hImg = (ImageView) itemView.findViewById(R.id.qhImage);
            level = (TextView) itemView.findViewById(R.id.level);
            timer = (TextView) itemView.findViewById(R.id.timer);

            body = itemView.findViewById(R.id.qBody);
            bQuestionText = (TextView) itemView.findViewById(R.id.qbQuestionText);

            bOptions = (RadioGroup) itemView.findViewById(R.id.qbOptions);
            optA = (RadioButton) itemView.findViewById(R.id.opta);
            optB = (RadioButton) itemView.findViewById(R.id.optb);
            optC = (RadioButton) itemView.findViewById(R.id.optc);
            optD = (RadioButton) itemView.findViewById(R.id.optd);

            submit = (Button) itemView.findViewById(R.id.qbSubmit);

        }
    }
}
