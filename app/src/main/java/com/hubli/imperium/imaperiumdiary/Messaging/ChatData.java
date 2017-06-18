package com.hubli.imperium.imaperiumdiary.Messaging;

import com.hubli.imperium.imaperiumdiary.Data.SPData;

/**
 * Created by Faheem on 18-06-2017.
 */

public class ChatData {

    private int chatId;
    private int from;
    private String firstName;
    private String lastName;
    private String time;
    private String propicUrl;
    private String message;

    public ChatData(int chatId, String from, String firstName, String lastName, String propicUrl, String time, String message) {
        this.chatId = chatId;
        this.from = from.contentEquals(new SPData().getUserData(SPData.USER_NUMBER)) ? 0:1;
        this.firstName = firstName;
        this.lastName = lastName;
        this.time = time;
        this.propicUrl = propicUrl;
        this.message = message;
    }

    public int getFrom() {
        return from;
    }

    public int getChatId() {
        return chatId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getTime() {
        return time;
    }

    public String getPropicUrl() {
        return propicUrl;
    }

    public String getMessage() {
        return message;
    }
}
