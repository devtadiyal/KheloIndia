package kheloindia.com.assessment.model;

import java.util.List;

/**
 * Created by PC10 on 16-Apr-18.
 */

public class UpdateLocationModel {


    /**
     * IsSuccess : True
     * Message : Location Updated Successfuly
     * Tracker : []
     */

    private String IsSuccess;
    private String Message;
    private List<?> Tracker;

    public String getIsSuccess() {
        return IsSuccess;
    }

    public void setIsSuccess(String IsSuccess) {
        this.IsSuccess = IsSuccess;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public List<?> getTracker() {
        return Tracker;
    }

    public void setTracker(List<?> Tracker) {
        this.Tracker = Tracker;
    }
}
