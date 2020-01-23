package kheloindia.com.assessment.model;

import java.util.Date;

import kheloindia.com.assessment.functions.Constant;

/**
 * Created by PC10 on 15-Mar-18.
 */

public class ScreenMasterModel {
    String screen_id;
    int partner_id;
    String screen_name;
    String web_url;
    String mobile_icon_title;
    String icon_image_name;
    String icon_path;
    Date created_on;
    String created_by;
    Date modified_on;
    String modified_by;
    int is_deleted;

    public String getScreen_id() {
        return screen_id;
    }

    public void setScreen_id(String screen_id) {
        this.screen_id = screen_id;
    }

    public int getPartner_id() {
        return partner_id;
    }

    public void setPartner_id(int partner_id) {
        this.partner_id = partner_id;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public String getWeb_url() {
        return web_url;
    }

    public void setWeb_url(String web_url) {
        this.web_url = web_url;
    }

    public String getMobile_icon_title() {
        return mobile_icon_title;
    }

    public void setMobile_icon_title(String mobile_icon_title) {
        this.mobile_icon_title = mobile_icon_title;
    }

    public String getIcon_image_name() {
        return icon_image_name;
    }

    public void setIcon_image_name(String icon_image_name) {
        this.icon_image_name = icon_image_name;
    }

    public String getIcon_path() {
        return icon_path;
    }

    public void setIcon_path(String icon_path) {
        this.icon_path = icon_path;
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

    public int getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(int is_deleted) {
        this.is_deleted = is_deleted;
    }
}
