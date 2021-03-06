package com.hubli.imperium.imaperiumdiary.Main;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.amulyakhare.textdrawable.TextDrawable;
import com.hubli.imperium.imaperiumdiary.Data.SPData;
import com.hubli.imperium.imaperiumdiary.R;

/**
 * Created by Faheem on 07-05-2017.
 */

public class Badges extends RelativeLayout {

    private Context con;
    public Badges(Context context) {
        super(context);
        this.con = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.tab_icon,this,true);
    }

    public View getBadgeIcon(int icon){
        ((ImageView)findViewById(R.id.badge_icon)).setImageResource(icon);
        int number = getNumberFromSP(icon);
        if(number > 0){
            String num = String.valueOf(number);
            TextDrawable drawable = TextDrawable.builder().buildRound(num, Color.RED);
            ((ImageView)findViewById(R.id.badge_num)).setImageDrawable(drawable);
        }
        return getRootView();
    }


    public int getNumberFromSP(int icon) {
        return new SPData().getNotificationCount();
    }
}
