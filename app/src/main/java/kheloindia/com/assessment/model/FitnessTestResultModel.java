package kheloindia.com.assessment.model;

import java.util.Date;

import kheloindia.com.assessment.functions.Constant;

/**
 * Created by CT13 on 2017-05-19.
 */

public class FitnessTestResultModel {

    private   int student_id;
    private int camp_id;
    private int test_type_id;
    private int test_coordinator_id;
    private String score;
    private double percentile;
    private Date created_on;
    private Date device_date;
    private String created_by;
    private long last_modified_on;
    private String last_modified_by;
    private Double latitude;
    private Double longitude;
    private String SubTestName;
    private String TestName;
    private boolean TestedOrNot;
    private boolean isSyncedOrNot;


    public Date getDevice_date() {
        return device_date;
    }

    public void setDevice_date(String device_date) {
        Date dateTime = Constant.ConvertStringTODate(device_date);
        this.device_date = dateTime;
    }

    public String getTestName() {
        return TestName;
    }

    public void setTestName(String testName) {
        TestName = testName;
    }


    public boolean isSyncedOrNot() {
        return isSyncedOrNot;
    }

    public void setSyncedOrNot(boolean syncedOrNot) {
        isSyncedOrNot = syncedOrNot;
    }

    public boolean isTestedOrNot() {
        return TestedOrNot;
    }

    public void setTestedOrNot(boolean testedOrNot) {
        TestedOrNot = testedOrNot;
    }

    public String getSubTestName() {
        return SubTestName;
    }

    public void setSubTestName(String subTestName) {
        SubTestName = subTestName;
    }


    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
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

    public int getTest_coordinator_id() {
        return test_coordinator_id;
    }

    public void setTest_coordinator_id(int test_coordinator_id) {
        this.test_coordinator_id = test_coordinator_id;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public double getPercentile() {
        return percentile;
    }

    public void setPercentile(double percentile) {
        this.percentile = percentile;
    }

    public Date getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        Date dateTime = Constant.ConvertStringTODate(created_on);
        this.created_on = dateTime;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public long getLast_modified_on() {
        return last_modified_on;
    }

    public void setLast_modified_on(String last_modified_on) {
        long dateTime = Constant.convertDateTomilliSec(last_modified_on);
        this.last_modified_on = dateTime;
    }

    public String getLast_modified_by() {
        return last_modified_by;
    }

    public void setLast_modified_by(String last_modified_by) {
        this.last_modified_by = last_modified_by;
    }
}
