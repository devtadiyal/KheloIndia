package kheloindia.com.assessment.model;

import java.util.Date;

import kheloindia.com.assessment.functions.Constant;

/**
 * Created by PC10 on 15-Mar-18.
 */

public class UserScreenMapModel {
    int mapping_id;
    String screen_id;
    int user_type_id;
    Date created_on;
    String created_by;
    Date modified_on;
    String modified_by;
    String is_active;

    public int getMapping_id() {
        return mapping_id;
    }

    public void setMapping_id(int mapping_id) {
        this.mapping_id = mapping_id;
    }

    public String getScreen_id() {
        return screen_id;
    }

    public void setScreen_id(String screen_id) {
        this.screen_id = screen_id;
    }

    public int getUser_type_id() {
        return user_type_id;
    }

    public void setUser_type_id(int user_type_id) {
        this.user_type_id = user_type_id;
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

    public Date getModified_on() {
        return modified_on;
    }

    public void setModified_on(String modified_on) {
        Date dateTime = Constant.ConvertStringTODate(modified_on);
        this.modified_on = dateTime;

    }

    public String getModified_by() {
        return modified_by;
    }

    public void setModified_by(String modified_by) {
        this.modified_by = modified_by;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }
}
