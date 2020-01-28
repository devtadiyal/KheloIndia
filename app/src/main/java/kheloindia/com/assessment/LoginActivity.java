package kheloindia.com.assessment;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import kheloindia.com.assessment.model.SchoolsMasterModel;
import kheloindia.com.assessment.model.UserModel;
import kheloindia.com.assessment.service.LocationService;
import kheloindia.com.assessment.util.AppConfig;
import kheloindia.com.assessment.util.ConnectionDetector;
import kheloindia.com.assessment.util.DBManager;
import kheloindia.com.assessment.util.ResponseListener;

import kheloindia.com.assessment.functions.Constant;
import kheloindia.com.assessment.functions.GPSTracker1;
import kheloindia.com.assessment.util.Utility;
import kheloindia.com.assessment.webservice.LoginRequest;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener, ResponseListener {
    private static final int MY_PERMISSIONS_REQUEST_CODE = 123;

    private Button login_btn, sign_up_btn;
    private TextView forgot_password_tv, user_name_tv, password_tv, fit_ass_tv, toolbarTitle;
    private EditText user_name_edt, password_edt;
    private ImageView backBtn;
    private SharedPreferences sp;
    private DBManager db;
    //  private CheckBox remember_cbx;
    private SharedPreferences.Editor e;
    String username_txt;
    String password_txt;
    private ConnectionDetector connectionDetector;
    private static Context ctx;

    String imei_string = "";

    boolean isCredentialSavedInLocalDB = false;
    String username = "";
    String password = "";
    private boolean isStudentDetailGet = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        ctx = LoginActivity.this;

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        checkPermission();


        init();

    }

    @Override
    protected void onResume() {
        db = DBManager.getInstance();

        super.onResume();
    }


    private void init() {

        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Constant.COORDINATOR_ID = "2";

        username = sp.getString(AppConfig.TEST_COORDINATOR_NAME, "");
        password = sp.getString(AppConfig.TEST_COORDINATOR_PASSWORD, "");

        if (username.length() > 0 && password.length() > 0) {
            isCredentialSavedInLocalDB = true;
        }

        login_btn = (Button) findViewById(R.id.login_btn);
        forgot_password_tv = (TextView) findViewById(R.id.forgot_password_tv);
        user_name_edt = (EditText) findViewById(R.id.user_name_edt);
        password_edt = (EditText) findViewById(R.id.password_edt);
        user_name_tv = (TextView) findViewById(R.id.user_name_tv);
        password_tv = (TextView) findViewById(R.id.password_tv);
        // remember_cbx = (CheckBox) findViewById(R.id.remember_cbx);
        sign_up_btn = (Button) findViewById(R.id.sign_up_btn);
        fit_ass_tv = (TextView) findViewById(R.id.fit_ass_tv);
        backBtn = (ImageView) findViewById(R.id.back_btn);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);

        Typeface font_light = Typeface.createFromAsset(getAssets(),
                "fonts/Barlow-Light.ttf");
        Typeface font_medium = Typeface.createFromAsset(getAssets(),
                "fonts/Barlow-Medium.ttf");
        Typeface font_semi_bold = Typeface.createFromAsset(getAssets(),
                "fonts/Barlow-SemiBold.ttf");
        Typeface font_semi_extra_bold_italic = Typeface.createFromAsset(getAssets(),
                "fonts/BarlowCondensed-ExtraBoldItalic.otf");

        forgot_password_tv.setTypeface(font_light);
        user_name_edt.setTypeface(font_medium);
        password_edt.setTypeface(font_medium);
        login_btn.setTypeface(font_semi_bold);
        sign_up_btn.setTypeface(font_semi_bold);
        user_name_tv.setTypeface(font_medium);
        password_tv.setTypeface(font_medium);
        fit_ass_tv.setTypeface(font_semi_extra_bold_italic);

        forgot_password_tv.setOnClickListener(this);
        login_btn.setOnClickListener(this);
        // remember_cbx.setOnClickListener(this);
        sign_up_btn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        toolbarTitle.setText(getString(R.string.fitness_assessment));

        e = sp.edit();


        isStudentDetailGet = sp.getBoolean(AppConfig.IS_STUDENT_DETAILS_GET, false);

        password_edt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (password_edt.getRight() - password_edt.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        if (password_edt.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                            password_edt.setInputType(InputType.TYPE_CLASS_TEXT |
                                    InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            password_edt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.pwd_show_i, 0);

                        } else {
                            password_edt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            password_edt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.pwd_hide_i, 0);
                        }
                        //   password_edt.setSelection(password_edt.getText().length());
                        return true;
                    }
                }
                return false;
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn:
                getDataAndValidate();

                break;

            case R.id.forgot_password_tv:

                startActivity(new Intent(this, ForgotPasswordActivity.class));
                break;

            case R.id.back_btn:
                super.onBackPressed();
                break;

            case R.id.sign_up_btn:

                Intent in = new Intent(LoginActivity.this, CreateProfileActivity.class);
                startActivity(in);

                break;
        }
    }

    public void getDataAndValidate() {

        username_txt = user_name_edt.getText().toString().trim();
        password_txt = password_edt.getText().toString().trim();

        if (username_txt.length() < 1) {
            user_name_edt.requestFocus();
            user_name_edt.setError(getResources().getString(R.string.username));
        } else if (password_txt.length() < 1) {
            password_edt.requestFocus();
            password_edt.setError(getResources().getString(R.string.password));
        } else {
            connectionDetector = new ConnectionDetector(this);
            if (connectionDetector.isConnectingToInternet()) {

                String id = sp.getString("test_coordinator_id", "");
                Constant.TEST_COORDINATOR_ID = id;
                ArrayList<Object> objectArrayList = DBManager.getInstance().getAllTableData(this, DBManager.TBL_LP_SCHOOLS_MASTER, "trainer_coordinator_id", Constant.TEST_COORDINATOR_ID, "", "");


                String max_date = "";

                try {
                    if (objectArrayList.size() > 0) {
                        max_date = DBManager.getInstance().getMaxDate(this, DBManager.TBL_LP_SCHOOLS_MASTER, "last_modified_on", Constant.CAMP_ID);

                    } else {
                        max_date = "";

                    }
                } catch (Exception e) {
                    max_date = "";
                    e.printStackTrace();
                }

                Log.e("LoginActivity", "school max date=> " + max_date);

                GPSTracker1 gps;
                gps = new GPSTracker1(getApplicationContext());

                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    imei_string = "";
                } else {
                    imei_string = telephonyManager.getDeviceId();
                }


                LoginRequest loginRequest = new LoginRequest(this, username_txt, password_txt, max_date, imei_string, "" + gps.getLatitude(), "" + gps.getLongitude(), this);
                loginRequest.hitUserRequest();

               /* if(user_type.equalsIgnoreCase("4")){
                UserRequest userRequest = new UserRequest(this, username_txt, password_txt,max_date,user_type, this);
                userRequest.hitUserRequest();
                }

                else {
                    StudentUserRequest userRequest = new StudentUserRequest(this, username_txt, password_txt,max_date,user_type, this);
                    userRequest.hitUserRequest();
                }*/
            } else {
                if (username_txt.equalsIgnoreCase(sp.getString("test_coordinator_name", "")) &&
                        password_txt.equals(sp.getString("test_coordinator_password", ""))) {
                    String user_type = sp.getString("user_type", "");
                    Constant.USER_TYPE = user_type;
                    //TrackTrainer();
                    if (user_type.equalsIgnoreCase("4")) {
                        if (isStudentDetailGet) {
                            Intent in = new Intent(this, TakeTestActivity.class);
                            startActivity(in);
                            finish();

                        } else {
                            Intent in = new Intent(this, SchoolActivity.class);
                            startActivity(in);
                            finish();
                        }
                    } else if (user_type.equalsIgnoreCase("5") || user_type.equalsIgnoreCase("3") || user_type.equalsIgnoreCase("1")) {
                        Intent in = new Intent(this, DashBoardStudentActivity.class);
                        startActivity(in);
                        finish();
                    } else {
                        Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void TrackTrainer() {
        long interval = 1000 * 60 * 15; // 15 minutes in milliseconds

        // hour = 36000 * 1000;

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 10);
        Intent intent = new Intent(LoginActivity.this, LocationService.class);
        PendingIntent pintent = PendingIntent.getService(LoginActivity.this, 0, intent,
                0);
     /*   PendingIntent pintent = PendingIntent.getBroadcast(DashBoardActivity.this, 0, intent,
                0);*/
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
       /* alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                interval, pintent);*/
        alarm.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(),
                interval, pintent);
    }


    public void onBackPressed() {
        // finish();
        super.onBackPressed();
        //moveTaskToBack(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return Utility.hideKeyboard(this);
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        if (requestCode == 0) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
//                    && grantResults[1] == PackageManager.PERMISSION_GRANTED&& grantResults[2] == PackageManager.PERMISSION_GRANTED
//                    &&grantResults[3] == PackageManager.PERMISSION_GRANTED &&grantResults[4] == PackageManager.PERMISSION_GRANTED ) {
//                }else
//                Utility.showPermissionDialog(this,getString(R.string.accept_permission,"All"));
//
//        }
//    }

    @Override
    public void onResponse(Object obj) {

        //  db.dropTables(this);
        //  db.dropNamedTable(this, db.TBL_LP_SCHOOLS_MASTER);
        try {
            db.createNamedTable(this, db.TBL_LP_SCHOOLS_MASTER);
            db.createNamedTable(this, db.TBL_LP_FITNESS_TEST_CATEGORY);
            db.createNamedTable(this, db.TBL_LP_FITNESS_TEST_TYPES);
            db.createNamedTable(this, db.TBL_LP_SKILL_TEST_TYPE);
            //db.createNamedTable(this, db.TBL_LP_USER_SCHOOL_MAPPING);
            db.createNamedTable(this, db.TBL_LP_FITNESS_SKILL_TEST_RESULT);
            db.createNamedTable(this, db.TBL_LP_FITNESS_TEST_RESULT);
            db.createNamedTable(this, db.TBL_LP_CAMP_TEST_MAPPING);
            db.createNamedTable(this, db.TBL_LP_TEST_COORDINATOR_MAPPING);
            db.createNamedTable(this, db.TBL_LP_STUDENT_USER_MASTER);
            db.createNamedTable(this, DBManager.TBL_LP_MY_DART_CLASS_TABLE);
            db.createNamedTable(this, DBManager.TBL_LP_MY_DART_PERIOD_TABLE);
            db.createNamedTable(this, DBManager.TBL_LP_MY_DART_SPORT_TABLE);
            db.createNamedTable(this, DBManager.TBL_LP_MY_DART_OTHER_TABLE);
            db.createNamedTable(this, DBManager.TBL_LP_MY_DART_LESSON_PLAN_TABLE);
            db.createNamedTable(this, DBManager.TBL_LP_MY_DART_SPORT_SKILL_TABLE);
            db.createNamedTable(this, DBManager.TBL_LP_MY_DART_STUDENT_TABLE);
            db.createNamedTable(this, DBManager.TBL_LP_MY_DART_INSERT_ACTIVITY_TABLE);
            db.createNamedTable(this, DBManager.TBL_LP_SCREEN_MASTER);
            db.createNamedTable(this, DBManager.TBL_LP_USER_SCREEN_MAP);
            db.createNamedTable(this, DBManager.TBL_LP_MORNING_TRACKING);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (obj instanceof UserModel) {

            UserModel userModel = (UserModel) obj;

            if (userModel.getMessage().equalsIgnoreCase("success")) {

                //TrackTrainer();


                e.putBoolean(AppConfig.IS_LOGIN, true);
                e.putString("test_coordinator_name", username_txt);
                e.putString("test_coordinator_password", password_txt);
                e.commit();

                List<UserModel.Result.School> schools = userModel.getResult().getSchools();

                UserModel.Result userModelResult = userModel.getResult();


                Constant.TEST_COORDINATOR_ID = "" + userModelResult.getUserId();
                String gender = userModelResult.getGender();
                String address = userModel.getResult().getAddress();
                String phone = userModelResult.getPhone();
                String username = userModelResult.getUserName();
                String image = userModelResult.getTestCoordinatorImage();
                String email = userModelResult.getEmail();
                String Qualification = userModelResult.getQualification();
                String Whatsup_No = userModelResult.getWhatsupNo();
                String Tshirt_Size = userModelResult.getTshirtSize();
                String Trouser_Size = userModelResult.getTrouserSize();
                String Aadhar_No = userModelResult.getAadharNo();
                String Alternative_Email = userModelResult.getAlternativeEmail();
                String ImgPath = userModelResult.getImgPath();


                e.putString("user_type", "" + userModelResult.getUserTypeId());


                Constant.USER_TYPE = "" + userModelResult.getUserTypeId();
                e.commit();

                e.putString("test_coordinator_id", Constant.TEST_COORDINATOR_ID);
                e.putString("gender", gender);
                e.putString(AppConfig.ADDRESS, address);
                e.putString("phone", phone);
                e.putString("username", username);
                e.putString("email", email);
                e.putString("image", image);
                e.putString("Qualification", Qualification);
                e.putString("Whatsup_No", Whatsup_No);
                e.putString("Tshirt_Size", Tshirt_Size);
                e.putString("Trouser_Size", Trouser_Size);
                e.putString("Aadhar_No", Aadhar_No);
                e.putString("Alternative_Email", Alternative_Email);
                e.putString("profile_pic_url", ImgPath);
                e.putInt(AppConfig.STATE_ID, userModelResult.getStateId());
                e.putString(AppConfig.DISTRICT_NAME, userModelResult.getDistrictName());
                e.putString(AppConfig.CITY_NAME, userModelResult.getCityName());
                e.putString(AppConfig.STATE_NAME, userModelResult.getStateName());
                e.putString(AppConfig.BLOCK_NAME, userModelResult.getBlockName());
                e.putString("END", "1");
                e.commit();

                String user_type = "" + userModel.getResult().getUserTypeId();

                if (user_type.equalsIgnoreCase("4")) {

                    SchoolsMasterModel model = new SchoolsMasterModel();

                    //UserSchoolMappingModel mapping_model = new UserSchoolMappingModel();

                    for (int i = 0; i < schools.size(); i++) {

                        UserModel.Result.School schoolsBean = schools.get(i);
                        model.setSchool_name(schoolsBean.getSchoolname().replaceAll("'", "'' "));
                        model.setSchool_id(schoolsBean.getSchoolId());
                        model.setSchool_image_name(schoolsBean.getSchoolImageName());
                        model.setSchool_image_path(schoolsBean.getSchoolImagePath());
                        model.setTrainer_coordinator_id(Constant.TEST_COORDINATOR_ID);
                        model.setLast_modified_on(schoolsBean.getLastModifiedOn());
                        model.setCreated_on(schoolsBean.getCreatedOn());
                        model.setSeniors_starting_from(schoolsBean.getSeniorsStartingFrom());
                        model.setLatitude(schoolsBean.getLatitude());
                        model.setLongitude(schoolsBean.getLongitude());
                        model.setSchool_start_time(schoolsBean.getSchoolStartTime());
                        model.setOffice(schoolsBean.getOffice());
                        model.setIsAttached(schoolsBean.getIsAttached());
                        model.setIsRetestAllowed(schoolsBean.getIsRetestAllowed());
                        model.setForRetest(0);

                        HashMap<String, String> map = null;
                        try {
                            map = db.getParticularRow(getApplicationContext(), DBManager.TBL_LP_SCHOOLS_MASTER,
                                    "school_id", "" + schoolsBean.getSchoolId(), "", "");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (map.size() == 0) {

                            Log.e("LoginActivty", "coordinator id=> " + Constant.TEST_COORDINATOR_ID);
                            Log.e("LoginActivty", "school id=> " + schoolsBean.getSchoolId());
                            Log.e("LoginActivty", "school name=> " + schoolsBean.getSchoolname());

                            db.insertTables(db.TBL_LP_SCHOOLS_MASTER, model);
                        } else {
                            db.deleteSchoolOrStudentUserRow(LoginActivity.this, db.TBL_LP_SCHOOLS_MASTER, "school_id", "" + schoolsBean.getSchoolId(),
                                    "", "");
                            db.insertTables(db.TBL_LP_SCHOOLS_MASTER, model);
                        }

                        if (model.getIsRetestAllowed().equalsIgnoreCase("True")) {
                            model.setIsRetestAllowed("True");
                            model.setForRetest(1);
                            db.insertTables(db.TBL_LP_SCHOOLS_MASTER, model);
                        }

                        if (i == 0) {
                            e.putString("school_name", schoolsBean.getSchoolname().replaceAll("'", "'' "));
                            e.putString("school_id", "" + schoolsBean.getSchoolId());
                            e.putString("seniors_starting_from", "" + schoolsBean.getSeniorsStartingFrom());
                            e.commit();


                            Constant.SCHOOL_NAME = sp.getString("school_name", "");
                            Constant.SCHOOL_ID = sp.getString("school_id", "");
                            Constant.SENIORS_STARTING_FROM = sp.getString("seniors_starting_from", "");
                            Constant.TEST_COORDINATOR_ID = sp.getString("test_coordinator_id", "");

                        }

                        // adding entries in mapping table
//                        mapping_model.setSchool_id(schoolsBean.getSchoolId());
//                        mapping_model.setUser_id(Integer.parseInt(Constant.TEST_COORDINATOR_ID));
//                        mapping_model.setModified(schoolsBean.getLastModifiedOn());
//                        mapping_model.setCreated(schoolsBean.getCreatedOn());
//
//                        HashMap<String, String> mapping_map = null;
//                        try {
//                            map = db.getParticularRow(getApplicationContext(), DBManager.TBL_LP_USER_SCHOOL_MAPPING,
//                                    "school_id", "" + schoolsBean.getSchoolId(), "user_id", Constant.TEST_COORDINATOR_ID);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                        if (map.size() == 0) {
//                            db.insertTables(db.TBL_LP_USER_SCHOOL_MAPPING, mapping_model);
//                        } else {
//                            db.deleteSchoolOrStudentUserRow(LoginActivity.this, db.TBL_LP_USER_SCHOOL_MAPPING, "school_id", "" + schoolsBean.getSchoolId(),
//                                    "", "");
//                            db.insertTables(db.TBL_LP_USER_SCHOOL_MAPPING, model);
//                        }


                    }

                    if (isStudentDetailGet) {
                        Intent in = new Intent(this, TakeTestActivity.class);
                        startActivity(in);
                        finish();

                    } else {
                        Intent in = new Intent(this, SchoolActivity.class);
                        startActivity(in);
                        finish();
                    }


                } else if (user_type.equalsIgnoreCase("5") || user_type.equalsIgnoreCase("3") || user_type.equalsIgnoreCase("1")) {

                    if (userModel.getMessage().equalsIgnoreCase("success")) {

                        Intent in = new Intent(this, DashBoardStudentActivity.class);
                        startActivity(in);
                        finish();

//                        e.putBoolean("IS_LOGIN", true);
//                        e.putString("test_coordinator_name", username_txt);
//                        e.putString("test_coordinator_password", password_txt);
//                        //    e.putString("user_type", user_type);
//                        e.commit();
//
//                        List<UserModel.Result.Student> students = userModel.getResult().getStudents();
//                        StudentUserMasterModel model = new StudentUserMasterModel();
//                        for (int i = 0; i < students.size(); i++) {
//                            UserModel.Result.StudentsBean studentsBean = students.get(i);
//                            model.setGuardian_name(studentsBean.getGuardian_name());
//                            model.setImage_path(studentsBean.getImage_Path());
//                            model.setPhone(studentsBean.getPhone());
//                            model.setStudent_name(studentsBean.getStudent_name());
//                            model.setUser_login_id(studentsBean.getUser_Login_ID());
//                            model.setUser_login_name(studentsBean.getUser_Login_Name());
//                            model.setAddress(studentsBean.getAddress());
//                            model.setCurrent_class(studentsBean.getCurrent_class());
//                            model.setDate_of_birth(studentsBean.getDate_Of_Birth());
//                            model.setGender(studentsBean.getGender());
//
//
//                            HashMap<String, String> map = null;
//                            try {
//                                map = db.getParticularRow(getApplicationContext(), DBManager.TBL_LP_STUDENT_USER_MASTER,
//                                        "user_login_id", "" + studentsBean.getUser_Login_ID(), "", "");
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//
//                            if (map.size() == 0) {
//
//                                db.insertTables(db.TBL_LP_STUDENT_USER_MASTER, model);
//                            } else {
//                                db.deleteSchoolOrStudentUserRow(LoginActivity.this, db.TBL_LP_STUDENT_USER_MASTER, "user_login_id", "" + studentsBean.getUser_Login_ID(),
//                                        "", "");
//                                db.insertTables(db.TBL_LP_STUDENT_USER_MASTER, model);
//                            }
//                            if (i == 0) {
//                                e.putString("user_login_id", "" + studentsBean.getUser_Login_ID());
//                                e.putString("student_name", "" + studentsBean.getStudent_name());
//                                e.commit();
//                                Constant.TEST_COORDINATOR_ID = sp.getString("user_login_id", "");
//                            }
//
//                        }
//
//                        Constant.END_URL = "?UserName=" + sp.getString("test_coordinator_name", "") + "&Password=" + sp.getString("test_coordinator_password", "");
//                        Constant.USER_URl = "http://23.253.243.116:92/MyHomePage.aspx" + Constant.END_URL;
//                        Log.e("Splash", "URL===> " + Constant.USER_URl);
//                       /* Intent in = new Intent(this, HMActivity.class);
//                        startActivity(in);
//                        finish();*/
//
//                        Intent in = new Intent(this, DashBoardStudentActivity.class);
//                        startActivity(in);
//                        finish();
                    }


                } else if (user_type.equalsIgnoreCase("3")) {
                    Constant.END_URL = "?UserName=" + sp.getString("test_coordinator_name", "") + "&Password=" + sp.getString("test_coordinator_password", "");
                    Constant.USER_URl = "http://23.253.243.116:92/Fitness365SchoolDashboard.aspx" + Constant.END_URL;
                    Log.e("Splash", "URL===> " + Constant.USER_URl);
                    Intent in = new Intent(this, HMActivity.class);
                    startActivity(in);
                    finish();


                } else if (user_type.equalsIgnoreCase("1")) {
                    Constant.USER_URl = "http://mydiary.fitness365.me/Admin.aspx";
                    Intent in = new Intent(this, HMActivity.class);
                    startActivity(in);
                    finish();


                } else {
                    Toast.makeText(this, userModel.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, userModel.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }


       /* else if (obj instanceof StudentUserModel) {
            StudentUserModel UserModel = (StudentUserModel) obj;

            if (userModel.getMessage().equalsIgnoreCase("success")) {

                e.putBoolean("IS_LOGIN", true);
                e.putString("test_coordinator_name", username_txt);
                e.putString("test_coordinator_password", password_txt);
                //    e.putString("user_type", user_type);
                e.commit();

                List<StudentUserModel.ResultBean.StudentsBean> students = userModel.getResult().getStudents();
                StudentUserMasterModel model = new StudentUserMasterModel();
                for (int i = 0; i < students.size(); i++) {
                    StudentUserModel.ResultBean.StudentsBean studentsBean = students.get(i);
                    model.setGuardian_name(studentsBean.getGuardianName());
                    model.setImage_path(studentsBean.getImagePath());
                    model.setPhone(studentsBean.getPhone());
                    model.setStudent_name(studentsBean.getStudentName());
                    model.setUser_login_id(studentsBean.getUserLoginID());
                    model.setUser_login_name(studentsBean.getUserLoginName());
                    model.setAddress(studentsBean.getAddress());
                    model.setCurrent_class(studentsBean.getCurrentClass());
                    model.setDate_of_birth(studentsBean.getDateOfBirth());
                    model.setGender(studentsBean.getGender());


                    HashMap<String, String> map = null;
                    try {
                        map = db.getParticularRow(getApplicationContext(), DBManager.TBL_LP_STUDENT_USER_MASTER,
                                "user_login_id", "" + studentsBean.getUserLoginID(), "", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (map.size() == 0) {

                        db.insertTables(db.TBL_LP_STUDENT_USER_MASTER, model);
                    } else {
                        db.deleteSchoolOrStudentUserRow(LoginActivity.this, db.TBL_LP_STUDENT_USER_MASTER, "user_login_id", "" + studentsBean.getUserLoginID(),
                                "", "");
                        db.insertTables(db.TBL_LP_STUDENT_USER_MASTER, model);
                    }
                    if (i == 0) {
                        e.putString("user_login_id", "" + studentsBean.getUserLoginID());
                        e.putString("student_name", "" + studentsBean.getStudentName());
                        e.commit();
                        Constant.TEST_COORDINATOR_ID = sp.getString("user_login_id", "");
                    }

                }

                Intent in = new Intent(this, DashBoardStudentActivity.class);
                startActivity(in);
                finish();


            } else {
                Toast.makeText(this, userModel.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }*/


        else {
            Toast.makeText(this, R.string.server_down, Toast.LENGTH_SHORT).show();
        }

    }

    /* @TargetApi(Build.VERSION_CODES.M)
     protected void generateKey() {
         try {
             keyStore = KeyStore.getInstance("AndroidKeyStore");
         } catch (Exception e) {
             e.printStackTrace();
         }

         KeyGenerator keyGenerator;
         try {
             keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
         } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
             throw new RuntimeException("Failed to get KeyGenerator instance", e);
         }

         try {
             keyStore.load(null);
             keyGenerator.init(new
                     KeyGenParameterSpec.Builder(KEY_NAME,
                     KeyProperties.PURPOSE_ENCRYPT |
                             KeyProperties.PURPOSE_DECRYPT)
                     .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                     .setUserAuthenticationRequired(true)
                     .setEncryptionPaddings(
                             KeyProperties.ENCRYPTION_PADDING_PKCS7)
                     .build());
             keyGenerator.generateKey();
         } catch (NoSuchAlgorithmException |
                 InvalidAlgorithmParameterException
                 | CertificateException | IOException e) {
             throw new RuntimeException(e);
         }
     }

     @TargetApi(Build.VERSION_CODES.M)
     public boolean cipherInit() {
         try {
             cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
         } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
             throw new RuntimeException("Failed to get Cipher", e);
         }

         try {
             keyStore.load(null);
             SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                     null);
             cipher.init(Cipher.ENCRYPT_MODE, key);
             return true;
         } catch (KeyPermanentlyInvalidatedException e) {
             return false;
         } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
             throw new RuntimeException("Failed to init Cipher", e);
         }
     }*/
    protected void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                + ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_CONTACTS)
                + ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Do something, when permissions not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this, Manifest.permission.CAMERA)
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    this, Manifest.permission.READ_CONTACTS)
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // If we should give explanation of requested permissions

                // Show an alert dialog here with request explanation
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Camera, Read Contacts and Write External" +
                        " Storage permissions are required to do the task.");
                builder.setTitle("Please grant those permissions");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(
                                LoginActivity.this,
                                new String[]{
                                        Manifest.permission.READ_PHONE_STATE,
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                },
                                MY_PERMISSIONS_REQUEST_CODE
                        );
                    }
                });
                builder.setNeutralButton("Cancel", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                // Directly request for required permissions, without explanation
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{
                                Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_CODE
                );
            }
        } else {
            // Do something, when permissions are already granted
            //  Toast.makeText(this,"Permissions already granted",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CODE: {
                // When request is cancelled, the results array are empty
                if (
                        (grantResults.length > 0) &&
                                (grantResults[0]
                                        + grantResults[1]
                                        == PackageManager.PERMISSION_GRANTED
                                )
                ) {
                    // Permissions are granted
                 //   Toast.makeText(this, "Permissions granted.", Toast.LENGTH_SHORT).show();
                } else {
                    // Permissions are denied
                    Toast.makeText(this, "Permissions denied.", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    public static void CallMethodInLoginClass() {

        Intent in = new Intent(ctx, SchoolActivity.class);
        ctx.startActivity(in);

    }
}