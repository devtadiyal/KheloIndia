package kheloindia.com.assessment;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import java.util.HashMap;


import java.util.Date;
import java.util.List;


import kheloindia.com.assessment.app.Fitness365App;
import kheloindia.com.assessment.model.CampMasterModel;
import kheloindia.com.assessment.model.CampTestMapping;
import kheloindia.com.assessment.model.CampTestModel;
import kheloindia.com.assessment.model.FitnessSkillTestResultModel;
import kheloindia.com.assessment.model.FitnessTestCategoryModel;
import kheloindia.com.assessment.model.FitnessTestTypesModel;
import kheloindia.com.assessment.model.InsertActivityModel;
import kheloindia.com.assessment.model.MyDartInsertActivityModel;
import kheloindia.com.assessment.model.ReportModel;
import kheloindia.com.assessment.model.SchoolsMasterModel;
import kheloindia.com.assessment.model.SkillReportModel;
import kheloindia.com.assessment.model.SkillTestTypeModel;
import kheloindia.com.assessment.model.SkillTransactionModel;
import kheloindia.com.assessment.model.StudentModel;
import kheloindia.com.assessment.model.TestCoordinatorMapping;
import kheloindia.com.assessment.model.TestTypeModel;
import kheloindia.com.assessment.model.TransactionModel;
import kheloindia.com.assessment.model.UserModel;
import kheloindia.com.assessment.service.OnClearFromRecentService;
import kheloindia.com.assessment.util.AppConfig;
import kheloindia.com.assessment.util.ConnectionDetector;
import kheloindia.com.assessment.util.ConnectivityReceiverListener;
import kheloindia.com.assessment.util.DBManager;
import kheloindia.com.assessment.util.GetVersionCode;
import kheloindia.com.assessment.util.ProgressDialogUtility;
import kheloindia.com.assessment.util.ResponseListener;
import kheloindia.com.assessment.util.Utility;

import kheloindia.com.assessment.functions.Constant;

import kheloindia.com.assessment.model.FitnessTestResultModel;

import kheloindia.com.assessment.webservice.CampTestMappingRequest;
import kheloindia.com.assessment.webservice.GetSchoolRequest;
import kheloindia.com.assessment.webservice.InsertActivityRequest;
import kheloindia.com.assessment.webservice.StudentRequest;
import kheloindia.com.assessment.webservice.TestTypesRequest;

import static kheloindia.com.assessment.functions.Constant.getDateTimeServer1;

/**
 * Created by PC10 on 05/11/2017.
 */

public class TakeTestActivity extends AppCompatActivity implements View.OnClickListener, ResponseListener, ConnectivityReceiverListener {

    // ImageView sync_img;
    TextView school_tv, reTestLabel;
    TextView school_name_tv;
    TextView welcome_student_tv;
    TextView sync_data_tv;
    TextView no_of_students_tv;
    TextView synced_data_tv, sync_tv;
    TextView last_synced_time_tv, last_synced_tv;
    //TextView sync_tv;
    TextView export_tv, import_tv;
    Toolbar toolbar;
    Button take_test_btn, view_report_btn;
    SharedPreferences sp;
    private DBManager db;
    String TAG = "TakeTestActivity";
    private int sync_inc;
    private ProgressDialogUtility progressDialogUtility1, progressDialogUtility2, progressDialogUtility3, progressDialogUtility4;
    private ConnectionDetector connectionDetector;
    private ArrayList<Object> transactionsDataList = new ArrayList<>(), transactionTempList = new ArrayList<>(), transactionsSkillDataList = new ArrayList<>();
    private int syncedtransactionsDataListSize, syncedtransactionsSkillDataListSize, studentDataListSize;
    private FitnessTestResultModel model;
    private String max_test_date = "";
    private String max_skill_date = "";
    private int sync_data_count;
    private int synced_data_count;
    private String last_synced_time = "";
    private ArrayList myDartActivityList;
    private int activity_data_to_be_sent;
    private FloatingActionButton sync_img, sync_new_img, sync_all_img;
    private CoordinatorLayout coordinator_layout;
    private Snackbar snackbar_end1;
    private Snackbar snackbar_end2;
    private ImageView school_big_i;
    private boolean isDestroyed = false;
    // private ImageView profile_img;

    private BroadcastReceiver mMessageReceiver;
    private boolean isFABOpen;
    private ProgressDialog progressDoalog;
    StudentModel studentModel;
    boolean forRefreshing = false;
    boolean onlyForFetchingData = false;
    private RelativeLayout sync_all_rlt, sync_new_rlt;
    private TextView sync_new_tv, sync_all_tv;
    private LinearLayout containerLayout;
    AlertDialog.Builder alertDialogBuider;
    String schoolId;
    String forReTest, testTableName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_test);
        //   getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

      /*  if (!((LocationManager) getSystemService("location"))
                .isProviderEnabled("gps")) {
            showGPSDisabledAlertToUser();
        }*/
        init();

        mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                int extra = intent.getIntExtra("extra", 0);
                if (extra == 1) {
                    if (progressDialogUtility1 != null)
                        progressDialogUtility1.dismissProgressDialog();
                    synced_data_tv.setText("" + Constant.imported_count);
                    //sync_data_tv.setText("" + sync_data_count);
                    if (snackbar_end2 != null) {
                        Snackbar.make(coordinator_layout, R.string.sync_end3, Snackbar.LENGTH_INDEFINITE).setDuration(5000).show();
                        Utility.rotateFabBackward(sync_img);
                        snackbar_end1 = null;
                        snackbar_end2 = null;
                        if (isFABOpen)
                            closeFABMenu();
                    } else {
                        snackbar_end1 = Snackbar.make(coordinator_layout, R.string.sync_end1, Snackbar.LENGTH_INDEFINITE).setDuration(5000);
                        snackbar_end1.show();
                    }

                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("last_synced_time", Utility.getDate(System.currentTimeMillis(), "dd MMM yy, hh:mm a"));
                    editor.commit();
                    last_synced_time = sp.getString("last_synced_time", "");
                    last_synced_time_tv.setText(last_synced_time);
                } else if (extra == 2) {
                    if (progressDialogUtility2 != null)
                        progressDialogUtility2.dismissProgressDialog();
                    synced_data_tv.setText("" + Constant.imported_count);
                    if (snackbar_end1 != null) {
                        Snackbar.make(coordinator_layout, R.string.sync_end3, Snackbar.LENGTH_INDEFINITE).setDuration(5000).show();
                        Utility.rotateFabBackward(sync_img);
                        snackbar_end1 = null;
                        snackbar_end2 = null;
                        if (isFABOpen)
                            closeFABMenu();
                    } else {
                        snackbar_end2 = Snackbar.make(coordinator_layout, R.string.sync_end2, Snackbar.LENGTH_INDEFINITE).setDuration(5000);
                        snackbar_end2.show();
                    }
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("last_synced_time", Utility.getDate(System.currentTimeMillis(), "dd MMM yy, hh:mm a"));
                    editor.commit();
                    last_synced_time = sp.getString("last_synced_time", "");
                    last_synced_time_tv.setText(last_synced_time);

                } else if (extra == 3) {
                    //if (sync_data_count > transactionsDataList.size()) {
                    sync_data_count = sync_data_count - transactionsDataList.size();
                    sync_data_tv.setText("" + sync_data_count);
//                    } else {
//                        sync_data_tv.setVisibility(View.GONE);
//
//                    }
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("last_synced_time", Utility.getDate(System.currentTimeMillis(), "dd MMM yy, hh:mm a"));
                    editor.commit();
                    last_synced_time = sp.getString("last_synced_time", "");
                    last_synced_time_tv.setText(last_synced_time);

                    Utility.CallAPIToFetchAllReport(TakeTestActivity.this, max_test_date, TakeTestActivity.this);
                } else if (extra == 4) {
                    //if (sync_data_count > transactionsSkillDataList.size()) {
                    sync_data_count = sync_data_count - transactionsSkillDataList.size();
                    sync_data_tv.setText("" + sync_data_count);
//                    } else{
//                        sync_data_tv.setVisibility(View.GONE);
//
//                    }

                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("last_synced_time", Utility.getDate(System.currentTimeMillis(), "dd MMM yy, hh:mm a"));
                    editor.commit();
                    last_synced_time = sp.getString("last_synced_time", "");
                    last_synced_time_tv.setText(last_synced_time);
                    Utility.CallAPIToFetchAllSkillReport(TakeTestActivity.this, max_skill_date, TakeTestActivity.this);
                }
                // Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("intentKey"));
    }

    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(TakeTestActivity.this);
        localBuilder
                .setMessage(
                        "GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface paramAnonymousDialogInterface,
                                    int paramAnonymousInt) {

                                Intent localIntent2 = new Intent(
                                        "android.settings.LOCATION_SOURCE_SETTINGS");
                                startActivity(localIntent2);

                            }
                        });
        localBuilder.create().show();
    }


    private void init() {
        // activity=this;
        progressDialogUtility1 = new ProgressDialogUtility(TakeTestActivity.this);
        progressDialogUtility2 = new ProgressDialogUtility(TakeTestActivity.this);
        progressDialogUtility3 = new ProgressDialogUtility(TakeTestActivity.this);
        progressDialogUtility4 = new ProgressDialogUtility(TakeTestActivity.this);
        new GetVersionCode(this, null).execute(); // checking an update of app
        connectionDetector = new ConnectionDetector(TakeTestActivity.this);
        //db = DBManager.getInstance();
        db = new DBManager();
        Fitness365App.getInstance().setConnectivityListener(this);
        sp = PreferenceManager.getDefaultSharedPreferences(TakeTestActivity.this);
        //progressDialogUtility1 = new ProgressDialogUtility(TakeTestActivity.this);
        schoolId = sp.getString(AppConfig.SCHOOL_ID, "");
        forReTest = sp.getString(AppConfig.FOR_RETEST, "0");
        testTableName = DBManager.TBL_LP_FITNESS_TEST_RESULT;
        if (forReTest.equalsIgnoreCase("1"))
            testTableName = DBManager.TBL_LP_FITNESS_RETEST_RESULT;

        Constant.CAMP_ID = sp.getString("camp_id", "");
        Constant.SCHOOL_NAME = sp.getString("school_name", "");
        Constant.SCHOOL_ID = sp.getString("school_id", "");
        Constant.SENIORS_STARTING_FROM = sp.getString("seniors_starting_from", "");
        Constant.TEST_COORDINATOR_ID = sp.getString("test_coordinator_id", "");
        Constant.SCHOOL_IMAGE_PATH = sp.getString("school_image_path", "");

       /* ArrayList<Object> objectArrayLst = DBManager.getInstance().getAllTableData(this, DBManager.TBL_LP_STUDENT_MASTER, "current_school_id", Constant.SCHOOL_ID);
        StudentMasterModel student_model = (StudentMasterModel) objectArrayLst.get(0);
        Constant.CAMP_ID = student_model.getCamp_id();
        Log.e("SchoolActivity", "camp_id==> " + Constant.CAMP_ID);*/

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        school_tv = toolbar.findViewById(R.id.school_tv);
        welcome_student_tv = toolbar.findViewById(R.id.welcome_student_tv);
        take_test_btn = (Button) findViewById(R.id.take_test_btn);
        school_name_tv = (TextView) findViewById(R.id.school_name_tv);
        view_report_btn = (Button) findViewById(R.id.view_report_btn);
        sync_data_tv = (TextView) findViewById(R.id.sync_data_tv);
        no_of_students_tv = (TextView) findViewById(R.id.no_of_students_tv);
        synced_data_tv = (TextView) findViewById(R.id.synced_data_tv);
        last_synced_time_tv = (TextView) findViewById(R.id.last_synced_time_tv);
        sync_img = (FloatingActionButton) findViewById(R.id.sync_img);
        sync_new_img = (FloatingActionButton) findViewById(R.id.sync_new_img);
        sync_all_img = (FloatingActionButton) findViewById(R.id.sync_all_img);
        coordinator_layout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        sync_tv = (TextView) findViewById(R.id.sync_tv);
        school_big_i = (ImageView) findViewById(R.id.school_big_i);
        // profile_img=(ImageView)findViewById(R.id.profile_img);
        export_tv = (TextView) findViewById(R.id.export_tv);
        import_tv = (TextView) findViewById(R.id.import_tv);
        last_synced_tv = (TextView) findViewById(R.id.last_synced_tv);
        sync_new_rlt = (RelativeLayout) findViewById(R.id.sync_new_rlt);
        sync_all_rlt = (RelativeLayout) findViewById(R.id.sync_all_rlt);
        sync_new_tv = (TextView) findViewById(R.id.sync_new_tv);
        sync_all_tv = (TextView) findViewById(R.id.sync_all_tv);
        containerLayout = (LinearLayout) findViewById(R.id.container_layout);
        reTestLabel = (TextView) findViewById(R.id.retest_label);


        school_tv.setText(Constant.SCHOOL_NAME);
        school_name_tv.setText(Constant.SCHOOL_NAME);
        view_report_btn.setText(getString(R.string.view_report_string, sp.getString(AppConfig.CAMP_NAME, "")));
        welcome_student_tv.setText("Welcome " + sp.getString("username", "ABC") + ", You are at:");

        String path = getString(R.string.image_base_url) + Constant.SCHOOL_IMAGE_PATH;

        Picasso.with(TakeTestActivity.this).load(path).into(school_big_i);

        //  Picasso.with(TakeTestActivity.this).load(path).transform(new CircleTransformWhite()).into(school_big_i);
        Typeface font_semi_bold = Typeface.createFromAsset(getAssets(),
                "fonts/Barlow-SemiBold.ttf");
        Typeface font_reg = Typeface.createFromAsset(getAssets(),
                "fonts/Barlow-Regular.ttf");

        school_tv.setTypeface(font_reg);
        school_name_tv.setTypeface(font_reg);
        welcome_student_tv.setTypeface(font_reg);
        take_test_btn.setTypeface(font_semi_bold);
        view_report_btn.setTypeface(font_semi_bold);
        export_tv.setTypeface(font_reg);
        import_tv.setTypeface(font_reg);
        no_of_students_tv.setTypeface(font_reg);
        last_synced_time_tv.setTypeface(font_reg);
        //   sync_tv.setTypeface(font_reg);
        last_synced_tv.setTypeface(font_reg);

        take_test_btn.setOnClickListener(this);
        school_tv.setOnClickListener(this);
        welcome_student_tv.setOnClickListener(this);
        sync_data_tv.setOnClickListener(this);
        view_report_btn.setOnClickListener(this);
        sync_img.setOnClickListener(this);
        sync_tv.setOnClickListener(this);
        //  profile_img.setOnClickListener(this);
        sync_new_img.setOnClickListener(this);
        sync_all_img.setOnClickListener(this);
        sync_new_tv.setOnClickListener(this);
        sync_all_tv.setOnClickListener(this);

        if (forReTest.equalsIgnoreCase("1"))
            reTestLabel.setText(getString(R.string.potential_talent_identification));

    }

    private void showFABMenu() {
        sync_new_rlt.setVisibility(View.VISIBLE);
        sync_all_rlt.setVisibility(View.VISIBLE);
        sync_new_rlt.animate().translationY(-getResources().getDimension(R.dimen.sync_new));
        sync_all_rlt.animate().translationY(-getResources().getDimension(R.dimen.sync_all));
        //containerLayout.setBackgroundColor(getResources().getColor(R.color.background_opaque_color));
        isFABOpen = true;
    }


    private void closeFABMenu() {
        sync_new_rlt.animate().translationY(0);
        sync_all_rlt.animate().translationY(0);
        sync_new_rlt.setVisibility(View.INVISIBLE);
        sync_all_rlt.setVisibility(View.INVISIBLE);
        //containerLayout.setBackgroundColor(getResources().getColor(android.R.color.white));
        isFABOpen = false;
    }

    private void CallAPIForCampTestmapping() {
        if (connectionDetector.isConnectingToInternet()) {
            //progressDialogUtility.showProgressDialog();
            CampTestMappingRequest camptestMappingRequest = new CampTestMappingRequest(TakeTestActivity.this, Constant.CAMP_ID, Constant.TEST_COORDINATOR_ID, this);
            camptestMappingRequest.hitUserRequest();
        }
    }

    private void sendMyDartActivityToServer() {
        if (connectionDetector.isConnectingToInternet()) {
            Object obj = myDartActivityList.get(0);
            if (obj instanceof MyDartInsertActivityModel) {
                MyDartInsertActivityModel myDartInsertActivityModel = (MyDartInsertActivityModel) obj;
                InsertActivityRequest insertActivityRequest = new InsertActivityRequest(this, myDartInsertActivityModel.getData(), myDartInsertActivityModel.getActivity_id(), myDartInsertActivityModel.getStudent_activity_id(), this);
                insertActivityRequest.hitUserRequest();

            }
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.take_test_btn:
                if (Utility.checkForSchoolDeActivated(this, schoolId))
                    Utility.showSchoolDeactivatedDialog(this);
                else
                    ValidateTest();
                break;

            case R.id.logout_img:
                showLogoutDialog();
                break;

            case R.id.school_tv:
            case R.id.welcome_student_tv:
                if (connectionDetector.isConnectingToInternet())
                    passIntentToSchoolActivity();
                else {
                    ArrayList<Object> objectArrayList = DBManager.getInstance().getAllTableData(this, DBManager.TBL_LP_SCHOOLS_MASTER,
                            AppConfig.SCHOOL_ID, schoolId, "", "");
                    SchoolsMasterModel schoolsMasterModel = (SchoolsMasterModel) objectArrayList.get(0);
                    if (schoolsMasterModel.getIsRetestAllowed().equalsIgnoreCase("False"))
                        passIntentToSchoolActivity();
                    else
                        Toast.makeText(this, getString(R.string.enable_your_internet), Toast.LENGTH_SHORT).show();
                }
                break;

//            case R.id.profile_img:
//                Intent i2 = new Intent(TakeTestActivity.this, ProfileActivity.class);
//                startActivity(i2);
//                break;

//            case R.id.welcome_student_tv:
//
//                SharedPreferences.Editor e1 = sp.edit();
//                e1.putBoolean("fromTakeTest", true);
//                e1.commit();
//                Intent i3 = new Intent(TakeTestActivity.this, SchoolActivity.class);
//                startActivity(i3);
//                break;

            case R.id.view_report_btn:

                Intent i5 = new Intent(TakeTestActivity.this, TestStatusActivity.class);
                startActivity(i5);
                break;


            case R.id.sync_tv:
            case R.id.sync_img:
                //showSyncDialog();
                if (Utility.checkForSchoolDeActivated(this, schoolId))
                    Utility.showSchoolDeactivatedDialog(this);
                else {
                    if (!isFABOpen) {
                        showFABMenu();
                    } else {
                        closeFABMenu();
                    }
                }

                break;

            case R.id.sync_new_tv:
            case R.id.sync_new_img:
                if (Utility.checkForSchoolDeActivated(this, schoolId))
                    Utility.showSchoolDeactivatedDialog(this);
                else {
                    forRefreshing = false;
                    showSyncDialog();
                }
                break;

            case R.id.sync_all_tv:
            case R.id.sync_all_img:
                if (Utility.checkForSchoolDeActivated(this, schoolId))
                    Utility.showSchoolDeactivatedDialog(this);
                else
                    showSyncAllDialog();
                break;


        }
    }

    private void passIntentToSchoolActivity() {
        SharedPreferences.Editor e = sp.edit();
        e.putBoolean("fromTakeTest", true);
        e.commit();
        Intent i1 = new Intent(TakeTestActivity.this, SchoolActivity.class);
        startActivity(i1);
    }

    private void showSyncAllDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(TakeTestActivity.this);

        alertDialog.setMessage(getResources().getString(R.string.data_sync_all_message_alert));

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.alarm);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
                forRefreshing = true;
                getStudentList();
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

//    private void checkPermissions(){
//        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
//            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
//            }
//            else {
//                new DownloadDataToExport1(this,false).execute();
//                //new DownloadFileFromDownloadManager(this,intent.getExtras());
//
//            }
//        }
//        else
//            new DownloadDataToExport1(this,false).execute();
//            //new DownloadFileFromDownloadManager(this,intent.getExtras());
//
//    }
//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
//            new DownloadDataToExport1(this,false).execute();
//        else
//            Utility.showPermissionDialog(this,getString(R.string.accept_permission,"Storage"));
//
//    }

    private void ValidateTest() {

       /* HashMap<String, String> map = null ;
        try {
            map = db.getParticularRow(this,DBManager.TBL_LP_STUDENT_MASTER, "current_school_id", Constant.SCHOOL_ID, "current_roll_num", subString);
        }catch(Exception e){
            Toast.makeText(getApplicationContext(), "Code doest not exist in database.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        if(map.size()>0){
            Constant.STUDENT_NAME = map.get("student_name");
            Constant.STUDENT_CLASS = map.get("current_class");
            Constant.STUDENT_SECTION = map.get("section");
            Constant.STUDENT_DOB = map.get("dob");
            Constant.STUDENT_GENDER = map.get("gender");
            Constant.STUDENT_ID = map.get("student_id");
            Constant.CAMP_ID = map.get("camp_id");

            checkForTestANdNavigate();
        }*/
        final ProgressDialogUtility dialog = new ProgressDialogUtility(TakeTestActivity.this);
        dialog.setMessage(getString(R.string.tests_message_loader));
        dialog.showProgressDialog();

//
//        Runnable r = new Runnable() {
//            @Override
//            public void run() {
//        ArrayList<Object> objectArrayLst = DBManager.getInstance().getCampParticularRow(TakeTestActivity.this,
//                DBManager.TBL_LP_STUDENT_MASTER, "current_school_id", Constant.SCHOOL_ID, "", "");
        try {
//           StudentMasterModel model = (StudentMasterModel) objectArrayLst.get(0);
//
//           Constant.CAMP_ID = model.getCamp_id();

            if (Constant.CAMP_ID.length() > 0 && sp.getBoolean("got_all_test", false) && sp.getBoolean("got_all_camp", false)) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismissProgressDialog();
                        Intent intent = new Intent(TakeTestActivity.this, TestActivity.class);
                        startActivity(intent);
                    }
                });

            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismissProgressDialog();
                        Toast.makeText(getApplicationContext(), R.string.cannnot_take_test, Toast.LENGTH_LONG).show();
                    }
                });

            }
        /*try {
            ArrayList<HashMap<String, String>> campList = getCampList(Constant.SCHOOL_ID);
        } catch (Exception e){
            e.printStackTrace();
        }*/
        } catch (Exception e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dialog.dismissProgressDialog();
                    Toast.makeText(TakeTestActivity.this, "No Student found", Toast.LENGTH_SHORT).show();
                }
            });


        }
        //}
//        };
        //    new Thread(r).start();


    }

    private ArrayList<HashMap<String, String>> getCampList(String schoolId) {

        ArrayList<HashMap<String, String>> tempList = new ArrayList<HashMap<String, String>>();

        ArrayList<Object> objectArrayLst = DBManager.getInstance().getCampParticularRow(this,
                DBManager.TBL_LP_CAMP_MASTER, "school_id", Constant.SCHOOL_ID, "camp_coordination_id", Constant.COORDINATOR_ID);

        if (objectArrayLst.size() > 0) {
            for (int i = 0; i < objectArrayLst.size(); i++) {
                CampMasterModel cmm = (CampMasterModel) objectArrayLst.get(i);
                HashMap<String, String> map = new HashMap<String, String>();

                map.put("camp_id", "" + cmm.getCamp_id());
                map.put("camp_name", cmm.getCamp_name());
                map.put("camp_coordination_id", "" + cmm.getCamp_coordination_id());
                map.put("start_date", cmm.getStart_date());
                map.put("end_date", cmm.getEnd_date());


                String inputFormat = "yyyy-MM-dd HH:mm:ss.SSS";
                String outputFormat = "yyyy-MM-dd";
                String startDate = cmm.getStart_date();
                String endDate = cmm.getEnd_date();
                String currentDate = Constant.getCurrentDate(); // yyyy-MM-dd
                startDate = Constant.formateDateFromstring(inputFormat, outputFormat, startDate);
                endDate = Constant.formateDateFromstring(inputFormat, outputFormat, endDate);

                Date current_date = Constant.ConvertStringTODate(currentDate);
                Date start_date = Constant.ConvertStringTODate(startDate);
                Date end_date = Constant.ConvertStringTODate(endDate);

                Log.e("TakeTestActivity", "current_date===> " + current_date);
                Log.e("TakeTestActivity", "start_date===> " + start_date);
                Log.e("TakeTestActivity", "end_date===> " + end_date);

                boolean inRange = Constant.isWithinRange(current_date, start_date, end_date);

                Log.e("TakeTestActivity", "inRange===> " + inRange);

                if (inRange) {
                    tempList.add(map);
                    Constant.CAMP_ID = "" + cmm.getCamp_id();
                    Intent intent = new Intent(TakeTestActivity.this, TestActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "No Test is available for current date.", Toast.LENGTH_LONG).show();
                }
            }
            return tempList;
        } else {
            Toast.makeText(getApplicationContext(), "No Test is available for current school id and coordinator id..", Toast.LENGTH_LONG).show();
            return null;
        }
    }

    public void showLogoutDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(TakeTestActivity.this);
        alertDialog.setCancelable(false);
        alertDialog.setMessage("Are you sure you want to Logout?");

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.alarm);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
                SharedPreferences sp = PreferenceManager
                        .getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sp.edit();
                editor.remove("IS_LOGIN");
                editor.remove("inSchoolActivity");
                editor.remove("isStudentDetailGet");
                editor.commit();
                //sp.edit().clear().commit();
                Intent i = new Intent(TakeTestActivity.this, LoginActivity.class);

                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);

                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_NEW_TASK);

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


    class MyAdapter extends BaseAdapter {

        LayoutInflater mInflater = null;

        ArrayList<String> school_list = new ArrayList<String>();

        public MyAdapter(Context context,
                         ArrayList<String> list) {
            mInflater = LayoutInflater.from(getApplicationContext());
            school_list = list;

        }


        @Override
        public int getCount() {

            return school_list.size();
        }

        @Override
        public Object getItem(int position) {
            return school_list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent, R.layout.simple_spinner_item, true);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent, R.layout.simple_spinner_dropdown_item, false);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent, int spinnerRow, boolean isDefaultRow) {

            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(spinnerRow, parent, false);
            TextView txt = (TextView) row.findViewById(R.id.text);

            txt.setText(school_list.get(position));

            return row;
        }

    }

    public void onBackPressed() {

        boolean inSchoolActivity = sp.getBoolean("inSchoolActivity", false);

     /*   if (inSchoolActivity) {
            showExitDIalog();
        } else {
            finish();
        }*/

//        Intent i2 = new Intent(TakeTestActivity.this, DashBoardActivity.class);
//        startActivity(i2);
//        finish();

        //finish();
        //super.onBackPressed();
        // moveTaskToBack(true);
        showExitDIalog();


    }

    private void showExitDIalog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(TakeTestActivity.this);

        alertDialog.setMessage(getResources().getString(R.string.exit_from_app));

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.alarm);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
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


    private void showExportDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(TakeTestActivity.this);

        alertDialog.setMessage(getResources().getString(R.string.data_export_message_alert));

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.alarm);

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //checkPermissions();
                dialog.cancel();


            }
        });

        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    private void showSyncDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(TakeTestActivity.this);

        alertDialog.setMessage(getResources().getString(R.string.data_sync_message_alert));

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.alarm);

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
                if (connectionDetector.isConnectingToInternet()) {
                    // progressDialogUtility3 = new ProgressDialogUtility(TakeTestActivity.this);
                    // progressDialogUtility3.showProgressDialog();
                    if (!Constant.test_running && !Constant.skill_test_running) {
                        //progressDialogUtility1 = new ProgressDialogUtility(TakeTestActivity.this);
                        progressDialogUtility1.showProgressDialog();
                        //progressDialogUtility2 = new ProgressDialogUtility(TakeTestActivity.this);
                        progressDialogUtility2.showProgressDialog();
                        //progressDialogUtility3 = new ProgressDialogUtility(TakeTestActivity.this);
                        progressDialogUtility3.showProgressDialog();
                        //progressDialogUtility4 = new ProgressDialogUtility(TakeTestActivity.this);
                        progressDialogUtility4.showProgressDialog();
                        int fitnessTestCount = db.getTableCount(testTableName,
                                AppConfig.EXPORTED, "true");
                        onlyForFetchingData = fitnessTestCount > 0;
                        new SyncOnResume1().execute();

                    } else {
                        Snackbar.make(coordinator_layout, R.string.sync_going_on, Snackbar.LENGTH_INDEFINITE).setDuration(5000).show();
                    }


                } else {
                    Toast.makeText(TakeTestActivity.this, R.string.no_internet, Toast.LENGTH_LONG).show();
                    //showExportDialog();
                }

            }
        });

        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hitSchoolApiAndCheckForSchoolDeactivated();
    }

    @Override
    public void onNetworkConnectionChanged() {
        hitSchoolApiAndCheckForSchoolDeactivated();
    }

    private void hitSchoolApiAndCheckForSchoolDeactivated() {
        if (connectionDetector.isConnectingToInternet()) {
//            if (!Constant.test_running && !Constant.skill_test_running) {
//                Snackbar.make(coordinator_layout, R.string.sync_start, Snackbar.LENGTH_INDEFINITE).setDuration(5000).show();
//                Utility.rotateFabForward( this,sync_img);
//                new SyncOnResume1().execute();
//            }

//            if (checkForSchoolDeActivated() && !AppConfig.IS_SCHOOL_DEACTIVATED_DIALOG_SHOWING) {
//                GetSchoolRequest getSchoolRequest = new GetSchoolRequest(this, sp.getString(AppConfig.TEST_COORDINATOR_ID, ""), this);
//                getSchoolRequest.hitSchoolRequest();
//            }
            GetSchoolRequest getSchoolRequest = new GetSchoolRequest(this, sp.getString(AppConfig.TEST_COORDINATOR_ID, ""), this);
            getSchoolRequest.hitSchoolRequest();
        } else {
//            if (Utility.checkForSchoolDeActivated(this,schoolId)){
//                //Utility.showSchoolDeactivatedDialog(this);
//                if (!Constant.test_running && !Constant.skill_test_running) {
//                    progressDialogUtility1.showProgressDialog();
//                    onlyForFetchingData = true;
//                    new SyncOnResume1().execute();
//                }
//            }
//            else {
//                new SyncOnResume2().execute();
//                if (!AppConfig.IS_STATE_UPDATION_DIALOG_SHOWING && (sp.getInt(AppConfig.STATE_ID,0)==0 || sp.getString(AppConfig.CITY_NAME,"").equals("") || sp.getString(AppConfig.DISTRICT_NAME,"").equals("") || sp.getString(AppConfig.BLOCK_NAME,"").equals("")))
//                    Utility.showStateCityUpdateDialog(this);
//            }
            new SyncOnResume2().execute();
            if (!AppConfig.IS_STATE_UPDATION_DIALOG_SHOWING && (sp.getInt(AppConfig.STATE_ID, 0) == 0 || sp.getString(AppConfig.CITY_NAME, "").equals("") || sp.getString(AppConfig.DISTRICT_NAME, "").equals("") || sp.getString(AppConfig.BLOCK_NAME, "").equals("")))
                Utility.showStateCityUpdateDialog(this);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestroyed = true;
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        if (progressDialogUtility1 != null)
            progressDialogUtility1.dismissProgressDialog();
        if (progressDialogUtility2 != null)
            progressDialogUtility2.dismissProgressDialog();
        if (progressDialogUtility3 != null)
            progressDialogUtility3.dismissProgressDialog();
        if (progressDialogUtility4 != null)
            progressDialogUtility4.dismissProgressDialog();

    }

    @Override
    public void onResponse(Object obj) {

        new LongOperation(obj).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }


    private void FetchList() {

        Constant.TEST_ID = "0";
        if (connectionDetector.isConnectingToInternet()) {

            ArrayList<Object> objectArrayListTestType = DBManager.getInstance().getAllTableDataTest(this, DBManager.TBL_LP_FITNESS_TEST_TYPES, "", "", "", "");

            ArrayList<Object> objectArrayListTestCategory = DBManager.getInstance().getAllTableData1(this, DBManager.TBL_LP_FITNESS_TEST_CATEGORY, "", "", "", "");

            ArrayList<Object> objectArrayListTestSkills = DBManager.getInstance().getAllTableData1(this, DBManager.TBL_LP_SKILL_TEST_TYPE, "", "", "", "");


            String ModifiedDateCatagory = "";
            String ModifiedDateType = "";
            String ModifiedDateSkill = "";

            if (objectArrayListTestType.size() > 0) {
                ModifiedDateCatagory = DBManager.getInstance().getMaxDate(this, DBManager.TBL_LP_FITNESS_TEST_TYPES, "last_modified_on", Constant.CAMP_ID);

            }

            if (objectArrayListTestCategory.size() > 0) {
                ModifiedDateType = DBManager.getInstance().getMaxDate(this, DBManager.TBL_LP_FITNESS_TEST_CATEGORY, "last_modified_on", Constant.CAMP_ID);

            }

            if (objectArrayListTestSkills.size() > 0) {
                ModifiedDateSkill = DBManager.getInstance().getMaxDate(this, DBManager.TBL_LP_SKILL_TEST_TYPE, "last_modified_on", Constant.CAMP_ID);

            }


            Log.e(TAG, "ModifiedDateCatagory=> " + ModifiedDateCatagory);
            Log.e(TAG, "ModifiedDateType=> " + ModifiedDateType);
            Log.e(TAG, "ModifiedDateSkill=> " + ModifiedDateSkill);
            //     progressDialogUtility.showProgressDialog();
            TestTypesRequest testRequest = new TestTypesRequest(this, Constant.TEST_ID, ModifiedDateCatagory,
                    ModifiedDateType, ModifiedDateSkill, this);
            testRequest.hitUserRequest();

        }
    }


    private class SyncOnResume1 extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            Constant.test_running = true;
            Constant.skill_test_running = true;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (forRefreshing) {
                max_test_date = max_skill_date = "";
            } else {
                if (!onlyForFetchingData) {
                    transactionsDataList = DBManager.getInstance().getAllTableData(TakeTestActivity.this,
                            testTableName, "tested", "true", "synced", "false");
                    //size=transactionsDataList.size();
                    transactionsSkillDataList = DBManager.getInstance().getAllTableData(TakeTestActivity.this,
                            db.TBL_LP_FITNESS_SKILL_TEST_RESULT, "tested", "true", "last_modified_on", "0");
                    myDartActivityList = DBManager.getInstance().getAllTableData(TakeTestActivity.this, DBManager.TBL_LP_MY_DART_INSERT_ACTIVITY_TABLE, "current_school_id", Constant.SCHOOL_ID, "", "");
                }
                try {
                    max_test_date = DBManager.getInstance().getMaxDate(TakeTestActivity.this, testTableName, "last_modified_on", Constant.CAMP_ID);
                } catch (Exception e) {
                    max_test_date = "";
                    e.printStackTrace();
                }
                try {
                    max_skill_date = DBManager.getInstance().getMaxDate(TakeTestActivity.this, DBManager.TBL_LP_FITNESS_SKILL_TEST_RESULT, "last_modified_on", Constant.CAMP_ID);
                } catch (Exception e) {
                    max_skill_date = "";
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            new SyncOnResume2().execute();
            if (connectionDetector.isConnectingToInternet()) {
                if (!onlyForFetchingData && transactionsDataList.size() > 0) {

                    //progressDialogUtility.showProgressDialog();

                    Utility.syncTransactionsAPI(TakeTestActivity.this, transactionsDataList, TakeTestActivity.this);
                    //sendDataInChunks(10);


                } else {
                    // progressDialogUtility.showProgressDialog();

                    Utility.CallAPIToFetchAllReport(TakeTestActivity.this, max_test_date, TakeTestActivity.this);
                }

            }


            if (connectionDetector.isConnectingToInternet()) {
                if (!onlyForFetchingData && transactionsSkillDataList.size() > 0) {
//                                int size = transactionsSkillDataList.size();
//                                transactionSkillTempList=new ArrayList<>();
                    // progressDialogUtility.showProgressDialog();

                    Utility.syncSkillTransactionAPI(TakeTestActivity.this, transactionsSkillDataList, TakeTestActivity.this);


//                                if(size>=200){
//                                    for (int i=0;i<size;i++){
//                                        transactionSkillTempList.add(transactionsSkillDataList.get(i));
//                                        int tempListSize = transactionSkillTempList.size();
//                                        if(tempListSize>=200){
//                                            Utility.syncSkillTransactionAPI(TakeTestActivity.this, transactionSkillTempList, TakeTestActivity.this);
//                                        }
//                                    }
//                                } else {
//                                    transactionSkillTempList=transactionsSkillDataList;
//                                    Utility.syncSkillTransactionAPI(TakeTestActivity.this,transactionSkillTempList, TakeTestActivity.this);
//
//
//                                }


                } else {
                    //   progressDialogUtility.showProgressDialog();

                    Utility.CallAPIToFetchAllSkillReport(TakeTestActivity.this, max_skill_date, TakeTestActivity.this);
                }


            }
            if (!sp.getBoolean("got_all_camp", false)) {
                CallAPIForCampTestmapping();
            } else if (progressDialogUtility3 != null)
                progressDialogUtility3.dismissProgressDialog();

            if (!sp.getBoolean("got_all_test", false)) {
                FetchList();
            } else if (progressDialogUtility4 != null)
                progressDialogUtility4.dismissProgressDialog();

            if (activity_data_to_be_sent == 0 && myDartActivityList != null && myDartActivityList.size() != 0)
                sendMyDartActivityToServer();


        }

    }


    private class SyncOnResume2 extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            //studentDataListSize = DBManager.getInstance().getTotalCount(TakeTestActivity.this, DBManager.TBL_LP_STUDENT_MASTER, "current_school_id", Constant.SCHOOL_ID, "", "","","");
            studentDataListSize = DBManager.getInstance().getTotalCount(TakeTestActivity.this, DBManager.TBL_LP_STUDENT_MASTER, AppConfig.CAMP_ID, sp.getString(AppConfig.CAMP_ID, ""), "", "", "", "");

            syncedtransactionsDataListSize = DBManager.getInstance().getTotalCount(TakeTestActivity.this,
                    testTableName, "tested", "true", "synced", "true", "camp_id", Constant.CAMP_ID);
            syncedtransactionsSkillDataListSize = DBManager.getInstance().getTotalCount(TakeTestActivity.this,
                    db.TBL_LP_FITNESS_SKILL_TEST_RESULT, "tested", "true", "synced", "1", "camp_id", Constant.CAMP_ID);
//            if(!connectionDetector.isConnectingToInternet()) {
//                transactionsDataList = DBManager.getInstance().getAllTableData(TakeTestActivity.this,
//                        db.TBL_LP_FITNESS_TEST_RESULT, "tested", "true", "synced", "false");
//                transactionsSkillDataList = DBManager.getInstance().getAllTableData(TakeTestActivity.this,
//                        db.TBL_LP_FITNESS_SKILL_TEST_RESULT, "tested", "true", "last_modified_on", "0");
//                myDartActivityList = DBManager.getInstance().getAllTableData(TakeTestActivity.this, DBManager.TBL_LP_MY_DART_INSERT_ACTIVITY_TABLE, "current_school_id", Constant.SCHOOL_ID, "", "");
//            }
            transactionsDataList = DBManager.getInstance().getAllTableData(TakeTestActivity.this,
                    testTableName, "tested", "true", "synced", "false");
            transactionsSkillDataList = DBManager.getInstance().getAllTableData(TakeTestActivity.this,
                    db.TBL_LP_FITNESS_SKILL_TEST_RESULT, "tested", "true", "last_modified_on", "0");
            myDartActivityList = DBManager.getInstance().getAllTableData(TakeTestActivity.this, DBManager.TBL_LP_MY_DART_INSERT_ACTIVITY_TABLE, "current_school_id", Constant.SCHOOL_ID, "", "");

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            synced_data_count = syncedtransactionsDataListSize + syncedtransactionsSkillDataListSize;
            no_of_students_tv.setText(getString(R.string.no_of_students) + studentDataListSize);
            sync_data_count = transactionsDataList.size() + transactionsSkillDataList.size();
//            if (sync_data_count != 0) {
            sync_data_tv.setVisibility(View.VISIBLE);
            sync_data_tv.setText("" + sync_data_count);

//            } else {
//                sync_data_tv.setVisibility(View.GONE);
//            }

            last_synced_time = sp.getString("last_synced_time", "");
            if (!last_synced_time.equals(""))
                last_synced_time_tv.setText(last_synced_time);
            // if (synced_data_count != 0) {
            synced_data_tv.setText("" + synced_data_count);
            // }

        }

    }

    private class LongOperation extends AsyncTask<Void, Void, Void> {
        private Object object;

        LongOperation(Object obj) {
            object = obj;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            longOperation(object);

            return null;
        }
    }

    private void longOperation(final Object obj) {
        if (obj instanceof TransactionModel) {

            TransactionModel model = (TransactionModel) obj;

            if (model.getIsSuccess().equalsIgnoreCase("true")) {

//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {

                Constant.fitnessTestResultModels.clear();
                for (Object o : transactionsDataList) {

                    FitnessTestResultModel fitnessTestResultModel = (FitnessTestResultModel) o;
                    db.deleteTransactionRow(TakeTestActivity.this, testTableName, "student_id", "" + fitnessTestResultModel.getStudent_id(),
                            "test_type_id", "" + fitnessTestResultModel.getTest_type_id(), "camp_id", Constant.CAMP_ID);
                    fitnessTestResultModel.setTestedOrNot(true);
                    fitnessTestResultModel.setSyncedOrNot(true);
                    Constant.fitnessTestResultModels.add(fitnessTestResultModel);
                }
                Intent intent = new Intent(this, OnClearFromRecentService.class);
                intent.putExtra("fitnessTestResultModel", 3);
                OnClearFromRecentService.enqueueWork(this, intent);


            } else {
                Constant.test_running = false;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (progressDialogUtility1 != null)
                            progressDialogUtility1.dismissProgressDialog();
                        if (!Constant.test_running && !Constant.skill_test_running)
                            Utility.rotateFabBackward(sync_img);
                        //showExportDialog();

                    }
                });

            }

        } else if (obj instanceof CampTestModel) {

            db.dropNamedTable(this, db.TBL_LP_CAMP_TEST_MAPPING);
            db.dropNamedTable(this, db.TBL_LP_TEST_COORDINATOR_MAPPING);

            db.createNamedTable(this, db.TBL_LP_CAMP_TEST_MAPPING);
            db.createNamedTable(this, db.TBL_LP_TEST_COORDINATOR_MAPPING);


            CampTestModel model = (CampTestModel) obj;

            if (model.getIsSuccess().equalsIgnoreCase("true")) {

//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {

                List<CampTestModel.ResultBean.CampWiseTestBean> camptestList = ((CampTestModel) obj).getResult().getCampWiseTest();
                try {
                    for (int i = 0; i < camptestList.size(); i++) {
                        CampTestModel.ResultBean.CampWiseTestBean campTestBean = camptestList.get(i);
                        CampTestMapping camptestMappingModel = new CampTestMapping();
                        camptestMappingModel.setSchool_id(Integer.parseInt(Constant.SCHOOL_ID));
                        camptestMappingModel.setCamp_id(Integer.parseInt(Constant.CAMP_ID));
                        camptestMappingModel.setTest_type_id(Integer.parseInt(campTestBean.getTest_Type_ID()));
                        camptestMappingModel.setClass_ID(campTestBean.getClass_ID());

                        //  Log.e(TAG, "camp test size=> " + camptestList.size());

                        db.deleteTransactionRow(TakeTestActivity.this, db.TBL_LP_CAMP_TEST_MAPPING, "camp_id", Constant.CAMP_ID,
                                "test_type_id", campTestBean.getTest_Type_ID(), "camp_id", Constant.CAMP_ID);

                        db.insertTables(db.TBL_LP_CAMP_TEST_MAPPING, camptestMappingModel);

                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (progressDialogUtility3 != null)
                                progressDialogUtility3.dismissProgressDialog();
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putBoolean("got_all_camp", true);
                            editor.commit();
                        }
                    });

                } catch (Exception e) {


                }
                List<CampTestModel.ResultBean.CoordinatorWiseTestBean> coordinatortestList = ((CampTestModel) obj).getResult().getCoordinatorWiseTest();
                try {
                    for (int i = 0; i < coordinatortestList.size(); i++) {
                        CampTestModel.ResultBean.CoordinatorWiseTestBean coordinatortestBean = coordinatortestList.get(i);
                        TestCoordinatorMapping coordinatorMappingModel = new TestCoordinatorMapping();
                        coordinatorMappingModel.setSchool_id(Integer.parseInt(Constant.SCHOOL_ID));
                        coordinatorMappingModel.setCamp_id(Integer.parseInt(Constant.CAMP_ID));
                        coordinatorMappingModel.setTest_type_id(Integer.parseInt(coordinatortestBean.getTest_Type_ID()));

                              /*  Log.e(TAG, "camp id=> " + Constant.CAMP_ID);
                                Log.e(TAG, "school id==> " + Constant.SCHOOL_ID);
                                Log.e(TAG, "test type id==>" + coordinatortestBean.getTest_Type_ID());*/

                        db.deleteTransactionRow(TakeTestActivity.this, db.TBL_LP_TEST_COORDINATOR_MAPPING, "camp_id", Constant.CAMP_ID,
                                "test_type_id", coordinatortestBean.getTest_Type_ID(), "camp_id", Constant.CAMP_ID);

                        db.insertTables(db.TBL_LP_TEST_COORDINATOR_MAPPING, coordinatorMappingModel);
                               /* Log.e(TAG, "camp id=> " + Constant.CAMP_ID);
                                Log.e(TAG, "school id==> " + Constant.SCHOOL_ID);
                                Log.e(TAG, "test type id==>" + coordinatortestBean.getTest_Type_ID());*/

                    }


                } catch (Exception e) {


                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //progressDialogUtility.dismissProgressDialog();
                    }
                });
//                    }
//                }).start();


            } else {
                if (progressDialogUtility3 != null)
                    progressDialogUtility3.dismissProgressDialog();
            }
        } else if (obj instanceof ReportModel) {
            ReportModel model = (ReportModel) obj;

            Log.e(TAG, "getIsSuccess=> " + model.getIsSuccess());
            Log.e(TAG, "message=> " + model.getMessage());
            Log.e(TAG, "result=> " + model.getResult());

            if (model.getIsSuccess().equalsIgnoreCase("true")) {

                if (forRefreshing) {
                    db.truncateTable(testTableName);
                    forRefreshing = false;
                }
                Constant.fitnessTestResultModels.clear();
                final List<ReportModel.ResultBean.StudentBean> studentBeanList = ((ReportModel) obj).getResult().getStudent();
                for (int i = 0; i < studentBeanList.size(); i++) {
                    ReportModel.ResultBean.StudentBean studentBean = studentBeanList.get(i);
                    FitnessTestResultModel fitnessTestResultModel = new FitnessTestResultModel();
                    fitnessTestResultModel.setStudent_id(Integer.parseInt(studentBean.getStudentID()));
                    fitnessTestResultModel.setCamp_id(Integer.parseInt(studentBean.getCampID()));
                    fitnessTestResultModel.setTest_type_id(Integer.parseInt(studentBean.getTestTypeID()));
                    fitnessTestResultModel.setTest_coordinator_id(Integer.parseInt(studentBean.getTestCoordinatorID()));
                    fitnessTestResultModel.setScore(studentBean.getScore());
                    fitnessTestResultModel.setPercentile(Double.valueOf(studentBean.getPercentile()));
                    fitnessTestResultModel.setCreated_on(studentBean.getCreatedOn());
                    fitnessTestResultModel.setCreated_by(studentBean.getCreatedBy());
                    fitnessTestResultModel.setLast_modified_by(studentBean.getLastModifiedBy());
                    fitnessTestResultModel.setLast_modified_on(studentBean.getLastModifiedOn());
                    fitnessTestResultModel.setDevice_date(studentBean.getCreatedOnDevice());
                    fitnessTestResultModel.setLatitude(0.0);
                    fitnessTestResultModel.setLongitude(0.0);
                    fitnessTestResultModel.setSubTestName("");
                    fitnessTestResultModel.setTestName("");
                    fitnessTestResultModel.setSyncedOrNot(true);
                    fitnessTestResultModel.setTestedOrNot(true);
                    if (!forRefreshing) {
                        db.deleteTransactionRow(TakeTestActivity.this, testTableName, "student_id", "" + fitnessTestResultModel.getStudent_id(),
                                "test_type_id", "" + fitnessTestResultModel.getTest_type_id(), "camp_id", Constant.CAMP_ID);

                    }
                    Constant.fitnessTestResultModels.add(fitnessTestResultModel);
                }

                Intent intent = new Intent(this, OnClearFromRecentService.class);
                intent.putExtra("fitnessTestResultModel", 1);
                OnClearFromRecentService.enqueueWork(this, intent);

            } else {
                Constant.test_running = false;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (progressDialogUtility1 != null)
                            progressDialogUtility1.dismissProgressDialog();
                        if (snackbar_end2 != null) {
                            Snackbar.make(coordinator_layout, R.string.sync_end3, Snackbar.LENGTH_INDEFINITE).setDuration(5000).show();
                            Utility.rotateFabBackward(sync_img);
                            snackbar_end1 = null;
                            snackbar_end2 = null;
                        } else {
                            snackbar_end1 = Snackbar.make(coordinator_layout, R.string.sync_end1, Snackbar.LENGTH_INDEFINITE).setDuration(5000);
                            snackbar_end1.show();
                        }

                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("last_synced_time", Utility.getDate(System.currentTimeMillis(), "dd MMM yy, hh:mm a"));
                        editor.commit();
                        last_synced_time = sp.getString("last_synced_time", "");
                        last_synced_time_tv.setText(last_synced_time);
                    }
                });

//                        }
//                    });


            }
        } else if (obj instanceof SkillReportModel) {
            SkillReportModel model = (SkillReportModel) obj;

            if (model.getIsSuccess().equalsIgnoreCase("true")) {

                if (forRefreshing) {
                    db.truncateTable(DBManager.TBL_LP_FITNESS_SKILL_TEST_RESULT);
                    forRefreshing = false;
                }
                Constant.fitnessSkillTestResultModels.clear();
                final List<SkillReportModel.ResultBean.StudentBean> studentBeanList = ((SkillReportModel) obj).getResult().getStudent();

                for (int j = 0; j < studentBeanList.size(); j++) {

                    SkillReportModel.ResultBean.StudentBean studentBean = studentBeanList.get(j);
                    FitnessSkillTestResultModel fitnessTestResultModel = new FitnessSkillTestResultModel();
                    fitnessTestResultModel.setSynced(true);
                    fitnessTestResultModel.setStudent_id(Integer.parseInt(studentBean.getFKStudentID()));
                    fitnessTestResultModel.setCamp_id(Integer.parseInt(studentBean.getFKCampID()));
                    fitnessTestResultModel.setTest_type_id(Integer.parseInt(studentBean.getFKTestTypeID()));
                    fitnessTestResultModel.setCoordinator_id(Integer.parseInt(studentBean.getFKTestCoordinatorID()));
                    fitnessTestResultModel.setScore(studentBean.getScore());
                    fitnessTestResultModel.setSkill_score_id(0);
                    fitnessTestResultModel.setCreated_on(studentBean.getCreatedOn());
                    fitnessTestResultModel.setLatitude("");
                    fitnessTestResultModel.setLongitude("");
                    fitnessTestResultModel.setChecklist_item_id(Integer.parseInt(studentBean.getFKCheckListItemID()));
                    // fitnessTestResultModel.setLast_modified_on(studentBean.getLastModifiedOn());
                    fitnessTestResultModel.setLast_modified_on(studentBean.getLastModifiedOn());
                    fitnessTestResultModel.setTested(true);
                    //                            if(db.getTotalCount(TakeTestActivity.this,DBManager.TBL_LP_FITNESS_SKILL_TEST_RESULT,"student_id", "" + fitnessTestResultModel.getStudent_id(),
//                                    "checklist_item_id", "" + fitnessTestResultModel.getChecklist_item_id(),"camp_id",Constant.CAMP_ID)!=0)

                    if (!forRefreshing) {
                        db.deleteTransactionRow(TakeTestActivity.this, db.TBL_LP_FITNESS_SKILL_TEST_RESULT, "student_id", "" + fitnessTestResultModel.getStudent_id(),
                                "checklist_item_id", "" + fitnessTestResultModel.getChecklist_item_id(), "camp_id", Constant.CAMP_ID);

                    }

                    //  db.insertTables(db.TBL_LP_FITNESS_SKILL_TEST_RESULT, fitnessTestResultModel);
                    Constant.fitnessSkillTestResultModels.add(fitnessTestResultModel);
                    //}
                }
                Intent intent = new Intent(this, OnClearFromRecentService.class);
                intent.putExtra("fitnessTestResultModel", 2);
                OnClearFromRecentService.enqueueWork(this, intent);

            } else {
                Constant.skill_test_running = false;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (progressDialogUtility2 != null)
                            progressDialogUtility2.dismissProgressDialog();
                        if (snackbar_end1 != null) {
                            Snackbar.make(coordinator_layout, R.string.sync_end3, Snackbar.LENGTH_INDEFINITE).setDuration(5000).show();
                            Utility.rotateFabBackward(sync_img);
                            snackbar_end1 = null;
                            snackbar_end2 = null;
                        } else {
                            snackbar_end2 = Snackbar.make(coordinator_layout, R.string.sync_end2, Snackbar.LENGTH_INDEFINITE).setDuration(5000);
                            snackbar_end2.show();
                        }

                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("last_synced_time", Utility.getDate(System.currentTimeMillis(), "dd MMM yy, hh:mm a"));
                        editor.commit();
                        last_synced_time = sp.getString("last_synced_time", "");
                        last_synced_time_tv.setText(last_synced_time);
                    }
                });

            }
        } else if (obj instanceof SkillTransactionModel) {

            SkillTransactionModel model = (SkillTransactionModel) obj;

            if (model.getIsSuccess().equalsIgnoreCase("true")) {

//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
                Constant.fitnessSkillTestResultModels.clear();
                for (Object o : transactionsSkillDataList) {

                    FitnessSkillTestResultModel fitnessTestResultModel = (FitnessSkillTestResultModel) o;
                    db.deleteTransactionRow(TakeTestActivity.this, db.TBL_LP_FITNESS_SKILL_TEST_RESULT, "student_id", "" + fitnessTestResultModel.getStudent_id(),
                            "checklist_item_id", "" + fitnessTestResultModel.getChecklist_item_id(), "camp_id", Constant.CAMP_ID);
                    fitnessTestResultModel.setTested(true);
                    fitnessTestResultModel.setLast_modified_on(getDateTimeServer1());
                    Constant.fitnessSkillTestResultModels.add(fitnessTestResultModel);
                }
                Intent intent = new Intent(this, OnClearFromRecentService.class);
                intent.putExtra("fitnessTestResultModel", 4);
                OnClearFromRecentService.enqueueWork(this, intent);
            } else {
                Constant.skill_test_running = false;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (progressDialogUtility2 != null)
                            progressDialogUtility2.dismissProgressDialog();
                        if (!Constant.test_running && !Constant.skill_test_running)
                            Utility.rotateFabBackward(sync_img);
                    }
                });

            }
        } else if (obj instanceof TestTypeModel) {

            final TestTypeModel testModel = (TestTypeModel) obj;

            if (testModel.getIsSuccess().equalsIgnoreCase("true")) {

                //  db.dropNamedTable(this, db.TBL_LP_FITNESS_TEST_TYPES);


                //   db.dropNamedTable(this, db.TBL_LP_FITNESS_TEST_CATEGORY);


                final List<TestTypeModel.ResultBean.TestBean> testsList = testModel.getResult().getTest();
                final FitnessTestTypesModel model = new FitnessTestTypesModel();

//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {

                Log.e(TAG, "testsList==> " + testsList);
                Log.e(TAG, "testsList==> " + testsList.size());

                for (int i = 0; i < testsList.size(); i++) {
                    TestTypeModel.ResultBean.TestBean testBean = testsList.get(i);
                    model.setTest_name(testBean.getTest_Name());
                    model.setTest_type_id(testBean.getTest_ID());
                    model.setTest_image(testBean.getTest_Image());
                    model.setTest_img_path(testBean.getTest_Img_path());
                    model.setCreated_on(testBean.getCreated_On());
                    model.setLast_modified_on(testBean.getModified_On());
                    model.setSchool_id(Constant.SCHOOL_ID);
                    model.setTest_coordinator_id(Constant.TEST_COORDINATOR_ID);


                    HashMap<String, String> map = null;
                    try {

                        Log.e(TAG, "test_id=> " + testBean.getTest_ID());
                        map = db.getParticularRow(getApplicationContext(), DBManager.TBL_LP_FITNESS_TEST_TYPES, "test_type_id", "" + testBean.getTest_ID(), "", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (map.size() == 0) {
                        db.insertTables(db.TBL_LP_FITNESS_TEST_TYPES, model);


                    } else {
                        db.deleteTestRow(getApplicationContext(), DBManager.TBL_LP_FITNESS_TEST_TYPES,
                                "test_type_id", "" + testBean.getTest_ID());
                        db.insertTables(db.TBL_LP_FITNESS_TEST_TYPES, model);
                    }
                }

                // ****************************
                List<TestTypeModel.ResultBean.TestSubtypesBean> subTestsList = testModel.getResult().getTestSubtypes();
                FitnessTestCategoryModel inner_model = new FitnessTestCategoryModel();
                CampTestMapping mapping = new CampTestMapping();

                for (int j = 0; j < subTestsList.size(); j++) {
                    TestTypeModel.ResultBean.TestSubtypesBean subTestBean = subTestsList.get(j);
                    inner_model.setTest_name(subTestBean.getTest_Name());
                    inner_model.setScore_criteria(subTestBean.getScore_Criteria());
                    inner_model.setScore_measurement(subTestBean.getScore_Measurement());
                    inner_model.setScore_unit(subTestBean.getScore_Unit());
                    inner_model.setSub_test_id(subTestBean.getTest_type_id());
                    inner_model.setTest_id(subTestBean.getTest_Category_ID());
                    inner_model.setTest_description(subTestBean.getTest_Description().replace("'", "''"));
                    inner_model.setPurpose(subTestBean.getPurpose());
                    inner_model.setAdministrative_Suggestions(subTestBean.getAdministrative_Suggestions().replace("'", "''"));
                    inner_model.setEquipment_Required(subTestBean.getEquipment_Required());
                    inner_model.setScoring(subTestBean.getScoring().replace("'", "''"));
                    inner_model.setCreated_on(subTestBean.getCreated_On());
                    inner_model.setLast_modified_on(subTestBean.getLast_Modified_On());
                    inner_model.setTest_applicable(subTestBean.getTestsApplicable());
                    inner_model.setMultipleLane(subTestBean.getMultipleLane());
                    inner_model.setTimerType(subTestBean.getTimerType());
                    inner_model.setFinalPosition(subTestBean.getInitialFinal());
                    inner_model.setVideoLink(subTestBean.getVideoLink());
                    mapping.setTest_type_id(subTestBean.getTest_type_id());
                    mapping.setSchool_id(Integer.parseInt(Constant.SCHOOL_ID));
                    mapping.setCamp_id(Integer.parseInt(Constant.CAMP_ID));


                    int test_id = subTestBean.getTest_Category_ID();


                    // changes

                    ArrayList<Object> objectArrayList = DBManager.getInstance().getAllTableDataTest(TakeTestActivity.this, DBManager.TBL_LP_FITNESS_TEST_TYPES, "", "", "", "");
                    Log.e(TAG, "testtypes size==> " + objectArrayList.size());


                    for (Object o : objectArrayList) {

                        FitnessTestTypesModel TestModel = (FitnessTestTypesModel) o;
                        int inner_test_id = TestModel.getTest_type_id();

                        if (inner_test_id == test_id) {
                            inner_model.setName(TestModel.getTest_name());

                                    /*Log.d(TAG," test id==> "+subTestBean.getTest_Category_ID());
                                    Log.d(TAG," test name==> "+TestModel.getTest_name());
                                    Log.e(TAG,"sub test id==> "+subTestBean.getTest_type_id());
                                    Log.e(TAG,"sub test name==> "+subTestBean.getTest_Name());*/
                        }

                    }

                    HashMap<String, String> map = null;
                    HashMap<String, String> mapping_map = null;
                    try {

                        map = db.getParticularRow(getApplicationContext(), DBManager.TBL_LP_FITNESS_TEST_CATEGORY, "sub_test_id", "" + subTestBean.getTest_type_id(), "", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {

                        mapping_map = db.getParticularRow(getApplicationContext(), DBManager.TBL_LP_CAMP_TEST_MAPPING, "test_type_id", "" + subTestBean.getTest_type_id(), "school_id", Constant.SCHOOL_ID);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (map.size() == 0) {

                        db.insertTables(db.TBL_LP_FITNESS_TEST_CATEGORY, inner_model);

                    } else {

                        db.deleteTestRow(getApplicationContext(), DBManager.TBL_LP_FITNESS_TEST_CATEGORY,
                                "sub_test_id", "" + subTestBean.getTest_type_id());
                        db.insertTables(db.TBL_LP_FITNESS_TEST_CATEGORY, inner_model);
                    }


                  /*  if (mapping_map.size() == 0) {

                        db.insertTables(db.TBL_LP_CAMP_TEST_MAPPING, mapping);

                    }*/

                   /* else {

                        db.deleteTestRow(getApplicationContext(),DBManager.TBL_LP_CAMP_TEST_MAPPING,
                                "test_type_id", ""+subTestBean.getTest_type_id());
                        db.insertTables(db.TBL_LP_CAMP_TEST_MAPPING, mapping);
                    }*/
                }


                // **************************

                List<TestTypeModel.ResultBean.TestCheckListBean> testCheckList = testModel.getResult().getTestCheckList();
                final SkillTestTypeModel checklist_model = new SkillTestTypeModel();

                for (int k = 0; k < testCheckList.size(); k++) {
                    TestTypeModel.ResultBean.TestCheckListBean checkListBean = testCheckList.get(k);
                    checklist_model.setChecklist_item_id(checkListBean.getCheckListItem_ID());
                    checklist_model.setItem_name(checkListBean.getItem_Name());
                    checklist_model.setTest_type_id(checkListBean.getTest_Type_ID());
                    checklist_model.setCreated_on(checkListBean.getCreated_On());
                    checklist_model.setLast_modified_on(checkListBean.getModified_On());


                    HashMap<String, String> map = null;
                    try {
                        map = db.getParticularRow(getApplicationContext(), DBManager.TBL_LP_SKILL_TEST_TYPE, "checklist_item_id", "" + checkListBean.getCheckListItem_ID(), "", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (map.size() == 0) {
                        db.deleteTestRow(getApplicationContext(), DBManager.TBL_LP_SKILL_TEST_TYPE,
                                "checklist_item_id", "" + checkListBean.getCheckListItem_ID());
                        db.insertTables(db.TBL_LP_SKILL_TEST_TYPE, checklist_model);
//                        boolean isDataInserted = sp.getBoolean(Constant.SCHOOL_ID, false);
//                        if(!isDataInserted){
                        //    insertDefaultTransactionData(inner_model);
                        // new LongOperation((FitnessTestCategoryModel) DBManager.getInstance().getAllTableData(this, DBManager.TBL_LP_FITNESS_TEST_CATEGORY, "", "","","").get(0)).execute();
                        // }

                    }
                }

                //***************************

                // ************************************
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //progressDialogUtility.dismissProgressDialog();
                        if (progressDialogUtility4 != null)
                            progressDialogUtility4.dismissProgressDialog();
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putBoolean("got_all_test", true);
                        editor.commit();
                    }
                });
//                    }
//                }).start();

            } else {
                if (progressDialogUtility4 != null)
                    progressDialogUtility4.dismissProgressDialog();
                //  progressDialogUtility.dismissProgressDialog();
            }
        } else if (obj instanceof InsertActivityModel) {
            InsertActivityModel insertActivityModel = (InsertActivityModel) obj;

            if (insertActivityModel.getIsSuccess().equalsIgnoreCase("true")) {

                activity_data_to_be_sent++;

                if (activity_data_to_be_sent < myDartActivityList.size()) {
                    Object ob = myDartActivityList.get(activity_data_to_be_sent);
                    if (ob instanceof MyDartInsertActivityModel) {
                        MyDartInsertActivityModel myDartInsertActivityModel = (MyDartInsertActivityModel) ob;
                        if (connectionDetector.isConnectingToInternet()) {
                            InsertActivityRequest insertActivityRequest = new InsertActivityRequest(this, myDartInsertActivityModel.getData(), myDartInsertActivityModel.getActivity_id(), myDartInsertActivityModel.getStudent_activity_id(), this);
                            insertActivityRequest.hitUserRequest();
                        }
                    }
                } else {

                    DBManager.getInstance().deleteAllRows(DBManager.TBL_LP_MY_DART_INSERT_ACTIVITY_TABLE);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (progressDialogUtility3 != null)
                                progressDialogUtility3.dismissProgressDialog();

                            if (sync_data_count > myDartActivityList.size()) {
                                sync_data_count = sync_data_count - myDartActivityList.size();
                                sync_data_tv.setText("" + sync_data_count);
                            } else {
                                sync_data_tv.setText("" + 0);
                                activity_data_to_be_sent = 0;
                            }

                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("last_synced_time", Utility.getDate(System.currentTimeMillis(), "dd MMM yy, hh:mm a"));
                            editor.commit();
                            last_synced_time = sp.getString("last_synced_time", "");
                            last_synced_time_tv.setText(last_synced_time);
                        }
                    });

                }

            }
        } else if (obj instanceof StudentModel) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    studentModel = (StudentModel) obj;
                    if (studentModel.getIsSuccess().equalsIgnoreCase("true")) {
                        new UpdateDB().execute(100);
                    } else {
                        progressDialogUtility1.dismissProgressDialog();
                        Constant.classList.clear();
                        Constant.classIdList.clear();
                        Constant.testList.clear();
                        Constant.testIdList.clear();
                        Constant.classList2.clear();
                        Constant.classIdList2.clear();
                        Toast.makeText(TakeTestActivity.this, R.string.term_expire, Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } else if (obj instanceof UserModel) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    UserModel userModel = (UserModel) obj;
                    if (userModel.getMessage().equalsIgnoreCase("success")) {
                        List<UserModel.Result.School> schools = userModel.getResult().getSchools();
//                ArrayList<Object> objectArrayList = DBManager.getInstance().getAllTableData(this, DBManager.TBL_LP_SCHOOLS_MASTER,
//                        "trainer_coordinator_id", Constant.TEST_COORDINATOR_ID,"","");
                        if (checkDataChanged(schools)) {
                            SchoolsMasterModel model = new SchoolsMasterModel();

                            for (int i = 0; i < schools.size(); i++) {

                                UserModel.Result.School schoolsBean = schools.get(i);
                                model.setSchool_name(schoolsBean.getSchoolname());
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
                                    db.deleteSchoolOrStudentUserRow(TakeTestActivity.this, db.TBL_LP_SCHOOLS_MASTER, "school_id", "" + schoolsBean.getSchoolId(),
                                            "", "");
                                    db.insertTables(db.TBL_LP_SCHOOLS_MASTER, model);
                                }

                                if (model.getIsRetestAllowed().equalsIgnoreCase("True")) {
                                    model.setIsRetestAllowed("True");
                                    model.setForRetest(1);
                                    db.insertTables(db.TBL_LP_SCHOOLS_MASTER, model);
                                }

//                               if (i == 0) {
//                                   SharedPreferences.Editor e = sp.edit();
//                                   e.putString("school_name", schoolsBean.getSchoolname().replaceAll("'", "'' "));
//                                   e.putString("school_id", "" + schoolsBean.getSchoolId());
//                                   e.putString("seniors_starting_from", "" + schoolsBean.getSeniorsStartingFrom());
//                                   e.apply();
//
//
//                                   Constant. SCHOOL_NAME = sp.getString("school_name", "");
//                                   Constant.SCHOOL_ID = sp.getString("school_id", "");
//                                   Constant.SENIORS_STARTING_FROM = sp.getString("seniors_starting_from", "");
//                                   Constant.TEST_COORDINATOR_ID = sp.getString("test_coordinator_id", "");
//                               }
                            }
                        }

                        if (Utility.checkForSchoolDeActivated(TakeTestActivity.this, schoolId)) {
                            //Utility.showSchoolDeactivatedDialog(TakeTestActivity.this);
                            if (!Constant.test_running && !Constant.skill_test_running) {
                                onlyForFetchingData = true;
                                progressDialogUtility1.showProgressDialog();
                                new SyncOnResume1().execute();
                            }
                        } else {
                            // school not de- deactivated

                            if (!Constant.test_running && !Constant.skill_test_running && sp.getBoolean(AppConfig.IS_LOGIN, false)) {
                                Snackbar.make(coordinator_layout, R.string.sync_start, Snackbar.LENGTH_INDEFINITE).setDuration(5000).show();
                                Utility.rotateFabForward(TakeTestActivity.this, sync_img);
                                int fitnessTestCount = db.getTableCount(testTableName,
                                        AppConfig.EXPORTED, "true");
//                               if (fitnessTestCount>0)
//                                   onlyForFetchingData = true;
//                               else
//                                   onlyForFetchingData = false;
                                onlyForFetchingData = fitnessTestCount > 0;
                                progressDialogUtility1.showProgressDialog();
                                new SyncOnResume1().execute();
                            }

                            if (!AppConfig.IS_STATE_UPDATION_DIALOG_SHOWING && (sp.getInt(AppConfig.STATE_ID, 0) == 0 || sp.getString(AppConfig.CITY_NAME, "").equals("") || sp.getString(AppConfig.DISTRICT_NAME, "").equals("") || sp.getString(AppConfig.BLOCK_NAME, "").equals("")))
                                Utility.showStateCityUpdateDialog(TakeTestActivity.this);
                        }
                    }
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!Constant.test_running && !Constant.skill_test_running) {
                        if (progressDialogUtility1 != null)
                            progressDialogUtility1.dismissProgressDialog();
                        if (progressDialogUtility2 != null)
                            progressDialogUtility2.dismissProgressDialog();
                        Utility.rotateFabBackward(sync_img);

                    }
                }
            });

        }

    }

    private boolean checkDataChanged(List<UserModel.Result.School> schools) {
        for (UserModel.Result.School schoolsBean : schools) {
            HashMap<String, String> map = null;
            try {
                map = db.getSchoolParticularRow(getApplicationContext(), DBManager.TBL_LP_SCHOOLS_MASTER,
                        AppConfig.SCHOOL_ID, "" + schoolsBean.getSchoolId(), AppConfig.IS_ATTACHED,
                        String.valueOf(schoolsBean.getIsAttached()), schoolsBean.getIsRetestAllowed());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (map != null && map.size() == 0)
                return true;

        }
        return false;
    }

    private void getStudentList() {

        connectionDetector = new ConnectionDetector(this);
        if (connectionDetector.isConnectingToInternet()) {

            //    db.dropNamedTable(this, db.TBL_LP_STUDENT_MASTER);
            //   db.createNamedTable(this, db.TBL_LP_STUDENT_MASTER);
            progressDialogUtility1 = new ProgressDialogUtility(this);
            progressDialogUtility1.showProgressDialog();
            progressDialogUtility1.setMessage("Fetching all the students.. Please wait..");
            StudentRequest studentRequest = new StudentRequest(TakeTestActivity.this, Constant.SCHOOL_ID, "", TakeTestActivity.this, sp.getString(AppConfig.FOR_RETEST, "0"));
            studentRequest.hitUserRequest();

        } else {
            Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }

    }

    public class UpdateDB extends AsyncTask<Integer, Integer, String> {


        @Override
        protected void onProgressUpdate(Integer... progress) {
            progressDoalog.setProgress(progress[0]);
        }

        protected String doInBackground(Integer... paramVarArgs) {

            try {
                List<StudentModel.ResultBean.StudentBean> students = studentModel.getResult().getStudent();
                db.insertStudentsForRefreshing(students, this);

            } catch (Exception localException) {
                localException.printStackTrace();
            }

            return null;
        }

        public void doProgress(int count, int total) {
            publishProgress(Integer.valueOf("" + ((count * 100) / total)));
        }

        protected void onPostExecute(String paramVoid) {
            progressDoalog.dismiss();
            if (!Constant.test_running && !Constant.skill_test_running) {
                int fitnessTestCount = db.getTableCount(testTableName, AppConfig.EXPORTED, "true");
                onlyForFetchingData = fitnessTestCount > 0;
                new SyncOnResume1().execute();
            }
        }

        protected void onPreExecute() {
            progressDialogUtility1.dismissProgressDialog();
            showDialog();
            super.onPreExecute();

        }

    }


    private void showDialog() {
        progressDoalog = new ProgressDialog(TakeTestActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage(getString(R.string.schools_message_loader));
        progressDoalog.setProgressNumberFormat(null);
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDoalog.setCancelable(false);
        progressDoalog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        // menu.findItem(R.id.profile_img).setIcon(Utility.resizeImage(this,R.drawable.person_black_i,100,100));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.profile_img:
                Intent i1 = new Intent(TakeTestActivity.this, ProfileActivity.class);
                startActivity(i1);

                break;


            case R.id.ecetificate:
                Utility.openURLBrowser(this, AppConfig.ECERTIFICATE_URL);
                break;
            case R.id.sop:
                Utility.openURLBrowser(this, AppConfig.SOP_URL);
                break;

            case R.id.admin_manual:
                Utility.openURLBrowser(this, AppConfig.ADMIN_MANUAL_URL);

                break;

            case R.id.logout:
                Utility.showLogoutDialog(this);

                break;
        }
        return true;
    }


}


