package kheloindia.com.assessment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import java.util.List;

import kheloindia.com.assessment.model.FitnessSkillTestResultModel;
import kheloindia.com.assessment.model.SkillTestTypeModel;
import kheloindia.com.assessment.model.SkillTransactionModel;
import kheloindia.com.assessment.util.ConnectionDetector;
import kheloindia.com.assessment.util.DBManager;
import kheloindia.com.assessment.util.ResponseListener;

import kheloindia.com.assessment.functions.Constant;
import kheloindia.com.assessment.functions.GPSTracker;
import kheloindia.com.assessment.util.Utility;
import kheloindia.com.assessment.webservice.OptionsRequest;
import kheloindia.com.assessment.webservice.TransactionSkillRequest;

/**
 * Created by PC10 on 13-Sep-17.
 */

public class OptionsActivity extends AppCompatActivity implements View.OnClickListener, ResponseListener {

    TextView test_name_tv, sub_test_name_tv, school_tv, student_name_tv, student_class_tv, score_tv;
    Button scan_btn, reset_btn, save_btn;
    ImageView dashboard_img, boy_or_girl_img;
    FloatingActionButton test_another_img;
    GPSTracker gps;
    DBManager db;
    ConnectionDetector connectionDetector;
    LinearLayout linear_layout;
    CheckBox checkbox;
    FitnessSkillTestResultModel model;
    //   private ProgressDialogUtility progressDialogUtility;
    double lat, lng;
    private Dialog dialog;
    JSONArray jArray;
    JSONObject jObj;
    String TAG = "OptionsActivity";

    boolean isDialogShown = false;

    ArrayList<String> current_score_list = new ArrayList<String>();
    ArrayList<Object> check_list;

    ArrayList<HashMap<String, String>> optionlist = new ArrayList<HashMap<String, String>>();


    @Override
    protected void onResume() {
        super.onResume();

        gps = new GPSTracker(getApplicationContext());
        gps.getmGoogleApiClient().connect();
        sub_test_name_tv.setText(Constant.SUB_TEST_TYPE);

        if (Constant.STUDENT_NAME.length() < 1) {
            student_name_tv.setText("Student Name");
        } else {
            student_name_tv.setText(Constant.STUDENT_NAME);
            //    checkForRecordExistence();
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

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_options);

        init();
    }

    private void init() {

        //  progressDialogUtility = new ProgressDialogUtility(this);

        gps = new GPSTracker(getApplicationContext());
        //location = new SimpleLocation(this);
        db = DBManager.getInstance();
        connectionDetector = new ConnectionDetector(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        test_name_tv = (TextView) toolbar.findViewById(R.id.test_name_tv);
        sub_test_name_tv = (TextView) toolbar.findViewById(R.id.sub_test_name_tv);
        school_tv = (TextView) findViewById(R.id.school_tv);
        student_name_tv = (TextView) findViewById(R.id.student_name_tv);
        student_class_tv = (TextView) findViewById(R.id.student_class_tv);
        scan_btn = (Button) findViewById(R.id.scan_btn);
        reset_btn = (Button) findViewById(R.id.reset_btn);
        save_btn = (Button) findViewById(R.id.save_btn);
        dashboard_img = (ImageView) toolbar.findViewById(R.id.dashboard_img);
        test_another_img = (FloatingActionButton) findViewById(R.id.test_another_img);
        boy_or_girl_img = (ImageView) findViewById(R.id.boy_or_girl_img);
        score_tv = (TextView) findViewById(R.id.score_tv);
        linear_layout = (LinearLayout) findViewById(R.id.linear_layout);


        toolbar.setTitle(Constant.TEST_TYPE);
        test_name_tv.setText(Constant.TEST_TYPE);
        sub_test_name_tv.setText(Constant.SUB_TEST_TYPE);
        school_tv.setText(Constant.SCHOOL_NAME);
        //  text_tv.setText("Enter "+Constant.SUB_TEST_TYPE+" Test Score");

        student_name_tv.setText(Constant.STUDENT_NAME);
//        student_class_tv.setText("Class: " + Constant.STUDENT_CLASS + ", " +
//                Constant.SCHOOL_ID + Constant.STUDENT_ROLL_NO);

        if (Constant.STUDENT_GENDER.equalsIgnoreCase("1")) {
            boy_or_girl_img.setImageResource(R.drawable.girl_blue_i);
        } else if (Constant.STUDENT_GENDER.equalsIgnoreCase("0")) {
            boy_or_girl_img.setImageResource(R.drawable.boy_i);
        }


        sub_test_name_tv.setOnClickListener(this);
        dashboard_img.setOnClickListener(this);
        test_another_img.setOnClickListener(this);
        reset_btn.setOnClickListener(this);
        save_btn.setOnClickListener(this);
        scan_btn.setOnClickListener(this);

        dashboard_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmptyStudentData();
                boolean isFilled = CheckFields();
                if (isFilled) {
                    showExitDialog2();
                } else {
                    EmptyStudentData();
                    Intent i1 = new Intent(OptionsActivity.this, TestActivity.class);
                    startActivity(i1);
                    finish();
                }
            }
        });

        //   getOptionsFromServer();

        getOptionsFromLocalDb();

    }

    private void showExitDialog2() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(OptionsActivity.this);
        alertDialog.setCancelable(false);

        alertDialog.setMessage(getResources().getString(R.string.exit_from_test));

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.alarm);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
                EmptyStudentData();
                Intent i1 = new Intent(OptionsActivity.this, TestActivity.class);
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

    private void getOptionsFromLocalDb() {

        final ArrayList<Object> objectArrayList = DBManager.getInstance().getAllTableData(this,
                DBManager.TBL_LP_SKILL_TEST_TYPE, "test_type_id", Constant.SUB_TEST_ID, "", "");


        for (int i = 0; i < objectArrayList.size(); i++) {

            SkillTestTypeModel model = (SkillTestTypeModel) objectArrayList.get(i);

            View layout1 = LayoutInflater.from(this).inflate(R.layout.options_layout, linear_layout, false);
            checkbox = (CheckBox) layout1.findViewById(R.id.checkbox);
//            final CheckBox checkBox = new CheckBox(this);
//            checkBox.setTag(i);
//            checkBox.setPadding(10,10,10,10);
            checkbox.setText(model.getItem_name());
//            LinearLayout.LayoutParams checkParams = new LinearLayout.LayoutParams(
//                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//           checkParams.setMargins(10, 10, 10, 10);
            //checkParams.gravity = Gravity.CENTER_VERTICAL;
            linear_layout.addView(layout1);


            final HashMap<String, String> map1 = new HashMap<String, String>();
            map1.put("CheckListItem_ID", "" + model.getChecklist_item_id());
            map1.put("Item_Name", model.getItem_name());
            map1.put("isChecked", "0");


            checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //  int j=  (Integer) checkBox.getTag();
//                    HashMap<String, String> map1=optionlist.get(j);
                    if (isChecked) {

                        map1.put("isChecked", "1");
                    } else {

                        map1.put("isChecked", "0");
                    }
                    //optionlist.set(j,map1);
                }
            });

            optionlist.add(map1);
        }


    }

    private void getOptionsFromServer() {

        connectionDetector = new ConnectionDetector(this);
        if (connectionDetector.isConnectingToInternet()) {

            //    db.dropNamedTable(this, db.TBL_LP_STUDENT_MASTER);
            //   db.createNamedTable(this, db.TBL_LP_STUDENT_MASTER);
            OptionsRequest optionRequest = new OptionsRequest(this, Constant.SUB_TEST_ID, Constant.TEST_ID, this);
            optionRequest.hitUserRequest();
        }
    }

    private void EmptyStudentData() {

        Constant.STUDENT_NAME = "";
        Constant.STUDENT_CLASS = "";
        Constant.STUDENT_REGISTRATION_NO = "0";
        Constant.STUDENT_SECTION = "";
        Constant.STUDENT_DOB = "";
        Constant.STUDENT_GENDER = "";
        Constant.STUDENT_ID = "";
        //   Constant.CAMP_ID = "";
        Constant.CLASS_ID = "";

        score_tv.setVisibility(View.GONE);

        student_class_tv.setText("Class " + ", " + "Registration Number");
        student_name_tv.setText("Student Name");
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.test_another_img:

                Intent i = new Intent(OptionsActivity.this, TestActivity.class);
                startActivity(i);
                finish();

                break;

            case R.id.reset_btn:

                boolean isFilled2 = CheckFields();
                if (isFilled2) {
                    showResetDialog();
                } else {
                    EmptyStudentData();
                    ClearFields();
                }

                break;

            case R.id.sub_test_name_tv:

                boolean isFilled = CheckFields();
                if (isFilled) {
                    showExitDialog1();
                } else {
                    Intent i1 = new Intent(OptionsActivity.this, ShowSubTestActivity.class);
                    startActivity(i1);
                    finish();
                }
                break;

            case R.id.scan_btn:

                Intent i2 = new Intent(OptionsActivity.this, ScanActivity.class);
                startActivity(i2);

                break;

            case R.id.save_btn:

                if (Constant.STUDENT_NAME.length() > 1) {
                    ArrayList<String> seniorList = new ArrayList<>();

                    int cut_off = Integer.parseInt(Constant.SENIORS_STARTING_FROM);

                    if (seniorList.isEmpty()) {
                        for (int j = cut_off; j <= 12; j++) {

                            seniorList.add(String.valueOf(j));
                        }
                    }

                    if (seniorList.contains(Constant.CLASS_ID)) {
                        Toast.makeText(getApplicationContext(), R.string.not_for_seniors, Toast.LENGTH_SHORT).show();
                    } else {
                        CheckForScore();

                    }


                } else {
                    Toast.makeText(getApplicationContext(), R.string.please_scan, Toast.LENGTH_SHORT).show();
                }

                break;

        }

    }

    private void showExitDialog1() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(OptionsActivity.this);

        alertDialog.setMessage(getResources().getString(R.string.exit_from_test));

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.alarm);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                EmptyStudentData();
                ClearFields();

                Constant.ISComingFromTestScreen = true;
                dialog.cancel();
                Intent i1 = new Intent(OptionsActivity.this, ShowSubTestActivity.class);
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

    private boolean CheckFields() {

        boolean isChecked = false;
        for (int i = 0; i < optionlist.size(); i++) {

            View mChild = linear_layout.getChildAt(i);

            //Replace R.id.checkbox with the id of CheckBox in your layout
            CheckBox mCheckBox = (CheckBox) mChild.findViewById(R.id.checkbox);
            // CheckBox mCheckBox = (CheckBox) mChild;

            isChecked = mCheckBox.isChecked();
            if (isChecked) {
                break;
            }
        }
        return isChecked;
    }

    private void showResetDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(OptionsActivity.this);
        alertDialog.setCancelable(false);
        alertDialog.setMessage("Are you sure you want to reset the data?");

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.alarm);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                EmptyStudentData();
                ClearFields();

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

    private void CheckForScore() {

        String score = "";
        String checklist_id = "";
        boolean isOne = false;

        // check for empty options

        for (int i = 0; i < optionlist.size(); i++) {

            score = optionlist.get(i).get("isChecked");
            checklist_id = optionlist.get(i).get("CheckListItem_ID");

            if (score.equals("1")) {
                isOne = true;
                break;
            }
        }

        //  String score = CheckedItems.toString().replaceAll("[\\[.\\].\\s+]", "");

        lat = gps.getLatitude();
        lng = gps.getLongitude();

        showSaveDialog();

//        if (isOne) {
//            showSaveDialog();
//        } else {
//            Toast.makeText(getApplicationContext(), "Please fill all the fields", Toast.LENGTH_LONG).show();
//        }


    }

    private void showSaveDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(OptionsActivity.this);

        alertDialog.setMessage("Are you sure you want to save the data?");

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.alarm);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();

                // *****************************************************************

                HashMap<String, String> check_update_map = null;
                try {
                    check_update_map = db.getParticularRow(getApplicationContext(), DBManager.TBL_LP_FITNESS_SKILL_TEST_RESULT,
                            "student_id", Constant.STUDENT_ID, "test_type_id", Constant.SUB_TEST_ID);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (check_update_map.size() == 0) {

                    String score = "";
                    String checklist_id = "";

                    for (int i = 0; i < optionlist.size(); i++) {

                        score = optionlist.get(i).get("isChecked");
                        checklist_id = optionlist.get(i).get("CheckListItem_ID");

                        lat = gps.getLatitude();
                        lng = gps.getLongitude();
                        String DateTime = Constant.getDateTimeServer();
                        String deviceDateTime = Constant.getDateTime();

                        Log.d(TAG, " for " + i + " score=> " + score);
                        Log.d(TAG, " for " + i + " checklist_id=> " + checklist_id);
                        // if (score.equals("1")) {
                        Log.d(TAG, " for " + i + " insertTransactionData");
                        insertTransactionData(score, checklist_id, DateTime, deviceDateTime);
                        //  }
                    }


                    showDataSavedSuccessfullyDialog();

                    if (connectionDetector.isConnectingToInternet()) {
                        CallTransactionAPI();
                    } else
                        Toast.makeText(getApplicationContext(), R.string.data_saved_offline, Toast.LENGTH_SHORT).show();
                } else {
                    current_score_list = new ArrayList<String>();
                    for (int i = 0; i < optionlist.size(); i++) {

                        current_score_list.add(optionlist.get(i).get("isChecked"));

                    }

                    showUpdateDialog();

                }

                // *************************************************************


                // **********************************************************************
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    public void onBackPressed() {
        EmptyStudentData();
        boolean isFilled = CheckFields();
        if (isFilled) {
            showExitDialog();
        } else {
            EmptyStudentData();
            Intent i = new Intent(OptionsActivity.this, ShowSubTestActivity.class);
            startActivity(i);
            finish();
        }
        //moveTaskToBack(true);
    }

    private void showExitDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(OptionsActivity.this);
        alertDialog.setCancelable(false);
        alertDialog.setMessage(getResources().getString(R.string.exit_from_test));

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.alarm);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
                EmptyStudentData();
                Intent i = new Intent(OptionsActivity.this, ShowSubTestActivity.class);
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

            Utility.hideKeyboard(OptionsActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    private void insertTransactionData(String score, String checklistID, String date, String device_date) {


        model = new FitnessSkillTestResultModel();
        model.setStudent_id(Integer.parseInt(Constant.STUDENT_ID));
        model.setCamp_id(Integer.parseInt(Constant.CAMP_ID));
        model.setTest_type_id(Integer.parseInt(Constant.SUB_TEST_ID));
        model.setCoordinator_id(Integer.parseInt(Constant.TEST_COORDINATOR_ID));
        model.setScore(score);
        model.setSkill_score_id(0);
        model.setCreated_on(date);
        model.setLast_modified_on(date);
        model.setLatitude("" + lat);
        model.setLongitude("" + lng);
        model.setChecklist_item_id(Integer.parseInt(checklistID));
        model.setLast_modified_on(date);
        model.setTested(true);
        model.setSynced(false);
        model.setDevice_date(device_date);

       /* Log.e("OptionsActivity", "setStudent_id==> " + Integer.parseInt(Constant.STUDENT_ID));
        Log.e("OptionsActivity", "setCamp_id==> " + Integer.parseInt(Constant.CAMP_ID));
        Log.e("OptionsActivity", "setTest_type_id==> " + Integer.parseInt(Constant.SUB_TEST_ID));
        Log.e("OptionsActivity", "setCoordinator_id==> " + Integer.parseInt(Constant.TEST_COORDINATOR_ID));
        Log.e("OptionsActivity", "setScore==> " + score);
        Log.e("OptionsActivity", "setSkill_score_id==> " + 0);
        Log.e("OptionsActivity", "setCreated_on==> " + date);
        Log.e("OptionsActivity", "setLatitude==> " + lat);
        Log.e("OptionsActivity", "setLongitude==> " + lng);
        Log.e("OptionsActivity", "setChecklist_item_id==> " + Integer.parseInt(checklistID));
        Log.e("OptionsActivity", "setLast_modified_on==> " + date);
        Log.e("OptionsActivity", "setDevice_date==> " + device_date);*/


        HashMap<String, String> map = null;
        try {
            map = db.getParticularRow(getApplicationContext(), DBManager.TBL_LP_FITNESS_SKILL_TEST_RESULT,
                    "student_id", Constant.STUDENT_ID, "checklist_item_id", checklistID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (map.size() == 0) {
            db.insertTables(db.TBL_LP_FITNESS_SKILL_TEST_RESULT, model);

        } else {
            String testedORNot = map.get("tested");
            if (testedORNot.equalsIgnoreCase("true")) {
                // showUpdateDialog();

                Log.e(TAG, "testedORNot==> " + testedORNot);
                Log.d(TAG, "delete and then insert");

                //     Toast.makeText(getApplicationContext(), "record exist already", Toast.LENGTH_SHORT).show();

                db.deleteTransactionRow(OptionsActivity.this, db.TBL_LP_FITNESS_SKILL_TEST_RESULT, "student_id", Constant.STUDENT_ID,
                        "checklist_item_id", checklistID, "camp_id", Constant.CAMP_ID);
                db.insertTables(db.TBL_LP_FITNESS_SKILL_TEST_RESULT, model);


            } else if (testedORNot.equalsIgnoreCase("false")) {

                Log.e(TAG, "testedORNot==> " + testedORNot);
                Log.d(TAG, "delete and then insert");

                db.deleteTransactionRow(OptionsActivity.this, db.TBL_LP_FITNESS_SKILL_TEST_RESULT, "student_id", Constant.STUDENT_ID,
                        "checklist_item_id", checklistID, "camp_id", Constant.CAMP_ID);
                db.insertTables(db.TBL_LP_FITNESS_SKILL_TEST_RESULT, model);

            }

        }
    }

    private void showUpdateDialog() {
        dialog = new Dialog(OptionsActivity.this);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //  dialog.getWindow().setFormat(PixelFormat.TRANSLUCENT);

        Drawable d = new ColorDrawable(Color.BLACK);
        d.setAlpha(0);
        dialog.getWindow().setBackgroundDrawable(d);

        dialog.setContentView(R.layout.option_dialog);


        TextView text;
        Button yes_btn, no_btn;
        LinearLayout option_layout;

        CheckBox checkbox_old, checkbox_new;
        TextView option_tv;

        text = (TextView) dialog.findViewById(R.id.text);
        yes_btn = (Button) dialog.findViewById(R.id.yes_btn);
        no_btn = (Button) dialog.findViewById(R.id.no_btn);
        option_layout = (LinearLayout) dialog.findViewById(R.id.option_layout);
        checkbox_old = (CheckBox) dialog.findViewById(R.id.checkbox_old);
        checkbox_new = (CheckBox) dialog.findViewById(R.id.checkbox_new);

        checkbox_old.setButtonDrawable(R.drawable.red_checkbox);
        checkbox_new.setButtonDrawable(R.drawable.green_checkbox);

        text.setText(Constant.STUDENT_NAME + " has already taken the " + Constant.SUB_TEST_TYPE + ". Do you want to update the test result?");


        final ArrayList<Object> objectArrayList = DBManager.getInstance().getAllTableData(this,
                DBManager.TBL_LP_SKILL_TEST_TYPE, "test_type_id", Constant.SUB_TEST_ID, "", "");


        for (int i = 0; i < objectArrayList.size(); i++) {

            SkillTestTypeModel model = (SkillTestTypeModel) objectArrayList.get(i);

            View layout1 = LayoutInflater.from(this).inflate(R.layout.old_new_checkbox, linear_layout, false);
            checkbox_old = (CheckBox) layout1.findViewById(R.id.checkbox_old);
            checkbox_new = (CheckBox) layout1.findViewById(R.id.checkbox_new);
            option_tv = (TextView) layout1.findViewById(R.id.option_tv);

            option_tv.setText(model.getItem_name());

            option_layout.addView(layout1);

            // ************** old check ******************************
            HashMap<String, String> check_list_exixtence = new HashMap<String, String>();
            check_list_exixtence = db.getParticularCheckListRow(getApplicationContext(), DBManager.TBL_LP_FITNESS_SKILL_TEST_RESULT,
                    "student_id", Constant.STUDENT_ID, "test_type_id", Constant.SUB_TEST_ID, "checklist_item_id", optionlist.get(i).get("CheckListItem_ID"));

            String id = optionlist.get(i).get("CheckListItem_ID");

            try {
                String score = check_list_exixtence.get("score");

                View mChild = option_layout.getChildAt(i);

                //Replace R.id.checkbox with the id of CheckBox in your layout
                CheckBox mCheckBox_old = (CheckBox) mChild.findViewById(R.id.checkbox_old);
                // CheckBox mCheckBox = (CheckBox) mChild;
                if (score.equalsIgnoreCase("1")) {
                    mCheckBox_old.setChecked(true);
                    mCheckBox_old.setEnabled(false);
                    mCheckBox_old.setButtonDrawable(R.drawable.red_checkbox);

                } else {
                    mCheckBox_old.setChecked(false);
                    mCheckBox_old.setEnabled(false);
                    mCheckBox_old.setButtonDrawable(R.drawable.grey_checkbox);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // *****************************new check **********************************

            try {
                String score = current_score_list.get(i);

                View mChild = option_layout.getChildAt(i);

                //Replace R.id.checkbox with the id of CheckBox in your layout
                CheckBox mCheckBox_new = (CheckBox) mChild.findViewById(R.id.checkbox_new);
                //   CheckBox mCheckBox = (CheckBox) mChild;
                if (score.equalsIgnoreCase("1")) {
                    mCheckBox_new.setChecked(true);
                    mCheckBox_new.setEnabled(false);

                    mCheckBox_new.setButtonDrawable(R.drawable.green_checkbox);

                } else {
                    mCheckBox_new.setChecked(false);
                    mCheckBox_new.setEnabled(false);

                    mCheckBox_new.setButtonDrawable(R.drawable.grey_checkbox);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        no_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        yes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                String score = "";
                String checklist_id = "";

                for (int i = 0; i < optionlist.size(); i++) {

                    score = optionlist.get(i).get("isChecked");
                    checklist_id = optionlist.get(i).get("CheckListItem_ID");

                    lat = gps.getLatitude();
                    lng = gps.getLongitude();
                    String DateTime = Constant.getDateTimeServer();
                    String deviceDateTime = Constant.getDateTime();

                    Log.d(TAG, " for " + i + " score=> " + score);
                    Log.d(TAG, " for " + i + " checklist_id=> " + checklist_id);
                    // if (score.equals("1")) {
                    Log.d(TAG, " for " + i + " insertTransactionData");
                    insertTransactionData(score, checklist_id, DateTime, deviceDateTime);
                    //  }
                }


                showDataSavedSuccessfullyDialog();

                if (connectionDetector.isConnectingToInternet()) {
                    CallTransactionAPI();
                } else
                    Toast.makeText(getApplicationContext(), R.string.data_saved_offline, Toast.LENGTH_SHORT).show();
            }
        });


        dialog.show();
    }

    private void CallTransactionAPI() {


        check_list = db.getAllTableData(getApplicationContext(), DBManager.TBL_LP_FITNESS_SKILL_TEST_RESULT,
                "student_id", Constant.STUDENT_ID, "test_type_id", Constant.SUB_TEST_ID);


        Log.e(TAG, "checklist size ===> " + check_list.size());

          /*FK_Student_ID,
    FK_Camp_ID,
    FK_Test_Type_ID,
    FK_CheckListItem_ID,
    FK_Test_Coordinator_ID,
    Score,
    Created_On,
    Last_Modified_On,
    Longitude,
    Latitude*/

        List<String> score_list = new ArrayList<String>();
        List<String> checklistid_list = new ArrayList<String>();

        String student_id_Str = "", camp_id_str = "", test_type_id_str = "", coordinator_id_str = "",
                lat_Str = "", lng_str = "", device_date_str = "", created_on_str = "";

        for (Object o : check_list) {
            FitnessSkillTestResultModel temp = (FitnessSkillTestResultModel) o;

            score_list.add(temp.getScore());
            checklistid_list.add("" + temp.getChecklist_item_id());

            student_id_Str = "" + temp.getStudent_id();
            camp_id_str = "" + temp.getCamp_id();
            test_type_id_str = "" + temp.getTest_type_id();
            coordinator_id_str = "" + temp.getCoordinator_id();
            lat_Str = temp.getLatitude();
            lng_str = temp.getLongitude();
            device_date_str = "" + temp.getDevice_date();


        }

        String score_str = score_list.toString().replace("[", "").replace("]", "").replace(", ", ",");
        String checkid_str = checklistid_list.toString().replace("[", "").replace("]", "").replace(", ", ",");


        Log.e(TAG, "score_str==> " + score_str);
        Log.e(TAG, "checkid_str==> " + checkid_str);

        try {
            jObj = new JSONObject();
            jArray = new JSONArray();
            jObj.put("FK_Student_ID", student_id_Str);
            jObj.put("FK_Camp_ID", camp_id_str);
            jObj.put("FK_Test_Type_ID", test_type_id_str);
            jObj.put("FK_Test_Coordinator_ID", coordinator_id_str);
            jObj.put("Score", score_str);
            // jObj.put("Created_On", "" + temp.getCreated_on());
            // jObj.put("Last_Modified_On", temp.getDevice_date());
            jObj.put("Longitude", lng_str);
            jObj.put("Latitude", lat_Str);
            jObj.put("FK_CheckListItem_ID", checkid_str);

            String dateStr = device_date_str;
            DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
            Date date = (Date) formatter.parse(dateStr);
            String Last_Modified_On = Constant.ConvertDateToString(date);


            jObj.put("Last_Modified_On", Last_Modified_On);


            String dateStr1 = created_on_str;
            DateFormat formatter1 = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
            Date date1 = (Date) formatter1.parse(dateStr);
            String created_on = Constant.ConvertDateToString(date1);


            jObj.put("Created_On", created_on);

            jArray.put(jObj);
        } catch (Exception e) {
            e.printStackTrace();
        }


        //    Toast.makeText(getApplicationContext(),jArray.toString(),Toast.LENGTH_LONG).show();

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("data", jArray.toString());

        Log.e(TAG, "jArray==> " + jArray);
        TransactionSkillRequest transactionRequest = new TransactionSkillRequest(this, map, this);
        transactionRequest.hitUserRequest();

    }

    private void showDataSavedSuccessfullyDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(OptionsActivity.this);

        alertDialog.setMessage("The " + Constant.SUB_TEST_TYPE + " scores for " + Constant.STUDENT_NAME + "" +
                " has been saved in the database. To see the score again, you need to scan the student card.");

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                isDialogShown = true;
                dialog.cancel();
                ClearFields();
                EmptyStudentData();

            }
        });
        alertDialog.show();
    }

    private void ClearFields() {
        for (int i = 0; i < optionlist.size(); i++) {

            View mChild = linear_layout.getChildAt(i);

            //Replace R.id.checkbox with the id of CheckBox in your layout
            CheckBox mCheckBox = (CheckBox) mChild.findViewById(R.id.checkbox);
            // CheckBox mCheckBox = (CheckBox) mChild;

            Boolean isChecked = mCheckBox.isChecked();
            mCheckBox.setChecked(false);
        }
    }

    /*private void showUpdateDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(OptionsActivity.this);

        alertDialog.setCancelable(false);

        alertDialog.setMessage("You have already taken the " + Constant.TEST_NAME + "test. Do you want to update the test result?");

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
                db.deleteTransactionRow(OptionsActivity.this, db.TBL_LP_FITNESS_SKILL_TEST_RESULT, "student_id", Constant.STUDENT_ID,
                        "test_type_id", Constant.SUB_TEST_ID);
                db.insertTables(db.TBL_LP_FITNESS_SKILL_TEST_RESULT, model);
                Toast.makeText(getApplicationContext(), "The " + Constant.SUB_TEST_TYPE + " scores for " + Constant.STUDENT_NAME + "" +
                        " has been updated in the database. To see the score again, you need to scan the student card.", Toast.LENGTH_SHORT).show();
                ClearFields();
                EmptyStudentData();
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

    @Override
    public void onResponse(Object obj) {
        if (obj instanceof SkillTransactionModel) {

            SkillTransactionModel model = (SkillTransactionModel) obj;

            Log.e(TAG, "sucess==> " + model.getIsSuccess());
            Log.e(TAG, "message==> " + model.getMessage());

            if (model.getIsSuccess().equalsIgnoreCase("true")) {

                Toast.makeText(this, model.getMessage(), Toast.LENGTH_SHORT).show();


                for (Object o : check_list) {


                    FitnessSkillTestResultModel temp = (FitnessSkillTestResultModel) o;
                    db.deleteTransactionRow(this, db.TBL_LP_FITNESS_SKILL_TEST_RESULT, "student_id", "" + temp.getStudent_id(),
                            "checklist_item_id", "" + temp.getChecklist_item_id(), "camp_id", Constant.CAMP_ID);

                    temp.setSynced(true);
                    db.insertTables(db.TBL_LP_FITNESS_SKILL_TEST_RESULT, temp);

                }

            } else {
                Toast.makeText(this, R.string.data_saved_offline, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "*********", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkForRecordExistence() {


        for (int i = 0; i < optionlist.size(); i++) {

            HashMap<String, String> check_list_exixtence = new HashMap<String, String>();
            check_list_exixtence = db.getParticularCheckListRow(getApplicationContext(), DBManager.TBL_LP_FITNESS_SKILL_TEST_RESULT,
                    "student_id", Constant.STUDENT_ID, "test_type_id", Constant.SUB_TEST_ID, "checklist_item_id", optionlist.get(i).get("CheckListItem_ID"));

            String id = optionlist.get(i).get("CheckListItem_ID");

            try {
                String score = check_list_exixtence.get("score");

                View mChild = linear_layout.getChildAt(i);

                //Replace R.id.checkbox with the id of CheckBox in your layout
                CheckBox mCheckBox = (CheckBox) mChild.findViewById(R.id.checkbox);
                // CheckBox mCheckBox = (CheckBox) mChild;
                if (score.equalsIgnoreCase("1")) {
                    mCheckBox.setChecked(true);

                } else {
                    mCheckBox.setChecked(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}


