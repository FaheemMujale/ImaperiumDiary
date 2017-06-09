package com.hubli.imperium.imaperiumdiary.Utility;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.hubli.imperium.imaperiumdiary.Data.SPData;

/**
 * Created by Faheem on 09-05-2017.
 */

public class URL {

    private static SPData spData = new SPData();

    public static final FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
    public static final String URL_CONFIG_NAME = "SERVER_BASE_URL";

    public static final String SERVER_URL = remoteConfig.getString(URL_CONFIG_NAME)+spData.getInstituteID()+"/";
    public static final String FETCH_QUESTIONS = SERVER_URL+"Questions/fetch_questions.php";
    public static final String QA_MARKS = SERVER_URL+"Questions/qa_marks.php";
    public static final String FEEDS_DOWNLOAD = SERVER_URL+"Feeds/fetch_feeds.php";
    public static final String LIKE_COMMENT = SERVER_URL+"Feeds/like_comment.php";
    public static final String DELETE_FROM_FEEDS = SERVER_URL+"Feeds/delete.php";
    public static final String FETCH_COMMENTS = SERVER_URL+"Feeds/fetch_comments.php";
    public static final String LOGIN = SERVER_URL+"LoginAndRegister/login.php";
    public static final String EVENTS_FETCH = SERVER_URL+"Events/events_fetch.php";
    public static final String EVENTS_INSERT = SERVER_URL+"Events/events_insert.php";
    public static final String CLASSES_DIVISIONS_SUBJECTS = SERVER_URL+"Generic/classes_divisions_subjects.php";
    public static final String ATTENDANCE = SERVER_URL+"Attendance/attendance.php";
    public static final String ATTENDANCE_FETCH = SERVER_URL+"Attendance/attendance_fetch.php";
    public static final String MARKS = SERVER_URL+"ProgressReport/marks.php";
    public static final String PROPIC_BASE_URL = SERVER_URL+"Profile/";
    public static final String PROPIC_CHANGE = PROPIC_BASE_URL+"propic.php";
    public static final String QA_MARKS_DATA = SERVER_URL+"Questions/qa_marks_data_fetch.php";


}
