package kheloindia.com.assessment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kheloindia.com.assessment.adapter.ScoolAdapter;
import kheloindia.com.assessment.model.SchoolsMasterModel;
import kheloindia.com.assessment.model.StudentMasterModel;
import kheloindia.com.assessment.model.StudentModel;
import kheloindia.com.assessment.model.UserModel;
import kheloindia.com.assessment.model.UserSchoolMappingModel;
import kheloindia.com.assessment.util.AppConfig;
import kheloindia.com.assessment.util.ConnectionDetector;
import kheloindia.com.assessment.util.DBManager;
import kheloindia.com.assessment.util.ItemClickListener;
import kheloindia.com.assessment.util.ProgressDialogUtility;
import kheloindia.com.assessment.util.ResponseListener;
import kheloindia.com.assessment.util.Utility;

import kheloindia.com.assessment.functions.Constant;


import kheloindia.com.assessment.webservice.GetSchoolRequest;
import kheloindia.com.assessment.webservice.StudentRequest;

/**
 * Created by CT13 on 2017-05-11.
 */

public class SchoolActivity extends AppCompatActivity implements ItemClickListener, ResponseListener, View.OnClickListener {

    private ListView school_lst;
    //private Toolbar toolbar;
    ImageView backBtn,logoutBtn;
    TextView toolbarTitle;
    private ScoolAdapter schoolAdapter;
    DBManager db;
    private ArrayList<HashMap<String, String>> schoolList;
    private ConnectionDetector connectionDetector;
    private ProgressDialogUtility progressDialogUtility;
    StudentModel model;
    SharedPreferences sp;
    Intent in;
    // private ProgressBar progressBar;
    ProgressDialog progressDoalog;
    int count =0;
    JSONObject jObj;

    JSONArray jArray = new JSONArray();
    TextView noResultsTxt;
    Button refreshBtn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.school_screen);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        init();
    }


    private void init()  {

        progressDialogUtility = new ProgressDialogUtility(this);
        db = DBManager.getInstance();
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        connectionDetector = new ConnectionDetector(this);
        //toolbar = (Toolbar) findViewById(R.id.toolbar);
        //TextView title = toolbar.findViewById(R.id.toolbar_title);
        //Utility.showActionBar(this, toolbar,"" );
        backBtn = (ImageView) findViewById(R.id.back_btn);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        logoutBtn = (ImageView) findViewById(R.id.toolbar_action_image_btn);
        school_lst = (ListView) findViewById(R.id.school_lst);
        noResultsTxt = (TextView) findViewById(R.id.no_results_txt);
        refreshBtn = (Button) findViewById(R.id.refresh_btn);


        schoolList = new ArrayList<HashMap<String, String>>();
        toolbarTitle.setText(getString(R.string.school_select));
        Typeface font_reg = Typeface.createFromAsset(getAssets(),
                "fonts/Barlow-Regular.ttf");
        toolbarTitle.setTypeface(font_reg);
        logoutBtn.setVisibility(View.VISIBLE);
        backBtn.setOnClickListener(this);
        logoutBtn.setOnClickListener(this);
        refreshBtn.setOnClickListener(this);
        SharedPreferences.Editor e = sp.edit();
        e.putBoolean("inSchoolActivity",true);
        e.commit();


        /*SchoolsMasterModel schoolsMasterModel=new SchoolsMasterModel();
        schoolsMasterModel.setSchool_name("Rabindranath World School");


        SchoolsMasterModel schoolsMasterModels=new SchoolsMasterModel();
        schoolsMasterModels.setSchool_name("Salwan Public School");*/

       /* DBManager.getInstance().createNamedTable(this,DBManager.TBL_LP_SCHOOLS_MASTER);
        DBManager.getInstance().insertTables(DBManager.TBL_LP_SCHOOLS_MASTER,schoolsMasterModel);
        DBManager.getInstance().insertTables(DBManager.TBL_LP_SCHOOLS_MASTER,schoolsMasterModels);*/

        Constant.TEST_COORDINATOR_ID = sp.getString("test_coordinator_id","");
        Log.e("SchoolActivty","coordinator id=> "+Constant.TEST_COORDINATOR_ID);


        loadSchoolsFromLocalDB();

        hideShowNoresultsTxt();

        schoolAdapter = new ScoolAdapter(this, schoolList);
        school_lst.setAdapter(schoolAdapter);
        schoolAdapter.setClickListener(this);

//        for (Object o : objectArrayList) {
//
//            jObj = new JSONObject();
//
//            try {
//                SchoolsMasterModel schoolModel = (SchoolsMasterModel) o;
//                jObj.put("school_name", schoolModel.getSchool_name());
//                jObj.put("school_id", "" + schoolModel.getSchool_id());
//
//                jArray.put(jObj);
//
//            } catch (Exception e1){
//                e1.printStackTrace();
//            }
//
//        }
//
//        Log.e("SchoolActivity","json array==> "+jArray.toString());
    }

    private void loadSchoolsFromLocalDB(){
//        ArrayList<Object> objectArrayList = DBManager.getInstance().getAllTableData(this, DBManager.TBL_LP_SCHOOLS_MASTER, "trainer_coordinator_id", Constant.TEST_COORDINATOR_ID,AppConfig.IS_ATTACHED,"1");
        ArrayList<Object> objectArrayList = DBManager.getInstance().getAllTableData(this, DBManager.TBL_LP_SCHOOLS_MASTER, "trainer_coordinator_id", Constant.TEST_COORDINATOR_ID,"","");
        Log.e("SchoolActivity", "school size==> " + objectArrayList.size());
        schoolList.clear();

        for (Object o : objectArrayList) {

            HashMap<String, String> map = new HashMap<String, String>();

            SchoolsMasterModel schoolModel = (SchoolsMasterModel) o;

            String office = schoolModel.getOffice();

            if(office.equals("0")){
                map.put("school_name", schoolModel.getSchool_name());
                map.put("school_id", "" + schoolModel.getSchool_id());
                map.put("school_chain_id", "" + schoolModel.getSchool_chain_id());
                map.put("school_image_path", "" + schoolModel.getSchool_image_path());
                map.put(AppConfig.IS_ATTACHED,schoolModel.getIsAttached()+"");
                map.put(AppConfig.FOR_RETEST,schoolModel.getForRetest()+"");
                map.put(AppConfig.IS_RETEST_ALLOWED,schoolModel.getIsRetestAllowed()+"");

                schoolList.add(map);
            }

        }
    }

    private void showNoSchoolsAssignedPopUp(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SchoolActivity.this);

        alertDialog.setMessage(getResources().getString(R.string.no_schools_found));

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.alarm);

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                SharedPreferences.Editor e = sp.edit();
                e.putBoolean(AppConfig.IS_STUDENT_DETAILS_GET,false);
                e.putBoolean(AppConfig.IS_LOGIN,false);
                e.apply();
                startActivity(new Intent(SchoolActivity.this,LoginActivity.class));
                finish();
            }
        });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }


    @Override
    public void onItemClick(View view, int position) {

        boolean isCOmingfromTakeTestScreen = sp.getBoolean("fromTakeTest",false);

        // if(isCOmingfromTakeTestScreen){
//        in = new Intent(this, TakeTestActivity.class);
//        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        in.putExtra("school", schoolList.get(position));
//        }
// else {
//            in = new Intent(this, DashBoardActivity.class);
//        }

        String schoolId = schoolList.get(position).get("school_id");


        SharedPreferences.Editor e = sp.edit();
        HashMap<String,String> school = schoolList.get(position);
        e.putString("school_name", school.get("school_name"));
        e.putString("school_id", schoolId);
        e.putString("school_chain_id", school.get("school_chain_id"));
        e.putString("school_image_path", school.get("school_image_path"));
        e.putString(AppConfig.FOR_RETEST,school.get(AppConfig.FOR_RETEST));
        e.commit();


        Constant.SCHOOL_NAME = sp.getString("school_name","");
        Constant.SCHOOL_ID = sp.getString("school_id","");
        Constant.SENIORS_STARTING_FROM = sp.getString("seniors_starting_from","");
        Constant.SCHOOL_CHAIN_ID = sp.getString("school_chain_id","");

//        if (Utility.checkForSchoolDeActivated(this,schoolId)) {
//            in = new Intent(this, TestStatusActivity.class);
//            //in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            in.putExtra("school", schoolList.get(position));
//            startActivity(in);
//        }
//        else {
//            in = new Intent(this, TakeTestActivity.class);
//            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            in.putExtra("school", schoolList.get(position));
//            getStudentList();
//        }

        in = new Intent(this, TakeTestActivity.class);
        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        in.putExtra("school", school);
        getStudentList(school.get(AppConfig.IS_RETEST_ALLOWED),school.get(AppConfig.FOR_RETEST));

    }

    private void getStudentList(final String isRetestAllowed, final String forRetest) {
        if (connectionDetector.isConnectingToInternet()) {

            //    db.dropNamedTable(this, db.TBL_LP_STUDENT_MASTER);
            //   db.createNamedTable(this, db.TBL_LP_STUDENT_MASTER);
            progressDialogUtility.showProgressDialog();
            //progressDialogUtility.setMessage("Loading Please wait...");
            Runnable r=new Runnable() {
                @Override
                public void run() {
                    //final ArrayList<Object> objectArrayLst = DBManager.getInstance().getAllTableData(SchoolActivity.this, DBManager.TBL_LP_STUDENT_MASTER, "current_school_id", Constant.SCHOOL_ID,"","");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String max_date ="";
                            if(DBManager.getInstance().getTotalCount(SchoolActivity.this, DBManager.TBL_LP_STUDENT_MASTER, "current_school_id", Constant.SCHOOL_ID,"","","","")>0){
                                max_date = DBManager.getInstance().getMaxDate(SchoolActivity.this, DBManager.TBL_LP_STUDENT_MASTER,"last_modified_on",Constant.CAMP_ID);

                            } else {
                                max_date = "";

                            }

//                            Log.e("LoginActivity","student max date=> "+max_date);
//                            Log.e("LoginActivity","SCHOOL_ID=> "+Constant.SCHOOL_ID);
                            StudentRequest studentRequest = new StudentRequest(SchoolActivity.this, Constant.SCHOOL_ID,max_date, SchoolActivity.this,forRetest);
                            studentRequest.hitUserRequest();


                        }
                    });

                }
            };
            new Thread(r).start();



        } else {
            if (isRetestAllowed.equalsIgnoreCase("False")) {
                progressDialogUtility.showProgressDialog();
                progressDialogUtility.setMessage(getString(R.string.schools_message_loader));
                FetchFromDB();
            }
            else {
                Toast.makeText(this, getString(R.string.enable_your_internet), Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void FetchFromDB() {
        Runnable r=new Runnable() {
            @Override
            public void run() {
                ArrayList<Object> objectArrayLst = DBManager.getInstance().getAllTableData(SchoolActivity.this, DBManager.TBL_LP_STUDENT_MASTER, "current_school_id", Constant.SCHOOL_ID,"","");

                if (objectArrayLst.size() > 0) {

                    SharedPreferences.Editor e = sp.edit();
                    e.putBoolean("isStudentDetailGet", true);
                    e.putString("camp_id",((StudentMasterModel)objectArrayLst.get(0)).getCamp_id());
                    e.commit();


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialogUtility.dismissProgressDialog();
                            startActivity(in);
                            finish();
                        }
                    });

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialogUtility.dismissProgressDialog();
                            Toast.makeText(getApplicationContext(), "No student has been assigned to the school for the current session.Please confirm or upload student data.", Toast.LENGTH_LONG).show();
                        }
                    });

                }

            }
        };
        new Thread(r).start();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        //finish();
        //super.onBackPressed();
        moveTaskToBack(true);

        //boolean isCOmingfromTakeTestScreen = sp.getBoolean("fromTakeTest",false);

        //  if(isCOmingfromTakeTestScreen){
//            SharedPreferences.Editor e = sp.edit();
//            e.putBoolean("fromTakeTestk",false);
//            e.commit();
//            Intent i1 = new Intent(SchoolActivity.this, TakeTestActivity.class);
//            startActivity(i1);
        //finish();
//        } else {
//            Intent i1 = new Intent(SchoolActivity.this, DashBoardActivity.class);
//            startActivity(i1);
//            finish();
//        }

    }

    private void showExitDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SchoolActivity.this);
        alertDialog.setCancelable(false);

        alertDialog.setMessage(getResources().getString(R.string.exit_from_app));

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.alarm);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {

                SharedPreferences.Editor e = sp.edit();
                e.putBoolean("fromTakeTest",false);
                e.commit();
                dialog.cancel();
                // finish();
                Intent i = new Intent(SchoolActivity.this, LoginActivity.class);

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

    @Override
    public void onResponse(Object obj) {
        if (progressDialogUtility!=null)
            progressDialogUtility.dismissProgressDialog();

        if (obj instanceof StudentModel) {

            model = (StudentModel) obj;

            if (model.getIsSuccess().equalsIgnoreCase("true")) {

                // db.dropNamedTable(this, db.TBL_LP_STUDENT_MASTER);
                db.createNamedTable(this, db.TBL_LP_STUDENT_MASTER);

                //  new UpdateDB().execute(new Void[0]);

                new UpdateDB().execute(100);

                //count =1;

                // progressDoalog.setProgress(0);

            } else {
                progressDialogUtility.dismissProgressDialog();
                Constant.classList.clear();
                Constant.classIdList.clear();
                Constant.testList.clear();
                Constant.testIdList.clear();
                Constant.classList2.clear();
                Constant.classIdList2.clear();
                Toast.makeText(this, R.string.term_expire, Toast.LENGTH_SHORT).show();
            }
        }
        else if (obj instanceof UserModel){
            UserModel userModel = (UserModel) obj;
            if (userModel.getMessage().equalsIgnoreCase("success")) {
                List<UserModel.Result.School> schools = userModel.getResult().getSchools();

                SchoolsMasterModel model = new SchoolsMasterModel();

//                UserSchoolMappingModel mapping_model = new UserSchoolMappingModel();

                for (int i = 0; i < schools.size(); i++) {

                    UserModel.Result.School schoolsBean = schools.get(i);
                    model.setSchool_name(schoolsBean.getSchoolname().replaceAll("'", "'' "));
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
                        db.deleteSchoolOrStudentUserRow(this, db.TBL_LP_SCHOOLS_MASTER, "school_id", "" + schoolsBean.getSchoolId(),
                                "", "");
                        db.insertTables(db.TBL_LP_SCHOOLS_MASTER, model);
                    }

                    if (model.getIsRetestAllowed().equalsIgnoreCase("True")){
                        model.setIsRetestAllowed("True");
                        model.setForRetest(1);
                        db.insertTables(db.TBL_LP_SCHOOLS_MASTER, model);
                    }

                    if (i == 0) {
                        SharedPreferences.Editor e = sp.edit();
                        e.putString("school_name", schoolsBean.getSchoolname().replaceAll("'", "'' "));
                        e.putString("school_id", "" + schoolsBean.getSchoolId());
                        e.putString("seniors_starting_from", "" + schoolsBean.getSeniorsStartingFrom());
                        e.apply();


                        Constant. SCHOOL_NAME = sp.getString("school_name", "");
                        Constant.SCHOOL_ID = sp.getString("school_id", "");
                        Constant.SENIORS_STARTING_FROM = sp.getString("seniors_starting_from", "");
                        Constant.TEST_COORDINATOR_ID = sp.getString("test_coordinator_id", "");

                    }

                    // adding entries in mapping table
//                    mapping_model.setSchool_id(schoolsBean.getSchoolId());
//                    mapping_model.setUser_id(Integer.parseInt(Constant.TEST_COORDINATOR_ID));
//                    mapping_model.setModified(schoolsBean.getLastModifiedOn());
//                    mapping_model.setCreated(schoolsBean.getCreatedOn());

//                    HashMap<String, String> mapping_map = null;
//                    try {
//                        map = db.getParticularRow(getApplicationContext(), DBManager.TBL_LP_USER_SCHOOL_MAPPING,
//                                "school_id", "" + schoolsBean.getSchoolId(), "user_id", Constant.TEST_COORDINATOR_ID);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }

//                    if (map.size() == 0) {
//                        db.insertTables(db.TBL_LP_USER_SCHOOL_MAPPING, mapping_model);
//                    } else {
//                        db.deleteSchoolOrStudentUserRow(this, db.TBL_LP_USER_SCHOOL_MAPPING, "school_id", "" + schoolsBean.getSchoolId(),
//                                "", "");
//                        db.insertTables(db.TBL_LP_USER_SCHOOL_MAPPING, model);
//                    }

                }

                if (schools.size()>0) {
                    loadSchoolsFromLocalDB();
                    schoolAdapter = new ScoolAdapter(this, schoolList);
                    school_lst.setAdapter(schoolAdapter);
                    schoolAdapter.setClickListener(this);
                    hideShowNoresultsTxt();
                }
            }
        }
    }

    private void hideShowNoresultsTxt(){
        if (schoolList.size()<=0) {
            noResultsTxt.setVisibility(View.VISIBLE);
            refreshBtn.setVisibility(View.VISIBLE);
        }
        else {
            noResultsTxt.setVisibility(View.GONE);
            refreshBtn.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_btn:
                //onBackPressed();
                moveTaskToBack(true);
                break;
            case R.id.toolbar_action_image_btn:
                Utility.showLogoutDialog(this);
                break;
            case R.id.refresh_btn:
                if (connectionDetector.isConnectingToInternet()) {
                    progressDialogUtility.showProgressDialog();
                    GetSchoolRequest getSchoolRequest = new GetSchoolRequest(this, sp.getString(AppConfig.TEST_COORDINATOR_ID, ""), this);
                    getSchoolRequest.hitSchoolRequest();
                }
                break;
        }
    }

    public class UpdateDB extends AsyncTask<Integer, Integer, String> {



        @Override
        protected void onProgressUpdate(Integer... progress) {


            progressDoalog.setProgress(progress[0]);



        }

        protected String doInBackground(Integer... paramVarArgs) {


           /* for (; count <= paramVarArgs[0]; count++) {
                try {
                    Thread.sleep(1000);
                    publishProgress(count);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }*/

            try {
                List<StudentModel.ResultBean.StudentBean> students = model.getResult().getStudent();
                db.insertStudents(students,this);
//                StudentMasterModel student_model = new StudentMasterModel();
//
//                for (int i = 0; i < students.size(); i++) {
//                    StudentModel.ResultBean.StudentBean studentBean = students.get(i);
//
//                    student_model.setStudent_name(studentBean.getStudent_Name());
//                    student_model.setCurrent_class(studentBean.getCurrent_Class());
//                    student_model.setCurrent_roll_num(studentBean.getCurrent_Roll_Num());
//                    student_model.setDob(studentBean.getDOB());
//                    student_model.setGender(Integer.parseInt(studentBean.getGender()));
//                    student_model.setSection(studentBean.getSection());
//                    //student_model.setStudent_liveplus_id(studentBean.getStudent_LivePLUS_ID());
//                    //student_model.setUser_login_id(0);
//                    student_model.setCurrent_school_id(studentBean.getCurrent_School_ID());
//                    student_model.setCamp_id(studentBean.getCampID());
//                    student_model.setStudent_id(Integer.parseInt(studentBean.getStudentID()));
//                    student_model.setClass_id(studentBean.getClassID());
//                    student_model.setStudent_registration_num(studentBean.getStudentRegistration());
//                    student_model.setCreated_on(studentBean.getCreated_On());
//                    student_model.setLast_modified_on(studentBean.getLast_Modified_On());
//                    // student_model.setClass_partition_id(studentBean.getcla);
//
//                 /*   Log.e("SchoolActivity","class_id=> "+studentBean.getClassID());
//                    Log.e("SchoolActivity","setCurrent_class=> "+studentBean.getCurrent_Class());
//                    Log.e("SchoolActivity","setCurrent_roll_num=> "+studentBean.getCurrent_Roll_Num());*/
//
//                    // first check for existence
//
//                    String subString = studentBean.getStudentID();
//                    HashMap<String, String> map = null;
//                    try {
//                        map = db.getParticularRow(getApplicationContext(),DBManager.TBL_LP_STUDENT_MASTER,
//                                "current_school_id", Constant.SCHOOL_ID, AppConfig.STUDENT_ID, subString);
//                    } catch(Exception e){
//                        e.printStackTrace();
//                    }
//
//                    if(map.size()==0) {
//                        db.insertTables(db.TBL_LP_STUDENT_MASTER, student_model);
//
//                    }
//                    else {
//                        db.deleteStudentRow(getApplicationContext(),DBManager.TBL_LP_STUDENT_MASTER,
//                                "current_school_id", Constant.SCHOOL_ID, AppConfig.STUDENT_ID, subString);
//                        db.insertTables(db.TBL_LP_STUDENT_MASTER, student_model);
//                    }
//                    if(i==0) {
//                        Constant.classList.clear();
//                        Constant.classIdList.clear();
//                        Constant.testList.clear();
//                        Constant.testIdList.clear();
//                        Constant.classList2.clear();
//                        Constant.classIdList2.clear();
//                        SharedPreferences sp=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                        SharedPreferences.Editor e=    sp.edit();
//                        e.putString("camp_id",studentBean.getCampID());
//                        e.putString(AppConfig.CAMP_NAME,studentBean.getCampName());
//                        e.remove("got_all_camp");
//                        e.remove("got_all_test");
//                        e.commit();
//
//                    }
//
////                    for (; count <= paramVarArgs[0]; count++) {
////                        try {
////                          Thread.sleep(1000);
////                            publishProgress(count);
////                        } catch (Exception e) {
////                            e.printStackTrace();
////                        }
////                    }
//
//                    count++;
//                    publishProgress(Integer.valueOf(""+((count*100)/students.size())));
//
//                }


            } catch (Exception localException) {
                localException.printStackTrace();
            }



            return null;
        }

        public void doProgress(int count,int total){
            publishProgress(Integer.valueOf(""+((count*100)/total)));
        }

        protected void onPostExecute(String paramVoid) {
            //  progressDialogUtility.dismissProgressDialog();

            progressDoalog.dismiss();

            SharedPreferences.Editor e = sp.edit();
            e.putBoolean("isStudentDetailGet", true);
            e.commit();

            startActivity(in);
            finish();
            try {

            } catch (Exception ae) {
                ae.printStackTrace();
            }

        }

        protected void onPreExecute() {
            progressDialogUtility.dismissProgressDialog();
            showDialog();
            super.onPreExecute();

        }

    }

    private void showDialog() {
        progressDoalog = new ProgressDialog(SchoolActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage(getString(R.string.schools_message_loader));
        progressDoalog.setProgressNumberFormat(null);
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDoalog.setCancelable(false);
        progressDoalog.show();
    }

}