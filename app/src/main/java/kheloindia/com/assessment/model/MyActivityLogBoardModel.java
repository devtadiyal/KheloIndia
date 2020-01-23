package kheloindia.com.assessment.model;

/**
 * Created by CT13 on 2017-07-04.
 */

public class MyActivityLogBoardModel {

    private String Date;
    private String start_end_time;
    private String school_name;
    private String attendance;


    public String getStart_end_time() {
        return start_end_time;
    }

    public void setStart_end_time(String start_end_time) {
        this.start_end_time = start_end_time;
    }

    public String getSchool_name() {
        return school_name;
    }

    public void setSchool_name(String school_name) {
        this.school_name = school_name;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }
}
