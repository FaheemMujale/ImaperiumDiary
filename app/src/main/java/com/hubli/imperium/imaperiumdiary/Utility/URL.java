package com.hubli.imperium.imaperiumdiary.Utility;

import android.content.Context;

import com.hubli.imperium.imaperiumdiary.Data.SPData;

/**
 * Created by Faheem on 09-05-2017.
 */

public class URL {

    private static SPData spData = new SPData();
    public static final String SERVER_URL = "http://imperiumapps.in/ImperiumDiary/"+spData.getInstitureID()+"/";
    public static final String FETCH_QUESTIONS = SERVER_URL+"Questions/fetch_questions.php";
    public static final String QA_MARKS = SERVER_URL+"Questions/qa_marks.php";
    public static final String FEEDS_DOWNLOAD = SERVER_URL+"Feeds/fetch_feeds.php";
    public static final String LIKE_COMMENT = SERVER_URL+"Feeds/like_comment.php";
    public static final String DELETE_FROM_FEEDS = SERVER_URL+"Feeds/delete.php";
    public static final String FETCH_COMMENTS = SERVER_URL+"Feeds/fetch_comments.php";
    public static final String LOGIN = SERVER_URL+"LoginAndRegister/login.php";
    public static final String EVENTS_FETCH = SERVER_URL+"Events/events_fetch.php";
    public static final String EVENTS_INSERT = SERVER_URL+"Events/events_insert.php";
    public static final String SUBJECTS = SERVER_URL+"Generic/subjects.php";





}
