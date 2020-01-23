package kheloindia.com.assessment.model;

/**
 * Created by PC10 on 27-Mar-18.
 */

public class TrackModel {
    String Activity_id;
    String created_on;
    String current_address;
    String Day_status;
    String latitude;
    String longitude;
    String school_id;
    String trainer_id;
    String distance_From_Destination;

    public String getActivity_id() {
        return Activity_id;
    }

    public void setActivity_id(String activity_id) {
        Activity_id = activity_id;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getCurrent_address() {
        return current_address;
    }

    public void setCurrent_address(String current_address) {
        this.current_address = current_address;
    }

    public String getDay_status() {
        return Day_status;
    }

    public void setDay_status(String day_status) {
        Day_status = day_status;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getSchool_id() {
        return school_id;
    }

    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }

    public String getTrainer_id() {
        return trainer_id;
    }

    public void setTrainer_id(String trainer_id) {
        this.trainer_id = trainer_id;
    }

    public String getDistance_From_Destination() {
        return distance_From_Destination;
    }

    public void setDistance_From_Destination(String distance_From_Destination) {
        this.distance_From_Destination = distance_From_Destination;
    }
}
