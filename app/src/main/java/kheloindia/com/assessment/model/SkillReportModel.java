package kheloindia.com.assessment.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by CT13 on 2017-09-21.
 */

public class SkillReportModel {


    /**
     * IsSuccess : true
     * Message : success
     * Result : {"student":[{"Created_On":"9/21/2017 4:40:43 AM","FK_Camp_ID":"129","FK_CheckListItem_ID":"29","FK_Student_ID":"4221","FK_Test_Coordinator_ID":"33200","FK_Test_Type_ID":"27","Last_Modified_On":"9/21/2017 4:40:43 AM","PK_Skill_Score_ID":"16","Score":"True"},{"Created_On":"9/21/2017 4:40:43 AM","FK_Camp_ID":"129","FK_CheckListItem_ID":"30","FK_Student_ID":"4221","FK_Test_Coordinator_ID":"33200","FK_Test_Type_ID":"27","Last_Modified_On":"9/21/2017 4:40:43 AM","PK_Skill_Score_ID":"17","Score":"True"},{"Created_On":"9/21/2017 4:40:43 AM","FK_Camp_ID":"129","FK_CheckListItem_ID":"31","FK_Student_ID":"4221","FK_Test_Coordinator_ID":"33200","FK_Test_Type_ID":"27","Last_Modified_On":"9/21/2017 4:40:43 AM","PK_Skill_Score_ID":"18","Score":"True"}]}
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
             * Created_On : 9/21/2017 4:40:43 AM
             * FK_Camp_ID : 129
             * FK_CheckListItem_ID : 29
             * FK_Student_ID : 4221
             * FK_Test_Coordinator_ID : 33200
             * FK_Test_Type_ID : 27
             * Last_Modified_On : 9/21/2017 4:40:43 AM
             * PK_Skill_Score_ID : 16
             * Score : True
             */

            @SerializedName("Created_On")
            private String CreatedOn;
            @SerializedName("FK_Camp_ID")
            private String FKCampID;
            @SerializedName("FK_CheckListItem_ID")
            private String FKCheckListItemID;
            @SerializedName("FK_Student_ID")
            private String FKStudentID;
            @SerializedName("FK_Test_Coordinator_ID")
            private String FKTestCoordinatorID;
            @SerializedName("FK_Test_Type_ID")
            private String FKTestTypeID;
            @SerializedName("Last_Modified_On")
            private String LastModifiedOn;
            @SerializedName("PK_Skill_Score_ID")
            private String PKSkillScoreID;
            @SerializedName("Score")
            private String Score;

            public String getCreatedOn() {
                return CreatedOn;
            }

            public void setCreatedOn(String CreatedOn) {
                this.CreatedOn = CreatedOn;
            }

            public String getFKCampID() {
                return FKCampID;
            }

            public void setFKCampID(String FKCampID) {
                this.FKCampID = FKCampID;
            }

            public String getFKCheckListItemID() {
                return FKCheckListItemID;
            }

            public void setFKCheckListItemID(String FKCheckListItemID) {
                this.FKCheckListItemID = FKCheckListItemID;
            }

            public String getFKStudentID() {
                return FKStudentID;
            }

            public void setFKStudentID(String FKStudentID) {
                this.FKStudentID = FKStudentID;
            }

            public String getFKTestCoordinatorID() {
                return FKTestCoordinatorID;
            }

            public void setFKTestCoordinatorID(String FKTestCoordinatorID) {
                this.FKTestCoordinatorID = FKTestCoordinatorID;
            }

            public String getFKTestTypeID() {
                return FKTestTypeID;
            }

            public void setFKTestTypeID(String FKTestTypeID) {
                this.FKTestTypeID = FKTestTypeID;
            }

            public String getLastModifiedOn() {
                return LastModifiedOn;
            }

            public void setLastModifiedOn(String LastModifiedOn) {
                this.LastModifiedOn = LastModifiedOn;
            }

            public String getPKSkillScoreID() {
                return PKSkillScoreID;
            }

            public void setPKSkillScoreID(String PKSkillScoreID) {
                this.PKSkillScoreID = PKSkillScoreID;
            }

            public String getScore() {
                return Score;
            }

            public void setScore(String Score) {
                this.Score = Score;
            }
        }
    }
}
