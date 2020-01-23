package kheloindia.com.assessment.model;

import java.util.ArrayList;

/**
 * Created by CT13 on 2017-07-04.
 */

public class MyActivityLogBoardItemModel {

    private String Boys;
    private String Girls;
    private String Class_Name;
    private String Period;
    private String Remarks;
    private String Lessons_Others;
    private ArrayList<AttendedByModel>attendedByModelList;
    private String Skill_name;
    private boolean attended_max;



    public String getBoys() {
        return Boys;
    }

    public void setBoys(String boys) {
        Boys = boys;
    }

    public String getSkill_name() {
        return Skill_name;
    }

    public void setSkill_name(String skill_name) {
        Skill_name = skill_name;
    }

    public String getGirls() {
        return Girls;
    }

    public void setGirls(String girls) {
        Girls = girls;
    }

    public String getClass_Name() {
        return Class_Name;
    }

    public void setClass_Name(String class_Name) {
        Class_Name = class_Name;
    }

    public String getPeriod() {
        return Period;
    }

    public void setPeriod(String period) {
        Period = period;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getLessons_Others() {
        return Lessons_Others;
    }

    public void setLessons_Others(String lessons_Others) {
        Lessons_Others = lessons_Others;
    }

    public ArrayList<AttendedByModel> getAttendedByModelList() {
        return attendedByModelList;
    }

    public void setAttendedByModelList(ArrayList<AttendedByModel> attendedByModelList) {
        this.attendedByModelList = attendedByModelList;
    }

    public boolean isAttended_max() {
        return attended_max;
    }

    public void setAttended_max(boolean attended_max) {
        this.attended_max = attended_max;
    }
}
