package kheloindia.com.assessment.model;

import java.util.Date;

import kheloindia.com.assessment.functions.Constant;

/**
 * Created by CT13 on 2017-05-18.
 */

public class SchoolsMasterModel {

    private int school_id;
    private String school_name;
    private int school_chain_id;
    private int city_id;
    private String address;
    private String phone_no;
    private String email;
    private String web_address;
    private String school_description;
    private String school_image_name;
    private String school_image_path;
    private String seniors_starting_from;
    private String Latitude;
    private String Longitude;
    private String school_start_time;
    private Date created_on;
    private String created_by;
    private Date last_modified_on;
    private String last_modified_by;
    private String school_logo_name;
    private String school_logo_path;
    private String school_alias_name;
    private int is_active;
    private String trainer_coordinator_id;
    private String office;
    private int isAttached;
    private String isRetestAllowed;
    private int forRetest;


    public int getIsAttached() {
        return isAttached;
    }

    public void setIsAttached(int isAttached) {
        this.isAttached = isAttached;
    }


    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getSchool_start_time() {
        return school_start_time;
    }

    public void setSchool_start_time(String school_start_time) {
        this.school_start_time = school_start_time;
    }

    public String getSeniors_starting_from() {
        return seniors_starting_from;
    }

    public void setSeniors_starting_from(String seniors_starting_from) {
        this.seniors_starting_from = seniors_starting_from;
    }


    public Date getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        Date dateTime = Constant.ConvertStringTODate(created_on);
        this.created_on = dateTime;
    }

    public Date getLast_modified_on() {
        return last_modified_on;
    }

    public void setLast_modified_on(String last_modified_on) {
        Date dateTime = Constant.ConvertStringTODate(last_modified_on);
        this.last_modified_on = dateTime;
    }




    public String getTrainer_coordinator_id() {
        return trainer_coordinator_id;
    }

    public void setTrainer_coordinator_id(String trainer_coordinator_id) {
        this.trainer_coordinator_id = trainer_coordinator_id;
    }



    public String getSchool_image_path() {
        return school_image_path;
    }

    public void setSchool_image_path(String school_image_path) {
        this.school_image_path = school_image_path;
    }

    public int getSchool_id() {
        return school_id;
    }

    public void setSchool_id(int school_id) {
        this.school_id = school_id;
    }

    public String getSchool_name() {
        return school_name;
    }

    public void setSchool_name(String school_name) {
        this.school_name = school_name;
    }

    public int getSchool_chain_id() {
        return school_chain_id;
    }

    public void setSchool_chain_id(int school_chain_id) {
        this.school_chain_id = school_chain_id;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWeb_address() {
        return web_address;
    }

    public void setWeb_address(String web_address) {
        this.web_address = web_address;
    }

    public String getSchool_description() {
        return school_description;
    }

    public void setSchool_description(String school_description) {
        this.school_description = school_description;
    }

    public String getSchool_image_name() {
        return school_image_name;
    }

    public void setSchool_image_name(String school_image_name) {
        this.school_image_name = school_image_name;
    }


    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }


    public String getLast_modified_by() {
        return last_modified_by;
    }

    public void setLast_modified_by(String last_modified_by) {
        this.last_modified_by = last_modified_by;
    }

    public String getSchool_logo_name() {
        return school_logo_name;
    }

    public void setSchool_logo_name(String school_logo_name) {
        this.school_logo_name = school_logo_name;
    }

    public String getSchool_logo_path() {
        return school_logo_path;
    }

    public void setSchool_logo_path(String school_logo_path) {
        this.school_logo_path = school_logo_path;
    }

    public String getSchool_alias_name() {
        return school_alias_name;
    }

    public void setSchool_alias_name(String school_alias_name) {
        this.school_alias_name = school_alias_name;
    }

    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }

    public String getIsRetestAllowed() {
        return isRetestAllowed;
    }

    public void setIsRetestAllowed(String isRetestAllowed) {
        this.isRetestAllowed = isRetestAllowed;
    }

    public int getForRetest() {
        return forRetest;
    }

    public void setForRetest(int forRetest) {
        this.forRetest = forRetest;
    }
}
