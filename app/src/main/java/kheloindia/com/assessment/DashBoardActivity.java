package kheloindia.com.assessment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jsoup.Jsoup;


import kheloindia.com.assessment.fragments.ActiveConnectFragment;
import kheloindia.com.assessment.fragments.DartFragment;
import kheloindia.com.assessment.fragments.liveplusFragment;
import kheloindia.com.assessment.functions.Constant;

/**
 * Created by PC10 on 06/27/2017.
 */

public class DashBoardActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CODE = 1 ;
    // LinearLayout assessment_layout, view_attendence_layout,view_dart_board_layout;
    //ImageView assessment_img, view_attendence_img,view_dart_board_img;
    boolean inSchoolActivity, isStudentDetailGet ;
    ImageView profile_img;
    TextView school_tv, welcome_student_tv;
    TextView dart_tv, active_connect_tv, liveplus_tv, notification_tv;
    Toolbar toolbar;
    SharedPreferences sp;

    ImageView dart_img, active_connect_img, liveplus_img, notification_img;
    LinearLayout dart_layout, student_connect_layout, liveplus_layout, notification_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }
        setContentView(R.layout.activity_trainer_home_screen);
        init();
    }

    private void init() {

        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        Constant.SCHOOL_NAME = sp.getString("school_name","");
        Constant.SCHOOL_ID = sp.getString("school_id","");
        Constant.TEST_COORDINATOR_ID = sp.getString("test_coordinator_id","");
        Constant.USER_TYPE = sp.getString("user_type","");

         inSchoolActivity = sp.getBoolean("inSchoolActivity", false);
        isStudentDetailGet = sp.getBoolean("isStudentDetailGet", false);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        profile_img = (ImageView) toolbar.findViewById(R.id.profile_img);
        school_tv = (TextView) toolbar.findViewById(R.id.school_tv);
        welcome_student_tv = (TextView) toolbar.findViewById(R.id.welcome_student_tv);

        liveplus_layout = (LinearLayout) findViewById(R.id.liveplus_layout);
        student_connect_layout = (LinearLayout) findViewById(R.id.student_connect_layout);
        dart_layout = (LinearLayout) findViewById(R.id.dart_layout);
        notification_layout = (LinearLayout) findViewById(R.id.notification_layout);

        dart_img = (ImageView) findViewById(R.id.dart_img);
        active_connect_img = (ImageView) findViewById(R.id.active_connect_img);
        liveplus_img = (ImageView) findViewById(R.id.liveplus_img);
        notification_img = (ImageView) findViewById(R.id.notification_img);

        dart_tv = (TextView) findViewById(R.id.dart_tv);
        active_connect_tv = (TextView) findViewById(R.id.active_connect_tv);
        liveplus_tv = (TextView) findViewById(R.id.liveplus_tv);
        notification_tv = (TextView) findViewById(R.id.notification_tv);

        liveplus_layout.setOnClickListener(this);
        student_connect_layout.setOnClickListener(this);
        dart_layout.setOnClickListener(this);
        notification_layout.setOnClickListener(this);
        dart_img.setOnClickListener(this);
        active_connect_img.setOnClickListener(this);
        liveplus_img.setOnClickListener(this);
        notification_img.setOnClickListener(this);

        setDefaultFragment();

        school_tv.setOnClickListener(this);
        profile_img.setOnClickListener(this);
        welcome_student_tv.setOnClickListener(this);
       /* view_attendence_layout.setOnClickListener(this);
        view_attendence_img.setOnClickListener(this);
        view_dart_board_img.setOnClickListener(this);
        view_dart_board_layout.setOnClickListener(this);
        assessment_layout.setOnClickListener(this);*/


        school_tv.setText(Constant.SCHOOL_NAME);
        welcome_student_tv.setText("Welcome " + sp.getString("test_coordinator_name", "ABC")+", You are at:");


        // Start service using AlarmManager

        /*long interval = 1000 * 60 * 15; // 15 minutes in milliseconds

        // hour = 36000 * 1000;

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 10);
        Intent intent = new Intent(DashBoardActivity.this, LocationService.class);
        PendingIntent pintent = PendingIntent.getService(DashBoardActivity.this, 0, intent,
                0);
     *//*   PendingIntent pintent = PendingIntent.getBroadcast(DashBoardActivity.this, 0, intent,
                0);*//*
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                interval, pintent);*/

/*
            Intent i = new Intent(getApplicationContext(), SendLocationAfterFixedIntervalReceiver.class);

            PendingIntent sender = PendingIntent.getBroadcast(getApplicationContext(),REQUEST_CODE, i, 0);

            // We want the alarm to go off 3 seconds from now.
            long firstTime = SystemClock.elapsedRealtime();
            firstTime += 3 * 1000;//start 3 seconds after first register.

            // Schedule the alarm!
            AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime,
                    600000, sender);//10min interval*/

//        try {
//            new GetVersionCode(this).execute();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private void setDefaultFragment() {
     /*   student_connect_layout.setBackgroundColor(Color.parseColor("#00ffffff"));
        dart_layout.setBackgroundColor(Color.parseColor("#d2d4d4"));
        liveplus_layout.setBackgroundColor(Color.parseColor("#00ffffff"));
        notification_layout.setBackgroundColor(Color.parseColor("#00ffffff"));*/

        dart_img.setImageResource(R.drawable.dart_i_o);
        active_connect_img.setImageResource(R.drawable.active_connect);
        liveplus_img.setImageResource(R.drawable.liveplus_i);
        notification_img.setImageResource(R.drawable.notifications_i);

        dart_tv.setTextColor(getResources().getColor(R.color.colorPrimary));
        active_connect_tv.setTextColor(getResources().getColor(R.color.grey));
        liveplus_tv.setTextColor(getResources().getColor(R.color.grey));
        notification_tv.setTextColor(getResources().getColor(R.color.grey));

        Fragment fragment = new DartFragment();
        CallFragment(fragment);
    }

    private void CallFragment(Fragment fragment) {
        FragmentManager fm = this.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (fragment != null) {
            ft.replace(R.id.frame_layout, fragment);
        } else {
            ft.add(R.id.frame_layout, fragment);
        }
        //ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onClick(View v) {
        /*if(v==assessment_layout){

            if(inSchoolActivity){
                if(isStudentDetailGet){

                    Intent i = new Intent(DashBoardActivity.this, TakeTestActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Intent i = new Intent(DashBoardActivity.this, SchoolActivity.class);
                    startActivity(i);
                    finish();

                   // Toast.makeText(getApplicationContext(),"Please select School first to take the Tests.",Toast.LENGTH_SHORT).show();
                }

            } else {
                Intent i = new Intent(DashBoardActivity.this, SchoolActivity.class);
                startActivity(i);
                finish();

                Toast.makeText(getApplicationContext(),"Please select School first to take the Tests.",Toast.LENGTH_SHORT).show();
            }
        }*/

         if(v==school_tv || v==welcome_student_tv) {

            Intent i1 = new Intent(DashBoardActivity.this, SchoolActivity.class);
            startActivity(i1);

        }
        else if(v==dart_layout || v== dart_img){

            /* student_connect_layout.setBackgroundColor(Color.parseColor("#00ffffff"));
             dart_layout.setBackgroundColor(Color.parseColor("#d2d4d4"));
             liveplus_layout.setBackgroundColor(Color.parseColor("#00ffffff"));
             notification_layout.setBackgroundColor(Color.parseColor("#00ffffff"));*/

             dart_img.setImageResource(R.drawable.dart_i_o);
             active_connect_img.setImageResource(R.drawable.active_connect);
             liveplus_img.setImageResource(R.drawable.liveplus_i);
             notification_img.setImageResource(R.drawable.notifications_i);

             dart_tv.setTextColor(getResources().getColor(R.color.colorPrimary));
             active_connect_tv.setTextColor(getResources().getColor(R.color.grey));
             liveplus_tv.setTextColor(getResources().getColor(R.color.grey));
             notification_tv.setTextColor(getResources().getColor(R.color.grey));

            Fragment fragment = new DartFragment();
            CallFragment(fragment);
        }

         else if(v==student_connect_layout || v== active_connect_img){

             /*student_connect_layout.setBackgroundColor(Color.parseColor("#d2d4d4"));
             dart_layout.setBackgroundColor(Color.parseColor("#00ffffff"));
             liveplus_layout.setBackgroundColor(Color.parseColor("#00ffffff"));
             notification_layout.setBackgroundColor(Color.parseColor("#00ffffff"));*/

             dart_img.setImageResource(R.drawable.dart_i);
             active_connect_img.setImageResource(R.drawable.active_connect_i_o);
             liveplus_img.setImageResource(R.drawable.liveplus_i);
             notification_img.setImageResource(R.drawable.notifications_i);

             dart_tv.setTextColor(getResources().getColor(R.color.grey));
             active_connect_tv.setTextColor(getResources().getColor(R.color.colorPrimary));
             liveplus_tv.setTextColor(getResources().getColor(R.color.grey));
             notification_tv.setTextColor(getResources().getColor(R.color.grey));

             /*Fragment fragment = new StudentConnectFragment();
             CallFragment(fragment);*/

             Fragment fragment = new ActiveConnectFragment();
             CallFragment(fragment);

             /*Constant.END_URL="?UserName="+sp.getString("test_coordinator_name", "")+"&Password="+sp.getString("test_coordinator_password", "");

             Constant.LOAD_URL = "http://23.253.243.116:92/Fitness365SchoolDashboard.aspx"+Constant.END_URL;

             Log.e("url=========>","url==========>  "+ Constant.LOAD_URL);

             Fragment fragment = new liveplusFragment();
             CallFragment(fragment);*/
         }

         else if(v==liveplus_layout || v== liveplus_img) {

            /* student_connect_layout.setBackgroundColor(Color.parseColor("#00ffffff"));
             dart_layout.setBackgroundColor(Color.parseColor("#00ffffff"));
             liveplus_layout.setBackgroundColor(Color.parseColor("#d2d4d4"));
             notification_layout.setBackgroundColor(Color.parseColor("#00ffffff"));*/

             dart_img.setImageResource(R.drawable.dart_i);
             active_connect_img.setImageResource(R.drawable.active_connect);
             liveplus_img.setImageResource(R.drawable.liveplus_i_o);
             notification_img.setImageResource(R.drawable.notifications_i);

             dart_tv.setTextColor(getResources().getColor(R.color.grey));
             active_connect_tv.setTextColor(getResources().getColor(R.color.grey));
             liveplus_tv.setTextColor(getResources().getColor(R.color.colorPrimary));
             notification_tv.setTextColor(getResources().getColor(R.color.grey));

             Constant.LOAD_URL = "http://liveplus.in";

             Fragment fragment = new liveplusFragment();
             CallFragment(fragment);
         }


        else if(v==profile_img) {
            Intent i1 = new Intent(DashBoardActivity.this, ProfileActivity.class);
            startActivity(i1);
        }

       /* else if(v== view_attendence_img || v== view_attendence_layout){
            Intent i = new Intent(DashBoardActivity.this, ViewAttendanceActivity.class);
            startActivity(i);
                    }
        else if(v== view_dart_board_img || v== view_dart_board_layout){
            Intent i = new Intent(DashBoardActivity.this, DartBoardActivity.class);
            startActivity(i);

        }*/
    }

    @Override
    public void onBackPressed() {

        showExitDIalog();
    }

    private void showExitDIalog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(DashBoardActivity.this);

        alertDialog.setMessage(getResources().getString(R.string.exit_from_app));

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.alarm);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {

                dialog.cancel();
               // finish();
               // moveTaskToBack(true);
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }



}
