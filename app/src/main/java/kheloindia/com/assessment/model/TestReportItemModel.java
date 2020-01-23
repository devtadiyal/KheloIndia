package kheloindia.com.assessment.model;

/**
 * Created by CT13 on 2017-05-16.
 */

public class TestReportItemModel {

    private boolean tested;
    private String test_name;

    public String getSub_test_name() {
        return sub_test_name;
    }

    public void setSub_test_name(String sub_test_name) {
        this.sub_test_name = sub_test_name;
    }

    private String sub_test_name;


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
}
