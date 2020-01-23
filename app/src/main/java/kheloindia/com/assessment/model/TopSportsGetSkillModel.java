package kheloindia.com.assessment.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by CT13 on 2018-05-15.
 */

public class TopSportsGetSkillModel {


    /**
     * IsSuccess : True
     * Message : Success
     * tu1 : [{"Current_Roll_Num":"001404","Current_school_id":null,"StudentID":"26","TestName":"Cricket","current_class":"IV-A","skillid":"6"}]
     */

    @SerializedName("IsSuccess")
    private String IsSuccess;
    @SerializedName("Message")
    private String Message;
    @SerializedName("tu1")
    private List<Tu1Bean> tu1;

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

    public List<Tu1Bean> getTu1() {
        return tu1;
    }

    public void setTu1(List<Tu1Bean> tu1) {
        this.tu1 = tu1;
    }

    public static class Tu1Bean {
        /**
         * Current_Roll_Num : 001404
         * Current_school_id : null
         * StudentID : 26
         * TestName : Cricket
         * current_class : IV-A
         * skillid : 6
         */

        @SerializedName("Current_Roll_Num")
        private String CurrentRollNum;
        @SerializedName("Current_school_id")
        private Object CurrentSchoolId;
        @SerializedName("StudentID")
        private String StudentID;
        @SerializedName("TestName")
        private String TestName;
        @SerializedName("current_class")
        private String currentClass;
        @SerializedName("skillid")
        private String skillid;

        public String getCurrentRollNum() {
            return CurrentRollNum;
        }

        public void setCurrentRollNum(String CurrentRollNum) {
            this.CurrentRollNum = CurrentRollNum;
        }

        public Object getCurrentSchoolId() {
            return CurrentSchoolId;
        }

        public void setCurrentSchoolId(Object CurrentSchoolId) {
            this.CurrentSchoolId = CurrentSchoolId;
        }

        public String getStudentID() {
            return StudentID;
        }

        public void setStudentID(String StudentID) {
            this.StudentID = StudentID;
        }

        public String getTestName() {
            return TestName;
        }

        public void setTestName(String TestName) {
            this.TestName = TestName;
        }

        public String getCurrentClass() {
            return currentClass;
        }

        public void setCurrentClass(String currentClass) {
            this.currentClass = currentClass;
        }

        public String getSkillid() {
            return skillid;
        }

        public void setSkillid(String skillid) {
            this.skillid = skillid;
        }
    }
}
