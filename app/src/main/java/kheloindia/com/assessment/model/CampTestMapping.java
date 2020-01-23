package kheloindia.com.assessment.model;

/**
 * Created by PC10 on 11-Oct-17.
 */

public class CampTestMapping {
    public int camp_id;
    public int school_id;
    public int test_type_id;
    public String Class_ID;
    public int getCamp_id() {
        return camp_id;
    }

    public void setCamp_id(int camp_id) {
        this.camp_id = camp_id;
    }

    public int getSchool_id() {
        return school_id;
    }

    public void setSchool_id(int school_id) {
        this.school_id = school_id;
    }

    public int getTest_type_id() {
        return test_type_id;
    }

    public void setTest_type_id(int test_type_id) {
        this.test_type_id = test_type_id;
    }


    public String getClass_ID() {
        return Class_ID;
    }

    public void setClass_ID(String class_ID) {
        Class_ID = class_ID;
    }
}
