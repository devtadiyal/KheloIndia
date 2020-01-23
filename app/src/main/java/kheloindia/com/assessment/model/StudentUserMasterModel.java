package kheloindia.com.assessment.model;

/**
 * Created by CT13 on 2017-05-18.
 */

public class StudentUserMasterModel {




    private String guardian_name;
    private String image_path;
    private String phone;
    private String student_name;
    private int user_login_id;
    private String user_login_name;
    private String address;
    private String current_class;
    private String date_of_birth;

    public String getGuardian_name() {
        return guardian_name;
    }

    public void setGuardian_name(String guardian_name) {
        this.guardian_name = guardian_name;
    }

    private String gender;

    public int getUser_login_id() {
        return user_login_id;
    }

    public void setUser_login_id(int user_login_id) {
        this.user_login_id = user_login_id;
    }



    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }



    public String getUser_login_name() {
        return user_login_name;
    }

    public void setUser_login_name(String user_login_name) {
        this.user_login_name = user_login_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCurrent_class() {
        return current_class;
    }

    public void setCurrent_class(String current_class) {
        this.current_class = current_class;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
