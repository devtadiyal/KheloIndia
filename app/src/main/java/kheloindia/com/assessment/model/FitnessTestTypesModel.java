package kheloindia.com.assessment.model;

import java.util.Date;

import kheloindia.com.assessment.functions.Constant;

/**
 * Created by CT13 on 2017-05-19.
 */

public class FitnessTestTypesModel {

    private  int test_type_id;
    private  String test_name;
    private  String excel_name;
    private  String score_measurement;
    private String score_unit;
    private String score_criteria;
    private String test_description;
    private String test_performed;
    private Date created_on;
    private String created_by;
    private Date last_modified_on;
    private String last_modified_by;
    private int test_category_id;
    private String test_image;
    private String test_img_path;


    public String getSchool_id() {
        return school_id;
    }

    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }

    public String getTest_coordinator_id() {
        return test_coordinator_id;
    }

    public void setTest_coordinator_id(String test_coordinator_id) {
        this.test_coordinator_id = test_coordinator_id;
    }

    private String school_id;
    private String test_coordinator_id;

    public String getTest_image() {
        return test_image;
    }

    public void setTest_image(String test_image) {
        this.test_image = test_image;
    }

    public String getTest_img_path() {
        return test_img_path;
    }

    public void setTest_img_path(String test_img_path) {
        this.test_img_path = test_img_path;
    }



    public int getTest_category_id() {
        return test_category_id;
    }

    public void setTest_category_id(int test_category_id) {
        this.test_category_id = test_category_id;
    }

    public int getTest_type_id() {
        return test_type_id;
    }

    public void setTest_type_id(int test_type_id) {
        this.test_type_id = test_type_id;
    }

    public String getTest_name() {
        return test_name;
    }

    public void setTest_name(String test_name) {
        this.test_name = test_name;
    }

    public String getExcel_name() {
        return excel_name;
    }

    public void setExcel_name(String excel_name) {
        this.excel_name = excel_name;
    }

    public String getScore_measurement() {
        return score_measurement;
    }

    public void setScore_measurement(String score_measurement) {
        this.score_measurement = score_measurement;
    }

    public String getScore_unit() {
        return score_unit;
    }

    public void setScore_unit(String score_unit) {
        this.score_unit = score_unit;
    }

    public String getScore_criteria() {
        return score_criteria;
    }

    public void setScore_criteria(String score_criteria) {
        this.score_criteria = score_criteria;
    }

    public String getTest_description() {
        return test_description;
    }

    public void setTest_description(String test_description) {
        this.test_description = test_description;
    }

    public String getTest_performed() {
        return test_performed;
    }

    public void setTest_performed(String test_performed) {
        this.test_performed = test_performed;
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

    public Date getLast_modified_on() {
        return last_modified_on;
    }

    public void setLast_modified_on(String last_modified_on) {
        Date dateTime = Constant.ConvertStringTODate(last_modified_on);
        this.last_modified_on = dateTime;
    }

    public String getLast_modified_by() {
        return last_modified_by;
    }

    public void setLast_modified_by(String last_modified_by) {
        this.last_modified_by = last_modified_by;
    }
}
