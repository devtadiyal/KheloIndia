package kheloindia.com.assessment.model;

/**
 * Created by CT13 on 2017-09-21.
 */

public class SkillTransactionModel {

    /**
     * IsSuccess : true
     * Message : Result updated
     * Result : 0
     */

    private String IsSuccess;
    private String Message;
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
