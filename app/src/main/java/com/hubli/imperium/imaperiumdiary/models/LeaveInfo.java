package com.hubli.imperium.imaperiumdiary.models;

/**
 * Created by Rafiq Ahmad on 6/6/2017.
 */

public class LeaveInfo {

    private String student_name;
    private String todate;
    private String fromdate;
    private String status;
    private String detail;

public LeaveInfo(String student_name,String todate,String fromdate,String status,String detail){
    this.student_name=student_name;
    this.todate=todate;
    this.fromdate=fromdate;
    this.status=status;
    this.detail=detail;
}

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getTodate() {
        return todate;
    }

    public void setTodate(String todate) {
        this.todate = todate;
    }

    public String getFromdate() {
        return fromdate;
    }

    public void setFromdate(String fromdate) {
        this.fromdate = fromdate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
