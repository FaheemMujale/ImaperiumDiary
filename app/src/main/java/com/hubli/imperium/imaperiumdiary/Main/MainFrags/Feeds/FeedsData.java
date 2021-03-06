package com.hubli.imperium.imaperiumdiary.Main.MainFrags.Feeds;
import com.hubli.imperium.imaperiumdiary.Utility.URL;

import java.util.ArrayList;



/**
 * Created by Faheem on 23-05-2017.
 */

public class FeedsData {
    private String first_name,last_name,id,text_message,src_link,timeString,propicLink;
    boolean isLiked;
    private int commentsNum, userNumber;
    ArrayList<String> likedStudents = new ArrayList<>();

    public FeedsData(String studentNum,String propicLink, String first_name, String last_name, String id, String text_message, String src_link,
                     String timeString, boolean isLiked, ArrayList<String> likedStudents,String commentNum) {
        this.userNumber = Integer.parseInt(studentNum);
        this.propicLink = propicLink;
        this.first_name = first_name;
        this.last_name = last_name;
        this.id = id;
        this.text_message = text_message;
        this.src_link = src_link;
        this.timeString = timeString;
        this.isLiked = isLiked;
        this.likedStudents = likedStudents;
        this.commentsNum = Integer.parseInt(commentNum);
    }

    public String getPropicLink() {
        return propicLink;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getId() {
        return id;
    }

    public String getText_message() {
        return text_message;
    }

    public String getSrc_link() {
        return URL.FEED_IMAGE_BASE+src_link;
    }

    public String getTimeString() {
        return timeString;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public ArrayList<String> getLikedStudents() {
        return likedStudents;
    }

    public void setLiked(boolean liked,String studentNum) {
        if(liked){
            likedStudents.add(studentNum);
        }else {
            likedStudents.remove(studentNum);
        }
        isLiked = liked;
    }
    public String numLikes(){
        return likedStudents.size()+"";
    }

    public int getCommentsNum() {
        return commentsNum;
    }
    public void setCommentsNum(int num){
        this.commentsNum = num;
    }

    public int getUserNumber() {
        return userNumber;
    }
}



