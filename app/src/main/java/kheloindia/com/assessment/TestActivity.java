package kheloindia.com.assessment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kheloindia.com.assessment.adapter.TestAdapter;
import kheloindia.com.assessment.model.CampTestMapping;
import kheloindia.com.assessment.model.FitnessTestCategoryModel;
import kheloindia.com.assessment.model.FitnessTestTypesModel;
import kheloindia.com.assessment.model.SkillTestTypeModel;
import kheloindia.com.assessment.model.TestTypeModel;
import kheloindia.com.assessment.util.ConnectionDetector;
import kheloindia.com.assessment.util.DBManager;
import kheloindia.com.assessment.util.ItemClickListener;
import kheloindia.com.assessment.util.ResponseListener;
import kheloindia.com.assessment.util.Utility;

import kheloindia.com.assessment.functions.Constant;

/**
 * Created by CT13 on 2017-05-11.
 */

public class TestActivity extends AppCompatActivity implements ItemClickListener, ResponseListener, CompoundButton.OnCheckedChangeListener {


    ArrayList<HashMap<String, String>> testList = new ArrayList<HashMap<String, String>>();
    String TAG = "TestActivity";
    TestAdapter testAdapter;
    private GridView test_lst;
    private TextView school_tv;
    private Toolbar toolbar;
    private ConnectionDetector connectionDetector;
    private DBManager db;
    ProgressDialog dialog;
    SharedPreferences sp;
    ArrayList<HashMap<String,String>> subCatList = new ArrayList<HashMap<String,String>>();
    private Switch all_incomp_switch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_screen);

        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        /*dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setIndeterminate(true);
        dialog.setProgress(0);*/

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        init();
    }


    private void init() {

        db = DBManager.getInstance();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView  title= toolbar.findViewById(R.id.toolbar_title);
        Utility.showActionBar(this, toolbar,"" );
        test_lst = (GridView) findViewById(R.id.test_lst);
        school_tv = (TextView) findViewById(R.id.school_tv);
        all_incomp_switch=(Switch) findViewById(R.id.all_incomp_switch);
        all_incomp_switch.setOnCheckedChangeListener(this);
        Typeface font_reg = Typeface.createFromAsset(getAssets(),
                "fonts/Barlow-Regular.ttf");
//        TestAdapter testAdapter = new TestAdapter(this, test);
//        test_lst.setAdapter(testAdapter);
//        //  testAdapter.setClickListener(this);
        title.setText(getString(R.string.test_select));
        title.setTypeface(font_reg);

        school_tv.setText(Constant.SCHOOL_NAME);
        school_tv.setTypeface(font_reg);

        connectionDetector = new ConnectionDetector(this);

        FetchList();

    }

    private void FetchList() {

        Constant.TEST_ID = "0";
      /*  if (connectionDetector.isConnectingToInternet()) {

            ArrayList<Object> objectArrayListTestType = DBManager.getInstance().getAllTableData(this, DBManager.TBL_LP_FITNESS_TEST_TYPES, "", "","","");

            ArrayList<Object> objectArrayListTestCategory = DBManager.getInstance().getAllTableData1(this, DBManager.TBL_LP_FITNESS_TEST_CATEGORY, "","","","");

            ArrayList<Object> objectArrayListTestSkills = DBManager.getInstance().getAllTableData1(this, DBManager.TBL_LP_SKILL_TEST_TYPE, "","","","");


            String ModifiedDateCatagory = "";
            String ModifiedDateType = "";
            String ModifiedDateSkill = "";

            if(objectArrayListTestType.size()>0){
                ModifiedDateCatagory = DBManager.getInstance().getMaxDate(this, DBManager.TBL_LP_FITNESS_TEST_TYPES,"last_modified_on");

            }

            if(objectArrayListTestType.size()>0){
                ModifiedDateType = DBManager.getInstance().getMaxDate(this, DBManager.TBL_LP_FITNESS_TEST_CATEGORY,"last_modified_on");

            }

            if(objectArrayListTestType.size()>0){
                ModifiedDateSkill = DBManager.getInstance().getMaxDate(this, DBManager.TBL_LP_SKILL_TEST_TYPE,"last_modified_on");

            }


            Log.e(TAG,"ModifiedDateCatagory=> "+ModifiedDateCatagory);
            Log.e(TAG,"ModifiedDateType=> "+ModifiedDateType);
            Log.e(TAG,"ModifiedDateSkill=> "+ModifiedDateSkill);

                TestTypesRequest testRequest = new TestTypesRequest(this, Constant.TEST_ID,ModifiedDateCatagory,
                        ModifiedDateType, ModifiedDateSkill,this);
                testRequest.hitUserRequest();

        } else {*/
            FetchFromDB();
     //   }
    }

    private void FetchFromDB() {
        showList();
    }

    @Override
    public void onItemClick(View view, int position) {
        Constant.TEST_ID = testList.get(position).get("test_type_id");
        Constant.TEST_NAME = testList.get(position).get("test_name");
        Constant.TEST_TYPE = testList.get(position).get("test_name");

        // Toast.makeText(this, Constant.TEST_ID+" "+Constant.TEST_NAME, Toast.LENGTH_SHORT).show();

        if(Constant.TEST_ID.equalsIgnoreCase("3")){
            getSubIDs();
            Constant.SUB_TEST_ID= Constant.WEIGHT_SUB_TEST_ID;
            Intent i = new Intent(TestActivity.this, WeightActivity.class);
            Constant.SUB_TEST_TYPE = "Height & Weight";
            Constant.STUDENT_CATEGORY ="senior_junior";
            startActivity(i);
            finish();
        } else {
            Intent i = new Intent(TestActivity.this, ShowSubTestActivity.class);
            startActivity(i);
            finish();
        }

    }

  /*  private void CheckForListExistence() {

        ArrayList<Object> objectArrayList = DBManager.getInstance().getAllTableData(this,
                DBManager.TBL_LP_FITNESS_TEST_CATEGORY,"test_id", Constant.TEST_ID,"","");

        ArrayList<Object> objectArrayList1 = DBManager.getInstance().getAllTableData(this,
                DBManager.TBL_LP_CAMP_TEST_MAPPING,"camp_id", Constant.CAMP_ID,"school_id",Constant.SCHOOL_ID);

        ArrayList<String> test_type_id_list = new ArrayList<String>();

        for (Object o : objectArrayList1){
            CampTestMapping camptestModel =(CampTestMapping) o;
            test_type_id_list.add(""+camptestModel.getTest_type_id());
        }


        Log.e(TAG, "total list size==> " + objectArrayList.size());
        Log.e(TAG, "sub list size==> " + test_type_id_list.size());

        if (objectArrayList.size() == 0) {
            Toast.makeText(this, "No tests available in database.", Toast.LENGTH_LONG).show();
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

            else {
                Intent i = new Intent(TestActivity.this, ShowSubTestActivity.class);
                startActivity(i);
                finish();
            }
        }

    }*/

    private void getSubIDs() {
        ArrayList<Object> objectArrayList = DBManager.getInstance().getAllTableData(this,
                DBManager.TBL_LP_FITNESS_TEST_CATEGORY,"test_id", Constant.TEST_ID,"","");

        Log.e(TAG, "testtypes size==> " + objectArrayList.size());

        if (objectArrayList.size() == 0) {
            Toast.makeText(this, "No sub test available.", Toast.LENGTH_LONG).show();
        } else {
            for (Object o : objectArrayList) {
                FitnessTestCategoryModel testModel = (FitnessTestCategoryModel) o;
                String sub_test_name = testModel.getTest_name();
                String sub_test_id = ""+testModel.getSub_test_id();

                if(sub_test_name.equalsIgnoreCase("height")){
                    Constant.HEIGHT_SUB_TEST_ID = sub_test_id;
                } else if(sub_test_name.equalsIgnoreCase("weight")){
                    Constant.WEIGHT_SUB_TEST_ID = sub_test_id;
                }
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Constant.checked_incomp){
            all_incomp_switch.setText(getString(R.string.only_incomplete));
            all_incomp_switch.setChecked(false);
        }else{
            all_incomp_switch.setText(getString(R.string.all));
            all_incomp_switch.setChecked(true);
        }
    }

    @Override
    public void onResponse(Object obj) {

        if (obj instanceof TestTypeModel) {

            TestTypeModel testModel = (TestTypeModel) obj;

            if (testModel.getIsSuccess().equalsIgnoreCase("true")) {

              //  db.dropNamedTable(this, db.TBL_LP_FITNESS_TEST_TYPES);


             //   db.dropNamedTable(this, db.TBL_LP_FITNESS_TEST_CATEGORY);



                List<TestTypeModel.ResultBean.TestBean> testsList = testModel.getResult().getTest();
                final FitnessTestTypesModel model = new FitnessTestTypesModel();


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
                        db.deleteTestRow(getApplicationContext(),DBManager.TBL_LP_FITNESS_TEST_TYPES,
                                "test_type_id", ""+testBean.getTest_ID());
                        db.insertTables(db.TBL_LP_FITNESS_TEST_TYPES, model);
                    }
                }

                // ****************************
                List<TestTypeModel.ResultBean.TestSubtypesBean> subTestsList = testModel.getResult().getTestSubtypes();
                FitnessTestCategoryModel inner_model = new FitnessTestCategoryModel();

                for (int j = 0; j < subTestsList.size(); j++) {
                    TestTypeModel.ResultBean.TestSubtypesBean subTestBean = subTestsList.get(j);
                    inner_model.setTest_name(subTestBean.getTest_Name());
                    inner_model.setScore_criteria(subTestBean.getScore_Criteria());
                    inner_model.setScore_measurement(subTestBean.getScore_Measurement());
                    inner_model.setScore_unit(subTestBean.getScore_Unit());
                    inner_model.setSub_test_id(subTestBean.getTest_type_id());
                    inner_model.setTest_id(subTestBean.getTest_Category_ID());
                    inner_model.setTest_description(subTestBean.getTest_Description().replace("'","''"));
                    inner_model.setPurpose(subTestBean.getPurpose());
                    inner_model.setAdministrative_Suggestions(subTestBean.getAdministrative_Suggestions().replace("'","''"));
                    inner_model.setEquipment_Required(subTestBean.getEquipment_Required());
                    inner_model.setScoring(subTestBean.getScoring().replace("'","''"));
                    inner_model.setCreated_on(subTestBean.getCreated_On());
                    inner_model.setLast_modified_on(subTestBean.getLast_Modified_On());




                    int test_id = subTestBean.getTest_Category_ID();

                    // changes

                    ArrayList<Object> objectArrayList = DBManager.getInstance().getAllTableDataTest(this, DBManager.TBL_LP_FITNESS_TEST_TYPES, "", "","","");
                    Log.e(TAG, "testtypes size==> " + objectArrayList.size());

                    testList.clear();

                    for (Object o : objectArrayList) {

                        FitnessTestTypesModel TestModel = (FitnessTestTypesModel) o;
                        int inner_test_id =TestModel.getTest_type_id();

                        if(inner_test_id==test_id){
                            inner_model.setName(TestModel.getTest_name());
                        }

                    }

                    HashMap<String, String> map = null;
                    try {

                        map = db.getParticularRow(getApplicationContext(), DBManager.TBL_LP_FITNESS_TEST_CATEGORY, "sub_test_id", "" + subTestBean.getTest_type_id(), "", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (map.size() == 0) {
                        Log.e(TAG, "insert sub test name=> " + subTestBean.getTest_Name());
                        Log.e(TAG, "insert sub test id=> " + subTestBean.getTest_type_id());
                        Log.e(TAG, "insert test id=> " + subTestBean.getTest_Category_ID());
                        db.insertTables(db.TBL_LP_FITNESS_TEST_CATEGORY, inner_model);

                    } else {
                        Log.e(TAG, "delete sub test name=> " + subTestBean.getTest_Name());
                        Log.e(TAG, "delete sub test id=> " + subTestBean.getTest_type_id());
                        Log.e(TAG, "delete test id=> " + subTestBean.getTest_Category_ID());
                        db.deleteTestRow(getApplicationContext(),DBManager.TBL_LP_FITNESS_TEST_CATEGORY,
                                "sub_test_id", ""+subTestBean.getTest_type_id());
                        db.insertTables(db.TBL_LP_FITNESS_TEST_CATEGORY, inner_model);
                    }
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
                        db.deleteTestRow(getApplicationContext(),DBManager.TBL_LP_SKILL_TEST_TYPE,
                                "checklist_item_id", ""+checkListBean.getCheckListItem_ID());
                        db.insertTables(db.TBL_LP_SKILL_TEST_TYPE, checklist_model);
//                        boolean isDataInserted = sp.getBoolean(Constant.SCHOOL_ID, false);
//                        if(!isDataInserted){
                    //    insertDefaultTransactionData(inner_model);
                         //  new LongOperation((FitnessTestCategoryModel) DBManager.getInstance().getAllTableData(this, DBManager.TBL_LP_FITNESS_TEST_CATEGORY, "", "","","").get(0)).execute();
                       // }

                    }
                }

                //***************************

                // ************************************

                showList();

            } else {
                showList();
               // Toast.makeText(this, testModel.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            try {
                showList();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Toast.makeText(this, R.string.server_down, Toast.LENGTH_SHORT).show();
        }

    }








    private void showList() {

       // ArrayList<Object> objectArrayList = DBManager.getInstance().getAllTableDataTest(this, DBManager.TBL_LP_FITNESS_TEST_TYPES, "school_id", Constant.SCHOOL_ID,"test_coordinator_id",Constant.TEST_COORDINATOR_ID);
        ArrayList<Object> objectArrayList = DBManager.getInstance().getAllTableDataTest(this, DBManager.TBL_LP_FITNESS_TEST_TYPES, "", "","","");
        Log.e(TAG, "testtypes size==> " + objectArrayList.size());

        if (objectArrayList.size() == 0) {
            Toast.makeText(this, "No data available in database. Please enable your internet to get the data directly from the server.", Toast.LENGTH_LONG).show();
        } else {
            testList.clear();

            for (Object o : objectArrayList) {

                HashMap<String, String> map = new HashMap<String, String>();
                FitnessTestTypesModel testModel = (FitnessTestTypesModel) o;
                map.put("test_name", testModel.getTest_name());
                map.put("test_image", "" + testModel.getTest_image());
                map.put("test_img_path", "" + testModel.getTest_img_path());
                map.put("test_type_id", "" + testModel.getTest_type_id());
                boolean isList=getSubIDs_temp(testModel.getTest_type_id());
                if(isList)
                testList.add(map);
            }

            testAdapter = new TestAdapter(this, testList);
            test_lst.setAdapter(testAdapter);
            testAdapter.setClickListener(this);
        }

    }

    public boolean getSubIDs_temp(int test_id) {

        boolean isList = false;
        ArrayList<Object> objectArrayList = DBManager.getInstance().getAllTableData(this,
                DBManager.TBL_LP_FITNESS_TEST_CATEGORY,"test_id", ""+test_id,"","");

        ArrayList<Object> objectArrayList1 = DBManager.getInstance().getAllTableData(this,
                DBManager.TBL_LP_CAMP_TEST_MAPPING,"camp_id", Constant.CAMP_ID,"school_id",Constant.SCHOOL_ID);

        ArrayList<String> test_type_id_list = new ArrayList<String>();

        for (Object o : objectArrayList1){
            CampTestMapping camptestModel =(CampTestMapping) o;
            test_type_id_list.add(""+camptestModel.getTest_type_id());
        }

        if (objectArrayList.size() == 0) {
            isList = false;
            // Toast.makeText(this, "No data available in database.", Toast.LENGTH_LONG).show();
        } else {
            subCatList.clear();

            for (Object o : objectArrayList) {

                HashMap<String, String> map = new HashMap<String, String>();
                FitnessTestCategoryModel testModel = (FitnessTestCategoryModel) o;
                map.put("test_name", testModel.getTest_name());
                map.put("sub_test_id", "" + testModel.getSub_test_id());
                map.put("score_criteria", testModel.getScore_criteria());
                map.put("score_measurement", testModel.getScore_measurement());
                map.put("score_unit", testModel.getScore_unit());
                map.put("test_description", testModel.getTest_description());
                map.put("purpose", testModel.getPurpose());
                map.put("Administrative_Suggestions", testModel.getAdministrative_Suggestions());
                map.put("Equipment_Required", testModel.getEquipment_Required());
                map.put("scoring", testModel.getScoring());
                map.put("test_applicable", testModel.getTest_applicable());
                map.put("multiple_lane", "" + testModel.getMultipleLane());
                map.put("timer_type", "" + testModel.getTimerType());
                map.put("final_position", "" + testModel.getFinalPosition());

                /*Log.e(TAG, "test type id==> " + testModel.getSub_test_id());
                Log.e(TAG, "inside test type id==> " + test_type_id_list);*/

                if (test_type_id_list.contains("" + testModel.getSub_test_id())) {
                    subCatList.add(map);
                    /* Log.e(TAG, "sub list size==> " + test_type_id_list.size());*/
                }
            }

            if (subCatList.size() < 1) {
                isList = false;
            } else {
                isList = true;
            }
        }
        return isList;
    }

    @Override
    public void onBackPressed() {
        //        Intent i = new Intent(TestActivity.this, TakeTestActivity.class);
//        startActivity(i);
        finish();
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if(b){
            all_incomp_switch.setText(getString(R.string.all));
            Constant.checked_incomp=false;
        }else{
            all_incomp_switch.setText(getString(R.string.only_incomplete));
            Constant.checked_incomp=true;
        }
    }
}
