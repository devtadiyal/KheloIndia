package kheloindia.com.assessment.model;

import java.util.Date;

import kheloindia.com.assessment.functions.Constant;

/**
 * Created by CT13 on 2017-05-18.
 */

public class StudentMasterModel {

//    private int student_liveplus_id;
//    private int user_login_id;
    private String student_name;
    private String current_school_id;
    private  String current_roll_num;
    private int gender;
    private String dob;
    private String email;
    private String gaurdian_name;
    private String phone;
    private String address;
    private Date created_on;
    private String created_by;
    private Date last_modified_on;
    private String last_modified_by;
    private int student_id;
    private String student_registration_num;
    private String current_class;
    private String class_id;
    private int  class_partition_id;
    private String section;
    private int is_active;
    private String camp_id;



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

    public String getCamp_id() {
        return camp_id;
    }

    public void setCamp_id(String camp_id) {
        this.camp_id = camp_id;
    }


//    public int getStudent_liveplus_id() {
//        return student_liveplus_id;
//    }

//    public void setStudent_liveplus_id(int student_liveplus_id) {
//        this.student_liveplus_id = student_liveplus_id;
//    }

//    public int getUser_login_id() {
//        return user_login_id;
//    }

//    public void setUser_login_id(int user_login_id) {
//        this.user_login_id = user_login_id;
//    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getCurrent_school_id() {
        return current_school_id;
    }

    public void setCurrent_school_id(String current_school_id) {
        this.current_school_id = current_school_id;
    }

    public String getCurrent_roll_num() {
        return current_roll_num;
    }

    public void setCurrent_roll_num(String current_roll_num) {
        this.current_roll_num = current_roll_num;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGaurdian_name() {
        return gaurdian_name;
    }

    public void setGaurdian_name(String gaurdian_name) {
        this.gaurdian_name = gaurdian_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public String getStudent_registration_num() {
        return student_registration_num;
    }

    public void setStudent_registration_num(String student_registration_num) {
        this.student_registration_num = student_registration_num;
    }

    public String getCurrent_class() {
        return current_class;
    }

    public void setCurrent_class(String current_class) {
        this.current_class = current_class;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public int getClass_partition_id() {
        return class_partition_id;
    }

    public void setClass_partition_id(int class_partition_id) {
        this.class_partition_id = class_partition_id;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }
}
