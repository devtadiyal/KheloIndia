package kheloindia.com.assessment.model;

import java.util.List;

/**
 * Created by PC10 on 14-Sep-17.
 */

public class OptionModel {


    /**
     * IsSuccess : true
     * Message : success
     * Result : [{"CheckListItem_ID":"1","Item_Name":"Both feet are off the ground for brief period of time"},{"CheckListItem_ID":"2","Item_Name":"Arms move in opposite direction to legs\r\n"},{"CheckListItem_ID":"3","Item_Name":"Head and trunk are still , with eyes focused straight ahead\r\n"},{"CheckListItem_ID":"4","Item_Name":"Foot placement is near or on a line"}]
     */

    private String IsSuccess;
    private String Message;
    private List<ResultBean> Result;

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

    public List<ResultBean> getResult() {
        return Result;
    }

    public void setResult(List<ResultBean> Result) {
        this.Result = Result;
    }

    public static class ResultBean {
        /**
         * CheckListItem_ID : 1
         * Item_Name : Both feet are off the ground for brief period of time
         */

        private String CheckListItem_ID;
        private String Item_Name;

        public String getCheckListItem_ID() {
            return CheckListItem_ID;
        }

        public void setCheckListItem_ID(String CheckListItem_ID) {
            this.CheckListItem_ID = CheckListItem_ID;
        }

        public String getItem_Name() {
            return Item_Name;
        }

        public void setItem_Name(String Item_Name) {
            this.Item_Name = Item_Name;
        }
    }
}
