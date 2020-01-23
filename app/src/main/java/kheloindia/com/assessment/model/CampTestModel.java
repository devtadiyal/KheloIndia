package kheloindia.com.assessment.model;

import java.util.List;

/**
 * Created by PC10 on 13-Oct-17.
 */

public class CampTestModel {


    /**
     * IsSuccess : true
     * Message : Success
     * Result : {"CampWiseTest":[{"Test_Type_ID":"1"},{"Test_Type_ID":"2"},{"Test_Type_ID":"3"},{"Test_Type_ID":"4"},{"Test_Type_ID":"5"},{"Test_Type_ID":"6"},{"Test_Type_ID":"7"},{"Test_Type_ID":"8"},{"Test_Type_ID":"9"},{"Test_Type_ID":"12"},{"Test_Type_ID":"20"},{"Test_Type_ID":"21"},{"Test_Type_ID":"22"},{"Test_Type_ID":"23"},{"Test_Type_ID":"24"},{"Test_Type_ID":"25"},{"Test_Type_ID":"33"},{"Test_Type_ID":"34"},{"Test_Type_ID":"10"},{"Test_Type_ID":"11"},{"Test_Type_ID":"13"},{"Test_Type_ID":"15"},{"Test_Type_ID":"16"},{"Test_Type_ID":"17"},{"Test_Type_ID":"18"},{"Test_Type_ID":"19"},{"Test_Type_ID":"26"},{"Test_Type_ID":"27"},{"Test_Type_ID":"28"},{"Test_Type_ID":"29"},{"Test_Type_ID":"30"},{"Test_Type_ID":"31"},{"Test_Type_ID":"32"}],"CoordinatorWiseTest":[{"Test_Type_ID":"1"},{"Test_Type_ID":"4"}]}
     */

    private String IsSuccess;
    private String Message;
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
        private List<CampWiseTestBean> CampWiseTest;
        private List<CoordinatorWiseTestBean> CoordinatorWiseTest;

        public List<CampWiseTestBean> getCampWiseTest() {
            return CampWiseTest;
        }

        public void setCampWiseTest(List<CampWiseTestBean> CampWiseTest) {
            this.CampWiseTest = CampWiseTest;
        }

        public List<CoordinatorWiseTestBean> getCoordinatorWiseTest() {
            return CoordinatorWiseTest;
        }

        public void setCoordinatorWiseTest(List<CoordinatorWiseTestBean> CoordinatorWiseTest) {
            this.CoordinatorWiseTest = CoordinatorWiseTest;
        }

        public static class CampWiseTestBean {
            /**
             * Test_Type_ID : 1
             */
            private String Class_ID;
            private String Test_Type_ID;

            public String getTest_Type_ID() {
                return Test_Type_ID;
            }

            public void setTest_Type_ID(String Test_Type_ID) {
                this.Test_Type_ID = Test_Type_ID;
            }

            public String getClass_ID() {
                return Class_ID;
            }

            public void setClass_ID(String class_ID) {
                Class_ID = class_ID;
            }
        }

        public static class CoordinatorWiseTestBean {
            /**
             * Test_Type_ID : 1
             */

            private String Test_Type_ID;

            public String getTest_Type_ID() {
                return Test_Type_ID;
            }

            public void setTest_Type_ID(String Test_Type_ID) {
                this.Test_Type_ID = Test_Type_ID;
            }
        }
    }
}
