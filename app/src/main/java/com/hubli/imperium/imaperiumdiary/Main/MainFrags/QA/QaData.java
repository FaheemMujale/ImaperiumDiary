package com.hubli.imperium.imaperiumdiary.Main.MainFrags.QA;

/**
 * Created by Faheem on 09-05-2017.
 */

public class QaData {

    private String user_number, qaImageUrl, name, questionText, qNum, numAnswers,time,profilePic_link;

    public QaData(String user_number, String qaImageUrl, String name, String questionText, String qNum, String numAnswers, String time, String profilePic_link) {
        this.user_number = user_number;
        this.qaImageUrl = qaImageUrl;
        this.name = name;
        this.questionText = questionText;
        this.qNum = qNum;
        this.numAnswers = numAnswers;
        this.time = time;
        this.profilePic_link = profilePic_link;
    }

    public String getUser_number() {
        return user_number;
    }

    public String getQaImageUrl() {
        return qaImageUrl;
    }

    public String getName() {
        return name;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getqNum() {
        return qNum;
    }

    public String getNumAnswers() {
        return numAnswers;
    }

    public String getTime() {
        return time;
    }

    public String getProfilePic_link() {
        return profilePic_link;
    }
}
