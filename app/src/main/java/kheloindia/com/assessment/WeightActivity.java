package kheloindia.com.assessment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import kheloindia.com.assessment.model.TransactionModel;
import kheloindia.com.assessment.util.AppConfig;
import kheloindia.com.assessment.util.ConnectionDetector;
import kheloindia.com.assessment.util.DBManager;
import kheloindia.com.assessment.util.ResponseListener;
import kheloindia.com.assessment.util.TestActivityFunctions;
import kheloindia.com.assessment.util.Utility;

import kheloindia.com.assessment.functions.Constant;
import kheloindia.com.assessment.functions.GPSTracker;
import kheloindia.com.assessment.model.FitnessTestResultModel;
import kheloindia.com.assessment.webservice.TransactionRequest;

/**
 * Created by PC10 on 05/15/2017.
 */

public class WeightActivity extends AppCompatActivity implements View.OnClickListener, ResponseListener {

    ImageView dashboard_img;
    FloatingActionButton test_another_img;
    Toolbar toolbar;
    Button reset_btn, save_btn;
    EditText height_et;
    EditText weight_et;

    Button scan_btn;
    JSONArray jArray;
    JSONObject jObj;
    String created_on = "", device_date = "";

    //TextView school_tv;

    boolean yesOverride = false;

    ImageView boy_or_girl_img;
    TextView student_name_tv, student_class_tv;

    TextView test_name_tv, sub_test_name_tv, text_tv;

    String current_height = "";

    GPSTracker gps;
    double lat, lng;

    DBManager db;
    FitnessTestResultModel model_weight;
    FitnessTestResultModel model_height;

    TextView score_tv, age_gender_tv;

    String previous_score = "";


    //private SimpleLocation location;

    ConnectionDetector connectionDetector;

    String TAG = "WeightActivity";

    String local_sub_test_id = "";
    String local_student_id = "";
    String forReTest, testTableName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_weight);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        init();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        gps = new GPSTracker(getApplicationContext());
        gps.getmGoogleApiClient().connect();
        sub_test_name_tv.setText(Constant.SUB_TEST_TYPE);
        sub_test_name_tv.setVisibility(View.GONE);
        text_tv.setText("Enter " + Constant.SUB_TEST_TYPE + " Score");

        if (Constant.STUDENT_NAME.length() < 1) {
            student_name_tv.setText("Student Name");
        } else {
            student_name_tv.setText(Constant.STUDENT_NAME);
        }


        if (Constant.CLASS_ID.length() < 1) {
            student_class_tv.setText("Class " + ", " + "Registration Number");
            age_gender_tv.setText(getResources().getString(R.string.age_gender));
        } else {
            student_class_tv.setText("Class: " + Constant.STUDENT_CLASS + ", " +
                    Constant.STUDENT_REGISTRATION_NO);

            Calendar c = Calendar.getInstance();
            //SimpleDateFormat df = new SimpleDateFormat("M/d/yyyy hh:mm:ss a");
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
            String curentdate = df.format(c.getTime());

            Log.e(TAG, "dob==> " + Constant.STUDENT_DOB);
            Log.e(TAG, "curentdate==> " + curentdate);

            String age = CalculateAge(Constant.STUDENT_DOB, curentdate);

//            if (Constant.STUDENT_GENDER.equalsIgnoreCase("1")) {
//
//                age_gender_tv.setText(age+"/"+"F");
//
//            } else if (Constant.STUDENT_GENDER.equalsIgnoreCase("0")) {
//
//                age_gender_tv.setText(age+"/"+"M");
//            }
//
//            else if (Constant.STUDENT_GENDER.equalsIgnoreCase("2")){
//                age_gender_tv.setText(age+"/"+"T");
//            }
//
//            if (Constant.STUDENT_GENDER.equalsIgnoreCase("1")) {
//                boy_or_girl_img.setImageResource(R.drawable.girl_blue_i);
//            } else if (Constant.STUDENT_GENDER.equalsIgnoreCase("0")) {
//                boy_or_girl_img.setImageResource(R.drawable.boy_i);
//            }
            TestActivityFunctions.setGender(age_gender_tv, boy_or_girl_img, Constant.STUDENT_GENDER, age);

        }


        HashMap<String, String> weight_map = null;

        HashMap<String, String> height_map = null;
        try {


            weight_map = db.getParticularRow(getApplicationContext(), testTableName,
                    "student_id", Constant.STUDENT_ID, "test_type_id", Constant.WEIGHT_SUB_TEST_ID);

            double weight_score = Double.parseDouble(weight_map.get("score")) / 1000;

            height_map = db.getParticularRow(getApplicationContext(), testTableName,
                    "student_id", Constant.STUDENT_ID, "test_type_id", Constant.HEIGHT_SUB_TEST_ID);

            double height_score = Double.parseDouble(height_map.get("score")) / 10;

            // created_on = weight_map.get("created_on");

            created_on = weight_map.get("last_modified_on");

//            String dateStr =  weight_map.get("device_date");
//            DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
//            Date date = (Date) formatter.parse(dateStr);
//
//            device_date = Constant.ConvertDateToString(date);
            device_date = weight_map.get("device_date");

            previous_score = "Weight: " + weight_score + " Kg & Height: " + height_score + " cm";

            if (weight_score > 0) {
                score_tv.setVisibility(View.VISIBLE);
                // score_tv.setText("Score: "+previous_score+" ("+device_date+")");
                score_tv.setText("Score: " + previous_score + "");
            } else {
                score_tv.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

       /* weight_et.setText("");

        weight_et.setHint("00");*/

        local_student_id = Constant.STUDENT_ID;
    }

    private String CalculateAge(String studentDob, String currentDate) {
        Calendar cal1 = new GregorianCalendar();
        Calendar cal2 = new GregorianCalendar();
        int age = 0;
        int factor = 0;
        Date date1 = null;
        Date date2 = null;
        try {
           /* date1 = new SimpleDateFormat("M/d/yyyy").parse(studentDob);
            date2 = new SimpleDateFormat("M/d/yyyy").parse(currentDate);*/

            date1 = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").parse(studentDob);
            date2 = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").parse(currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        cal1.setTime(date1);
        cal2.setTime(date2);
        if (cal2.get(Calendar.DAY_OF_YEAR) < cal1.get(Calendar.DAY_OF_YEAR)) {
            factor = -1;
        }
        age = cal2.get(Calendar.YEAR) - cal1.get(Calendar.YEAR) + factor;

        return "" + age;
    }


    private void init() {

        db = DBManager.getInstance();
        gps = new GPSTracker(getApplicationContext());
        //location = new SimpleLocation(this);
        connectionDetector = new ConnectionDetector(this);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        forReTest = sharedPreferences.getString(AppConfig.FOR_RETEST, "0");
        testTableName = DBManager.TBL_LP_FITNESS_TEST_RESULT;
        if (forReTest.equalsIgnoreCase("1"))
            testTableName = DBManager.TBL_LP_FITNESS_RETEST_RESULT;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        Utility.showActionBar(this, toolbar, "");


        dashboard_img = (ImageView) toolbar.findViewById(R.id.dashboard_img);
        test_another_img = (FloatingActionButton) findViewById(R.id.test_another_img);
        reset_btn = (Button) findViewById(R.id.reset_btn);
        height_et = (EditText) findViewById(R.id.height_et);
        weight_et = (EditText) findViewById(R.id.weight_et);
        // school_tv = (TextView) findViewById(R.id.school_tv);
        save_btn = (Button) findViewById(R.id.save_btn);
        boy_or_girl_img = (ImageView) findViewById(R.id.boy_or_girl_img);
        student_class_tv = (TextView) findViewById(R.id.student_class_tv);
        student_name_tv = (TextView) findViewById(R.id.student_name_tv);
        test_name_tv = (TextView) toolbar.findViewById(R.id.test_name_tv);
        sub_test_name_tv = (TextView) toolbar.findViewById(R.id.sub_test_name_tv);
        text_tv = (TextView) findViewById(R.id.text_tv);
        score_tv = (TextView) findViewById(R.id.score_tv);
        age_gender_tv = (TextView) findViewById(R.id.age_gender_tv);
        scan_btn = (Button) findViewById(R.id.scan_btn);
        text_tv = (TextView) findViewById(R.id.text_tv);
        Typeface font_semi_bold = Typeface.createFromAsset(getAssets(),
                "fonts/Barlow-SemiBold.ttf");
        Typeface font_reg = Typeface.createFromAsset(getAssets(),
                "fonts/Barlow-Regular.ttf");
        save_btn.setTypeface(font_semi_bold);
        reset_btn.setTypeface(font_semi_bold);
        scan_btn.setTypeface(font_semi_bold);
        student_name_tv.setTypeface(font_reg);
        student_class_tv.setTypeface(font_reg);
        text_tv.setTypeface(font_reg);
        age_gender_tv.setTypeface(font_reg);
        height_et.setTypeface(font_reg);
        weight_et.setTypeface(font_reg);
        test_name_tv.setTypeface(font_reg);
        sub_test_name_tv.setTypeface(font_reg);

        // school_tv.setText(Constant.SCHOOL_NAME);

        test_another_img.setOnClickListener(this);
        reset_btn.setOnClickListener(this);
        save_btn.setOnClickListener(this);
        sub_test_name_tv.setOnClickListener(this);
        scan_btn.setOnClickListener(this);


        student_name_tv.setText(Constant.STUDENT_NAME);
//        student_class_tv.setText("Class: "+Constant.STUDENT_CLASS+", "+
//                Constant.SCHOOL_ID+Constant.STUDENT_ROLL_NO);
        test_name_tv.setText(Constant.TEST_TYPE);
        sub_test_name_tv.setText(Constant.SUB_TEST_TYPE);

        if (Constant.STUDENT_GENDER.equalsIgnoreCase("1")) {
            boy_or_girl_img.setImageResource(R.drawable.girl_blue_i);
        } else if (Constant.STUDENT_GENDER.equalsIgnoreCase("0")) {
            boy_or_girl_img.setImageResource(R.drawable.boy_i);
        }


        dashboard_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isFilled = CheckFields();
                if (isFilled) {
                    showExitDialog2();
                } else {
                    emptyStudentData();
                    Intent i1 = new Intent(WeightActivity.this, TestActivity.class);
                    startActivity(i1);
                    finish();
                }
            }
        });
    }

    private void emptyStudentData() {
        Constant.STUDENT_NAME = "";
        Constant.STUDENT_CLASS = "";
        Constant.STUDENT_REGISTRATION_NO = "0";
        Constant.STUDENT_SECTION = "";
        Constant.STUDENT_DOB = "";
        Constant.STUDENT_GENDER = "";
        Constant.STUDENT_ID = "";
        //    Constant.CAMP_ID = "";
        Constant.CLASS_ID = "";

        age_gender_tv.setText(getResources().getString(R.string.age_gender));

        score_tv.setVisibility(View.GONE);

        student_class_tv.setText("Class " + ", " + "Registration Number");
        student_name_tv.setText("Student Name");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.test_another_img:
                boolean isFilled1 = CheckFields();

                if (isFilled1) {
                    showExitDialog1();
                } else {
                    emptyStudentData();
                    Intent i = new Intent(WeightActivity.this, TestActivity.class);
                    startActivity(i);
                    finish();
                }
                break;

            case R.id.reset_btn:

                boolean isFilled2 = CheckFields();
                if (isFilled2) {
                    showResetDialog();
                } else {
                    emptyStudentData();
                    ClearFields();
                }


                break;

            case R.id.save_btn:
                String weight_txt = weight_et.getText().toString();
                String height_txt = height_et.getText().toString();

                lat = gps.getLatitude();
                lng = gps.getLongitude();
                String DateTime = Constant.getDateTimeServer();
                String deviceDateTime = Constant.getDateTime();

                Log.e(TAG, "lat=> " + lat + "   lng=> " + lng);
                Log.e(TAG, "DateTime=> " + DateTime);


                if (weight_txt.length() <= 0 || height_txt.length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
                } else if (Constant.STUDENT_NAME.length() == 0) {
                    Toast.makeText(getApplicationContext(), R.string.please_scan, Toast.LENGTH_SHORT).show();
                } else {
//                    double weight_count = Double.parseDouble(weight_txt)*1000;
//                    int height_int = Integer.parseInt(height_txt)*10;
                    double weight_count = Double.parseDouble(weight_txt);
                    int height_int = Integer.parseInt(height_txt);
                    if (height_int < AppConfig.HEIGHT_MIN_LIMIT || height_int > AppConfig.HEIGHT_MAX_LIMIT)
                        Toast.makeText(this, getString(R.string.validate_height), Toast.LENGTH_SHORT).show();
                    else if (weight_count < AppConfig.WEIGHT_MIN_LIMIT || weight_count > AppConfig.WEIGHT_MAX_LIMIT)
                        Toast.makeText(this, getString(R.string.validate_weight), Toast.LENGTH_SHORT).show();
                    else {
                        weight_count = weight_count * 1000;
                        height_int = height_int * 10;
                        showSaveDialog("" + weight_count, "" + height_int, DateTime, deviceDateTime);
                    }

                }
//                else {
//                    double weight_count = Double.parseDouble(weight_txt)*1000;
//
//                    int height_int = Integer.parseInt(height_txt)*10;
//                    if(Constant.STUDENT_NAME.length()>1){
//                        showSaveDialog(""+weight_count,""+height_int, DateTime, deviceDateTime);
//
//                    } else {
//                        Toast.makeText(getApplicationContext(),R.string.please_scan, Toast.LENGTH_SHORT).show();
//                    }
//
//                }

                break;

            case R.id.sub_test_name_tv:

                boolean isFilled = CheckFields();
                if (isFilled) {
                    showExitDialog1();
                } else {
                    emptyStudentData();
                    ClearFields();
                    Intent i1 = new Intent(WeightActivity.this, TestActivity.class);
                    startActivity(i1);
                    finish();
                }
                break;

            case R.id.scan_btn:
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);
                else {
                    Intent i1 = new Intent(this, ScanActivity.class);
                    startActivity(i1);
                }


                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent i1 = new Intent(this, ScanActivity.class);
                startActivity(i1);
            } else
                Utility.showPermissionDialog(this, getString(R.string.accept_permission, "Camera"));
        }
    }

    private void showResetDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(WeightActivity.this);

        alertDialog.setMessage(R.string.reset_data);

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.alarm);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                weight_et.setText("");

                weight_et.setHint("00");

                height_et.setText("");

                height_et.setHint("00");

                emptyStudentData();

                dialog.cancel();
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    private void showSaveDialog(final String weight_txt, final String height, final String DateTime, final String deviceDateTime) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(WeightActivity.this);
        alertDialog.setCancelable(false);
        alertDialog.setMessage("Are you sure you want to save the data?");

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.alarm);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();

                yesOverride = false;
                current_height = height;

                  /*  insertWeightTransactionData(weight_txt, DateTime,deviceDateTime);

                    insertHeightTransactionData(height, DateTime,deviceDateTime);*/

System.out.println("Weight Activity "+weight_txt+"      "+height+"    "+DateTime+"    "+deviceDateTime);
                insertWeightHeightTransactionData(weight_txt, height, DateTime, deviceDateTime);


            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

  /*  private void showHeightUpdateDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(WeightActivity.this);
        alertDialog.setCancelable(false);

        // alertDialog.setMessage("This Student has already given the test. Do you want to update the test result?");

        alertDialog.setMessage("You have already taken the "+Constant.TEST_NAME+"test. Do you want to update the test result?");

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
                db.deleteTransactionRow(WeightActivity.this, db.TBL_LP_FITNESS_TEST_RESULT, "student_id", Constant.STUDENT_ID,
                        "test_type_id", Constant.HEIGHT_SUB_TEST_ID);
                db.insertTables(db.TBL_LP_FITNESS_TEST_RESULT, model_height);
                Toast.makeText(getApplicationContext(), "Data updated successfully in local DB", Toast.LENGTH_SHORT).show();
                ClearFields();
                emptyStudentData();
                // db.updateTables(db.TBL_LP_FITNESS_TEST_RESULT, model,"student_id",Constant.STUDENT_ID,"test_type_id", Constant.SUB_TEST_ID);
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }*/


  /*  private void insertWeightTransactionData(String weight, String date, String deviceDateTime) {

        model_weight = new FitnessTestResultModel();
        model_weight.setStudent_id(Integer.parseInt(Constant.STUDENT_ID));
        model_weight.setCamp_id(Integer.parseInt(Constant.CAMP_ID));
        model_weight.setTest_type_id(Integer.parseInt(Constant.WEIGHT_SUB_TEST_ID));
        model_weight.setTest_coordinator_id(Integer.parseInt(Constant.TEST_COORDINATOR_ID));
        model_weight.setScore(weight);
        model_weight.setPercentile(0);
        model_weight.setCreated_on(date);
        model_weight.setCreated_by(Constant.TEST_COORDINATOR_ID);
        model_weight.setLast_modified_by("");
        model_weight.setLast_modified_on(date);
        model_weight.setLatitude(lat);
        model_weight.setLongitude(lng);
        model_weight.setSubTestName("Weight");
        model_weight.setTestedOrNot(true);
        model_weight.setSyncedOrNot(false);
        model_weight.setTestName(Constant.TEST_TYPE);
        model_weight.setDevice_date(deviceDateTime);

        HashMap<String, String> map = null;
        try {
            map = db.getParticularRow(getApplicationContext(),DBManager.TBL_LP_FITNESS_TEST_RESULT,
                    "student_id", Constant.STUDENT_ID, "test_type_id", Constant.WEIGHT_SUB_TEST_ID);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        Log.e(TAG,"weight==> "+weight);
        if(map.size()==0){
            db.insertTables(db.TBL_LP_FITNESS_TEST_RESULT, model_weight);

            showDataSavedSuccessfullyDialog();

            local_sub_test_id =  Constant.WEIGHT_SUB_TEST_ID;

            if (connectionDetector.isConnectingToInternet()) {
                CallTransactionAPI(Integer.parseInt(Constant.WEIGHT_SUB_TEST_ID));
            }

        } else {
            String testedORNot = map.get("tested");
            if(testedORNot.equalsIgnoreCase("true")){

                showWeightUpdateDialog(weight,current_height);
            }
            else if(testedORNot.equalsIgnoreCase("false")) {
                db.deleteTransactionRow(WeightActivity.this, db.TBL_LP_FITNESS_TEST_RESULT, "student_id", Constant.STUDENT_ID,
                        "test_type_id", Constant.WEIGHT_SUB_TEST_ID);
                db.insertTables(db.TBL_LP_FITNESS_TEST_RESULT, model_weight);
                showDataSavedSuccessfullyDialog();

                local_sub_test_id =  Constant.WEIGHT_SUB_TEST_ID;

                if (connectionDetector.isConnectingToInternet()) {
                    CallTransactionAPI(Integer.parseInt(Constant.WEIGHT_SUB_TEST_ID));
                }
            }

        }
    }*/

  /*  private void insertHeightTransactionData(String height, String date, String deviceDate) {

        model_height = new FitnessTestResultModel();
        model_height.setStudent_id(Integer.parseInt(Constant.STUDENT_ID));
        model_height.setCamp_id(Integer.parseInt(Constant.CAMP_ID));
        model_height.setTest_type_id(Integer.parseInt(Constant.HEIGHT_SUB_TEST_ID));
        model_height.setTest_coordinator_id(Integer.parseInt(Constant.TEST_COORDINATOR_ID));
        model_height.setScore(height);
        model_height.setPercentile(0);
        model_height.setCreated_on(date);
        model_height.setCreated_by(Constant.TEST_COORDINATOR_ID);
        model_height.setLast_modified_by("");
        model_height.setLast_modified_on(date);
        model_height.setLatitude(lat);
        model_height.setLongitude(lng);
        model_height.setSubTestName("Height");
        model_height.setTestedOrNot(true);
        model_height.setSyncedOrNot(false);
        model_height.setTestName(Constant.TEST_TYPE);
        model_height.setDevice_date(deviceDate);


        HashMap<String, String> map = null;
        try {
            map = db.getParticularRow(getApplicationContext(),DBManager.TBL_LP_FITNESS_TEST_RESULT,
                    "student_id", Constant.STUDENT_ID, "test_type_id", Constant.HEIGHT_SUB_TEST_ID);
        }catch(Exception e){
            e.printStackTrace();
        }

        String rows = db.getTableAsString( db.TBL_LP_FITNESS_TEST_RESULT);
        Log.e(TAG,"rows==> "+rows);

        Log.e(TAG,"height==> "+height);
        if(map.size()==0){
            db.insertTables(db.TBL_LP_FITNESS_TEST_RESULT, model_height);

            //   showDataSavedSuccessfullyDialog();


            local_sub_test_id =  Constant.HEIGHT_SUB_TEST_ID;

            if (connectionDetector.isConnectingToInternet()) {
                CallTransactionAPI(Integer.parseInt(Constant.HEIGHT_SUB_TEST_ID));
            }

            // }

        } else {
            String testedORNot = map.get("tested");
            if(testedORNot.equalsIgnoreCase("true")){
                //  showHeightUpdateDialog();

                if(yesOverride){
                    // update

                    local_sub_test_id =  Constant.HEIGHT_SUB_TEST_ID;

                    db.deleteTransactionRow(WeightActivity.this, db.TBL_LP_FITNESS_TEST_RESULT, "student_id", Constant.STUDENT_ID,
                            "test_type_id", Constant.HEIGHT_SUB_TEST_ID);
                    db.insertTables(db.TBL_LP_FITNESS_TEST_RESULT, model_height);

                    //       showDataSavedSuccessfullyDialog();

                    local_sub_test_id =  Constant.HEIGHT_SUB_TEST_ID;

                    if (connectionDetector.isConnectingToInternet()) {
                        CallTransactionAPI(Integer.parseInt(Constant.HEIGHT_SUB_TEST_ID));
                    }
                }
            } else if(testedORNot.equalsIgnoreCase("false")) {
                db.deleteTransactionRow(WeightActivity.this, db.TBL_LP_FITNESS_TEST_RESULT, "student_id", Constant.STUDENT_ID,
                        "test_type_id", Constant.HEIGHT_SUB_TEST_ID);
                db.insertTables(db.TBL_LP_FITNESS_TEST_RESULT, model_height);

                //    showDataSavedSuccessfullyDialog();

                if (connectionDetector.isConnectingToInternet()) {
                    CallTransactionAPI(Integer.parseInt(Constant.HEIGHT_SUB_TEST_ID));
                }
            }

        }
    }*/

    // *********************************************************************************************//
    private void insertWeightHeightTransactionData(String weight, String height, String date, String deviceDateTime) {
        try {
            model_weight = new FitnessTestResultModel();
            model_weight.setStudent_id(Integer.parseInt(Constant.STUDENT_ID));
            model_weight.setCamp_id(Integer.parseInt(Constant.CAMP_ID));
            model_weight.setTest_type_id(Integer.parseInt(Constant.WEIGHT_SUB_TEST_ID));
            model_weight.setTest_coordinator_id(Integer.parseInt(Constant.TEST_COORDINATOR_ID));
            model_weight.setScore(weight);
            model_weight.setPercentile(0);
            model_weight.setCreated_on(date);
            model_weight.setCreated_by(Constant.TEST_COORDINATOR_ID);
            model_weight.setLast_modified_by("");
            model_weight.setLast_modified_on(date);
            model_weight.setLatitude(lat);
            model_weight.setLongitude(lng);
            model_weight.setSubTestName("Weight");
            model_weight.setTestedOrNot(true);
            model_weight.setSyncedOrNot(false);
            model_weight.setTestName(Constant.TEST_TYPE);
            model_weight.setDevice_date(deviceDateTime);

            model_height = new FitnessTestResultModel();
            model_height.setStudent_id(Integer.parseInt(Constant.STUDENT_ID));
            model_height.setCamp_id(Integer.parseInt(Constant.CAMP_ID));
            model_height.setTest_type_id(Integer.parseInt(Constant.HEIGHT_SUB_TEST_ID));
            model_height.setTest_coordinator_id(Integer.parseInt(Constant.TEST_COORDINATOR_ID));
            model_height.setScore(height);
            model_height.setPercentile(0);
            model_height.setCreated_on(date);
            model_height.setCreated_by(Constant.TEST_COORDINATOR_ID);
            model_height.setLast_modified_by("");
            model_height.setLast_modified_on(date);
            model_height.setLatitude(lat);
            model_height.setLongitude(lng);
            model_height.setSubTestName("Height");
            model_height.setTestedOrNot(true);
            model_height.setSyncedOrNot(false);
            model_height.setTestName(Constant.TEST_TYPE);
            model_height.setDevice_date(deviceDateTime);

            HashMap<String, String> map = null;
            try {
                map = db.getParticularRow(getApplicationContext(), testTableName,
                        "student_id",
                        Constant.STUDENT_ID,
                        "test_type_id",
                        Constant.WEIGHT_SUB_TEST_ID);
                System.out.println("WEIGHT "+map);

            } catch (Exception e) {
                System.out.println("WEIGHT "+e.getMessage());
                e.printStackTrace();
            }

            Log.e(TAG, "weight==> " + weight);
            if (map.size() == 0) {
                db.insertTables(testTableName, model_weight);

                db.insertTables(testTableName, model_height);

                showDataSavedSuccessfullyDialog();

                local_sub_test_id = Constant.WEIGHT_SUB_TEST_ID;

                if (connectionDetector.isConnectingToInternet()) {
                    CallTransactionAPI(Integer.parseInt(Constant.WEIGHT_SUB_TEST_ID), Integer.parseInt(Constant.HEIGHT_SUB_TEST_ID));
                } else
                    Toast.makeText(getApplicationContext(), R.string.data_saved_offline, Toast.LENGTH_SHORT).show();

            } else {
                String testedORNot = map.get("tested");
                if (testedORNot.equalsIgnoreCase("true")) {

                    showWeightUpdateDialog(weight, height);
                } else if (testedORNot.equalsIgnoreCase("false")) {

                    db.deleteTransactionRow(WeightActivity.this, testTableName, "student_id", Constant.STUDENT_ID,
                            "test_type_id", Constant.WEIGHT_SUB_TEST_ID, "camp_id", Constant.CAMP_ID);

                    db.deleteTransactionRow(WeightActivity.this, testTableName, "student_id", Constant.STUDENT_ID,
                            "test_type_id", Constant.HEIGHT_SUB_TEST_ID, "camp_id", Constant.CAMP_ID);

                    db.insertTables(testTableName, model_weight);
                    db.insertTables(testTableName, model_height);

                    showDataSavedSuccessfullyDialog();

                    local_sub_test_id = Constant.WEIGHT_SUB_TEST_ID;

                    if (connectionDetector.isConnectingToInternet()) {
                        CallTransactionAPI(Integer.parseInt(Constant.WEIGHT_SUB_TEST_ID), Integer.parseInt(Constant.HEIGHT_SUB_TEST_ID));
                    } else
                        Toast.makeText(getApplicationContext(), R.string.data_saved_offline, Toast.LENGTH_SHORT).show();
                }

            }
        } catch (Exception e) {
            System.out.println("WeightActivity" + e.getMessage());
        }
    }


    private void showDataSavedSuccessfullyDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(WeightActivity.this);
        alertDialog.setCancelable(false);
        //   alertDialog.setMessage("This Student has already given the test. Do you want to update the test result?");

        alertDialog.setMessage("The Height & Weight scores for " + Constant.STUDENT_NAME + "" +
                " has been saved in the database. To see the score again, you need to scan the student card.");

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
                ClearFields();
                emptyStudentData();

            }
        });
        alertDialog.show();
    }

    private void showWeightUpdateDialog(String weight, String height) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(WeightActivity.this);
        alertDialog.setCancelable(false);
        double weight_double = Double.parseDouble(weight) / 1000;

        double height_double = Double.parseDouble(height) / 10;

        String current_score = "Weight: " + weight_double + " Kg & Height: " + height_double + " cm";

//        alertDialog.setMessage(Constant.STUDENT_NAME+" has already taken the height & Weight test" +
//                " on "+ device_date+"\n\n"+"Previous Score: " + previous_score+"\n\n"+"Current Score: " + current_score+"\n\n"+" Do you want to update the test result?");

        alertDialog.setMessage(Constant.STUDENT_NAME + " has already taken the Height & Weight test" +
                "\n\n" + "Previous Score: " + previous_score + "\n\n" + "Current Score: " + current_score + "\n\n" + " Do you want to update the test result?");

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


                yesOverride = true;
                db.deleteTransactionRow(WeightActivity.this, testTableName, "student_id", Constant.STUDENT_ID,
                        "test_type_id", Constant.WEIGHT_SUB_TEST_ID, "camp_id", Constant.CAMP_ID);
                db.deleteTransactionRow(WeightActivity.this, testTableName, "student_id", Constant.STUDENT_ID,
                        "test_type_id", Constant.HEIGHT_SUB_TEST_ID, "camp_id", Constant.CAMP_ID);

                db.insertTables(testTableName, model_weight);
                db.insertTables(testTableName, model_height);
                Log.e("weight score=> ", "weight== " + model_weight.getScore());

                //     showDataSavedSuccessfullyDialog();

                local_sub_test_id = Constant.WEIGHT_SUB_TEST_ID;


                if (connectionDetector.isConnectingToInternet()) {
                    CallTransactionAPI(Integer.parseInt(Constant.WEIGHT_SUB_TEST_ID), Integer.parseInt(Constant.HEIGHT_SUB_TEST_ID));
                } else
                    Toast.makeText(getApplicationContext(), R.string.data_saved_offline, Toast.LENGTH_SHORT).show();

                emptyStudentData();
                ClearFields();

                dialog.cancel();
                // db.updateTables(db.TBL_LP_FITNESS_TEST_RESULT, model,"student_id",Constant.STUDENT_ID,"test_type_id", Constant.SUB_TEST_ID);
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                emptyStudentData();
                ClearFields();
            }
        });
        alertDialog.show();
    }

    private void CallTransactionAPI(int weight_sub_test_id, int height_sub_test_id) {

        HashMap<String, String> map_result_weight = db.getParticularRow(getApplicationContext(), testTableName,
                "student_id", Constant.STUDENT_ID, "test_type_id", String.valueOf(weight_sub_test_id));

        HashMap<String, String> map_result_height = db.getParticularRow(getApplicationContext(), testTableName,
                "student_id", Constant.STUDENT_ID, "test_type_id", String.valueOf(height_sub_test_id));

        jArray = new JSONArray();
        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                jObj = new JSONObject();

                try {
                    jObj.put("Student_ID", map_result_weight.get("student_id"));
                    jObj.put("Camp_ID", "" + map_result_weight.get("camp_id"));
                    jObj.put("Test_Type_ID", "" + map_result_weight.get("test_type_id"));
                    jObj.put("Test_Coordinator_ID", "" + map_result_weight.get("test_coordinator_id"));
                    jObj.put("Score", "" + map_result_weight.get("score"));
                    jObj.put("Percentile", "" + map_result_weight.get("percentile"));
                    jObj.put("Created_By", "" + map_result_weight.get("created_by"));
                    jObj.put("Created_On", "" + map_result_weight.get("created_on"));
                    jObj.put("SyncDateTime", "");
                    jObj.put("Longitude", "" + map_result_weight.get("longitude"));
                    jObj.put("Latitude", "" + map_result_weight.get("latitude"));


                    String dateStr = map_result_weight.get("device_date");
                    DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
                    Date date = (Date) formatter.parse(dateStr);

                    String Last_Modified_On = Constant.ConvertDateToString(date);

                    jObj.put("Last_Modified_On", Last_Modified_On);

                    jArray.put(jObj);

                    Log.e("WeightActivity", "jArray==> " + jArray);

                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            } else if (i == 1) {
                jObj = new JSONObject();

                try {
                    jObj.put("Student_ID", map_result_height.get("student_id"));
                    jObj.put("Camp_ID", "" + map_result_height.get("camp_id"));
                    jObj.put("Test_Type_ID", "" + map_result_height.get("test_type_id"));
                    jObj.put("Test_Coordinator_ID", "" + map_result_height.get("test_coordinator_id"));
                    jObj.put("Score", "" + map_result_height.get("score"));
                    jObj.put("Percentile", "" + map_result_height.get("percentile"));
                    jObj.put("Created_By", "" + map_result_height.get("created_by"));
                    jObj.put("Created_On", "" + map_result_height.get("created_on"));
                    jObj.put("SyncDateTime", "");
                    jObj.put("Longitude", "" + map_result_height.get("longitude"));
                    jObj.put("Latitude", "" + map_result_height.get("latitude"));


                    String dateStr = map_result_height.get("device_date");
                    DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
                    Date date = (Date) formatter.parse(dateStr);

                    String Last_Modified_On = Constant.ConvertDateToString(date);

                    jObj.put("Last_Modified_On", Last_Modified_On);

                    jArray.put(jObj);

                    Log.e("WeightActivity", "jArray==> " + jArray);

                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }


        HashMap<String, String> map = new HashMap<String, String>();
        map.put("data", jArray.toString());
        TransactionRequest transactionRequest = new TransactionRequest(this, map, this);
        transactionRequest.hitUserRequest();

    }

    private void ClearFields() {
        weight_et.setText("");
        weight_et.setHint("00");

        height_et.setText("");
        height_et.setHint("00");
    }

    public void onBackPressed() {

        boolean isFilled = CheckFields();
        if (isFilled) {
            showExitDialog();
        } else {
            emptyStudentData();
            Intent i = new Intent(WeightActivity.this, TestActivity.class);
            startActivity(i);
            finish();
        }
        //moveTaskToBack(true);

    }

    private boolean CheckFields() {
        String weight = weight_et.getText().toString().trim();
        String height = height_et.getText().toString().trim();

        if (weight.length() > 0 || height.length() > 0) {
            return true;
        }

        return false;

    }

    private void showExitDialog1() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(WeightActivity.this);
        alertDialog.setCancelable(false);
        alertDialog.setMessage(getResources().getString(R.string.exit_from_test));

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.alarm);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                emptyStudentData();
                ClearFields();

                Constant.ISComingFromTestScreen = true;
                dialog.cancel();
                Intent i1 = new Intent(WeightActivity.this, TestActivity.class);
                startActivity(i1);
                finish();
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }


    private void showExitDialog2() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(WeightActivity.this);
        alertDialog.setCancelable(false);

        alertDialog.setMessage(getResources().getString(R.string.exit_from_test));

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.alarm);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                emptyStudentData();
                dialog.cancel();
                Intent i1 = new Intent(WeightActivity.this, TestActivity.class);
                startActivity(i1);
                finish();
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }


    private void showExitDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(WeightActivity.this);
        alertDialog.setCancelable(false);

        alertDialog.setMessage(getResources().getString(R.string.exit_from_test));

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.alarm);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
                emptyStudentData();
                Intent i = new Intent(WeightActivity.this, TestActivity.class);
                startActivity(i);
                finish();
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return Utility.hideKeyboard(WeightActivity.this);
    }

    @Override
    protected void onPause() {
        // stop location updates (saves battery)
        //location.endUpdates();

        super.onPause();
    }

    @Override
    public void onResponse(Object obj) {
        if (obj instanceof TransactionModel) {

            TransactionModel model = (TransactionModel) obj;

            Log.e(TAG, "sucess==> " + model.getIsSuccess());
            Log.e(TAG, "message==> " + model.getMessage());

            if (model.getIsSuccess().equalsIgnoreCase("true")) {

                Toast.makeText(this, model.getMessage(), Toast.LENGTH_SHORT).show();

                try {

                    db.deleteTransactionRow(this, testTableName, "student_id", "" + WeightActivity.this.model_weight.getStudent_id(),
                            "test_type_id", "" + WeightActivity.this.model_weight.getTest_type_id(), "camp_id", Constant.CAMP_ID);
//
                    this.model_weight.setSyncedOrNot(true);
                    db.insertTables(testTableName, this.model_weight);


                    db.deleteTransactionRow(this, testTableName, "student_id", "" + WeightActivity.this.model_height.getStudent_id(),
                            "test_type_id", "" + WeightActivity.this.model_height.getTest_type_id(), "camp_id", Constant.CAMP_ID);
//
                    this.model_height.setSyncedOrNot(true);
                    db.insertTables(testTableName, this.model_height);

                    Log.e("TimeActivity", "synced==> " + this.model_height.isSyncedOrNot());
                    Log.e("TimeActivity", "synced==> " + this.model_weight.isSyncedOrNot());


                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else {
                Toast.makeText(this, R.string.data_saved_offline, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, getString(R.string.unable_connect_server), Toast.LENGTH_SHORT).show();
        }
    }

}
