package kheloindia.com.assessment.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by CT13 on 2017-07-05.
 */

public class ReportModel {


    /**
     * IsSuccess : true
     * Message : success
     * Result : {"student":[{"Camp_ID":"129","Created_By":"33200","Created_On":"9/19/2017 4:54:04 PM","Last_Modified_By":"","Last_Modified_On":"9/19/2017 4:24:04 AM","Percentile":"0.00","Score":"6126","Student_ID":"12003","SyncDateTime":"1/1/1900 12:00:00 AM","Test_Coordinator_ID":"33200","Test_Type_ID":"2"},{"Camp_ID":"129","Created_By":"33200","Created_On":"9/19/2017 5:01:48 PM","Last_Modified_By":"","Last_Modified_On":"9/19/2017 4:31:48 AM","Percentile":"0.00","Score":"336","Student_ID":"5567","SyncDateTime":"1/1/1900 12:00:00 AM","Test_Coordinator_ID":"33200","Test_Type_ID":"2"}]}
     */

    @SerializedName("IsSuccess")
    private String IsSuccess;
    @SerializedName("Message")
    private String Message;
    @SerializedName("Result")
    private ResultBean Result;

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

    public ResultBean getResult() {
        return Result;
    }

    public void setResult(ResultBean Result) {
        this.Result = Result;
    }

    public static class ResultBean {
        @SerializedName("student")
        private List<StudentBean> student;

        public List<StudentBean> getStudent() {
            return student;
        }

        public void setStudent(List<StudentBean> student) {
            this.student = student;
        }

        public static class StudentBean {
            /**
             * Camp_ID : 129
             * Created_By : 33200
             * Created_On : 9/19/2017 4:54:04 PM
             * Last_Modified_By :
             * Last_Modified_On : 9/19/2017 4:24:04 AM
             * Percentile : 0.00
             * Score : 6126
             * Student_ID : 12003
             * SyncDateTime : 1/1/1900 12:00:00 AM
             * Test_Coordinator_ID : 33200
             * Test_Type_ID : 2
             */

            @SerializedName("Camp_ID")
            private String CampID;
            @SerializedName("Created_By")
            private String CreatedBy;
            @SerializedName("Created_On")
            private String CreatedOn;
            @SerializedName("Last_Modified_By")
            private String LastModifiedBy;
            @SerializedName("Last_Modified_On")
            private String LastModifiedOn;
            @SerializedName("Percentile")
            private String Percentile;
            @SerializedName("Score")
            private String Score;
            @SerializedName("Student_ID")
            private String StudentID;
            @SerializedName("SyncDateTime")
            private String SyncDateTime;
            @SerializedName("Test_Coordinator_ID")
            private String TestCoordinatorID;
            @SerializedName("Test_Type_ID")
            private String TestTypeID;
            @SerializedName("Created_On_Device")
            private String CreatedOnDevice;

            public String getCreatedOnDevice() {
                return CreatedOnDevice;
            }

            public void setCreatedOnDevice(String createdOnDevice) {
                CreatedOnDevice = createdOnDevice;
            }

            public String getCampID() {
                return CampID;
            }

            public void setCampID(String CampID) {
                this.CampID = CampID;
            }

            public String getCreatedBy() {
                return CreatedBy;
            }

            public void setCreatedBy(String CreatedBy) {
                this.CreatedBy = CreatedBy;
            }

            public String getCreatedOn() {
                return CreatedOn;
            }

            public void setCreatedOn(String CreatedOn) {
                this.CreatedOn = CreatedOn;
            }

            public String getLastModifiedBy() {
                return LastModifiedBy;
            }

            public void setLastModifiedBy(String LastModifiedBy) {
                this.LastModifiedBy = LastModifiedBy;
            }

            public String getLastModifiedOn() {
                return LastModifiedOn;
            }

            public void setLastModifiedOn(String LastModifiedOn) {
                this.LastModifiedOn = LastModifiedOn;
            }

            public String getPercentile() {
                return Percentile;
            }

            public void setPercentile(String Percentile) {
                this.Percentile = Percentile;
            }

            public String getScore() {
                return Score;
            }

            public void setScore(String Score) {
                this.Score = Score;
            }

            public String getStudentID() {
                return StudentID;
            }

            public void setStudentID(String StudentID) {
                this.StudentID = StudentID;
            }

            public String getSyncDateTime() {
                return SyncDateTime;
            }

            public void setSyncDateTime(String SyncDateTime) {
                this.SyncDateTime = SyncDateTime;
            }

            public String getTestCoordinatorID() {
                return TestCoordinatorID;
            }

            public void setTestCoordinatorID(String TestCoordinatorID) {
                this.TestCoordinatorID = TestCoordinatorID;
            }

            public String getTestTypeID() {
                return TestTypeID;
            }

            public void setTestTypeID(String TestTypeID) {
                this.TestTypeID = TestTypeID;
            }
        }
    }
}
