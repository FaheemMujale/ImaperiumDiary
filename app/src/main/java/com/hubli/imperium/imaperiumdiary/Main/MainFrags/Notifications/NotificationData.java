package com.hubli.imperium.imaperiumdiary.Main.MainFrags.Notifications;

/**
 * Created by Faheem on 18-06-2017.
 */

class NotificationData {
    private String notificationNumber;
    private String date;
    private String cd_id;
    private String notificationText;
    private String type;

    public static final String HOME_WORK = "HomeWork";
    public static final String MODEL_QP = "M_Q_P";
    public static final String EVENT = "Event";
    public static final String TIME_TABLE = "Time Table";

    public NotificationData(String notificationNumber,String type, String cd_id, String notificationText,String date) {
        this.notificationNumber = notificationNumber;
        this.cd_id = cd_id;
        this.notificationText = notificationText;
        this.type = type;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public String getNotificationNumber() {
        return notificationNumber;
    }

    public String getCd_id() {
        return cd_id;
    }

    public String getNotificationText() {
        return notificationText;
    }
}
