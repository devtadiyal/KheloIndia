package kheloindia.com.assessment.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by CT13 on 2018-01-03.
 */

public class ActivityStudentModel {


    /**
     * IsSuccess : True
     * Message : Success
     * SAL : [{"Grade_key":"","Student_Name":"Aarohi Ranjeet Bankar","gradeid":"","studentId":"11"},{"Grade_key":"","Student_Name":"Ansh Agarwal","gradeid":"","studentId":"133"},{"Grade_key":"","Student_Name":"Ashu Choudhary","gradeid":"","studentId":"185"},{"Grade_key":"","Student_Name":"Himanish Sarkar","gradeid":"","studentId":"370"},{"Grade_key":"","Student_Name":"Himanshu Daurata Dutt","gradeid":"","studentId":"377"},{"Grade_key":"","Student_Name":"Lakshya Sharma","gradeid":"","studentId":"524"},{"Grade_key":"","Student_Name":"Pavni Arora","gradeid":"","studentId":"692"},{"Grade_key":"","Student_Name":"R Dhaksesh ","gradeid":"","studentId":"767"},{"Grade_key":"","Student_Name":"Roma Dudeia","gradeid":"","studentId":"846"},{"Grade_key":"","Student_Name":"Roshni Rai","gradeid":"","studentId":"849"},{"Grade_key":"","Student_Name":"Vidit Parmar","gradeid":"","studentId":"1143"},{"Grade_key":"","Student_Name":"Simran Chhabra","gradeid":"","studentId":"1774"},{"Grade_key":"","Student_Name":"Shreyansh Gupta","gradeid":"","studentId":"1821"},{"Grade_key":"","Student_Name":"Ganesh Lohia","gradeid":"","studentId":"1925"},{"Grade_key":"","Student_Name":"Anshika Kaushik","gradeid":"","studentId":"2741"},{"Grade_key":"","Student_Name":"Kabir Chauhan","gradeid":"","studentId":"2748"},{"Grade_key":"","Student_Name":"Ashutosh Singh","gradeid":"","studentId":"2749"},{"Grade_key":"","Student_Name":"Priyanka Danu","gradeid":"","studentId":"2753"},{"Grade_key":"","Student_Name":"Srishti Gupta","gradeid":"","studentId":"2758"},{"Grade_key":"","Student_Name":"Laksh Dangwal","gradeid":"","studentId":"2772"},{"Grade_key":"","Student_Name":"Hemant Nagar","gradeid":"","studentId":"2777"},{"Grade_key":"","Student_Name":"Tanveer Yadav","gradeid":"","studentId":"2780"},{"Grade_key":"","Student_Name":"Pranit Lohmod","gradeid":"","studentId":"2798"},{"Grade_key":"","Student_Name":"Yashika Rawat","gradeid":"","studentId":"2804"},{"Grade_key":"","Student_Name":"Mahak Yadav","gradeid":"","studentId":"2819"},{"Grade_key":"","Student_Name":"Anushree Gupta","gradeid":"","studentId":"2826"},{"Grade_key":"","Student_Name":"Janvi Rawat","gradeid":"","studentId":"2851"},{"Grade_key":"","Student_Name":"Naisha Rathi","gradeid":"","studentId":"2856"},{"Grade_key":"","Student_Name":"Manav Yadav","gradeid":"","studentId":"5048"},{"Grade_key":"","Student_Name":"Subham Mahapatra","gradeid":"","studentId":"5068"},{"Grade_key":"","Student_Name":"Takshita Gupta","gradeid":"","studentId":"5070"},{"Grade_key":"","Student_Name":"Ranvir Singh","gradeid":"","studentId":"5074"},{"Grade_key":"","Student_Name":"Lakshay Khatana","gradeid":"","studentId":"5483"},{"Grade_key":"","Student_Name":"Prabhleen Kaur","gradeid":"","studentId":"7659"},{"Grade_key":"","Student_Name":"Kapish Tanwar","gradeid":"","studentId":"7668"},{"Grade_key":"","Student_Name":"Shubh Partap Shekhawat","gradeid":"","studentId":"7702"},{"Grade_key":"","Student_Name":"Avni Baghel","gradeid":"","studentId":"7703"},{"Grade_key":"","Student_Name":"Supriya Kumari Barnwal","gradeid":"","studentId":"7733"},{"Grade_key":"","Student_Name":"Jai Kuhar","gradeid":"","studentId":"7742"},{"Grade_key":"","Student_Name":"Shaurya Garg","gradeid":"","studentId":"7751"},{"Grade_key":"","Student_Name":"Aryan Lohia","gradeid":"","studentId":"12592"},{"Grade_key":"","Student_Name":"Diksha Tyagi","gradeid":"","studentId":"12604"},{"Grade_key":"","Student_Name":"Bhavya Manchanda","gradeid":"","studentId":"12619"},{"Grade_key":"","Student_Name":"Vansh Tanwar","gradeid":"","studentId":"12654"},{"Grade_key":"","Student_Name":"Aadi Kalra","gradeid":"","studentId":"12687"},{"Grade_key":"","Student_Name":"Shubham Kumar","gradeid":"","studentId":"12696"},{"Grade_key":"","Student_Name":"Siddhant Maniyara","gradeid":"","studentId":"12758"}]
     */

    @SerializedName("IsSuccess")
    private String IsSuccess;
    @SerializedName("Message")
    private String Message;
    @SerializedName("SAL")
    private List<SALBean> SAL;

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

    public List<SALBean> getSAL() {
        return SAL;
    }

    public void setSAL(List<SALBean> SAL) {
        this.SAL = SAL;
    }

    public static class SALBean {
        /**
         * Grade_key :
         * Student_Name : Aarohi Ranjeet Bankar
         * gradeid :
         * studentId : 11
         */

        @SerializedName("Grade_key")
        private String GradeKey;
        @SerializedName("Student_Name")
        private String StudentName;
        @SerializedName("gradeid")
        private String gradeid;
        @SerializedName("studentId")
        private String studentId;

        public String getGradeKey() {
            return GradeKey;
        }

        public void setGradeKey(String GradeKey) {
            this.GradeKey = GradeKey;
        }

        public String getStudentName() {
            return StudentName;
        }

        public void setStudentName(String StudentName) {
            this.StudentName = StudentName;
        }

        public String getGradeid() {
            return gradeid;
        }

        public void setGradeid(String gradeid) {
            this.gradeid = gradeid;
        }

        public String getStudentId() {
            return studentId;
        }

        public void setStudentId(String studentId) {
            this.studentId = studentId;
        }
    }
}
