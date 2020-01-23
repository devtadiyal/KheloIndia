package kheloindia.com.assessment.model;

import java.util.Date;

import kheloindia.com.assessment.functions.Constant;

/**
 * Created by CT13 on 2017-09-18.
 */

public class FitnessSkillTestResultModel {

    private int skill_score_id;
    private int student_id;
    private int camp_id;
    private int test_type_id;
    private int checklist_item_id;
    private int coordinator_id;
    private String score;
    private Date created_on;
    private String  longitude;
    private String latitude;
    private long last_modified_on;
    private boolean synced;
    private boolean tested;
    private Date device_date;

    public Date getDevice_date() {
        return device_date;
    }

    public void setDevice_date(String device_date) {
        Date dateTime = Constant.ConvertStringTODate(device_date);
        this.device_date = dateTime;
    }


    public boolean getSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }



    public boolean getTested() {
        return tested;
    }

    public void setTested(boolean tested) {
        this.tested = tested;
    }



    public int getSkill_score_id() {
        return skill_score_id;
    }

    public void setSkill_score_id(int skill_score_id) {
        this.skill_score_id = skill_score_id;
    }

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public int getCamp_id() {
        return camp_id;
    }

    public void setCamp_id(int camp_id) {
        this.camp_id = camp_id;
    }

    public int getTest_type_id() {
        return test_type_id;
    }

    public void setTest_type_id(int test_type_id) {
        this.test_type_id = test_type_id;
    }

    public int getChecklist_item_id() {
        return checklist_item_id;
    }

    public void setChecklist_item_id(int checklist_item_id) {
        this.checklist_item_id = checklist_item_id;
    }

    public int getCoordinator_id() {
        return coordinator_id;
    }

    public void setCoordinator_id(int coordinator_id) {
        this.coordinator_id = coordinator_id;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public Date getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        Date dateTime = Constant.ConvertStringTODate(created_on);
        this.created_on = dateTime;

    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public long getLast_modified_on() {
        return last_modified_on;
    }

    public void setLast_modified_on(String last_modified_on) {
        long dateTime = Constant.convertDateTomilliSec(last_modified_on);
        this.last_modified_on = dateTime;
    }





}
