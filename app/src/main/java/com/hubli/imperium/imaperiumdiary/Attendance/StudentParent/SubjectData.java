package com.hubli.imperium.imaperiumdiary.Attendance.StudentParent;

import com.hubli.imperium.imaperiumdiary.Data.SPData;

/**
 * Created by Faheem on 03-06-2017.
 */

public class SubjectData {

    private String subjectName;
    private int subjectID;

    public SubjectData(String subjectID, String subjectName) {
        this.subjectName = subjectName;
        this.subjectID = Integer.parseInt(subjectID);
    }

    public String getSubjectName() {
        return subjectName;
    }

    public int getSubjectID() {
        return subjectID;
    }
}
