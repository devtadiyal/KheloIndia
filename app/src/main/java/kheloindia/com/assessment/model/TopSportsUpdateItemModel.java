package kheloindia.com.assessment.model;

import java.util.ArrayList;

/**
 * Created by CT13 on 2018-05-08.
 */

public class TopSportsUpdateItemModel {

    private String student_name;
    private String student_roll_no;
    private String student_id;
    private ArrayList<String>sportsList;
    private String sport_name;


    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getStudent_roll_no() {
        return student_roll_no;
    }

    public void setStudent_roll_no(String student_roll_no) {
        this.student_roll_no = student_roll_no;
    }

    public ArrayList<String> getSportsList() {
        return sportsList;
    }

    public void setSportsList(ArrayList<String> sportsList) {
        this.sportsList = sportsList;
    }

    public String getSport_name() {
        return sport_name;
    }

    public void setSport_name(String sport_name) {
        this.sport_name = sport_name;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }




}
