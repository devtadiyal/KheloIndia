package kheloindia.com.assessment;

import android.Manifest;
import android.app.ProgressDialog;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;

import kheloindia.com.assessment.functions.Constant;
import kheloindia.com.assessment.functions.GPSTracker;
import kheloindia.com.assessment.model.TransactionModel;
import kheloindia.com.assessment.util.AppConfig;
import kheloindia.com.assessment.util.ConnectionDetector;
import kheloindia.com.assessment.util.DBManager;
import kheloindia.com.assessment.util.ResponseListener;

import kheloindia.com.assessment.model.FitnessTestResultModel;

import kheloindia.com.assessment.util.Utility;
import kheloindia.com.assessment.webservice.TransactionRequest;


/**
 * Created by PC10 on 05/15/2017.
 */

public class TimeActivity extends AppCompatActivity implements View.OnClickListener, ResponseListener {

    ImageView dashboard_img;
    FloatingActionButton test_another_img;
    Toolbar toolbar;
    Button reset_btn;
    TextView minutes_tv, seconds_tv, mili_seconds_tv;
    Button start_timer_btn, stop_timer_btn;
    LinearLayout linear_layout;
    Boolean isStart = false;
    TextView test_name_tv, sub_test_name_tv;
    ArrayList<TextView> mTextViewList_Name = new ArrayList<>();
    ArrayList<TextView> mTextViewList_Class = new ArrayList<>();
    ArrayList<LinearLayout> mLinearLaoyutList = new ArrayList<>();
    ArrayList<String> student_ids = new ArrayList<>();
    ArrayList<Button> mButtonList = new ArrayList<>();
    ArrayList<Button> remove_Button_list = new ArrayList<>();
    ArrayList<Button> scan_Button_list = new ArrayList<>();
    ArrayList<TextView> skipped_label_list = new ArrayList<>();
    ArrayList<Button> undoSkippedButtonsList = new ArrayList<>();

    int NoOfStudents = 1;

    int global_pos = 0;

    int temp_global_pos = 0;

    FitnessTestResultModel model;

    String created_on = "";
    String device_date = "";
    String previousTime = null;

    String currentScore = null;

    private Handler customHandler = new Handler();
    private long startTime = 0L;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;

    String local_student_id = "";

    TextView text_tv, score_tv;

    GPSTracker gps;
    double lat = 0.0, lng = 0.0;
    Button save_btn;

    int students = 0;
    Button scan_btn;
    //  Button stop_split_timer_btn;

    boolean clearStudentDetails = false;

    ImageView boy_or_girl_img;

    TextView student_name_tv, student_class_tv;

    DBManager db;

    LinearLayout timer_linear_layout;

    SharedPreferences sp;

    ConnectionDetector connectionDetector;

    JSONArray jArray;
    JSONObject jObj;

    RadioGroup student_radio_group;
    RadioButton single_student_radio_btn, multiple_student_radio_btn;
    int studentStatus = 0;

    Spinner position_spinner;

    RelativeLayout relative_layout;

    int scan_position = 0;

    ArrayList<HashMap<String, String>> scoreList = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> studentList = new ArrayList<HashMap<String, String>>();

    private ArrayList<String> position_list = new ArrayList<String>();
    private ArrayList<FitnessTestResultModel> bulk_data_list;

    int StudentCount = 0;


    String TAG = "TimeActivity";

    private boolean timer, whistle;
    private Thread audioDispatcher;
    private boolean save;
    AudioDispatcher dispatcher;
    PitchDetectionHandler pdh;

    // EditText no_of_students;
    String no_of_student;
    int noOfTimesSplit = 1;
    TextView tv_1, tv_2, tv_3, tv_4, tv_5, tv_6, tv_7, tv_8;
    ArrayList<TextView> tv_list = new ArrayList<TextView>();

    TextView select_lanes_text_tv, rescan_msg;
    LinearLayout lanes_layout;
    Typeface font_semi_bold, font_reg;
    ProgressDialog progressDialog;
    String forReTest, testTableName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_agility);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        init();
    }


    @Override
    protected void onResume() {
        super.onResume();
        try {
            gps = new GPSTracker(getApplicationContext());
            // gps.getmGoogleApiClient().connect();
            sub_test_name_tv.setText(Constant.SUB_TEST_TYPE);
            text_tv.setText("Enter " + Constant.SUB_TEST_TYPE + " Score");

            if (studentStatus == 1) {
                Log.e(TAG, "studentStatus=> " + studentStatus);

                if (Constant.STUDENT_NAME.length() < 1) {
                    Log.e(TAG, "student name length less than 1");
                } else {
                    Log.e(TAG, "Constant.CLASS_ID==> " + Constant.CLASS_ID);
                    Log.e(TAG, "student_ids before==> " + student_ids);

                    if (student_ids.contains(Constant.STUDENT_ID)) {
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

                        forReTest = sharedPreferences.getString("backclick", "0");

                        System.out.println("STUDENT  " + forReTest + "    " + student_ids + "      " + Constant.STUDENT_ID);
                        if (forReTest.equals("1")) {

                        } else {
                            Toast.makeText(getApplicationContext(), "You have already scanned this student", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Log.e(TAG, "student name length greater than 1");

                        Log.e(TAG, "scan_position==" + (scan_position - 1));

//                    mTextViewList_Class.get(scan_position - 1).setText("Class: " + Constant.STUDENT_CLASS + ", " +
//                            Constant.STUDENT_REGISTRATION_NO);
                        mTextViewList_Class.get(scan_position - 1).setText(Constant.STUDENT_CLASS + ", " +
                                Constant.STUDENT_REGISTRATION_NO.substring(6));


                        mTextViewList_Name.get(scan_position - 1).setText(Constant.STUDENT_NAME);
                        mLinearLaoyutList.get(scan_position - 1).setVisibility(View.VISIBLE);
                        mButtonList.get(scan_position - 1).setVisibility(View.GONE);
                        relative_layout.setVisibility(View.GONE);
                        //remove_Button_list.get(scan_position - 1).setVisibility(View.GONE);


                        HashMap<String, String> map = new HashMap<>();
                        map.put("name", Constant.STUDENT_NAME);
                        map.put("student_id", Constant.STUDENT_ID);
                        map.put("class_id", Constant.CLASS_ID);
                        studentList.set(scan_position - 1, map);
                        student_ids.set(scan_position - 1, Constant.STUDENT_ID);
//                    studentList.add(map);
//
//                    student_ids.add(Constant.STUDENT_ID);

                        Log.e(TAG, "student_ids after==> " + student_ids);
                    }
                    if (rescan_msg.getVisibility() == View.GONE)
                        rescan_msg.setVisibility(View.VISIBLE);
                }

            } else {
                relative_layout.setVisibility(View.VISIBLE);
            }

            if (Constant.STUDENT_NAME.length() < 1) {
                student_name_tv.setText("Student Name");
            } else {
                student_name_tv.setText(Constant.STUDENT_NAME);
            }


            if (Constant.CLASS_ID.length() < 1) {
                student_class_tv.setText("Class " + ", " + "Registration Number");
            } else {
//            student_class_tv.setText("Class: " + Constant.STUDENT_CLASS + ", " +
//                    Constant.STUDENT_REGISTRATION_NO);
                student_class_tv.setText(Constant.STUDENT_CLASS + ", " +
                        Constant.STUDENT_REGISTRATION_NO.substring(6));
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


                String score = map.get("score");
                // created_on = map.get("created_on");
                created_on = map.get("last_modified_on");

//            String dateStr = map.get("device_date");
//            DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
//            Date date = (Date) formatter.parse(dateStr);

                //  device_date = Constant.ConvertDateToString(date);
                device_date = map.get("device_date");

                if (score.length() > 0) {

                    score_tv.setVisibility(View.VISIBLE);
                    previousTime = Constant.ConvertMilisToTime(Double.parseDouble(score));
                    // score_tv.setText("Score: " + previousTime + " " + " (" + device_date + ")");
                    score_tv.setText("Score: " + previousTime + " " + Constant.SCORE_UNIT);
                } else {
                    score_tv.setVisibility(View.GONE);
                }

                save = false;


            } catch (Exception e) {
                e.printStackTrace();
            }

            local_student_id = Constant.STUDENT_ID;

        } catch (Exception e) {
            Log.e("Timeactivity", e.getMessage());
        }

    }

    private void init() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.please_wait_saving_data));
        progressDialog.setCancelable(false);

        timer = false;
        db = DBManager.getInstance();
        gps = new GPSTracker(getApplicationContext());
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        connectionDetector = new ConnectionDetector(this);
        forReTest = sp.getString(AppConfig.FOR_RETEST, "0");
        testTableName = DBManager.TBL_LP_FITNESS_TEST_RESULT;
        if (forReTest.equalsIgnoreCase("1"))
            testTableName = DBManager.TBL_LP_FITNESS_RETEST_RESULT;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        dashboard_img = (ImageView) toolbar.findViewById(R.id.dashboard_img);
        test_another_img = (FloatingActionButton) findViewById(R.id.test_another_img);
        reset_btn = (Button) findViewById(R.id.reset_btn);
        mili_seconds_tv = (TextView) findViewById(R.id.mili_seconds_tv);
        seconds_tv = (TextView) findViewById(R.id.seconds_tv);
        minutes_tv = (TextView) findViewById(R.id.minutes_tv);
        linear_layout = (LinearLayout) findViewById(R.id.linear_layout);
        start_timer_btn = (Button) findViewById(R.id.start_timer_btn);
        stop_timer_btn = (Button) findViewById(R.id.stop_timer_btn);
        test_name_tv = (TextView) toolbar.findViewById(R.id.test_name_tv);
        sub_test_name_tv = (TextView) toolbar.findViewById(R.id.sub_test_name_tv);
        // school_tv = (TextView) findViewById(R.id.school_tv);
        save_btn = (Button) findViewById(R.id.save_btn);
        text_tv = (TextView) findViewById(R.id.text_tv);
        boy_or_girl_img = (ImageView) findViewById(R.id.boy_or_girl_img);
        student_class_tv = (TextView) findViewById(R.id.student_class_tv);
        student_name_tv = (TextView) findViewById(R.id.student_name_tv);
        scan_btn = (Button) findViewById(R.id.scan_btn);
        score_tv = (TextView) findViewById(R.id.score_tv);
        timer_linear_layout = (LinearLayout) findViewById(R.id.timer_linear_layout);
        student_radio_group = (RadioGroup) findViewById(R.id.student_radio_group);
        single_student_radio_btn = (RadioButton) findViewById(R.id.single_student_radio_btn);
        multiple_student_radio_btn = (RadioButton) findViewById(R.id.multiple_student_radio_btn);
        position_spinner = (Spinner) findViewById(R.id.position_spinner);
        relative_layout = (RelativeLayout) findViewById(R.id.relative_layout);
        lanes_layout = (LinearLayout) findViewById(R.id.lanes_layout);
        select_lanes_text_tv = (TextView) findViewById(R.id.select_lanes_text_tv);
        rescan_msg = (TextView) findViewById(R.id.rescan_msg);

        font_semi_bold = Typeface.createFromAsset(getAssets(),
                "fonts/Barlow-SemiBold.ttf");
        font_reg = Typeface.createFromAsset(getAssets(),
                "fonts/Barlow-Regular.ttf");
        student_name_tv.setTypeface(font_reg);
        student_class_tv.setTypeface(font_reg);
        text_tv.setTypeface(font_reg);
        minutes_tv.setTypeface(font_reg);
        seconds_tv.setTypeface(font_reg);
        mili_seconds_tv.setTypeface(font_reg);
        select_lanes_text_tv.setTypeface(font_reg);
        test_name_tv.setTypeface(font_reg);
        sub_test_name_tv.setTypeface(font_reg);

        save_btn.setTypeface(font_semi_bold);
        reset_btn.setTypeface(font_semi_bold);
        start_timer_btn.setTypeface(font_semi_bold);
        stop_timer_btn.setTypeface(font_semi_bold);
        scan_btn.setTypeface(font_semi_bold);


        if (Constant.MULTIPLE_LANE.equals("0")) {
            lanes_layout.setVisibility(View.GONE);
            select_lanes_text_tv.setVisibility(View.GONE);
        } else {
            lanes_layout.setVisibility(View.VISIBLE);
            select_lanes_text_tv.setVisibility(View.VISIBLE);
        }


        //   stop_split_timer_btn = (Button) findViewById(R.id.stop_split_timer_btn);
        //   no_of_students = (EditText) findViewById(R.id.no_of_students);
        tv_1 = (TextView) findViewById(R.id.tv_1);
        tv_2 = (TextView) findViewById(R.id.tv_2);
        tv_3 = (TextView) findViewById(R.id.tv_3);
        tv_4 = (TextView) findViewById(R.id.tv_4);
        tv_5 = (TextView) findViewById(R.id.tv_5);
        tv_6 = (TextView) findViewById(R.id.tv_6);
        tv_7 = (TextView) findViewById(R.id.tv_7);
        tv_8 = (TextView) findViewById(R.id.tv_8);

        tv_list.add(tv_1);
        tv_list.add(tv_2);
        tv_list.add(tv_3);
        tv_list.add(tv_4);
        tv_list.add(tv_5);
        tv_list.add(tv_6);
        tv_list.add(tv_7);
        tv_list.add(tv_8);


        student_radio_group.setVisibility(View.GONE);
        position_spinner.setVisibility(View.GONE);

        stop_timer_btn.setVisibility(View.INVISIBLE);
        start_timer_btn.setVisibility(View.VISIBLE);


        //school_tv.setText(Constant.SCHOOL_NAME);
        linear_layout.setVisibility(View.INVISIBLE);

        scan_btn.setOnClickListener(this);
        tv_1.setOnClickListener(this);
        tv_2.setOnClickListener(this);
        tv_3.setOnClickListener(this);
        tv_4.setOnClickListener(this);
        tv_5.setOnClickListener(this);
        tv_6.setOnClickListener(this);
        tv_7.setOnClickListener(this);
        tv_8.setOnClickListener(this);


        try {
            if (Constant.STUDENT_NAME.length() < 1) {
                student_name_tv.setText("Student Name");
            } else {
                student_name_tv.setText(Constant.STUDENT_NAME);
            }


//            if (Constant.CLASS_ID.length() < 1) {
//                student_class_tv.setText("Class " + ", " + "Registration Number");
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


        toolbar.setTitle(Constant.TEST_TYPE);
        test_name_tv.setText(Constant.TEST_TYPE);
        sub_test_name_tv.setText(Constant.SUB_TEST_TYPE);
        text_tv.setText("Enter " + Constant.SUB_TEST_TYPE + " Test Score");

        test_another_img.setOnClickListener(this);
        reset_btn.setOnClickListener(this);
        start_timer_btn.setOnClickListener(this);
        stop_timer_btn.setOnClickListener(this);
        save_btn.setOnClickListener(this);
        sub_test_name_tv.setOnClickListener(this);

        tv_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnableDisableTextView(1);
                studentStatus = 0;
                NoOfStudents = 1;
                stop_timer_btn.setText("STOP");
                timer_linear_layout.setVisibility(View.GONE);
                position_spinner.setVisibility(View.GONE);
                // no_of_students.setVisibility(View.GONE);
                timer_linear_layout.removeAllViews();
            }
        });

        tv_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnableDisableTextView(2);
                NoOfStudents = 2;
                EnableSplitTimerButton();
            }
        });
        tv_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnableDisableTextView(3);
                NoOfStudents = 3;
                EnableSplitTimerButton();
            }
        });
        tv_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnableDisableTextView(4);
                NoOfStudents = 4;
                EnableSplitTimerButton();
            }
        });
        tv_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnableDisableTextView(5);
                NoOfStudents = 5;
                EnableSplitTimerButton();
            }
        });
        tv_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnableDisableTextView(6);
                NoOfStudents = 6;
                EnableSplitTimerButton();
            }
        });
        tv_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnableDisableTextView(7);
                NoOfStudents = 7;
                EnableSplitTimerButton();
            }
        });
        tv_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnableDisableTextView(8);
                NoOfStudents = 8;
                EnableSplitTimerButton();
            }
        });

        dashboard_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (timer) {
                    // stopTimer();

                    timer = false;
                }
                boolean isFilled = CheckFields();

                if (isFilled) {
                    showExitDialog2();
                } else {
                    emptyStudentData();
                    Intent i1 = new Intent(TimeActivity.this, TestActivity.class);
                    startActivity(i1);
                    finish();
                }
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        else {
            startAudioProcessing();
        }


        student_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.single_student_radio_btn:

                        studentStatus = 0;
                        stop_timer_btn.setText("STOP");
                        timer_linear_layout.setVisibility(View.GONE);
                        position_spinner.setVisibility(View.GONE);
                        //   no_of_students.setVisibility(View.GONE);

                        timer_linear_layout.removeAllViews();
                        //  no_of_students.setText("");
                        //  no_of_students.setHint("Enter No of Students");

                        position_spinner.setSelection(0);

                        break;
                    case R.id.multiple_student_radio_btn:
                        studentStatus = 1;
                        stop_timer_btn.setText("SPLIT");
                        timer_linear_layout.setVisibility(View.VISIBLE);
                        //  position_spinner.setVisibility(View.VISIBLE);
                        //  no_of_students.setVisibility(View.VISIBLE);

                        //  no_of_students.setText("");
                        //  no_of_students.setHint("Enter No of Students");

                        position_spinner.setSelection(0);

                        break;
                }
            }
        });

    }

    private void startAudioProcessing() {
        try {
            dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0);
            pdh = new PitchDetectionHandler() {
                @Override
                public void handlePitch(PitchDetectionResult result, AudioEvent e) {
                    final float pitchInHz = result.getPitch();

                    float pb = result.getProbability();


//
                    //  Log.e("pitchInHz===>", "" + pitchInHz);
//
                    //    Log.e("pitchInHz===>", "" + pitchInHz);


                    if (pitchInHz != -1.0) {
                        //      Log.e("pitchInHz===>", "" + pitchInHz);

                        //    Log.e("pitchInPb===>", "" + pb);

                        //     Log.e("pitchInPb===>", "" + pb);

                        //Log.e("pitchInPb===>", "" + pb);

                    }
//                }
//                if(pitchInHz > 100) {
//
//                }
//                        MediaPlayer mPlayer = MediaPlayer.create(TimeActivity.this, R.raw.gun_sound_two);
//
//                        if (!mPlayer.isPlaying()) {
//                            mPlayer.start();
//                        } else {
//                            mPlayer.stop();
//                        }
//                        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                            @Override
//                            public void onCompletion(MediaPlayer mp) {
//                                mp.reset();
//                                mp.release();
//                            }
//                        });


////signal
//                if(pitchInHz > 1500 &&  pitchInHz < 1575){
//
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            if(!timer){
//                                startTimer();
//                                timer=true;
//                            }
//
//                        }
//                    });
//
//                }
//                ////whistle
//                else

//1900 to 2100 gun sound
                    //  if ((pitchInHz > 2800 && pitchInHz < 3000)) {
                    if (pitchInHz > 3500 && pitchInHz < 4000) {
                        //if(pitchInHz>100){
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {

                        if (!timer) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    /*startTimer();
                                    timer = true;*/
                                    // whistle = false;


                                    if (studentStatus == 0) {
                                        stop_timer_btn.setText("STOP");
                                    } else if (studentStatus == 1) {
                                        stop_timer_btn.setText("SPLIT");
                                    }

                                    EnableTextView(false);

                                    MediaPlayer mPlayer = MediaPlayer.create(TimeActivity.this, R.raw.gun_sound_two);

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

                                    if (studentStatus == 0) {
                                        //     stop_split_timer_btn.setVisibility(View.GONE);
                                        startTimer();
                                        timer = true;
                                    } else {

                                        // String no_of_student_txt = no_of_students.getText().toString().trim();
                                        String no_of_student_txt = no_of_student;

                                        if (no_of_student_txt.length() < 1) {
                                            Toast.makeText(getApplicationContext(), "Please enter no of students", Toast.LENGTH_SHORT).show();
                                        } else {
                                            students = Integer.parseInt(no_of_student_txt);

                                            if (students > 0) {

                                                position_list.add("Select Position");

                                                for (int i = 0; i < students; i++) {
                                                    position_list.add("" + (i + 1));
                                                }

                                                ArrayAdapter position_spin_adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.position_item, position_list);
                                                position_spin_adapter.setDropDownViewResource(R.layout.position_dropdown_item);
                                                position_spinner.setAdapter(position_spin_adapter);

                                                //    position_spinner.setVisibility(View.VISIBLE);

                                                EnableDisableEditText(false);


                                                startTimer();
                                                timer = true;
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Please enter no of students", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    }


                                }
                            });


                        }

                    }


//                    });

                    // }
                    // scan=false;

                }

            };

            AudioProcessor p = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, pdh);
            dispatcher.addAudioProcessor(p);
            if (audioDispatcher == null) {
                audioDispatcher = new Thread(dispatcher, "Audio Dispatcher");
                audioDispatcher.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopAudioProcessing() {
        try {
            dispatcher.stop();
            pdh = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void EnableSplitTimerButton() {
        studentStatus = 1;
        stop_timer_btn.setText("SPLIT");
        timer_linear_layout.setVisibility(View.VISIBLE);
    }

    private void EnableDisableTextView(int pos) {

        start_timer_btn.setEnabled(true);
        start_timer_btn.setBackgroundResource(R.drawable.start_button);
        timer_linear_layout.removeAllViews();
        ClearFields();

        if (pos == 1) {
            relative_layout.setVisibility(View.VISIBLE);
            timer_linear_layout.setVisibility(View.GONE);
        } else {
            relative_layout.setVisibility(View.GONE);
            timer_linear_layout.setVisibility(View.VISIBLE);
        }

        no_of_student = "" + pos;
        int size = tv_list.size();
        for (int i = 1; i <= size; i++) {
            if (i == pos) {
                tv_list.get(i - 1).setBackgroundResource(R.drawable.color_primary_full_box);
                tv_list.get(i - 1).setTextColor(getResources().getColor(R.color.white));
            } else {
                tv_list.get(i - 1).setBackgroundResource(R.drawable.color_primary_box);
                tv_list.get(i - 1).setTextColor(getResources().getColor(R.color.black));
            }
        }
    }

    private void EnableTextView(boolean falseOrTrue) {

        int size = tv_list.size();
        for (int i = 0; i < size; i++) {

            tv_list.get(i).setClickable(falseOrTrue);
            tv_list.get(i).setFocusable(falseOrTrue);
            tv_list.get(i).setFocusableInTouchMode(falseOrTrue);
            tv_list.get(i).setEnabled(falseOrTrue);

        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.test_another_img:
                boolean isFilled = CheckFields();
                if (isFilled) {
                    showExitDialog2();
                } else {
                    emptyStudentData();
                    Intent i = new Intent(TimeActivity.this, TestActivity.class);
                    startActivity(i);
                    finish();
                }


//                whistle = true;
//                Toast.makeText(this, "Whistle sensor activated", Toast.LENGTH_SHORT).show();

//                    MediaPlayer mPlayer = MediaPlayer.create(TimeActivity.this, R.raw.gun_sound);
//
//                    if (!mPlayer.isPlaying()) {
//                        mPlayer.start();
//                    } else {
//                        mPlayer.stop();
//                    }
//                    mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                        @Override
//                        public void onCompletion(MediaPlayer mp) {
//                            mp.reset();
//                            mp.release();
//
//
//                        }
//                    });

                break;

            case R.id.reset_btn:


//                boolean isFilled2 = CheckFields();
//
//                if (isFilled2) {
//                    showResetDialog();
//                } else {
//                    emptyStudentData();
//                    EnableTextView(true);
//                    ClearFields();
//                    if (studentStatus > 0) {
//                        //stop_split_timer_btn.setVisibility(View.GONE);
//                        timer_linear_layout.removeAllViews();
//                        timer_linear_layout.setVisibility(View.GONE);
//                        EnableDisableEditText(true);
//                        studentStatus = 0;
//                        stop_timer_btn.setText("STOP");
//                        student_ids.clear();
//                    }
//
//                    student_ids.clear();
//                    finish();
//                    startActivity(getIntent());
//                   stopAudioProcessing();
//                }


                checkDataFilledAndReset();

                break;

            case R.id.start_timer_btn:

                if (studentStatus == 0) {
                    stop_timer_btn.setText("STOP");
                } else if (studentStatus == 1) {
                    stop_timer_btn.setText("SPLIT");
                }

                EnableTextView(false);

                MediaPlayer mPlayer = MediaPlayer.create(TimeActivity.this, R.raw.gun_sound_two);

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
//
//                stop_timer_btn.setVisibility(View.VISIBLE);
//                start_timer_btn.setVisibility(View.INVISIBLE);
//                linear_layout.setVisibility(View.VISIBLE);
//

                if (studentStatus == 0) {
                    //     stop_split_timer_btn.setVisibility(View.GONE);
                    startTimer();
                    timer = true;
                } else {

                    // String no_of_student_txt = no_of_students.getText().toString().trim();
                    String no_of_student_txt = no_of_student;

                    if (no_of_student_txt.length() < 1) {
                        Toast.makeText(getApplicationContext(), "Please enter no of students", Toast.LENGTH_SHORT).show();
                    } else {
                        students = Integer.parseInt(no_of_student_txt);

                        if (students > 0) {

                            position_list.add("Select Position");

                            for (int i = 0; i < students; i++) {
                                position_list.add("" + (i + 1));
                            }

                            ArrayAdapter position_spin_adapter = new ArrayAdapter<String>(this, R.layout.position_item, position_list);
                            position_spin_adapter.setDropDownViewResource(R.layout.position_dropdown_item);
                            position_spinner.setAdapter(position_spin_adapter);

                            //    position_spinner.setVisibility(View.VISIBLE);

                            EnableDisableEditText(false);


                            startTimer();
                            timer = true;
                        } else {
                            Toast.makeText(getApplicationContext(), "Please enter no of students", Toast.LENGTH_SHORT).show();
                        }
                    }

                }


                break;

          /*  case R.id.stop_split_timer_btn:
                stopTimer();
                break;*/

            case R.id.stop_timer_btn:

                Log.e(TAG, "studentStatus==> " + studentStatus);
                //System.out.println("DEV TIMER " + minutes_tv.getText().toString());

                if (text_tv.getText().toString().equals("Enter 600 meter run/walk Score")) {
                    if (Integer.parseInt(minutes_tv.getText().toString()) != 00) {

                        if (studentStatus == 0) {
                            System.out.println("DEV TIMER 1 " + minutes_tv.getText().toString());
                            StopSound();

                            stopTimer();
                            //  timer=false;
                        } else {
                            System.out.println("DEV TIMER 2 " + minutes_tv.getText().toString());
                            Log.e(TAG, "noOfTimesSplit==> " + noOfTimesSplit);
                            Log.e(TAG, "students==> " + students);

                            if (noOfTimesSplit <= students) {
                                splitTimer(noOfTimesSplit);
                                if (noOfTimesSplit == students) {
                                    StopSound();
                                    stopTimer();
                                    // make scan buttons visible
                                    for (int i = 0; i < scan_Button_list.size(); i++) {
                                        scan_Button_list.get(i).setVisibility(View.VISIBLE);
                                        remove_Button_list.get(i).setVisibility(View.VISIBLE);
                                    }
                                }
                                noOfTimesSplit++;
                            } else {
                                StopSound();
                                stopTimer();
                            }

                        }

                    } else {
                        Toast.makeText(this, R.string.time_not_completed_60sec, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    if (studentStatus == 0) {
                        System.out.println("DEV TIMER 1 " + minutes_tv.getText().toString());
                        StopSound();

                        stopTimer();
                        //  timer=false;
                    } else {
                        System.out.println("DEV TIMER 2 " + minutes_tv.getText().toString());
                        Log.e(TAG, "noOfTimesSplit==> " + noOfTimesSplit);
                        Log.e(TAG, "students==> " + students);

                        if (noOfTimesSplit <= students) {
                            splitTimer(noOfTimesSplit);
                            if (noOfTimesSplit == students) {
                                StopSound();
                                stopTimer();
                                // make scan buttons visible
                                for (int i = 0; i < scan_Button_list.size(); i++) {
                                    scan_Button_list.get(i).setVisibility(View.VISIBLE);
                                    remove_Button_list.get(i).setVisibility(View.VISIBLE);
                                }
                            }
                            noOfTimesSplit++;
                        } else {
                            StopSound();
                            stopTimer();
                        }

                    }
                }


                break;

            case R.id.save_btn:

                try {
                    if (timer) {
                        stopTimer();
                        timer = false;
                    }


                    lat = gps.getLatitude();
                    lng = gps.getLongitude();


                    String DateTime = Constant.getDateTimeServer();
                    String deviceDateTime = Constant.getDateTime();

                    Log.e(TAG, "lat=> " + lat + "   lng=> " + lng);
                    Log.e(TAG, "DateTime=> " + DateTime);
                    Log.e(TAG, "deviceDateTime=> " + deviceDateTime);

                    String sec_txt = "", min_txt = "", mili_txt = "";

                    if (studentStatus == 0) {
                        sec_txt = seconds_tv.getText().toString();
                        min_txt = minutes_tv.getText().toString();
                        mili_txt = mili_seconds_tv.getText().toString();

                        SaveScores(sec_txt, min_txt, mili_txt, 0);
                        System.out.println("SINGH 1");
                    } else if (studentStatus == 1) {
                      /*  int pos = position_spinner.getSelectedItemPosition();
                        Log.e(TAG, "position_spinner=> " + pos);
                        if(pos==0){
                            Toast.makeText(getApplicationContext(),"Please select student position", Toast.LENGTH_SHORT).show();
                        } else {*/
                        System.out.println("SINGH 2");

                        if (studentList.size() > 0 && checkForStudentsListFilled()) {
                            System.out.println("SINGH 3");

                            showSaveDataInBulkDialog();
                        } else {

                            Toast.makeText(getApplicationContext(), R.string.please_scan, Toast.LENGTH_SHORT).show();
                        }


                        // }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.sub_test_name_tv:

                emptyStudentData();
                if (timer) {
                    timer = false;
                    //   stopTimer();

                }
                boolean isFilled1 = CheckFields();
                if (isFilled1) {
                    showExitDialog1();
                } else {
                    Intent i1 = new Intent(TimeActivity.this, ShowSubTestActivity.class);
                    startActivity(i1);
                    finish();
                }
                break;

            case R.id.scan_btn:
                if (timer) {
                    //stopTimer();
                }
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);
                else {
                    Intent i1 = new Intent(TimeActivity.this, ScanActivity.class);
                    startActivity(i1);
                }
                break;
        }
    }

    private void checkDataFilledAndReset() {

        boolean isFilled2 = CheckFields();

        if (isFilled2) {
            showResetDialog();
        } else {
            resetData();
        }

    }

    private void resetData() {
        emptyStudentData();
        EnableTextView(true);
        ClearFields();
        if (studentStatus > 0) {
            //stop_split_timer_btn.setVisibility(View.GONE);
            timer_linear_layout.removeAllViews();
            timer_linear_layout.setVisibility(View.GONE);
            EnableDisableEditText(true);
            studentStatus = 0;
            stop_timer_btn.setText("STOP TIMER");
            student_ids.clear();
        }

        student_ids.clear();
        finish();
        startActivity(getIntent());
        stopAudioProcessing();
    }


    private boolean checkForStudentsListFilled() {
        for (HashMap<String, String> map : studentList) {
            if (map.get(AppConfig.STUDENT_ID).equalsIgnoreCase(""))
                return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        try {
            if (requestCode == 0) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent i1 = new Intent(TimeActivity.this, ScanActivity.class);
                    startActivity(i1);
                } else
                    Utility.showPermissionDialog(this, getString(R.string.accept_permission, "Camera"));

            } else if (requestCode == 1) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startAudioProcessing();
                } else
                    Utility.showPermissionDialog(this, getString(R.string.accept_permission, "Audio Recording"));

            }
        } catch (Exception e) {
            Log.e("TIMEACTIVITY", e.getMessage());
        }

    }


    private void showSaveDataInBulkDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(TimeActivity.this);
        alertDialog.setCancelable(false);

        alertDialog.setMessage("Are you sure you want to save the data?");

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.alarm);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                //Toast.makeText(getApplicationContext(),"Please wait while we are saving the data.", Toast.LENGTH_SHORT).show();
                String sec_txt = "", min_txt = "", mili_txt = "";
                bulk_data_list = new ArrayList<>();
                jArray = new JSONArray();
                for (int i = 0; i < scoreList.size(); i++) {
                    try {
                        if (!student_ids.get(i).equalsIgnoreCase("-1")) {
                            sec_txt = scoreList.get(i).get("sec");
                            min_txt = scoreList.get(i).get("min");
                            mili_txt = scoreList.get(i).get("mili");

                            SaveScoresInBulk(sec_txt, min_txt, mili_txt, i);

                            System.out.println(" 17 " + scoreList.size() + " " + student_ids.get(i));
                        }
                    } catch (Exception e) {
                        System.out.println(" 16 " + e.getMessage());
                    }
                }

                student_ids.clear();

                //    callAPIInBulk();

            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();


    }


    private void callAPIInBulk() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("data", jArray.toString());
        TransactionRequest transactionRequest = new TransactionRequest(this, map, this);
        transactionRequest.hitUserRequest();
    }


    private void StopSound() {
        MediaPlayer mPlayerStop = MediaPlayer.create(TimeActivity.this, R.raw.whistle_sound);

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

    private void SaveScores(String sec_txt, String min_txt, String mili_txt, int pos) {
        int sec_int, min_int, mili_int;

        if (sec_txt.length() > 0) {
            sec_int = Integer.parseInt(sec_txt);
        } else {
            sec_int = 0;
        }

        if (min_txt.length() > 0) {
            min_int = Integer.parseInt(min_txt);
        } else {
            min_int = 0;
        }

        if (mili_txt.length() > 0) {
            mili_int = Integer.parseInt(mili_txt);
        } else {
            mili_int = 0;
        }


        if (sec_int < 1 && min_int < 1 && mili_int < 1) {

            Toast.makeText(getApplicationContext(), "Please fill all the fields", Toast.LENGTH_LONG).show();
        } else if (sec_int > 60) {
            Toast.makeText(getApplicationContext(), "seconds should not be greater then 60.", Toast.LENGTH_LONG).show();
        } else {
            double miliseconds = Constant.COnvertToMiliseconds(String.valueOf(min_int),
                    String.valueOf(sec_int), String.valueOf(mili_int));
            //   Log.e(TAG, "miliseconds=> " + miliseconds);
            // Toast.makeText(getApplicationContext(), "" + miliseconds, Toast.LENGTH_LONG).show();

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
                    String DateTime = Constant.getDateTimeServer();
                    String deviceDateTime = Constant.getDateTime();

                    showSaveDialog(miliseconds, DateTime, deviceDateTime, pos);
                } else {
                    if (seniorList.contains(Constant.CLASS_ID)) {

                        String DateTime = Constant.getDateTimeServer();
                        String deviceDateTime = Constant.getDateTime();

                        showSaveDialog(miliseconds, DateTime, deviceDateTime, pos);
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.not_for_juniors, Toast.LENGTH_SHORT).show();
                    }
                }


            } else {
                Toast.makeText(getApplicationContext(), R.string.please_scan, Toast.LENGTH_SHORT).show();


            }

        }
    }


    private void SaveScoresInBulk(String sec_txt, String min_txt, String mili_txt, int pos) {
        int sec_int, min_int, mili_int;

        if (sec_txt.length() > 0) {
            sec_int = Integer.parseInt(sec_txt);
        } else {
            sec_int = 0;
        }

        if (min_txt.length() > 0) {
            min_int = Integer.parseInt(min_txt);
        } else {
            min_int = 0;
        }

        if (mili_txt.length() > 0) {
            mili_int = Integer.parseInt(mili_txt);
        } else {
            mili_int = 0;
        }


        if (sec_int < 1 && min_int < 1 && mili_int < 1) {

            Toast.makeText(getApplicationContext(), "Please fill all the fields", Toast.LENGTH_LONG).show();
        } else if (sec_int > 60) {
            Toast.makeText(getApplicationContext(), "seconds should not be greater then 60.", Toast.LENGTH_LONG).show();
        } else {
            double miliseconds = Constant.COnvertToMiliseconds(String.valueOf(min_int),
                    String.valueOf(sec_int), String.valueOf(mili_int));
            Log.e(TAG, "miliseconds=> " + miliseconds);
            // Toast.makeText(getApplicationContext(), "" + miliseconds, Toast.LENGTH_LONG).show();

            try {
                if (studentList.get(pos).get("name").toString().length() > 0) {

                    ArrayList<String> seniorList = new ArrayList<>();

                    int cut_off = Integer.parseInt(Constant.SENIORS_STARTING_FROM);

                    if (seniorList.isEmpty()) {
                        for (int j = cut_off; j <= 12; j++) {

                            seniorList.add(String.valueOf(j));
                        }
                    }

                    if (seniorList.contains(studentList.get(pos).get("class_id"))) {

                        String DateTime = Constant.getDateTimeServer();
                        String deviceDateTime = Constant.getDateTime();

                        //  showSaveDialog(miliseconds, DateTime, deviceDateTime, pos);
                        System.out.println("SINGH 4");
                        showSaveDialogInBulk(miliseconds, DateTime, deviceDateTime, pos);


                    } else {
                        Toast.makeText(getApplicationContext(), R.string.not_for_juniors, Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(getApplicationContext(), R.string.please_scan, Toast.LENGTH_SHORT).show();


                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), R.string.please_scan, Toast.LENGTH_SHORT).show();
            }


        }
    }


    private void EnableDisableEditText(boolean b) {
        //  no_of_students.setEnabled(b);
        //  no_of_students.setFocusableInTouchMode(b);
        //  no_of_students.setClickable(b);
    }


    void insertTransactionData(double score, String date, String deviceDate, int pos) {
        try {
            // check whether student data for particular test exist or not

        /*"Student_ID":100,
                "Camp_ID":10,
                "Test_Type_ID":"2",
                "Test_Cordinator":"1",
                "Score":3390,
                "Percentile":11.52,
                "Created_by":"1",
                "Created_on":"2017-Jun-06",
                "SyncDateTime":"2017-06-18T11:00:00",
                "Longitude":"10.52",
                "Latitude":"24.63"*/

            model = new FitnessTestResultModel();
            if (studentStatus == 0) {
                model.setStudent_id(Integer.parseInt(Constant.STUDENT_ID));
            } else if (studentStatus == 1) {
                model.setStudent_id(Integer.parseInt(studentList.get(pos).get("student_id")));
            }
            model.setCamp_id(Integer.parseInt(Constant.CAMP_ID));
            model.setTest_type_id(Integer.parseInt(Constant.SUB_TEST_ID));
            model.setTest_coordinator_id(Integer.parseInt(Constant.TEST_COORDINATOR_ID));
            Double score_double = Double.parseDouble(String.valueOf(score));
            model.setScore("" + score_double.intValue());
            model.setPercentile(0);
            model.setCreated_on(date);
            model.setCreated_by(Constant.TEST_COORDINATOR_ID);
            model.setLast_modified_on(date);
            model.setLatitude(lat);
            model.setLongitude(lng);
            model.setDevice_date(deviceDate);
            model.setSubTestName(Constant.SUB_TEST_TYPE);
            model.setTestedOrNot(true);
            model.setSyncedOrNot(false);
            model.setTestName(Constant.TEST_TYPE);

            Log.e(TAG, "testname=> " + Constant.TEST_TYPE);
            Log.e(TAG, "sub testname=> " + Constant.SUB_TEST_TYPE);

            Log.e(TAG, "setCreated_on=> " + date);
            Log.e(TAG, "setLast_modified_on=> " + date);


            HashMap<String, String> map = null;
            try {
                map = db.getParticularRow(getApplicationContext(), testTableName,
                        "student_id", Constant.STUDENT_ID, "test_type_id", Constant.SUB_TEST_ID);
            } catch (Exception e) {
                System.out.println("SINGH 9 " + e.getMessage());

                e.printStackTrace();
            }

            if (map.size() == 0) {
                System.out.println("SINGH 20");

                db.insertTables(testTableName, model);
                start_timer_btn.setEnabled(true);
                start_timer_btn.setBackgroundResource(R.drawable.start_button);

                // After entering into local DB check for net connectivity and sync to server
                if (connectionDetector.isConnectingToInternet()) {
                    CallTransactionAPI();
                    System.out.println("SINGH 6 " + connectionDetector.isConnectingToInternet());
                } else {
                    System.out.println("SINGH 7 " + connectionDetector.isConnectingToInternet());
                    Toast.makeText(getApplicationContext(), R.string.data_saved_offline, Toast.LENGTH_SHORT).show();

                }


                showDataSavedSuccessfullyDialog(pos);

                // }
            } else {
                System.out.println("SINGH 19");

                String testedORNot = map.get("tested");
                String score1 = map.get("score");

                Log.e(TAG, "Previous score===> " + score1);
                if (testedORNot.equalsIgnoreCase("true")) {
                    showUpdateDialog(score, pos);
                } else if (testedORNot.equalsIgnoreCase("false")) {
                    db.deleteTransactionRow(TimeActivity.this, testTableName, "student_id", Constant.STUDENT_ID,
                            "test_type_id", Constant.SUB_TEST_ID, "camp_id", Constant.CAMP_ID);
                    db.insertTables(testTableName, model);
                    start_timer_btn.setEnabled(true);
                    start_timer_btn.setBackgroundResource(R.drawable.start_button);
                    showDataSavedSuccessfullyDialog(pos);

                    // After entering into local DB check for net connectivity and sync to server
                    if (connectionDetector.isConnectingToInternet()) {
                        CallTransactionAPI();
                        System.out.println("SINGH 10");
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.data_saved_offline, Toast.LENGTH_SHORT).show();
                        System.out.println("SINGH 11");
                    }


                }

            }
        } catch (Exception e) {
            System.out.println("TIME ACTIVITY a " + e.getMessage());
        }


    }


    void insertTransactionDataInBulk(double score, String date, String deviceDate, int pos) {

        String studentId = "";
        // check whether student data for particular test exist or not

        /*"Student_ID":100,
                "Camp_ID":10,
                "Test_Type_ID":"2",
                "Test_Cordinator":"1",
                "Score":3390,
                "Percentile":11.52,
                "Created_by":"1",
                "Created_on":"2017-Jun-06",
                "SyncDateTime":"2017-06-18T11:00:00",
                "Longitude":"10.52",
                "Latitude":"24.63"*/

        model = new FitnessTestResultModel();
//        if (studentStatus == 0) {
//            model.setStudent_id(Integer.parseInt(Constant.STUDENT_ID));
//        } else if (studentStatus == 1) {
//            model.setStudent_id(Integer.parseInt(studentList.get(pos).get("student_id")));
//        }
        if (studentStatus == 0) {
            studentId = Constant.STUDENT_ID;
        } else if (studentStatus == 1) {
            studentId = studentList.get(pos).get("student_id");
        }
        model.setStudent_id(Integer.parseInt(studentId));
        model.setCamp_id(Integer.parseInt(Constant.CAMP_ID));
        model.setTest_type_id(Integer.parseInt(Constant.SUB_TEST_ID));
        model.setTest_coordinator_id(Integer.parseInt(Constant.TEST_COORDINATOR_ID));
        Double score_double = Double.parseDouble(String.valueOf(score));
        model.setScore("" + score_double.intValue());
        model.setPercentile(0);
        model.setCreated_on(date);
        model.setCreated_by(Constant.TEST_COORDINATOR_ID);
        model.setLast_modified_on(date);
        model.setLatitude(lat);
        model.setLongitude(lng);
        model.setDevice_date(deviceDate);
        model.setSubTestName(Constant.SUB_TEST_TYPE);
        model.setTestedOrNot(true);
        model.setSyncedOrNot(false);
        model.setTestName(Constant.TEST_TYPE);
        bulk_data_list.add(model);

        Log.e(TAG, "testname=> " + Constant.TEST_TYPE);
        Log.e(TAG, "sub testname=> " + Constant.SUB_TEST_TYPE);

        Log.e(TAG, "setCreated_on=> " + date);
        Log.e(TAG, "setLast_modified_on=> " + date);


        HashMap<String, String> map = null;
        try {
            map = db.getParticularRow(getApplicationContext(), testTableName,
                    "student_id", studentId, "test_type_id", Constant.SUB_TEST_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (map.size() == 0) {
            db.insertTables(testTableName, model);
            start_timer_btn.setEnabled(true);
            start_timer_btn.setBackgroundResource(R.drawable.start_button);

            // After entering into local DB check for net connectivity and sync to server
          /*  if (connectionDetector.isConnectingToInternet()) {
                CallTransactionAPI();
            }*/

            //  showDataSavedSuccessfullyDialog(pos);


        } else {
           /* String testedORNot = map.get("tested");
            String score1 = map.get("score");

            Log.e(TAG, "Previous score===> " + score1);
            if (testedORNot.equalsIgnoreCase("true")) {
                showUpdateDialog(score, pos);
            } else if (testedORNot.equalsIgnoreCase("false")) {*/
            db.deleteTransactionRow(TimeActivity.this, testTableName, "student_id", studentId,
                    "test_type_id", Constant.SUB_TEST_ID, "camp_id", Constant.CAMP_ID);
            db.insertTables(testTableName, model);
            start_timer_btn.setEnabled(true);
            start_timer_btn.setBackgroundResource(R.drawable.start_button);
            //    showDataSavedSuccessfullyDialog(pos);

            // After entering into local DB check for net connectivity and sync to server
              /*  if (connectionDetector.isConnectingToInternet()) {
                    CallTransactionAPI();
                }*/
            //  }

        }

        putDataInArrayInBulk(pos);

    }

    private void putDataInArrayInBulk(int pos) {

        //global_pos = pos;

        HashMap<String, String> map_result = db.getParticularRow(getApplicationContext(), testTableName,
                "student_id", studentList.get(pos).get("student_id"), "test_type_id", Constant.SUB_TEST_ID);
//        if(global_pos==0)
//            jArray = new JSONArray();
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

        if (connectionDetector.isConnectingToInternet()) {
            if (NoOfStudents == jArray.length()) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("data", jArray.toString());
                map.put("AccessBy", "" + map_result.get("test_coordinator_id"));

                progressDialog.show();
                TransactionRequest transactionRequest = new TransactionRequest(this, map, this);
                transactionRequest.hitUserRequest();
            }
        } else {
            if (studentStatus == 1) {
                //Log.e(TAG,"global_pos==> "+global_pos);
                Log.e(TAG, "studentList size==> " + (studentList.size() - 1));
                if (NoOfStudents == jArray.length()) {


                    Log.e(TAG, "intent started");

                    emptyStudentData();
                    timer_linear_layout.removeAllViews();
                    timer_linear_layout.setVisibility(View.GONE);
                    EnableDisableEditText(true);
                    EnableTextView(true);
                    ClearFields();
                    Toast.makeText(getApplicationContext(), R.string.data_saved_offline, Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(getIntent());

                }
            }


        }


    }

    private void showDataSavedSuccessfullyDialog(final int pos) {
        System.out.println("SINGH 8");
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(TimeActivity.this);
        alertDialog.setCancelable(false);

        //   alertDialog.setMessage("This Student has already given the test. Do you want to update the test result?");


        if (studentStatus == 1) {
            alertDialog.setMessage("The " + Constant.SUB_TEST_TYPE + " scores for " + studentList.get(pos).get("name") + "" +
                    " has been saved in the database. To see the score again, you need to scan the student card.");
        } else if (studentStatus == 0) {
            alertDialog.setMessage("The " + Constant.SUB_TEST_TYPE + " scores for " + Constant.STUDENT_NAME + "" +
                    " has been saved in the database. To see the score again, you need to scan the student card.");
        }


        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                if (studentStatus == 1) {
                    start_timer_btn.setEnabled(false);
                    start_timer_btn.setBackgroundResource(R.drawable.disable_button);
                } else {
                    start_timer_btn.setEnabled(true);
                    start_timer_btn.setBackgroundResource(R.drawable.start_button);
                }

                dialog.cancel();

                ClearFields();
                if (studentStatus == 0) {
                    emptyStudentData();
                    EnableTextView(true);
                } else if (studentStatus == 1) {
                    if (pos == (studentList.size() - 1)) {
                        emptyStudentData();
                        timer_linear_layout.removeAllViews();
                        timer_linear_layout.setVisibility(View.GONE);
                        EnableDisableEditText(true);
                        EnableTextView(true);

                       /* finish();
                        startActivity(getIntent());*/

                    }
                }


            }
        });
        alertDialog.show();
    }

    private void emptyStudentData() {

        int size = tv_list.size();
        for (int i = 0; i < size; i++) {

            tv_list.get(i).setBackgroundResource(R.drawable.color_primary_box);
            tv_list.get(i).setTextColor(getResources().getColor(R.color.black));

        }

        noOfTimesSplit = 1;

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

        //  studentStatus = 0;
        StudentCount = 0;
    }

    private void showUpdateDialog(double score, final int pos) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(TimeActivity.this);
        alertDialog.setCancelable(false);
        currentScore = Constant.ConvertMilisToTime(score);

        //  alertDialog.setMessage("This Student has already given the test. Do you want to update the test result?");
//        alertDialog.setMessage(Constant.STUDENT_NAME + " has already taken the " + Constant.SUB_TEST_TYPE + "" +
//                " on " + device_date + "\n\n" + "Previous Score: " + previousTime + "\n\n" + "Current Score: " + currentScore + "\n\n" + " Do you want to update the test result?");
        alertDialog.setMessage(Constant.STUDENT_NAME + " has already taken the " + Constant.SUB_TEST_TYPE + "" +
                "\n\n" + "Previous Score: " + previousTime + "\n\n" + "Current Score: " + currentScore + "\n\n" + " Do you want to update the test result?");


        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
                db.deleteTransactionRow(TimeActivity.this, testTableName, "student_id", Constant.STUDENT_ID,
                        "test_type_id", Constant.SUB_TEST_ID, "camp_id", Constant.CAMP_ID);
                db.insertTables(testTableName, model);
                start_timer_btn.setEnabled(true);
                start_timer_btn.setBackgroundResource(R.drawable.start_button);
                showDataSavedSuccessfullyDialog(pos);

                // After entering into local DB check for net connectivity and sync to server
                if (connectionDetector.isConnectingToInternet()) {
                    CallTransactionAPI();
                    System.out.println("SINGH 12");

                } else
                    Toast.makeText(getApplicationContext(), R.string.data_saved_offline, Toast.LENGTH_SHORT).show();
                System.out.println("SINGH 13");

                // db.updateTables(db.TBL_LP_FITNESS_TEST_RESULT, model,"student_id",Constant.STUDENT_ID,"test_type_id", Constant.SUB_TEST_ID);
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                emptyStudentData();
                timer_linear_layout.removeAllViews();
                timer_linear_layout.setVisibility(View.GONE);
                EnableDisableEditText(true);
                EnableTextView(true);
                ClearFields();
            }
        });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private void CallTransactionAPI() {

        HashMap<String, String> map_result = db.getParticularRow(getApplicationContext(), testTableName,
                "student_id", Constant.STUDENT_ID, "test_type_id", Constant.SUB_TEST_ID);

        jArray = new JSONArray();
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
        minutes_tv.setText("");
        seconds_tv.setText("");
        mili_seconds_tv.setText("");

        minutes_tv.setHint("00");
        seconds_tv.setHint("00");
        mili_seconds_tv.setHint("00");

    }

    private void stopTimer() {
        stop_timer_btn.setVisibility(View.INVISIBLE);
        start_timer_btn.setVisibility(View.VISIBLE);
        reset_btn.setEnabled(true);
        reset_btn.setBackgroundResource(R.drawable.rectangle);
        save_btn.setEnabled(true);
        save_btn.setBackgroundResource(R.drawable.take_test_button);
//
        if (!save) {
            start_timer_btn.setEnabled(false);
            start_timer_btn.setBackgroundResource(R.drawable.disable_button);
        } else {
            start_timer_btn.setEnabled(true);
            start_timer_btn.setBackgroundResource(R.drawable.start_button);
        }


        customHandler.removeCallbacks(updateTimerThread);
    }

    private void splitTimer(int pos) {
        stop_timer_btn.setVisibility(View.VISIBLE);
        //start_timer_btn.setVisibility(View.VISIBLE);
        reset_btn.setEnabled(true);
        reset_btn.setBackgroundResource(R.drawable.rectangle);
        //save_btn.setEnabled(true);
        //save_btn.setBackgroundResource(R.drawable.take_test_button);
//
        if (!save) {
            start_timer_btn.setEnabled(false);
            start_timer_btn.setBackgroundResource(R.drawable.disable_button);
        } else {
            start_timer_btn.setEnabled(true);
            start_timer_btn.setBackgroundResource(R.drawable.start_button);
        }

        String mili = mili_seconds_tv.getText().toString();
        String min = minutes_tv.getText().toString();
        String sec = seconds_tv.getText().toString();

        int sec_int, min_int, mili_int;

        if (sec.length() > 0) {
            sec_int = Integer.parseInt(sec);
        } else {
            sec_int = 0;
        }

        if (min.length() > 0) {
            min_int = Integer.parseInt(min);
        } else {
            min_int = 0;
        }

        if (mili.length() > 0) {
            mili_int = Integer.parseInt(mili);
        } else {
            mili_int = 0;
        }

        AddViewToLinearLayout(mili_int, sec_int, min_int, sec, min, mili, pos);

        //  customHandler.removeCallbacks(updateTimerThread);
    }

    private void AddViewToLinearLayout(int mili_int, int sec_int, int min_int, String sec, String min,
                                       String mili, int pos) {
        try {
            final LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = vi.inflate(R.layout.timer_layout, null);

// fill in any details dynamically here
            TextView student_timer_tv = (TextView) v.findViewById(R.id.student_timer_tv);
            LinearLayout student_details_layout = (LinearLayout) v.findViewById(R.id.student_details_layout);
            Button student_scan_btn = (Button) v.findViewById(R.id.student_scan_btn);
            TextView student_name_tv = (TextView) v.findViewById(R.id.student_name_tv);
            TextView student_class_tv = (TextView) v.findViewById(R.id.student_class_tv);
            Button student_remove_btn = (Button) v.findViewById(R.id.student_remove_btn);
            TextView skipped_label = (TextView) v.findViewById(R.id.skipped_label);
            Button undo_skipped_btn = (Button) v.findViewById(R.id.undo_skipped_btn);

            student_timer_tv.setTypeface(font_reg);
            student_scan_btn.setTypeface(font_semi_bold);
            student_name_tv.setTypeface(font_reg);
            student_class_tv.setTypeface(font_reg);

            mTextViewList_Name.add(student_name_tv);
            mTextViewList_Class.add(student_class_tv);
            scan_Button_list.add(student_scan_btn);
            mLinearLaoyutList.add(student_details_layout);
            mButtonList.add(student_scan_btn);
            remove_Button_list.add(student_remove_btn);
            skipped_label_list.add(skipped_label);
            undoSkippedButtonsList.add(undo_skipped_btn);


            student_details_layout.setVisibility(View.GONE);
            final String lane = "Lane " + pos;
            final String scoreString = min + ":" + sec + ":" + mili;
            student_timer_tv.setText(lane + " " + scoreString);

            //student_timer_tv.setText(pos + ". " + "Score: " + min + ":" + sec + ":" + mili);

            student_scan_btn.setTag(pos);
            student_details_layout.setTag(pos);
            student_remove_btn.setTag(pos);
            undo_skipped_btn.setTag(pos);

            student_scan_btn.setVisibility(View.INVISIBLE);
            student_remove_btn.setVisibility(View.INVISIBLE);

// insert into main view
            timer_linear_layout.addView(v);

            HashMap<String, String> studentMap = new HashMap<>();
            studentMap.put("name", "");
            studentMap.put("student_id", "");
            studentMap.put("class_id", "");
            studentList.add(studentMap);
            student_ids.add("");

            student_scan_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (Integer) v.getTag();
                    scan_position = position;
                    Log.e("TimeActivity", "positionssss====> " + position);
                    if (timer) {
                        //stopTimer();
                    }
                    if (ContextCompat.checkSelfPermission(TimeActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                        ActivityCompat.requestPermissions(TimeActivity.this, new String[]{Manifest.permission.CAMERA}, 0);
                    else {
                        Intent i1 = new Intent(TimeActivity.this, ScanActivity.class);
                        startActivity(i1);
                    }

                }
            });

            student_details_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = (Integer) view.getTag();
                    scan_position = position;
                    Log.e("TimeActivity", "positionssss====> " + position);
                    if (timer) {
                        stopTimer();
                    }
                    startActivity(new Intent(TimeActivity.this, ScanActivity.class));
                }
            });

            student_remove_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (NoOfStudents > 1)
                        showSkipConfirmDialog(view, lane, scoreString);
                    else
                        checkDataFilledAndReset();
                }
            });

            undo_skipped_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = (int) view.getTag();

                    HashMap<String, String> map = new HashMap<>();
                    map.put(AppConfig.NAME_STRING, "");
                    map.put(AppConfig.STUDENT_ID, "");
                    map.put(AppConfig.CLASS_ID, "");
                    studentList.set(position - 1, map);
                    student_ids.set(position - 1, "");
                    //scoreList.remove(position-1);
                    NoOfStudents++;
                    skipped_label_list.get(position - 1).setVisibility(View.GONE);
                    undoSkippedButtonsList.get(position - 1).setVisibility(View.GONE);
                    scan_Button_list.get(position - 1).setVisibility(View.VISIBLE);
                    remove_Button_list.get(position - 1).setVisibility(View.VISIBLE);
                    mLinearLaoyutList.get(position - 1).setVisibility(View.GONE);
                }
            });

            double miliseconds = Constant.COnvertToMiliseconds(String.valueOf(min_int),
                    String.valueOf(sec_int), String.valueOf(mili_int));

            String DateTime = Constant.getDateTimeServer();
            String deviceDateTime = Constant.getDateTime();

            HashMap<String, String> map = new HashMap<String, String>();

            map.put("Score", "" + miliseconds);
            map.put("DateTime", DateTime);
            map.put("DeviceDateTime", deviceDateTime);
            map.put("sec", sec);
            map.put("min", min);
            map.put("mili", mili);

            scoreList.add(map);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }


    private void showSkipConfirmDialog(final View view, String lane, String score) {
        try {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(TimeActivity.this);
            alertDialog.setCancelable(false);
            alertDialog.setMessage("Are you sure you want to skip the " + lane + " score (" + score + ") ?");

            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    int position = (int) view.getTag();
                    //((ViewGroup) remove_Button_list.get(position - 1).getParent()).setVisibility(View.GONE);

                    //scoreList.get(position - 1).put(AppConfig.SCORE, "-1");
                    HashMap<String, String> map = new HashMap<>();
                    map.put(AppConfig.NAME_STRING, "-1");
                    map.put(AppConfig.STUDENT_ID, "-1");
                    map.put(AppConfig.CLASS_ID, "-1");
                    studentList.set(position - 1, map);
                    student_ids.set(position - 1, "-1");
                    //scoreList.remove(position-1);
                    NoOfStudents--;
                    skipped_label_list.get(position - 1).setVisibility(View.VISIBLE);
                    undoSkippedButtonsList.get(position - 1).setVisibility(View.VISIBLE);
                    scan_Button_list.get(position - 1).setVisibility(View.GONE);
                    remove_Button_list.get(position - 1).setVisibility(View.GONE);
                    mLinearLaoyutList.get(position - 1).setVisibility(View.GONE);
                    Constant.STUDENT_ID = "";
                    Constant.STUDENT_NAME = "";

                }

            });

            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();
        } catch (Exception e) {
            System.out.println("TimeActivity " + e.getMessage());
        }

    }

    private void startTimer() {

        stop_timer_btn.setVisibility(View.VISIBLE);
        start_timer_btn.setVisibility(View.INVISIBLE);
        reset_btn.setEnabled(false);
        reset_btn.setBackgroundResource(R.drawable.disable_button);
        linear_layout.setVisibility(View.VISIBLE);
        save_btn.setEnabled(false);
        save_btn.setBackgroundResource(R.drawable.disable_button);
        save = false;
        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);
//        test_another_img.setVisibility(View.INVISIBLE);

    }

    Runnable updateTimerThread = new Runnable() {

        public void run() {
            //
            // timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            // updatedTime = timeSwapBuff + timeInMilliseconds;

            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;

            int mins = (int) ((updatedTime / (1000 * 60)) % 60);
            int hr = (int) ((updatedTime / (1000 * 60 * 60)) % 24);
            int secs = (int) (updatedTime / 1000);
            // int mins = secs / 60;
            // int hr = mins / 60;

            int milliseconds = (int) (updatedTime % 1000);

            secs = secs % 60;
            if (!((secs + "").equals(seconds_tv.getText())))
                //    Log.e("AT Sec ", String.valueOf(secs) + "");

                minutes_tv.setText(String.format("%02d", mins));
            seconds_tv.setText(String.format("%02d", secs));
            mili_seconds_tv.setText(String.format("%02d", milliseconds));

           /* timer.setText("" + String.format("%02d", hr) + ":"
                    + String.format("%02d", mins) + ":"
                    + String.format("%02d", secs));*/
            customHandler.postDelayed(this, 0);
        }

    };


    public void onBackPressed() {


        boolean isFilled = CheckFields();

        if (timer) {
            timer = false;
            // stopTimer();

        }

        if (isFilled) {
            showExitDialog();
        } else {

            emptyStudentData();
            Intent i = new Intent(TimeActivity.this, ShowSubTestActivity.class);
            startActivity(i);
            finish();
        }


        //moveTaskToBack(true);

    }

    private boolean CheckFields() {

        String mins = minutes_tv.getText().toString();
        String secs = seconds_tv.getText().toString();
        String milisec = mili_seconds_tv.getText().toString();

        if (mins.length() > 0 || secs.length() > 0 || milisec.length() > 0) {
            return true;
        }

        return false;
    }

    private void showSaveDialog(final double miliseconds, final String DateTime, final String deviceDateTime, final int pos) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(TimeActivity.this);
        alertDialog.setCancelable(false);

        alertDialog.setMessage("Are you sure you want to save the data?");

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.alarm);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                save = true;
                //   test_another_img.setVisibility(View.VISIBLE);


//                if (studentStatus == 0) {
//                    linear_layout.setVisibility(View.GONE);
//                } else {
//                    linear_layout.setVisibility(View.VISIBLE);
//                }


                insertTransactionData(miliseconds, DateTime, deviceDateTime, pos);

            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    private void showSaveDialogInBulk(final double miliseconds, final String DateTime, final String deviceDateTime, final int pos) {
        System.out.println("SINGH 5");
        insertTransactionDataInBulk(miliseconds, DateTime, deviceDateTime, pos);


    }

    private void showExitDialog1() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(TimeActivity.this);
        alertDialog.setCancelable(false);
        alertDialog.setMessage(getResources().getString(R.string.exit_from_test));

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.alarm);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                Constant.ISComingFromTestScreen = true;
                dialog.cancel();
                Intent i1 = new Intent(TimeActivity.this, ShowSubTestActivity.class);
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

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(TimeActivity.this);
        alertDialog.setCancelable(false);
        alertDialog.setMessage(getResources().getString(R.string.exit_from_test));

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.alarm);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
                emptyStudentData();
                Intent i1 = new Intent(TimeActivity.this, TestActivity.class);
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

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(TimeActivity.this);
        alertDialog.setCancelable(false);
        alertDialog.setMessage(getResources().getString(R.string.exit_from_test));

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.alarm);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
                emptyStudentData();
                Intent i = new Intent(TimeActivity.this, ShowSubTestActivity.class);
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


    private void showResetDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(TimeActivity.this);
        alertDialog.setCancelable(false);
        alertDialog.setMessage(R.string.reset_data);

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.alarm);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();

                student_ids.clear();

                try {
                    if (timer) {
                        timer = false;

                    }
                    isStart = false;
                    minutes_tv.setText("");
                    seconds_tv.setText("");
                    mili_seconds_tv.setText("");

                    minutes_tv.setHint("00");
                    seconds_tv.setHint("00");
                    mili_seconds_tv.setHint("00");

                    stop_timer_btn.setVisibility(View.INVISIBLE);
                    start_timer_btn.setVisibility(View.VISIBLE);

                    EnableTextView(true);

                    if (studentStatus == 0) {
                        linear_layout.setVisibility(View.GONE);
                    } else {

                        linear_layout.setVisibility(View.VISIBLE);
                    }


                    //  test_another_img.setVisibility(View.VISIBLE);

                    start_timer_btn.setEnabled(true);
                    start_timer_btn.setBackgroundResource(R.drawable.start_button);

                    emptyStudentData();

                    if (studentStatus > 0) {
                        //stop_split_timer_btn.setVisibility(View.GONE);
                        timer_linear_layout.removeAllViews();
                        timer_linear_layout.setVisibility(View.GONE);
                        EnableDisableEditText(true);
                    }

                    studentStatus = 0;
                    stop_timer_btn.setText("STOP");


                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
                startActivity(getIntent());

                stopAudioProcessing();

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
    public void onResponse(Object obj) {

        if (obj instanceof TransactionModel) {

            TransactionModel model = (TransactionModel) obj;

            Log.e(TAG, "sucess==> " + model.getIsSuccess());
            Log.e(TAG, "message==> " + model.getMessage());

            if (model.getIsSuccess().trim().equalsIgnoreCase("true")) {


                if (studentStatus == 0) {
                    try {
                        db.deleteTransactionRow(this, testTableName, "student_id", "" + TimeActivity.this.model.getStudent_id(),
                                "test_type_id", "" + TimeActivity.this.model.getTest_type_id(), "camp_id", Constant.CAMP_ID);
                        this.model.setSyncedOrNot(true);
                        db.insertTables(testTableName, this.model);

                 /*   Log.e("TimeActivity", "getCreated_on==> " + this.model.getCreated_on());
                    Log.e("TimeActivity", "getLast_modified_on==> " + this.model.getLast_modified_on());*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    progressDialog.dismiss();
                    for (int i = 0; i < bulk_data_list.size(); i++) {
                        try {
                            FitnessTestResultModel fitnessTestResultModel = bulk_data_list.get(i);
                            db.deleteTransactionRow(this, testTableName, "student_id", "" + fitnessTestResultModel.getStudent_id(),
                                    "test_type_id", "" + fitnessTestResultModel.getTest_type_id(), "camp_id", Constant.CAMP_ID);
                            fitnessTestResultModel.setSyncedOrNot(true);
                            db.insertTables(testTableName, fitnessTestResultModel);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
         /*   Log.e("TimeActivity", "getCreated_on==> " + this.model.getCreated_on());
                    Log.e("TimeActivity", "getLast_modified_on==> " + this.model.getLast_modified_on());*/

                    Toast.makeText(this, model.getMessage(), Toast.LENGTH_SHORT).show();
                    emptyStudentData();
                    timer_linear_layout.removeAllViews();
                    timer_linear_layout.setVisibility(View.GONE);
                    EnableDisableEditText(true);
                    EnableTextView(true);
                    ClearFields();
                    finish();
                    startActivity(getIntent());


                }
            } else {
                if (progressDialog != null)
                    progressDialog.dismiss();
                Toast.makeText(this, getString(R.string.data_saved_offline), Toast.LENGTH_SHORT).show();
                System.out.println("SINGH 14");
                emptyStudentData();
                timer_linear_layout.removeAllViews();
                timer_linear_layout.setVisibility(View.GONE);
                EnableDisableEditText(true);
                EnableTextView(true);
                ClearFields();
                //finish();
                //  startActivity(getIntent());


            }
        } else {
            if (progressDialog != null)
                progressDialog.dismiss();
            Toast.makeText(this, getString(R.string.unable_connect_server), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopAudioProcessing();

    }
}
