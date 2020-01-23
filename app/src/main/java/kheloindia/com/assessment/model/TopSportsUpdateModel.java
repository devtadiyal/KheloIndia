package kheloindia.com.assessment.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by CT13 on 2018-05-08.
 */

public class TopSportsUpdateModel {


    /**
     * Detail : [{"Current_Roll_Num":"001541","StudentID":"21","student_name":"Abhi Yadav"},{"Current_Roll_Num":"001608","StudentID":"169","student_name":"Arshit Setia"},{"Current_Roll_Num":"001487","StudentID":"173","student_name":"Arunita Rawat"},{"Current_Roll_Num":"001614","StudentID":"206","student_name":"Ayush Tyagi"},{"Current_Roll_Num":"001485","StudentID":"338","student_name":"Hardik"},{"Current_Roll_Num":"001496","StudentID":"479","student_name":"Khusshi Kashyap"},{"Current_Roll_Num":"001424","StudentID":"530","student_name":"Lucky Lohia"},{"Current_Roll_Num":"001619","StudentID":"797","student_name":"Ronak Kashania"},{"Current_Roll_Num":"001581","StudentID":"943","student_name":"Shagun Dangwal"},{"Current_Roll_Num":"001570","StudentID":"965","student_name":"Shiv"},{"Current_Roll_Num":"001411","StudentID":"1126","student_name":"Vansh Yadav"},{"Current_Roll_Num":"001599","StudentID":"1139","student_name":"Vibhansh Garg"},{"Current_Roll_Num":"001689","StudentID":"1798","student_name":"Tejaswita"},{"Current_Roll_Num":"001711","StudentID":"1816","student_name":"Garv Bhanot"},{"Current_Roll_Num":"001717","StudentID":"1820","student_name":"Rajan"},{"Current_Roll_Num":"001849","StudentID":"2739","student_name":"Rishika Chauhan"},{"Current_Roll_Num":"001870","StudentID":"2757","student_name":"Tanishka Gupta"},{"Current_Roll_Num":"001873","StudentID":"2759","student_name":"Aditya Kharwar"},{"Current_Roll_Num":"001906","StudentID":"2789","student_name":"Parteek Yadav"},{"Current_Roll_Num":"002081","StudentID":"5036","student_name":"Aditya Adhikari"},{"Current_Roll_Num":"002084","StudentID":"5039","student_name":"Aditya Sati"},{"Current_Roll_Num":"002312","StudentID":"7683","student_name":"Khushi Aggarwal"},{"Current_Roll_Num":"002346","StudentID":"7711","student_name":"Anaya Datta"},{"Current_Roll_Num":"002633","StudentID":"12717","student_name":"Shaurya Mishra"},{"Current_Roll_Num":"002693","StudentID":"12752","student_name":"Mayank Dudi"}]
     * IsSuccess : True
     * Message : Success
     * TopSport : [{"TestID":"9","TestName":"Athletics"},{"TestID":"13","TestName":"Badminton"},{"TestID":"7","TestName":"Basketball"},{"TestID":"6","TestName":"Cricket"},{"TestID":"3","TestName":"Football"},{"TestID":"10","TestName":"Gymnastics/Aerobics"},{"TestID":"5","TestName":"Handball"},{"TestID":"11","TestName":"Health and Fitness"},{"TestID":"18","TestName":"Hockey"},{"TestID":"2","TestName":"Kabaddi"},{"TestID":"4","TestName":"Kho Kho"},{"TestID":"12","TestName":"Motor Skills"},{"TestID":"15","TestName":"Squash"},{"TestID":"16","TestName":"TableTennis"},{"TestID":"17","TestName":"Tennis"},{"TestID":"1","TestName":"Throw Ball"},{"TestID":"8","TestName":"Volleyball"}]
     */

    @SerializedName("IsSuccess")
    private String IsSuccess;
    @SerializedName("Message")
    private String Message;
    @SerializedName("Detail")
    private List<DetailBean> Detail;
    @SerializedName("TopSport")
    private List<TopSportBean> TopSport;

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

    public List<DetailBean> getDetail() {
        return Detail;
    }

    public void setDetail(List<DetailBean> Detail) {
        this.Detail = Detail;
    }

    public List<TopSportBean> getTopSport() {
        return TopSport;
    }

    public void setTopSport(List<TopSportBean> TopSport) {
        this.TopSport = TopSport;
    }

    public static class DetailBean {
        /**
         * Current_Roll_Num : 001541
         * StudentID : 21
         * student_name : Abhi Yadav
         */

        @SerializedName("Current_Roll_Num")
        private String CurrentRollNum;
        @SerializedName("StudentID")
        private String StudentID;
        @SerializedName("student_name")
        private String studentName;

        public String getCurrentRollNum() {
            return CurrentRollNum;
        }

        public void setCurrentRollNum(String CurrentRollNum) {
            this.CurrentRollNum = CurrentRollNum;
        }

        public String getStudentID() {
            return StudentID;
        }

        public void setStudentID(String StudentID) {
            this.StudentID = StudentID;
        }

        public String getStudentName() {
            return studentName;
        }

        public void setStudentName(String studentName) {
            this.studentName = studentName;
        }
    }

    public static class TopSportBean {
        /**
         * TestID : 9
         * TestName : Athletics
         */

        @SerializedName("TestID")
        private String TestID;
        @SerializedName("TestName")
        private String TestName;

        public String getTestID() {
            return TestID;
        }

        public void setTestID(String TestID) {
            this.TestID = TestID;
        }

        public String getTestName() {
            return TestName;
        }

        public void setTestName(String TestName) {
            this.TestName = TestName;
        }
    }
}
