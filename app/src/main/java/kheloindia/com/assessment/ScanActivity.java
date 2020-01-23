package kheloindia.com.assessment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;


import kheloindia.com.assessment.model.StudentMasterModel;
import kheloindia.com.assessment.util.AppConfig;
import kheloindia.com.assessment.util.DBManager;
import kheloindia.com.assessment.util.ProgressDialogUtility;

import kheloindia.com.assessment.functions.Constant;
import github.nisrulz.qreader.QRDataListener;
import github.nisrulz.qreader.QREader;


/**
 * Created by PC10 on 05/15/2017.
 */

public class ScanActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int MY_REQUEST_CODE = 1;
    Toolbar toolbar;
    Button go_btn;
    //  BarcodeDetector barcodeDetector;
    //  CameraSource cameraSource;
    SurfaceView cameraView;
    private SurfaceHolder previewHolder = null;
    String TAG = "ScanActivity";
    Typeface font_regular, font_medium;
    EditText code_edt;
    TextView text3, change_test_tv;
    TextView test_name_tv, sub_test_name_tv;
    DBManager db;
    //  String previous_value = "";
    //  String current_value = "";
    int count = 0;
    private boolean inPreview = false;
    private boolean cameraConfigured = false;
    Context context;
    int width = 0, height = 0;
    private boolean isRunning = false;
    boolean ifAlreadyShowing = false;

    Spinner class_spinner, roll_no_spinner;
    ArrayList<Object> studentList;
    ArrayList<Object> rollNoList =new ArrayList<>();

    private ArrayList<String> class_ids = new ArrayList<String>();
    private ArrayList<String> class_spin_data = new ArrayList<String>();

    private ArrayList<HashMap<String, String>> roll_no_spin_data = new ArrayList<HashMap<String, String>>();

    String class_id = "0";
    //String roll_no = "0";
    String student_registration = "0";
    String current_class = "0";

    TextView student_name;

    Context ctx;
    SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scan);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        db = DBManager.getInstance();
        context = ScanActivity.this;
        init();
    }

    @Override
    protected void onResume() {

        Log.e(TAG, "On Resume");

        sub_test_name_tv.setText(Constant.SUB_TEST_TYPE);
        test_name_tv.setText(Constant.TEST_TYPE);

        //  Constant.classListTemp1.clear();
        //  Constant.classIdListTemp1.clear();

//        code_edt.setText("");
//        code_edt.setHint("Code Here");

        super.onResume();

        QREader.getInstance().start();
        isRunning = true;

    }

    @Override
    public void onPause() {

        Log.e(TAG, "On Pause");
        super.onPause();

        QREader.getInstance().stop();
        isRunning = false;
    }

    @Override
    protected void onDestroy() {

        Log.e(TAG, "On Destroy");

        super.onDestroy();

        QREader.getInstance().releaseAndCleanup();
        isRunning = false;
    }

    private void init() {

        ctx = ScanActivity.this;

        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        toolbar = (Toolbar) findViewById(R.id.toolbar);


        sub_test_name_tv = (TextView) toolbar.findViewById(R.id.sub_test_name_tv);
        test_name_tv = (TextView) toolbar.findViewById(R.id.test_name_tv);

        sub_test_name_tv.setText(Constant.SUB_TEST_TYPE);
        test_name_tv.setText(Constant.TEST_TYPE);


        go_btn = (Button) findViewById(R.id.go_btn);
        cameraView = (SurfaceView) findViewById(R.id.cameraView);
        code_edt = (EditText) findViewById(R.id.code_edt);
        text3 = (TextView) findViewById(R.id.text3);
        change_test_tv = (TextView) findViewById(R.id.change_test_tv);

        class_spinner = (Spinner) findViewById(R.id.class_spinner);
        roll_no_spinner = (Spinner) findViewById(R.id.roll_no_spinner);

        student_name = (TextView) findViewById(R.id.student_name);

        student_name.setVisibility(View.GONE);


        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        font_regular = Typeface.createFromAsset(getAssets(),
                "fonts/Barlow-Regular.ttf");
        font_medium = Typeface.createFromAsset(getAssets(),
                "fonts/Barlow-Medium.ttf");

        code_edt.setTypeface(font_medium);
        text3.setTypeface(font_regular);
        change_test_tv.setTypeface(font_regular);
        student_name.setTypeface(font_regular);
        change_test_tv.setTypeface(font_regular);
        test_name_tv.setTypeface(font_regular);
        sub_test_name_tv.setTypeface(font_regular);


        go_btn.setOnClickListener(this);
        change_test_tv.setOnClickListener(this);

        if (Constant.classList1.size() == 0 || Constant.classIdList1.size() == 0) {
            roll_no_spinner.setVisibility(View.GONE);
            new LoadClasses().execute();
        } else {
            // roll_no_spinner.setVisibility(View.GONE);

            if (Constant.STUDENT_CATEGORY_OLD.equals("") || Constant.STUDENT_CATEGORY_OLD.equals(Constant.STUDENT_CATEGORY)) {

                Log.e(TAG, "Constant.SUB_TEST_ID=> " + Constant.SUB_TEST_ID);
                List<String> class_ids = CheckForClassIDsInDB(Constant.SUB_TEST_ID);
                Log.e(TAG, "comma sep class_ids=> " + class_ids);

                ArrayList<String> requiredClassIDList = getSelectedClasses(Constant.classIdList1, class_ids);

                Constant.classIdListTemp1 = new ArrayList<String>();

                Constant.classIdListTemp1 = requiredClassIDList;

                Log.e(TAG, "Constant.classIdListTemp1=> " + Constant.classIdListTemp1);
                Log.e(TAG, "Constant.classListTemp1=> " + Constant.classListTemp1);


                ArrayAdapter class_spin_array =
                        new ArrayAdapter<String>(ScanActivity.this,
                                R.layout.spinner_item,
                                R.id.text1,
                                Constant.classListTemp1);

                class_spin_array.setDropDownViewResource(R.layout.spinner_dropdown_item);
                class_spinner.setAdapter(class_spin_array);
//                class_spinner.setSelection(Constant.SCAN_POSITION);

                /*roll_no_spinner.setVisibility(View.VISIBLE);

                ArrayAdapter roll_no_spin_array = new ArrayAdapter<String>(ScanActivity.this, R.layout.spinner_item, Constant.rollNoList);
                roll_no_spin_array.setDropDownViewResource(R.layout.spinner_dropdown_item);
                roll_no_spinner.setAdapter(roll_no_spin_array);*/

            } else {
                roll_no_spinner.setVisibility(View.GONE);
                new LoadClasses().execute();
            }


        }

        Constant.STUDENT_CATEGORY_OLD = Constant.STUDENT_CATEGORY;


        class_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    try {
                        rollNoList.clear();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        roll_no_spin_data.clear();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    code_edt.setText("");
                    student_name.setText("");
                    student_name.setVisibility(View.GONE);
                  /*   Constant.SCAN_CLASS_ID = class_id =  Constant.classIdList1.get(position);
                    Constant.SCAN_CURRENT_CLASS = current_class = Constant.classList1.get(position);*/
                    Constant.SCAN_CLASS_ID = class_id = Constant.classIdListTemp1.get(position);
                    Constant.SCAN_CURRENT_CLASS = current_class = Constant.classListTemp1.get(position);
                    new LoadRollNumbers().execute();
                } else {
                    code_edt.setText("");
                    student_name.setVisibility(View.GONE);
                    Constant.SCAN_CLASS_ID = class_id = "";
                    Constant.SCAN_CURRENT_CLASS = current_class = "";
                    roll_no_spinner.setVisibility(View.GONE);

                }
                //Constant.SCAN_POSITION = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        roll_no_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    //roll_no = roll_no_spin_data.get(position).get("roll_num");
                    student_registration = roll_no_spin_data.get(position).get("student_registration");


                    HashMap<String, String> map = null;
                    try {

                        map = db.getParticularRow(getApplicationContext(), DBManager.TBL_LP_STUDENT_MASTER,
                                "class_id", class_id, "student_registration_num", student_registration);
                        code_edt.setText(map.get("student_registration_num"));
                        student_name.setVisibility(View.VISIBLE);
                        //santosh
                        student_name.setText("Name: " + map.get("student_name") + "\n" + "DOB: " + Constant.DateConverter(map.get("dob")));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    //roll_no = "0";
                    student_registration = "0";
                    code_edt.setText("");
                    student_name.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        QREader.getInstance().setUpConfig(new QRDataListener() {
            @Override
            public void onDetected(final String data) {
                Log.e("QREader", "Value : " + data);
                code_edt.post(new Runnable() {   // Use the post method of the TextView
                    public void run() {

                        Log.e("ScanActivity", "barcode ==> " + data);
                        code_edt.setText(data);
                        String code_value = data;

                        //     current_value = code_edt.getText().toString();
                        //    if (!current_value.equalsIgnoreCase(previous_value)) {
                        /*MediaPlayer mPlayer = MediaPlayer.create(ScanActivity.this, R.raw.beep_sound);
                        mPlayer.start();*/
                        //       previous_value = current_value;
                        //    }
                        if (code_value.length() >= 1) {

//                            MediaPlayer mPlayer = MediaPlayer.create(ScanActivity.this, R.raw.beep_sound);
//                            mPlayer.start();


                            checkCOdeInDB(code_value);


                        } else {
                            Toast.makeText(getApplicationContext(), "Please enter code",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });

        QREader.getInstance().init(this, cameraView);



       /* barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {


                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                Log.e(TAG, "barcode size==> " + barcodes.size());


                if (barcodes.size() != 0) {

                    count++;

                    Log.e(TAG, "code==> " + barcodes.valueAt(0).displayValue);

                    for (int i=0;i<barcodes.size();i++) {

                        if(i==0) {

                            code_edt.post(new Runnable() {   // Use the post method of the TextView
                                public void run() {

                                    Log.e("ScanActivity", "barcode size==> " + barcodes.size());
                                    Log.e("ScanActivity", "barcode value==> " + barcodes.valueAt(0).displayValue);
                                    code_edt.setText(barcodes.valueAt(0).displayValue);
                                    String code_value = barcodes.valueAt(0).displayValue;

                               //     current_value = code_edt.getText().toString();
                                //    if (!current_value.equalsIgnoreCase(previous_value)) {
                                        MediaPlayer mPlayer = MediaPlayer.create(ScanActivity.this, R.raw.beep_sound);
                                        mPlayer.start();
                                 //       previous_value = current_value;
                                //    }
                                    if (code_value.length() >= 1) {
                                        checkCOdeInDB(code_value);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Please enter code",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                        } else {
                            Log.e(TAG,"i==> "+i);
                        }
                }
                }

            }
        });*/


    }

    private ArrayList<String> getSelectedClasses(ArrayList<String> classListIDs1, List<String> requiredClassIds) {
        ArrayList<String> tempList = null;
        try {
            tempList = new ArrayList<>();
            int size = classListIDs1.size();

            Constant.classListTemp1 = new ArrayList<String>();

            Log.e(TAG, "for loop****** classListIDs1****>> " + classListIDs1);
            Log.e(TAG, "for loop****** requiredClassIds****>> " + requiredClassIds);

            tempList.add("0");
            Constant.classListTemp1.add("Class");

            for (int i = 0; i < size; i++) {

                if (requiredClassIds.contains(classListIDs1.get(i))) {


                    if (tempList.contains(classListIDs1.get(i))) {
                        tempList.add(classListIDs1.get(i));
                        Constant.classListTemp1.add(Constant.classList1.get(i));
                    } else {
                        tempList.add(classListIDs1.get(i));
                        Constant.classListTemp1.add(Constant.classList1.get(i));
                    }

                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return tempList;


    }

    private List<String> CheckForClassIDsInDB(String subTestId) {
        List<String> class_ids = null;
        HashMap<String, String> map = null;
        try {
            map = db.getParticularRow(getApplicationContext(), DBManager.TBL_LP_CAMP_TEST_MAPPING,
                    "camp_id", Constant.CAMP_ID, "test_type_id", subTestId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (map.size() > 0) {
            String class_id = map.get("class_id");
            class_ids = Arrays.asList(class_id.split("\\s*,\\s*"));

            return class_ids;
        }
        return class_ids;
    }

    private void checkCOdeInDB(String code) {

//        try {
//            String subString = code.substring(2);
//            Log.e("ScanActivity", "whole code==> " + code);
//            Log.e("ScanActivity", "SCHOOL_ID==> " + Constant.SCHOOL_ID);
//            Log.e("ScanActivity", "student code==> " + subString);
//            Constant.STUDENT_ROLL_NO = subString;
//        } catch(Exception e){
//            e.printStackTrace();
//        }

        HashMap<String, String> map = null;
        try {

            map = db.getParticularRow(this, DBManager.TBL_LP_STUDENT_MASTER, "current_school_id", Constant.SCHOOL_ID, "student_registration_num", code);
            //map = db.getParticularRow(this, DBManager.TBL_LP_STUDENT_MASTER, "current_school_id", Constant.SCHOOL_ID, "student_registration_num", Constant.STUDENT_ROLL_NO);
            //map = db.getParticularRow(this, DBManager.TBL_LP_STUDENT_MASTER, "student_registration_num",code,"","");
        } catch (Exception e) {
            code_edt.setText("");
            code_edt.setHint("Code Here");
            Toast.makeText(getApplicationContext(), "Code doest not exist in database.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        try {


            List<String> class_ids = CheckForClassIDsInDB(Constant.SUB_TEST_ID);
            if (map != null && map.size() > 0) {
                if (class_ids.contains(map.get("class_id"))) {
                    // barcodeDetector.release();

//            MediaPlayer mPlayer = MediaPlayer.create(ScanActivity.this, R.raw.gun_sound);
//            mPlayer.start();

                    QREader.getInstance().stop();
                    isRunning = false;

                    Constant.STUDENT_NAME = map.get("student_name");
                    Constant.STUDENT_CLASS = map.get("current_class");
                    Constant.STUDENT_SECTION = map.get("section");
                    Constant.STUDENT_DOB = map.get("dob");
                    Constant.STUDENT_GENDER = map.get("gender");
                    Constant.STUDENT_ID = map.get("student_id");
                    Constant.CAMP_ID = map.get("camp_id");
                    Constant.CLASS_ID = map.get("class_id");
                    Constant.STUDENT_REGISTRATION_NO = map.get("student_registration_num");

                    SharedPreferences.Editor e = sp.edit();
                    e.putString("camp_id", Constant.CAMP_ID);
                    e.commit();

                    finish();

//            Log.e(TAG,"Constant.STUDENT_NAME==> "+Constant.STUDENT_NAME);
//            Log.e(TAG,"Constant.STUDENT_CLASS==> "+Constant.STUDENT_CLASS);
//            Log.e(TAG,"Constant.STUDENT_SECTION==> "+Constant.STUDENT_SECTION);
//            Log.e(TAG,"Constant.STUDENT_DOB==> "+Constant.STUDENT_DOB);
//            Log.e(TAG,"Constant.STUDENT_GENDER==> "+Constant.STUDENT_GENDER);
//            Log.e(TAG,"Constant.STUDENT_ID==> "+Constant.STUDENT_ID);
//            Log.e(TAG,"Constant.CAMP_ID==> "+Constant.CAMP_ID);
//
//
//            checkForTestANdNavigate();
                } else {
                    if (!ifAlreadyShowing) {
                        ifAlreadyShowing = true;
                        showDialog("This test is not valid for this student! Please check for juniors/seniors");
                    }
                }
            } else {
                if (!ifAlreadyShowing) {
                    ifAlreadyShowing = true;
                    showDialog("Code does not exist in database.");
                }

            }
        } catch (Exception e) {
            System.out.println("SCAN ACTIVITY " + e.getMessage());
        }

    }

    @Override
    public void onClick(View v) {

        if (v == go_btn) {
            if (code_edt.getText().toString().length() >= 1) {
                String code = code_edt.getText().toString().trim();

                checkCOdeInDB(code);
                SharedPreferences.Editor e = sp.edit();
                e.putString("backclick", "2");
                e.commit();

            } else {
                Toast.makeText(getApplicationContext(), "Please enter code", Toast.LENGTH_SHORT).show();
            }
        } else if (v == change_test_tv) {
            Intent i = new Intent(ScanActivity.this, ShowSubTestActivity.class);
            startActivity(i);
            finish();
        }

    }

    private void checkForTestANdNavigate() {

        Log.e(TAG, "score_measurement==> " + Constant.SCORE_MEASUREMENT);

        db.createNamedTable(this, db.TBL_LP_FITNESS_TEST_RESULT);


        if (Constant.SCORE_UNIT.equalsIgnoreCase("msec")) {
           /* Intent i = new Intent(ScanActivity.this, TimeActivity.class);
            startActivity(i);*/
            finish();
           /*if(class_ids.contains(Constant.CLASS_ID)){
               finish();
           } else {
               Toast.makeText(getApplicationContext(),"You cannot take this test for junior category", Toast.LENGTH_LONG).show();
               finish();
               startActivity(getIntent());
           }*/

        } else if (Constant.SCORE_UNIT.equalsIgnoreCase("mm")) {
           /* Intent i = new Intent(ScanActivity.this, DistanceActivity.class);
            startActivity(i);*/
            finish();
            /*if(class_ids.contains(Constant.CLASS_ID)){
                finish();
            } else {
                Toast.makeText(getApplicationContext(),"You cannot take this test for junior category", Toast.LENGTH_LONG).show();
            }*/
        } else if (Constant.SCORE_UNIT.equalsIgnoreCase("gram")) {
         /*   Intent i = new Intent(ScanActivity.this, WeightActivity.class);
            startActivity(i);*/
            finish();
            /*if(class_ids.contains(Constant.CLASS_ID)){
                finish();
            } else {
                Toast.makeText(getApplicationContext(),"You cannot take this test for junior category", Toast.LENGTH_LONG).show();
            }*/
        } else if (Constant.SCORE_UNIT.equalsIgnoreCase("number")) {
           /* Intent i = new Intent(ScanActivity.this, FixedScoreActivity.class);
            startActivity(i);*/
            finish();
           /* if(class_ids.contains(Constant.CLASS_ID)){
                finish();
            } else {
                Toast.makeText(getApplicationContext(),"You cannot take this test for junior category", Toast.LENGTH_LONG).show();
            }*/
        } else if (Constant.SCORE_UNIT.equalsIgnoreCase("skill")) {
            finish();
          /*  if(class_ids.contains(Constant.CLASS_ID)){
                finish();
            } else {
                Toast.makeText(getApplicationContext(),"You cannot take this test for senior category", Toast.LENGTH_LONG).show();
            }*/
        } else {
            finish();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        System.out.println("BACK PRESSED 1");
        onBackPressed();
        return true;
    }


    public void onBackPressed() {
        System.out.println("BACK PRESSED 2");
        SharedPreferences.Editor e = sp.edit();
        e.putString("backclick", "1");
        e.commit();
        //  super.onBackPressed();
        // moveTaskToBack(true);
      /*  Intent i = new Intent(ScanActivity.this, ShowSubTestActivity.class);
        startActivity(i);*/
        finish();

        /*if (Constant.SCORE_UNIT.equalsIgnoreCase("msec")) {
            Intent i = new Intent(ScanActivity.this, TimeActivity.class);
            startActivity(i);
            finish();
        } else if (Constant.SCORE_UNIT.equalsIgnoreCase("mm")) {
            Intent i = new Intent(ScanActivity.this, DistanceActivity.class);
            startActivity(i);
            finish();
        } else if (Constant.SCORE_UNIT.equalsIgnoreCase("gram")) {
            Intent i = new Intent(ScanActivity.this, WeightActivity.class);
            startActivity(i);
            finish();
        }  else if (Constant.SCORE_UNIT.equalsIgnoreCase("number")) {
            Intent i = new Intent(ScanActivity.this, FixedScoreActivity.class);
            startActivity(i);
            finish();
        }*/

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                View view = getCurrentFocus();
                if (view != null)
                    imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {
            Log.e("SCANACTIVITY", e.getMessage());
        }

        return true;
    }

    private void showDialog(String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        ifAlreadyShowing = false;
                        // onPause();
                       /* finish();
                        Intent i = new Intent(ScanActivity.this, ScanActivity.class);
                        startActivity(i);*/

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public class LoadClasses extends AsyncTask<Void, Void, Void> {
        //ProgressDialog progressDialog;
        private final ProgressDialogUtility dialogUtility = new ProgressDialogUtility(ScanActivity.this);
        private String completed;

        @Override
        protected void onPreExecute() {
//            this.dialogUtility.setMessage("...");
            dialogUtility.showProgressDialog();
            /*progressDialog = ProgressDialog.show(ctx,
                    "Loading",
                    "please wait...");*/
        }

        protected Void doInBackground(Void... params) {
            Log.e(TAG, "selected school id==> " + Constant.SCHOOL_ID);
            studentList = DBManager.getInstance().getAllTableData(ScanActivity.this, DBManager.TBL_LP_STUDENT_MASTER, AppConfig.CAMP_ID, sp.getString(AppConfig.CAMP_ID, ""), "", "");


            class_spin_data.add("Class");
            //   class_spin_data.add(getString(R.string.all));
            class_ids.add("0");
            if (studentList.size() == 0) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ScanActivity.this, "No data available in database.", Toast.LENGTH_LONG).show();
                    }
                });

            } else {

                for (Object o : studentList) {
                    StudentMasterModel studentModel = (StudentMasterModel) o;
                    /*if (!class_spin_data.contains(studentModel.getCurrent_class())) {
                        class_spin_data.add(studentModel.getCurrent_class());
                        class_ids.add(studentModel.getClass_id());

                    }*/

                    if (Constant.STUDENT_CATEGORY.equalsIgnoreCase("senior")) {
                        //santosh

                        if (!(studentModel.getClass_id().charAt(0) >= 'A' && studentModel.getClass_id().charAt(0) <= 'Z') && Integer.parseInt(studentModel.getClass_id()) > 3) {

                            if (!class_spin_data.contains(studentModel.getCurrent_class())) {
                                class_spin_data.add(studentModel.getCurrent_class());
                                class_ids.add(studentModel.getClass_id());

                            }
                        }


                    } else if (Constant.STUDENT_CATEGORY.equalsIgnoreCase("junior")) {
                        //santosh
                        if ((studentModel.getClass_id().charAt(0) >= 'A' && studentModel.getClass_id().charAt(0) <= 'Z') || Integer.parseInt(studentModel.getClass_id()) <= 3) {

                            if (!class_spin_data.contains(studentModel.getCurrent_class())) {

                                class_spin_data.add(studentModel.getCurrent_class());
                                class_ids.add(studentModel.getClass_id());

                            }
                        }
                    } else if (Constant.STUDENT_CATEGORY.equalsIgnoreCase("senior_junior")) {

                        Log.e(TAG, "student category==> senior_junior");

                        if (!class_spin_data.contains(studentModel.getCurrent_class())) {

                            class_spin_data.add(studentModel.getCurrent_class());
                            class_ids.add(studentModel.getClass_id());

                        }
                    }


                }

            }
            return null;
        }

        protected void onPostExecute(Void result) {
            this.dialogUtility.dismissProgressDialog();

            //   progressDialog.dismiss();

            Constant.classList1 = class_spin_data;
            Constant.classIdList1 = class_ids;

            Log.e(TAG, "classList1==> " + Constant.classList1);
            Log.e(TAG, "classIdList1==> " + Constant.classIdList1);


            Log.e(TAG, "Constant.SUB_TEST_ID=> " + Constant.SUB_TEST_ID);
            List<String> class_ids = CheckForClassIDsInDB(Constant.SUB_TEST_ID);
            Log.e(TAG, "comma sep class_ids=> " + class_ids);


            ArrayList<String> requiredClassIDList = getSelectedClasses(Constant.classIdList1, class_ids);

            Log.e(TAG, "comma sep class_ids=> " + class_ids);

            Constant.classIdListTemp1 = new ArrayList<String>();
            Constant.classIdListTemp1 = requiredClassIDList;

            Log.e(TAG, "classIdListTemp1=> " + Constant.classIdListTemp1);
            Log.e(TAG, "classListTemp1=> " + Constant.classListTemp1);


            ArrayAdapter class_spin_array = new ArrayAdapter<String>(ScanActivity.this, R.layout.spinner_item, Constant.classListTemp1);
            //ArrayAdapter class_spin_array = new ArrayAdapter<String>(ScanActivity.this, R.layout.spinner_item, Constant.classList1);
            class_spin_array.setDropDownViewResource(R.layout.spinner_dropdown_item);
            class_spinner.setAdapter(class_spin_array);
        }

    }

    public class LoadRollNumbers extends AsyncTask<Void, Void, Void> {
        private final ProgressDialogUtility dialogUtility = new ProgressDialogUtility(ScanActivity.this);
        private String completed;

        @Override
        protected void onPreExecute() {
//            this.dialogUtility.setMessage("...");
            this.dialogUtility.showProgressDialog();
        }

        protected Void doInBackground(Void... params) {

            roll_no_spin_data.clear();
            Constant.rollNoList.clear();

            rollNoList = DBManager.getInstance().getAllTableDataTest(ScanActivity.this, DBManager.TBL_LP_STUDENT_MASTER, AppConfig.CAMP_ID, sp.getString(AppConfig.CAMP_ID, ""), "current_class", current_class);

            HashMap<String, String> map = new HashMap<>();
            map.put("roll_num", "Roll Number");
            map.put("student_name", "Student Name");
System.out.println("STUDENT "+map);
            roll_no_spin_data.add(map);

            //   class_spin_data.add(getString(R.string.all));
            if (rollNoList.size() == 0) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ScanActivity.this, "No data available in database.", Toast.LENGTH_LONG).show();
                    }
                });

            } else {



                //  for (Object o : rollNoList) {

                for (int i = 0; i < rollNoList.size(); i++) {
                    StudentMasterModel studentModel = (StudentMasterModel) rollNoList.get(i);
                    HashMap<String, String> map1 = new HashMap<>();
                    if (!Constant.checked_incomp) {

                        map1.put("roll_num", studentModel.getCurrent_roll_num());
                        map1.put("student_name", studentModel.getStudent_name());
                        map1.put("student_registration", studentModel.getStudent_registration_num());
                        roll_no_spin_data.add(map1);

                    } else if (Constant.checked_incomp && (Constant.STUDENT_CATEGORY.equalsIgnoreCase("senior") || Constant.STUDENT_CATEGORY.equalsIgnoreCase("senior_junior")) && DBManager.getInstance().getTotalCount(ScanActivity.this, DBManager.TBL_LP_FITNESS_TEST_RESULT, "test_type_id", Constant.SUB_TEST_ID, "student_id", "" + studentModel.getStudent_id(), "camp_id", Constant.CAMP_ID) == 0
                    ) {
                        map1.put("roll_num", studentModel.getCurrent_roll_num());
                        map1.put("student_name", studentModel.getStudent_name());
                        map1.put("student_registration", studentModel.getStudent_registration_num());
                        roll_no_spin_data.add(map1);
                    } else if (Constant.checked_incomp && (Constant.STUDENT_CATEGORY.equalsIgnoreCase("junior")) && DBManager.getInstance().getTotalCount(ScanActivity.this, DBManager.TBL_LP_FITNESS_SKILL_TEST_RESULT, "test_type_id", Constant.SUB_TEST_ID, "student_id", "" + studentModel.getStudent_id(), "camp_id", Constant.CAMP_ID) == 0
                    ) {
                        map1.put("roll_num", studentModel.getCurrent_roll_num());
                        map1.put("student_name", studentModel.getStudent_name());
                        map1.put("student_registration", studentModel.getStudent_registration_num());
                        roll_no_spin_data.add(map1);
                    }

                }

            }
            return null;
        }

        protected void onPostExecute(Void result) {
            this.dialogUtility.dismissProgressDialog();

            roll_no_spinner.setVisibility(View.VISIBLE);


            for (int i = 0; i < roll_no_spin_data.size(); i++) {
                Constant.rollNoList.add(roll_no_spin_data.get(i).get("student_name"));
            }

            roll_no_spinner.setVisibility(View.VISIBLE);
            ArrayAdapter roll_no_spin_array = new ArrayAdapter<String>(ScanActivity.this, R.layout.spinner_item, Constant.rollNoList);
            roll_no_spin_array.setDropDownViewResource(R.layout.spinner_dropdown_item);
            roll_no_spinner.setAdapter(roll_no_spin_array);
        }

    }
}