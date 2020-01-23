package kheloindia.com.assessment.util;

import java.security.PublicKey;

import kheloindia.com.assessment.functions.Constant;

public class AppConfig {

    // COMMON STRINGS
    public static final String YOUTUBE_API_KEY = "AIzaSyABk_T7991L2avBm_RgYOkpHTFUEhPMZMg";
    public static final String TAG = "check";
    public static final String IS_APP_UPDATED = "isAppUpdated";
    public static boolean IS_STATE_UPDATION_DIALOG_SHOWING = false;
    public static boolean IS_SCHOOL_DEACTIVATED_DIALOG_SHOWING = false;


    // FONT PATH STRINGS
    public static final String FONT_BASE_PATH = "fonts/";
    public static final String BARLOW_LIGHT_PATH = FONT_BASE_PATH + "Barlow-Light.ttf";
    public static final String BARLOW_MEDIUM_PATH = FONT_BASE_PATH + "Barlow-Medium.ttf";
    public static final String BARLOW_SEMIBOLD_PATH = FONT_BASE_PATH + "Barlow-SemiBold.ttf";
    public static final String BARLOW_CONDENSED_PATH = FONT_BASE_PATH + "BarlowCondensed-ExtraBoldItalic.otf";

    // LOGIN STRINGS
    public static final String TEST_COORDINATOR_NAME = "test_coordinator_name";
    public static final String TEST_COORDINATOR_PASSWORD = "test_coordinator_password";
    public static final String IS_STUDENT_DETAILS_GET = "isStudentDetailGet";
    public static final String USER_ID = "userid";
    public static final String TEST_COORDINATOR_ID = "test_coordinator_id";
    public static final String USER_NAME = "username";

    // SCHOOL STRings
    public static final String SCHOOL_ID = "school_id";
    public static final String SCHOOL_NAME = "school_name";
    public static final String IS_ATTACHED = "IsActive";
    public static final String CURRENT_SCHOOL_ID = "current_school_id";


    // COORDINATOR STRINGS
    public static final String ADDRESS = "Address";
    public static final String IS_TOT_ATTENDED = "isattended";
    public static final String TOT_CODE = "totcode";


    // STUDENTS STRINGS
    public static final String STUDENT_ID = "student_id";
    public static final String TRAINER_ID = "trainer_id";
    public static final String CAMP_ID = "camp_id";
    public static final String CAMP_NAME = "CampName";
    public static final String security_key = "IP84UTvzJKds1Jomx8gIbTXcEEJSUilGqpxCcmnx";
    public static final String NAME_STRING = "name";
    public static final String CLASS_ID = "class_id";
    public static final String IS_RETEST_ALLOWED = "IsRetestAllowed";
    public static final String FOR_RETEST = "forRetest";

    // STATE STRINGS
    public static final String STATE_ID = "StateId";
    public static final String STATE_NAME = "StateName";
    public static final String CITY_NAME = "CityName";
    public static final String DISTRICT_NAME = "District";
    public static final String BLOCK_NAME = "Block";

    // TEST STRINGS
    public static final String EXPORTED = "exported";
    public static final String TEST_TYPE_ID = "test_type_id";
    public static final String SYNCED = "synced";
    public static final int HEIGHT_MIN_LIMIT=40;
    public static final int HEIGHT_MAX_LIMIT=250;
    public static final int WEIGHT_MIN_LIMIT=10;
    public static final int WEIGHT_MAX_LIMIT=500;
    public static final String IS_LOGIN ="isLogin";
    public static final String SCORE = "Score";



    //URLS
    public static final String REGISTER_TOT_URL = "https://docs.google.com/forms/d/1apHfphffPv8UQcw_U2h9XOOuP6MkBNzKlsls04El6xM/viewform?ts=5c3c2875&edit_requested=true";
    public static final String SOP_URL = "https://schoolfitness.kheloindia.gov.in/UploadedFiles/SampleData/SOP.pdf";
    public static final String ECERTIFICATE_URL = "http://schoolfitness.kheloindia.gov.in:8080/";
    public static final String ADMIN_MANUAL_URL = "https://schoolfitness.kheloindia.gov.in/UploadedFiles/SampleData/AdminManual.pdf";

    public static final String GET_SCHOOLS_URL = "getSchools/";


}
