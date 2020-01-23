package kheloindia.com.assessment.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.preference.PreferenceManager;
import android.util.Log;

import kheloindia.com.assessment.SchoolActivity;
import kheloindia.com.assessment.TakeTestActivity;
import kheloindia.com.assessment.model.CampMasterModel;
import kheloindia.com.assessment.model.CampTestMapping;
import kheloindia.com.assessment.model.FitnessSkillTestResultModel;
import kheloindia.com.assessment.model.FitnessTestCategoryModel;
import kheloindia.com.assessment.model.FitnessTestTypesModel;
import kheloindia.com.assessment.model.ListViewDialogModel;
import kheloindia.com.assessment.model.LocationTrackingModel;
import kheloindia.com.assessment.model.LoginModel;
import kheloindia.com.assessment.model.MyDartClassModel;
import kheloindia.com.assessment.model.MyDartGradeModel;
import kheloindia.com.assessment.model.MyDartInsertActivityModel;
import kheloindia.com.assessment.model.MyDartLessonPlanModel;
import kheloindia.com.assessment.model.MyDartOtherModel;
import kheloindia.com.assessment.model.MyDartPeriodModel;
import kheloindia.com.assessment.model.MyDartSportModel;
import kheloindia.com.assessment.model.MyDartSportSkillModel;
import kheloindia.com.assessment.model.MyDartStudentModel;
import kheloindia.com.assessment.model.SchoolClassMapping;
import kheloindia.com.assessment.model.SchoolsMasterModel;
import kheloindia.com.assessment.model.ScreenMasterModel;
import kheloindia.com.assessment.model.SkillTestTypeModel;
import kheloindia.com.assessment.model.StudentMasterModel;
import kheloindia.com.assessment.model.StudentModel;
import kheloindia.com.assessment.model.StudentUserMasterModel;
import kheloindia.com.assessment.model.TestCoordinatorMapping;
import kheloindia.com.assessment.model.TestCoordinatorsModel;
import kheloindia.com.assessment.model.TestReportModel;
import kheloindia.com.assessment.model.UserSchoolMappingModel;
import kheloindia.com.assessment.model.UserScreenMapModel;
import kheloindia.com.assessment.functions.Constant;
import kheloindia.com.assessment.model.FitnessTestResultModel;


public class DBManager {

    private static DBManager iSelf;
    private static final String DB_NAME = "fitness365.db";
    private static final int DB_VER = 13;
    private static SQLiteDatabase iDB;
    private DatabaseHelper iDbHelper;
    private  Context iContext;

    public static final String TBL_LP_USER_LOGIN = "lp_user_login";
    public static final String TBL_LP_TEST_COORDINATORS = "lp_test_coordinators";
    public static final String TBL_LP_SCHOOLS_MASTER = "lp_schools_master";
    public static final String TBL_LP_STUDENT_USER_MASTER = "lp_student_user_master";
    public static final String TBL_LP_STUDENT_MASTER = "lp_student_master";
    public static final String TBL_LP_CAMP_MASTER = "lp_camp_master";
    public static final String TBL_LP_FITNESS_TEST_TYPES = "lp_fitness_test_types";
    public static final String TBL_LP_FITNESS_TEST_RESULT = "lp_fitness_test_result";
    public static final String TBL_LP_FITNESS_RETEST_RESULT = "lp_fitness_retest_result";
    public static final String TBL_LP_FITNESS_TEST_CATEGORY = "lp_fitness_test_category";
    public static final String TBL_LP_MY_DART_CLASS_TABLE = "lp_my_dart_class";
    public static final String  TBL_LP_MY_DART_PERIOD_TABLE = "lp_my_dart_period";
    public static final String  TBL_LP_MY_DART_GRADE_TABLE = "lp_my_dart_grade";
    public static final String  TBL_LP_MY_DART_SPORT_TABLE = "lp_my_dart_sport";
    public static final String  TBL_LP_MY_DART_OTHER_TABLE = "lp_my_dart_other";
    public static final String  TBL_LP_MY_DART_LESSON_PLAN_TABLE = "lp_my_dart_lesson";
    public static final String  TBL_LP_MY_DART_SPORT_SKILL_TABLE = "lp_my_dart_sport_skill";
    public static final String  TBL_LP_MY_DART_STUDENT_TABLE ="lp_my_dart_student";
    public static final String TBL_LP_MY_DART_INSERT_ACTIVITY_TABLE="lp_my_dart_insert_activity";
    public static final String TBL_LP_SKILL_TEST_TYPE = "lp_skill_test_type";

    public static final String TBL_LP_USER_SCHOOL_MAPPING = "lp_user_school_mapping";

    public static final String TBL_LP_TEST_COORDINATOR_MAPPING = "lp_test_coordinator_mapping";
    public static final String TBL_LP_FITNESS_SKILL_TEST_RESULT = "lp_fitness_skill_test_result";

    public static final String TBL_LP_SCHOOL_CLASS_FLAG_MAPPING = "lp_school_class_flag_mapping";

    public static final String TBL_LP_CAMP_TEST_MAPPING = "camp_test_mapping";

    public static final String TBL_LP_SCREEN_MASTER = "lp_screen_master";
    public static final String TBL_LP_USER_SCREEN_MAP = "lp_user_screen_map";

    public static final String TBL_LP_MORNING_TRACKING = "lp_morning_tracking";

    public static final String TBL_STATE_MASTER = "StateMaster";


//    public static final String CREATE_LP_USER_LOGIN_TABLE = "create table " + TBL_LP_USER_LOGIN + " ( _id integer primary key autoincrement," + "user_login_id integer not null,user_login_name text not null, user_password text not null,user_type_id text null, login_type text null,"
//            + "image_name text null, image_path text null, created_on datetime null,created_by text null, last_modified_on datetime null, last_modified_by text null );";

    public static final String CREATE_LP_USER_LOGIN_TABLE = "create table " + TBL_LP_USER_LOGIN + " ( _id integer primary key autoincrement," + "user_login_name text not null, user_password text not null,user_type_id text null, login_type text null,"
            + "image_name text null, image_path text null, created_on datetime null,created_by text null, last_modified_on datetime null, last_modified_by text null );";


    public static final String CREATE_LP_TEST_COORDINATORS_TABLE = "create table " + TBL_LP_TEST_COORDINATORS + " ( _id integer primary key autoincrement, test_coordinator_id integer null, test_coordinator_name text null, gender text null, email text null,"
            + "phone text null, address text null, school_id text null, user_type_id text null, is_active integer null, created_on datetime null,created_by text null, last_modified_on datetime null, last_modified_by text null, imei integer null, android_id integer null);";

//    public static final String CREATE_LP_SCHOOLS_MASTER_TABLE = "create table " + TBL_LP_SCHOOLS_MASTER + " ( _id integer primary key autoincrement," + "school_id integer not null, school_name text null, school_chain_id integer not null,"
//            + "city_id integer null, address text null, phone_no text null, email text null, web_address text null, school_description text null, school_image_name text null , school_image_path text null, created_on datetime null, created_by text null, last_modified_on datetime null, last_modified_by text null, school_logo_name text null, school_logo_path text null, school_alias_name text null, is_active integer null, trainer_coordinator_id text null, seniors_starting_from text null, latitude varchar null, longitude varchar null, school_start_time varchar null, office text null );";


//    public static final String CREATE_LP_SCHOOLS_MASTER_TABLE = "create table " + TBL_LP_SCHOOLS_MASTER + " ( _id integer primary key autoincrement," + "school_id integer not null, school_name text null, school_chain_id integer not null,"
//            + "city_id integer null, address text null, phone_no text null, email text null, web_address text null, school_description text null, school_image_name text null , school_image_path text null, created_on datetime null, created_by text null, last_modified_on datetime null, last_modified_by text null, school_logo_name text null, school_logo_path text null, school_alias_name text null, is_active integer null, trainer_coordinator_id text null, seniors_starting_from text null, latitude varchar null, longitude varchar null, school_start_time varchar null, office text null, "+AppConfig.IS_ATTACHED +" INT DEFAULT 1);";

    public static final String CREATE_LP_SCHOOLS_MASTER_TABLE = "create table " + TBL_LP_SCHOOLS_MASTER + " ( _id integer primary key autoincrement," + "school_id integer not null, school_name text null, school_chain_id integer not null,"
            + "city_id integer null, address text null, phone_no text null, email text null, web_address text null, school_description text null, school_image_name text null , school_image_path text null, created_on datetime null, created_by text null, last_modified_on datetime null, last_modified_by text null, school_logo_name text null, school_logo_path text null, school_alias_name text null, is_active integer null, trainer_coordinator_id text null, seniors_starting_from text null, latitude varchar null, longitude varchar null, school_start_time varchar null, office text null, "+AppConfig.IS_ATTACHED +" INT DEFAULT 1, "+AppConfig.IS_RETEST_ALLOWED +" TEXT DEFAULT 'False', " + AppConfig.FOR_RETEST + " INT DEFAULT 0);";


    public static final String CREATE_LP_STUDENT_USER_MASTER_TABLE = "create table " + TBL_LP_STUDENT_USER_MASTER + " ( _id integer primary key autoincrement, guardian_name text null, image_path text null, phone text null, student_name text null, user_login_name text null, address text null, current_class text null, date_of_birth text null,  gender text null );";


    public static final String CREATE_LP_STUDENT_MASTER_TABLE = "create table " + TBL_LP_STUDENT_MASTER + " ( _id integer primary key autoincrement, student_name text null, current_school_id text null, current_roll_num text null, gender integer null, dob text null, email text null, gaurdian_name text null, phone text null, address text null, created_on text null, created_by text null, last_modified_on text null, last_modified_by text null, student_id integer null, student_registration_num text null, current_class text null, class_id text null, class_partition_id integer null, section text null, is_active integer null, camp_id text null );";

    public static final String CREATE_LP_CAMP_MASTER_TABLE = "create table " + TBL_LP_CAMP_MASTER + " ( _id integer primary key autoincrement," + "camp_id integer not null, camp_name text null, camp_type integer null, school_id integer null, "
            + "venue_id integer null, start_date text null, end_date text null, camp_coordination_id integer null, questionaire_id integer null, registration_coordinator_id integer null, status integer null, data_flag integer null );";

    public static final String CREATE_LP_FITNESS_TEST_TYPES_TABLE = "create table " + TBL_LP_FITNESS_TEST_TYPES + " ( _id integer primary key autoincrement," + "test_type_id integer not null, test_name text null, excel_name text null, score_measurement text null, "
            + "score_unit text null, score_criteria text null, test_description text null, test_performed text null, created_on datetime null, created_by text null, last_modified_on datetime null, last_modified_by text null, test_category_id text null, test_image text null, test_img_path text null, school_id text null, test_coordinator_id text null );";

    public static final String CREATE_LP_FITNESS_TEST_RESULT_TABLE = "create table " + TBL_LP_FITNESS_TEST_RESULT + " ( _id integer primary key autoincrement," + "student_id integer not null, camp_id integer null, test_type_id integer null, test_coordinator_id integer null, "
            + "score integer null, percentile text null, created_on datetime null, created_by text null, last_modified_on long null, last_modified_by text null, latitude text null, longitude text null, test_name text null, tested text null, sub_test_name text null, device_date datetime null, synced text null,"+ AppConfig.EXPORTED + " TEXT default 'false');";

//    public static final String CREATE_LP_FITNESS_TEST_RESULT_TABLE = "create table " + TBL_LP_FITNESS_TEST_RESULT + " ( _id integer primary key autoincrement," + "student_id integer not null, camp_id integer null, test_type_id integer null, test_coordinator_id integer null, "
//        + "score integer null, percentile text null, created_on datetime null, created_by text null, last_modified_on long null, last_modified_by text null, latitude text null, longitude text null, test_name text null, tested text null, sub_test_name text null, device_date datetime null, synced text null );";

    public static final String CREATE_LP_FITNESS_TEST_CATEGORY = "create table " + TBL_LP_FITNESS_TEST_CATEGORY + " ( _id integer primary key autoincrement," + "sub_test_id integer not null, test_id integer not null, test_description text null, purpose text null, Administrative_Suggestions text null, Equipment_Required text null, scoring text null, test_name text null, score_criteria text null, score_measurement text null, name text null, created_on datetime null, last_modified_on datetime null, multiple_lane integer not null, timer_type integer not null, final_position integer not null, video_link text null, tests_applicable text null, " + "score_unit text null );";

    public static final String CREATE_LP_SKILL_TEST_TYPE = "create table " + TBL_LP_SKILL_TEST_TYPE + " ( _id integer primary key autoincrement," + "checklist_item_id integer not null, item_name text not null, test_type_id text null, created_on datetime null, synced text null, last_modified_on datetime null );";

    public static final String CREATE_LP_USER_SCHOOL_MAPPING = "create table " + TBL_LP_USER_SCHOOL_MAPPING + " ( _id integer primary key autoincrement," + "user_id integer not null, school_id integer not null, created_on datetime null, last_modified_on datetime null );";

    public static final String CREATE_LP_TEST_COORDINATOR_MAPPING = "create table " + TBL_LP_TEST_COORDINATOR_MAPPING + " ( _id integer primary key autoincrement," + "school_id integer not null, camp_id integer not null, test_type_id integer not null );";

    public static final String CREATE_LP_FITNESS_SKILL_TEST_RESULT = "create table " + TBL_LP_FITNESS_SKILL_TEST_RESULT + " ( _id integer primary key autoincrement," + "skill_score_id integer not null, student_id integer not null, camp_id integer not null, test_type_id integer not null, checklist_item_id integer not null, coordinator_id integer not null, score text null, created_on datetime null, longitude text null, latitude text null, last_modified_on long null, tested text null, device_date datetime null, synced integer not null );";

    public static final String CREATE_LP_SCHOOL_CLASS_FLAG_MAPPING = "create table " + TBL_LP_SCHOOL_CLASS_FLAG_MAPPING + " ( _id integer primary key autoincrement," + "school_id integer not null, class_name text null, class_id integer not null, flag integer not null, created_on datetime null, last_modified_on datetime null );";

    public static final String CREATE_LP_CAMP_TEST_MAPPING = "create table " + TBL_LP_CAMP_TEST_MAPPING + " ( _id integer primary key autoincrement," + "school_id integer not null, camp_id integer not null, test_type_id integer not null, class_id text null );";

    public static final String CREATE_LP_MY_DART_CLASS_TABLE="create table "+TBL_LP_MY_DART_CLASS_TABLE+" (_id integer  primary key autoincrement,"+ "test_coordinator_id integer null, current_school_id integer null, class text null, class_id text null );";

    public static final String CREATE_LP_MY_DART_PERIOD_TABLE="create table "+TBL_LP_MY_DART_PERIOD_TABLE+" (_id integer  primary key autoincrement,"+ "test_coordinator_id integer null, current_school_id integer null, period text null );";
    public static final String CREATE_LP_MY_DART_GRADE_TABLE="create table "+TBL_LP_MY_DART_GRADE_TABLE+" (_id integer  primary key autoincrement,"+ "current_school_id integer null, grade text null, grade_id text null );";
    public static final String CREATE_LP_MY_DART_SPORT_TABLE="create table "+TBL_LP_MY_DART_SPORT_TABLE+" (_id integer  primary key autoincrement,"+ "test_coordinator_id integer null, current_school_id integer null, current_class text null, sport text null, sport_id text null );";
    public static final String CREATE_LP_MY_DART_OTHER_TABLE="create table "+TBL_LP_MY_DART_OTHER_TABLE+" (_id integer  primary key autoincrement,"+ "test_coordinator_id integer null, current_school_id integer null, other text null, other_id text null );";
    public static final String CREATE_LP_MY_DART_LESSON_PLAN_TABLE="create table "+TBL_LP_MY_DART_LESSON_PLAN_TABLE+" (_id integer  primary key autoincrement,"+ "test_coordinator_id integer null, current_school_id integer null, class_id text null, lesson text null, lesson_id text null );";
    public static final String CREATE_LP_MY_DART_SPORT_SKILL_TABLE="create table "+TBL_LP_MY_DART_SPORT_SKILL_TABLE+" (_id integer  primary key autoincrement,"+ "test_coordinator_id integer null, current_school_id integer null, sport_test_id text null, sport_skill text null, sport_skill_id text null, technique_id text null);";
    public static final String CREATE_LP_MY_DART_STUDENT_TABLE="create table "+TBL_LP_MY_DART_STUDENT_TABLE+" (_id integer  primary key autoincrement,"+"test_coordinator_id integer null, current_school_id integer null, current_class text null, student_name text null, student_id text null, sport_skill_id text null, student_grade_id text null );";
    public static final String CREATE_LP_MY_DART_INSERT_ACTIVITY_TABLE="create table "+TBL_LP_MY_DART_INSERT_ACTIVITY_TABLE+" (_id integer  primary key autoincrement,"+"test_coordinator_id integer null, current_school_id integer null, data text null, activity_id text null, student_activity_id text null );";

    public static final String CREATE_LP_SCREEN_MASTER="create table "+TBL_LP_SCREEN_MASTER+" (screen_id text  primary key,"+"parent_id integer null, screen_name text null, web_url text null, mobile_icon_title text null, icon_image_name text null, icon_path text null, created_on datetime null, created_by text null, modified_on datetime null, modified_by text null, is_deleted integer null  );";

    public static final String CREATE_LP_USER_SCREEN_MAP="create table "+TBL_LP_USER_SCREEN_MAP+" (mapping_id integer not null,"+"screen_id varchar null, user_type_id integer null, created_on datetime null, created_by text null, modified_on datetime null, modified_by text null, is_active text null  );";

    public static final String CREATE_LP_MORNING_TRACKING="create table "+TBL_LP_MORNING_TRACKING+" (latitude varchar null,"+ "longitude varchar null, address text null, created_on text null, trainer_id integer null);";

    public static final String CREATE_STATE_MASTER_TABLE = "create table " + TBL_STATE_MASTER + "("+ AppConfig.STATE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+AppConfig.STATE_NAME+" varchar(50) NOT NULL,StateHeadName varchar(50),Gender INTEGER,EmailId varchar(50),ContactNo varchar(12),IsActive bit NULL DEFAULT ((1)),IsDeleted bit NULL DEFAULT ((0)),CreatedOn datetime NULL DEFAULT (CURRENT_TIMESTAMP),CreatedBy INTEGER NULL,ModifiedOn datetime NULL DEFAULT (CURRENT_TIMESTAMP),ModifiedBy INTEGER NULL)";

    public static final String INSERT_INTO_STATE_MASTER_TABLE = "INSERT INTO STATEMASTER (STATENAME) VALUES ('Andaman & Nicobar Islands'),('Andhra Pradesh '),('Arunachal Pradesh'),('Assam'),('Bihar'),('Chhattisgarh'),('Dadra & Nagar Haveli'),('Daman & Diu'),('Delhi (East) '),('Delhi (West) '),('Foreign School'),('Goa'),('Gujarat'),('Haryana'),('Himachal Pradesh'),('Jammu and Kashmir'),('Jharkhand'),('Karnataka'),('Kerala'),('Lakshadweep'),('Madhya Pradesh'),('Maharashtra'),('Manipur'),('Meghalaya'),('Mizoram'),('Nagaland'),('Odisha'),('Puducherry'),('Punjab'),('Rajasthan'),('Sikkim'),('Tamil Nadu '),('Telangana '),('Tripura'),('U.T. of Chandigarh'),('Uttar Pradesh (East)'),('Uttar Pradesh (North)'),('Uttar Pradesh (West)'),('West Bengal'),('Uttarakhand')";

    public static final String CREATE_LP_FITNESS_RETEST_RESULT_TABLE = "create table " + TBL_LP_FITNESS_RETEST_RESULT + " ( _id integer primary key autoincrement," + "student_id integer not null, camp_id integer null, test_type_id integer null, test_coordinator_id integer null, "
            + "score integer null, percentile text null, created_on datetime null, created_by text null, last_modified_on long null, last_modified_by text null, latitude text null, longitude text null, test_name text null, tested text null, sub_test_name text null, device_date datetime null, synced text null,"+ AppConfig.EXPORTED + " TEXT default 'false');";


    private final static String TAG = "FITNESS365DBManager";

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     */
    public static DBManager getInstance() {
        if (iSelf == null) {
            iSelf = new DBManager();
            return iSelf;

        } else {
            return iSelf;
        }
    }

    /**
     * this method is used for creating or opening the connection.
     *
     * @return
     * @throws SQLException
     */
    public DBManager openDB() throws SQLException {

        iDbHelper = new DatabaseHelper(iContext);
        Log.e("DBManager", "iDbHelper==> " + iDbHelper);
        Log.e("DBManager", "iContext==> " + iContext);
        iDB = iDbHelper.getWritableDatabase();
        Log.e("DBManager", "iDB==> " + iDB);

        return this;
    }

    /**
     * this method is used for closing the connection.
     */
    public void close() {
        try {
            iDbHelper.close();
        } catch (Exception e) {
            Log.e("["+TAG+"]:", "'" + TAG + "' - close() : ", e);
        }

    }

    public String getMaxDate(Context context, String TBL_NAME, String ARGS1,String ARGS2) {
        iContext = context;
        long maxDate =0;
        String dateString ="";


        if (iDB == null) {

            // OPENING DATABASE CONN.
            try {
                DBManager.getInstance().openDB();
            } catch (Exception e) {
                Log.e("["+TAG+"]:", "'" + TAG + "' - getAllTableData() :", e);
            }
        }

        if (TBL_NAME.equalsIgnoreCase(TBL_NAME)) {
            try {

                int count = 0;
                Cursor cr = null;

                String query = "select max(" + ARGS1 + ") as last_modified_on " + "from " + TBL_NAME + "";
                if (TBL_NAME.equals("lp_fitness_test_result") ) {
                    //Amit
                    query += " where synced = 'true'  and  tested ='true'  and  camp_id ='"+ARGS2+"'";
                }else if(TBL_NAME.equals("lp_fitness_skill_test_result")){
                    query += " where tested ='true'  and  synced ='1'  and  camp_id ='"+ARGS2+"'";

                }
                Log.e(TAG, "max date query==> " + query);
                cr = iDB.rawQuery(query, null);
                if (cr.moveToFirst()) {
                    do {
                        count++;

                        maxDate = cr.getLong(0);

                        Log.e(TAG, "maxDate==> " + maxDate+" count==> "+count);


                    } while (cr.moveToNext());
                }
                //dateStr = maxDate;
//                DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
//                Date date = (Date) formatter.parse(dateStr);
//
//                dateString = Constant.ConvertDateToString(date);
//
//                Log.e(TAG, "dateString==> " + dateString);
                if(maxDate>1)
                    dateString=Utility.getDate(maxDate,"dd MMM yyyy HH:mm:ss:SSS");
                else
                    dateString="";

                if (cr != null && !cr.isClosed()) {
                    cr.close();
                }

            } catch (Exception e) {
                Log.e("[" + TAG + "]: ", "TBL_LP_USER_LOGIN - getAllTableData(): ", e);
            }
        }
        return dateString;
    }

    // print all rows of the table
    public String getTableAsString( String tableName) {

        int rowCount = 0;
        Log.d(TAG, "getTableAsString called");
        String tableString = String.format("Table %s:\n", tableName);
        Cursor allRows  = iDB.rawQuery("SELECT * FROM " + tableName+ " WHERE synced = 'false' ", null);
        if (allRows.moveToFirst() ){
            String[] columnNames = allRows.getColumnNames();
            do {
                for (String name: columnNames) {
                    rowCount = rowCount+1;
                    tableString += String.format("%s: %s\n", name,
                            allRows.getString(allRows.getColumnIndex(name)));
                }
                tableString += "\n";

            } while (allRows.moveToNext());
        }

        Log.e(TAG,"rowCount==> "+rowCount);
        return tableString;
    }


    //    public String getMaxDateDevice(Context context, String TBL_NAME1,String TBL_NAME2, String ARGS1) {
//        iContext = context;
//        String maxDate1 = null;
//        String maxDate2 =null;
//        String dateString = "";
//        Date date1=null;
//        Date date2=null;
//
//
//        if (iDB == null) {
//
//            // OPENING DATABASE CONN.
//            try {
//                DBManager.getInstance().openDB();
//            } catch (Exception e) {
//                Log.e("["+TAG+"]:", "'" + TAG + "' - getAllTableData() :", e);
//            }
//        }
//
//      //  if (TBL_NAME.equalsIgnoreCase(TBL_NAME)) {
//            try {
//
//                Cursor cr1 = null;
//                Cursor cr2=null;
//
//                cr1 = iDB.rawQuery("select Max(" + ARGS1 + ") as device_date " + "from " + TBL_NAME1 + " where synced = 'true' ", null);
//                cr2 = iDB.rawQuery("select Max(" + ARGS1 + ") as device_date " + "from " + TBL_NAME2 + " where synced = 'true' ", null);
//
//                //String query = "select Max(" + ARGS1 + ") as device_date " + "from " + TBL_NAME + "";
////                if (TBL_NAME.equals("lp_fitness_test_result") || TBL_NAME.equals("lp_fitness_skill_test_result")) {
////                    //Amit
////                    query += " where synced = 'true' ";
////                }
////
////                Log.e(TAG, "max date query==> " + query);
//
//                if (cr1.moveToFirst()) {
//                    do {
//
//                        maxDate1 = cr1.getString(0);
//
//                        Log.e(TAG, "maxDate1==> " + maxDate1);
//
//
//                    } while (cr1.moveToNext());
//                }
//
//                if (cr2.moveToFirst()) {
//                    do {
//
//                        maxDate2 = cr2.getString(0);
//
//                        Log.e(TAG, "maxDate2==> " + maxDate2);
//
//
//                    } while (cr1.moveToNext());
//                }
//
//
//
//                DateFormat formatter = new SimpleDateFormat("M/dd/yyyy hh:mm:ss");
//                if(maxDate1!=null)
//                date1 = formatter.parse(maxDate1);
//                if(maxDate2!=null)
//                date2 =  formatter.parse(maxDate2);
//
//   if (date1!=null &&date2!=null) {
//       if (date1.getTime() >= date2.getTime())
//           dateString = Constant.ConvertDateToString(date1);
//       else
//           dateString = Constant.ConvertDateToString(date2);
//   }
//                else if(date1!=null)
//       dateString = Constant.ConvertDateToString(date1);
//                else if (date2!=null)
//       dateString = Constant.ConvertDateToString(date2);
//
//
//
//
//    Log.e(TAG, "dateString==> " + dateString);
//
//                if (cr1 != null && !cr1.isClosed()) {
//                    cr1.close();
//                }
//                if (cr2 != null && !cr2.isClosed()) {
//                    cr2.close();
//                }
//
//            } catch (Exception e) {
//                Log.e("[" + TAG + "]: ", "MaxDateDevice - getAllTableData(): ", e);
//            }
//        //}
//        return dateString;
//    }
    //
    private static class DatabaseHelper extends SQLiteOpenHelper {

        Context context;
        DatabaseHelper(Context context) {
            super(context, DB_NAME, null, DB_VER);
            this.context = context;
        }

        public void onCreateNamedTable(SQLiteDatabase iDB, String CREATE_NAMED_TABLE) {

            try {

                if (CREATE_NAMED_TABLE.equalsIgnoreCase(TBL_LP_STUDENT_MASTER)) {
                    iDB.execSQL(CREATE_LP_STUDENT_MASTER_TABLE);
                } else if (CREATE_NAMED_TABLE.equalsIgnoreCase(TBL_LP_SCHOOLS_MASTER)) {
                    iDB.execSQL(CREATE_LP_SCHOOLS_MASTER_TABLE);
                } else if (CREATE_NAMED_TABLE.equalsIgnoreCase(TBL_LP_STUDENT_USER_MASTER)) {
                    iDB.execSQL(CREATE_LP_STUDENT_USER_MASTER_TABLE);
                } else if (CREATE_NAMED_TABLE.equalsIgnoreCase(TBL_LP_FITNESS_TEST_TYPES)) {
                    iDB.execSQL(CREATE_LP_FITNESS_TEST_TYPES_TABLE);
                } else if (CREATE_NAMED_TABLE.equalsIgnoreCase(TBL_LP_FITNESS_TEST_CATEGORY)) {
                    iDB.execSQL(CREATE_LP_FITNESS_TEST_CATEGORY);
                } else if (CREATE_NAMED_TABLE.equalsIgnoreCase(TBL_LP_SKILL_TEST_TYPE)) {
                    iDB.execSQL(CREATE_LP_SKILL_TEST_TYPE);
                } else if (CREATE_NAMED_TABLE.equalsIgnoreCase(TBL_LP_USER_SCHOOL_MAPPING)) {
                    iDB.execSQL(CREATE_LP_USER_SCHOOL_MAPPING);
                } else if (CREATE_NAMED_TABLE.equalsIgnoreCase(TBL_LP_TEST_COORDINATOR_MAPPING)) {
                    iDB.execSQL(CREATE_LP_TEST_COORDINATOR_MAPPING);
                } else if (CREATE_NAMED_TABLE.equalsIgnoreCase(TBL_LP_FITNESS_SKILL_TEST_RESULT)) {
                    iDB.execSQL(CREATE_LP_FITNESS_SKILL_TEST_RESULT);}
                else if (CREATE_NAMED_TABLE.equalsIgnoreCase(TBL_LP_FITNESS_TEST_RESULT)) {
                    iDB.execSQL(CREATE_LP_FITNESS_TEST_RESULT_TABLE);
                } else if (CREATE_NAMED_TABLE.equalsIgnoreCase(TBL_LP_SCHOOL_CLASS_FLAG_MAPPING)) {
                    iDB.execSQL(CREATE_LP_SCHOOL_CLASS_FLAG_MAPPING);
                } else if (CREATE_NAMED_TABLE.equalsIgnoreCase(TBL_LP_CAMP_TEST_MAPPING)) {
                    iDB.execSQL(CREATE_LP_CAMP_TEST_MAPPING);
                }
                else if (CREATE_NAMED_TABLE.equalsIgnoreCase(TBL_LP_MY_DART_CLASS_TABLE)) {
                    iDB.execSQL(CREATE_LP_MY_DART_CLASS_TABLE);
                }
                else if (CREATE_NAMED_TABLE.equalsIgnoreCase(TBL_LP_MY_DART_PERIOD_TABLE)) {
                    iDB.execSQL(CREATE_LP_MY_DART_PERIOD_TABLE);
                }
                else if (CREATE_NAMED_TABLE.equalsIgnoreCase(TBL_LP_MY_DART_SPORT_TABLE)) {
                    iDB.execSQL(CREATE_LP_MY_DART_SPORT_TABLE);
                }

                else if (CREATE_NAMED_TABLE.equalsIgnoreCase(TBL_LP_MY_DART_OTHER_TABLE)) {
                    iDB.execSQL(CREATE_LP_MY_DART_OTHER_TABLE);
                }
                else if (CREATE_NAMED_TABLE.equalsIgnoreCase(TBL_LP_MY_DART_LESSON_PLAN_TABLE)) {
                    iDB.execSQL(CREATE_LP_MY_DART_LESSON_PLAN_TABLE);
                }
                else if (CREATE_NAMED_TABLE.equalsIgnoreCase(TBL_LP_MY_DART_SPORT_SKILL_TABLE)) {
                    iDB.execSQL(CREATE_LP_MY_DART_SPORT_SKILL_TABLE);
                }
                else if (CREATE_NAMED_TABLE.equalsIgnoreCase(TBL_LP_MY_DART_STUDENT_TABLE)) {
                    iDB.execSQL(CREATE_LP_MY_DART_STUDENT_TABLE);
                }else if (CREATE_NAMED_TABLE.equalsIgnoreCase(TBL_LP_MY_DART_INSERT_ACTIVITY_TABLE)) {
                    iDB.execSQL(CREATE_LP_MY_DART_INSERT_ACTIVITY_TABLE);
                }else if (CREATE_NAMED_TABLE.equalsIgnoreCase(TBL_LP_SCREEN_MASTER)) {
                    iDB.execSQL(CREATE_LP_SCREEN_MASTER);
                }else if (CREATE_NAMED_TABLE.equalsIgnoreCase(TBL_LP_USER_SCREEN_MAP)) {
                    iDB.execSQL(CREATE_LP_USER_SCREEN_MAP);
                } else if(CREATE_NAMED_TABLE.equalsIgnoreCase(TBL_LP_MORNING_TRACKING)){
                    iDB.execSQL(CREATE_LP_MORNING_TRACKING);
                } else if (CREATE_NAMED_TABLE.equalsIgnoreCase(TBL_LP_MY_DART_GRADE_TABLE)) {
                    iDB.execSQL(CREATE_LP_MY_DART_GRADE_TABLE);
                } else if (CREATE_NAMED_TABLE.equalsIgnoreCase(TBL_STATE_MASTER)) {
                    iDB.execSQL(CREATE_STATE_MASTER_TABLE);
                } else if (CREATE_NAMED_TABLE.equalsIgnoreCase(TBL_LP_FITNESS_RETEST_RESULT)){
                    iDB.execSQL(CREATE_LP_FITNESS_RETEST_RESULT_TABLE);
                }

            } catch (Exception e) {
                // Log.e("[ " + TAG + " ] : ", "onCreateNamedTable() : ", e);
                e.printStackTrace();
            }
        }

        @Override
        public void onCreate(SQLiteDatabase iDB) {

            Log.i("[" + TAG + "]: ", "Creating DataBase: Tables");

            iDB.execSQL(CREATE_LP_USER_LOGIN_TABLE);
            iDB.execSQL(CREATE_LP_TEST_COORDINATORS_TABLE);
            iDB.execSQL(CREATE_LP_SCHOOLS_MASTER_TABLE);
            iDB.execSQL(CREATE_LP_STUDENT_MASTER_TABLE);
            iDB.execSQL(CREATE_LP_CAMP_MASTER_TABLE);
            iDB.execSQL(CREATE_LP_FITNESS_TEST_TYPES_TABLE);
            iDB.execSQL(CREATE_LP_FITNESS_TEST_RESULT_TABLE);
            iDB.execSQL(CREATE_LP_FITNESS_TEST_CATEGORY);
            iDB.execSQL(CREATE_LP_SKILL_TEST_TYPE);
            iDB.execSQL(CREATE_LP_USER_SCHOOL_MAPPING);
            iDB.execSQL(CREATE_LP_FITNESS_SKILL_TEST_RESULT);
            iDB.execSQL(CREATE_LP_CAMP_TEST_MAPPING);
            iDB.execSQL(CREATE_LP_TEST_COORDINATOR_MAPPING);
            iDB.execSQL(CREATE_LP_STUDENT_USER_MASTER_TABLE);
            iDB.execSQL(CREATE_LP_SCREEN_MASTER);
            iDB.execSQL(CREATE_LP_USER_SCREEN_MAP);
            iDB.execSQL(CREATE_LP_MORNING_TRACKING);
            iDB.execSQL(CREATE_LP_MY_DART_GRADE_TABLE);
            iDB.execSQL(CREATE_STATE_MASTER_TABLE);
            Cursor cursor = iDB.query(TBL_STATE_MASTER,null,null,null,null,null,null);
            if (cursor.getCount()<=0){
                iDB.execSQL(INSERT_INTO_STATE_MASTER_TABLE);
            }
            cursor.close();
            iDB.execSQL(CREATE_LP_FITNESS_RETEST_RESULT_TABLE);

            //iDB.execSQL("ALTER table "+TBL_LP_SCHOOLS_MASTER+" add "+AppConfig.IS_ATTACHED+" INT DEFAULT 1");

        }

        @Override
        public void onUpgrade(SQLiteDatabase iDB, int oldVersion, int newVersion) {

            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion);

//            iDB.execSQL(query+TBL_LP_USER_LOGIN);
//            iDB.execSQL(query+TBL_LP_TEST_COORDINATORS);
//            iDB.execSQL(query+TBL_LP_SCHOOLS_MASTER);
//            iDB.execSQL(query+TBL_LP_STUDENT_MASTER);
//            iDB.execSQL(query+TBL_LP_CAMP_MASTER);
//            iDB.execSQL(query+TBL_LP_FITNESS_TEST_TYPES);
//            iDB.execSQL(query+TBL_LP_FITNESS_TEST_RESULT);
//            iDB.execSQL(query+CREATE_LP_FITNESS_TEST_CATEGORY);
//            iDB.execSQL(CREATE_LP_SKILL_TEST_TYPE);
//            iDB.execSQL(CREATE_LP_USER_SCHOOL_MAPPING);
//            iDB.execSQL(CREATE_LP_FITNESS_SKILL_TEST_RESULT);
//            iDB.execSQL(CREATE_LP_CAMP_TEST_MAPPING);
//            iDB.execSQL(CREATE_LP_TEST_COORDINATOR_MAPPING);
//            iDB.execSQL(CREATE_LP_STUDENT_USER_MASTER_TABLE);
//            iDB.execSQL(CREATE_LP_SCREEN_MASTER);
//            iDB.execSQL(CREATE_LP_USER_SCREEN_MAP);
//            iDB.execSQL(CREATE_LP_MORNING_TRACKING);
//            iDB.execSQL(CREATE_LP_MY_DART_GRADE_TABLE);



//            iDB.execSQL(query + TBL_LP_SCHOOLS_MASTER);
//            iDB.execSQL(query + TBL_LP_FITNESS_TEST_RESULT);
//            iDB.execSQL(query + TBL_LP_FITNESS_TEST_CATEGORY);
//            iDB.execSQL(query + TBL_LP_FITNESS_SKILL_TEST_RESULT);
//
//
//
//            iDB.execSQL(query + TBL_LP_USER_LOGIN);
//            iDB.execSQL(query + TBL_LP_TEST_COORDINATORS);
//
//            iDB.execSQL(query + TBL_LP_STUDENT_MASTER);
//            iDB.execSQL(query + TBL_LP_CAMP_MASTER);
//            iDB.execSQL(query + TBL_LP_FITNESS_TEST_TYPES);
//
//            iDB.execSQL(query + TBL_LP_SKILL_TEST_TYPE);
//            iDB.execSQL(query + TBL_LP_USER_SCHOOL_MAPPING);
//
//            //iDB.execSQL(query + TBL_LP_SCHOOL_CLASS_FLAG_MAPPING);
//            iDB.execSQL(query + TBL_LP_CAMP_TEST_MAPPING);
//            iDB.execSQL(query + TBL_LP_TEST_COORDINATOR_MAPPING);



//            iDB.execSQL("Drop table if exists " + TBL_LP_SCHOOLS_MASTER);
//            iDB.execSQL("Drop table if exists " + TBL_LP_FITNESS_TEST_RESULT);
//            iDB.execSQL("Drop table if exists " + TBL_LP_FITNESS_TEST_CATEGORY);
//            iDB.execSQL("Drop table if exists " + TBL_LP_FITNESS_SKILL_TEST_RESULT);
//
//
//
//            iDB.execSQL("Drop table if exists " + TBL_LP_USER_LOGIN);
//            iDB.execSQL("Drop table if exists " + TBL_LP_TEST_COORDINATORS);
//
//            iDB.execSQL("Drop table if exists " + TBL_LP_STUDENT_MASTER);
//            iDB.execSQL("Drop table if exists " + TBL_LP_CAMP_MASTER);
//            iDB.execSQL("Drop table if exists " + TBL_LP_FITNESS_TEST_TYPES);
//
//            iDB.execSQL("Drop table if exists " + TBL_LP_SKILL_TEST_TYPE);
//            iDB.execSQL("Drop table if exists " + TBL_LP_USER_SCHOOL_MAPPING);
//
//            //iDB.execSQL("Drop table if exists " + TBL_LP_SCHOOL_CLASS_FLAG_MAPPING);
//            iDB.execSQL("Drop table if exists " + TBL_LP_CAMP_TEST_MAPPING);
//            iDB.execSQL("Drop table if exists " + TBL_LP_TEST_COORDINATOR_MAPPING);

            // end
//23.04.19


//
//            // RE-CREATE DATABSE STRUCTURE.
            //onCreate(iDB);


            // last code by rahul
            // for db version 11
//            Utility.logoutUser((Activity)context,true);
//            String query = "alter table "+ TBL_LP_FITNESS_TEST_RESULT + " add "+AppConfig.EXPORTED + " TEXT default 'false'";
//            iDB.execSQL(query);
//            Log.w(AppConfig.TAG,query);

            if (oldVersion==11) {
                // for upgrading to 12 version (oldversion = 11)
                iDB.execSQL(CREATE_STATE_MASTER_TABLE);
                Cursor cursor = iDB.query(TBL_STATE_MASTER, null, null, null, null, null, null);
                if (cursor.getCount() <= 0) {
                    iDB.execSQL(INSERT_INTO_STATE_MASTER_TABLE);
                }
                cursor.close();

                iDB.execSQL("ALTER table " + TBL_LP_SCHOOLS_MASTER + " add " + AppConfig.IS_ATTACHED + " INT DEFAULT 1");
            }
            // for upgrading to version 13(old version = 11 or 12)
            iDB.execSQL("alter table "+TBL_LP_SCHOOLS_MASTER + " add "+AppConfig.IS_RETEST_ALLOWED + " TEXT DEFAULT 'False'");
            iDB.execSQL("alter table "+TBL_LP_SCHOOLS_MASTER + " add "+AppConfig.FOR_RETEST + " INT DEFAULT 0");
            //iDB.execSQL("alter table "+TBL_LP_STUDENT_MASTER + " add "+AppConfig.IS_RETEST_ALLOWED + " INT DEFAULT 0");
            iDB.execSQL(CREATE_LP_FITNESS_RETEST_RESULT_TABLE);

        }


    }

    /**
     * create Tables - this is done to facilitate the facilities provided by
     * TRUNCATE
     */
    public void createTables(Context context) {
        try {
            iContext = context;

            if (iDB == null) {
                // OPENING DATABASE CONN.
                try {
                    DBManager.getInstance().openDB();
                } catch (Exception e) {
                    //
                }
            }


            new DatabaseHelper(context).onCreate(iDB);
        } catch (Exception e) {
            Log.e("[ " + TAG + " ]", "createTables()", e);

            e.printStackTrace();
        }
    }

    /**
     * CALL TO CREATE THE TABLE ON THE BASIS OF NAMED PARAMETER PASSED, IF THE
     * TABLE NOT EXISTS
     *
     * @param CREATE_NAMED_TABLE
     */
    public void createNamedTable(Context context, String CREATE_NAMED_TABLE) {

        try {
            iContext = context;
            if (iDB == null) {
                // OPENING DATABASE CONN.
                try {
                    DBManager.getInstance().openDB();
                } catch (Exception e) {
                    //
                }
            }
            new DatabaseHelper(context).onCreateNamedTable(iDB, CREATE_NAMED_TABLE);
        } catch (Exception e) {
            Log.e("["+TAG+"]:", "createNamedTable() : ", e);
        }
    }

    public void insertTables(String TBL_NAME, Object aData) {
        try {
            String query = "insert into ";

            if (TBL_NAME.equalsIgnoreCase(TBL_LP_USER_LOGIN)) {
                LoginModel temp = (LoginModel) aData;

                query += TBL_LP_USER_LOGIN + " (user_login_name, user_password, user_type_id, login_type, image_name, image_path, created_on, created_by, last_modified_on, last_modified_by) " + "values('" + temp.getUser_login_name() + "', '" + temp.getUser_password() + "', '" + temp.getUser_type_id() + "', '"
                        + temp.getLogin_type() + "', '" + temp.getImage_name() + "', '" + temp.getImage_path() + "', '" + temp.getCreated_on() + "', '" + temp.getCreated_by() + "', '" + temp.getLast_modified_on() + "', '" + temp.getLast_modified_by() + "' );";

                temp = null;
            } else if (TBL_NAME.equalsIgnoreCase(TBL_LP_TEST_COORDINATORS)) {
                TestCoordinatorsModel temp = (TestCoordinatorsModel) aData;
                query += TBL_LP_TEST_COORDINATORS + " (test_coordinator_id, test_coordinator_name, gender, email, phone, address, school_id, user_type_id, is_active, created_on, created_by, last_modified_on, last_modified_by, imei, android_id) " + "values('" + temp.getTest_coordinator_id() + "', '" + temp.getTest_coordinator_name() + "', '" + temp.getGender() + "', '"
                        + temp.getEmail() + "', '" + temp.getPhone() + "', '" + temp.getAddress() + "', '" + temp.getSchool_id() + "', '" + temp.getUser_type_id() + "', '" + temp.getIs_active() + "', '" + temp.getCreated_on() + "', '" + temp.getCreated_by() + "', '" + temp.getLast_modified_on() + "', '" + temp.getLast_modified_by() + "', '" + temp.getImei() + "', '" + temp.getAndroid_id() + "' );";
                //
                temp = null;
            } else if (TBL_NAME.equalsIgnoreCase(TBL_LP_SCHOOLS_MASTER)) {

                SchoolsMasterModel temp = (SchoolsMasterModel) aData;

//                query += TBL_LP_SCHOOLS_MASTER + " (school_id, school_name, school_chain_id, city_id, address, phone_no, email, web_address, school_description, school_image_name, school_image_path, created_on, created_by, last_modified_on, last_modified_by, school_logo_name, school_logo_path, school_alias_name, is_active, trainer_coordinator_id, seniors_starting_from, latitude, longitude, school_start_time, office) " + "values('" + temp.getSchool_id() + "', '" + temp.getSchool_name() + "', '" + temp.getSchool_chain_id() + "', '"
//                        + temp.getCity_id() + "', '" + temp.getAddress() + "', '" + temp.getPhone_no() + "', '" + temp.getEmail() + "', '" + temp.getWeb_address() + "', '" + temp.getSchool_description() + "', '" + temp.getSchool_image_name() + "', '" + temp.getSchool_image_path() + "', '" + temp.getCreated_on() + "', '" + temp.getCreated_by() + "', '" + temp.getLast_modified_on() + "', '" + temp.getLast_modified_by() + "', '" + temp.getSchool_logo_name() + "', '" + temp.getSchool_logo_path() + "', '" + temp.getSchool_alias_name() + "', '" + temp.getIs_active() + "', '" + temp.getTrainer_coordinator_id() + "', '" + temp.getSeniors_starting_from() + "', '" + temp.getLatitude() + "', '" + temp.getLongitude() + "', '" + temp.getSchool_start_time() +"', '" + temp.getOffice() + "' );";

//                query += TBL_LP_SCHOOLS_MASTER + " (school_id, school_name, school_chain_id, city_id, address, phone_no, email, web_address, school_description, school_image_name, school_image_path, created_on, created_by, last_modified_on, last_modified_by, school_logo_name, school_logo_path, school_alias_name, is_active, trainer_coordinator_id, seniors_starting_from, latitude, longitude, school_start_time, office, " + AppConfig.IS_ATTACHED + ") " + "values('" + temp.getSchool_id() + "', '" + temp.getSchool_name() + "', '" + temp.getSchool_chain_id() + "', '"
//                        + temp.getCity_id() + "', '" + temp.getAddress() + "', '" + temp.getPhone_no() + "', '" + temp.getEmail() + "', '" + temp.getWeb_address() + "', '" + temp.getSchool_description() + "', '" + temp.getSchool_image_name() + "', '" + temp.getSchool_image_path() + "', '" + temp.getCreated_on() + "', '" + temp.getCreated_by() + "', '" + temp.getLast_modified_on() + "', '" + temp.getLast_modified_by() + "', '" + temp.getSchool_logo_name() + "', '" + temp.getSchool_logo_path() + "', '" + temp.getSchool_alias_name() + "', '" + temp.getIs_active() + "', '" + temp.getTrainer_coordinator_id() + "', '" + temp.getSeniors_starting_from() + "', '" + temp.getLatitude() + "', '" + temp.getLongitude() + "', '" + temp.getSchool_start_time() +"', '" + temp.getOffice() + "', '" + temp.getIsAttached()+"' );";

                query += TBL_LP_SCHOOLS_MASTER + " (school_id, school_name, school_chain_id, city_id, address, phone_no, email, web_address, school_description, school_image_name, school_image_path, created_on, created_by, last_modified_on, last_modified_by, school_logo_name, school_logo_path, school_alias_name, is_active, trainer_coordinator_id, seniors_starting_from, latitude, longitude, school_start_time, office, " + AppConfig.IS_ATTACHED + "," + AppConfig.IS_RETEST_ALLOWED + "," + AppConfig.FOR_RETEST + ") values('" + temp.getSchool_id() + "', '" + temp.getSchool_name() + "', '" + temp.getSchool_chain_id() + "', '"
                        + temp.getCity_id() + "', '" + temp.getAddress() + "', '" + temp.getPhone_no() + "', '" + temp.getEmail() + "', '" + temp.getWeb_address() + "', '" + temp.getSchool_description() + "', '" + temp.getSchool_image_name() + "', '" + temp.getSchool_image_path() + "', '" + temp.getCreated_on() + "', '" + temp.getCreated_by() + "', '" + temp.getLast_modified_on() + "', '" + temp.getLast_modified_by() + "', '" + temp.getSchool_logo_name() + "', '" + temp.getSchool_logo_path() + "', '" + temp.getSchool_alias_name() + "', '" + temp.getIs_active() + "', '" + temp.getTrainer_coordinator_id() + "', '" + temp.getSeniors_starting_from() + "', '" + temp.getLatitude() + "', '" + temp.getLongitude() + "', '" + temp.getSchool_start_time() +"', '" + temp.getOffice() + "', '" + temp.getIsAttached()+"','"+temp.getIsRetestAllowed()+"'," + temp.getForRetest()+ " );";


                temp = null;

            } else if (TBL_NAME.equalsIgnoreCase(TBL_LP_STUDENT_USER_MASTER)) {

                StudentUserMasterModel temp = (StudentUserMasterModel) aData;

                query += TBL_LP_STUDENT_USER_MASTER + " (guardian_name, image_path, phone, student_name, user_login_name, address, current_class, date_of_birth, gender) " + "values('" + temp.getGuardian_name() + "', '" + temp.getImage_path() + "', '" + temp.getPhone() + "', '"
                        + temp.getStudent_name() + "', '" + temp.getUser_login_name() + "', '" + temp.getAddress() + "', '" + temp.getCurrent_class() + "', '" + temp.getDate_of_birth() + "', '" + temp.getGender() + "' );";
                //

                temp = null;
            } else if (TBL_NAME.equalsIgnoreCase(TBL_LP_STUDENT_MASTER)) {

                StudentMasterModel temp = (StudentMasterModel) aData;
                query += TBL_LP_STUDENT_MASTER + " (student_name, current_school_id, current_roll_num, gender, dob, email, gaurdian_name, phone, address, created_on, created_by, last_modified_on, last_modified_by, student_id, student_registration_num, current_class, class_id, class_partition_id, section, is_active, camp_id) " + "values('" + temp.getStudent_name() + "', '" + temp.getCurrent_school_id() + "', '" + temp.getCurrent_roll_num() + "', '" + temp.getGender() + "', '" + temp.getDob() + "', '" + temp.getEmail() + "', '" + temp.getGaurdian_name() + "', '" + temp.getPhone() + "', '" + temp.getAddress() + "', '" + temp.getCreated_on() + "', '" + temp.getCreated_by() + "', '" + temp.getLast_modified_on() + "', '" + temp.getLast_modified_by() + "', '" + temp.getStudent_id() + "', '" + temp.getStudent_registration_num() + "', '" + temp.getCurrent_class() + "', '" + temp.getClass_id() + "', '" + temp.getClass_partition_id() + "', '" + temp.getSection() + "', '" + temp.getIs_active() + "' , '" + temp.getCamp_id() + "' );";
                //
                temp = null;

            } else if (TBL_NAME.equalsIgnoreCase(TBL_LP_CAMP_MASTER)) {

                CampMasterModel temp = (CampMasterModel) aData;
                query += TBL_LP_CAMP_MASTER + " (camp_id, camp_name, camp_type, school_id, venue_id, start_date, end_date, camp_coordination_id, questionaire_id, registration_coordinator_id, status, data_flag) " + "values('" + temp.getCamp_id() + "', '" + temp.getCamp_name() + "', '" + temp.getCamp_type() + "', '" + temp.getSchool_id() + "', '" + temp.getVenue_id() + "', '" + temp.getStart_date() + "', '" + temp.getEnd_date() + "', '" + temp.getCamp_coordination_id() + "', '" + temp.getQuestionaire_id() + "', '" + temp.getRegistration_coordinator_id() + "', '" + temp.getStatus() + "', '" + temp.getData_flag() + "' );";
                //
                temp = null;

            } else if (TBL_NAME.equalsIgnoreCase(TBL_LP_FITNESS_TEST_TYPES)) {

                FitnessTestTypesModel temp = (FitnessTestTypesModel) aData;
                query += TBL_LP_FITNESS_TEST_TYPES + " (test_type_id, test_name, excel_name, score_measurement, score_unit, score_criteria, test_description, test_performed, created_on, created_by, last_modified_on, last_modified_by, test_category_id, test_image, test_img_path, school_id, test_coordinator_id) " + "values('" + temp.getTest_type_id() + "', '" + temp.getTest_name() + "', '" + temp.getExcel_name() + "', '" + temp.getScore_measurement() + "', '" + temp.getScore_unit() + "', '" + temp.getScore_criteria() + "', '" + temp.getTest_description() + "', '" + temp.getTest_performed() + "', '" + temp.getCreated_on() + "', '" + temp.getCreated_by() + "', '" + temp.getLast_modified_on() + "', '" + temp.getLast_modified_by() + "', '" + temp.getTest_category_id() + "', '" + temp.getTest_image() + "', '" + temp.getTest_img_path() + "', '" + temp.getSchool_id() + "', '" + temp.getTest_coordinator_id() + "' );";
                //
                temp = null;

            } else if (TBL_NAME.equalsIgnoreCase(TBL_LP_FITNESS_TEST_RESULT) || TBL_NAME.equalsIgnoreCase(TBL_LP_FITNESS_RETEST_RESULT)) {

                FitnessTestResultModel temp = (FitnessTestResultModel) aData;
                query += TBL_NAME + " (student_id, camp_id, test_type_id, test_coordinator_id, score, percentile, created_on, created_by, last_modified_on, last_modified_by, latitude, longitude, test_name, tested, sub_test_name, device_date, synced) " + "values('" + temp.getStudent_id() + "', '" + temp.getCamp_id() + "', '" + temp.getTest_type_id() + "', '" + temp.getTest_coordinator_id() + "', '" + temp.getScore() + "', '" + temp.getPercentile() + "', '" + temp.getCreated_on() + "', '" + temp.getCreated_by() + "', '" + temp.getLast_modified_on() + "', '" + temp.getLast_modified_by() + "', '" + temp.getLatitude() + "', '" + temp.getLongitude() + "', '" + temp.getTestName() + "', '" + temp.isTestedOrNot() +  "', '" + temp.getSubTestName() + "', '" + temp.getDevice_date() + "', '" + temp.isSyncedOrNot() +"' );";
                //
                temp = null;
            } else if (TBL_NAME.equalsIgnoreCase(TBL_LP_FITNESS_TEST_CATEGORY)) {

                FitnessTestCategoryModel temp = (FitnessTestCategoryModel) aData;
                query += TBL_LP_FITNESS_TEST_CATEGORY + " (sub_test_id, test_id, test_description, purpose, Administrative_Suggestions, Equipment_Required, scoring, test_name, score_criteria, score_measurement, name, created_on, last_modified_on, score_unit, tests_applicable, multiple_lane, timer_type, final_position, video_link) " + "values('" + temp.getSub_test_id() + "', '" + temp.getTest_id() + "', '" + temp.getTest_description() + "', '" + temp.getPurpose() + "', '" + temp.getAdministrative_Suggestions() + "', '" + temp.getEquipment_Required() + "', '" + temp.getScoring() + "', '" + temp.getTest_name() + "', '" + temp.getScore_criteria() + "', '" + temp.getScore_measurement() + "', '" + temp.getName() + "', '" + temp.getCreated_on() + "', '" + temp.getLast_modified_on() + "', '" + temp.getScore_unit() + "', '" + temp.getTest_applicable() + "', '" + temp.getMultipleLane() + "', '" + temp.getTimerType() +"', '" + temp.getFinalPosition() + "', '" + temp.getVideoLink() + "');";

                //
                temp = null;
            } else if (TBL_NAME.equalsIgnoreCase(TBL_LP_SKILL_TEST_TYPE)) {

                SkillTestTypeModel temp = (SkillTestTypeModel) aData;
                query += TBL_LP_SKILL_TEST_TYPE + " (checklist_item_id, item_name, test_type_id, created_on, synced, last_modified_on ) " + "values('" + temp.getChecklist_item_id() + "', '" + temp.getItem_name() + "', '" + temp.getTest_type_id() + "', '" + temp.getCreated_on() + "', '" + temp.getSynced() + "', '" + temp.getLast_modified_on() + "' );";
                //
                temp = null;
            } else if (TBL_NAME.equalsIgnoreCase(TBL_LP_USER_SCHOOL_MAPPING)) {

                UserSchoolMappingModel temp = (UserSchoolMappingModel) aData;
                query += TBL_LP_USER_SCHOOL_MAPPING + " (user_id, school_id, created_on, last_modified_on) " + "values('" + temp.getUser_id() + "', '" + temp.getSchool_id() + "', '" + temp.getCreated() + "', '" + temp.getModified() + "' );";
                //
                temp = null;
            } else if (TBL_NAME.equalsIgnoreCase(TBL_LP_FITNESS_SKILL_TEST_RESULT)) {

                FitnessSkillTestResultModel temp = (FitnessSkillTestResultModel) aData;
                String synced=temp.getSynced()?"1":"0";
                query += TBL_LP_FITNESS_SKILL_TEST_RESULT + " (skill_score_id, student_id, camp_id, test_type_id, checklist_item_id, coordinator_id, score, created_on, longitude, latitude, last_modified_on, tested, device_date, synced) " + "values('" + temp.getSkill_score_id() + "', '" + temp.getStudent_id() + "', '" + temp.getCamp_id() + "', '" + temp.getTest_type_id() + "','" + temp.getChecklist_item_id() + "', '" + temp.getCoordinator_id() + "', '" + temp.getScore() + "', '" + temp.getCreated_on() + "', '"  + temp.getLongitude() + "', '" + temp.getLatitude() + "', '" + temp.getLast_modified_on() + "', '" + temp.getTested() + "', '" +  temp.getDevice_date() + "', '" +synced + "' );";
                //
                temp = null;
            } else if (TBL_NAME.equalsIgnoreCase(TBL_LP_SCHOOL_CLASS_FLAG_MAPPING)) {

                SchoolClassMapping temp = (SchoolClassMapping) aData;
                query += TBL_LP_SCHOOL_CLASS_FLAG_MAPPING + " (school_id, class_name, class_id, flag) " + "values('" + temp.getSchool_id() + "', '" + temp.getClass_name() + "', '" + temp.getClass_id() + "', '" + temp.getFlag() + "' );";
                //
                temp = null;
            } else if (TBL_NAME.equalsIgnoreCase(TBL_LP_CAMP_TEST_MAPPING)) {

                CampTestMapping temp = (CampTestMapping) aData;
                query += TBL_LP_CAMP_TEST_MAPPING + " (camp_id, school_id, test_type_id, class_id) " + "values('" + temp.getCamp_id() + "', '" + temp.getSchool_id() + "', '" + temp.getTest_type_id() + "', '" + temp.getClass_ID() + "' );";
                //
                temp = null;
            } else if (TBL_NAME.equalsIgnoreCase(TBL_LP_TEST_COORDINATOR_MAPPING)) {

                TestCoordinatorMapping temp = (TestCoordinatorMapping) aData;
                query += TBL_LP_TEST_COORDINATOR_MAPPING + " (camp_id, school_id, test_type_id) " + "values('" + temp.getCamp_id() + "', '" + temp.getSchool_id() + "', '" + temp.getTest_type_id() + "' );";
                //
                temp = null;
            }
            else if(TBL_NAME.equalsIgnoreCase(TBL_LP_MY_DART_CLASS_TABLE)){

                MyDartClassModel temp=(MyDartClassModel)aData;
                query+=TBL_LP_MY_DART_CLASS_TABLE+ " (test_coordinator_id,current_school_id,class, class_id) "+"values('"+ temp.getTest_coordinator_id() +"', '" + temp.getSchool_id() +"', '" + temp.getClasss() +"', '"+temp.getClass_id()+"' ) ;";

            }
            else if(TBL_NAME.equalsIgnoreCase(TBL_LP_MY_DART_PERIOD_TABLE)){
                MyDartPeriodModel temp=(MyDartPeriodModel) aData;
                query+=TBL_LP_MY_DART_PERIOD_TABLE+ " (test_coordinator_id,current_school_id,period) "+"values('" + temp.getTest_coordinator_id() +"', '" + temp.getSchool_id() +"', '" +temp.getPeriod()+"' ) ;";

            } else if(TBL_NAME.equalsIgnoreCase(TBL_LP_MY_DART_GRADE_TABLE)){
                MyDartGradeModel temp=(MyDartGradeModel) aData;
                query+=TBL_LP_MY_DART_GRADE_TABLE+ " (current_school_id,grade,grade_id) "+"values('" +temp.getCurrent_school_id() +"', '" + temp.getGrade() +"', '" + temp.getGrade_id() +"' ) ;";

            }

            else if(TBL_NAME.equalsIgnoreCase(TBL_LP_MY_DART_SPORT_TABLE)){
                MyDartSportModel temp=(MyDartSportModel) aData;
                query+=TBL_LP_MY_DART_SPORT_TABLE+ " (test_coordinator_id,current_school_id,current_class,sport,sport_id) "+"values('" + temp.getTest_coordinator_id() +"', '" + temp.getSchool_id() +"', '" +temp.getCurrent_class() +"', '"+temp.getSport()+"', '" +temp.getSport_id()+"' ) ;";

            }
            else if(TBL_NAME.equalsIgnoreCase(TBL_LP_MY_DART_OTHER_TABLE)){
                MyDartOtherModel temp=(MyDartOtherModel) aData;
                query+=TBL_LP_MY_DART_OTHER_TABLE+ " (test_coordinator_id,current_school_id,other,other_id) "+"values('" + temp.getTest_coordinator_id() +"', '" + temp.getSchool_id() +"', '" +temp.getOther()+"', '" +temp.getOther_id()+"' ) ;";

            }
            else if(TBL_NAME.equalsIgnoreCase(TBL_LP_MY_DART_LESSON_PLAN_TABLE)){
                MyDartLessonPlanModel temp=(MyDartLessonPlanModel) aData;
                query+=TBL_LP_MY_DART_LESSON_PLAN_TABLE+ " (test_coordinator_id,current_school_id,class_id,lesson,lesson_id) "+"values('" + temp.getTest_coordinator_id() +"', '" + temp.getSchool_id() +"', '" +temp.getClass_id()+"', '" +temp.getLesson()+"', '" +temp.getLesson_id()+"' ) ;";

            } else if(TBL_NAME.equalsIgnoreCase(TBL_LP_MY_DART_SPORT_SKILL_TABLE)){
                MyDartSportSkillModel temp=(MyDartSportSkillModel) aData;
                query+=TBL_LP_MY_DART_SPORT_SKILL_TABLE+ " (test_coordinator_id,current_school_id,sport_test_id,sport_skill,sport_skill_id,technique_id) "+"values('" + temp.getTest_coordinator_id() +"', '" + temp.getSchool_id() +"', '" +temp.getSport_test_id()+"', '" +temp.getSport_skill()+"', '" +temp.getSport_skill_id()+"', '" +temp.getTechnique_id()+"' ) ;";

            }else if(TBL_NAME.equalsIgnoreCase(TBL_LP_MY_DART_STUDENT_TABLE)){
                MyDartStudentModel temp=(MyDartStudentModel) aData;
                query+=TBL_LP_MY_DART_STUDENT_TABLE+ " (test_coordinator_id,current_school_id,current_class,sport_skill_id,student_name,student_id,student_grade_id) "+"values('" + temp.getTest_coordinator_id() +"', '" + temp.getSchool_id() +"', '" +temp.getCurrent_class()+"', '" +temp.getSport_skill_id()+"', '"+temp.getStudent_name()+"', '" +temp.getStudent_id()+"', '" +temp.getStudent_grade_id()+"' ) ;";

            }else if(TBL_NAME.equalsIgnoreCase(TBL_LP_MY_DART_INSERT_ACTIVITY_TABLE)){
                MyDartInsertActivityModel temp=(MyDartInsertActivityModel) aData;
                query+=TBL_LP_MY_DART_INSERT_ACTIVITY_TABLE+ " (test_coordinator_id,current_school_id,data,activity_id,student_activity_id) "+"values('" + temp.getTest_coordinator_id() +"', '" + temp.getSchool_id() +"', '" +temp.getData()+"', '" +temp.getActivity_id()+"', '" +temp.getStudent_activity_id()+"' ) ;";

            }
            else if(TBL_NAME.equalsIgnoreCase(TBL_LP_SCREEN_MASTER)){
                ScreenMasterModel temp=(ScreenMasterModel) aData;
                query+=TBL_LP_SCREEN_MASTER+ " (screen_id, parent_id, screen_name, web_url, mobile_icon_title, icon_image_name, icon_path, created_on, created_by, modified_on, modified_by, is_deleted  ) "+"values('" + temp.getScreen_id() +"', '" + temp.getPartner_id() +"', '" +temp.getScreen_name()+"', '" +temp.getWeb_url()+"', '" +temp.getMobile_icon_title()+"', '" +temp.getIcon_image_name()+"', '" +temp.getIcon_path()+"', '" +temp.getCreated_on()+"', '" +temp.getCreated_by()+"', '" +temp.getModified_on()+"', '" +temp.getModified_by()+"', '" +temp.getIs_deleted()+"' ) ;";


            } else if(TBL_NAME.equalsIgnoreCase(TBL_LP_USER_SCREEN_MAP)){
                UserScreenMapModel temp=(UserScreenMapModel) aData;
                query+=TBL_LP_USER_SCREEN_MAP+ " (mapping_id, screen_id, user_type_id, created_on, created_by, modified_on, modified_by, is_active  ) "+"values('" + temp.getMapping_id() +"', '" + temp.getScreen_id() +"', '" +temp.getUser_type_id()+"', '" +temp.getCreated_on()+"', '" +temp.getCreated_by()+"', '" +temp.getModified_on()+"', '" +temp.getModified_by()+"', '" +temp.getIs_active()+"' ) ;";

                Log.e(TAG,"insert user screen map query==> "+query);
            }
            else if(TBL_NAME.equalsIgnoreCase(TBL_LP_MORNING_TRACKING)){
                Log.e(TAG,"***inside else if== ");
                LocationTrackingModel temp=(LocationTrackingModel) aData;
                query+=TBL_LP_MORNING_TRACKING+ " (latitude, longitude, address, created_on, trainer_id ) "+"values('" + temp.getLat() +"', '" + temp.getLng() +"', '" +temp.getCurrent_address()+"', '" +temp.getCreated_on()+"', '" +temp.getTrainer_id()+"' ) ;";


            }




            if (iDB == null) {
                // OPENING DATABASE CONN.
                Log.e(TAG,"***iDB== "+iDB);
                try {
                    DBManager.getInstance().openDB();
                } catch (Exception e) {
                    Log.e("["+TAG+"]:", "insertTables() :", e);
                }
            }
            //  Log.e(TAG,"***iDB again== "+iDB);
            iDB.execSQL(query);
        } catch (Exception e) {
            Log.e("[" + TAG + "]: ", "insertTables(): ", e);
        }
    }


    public void insertStudents(List<StudentModel.ResultBean.StudentBean> students, SchoolActivity.UpdateDB reference){
        int count = 0;
        if (iDB == null) {
            // OPENING DATABASE CONN.
            Log.e(TAG,"***iDB== "+iDB);
            try {
                DBManager.getInstance().openDB();
            } catch (Exception e) {
                //Log.e("["+TAG+"]:", "insertTables() :", e);
            }
        }

//        SQLiteDatabase sqLiteDatabase = iDbHelper.getWritableDatabase();
//        sqLiteDatabase.beginTransaction();

        iDB.beginTransaction();

        StudentMasterModel student_model = new StudentMasterModel();
        for (int i = 0; i < students.size(); i++) {
            StudentModel.ResultBean.StudentBean studentBean = students.get(i);

            student_model.setStudent_name(studentBean.getStudent_Name());
            student_model.setCurrent_class(studentBean.getCurrent_Class());
            student_model.setCurrent_roll_num(studentBean.getCurrent_Roll_Num());
            student_model.setDob(studentBean.getDOB());
            student_model.setGender(Integer.parseInt(studentBean.getGender()));
            student_model.setSection(studentBean.getSection());
            //student_model.setStudent_liveplus_id(studentBean.getStudent_LivePLUS_ID());
            //student_model.setUser_login_id(0);
            student_model.setCurrent_school_id(studentBean.getCurrent_School_ID());
            student_model.setCamp_id(studentBean.getCampID());
            student_model.setStudent_id(Integer.parseInt(studentBean.getStudentID()));
            student_model.setClass_id(studentBean.getClassID());
            student_model.setStudent_registration_num(studentBean.getStudentRegistration());
            student_model.setCreated_on(studentBean.getCreated_On());
            student_model.setLast_modified_on(studentBean.getLast_Modified_On());
            // student_model.setClass_partition_id(studentBean.getcla);

                 /*   Log.e("SchoolActivity","class_id=> "+studentBean.getClassID());
                    Log.e("SchoolActivity","setCurrent_class=> "+studentBean.getCurrent_Class());
                    Log.e("SchoolActivity","setCurrent_roll_num=> "+studentBean.getCurrent_Roll_Num());*/

            // first check for existence

            String subString = studentBean.getStudentID();
            HashMap<String, String> map = null;
            try {
                map = getParticularRow(iContext,DBManager.TBL_LP_STUDENT_MASTER,
                        "current_school_id", Constant.SCHOOL_ID, AppConfig.STUDENT_ID, subString);
            } catch(Exception e){
                e.printStackTrace();
            }

            if(map.size()==0) {
                insertTables(TBL_LP_STUDENT_MASTER, student_model);

            }
            else {
                deleteStudentRow(iContext,DBManager.TBL_LP_STUDENT_MASTER,
                        "current_school_id", Constant.SCHOOL_ID, AppConfig.STUDENT_ID, subString);
                insertTables(TBL_LP_STUDENT_MASTER, student_model);
            }
            if (i == 0) {
                Constant.classList.clear();
                Constant.classIdList.clear();
                Constant.classList1.clear();
                Constant.classIdList1.clear();
                Constant.testList.clear();
                Constant.testIdList.clear();
                Constant.classList2.clear();
                Constant.classIdList2.clear();
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(iContext);
                SharedPreferences.Editor e = sp.edit();
                e.putString(AppConfig.CAMP_ID, studentBean.getCampID());
                e.putString(AppConfig.CAMP_NAME,studentBean.getCampName());
                e.remove("got_all_camp");
                e.remove("got_all_test");
                e.commit();

            }

//                    for (; count <= paramVarArgs[0]; count++) {
//                        try {
//                          Thread.sleep(1000);
//                            publishProgress(count);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }

            count++;
            reference.doProgress(count,students.size());
        }
        iDB.setTransactionSuccessful();
        iDB.endTransaction();

        //  Log.e(TAG,"***iDB again== "+iDB);

    }

    public void insertStudentsForRefreshing(List<StudentModel.ResultBean.StudentBean> students, TakeTestActivity.UpdateDB reference){
        int count = 0;
        if (iDB == null) {
            // OPENING DATABASE CONN.
            Log.e(TAG,"***iDB== "+iDB);
            try {
                DBManager.getInstance().openDB();
            } catch (Exception e) {
                //Log.e("["+TAG+"]:", "insertTables() :", e);
            }
        }

//        SQLiteDatabase sqLiteDatabase = iDbHelper.getWritableDatabase();
//        sqLiteDatabase.beginTransaction();

        iDB.beginTransaction();
//        iDB.execSQL("delete from "+TBL_LP_STUDENT_MASTER);
        truncateTable(TBL_LP_STUDENT_MASTER);

        StudentMasterModel student_model = new StudentMasterModel();
        for (int i = 0; i < students.size(); i++) {
            StudentModel.ResultBean.StudentBean studentBean = students.get(i);

            student_model.setStudent_name(studentBean.getStudent_Name());
            student_model.setCurrent_class(studentBean.getCurrent_Class());
            student_model.setCurrent_roll_num(studentBean.getCurrent_Roll_Num());
            student_model.setDob(studentBean.getDOB());
            student_model.setGender(Integer.parseInt(studentBean.getGender()));
            student_model.setSection(studentBean.getSection());
            //student_model.setStudent_liveplus_id(studentBean.getStudent_LivePLUS_ID());
            //student_model.setUser_login_id(0);
            student_model.setCurrent_school_id(studentBean.getCurrent_School_ID());
            student_model.setCamp_id(studentBean.getCampID());
            student_model.setStudent_id(Integer.parseInt(studentBean.getStudentID()));
            student_model.setClass_id(studentBean.getClassID());
            student_model.setStudent_registration_num(studentBean.getStudentRegistration());
            student_model.setCreated_on(studentBean.getCreated_On());
            student_model.setLast_modified_on(studentBean.getLast_Modified_On());

            insertTables(TBL_LP_STUDENT_MASTER, student_model);

            if (i == 0) {
                Constant.classList.clear();
                Constant.classIdList.clear();
                Constant.testList.clear();
                Constant.testIdList.clear();
                Constant.classList2.clear();
                Constant.classIdList2.clear();
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(iContext);
                SharedPreferences.Editor e = sp.edit();
                e.putString("camp_id", studentBean.getCampID());
                e.putString(AppConfig.CAMP_NAME,studentBean.getCampName());
                e.remove("got_all_camp");
                e.remove("got_all_test");
                e.commit();

            }

//                    for (; count <= paramVarArgs[0]; count++) {
//                        try {
//                          Thread.sleep(1000);
//                            publishProgress(count);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }

            count++;
            reference.doProgress(count,students.size());
        }
        iDB.setTransactionSuccessful();
        iDB.endTransaction();

        //  Log.e(TAG,"***iDB again== "+iDB);

    }


    public void insertTables1(String TBL_NAME, Object aData, Context ctx) {

        iContext = ctx;
        try {
            String query = "insert into ";

            if(TBL_NAME.equalsIgnoreCase(TBL_LP_MORNING_TRACKING)){
                Log.e(TAG,"***inside else if== ");
                LocationTrackingModel temp=(LocationTrackingModel) aData;
                query+=TBL_LP_MORNING_TRACKING+ " (latitude, longitude, address, created_on, trainer_id ) "+"values('" + temp.getLat() +"', '" + temp.getLng() +"', '" +temp.getCurrent_address()+"', '" +temp.getCreated_on()+"', '" +temp.getTrainer_id()+"' ) ;";


            }




            if (iDB == null) {
                // OPENING DATABASE CONN.
                Log.e(TAG,"***iDB== "+iDB);
                try {
                    DBManager.getInstance().openDB();
                } catch (Exception e) {
                    Log.e("["+TAG+"]:", "insertTables() :", e);
                }
            }
            //   Log.e(TAG,"***iDB again== "+iDB);
            iDB.execSQL(query);

        } catch (Exception e) {
            Log.e("[" + TAG + "]: ", "insertTables(): ", e);
        }
    }


    /**
     * update entry in particular table
     */


//    public void updateTables(String TBL_NAME, Object aData, String ARGS1, String ARGS2, String ARGS3, String ARGS4) {
//
//        if (iDB == null) {
//            // OPENING DATABASE CONN.
//            try {
//                DBManager.getInstance().openDB();
//            } catch (Exception e) {
//                Log.e("["+TAG+"]:", "updateTables() :", e);
//            }
//        }
//        String query = "update ";
//        try {
//            ContentValues args = new ContentValues();
//            if (TBL_NAME.equalsIgnoreCase(TBL_LP_USER_LOGIN)) {
//                LoginModel temp = (LoginModel) aData;
//
//                query += TBL_LP_USER_LOGIN + " (user_login_id, user_login_name, user_password, user_type_id, login_type, image_name, image_path, created_on, created_by, last_modified_on, last_modified_by) " + "values('" + temp.getUser_login_id() + "', '" + temp.getUser_login_name() + "', '" + temp.getUser_password() + "', '" + temp.getUser_type_id() + "', '"
//                        + temp.getLogin_type() + "', '" + temp.getImage_name() + "', '" + temp.getImage_path() + "', '" + temp.getCreated_on() + "', '" + temp.getCreated_by() + "', '" + temp.getLast_modified_on() + "', '" + temp.getLast_modified_by() + "' );";
//
//                temp = null;
//            } else if (TBL_NAME.equalsIgnoreCase(TBL_LP_TEST_COORDINATORS)) {
//                TestCoordinatorsModel temp = (TestCoordinatorsModel) aData;
//                query += TBL_LP_TEST_COORDINATORS + " (test_coordinator_id, test_coordinator_name, gender, email, phone, address, school_id, user_type_id, is_active, created_on, created_by, last_modified_on, last_modified_by, imei, android_id) " + "values('" + temp.getTest_coordinator_id() + "', '" + temp.getTest_coordinator_name() + "', '" + temp.getGender() + "', '"
//                        + temp.getEmail() + "', '" + temp.getPhone() + "', '" + temp.getAddress() + "', '" + temp.getSchool_id() + "', '" + temp.getUser_type_id() + "', '" + temp.getIs_active() + "', '" + temp.getCreated_on() + "', '" + temp.getCreated_by() + "', '" + temp.getLast_modified_on() + "', '" + temp.getLast_modified_by() + "', '" + temp.getImei() + "', '" + temp.getAndroid_id() + "' );";
//                //
//                temp = null;
//            } else if (TBL_NAME.equalsIgnoreCase(TBL_LP_SCHOOLS_MASTER)) {
//
//                SchoolsMasterModel temp = (SchoolsMasterModel) aData;
//
//                query += TBL_LP_SCHOOLS_MASTER + " (school_id, school_name, school_chain_id, city_id, address, phone_no, email, web_address, school_description, school_image_name, school_image_path, created_on, created_by, last_modified_on, last_modified_by, school_logo_name, school_logo_path, school_alias_name, is_active, trainer_coordinator_id, seniors_starting_from, latitude, longitude, school_start_time, office) " + "values('" + temp.getSchool_id() + "', '" + temp.getSchool_name() + "', '" + temp.getSchool_chain_id() + "', '"
//                        + temp.getCity_id() + "', '" + temp.getAddress() + "', '" + temp.getPhone_no() + "', '" + temp.getEmail() + "', '" + temp.getWeb_address() + "', '" + temp.getSchool_description() + "', '" + temp.getSchool_image_name() + "', '" + temp.getSchool_image_path() + "', '" + temp.getCreated_on() + "', '" + temp.getCreated_by() + "', '" + temp.getLast_modified_on() + "', '" + temp.getLast_modified_by() + "', '" + temp.getSchool_logo_name() + "', '" + temp.getSchool_logo_path() + "', '" + temp.getSchool_alias_name() + "', '" + temp.getIs_active() + "', '" + temp.getTrainer_coordinator_id() + "', '" + temp.getSeniors_starting_from() + "', '" + temp.getLatitude() + "', '" + temp.getLongitude() + "', '" + temp.getSchool_start_time() +"', '" + temp.getOffice()+ "' );";
//                //
//
//                temp = null;
//
//            } else if (TBL_NAME.equalsIgnoreCase(TBL_LP_STUDENT_MASTER)) {
//
//                StudentMasterModel temp = (StudentMasterModel) aData;
//
//                query += TBL_LP_STUDENT_MASTER + " (student_liveplus_id, user_login_id, student_name, current_school_id, current_roll_num, gender, dob, email, gaurdian_name, phone, address, created_on, created_by, last_modified_on, last_modified_by, student_id, student_registration_num, current_class, class_id, class_partition_id, section, is_active, camp_id) " + " values('" + temp.getStudent_liveplus_id() + "', '" + temp.getUser_login_id() + "', '" + temp.getStudent_name() + "', '" + temp.getCurrent_school_id() + "', '" + temp.getCurrent_roll_num() + "', '" + temp.getGender() + "', '" + temp.getDob() + "', '" + temp.getEmail() + "', '" + temp.getGaurdian_name() + "', '" + temp.getPhone() + "', '" + temp.getAddress() + "', '" + temp.getCreated_on() + "', '" + temp.getCreated_by() + "', '" + temp.getLast_modified_on() + "', '" + temp.getLast_modified_by() + "', '" + temp.getStudent_id() + "', '" + temp.getStudent_registration_num() + "', '" + temp.getCurrent_class() + "', '" + temp.getClass_id() + "', '" + temp.getClass_partition_id() + "', '" + temp.getSection() + "', '" + temp.getIs_active() + "' , '" + temp.getCamp_id() + "' );";
//
//                Log.e("DBManager ", "query==> " + query);
//                temp = null;
//
//            } else if (TBL_NAME.equalsIgnoreCase(TBL_LP_CAMP_MASTER)) {
//
//                CampMasterModel temp = (CampMasterModel) aData;
//                query += TBL_LP_CAMP_MASTER + " (camp_id, camp_name, camp_type, school_id, venue_id, start_date, end_date, camp_coordination_id, questionaire_id, registration_coordinator_id, status, data_flag) " + "values('" + temp.getCamp_id() + "', '" + temp.getCamp_name() + "', '" + temp.getCamp_type() + "', '" + temp.getSchool_id() + "', '" + temp.getVenue_id() + "', '" + temp.getStart_date() + "', '" + temp.getEnd_date() + "', '" + temp.getCamp_coordination_id() + "', '" + temp.getQuestionaire_id() + "', '" + temp.getRegistration_coordinator_id() + "', '" + temp.getStatus() + "', '" + temp.getData_flag() + "' );";
//                //
//                temp = null;
//
//            } else if (TBL_NAME.equalsIgnoreCase(TBL_LP_FITNESS_TEST_TYPES)) {
//
//                FitnessTestTypesModel temp = (FitnessTestTypesModel) aData;
//                query += TBL_LP_FITNESS_TEST_TYPES + " (test_type_id, test_name, excel_name, score_measurement, score_unit, score_criteria, test_description, test_performed, created_on, created_by, last_modified_on, last_modified_by, test_image, test_img_path, school_id, test_coordinator_id) " + "values('" + temp.getTest_type_id() + "', '" + temp.getTest_name() + "', '" + temp.getExcel_name() + "', '" + temp.getScore_measurement() + "', '" + temp.getScore_unit() + "', '" + temp.getScore_criteria() + "', '" + temp.getTest_description() + "', '" + temp.getTest_performed() + "', '" + temp.getCreated_on() + "', '" + temp.getCreated_by() + "', '" + temp.getLast_modified_on() + "', '" + temp.getLast_modified_by() + "', '" + temp.getTest_image() + "', '" + temp.getTest_img_path() + "', '" + temp.getSchool_id() + "', '" + temp.getTest_coordinator_id() + "' );";
//                //
//                temp = null;
//
//            }/* else if (TBL_NAME.equalsIgnoreCase(TBL_LP_FITNESS_TEST_RESULT)) {
//
//                FitnessTestResultModel temp = (FitnessTestResultModel) aData;
//                //query += TBL_LP_FITNESS_TEST_RESULT + " (student_id, camp_id, test_type_id, test_coordinator_id, score, percentile, created_on, created_by, last_modified_on, last_modified_by, latitude, longitude, test_name, tested) " + "values('" + temp.getStudent_id() + "', '" + temp.getCamp_id()+ "', '" + temp.getTest_type_id() + "', '" + temp.getTest_coordinator_id()+ "', '" + temp.getScore() + "', '" + temp.getPercentile()+ "', '" + temp.getCreated_on()+ "', '" + temp.getCreated_by()+ "', '" + temp.getLast_modified_on()+ "', '" + temp.getLast_modified_by()+"', '" + temp.getLatitude()+"', '" + temp.getLongitude()+"', '" + temp.getSubTestName()+"', '" + temp.isTestedOrNot()+ " where " + ARGS1 + "='" + ARGS2 +" AND "+ ARGS3 +"='" + ARGS4 +"' );";
//                //  query += TBL_LP_FITNESS_TEST_RESULT + " (student_id, camp_id, test_type_id, test_coordinator_id, score, percentile, created_on, created_by, last_modified_on, last_modified_by, latitude, longitude, test_name, tested, synced) " + "values('" + temp.getStudent_id() + "', '" + temp.getCamp_id()+ "', '" + temp.getTest_type_id() + "', '" + temp.getTest_coordinator_id()+ "', '" + temp.getScore() + "', '" + temp.getPercentile()+ "', '" + temp.getCreated_on()+ "', '" + temp.getCreated_by()+ "', '" + temp.getLast_modified_on()+ "', '" + temp.getLast_modified_by()+"', '" + temp.getLatitude()+"', '" + temp.getLongitude()+"', '" + temp.getSubTestName()+"', '" + temp.isTestedOrNot()+ "', '" + temp.isSyncedOrNot()+ "' )"+" where " + ARGS1 + "='" + ARGS2 +"'"+" AND "+ ARGS3 +"='" + ARGS4 +"'"+";";
//                //
//                args.put("student_id", temp.getStudent_id());
//                args.put("camp_id", temp.getCamp_id());
//                args.put("test_type_id", temp.getTest_type_id());
//                args.put("test_coordinator_id", temp.getTest_coordinator_id());
//                args.put("score", temp.getScore());
//                args.put("percentile", temp.getPercentile());
//                args.put("created_on", temp.getCreated_on());
//                args.put("created_by", temp.getCreated_by());
//                args.put("last_modified_on", temp.getLast_modified_by());
//                args.put("last_modified_by", temp.getLast_modified_by());
//                args.put("latitude", temp.getLatitude());
//                args.put("longitude", temp.getLongitude());
//                args.put("test_name", temp.getTestName());
//                args.put("sub_test_name", temp.getSubTestName());
//                args.put("tested", temp.isTestedOrNot());
//                args.put("synced", temp.isSyncedOrNot());
//
//
//                iDB.update(TBL_LP_FITNESS_TEST_RESULT, args, ARGS1 + " = ? AND " + ARGS3 + " = ?",
//                        new String[]{ARGS2, ARGS4});
//                temp = null;
//
//            }*/
//
//
//            //  iDB.execSQL(query);
//
//        } catch (Exception e) {
//            Log.e("DBManager ", "query==> " + query);
//            Log.e("[" + TAG + "]: ", "insertTables(): ", e);
//        }
//    }

    /**
     * get all data of specified Table.
     *
     * @param TBL_NAME
     * @param ARGS1
     * @param ARGS2
     * @return
     */
    public ArrayList<Object> getAllTableData(Context context, String TBL_NAME, String ARGS1, String ARGS2, String ARGS3, String ARGS4) {
        iContext = context;
        ArrayList<Object> aData = new ArrayList<Object>();

        if (iDB == null) {

            // OPENING DATABASE CONN.
            try {
                DBManager.getInstance().openDB();
            } catch (Exception e) {
                Log.e("["+TAG+"]:", "'" + TAG + "' - getAllTableData() :", e);
            }
        }

        if (!checkForTableExists(iDB,TBL_NAME))
            return aData;

//        if (TBL_NAME.equalsIgnoreCase(TBL_LP_USER_LOGIN)) {
//            try {
//                Cursor cr = null;
//                cr = iDB.query(TBL_LP_USER_LOGIN, null, null, null, null, null, null);
//
//                if (cr.moveToFirst()) {
//                    do {
//                        LoginModel temp = new LoginModel();
//
//                        temp.setUser_login_id(cr.getInt(cr.getColumnIndex("user_login_id")));
//                        temp.setUser_login_name(cr.getString(cr.getColumnIndex("user_login_name")));
//                        temp.setUser_password(cr.getString(cr.getColumnIndex("user_password")));
//                        temp.setUser_type_id(cr.getString(cr.getColumnIndex("user_type_id")));
//                        temp.setLogin_type(cr.getString(cr.getColumnIndex("login_type")));
//                        temp.setImage_name(cr.getString(cr.getColumnIndex("image_name")));
//                        temp.setImage_path(cr.getString(cr.getColumnIndex("image_path")));
//                        temp.setCreated_on(cr.getString(cr.getColumnIndex("created_on")));
//                        temp.setCreated_by(cr.getString(cr.getColumnIndex("created_by")));
//                        temp.setLast_modified_on(cr.getString(cr.getColumnIndex("last_modified_on")));
//                        temp.setLast_modified_by(cr.getString(cr.getColumnIndex("last_modified_by")));
//
//                        aData.add(temp);
//
//                    } while (cr.moveToNext());
//                }
//
//                if (cr != null && !cr.isClosed()) {
//                    cr.close();
//                }
//
//            } catch (Exception e) {
//                Log.e("[" + TAG + "]: ", "TBL_LP_USER_LOGIN - getAllTableData(): ", e);
//            }
//        }
        if (TBL_NAME.equalsIgnoreCase(TBL_LP_MORNING_TRACKING)) {
            try {
                Cursor cr = null;
                cr = iDB.query(TBL_LP_MORNING_TRACKING, null, null, null, null, null, null);

                if (cr.moveToFirst()) {
                    do {
                        LocationTrackingModel temp = new LocationTrackingModel();

                        temp.setLat(cr.getString(cr.getColumnIndex("latitude")));
                        temp.setLng(cr.getString(cr.getColumnIndex("longitude")));
                        temp.setCurrent_address(cr.getString(cr.getColumnIndex("address")));
                        temp.setCreated_on(cr.getString(cr.getColumnIndex("created_on")));
                        temp.setTrainer_id(cr.getString(cr.getColumnIndex("trainer_id")));

                        aData.add(temp);

                    } while (cr.moveToNext());
                }

                if (cr != null && !cr.isClosed()) {
                    cr.close();
                }

            } catch (Exception e) {
                Log.e("[" + TAG + "]: ", "TBL_LP_MORNING_TRACKING - getAllTableData(): ", e);
            }
        }


        else if (TBL_NAME.equalsIgnoreCase(TBL_LP_TEST_COORDINATORS)) {
            try {
                Cursor cr = null;

                cr = iDB.query(TBL_LP_TEST_COORDINATORS, null, null, null, null, null, null);

                if (cr.moveToFirst()) {
                    do {
                        TestCoordinatorsModel temp = new TestCoordinatorsModel();

                        temp.setTest_coordinator_id(cr.getInt(cr.getColumnIndex("test_coordinator_id")));
                        temp.setTest_coordinator_name(cr.getString(cr.getColumnIndex("test_coordinator_name")));
                        temp.setGender(cr.getString(cr.getColumnIndex("gender")));
                        temp.setEmail(cr.getString(cr.getColumnIndex("email")));
                        temp.setPhone(cr.getString(cr.getColumnIndex("phone")));
                        temp.setAddress(cr.getString(cr.getColumnIndex("address")));
                        temp.setSchool_id(cr.getString(cr.getColumnIndex("school_id")));
                        temp.setUser_type_id(cr.getString(cr.getColumnIndex("user_type_id")));
                        temp.setIs_active(cr.getInt(cr.getColumnIndex("is_active")));
                        temp.setCreated_on(cr.getString(cr.getColumnIndex("created_on")));
                        temp.setCreated_by(cr.getString(cr.getColumnIndex("created_by")));
                        temp.setLast_modified_on(cr.getString(cr.getColumnIndex("last_modified_on")));
                        temp.setLast_modified_by(cr.getString(cr.getColumnIndex("last_modified_by")));
                        temp.setImei(cr.getInt(cr.getColumnIndex("imei")));
                        temp.setAndroid_id(cr.getInt(cr.getColumnIndex("android_id")));

                        aData.add(temp);

                    } while (cr.moveToNext());
                }

                if (cr != null && !cr.isClosed()) {
                    cr.close();
                }

            } catch (Exception e) {
                Log.e("[" + TAG + "]: ", "TBL_LP_TEST_COORDINATORS - getAllTableData(): ", e);
            }
        } else if (TBL_NAME.equalsIgnoreCase(TBL_LP_STUDENT_USER_MASTER)) {
            try {
                Cursor cr = null;
//                cr = iDB.rawQuery("select * from " + TBL_LP_STUDENT_USER_MASTER + " where " +
//                        ARGS1 + "='" + ARGS2 + "'", null);
                cr = iDB.query(TBL_LP_STUDENT_USER_MASTER, null, null, null, null, null, null);
                if (cr.moveToFirst()) {
                    do {
                        StudentUserMasterModel temp = new StudentUserMasterModel();

                        temp.setGuardian_name(cr.getString(cr.getColumnIndex("guardian_name")));
                        temp.setImage_path(cr.getString(cr.getColumnIndex("image_path")));
                        temp.setPhone(cr.getString(cr.getColumnIndex("phone")));
                        temp.setStudent_name(cr.getString(cr.getColumnIndex("student_name")));
                        //temp.setUser_login_id(cr.getInt(cr.getColumnIndex("user_login_id")));
                        temp.setUser_login_name(cr.getString(cr.getColumnIndex("user_login_name")));
                        temp.setAddress(cr.getString(cr.getColumnIndex("address")));
                        temp.setCurrent_class(cr.getString(cr.getColumnIndex("current_class")));
                        temp.setDate_of_birth(cr.getString(cr.getColumnIndex("date_of_birth")));

                        temp.setGender(cr.getString(cr.getColumnIndex("gender")));


                        aData.add(temp);

                    } while (cr.moveToNext());
                }

                if (cr != null && !cr.isClosed()) {
                    cr.close();
                }

            } catch (Exception e) {
                Log.e("[" + TAG + "]: ", "TBL_LP_STUDENT_USER_MASTER - getAllTableData(): ", e);
            }
        } else if (TBL_NAME.equalsIgnoreCase(TBL_LP_SCHOOLS_MASTER)) {
            try {
                Cursor cr = null;
                if (ARGS3.equalsIgnoreCase("")) {
                    cr = iDB.rawQuery("select * from " + TBL_LP_SCHOOLS_MASTER + " where " +
                            ARGS1 + "='" + ARGS2 + "'", null);
                }
                else {
                    cr = iDB.rawQuery("select * from " + TBL_LP_SCHOOLS_MASTER + " where " +
                            ARGS1 + "='" + ARGS2 + "' and "+ARGS3 + "='" + ARGS4 + "'",null);
                }

//                cr = iDB.rawQuery("select * from " + TBL_LP_SCHOOLS_MASTER + " where " +
//                        ARGS1 + "='" + ARGS2 + "'", null);


                //   cr = iDB.query(TBL_LP_SCHOOLS_MASTER, null, null, null, null, null, null);
                if (cr.moveToFirst()) {
                    do {
                        SchoolsMasterModel temp = new SchoolsMasterModel();

                        temp.setSchool_id(cr.getInt(cr.getColumnIndex("school_id")));
                        temp.setSchool_name(cr.getString(cr.getColumnIndex("school_name")));
                        temp.setSchool_chain_id(cr.getInt(cr.getColumnIndex("school_chain_id")));
                        temp.setCity_id(cr.getInt(cr.getColumnIndex("city_id")));
                        temp.setAddress(cr.getString(cr.getColumnIndex("address")));
                        temp.setPhone_no(cr.getString(cr.getColumnIndex("phone_no")));
                        temp.setEmail(cr.getString(cr.getColumnIndex("email")));
                        temp.setWeb_address(cr.getString(cr.getColumnIndex("web_address")));
                        temp.setSchool_description(cr.getString(cr.getColumnIndex("school_description")));
                        temp.setSchool_image_name(cr.getString(cr.getColumnIndex("school_image_name")));
                        temp.setSchool_image_path(cr.getString(cr.getColumnIndex("school_image_path")));
                        temp.setCreated_on(cr.getString(cr.getColumnIndex("created_on")));
                        temp.setCreated_by(cr.getString(cr.getColumnIndex("created_by")));
                        temp.setLast_modified_on(cr.getString(cr.getColumnIndex("last_modified_on")));
                        temp.setLast_modified_by(cr.getString(cr.getColumnIndex("last_modified_by")));
                        temp.setSchool_logo_name(cr.getString(cr.getColumnIndex("school_logo_name")));
                        temp.setSchool_logo_path(cr.getString(cr.getColumnIndex("school_logo_path")));
                        temp.setSchool_alias_name(cr.getString(cr.getColumnIndex("school_alias_name")));
                        temp.setIs_active(cr.getInt(cr.getColumnIndex("is_active")));
                        temp.setTrainer_coordinator_id(cr.getString(cr.getColumnIndex("trainer_coordinator_id")));
                        temp.setSeniors_starting_from(cr.getString(cr.getColumnIndex("seniors_starting_from")));
                        temp.setLatitude(cr.getString(cr.getColumnIndex("latitude")));
                        temp.setLongitude(cr.getString(cr.getColumnIndex("longitude")));
                        temp.setSchool_start_time(cr.getString(cr.getColumnIndex("school_start_time")));
                        temp.setOffice(cr.getString(cr.getColumnIndex("office")));
                        temp.setIsAttached(cr.getInt(cr.getColumnIndex(AppConfig.IS_ATTACHED)));
                        temp.setForRetest(cr.getInt(cr.getColumnIndex(AppConfig.FOR_RETEST)));
                        temp.setIsRetestAllowed(cr.getString(cr.getColumnIndex(AppConfig.IS_RETEST_ALLOWED)));

                        aData.add(temp);

                    } while (cr.moveToNext());
                }

                if (cr != null && !cr.isClosed()) {
                    cr.close();
                }

            } catch (Exception e) {
                Log.e("[" + TAG + "]: ", "TBL_LP_SCHOOLS_MASTER - getAllTableData(): ", e);
            }
        } else if (TBL_NAME.equalsIgnoreCase(TBL_LP_STUDENT_MASTER)) {
            try {
                Cursor cr = null;

                cr = iDB.rawQuery("select * from " + TBL_LP_STUDENT_MASTER + " where " + ARGS1 + "='" + ARGS2 + "' GROUP BY current_class ORDER BY current_class ASC", null);

                //  cr = iDB.query(TBL_LP_STUDENT_MASTER, null, null, null, null, null, null);

                if (cr.moveToFirst()) {
                    do {
                        StudentMasterModel temp = new StudentMasterModel();

                        //temp.setStudent_liveplus_id(cr.getInt(cr.getColumnIndex("student_liveplus_id")));
                        //temp.setUser_login_id(cr.getInt(cr.getColumnIndex("user_login_id")));
                        temp.setStudent_name(cr.getString(cr.getColumnIndex("student_name")));
                        temp.setCurrent_school_id(cr.getString(cr.getColumnIndex("current_school_id")));
                        temp.setCurrent_roll_num(cr.getString(cr.getColumnIndex("current_roll_num")));
                        temp.setGender(cr.getInt(cr.getColumnIndex("gender")));
                        temp.setDob(cr.getString(cr.getColumnIndex("dob")));
                        temp.setEmail(cr.getString(cr.getColumnIndex("email")));
                        temp.setGaurdian_name(cr.getString(cr.getColumnIndex("gaurdian_name")));
                        temp.setPhone(cr.getString(cr.getColumnIndex("phone")));
                        temp.setAddress(cr.getString(cr.getColumnIndex("address")));
                        temp.setCreated_on(cr.getString(cr.getColumnIndex("created_on")));
                        temp.setCreated_by(cr.getString(cr.getColumnIndex("created_by")));
                        temp.setLast_modified_on(cr.getString(cr.getColumnIndex("last_modified_on")));
                        temp.setLast_modified_by(cr.getString(cr.getColumnIndex("last_modified_by")));
                        temp.setStudent_id(cr.getInt(cr.getColumnIndex("student_id")));
                        temp.setStudent_registration_num(cr.getString(cr.getColumnIndex("student_registration_num")));
                        temp.setCurrent_class(cr.getString(cr.getColumnIndex("current_class")));
                        temp.setClass_id(cr.getString(cr.getColumnIndex("class_id")));
                        temp.setClass_partition_id(cr.getInt(cr.getColumnIndex("class_partition_id")));
                        temp.setSection(cr.getString(cr.getColumnIndex("section")));
                        temp.setIs_active(cr.getInt(cr.getColumnIndex("is_active")));
                        temp.setCamp_id(cr.getString(cr.getColumnIndex("camp_id")));

                        aData.add(temp);

                    } while (cr.moveToNext());
                }

                if (cr != null && !cr.isClosed()) {
                    cr.close();
                }

            } catch (Exception e) {
                Log.e("[" + TAG + "]: ", "TBL_LP_STUDENT_MASTER - getAllTableData(): ", e);
            }
        } else if (TBL_NAME.equalsIgnoreCase(TBL_LP_CAMP_MASTER)) {
            try {
                Cursor cr = null;

                //  cr = iDB.query(TBL_LP_CAMP_MASTER, null, null, null, null, null, null);

                cr = iDB.rawQuery("select * from " + TBL_LP_CAMP_MASTER + " where " +
                        ARGS1 + "='" + ARGS2 + "'", null);

                if (cr.moveToFirst()) {
                    do {
                        CampMasterModel temp = new CampMasterModel();

                        temp.setCamp_id(cr.getInt(cr.getColumnIndex("camp_id")));
                        temp.setCamp_name(cr.getString(cr.getColumnIndex("camp_name")));
                        temp.setCamp_type(cr.getInt(cr.getColumnIndex("camp_type")));
                        temp.setSchool_id(cr.getInt(cr.getColumnIndex("school_id")));
                        temp.setVenue_id(cr.getInt(cr.getColumnIndex("venue_id")));
                        temp.setStart_date(cr.getString(cr.getColumnIndex("start_date")));
                        temp.setEnd_date(cr.getString(cr.getColumnIndex("end_date")));
                        temp.setCamp_coordination_id(cr.getInt(cr.getColumnIndex("camp_coordination_id")));
                        temp.setQuestionaire_id(cr.getInt(cr.getColumnIndex("questionaire_id")));
                        temp.setRegistration_coordinator_id(cr.getInt(cr.getColumnIndex("registration_coordinator_id")));
                        temp.setStatus(cr.getInt(cr.getColumnIndex("status")));
                        temp.setData_flag(cr.getInt(cr.getColumnIndex("data_flag")));

                        aData.add(temp);

                    } while (cr.moveToNext());
                }

                if (cr != null && !cr.isClosed()) {
                    cr.close();
                }

            } catch (Exception e) {
                Log.e("[" + TAG + "]: ", "TBL_LP_CAMP_MASTER - getAllTableData(): ", e);
            }
        } else if (TBL_NAME.equalsIgnoreCase(TBL_LP_FITNESS_TEST_TYPES)) {
            try {
                Cursor cr = null;
                if (ARGS2.equalsIgnoreCase("'0'"))
                    cr = iDB.query(TBL_LP_FITNESS_TEST_TYPES, null, null, null, null, null, null);
                else

                    cr = iDB.rawQuery("select * from " + TBL_LP_FITNESS_TEST_TYPES+ " where " +
                            ARGS1 + " IN ("+ARGS2+")" , null);

                if (cr.moveToFirst()) {
                    do {
                        FitnessTestTypesModel temp = new FitnessTestTypesModel();

                        temp.setTest_type_id(cr.getInt(cr.getColumnIndex("test_type_id")));
                        temp.setTest_name(cr.getString(cr.getColumnIndex("test_name")));
                        temp.setExcel_name(cr.getString(cr.getColumnIndex("excel_name")));
                        temp.setScore_measurement(cr.getString(cr.getColumnIndex("score_measurement")));
                        temp.setScore_unit(cr.getString(cr.getColumnIndex("score_unit")));
                        temp.setScore_criteria(cr.getString(cr.getColumnIndex("score_criteria")));
                        temp.setTest_description(cr.getString(cr.getColumnIndex("test_description")));
                        temp.setTest_performed(cr.getString(cr.getColumnIndex("test_performed")));
                        temp.setCreated_on(cr.getString(cr.getColumnIndex("created_on")));
                        temp.setCreated_by(cr.getString(cr.getColumnIndex("created_by")));
                        temp.setLast_modified_on(cr.getString(cr.getColumnIndex("last_modified_on")));
                        temp.setLast_modified_by(cr.getString(cr.getColumnIndex("last_modified_by")));
                        temp.setTest_category_id(cr.getInt(cr.getColumnIndex("test_category_id")));
                        temp.setTest_image(cr.getString(cr.getColumnIndex("test_image")));
                        temp.setTest_img_path(cr.getString(cr.getColumnIndex("test_img_path")));
                        temp.setSchool_id(cr.getString(cr.getColumnIndex("school_id")));
                        temp.setTest_coordinator_id(cr.getString(cr.getColumnIndex("test_coordinator_id")));
                        aData.add(temp);

                    } while (cr.moveToNext());
                }

                if (cr != null && !cr.isClosed()) {
                    cr.close();
                }

            } catch (Exception e) {
                Log.e("[" + TAG + "]: ", "TBL_LP_FITNESS_TEST_TYPES - getAllTableData(): ", e);
            }
        } else if (TBL_NAME.equalsIgnoreCase(TBL_LP_FITNESS_TEST_RESULT) || TBL_NAME.equalsIgnoreCase(TBL_LP_FITNESS_RETEST_RESULT)) {
            try {
                Cursor cr = null;

                //cr = iDB.query(TBL_LP_FITNESS_TEST_RESULT, null, null, null, null, null, null);
                cr = iDB.rawQuery("select * from " + TBL_NAME + " where " + ARGS1 + "='" + ARGS2 + "'" + " AND " + ARGS3 + "='" + ARGS4 + "'", null);
                if (cr.moveToFirst()) {
                    do {
                        FitnessTestResultModel temp = new FitnessTestResultModel();

                        temp.setStudent_id(cr.getInt(cr.getColumnIndex("student_id")));
                        temp.setCamp_id(cr.getInt(cr.getColumnIndex("camp_id")));
                        temp.setTest_type_id(cr.getInt(cr.getColumnIndex("test_type_id")));
                        temp.setTest_coordinator_id(cr.getInt(cr.getColumnIndex("test_coordinator_id")));
                        temp.setScore("" + cr.getInt(cr.getColumnIndex("score")));
                        temp.setPercentile(Double.parseDouble(cr.getString(cr.getColumnIndex("percentile"))));
                        //          temp.setCreated_on(cr.getString(cr.getColumnIndex("created_on")));
                        //          temp.setDevice_date(cr.getString(cr.getColumnIndex("device_date")));
                        temp.setCreated_by(cr.getString(cr.getColumnIndex("created_by")));
                        //          temp.setLast_modified_on(cr.getString(cr.getColumnIndex("last_modified_on")));
                        temp.setLast_modified_by(cr.getString(cr.getColumnIndex("last_modified_by")));
                        temp.setLatitude(cr.getDouble(cr.getColumnIndex("latitude")));
                        temp.setLongitude(cr.getDouble(cr.getColumnIndex("longitude")));
                        temp.setTestName(cr.getString(cr.getColumnIndex("test_name")));
                        temp.setSubTestName(cr.getString(cr.getColumnIndex("sub_test_name")));
                        temp.setTestedOrNot(Boolean.parseBoolean(cr.getString(cr.getColumnIndex("tested"))));
                        temp.setSyncedOrNot(Boolean.parseBoolean(cr.getString(cr.getColumnIndex("synced"))));

                        String crreated_on_string = cr.getString(cr.getColumnIndex("created_on"));
                        DateFormat formatter1 = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");

                        Date date1 = (Date) formatter1.parse(crreated_on_string);
                        String created_on = Constant.ConvertDateToString(date1);
                        temp.setCreated_on(created_on);


                        long last_modified_string = cr.getLong(cr.getColumnIndex("last_modified_on"));
//                        DateFormat formatter2 = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
//
//                        Date date2 = (Date) formatter2.parse(last_modified_string);
//                        String last_modified = Constant.ConvertDateToString(date2);
                        temp.setLast_modified_on(Utility.getDate(last_modified_string,"dd MMM yyyy HH:mm:ss:SSS"));


                        try {
                            String devive_date_string = cr.getString(cr.getColumnIndex("device_date"));
                            DateFormat formatter3 = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
                            Date date3 = (Date) formatter3.parse(devive_date_string);
                            String device_date = Constant.ConvertDateToString(date3);
                            temp.setDevice_date(device_date);
                        } catch (Exception e) {

                        }


                        aData.add(temp);

                    } while (cr.moveToNext());
                }

                if (cr != null && !cr.isClosed()) {
                    cr.close();
                }

            } catch (Exception e) {
                Log.e("[" + TAG + "]: ", "TBL_LP_FITNESS_TEST_RESULT - getAllTableData(): ", e);
            }
        } else if (TBL_NAME.equalsIgnoreCase(TBL_LP_FITNESS_TEST_CATEGORY)) {
            try {
                Cursor cr = null;

                Log.e(TAG, "query==> " + "select * from " + TBL_LP_FITNESS_TEST_CATEGORY + " where " +
                        ARGS1 + "='" + ARGS2 + "'");
                cr = iDB.rawQuery("select * from " + TBL_LP_FITNESS_TEST_CATEGORY + " where " +
                        ARGS1 + "='" + ARGS2 + "'", null);


                // cr = iDB.query(TBL_LP_FITNESS_TEST_CATEGORY, null, null, null, null, null, null);

                if (cr.moveToFirst()) {
                    do {
                        FitnessTestCategoryModel temp = new FitnessTestCategoryModel();

                        temp.setSub_test_id(cr.getInt(cr.getColumnIndex("sub_test_id")));
                        temp.setTest_id(cr.getInt(cr.getColumnIndex("test_id")));
                        temp.setTest_name(cr.getString(cr.getColumnIndex("test_name")));
                        temp.setScore_criteria(cr.getString(cr.getColumnIndex("score_criteria")));
                        temp.setScore_measurement(cr.getString(cr.getColumnIndex("score_measurement")));
                        temp.setScore_unit(cr.getString(cr.getColumnIndex("score_unit")));
                        temp.setName(cr.getString(cr.getColumnIndex("name")));
                        temp.setTest_description(cr.getString(cr.getColumnIndex("test_description")));
                        temp.setPurpose(cr.getString(cr.getColumnIndex("purpose")));
                        temp.setAdministrative_Suggestions(cr.getString(cr.getColumnIndex("Administrative_Suggestions")));
                        temp.setEquipment_Required(cr.getString(cr.getColumnIndex("Equipment_Required")));
                        temp.setScoring(cr.getString(cr.getColumnIndex("scoring")));
                        temp.setMultipleLane(cr.getInt(cr.getColumnIndex("multiple_lane")));
                        temp.setTimerType(cr.getInt(cr.getColumnIndex("timer_type")));
                        temp.setFinalPosition(cr.getInt(cr.getColumnIndex("final_position")));

                        temp.setCreated_on(cr.getString(cr.getColumnIndex("created_on")));
                        temp.setLast_modified_on(cr.getString(cr.getColumnIndex("last_modified_on")));
                        temp.setTest_applicable(cr.getString(cr.getColumnIndex("tests_applicable")));
                        temp.setVideoLink(cr.getString(cr.getColumnIndex("video_link")));


                        aData.add(temp);

                    } while (cr.moveToNext());
                }

                if (cr != null && !cr.isClosed()) {
                    cr.close();
                }

            } catch (Exception e) {
                Log.e("[" + TAG + "]: ", "TBL_LP_FITNESS_TEST_CATEGORY - getAllTableData(): ", e);
            }
        } else if (TBL_NAME.equalsIgnoreCase(TBL_LP_SKILL_TEST_TYPE)) {
            try {
                Cursor cr = null;

                Log.e(TAG, "query==> " + "select * from " + TBL_LP_SKILL_TEST_TYPE + " where " +
                        ARGS1 + "='" + ARGS2 + "'");
                cr = iDB.rawQuery("select * from " + TBL_LP_SKILL_TEST_TYPE + " where " +
                        ARGS1 + "='" + ARGS2 + "'", null);


                if (cr.moveToFirst()) {
                    do {
                        SkillTestTypeModel temp = new SkillTestTypeModel();

                        temp.setChecklist_item_id(cr.getInt(cr.getColumnIndex("checklist_item_id")));
                        temp.setTest_type_id(cr.getInt(cr.getColumnIndex("test_type_id")));
                        temp.setItem_name(cr.getString(cr.getColumnIndex("item_name")));
                        temp.setCreated_on(cr.getString(cr.getColumnIndex("created_on")));
                        temp.setLast_modified_on(cr.getString(cr.getColumnIndex("last_modified_on")));
                        temp.setSynced(cr.getString(cr.getColumnIndex("synced")));


                        aData.add(temp);

                    } while (cr.moveToNext());
                }

                if (cr != null && !cr.isClosed()) {
                    cr.close();
                }

            } catch (Exception e) {
                Log.e("[" + TAG + "]: ", "TBL_LP_FITNESS_TEST_CATEGORY - getAllTableData(): ", e);
            }
        } else if (TBL_NAME.equalsIgnoreCase(TBL_LP_FITNESS_SKILL_TEST_RESULT)) {
            try {
                Cursor cr = null;
                //  cr = iDB.query(TBL_LP_FITNESS_SKILL_TEST_RESULT, null, null, null, null, null, null);
                cr = iDB.rawQuery("select * from " + TBL_LP_FITNESS_SKILL_TEST_RESULT + " where " +
                        ARGS1 + "='" + ARGS2 + "'"+ " AND " + ARGS3 + "='" + ARGS4 + "'", null);


                if (cr.moveToFirst()) {
                    do {

                        FitnessSkillTestResultModel temp = new FitnessSkillTestResultModel();
                        temp.setSkill_score_id(cr.getInt(cr.getColumnIndex("skill_score_id")));
                        temp.setStudent_id(cr.getInt(cr.getColumnIndex("student_id")));
                        temp.setCamp_id(cr.getInt(cr.getColumnIndex("camp_id")));
                        temp.setCoordinator_id(cr.getInt(cr.getColumnIndex("coordinator_id")));
                        temp.setChecklist_item_id(cr.getInt(cr.getColumnIndex("checklist_item_id")));
                        temp.setTest_type_id(cr.getInt(cr.getColumnIndex("test_type_id")));
                        temp.setScore(cr.getString(cr.getColumnIndex("score")));
                        // temp.setCreated_on(cr.getString(cr.getColumnIndex("created_on")));
                        // temp.setLast_modified_on(cr.getString(cr.getColumnIndex("last_modified_on")));
                        temp.setLatitude(cr.getString(cr.getColumnIndex("latitude")));
                        temp.setLongitude(cr.getString(cr.getColumnIndex("longitude")));
                        temp.setTested(Boolean.parseBoolean(cr.getString(cr.getColumnIndex("tested"))));
                        temp.setSynced(cr.getInt(cr.getColumnIndex("synced"))==1);
                        //  temp.setDevice_date(cr.getString(cr.getColumnIndex("device_date")));

                        try{
                            String crreated_on_string = cr.getString(cr.getColumnIndex("created_on"));
                            DateFormat formatter1 = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
//
                            Date date1 = (Date) formatter1.parse(crreated_on_string);
                            String created_on = Constant.ConvertDateToString(date1);
                            temp.setCreated_on(created_on);
                        } catch (Exception e){

                        }

                        long last_modified_string = cr.getLong(cr.getColumnIndex("last_modified_on"));
//                        DateFormat formatter2 = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
////                        try{
//                        Date date2 = (Date) formatter2.parse(last_modified_string);
//                        String last_modified = Constant.ConvertDateToString(date2);
                        temp.setLast_modified_on(Utility.getDate(last_modified_string,"dd MMM yyyy HH:mm:ss:SSS"));
//                    }catch (Exception e){
//
//                        }


                        try {
                            String devive_date_string = cr.getString(cr.getColumnIndex("device_date"));
                            DateFormat formatter3 = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
                            Date date3 = (Date) formatter3.parse(devive_date_string);
                            String device_date = Constant.ConvertDateToString(date3);
                            temp.setDevice_date(device_date);
                        } catch (Exception e) {

                        }


                        aData.add(temp);

                    } while (cr.moveToNext());
                }

                if (cr != null && !cr.isClosed()) {
                    cr.close();
                }

            } catch (Exception e) {
                Log.e("[" + TAG + "]: ", "TBL_LP_FITNESS_SKILL_TEST_RESULT - getAllTableData(): ", e);
            }
        } else if (TBL_NAME.equalsIgnoreCase(TBL_LP_SCHOOL_CLASS_FLAG_MAPPING)) {
            try {
                Cursor cr = null;

                Log.e(TAG, "query==> " + "select * from " + TBL_LP_SCHOOL_CLASS_FLAG_MAPPING + " where " +
                        ARGS1 + "='" + ARGS2 + "'");
                cr = iDB.rawQuery("select * from " + TBL_LP_SCHOOL_CLASS_FLAG_MAPPING + " where " +
                        ARGS1 + "='" + ARGS2 + "'", null);


                if (cr.moveToFirst()) {
                    do {
                        SchoolClassMapping temp = new SchoolClassMapping();

                        temp.setSchool_id(cr.getInt(cr.getColumnIndex("school_id")));
                        temp.setClass_id(cr.getInt(cr.getColumnIndex("class_id")));
                        temp.setClass_name(cr.getString(cr.getColumnIndex("class_name")));
                        temp.setFlag(cr.getInt(cr.getColumnIndex("flag")));


                        aData.add(temp);

                    } while (cr.moveToNext());
                }

                if (cr != null && !cr.isClosed()) {
                    cr.close();
                }

            } catch (Exception e) {
                Log.e("[" + TAG + "]: ", "TBL_LP_FITNESS_TEST_CATEGORY - getAllTableData(): ", e);
            }
        } else if (TBL_NAME.equalsIgnoreCase(TBL_LP_CAMP_TEST_MAPPING)) {
            try {
                Cursor cr = null;

                Log.e(TAG, "query==> " + "select * from " + TBL_LP_CAMP_TEST_MAPPING + " where " +
                        ARGS1 + "='" + ARGS2 + "'" + " AND " + ARGS3 + "='" + ARGS4 + "'");
                cr = iDB.rawQuery("select * from " + TBL_LP_CAMP_TEST_MAPPING + " where " +
                        ARGS1 + "='" + ARGS2 + "'" + " AND " + ARGS3 + "='" + ARGS4 + "'", null);


                if (cr.moveToFirst()) {
                    do {
                        CampTestMapping temp = new CampTestMapping();

                        temp.setCamp_id(cr.getInt(cr.getColumnIndex("camp_id")));
                        temp.setSchool_id(cr.getInt(cr.getColumnIndex("school_id")));
                        temp.setTest_type_id(cr.getInt(cr.getColumnIndex("test_type_id")));
                        temp.setClass_ID(cr.getString(cr.getColumnIndex("class_id")));


                        aData.add(temp);

                    } while (cr.moveToNext());
                }

                if (cr != null && !cr.isClosed()) {
                    cr.close();
                }

            } catch (Exception e) {
                Log.e("[" + TAG + "]: ", "TBL_LP_FITNESS_TEST_CATEGORY - getAllTableData(): ", e);
            }
        } else if (TBL_NAME.equalsIgnoreCase(TBL_LP_TEST_COORDINATOR_MAPPING)) {
            try {
                Cursor cr = null;

                Log.e(TAG, "query==> " + "select * from " + TBL_LP_TEST_COORDINATOR_MAPPING + " where " +
                        ARGS1 + "='" + ARGS2 + "'" + " AND " + ARGS3 + "='" + ARGS4 + "'");
                cr = iDB.rawQuery("select * from " + TBL_LP_TEST_COORDINATOR_MAPPING + " where " +
                        ARGS1 + "='" + ARGS2 + "'" + " AND " + ARGS3 + "='" + ARGS4 + "'", null);


                if (cr.moveToFirst()) {
                    do {
                        TestCoordinatorMapping temp = new TestCoordinatorMapping();

                        temp.setCamp_id(cr.getInt(cr.getColumnIndex("camp_id")));
                        temp.setSchool_id(cr.getInt(cr.getColumnIndex("school_id")));
                        temp.setTest_type_id(cr.getInt(cr.getColumnIndex("test_type_id")));


                        aData.add(temp);

                    } while (cr.moveToNext());
                }

                if (cr != null && !cr.isClosed()) {
                    cr.close();
                }

            } catch (Exception e) {
                Log.e("[" + TAG + "]: ", "TBL_LP_FITNESS_TEST_CATEGORY - getAllTableData(): ", e);
            }
        }
        else if(TBL_NAME.equalsIgnoreCase(TBL_LP_MY_DART_CLASS_TABLE)){
            try{
                Cursor cr = null;

                cr = iDB.rawQuery("select * from " + TBL_LP_MY_DART_CLASS_TABLE + " where " +
                        ARGS1 + "='" + ARGS2 + "' ORDER BY class_id ASC",null);

                if (cr.moveToFirst()) {
                    do {
                        MyDartClassModel temp = new MyDartClassModel();

                        temp.setClasss(cr.getString(cr.getColumnIndex("class")));
                        temp.setClass_id(cr.getString(cr.getColumnIndex("class_id")));

                        aData.add(temp);

                    } while (cr.moveToNext());
                }

                if (cr != null && !cr.isClosed()) {
                    cr.close();
                }
            }catch (Exception e){
                Log.e("[" + TAG + "]: ", "TBL_LP_MY_DART_CLASS_TABLE - getAllTableData(): ", e);
            }


        }else if(TBL_NAME.equalsIgnoreCase(TBL_LP_MY_DART_PERIOD_TABLE)){
            try{
                Cursor cr = null;

                cr = iDB.rawQuery("select * from " + TBL_LP_MY_DART_PERIOD_TABLE + " where " +
                        ARGS1 + "='" + ARGS2 + "'",null);


                if (cr.moveToFirst()) {
                    do {
                        MyDartPeriodModel temp = new MyDartPeriodModel();
                        temp.setPeriod(cr.getString(cr.getColumnIndex("period")));
                        aData.add(temp);

                    } while (cr.moveToNext());
                }

                if (cr != null && !cr.isClosed()) {
                    cr.close();
                }
            }catch (Exception e){
                Log.e("[" + TAG + "]: ", "TBL_LP_MY_DART_PERIOD_TABLE - getAllTableData(): ", e);
            }


        }else if(TBL_NAME.equalsIgnoreCase(TBL_LP_MY_DART_GRADE_TABLE)){
            try{
                Cursor cr = null;

                cr = iDB.rawQuery("select * from " + TBL_LP_MY_DART_GRADE_TABLE+ " where " +
                        ARGS1 + "='" + ARGS2 + "'" ,null);


                if (cr.moveToFirst()) {
                    do {
                        MyDartGradeModel temp = new MyDartGradeModel();
                        temp.setGrade(cr.getString(cr.getColumnIndex("grade")));
                        temp.setGrade_id(cr.getString(cr.getColumnIndex("grade_id")));
                        aData.add(temp);

                    } while (cr.moveToNext());
                }

                if (cr != null && !cr.isClosed()) {
                    cr.close();
                }
            }catch (Exception e){
                Log.e("[" + TAG + "]: ", "TBL_LP_MY_DART_GRADE_TABLE - getAllTableData(): ", e);
            }


        }
        else if(TBL_NAME.equalsIgnoreCase(TBL_LP_MY_DART_SPORT_TABLE)){
            try{
                Cursor cr = null;

                cr = iDB.rawQuery("select * from " + TBL_LP_MY_DART_SPORT_TABLE + " where " +
                        ARGS1 + "='" + ARGS2 + "'"+ " AND " + ARGS3 + "='" + ARGS4 + "'",null);


                if (cr.moveToFirst()) {
                    do {
                        MyDartSportModel temp = new MyDartSportModel();
                        temp.setSport(cr.getString(cr.getColumnIndex("sport")));
                        temp.setSport_id(cr.getString(cr.getColumnIndex("sport_id")));
                        aData.add(temp);

                    } while (cr.moveToNext());
                }

                if (cr != null && !cr.isClosed()) {
                    cr.close();
                }
            }catch (Exception e){
                Log.e("[" + TAG + "]: ", "TBL_LP_MY_DART_SPORT_TABLE - getAllTableData(): ", e);
            }


        }   else if(TBL_NAME.equalsIgnoreCase(TBL_LP_MY_DART_OTHER_TABLE)){
            try{
                Cursor cr = null;

                cr = iDB.rawQuery("select * from " + TBL_LP_MY_DART_OTHER_TABLE + " where " +
                        ARGS1 + "='" + ARGS2 + "'",null);


                if (cr.moveToFirst()) {
                    do {
                        MyDartOtherModel temp = new MyDartOtherModel();
                        temp.setOther(cr.getString(cr.getColumnIndex("other")));
                        temp.setOther_id(cr.getString(cr.getColumnIndex("other_id")));
                        aData.add(temp);

                    } while (cr.moveToNext());
                }

                if (cr != null && !cr.isClosed()) {
                    cr.close();
                }
            }catch (Exception e){
                Log.e("[" + TAG + "]: ", "TBL_LP_MY_DART_OTHER_TABLE - getAllTableData(): ", e);
            }


        } else if(TBL_NAME.equalsIgnoreCase(TBL_LP_MY_DART_LESSON_PLAN_TABLE)){
            try{
                Cursor cr = null;

                cr = iDB.rawQuery("select * from " + TBL_LP_MY_DART_LESSON_PLAN_TABLE+ " where " +
                        ARGS1 + "='" + ARGS2 + "'" + " AND " + ARGS3 + "='" + ARGS4 + "'",null);


                if (cr.moveToFirst()) {
                    do {
                        MyDartLessonPlanModel temp = new MyDartLessonPlanModel();
                        temp.setLesson(cr.getString(cr.getColumnIndex("lesson")));
                        temp.setLesson_id(cr.getString(cr.getColumnIndex("lesson_id")));
                        aData.add(temp);

                    } while (cr.moveToNext());
                }

                if (cr != null && !cr.isClosed()) {
                    cr.close();
                }
            }catch (Exception e){
                Log.e("[" + TAG + "]: ", "TBL_LP_MY_DART_LESSON_PLAN_TABLE - getAllTableData(): ", e);
            }


        }else if(TBL_NAME.equalsIgnoreCase(TBL_LP_MY_DART_SPORT_SKILL_TABLE)){
            try{
                Cursor cr = null;

                cr = iDB.rawQuery("select * from " + TBL_LP_MY_DART_SPORT_SKILL_TABLE+ " where " +
                        ARGS1 + "='" + ARGS2 + "'" + " AND " + ARGS3 + "='" + ARGS4 + "'",null);


                if (cr.moveToFirst()) {
                    do {
                        MyDartSportSkillModel temp = new MyDartSportSkillModel();
                        temp.setSport_skill(cr.getString(cr.getColumnIndex("sport_skill")));
                        temp.setSport_skill_id(cr.getString(cr.getColumnIndex("sport_skill_id")));
                        temp.setTechnique_id(cr.getString(cr.getColumnIndex("technique_id")));
                        aData.add(temp);

                    } while (cr.moveToNext());
                }

                if (cr != null && !cr.isClosed()) {
                    cr.close();
                }
            }catch (Exception e){
                Log.e("[" + TAG + "]: ", "TBL_LP_MY_DART_SPORT_SKILL_TABLE - getAllTableData(): ", e);
            }
        }else if(TBL_NAME.equalsIgnoreCase(TBL_LP_MY_DART_INSERT_ACTIVITY_TABLE)) {
            try{
                Cursor cr = null;

                cr = iDB.rawQuery("select * from " + TBL_LP_MY_DART_INSERT_ACTIVITY_TABLE+ " where " +
                        ARGS1 + "='" + ARGS2 + "'" ,null);


                if (cr.moveToFirst()) {
                    do {
                        MyDartInsertActivityModel temp = new MyDartInsertActivityModel();
                        temp.setData(cr.getString(cr.getColumnIndex("data")));
                        temp.setActivity_id(cr.getString(cr.getColumnIndex("activity_id")));
                        temp.setStudent_activity_id(cr.getString(cr.getColumnIndex("student_activity_id")));
                        aData.add(temp);

                    } while (cr.moveToNext());
                }

                if (cr != null && !cr.isClosed()) {
                    cr.close();
                }
            }catch (Exception e){
                Log.e("[" + TAG + "]: ", "TBL_LP_MY_DART_INSERT_ACTIVITY_TABLE - getAllTableData(): ", e);
            }

        } else if (TBL_NAME.equalsIgnoreCase(TBL_STATE_MASTER)){
            try{
                Cursor cr = null;

                cr = iDB.rawQuery("select * from " + TBL_STATE_MASTER,null);
                if (cr.moveToFirst()) {
                    do {
                        ListViewDialogModel stateModel = new ListViewDialogModel(
                                cr.getInt(cr.getColumnIndex(AppConfig.STATE_ID)),
                                cr.getString(cr.getColumnIndex(AppConfig.STATE_NAME)),false);
//                        MyDartInsertActivityModel temp = new MyDartInsertActivityModel();
//                        temp.setData(cr.getString(cr.getColumnIndex("data")));
//                        temp.setActivity_id(cr.getString(cr.getColumnIndex("activity_id")));
//                        temp.setStudent_activity_id(cr.getString(cr.getColumnIndex("student_activity_id")));
                        aData.add(stateModel);

                    } while (cr.moveToNext());
                }

                if (cr != null && !cr.isClosed()) {
                    cr.close();
                }
            }catch (Exception e){
                Log.e("[" + TAG + "]: ", "state master - getAllTableData(): ", e);
            }
        }
        return aData;

    }


    @SuppressLint("LongLogTag")
    public ArrayList<Object> getAllTableDartStudentData(Context context, String ARGS1, String ARGS2, String ARGS3, String ARGS4, String ARGS5, String ARGS6){
        iContext = context;
        ArrayList<Object> aData = new ArrayList<Object>();

        if (iDB == null) {

            // OPENING DATABASE CONN.
            try {
                DBManager.getInstance().openDB();
            } catch (Exception e) {
                Log.e("["+TAG+"]:", "'" + TAG + "' - getAllTableData() :", e);
            }
        }
        try{
            Cursor cr = null;

            cr = iDB.rawQuery("select * from " + TBL_LP_MY_DART_STUDENT_TABLE+ " where " +
                    ARGS1 + "='" + ARGS2 + "'" + " AND " + ARGS3 + "='" + ARGS4 + "'"+ " AND " + ARGS5 + "='" + ARGS6 + "'",null);


            if (cr.moveToFirst()) {
                do {
                    MyDartStudentModel temp = new MyDartStudentModel();
                    temp.setStudent_name(cr.getString(cr.getColumnIndex("student_name")));
                    temp.setStudent_id(cr.getString(cr.getColumnIndex("student_id")));
                    temp.setStudent_grade_id(cr.getString(cr.getColumnIndex("student_grade_id")));
                    aData.add(temp);

                } while (cr.moveToNext());
            }

            if (cr != null && !cr.isClosed()) {
                cr.close();
            }
        }catch (Exception e){
            Log.e("[" + TAG + "]: ", "TBL_LP_MY_DART_STUDENT_TABLE - getAllTableData(): ", e);
        }
        return aData;
    }
    @SuppressLint("LongLogTag")
    public ArrayList<Object> getAllTableDataTest(Context context, String TBL_NAME, String ARGS1, String ARGS2, String ARGS3, String ARGS4) {
        iContext = context;
        ArrayList<Object> aData = new ArrayList<Object>();

        if (iDB == null) {

            // OPENING DATABASE CONN.
            try {
                DBManager.getInstance().openDB();
            } catch (Exception e) {
                Log.e("["+TAG+"]:", "'" + TAG + "' - getAllTableData() :", e);
            }
        }
        if (TBL_NAME.equalsIgnoreCase(TBL_LP_FITNESS_TEST_TYPES)) {
            try {
                Cursor cr = null;
                cr = iDB.query(TBL_LP_FITNESS_TEST_TYPES, null, null, null, null, null, null);
                //  cr = iDB.rawQuery("select * from " + TBL_LP_FITNESS_TEST_TYPES + " where " + ARGS1 + "='" + ARGS2 + "'" + " AND " + ARGS3 + "='" + ARGS4 + "'", null);

                if (cr.moveToFirst()) {
                    do {
                        FitnessTestTypesModel temp = new FitnessTestTypesModel();

                        temp.setTest_type_id(cr.getInt(cr.getColumnIndex("test_type_id")));
                        temp.setTest_name(cr.getString(cr.getColumnIndex("test_name")));
                        temp.setExcel_name(cr.getString(cr.getColumnIndex("excel_name")));
                        temp.setScore_measurement(cr.getString(cr.getColumnIndex("score_measurement")));
                        temp.setScore_unit(cr.getString(cr.getColumnIndex("score_unit")));
                        temp.setScore_criteria(cr.getString(cr.getColumnIndex("score_criteria")));
                        temp.setTest_description(cr.getString(cr.getColumnIndex("test_description")));
                        temp.setTest_performed(cr.getString(cr.getColumnIndex("test_performed")));
                        temp.setCreated_on(cr.getString(cr.getColumnIndex("created_on")));
                        temp.setCreated_by(cr.getString(cr.getColumnIndex("created_by")));
                        temp.setLast_modified_on(cr.getString(cr.getColumnIndex("last_modified_on")));
                        temp.setLast_modified_by(cr.getString(cr.getColumnIndex("last_modified_by")));
                        temp.setTest_category_id(cr.getInt(cr.getColumnIndex("test_category_id")));
                        temp.setTest_image(cr.getString(cr.getColumnIndex("test_image")));
                        temp.setTest_img_path(cr.getString(cr.getColumnIndex("test_img_path")));
                        temp.setSchool_id(cr.getString(cr.getColumnIndex("school_id")));
                        temp.setTest_coordinator_id(cr.getString(cr.getColumnIndex("test_coordinator_id")));


                        aData.add(temp);

                    } while (cr.moveToNext());
                }

                if (cr != null && !cr.isClosed()) {
                    cr.close();
                }

            } catch (Exception e) {
                Log.e("[" + TAG + "]: ", "TBL_LP_FITNESS_TEST_TYPES - getAllTableData(): ", e);
            }
        }
        else if (TBL_NAME.equalsIgnoreCase(TBL_LP_SCREEN_MASTER)) {
            try {
                Cursor cr = null;
                // cr = iDB.query(TBL_LP_SCREEN_MASTER, null, null, null, null, null, null);
                //  cr = iDB.rawQuery("select * from " + TBL_LP_SCREEN_MASTER + " where " + ARGS1 + "='" + ARGS2 + "'" , null);

                cr = iDB.rawQuery(  "select * from "+ TBL_LP_SCREEN_MASTER+" where "+ARGS1+" IN ("+ARGS2+")" , null);

                Log.e(TAG,"query==> "+  "select * from "+ TBL_LP_SCREEN_MASTER+" where "+ARGS1+" IN ("+ARGS2+")");




                if (cr.moveToFirst()) {
                    do {
                        ScreenMasterModel temp = new ScreenMasterModel();
//  screen_id, parent_id, screen_name, web_url, mobile_icon_title, icon_image_name, icon_path,
                        // created_on, created_by, modified_on, modified_by, is_deleted

                        temp.setScreen_id(cr.getString(cr.getColumnIndex("screen_id")));
                        temp.setPartner_id(cr.getInt(cr.getColumnIndex("parent_id")));
                        temp.setScreen_name(cr.getString(cr.getColumnIndex("screen_name")));
                        temp.setWeb_url(cr.getString(cr.getColumnIndex("web_url")));
                        temp.setMobile_icon_title(cr.getString(cr.getColumnIndex("mobile_icon_title")));
                        temp.setIcon_image_name(cr.getString(cr.getColumnIndex("icon_image_name")));
                        temp.setIcon_path(cr.getString(cr.getColumnIndex("icon_path")));
                        temp.setCreated_on(cr.getString(cr.getColumnIndex("created_on")));
                        temp.setModified_on(cr.getString(cr.getColumnIndex("modified_on")));
                        temp.setModified_by(cr.getString(cr.getColumnIndex("modified_by")));
                        temp.setIs_deleted(cr.getInt(cr.getColumnIndex("is_deleted")));



                        aData.add(temp);

                    } while (cr.moveToNext());
                }

                if (cr != null && !cr.isClosed()) {
                    cr.close();
                }

            } catch (Exception e) {
                Log.e("[" + TAG + "]: ", "TBL_LP_FITNESS_TEST_TYPES - getAllTableData(): ", e);
            }
        }

     /*   else if (TBL_NAME.equalsIgnoreCase(TBL_LP_USER_SCREEN_MAP)) {
            try {
                Cursor cr = null;
                //cr = iDB.query(TBL_LP_USER_SCREEN_MAP, null, null, null, null, null, null);
                  cr = iDB.rawQuery("select * from " + TBL_LP_USER_SCREEN_MAP + " where " + ARGS1 + "='" + ARGS2 + "'" , null);
                Log.e("DBManager","query==> "+"select * from " + TBL_LP_USER_SCREEN_MAP + " where " + ARGS1 + "='" + ARGS2 + "'");

                if (cr.moveToFirst()) {
                    do {
                        UserScreenMapModel temp = new UserScreenMapModel();


                        temp.setScreen_id(""+cr.getColumnIndex("screen_id"));
                        temp.setUser_type_id(cr.getInt(cr.getColumnIndex("user_type_id")));

                        aData.add(temp);

                    } while (cr.moveToNext());
                }

                if (cr != null && !cr.isClosed()) {
                    cr.close();
                }

            } catch (Exception e) {
                Log.e("[" + TAG + "]: ", "TBL_LP_FITNESS_TEST_TYPES - getAllTableData(): ", e);
            }
        }*/


        else if (TBL_NAME.equalsIgnoreCase(TBL_LP_STUDENT_MASTER)) {
            try {
                Cursor cr = null;

                // cr = iDB.rawQuery("select * from " + TBL_LP_STUDENT_MASTER + " where " + ARGS1 + "='" + ARGS2 + "' ORDER BY class_id ASC", null);
                //  cr = iDB.query(TBL_LP_STUDENT_MASTER, null, null, null, null, null, null);

                cr = iDB.rawQuery("select * from " + TBL_LP_STUDENT_MASTER + " where " + ARGS1 + "='" + ARGS2 + "'" + " AND " + ARGS3 + "='" + ARGS4 + "' ORDER BY student_name ASC", null);

                Log.e(TAG,"reg query=> "+"select * from " + TBL_LP_STUDENT_MASTER + " where " + ARGS1 + "='" + ARGS2 + "'" + " AND " + ARGS3 + "='" + ARGS4 + "' ORDER BY student_name ASC");

                if (cr.moveToFirst()) {
                    do {
                        StudentMasterModel temp = new StudentMasterModel();

                        //temp.setStudent_liveplus_id(cr.getInt(cr.getColumnIndex("student_liveplus_id")));
                        //temp.setUser_login_id(cr.getInt(cr.getColumnIndex("user_login_id")));
                        temp.setStudent_name(cr.getString(cr.getColumnIndex("student_name")));
                        temp.setCurrent_school_id(cr.getString(cr.getColumnIndex("current_school_id")));
                        temp.setCurrent_roll_num(cr.getString(cr.getColumnIndex("current_roll_num")));
                        temp.setGender(cr.getInt(cr.getColumnIndex("gender")));
                        temp.setDob(cr.getString(cr.getColumnIndex("dob")));
                        temp.setEmail(cr.getString(cr.getColumnIndex("email")));
                        temp.setGaurdian_name(cr.getString(cr.getColumnIndex("gaurdian_name")));
                        temp.setPhone(cr.getString(cr.getColumnIndex("phone")));
                        temp.setAddress(cr.getString(cr.getColumnIndex("address")));
                        temp.setCreated_on(cr.getString(cr.getColumnIndex("created_on")));
                        temp.setCreated_by(cr.getString(cr.getColumnIndex("created_by")));
                        temp.setLast_modified_on(cr.getString(cr.getColumnIndex("last_modified_on")));
                        temp.setLast_modified_by(cr.getString(cr.getColumnIndex("last_modified_by")));
                        temp.setStudent_id(cr.getInt(cr.getColumnIndex("student_id")));
                        temp.setStudent_registration_num(cr.getString(cr.getColumnIndex("student_registration_num")));
                        temp.setCurrent_class(cr.getString(cr.getColumnIndex("current_class")));
                        temp.setClass_id(cr.getString(cr.getColumnIndex("class_id")));
                        temp.setClass_partition_id(cr.getInt(cr.getColumnIndex("class_partition_id")));
                        temp.setSection(cr.getString(cr.getColumnIndex("section")));
                        temp.setIs_active(cr.getInt(cr.getColumnIndex("is_active")));
                        temp.setCamp_id(cr.getString(cr.getColumnIndex("camp_id")));

                        aData.add(temp);

                    } while (cr.moveToNext());
                }

                if (cr != null && !cr.isClosed()) {
                    cr.close();
                }

            } catch (Exception e) {
                Log.e("[" + TAG + "]: ", "TBL_LP_STUDENT_MASTER - getAllTableData(): ", e);
            }
        }

        return aData;

    }

    public ArrayList<Object> getAllTableData1(Context context, String TBL_NAME, String ARGS1, String ARGS2, String ARGS3, String ARGS4) {
        iContext = context;
        ArrayList<Object> aData = new ArrayList<Object>();

        if (iDB == null) {

            // OPENING DATABASE CONN.
            try {
                DBManager.getInstance().openDB();
            } catch (Exception e) {
                Log.e("["+TAG+"]:", "'" + TAG + "' - getAllTableData() :", e);
            }
        }


        if (TBL_NAME.equalsIgnoreCase(TBL_LP_FITNESS_TEST_CATEGORY)) {
            try {
                Cursor cr = null;


                cr = iDB.query(TBL_LP_FITNESS_TEST_CATEGORY, null, null, null, null, null, null);


                // cr = iDB.query(TBL_LP_FITNESS_TEST_CATEGORY, null, null, null, null, null, null);

                if (cr.moveToFirst()) {
                    do {
                        FitnessTestCategoryModel temp = new FitnessTestCategoryModel();

                        temp.setSub_test_id(cr.getInt(cr.getColumnIndex("sub_test_id")));
                        temp.setTest_id(cr.getInt(cr.getColumnIndex("test_id")));
                        temp.setTest_name(cr.getString(cr.getColumnIndex("test_name")));
                        temp.setScore_criteria(cr.getString(cr.getColumnIndex("score_criteria")));
                        temp.setScore_measurement(cr.getString(cr.getColumnIndex("score_measurement")));
                        temp.setScore_unit(cr.getString(cr.getColumnIndex("score_unit")));
                        temp.setName(cr.getString(cr.getColumnIndex("name")));
                        temp.setTest_description(cr.getString(cr.getColumnIndex("test_description")));
                        temp.setPurpose(cr.getString(cr.getColumnIndex("purpose")));
                        temp.setAdministrative_Suggestions(cr.getString(cr.getColumnIndex("Administrative_Suggestions")));
                        temp.setEquipment_Required(cr.getString(cr.getColumnIndex("Equipment_Required")));
                        temp.setScoring(cr.getString(cr.getColumnIndex("scoring")));
                        temp.setCreated_on(cr.getString(cr.getColumnIndex("created_on")));
                        temp.setLast_modified_on(cr.getString(cr.getColumnIndex("last_modified_on")));
                        temp.setTest_applicable(cr.getString(cr.getColumnIndex("tests_applicable")));
                        temp.setMultipleLane(cr.getInt(cr.getColumnIndex("multiple_lane")));
                        temp.setTimerType(cr.getInt(cr.getColumnIndex("timer_type")));
                        temp.setVideoLink(cr.getString(cr.getColumnIndex("video_link")));

                        aData.add(temp);

                    } while (cr.moveToNext());
                }

                if (cr != null && !cr.isClosed()) {
                    cr.close();
                }

            } catch (Exception e) {
                Log.e("[" + TAG + "]: ", "TBL_LP_FITNESS_TEST_CATEGORY - getAllTableData(): ", e);
            }
        } else if (TBL_NAME.equalsIgnoreCase(TBL_LP_SKILL_TEST_TYPE)) {
            try {
                Cursor cr = null;

                cr = iDB.query(TBL_LP_SKILL_TEST_TYPE, null, null, null, null, null, null);


                // cr = iDB.query(TBL_LP_FITNESS_TEST_CATEGORY, null, null, null, null, null, null);

                if (cr.moveToFirst()) {
                    do {
                        SkillTestTypeModel temp = new SkillTestTypeModel();

                        temp.setChecklist_item_id(cr.getInt(cr.getColumnIndex("checklist_item_id")));
                        temp.setTest_type_id(cr.getInt(cr.getColumnIndex("test_type_id")));
                        temp.setItem_name(cr.getString(cr.getColumnIndex("item_name")));
                        temp.setCreated_on(cr.getString(cr.getColumnIndex("created_on")));
                        temp.setLast_modified_on(cr.getString(cr.getColumnIndex("last_modified_on")));
                        temp.setSynced(cr.getString(cr.getColumnIndex("synced")));


                        aData.add(temp);

                    } while (cr.moveToNext());
                }

                if (cr != null && !cr.isClosed()) {
                    cr.close();
                }

            } catch (Exception e) {
                Log.e("[" + TAG + "]: ", "TBL_LP_FITNESS_TEST_CATEGORY - getAllTableData(): ", e);
            }
        }



        return aData;

    }

    /**
     * GET THE ID ON THE BASIS OF ARGUMENT PASSED
     *
     * @param TBL_NAME
     * @param whereClause
     * @param whereArg
     * @return
     */
    public int getID(String TBL_NAME, String whereClause, String whereArg) {
        int iD = 0;

        if (iDB == null) {
            // OPENING DATABASE CONN.
            try {
                DBManager.getInstance().openDB();
            } catch (Exception e) {
                Log.e("["+TAG+"]:", "'" + TAG + "' - getID()", e);
            }
        }

        Cursor cr = null;
        try {

            cr = iDB.query(TBL_NAME, null, whereClause + "=?", new String[]{whereArg}, null, null, null);

        } catch (Exception e) {
            Log.e("["+TAG+"]:", "getID() : ", e);
        } finally {
            // close the SqlCursor object.
            if (cr != null && !cr.isClosed()) {
                cr.close();
            }
        }

        return iD;
    }

    /**
     * GET THE LAST ROW COUNT AVAILABLE IN THE TABLE DENOTED BY 'TBL_NAME'... *
     *
     * @param TBL_NAME
     * @return
     */
    public int lastRowId(String TBL_NAME) {
        int lastRowCount = 0;

        if (iDB == null) {
            // OPENING DATABASE CONN.
            try {
                DBManager.getInstance().openDB();
            } catch (Exception e) {
                Log.e("["+TAG+"]:", "'" + TAG + "' - lastRowId()", e);
            }
        }

        Cursor cr = null;
        try {

            cr = iDB.rawQuery("SELECT max(_id) FROM " + TBL_NAME, null);

            if (cr.moveToFirst()) {

                lastRowCount = cr.getInt(0);

            } else {
                lastRowCount = 0;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            Log.e("["+TAG+"]:", "lastRowId() : ", e);
        } finally {
            // close the SqlCursor object.
            if (cr != null && !cr.isClosed()) {
                cr.close();
            }
        }

        return lastRowCount;
    }

    /**
     * @param TBL_NAME
     * @return
     */
    public int getTotalCount(Context context,String TBL_NAME,String ARGS1,String ARGS2,String ARGS3,String ARGS4,String ARGS5,String ARGS6) {
        iContext = context;
        int count = 0;
        Cursor cursor=null;

        if (iDB == null) {
            // OPENING DATABASE CONN.
            try {
                DBManager.getInstance().openDB();
            } catch (Exception e) {
                //
            }
        }

        if (!checkForTableExists(iDB,TBL_NAME) || TBL_NAME == null || iDB == null || !iDB.isOpen())
            return 0;

        /*
         * Cursor cursor = iDB.rawQuery(
         * "SELECT state FROM sqlite_master WHERE type = ? AND name = ?", new
         * String[] { "table", TBL_NAME });
         */
        if(TBL_NAME.equalsIgnoreCase(TBL_LP_STUDENT_MASTER) && ARGS4.equalsIgnoreCase(""))
            cursor = iDB.rawQuery("select * from " + TBL_NAME + " where " + ARGS1 + "='" + ARGS2 + "'" , null);
        else if(TBL_NAME.equalsIgnoreCase(TBL_LP_STUDENT_MASTER))
            cursor = iDB.rawQuery("select * from " + TBL_NAME + " where " + ARGS1 + "='" + ARGS2 + "'" + " AND " + ARGS3 + "='" + ARGS4 + "'", null);
        else
            cursor = iDB.rawQuery("select * from " + TBL_NAME + " where " + ARGS1 + "='" + ARGS2 + "'" + " AND " + ARGS3 + "='" + ARGS4 + "'"+ " AND " + ARGS5 + "='" + ARGS6 + "'", null);
        count = cursor.getCount();

        // close the SqlCursor object.
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return count;
    }

    private boolean checkForTableExists(SQLiteDatabase db, String table){
        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='"+table+"'";
        Cursor mCursor = db.rawQuery(sql, null);
        if (mCursor.getCount() > 0) {
            return true;
        }
        mCursor.close();
        return false;
    }

    public void UpdateRow(Context context, String TBL_NAME, String ARGS1, String ARGS2, String ARGS3, String ARGS4, String ARGS5, String ARGS6,
                          String ARGS7, String ARGS8,String ARGS9, String ARGS10   ) {
        iContext = context;
        ArrayList<Object> aData = new ArrayList<Object>();

        if (iDB == null) {

            // OPENING DATABASE CONN.
            try {
                DBManager.getInstance().openDB();
            } catch (Exception e) {
                Log.e("["+TAG+"]:", "'" + TAG + "' - getAllTableData() :", e);
            }
        }
        try {

            Cursor cr = null;


            cr = iDB.rawQuery("update " + TBL_NAME + " set " + ARGS1 + "='" + ARGS2 + "'" + " , " + ARGS3 + "='" + ARGS4 + "'" + " where " +
                    ARGS5 + "='" + ARGS6 + "'" + " AND " + ARGS7 + "='" + ARGS8 + "'", null);

            String query = "update " + TBL_NAME + " set " + ARGS1 + "='" + ARGS2 + "'" + " , " + ARGS3 + "='" + ARGS4 + "'" + " where " +
                    ARGS5 + "='" + ARGS6 + "'" + " AND " + ARGS7 + "='" + ARGS8 + "'";

            Log.e(TAG, "update query==> " + query);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    /**
     * @param TBL_NAME
     * @return
     */
    public boolean checkIfDataExists(String TBL_NAME) {
        boolean retVal = false;

        if (iDB == null) {
            // OPENING DATABASE CONNECTION
            try {
                DBManager.getInstance().openDB();
            } catch (Exception e) {
                Log.e("["+TAG+"]:", "'" + TAG + "' - checkIfDataExists()", e);
            }
        }

        Cursor cr = null;
        try {

            cr = iDB.rawQuery("SELECT COUNT(*) FROM " + TBL_NAME, null);

            if (cr != null) {
                cr.moveToFirst(); // Always one row returned.
                if (cr.getInt(0) == 0) { // Zero count means empty table.
                    retVal = false;
                } else {
                    retVal = true;
                }
            } else {
                retVal = false;
            }

        } catch (Exception e) {
            Log.e("["+TAG+"]:", "checkIfDataExists() : ", e);
        } finally {
            // close the SqlCursor object.
            if (cr != null && !cr.isClosed()) {
                cr.close();
            }
        }

        return retVal;
    }

    /**
     * @param TBL_NAME
     * @return
     */
    public boolean isTableExists(String TBL_NAME) {

        if (iDB == null) {
            // OPENING DATABASE CONN.
            try {
                DBManager.getInstance().openDB();
            } catch (Exception e) {
                //
            }
        }

        if (TBL_NAME == null || iDB == null || !iDB.isOpen()) {
            return false;
        }
        Cursor cursor = iDB.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[]{"table", TBL_NAME});

        if (!cursor.moveToFirst()) {
            return false;
        }

        int count = cursor.getInt(0);

        // close the SqlCursor object.
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return count > 0;
    }

    /**
     * delete a particular row from the Table.
     */
    public boolean deleteRows(Context context, String TBL_NAME, String whereClause, String whereArg,
                              String whereClause2, String whereArg2) {
        iContext = context;
        if (iDB == null) {
            // OPENING DATABASE CONN.
            try {
                DBManager.getInstance().openDB();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {

            return iDB.delete(TBL_NAME, whereClause + "=" + whereArg+ " AND " + whereClause2 + "='" + whereArg2 + "'", null) > 0;

        } catch (Exception e) {
            Log.e("["+TAG+"]:", "'" + TAG + "' - deleteRows() : ", e);
            return iDB.delete(TBL_NAME, whereClause + "=" + whereArg, null) > 0;
        }

    }

    public boolean deleteDartStudentRows(Context context, String TBL_NAME, String whereClause, String whereArg,
                                         String whereClause2, String whereArg2,String whereClause3, String whereArg3) {
        iContext = context;
        if (iDB == null) {
            // OPENING DATABASE CONN.
            try {
                DBManager.getInstance().openDB();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {

            return iDB.delete(TBL_NAME, whereClause + "=" + whereArg+ " AND " + whereClause2 + "='" + whereArg2 + "'"+ " AND " + whereClause3 + "='" + whereArg3 + "'", null) > 0;

        } catch (Exception e) {
            Log.e("["+TAG+"]:", "'" + TAG + "' - deleteRows() : ", e);
            return 0 > 0;
        }

    }


    public boolean deleteTransactionRow(Context context, String TBL_NAME, String whereClause, String whereArg,
                                        String whereClause2, String whereArg2,String whereClause3, String whereArg3) {
        iContext = context;
        if (iDB == null) {
            // OPENING DATABASE CONN.
            try {
                DBManager.getInstance().openDB();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            return iDB.delete(TBL_NAME, whereClause + "=" + whereArg + " AND " + whereClause2 + "='" + whereArg2 + "'"+" AND " + whereClause3 + "='" + whereArg3 + "'", null) > 0;

        } catch (Exception e) {
            Log.e("["+TAG+"]:", "'" + TAG + "' - deleteRows() : ", e);


        }
        return 0 > 0;

    }

    @SuppressLint("LongLogTag")
    public boolean deleteStudentRow(Context context, String TBL_NAME, String whereClause, String whereArg,
                                    String whereClause2, String whereArg2) {
        iContext = context;
        if (iDB == null) {
            // OPENING DATABASE CONN.
            try {
                DBManager.getInstance().openDB();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {

            return iDB.delete(TBL_NAME, whereClause + "='" + whereArg + "'" + " AND " + whereClause2 + "='" + whereArg2 + "'", null) > 0;

        } catch (Exception e) {
            Log.e("["+TAG+"]:", "'" + TAG + "' - deleteRows() : ", e);

            return 0 > 0;
        }

    }

    public boolean deleteTestRow(Context context, String TBL_NAME, String whereClause, String whereArg) {
        iContext = context;
        if (iDB == null) {
            // OPENING DATABASE CONN.
            try {
                DBManager.getInstance().openDB();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {

            return iDB.delete(TBL_NAME, whereClause + "='" + whereArg + "'", null) > 0;

        } catch (Exception e) {
            Log.e("["+TAG+"]:", "'" + TAG + "' - deleteRows() : ", e);

            return 0 > 0;
        }

    }

    public boolean deleteSchoolOrStudentUserRow(Context context, String TBL_NAME, String whereClause, String whereArg,
                                                String whereClause2, String whereArg2) {
        iContext = context;
        if (iDB == null) {
            // OPENING DATABASE CONN.
            try {
                DBManager.getInstance().openDB();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {

            return iDB.delete(TBL_NAME, whereClause + "='" + whereArg + "'", null) > 0;

        } catch (Exception e) {
            Log.e("["+TAG+"]:", "'" + TAG + "' - deleteRows() : ", e);

            return 0 > 0;
        }

    }

    /**
     * @param TBL_NAME
     * @return
     */
    public boolean deleteAllRows(String TBL_NAME) {

        if (iDB == null) {
            // OPENING DATABASE CONN.
            try {
                DBManager.getInstance().openDB();
            } catch (Exception e) {

            }
        }

        try {
            return iDB.delete(TBL_NAME, null, null) > 0;

        } catch (Exception e) {
            Log.e("["+TAG+"]:", "deleteAllRows() : ", e);

            return 0 > 0;
        }

    }

    /**
     * Function resembles - table drop on the basis of table_name as parameter
     * <p>
     * CREATED ON : 1/07/2013
     *
     * @param TBL_NAME
     */
    public void dropNamedTable(Context context, String TBL_NAME) {

        iContext = context;

        if (iDB == null) {
            // OPENING DATABASE CONN.
            try {
                DBManager.getInstance().openDB();
            } catch (Exception e) {
                //
            }
        }
        try {
            iDB.execSQL("Drop table if exists " + TBL_NAME);
        } catch (Exception e) {
            Log.e("[ " + TAG + " ] : ", "dropNamedTable(): ", e);
        }
    }

    /**
     * drop all tables used while syncEmployees from server.
     */
    public void dropTables(Context context) {

        iContext = context;

        if (iDB == null) {
            // OPENING DATABASE CONN.
            try {
                DBManager.getInstance().openDB();
            } catch (Exception e) {
                //
            }
        }

        try {
            iDB.execSQL("Drop table if exists " + TBL_LP_USER_LOGIN);
            iDB.execSQL("Drop table if exists " + TBL_LP_TEST_COORDINATORS);
            iDB.execSQL("Drop table if exists " + TBL_LP_SCHOOLS_MASTER);
            iDB.execSQL("Drop table if exists " + TBL_LP_STUDENT_MASTER);
            iDB.execSQL("Drop table if exists " + TBL_LP_CAMP_MASTER);
            iDB.execSQL("Drop table if exists " + TBL_LP_FITNESS_TEST_TYPES);
            iDB.execSQL("Drop table if exists " + TBL_LP_FITNESS_TEST_RESULT);

        } catch (Exception e) {
            Log.e("[" + TAG + "] : ", "dropTables()", e);

        }

    }

        /*try {
            iDB.execSQL("Drop table if exists " + TBL_LP_USER_LOGIN);
            iDB.execSQL("Drop table if exists " + TBL_LP_TEST_COORDINATORS);
            iDB.execSQL("Drop table if exists " + TBL_LP_SCHOOLS_MASTER);
            iDB.execSQL("Drop table if exists " + TBL_LP_STUDENT_MASTER);
            iDB.execSQL("Drop table if exists " + TBL_LP_CAMP_MASTER);

        } catch (Exception e) {
            Log.e("[" + TAG + "] : ", "dropTables()", e);

        }*/


    // ********************  particula rorw  *********************************************************//

    public HashMap<String, String> getParticularRow(Context context, String TBL_NAME, String ARGS1, String ARGS2,
                                                    String ARGS3, String ARGS4) {

        iContext = context;
        HashMap<String, String> map = new HashMap<String, String>();

        if (iDB == null) {

            iDbHelper = new DatabaseHelper(iContext);
            iDB = iDbHelper.getWritableDatabase();

            try {
                DBManager.getInstance().openDB();
            } catch (Exception e) {
                Log.e("["+TAG+"]:", "'" + TAG + "' - getAllTableData() :", e);
            }
        }

        if (TBL_NAME.equalsIgnoreCase(TBL_LP_STUDENT_MASTER)) {
            try {
                Cursor cr = null;
                Log.e("query==>", "select * from " + TBL_LP_STUDENT_MASTER + " where " + ARGS1 + "='" + ARGS2 + "'" + " AND " + ARGS3 + "='" + ARGS4 + "'");
                cr = iDB.rawQuery("select * from " + TBL_LP_STUDENT_MASTER + " where " + ARGS1 + "='" + ARGS2 + "'" + " AND " + ARGS3 + "='" + ARGS4 + "'", null);
                //cr =  iDB.rawQuery("select * from " + TBL_LP_STUDENT_MASTER + " where " + ARGS1 + "='" + ARGS2 + "'", null);

                if (cr.moveToFirst()) {
                    do {
                        //StudentMasterModel temp = new StudentMasterModel();

                        //map.put("student_liveplus_id", "" + cr.getInt(cr.getColumnIndex("student_liveplus_id")));
                        //map.put("user_login_id", "" + cr.getInt(cr.getColumnIndex("user_login_id")));
                        map.put("student_name", cr.getString(cr.getColumnIndex("student_name")));
                        map.put("current_school_id", cr.getString(cr.getColumnIndex("current_school_id")));
                        map.put("current_roll_num", cr.getString(cr.getColumnIndex("current_roll_num")));
                        map.put("gender", "" + cr.getInt(cr.getColumnIndex("gender")));
                        map.put("dob", cr.getString(cr.getColumnIndex("dob")));
                        map.put("email", cr.getString(cr.getColumnIndex("email")));
                        map.put("gaurdian_name", cr.getString(cr.getColumnIndex("gaurdian_name")));
                        map.put("phone", cr.getString(cr.getColumnIndex("phone")));
                        map.put("address", cr.getString(cr.getColumnIndex("address")));
                        map.put("created_on", cr.getString(cr.getColumnIndex("created_on")));
                        map.put("created_by", cr.getString(cr.getColumnIndex("created_by")));
                        map.put("last_modified_on", cr.getString(cr.getColumnIndex("last_modified_on")));
                        map.put("last_modified_by", cr.getString(cr.getColumnIndex("last_modified_by")));
                        map.put("student_id", "" + cr.getInt(cr.getColumnIndex("student_id")));
                        map.put("student_registration_num", cr.getString(cr.getColumnIndex("student_registration_num")));
                        map.put("current_class", cr.getString(cr.getColumnIndex("current_class")));
                        map.put("class_id", cr.getString(cr.getColumnIndex("class_id")));
                        map.put("class_partition_id", "" + cr.getInt(cr.getColumnIndex("class_partition_id")));
                        map.put("section", cr.getString(cr.getColumnIndex("section")));
                        map.put("is_active", "" + cr.getInt(cr.getColumnIndex("is_active")));
                        map.put("camp_id", "" + cr.getString(cr.getColumnIndex("camp_id")));


                    } while (cr.moveToNext());
                }

                if (cr != null && !cr.isClosed()) {
                    cr.close();
                }

            } catch (Exception e) {
                Log.e("[" + TAG + "]: ", "TBL_LP_STUDENT_MASTER - getAllTableData(): ", e);
            }
        } else if (TBL_NAME.equalsIgnoreCase(TBL_LP_SCHOOLS_MASTER)) {
            try {
                Cursor cr = null;
                //Log.e("query==>", "select * from " + TBL_LP_SCHOOLS_MASTER + " where " + ARGS1 + "='" + ARGS2 + "'");

                if (ARGS3.equalsIgnoreCase(""))
                    cr = iDB.rawQuery("select * from " + TBL_LP_SCHOOLS_MASTER + " where " + ARGS1 + "='" + ARGS2 + "'", null);
                else
                    cr = iDB.rawQuery("select * from " + TBL_LP_SCHOOLS_MASTER + " where " + ARGS1 + "='" + ARGS2 + "' and " + ARGS3 + "='" + ARGS4 + "'", null);
                if (cr.moveToFirst()) {
                    do {

                        map.put("school_id", "" + cr.getInt(cr.getColumnIndex("school_id")));
                        map.put("school_name", "" + cr.getString(cr.getColumnIndex("school_name")));
                        map.put("latitude", "" + cr.getString(cr.getColumnIndex("latitude")));
                        map.put("longitude", "" + cr.getString(cr.getColumnIndex("longitude")));
                        map.put(AppConfig.IS_RETEST_ALLOWED, "" + cr.getString(cr.getColumnIndex(AppConfig.IS_RETEST_ALLOWED)));
                        //map.put("start_school_time", "" + cr.getString(cr.getColumnIndex("start_school_time")));




                    } while (cr.moveToNext());
                }

                if (cr != null && !cr.isClosed()) {
                    cr.close();
                }

            } catch (Exception e) {
                Log.e("[" + TAG + "]: ", "TBL_LP_SCHOOLS_MASTER - getAllTableData(): ", e);
            }
        }


        else if (TBL_NAME.equalsIgnoreCase(TBL_LP_SCREEN_MASTER)) {
            try {
                Cursor cr = null;
                //   Log.e("query==>", "select * from " + TBL_LP_SCREEN_MASTER + " where " + ARGS1 + "='" + ARGS2 + "'");
                cr = iDB.rawQuery("select * from " + TBL_LP_SCREEN_MASTER + " where " + ARGS1 + "='" + ARGS2 + "'", null);

                if (cr.moveToFirst()) {
                    do {
                        //  screen_id, parent_id, screen_name, web_url, mobile_icon_title, icon_image_name, icon_path,
                        // created_on, created_by, modified_on, modified_by, is_deleted
                        map.put("screen_id", "" + cr.getInt(cr.getColumnIndex("screen_id")));
                        map.put("parent_id", "" + cr.getInt(cr.getColumnIndex("parent_id")));
                        map.put("screen_name", "" + cr.getString(cr.getColumnIndex("screen_name")));
                        map.put("web_url", "" + cr.getString(cr.getColumnIndex("web_url")));
                        map.put("mobile_icon_title", "" + cr.getString(cr.getColumnIndex("mobile_icon_title")));
                        map.put("icon_image_name", "" + cr.getString(cr.getColumnIndex("icon_image_name")));
                        map.put("icon_path", "" + cr.getString(cr.getColumnIndex("icon_path")));
                        map.put("created_on", "" + cr.getString(cr.getColumnIndex("created_on")));
                        map.put("created_by", "" + cr.getString(cr.getColumnIndex("created_by")));
                        map.put("modified_on", "" + cr.getString(cr.getColumnIndex("modified_on")));
                        map.put("modified_by", "" + cr.getString(cr.getColumnIndex("modified_by")));
                        map.put("is_deleted", "" + cr.getString(cr.getColumnIndex("is_deleted")));


                    } while (cr.moveToNext());
                }

                if (cr != null && !cr.isClosed()) {
                    cr.close();
                }

            } catch (Exception e) {
                Log.e("[" + TAG + "]: ", "TBL_LP_SCHOOLS_MASTER - getAllTableData(): ", e);
            }
        }

        else if(TBL_NAME.equalsIgnoreCase(TBL_LP_USER_SCREEN_MAP)){
            try {
                Cursor cr = null;

                cr = iDB.rawQuery("select * from " + TBL_LP_USER_SCREEN_MAP + " where " + ARGS1 + "='" + ARGS2 + "'" , null);
                // Log.e("DBManager","query==> "+"select * from " + TBL_LP_USER_SCREEN_MAP + " where " + ARGS1 + "='" + ARGS2 + "'");

                if (cr.moveToFirst()) {
                    do {

                        map.put("screen_id", "" + cr.getString(cr.getColumnIndex("screen_id")));
                        map.put("user_type_id", "" + cr.getInt(cr.getColumnIndex("user_type_id")));

                    } while (cr.moveToNext());
                }

                if (cr != null && !cr.isClosed()) {
                    cr.close();
                }

            } catch (Exception e) {
                Log.e("[" + TAG + "]: ", "TBL_LP_FITNESS_TEST_TYPES - getAllTableData(): ", e);
            }
        }


        else if (TBL_NAME.equalsIgnoreCase(TBL_LP_USER_SCHOOL_MAPPING)) {
            try {
                Cursor cr = null;
                //Log.e("query==>", "select * from " + TBL_LP_USER_SCHOOL_MAPPING + " where " + ARGS1 + "='" + ARGS2 + "'" + " AND " + ARGS3 + "='" + ARGS4 + "'");
                cr = iDB.rawQuery("select * from " + TBL_LP_USER_SCHOOL_MAPPING + " where " + ARGS1 + "='" + ARGS2 + "'" + " AND " + ARGS3 + "='" + ARGS4 + "'", null);
                //cr =  iDB.rawQuery("select * from " + TBL_LP_STUDENT_MASTER + " where " + ARGS1 + "='" + ARGS2 + "'", null);

                if (cr.moveToFirst()) {
                    do {

                        map.put("user_id", "" + cr.getInt(cr.getColumnIndex("user_id")));
                        map.put("school_id", "" + cr.getInt(cr.getColumnIndex("school_id")));
                        map.put("created_on", cr.getString(cr.getColumnIndex("created_on")));
                        map.put("last_modified_on", cr.getString(cr.getColumnIndex("last_modified_on")));

                    } while (cr.moveToNext());
                }

                if (cr != null && !cr.isClosed()) {
                    cr.close();
                }

            } catch (Exception e) {
                Log.e("[" + TAG + "]: ", "TBL_LP_USER_SCHOOL_MAPPING - getAllTableData(): ", e);
            }
        } else if (TBL_NAME.equalsIgnoreCase(TBL_LP_STUDENT_USER_MASTER)) {
            try {
                Cursor cr = null;
                Log.e("query==>", "select * from " + TBL_LP_STUDENT_USER_MASTER + " where " + ARGS1 + "='" + ARGS2 + "'");
                cr = iDB.rawQuery("select * from " + TBL_LP_STUDENT_USER_MASTER + " where " + ARGS1 + "='" + ARGS2 + "'", null);

                if (cr.moveToFirst()) {
                    do {

                        //map.put("user_login_id", "" + cr.getInt(cr.getColumnIndex("user_login_id")));
                        map.put("student_name", "" + cr.getString(cr.getColumnIndex("student_name")));
                        map.put("guardian_name", "" + cr.getString(cr.getColumnIndex("guardian_name")));
                        map.put("image_path", "" + cr.getString(cr.getColumnIndex("image_path")));
                        map.put("phone", "" + cr.getInt(cr.getColumnIndex("phone")));
                        map.put("address", "" + cr.getString(cr.getColumnIndex("address")));
                        map.put("current_class", "" + cr.getString(cr.getColumnIndex("current_class")));
                        map.put("date_of_birth", "" + cr.getString(cr.getColumnIndex("date_of_birth")));
                        map.put("gender", "" + cr.getString(cr.getColumnIndex("gender")));


                    } while (cr.moveToNext());
                }

                if (cr != null && !cr.isClosed()) {
                    cr.close();
                }

            } catch (Exception e) {
                Log.e("[" + TAG + "]: ", "TBL_LP_STUDENT_USER_MASTER - getAllTableData(): ", e);
            }
        } else if (TBL_NAME.equalsIgnoreCase(TBL_LP_FITNESS_TEST_TYPES)) {
            try {
                Cursor cr = null;
                Log.e("query==>", "select * from " + TBL_LP_FITNESS_TEST_TYPES + " where " + ARGS1 + "='" + ARGS2 + "'");
                cr = iDB.rawQuery("select * from " + TBL_LP_FITNESS_TEST_TYPES + " where " + ARGS1 + "='" + ARGS2 + "'", null);

                if (cr.moveToFirst()) {
                    do {
                        map.put("test_type_id", "" + cr.getInt(cr.getColumnIndex("test_type_id")));
                        map.put("test_name", "" + cr.getString(cr.getColumnIndex("test_name")));
                        map.put("test_image", cr.getString(cr.getColumnIndex("test_image")));
                        map.put("test_img_path", cr.getString(cr.getColumnIndex("test_img_path")));
                        map.put("created_on", cr.getString(cr.getColumnIndex("created_on")));
                        map.put("last_modified_on", cr.getString(cr.getColumnIndex("last_modified_on")));
                        map.put("school_id", cr.getString(cr.getColumnIndex("school_id")));
                        map.put("test_coordinator_id", cr.getString(cr.getColumnIndex("test_coordinator_id")));

                    } while (cr.moveToNext());
                }

                if (cr != null && !cr.isClosed()) {
                    cr.close();
                }

            } catch (Exception e) {
                Log.e("[" + TAG + "]: ", "TBL_LP_FITNESS_TEST_TYPES - getAllTableData(): ", e);
            }
        } else if (TBL_NAME.equalsIgnoreCase(TBL_LP_FITNESS_TEST_CATEGORY)) {
            try {
                Cursor cr = null;
                Log.e("query==>", "select * from " + TBL_LP_FITNESS_TEST_CATEGORY + " where " + ARGS1 + "='" + ARGS2 + "'");
                cr = iDB.rawQuery("select * from " + TBL_LP_FITNESS_TEST_CATEGORY + " where " + ARGS1 + "='" + ARGS2 + "'", null);

                if (cr.moveToFirst()) {
                    do {
                        map.put("sub_test_id", "" + cr.getInt(cr.getColumnIndex("sub_test_id")));
                        map.put("test_id", "" + cr.getInt(cr.getColumnIndex("test_id")));
                        map.put("test_name", "" + cr.getString(cr.getColumnIndex("test_name")));
                        map.put("score_criteria", cr.getString(cr.getColumnIndex("score_criteria")));
                        map.put("score_measurement", cr.getString(cr.getColumnIndex("score_measurement")));
                        map.put("score_unit", cr.getString(cr.getColumnIndex("score_unit")));
                        map.put("name", cr.getString(cr.getColumnIndex("name")));
                        map.put("test_description", cr.getString(cr.getColumnIndex("test_description")));
                        map.put("purpose", cr.getString(cr.getColumnIndex("purpose")));
                        map.put("Administrative_Suggestions", cr.getString(cr.getColumnIndex("Administrative_Suggestions")));
                        map.put("Equipment_Required", cr.getString(cr.getColumnIndex("Equipment_Required")));
                        map.put("scoring", cr.getString(cr.getColumnIndex("scoring")));
                        map.put("created_on", cr.getString(cr.getColumnIndex("created_on")));
                        map.put("last_modified_on", cr.getString(cr.getColumnIndex("last_modified_on")));
                        map.put("tests_applicable", cr.getString(cr.getColumnIndex("tests_applicable")));
                        map.put("multiple_lane", cr.getString(cr.getColumnIndex("multiple_lane")));
                        map.put("timer_type", cr.getString(cr.getColumnIndex("timer_type")));
                        map.put("video_link", cr.getString(cr.getColumnIndex("video_link")));


                    } while (cr.moveToNext());
                }

                if (cr != null && !cr.isClosed()) {
                    cr.close();
                }

            } catch (Exception e) {
                Log.e("[" + TAG + "]: ", "TBL_LP_FITNESS_TEST_CATEGORY - getAllTableData(): ", e);
            }
        } else if (TBL_NAME.equalsIgnoreCase(TBL_LP_SKILL_TEST_TYPE)) {
            try {
                Cursor cr = null;
                Log.e("query==>", "select * from " + TBL_LP_SKILL_TEST_TYPE + " where " + ARGS1 + "='" + ARGS2 + "'");
                cr = iDB.rawQuery("select * from " + TBL_LP_SKILL_TEST_TYPE + " where " + ARGS1 + "='" + ARGS2 + "'", null);

                if (cr.moveToFirst()) {
                    do {

                        map.put("checklist_item_id", "" + cr.getInt(cr.getColumnIndex("checklist_item_id")));
                        map.put("item_name", "" + cr.getString(cr.getColumnIndex("item_name")));
                        map.put("test_type_id", "" + cr.getString(cr.getColumnIndex("test_type_id")));
                        map.put("created_on", "" + cr.getString(cr.getColumnIndex("created_on")));
                        map.put("last_modified_on", "" + cr.getString(cr.getColumnIndex("last_modified_on")));
                        map.put("synced", "" + cr.getString(cr.getColumnIndex("synced")));


                    } while (cr.moveToNext());
                }

                if (cr != null && !cr.isClosed()) {
                    cr.close();
                }

            } catch (Exception e) {
                Log.e("[" + TAG + "]: ", "TBL_LP_FITNESS_TEST_TYPES - getAllTableData(): ", e);
            }
        } else if (TBL_NAME.equalsIgnoreCase(TBL_LP_FITNESS_SKILL_TEST_RESULT)) {
            try {
                Cursor cr = null;
                Log.e("query==>", "select * from " + TBL_LP_FITNESS_SKILL_TEST_RESULT + " where " + ARGS1 + "='" + ARGS2 + "'" + " AND " + ARGS3 + "='" + ARGS4 + "'");
                cr = iDB.rawQuery("select * from " + TBL_LP_FITNESS_SKILL_TEST_RESULT + " where " + ARGS1 + "='" + ARGS2 + "'" + " AND " + ARGS3 + "='" + ARGS4 + "'", null);

                if (cr.moveToFirst()) {
                    do {

                        map.put("skill_score_id", "" + cr.getInt(cr.getColumnIndex("skill_score_id")));
                        map.put("student_id", "" + cr.getString(cr.getColumnIndex("student_id")));
                        map.put("camp_id", "" + cr.getString(cr.getColumnIndex("camp_id")));
                        map.put("test_type_id", "" + cr.getString(cr.getColumnIndex("test_type_id")));
                        map.put("checklist_item_id", "" + cr.getString(cr.getColumnIndex("checklist_item_id")));
                        map.put("coordinator_id", "" + cr.getString(cr.getColumnIndex("coordinator_id")));

                        map.put("score", "" + cr.getInt(cr.getColumnIndex("score")));
                        map.put("created_on", "" + cr.getString(cr.getColumnIndex("created_on")));
                        map.put("longitude", "" + cr.getString(cr.getColumnIndex("longitude")));
                        map.put("latitude", "" + cr.getString(cr.getColumnIndex("latitude")));
                        map.put("last_modified_on", "" + cr.getString(cr.getColumnIndex("last_modified_on")));

                        map.put("tested", cr.getString(cr.getColumnIndex("tested")));
                        map.put("synced", cr.getString(cr.getColumnIndex("synced")));

                        map.put("device_date", "" + cr.getString(cr.getColumnIndex("device_date")));

                    } while (cr.moveToNext());
                }

                if (cr != null && !cr.isClosed()) {
                    cr.close();
                }

            } catch (Exception e) {
                Log.e("[" + TAG + "]: ", "TBL_LP_FITNESS_SKILL_TEST_RESULT - getAllTableData(): ", e);
            }
        } else if (TBL_NAME.equalsIgnoreCase(TBL_LP_SCHOOL_CLASS_FLAG_MAPPING)) {
            try {
                Cursor cr = null;
                Log.e("query==>", "select * from " + TBL_LP_SCHOOL_CLASS_FLAG_MAPPING + " where " + ARGS1 + "='" + ARGS2 + "'" + " AND " + ARGS3 + "='" + ARGS4 + "'");
                cr = iDB.rawQuery("select * from " + TBL_LP_SCHOOL_CLASS_FLAG_MAPPING + " where " + ARGS1 + "='" + ARGS2 + "'" + " AND " + ARGS3 + "='" + ARGS4 + "'", null);

                if (cr.moveToFirst()) {
                    do {

                        map.put("school_id", "" + cr.getInt(cr.getColumnIndex("school_id")));
                        map.put("class_id", "" + cr.getInt(cr.getColumnIndex("class_id")));
                        map.put("class_name", "" + cr.getString(cr.getColumnIndex("class_name")));
                        map.put("flag", "" + cr.getInt(cr.getColumnIndex("flag")));

                    } while (cr.moveToNext());
                }

                if (cr != null && !cr.isClosed()) {
                    cr.close();
                }

            } catch (Exception e) {
                Log.e("[" + TAG + "]: ", "TBL_LP_FITNESS_SKILL_TEST_RESULT - getAllTableData(): ", e);
            }
        } else if (TBL_NAME.equalsIgnoreCase(TBL_LP_CAMP_TEST_MAPPING)) {
            try {
                Cursor cr = null;
                Log.e("query==>", "select * from " + TBL_LP_CAMP_TEST_MAPPING + " where " + ARGS1 + "='" + ARGS2 + "'" + " AND " + ARGS3 + "='" + ARGS4 + "'");
                cr = iDB.rawQuery("select * from " + TBL_LP_CAMP_TEST_MAPPING + " where " + ARGS1 + "='" + ARGS2 + "'" + " AND " + ARGS3 + "='" + ARGS4 + "'", null);

                if (cr.moveToFirst()) {
                    do {

                        map.put("camp_id", "" + cr.getInt(cr.getColumnIndex("camp_id")));
                        map.put("school_id", "" + cr.getInt(cr.getColumnIndex("school_id")));
                        map.put("test_type_id", "" + cr.getInt(cr.getColumnIndex("test_type_id")));
                        map.put("class_id", "" + cr.getString(cr.getColumnIndex("class_id")));

                    } while (cr.moveToNext());
                }

                if (cr != null && !cr.isClosed()) {
                    cr.close();
                }

            } catch (Exception e) {
                Log.e("[" + TAG + "]: ", "TBL_LP_FITNESS_SKILL_TEST_RESULT - getAllTableData(): ", e);
            }
        } else if (TBL_NAME.equalsIgnoreCase(TBL_LP_TEST_COORDINATOR_MAPPING)) {
            try {
                Cursor cr = null;
                Log.e("query==>", "select * from " + TBL_LP_TEST_COORDINATOR_MAPPING + " where " + ARGS1 + "='" + ARGS2 + "'" + " AND " + ARGS3 + "='" + ARGS4 + "'");
                cr = iDB.rawQuery("select * from " + TBL_LP_TEST_COORDINATOR_MAPPING + " where " + ARGS1 + "='" + ARGS2 + "'" + " AND " + ARGS3 + "='" + ARGS4 + "'", null);

                if (cr.moveToFirst()) {
                    do {

                        map.put("camp_id", "" + cr.getInt(cr.getColumnIndex("camp_id")));
                        map.put("school_id", "" + cr.getInt(cr.getColumnIndex("school_id")));
                        map.put("test_type_id", "" + cr.getInt(cr.getColumnIndex("test_type_id")));

                    } while (cr.moveToNext());
                }

                if (cr != null && !cr.isClosed()) {
                    cr.close();
                }

            } catch (Exception e) {
                Log.e("[" + TAG + "]: ", "TBL_LP_FITNESS_SKILL_TEST_RESULT - getAllTableData(): ", e);
            }
        } else if (TBL_NAME.equalsIgnoreCase(TBL_LP_FITNESS_TEST_RESULT) || TBL_NAME.equalsIgnoreCase(TBL_LP_FITNESS_RETEST_RESULT)) {
            try {
                Cursor cr = null;
                Log.e("query==>", "select * from " + TBL_NAME + " where " + ARGS1 + "='" + ARGS2 + "'" + " AND " + ARGS3 + "='" + ARGS4 + "'");
                cr = iDB.rawQuery("select * from " + TBL_NAME + " where " + ARGS1 + "='" + ARGS2 + "'" + " AND " + ARGS3 + "='" + ARGS4 + "'", null);


                if (cr.moveToFirst()) {
                    do {
                        map.put("student_id", cr.getString(cr.getColumnIndex("student_id")));
                        map.put("camp_id", cr.getString(cr.getColumnIndex("camp_id")));
                        map.put("test_type_id", cr.getString(cr.getColumnIndex("test_type_id")));
                        map.put("test_coordinator_id", cr.getString(cr.getColumnIndex("test_coordinator_id")));
                        map.put("score", cr.getString(cr.getColumnIndex("score")));
                        map.put("percentile", cr.getString(cr.getColumnIndex("percentile")));
                        map.put("created_on", cr.getString(cr.getColumnIndex("created_on")));
                        map.put("created_by", cr.getString(cr.getColumnIndex("created_by")));
                        map.put("last_modified_on", cr.getString(cr.getColumnIndex("last_modified_on")));
                        map.put("getColumnIndex", cr.getString(cr.getColumnIndex("last_modified_by")));
                        map.put("latitude", cr.getString(cr.getColumnIndex("latitude")));
                        map.put("longitude", cr.getString(cr.getColumnIndex("longitude")));
                        map.put("test_name", cr.getString(cr.getColumnIndex("test_name")));
                        map.put("sub_test_name", cr.getString(cr.getColumnIndex("sub_test_name")));
                        map.put("tested", cr.getString(cr.getColumnIndex("tested")));
                        map.put("synced", cr.getString(cr.getColumnIndex("synced")));
                        map.put("device_date", cr.getString(cr.getColumnIndex("device_date")));

                    } while (cr.moveToNext());
                }

                if (cr != null && !cr.isClosed()) {
                    cr.close();
                }

            } catch (Exception e) {
                Log.e("[" + TAG + "]: ", "TBL_LP_FITNESS_TEST_CATEGORY - getAllTableData(): ", e);
            }
        }
        return map;
    }

    public HashMap<String, String> getSchoolParticularRow(Context context, String TBL_NAME, String ARGS1, String ARGS2,
                                                          String ARGS3, String ARGS4,String ARGS5) {

        iContext = context;
        HashMap<String, String> map = new HashMap<String, String>();

        if (iDB == null) {

            iDbHelper = new DatabaseHelper(iContext);
            iDB = iDbHelper.getWritableDatabase();

            try {
                DBManager.getInstance().openDB();
            } catch (Exception e) {
                Log.e("["+TAG+"]:", "'" + TAG + "' - getAllTableData() :", e);
            }
        }

        try {
            Cursor cr = null;
            //Log.e("query==>", "select * from " + TBL_LP_SCHOOLS_MASTER + " where " + ARGS1 + "='" + ARGS2 + "'");

            if (ARGS3.equalsIgnoreCase(""))
                cr = iDB.rawQuery("select * from " + TBL_LP_SCHOOLS_MASTER + " where " + ARGS1 + "='" + ARGS2 + "'", null);
            else
                cr = iDB.rawQuery("select * from " + TBL_LP_SCHOOLS_MASTER + " where " + ARGS1 + "='" + ARGS2 + "' and " + ARGS3 + "='" + ARGS4 + "' and "+
                        AppConfig.IS_RETEST_ALLOWED+"='"+ARGS5+"'", null);
//            cr = iDB.rawQuery("select * from " + TBL_LP_SCHOOLS_MASTER + " where " + ARGS1 + "='" + ARGS2 + "' and " + ARGS3 + "='" + ARGS4 + "'", null);


            if (cr.moveToFirst()) {
                do {

                    map.put("school_id", "" + cr.getInt(cr.getColumnIndex("school_id")));
                    map.put("school_name", "" + cr.getString(cr.getColumnIndex("school_name")));
                    map.put("latitude", "" + cr.getString(cr.getColumnIndex("latitude")));
                    map.put("longitude", "" + cr.getString(cr.getColumnIndex("longitude")));
                    //map.put("start_school_time", "" + cr.getString(cr.getColumnIndex("start_school_time")));




                } while (cr.moveToNext());
            }

            if (cr != null && !cr.isClosed()) {
                cr.close();
            }

        } catch (Exception e) {
            Log.e("[" + TAG + "]: ", "TBL_LP_SCHOOLS_MASTER - getAllTableData(): ", e);
        }

        return map;
    }

    public ArrayList<Object> getCampParticularRow(Context context, String TBL_NAME, String ARGS1, String ARGS2,
                                                  String ARGS3, String ARGS4) {

        iContext = context;
        ArrayList<Object> aData = new ArrayList<Object>();

        Log.e("DBManager", "table name in else condition=> " + TBL_NAME);

        if (iDB == null) {

            iDbHelper = new DatabaseHelper(iContext);
            iDB = iDbHelper.getWritableDatabase();

            try {
                DBManager.getInstance().openDB();
            } catch (Exception e) {
                Log.e("["+TAG+"]:", "'" + TAG + "' - getAllTableData() :", e);
            }
        }

        if (TBL_NAME.equalsIgnoreCase(TBL_LP_CAMP_MASTER)) {
            try {
                Cursor cr = null;
                // cr = iDB.query(TBL_LP_USER_LOGIN, null, null, null, null, null, null);
                Log.e("query==>", "select * from " + TBL_LP_CAMP_MASTER + " where " + ARGS1 + "='" + ARGS2 + "'" + " AND " + ARGS3 + "='" + ARGS4 + "'");
                cr = iDB.rawQuery("select * from " + TBL_LP_CAMP_MASTER + " where " + ARGS1 + "='" + ARGS2 + "'" + " AND " + ARGS3 + "='" + ARGS4 + "'", null);

                if (cr.moveToFirst()) {
                    do {
                        CampMasterModel temp = new CampMasterModel();

                        temp.setCamp_id(cr.getInt(cr.getColumnIndex("camp_id")));
                        temp.setCamp_name(cr.getString(cr.getColumnIndex("camp_name")));
                        temp.setCamp_type(cr.getInt(cr.getColumnIndex("camp_type")));
                        temp.setSchool_id(cr.getInt(cr.getColumnIndex("school_id")));
                        temp.setVenue_id(cr.getInt(cr.getColumnIndex("venue_id")));
                        temp.setStart_date(cr.getString(cr.getColumnIndex("start_date")));
                        temp.setEnd_date(cr.getString(cr.getColumnIndex("end_date")));
                        temp.setCamp_coordination_id(cr.getInt(cr.getColumnIndex("camp_coordination_id")));
                        temp.setQuestionaire_id(cr.getInt(cr.getColumnIndex("questionaire_id")));
                        temp.setRegistration_coordinator_id(cr.getInt(cr.getColumnIndex("registration_coordinator_id")));
                        temp.setStatus(cr.getInt(cr.getColumnIndex("status")));
                        temp.setData_flag(cr.getInt(cr.getColumnIndex("data_flag")));

                        aData.add(temp);
                    } while (cr.moveToNext());
                }

                if (cr != null && !cr.isClosed()) {
                    cr.close();
                }

            } catch (Exception e) {
                Log.e("[" + TAG + "]: ", "TBL_LP_CAMP_MASTER - getAllTableData(): ", e);
            }
        } else if (TBL_NAME.equalsIgnoreCase(TBL_LP_STUDENT_MASTER)) {
            try {
                Cursor cr = null;
                cr = iDB.rawQuery("select * from " + TBL_LP_STUDENT_MASTER + " where " + ARGS1 + "='" + ARGS2 + "'", null);
                if (cr.moveToFirst()) {
                    do {
                        StudentMasterModel temp = new StudentMasterModel();

                        //temp.setStudent_liveplus_id(cr.getInt(cr.getColumnIndex("student_liveplus_id")));
                        //temp.setUser_login_id(cr.getInt(cr.getColumnIndex("user_login_id")));
                        temp.setStudent_name(cr.getString(cr.getColumnIndex("student_name")));
                        temp.setCurrent_school_id(cr.getString(cr.getColumnIndex("current_school_id")));
                        temp.setCurrent_roll_num(cr.getString(cr.getColumnIndex("current_roll_num")));
                        temp.setGender(cr.getInt(cr.getColumnIndex("gender")));
                        temp.setDob(cr.getString(cr.getColumnIndex("dob")));
                        temp.setEmail(cr.getString(cr.getColumnIndex("email")));
                        temp.setGaurdian_name(cr.getString(cr.getColumnIndex("gaurdian_name")));
                        temp.setPhone(cr.getString(cr.getColumnIndex("phone")));
                        temp.setAddress(cr.getString(cr.getColumnIndex("address")));
                        temp.setCreated_on(cr.getString(cr.getColumnIndex("created_on")));
                        temp.setCreated_by(cr.getString(cr.getColumnIndex("created_by")));
                        temp.setLast_modified_on(cr.getString(cr.getColumnIndex("last_modified_on")));
                        temp.setLast_modified_by(cr.getString(cr.getColumnIndex("last_modified_by")));
                        temp.setStudent_id(cr.getInt(cr.getColumnIndex("student_id")));
                        temp.setStudent_registration_num(cr.getString(cr.getColumnIndex("student_registration_num")));
                        temp.setCurrent_class(cr.getString(cr.getColumnIndex("current_class")));
                        temp.setClass_id(cr.getString(cr.getColumnIndex("class_id")));
                        temp.setClass_partition_id(cr.getInt(cr.getColumnIndex("class_partition_id")));
                        temp.setSection(cr.getString(cr.getColumnIndex("section")));
                        temp.setIs_active(cr.getInt(cr.getColumnIndex("is_active")));
                        temp.setCamp_id(cr.getString(cr.getColumnIndex("camp_id")));


                        aData.add(temp);

                    } while (cr.moveToNext());
                }

                if (cr != null && !cr.isClosed()) {
                    cr.close();
                }

            } catch (Exception e) {
                Log.e("[" + TAG + "]: ", "TBL_LP_SCHOOLS_MASTER - getAllTableData(): ", e);
            }
        }
        return aData;
    }


    public ArrayList<Object> getTestReport(Context context, String ARGS1, String ARGS2, String ARGS3, String ARGS4, String ARGS5) {
        iContext = context;
        ArrayList<Object> aData = new ArrayList<Object>();

        if (iDB == null) {

            // OPENING DATABASE CONN.
            try {
                DBManager.getInstance().openDB();
            } catch (Exception e) {
                Log.e("["+TAG+"]:", "'" + TAG + "' - getAllTableData() :", e);
            }
        }
        try {

            Cursor cr0 = iDB.rawQuery(TestReportQuery.GetReportQuery(iContext,ARGS1, ARGS2, ARGS3, ARGS4, ARGS5), null);
            Cursor cr1 = iDB.rawQuery("select * from " + TBL_LP_CAMP_TEST_MAPPING + " where " + "camp_id" + "='" + ARGS1 + "'" + " AND " + "school_id" + "='" + ARGS2 + "'", null);



            HashMap<String,ArrayList<String>>   campTestiDList = new HashMap<>();
            String class_idss;
            List<String> class_ids;
            ArrayList<String>class_list;
            if (cr1.moveToFirst()) {
                do {
                    class_list=new ArrayList<>();
                    class_idss= cr1.getString(4);
                    class_ids= Arrays.asList(class_idss.split("\\s*,\\s*"));
                    for(int i=0;i<class_ids.size();i++){
                        class_list.add("" + class_ids.get(i));
                    }
                    campTestiDList.put(cr1.getString(3),class_list);


                } while (cr1.moveToNext());
            }


            if (cr0.moveToFirst()) {
                do {
                    TestReportModel temp = new TestReportModel();
                       /* temp.setStudent_registration_num(cr0.getString(cr0.getColumnIndex("LSM.student_registration_num")));
                        temp.setStudent_name(cr0.getString(cr0.getColumnIndex("LSM.Student_Name")));
                        temp.setGender(cr0.getString(cr0.getColumnIndex("LSM.Gender")));
                        temp.setTest_name(cr0.getString(cr0.getColumnIndex("LTC.Test_Name")));
                        temp.setSubTestName(cr0.getString(cr0.getColumnIndex("SubTestName")));
                        temp.setTestCompleted(cr0.getString(cr0.getColumnIndex("TestCompleted")));
                        temp.setScore(cr0.getString(cr0.getColumnIndex("FTRA.Score")));
                        */

                    temp.setStudent_registration_num(cr0.getString(0));
                    temp.setStudent_name(cr0.getString(1));
                    temp.setStudent_id(cr0.getString(3));
                    temp.setGender(cr0.getString(4));
                    temp.setTest_name(cr0.getString(5));
                    temp.setSubTestName(cr0.getString(8));
                    temp.setTestCompleted(cr0.getString(6));
                    temp.setScore(cr0.getString(9));
                    temp.setTest_type_id(cr0.getString(7));
                    temp.setTotal(cr0.getString(10));
                    temp.setTests_applicable(cr0.getString(11));
                    temp.setClass_id(cr0.getString(12));

                    if (campTestiDList.containsKey(temp.getTest_type_id())&&campTestiDList.get(temp.getTest_type_id()).contains(temp.getClass_id())) {

                        // if (temp.getTests_applicable().equals("1") && !Constant.seniorList.contains(temp.getClass_id())) {
                        aData.add(temp);
//                        } else if (temp.getTests_applicable().equals("2") && Constant.seniorList.contains(temp.getClass_id())) {
//                            aData.add(temp);
//                        } else if (temp.getTests_applicable().equals("3")) {
//                            aData.add(temp);
//                        }
                    }
//                        String skill_score_id,student_id,test_type_id,score;
//
//                        skill_score_id = cr0.getString(0);
//                        student_id = cr0.getString(1);
//                        test_type_id = cr0.getString(2);
//                        score = cr0.getString(3);


                } while (cr0.moveToNext());
            }
            if (cr0 != null && !cr0.isClosed()) {
                cr0.close();
            }

            if (cr1 != null && !cr1.isClosed()) {
                cr1.close();
            }

//            if(TBL.equalsIgnoreCase(TBL_LP_STUDENT_MASTER)) {
//                Cursor cr1 = null;
//                if(ARGS4.equalsIgnoreCase("all")) {
//                    cr1 = iDB.rawQuery("select * from " + TBL_LP_STUDENT_MASTER + " where " + ARGS1 + "='" + ARGS2 + "' ORDER BY current_class ASC", null);
//                }
//                else{
//                    cr1 = iDB.rawQuery("select * from " + TBL_LP_STUDENT_MASTER + " where " + ARGS1 + "='" + ARGS2 + "'"+" AND "+ ARGS3 +"='" + ARGS4+ "' ORDER BY current_roll_num ASC" , null);
//                }
//                if (cr1.moveToFirst()) {
//                    do {
//                        StudentMasterModel temp = new StudentMasterModel();
//                        temp.setStudent_id( cr1.getInt(cr1.getColumnIndex("student_id")));
//                        temp.setStudent_name( cr1.getString(cr1.getColumnIndex("student_name")));
//                        temp.setCurrent_roll_num(cr1.getString(cr1.getColumnIndex("current_roll_num")));
//                        temp.setGender(cr1.getInt(cr1.getColumnIndex("gender")));
//                        temp.setStudent_registration_num(cr1.getString(cr1.getColumnIndex("student_registration_num")));
//
//                        aData.add(temp);
//                    } while (cr1.moveToNext());
//                }
//                if (cr1 != null && !cr1.isClosed()) {
//                    cr1.close();
//                }
//            }else{
//                Cursor cr2 = null;
//                if(ARGS4.equalsIgnoreCase("all") && ARGS6.equalsIgnoreCase("all")) {
//                    cr2 = iDB.rawQuery("select * from " + TBL_LP_FITNESS_TEST_RESULT + " where " + ARGS1 + "='" + ARGS2  + "' ORDER BY test_name ASC", null);
//                }
//                else if(ARGS6.equalsIgnoreCase("all")){
//                    cr2 = iDB.rawQuery("select * from " + TBL_LP_FITNESS_TEST_RESULT + " where " + ARGS1 + "='" + ARGS2 + "'"+" AND "+ ARGS3 +"='" + ARGS4 + "' ORDER BY test_name ASC", null);
//                }
//                else if(ARGS4.equalsIgnoreCase("all")){
//                    cr2 = iDB.rawQuery("select * from " + TBL_LP_FITNESS_TEST_RESULT + " where " + ARGS1 + "='" + ARGS2 + "'"+" AND "+ ARGS5 +"='" + ARGS6 + "' ORDER BY test_name ASC", null);
//                }
//                else {
//                    cr2 = iDB.rawQuery("select * from " + TBL_LP_FITNESS_TEST_RESULT + " where " + ARGS1 + "='" + ARGS2 + "'"+" AND "+ ARGS3 +"='" + ARGS4 +"'"+" AND "+ ARGS5 +"='" + ARGS6 + "' ORDER BY test_name ASC", null);
//                }
//                if (cr2.moveToFirst()) {
//                    do {
//                        FitnessTestResultModel temp = new FitnessTestResultModel();
//                        temp.setTestName(cr2.getString(cr2.getColumnIndex("test_name")));
//                        temp.setSubTestName(cr2.getString(cr2.getColumnIndex("sub_test_name")));
//                        temp.setTestedOrNot(Boolean.parseBoolean(cr2.getString(cr2.getColumnIndex("tested"))));
//
//                        aData.add(temp);
//                    } while (cr2.moveToNext());
//                }
//
//                if (cr2 != null && !cr2.isClosed()) {
//                    cr2.close();
//                }
            //  }


        } catch (Exception e) {
            Log.e("[" + TAG + "]: ", "TBL_LP_TEST_REPORT - getAllTableData(): ", e);
        }

        return aData;
    }

    public void UpdateColumn(Context context, String TBL_NAME, String columnName, String ARGS1, String ARGS2, String ARGS3, String ARGS4) {
        iContext = context;
        ArrayList<Object> aData = new ArrayList<Object>();

        if (iDB == null) {

            // OPENING DATABASE CONN.
            try {
                DBManager.getInstance().openDB();
            } catch (Exception e) {
                Log.e("["+TAG+"]:", "'" + TAG + "' - getAllTableData() :", e);
            }
        }
        try {

            Cursor cr = null;


            cr = iDB.rawQuery("update " + TBL_NAME + " set " + columnName + "='true'" + " where " +
                    ARGS1 + "='" + ARGS2 + "'" + " AND " + ARGS3 + "='" + ARGS4 + "'", null);

            String query = "update " + TBL_NAME + " set " + columnName + "='true'" + " where " +
                    ARGS1 + "='" + ARGS2 + "'" + " AND " + ARGS3 + "='" + ARGS4 + "'";

            Log.e(TAG, "update query==> " + query);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public HashMap<String, String> getParticularCheckListRow(Context context, String TBL_NAME, String ARGS1, String ARGS2,
                                                             String ARGS3, String ARGS4, String ARGS5, String ARGS6) {

        iContext = context;
        HashMap<String, String> map = new HashMap<String, String>();

        if (iDB == null) {

            iDbHelper = new DatabaseHelper(iContext);
            iDB = iDbHelper.getWritableDatabase();

            try {
                DBManager.getInstance().openDB();
            } catch (Exception e) {
                Log.e("["+TAG+"]:", "'" + TAG + "' - getAllTableData() :", e);
            }
        }

        if (TBL_NAME.equalsIgnoreCase(TBL_LP_STUDENT_MASTER)) {
            try {
                Cursor cr = null;
                Log.e("query==>", "select * from " + TBL_LP_STUDENT_MASTER + " where " + ARGS1 + "='" + ARGS2 + "'" + " AND " + ARGS3 + "='" + ARGS4 + "'");
                cr = iDB.rawQuery("select * from " + TBL_LP_STUDENT_MASTER + " where " + ARGS1 + "='" + ARGS2 + "'" + " AND " + ARGS3 + "='" + ARGS4 + "'", null);
                //cr =  iDB.rawQuery("select * from " + TBL_LP_STUDENT_MASTER + " where " + ARGS1 + "='" + ARGS2 + "'", null);

                if (cr.moveToFirst()) {
                    do {
                        //StudentMasterModel temp = new StudentMasterModel();

                        //map.put("student_liveplus_id", "" + cr.getInt(cr.getColumnIndex("student_liveplus_id")));
                        //map.put("user_login_id", "" + cr.getInt(cr.getColumnIndex("user_login_id")));
                        map.put("student_name", cr.getString(cr.getColumnIndex("student_name")));
                        map.put("current_school_id", cr.getString(cr.getColumnIndex("current_school_id")));
                        map.put("current_roll_num", cr.getString(cr.getColumnIndex("current_roll_num")));
                        map.put("gender", "" + cr.getInt(cr.getColumnIndex("gender")));
                        map.put("dob", cr.getString(cr.getColumnIndex("dob")));
                        map.put("email", cr.getString(cr.getColumnIndex("email")));
                        map.put("gaurdian_name", cr.getString(cr.getColumnIndex("gaurdian_name")));
                        map.put("phone", cr.getString(cr.getColumnIndex("phone")));
                        map.put("address", cr.getString(cr.getColumnIndex("address")));
                        map.put("created_on", cr.getString(cr.getColumnIndex("created_on")));
                        map.put("created_by", cr.getString(cr.getColumnIndex("created_by")));
                        map.put("last_modified_on", cr.getString(cr.getColumnIndex("last_modified_on")));
                        map.put("last_modified_by", cr.getString(cr.getColumnIndex("last_modified_by")));
                        map.put("student_id", "" + cr.getInt(cr.getColumnIndex("student_id")));
                        map.put("student_registration_num", cr.getString(cr.getColumnIndex("student_registration_num")));
                        map.put("current_class", cr.getString(cr.getColumnIndex("current_class")));
                        map.put("class_id", cr.getString(cr.getColumnIndex("class_id")));
                        map.put("class_partition_id", "" + cr.getInt(cr.getColumnIndex("class_partition_id")));
                        map.put("section", cr.getString(cr.getColumnIndex("section")));
                        map.put("is_active", "" + cr.getInt(cr.getColumnIndex("is_active")));
                        map.put("camp_id", "" + cr.getString(cr.getColumnIndex("camp_id")));


                    } while (cr.moveToNext());
                }

                if (cr != null && !cr.isClosed()) {
                    cr.close();
                }

            } catch (Exception e) {
                Log.e("[" + TAG + "]: ", "TBL_LP_USER_LOGIN - getAllTableData(): ", e);
            }
        } else if (TBL_NAME.equalsIgnoreCase(TBL_LP_FITNESS_SKILL_TEST_RESULT)) {
            try {
                Cursor cr = null;
                Log.e("query==>", "select * from " + TBL_LP_FITNESS_SKILL_TEST_RESULT + " where " + ARGS1 + "='" + ARGS2 + "'" + " AND " + ARGS3 + "='" + ARGS4 + "'");
                cr = iDB.rawQuery("select * from " + TBL_LP_FITNESS_SKILL_TEST_RESULT + " where " + ARGS1 + "='" + ARGS2 + "'" + " AND " + ARGS3 + "='" + ARGS4 + "'" + " AND " + ARGS5 + "='" + ARGS6 + "'", null);

                if (cr.moveToFirst()) {
                    do {


                        map.put("score", "" + cr.getInt(cr.getColumnIndex("score")));


                    } while (cr.moveToNext());
                }

                if (cr != null && !cr.isClosed()) {
                    cr.close();
                }

            } catch (Exception e) {
                Log.e("[" + TAG + "]: ", "TBL_LP_FITNESS_SKILL_TEST_RESULT - getAllTableData(): ", e);
            }
        }
        return map;
    }

    public void updateFitnessTestTable(String TABLE_NAME,int testTypeId, int studentId){
        if (iDB == null) {
            // OPENING DATABASE CONN.
            try {
                DBManager.getInstance().openDB();
            } catch (Exception e) {
                Log.e("["+TAG+"]:", "updateTables() :", e);
            }
        }
        String query = "update ";
        try {
            query += TABLE_NAME + " set "+ AppConfig.EXPORTED + "='true' where "+ AppConfig.TEST_TYPE_ID
                    +"="+testTypeId+" and "+AppConfig.STUDENT_ID+"="+studentId;
            iDB.execSQL(query);

        } catch (Exception e) {
            Log.e("DBManager ", "query==> " + query);
        }
        Log.e("check", "update query==> " + query);
    }

    public void insertBatchTestData(String TBL_NAME,ArrayList<Object> aData,Context context) {
        iContext = context;
        if (iDB == null) {
            // OPENING DATABASE CONN.
            //  Log.e(TAG, "***iDB== " + iDB);
            iDbHelper = new DatabaseHelper(iContext);
            iDB = iDbHelper.getWritableDatabase();
            try {
                DBManager.getInstance().openDB();
            } catch (Exception e) {
                Log.e("["+TAG+"]:", "insertTables() :", e);
            }
        }


        if (TBL_NAME.equalsIgnoreCase(TBL_LP_FITNESS_SKILL_TEST_RESULT)) {
            //iDB = iDbHelper.getWritableDatabase();
            iDB.beginTransaction();
            String sql_skill_test = "insert into " + TBL_NAME + " (skill_score_id, student_id, camp_id, test_type_id, checklist_item_id, coordinator_id, score, created_on, longitude, latitude, last_modified_on, tested, device_date, synced) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            SQLiteStatement stmt_skill_test     = iDB.compileStatement(sql_skill_test);
            for (int i = 0; i < aData.size(); i++) {
                FitnessSkillTestResultModel temp = (FitnessSkillTestResultModel) aData.get(i);
                int synced = temp.getSynced() ? 1 : 0;
//                deleteTransactionRow(iContext, TBL_LP_FITNESS_SKILL_TEST_RESULT, "student_id", "" + temp.getStudent_id(),
//                        "checklist_item_id", "" +temp.getChecklist_item_id(),"camp_id",Constant.CAMP_ID);
                //generate some values
                stmt_skill_test.bindLong(1, temp.getSkill_score_id());
                stmt_skill_test.bindLong(2, temp.getStudent_id());
                stmt_skill_test.bindLong(3, temp.getCamp_id());
                stmt_skill_test.bindLong(4, temp.getTest_type_id());
                stmt_skill_test.bindLong(5, temp.getChecklist_item_id());
                stmt_skill_test.bindLong(6, temp.getCoordinator_id());
                stmt_skill_test.bindString(7, temp.getScore());
                // stmt.bindDouble(8, temp.getCreated_on());
                stmt_skill_test.bindString(9, temp.getLongitude());
                stmt_skill_test.bindString(10, temp.getLatitude());
                stmt_skill_test.bindLong(11, temp.getLast_modified_on());
                stmt_skill_test.bindString(12, "" + temp.getTested());
                // stmt.bindString(13,temp.getDevice_date());
                stmt_skill_test.bindLong(14, synced);
                stmt_skill_test.executeInsert();
                stmt_skill_test.clearBindings();
            }
            iDB.setTransactionSuccessful();
            iDB.endTransaction();
        }else  if (TBL_NAME.equalsIgnoreCase(TBL_LP_FITNESS_TEST_RESULT)) {
            //iDB = iDbHelper.getWritableDatabase();
            iDB.beginTransaction();
            String sql_test = "insert into " + TBL_NAME + " (student_id, camp_id, test_type_id, test_coordinator_id, score, percentile, created_on, created_by, last_modified_on, last_modified_by, latitude, longitude, test_name, tested, sub_test_name, device_date, synced) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            SQLiteStatement    stmt_test = iDB.compileStatement(sql_test);
            for (int i = 0; i < aData.size(); i++) {
                FitnessTestResultModel temp = (FitnessTestResultModel) aData.get(i);
//              deleteTransactionRow(iContext, TBL_LP_FITNESS_TEST_RESULT, "student_id", "" + temp.getStudent_id(),
//                      "test_type_id", "" + temp.getTest_type_id(),"camp_id",Constant.CAMP_ID);
                //generate some values
                stmt_test.bindLong(1, temp.getStudent_id());
                stmt_test.bindLong(2, temp.getCamp_id());
                stmt_test.bindLong(3, temp.getTest_type_id());
                stmt_test.bindLong(4, temp.getTest_coordinator_id());
                stmt_test.bindLong(5, Integer.parseInt(temp.getScore()));
                stmt_test.bindString(6, "" + (temp.getPercentile()));
                // stmt.bindDouble(7, temp.getCreated_on());
                stmt_test.bindString(8, "" + temp.getCreated_by());
                stmt_test.bindLong(9, temp.getLast_modified_on());
                stmt_test.bindString(10, temp.getLast_modified_by());
                stmt_test.bindString(11, "" + temp.getLatitude());
                stmt_test.bindString(12, "" + temp.getLongitude());
                stmt_test.bindString(13, temp.getTestName());
                stmt_test.bindString(14, "" + temp.isTestedOrNot());
                stmt_test.bindString(15, temp.getSubTestName());
                //   stmt.bindLong(16, temp.getDevice_date());
                stmt_test.bindString(17, "" + temp.isSyncedOrNot());
                stmt_test.executeInsert();
                stmt_test.clearBindings();
            }
            iDB.setTransactionSuccessful();
            iDB.endTransaction();
        }

    }

    public ArrayList<Object> getFitnessTestResultData(Context context, String synced, String exported, int limit) {
        iContext = context;
        ArrayList<Object> aData = new ArrayList<Object>();

        if (iDB == null) {

            // OPENING DATABASE CONN.
            try {
                DBManager.getInstance().openDB();
            } catch (Exception e) {
                Log.e("["+TAG+"]:", "'" + TAG + "' - getAllTableData() :", e);
            }
        }

        if (!checkForTableExists(iDB, TBL_LP_FITNESS_TEST_RESULT))
            return aData;

        try {
            Cursor cr = null;

            //cr = iDB.query(TBL_LP_FITNESS_TEST_RESULT, null, null, null, null, null, null);
            String query = "";
            if (limit!=0)
                query = "select * from " + TBL_LP_FITNESS_TEST_RESULT + " where " + AppConfig.SYNCED + "='" + synced + "' limit 2";
            else if (!exported.equals(""))
                query = "select * from " + TBL_LP_FITNESS_TEST_RESULT + " where " + AppConfig.SYNCED + "='" + synced + "' OR "+AppConfig.EXPORTED + "='"+exported+"'";
            else
                query = "select * from " + TBL_LP_FITNESS_TEST_RESULT + " where " + AppConfig.SYNCED + "='" + synced + "'";

            cr = iDB.rawQuery(query,null);

            Log.e(AppConfig.TAG, "query "+query);
            if (cr.moveToFirst()) {
                do {
                    FitnessTestResultModel temp = new FitnessTestResultModel();

                    temp.setStudent_id(cr.getInt(cr.getColumnIndex("student_id")));
                    temp.setCamp_id(cr.getInt(cr.getColumnIndex("camp_id")));
                    temp.setTest_type_id(cr.getInt(cr.getColumnIndex("test_type_id")));
                    temp.setTest_coordinator_id(cr.getInt(cr.getColumnIndex("test_coordinator_id")));
                    temp.setScore("" + cr.getInt(cr.getColumnIndex("score")));
                    temp.setPercentile(Double.parseDouble(cr.getString(cr.getColumnIndex("percentile"))));
                    //          temp.setCreated_on(cr.getString(cr.getColumnIndex("created_on")));
                    //          temp.setDevice_date(cr.getString(cr.getColumnIndex("device_date")));
                    temp.setCreated_by(cr.getString(cr.getColumnIndex("created_by")));
                    //          temp.setLast_modified_on(cr.getString(cr.getColumnIndex("last_modified_on")));
                    temp.setLast_modified_by(cr.getString(cr.getColumnIndex("last_modified_by")));
                    temp.setLatitude(cr.getDouble(cr.getColumnIndex("latitude")));
                    temp.setLongitude(cr.getDouble(cr.getColumnIndex("longitude")));
                    temp.setTestName(cr.getString(cr.getColumnIndex("test_name")));
                    temp.setSubTestName(cr.getString(cr.getColumnIndex("sub_test_name")));
                    temp.setTestedOrNot(Boolean.parseBoolean(cr.getString(cr.getColumnIndex("tested"))));
                    temp.setSyncedOrNot(Boolean.parseBoolean(cr.getString(cr.getColumnIndex("synced"))));

                    String crreated_on_string = cr.getString(cr.getColumnIndex("created_on"));
                    DateFormat formatter1 = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");

                    Date date1 = (Date) formatter1.parse(crreated_on_string);
                    String created_on = Constant.ConvertDateToString(date1);
                    temp.setCreated_on(created_on);


                    long last_modified_string = cr.getLong(cr.getColumnIndex("last_modified_on"));
//                        DateFormat formatter2 = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
//
//                        Date date2 = (Date) formatter2.parse(last_modified_string);
//                        String last_modified = Constant.ConvertDateToString(date2);
                    temp.setLast_modified_on(Utility.getDate(last_modified_string,"dd MMM yyyy HH:mm:ss:SSS"));


                    try {
                        String devive_date_string = cr.getString(cr.getColumnIndex("device_date"));
                        DateFormat formatter3 = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
                        Date date3 = (Date) formatter3.parse(devive_date_string);
                        String device_date = Constant.ConvertDateToString(date3);
                        temp.setDevice_date(device_date);
                    } catch (Exception e) {

                    }

                    aData.add(temp);

                } while (cr.moveToNext());
            }

            if (cr != null && !cr.isClosed()) {
                cr.close();
            }

        } catch (Exception e) {
            Log.e(TAG, "TBL_LP_FITNESS_TEST_RESULT - getAllTableData(): ", e);
        }

        return aData;
    }

    public void truncateTable(String tableName){
        if (iDB == null) {
            // OPENING DATABASE CONN.
            Log.e(TAG,"***iDB== "+iDB);
            try {
                DBManager.getInstance().openDB();
            } catch (Exception e) {
                //Log.e("["+TAG+"]:", "insertTables() :", e);
            }
        }
        iDB.execSQL("delete from "+tableName);
    }


    public Integer getTableCount(String tableName, String ARGS1, String ARGS2){
        if (iDB == null) {
            // OPENING DATABASE CONN.
            Log.e(TAG,"***iDB== "+iDB);
            try {
                DBManager.getInstance().openDB();
            } catch (Exception e) {
                //Log.e("["+TAG+"]:", "insertTables() :", e);
            }
        }

        try {
            Cursor cr = null;
            Log.e(AppConfig.TAG, "select count(*) as count from " + tableName + " where " + ARGS1 + "='"+ARGS2+"+'");
            cr = iDB.rawQuery(  "select count(*) as count from " + tableName + " where " + ARGS1 + "='"+ARGS2+"'", null);
            cr.moveToFirst();
            return cr.getInt(cr.getColumnIndex("count"));


        } catch (Exception e) {
            Log.e("[" + TAG + "]: ", "TBL_LP_FITNESS_SKILL_TEST_RESULT - getAllTableData(): ", e);
        }
        return -1;
    }


//        public void deleteAllStuff(Context context,String TBL_NAME,ArrayList<Object> nameArray,String whereClause,String whereClause2,String whereClause3) {
//
//                iContext = context;
//               if (iDbHelper == null) {
//                    // OPENING DATABASE CONN.
//                    try {
//                        DBManager.getInstance().openDB();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//               }
//               if (TBL_NAME.equalsIgnoreCase(TBL_LP_FITNESS_TEST_RESULT)) {
//                   SQLiteDatabase sqLiteDatabaseTest=iDbHelper.getWritableDatabase();
//                    sqLiteDatabaseTest.beginTransaction();
//                String test_query="delete from "+TBL_NAME+" where "+whereClause+" = ? and "+whereClause2+" = ? and "+whereClause3+" = ?";
//                SQLiteStatement stmt_test = sqLiteDatabaseTest.compileStatement(test_query);
//                for (int i=0; i < nameArray.size(); i++) {
//                    stmt_test.clearBindings();
//                    FitnessTestResultModel fitnessTestResultModel=(FitnessTestResultModel)nameArray.get(i);
//                    stmt_test.bindString(1,""+fitnessTestResultModel.getStudent_id() );
//                    stmt_test.bindString(2, ""+fitnessTestResultModel.getTest_type_id());
//                    stmt_test.bindString(3,""+fitnessTestResultModel.getCamp_id() );
//                    stmt_test.execute();
//                    }
//                sqLiteDatabaseTest.setTransactionSuccessful();
//                sqLiteDatabaseTest.endTransaction();
//
//            }else   if (TBL_NAME.equalsIgnoreCase(TBL_LP_FITNESS_SKILL_TEST_RESULT)) {
//                   SQLiteDatabase sqLiteDatabaseSkillTest=iDbHelper.getWritableDatabase();
//                   sqLiteDatabaseSkillTest.beginTransaction();
//                   String skill_query="delete from "+TBL_NAME+" where "+whereClause+" = ? and "+whereClause2+" = ? and "+whereClause3+" = ?";
//                   SQLiteStatement stmt_skill_test = sqLiteDatabaseSkillTest.compileStatement(skill_query);
//                for (int i=0; i < nameArray.size(); i++) {
//                    stmt_skill_test.clearBindings();
//                    FitnessSkillTestResultModel fitnessSkillTestResultModel=(FitnessSkillTestResultModel)nameArray.get(i);
//                    stmt_skill_test.bindString(1,""+fitnessSkillTestResultModel.getStudent_id() );
//                    stmt_skill_test.bindString(2, ""+fitnessSkillTestResultModel.getChecklist_item_id());
//                    stmt_skill_test.bindString(3,""+fitnessSkillTestResultModel.getCamp_id() );
//                    stmt_skill_test.execute();
//                    }
//                sqLiteDatabaseSkillTest.setTransactionSuccessful();
//                sqLiteDatabaseSkillTest.endTransaction();
//            }
//
//
//    }

}
