package kheloindia.com.assessment.model;

import java.util.List;

/**
 * Created by PC10 on 06/06/2017.
 */

public class TestCategoryModel {
    /**
     * IsSuccess : true
     * Message : success
     * Result : {"Test":[{"IsActive":"True","Test_ID":6,"Test_Image":"hand_e_coor_i.png","Test_Img_path":"\\images\\","Test_Name":"Hand Eye Coordination"}],"TestSubtypes":[{"Score_Criteria":"More_is_better","Score_Measurement":"Fixed_Score","Score_Unit":"number","Test_Category_ID":6,"Test_Description":"Hand-eye Co-ordination1","Test_Name":"Catch","Test_type_id":10},{"Score_Criteria":"More_is_better","Score_Measurement":"Fixed_Score","Score_Unit":"number","Test_Category_ID":6,"Test_Description":"Hand-eye Co-ordination","Test_Name":"Throw","Test_type_id":11}]}
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
        private List<TestBean> Test;
        private List<TestSubtypesBean> TestSubtypes;

        public List<TestBean> getTest() {
            return Test;
        }

        public void setTest(List<TestBean> Test) {
            this.Test = Test;
        }

        public List<TestSubtypesBean> getTestSubtypes() {
            return TestSubtypes;
        }

        public void setTestSubtypes(List<TestSubtypesBean> TestSubtypes) {
            this.TestSubtypes = TestSubtypes;
        }

        public static class TestBean {
            /**
             * IsActive : True
             * Test_ID : 6
             * Test_Image : hand_e_coor_i.png
             * Test_Img_path : \images\
             * Test_Name : Hand Eye Coordination
             */

            private String IsActive;
            private int Test_ID;
            private String Test_Image;
            private String Test_Img_path;
            private String Test_Name;

            public String getIsActive() {
                return IsActive;
            }

            public void setIsActive(String IsActive) {
                this.IsActive = IsActive;
            }

            public int getTest_ID() {
                return Test_ID;
            }

            public void setTest_ID(int Test_ID) {
                this.Test_ID = Test_ID;
            }

            public String getTest_Image() {
                return Test_Image;
            }

            public void setTest_Image(String Test_Image) {
                this.Test_Image = Test_Image;
            }

            public String getTest_Img_path() {
                return Test_Img_path;
            }

            public void setTest_Img_path(String Test_Img_path) {
                this.Test_Img_path = Test_Img_path;
            }

            public String getTest_Name() {
                return Test_Name;
            }

            public void setTest_Name(String Test_Name) {
                this.Test_Name = Test_Name;
            }
        }

        public static class TestSubtypesBean {
            /**
             * Score_Criteria : More_is_better
             * Score_Measurement : Fixed_Score
             * Score_Unit : number
             * Test_Category_ID : 6
             * Test_Description : Hand-eye Co-ordination1
             * Test_Name : Catch
             * Test_type_id : 10
             * Purpose
             * Equipment_Required
             * Administrative_Suggestions
             * scoring
             */

            private String Score_Criteria;
            private String Score_Measurement;
            private String Score_Unit;
            private int Test_Category_ID;
            private String Test_Description;
            private String Test_Name;
            private int Test_type_id;
            private String Purpose;
            private String Equipment_Required;
            private String Administrative_Suggestions;
            private String scoring;
            private String video_url;

            public String getVideo_url() {
                return video_url;
            }

            public void setVideo_url(String video_url) {
                this.video_url = video_url;
            }

            public String getPurpose() {
                return Purpose;
            }

            public void setPurpose(String purpose) {
                Purpose = purpose;
            }

            public String getEquipment_Required() {
                return Equipment_Required;
            }

            public void setEquipment_Required(String equipment_Required) {
                Equipment_Required = equipment_Required;
            }

            public String getAdministrative_Suggestions() {
                return Administrative_Suggestions;
            }

            public void setAdministrative_Suggestions(String administrative_Suggestions) {
                Administrative_Suggestions = administrative_Suggestions;
            }

            public String getScoring() {
                return scoring;
            }

            public void setScoring(String scoring) {
                this.scoring = scoring;
            }

            public String getScore_Criteria() {
                return Score_Criteria;
            }

            public void setScore_Criteria(String Score_Criteria) {
                this.Score_Criteria = Score_Criteria;
            }

            public String getScore_Measurement() {
                return Score_Measurement;
            }

            public void setScore_Measurement(String Score_Measurement) {
                this.Score_Measurement = Score_Measurement;
            }



            public String getScore_Unit() {
                return Score_Unit;
            }

            public void setScore_Unit(String Score_Unit) {
                this.Score_Unit = Score_Unit;
            }

            public int getTest_Category_ID() {
                return Test_Category_ID;
            }

            public void setTest_Category_ID(int Test_Category_ID) {
                this.Test_Category_ID = Test_Category_ID;
            }

            public String getTest_Description() {
                return Test_Description;
            }

            public void setTest_Description(String Test_Description) {
                this.Test_Description = Test_Description;
            }

            public String getTest_Name() {
                return Test_Name;
            }

            public void setTest_Name(String Test_Name) {
                this.Test_Name = Test_Name;
            }

            public int getTest_type_id() {
                return Test_type_id;
            }

            public void setTest_type_id(int Test_type_id) {
                this.Test_type_id = Test_type_id;
            }
        }
    }
}
