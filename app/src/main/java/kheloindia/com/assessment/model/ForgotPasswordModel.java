package kheloindia.com.assessment.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by CT13 on 2017-06-14.
 */

public class ForgotPasswordModel {


    /**
     * IsSuccess : true
     * Message : success
     * Result : 5DD001
     */

    @SerializedName("IsSuccess")
    private String IsSuccess;
    @SerializedName("Message")
    private String Message;
    @SerializedName("Result")
    private String Result;
    @SerializedName("OTP")
    private String otp;

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

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
