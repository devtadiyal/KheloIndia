package kheloindia.com.assessment.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by CT13 on 2018-05-14.
 */

public class DartBoardHeaderModel {


    /**
     * IsSuccess : True
     * Message : Success
     * Result : [{"Attendance":"Late","Day_End":" 1:57PM","Day_Start":" 1:57PM","School_end_time":"2:00 PM","school_id":"23","school_start_Time":"7:30 AM","trainer_id":"34339"}]
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
         * Attendance : Late
         * Day_End :  1:57PM
         * Day_Start :  1:57PM
         * School_end_time : 2:00 PM
         * school_id : 23
         * school_start_Time : 7:30 AM
         * trainer_id : 34339
         */

        @SerializedName("Attendance")
        private String Attendance;
        @SerializedName("Day_End")
        private String DayEnd;
        @SerializedName("Day_Start")
        private String DayStart;
        @SerializedName("School_end_time")
        private String SchoolEndTime;
        @SerializedName("school_id")
        private String schoolId;
        @SerializedName("school_start_Time")
        private String schoolStartTime;
        @SerializedName("trainer_id")
        private String trainerId;

        public String getAttendance() {
            return Attendance;
        }

        public void setAttendance(String Attendance) {
            this.Attendance = Attendance;
        }

        public String getDayEnd() {
            return DayEnd;
        }

        public void setDayEnd(String DayEnd) {
            this.DayEnd = DayEnd;
        }

        public String getDayStart() {
            return DayStart;
        }

        public void setDayStart(String DayStart) {
            this.DayStart = DayStart;
        }

        public String getSchoolEndTime() {
            return SchoolEndTime;
        }

        public void setSchoolEndTime(String SchoolEndTime) {
            this.SchoolEndTime = SchoolEndTime;
        }

        public String getSchoolId() {
            return schoolId;
        }

        public void setSchoolId(String schoolId) {
            this.schoolId = schoolId;
        }

        public String getSchoolStartTime() {
            return schoolStartTime;
        }

        public void setSchoolStartTime(String schoolStartTime) {
            this.schoolStartTime = schoolStartTime;
        }

        public String getTrainerId() {
            return trainerId;
        }

        public void setTrainerId(String trainerId) {
            this.trainerId = trainerId;
        }
    }
}
