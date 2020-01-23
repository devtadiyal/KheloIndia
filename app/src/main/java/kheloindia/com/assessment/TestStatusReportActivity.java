package kheloindia.com.assessment;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AbsListView;


import android.view.WindowManager;

import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kheloindia.com.assessment.adapter.TestReportAdapter;
import kheloindia.com.assessment.model.TestReportHeaderModel;
import kheloindia.com.assessment.model.TestReportModel;
import kheloindia.com.assessment.util.ConnectionDetector;
import kheloindia.com.assessment.util.DBManager;
import kheloindia.com.assessment.util.ProgressDialogUtility;
import kheloindia.com.assessment.util.ResponseListener;
import kheloindia.com.assessment.util.Utility;

import kheloindia.com.assessment.functions.Constant;

/**
 * Created by CT13 on 2017-05-15.
 */

public class TestStatusReportActivity extends AppCompatActivity implements AbsListView.OnScrollListener, ResponseListener {

    private ExpandableListView test_report_ex_lv;
    private RelativeLayout school_bar_rlt;
    private ImageView school_img;
    private TextView school_txt, student_count_txt;
    private Toolbar toolbar;
    private List<TestReportHeaderModel> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<TestReportHeaderModel, List<TestReportModel>> _listDataChild;
    private ArrayList<String> statusList, statusIDList;
    private ArrayList<String> buttonsList;
    private ProgressDialogUtility progressDialogUtility;
    private TestReportAdapter testReportAdapter;
    private TextView no_data_tv;
    private ConnectionDetector connectionDetector;
    private List<TestReportModel> testReportModelList;
    private int selected_class_length;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_status_report_screen);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        init();
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.toolbar_title);
        Utility.showActionBar(this, toolbar, "");
        test_report_ex_lv = (ExpandableListView) findViewById(R.id.test_report_lst);
        school_bar_rlt = (RelativeLayout) findViewById(R.id.school_bar_rlt);
        school_img = (ImageView) findViewById(R.id.school_img);
        school_txt = (TextView) findViewById(R.id.school_txt);
        no_data_tv = (TextView) findViewById(R.id.no_data_tv);
        student_count_txt = (TextView) findViewById(R.id.student_count_txt);
        Typeface font_reg = Typeface.createFromAsset(getAssets(),
                "fonts/Barlow-Regular.ttf");
        progressDialogUtility = new ProgressDialogUtility(this);
        connectionDetector = new ConnectionDetector(this);
        school_txt.setText(Constant.SCHOOL_NAME);
        school_txt.setTypeface(font_reg);
        no_data_tv.setTypeface(font_reg);
        student_count_txt.setTypeface(font_reg);
        title.setText(getString(R.string.test_status_report));
        title.setTypeface(font_reg);


        test_report_ex_lv.setGroupIndicator(null);
        test_report_ex_lv.setChildIndicator(null);
        test_report_ex_lv.setChildDivider(null);
        test_report_ex_lv.setDivider(null);

        final LinearLayout LL = (LinearLayout) findViewById(R.id.school_lt);
        final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, school_txt.getId());
        params.setMargins(Utility.dpToPx(this, 10), Utility.dpToPx(this, 5), Utility.dpToPx(this, 5), Utility.dpToPx(this, 5));
        LL.setLayoutParams(params);
        statusList = getIntent().getStringArrayListExtra("status");
        statusIDList = getIntent().getStringArrayListExtra("status_id");
        selected_class_length = getIntent().getIntExtra("selected_class_length", 0);
        buttonsList = new ArrayList<>();
//       progressBar=new ProgressBar(this);
//       test_report_ex_lv.addFooterView(progressBar);
        test_report_ex_lv.setOnScrollListener(this);


        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        LL.addView(row);
        int screenWidth = Utility.dpToPx(this, getResources().getConfiguration().screenWidthDp) - Utility.dpToPx(this, 15);


        for (int i = 0; i < statusList.size(); i++) {

            final int pos = i;

            if (!statusList.get(i).equalsIgnoreCase("all")) {
                LL.setVisibility(View.VISIBLE);

                final View to_add = getLayoutInflater().inflate(R.layout.test_status_report_element,
                        school_bar_rlt, false);
                ImageView cancel_btn = (ImageView) to_add.findViewById(R.id.cancel_btn);


                final TextView test_txt = (TextView) to_add.findViewById(R.id.test_txt);
                test_txt.setTypeface(font_reg);

                buttonsList.add("Button");

                if (statusList.get(i).contains("Coordination"))
                    test_txt.setText("Coordination" + statusList.get(i).substring(statusList.get(i).indexOf("(")));
                else if (statusList.get(i).contains("Manipulative"))
                    test_txt.setText("M.Skills" + statusList.get(i).substring(statusList.get(i).indexOf("(")));
                else if (statusList.get(i).contains("Body Management"))
                    test_txt.setText("B.M.Skills" + statusList.get(i).substring(statusList.get(i).indexOf("(")));
                else if (statusList.get(i).contains("Locomotor"))
                    test_txt.setText("L.Skills" + statusList.get(i).substring(statusList.get(i).indexOf("(")));
                else if (statusList.get(i).contains("Cardiovascular"))
                    test_txt.setText("Cardio.Endurance" + statusList.get(i).substring(statusList.get(i).indexOf("(")));
                else if (statusList.get(i).equalsIgnoreCase("complete"))
                    test_txt.setText("Comp.");
                else if (statusList.get(i).equalsIgnoreCase("incomplete"))
                    test_txt.setText("Incomp.");
                else
                    test_txt.setText(statusList.get(i));

                to_add.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                int button_width = to_add.getMeasuredWidth() + Utility.dpToPx(this, 5);

                if (button_width > screenWidth) {
                    row = new LinearLayout(this);
                    row.setOrientation(LinearLayout.HORIZONTAL);
                    LL.addView(row);
                    screenWidth = Utility.dpToPx(this, getResources().getConfiguration().screenWidthDp) - Utility.dpToPx(this, 15);
                }

                row.addView(to_add);
                screenWidth = screenWidth - button_width;

                final LinearLayout finalRow = row;
                cancel_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finalRow.removeView(to_add);
                        buttonsList.remove("Button");

                        statusList.set(pos, "All");
                        statusIDList.set(pos, "0");


                        if (buttonsList.size() == 0) {
                            params.setMargins(0, 0, 0, 0);
                            LL.setLayoutParams(params);
                            LL.setVisibility(View.GONE);
                        }


//                    if (connectionDetector.isConnectingToInternet()) {
//                        TestReportRequest testReportRequest = new TestReportRequest(TestStatusReportActivity.this, Constant.CAMP_ID, Constant.SCHOOL_ID, testStatusIDList.get(1), testStatusIDList.get(0), testStatusIDList.get(2), Constant.TEST_COORDINATOR_ID, TestStatusReportActivity.this);
//                        testReportRequest.hitUserRequest();
//                    } else
                        new LongOperation().execute();


                    }
                });
            }


        }
//        if (connectionDetector.isConnectingToInternet()) {
//            TestReportRequest testReportRequest = new TestReportRequest(TestStatusReportActivity.this, Constant.CAMP_ID, Constant.SCHOOL_ID, testStatusIDList.get(1), testStatusIDList.get(0), testStatusIDList.get(2), Constant.TEST_COORDINATOR_ID, TestStatusReportActivity.this);
//            testReportRequest.hitUserRequest();
//        } else
        new LongOperation().execute();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Constant.statusIDAllList = statusIDList;

    }

    private void prepareTestReport() {
        try {
            StringBuilder class_buffer = new StringBuilder("");
            StringBuilder test_buffer = new StringBuilder("");

            boolean some_class = false;
            boolean some_test = false;

            for (int i = 0; i < selected_class_length; i++) {
                if (!statusIDList.get(i).equals("0")) {
                    class_buffer.append("'" + statusList.get(i).substring(0, statusList.get(i).indexOf(" ")) + "',");

                    some_class = true;

                }
            }


            for (int i = selected_class_length; i < statusIDList.size() - 1; i++) {
                if (!statusIDList.get(i).equals("0")) {
                    test_buffer.append("'" + statusIDList.get(i) + "',");

                    some_test = true;
                }


            }


            ArrayList<Object> objectArrayLst = DBManager.getInstance().getTestReport(TestStatusReportActivity.this, Constant.CAMP_ID, Constant.SCHOOL_ID, some_test ? test_buffer.toString().substring(0, test_buffer.length() - 1) : "'0'", some_class ? class_buffer.toString().substring(0, class_buffer.length() - 1) : "'All'", statusIDList.get(statusIDList.size() - 1));
            _listDataHeader = new ArrayList<>();
            _listDataChild = new HashMap<>();
            if (objectArrayLst.size() != 0) {
                TestReportModel ObjtestReportModel = (TestReportModel) objectArrayLst.get(0);
                String Student_registration_num = ObjtestReportModel.getStudent_registration_num();
                TestReportHeaderModel temptrhm = new TestReportHeaderModel();
                temptrhm = new TestReportHeaderModel();
                temptrhm.setStudent_registration_num(ObjtestReportModel.getStudent_registration_num());
                temptrhm.setStudent_name(ObjtestReportModel.getStudent_name());
                temptrhm.setGender(ObjtestReportModel.getGender());
                Student_registration_num = ObjtestReportModel.getStudent_registration_num();
                testReportModelList = new ArrayList<TestReportModel>();
                _listDataHeader.add(temptrhm);
                for (int i = 0; i < objectArrayLst.size(); i++) {
                    ObjtestReportModel = (TestReportModel) objectArrayLst.get(i);
                    if (!ObjtestReportModel.getStudent_registration_num().equals(Student_registration_num)) {

                        _listDataChild.put(temptrhm, testReportModelList);
                        testReportModelList = new ArrayList<TestReportModel>();
                        temptrhm = new TestReportHeaderModel();
                        temptrhm.setStudent_registration_num(ObjtestReportModel.getStudent_registration_num());
                        temptrhm.setStudent_name(ObjtestReportModel.getStudent_name());
                        temptrhm.setGender(ObjtestReportModel.getGender());

                        _listDataHeader.add(temptrhm);
                        Student_registration_num = ObjtestReportModel.getStudent_registration_num();
                    }

                    testReportModelList.add(ObjtestReportModel);
                }

                _listDataChild.put(temptrhm, testReportModelList);

                // StudentMasterModel studentMasterModel = (StudentMasterModel) objectArrayLst.get(i);

//            TestReportModel testReportModel = new TestReportModel();
//            testReportModel.setStudent_name(studentMasterModel.getStudent_name());
//            testReportModel.setGender(studentMasterModel.getGender() == 1 ? "F" : "M");
//            testReportModel.setStudent_registration_num(studentMasterModel.getStudent_registration_num());


                //  ArrayList<Object> objectSubArrayLst = DBManager.getInstance().getTestReport(TestStatusReportActivity.this, DBManager.TBL_LP_FITNESS_TEST_RESULT, "student_id", "" + studentMasterModel.getStudent_id(), "test_name", testStatusList.get(1), "tested", testStatusList.get(2));
//            List<TestReportItemModel> testReportItemModelList = new ArrayList<>();
//
//            for (int j = 0; j < objectSubArrayLst.size(); j++) {
//                FitnessTestResultModel fitnessTestResultModel = (FitnessTestResultModel) objectSubArrayLst.get(j);
//
//                Log.e("TAG", "test name==> " + fitnessTestResultModel.getTestName());
//                Log.e("TAG", "sub test name==> " + fitnessTestResultModel.getSubTestName());
//
//                TestReportItemModel testReportItemModel = new TestReportItemModel();
//                testReportItemModel.setTest_name(fitnessTestResultModel.getTestName());
//                testReportItemModel.setSub_test_name(fitnessTestResultModel.getSubTestName());
//                testReportItemModel.setTested(fitnessTestResultModel.isTestedOrNot());
//                testReportItemModelList.add(testReportItemModel);
//            }
/*
            if (testReportItemModelList.size() != 0) {
                _listDataHeader.add(testReportModel);
                _listDataChild.put(testReportModel, testReportItemModelList);
            }*/
                int tempint = 0;

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
//        if (loading) {
//            if (totalItemCount > previousTotal) {
//                loading = false;
//                previousTotal = totalItemCount;
//                currentPage++;
//            }
//        }
//
//        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
//            // I load the next page of gigs using a background task,
//            // but you can call any function here.
//             new LongOperation().execute();
//            loading = true;
//        }
    }

    @Override
    public void onResponse(Object obj) {

//        if (obj instanceof ReportModel) {
//            _listDataHeader = new ArrayList<>();
//            _listDataChild = new HashMap<>();
//            List<ReportModel.ResultBean> resultBeanList = ((ReportModel) obj).getResult();
//            for (int i = 0; i < resultBeanList.size(); i++) {
//                TestReportModel testReportModel = new TestReportModel();
//                ReportModel.ResultBean resultBean = resultBeanList.get(i);
//                testReportModel.setStudent_name(resultBean.getStudentName());
//                testReportModel.setGender(resultBean.getGender());
//                testReportModel.setCurrent_roll_num(resultBean.getStudentRegistrationNum());
//
//                _listDataHeader.add(testReportModel);
//                testReportItemModelList = new ArrayList<>();
//
//                for (int j = 0; j < resultBean.getTest().size(); j++) {
//                    TestReportItemModel testReportItemModel = new TestReportItemModel();
//                    ReportModel.ResultBean.TestBean testBean = resultBean.getTest().get(j);
//                    testReportItemModel.setTest_name(testBean.getTestName());
//                    testReportItemModel.setTested(testBean.getTestCompleted().equalsIgnoreCase("1"));
//                    testReportItemModelList.add(testReportItemModel);
//
//                    FitnessTestResultModel fitnessTestResultModel=new FitnessTestResultModel();
//                    fitnessTestResultModel.setTestedOrNot(testBean.getTestCompleted().equalsIgnoreCase("1"));
//                    fitnessTestResultModel.setSyncedOrNot(true);
//                    fitnessTestResultModel.setSubTestName(testBean.getTestName());
//                    fitnessTestResultModel.setStudent_id(Integer.parseInt(resultBean.getStudentID()));
//
//                    DBManager.getInstance().deleteTransactionRow(this, DBManager.getInstance().TBL_LP_FITNESS_TEST_RESULT, "student_id", "" +  fitnessTestResultModel.getStudent_id(),
//                            "test_name", "" +  fitnessTestResultModel.getSubTestName());
//                    DBManager.getInstance().insertTables(DBManager.getInstance().TBL_LP_FITNESS_TEST_RESULT, fitnessTestResultModel);
//                }
//                _listDataChild.put(testReportModel, testReportItemModelList);
//            }
//            if (_listDataHeader.size() != 0) {
//                test_report_ex_lv.setVisibility(View.VISIBLE);
//                no_data_tv.setVisibility(View.GONE);
//                testReportAdapter = new TestReportAdapter(TestStatusReportActivity.this, _listDataHeader, _listDataChild);
//                test_report_ex_lv.setAdapter(testReportAdapter);
//            } else {
//                test_report_ex_lv.setVisibility(View.GONE);
//                no_data_tv.setVisibility(View.VISIBLE);
//            }
//
//        } else {
//            test_report_ex_lv.setVisibility(View.GONE);
//            no_data_tv.setVisibility(View.VISIBLE);
//        }

    }


    private class LongOperation extends AsyncTask<String, String, Void> {


        @Override
        protected Void doInBackground(String... params) {

            prepareTestReport();
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialogUtility.showProgressDialog();
            if (testReportAdapter != null) {
                _listDataHeader.clear();
                _listDataChild.clear();
                testReportAdapter = new TestReportAdapter(TestStatusReportActivity.this, _listDataHeader, _listDataChild);
                test_report_ex_lv.setAdapter(testReportAdapter);
                student_count_txt.setText("");

            }
        }

        @Override
        protected void onPostExecute(Void result) {
            progressDialogUtility.dismissProgressDialog();
            if (_listDataHeader.size() != 0) {
                no_data_tv.setVisibility(View.GONE);
                test_report_ex_lv.setVisibility(View.VISIBLE);
                System.out.println("VIEW LIST  " + _listDataHeader + "   " + _listDataChild);
                testReportAdapter = new TestReportAdapter(TestStatusReportActivity.this, _listDataHeader, _listDataChild);
                test_report_ex_lv.setAdapter(testReportAdapter);
                student_count_txt.setText("(" + _listDataHeader.size() + ")");
            } else {
                no_data_tv.setVisibility(View.VISIBLE);
                test_report_ex_lv.setVisibility(View.GONE);
                student_count_txt.setText("");
            }
        }


        @Override
        protected void onProgressUpdate(String... values) {


        }
    }


}
