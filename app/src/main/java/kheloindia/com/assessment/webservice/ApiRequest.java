package kheloindia.com.assessment.webservice;

/**
 * Created by CT13 on 2017-05-08.
 */

import java.util.HashMap;
import kheloindia.com.assessment.model.ActiveModel;
import kheloindia.com.assessment.model.ActivityStudentModel;
import kheloindia.com.assessment.model.AttendancetrackModel;
import kheloindia.com.assessment.model.CampTestModel;
import kheloindia.com.assessment.model.CreateProfileModel;
import kheloindia.com.assessment.model.DartBoardHeaderModel;
import kheloindia.com.assessment.model.DartBoardModel;
import kheloindia.com.assessment.model.ForgotPasswordModel;
import kheloindia.com.assessment.model.InsertActivityModel;
import kheloindia.com.assessment.model.MarkAttendanceModel;
import kheloindia.com.assessment.model.MarkAttendanceModel1;
import kheloindia.com.assessment.model.OptionModel;
import kheloindia.com.assessment.model.ProfileImageModel;
import kheloindia.com.assessment.model.ProfileModel;
import kheloindia.com.assessment.model.ReportModel;
import kheloindia.com.assessment.model.SkillReportModel;
import kheloindia.com.assessment.model.SkillTransactionModel;
import kheloindia.com.assessment.model.StudentModel;
import kheloindia.com.assessment.model.StudentUserModel;
import kheloindia.com.assessment.model.TestCategoryModel;
import kheloindia.com.assessment.model.TestTypeModel;
import kheloindia.com.assessment.model.TopSportsGetSkillModel;
import kheloindia.com.assessment.model.TopSportsUpdateModel;
import kheloindia.com.assessment.model.TopSportsUpdateSecondModel;
import kheloindia.com.assessment.model.TransactionModel;
import kheloindia.com.assessment.model.UpdateLocationModel;
import kheloindia.com.assessment.model.UserModel;
import kheloindia.com.assessment.model.ViewActivityModel;
import kheloindia.com.assessment.model.ViewAttendanceModel;
import kheloindia.com.assessment.util.AppConfig;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface ApiRequest {


    //login with student list
    @POST("getloginall/")
    Call<UserModel> getUserDetails(@Body HashMap<String, Object> hashMap);

    //Signup
    @POST("SelfRegistration/")
    Call<CreateProfileModel> createProfile(@Body HashMap<String, Object> hashMap);


    @POST("getlogin/")
    Call<StudentUserModel> getStudentUserDetails(@Body HashMap<String, Object> hashMap);

    @POST("getstudent/")
    Call<StudentModel> getStudentDetails(@Body HashMap<String, Object> hashMap);

    @POST("getreteststudent/")
    Call<StudentModel> getRetestStudentDetails(@Body HashMap<String, Object> hashMap);

    @POST("upload/")
    Call<ProfileImageModel> uploadProfilepic(@Body HashMap<String, Object> hashMap);


    @POST("gettestdetails/")
    Call<TestTypeModel> getTestDetails(@Body HashMap<String, Object> hashMap);

    @POST("gettestdetails/")
    Call<TestCategoryModel> getSubTestDetails(@Body HashMap<String, Object> hashMap);

    @POST("InsertTestResult/")
    Call<TransactionModel> submitTestDetail(@Body HashMap<String, Object> hashMap);

    @POST("InsertRetestTestResult/")
    Call<TransactionModel> submitRetestDetail(@Body HashMap<String, Object> hashMap);

    @POST("InsertSkillResult/")
    Call<SkillTransactionModel> submitSkillTestDetail(@Body HashMap<String, Object> hashMap);

    @POST("InsertSkillResult/")
    Call<SkillTransactionModel> submitSyncSkillTestDetail(@Body HashMap<String, Object> hashMap);

    @POST("forgotpwd/")
    Call<ForgotPasswordModel> submitForgotPassword(@Body HashMap<String, Object> hashMap);

    @POST("StudentReportCard/")
    Call<ReportModel> getReport(@Body HashMap<String, Object> hashMap);

    @POST("StudentSkillReportCard/")
    Call<SkillReportModel> getSkillReport(@Body HashMap<String, Object> hashMap);

    @POST("ViewAttendance/")
    Call<ViewAttendanceModel> getAttendanceDetails(@Body HashMap<String, Object> requestBody);

    @POST("ViewEditProfile/")
    Call<ProfileModel> getProfileDetails(@Body HashMap<String, Object> hashMap);

    @POST("ViewSkillTest/")
    Call<OptionModel> getOptionDetails(@Body HashMap<String, Object> hashMap);

    @POST("ViewDartBoardresult/")
    Call<DartBoardModel> getDartBoard(@Body HashMap<String, Object> hashMap);

    @POST("CampWiseTest/")
    Call<CampTestModel> getcamptestList(@Body HashMap<String, Object> hashMap);


    @POST("ViewActivityForAll/")
    Call<ViewActivityModel> getViewActivityDetails(@Body HashMap<String, Object> hashMap);

    @POST("ActivityStudentList/")
    Call<ActivityStudentModel> getActivityStudentDetails(@Body HashMap<String, Object> hashMap);

    @Multipart
    @POST("UploadPic/")
    Call<ProfileModel> uploadImage(@Part MultipartBody.Part image);

    @Multipart
    @POST("UploadPic/")
    Call<ProfileModel> postImage(@Part MultipartBody.Part image, @Part("name") RequestBody name);


    @POST("InsertActivity/")
    Call<InsertActivityModel> getInsertActivityDetails(@Body HashMap<String, Object> hashMap);


    @POST("InsertActivityfortopsports/")
    Call<InsertActivityModel> getInsertActivitySportsDetails(@Body HashMap<String, Object> hashMap);

    @POST("InsertMapAttendance/")
    Call<MarkAttendanceModel> MarkAttendance(@Body HashMap<String, Object> hashMap);

    @POST("InsertMapAttendanceFrom6Am/")
    Call<UpdateLocationModel> UpdateLocation(@Body HashMap<String, Object> hashMap);

    @POST("InsertMapAttendance/")
    Call<MarkAttendanceModel1> MarkAttendance1(@Body HashMap<String, Object> hashMap);

    @POST("ViewTrackAtendance/")
    Call<AttendancetrackModel> AttendanceTrack(@Body HashMap<String, Object> hashMap);

    @POST("Viewurl/")
    Call<ActiveModel> ViewUrl(@Body HashMap<String, Object> hashMap);

    @POST("TopSportsAssign/")
    Call<TopSportsUpdateModel> topSportsUpdate(@Body HashMap<String, Object> hashMap);

    @POST("TopSportsUpdateView/")
    Call<TopSportsGetSkillModel> topSportsGetSkill(@Body HashMap<String, Object> hashMap);

    @POST("TopSportsUpdate/")
    Call<TopSportsUpdateSecondModel> changeSport(@Body HashMap<String, Object> hashMap);

    @POST("dailyAttendance/")
    Call<DartBoardHeaderModel> getDartBoardHeader(@Body HashMap<String, Object> hashMap);

   /* @Multipart
    @POST("UploadPic/")
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part image, @Part("test_coordinator_id") RequestBody name);*/

    @Multipart
    @POST("UploadProfilePic/")
    Call<ProfileImageModel> postImage1(@Part MultipartBody.Part image,  @Part("ID") RequestBody name);

    @Multipart
    @POST("UploadSelfie/")
    Call<ProfileImageModel> postImageSelfie(@Part MultipartBody.Part image,  @Part("Trainer_ID") RequestBody name);

    @POST(AppConfig.GET_SCHOOLS_URL)
    Call<UserModel> getSchools(@Body HashMap<String, Object> hashMap);
}