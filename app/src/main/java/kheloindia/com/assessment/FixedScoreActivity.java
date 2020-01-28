package kheloindia.com.assessment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import kheloindia.com.assessment.functions.Constant;
import kheloindia.com.assessment.functions.GPSTracker;
import kheloindia.com.assessment.model.FitnessTestResultModel;
import kheloindia.com.assessment.util.Utility;
import kheloindia.com.assessment.webservice.TransactionRequest;

/**
 * Created by PC10 on 06/07/2017.
 */

public class FixedScoreActivity extends AppCompatActivity implements View.OnClickListener, ResponseListener {

    ImageView dashboard_img;
    FloatingActionButton test_another_img;
    Toolbar toolbar;
    Button reset_btn, save_btn;
    EditText count_et;
    TextView test_name_tv, sub_test_name_tv, text_tv, disqualified_tv;
    ImageView boy_or_girl_img;
    TextView student_name_tv, student_class_tv;

    //TextView school_tv;

    JSONObject jObj;

    private Handler customHandler = new Handler();
  /*  private long startTime = 0L;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;*/

    GPSTracker gps;
    double lat, lng;

    TextView timer_tv;
    Button start_timer_btn, stop_timer_btn;

    //private OneMinuteCountDownTimer countDownTimer;
    private final long startTime = 60 * 1000;
    private final long interval = 1 * 1000;

    DBManager db;

    TextView score_tv;
    Button scan_btn;

    int Pausecount = 0;
    String created_on = "", device_date = "";

    String score = "";

    String local_student_id = "";

    //private SimpleLocation location;

    ConnectionDetector connectionDetector;

    FitnessTestResultModel model;

    String TAG = "FixedScoreActivity";

    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L;

    Handler handler;

    int Seconds, Minutes, MilliSeconds;
    String forReTest, testTableName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_count);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gps = new GPSTracker(getApplicationContext());
        gps.getmGoogleApiClient().connect();
        sub_test_name_tv.setText(Constant.SUB_TEST_TYPE);
        text_tv.setText("Enter " + Constant.SUB_TEST_TYPE + " Score");

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


        HashMap<String, String> map = null;
        try {
            map = db.getParticularRow(getApplicationContext(), testTableName,
                    "student_id", Constant.STUDENT_ID, "test_type_id", Constant.SUB_TEST_ID);

            score = map.get("score");
            // created_on = map.get("created_on");
            created_on = map.get("last_modified_on");
            //  device_date = map.get("device_date");
//
//            String dateStr =  map.get("device_date");
//            DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
//            Date date = (Date) formatter.parse(dateStr);
//
//            device_date = Constant.ConvertDateToString(date);
            device_date = map.get("device_date");
            if (score.length() > 0) {
                score_tv.setVisibility(View.VISIBLE);
                //  score_tv.setText("Score: "+score+" "+Constant.SCORE_UNIT+" ("+device_date+")");
                //score_tv.setText("Score: "+score+" "+" ("+device_date+")");
                score_tv.setText("Score: " + score + " ");
            } else {
                score_tv.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        /*count_et.setText("");
        count_et.setHint("00");*/

        local_student_id = Constant.STUDENT_ID;
    }

    private void init() {

        handler = new Handler();
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
        count_et = (EditText) findViewById(R.id.count_et);
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
        timer_tv = (TextView) findViewById(R.id.timer_tv);
        start_timer_btn = (Button) findViewById(R.id.start_timer_btn);
        stop_timer_btn = (Button) findViewById(R.id.stop_timer_btn);
        //santosh
        disqualified_tv = (TextView) findViewById(R.id.disqualified_tv);
        text_tv = (TextView) findViewById(R.id.text_tv);
        disqualified_tv.setVisibility(View.GONE);
        //
        stop_timer_btn.setVisibility(View.GONE);
        start_timer_btn.setVisibility(View.VISIBLE);


        toolbar.setTitle(Constant.TEST_TYPE);
        test_name_tv.setText(Constant.TEST_TYPE);
        sub_test_name_tv.setText(Constant.SUB_TEST_TYPE);
        // school_tv.setText(Constant.SCHOOL_NAME);
        text_tv.setText("Enter " + Constant.SUB_TEST_TYPE + " Test Score");

        student_name_tv.setText(Constant.STUDENT_NAME);
//        student_class_tv.setText("Class: "+Constant.STUDENT_CLASS+", "+
//                Constant.SCHOOL_ID+Constant.STUDENT_ROLL_NO);

        if (Constant.STUDENT_GENDER.equalsIgnoreCase("1")) {
            boy_or_girl_img.setImageResource(R.drawable.girl_blue_i);
        } else if (Constant.STUDENT_GENDER.equalsIgnoreCase("0")) {
            boy_or_girl_img.setImageResource(R.drawable.boy_i);
        }

        Typeface font_semi_bold = Typeface.createFromAsset(getAssets(),
                "fonts/Barlow-SemiBold.ttf");
        Typeface font_reg = Typeface.createFromAsset(getAssets(),
                "fonts/Barlow-Regular.ttf");
        save_btn.setTypeface(font_semi_bold);
        reset_btn.setTypeface(font_semi_bold);
        scan_btn.setTypeface(font_semi_bold);
        start_timer_btn.setTypeface(font_semi_bold);
        stop_timer_btn.setTypeface(font_semi_bold);
        student_name_tv.setTypeface(font_reg);
        student_class_tv.setTypeface(font_reg);
        score_tv.setTypeface(font_reg);
        timer_tv.setTypeface(font_reg);


        text_tv.setTypeface(font_reg);
        test_name_tv.setTypeface(font_reg);
        sub_test_name_tv.setTypeface(font_reg);

        resetTimeFields();


        test_another_img.setOnClickListener(this);
        reset_btn.setOnClickListener(this);
        save_btn.setOnClickListener(this);
        sub_test_name_tv.setOnClickListener(this);
        scan_btn.setOnClickListener(this);
        start_timer_btn.setOnClickListener(this);
        stop_timer_btn.setOnClickListener(this);
//        timer_min_tv.setCursorVisible(false);
//        timer_min_tv.setLongClickable(false);
//        timer_min_tv.setClickable(false);
//        timer_min_tv.setFocusable(false);
//        timer_min_tv.setSelected(false);
//        timer_min_tv.setKeyListener(null);
//        timer_min_tv.setBackgroundResource(android.R.color.transparent);
//        timer_sec_tv.setCursorVisible(false);
//        timer_sec_tv.setLongClickable(false);
//        timer_sec_tv.setClickable(false);
//        timer_sec_tv.setFocusable(false);
//        timer_sec_tv.setSelected(false);
//        timer_sec_tv.setKeyListener(null);
//        timer_sec_tv.setBackgroundResource(android.R.color.transparent);
//        timer_milli_tv.setCursorVisible(false);
//        timer_milli_tv.setLongClickable(false);
//        timer_milli_tv.setClickable(false);
//        timer_milli_tv.setFocusable(false);
//        timer_milli_tv.setSelected(false);
//        timer_milli_tv.setKeyListener(null);
//        timer_milli_tv.setBackgroundResource(android.R.color.transparent);

        dashboard_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isFilled = CheckFields();
                if (!isFilled) {
                    showExitDialog2();
                } else {
                    emptyStudentData();
                    Intent i1 = new Intent(FixedScoreActivity.this, TestActivity.class);
                    startActivity(i1);
                    finish();
                }
            }
        });

        //santosh
        if (Constant.TIMER_TYPE.equals("2")) {
            count_et.setCursorVisible(false);
            count_et.setFocusable(false);
        }
        //
    }

    private void emptyStudentData() {

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
        //santosh
        disqualified_tv.setVisibility(View.GONE);
//
        student_class_tv.setText("Class " + ", " + "Registration Number");
        student_name_tv.setText("Student Name");
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.test_another_img:
                boolean isFilled = CheckFields();
                if (!isFilled) {
                    showExitDialog2();
                } else {
                    emptyStudentData();
                    Intent i = new Intent(FixedScoreActivity.this, TestActivity.class);
                    startActivity(i);
                    finish();
                }
                break;

            case R.id.reset_btn:


                boolean isFilled2 = CheckFields();
                if (isFilled2) {
                    showResetDialog();
                } else {
                    start_timer_btn.setEnabled(true);
                    resetTimeFields();
                    emptyStudentData();
                    ClearFields();
                }

                break;

            case R.id.save_btn:

                String count_txt = count_et.getText().toString();


                lat = gps.getLatitude();
                lng = gps.getLongitude();
                String DateTime = Constant.getDateTimeServer();
                String deviceDateTime = Constant.getDateTime();

                Log.e("hand-eye", "lat=> " + lat + "   lng=> " + lng);
                Log.e(TAG, "DateTime=> " + DateTime);
                Log.e(TAG, "deviceDateTime=> " + deviceDateTime);


                int count = count_txt.equals("") ? 0 : Integer.parseInt(count_txt);

                if (UpdateTime == 0) {
                    Toast.makeText(getApplicationContext(), getString(R.string.start_timer), Toast.LENGTH_SHORT).show();
                } else if (Constant.TIMER_TYPE.equals("1") && UpdateTime < 30000) {
                    Toast.makeText(getApplicationContext(), getString(R.string.time_not_completed_30sec), Toast.LENGTH_SHORT).show();
                } else if (!Constant.TIMER_TYPE.equals("2") && count == 0) {
                    Toast.makeText(getApplicationContext(), getString(R.string.fill_some_score), Toast.LENGTH_SHORT).show();
                } else if (Constant.TIMER_TYPE.equals("2") && UpdateTime < 60000 && Integer.parseInt(count_txt) < 15) {
                    Toast.makeText(getApplicationContext(), getString(R.string.time_not_completed_60sec), Toast.LENGTH_SHORT).show();
                } else {
                    if (Constant.STUDENT_NAME.length() > 1) {

                        ArrayList<String> seniorList = new ArrayList<>();

                        int cut_off = Integer.parseInt(Constant.SENIORS_STARTING_FROM);

                        if (seniorList.isEmpty()) {
                            for (int j = cut_off; j <= 12; j++) {

                                seniorList.add(String.valueOf(j));
                            }
                        }
                        if (stop_timer_btn.getVisibility() != View.VISIBLE) {
                            //for both juniors and seniors
                            if (Constant.TEST_APPLICABLE.equals("3")) {
                                showSaveDialog(count_txt, DateTime, deviceDateTime);
                            } else {

                                if (seniorList.contains(Constant.CLASS_ID)) {
                                    showSaveDialog(count_txt, DateTime, deviceDateTime);
                                } else {
                                    Toast.makeText(getApplicationContext(), R.string.not_for_juniors, Toast.LENGTH_SHORT).show();
                                }

                            }
                        } else
                            Toast.makeText(getApplicationContext(), R.string.please_stop_timer, Toast.LENGTH_SHORT).show();


                    } else {
                        Toast.makeText(getApplicationContext(), R.string.please_scan, Toast.LENGTH_SHORT).show();
                    }


                }

                break;

            case R.id.sub_test_name_tv:


                boolean isFilled1 = CheckFields();
                if (!isFilled1) {
                    showExitDialog1();
                } else {
                    emptyStudentData();
                    ClearFields();
                    Intent i1 = new Intent(FixedScoreActivity.this, ShowSubTestActivity.class);
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

            case R.id.start_timer_btn:

                startSound();

                // 0 - pushup, normal without any restriction of time limit
                // 1 - curl up 30 sec
                // 2 - flamingo balance
                // Constant.TIMER_TYPE = 0,1= normlal timer
                // Constant.TIMER_TYPE = 2 = pause timer

                if (Constant.TIMER_TYPE.equals("1")) {
                    start_timer_btn.setEnabled(false);
                    stop_timer_btn.setText("STOP");
                } else if (Constant.TIMER_TYPE.equals("2")) {
                    start_timer_btn.setEnabled(true);
                    stop_timer_btn.setText("PAUSE");
                } else {
                    start_timer_btn.setEnabled(false);
                    stop_timer_btn.setText("STOP");
                }


                reset_btn.setEnabled(false);

                //      countDownTimer = new OneMinuteCountDownTimer(startTime,interval);

                //     countDownTimer.start();

                StartTime = SystemClock.uptimeMillis();
                handler.postDelayed(runnable, 0);

                stop_timer_btn.setVisibility(View.VISIBLE);
                start_timer_btn.setVisibility(View.GONE);

                break;

            //     startTime();

            case R.id.stop_timer_btn:
                //santosh


                if (Constant.TIMER_TYPE.equals("1") && UpdateTime < 30000)
                    Toast.makeText(this, getString(R.string.time_not_completed_30sec), Toast.LENGTH_SHORT).show();
                else {
                    if (Constant.TIMER_TYPE.equals("2")) {
                        Pausecount++;
                        count_et.setText("" + Pausecount);
                    }
                    stopSound();
                }

                //


                //    countDownTimer.cancel();
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

    private void resetTimeFields() {

        start_timer_btn.setVisibility(View.VISIBLE);
        start_timer_btn.setEnabled(true);


        MillisecondTime = 0L;
        StartTime = 0L;
        TimeBuff = 0L;
        UpdateTime = 0L;
        Seconds = 0;
        Minutes = 0;
        MilliSeconds = 0;

        timer_tv.setText("00:00:000");
        //santosh
        Pausecount = 0;
    }

    private void stopSound() {

        MediaPlayer mPlayerStop = MediaPlayer.create(FixedScoreActivity.this, R.raw.whistle_sound);
        if (mPlayerStop != null) {
            if (!mPlayerStop.isPlaying()) {
                mPlayerStop.start();
            } else {
                mPlayerStop.stop();
            }
            mPlayerStop.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    mp.release();
                }
            });
        }
        reset_btn.setEnabled(true);

        stop_timer_btn.setVisibility(View.GONE);

        start_timer_btn.setVisibility(View.VISIBLE);

        TimeBuff += MillisecondTime;

        handler.removeCallbacks(runnable);
        //santosh
        if (!Constant.TIMER_TYPE.equals("2")) {
            start_timer_btn.setEnabled(false);
        }
//

    }

    private void startSound() {

        MediaPlayer mPlayer = MediaPlayer.create(FixedScoreActivity.this, R.raw.gun_sound_two);
        if (mPlayer != null) {
            if (!mPlayer.isPlaying()) {
                mPlayer.start();
            } else {
                mPlayer.stop();
            }
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    mp.release();
                }
            });
        }
    }

    public Runnable runnable = new Runnable() {

        public void run() {

            MillisecondTime = SystemClock.uptimeMillis() - StartTime;

            UpdateTime = TimeBuff + MillisecondTime;

            Seconds = (int) (UpdateTime / 1000);

            Minutes = Seconds / 60;

            Seconds = Seconds % 60;

            MilliSeconds = (int) (UpdateTime % 1000);
            timer_tv.setText("" + Minutes + ":" + String.format("%02d", Seconds) + ":" + String.format("%02d", MilliSeconds));
            handler.postDelayed(this, 0);


            if (Constant.SUB_TEST_ID.equals("55")) {

                if (Minutes == 0 && Seconds == 30) {
                    TimeBuff += MillisecondTime;
                    handler.removeCallbacks(runnable);
                    timer_tv.setText("" + Minutes + ":" + String.format("%02d", Seconds) + ":" + "000");
                    stopSound(); //santosh
                    start_timer_btn.setEnabled(false);
                }

            } else {
                if (Minutes == 1) {
                    TimeBuff += MillisecondTime;

                    handler.removeCallbacks(runnable);
                    timer_tv.setText("" + Minutes + ":"
                            + String.format("%02d", Seconds) + ":"
                            + "000");
                    stopSound();
                    //santosh
                    start_timer_btn.setEnabled(false);
                    //
                }
                //santosh
                else if (Minutes == 0 && Seconds <= 30 && Pausecount == 15) {
                    disqualified_tv.setVisibility(View.VISIBLE);
                    stopSound();
                    start_timer_btn.setEnabled(false);
                }

            }
            //

        }

    };


    int Time = 0;

   /* public class OneMinuteCountDownTimer extends CountDownTimer {

        public OneMinuteCountDownTimer(long startTime, long interval) {

            super(startTime, interval);
        }

        @Override
        public void onFinish() {

          //  timer_tv.setText("timer=" + Time + " time finished");
            timer_tv.setText("" + "01:00");
            Time = 0;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            Time++;
            int milliseconds = (int) (millisUntilFinished % 1000);
            timer_tv.setText("" + "00:"+Time+":"+String.format("%02d", milliseconds));
        }
    }*/

    /*private void startTime() {
        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {

                timer_tv.setText("seconds remaining: " + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                start_timer_btn.setEnabled(true);
                timer_tv.setText("00:00:00");
            }

        }.start();
    }*/


    private void showResetDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FixedScoreActivity.this);
        alertDialog.setCancelable(false);

        alertDialog.setMessage("Are you sure you want to reset the data?");

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.alarm);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                count_et.setText("0");

                resetTimeFields();
                start_timer_btn.setEnabled(true);
                //santosh
                disqualified_tv.setVisibility(View.GONE);
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

    private void showSaveDialog(final String count_txt, final String dateTime, final String deviceDateTime) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FixedScoreActivity.this);
        alertDialog.setCancelable(false);

        alertDialog.setMessage("Are you sure you want to save the data?");

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.alarm);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                timer_tv.setText("00:00:00");
                start_timer_btn.setVisibility(View.VISIBLE);
                stop_timer_btn.setVisibility(View.GONE);
                handler.removeCallbacks(runnable);
                resetTimeFields();

                insertTransactionData(count_txt, dateTime, deviceDateTime);
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();

    }

    private void insertTransactionData(String count, String date, String deviceDateTime) {
        try {
            model = new FitnessTestResultModel();
            model.setStudent_id(Integer.parseInt(Constant.STUDENT_ID));
            model.setCamp_id(Integer.parseInt(Constant.CAMP_ID));
            model.setTest_type_id(Integer.parseInt(Constant.SUB_TEST_ID));
            model.setTest_coordinator_id(Integer.parseInt(Constant.TEST_COORDINATOR_ID));
            //santosh
            Double score_double = Double.parseDouble(disqualified_tv.getVisibility() == View.VISIBLE ? "1000" : count);
            //
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
            model.setTestName(Constant.TEST_TYPE);
            model.setDevice_date(deviceDateTime);

            HashMap<String, String> map = null;
            try {
                map = db.getParticularRow(getApplicationContext(), testTableName,
                        "student_id", Constant.STUDENT_ID, "test_type_id", Constant.SUB_TEST_ID);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (map.size() == 0) {
                db.insertTables(testTableName, model);

                showDataSavedSuccessfullyDialog();

                // After entering into local DB check for net connectivity and sync to server
                if (connectionDetector.isConnectingToInternet()) {
                    CallTransactionAPI();
                } else
                    Toast.makeText(getApplicationContext(), R.string.data_saved_offline, Toast.LENGTH_SHORT).show();


            } else {
                String testedORNot = map.get("tested");
                if (testedORNot.equalsIgnoreCase("true")) {
                    showUpdateDialog(count);
                } else if (testedORNot.equalsIgnoreCase("false")) {
                    db.deleteTransactionRow(FixedScoreActivity.this, testTableName, "student_id", Constant.STUDENT_ID,
                            "test_type_id", Constant.SUB_TEST_ID, "camp_id", Constant.CAMP_ID);
                    db.insertTables(testTableName, model);
                    showDataSavedSuccessfullyDialog();

                    // After entering into local DB check for net connectivity and sync to server
                    if (connectionDetector.isConnectingToInternet()) {
                        CallTransactionAPI();
                    } else
                        Toast.makeText(getApplicationContext(), R.string.data_saved_offline, Toast.LENGTH_SHORT).show();
                }

            }
        } catch (Exception e) {
            System.out.println("Fixed Score Avctivity"+e.getMessage());
        }

    }

    private void showDataSavedSuccessfullyDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FixedScoreActivity.this);
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

    private void showUpdateDialog(String count) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FixedScoreActivity.this);
        alertDialog.setCancelable(false);

        /*alertDialog.setMessage(Constant.STUDENT_NAME+" has already taken the "+Constant.SUB_TEST_TYPE+"" +
                " on "+ device_date+"\n\n"+"Previous Score: " + score+Constant.SCORE_UNIT+"\n\n"+"Current Score: " +
                count+Constant.SCORE_UNIT+"\n\n"+" Do you want to update the test result?");*/


//        alertDialog.setMessage(Constant.STUDENT_NAME+" has already taken the "+Constant.SUB_TEST_TYPE+"" +
//                " on "+ device_date+"\n\n"+"Previous Score: " + score+"\n\n"+"Current Score: " +
//                count+"\n\n"+" Do you want to update the test result?");
        alertDialog.setMessage(Constant.STUDENT_NAME + " has already taken the " + Constant.SUB_TEST_TYPE + "" +
                "\n\n" + "Previous Score: " + score + "\n\n" + "Current Score: " +
                count + "\n\n" + " Do you want to update the test result?");

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
                db.deleteTransactionRow(FixedScoreActivity.this, testTableName, "student_id", Constant.STUDENT_ID,
                        "test_type_id", Constant.SUB_TEST_ID, "camp_id", Constant.CAMP_ID);
                db.insertTables(testTableName, model);
                showDataSavedSuccessfullyDialog();
                // After entering into local DB check for net connectivity and sync to server
                if (connectionDetector.isConnectingToInternet()) {
                    CallTransactionAPI();
                } else
                    Toast.makeText(getApplicationContext(), R.string.data_saved_offline, Toast.LENGTH_SHORT).show();
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

            Log.e(TAG, "jArray=> " + jArray);

        } catch (Exception e1) {
            e1.printStackTrace();
        }

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("data", jArray.toString());
        map.put("AccessBy", "" + Constant.TEST_COORDINATOR_ID);
        TransactionRequest transactionRequest = new TransactionRequest(this, map, this);
        transactionRequest.hitUserRequest();

    }

    private void ClearFields() {
        count_et.setText("0");

    }

    public void onBackPressed() {

        boolean isFilled = CheckFields();
        if (!isFilled) {
            showExitDialog2();
        } else {
            emptyStudentData();
            Intent i1 = new Intent(FixedScoreActivity.this, TestActivity.class);
            startActivity(i1);
            finish();
        }
        //moveTaskToBack(true);
    }

    private boolean CheckFields() {
        String count = timer_tv.getText().toString().trim();

        if (count.equals("00:00:000")) {
            return true;
        }

        return false;

    }


    private void showExitDialog2() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FixedScoreActivity.this);
        alertDialog.setCancelable(false);
        alertDialog.setMessage(getResources().getString(R.string.exit_from_test));

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.alarm);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                emptyStudentData();
                dialog.cancel();
                Intent i1 = new Intent(FixedScoreActivity.this, TestActivity.class);
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

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FixedScoreActivity.this);
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
                Intent i1 = new Intent(FixedScoreActivity.this, ShowSubTestActivity.class);
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

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FixedScoreActivity.this);
        alertDialog.setCancelable(false);
        alertDialog.setMessage(getResources().getString(R.string.exit_from_test));

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.alarm);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
                emptyStudentData();
                Intent i = new Intent(FixedScoreActivity.this, ShowSubTestActivity.class);
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
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                View view = getCurrentFocus();
                if (view != null)
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {
            Log.e("FIXEDSCOREACTIVITY", e.getMessage());
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

                    db.deleteTransactionRow(this, testTableName, "student_id", "" + FixedScoreActivity.this.model.getStudent_id(),
                            "test_type_id", "" + FixedScoreActivity.this.model.getTest_type_id(), "camp_id", Constant.CAMP_ID);
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
}


