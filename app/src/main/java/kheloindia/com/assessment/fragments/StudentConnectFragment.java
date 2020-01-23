package kheloindia.com.assessment.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import kheloindia.com.assessment.WebViewActivity;
import kheloindia.com.assessment.functions.Constant;
import kheloindia.com.assessment.R;

/**
 * Created by PC10 on 13-Nov-17.
 */

public class StudentConnectFragment extends Fragment implements View.OnClickListener{

    View rootView;
    LinearLayout insights_layout, top_sports_layout,shape_365_layout,as_program_layout,school_camps_layout, report_layout;

    ImageView insights_img, top_sport_img, shape365_img, as_program_img, school_camps_img, report_img;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_student_connect, container, false);


        init();

        return rootView;
    }

    private void init() {

        insights_layout=(LinearLayout) rootView.findViewById(R.id.insights_layout);
        top_sports_layout=(LinearLayout) rootView.findViewById(R.id.top_sports_layout);
        shape_365_layout=(LinearLayout) rootView.findViewById(R.id.shape_365_layout);
        as_program_layout=(LinearLayout) rootView.findViewById(R.id.as_program_layout);
        school_camps_layout=(LinearLayout) rootView.findViewById(R.id.school_camps_layout);
        report_layout=(LinearLayout) rootView.findViewById(R.id.report_layout);

        insights_img = (ImageView) rootView.findViewById(R.id.insights_img);
        top_sport_img = (ImageView) rootView.findViewById(R.id.top_sport_img);
        shape365_img = (ImageView) rootView.findViewById(R.id.shape365_img);
        as_program_img = (ImageView) rootView.findViewById(R.id.as_program_img);
        school_camps_img = (ImageView) rootView.findViewById(R.id.school_camps_img);
        report_img = (ImageView) rootView.findViewById(R.id.report_img);

        as_program_layout.setOnClickListener(this);
        insights_layout.setOnClickListener(this);
        top_sports_layout.setOnClickListener(this);
        shape_365_layout.setOnClickListener(this);
        school_camps_layout.setOnClickListener(this);
        report_layout.setOnClickListener(this);
        insights_img.setOnClickListener(this);
        top_sport_img.setOnClickListener(this);
        shape365_img.setOnClickListener(this);
        as_program_img.setOnClickListener(this);
        school_camps_img.setOnClickListener(this);
        report_img.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.insights_layout:
            case R.id.insights_img:
                String insights[]={"Partner Schools", "Overall Performance", "Agewise Performance"};
                showDialogInsightsList(getActivity(),insights);

                break;

            case R.id.top_sports_layout:
            case R.id.top_sport_img:
                String top_sports[]={getString(R.string.throw_ball),getString(R.string.kabaddi),getString(R.string.football),getString(R.string.kho_kho),getString(R.string.hand_ball),getString(R.string.cricket),getString(R.string.basket_ball),getString(R.string.volley_ball),getString(R.string.athletics),getString(R.string.gymnastics_aerobics),getString(R.string.badminton),getString(R.string.squash)};
                showDialogSportsList(getActivity(),top_sports);
                break;

            case R.id.shape_365_layout:
            case R.id.shape365_img:
                String shape365[]={"Curriculum Break-up", "SHAPE365 Activities"};
                showDialogShape365List(getActivity(),shape365);
                break;

            case R.id.as_program_layout:
            case R.id.as_program_img:
                String as_programs[]={"Curriculum","Lesson Index"};
                showDialogASProgramList(getActivity(),as_programs);
                break;


            case R.id.school_camps_layout:
            case R.id.school_camps_img:
                String school_camps[]={"Student","Fitness Assessment Camps"};
                showDialogSchoolCampList(getActivity(),school_camps);
                break;

            case R.id.report_img:
            case R.id.report_layout:
                String reports[]={"Camp Report","TOP Performer Certificate"};
                showDialogReportList(getActivity(),reports);
                break;

        }
    }

    private void showDialogInsightsList(Context context, final String[] items){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Constant.TITLE = items[which];
                Intent i = new Intent(getActivity(), WebViewActivity.class);
                switch (which) {
                    case 0: Constant.WEBVIEW_URL = "http://mydiary.fitness365.me/Fitness365SchoolDashboard.aspx";
                        startActivity(i);
                        break;

                    case 1: Constant.WEBVIEW_URL = "http://mydiary.fitness365.me/MyhomePage.aspx";
                        startActivity(i);
                        break;

                    case 2: Constant.WEBVIEW_URL = "http://mydiary.fitness365.me/ClasswiseDashboard.aspx";
                        startActivity(i);
                        break;

                    case 3: Constant.WEBVIEW_URL = "http://mydiary.fitness365.me/StudentReport.aspx";
                        startActivity(i);
                        break;

                    case 4: Constant.WEBVIEW_URL = "http://mydiary.fitness365.me/Toppers.aspx";
                        startActivity(i);
                        break;

                    case 5: Constant.WEBVIEW_URL = "http://mydiary.fitness365.me/Topconsisientperformers.aspx";
                        startActivity(i);
                        break;
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void showDialogSchoolCampList(Context context, final String[] items){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Constant.TITLE = items[which];
                Intent i = new Intent(getActivity(), WebViewActivity.class);

                switch (which) {
                    case 0:
                    String student[]={"Add Student", "Manage Student", "Student Data Upload","Student QR Code"};
                    showDialogStudentList(getActivity(),student);
                        break;
                    case 1:
                        String fitnessAssesmentCamp[]={"Manage Camps", "Test Data upload", "Download template",
                        "Upload image","Percentile Engine"};
                        showDialogfitnessAssesmentCampList(getActivity(),fitnessAssesmentCamp);
                        break;
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showDialogStudentList(Context context, final String[] items){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Constant.TITLE = items[which];
                Intent i = new Intent(getActivity(), WebViewActivity.class);

                switch (which) {
                    case 0:
                        Constant.WEBVIEW_URL = "http://mydiary.fitness365.me/AddStudent.aspx";
                        startActivity(i);
                        break;

                    case 1:
                        Constant.WEBVIEW_URL = "http://mydiary.fitness365.me/EditStudent.aspx";
                        startActivity(i);
                        break;
                    case 2:
                        Constant.WEBVIEW_URL = "http://mydiary.fitness365.me/UploadStudentData.aspx";
                        startActivity(i);
                        break;
                    case 3:
                        Constant.WEBVIEW_URL = "http://mydiary.fitness365.me/StudentQRCode.aspx";
                        startActivity(i);
                        break;
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void showDialogfitnessAssesmentCampList(Context context, final String[] items){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Constant.TITLE = items[which];
                Intent i = new Intent(getActivity(), WebViewActivity.class);

                switch (which) {
                    case 0:
                        Constant.WEBVIEW_URL = "http://mydiary.fitness365.me/AddCamp.aspx";
                        startActivity(i);
                        break;

                    case 1:
                        Constant.WEBVIEW_URL = "http://mydiary.fitness365.me/ExcelUpload.aspx";
                        startActivity(i);
                        break;
                    case 2:
                        Constant.WEBVIEW_URL = "http://mydiary.fitness365.me/ExcelDownload.aspx";
                        startActivity(i);
                        break;
                    case 3:
                        Constant.WEBVIEW_URL = "http://mydiary.fitness365.me/UploadImage.aspx";
                        startActivity(i);
                        break;
                    case 4:
                        break;
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void showDialogReportList(Context context, final String[] items){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Constant.TITLE = items[which];
                Intent i = new Intent(getActivity(), WebViewActivity.class);

                switch (which) {
                    case 0:  Constant.WEBVIEW_URL = "http://mydiary.fitness365.me/CampReport.aspx";
                        startActivity(i);
                        break;

                    case 1:
                        Constant.WEBVIEW_URL = "http://mydiary.fitness365.me/Errorpage.html?aspxerrorpath=/Topperformercertificate.aspx";
                        startActivity(i);
                        break;
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void showDialogShape365List(Context context, final String[] items){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Constant.TITLE = items[which];
                Intent i = new Intent(getActivity(), WebViewActivity.class);

                switch (which) {
                    case 0:  Constant.WEBVIEW_URL = "http://mydiary.fitness365.me/LessonPlanBreakupReport.aspx";
                        startActivity(i);
                        break;

                    case 1:
                        Constant.WEBVIEW_URL = "http://mydiary.fitness365.me/Shape365.aspx";
                        startActivity(i);
                        break;
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void showDialogASProgramList(Context context, final String[] items){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Constant.TITLE = items[which];
                Intent i = new Intent(getActivity(), WebViewActivity.class);

                switch (which) {
                    case 0:  Constant.WEBVIEW_URL = "http://mydiary.fitness365.me/ASCurriculum.aspx";
                        startActivity(i);
                        break;

                    case 1:
                        Constant.WEBVIEW_URL = "http://mydiary.fitness365.me/AllclassLessonplan.aspx";
                        startActivity(i);
                        break;
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void showDialogSportsList(Context context,final String[] items){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Constant.TITLE = items[which];
                Intent i = new Intent(getActivity(), WebViewActivity.class);

                switch (which) {
                    case 0: Constant.WEBVIEW_URL = "http://mydiary.fitness365.me/Top_Sports.aspx?id=1&sports=Throw%20Ball";
                        startActivity(i);
                        break;

                    case 1:  Constant.WEBVIEW_URL = "http://mydiary.fitness365.me/Top_Sports.aspx?id=2&sports=Kabaddi";
                        startActivity(i);
                        break;

                    case 2:  Constant.WEBVIEW_URL = "http://mydiary.fitness365.me/Top_Sports.aspx?id=3&sports=Football";
                        startActivity(i);
                        break;

                    case 3:  Constant.WEBVIEW_URL = "http://mydiary.fitness365.me/Top_Sports.aspx?id=4&sports=Kho%20Kho";
                        startActivity(i);
                        break;

                    case 4:  Constant.WEBVIEW_URL = "http://mydiary.fitness365.me/Top_Sports.aspx?id=5&sports=Handball";
                        startActivity(i);
                        break;

                    case 5:  Constant.WEBVIEW_URL = "http://mydiary.fitness365.me/Top_Sports.aspx?id=6&sports=Cricket";
                        startActivity(i);
                        break;

                    case 6:  Constant.WEBVIEW_URL = "http://mydiary.fitness365.me/Top_Sports.aspx?id=7&sports=Basketball";
                        startActivity(i);
                        break;

                    case 7:  Constant.WEBVIEW_URL = "http://mydiary.fitness365.me/Top_Sports.aspx?id=8&sports=Volleyball";
                        startActivity(i);
                        break;

                    case 8: Constant.WEBVIEW_URL = "http://mydiary.fitness365.me/Top_Sports.aspx?id=9&sports=Athletics";
                        startActivity(i);
                        break;

                    case 9:  Constant.WEBVIEW_URL = "http://mydiary.fitness365.me/Top_Sports.aspx?id=10&sports=Gymnastics/Aerobics";
                        startActivity(i);
                        break;

                    case 10:  Constant.WEBVIEW_URL = "http://mydiary.fitness365.me/Top_Sports.aspx?id=13&sports=Badminton";
                        startActivity(i);
                        break;

                    case 11:  Constant.WEBVIEW_URL = "http://mydiary.fitness365.me/Top_Sports.aspx?id=15&sports=Squash";
                        startActivity(i);
                        break;


                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
