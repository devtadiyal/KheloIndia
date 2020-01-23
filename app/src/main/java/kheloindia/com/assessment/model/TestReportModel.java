package kheloindia.com.assessment.model;

/**
 * Created by CT13 on 2017-05-16.
 */

/*
LSM.student_registration_num, LSM.Student_Name, LSM.Current_School_ID, LSM.Student_ID,LSM.Gender,
" +
            "LTC.Test_Name ,CASE WHEN FTRA.Score IS NULL THEN 2 ELSE 1 END as 'TestCompleted', \n" +
            "		LFTT.sub_test_ID AS SubTestTypeID,LFTT.Test_Name AS SubTestName
 */

public class TestReportModel {

    private String gender;
    private String student_name;
    private String current_roll_num;
    private String student_registration_num;
    private boolean tested;
    private String TestCompleted;
    private String test_name;
    private String SubTestName;
    private String student_id;
    private String total;
    private String tests_applicable;
    private String class_id;
    private String score;
    private String test_type_id;

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTests_applicable() {
        return tests_applicable;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public void setTests_applicable(String tests_applicable) {
        this.tests_applicable = tests_applicable;
    }

    public String getTest_type_id() {

        return test_type_id;

    }

    public void setTest_type_id(String test_type_id) {
        this.test_type_id = test_type_id;
    }



    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCurrent_roll_num() {
        return current_roll_num;
    }

    public void setCurrent_roll_num(String current_roll_num) {
        this.current_roll_num = current_roll_num;
    }

//    public List<TestReportItemModel> getTestReportItems() {
//        return testReportItems;
//    }
//
//    public void setTestReportItems(List<TestReportItemModel> testReportItems) {
//        this.testReportItems = testReportItems;
//    }


    public boolean isTested() {
        return tested;
    }

    public void setTested(boolean tested) {
        this.tested = tested;
    }

    public String getTest_name() {
        return test_name;
    }

    public void setTest_name(String test_name) {
        this.test_name = test_name;
    }

    public String getStudent_registration_num() {
        return student_registration_num;
    }

    public void setStudent_registration_num(String student_registration_num) {
        this.student_registration_num = student_registration_num;
    }

    public String getTestCompleted() {
        return TestCompleted;
    }

    public void setTestCompleted(String testCompleted) {
        TestCompleted = testCompleted;
    }

    public String getSubTestName() {
        return SubTestName;
    }

    public void setSubTestName(String subTestName) {
        SubTestName = subTestName;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
