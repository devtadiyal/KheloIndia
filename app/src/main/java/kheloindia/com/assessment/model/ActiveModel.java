package kheloindia.com.assessment.model;

import java.util.List;

/**
 * Created by PC10 on 15-Mar-18.
 */

public class ActiveModel {
    /**
     * IsSuccess : True
     * Message : Success
     * MyUrl : [{"Icon_image_name":"insights.png","Icon_path":"\\images\\","Modile_icon_Titel":"","Parent_id":"0","screen_id":"1","screen_name":"Insight","web_url":"http://mydiary.fitness365.me/Admin.aspx"},{"Icon_image_name":"","Icon_path":"","Modile_icon_Titel":"","Parent_id":"0","screen_id":"2","screen_name":"Shape365","web_url":"http://mydiary.fitness365.me/Admin.aspx"},{"Icon_image_name":"","Icon_path":"","Modile_icon_Titel":"","Parent_id":"0","screen_id":"4","screen_name":"Top Sports","web_url":"http://mydiary.fitness365.me/Admin.aspx"},{"Icon_image_name":"","Icon_path":"","Modile_icon_Titel":"","Parent_id":"0","screen_id":"6","screen_name":"Schools and Camps","web_url":"http://mydiary.fitness365.me/Admin.aspx"},{"Icon_image_name":"","Icon_path":"","Modile_icon_Titel":"","Parent_id":"0","screen_id":"8","screen_name":"Utilities","web_url":"http://mydiary.fitness365.me/Admin.aspx"},{"Icon_image_name":"","Icon_path":"","Modile_icon_Titel":"","Parent_id":"1","screen_id":"12","screen_name":"School Performance","web_url":"http://mydiary.fitness365.me/Fitness365SchoolDashboard.aspx"},{"Icon_image_name":"","Icon_path":"","Modile_icon_Titel":"","Parent_id":"1","screen_id":"13","screen_name":"Fitness Dashboard","web_url":""},{"Icon_image_name":"","Icon_path":"","Modile_icon_Titel":"","Parent_id":"1","screen_id":"14","screen_name":"Class-wise Performance","web_url":"http://mydiary.fitness365.me/ClasswiseDashboard.aspx"},{"Icon_image_name":"","Icon_path":"","Modile_icon_Titel":"","Parent_id":"1","screen_id":"15","screen_name":"Latest Students Reports","web_url":"http://mydiary.fitness365.me/SchoolStudentReport.aspx"},{"Icon_image_name":"","Icon_path":"","Modile_icon_Titel":"","Parent_id":"1","screen_id":"16","screen_name":"Top Performers","web_url":"http://mydiary.fitness365.me/Toppers.aspx"},{"Icon_image_name":"","Icon_path":"","Modile_icon_Titel":"","Parent_id":"1","screen_id":"17","screen_name":"Consistent Performers","web_url":"http://mydiary.fitness365.me/Topconsisientperformers.aspx"},{"Icon_image_name":"","Icon_path":"","Modile_icon_Titel":"","Parent_id":"2","screen_id":"19","screen_name":"SHAPE365 Activities","web_url":"http://mydiary.fitness365.me/Shape365.aspx"},{"Icon_image_name":"","Icon_path":"","Modile_icon_Titel":"","Parent_id":"6","screen_id":"32","screen_name":"Student","web_url":""},{"Icon_image_name":"","Icon_path":"","Modile_icon_Titel":"","Parent_id":"32","screen_id":"36","screen_name":"Student Login/Passwords","web_url":"http://mydiary.fitness365.me/Student_Login_Details.aspx"},{"Icon_image_name":"","Icon_path":"","Modile_icon_Titel":"","Parent_id":"6","screen_id":"37","screen_name":"Manage School Programs","web_url":""},{"Icon_image_name":"","Icon_path":"","Modile_icon_Titel":"","Parent_id":"37","screen_id":"40","screen_name":"Upload Image","web_url":"http://mydiary.fitness365.me/UploadImage.aspx"},{"Icon_image_name":"","Icon_path":"","Modile_icon_Titel":"","Parent_id":"8","screen_id":"46","screen_name":"Utilities for Parents ","web_url":"http://mydiary.fitness365.me/DisplayWidgetsTitles_Parents.aspx"},{"Icon_image_name":"","Icon_path":"","Modile_icon_Titel":"","Parent_id":"8","screen_id":"47","screen_name":"Utilities for Children","web_url":"http://mydiary.fitness365.me/DisplayWidgetsTitles_Students.aspx"}]
     */

    private String IsSuccess;
    private String Message;
    private List<MyUrlBean> MyUrl;

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

    public List<MyUrlBean> getMyUrl() {
        return MyUrl;
    }

    public void setMyUrl(List<MyUrlBean> MyUrl) {
        this.MyUrl = MyUrl;
    }

    public static class MyUrlBean {
        /**
         * Icon_image_name : insights.png
         * Icon_path : \images\
         * Modile_icon_Titel :
         * Parent_id : 0
         * screen_id : 1
         * screen_name : Insight
         * web_url : http://mydiary.fitness365.me/Admin.aspx
         */

        private String Icon_image_name;
        private String Icon_path;
        private String Modile_icon_Titel;
        private String Parent_id;
        private String screen_id;
        private String screen_name;
        private String web_url;

        public String getIcon_image_name() {
            return Icon_image_name;
        }

        public void setIcon_image_name(String Icon_image_name) {
            this.Icon_image_name = Icon_image_name;
        }

        public String getIcon_path() {
            return Icon_path;
        }

        public void setIcon_path(String Icon_path) {
            this.Icon_path = Icon_path;
        }

        public String getModile_icon_Titel() {
            return Modile_icon_Titel;
        }

        public void setModile_icon_Titel(String Modile_icon_Titel) {
            this.Modile_icon_Titel = Modile_icon_Titel;
        }

        public String getParent_id() {
            return Parent_id;
        }

        public void setParent_id(String Parent_id) {
            this.Parent_id = Parent_id;
        }

        public String getScreen_id() {
            return screen_id;
        }

        public void setScreen_id(String screen_id) {
            this.screen_id = screen_id;
        }

        public String getScreen_name() {
            return screen_name;
        }

        public void setScreen_name(String screen_name) {
            this.screen_name = screen_name;
        }

        public String getWeb_url() {
            return web_url;
        }

        public void setWeb_url(String web_url) {
            this.web_url = web_url;
        }
    }




   /* *//**
     * IsSuccess : True
     * Message : Success
     * MyUrl : [{"Parent_id":"0","screen_id":"1","screen_name":"Insight","web_url":"http://mydiary.fitness365.me/Admin.aspx"},{"Parent_id":"0","screen_id":"2","screen_name":"Shape365","web_url":"http://mydiary.fitness365.me/Admin.aspx"},{"Parent_id":"0","screen_id":"3","screen_name":"As Program","web_url":"http://mydiary.fitness365.me/Admin.aspx"},{"Parent_id":"0","screen_id":"4","screen_name":"Top Sports","web_url":"http://mydiary.fitness365.me/Admin.aspx"},{"Parent_id":"0","screen_id":"5","screen_name":"Test","web_url":"http://mydiary.fitness365.me/Admin.aspx"},{"Parent_id":"0","screen_id":"6","screen_name":"Schools and Camps","web_url":"http://mydiary.fitness365.me/Admin.aspx"},{"Parent_id":"0","screen_id":"7","screen_name":"Reports","web_url":"http://mydiary.fitness365.me/Admin.aspx"},{"Parent_id":"0","screen_id":"8","screen_name":"Utilities","web_url":"http://mydiary.fitness365.me/Admin.aspx"},{"Parent_id":"1","screen_id":"9","screen_name":"Partner Schools","web_url":"http://mydiary.fitness365.me/Trainer_Schools.aspx"},{"Parent_id":"1","screen_id":"10","screen_name":"Overall Performance","web_url":"http://mydiary.fitness365.me/Fitness365OverallDashboard.aspx"},{"Parent_id":"1","screen_id":"11","screen_name":"Age-wise Performance","web_url":"http://mydiary.fitness365.me/Fitness365_Agewise_AdminDashboard.aspx"},{"Parent_id":"1","screen_id":"12","screen_name":"School Performance","web_url":"http://mydiary.fitness365.me/Fitness365SchoolDashboard.aspx"},{"Parent_id":"1","screen_id":"13","screen_name":"Fitness Dashboard","web_url":""},{"Parent_id":"1","screen_id":"14","screen_name":"Class-wise Performance","web_url":"http://mydiary.fitness365.me/ClasswiseDashboard.aspx"},{"Parent_id":"1","screen_id":"15","screen_name":"Latest Students Reports","web_url":"http://mydiary.fitness365.me/SchoolStudentReport.aspx"},{"Parent_id":"1","screen_id":"16","screen_name":"Top Performers","web_url":"http://mydiary.fitness365.me/Toppers.aspx"},{"Parent_id":"1","screen_id":"17","screen_name":"Consistent Performers","web_url":"http://mydiary.fitness365.me/Topconsisientperformers.aspx"},{"Parent_id":"2","screen_id":"18","screen_name":"Curriculum Break-up","web_url":"http://mydiary.fitness365.me/LessonPlanBreakupReport.aspx"},{"Parent_id":"2","screen_id":"19","screen_name":"SHAPE365 Activities","web_url":"http://mydiary.fitness365.me/Shape365.aspx"},{"Parent_id":"2","screen_id":"20","screen_name":"Lesson Planning","web_url":"http://mydiary.fitness365.me/PEP365LessonPlanning.aspx"},{"Parent_id":"3","screen_id":"21","screen_name":"Curriculum","web_url":"http://mydiary.fitness365.me/ASCurriculum.aspx"},{"Parent_id":"3","screen_id":"22","screen_name":"AS Lesson Planning","web_url":"http://mydiary.fitness365.me/ASLessonPlanning.aspx"},{"Parent_id":"3","screen_id":"23","screen_name":"Lesson Index","web_url":""},{"Parent_id":"5","screen_id":"24","screen_name":"Add New Test","web_url":"http://mydiary.fitness365.me/AddNewTest.aspx"},{"Parent_id":"6","screen_id":"25","screen_name":"Manage Master","web_url":""},{"Parent_id":"25","screen_id":"26","screen_name":"Add Zone","web_url":""},{"Parent_id":"25","screen_id":"27","screen_name":"Add Region","web_url":""},{"Parent_id":"25","screen_id":"28","screen_name":"Add City","web_url":""},{"Parent_id":"6","screen_id":"29","screen_name":"Schools","web_url":""},{"Parent_id":"29","screen_id":"30","screen_name":"Add School","web_url":"http://mydiary.fitness365.me/AddSchool.aspx"},{"Parent_id":"29","screen_id":"31","screen_name":"Manage School","web_url":"http://mydiary.fitness365.me/ManageSchool.aspx"},{"Parent_id":"6","screen_id":"32","screen_name":"Student","web_url":""},{"Parent_id":"32","screen_id":"33","screen_name":"Add Students","web_url":"http://mydiary.fitness365.me/AddStudent.aspx"},{"Parent_id":"32","screen_id":"34","screen_name":"Manage Students","web_url":"http://mydiary.fitness365.me/EditStudent.aspx"},{"Parent_id":"32","screen_id":"35","screen_name":"Student Data Upload","web_url":"http://mydiary.fitness365.me/UploadStudentData.aspx"},{"Parent_id":"32","screen_id":"36","screen_name":"Student Login/Passwords","web_url":"http://mydiary.fitness365.me/Student_Login_Details.aspx"},{"Parent_id":"6","screen_id":"37","screen_name":"Manage School Programs","web_url":""},{"Parent_id":"37","screen_id":"38","screen_name":"Manage Terms","web_url":"http://mydiary.fitness365.me/AddCamp.aspx"},{"Parent_id":"37","screen_id":"39","screen_name":"Test Data Upload","web_url":"http://mydiary.fitness365.me/ExcelUpload.aspx"},{"Parent_id":"37","screen_id":"40","screen_name":"Upload Image","web_url":"http://mydiary.fitness365.me/UploadImage.aspx"},{"Parent_id":"37","screen_id":"41","screen_name":"Percentile Engine","web_url":"http://mydiary.fitness365.me/UploadImage.aspx#"},{"Parent_id":"7","screen_id":"42","screen_name":"Camp Report","web_url":"http://mydiary.fitness365.me/CampReport.aspx"},{"Parent_id":"8","screen_id":"43","screen_name":"Manage School Programs","web_url":"http://mydiary.fitness365.me/SchoolClassMap.aspx"},{"Parent_id":"8","screen_id":"44","screen_name":"Add PE Trainers","web_url":"http://mydiary.fitness365.me/AddTestCoordinator.aspx"},{"Parent_id":"8","screen_id":"45","screen_name":"Manage PE Trainers","web_url":"http://mydiary.fitness365.me/ManageTestCoordinators.aspx"},{"Parent_id":"8","screen_id":"46","screen_name":"Utilities for Parents ","web_url":"http://mydiary.fitness365.me/DisplayWidgetsTitles_Parents.aspx"},{"Parent_id":"8","screen_id":"47","screen_name":"Utilities for Children","web_url":"http://mydiary.fitness365.me/DisplayWidgetsTitles_Students.aspx"}]
     *//*

    private String IsSuccess;
    private String Message;
    private List<MyUrlBean> MyUrl;

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

    public List<MyUrlBean> getMyUrl() {
        return MyUrl;
    }

    public void setMyUrl(List<MyUrlBean> MyUrl) {
        this.MyUrl = MyUrl;
    }

    public static class MyUrlBean {
        *//**
         * Parent_id : 0
         * screen_id : 1
         * screen_name : Insight
         * web_url : http://mydiary.fitness365.me/Admin.aspx
         *//*

        private String Parent_id;
        private String screen_id;
        private String screen_name;
        private String web_url;

        public String getParent_id() {
            return Parent_id;
        }

        public void setParent_id(String Parent_id) {
            this.Parent_id = Parent_id;
        }

        public String getScreen_id() {
            return screen_id;
        }

        public void setScreen_id(String screen_id) {
            this.screen_id = screen_id;
        }

        public String getScreen_name() {
            return screen_name;
        }

        public void setScreen_name(String screen_name) {
            this.screen_name = screen_name;
        }

        public String getWeb_url() {
            return web_url;
        }

        public void setWeb_url(String web_url) {
            this.web_url = web_url;
        }
    }*/
}
