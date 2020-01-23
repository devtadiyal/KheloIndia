package kheloindia.com.assessment.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by CT13 on 2018-05-09.
 */

public class TopSportsUpdateSecondModel {


    /**
     * IsSuccess : false
     * Message : Please select a valid Student Id
     * Result :
     */

    @SerializedName("IsSuccess")
    private String IsSuccess;
    @SerializedName("Message")
    private String Message;
    @SerializedName("Result")
    private String Result;

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

    public String getResult() {
        return Result;
    }

    public void setResult(String Result) {
        this.Result = Result;
    }
}
