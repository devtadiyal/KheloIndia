package kheloindia.com.assessment.model;

import java.util.Date;

import kheloindia.com.assessment.functions.Constant;

/**
 * Created by PC10 on 15-Sep-17.
 */

public class SkillTestTypeModel {

    private int checklist_item_id;
    private int test_type_id;
    private String item_name;
    private Date created_on;
    private Date last_modified_on;
    private String synced;

    public Date getLast_modified_on() {
        return last_modified_on;
    }

    public void setLast_modified_on(String last_modified_on) {
        Date dateTime = Constant.ConvertStringTODate(last_modified_on);
        this.last_modified_on = dateTime;
    }



    public int getChecklist_item_id() {
        return checklist_item_id;
    }

    public void setChecklist_item_id(int checklist_item_id) {
        this.checklist_item_id = checklist_item_id;
    }

    public int getTest_type_id() {
        return test_type_id;
    }

    public void setTest_type_id(int test_type_id) {
        this.test_type_id = test_type_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public Date getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        Date dateTime = Constant.ConvertStringTODate(created_on);
        this.created_on = dateTime;
    }

    public String getSynced() {
        return synced;
    }

    public void setSynced(String synced) {
        this.synced = synced;
    }
}
