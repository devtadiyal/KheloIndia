package kheloindia.com.assessment.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by CT13 on 2017-11-14.
 */

public class StudentUserModel {


    /**
     * IsSuccess : true
     * Message : success
     * Result : {"Email":null,"ImgPath":null,"Phone":null,"Schools":null,"Students":[{"Guardian_name":"Puneet  Mahajan","Image_Path":"SchoolImages/School_user_female.png","Phone":"","Student_name":"Drishti Mahajan","User_Login_ID":15004,"User_Login_Name":"4500104613","address":"","current_class":"I","date_Of_Birth":"6/15/2010 12:00:00 AM","gender":"Female"}],"Test_Coordinator_image":null,"User_id":0,"User_login":null,"User_name":null,"User_type_id":0,"gender":null}
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
        /**
         * Email : null
         * ImgPath : null
         * Phone : null
         * Schools : null
         * Students : [{"Guardian_name":"Puneet  Mahajan","Image_Path":"SchoolImages/School_user_female.png","Phone":"","Student_name":"Drishti Mahajan","User_Login_ID":15004,"User_Login_Name":"4500104613","address":"","current_class":"I","date_Of_Birth":"6/15/2010 12:00:00 AM","gender":"Female"}]
         * Test_Coordinator_image : null
         * User_id : 0
         * User_login : null
         * User_name : null
         * User_type_id : 0
         * gender : null
         */

        @SerializedName("Email")
        private Object Email;
        @SerializedName("ImgPath")
        private Object ImgPath;
        @SerializedName("Phone")
        private Object Phone;
        @SerializedName("Schools")
        private Object Schools;
        @SerializedName("Test_Coordinator_image")
        private Object TestCoordinatorImage;
        @SerializedName("User_id")
        private int UserId;
        @SerializedName("User_login")
        private Object UserLogin;
        @SerializedName("User_name")
        private Object UserName;
        @SerializedName("User_type_id")
        private int UserTypeId;
        @SerializedName("gender")
        private Object gender;
        @SerializedName("Students")
        private List<StudentsBean> Students;

        public Object getEmail() {
            return Email;
        }

        public void setEmail(Object Email) {
            this.Email = Email;
        }

        public Object getImgPath() {
            return ImgPath;
        }

        public void setImgPath(Object ImgPath) {
            this.ImgPath = ImgPath;
        }

        public Object getPhone() {
            return Phone;
        }

        public void setPhone(Object Phone) {
            this.Phone = Phone;
        }

        public Object getSchools() {
            return Schools;
        }

        public void setSchools(Object Schools) {
            this.Schools = Schools;
        }

        public Object getTestCoordinatorImage() {
            return TestCoordinatorImage;
        }

        public void setTestCoordinatorImage(Object TestCoordinatorImage) {
            this.TestCoordinatorImage = TestCoordinatorImage;
        }

        public int getUserId() {
            return UserId;
        }

        public void setUserId(int UserId) {
            this.UserId = UserId;
        }

        public Object getUserLogin() {
            return UserLogin;
        }

        public void setUserLogin(Object UserLogin) {
            this.UserLogin = UserLogin;
        }

        public Object getUserName() {
            return UserName;
        }

        public void setUserName(Object UserName) {
            this.UserName = UserName;
        }

        public int getUserTypeId() {
            return UserTypeId;
        }

        public void setUserTypeId(int UserTypeId) {
            this.UserTypeId = UserTypeId;
        }

        public Object getGender() {
            return gender;
        }

        public void setGender(Object gender) {
            this.gender = gender;
        }

        public List<StudentsBean> getStudents() {
            return Students;
        }

        public void setStudents(List<StudentsBean> Students) {
            this.Students = Students;
        }

        public static class StudentsBean {
            /**
             * Guardian_name : Puneet  Mahajan
             * Image_Path : SchoolImages/School_user_female.png
             * Phone :
             * Student_name : Drishti Mahajan
             * User_Login_ID : 15004
             * User_Login_Name : 4500104613
             * address :
             * current_class : I
             * date_Of_Birth : 6/15/2010 12:00:00 AM
             * gender : Female
             */

            @SerializedName("Guardian_name")
            private String GuardianName;
            @SerializedName("Image_Path")
            private String ImagePath;
            @SerializedName("Phone")
            private String Phone;
            @SerializedName("Student_name")
            private String StudentName;
            @SerializedName("User_Login_ID")
            private int UserLoginID;
            @SerializedName("User_Login_Name")
            private String UserLoginName;
            @SerializedName("address")
            private String address;
            @SerializedName("current_class")
            private String currentClass;
            @SerializedName("date_Of_Birth")
            private String dateOfBirth;
            @SerializedName("gender")
            private String gender;

            public String getGuardianName() {
                return GuardianName;
            }

            public void setGuardianName(String GuardianName) {
                this.GuardianName = GuardianName;
            }

            public String getImagePath() {
                return ImagePath;
            }

            public void setImagePath(String ImagePath) {
                this.ImagePath = ImagePath;
            }

            public String getPhone() {
                return Phone;
            }

            public void setPhone(String Phone) {
                this.Phone = Phone;
            }

            public String getStudentName() {
                return StudentName;
            }

            public void setStudentName(String StudentName) {
                this.StudentName = StudentName;
            }

            public int getUserLoginID() {
                return UserLoginID;
            }

            public void setUserLoginID(int UserLoginID) {
                this.UserLoginID = UserLoginID;
            }

            public String getUserLoginName() {
                return UserLoginName;
            }

            public void setUserLoginName(String UserLoginName) {
                this.UserLoginName = UserLoginName;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getCurrentClass() {
                return currentClass;
            }

            public void setCurrentClass(String currentClass) {
                this.currentClass = currentClass;
            }

            public String getDateOfBirth() {
                return dateOfBirth;
            }

            public void setDateOfBirth(String dateOfBirth) {
                this.dateOfBirth = dateOfBirth;
            }

            public String getGender() {
                return gender;
            }

            public void setGender(String gender) {
                this.gender = gender;
            }
        }
    }
}
