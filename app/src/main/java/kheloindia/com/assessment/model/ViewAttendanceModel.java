package kheloindia.com.assessment.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by PC10 on 17-Aug-17.
 */

public class ViewAttendanceModel {


    /**
     * IsSuccess : True
     * Message : Success
     * Result : [{"ActivityId":"1","Activity_Name":"At_School","CreatedON":"9 May 2018 11:23:47:000","Day":"9","Month":"5","School_Name":"RWS","Schoolid":"23","TrainerID":"34339","Year":"2018"},{"ActivityId":"1","Activity_Name":"At_School","CreatedON":"9 May 2018 11:23:47:000","Day":"9","Month":"5","School_Name":"RWS","Schoolid":"23","TrainerID":"34339","Year":"2018"}]
     */

    @SerializedName("IsSuccess")
    private String IsSuccess;
    @SerializedName("Message")
    private String Message;
    @SerializedName("Result")
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
         * ActivityId : 1
         * Activity_Name : At_School
         * CreatedON : 9 May 2018 11:23:47:000
         * Day : 9
         * Month : 5
         * School_Name : RWS
         * Schoolid : 23
         * TrainerID : 34339
         * Year : 2018
         */

        @SerializedName("ActivityId")
        private String ActivityId;
        @SerializedName("Activity_Name")
        private String ActivityName;
        @SerializedName("CreatedON")
        private String CreatedON;
        @SerializedName("Day")
        private String Day;
        @SerializedName("Month")
        private String Month;
        @SerializedName("School_Name")
        private String SchoolName;
        @SerializedName("Schoolid")
        private String Schoolid;
        @SerializedName("TrainerID")
        private String TrainerID;
        @SerializedName("Year")
        private String Year;

        public String getActivityId() {
            return ActivityId;
        }

        public void setActivityId(String ActivityId) {
            this.ActivityId = ActivityId;
        }

        public String getActivityName() {
            return ActivityName;
        }

        public void setActivityName(String ActivityName) {
            this.ActivityName = ActivityName;
        }

        public String getCreatedON() {
            return CreatedON;
        }

        public void setCreatedON(String CreatedON) {
            this.CreatedON = CreatedON;
        }

        public String getDay() {
            return Day;
        }

        public void setDay(String Day) {
            this.Day = Day;
        }

        public String getMonth() {
            return Month;
        }

        public void setMonth(String Month) {
            this.Month = Month;
        }

        public String getSchoolName() {
            return SchoolName;
        }

        public void setSchoolName(String SchoolName) {
            this.SchoolName = SchoolName;
        }

        public String getSchoolid() {
            return Schoolid;
        }

        public void setSchoolid(String Schoolid) {
            this.Schoolid = Schoolid;
        }

        public String getTrainerID() {
            return TrainerID;
        }

        public void setTrainerID(String TrainerID) {
            this.TrainerID = TrainerID;
        }

        public String getYear() {
            return Year;
        }

        public void setYear(String Year) {
            this.Year = Year;
        }
    }
}
