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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import kheloindia.com.assessment.model.TransactionModel;
import kheloindia.com.assessment.util.AppConfig;
import kheloindia.com.assessment.util.ConnectionDetector;
import kheloindia.com.assessment.util.DBManager;
import kheloindia.com.assessment.util.ResponseListener;
import kheloindia.com.assessment.util.Utility;
import kheloindia.com.assessment.functions.Constant;
import kheloindia.com.assessment.functions.GPSTracker;
import kheloindia.com.assessment.model.FitnessTestResultModel;
import kheloindia.com.assessment.webservice.TransactionRequest;

/**
 * Created by PC10 on 05/15/2017.
 */

public class DistanceActivity extends AppCompatActivity implements View.OnClickListener, ResponseListener {

    ImageView dashboard_img;
    FloatingActionButton test_another_img;
    Toolbar toolbar;
    Button reset_btn, save_btn, scan_btn;
    EditText meter_et, cm_et, mili_meter_et, meter2_et, cm2_et, mili_meter2_et;
    TextView test_name_tv, sub_test_name_tv, text_tv, net_score_tv, init_pos_tv, fin_pos_tv;
    ImageView boy_or_girl_img;
    TextView student_name_tv, student_class_tv;
    // TextView school_tv;
    FitnessTestResultModel model;
    TextView score_tv;
    LinearLayout distance_lt2;

    JSONObject jObj;

    String created_on = "", device_date = "";

    double distance = 0.0;

    DBManager db;

    GPSTracker gps;
    double lat, lng;

    String local_student_id = "";

    //private SimpleLocation location;

    String TAG = "DistanceActivity";

    ConnectionDetector connectionDetector;
    String forReTest, testTableName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_flexibility);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gps = new GPSTracker(getApplicationContext());
        gps.getmGoogleApiClient().connect();
        sub_test_name_tv.setText(Constant.SUB_TEST_TYPE);
        //santosh
        text_tv.setText("Enter " + Constant.SUB_TEST_TYPE + " Scores");
        //


        if (Constant.STUDENT_NAME.length() < 1) {
            student_name_tv.setText("Student Name");
        } else {
            student_name_tv.setText(Constant.STUDENT_NAME);
        }


        if (Constant.CLASS_ID.length() < 1) {
            student_class_tv.setText("Class " + ", " + "Registration Number");
        } else {
            student_class_tv.setText("Class: " + Constant.STUDENT_CLASS + ", " +
                    Constant.STUDENT_REGISTRATION_NO);
        }

        if (Constant.STUDENT_GENDER.equalsIgnoreCase("1")) {
            boy_or_girl_img.setImageResource(R.drawable.girl_blue_i);
        } else if (Constant.STUDENT_GENDER.equalsIgnoreCase("0")) {
            boy_or_girl_img.setImageResource(R.drawable.boy_i);
        }
        /*minutes_tv.setText("");
        seconds_tv.setText("");
        mili_seconds_tv.setText("");

        minutes_tv.setHint("00");
        seconds_tv.setHint("00");
        mili_seconds_tv.setHint("00");*/

        HashMap<String, String> map = null;
        try {
            map = db.getParticularRow(getApplicationContext(), testTableName,
                    "student_id", Constant.STUDENT_ID, "test_type_id", Constant.SUB_TEST_ID);

            String score = map.get("score");
            //  created_on = map.get("created_on");
            created_on = map.get("last_modified_on");


//            String dateStr =  map.get("device_date");
//            DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
//            Date date = (Date) formatter.parse(dateStr);
//
//            device_date = Constant.ConvertDateToString(date);
            device_date = map.get("device_date");

            if (score.length() > 0) {
                distance = Double.parseDouble(score) / 10;
                score_tv.setVisibility(View.VISIBLE);
                String converted_distance = Utility.convertCentiMetreToMtCmMm(DistanceActivity.this, distance);
                score_tv.setText("Score: " + converted_distance + " ");
                //  score_tv.setText("Score: "+converted_distance+" "+" ("+device_date+")");
            } else {
                score_tv.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        local_student_id = Constant.STUDENT_ID;
    }


    private void init() {

        gps = new GPSTracker(getApplicationContext());
        //location = new SimpleLocation(this);
        db = DBManager.getInstance();
        connectionDetector = new ConnectionDetector(this);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        forReTest = sharedPreferences.getString(AppConfig.FOR_RETEST, "0");
        testTableName = DBManager.TBL_LP_FITNESS_TEST_RESULT;
        if (forReTest.equalsIgnoreCase("1"))
            testTableName = DBManager.TBL_LP_FITNESS_RETEST_RESULT;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        dashboard_img = (ImageView) toolbar.findViewById(R.id.dashboard_img);
        test_another_img = (FloatingActionButton) findViewById(R.id.test_another_img);
        reset_btn = (Button) findViewById(R.id.reset_btn);
        mili_meter_et = (EditText) findViewById(R.id.mili_meter_et);
        mili_meter2_et = (EditText) findViewById(R.id.mili_meter2_et);
        meter_et = (EditText) findViewById(R.id.meter_et);
        meter2_et = (EditText) findViewById(R.id.meter2_et);
        cm_et = (EditText) findViewById(R.id.cm_et);
        cm2_et = (EditText) findViewById(R.id.cm2_et);

        test_name_tv = (TextView) toolbar.findViewById(R.id.test_name_tv);
        sub_test_name_tv = (TextView) toolbar.findViewById(R.id.sub_test_name_tv);
        //school_tv = (TextView) findViewById(R.id.school_tv);
        save_btn = (Button) findViewById(R.id.save_btn);
        text_tv = (TextView) findViewById(R.id.text_tv);
        boy_or_girl_img = (ImageView) findViewById(R.id.boy_or_girl_img);
        student_class_tv = (TextView) findViewById(R.id.student_class_tv);
        student_name_tv = (TextView) findViewById(R.id.student_name_tv);
        scan_btn = (Button) findViewById(R.id.scan_btn);
        score_tv = (TextView) findViewById(R.id.score_tv);
        net_score_tv = (TextView) findViewById(R.id.net_score_tv);
        init_pos_tv = (TextView) findViewById(R.id.init_pos_tv);
        fin_pos_tv = (TextView) findViewById(R.id.fin_pos_tv);
        distance_lt2 = (LinearLayout) findViewById(R.id.distance_lt2);
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
        meter_et.setTypeface(font_reg);
        cm_et.setTypeface(font_reg);
        mili_meter_et.setTypeface(font_reg);
        meter2_et.setTypeface(font_reg);
        cm2_et.setTypeface(font_reg);
        mili_meter2_et.setTypeface(font_reg);
        init_pos_tv.setTypeface(font_reg);
        fin_pos_tv.setTypeface(font_reg);
        net_score_tv.setTypeface(font_reg);
        text_tv.setTypeface(font_reg);
        test_name_tv.setTypeface(font_reg);
        sub_test_name_tv.setTypeface(font_reg);

        toolbar.setTitle(Constant.TEST_TYPE);
        test_name_tv.setText(Constant.TEST_TYPE);
        sub_test_name_tv.setText(Constant.SUB_TEST_TYPE);
        //   school_tv.setText(Constant.SCHOOL_NAME);
        text_tv.setText("Enter " + Constant.SUB_TEST_TYPE + " Score");


        try {

//            if(Constant.SUB_TEST_ID.equalsIgnoreCase("8")){
//                meter_et.setText("00");
//                meter_et.setFocusable(false);
//            }
            if (Constant.STUDENT_NAME.length() < 1) {
                student_name_tv.setText("Student Name");
            } else {
                student_name_tv.setText(Constant.STUDENT_NAME);
            }


//            if(Constant.CLASS_ID.length()<1){
//                student_class_tv.setText("Class "+", "+"Registration Number");
//            } else {
//                student_class_tv.setText("Class: " + Constant.STUDENT_CLASS + ", " +
//                        Constant.STUDENT_REGISTRATION_NO);
//            }

            if (Constant.STUDENT_GENDER.equalsIgnoreCase("1")) {
                boy_or_girl_img.setImageResource(R.drawable.girl_blue_i);
            } else if (Constant.STUDENT_GENDER.equalsIgnoreCase("0")) {
                boy_or_girl_img.setImageResource(R.drawable.boy_i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        test_another_img.setOnClickListener(this);
        reset_btn.setOnClickListener(this);
        save_btn.setOnClickListener(this);
        sub_test_name_tv.setOnClickListener(this);
        scan_btn.setOnClickListener(this);

        //santosh
        cm2_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                net_score_tv.setText("Net Score: " + Utility.convertMilliMetreToCmMm(DistanceActivity.this, validateEditFieldsAndSetValues()));

            }
        });
        cm_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                net_score_tv.setText("Net Score: " + Utility.convertMilliMetreToCmMm(DistanceActivity.this, validateEditFieldsAndSetValues()));
            }
        });
        mili_meter2_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                net_score_tv.setText("Net Score: " + Utility.convertMilliMetreToCmMm(DistanceActivity.this, validateEditFieldsAndSetValues()));
            }
        });
        mili_meter_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                net_score_tv.setText("Net Score: " + Utility.convertMilliMetreToCmMm(DistanceActivity.this, validateEditFieldsAndSetValues()));
            }
        });

        if (Constant.FINAL_POSITION.equals("0")) {
            init_pos_tv.setVisibility(View.GONE);
            fin_pos_tv.setVisibility(View.GONE);
            net_score_tv.setVisibility(View.GONE);
            distance_lt2.setVisibility(View.GONE);

        } else {
            meter_et.setCursorVisible(false);
            meter_et.setFocusable(false);
        }
        //

        dashboard_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isFilled = CheckFields();
                if (isFilled) {
                    showExitDialog2();
                } else {
                    emptyStudentData();
                    Intent i1 = new Intent(DistanceActivity.this, TestActivity.class);
                    startActivity(i1);
                    finish();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.test_another_img:
                boolean isFilled = CheckFields();
                if (isFilled) {
                    showExitDialog2();
                } else {
                    Intent i = new Intent(DistanceActivity.this, TestActivity.class);
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
                String meter_txt = meter_et.getText().toString();
                String cm_txt = cm_et.getText().toString();
                String mili_meter_txt = mili_meter_et.getText().toString();

//santosh

                String meter2_txt = meter2_et.getText().toString();
                String cm2_txt = cm2_et.getText().toString();
                String mili_meter2_txt = mili_meter2_et.getText().toString();
                //

                int meter_int, cm_int, mili_meter_int, meter2_int, cm2_int, mili_meter2_int;

                if (meter_txt.length() > 0) {
                    meter_int = Integer.parseInt(meter_txt);
                } else {
                    meter_int = 0;
                }

                if (cm_txt.length() > 0) {
                    cm_int = Integer.parseInt(cm_txt);
                } else {
                    cm_int = 0;
                }

                if (mili_meter_txt.length() > 0) {
                    mili_meter_int = Integer.parseInt(mili_meter_txt);
                } else {
                    mili_meter_int = 0;
                }

                //santosh
                if (meter2_txt.length() > 0) {
                    meter2_int = Integer.parseInt(meter2_txt);
                } else {
                    meter2_int = 0;
                }

                if (cm2_txt.length() > 0) {
                    cm2_int = Integer.parseInt(cm2_txt);
                } else {
                    cm2_int = 0;
                }

                if (mili_meter2_txt.length() > 0) {
                    mili_meter2_int = Integer.parseInt(mili_meter2_txt);
                } else {
                    mili_meter2_int = 0;
                }
//
                lat = gps.getLatitude();
                lng = gps.getLongitude();
                String DateTime = Constant.getDateTimeServer();
                String deviceDateTime = Constant.getDateTime();

                Log.e(TAG, "lat=> " + lat + "   lng=> " + lng);
                Log.e(TAG, "DateTime=> " + DateTime);

                if ((meter_int < 1 && cm_int < 1 && mili_meter_int < 1) || (Constant.FINAL_POSITION.equals("1") && meter2_int < 1 && cm2_int < 1 && mili_meter2_int < 1)) {

                    Toast.makeText(getApplicationContext(), "Please fill all the fields", Toast.LENGTH_LONG).show();
                } else {
                    double milimeters = Constant.COnvertToMilimeter(String.valueOf(meter_int),
                            String.valueOf(cm_int), String.valueOf(mili_meter_int));
                    Log.e(TAG, "milimeters=> " + milimeters);
                    //Toast.makeText(getApplicationContext(),""+milimeters,Toast.LENGTH_LONG).show();
//santosh
                    double milimeters2 = Constant.COnvertToMilimeter(String.valueOf(meter2_int),
                            String.valueOf(cm2_int), String.valueOf(mili_meter2_int));
                    Log.e(TAG, "milimeters2=> " + milimeters2);
                    //

                    if (Constant.STUDENT_NAME.length() > 1) {

                        ArrayList<String> seniorList = new ArrayList<>();

                        int cut_off = Integer.parseInt(Constant.SENIORS_STARTING_FROM);

                        if (seniorList.isEmpty()) {
                            for (int j = cut_off; j <= 12; j++) {

                                seniorList.add(String.valueOf(j));
                            }
                        }

                        //for both juniors and seniors
                        if (Constant.TEST_APPLICABLE.equals("3")) {
                            //santosh
                            if (Constant.FINAL_POSITION.equals("1")) {
                                double milimeters3 = milimeters2 - milimeters;
                                if (milimeters2 > milimeters) {
                                    showSaveDialog(milimeters3, DateTime, deviceDateTime);
                                } else
                                    Toast.makeText(getApplicationContext(), R.string.validate_net_reading, Toast.LENGTH_SHORT).show();
                            } else
                                showSaveDialog(milimeters, DateTime, deviceDateTime);
                            //
                        } else {

                            if (seniorList.contains(Constant.CLASS_ID)) {
                                //santosh
                                if (Constant.FINAL_POSITION.equals("1")) {
                                    double milimeters3 = milimeters2 - milimeters;
                                    if (milimeters2 > milimeters) {
                                        showSaveDialog(milimeters3, DateTime, deviceDateTime);
                                    } else
                                        Toast.makeText(getApplicationContext(), R.string.validate_net_reading, Toast.LENGTH_SHORT).show();
                                } else
                                    showSaveDialog(milimeters, DateTime, deviceDateTime);
                                //
                            } else {
                                Toast.makeText(getApplicationContext(), R.string.not_for_juniors, Toast.LENGTH_SHORT).show();
                            }
                        }


                    } else {
                        Toast.makeText(getApplicationContext(), R.string.please_scan, Toast.LENGTH_SHORT).show();
                    }


                }

                break;

            case R.id.sub_test_name_tv:

                boolean isFilled1 = CheckFields();
                if (isFilled1) {
                    showExitDialog1();
                } else {
                    emptyStudentData();
                    ClearFields();
                    Intent i1 = new Intent(DistanceActivity.this, ShowSubTestActivity.class);
                    startActivity(i1);
                    finish();
                }
                break;

            case R.id.scan_btn:

                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);
                else {
                    Intent i1 = new Intent(DistanceActivity.this, ScanActivity.class);
                    startActivity(i1);
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent i1 = new Intent(DistanceActivity.this, ScanActivity.class);
                startActivity(i1);
            } else
                Utility.showPermissionDialog(this, getString(R.string.accept_permission, "Camera"));

        }
    }

    private void showResetDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(DistanceActivity.this);
        alertDialog.setCancelable(false);
        alertDialog.setMessage("Are you sure you want to reset the data?");

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.alarm);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                mili_meter_et.setText("");
                cm_et.setText("");
                meter_et.setText("");
                mili_meter_et.setHint("00");
                cm_et.setHint("00");
                meter_et.setHint("00");
                //santosh
                mili_meter2_et.setText("");
                cm2_et.setText("");
                meter2_et.setText("");
                mili_meter2_et.setHint("00");
                cm2_et.setHint("00");
                meter2_et.setHint("00");
//
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

    private void showSaveDialog(final double milimeters, final String DateTime, final String deviceDateTime) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(DistanceActivity.this);
        alertDialog.setCancelable(false);
        alertDialog.setMessage("Are you sure you want to save the data?");

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.alarm);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                insertTransactionData(milimeters, DateTime, deviceDateTime);
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    private void insertTransactionData(double score, String date, String deviceDate) {
        try {
            model = new FitnessTestResultModel();
            model.setStudent_id(Integer.parseInt(Constant.STUDENT_ID));
            model.setCamp_id(Integer.parseInt(Constant.CAMP_ID));
            model.setTest_type_id(Integer.parseInt(Constant.SUB_TEST_ID));
            model.setTest_coordinator_id(Integer.parseInt(Constant.TEST_COORDINATOR_ID));
            Double score_double = Double.parseDouble(String.valueOf(score));
            model.setScore("" + score_double.intValue());
            model.setPercentile(0);
            model.setCreated_on(date);
            model.setCreated_by(Constant.TEST_COORDINATOR_ID);
            model.setLast_modified_by("");
            model.setLast_modified_on(date);
            model.setLatitude(lat);
            model.setLongitude(lng);
            model.setSubTestName(Constant.SUB_TEST_TYPE);
            model.setTestedOrNot(true);
            model.setSyncedOrNot(false);
            model.setDevice_date(deviceDate);
            model.setTestName(Constant.TEST_TYPE);

            HashMap<String, String> map = null;
            try {
                map = db.getParticularRow(getApplicationContext(), testTableName,
                        "student_id", Constant.STUDENT_ID, "test_type_id", Constant.SUB_TEST_ID);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (map.size() == 0) {
                db.insertTables(testTableName, model);

                // After entering into local DB check for net connectivity and sync to server
                if (connectionDetector.isConnectingToInternet()) {
                    CallTransactionAPI();
                } else
                    Toast.makeText(getApplicationContext(), R.string.data_saved_offline, Toast.LENGTH_SHORT).show();

                showDataSavedSuccessfullyDialog();


            } else {
                String testedORNot = map.get("tested");
                if (testedORNot.equalsIgnoreCase("true")) {
                    showUpdateDialog(score);
                } else if (testedORNot.equalsIgnoreCase("false")) {
                    db.deleteTransactionRow(DistanceActivity.this, testTableName, "student_id", Constant.STUDENT_ID,
                            "test_type_id", Constant.SUB_TEST_ID, "camp_id", Constant.CAMP_ID);
                    db.insertTables(testTableName, model);
                    showDataSavedSuccessfullyDialog();

                    if (connectionDetector.isConnectingToInternet()) {
                        CallTransactionAPI();
                    } else
                        Toast.makeText(getApplicationContext(), R.string.data_saved_offline, Toast.LENGTH_SHORT).show();
                }

            }
        } catch (Exception e) {
            System.out.println("Fixed Score Avctivity" + e.getMessage());
        }

    }

    private void showDataSavedSuccessfullyDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(DistanceActivity.this);
        alertDialog.setCancelable(false);
        //   alertDialog.setMessage("This Student has already given the test. Do you want to update the test result?");

        alertDialog.setMessage("The " + Constant.SUB_TEST_TYPE + " scores for " + Constant.STUDENT_NAME + "" +
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

    private void showUpdateDialog(double score) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(DistanceActivity.this);
        alertDialog.setCancelable(false);

        double current_distance = score / 10;

        double cm = distance;
        double mm = cm * 10;

        String previous_converted_distance = Utility.convertCentiMetreToMtCmMm(DistanceActivity.this, distance);
        String current_converted_distance = Utility.convertCentiMetreToMtCmMm(DistanceActivity.this, current_distance);

//        alertDialog.setMessage(Constant.STUDENT_NAME+" has already taken the "+Constant.SUB_TEST_TYPE+"" +
//                " on "+ device_date+"\n\n"+"Previous Score: " + previous_converted_distance+"\n\n"+"Current Score: " + current_converted_distance+"\n\n"+" Do you want to update the test result?");

        alertDialog.setMessage(Constant.STUDENT_NAME + " has already taken the " + Constant.SUB_TEST_TYPE + "" +
                "\n\n" + "Previous Score: " + previous_converted_distance + "\n\n" + "Current Score: " + current_converted_distance + "\n\n" + " Do you want to update the test result?");


        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
                db.deleteTransactionRow(DistanceActivity.this, testTableName, "student_id", Constant.STUDENT_ID,
                        "test_type_id", Constant.SUB_TEST_ID, "camp_id", Constant.CAMP_ID);
                db.insertTables(testTableName, model);
                showDataSavedSuccessfullyDialog();

                if (connectionDetector.isConnectingToInternet()) {
                    CallTransactionAPI();
                } else
                    Toast.makeText(getApplicationContext(), R.string.data_saved_offline, Toast.LENGTH_SHORT).show();

                // db.updateTables(testTableName, model,"student_id",Constant.STUDENT_ID,"test_type_id", Constant.SUB_TEST_ID);
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                ClearFields();
                emptyStudentData();
            }
        });
        alertDialog.show();
    }

    private void emptyStudentData() {

        Constant.STUDENT_NAME = "";
        Constant.STUDENT_CLASS = "";
        Constant.STUDENT_REGISTRATION_NO = "0";
        Constant.STUDENT_SECTION = "";
        Constant.STUDENT_DOB = "";
        Constant.STUDENT_GENDER = "";
        Constant.STUDENT_ID = "";
        //  Constant.CAMP_ID = "";
        Constant.CLASS_ID = "";

        score_tv.setVisibility(View.GONE);
        //santosh
        net_score_tv.setText("Net Score:");
//
        student_class_tv.setText("Class " + ", " + "Registration Number");
        student_name_tv.setText("Student Name");
    }

    private void CallTransactionAPI() {

        HashMap<String, String> map_result = db.getParticularRow(getApplicationContext(), testTableName,
                "student_id", Constant.STUDENT_ID, "test_type_id", Constant.SUB_TEST_ID);


        JSONArray jArray = new JSONArray();
        jObj = new JSONObject();

        try {
            jObj.put("Student_ID", map_result.get("student_id"));
            jObj.put("Camp_ID", "" + map_result.get("camp_id"));
            jObj.put("Test_Type_ID", "" + map_result.get("test_type_id"));
            jObj.put("Test_Coordinator_ID", "" + map_result.get("test_coordinator_id"));
            jObj.put("Score", "" + map_result.get("score"));
            jObj.put("Percentile", "" + map_result.get("percentile"));
            jObj.put("Created_By", "" + map_result.get("created_by"));
            jObj.put("Created_On", "" + map_result.get("created_on"));
            jObj.put("SyncDateTime", "");
            jObj.put("Longitude", "" + map_result.get("longitude"));
            jObj.put("Latitude", "" + map_result.get("latitude"));

            String dateStr = map_result.get("device_date");
            DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
            Date date = (Date) formatter.parse(dateStr);

            String Last_Modified_On = Constant.ConvertDateToString(date);

            jObj.put("Last_Modified_On", Last_Modified_On);

            jArray.put(jObj);

        } catch (Exception e1) {
            e1.printStackTrace();
        }

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("data", jArray.toString());
        map.put("AccessBy", "" + map_result.get("test_coordinator_id"));
        TransactionRequest transactionRequest = new TransactionRequest(this, map, this);
        transactionRequest.hitUserRequest();

    }

    private void ClearFields() {
        mili_meter_et.setText("");
        cm_et.setText("");
        meter_et.setText("");
        mili_meter_et.setHint("00");
        cm_et.setHint("00");
        meter_et.setHint("00");
        //santosh
        mili_meter2_et.setText("");
        cm2_et.setText("");
        meter2_et.setText("");
        mili_meter2_et.setHint("00");
        cm2_et.setHint("00");
        meter2_et.setHint("00");
        //
    }

    public void onBackPressed() {

        boolean isFilled = CheckFields();
        if (isFilled) {
            showExitDialog();
        } else {
            emptyStudentData();
            Intent i = new Intent(DistanceActivity.this, ShowSubTestActivity.class);
            startActivity(i);
            finish();
        }
        //moveTaskToBack(true);

    }

    private boolean CheckFields() {
        String milimeter = mili_meter_et.getText().toString().trim();
        String cm = cm_et.getText().toString().trim();
        String meter = meter_et.getText().toString().trim();

        if (milimeter.length() > 0 || cm.length() > 0 || meter.length() > 0) {
            return true;
        }

        return false;

    }

    private void showExitDialog2() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(DistanceActivity.this);
        alertDialog.setCancelable(false);

        alertDialog.setMessage(getResources().getString(R.string.exit_from_test));

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.alarm);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                emptyStudentData();
                dialog.cancel();
                Intent i1 = new Intent(DistanceActivity.this, TestActivity.class);
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


    private void showExitDialog1() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(DistanceActivity.this);
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
                Intent i1 = new Intent(DistanceActivity.this, ShowSubTestActivity.class);
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

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(DistanceActivity.this);
        alertDialog.setCancelable(false);

        alertDialog.setMessage(getResources().getString(R.string.exit_from_test));

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.alarm);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
                emptyStudentData();
                Intent i = new Intent(DistanceActivity.this, ShowSubTestActivity.class);
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
        try {
            return  Utility.hideKeyboard(DistanceActivity.this);
        } catch (Exception e) {
            Log.e("DISTANCEACTIVITY", e.getMessage());
        }

        return true;
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

                    db.deleteTransactionRow(this, testTableName, "student_id", "" + DistanceActivity.this.model.getStudent_id(),
                            "test_type_id", "" + DistanceActivity.this.model.getTest_type_id(), "camp_id", Constant.CAMP_ID);
//
                    this.model.setSyncedOrNot(true);
                    db.insertTables(testTableName, this.model);

                    Log.e("TimeActivity", "synced==> " + this.model.isSyncedOrNot());


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

    @Override
    protected void onPause() {
        // stop location updates (saves battery)
        // location.endUpdates();

        super.onPause();
    }

    //santosh
    private double validateEditFieldsAndSetValues() {


        String meter_txt = meter_et.getText().toString();
        String cm_txt = cm_et.getText().toString();
        String mili_meter_txt = mili_meter_et.getText().toString();

//santosh
        String meter2_txt = meter2_et.getText().toString();
        String cm2_txt = cm2_et.getText().toString();
        String mili_meter2_txt = mili_meter2_et.getText().toString();
        //

        int meter_int, cm_int, mili_meter_int, meter2_int, cm2_int, mili_meter2_int;


        if (meter_txt.length() > 0) {
            meter_int = Integer.parseInt(meter_txt);
        } else {
            meter_int = 0;
        }

        if (cm_txt.length() > 0) {
            cm_int = Integer.parseInt(cm_txt);
        } else {
            cm_int = 0;
        }

        if (mili_meter_txt.length() > 0) {
            mili_meter_int = Integer.parseInt(mili_meter_txt);
        } else {
            mili_meter_int = 0;
        }

        //santosh
        if (meter2_txt.length() > 0) {
            meter2_int = Integer.parseInt(meter2_txt);
        } else {
            meter2_int = 0;
        }

        if (cm2_txt.length() > 0) {
            cm2_int = Integer.parseInt(cm2_txt);
        } else {
            cm2_int = 0;
        }

        if (mili_meter2_txt.length() > 0) {
            mili_meter2_int = Integer.parseInt(mili_meter2_txt);
        } else {
            mili_meter2_int = 0;
        }

        double milimeters = Constant.COnvertToMilimeter(String.valueOf(meter_int),
                String.valueOf(cm_int), String.valueOf(mili_meter_int));
        Log.e(TAG, "milimeters=> " + milimeters);
        //Toast.makeText(getApplicationContext(),""+milimeters,Toast.LENGTH_LONG).show();
//santosh
        double milimeters2 = Constant.COnvertToMilimeter(String.valueOf(meter2_int),
                String.valueOf(cm2_int), String.valueOf(mili_meter2_int));
        Log.e(TAG, "milimeters2=> " + milimeters2);
        //
        return milimeters2 - milimeters;
    }
    //


}
