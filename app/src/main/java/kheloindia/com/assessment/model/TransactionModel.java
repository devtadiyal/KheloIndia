package kheloindia.com.assessment.model;

/**
 * Created by PC10 on 06/09/2017.
 */

public class TransactionModel {


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
