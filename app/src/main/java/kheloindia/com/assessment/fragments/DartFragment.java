package kheloindia.com.assessment.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import kheloindia.com.assessment.AttendanceOfficeActivity;
import kheloindia.com.assessment.AttendanceSchoolActivity;
import kheloindia.com.assessment.SchoolActivity;
import kheloindia.com.assessment.service.LocationService;
import kheloindia.com.assessment.util.DBManager;
import kheloindia.com.assessment.R;
import kheloindia.com.assessment.TakeTestActivity;
import kheloindia.com.assessment.functions.Constant;

/**
 * Created by PC10 on 13-Nov-17.
 */

public class DartFragment extends Fragment implements View.OnClickListener {
    View rootView;
    LinearLayout view_attendence_layout, view_dart_board_layout, assessment_layout, mark_attendance_layout;
    ImageView view_attendence_img, mark_attendance_img, assessment_img, make_my_dart_img;
    SharedPreferences sp;

    boolean inSchoolActivity, isStudentDetailGet;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_dart, container, false);


        init();

        return rootView;
    }

    private void init() {

        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());

        inSchoolActivity = sp.getBoolean("inSchoolActivity", false);
        isStudentDetailGet = sp.getBoolean("isStudentDetailGet", false);

        view_attendence_img = (ImageView) rootView.findViewById(R.id.view_attendence_img);
        view_attendence_layout = (LinearLayout) rootView.findViewById(R.id.view_attendence_layout);
        mark_attendance_img = (ImageView) rootView.findViewById(R.id.mark_attendance_img);
        view_dart_board_layout = (LinearLayout) rootView.findViewById(R.id.view_dart_board_layout);
        assessment_layout = (LinearLayout) rootView.findViewById(R.id.assessment_layout);
        assessment_img = (ImageView) rootView.findViewById(R.id.assessment_img);
        mark_attendance_layout = (LinearLayout) rootView.findViewById(R.id.mark_attendance_layout);
        make_my_dart_img = (ImageView) rootView.findViewById(R.id.make_my_dart_img);

        view_attendence_img.setOnClickListener(this);
        view_attendence_layout.setOnClickListener(this);
        mark_attendance_img.setOnClickListener(this);
        view_dart_board_layout.setOnClickListener(this);
        assessment_layout.setOnClickListener(this);
        assessment_img.setOnClickListener(this);
        mark_attendance_layout.setOnClickListener(this);
        make_my_dart_img.setOnClickListener(this);

      //  TrackTrainer();
    }

    private void TrackTrainer() {
        long interval = 1000 * 60 * 15; // 15 minutes in milliseconds

        // hour = 36000 * 1000;

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 10);
        Intent intent = new Intent(getActivity(), LocationService.class);
        PendingIntent pintent = PendingIntent.getService(getActivity(), 0, intent,
                0);
     /*   PendingIntent pintent = PendingIntent.getBroadcast(DashBoardActivity.this, 0, intent,
                0);*/
        AlarmManager alarm = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
       /* alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                interval, pintent);*/
        alarm.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(),
                interval,pintent);
    }

    @Override
    public void onClick(View v) {

        if (v == mark_attendance_layout || v == mark_attendance_img) {
           // showTrainerActivityListDialog();

        }

      /* if(v==make_my_dart_layout || v==make_my_dart_img) {
           Intent i = new Intent(getActivity(), MakeDartActivity.class);
           startActivity(i);

       }*/
        else if (v == view_attendence_img || v == view_attendence_layout) {
//            Intent i = new Intent(getActivity(), ViewAttendanceActivity.class);
//            startActivity(i);
            Toast.makeText(getActivity(), R.string.auth_sect_unallow,Toast.LENGTH_LONG).show();
        }
        /*else if(v== view_dart_board_img || v==view_dart_board_layout){
            Intent i = new Intent(getActivity(), DartBoardActivity.class);
            startActivity(i);
        }*/
        else if (v == assessment_layout || v == assessment_img) {
            if (inSchoolActivity) {
                if (isStudentDetailGet) {
                  //  FetchDataFromDb();
                    if(!Constant.test_running && !Constant.skill_test_running) {
                        Intent i = new Intent(getActivity(), TakeTestActivity.class);
                        startActivity(i);
                    }
                    getActivity().finish();



                    /*if(isDataThere){
                       Intent i = new Intent(getActivity(), TakeTestActivity.class);
                       startActivity(i);
                       getActivity().finish();
                   } else {
                       Toast.makeText(getActivity(), "No student has been assigned to the school for the " +
                               "current session.Please confirm or upload student data.", Toast.LENGTH_LONG).show();
                   }*/
                } else {
                    Intent i = new Intent(getActivity(), SchoolActivity.class);
                    startActivity(i);
                    getActivity().finish();

                    // Toast.makeText(getApplicationContext(),"Please select School first to take the Tests.",Toast.LENGTH_SHORT).show();
                }

            } else {
                Intent i = new Intent(getActivity(), SchoolActivity.class);
                startActivity(i);
                getActivity().finish();

                // Toast.makeText(getApplicationContext(),"Please select School first to take the Tests.",Toast.LENGTH_SHORT).show();
            }
        }
        else if (v == view_dart_board_layout|| v == make_my_dart_img) {
//            if (inSchoolActivity) {
//                if (isStudentDetailGet) {
//
//                    Intent i = new Intent(getActivity(), TopSportsUpdateActivity.class);
//                    startActivity(i);
//                    } else {
//                    Intent i = new Intent(getActivity(), SchoolActivity.class);
//                    startActivity(i);
//                    getActivity().finish();
//
//                    // Toast.makeText(getApplicationContext(),"Please select School first to take the Tests.",Toast.LENGTH_SHORT).show();
//                }
//
//            } else {
//                Intent i = new Intent(getActivity(), SchoolActivity.class);
//                startActivity(i);
//                getActivity().finish();
//
//                // Toast.makeText(getApplicationContext(),"Please select School first to take the Tests.",Toast.LENGTH_SHORT).show();
//            }

            Toast.makeText(getActivity(), R.string.auth_sect_unallow,Toast.LENGTH_LONG).show();
        }
    }

    public void FetchDataFromDb() {

        Runnable r=new Runnable() {
            @Override
            public void run() {
                ArrayList<Object> objectArrayLst = DBManager.getInstance().getAllTableData(getActivity(), DBManager.TBL_LP_STUDENT_MASTER, "current_school_id", Constant.SCHOOL_ID,"","");

                if (objectArrayLst.size() > 0) {
                    SharedPreferences.Editor e = sp.edit();
                    e.putBoolean("isStudentDetailGet", true);
                    e.commit();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(getActivity(), TakeTestActivity.class);
                            startActivity(i);
                            getActivity().finish();
                        }
                    });

                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "No student has been assigned to the school for the current session.Please confirm or upload student data.", Toast.LENGTH_LONG).show();
                        }
                    });

                }

            }
        };
        new Thread(r).start();
    }

    private void showTrainerActivityListDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setTitle("Choose your activity");
        String[]activityList = {"Going to School", "Going to office", "Others"};
        builder.setItems(activityList, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Intent i = new Intent(getActivity(), AttendanceSchoolActivity.class);
                        Constant.GOING_TO = "School";
                        startActivity(i);
                        break;
                    case 1:
                       /* Intent i1 = new Intent(getActivity(), AttendanceGeofencingActivity.class);
                        Constant.GOING_TO = "Office";
                        i1.putExtra("school_name", "Fitness365");
                        i1.putExtra("school_id", "1172");
                        i1.putExtra("latitude", "28.413000");
                        i1.putExtra("longitude", "77.044059");
                        Constant.TRACKING_LATITUDE = "28.413000";
                        Constant.TRACKING_LONGITUDE = "77.044059";
                        Constant.TRACKING_SCHOOL_NAME = "Fitness365";
                        Constant.TRACKING_SCHOOL_ID = "1172";

                        // 1172 office id on live server
                        // different for diff server like staging or live

                        SharedPreferences.Editor e = sp.edit();
                        e.putString("tracking_latitude",Constant.TRACKING_LATITUDE);
                        e.putString("tracking_longitude",Constant.TRACKING_LONGITUDE);
                        e.commit();

                        startActivity(i1);*/

                        Intent i1 = new Intent(getActivity(), AttendanceOfficeActivity.class);
                        Constant.GOING_TO = "Office";
                        startActivity(i1);
                        break;
                    case 2:
                        Constant.GOING_TO = "Others";
                        dialog.dismiss();
                        break;

                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

