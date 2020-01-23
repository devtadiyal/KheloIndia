package kheloindia.com.assessment.model;

/**
 * Created by CT13 on 2017-05-18.
 */

public class CampMasterModel {

   private int camp_id;
    private String camp_name;
    private int camp_type;
    private int school_id;
    private int venue_id;
    private String start_date;
    private String end_date;
    private int camp_coordination_id;
    private int questionaire_id;
    private int registration_coordinator_id;
    private int status;
    private int data_flag;

    public int getCamp_id() {
        return camp_id;
    }

    public void setCamp_id(int camp_id) {
        this.camp_id = camp_id;
    }

    public String getCamp_name() {
        return camp_name;
    }

    public void setCamp_name(String camp_name) {
        this.camp_name = camp_name;
    }

    public int getCamp_type() {
        return camp_type;
    }

    public void setCamp_type(int camp_type) {
        this.camp_type = camp_type;
    }

    public int getSchool_id() {
        return school_id;
    }

    public void setSchool_id(int school_id) {
        this.school_id = school_id;
    }

    public int getVenue_id() {
        return venue_id;
    }

    public void setVenue_id(int venue_id) {
        this.venue_id = venue_id;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public int getCamp_coordination_id() {
        return camp_coordination_id;
    }

    public void setCamp_coordination_id(int camp_coordination_id) {
        this.camp_coordination_id = camp_coordination_id;
    }

    public int getQuestionaire_id() {
        return questionaire_id;
    }

    public void setQuestionaire_id(int questionaire_id) {
        this.questionaire_id = questionaire_id;
    }

    public int getRegistration_coordinator_id() {
        return registration_coordinator_id;
    }

    public void setRegistration_coordinator_id(int registration_coordinator_id) {
        this.registration_coordinator_id = registration_coordinator_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getData_flag() {
        return data_flag;
    }

    public void setData_flag(int data_flag) {
        this.data_flag = data_flag;
    }
}
