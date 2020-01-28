package kheloindia.com.assessment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;


import kheloindia.com.assessment.adapter.SubCatAdapter;
import kheloindia.com.assessment.model.CampTestMapping;
import kheloindia.com.assessment.model.FitnessTestCategoryModel;
import kheloindia.com.assessment.model.TestCategoryModel;
import kheloindia.com.assessment.util.ConnectionDetector;
import kheloindia.com.assessment.util.DBManager;
import kheloindia.com.assessment.util.ProgressDialogUtility;
import kheloindia.com.assessment.util.ResponseListener;

import kheloindia.com.assessment.functions.Constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kheloindia.com.assessment.util.Utility;

/**
 * Created by PC10 on 05/11/2017.
 */

public class ShowSubTestActivity extends AppCompatActivity implements ResponseListener {

    RecyclerView recycler_view;
    Toolbar toolbar;
    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<HashMap<String,String>> subCatList = new ArrayList<HashMap<String,String>>();
    SubCatAdapter scAdapter;
    TextView school_tv;
    private ConnectionDetector connectionDetector;
    private DBManager db;
    String TAG = "ShowSubTestActivity";
    private ProgressDialogUtility progressDialogUtility;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show_sub_test);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        init();
    }

    private void init() {

        db = DBManager.getInstance();
        progressDialogUtility=new ProgressDialogUtility(this);
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        school_tv = (TextView) findViewById(R.id.school_tv);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        Utility.showActionBar(this, toolbar, "");
        TextView title=  toolbar.findViewById(R.id.toolbar_title);
        Typeface font_reg = Typeface.createFromAsset(getAssets(),
                "fonts/Barlow-Regular.ttf");
        school_tv.setText(Constant.SCHOOL_NAME);
        school_tv.setTypeface(font_reg);
        title.setText(Constant.TEST_NAME);
        title.setTypeface(font_reg);





        mLayoutManager= new LinearLayoutManager(ShowSubTestActivity.this);                // Creating a layout Manager
        recycler_view.setLayoutManager(mLayoutManager);

        recycler_view.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));

        recycler_view.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


        connectionDetector = new ConnectionDetector(this);

        FetchList();

    }

    private void FetchList() {

        /*if (connectionDetector.isConnectingToInternet()) {
            TestCategoryRequest testCategoryRequest = new TestCategoryRequest(this, Constant.TEST_ID, this);
            testCategoryRequest.hitUserRequest();
        } else {*/
        FetchFromDB();
        //   }
    }

    private void FetchFromDB() {

        showList();
    }

        @Override
        public boolean onSupportNavigateUp() {
            onBackPressed();
            return true;
        }

    public void onBackPressed() {

       /* if(Constant.ISComingFromTestScreen){
            Constant.ISComingFromTestScreen = false;
            finish();
        } else {*/
            Intent i = new Intent(ShowSubTestActivity.this, TestActivity.class);
            startActivity(i);
            finish();
     //   }
    }

    @Override
    public void onResponse(Object obj) {

        if (obj instanceof TestCategoryModel) {

            TestCategoryModel subtestModel = (TestCategoryModel) obj;

            if (subtestModel.getIsSuccess().equalsIgnoreCase("true")) {

                // db.dropNamedTable(this, db.TBL_LP_FITNESS_TEST_CATEGORY);
                db.createNamedTable(this, db.TBL_LP_FITNESS_TEST_CATEGORY);

                List<TestCategoryModel.ResultBean.TestSubtypesBean> subTestsList = subtestModel.getResult().getTestSubtypes();

                FitnessTestCategoryModel model = new FitnessTestCategoryModel();

                for (int i = 0; i < subTestsList.size(); i++) {
                    final TestCategoryModel.ResultBean.TestSubtypesBean subTestBean = subTestsList.get(i);
                    model.setTest_name(subTestBean.getTest_Name());
                    model.setScore_criteria(subTestBean.getScore_Criteria());
                    model.setScore_measurement(subTestBean.getScore_Measurement());
                    model.setScore_unit(subTestBean.getScore_Unit());
                    model.setSub_test_id(subTestBean.getTest_type_id());
                    model.setTest_id(subTestBean.getTest_Category_ID());
                    model.setName(Constant.TEST_TYPE);
                    model.setTest_description(subTestBean.getTest_Description());
                    model.setPurpose(subTestBean.getPurpose());
                    model.setVideoLink(subTestBean.getVideo_url());

                    HashMap<String, String> map = null;
                    try {

                        Log.e(TAG, "sub test_id=> " + subTestBean.getTest_type_id());
                        map = db.getParticularRow(getApplicationContext(), DBManager.TBL_LP_FITNESS_TEST_CATEGORY, "sub_test_id", "" + subTestBean.getTest_type_id(), "", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (map.size() == 0) {

                        db.insertTables(db.TBL_LP_FITNESS_TEST_CATEGORY, model);
                        //      insertDefaultTransactionData(subTestBean.getTest_type_id());

                    }
                }

                showList();

            } else {
                Toast.makeText(this, subtestModel.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, R.string.server_down, Toast.LENGTH_SHORT).show();
        }

    }
//    private void insertDefaultTransactionData(int sub_test_id){
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                progressDialogUtility.showProgressDialog();
//            }
//        });
//        ArrayList<Object> objectArrayLst = DBManager.getInstance().getAllTableData(this, DBManager.TBL_LP_STUDENT_MASTER, "current_school_id", Constant.SCHOOL_ID,"","");
//
//        for(int i=0;i<objectArrayLst.size();i++){
//            StudentMasterModel studentMasterModel=    (StudentMasterModel) objectArrayLst.get(i);
//            FitnessTestResultModel model = new FitnessTestResultModel();
//
//            model.setStudent_id(studentMasterModel.getStudent_id());
//            model.setCamp_id(Integer.parseInt(Constant.CAMP_ID));
//            model.setTest_type_id(sub_test_id);
//            model.setTest_coordinator_id(Integer.parseInt(Constant.TEST_COORDINATOR_ID));
//            model.setScore(""+0);
//            model.setPercentile(0);
//            model.setCreated_on(Constant.getDateTimeServer());
//            model.setCreated_by(Constant.TEST_COORDINATOR_ID);
//            model.setLast_modified_by("");
//            model.setLast_modified_by("");
//            model.setLatitude(0.0);
//            model.setLongitude(0.0);
//            model.setSubTestName(Constant.TEST_TYPE);
//            model.setTestedOrNot(false);
//
//            DBManager.getInstance().insertTables(DBManager.TBL_LP_FITNESS_TEST_RESULT,model);
//
//        }
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                progressDialogUtility.showProgressDialog();
//            }
//        });
//    }
    private void showList() {

        ArrayList<Object> objectArrayList = DBManager.getInstance().getAllTableData(this,
                DBManager.TBL_LP_FITNESS_TEST_CATEGORY,"test_id", Constant.TEST_ID,"","");

        ArrayList<Object> objectArrayList1 = DBManager.getInstance().getAllTableData(this,
                DBManager.TBL_LP_CAMP_TEST_MAPPING,"camp_id", Constant.CAMP_ID,"school_id",Constant.SCHOOL_ID);

        ArrayList<String> test_type_id_list = new ArrayList<String>();

        for (Object o : objectArrayList1){
            CampTestMapping camptestModel =(CampTestMapping) o;
            test_type_id_list.add(""+camptestModel.getTest_type_id());
        }


        Log.e(TAG, "objectArrayList size==> " + objectArrayList.size());
        Log.e(TAG, "objectArrayList1 size==> " + objectArrayList1.size());

        if (objectArrayList.size() == 0) {
            Toast.makeText(this, "No data available in database.", Toast.LENGTH_LONG).show();
        } else {
            subCatList.clear();

            for (Object o : objectArrayList) {

                HashMap<String, String> map = new HashMap<String, String>();
                FitnessTestCategoryModel testModel = (FitnessTestCategoryModel) o;
                map.put("test_name", testModel.getTest_name());
                map.put("sub_test_id", ""+testModel.getSub_test_id());
                map.put("score_criteria",  testModel.getScore_criteria());
                map.put("score_measurement",  testModel.getScore_measurement());
                map.put("score_unit",  testModel.getScore_unit());
                map.put("test_description",  testModel.getTest_description());
                map.put("purpose",  testModel.getPurpose());
                map.put("Administrative_Suggestions",  testModel.getAdministrative_Suggestions());
                map.put("Equipment_Required",  testModel.getEquipment_Required());
                map.put("scoring",  testModel.getScoring());
                map.put("test_applicable", testModel.getTest_applicable());
                map.put("multiple_lane", ""+testModel.getMultipleLane()); map.put("timer_type", ""+testModel.getTimerType());
                map.put("final_position",""+testModel.getFinalPosition());
                map.put("video_link",testModel.getVideoLink());
                Log.e(TAG, "test type id==> " + testModel.getSub_test_id());
                Log.e(TAG, "inside test type id==> " +test_type_id_list);

                if(test_type_id_list.contains(""+testModel.getSub_test_id())) {
                    subCatList.add(map);
                    Log.e(TAG, "sub list size==> " + test_type_id_list.size());
                }
            }

            if(subCatList.size()<1) {
               Toast.makeText(getApplicationContext(),"No test available.",Toast.LENGTH_SHORT).show();
            }

            Log.e(TAG,"subCatList=> " +subCatList);
            scAdapter = new SubCatAdapter(ShowSubTestActivity.this, subCatList);
            recycler_view.setAdapter(scAdapter);
        }

    }

}
