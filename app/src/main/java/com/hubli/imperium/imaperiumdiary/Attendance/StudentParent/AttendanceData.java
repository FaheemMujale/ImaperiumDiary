package com.hubli.imperium.imaperiumdiary.Attendance.StudentParent;


import com.hubli.imperium.imaperiumdiary.Utility.GenericMethods;

public class AttendanceData {
    String year, day, month, attendance;

    public AttendanceData(String year, String day, String month, String attendance) {
        this.year = year;
        this.day = day;
        this.month = month;
        this.attendance = attendance;
    }
    public String getAttendance() {
        return attendance;
    }

    public long getTimeInMili(){
       return GenericMethods.getTimeInMili(year + "-" + month + "-" + day + " " + 10 + ":" + 20 + ":" + 12);
    }
}
