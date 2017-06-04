package com.hubli.imperium.imaperiumdiary.Attendance.Teacher.GiveAttendance;

/**
 * Created by Faheem on 04-06-2017.
 */

public class ListData {

    private int userNumber, rollNumber;
    private String studentName, attendance;

    public ListData(String userNumber, String rollNumber, String studentName, String attendance) {
        this.userNumber = Integer.parseInt(userNumber);
        this.rollNumber = Integer.parseInt(rollNumber);
        this.studentName = studentName;
        this.attendance = attendance;
    }

    public int getUserNumber() {
        return userNumber;
    }

    public int getRollNumber() {
        return rollNumber;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }
}
