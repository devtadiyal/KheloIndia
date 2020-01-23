package kheloindia.com.assessment.model;

import java.util.List;

/**
 * Created by PC10 on 18-Aug-17.
 */

public class ProfileModel {


    /**
     * IsSuccess : true
     * Message : Table update successfuly
     * Result : []
     */

    private String IsSuccess;
    private String Message;
    private List<?> Result;

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

    public List<?> getResult() {
        return Result;
    }

    public void setResult(List<?> Result) {
        this.Result = Result;
    }
}
