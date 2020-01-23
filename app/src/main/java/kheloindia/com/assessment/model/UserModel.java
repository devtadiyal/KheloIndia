
package kheloindia.com.assessment.model;


import com.google.gson.annotations.SerializedName;

import java.util.List;

import kheloindia.com.assessment.util.AppConfig;

public class UserModel {

    @SerializedName("IsSuccess")
    private String mIsSuccess;
    @SerializedName("Message")
    private String mMessage;
    @SerializedName("Result")
    private Result mResult;

    public String getIsSuccess() {
        return mIsSuccess;
    }

    public void setIsSuccess(String isSuccess) {
        mIsSuccess = isSuccess;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public Result getResult() {
        return mResult;
    }

    public void setResult(Result result) {
        mResult = result;
    }


    public static class Result {

        @SerializedName("Aadhar_No")
        private String mAadharNo;
        @SerializedName("Alternative_Email")
        private String mAlternativeEmail;
        @SerializedName("Designation")
        private String mDesignation;
        @SerializedName("Email")
        private String mEmail;
        @SerializedName("gender")
        private String mGender;
        @SerializedName("imgpath")
        private String mImgPath;
        @SerializedName("Phone")
        private String mPhone;
        @SerializedName("Qualification")
        private String mQualification;
        @SerializedName("School_Coordinator_ID")
        private String mSchoolCoordinatorID;
        @SerializedName("School_Coordinator_Name")
        private String mSchoolCoordinatorName;
        @SerializedName("School_ID")
        private Object mSchoolID;
        @SerializedName("Schools")
        private List<School> mSchools;
        @SerializedName("Students")
        private List<Object> mStudents;
        @SerializedName("Test_Coordinator_image")
        private String mTestCoordinatorImage;
        @SerializedName("Trouser_Size")
        private String mTrouserSize;
        @SerializedName("Tshirt_Size")
        private String mTshirtSize;
        @SerializedName("User_id")
        private Long mUserId;
        @SerializedName("User_login")
        private String mUserLogin;
        @SerializedName("User_name")
        private String mUserName;
        @SerializedName("User_type_id")
        private Long mUserTypeId;
        @SerializedName("Whatsup_No")
        private String mWhatsupNo;
        @SerializedName(AppConfig.STATE_ID)
        private int stateId;
        @SerializedName(AppConfig.STATE_NAME)
        private String stateName;
        @SerializedName(AppConfig.CITY_NAME)
        private String cityName;
        @SerializedName(AppConfig.ADDRESS)
        private String address;
        @SerializedName(AppConfig.DISTRICT_NAME)
        private String districtName;
        @SerializedName(AppConfig.BLOCK_NAME)
        private String blockName;


      /*  @SerializedName("ImgPath")
        private String ImgPath;*/



       /* public String getImgpath() {
            return ImgPath;
        }
        public void setImgpath(String imgpath) {
            this.ImgPath = imgpath;
        }*/


        public int getStateId() {
            return stateId;
        }

        public String getStateName() {
            return stateName;
        }

        public String getCityName() {
            return cityName;
        }

        public String getAddress() {
            return address;
        }

        public String getDistrictName() {
            return districtName;
        }

        public String getBlockName() {
            return blockName;
        }

        public String getAadharNo() {
            return mAadharNo;
        }

        public void setAadharNo(String aadharNo) {
            mAadharNo = aadharNo;
        }

        public String getAlternativeEmail() {
            return mAlternativeEmail;
        }

        public void setAlternativeEmail(String alternativeEmail) {
            mAlternativeEmail = alternativeEmail;
        }

        public String getDesignation() {
            return mDesignation;
        }

        public void setDesignation(String designation) {
            mDesignation = designation;
        }

        public String getEmail() {
            return mEmail;
        }

        public void setEmail(String email) {
            mEmail = email;
        }

        public String getGender() {
            return mGender;
        }

        public void setGender(String gender) {
            mGender = gender;
        }

        public String getImgPath() {
            return mImgPath;
        }

        public void setImgPath(String imgPath) {
            mImgPath = imgPath;
        }

        public String getPhone() {
            return mPhone;
        }

        public void setPhone(String phone) {
            mPhone = phone;
        }

        public String getQualification() {
            return mQualification;
        }

        public void setQualification(String qualification) {
            mQualification = qualification;
        }

        public String getSchoolCoordinatorID() {
            return mSchoolCoordinatorID;
        }

        public void setSchoolCoordinatorID(String schoolCoordinatorID) {
            mSchoolCoordinatorID = schoolCoordinatorID;
        }

        public String getSchoolCoordinatorName() {
            return mSchoolCoordinatorName;
        }

        public void setSchoolCoordinatorName(String schoolCoordinatorName) {
            mSchoolCoordinatorName = schoolCoordinatorName;
        }

        public Object getSchoolID() {
            return mSchoolID;
        }

        public void setSchoolID(Object schoolID) {
            mSchoolID = schoolID;
        }

        public List<School> getSchools() {
            return mSchools;
        }

        public void setSchools(List<School> schools) {
            mSchools = schools;
        }

        public List<Object> getStudents() {
            return mStudents;
        }

        public void setStudents(List<Object> students) {
            mStudents = students;
        }

        public String getTestCoordinatorImage() {
            return mTestCoordinatorImage;
        }

        public void setTestCoordinatorImage(String testCoordinatorImage) {
            mTestCoordinatorImage = testCoordinatorImage;
        }

        public String getTrouserSize() {
            return mTrouserSize;
        }

        public void setTrouserSize(String trouserSize) {
            mTrouserSize = trouserSize;
        }

        public String getTshirtSize() {
            return mTshirtSize;
        }

        public void setTshirtSize(String tshirtSize) {
            mTshirtSize = tshirtSize;
        }

        public Long getUserId() {
            return mUserId;
        }

        public void setUserId(Long userId) {
            mUserId = userId;
        }

        public String getUserLogin() {
            return mUserLogin;
        }

        public void setUserLogin(String userLogin) {
            mUserLogin = userLogin;
        }

        public String getUserName() {
            return mUserName;
        }

        public void setUserName(String userName) {
            mUserName = userName;
        }

        public Long getUserTypeId() {
            return mUserTypeId;
        }

        public void setUserTypeId(Long userTypeId) {
            mUserTypeId = userTypeId;
        }

        public String getWhatsupNo() {
            return mWhatsupNo;
        }

        public void setWhatsupNo(String whatsupNo) {
            mWhatsupNo = whatsupNo;
        }

        public static  class School {

            @SerializedName("Created_On")
            private String mCreatedOn;
            @SerializedName("Last_Modified_On")
            private String mLastModifiedOn;
            @SerializedName("Latitude")
            private String mLatitude;
            @SerializedName("Longitude")
            private String mLongitude;
            @SerializedName("School_end_time")
            private String mSchoolEndTime;
            @SerializedName("School_Id")
            private int mSchoolId;
            @SerializedName("School_Image_Name")
            private String mSchoolImageName;
            @SerializedName("School_Image_Path")
            private String mSchoolImagePath;
            @SerializedName("school_start_time")
            private String mSchoolStartTime;
            @SerializedName("Schoolname")
            private String mSchoolname;
            @SerializedName("SeniorsStartingFrom")
            private String mSeniorsStartingFrom;
            @SerializedName("Office")
            private String office;
            @SerializedName(AppConfig.IS_ATTACHED)
            private int isAttached;
            @SerializedName(AppConfig.IS_RETEST_ALLOWED)
            private String isRetestAllowed;


            public String getCreatedOn() {
                return mCreatedOn;
            }

            public void setCreatedOn(String createdOn) {
                mCreatedOn = createdOn;
            }

            public String getLastModifiedOn() {
                return mLastModifiedOn;
            }

            public void setLastModifiedOn(String lastModifiedOn) {
                mLastModifiedOn = lastModifiedOn;
            }

            public String getLatitude() {
                return mLatitude;
            }

            public void setLatitude(String latitude) {
                mLatitude = latitude;
            }

            public String getLongitude() {
                return mLongitude;
            }

            public void setLongitude(String longitude) {
                mLongitude = longitude;
            }

            public String getSchoolEndTime() {
                return mSchoolEndTime;
            }

            public void setSchoolEndTime(String schoolEndTime) {
                mSchoolEndTime = schoolEndTime;
            }

            public int getSchoolId() {
                return mSchoolId;
            }

            public void setSchoolId(int schoolId) {
                mSchoolId = schoolId;
            }

            public String getSchoolImageName() {
                return mSchoolImageName;
            }

            public void setSchoolImageName(String schoolImageName) {
                mSchoolImageName = schoolImageName;
            }

            public String getSchoolImagePath() {
                return mSchoolImagePath;
            }

            public void setSchoolImagePath(String schoolImagePath) {
                mSchoolImagePath = schoolImagePath;
            }

            public String getSchoolStartTime() {
                return mSchoolStartTime;
            }

            public void setSchoolStartTime(String schoolStartTime) {
                mSchoolStartTime = schoolStartTime;
            }

            public String getSchoolname() {
                return mSchoolname;
            }

            public void setSchoolname(String schoolname) {
                mSchoolname = schoolname;
            }

            public String getSeniorsStartingFrom() {
                return mSeniorsStartingFrom;
            }

            public void setSeniorsStartingFrom(String seniorsStartingFrom) {
                mSeniorsStartingFrom = seniorsStartingFrom;
            }


            public String getOffice() {
                return office;
            }

            public void setOffice(String office) {
                this.office = office;
            }

            public int getIsAttached() {
                return isAttached;
            }

            public void setIsAttached(int isAttached) {
                this.isAttached = isAttached;
            }

            public String getIsRetestAllowed() {
                return isRetestAllowed;
            }

            public void setIsRetestAllowed(String isRetestAllowed) {
                this.isRetestAllowed = isRetestAllowed;
            }
        }
    }

}
