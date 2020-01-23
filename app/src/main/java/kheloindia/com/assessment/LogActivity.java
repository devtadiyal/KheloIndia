package kheloindia.com.assessment;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import kheloindia.com.assessment.adapter.ActivityLogListAdapter;
import kheloindia.com.assessment.model.ActivityLogChildModel;
import kheloindia.com.assessment.model.ActivityStudentModel;
import kheloindia.com.assessment.model.InsertActivityModel;
import kheloindia.com.assessment.model.MyDartClassModel;
import kheloindia.com.assessment.model.MyDartGradeModel;
import kheloindia.com.assessment.model.MyDartInsertActivityModel;
import kheloindia.com.assessment.model.MyDartLessonPlanModel;
import kheloindia.com.assessment.model.MyDartOtherModel;
import kheloindia.com.assessment.model.MyDartPeriodModel;
import kheloindia.com.assessment.model.MyDartSportModel;
import kheloindia.com.assessment.model.MyDartSportSkillModel;
import kheloindia.com.assessment.model.MyDartStudentModel;
import kheloindia.com.assessment.model.ViewActivityModel;
import kheloindia.com.assessment.util.ConnectionDetector;
import kheloindia.com.assessment.util.DBManager;
import kheloindia.com.assessment.util.ItemClickListener;
import kheloindia.com.assessment.util.ProgressDialogUtility;
import kheloindia.com.assessment.util.ResponseListener;
import kheloindia.com.assessment.util.Utility;

import kheloindia.com.assessment.functions.Constant;
import kheloindia.com.assessment.webservice.InsertActivityRequest;
import kheloindia.com.assessment.webservice.StudentActivityRequest;
import kheloindia.com.assessment.webservice.ViewActivityRequest;
import io.fabric.sdk.android.services.concurrency.AsyncTask;

/**
 * Created by CT13 on 2017-06-29.
 */

public class LogActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener, ResponseListener, AdapterView.OnItemSelectedListener {

    //  private RadioGroup student_radio_group_one,student_radio_group_two,student_radio_group_three;
        private RadioGroup student_radio_group_three;
        private RadioButton shape_rb, sports_rb, other_rb;
        private Spinner class_spin, period_spin, shape_sports_other_spin;
        private ArrayList<String> class_spin_data = new ArrayList<>();
        private ArrayList<String> class_ids = new ArrayList<>();
        private ArrayList<String> sports_class_spin_data = new ArrayList<>();
        private ArrayList<String> sports_class_ids = new ArrayList<>();
        //private ArrayList<String> other_class_spin_data = new ArrayList<>();
        private ArrayList<String> period_spin_data = new ArrayList<>();
        private ArrayList<String> shape_sports_other_spin_data = new ArrayList<String>();
        private TextView activity_log_tv, school_tv, class_tv, period_tv;
        private Typeface font_medium, font_light;
        private Toolbar toolbar;
        private ListView log_list_view;
        private LinearLayout other_lt, at_school_lt;
        private Button reset_btn, save_btn, school_btn, leave_btn, holiday_btn, ptm_btn, training_btn, office_btn;
        private EditText remark_edt, boys_edt, girls_edt;
    // private ActivityLogAdapter activityLogAdapter;
        private ItemClickListener mClickListener;
        private ArrayAdapter shape_sports_other_spin_array, period_spin_array, class_spin_array, sports_skill_spin_array;
        private String selected_date;
        private ArrayList<String> lesson_list = new ArrayList<>();
        private ArrayList<String> lesson_id_list = new ArrayList<>();
        private ArrayList<String> sports_skill_spin_data = new ArrayList<String>();
        private ArrayList<String> sports_skill_id_list = new ArrayList<>();
        private ArrayList<String> sports_skill_technique_id_list = new ArrayList<>();
        private ArrayList<String> sports_list = new ArrayList<>();
        private ArrayList<String> sports_id_list = new ArrayList<>();
        private ArrayList<String> other_list = new ArrayList<>();
        private ArrayList<String> other_id_list = new ArrayList<>();
        private ConnectionDetector connectionDetector;
    // private List<ActivityLogHeaderModel> studentActivityHeaderList=new ArrayList<>();
        private List<ActivityLogChildModel> studentActivityChildList1 = new ArrayList<>();
    // private HashMap<ActivityLogHeaderModel, List<ActivityLogChildModel>> studentActivityMap=new HashMap<>();
        private List<ActivityLogChildModel> studentActivityChildList2 = new ArrayList<>();
        private LinearLayout log_lt;
        private ScrollView scroll_view;
        private RelativeLayout shape_sports_other_spin_rlt;
        private ActivityLogListAdapter activityLogAdapter1, activityLogAdapter2;
        private boolean school_checked, leave_checked, holiday_checked, ptm_checked, training_checked;
        private ProgressDialogUtility progressDialogUtility;
        private int class_position_student_list1, class_position_student_list2;
        private RelativeLayout sports_skill_spin_rlt;
        private Spinner sports_skill_spin;
        private int sport_position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        init();
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        Utility.showActionBar(this, toolbar, "");
        activity_log_tv = (TextView) findViewById(R.id.activity_log_tv);
        school_tv = (TextView) findViewById(R.id.school_tv);
//        student_radio_group_one=(RadioGroup)findViewById(R.id.student_radio_group_one);
//        student_radio_group_two=(RadioGroup)findViewById(R.id.student_radio_group_two);
        student_radio_group_three = (RadioGroup) findViewById(R.id.student_radio_group_three);
//        school_rb=(RadioButton)findViewById(R.id.school_rb);
//        leave_rb=(RadioButton)findViewById(R.id.leave_rb);
//        holiday_rb=(RadioButton)findViewById(R.id.holiday_rb);
//        ptm_rb=(RadioButton)findViewById(R.id.ptm_rb);
//        training_rb=(RadioButton)findViewById(R.id.training_rb);
        shape_rb = (RadioButton) findViewById(R.id.shape_rb);
        sports_rb = (RadioButton) findViewById(R.id.sports_rb);
        other_rb = (RadioButton) findViewById(R.id.other_rb);
        class_spin = (Spinner) findViewById(R.id.class_spin);
        period_spin = (Spinner) findViewById(R.id.period_spin);
        shape_sports_other_spin = (Spinner) findViewById(R.id.shape_sports_other_spin);
        log_list_view = (ListView) findViewById(R.id.log_list_view);
        other_lt = (LinearLayout) findViewById(R.id.other_lt);
        reset_btn = (Button) findViewById(R.id.reset_btn);
        save_btn = (Button) findViewById(R.id.save_btn);
        remark_edt = (EditText) findViewById(R.id.remark_edt);
        at_school_lt = (LinearLayout) findViewById(R.id.at_school_lt);
        boys_edt = (EditText) findViewById(R.id.boys_edt);
        girls_edt = (EditText) findViewById(R.id.girls_edt);
        class_tv = (TextView) findViewById(R.id.class_tv);
        period_tv = (TextView) findViewById(R.id.period_tv);
        log_lt = (LinearLayout) findViewById(R.id.log_lt);
        //  scroll_view=(ScrollView)findViewById(R.id.scroll_view);
        shape_sports_other_spin_rlt = (RelativeLayout) findViewById(R.id.shape_sports_other_spin_rlt);

        school_btn = (Button) findViewById(R.id.school_btn);
        leave_btn = (Button) findViewById(R.id.leave_btn);
        holiday_btn = (Button) findViewById(R.id.holiday_btn);

//        ptm_btn=(Button)findViewById(R.id.ptm_btn);
//        training_btn=(Button)findViewById(R.id.training_btn);
        office_btn = (Button) findViewById(R.id.office_btn);
        sports_skill_spin_rlt = (RelativeLayout) findViewById(R.id.sports_skill_spin_rlt);
        sports_skill_spin = (Spinner) findViewById(R.id.sports_skill_spin);
        school_btn.setBackgroundResource(R.color.green);
        school_btn.setTextColor(getResources().getColor(R.color.white));
        school_checked = true;


        school_btn.setOnClickListener(this);
        leave_btn.setOnClickListener(this);
        holiday_btn.setOnClickListener(this);
//        ptm_btn.setOnClickListener(this);
//        training_btn.setOnClickListener(this);
        office_btn.setOnClickListener(this);


//        student_radio_group_one.setOnCheckedChangeListener(this);
//        student_radio_group_two.setOnCheckedChangeListener(this);
        student_radio_group_three.setOnCheckedChangeListener(this);


        class_spin_data.add(getString(R.string.classs));
        sports_class_spin_data.add(getString(R.string.classs));
        class_spin.setOnItemSelectedListener(this);

        period_spin_data.add(getString(R.string.period));
        period_spin_array = new ArrayAdapter<String>(this, R.layout.spinner_item, period_spin_data);
        period_spin_array.setDropDownViewResource(R.layout.spinner_dropdown_item);
        period_spin.setAdapter(period_spin_array);
        period_spin.setOnItemSelectedListener(this);

        shape_sports_other_spin_data.add(getString(R.string.lesson_plan));
        shape_sports_other_spin_array = new ArrayAdapter<String>(this, R.layout.spinner_item, shape_sports_other_spin_data);
        shape_sports_other_spin_array.setDropDownViewResource(R.layout.spinner_dropdown_item);
        shape_sports_other_spin.setAdapter(shape_sports_other_spin_array);
        shape_sports_other_spin.setOnItemSelectedListener(this);

        sports_skill_spin_array = new ArrayAdapter<String>(this, R.layout.spinner_item, sports_skill_spin_data);
        sports_skill_spin_array.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);


        font_medium = Typeface.createFromAsset(getAssets(),
                "fonts/Roboto-Medium" +
                        "_0.ttf");
        font_light = Typeface.createFromAsset(getAssets(),
                "fonts/Roboto-Light" +
                        "_0.ttf");
        activity_log_tv.setTypeface(font_medium);
        school_tv.setTypeface(font_light);
        selected_date = getIntent().getStringExtra("current_date");
        activity_log_tv.setText(selected_date);


        shape_rb.setChecked(true);
        school_tv.setText(Constant.SCHOOL_NAME);

//        log_list_view.setGroupIndicator(null);
//        log_list_view.setChildIndicator(null);
//        log_list_view.setChildDivider(null);
//        log_list_view.setDivider(null);
        reset_btn.setOnClickListener(this);
        save_btn.setOnClickListener(this);
        log_lt.setVisibility(View.GONE);

        Constant.SCHOOL_ID = PreferenceManager.getDefaultSharedPreferences(this).getString("school_id", "");
        Constant.TEST_COORDINATOR_ID = PreferenceManager.getDefaultSharedPreferences(this).getString("test_coordinator_id", "");
        Constant.CAMP_ID =  PreferenceManager.getDefaultSharedPreferences(this).getString("camp_id", "");

        connectionDetector = new ConnectionDetector(this);

        if (connectionDetector.isConnectingToInternet()) {
            ViewActivityRequest viewActivityRequest = new ViewActivityRequest(this, Constant.TEST_COORDINATOR_ID, "", Constant.SCHOOL_ID, "","",Constant.CAMP_ID,"", this);
            viewActivityRequest.hitUserRequest();
        } else if (DBManager.getInstance().getAllTableData(this, DBManager.TBL_LP_MY_DART_CLASS_TABLE, "current_school_id", Constant.SCHOOL_ID, "", "").size() != 0 && DBManager.getInstance().getAllTableData(this, DBManager.TBL_LP_MY_DART_PERIOD_TABLE, "current_school_id", Constant.SCHOOL_ID, "", "").size() != 0) {

            ArrayList arrayClassList = DBManager.getInstance().getAllTableData(this, DBManager.TBL_LP_MY_DART_CLASS_TABLE, "current_school_id", Constant.SCHOOL_ID, "", "");
            for (int i = 0; i < arrayClassList.size(); i++) {
                Object ob = arrayClassList.get(i);
                if (ob instanceof MyDartClassModel) {
                    if(!(((MyDartClassModel) ob).getClass_id().charAt(0)>='A'&& ((MyDartClassModel) ob).getClass_id().charAt(0)<='Z') && !((MyDartClassModel) ob).getClass_id().equals("C")&&Integer.parseInt(((MyDartClassModel) ob).getClass_id())>=4){

                        sports_class_spin_data.add(((MyDartClassModel) ob).getClasss());
                        sports_class_ids.add(((MyDartClassModel) ob).getClass_id());
                    }
                    class_spin_data.add(((MyDartClassModel) ob).getClasss());
                    class_ids.add(((MyDartClassModel) ob).getClass_id());
                }

            }
            ArrayList arrayPeriodList = DBManager.getInstance().getAllTableData(this, DBManager.TBL_LP_MY_DART_PERIOD_TABLE, "current_school_id", Constant.SCHOOL_ID, "", "");
            for (int i = 0; i < arrayPeriodList.size(); i++) {
                Object ob = arrayPeriodList.get(i);
                if (ob instanceof MyDartPeriodModel) {
                    period_spin_data.add(((MyDartPeriodModel) ob).getPeriod());
                }
            }
            period_spin.setAdapter(period_spin_array);
        } else {
            Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show();
        }


        progressDialogUtility = new ProgressDialogUtility(this);
        sports_skill_spin_rlt.setVisibility(View.GONE);


    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int id = group.getCheckedRadioButtonId();
        switch (id) {
//
//            case R.id.school_rb:
//
//                student_radio_group_two.setOnCheckedChangeListener(null);
//                student_radio_group_two.clearCheck();
//                student_radio_group_two.setOnCheckedChangeListener(this);
//                reset_btn.setVisibility(View.VISIBLE);
//                at_school_lt.setVisibility(View.VISIBLE);
//                if(!other_rb.isChecked()&&class_spin.getSelectedItemPosition()!=0&& period_spin.getSelectedItemPosition()!=0)
//                log_lt.setVisibility(View.VISIBLE);
//
//
//                break;
//
//            case R.id.leave_rb:
//
//                student_radio_group_two.setOnCheckedChangeListener(null);
//                student_radio_group_two.clearCheck();
//                student_radio_group_two.setOnCheckedChangeListener(this);
//                reset_btn.setVisibility(View.GONE);
//                at_school_lt.setVisibility(View.GONE);
//                log_lt.setVisibility(View.GONE);
//
//                break;
//
//            case R.id.holiday_rb:
//
//                student_radio_group_two.setOnCheckedChangeListener(null);
//                student_radio_group_two.clearCheck();
//                student_radio_group_two.setOnCheckedChangeListener(this);
//                reset_btn.setVisibility(View.GONE);
//                at_school_lt.setVisibility(View.GONE);
//                log_lt.setVisibility(View.GONE);
//
//                break;
//
//            case R.id.ptm_rb:
//                student_radio_group_one.setOnCheckedChangeListener(null);
//                student_radio_group_one.clearCheck();
//                student_radio_group_one.setOnCheckedChangeListener(this);
//                reset_btn.setVisibility(View.GONE);
//                at_school_lt.setVisibility(View.GONE);
//                log_lt.setVisibility(View.GONE);
//
//                break;
//
//            case R.id.training_rb:
//
//                student_radio_group_one.setOnCheckedChangeListener(null);
//                student_radio_group_one.clearCheck();
//                student_radio_group_one.setOnCheckedChangeListener(this);
//                reset_btn.setVisibility(View.GONE);
//                at_school_lt.setVisibility(View.GONE);
//                log_lt.setVisibility(View.GONE);
//
//                break;


            case R.id.shape_rb:

                class_spin_array = new ArrayAdapter<String>(this, R.layout.spinner_item, class_spin_data);
                class_spin_array.setDropDownViewResource(R.layout.spinner_dropdown_item);
                class_spin.setAdapter(class_spin_array);
                other_lt.setVisibility(View.GONE);
                reset();
                break;

            case R.id.sports_rb:
                class_spin_array = new ArrayAdapter<String>(this, R.layout.spinner_item, sports_class_spin_data);
                class_spin_array.setDropDownViewResource(R.layout.spinner_dropdown_item);
                class_spin.setAdapter(class_spin_array);
                period_spin.setSelection(0);
                sports_list.clear();
                other_lt.setVisibility(View.GONE);
                class_position_student_list1=0;
                remark_edt.setText("");
                break;


            default:
                class_spin_array = new ArrayAdapter<String>(this, R.layout.spinner_item, class_spin_data);
                class_spin_array.setDropDownViewResource(R.layout.spinner_dropdown_item);
                class_spin.setAdapter(class_spin_array);
                period_spin.setSelection(0);
                other_lt.setVisibility(View.VISIBLE);
                log_lt.setVisibility(View.GONE);
                remark_edt.setText("");
                shape_sports_other_spin_data.clear();
                shape_sports_other_spin_data.add(getString(R.string.other));
                other_list.clear();
                other_id_list.clear();
                ArrayList arrayOthersList = DBManager.getInstance().getAllTableData(this, DBManager.TBL_LP_MY_DART_OTHER_TABLE, "current_school_id", Constant.SCHOOL_ID, "", "");
                if (arrayOthersList.size() != 0) {
                    for (int i = 0; i < arrayOthersList.size(); i++) {
                        Object ob = arrayOthersList.get(i);
                        if (ob instanceof MyDartOtherModel) {
                            other_list.add(((MyDartOtherModel) ob).getOther());
                            other_id_list.add(((MyDartOtherModel) ob).getOther_id());
                        }

                    }
                }

                shape_sports_other_spin_data.addAll(other_list);
                shape_sports_other_spin.setAdapter(shape_sports_other_spin_array);
                sports_skill_spin_rlt.setVisibility(View.GONE);

                break;
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private boolean validate() {


        if (school_checked && (class_spin.getSelectedItemPosition() == 0 || period_spin.getSelectedItemPosition() == 0)) {

            Toast.makeText(this, R.string.validate_class_period_drop_down, Toast.LENGTH_SHORT).show();
            return false;

        } else if (school_checked && shape_rb.isChecked() && shape_sports_other_spin.getSelectedItemPosition() == 0) {

            Toast.makeText(this, R.string.validate_lesson_drop_down, Toast.LENGTH_SHORT).show();
            return false;
        } else if (school_checked && other_rb.isChecked() && shape_sports_other_spin.getSelectedItemPosition() == 0) {

            Toast.makeText(this, R.string.validate_other_drop_down, Toast.LENGTH_SHORT).show();
            return false;


        } else if (school_checked && sports_rb.isChecked() && shape_sports_other_spin.getSelectedItemPosition() == 0 && sports_list.size()!=0) {

            Toast.makeText(this, R.string.validate_sports_drop_down, Toast.LENGTH_SHORT).show();
            return false;

        } else if (school_checked && other_rb.isChecked() && (TextUtils.isEmpty(boys_edt.getText().toString().trim()) || TextUtils.isEmpty(girls_edt.getText().toString().trim()))) {

            Toast.makeText(this, R.string.validate_boys_girls, Toast.LENGTH_SHORT).show();
            return false;
//        } else if (school_checked && sports_rb.isChecked() &&  sports_list.size()!=0&& sports_skill_id_list.size() == 0) {
//
//            Toast.makeText(this, R.string.validate_sport_skill, Toast.LENGTH_SHORT).show();
//            return false;
        } else if (TextUtils.isEmpty(remark_edt.getText().toString().trim())) {

            Toast.makeText(this, R.string.validate_remarks, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.reset_btn:
                reset();
                break;

            case R.id.save_btn:

                if (validate())
                    dataToBeSentToServer();

                break;


            case R.id.school_btn:

                school_btn.setBackgroundResource(R.color.green);
                leave_btn.setBackgroundResource(android.R.color.transparent);
                holiday_btn.setBackgroundResource(android.R.color.transparent);
//                ptm_btn.setBackgroundResource(android.R.color.transparent);
//                training_btn.setBackgroundResource(android.R.color.transparent);
                office_btn.setBackgroundResource(android.R.color.transparent);
                school_btn.setTextColor(getResources().getColor(R.color.white));
                leave_btn.setTextColor(getResources().getColor(R.color.grey));
                holiday_btn.setTextColor(getResources().getColor(R.color.grey));
//                ptm_btn.setTextColor(getResources().getColor(R.color.grey));
//                training_btn.setTextColor(getResources().getColor(R.color.grey));
                office_btn.setTextColor(getResources().getColor(R.color.grey));
                school_checked = true;
                leave_checked = false;
                holiday_checked = false;
                ptm_checked = false;
                training_checked = false;

                reset_btn.setVisibility(View.VISIBLE);
                at_school_lt.setVisibility(View.VISIBLE);

                reset();

                break;

            case R.id.leave_btn:

                leave_btn.setBackgroundResource(R.color.green);
                school_btn.setBackgroundResource(android.R.color.transparent);
                holiday_btn.setBackgroundResource(android.R.color.transparent);
//                ptm_btn.setBackgroundResource(android.R.color.transparent);
//                training_btn.setBackgroundResource(android.R.color.transparent);
                office_btn.setBackgroundResource(android.R.color.transparent);
                leave_btn.setTextColor(getResources().getColor(R.color.white));
                school_btn.setTextColor(getResources().getColor(R.color.grey));
                holiday_btn.setTextColor(getResources().getColor(R.color.grey));
//                ptm_btn.setTextColor(getResources().getColor(R.color.grey));
//                training_btn.setTextColor(getResources().getColor(R.color.grey));
                office_btn.setTextColor(getResources().getColor(R.color.grey));
                school_checked = false;
                leave_checked = true;
                holiday_checked = false;
                ptm_checked = false;
                training_checked = false;
                reset_btn.setVisibility(View.GONE);
                at_school_lt.setVisibility(View.GONE);
                log_lt.setVisibility(View.GONE);
                remark_edt.setText("");

                break;


            case R.id.holiday_btn:

                holiday_btn.setBackgroundResource(R.color.green);
                leave_btn.setBackgroundResource(android.R.color.transparent);
                school_btn.setBackgroundResource(android.R.color.transparent);
//                ptm_btn.setBackgroundResource(android.R.color.transparent);
//                training_btn.setBackgroundResource(android.R.color.transparent);
                office_btn.setBackgroundResource(android.R.color.transparent);
                holiday_btn.setTextColor(getResources().getColor(R.color.white));
                leave_btn.setTextColor(getResources().getColor(R.color.grey));
                school_btn.setTextColor(getResources().getColor(R.color.grey));
//                ptm_btn.setTextColor(getResources().getColor(R.color.grey));
//                training_btn.setTextColor(getResources().getColor(R.color.grey));
                office_btn.setTextColor(getResources().getColor(R.color.grey));
                school_checked = false;
                leave_checked = false;
                holiday_checked = true;
                ptm_checked = false;
                training_checked = false;
                reset_btn.setVisibility(View.GONE);
                at_school_lt.setVisibility(View.GONE);
                log_lt.setVisibility(View.GONE);
                remark_edt.setText("");

                break;


//            case R.id.ptm_btn:
//                ptm_btn.setBackgroundResource(R.color.green);
//                leave_btn.setBackgroundResource(android.R.color.transparent);
//                holiday_btn.setBackgroundResource(android.R.color.transparent);
//                school_btn.setBackgroundResource(android.R.color.transparent);
//                training_btn.setBackgroundResource(android.R.color.transparent);
//                office_btn.setBackgroundResource(android.R.color.transparent);
//                ptm_btn.setTextColor(getResources().getColor(R.color.white));
//                leave_btn.setTextColor(getResources().getColor(R.color.grey));
//                holiday_btn.setTextColor(getResources().getColor(R.color.grey));
//                school_btn.setTextColor(getResources().getColor(R.color.grey));
//                training_btn.setTextColor(getResources().getColor(R.color.grey));
//                office_btn.setTextColor(getResources().getColor(R.color.grey));
//                school_checked=false;
//                leave_checked=false;
//                holiday_checked=false;
//                ptm_checked=true;
//                training_checked=false;
//                reset_btn.setVisibility(View.GONE);
//                at_school_lt.setVisibility(View.GONE);
//                log_lt.setVisibility(View.GONE);
//
//
//                break;
//
//
//            case R.id.training_btn:
//                training_btn.setBackgroundResource(R.color.green);
//                leave_btn.setBackgroundResource(android.R.color.transparent);
//                holiday_btn.setBackgroundResource(android.R.color.transparent);
//                ptm_btn.setBackgroundResource(android.R.color.transparent);
//                school_btn.setBackgroundResource(android.R.color.transparent);
//                office_btn.setBackgroundResource(android.R.color.transparent);
//                training_btn.setTextColor(getResources().getColor(R.color.white));
//                leave_btn.setTextColor(getResources().getColor(R.color.grey));
//                holiday_btn.setTextColor(getResources().getColor(R.color.grey));
//                ptm_btn.setTextColor(getResources().getColor(R.color.grey));
//                school_btn.setTextColor(getResources().getColor(R.color.grey));
//                office_btn.setTextColor(getResources().getColor(R.color.grey));
//                school_checked=false;
//                leave_checked=false;
//                holiday_checked=false;
//                ptm_checked=false;
//                training_checked=true;
//                reset_btn.setVisibility(View.GONE);
//                at_school_lt.setVisibility(View.GONE);
//                log_lt.setVisibility(View.GONE);
//                break;

            case R.id.office_btn:
                office_btn.setBackgroundResource(R.color.green);
                leave_btn.setBackgroundResource(android.R.color.transparent);
                holiday_btn.setBackgroundResource(android.R.color.transparent);
//                ptm_btn.setBackgroundResource(android.R.color.transparent);
//                training_btn.setBackgroundResource(android.R.color.transparent);
                school_btn.setBackgroundResource(android.R.color.transparent);
                office_btn.setTextColor(getResources().getColor(R.color.white));
                leave_btn.setTextColor(getResources().getColor(R.color.grey));
                holiday_btn.setTextColor(getResources().getColor(R.color.grey));
//                ptm_btn.setTextColor(getResources().getColor(R.color.grey));
//                training_btn.setTextColor(getResources().getColor(R.color.grey));
                school_btn.setTextColor(getResources().getColor(R.color.grey));
                school_checked = false;
                leave_checked = false;
                holiday_checked = false;
                ptm_checked = false;
                training_checked = false;
                reset_btn.setVisibility(View.GONE);
                at_school_lt.setVisibility(View.GONE);
                log_lt.setVisibility(View.GONE);
                remark_edt.setText("");
                break;



        }

    }


    private void dataToBeSentToServer() {
        JSONArray jsonArray = null;
        JSONObject jsonObject;
        ActivityLogChildModel activityLogChildModel;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        Constant.CAMP_ID = sp.getString("camp_id", "");
        InsertActivityRequest insertActivityRequest;
        MyDartInsertActivityModel myDartInsertActivityModel;
        try {
            if (school_checked) {
                
                if (sports_rb.isChecked()) {
                    if (studentActivityChildList1.size() != 0){
                        jsonArray = new JSONArray();
                    for (int i = 0; i < studentActivityChildList1.size(); i++) {
                        activityLogChildModel = studentActivityChildList1.get(i);
                        jsonObject = new JSONObject();
                        jsonObject.put("StudentID", activityLogChildModel.getStudent_id());
                        jsonObject.put("TrainerID", Constant.TEST_COORDINATOR_ID);
                        if (activityLogAdapter1 != null)
                            jsonObject.put("Gradeid", String.valueOf(activityLogAdapter1.selectedItems.get(i) + 1));
                        jsonObject.put("Class", sports_class_spin_data.get(class_spin.getSelectedItemPosition()));
                        jsonObject.put("Period", period_spin_data.get(period_spin.getSelectedItemPosition()));
                        jsonObject.put("TermId", Constant.CAMP_ID);
                        jsonObject.put("ActivityID", "1");
//                            if (shape_rb.isChecked()) {
//                                jsonObject.put("LessionID", lesson_id_list.get(shape_sports_other_spin.getSelectedItemPosition() - 1));
//                                jsonObject.put("Student_ActivityID", "1");
//                            }

                        jsonObject.put("SkillID", sports_id_list.get(shape_sports_other_spin.getSelectedItemPosition() - 1));
                        jsonObject.put("Student_ActivityID", "2");
                        if (sports_skill_id_list.size() != 0) {
                            String full_skill_data[] = sports_skill_spin_data.get(sports_skill_spin.getSelectedItemPosition()).split("\\(");
                            //  jsonObject.put("sskillname", full_skill_data[0]);
                            jsonObject.put("SSkillid", sports_skill_id_list.get(sports_skill_spin.getSelectedItemPosition()));
                            jsonObject.put("technique_ID", sports_skill_technique_id_list.get(sports_skill_spin.getSelectedItemPosition()));
//                                    if(full_skill_data.length>1)
//                                        jsonObject.put("Technique_Name", full_skill_data[1].substring(0, full_skill_data[1].length() - 1));
//                                    else
//                                        jsonObject.put("Technique_Name", "");
                        }

                        jsonObject.put("CreatedBy", Constant.TEST_COORDINATOR_ID);
                        jsonObject.put("Modifiedby", Constant.TEST_COORDINATOR_ID);
                        if(!selected_date.equals(Utility.getDate(System.currentTimeMillis(),"dd MMM yyyy"))){
                            jsonObject.put("Created_On", selected_date+Utility.getDate(System.currentTimeMillis()," HH:mm:ss:SSS"));
                            jsonObject.put("ModifiedOn", selected_date+Utility.getDate(System.currentTimeMillis()," HH:mm:ss:SSS"));
                        } else{
                            jsonObject.put("Created_On", Constant.getDateTime());
                            jsonObject.put("ModifiedOn", Constant.getDateTime());
                        }
                        jsonObject.put("Remarks", remark_edt.getText().toString());
                        jsonObject.put("SchoolID", Constant.SCHOOL_ID);
                        jsonObject.put("Boys", "");
                        jsonObject.put("Girl", "");
                        jsonArray.put(jsonObject);

                    }
                        if (connectionDetector.isConnectingToInternet()) {
                            progressDialogUtility.showProgressDialog();
                            insertActivityRequest = new InsertActivityRequest(this, jsonArray.toString(), "1", "2", this);
                            insertActivityRequest.hitUserRequest();
                        } else {
                            myDartInsertActivityModel = new MyDartInsertActivityModel();
                            myDartInsertActivityModel.setTest_coordinator_id(Constant.TEST_COORDINATOR_ID);
                            myDartInsertActivityModel.setSchool_id(Constant.SCHOOL_ID);
                            myDartInsertActivityModel.setData(jsonArray.toString());
                            myDartInsertActivityModel.setActivity_id("1");
                            myDartInsertActivityModel.setStudent_activity_id("2");
                            DBManager.getInstance().insertTables(DBManager.TBL_LP_MY_DART_INSERT_ACTIVITY_TABLE, myDartInsertActivityModel);
                            // if( DBManager.getInstance().getAllTableData(this,DBManager.TBL_LP_MY_DART_INSERT_ACTIVITY_TABLE,"current_school_id",Constant.SCHOOL_ID,"","").size()>0)
                            Toast.makeText(this, R.string.data_save_success, Toast.LENGTH_SHORT).show();
                            reset();
                        }
                }else{

                        showDataSaveDialog();
                    }
                    

                } else if (shape_rb.isChecked()) {
                    if (studentActivityChildList2.size() != 0){
                    jsonArray = new JSONArray();
                    for (int i = 0; i < studentActivityChildList2.size(); i++) {
                        activityLogChildModel = studentActivityChildList2.get(i);
                        jsonObject = new JSONObject();
                        jsonObject.put("StudentID", activityLogChildModel.getStudent_id());
                        jsonObject.put("TrainerID", Constant.TEST_COORDINATOR_ID);
                        if (activityLogAdapter2 != null)
                            jsonObject.put("Gradeid", String.valueOf(activityLogAdapter2.selectedItems.get(i) + 1));
                        jsonObject.put("Class", class_spin_data.get(class_spin.getSelectedItemPosition()));
                        jsonObject.put("Period", period_spin_data.get(period_spin.getSelectedItemPosition()));
                        jsonObject.put("TermId", Constant.CAMP_ID);
                        jsonObject.put("ActivityID", "1");
                        jsonObject.put("LessionID", lesson_id_list.get(shape_sports_other_spin.getSelectedItemPosition() - 1));
                        jsonObject.put("Student_ActivityID", "1");
                        jsonObject.put("CreatedBy", Constant.TEST_COORDINATOR_ID);
                        if(!selected_date.equals(Utility.getDate(System.currentTimeMillis(),"dd MMM yyyy"))){
                            jsonObject.put("Created_On", selected_date+Utility.getDate(System.currentTimeMillis()," HH:mm:ss:SSS"));
                            jsonObject.put("ModifiedOn", selected_date+Utility.getDate(System.currentTimeMillis()," HH:mm:ss:SSS"));
                        } else{
                            jsonObject.put("Created_On", Constant.getDateTime());
                            jsonObject.put("ModifiedOn", Constant.getDateTime());
                        }
                        jsonObject.put("Modifiedby", Constant.TEST_COORDINATOR_ID);
                        jsonObject.put("Remarks", remark_edt.getText().toString());
                        jsonObject.put("SchoolID", Constant.SCHOOL_ID);
                        jsonObject.put("Boys", "");
                        jsonObject.put("Girl", "");
                        jsonArray.put(jsonObject);

                        }

                        if (connectionDetector.isConnectingToInternet()) {
                            progressDialogUtility.showProgressDialog();
                            insertActivityRequest = new InsertActivityRequest(this, jsonArray.toString(), "1", "1", this);
                            insertActivityRequest.hitUserRequest();
                        } else {
                            myDartInsertActivityModel = new MyDartInsertActivityModel();
                            myDartInsertActivityModel.setTest_coordinator_id(Constant.TEST_COORDINATOR_ID);
                            myDartInsertActivityModel.setSchool_id(Constant.SCHOOL_ID);
                            myDartInsertActivityModel.setData(jsonArray.toString());
                            myDartInsertActivityModel.setActivity_id("1");
                            myDartInsertActivityModel.setStudent_activity_id("1");
                            DBManager.getInstance().insertTables(DBManager.TBL_LP_MY_DART_INSERT_ACTIVITY_TABLE, myDartInsertActivityModel);
                            // if( DBManager.getInstance().getAllTableData(this,DBManager.TBL_LP_MY_DART_INSERT_ACTIVITY_TABLE,"current_school_id",Constant.SCHOOL_ID,"","").size()>0)
                            Toast.makeText(this, R.string.data_save_success, Toast.LENGTH_SHORT).show();
                            reset();
                        }


                    }else{
                        showDataSaveDialog();
                    }
                } else {

                    jsonArray = new JSONArray();
                    jsonObject = new JSONObject();
                    jsonObject.put("StudentID", "");
                    jsonObject.put("TrainerID", Constant.TEST_COORDINATOR_ID);
                    jsonObject.put("Other_Activity_Id", other_id_list.get(shape_sports_other_spin.getSelectedItemPosition() - 1));
                    jsonObject.put("Gradeid", "");
                    jsonObject.put("Class", class_spin_data.get(class_spin.getSelectedItemPosition()));
                    jsonObject.put("Period", period_spin_data.get(period_spin.getSelectedItemPosition()));
                    jsonObject.put("TermId", Constant.CAMP_ID);
                    jsonObject.put("ActivityID", "1");
                    jsonObject.put("Student_ActivityID", "3");
                    jsonObject.put("CreatedBy", Constant.TEST_COORDINATOR_ID);
                    jsonObject.put("Modifiedby", Constant.TEST_COORDINATOR_ID);
                    if(!selected_date.equals(Utility.getDate(System.currentTimeMillis(),"dd MMM yyyy"))){
                        jsonObject.put("Created_On", selected_date+Utility.getDate(System.currentTimeMillis()," HH:mm:ss:SSS"));
                        jsonObject.put("ModifiedOn", selected_date+Utility.getDate(System.currentTimeMillis()," HH:mm:ss:SSS"));
                    } else{
                        jsonObject.put("Created_On", Constant.getDateTime());
                        jsonObject.put("ModifiedOn", Constant.getDateTime());
                    }
                    jsonObject.put("Remarks", remark_edt.getText().toString());
                    jsonObject.put("SchoolID", Constant.SCHOOL_ID);
                    jsonObject.put("Boys", boys_edt.getText().toString());
                    jsonObject.put("Girl", girls_edt.getText().toString());
                    jsonArray.put(jsonObject);
                    if (connectionDetector.isConnectingToInternet()) {
                        progressDialogUtility.showProgressDialog();
                        insertActivityRequest = new InsertActivityRequest(this, jsonArray.toString(), "1", "3", this);
                        insertActivityRequest.hitUserRequest();
                    } else {
                        myDartInsertActivityModel = new MyDartInsertActivityModel();
                        myDartInsertActivityModel.setTest_coordinator_id(Constant.TEST_COORDINATOR_ID);
                        myDartInsertActivityModel.setSchool_id(Constant.SCHOOL_ID);
                        myDartInsertActivityModel.setData(jsonArray.toString());
                        myDartInsertActivityModel.setActivity_id("1");
                        myDartInsertActivityModel.setStudent_activity_id("3");
                        DBManager.getInstance().insertTables(DBManager.TBL_LP_MY_DART_INSERT_ACTIVITY_TABLE, myDartInsertActivityModel);
                        // if( DBManager.getInstance().getAllTableData(this,DBManager.TBL_LP_MY_DART_INSERT_ACTIVITY_TABLE,"current_school_id",Constant.SCHOOL_ID,"","").size()>0)
                        Toast.makeText(this, R.string.data_save_success, Toast.LENGTH_SHORT).show();
                        reset();
                    }
                }
            } else {

                String checked_activity = "";
                jsonArray = new JSONArray();
                jsonObject = new JSONObject();
                jsonObject.put("StudentID", "");
                jsonObject.put("TrainerID", Constant.TEST_COORDINATOR_ID);
                jsonObject.put("LessionID", "");
                jsonObject.put("Gradeid", "");
                jsonObject.put("Class", "");
                jsonObject.put("Period", "");
                jsonObject.put("TermId", Constant.CAMP_ID);
                if (leave_checked) {
                    checked_activity = "2";
                    jsonObject.put("ActivityID", checked_activity);

                } else if (holiday_checked) {
                    checked_activity = "3";
                    jsonObject.put("ActivityID", checked_activity);

                }
//                    else if(ptm_checked) {
//                        checked_activity="4";
//                        jsonObject.put("ActivityID",checked_activity);
//
//                    }
//                    else if(training_checked) {
//                        checked_activity="4";
//                        jsonObject.put("ActivityID",checked_activity);
//
//                    }
                else {
                    checked_activity = "4";
                    jsonObject.put("ActivityID", checked_activity);

                }

                jsonObject.put("Student_ActivityID", "");
                jsonObject.put("CreatedBy", Constant.TEST_COORDINATOR_ID);
                jsonObject.put("Modifiedby", Constant.TEST_COORDINATOR_ID);
                if(!selected_date.equals(Utility.getDate(System.currentTimeMillis(),"dd MMM yyyy"))){
                    jsonObject.put("Created_On", selected_date+Utility.getDate(System.currentTimeMillis()," HH:mm:ss:SSS"));
                    jsonObject.put("ModifiedOn", selected_date+Utility.getDate(System.currentTimeMillis()," HH:mm:ss:SSS"));
                } else{
                    jsonObject.put("Created_On", Constant.getDateTime());
                    jsonObject.put("ModifiedOn", Constant.getDateTime());
                }
                jsonObject.put("Remarks", remark_edt.getText().toString());
                jsonObject.put("SchoolID", Constant.SCHOOL_ID);
                jsonObject.put("Boys", "");
                jsonObject.put("Girl", "");
                jsonArray.put(jsonObject);

                if (connectionDetector.isConnectingToInternet()) {
                    progressDialogUtility.showProgressDialog();
                    insertActivityRequest = new InsertActivityRequest(this, jsonArray.toString(), checked_activity, "", this);
                    insertActivityRequest.hitUserRequest();

                } else {
                    myDartInsertActivityModel = new MyDartInsertActivityModel();
                    myDartInsertActivityModel.setTest_coordinator_id(Constant.TEST_COORDINATOR_ID);
                    myDartInsertActivityModel.setSchool_id(Constant.SCHOOL_ID);
                    myDartInsertActivityModel.setData(jsonArray.toString());
                    myDartInsertActivityModel.setActivity_id(checked_activity);
                    myDartInsertActivityModel.setStudent_activity_id("");
                    DBManager.getInstance().insertTables(DBManager.TBL_LP_MY_DART_INSERT_ACTIVITY_TABLE, myDartInsertActivityModel);
                    // if( DBManager.getInstance().getAllTableData(this,DBManager.TBL_LP_MY_DART_INSERT_ACTIVITY_TABLE,"current_school_id",Constant.SCHOOL_ID,"","").size()>0)
                    Toast.makeText(this, R.string.data_save_success, Toast.LENGTH_SHORT).show();
                    reset();

                }


            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void reset() {
        class_spin.setSelection(0);
        period_spin.setSelection(0);
        lesson_list.clear();
        sports_list.clear();
        shape_sports_other_spin.setSelection(0);
//        class_position_student_list2=0;
//        class_position_student_list1=0;
        sports_skill_spin_rlt.setVisibility(View.GONE);
        remark_edt.setText("");
        boys_edt.setText("");
        girls_edt.setText("");
        }

    @Override
    public void onResponse(Object obj) {
        if (obj instanceof ViewActivityModel) {
            ViewActivityModel viewActivityModel = (ViewActivityModel) obj;

            if (viewActivityModel.getMessage().equalsIgnoreCase("success")) {

                Log.e("LogActivity", "status==> sucess");

                Log.e("LogActivity","viewActivityModel.getS365().size()===> "+viewActivityModel.getS365().size());
                Log.e("LogActivity", "viewActivityModel.getTopSkill().size()===> "+viewActivityModel.getTopSkill().size());
                    if (class_spin.getSelectedItemPosition() == 0 && shape_sports_other_spin.getSelectedItemPosition() == 0) {
                        MyDartClassModel myDartClassModel;
                        MyDartPeriodModel myDartPeriodModel;
                        MyDartOtherModel myDartOtherModel;
                        MyDartGradeModel myDartGradeModel;
                        DBManager.getInstance().deleteRows(this, DBManager.TBL_LP_MY_DART_PERIOD_TABLE, "current_school_id", Constant.SCHOOL_ID, "", "");
                        DBManager.getInstance().deleteRows(this, DBManager.TBL_LP_MY_DART_CLASS_TABLE, "current_school_id", Constant.SCHOOL_ID, "", "");
                        DBManager.getInstance().deleteRows(this, DBManager.TBL_LP_MY_DART_OTHER_TABLE, "current_school_id", Constant.SCHOOL_ID, "", "");
                        DBManager.getInstance().deleteRows(this, DBManager.TBL_LP_MY_DART_GRADE_TABLE, "current_school_id", Constant.SCHOOL_ID, "", "");
                        Constant.student_spin_data.clear();
                        for (int i = 0; i < viewActivityModel.getPeriod().size(); i++) {
                            myDartPeriodModel = new MyDartPeriodModel();
                            myDartPeriodModel.setTest_coordinator_id(Constant.TEST_COORDINATOR_ID);
                            myDartPeriodModel.setSchool_id(Constant.SCHOOL_ID);
                            myDartPeriodModel.setPeriod(viewActivityModel.getPeriod().get(i).getPeriod());
                            DBManager.getInstance().insertTables(DBManager.TBL_LP_MY_DART_PERIOD_TABLE, myDartPeriodModel);
                        }
for(int i=0;i<viewActivityModel.getStuGrade().size();i++){
             myDartGradeModel =new MyDartGradeModel();
             myDartGradeModel.setCurrent_school_id(Constant.SCHOOL_ID);
             myDartGradeModel.setGrade(viewActivityModel.getStuGrade().get(i).getGrade());
             myDartGradeModel.setGrade_id(viewActivityModel.getStuGrade().get(i).getGradeId());
    DBManager.getInstance().insertTables(DBManager.TBL_LP_MY_DART_GRADE_TABLE, myDartGradeModel);
    }
                        ArrayList arrayPeriodList = DBManager.getInstance().getAllTableData(this, DBManager.TBL_LP_MY_DART_PERIOD_TABLE, "current_school_id", Constant.SCHOOL_ID, "", "");

                        for (int i = 0; i < arrayPeriodList.size(); i++) {
                            Object ob = arrayPeriodList.get(i);
                            if (ob instanceof MyDartPeriodModel)
                                period_spin_data.add(((MyDartPeriodModel) ob).getPeriod());

                            }

                        for (int i = 0; i < viewActivityModel.getStudentClasses().size(); i++) {
                                myDartClassModel = new MyDartClassModel();
                                myDartClassModel.setTest_coordinator_id(Constant.TEST_COORDINATOR_ID);
                                myDartClassModel.setSchool_id(Constant.SCHOOL_ID);
                                myDartClassModel.setClasss(viewActivityModel.getStudentClasses().get(i).getClasss());
                                myDartClassModel.setClass_id(viewActivityModel.getStudentClasses().get(i).getClassID());
                                DBManager.getInstance().insertTables(DBManager.TBL_LP_MY_DART_CLASS_TABLE, myDartClassModel);
                                }

                        ArrayList arrayClassList = DBManager.getInstance().getAllTableData(this, DBManager.TBL_LP_MY_DART_CLASS_TABLE, "current_school_id", Constant.SCHOOL_ID, "", "");
                        for (int i = 0; i < arrayClassList.size(); i++) {
                            Object ob = arrayClassList.get(i);
                            if (ob instanceof MyDartClassModel) {
                                if(!(((MyDartClassModel) ob).getClass_id().charAt(0)>='A'&& ((MyDartClassModel) ob).getClass_id().charAt(0)<='Z')&&Integer.parseInt(((MyDartClassModel) ob).getClass_id())>=4){
                                    sports_class_spin_data.add(((MyDartClassModel) ob).getClasss());
                                    sports_class_ids.add(((MyDartClassModel) ob).getClass_id());

                            }
                                class_spin_data.add(((MyDartClassModel) ob).getClasss());
                                class_ids.add(((MyDartClassModel) ob).getClass_id());
                            }
                        }


                            for (int i = 0; i < viewActivityModel.getOthers().size(); i++) {
                            myDartOtherModel = new MyDartOtherModel();
                            myDartOtherModel.setTest_coordinator_id(Constant.TEST_COORDINATOR_ID);
                            myDartOtherModel.setSchool_id(Constant.SCHOOL_ID);
                            myDartOtherModel.setOther(viewActivityModel.getOthers().get(i).getTestName());
                            myDartOtherModel.setOther_id(viewActivityModel.getOthers().get(i).getTestID());
                            DBManager.getInstance().insertTables(DBManager.TBL_LP_MY_DART_OTHER_TABLE, myDartOtherModel);

                        }
                          class_spin.setAdapter(class_spin_array);
                          period_spin.setAdapter(period_spin_array);
                    }  else if (shape_rb.isChecked()) {
                        lesson_list.clear();
                        lesson_id_list.clear();
                        MyDartLessonPlanModel myDartLessonPlanModel;
                         DBManager.getInstance().deleteRows(this, DBManager.TBL_LP_MY_DART_LESSON_PLAN_TABLE, "current_school_id", Constant.SCHOOL_ID, "class_id", class_ids.get(class_spin.getSelectedItemPosition() - 1));
                         for (int i = 0; i < viewActivityModel.getS365().size(); i++) {
                             myDartLessonPlanModel = new MyDartLessonPlanModel();
                             myDartLessonPlanModel.setTest_coordinator_id(Constant.TEST_COORDINATOR_ID);
                             myDartLessonPlanModel.setSchool_id(Constant.SCHOOL_ID);
                             myDartLessonPlanModel.setClass_id(class_ids.get(class_spin.getSelectedItemPosition() - 1));
                             myDartLessonPlanModel.setLesson(viewActivityModel.getS365().get(i).getTestName());
                             myDartLessonPlanModel.setLesson_id(viewActivityModel.getS365().get(i).getTestID());
                             DBManager.getInstance().insertTables(DBManager.TBL_LP_MY_DART_LESSON_PLAN_TABLE, myDartLessonPlanModel);
                             Object ob = DBManager.getInstance().getAllTableData(this, DBManager.TBL_LP_MY_DART_LESSON_PLAN_TABLE, "current_school_id", Constant.SCHOOL_ID, "class_id", class_ids.get(class_spin.getSelectedItemPosition() - 1)).get(i);
                             if (ob instanceof MyDartLessonPlanModel) {
                                 lesson_list.add(((MyDartLessonPlanModel) ob).getLesson());
                                 lesson_id_list.add(((MyDartLessonPlanModel) ob).getLesson_id());
                             }
                         }

                        shape_sports_other_spin_data.clear();
                        shape_sports_other_spin_data.add(getString(R.string.lesson_plan));
                        shape_sports_other_spin_data.addAll(lesson_list);
                        shape_sports_other_spin.setAdapter(shape_sports_other_spin_array);

                    }

                    else if (viewActivityModel.getTopSkill().size() != 0) {

                        sports_skill_spin_rlt.setVisibility(View.VISIBLE);
                        sports_skill_spin_data.clear();
                        sports_skill_id_list.clear();
                        sports_skill_technique_id_list.clear();
                        MyDartSportSkillModel myDartSportSkillModel;
                        DBManager.getInstance().deleteRows(this, DBManager.TBL_LP_MY_DART_SPORT_SKILL_TABLE, "current_school_id", Constant.SCHOOL_ID, "sport_test_id", sports_id_list.get(shape_sports_other_spin.getSelectedItemPosition() - 1));
                        for (int i = 0; i < viewActivityModel.getTopSkill().size(); i++) {
                            myDartSportSkillModel = new MyDartSportSkillModel();
                            myDartSportSkillModel.setTest_coordinator_id(Constant.TEST_COORDINATOR_ID);
                            myDartSportSkillModel.setSchool_id(Constant.SCHOOL_ID);
                            myDartSportSkillModel.setSport_test_id(sports_id_list.get(shape_sports_other_spin.getSelectedItemPosition() - 1));
                            boolean sub_skill = false;

                            for (int j = 0; j < viewActivityModel.getTopSubSkill().size(); j++) {
                                if (viewActivityModel.getTopSkill().get(i).getSSkillid().equalsIgnoreCase(viewActivityModel.getTopSubSkill().get(j).getSkillID())) {
                                    sub_skill = true;
                                    myDartSportSkillModel.setSport_skill(viewActivityModel.getTopSkill().get(i).getSskillname() + "(" + viewActivityModel.getTopSubSkill().get(j).getTechniqueName() + ")");
                                    myDartSportSkillModel.setSport_skill_id(viewActivityModel.getTopSkill().get(i).getSSkillid());
                                    myDartSportSkillModel.setTechnique_id(viewActivityModel.getTopSubSkill().get(j).getTechniqueID());
                                    DBManager.getInstance().insertTables(DBManager.TBL_LP_MY_DART_SPORT_SKILL_TABLE, myDartSportSkillModel);
                                }

                            }

                            if (!sub_skill) {
                                myDartSportSkillModel.setSport_skill(viewActivityModel.getTopSkill().get(i).getSskillname());
                                myDartSportSkillModel.setSport_skill_id(viewActivityModel.getTopSkill().get(i).getSSkillid());
                                myDartSportSkillModel.setTechnique_id("");
                                DBManager.getInstance().insertTables(DBManager.TBL_LP_MY_DART_SPORT_SKILL_TABLE, myDartSportSkillModel);
                            }

                            ArrayList sportsSkillList = DBManager.getInstance().getAllTableData(this, DBManager.TBL_LP_MY_DART_SPORT_SKILL_TABLE, "current_school_id", Constant.SCHOOL_ID, "sport_skill_id", myDartSportSkillModel.getSport_skill_id());
                            for (int k = 0; k < sportsSkillList.size(); k++) {
                                Object ob = sportsSkillList.get(k);
                                if (ob instanceof MyDartSportSkillModel) {
                                    sports_skill_spin_data.add(((MyDartSportSkillModel) ob).getSport_skill());
                                    sports_skill_id_list.add(((MyDartSportSkillModel) ob).getSport_skill_id());
                                    sports_skill_technique_id_list.add(((MyDartSportSkillModel) ob).getTechnique_id());
                                }
                            }

                        }

                        sports_skill_spin.setAdapter(sports_skill_spin_array);


                    } else if (shape_sports_other_spin.getSelectedItemPosition()!=0&&viewActivityModel.getTopSkill().size() == 0) {
                        sports_skill_spin_data.clear();
                        sports_skill_id_list.clear();
                        sports_skill_spin_rlt.setVisibility(View.GONE);
                    }
                    else if (sports_rb.isChecked() ) {
                        sports_list.clear();
                        sports_id_list.clear();
                        MyDartSportModel myDartSportModel;
                        DBManager.getInstance().deleteRows(this, DBManager.TBL_LP_MY_DART_SPORT_TABLE, "current_school_id", Constant.SCHOOL_ID, "current_class", sports_class_spin_data.get(class_spin.getSelectedItemPosition()));
                        for (int i = 0; i < viewActivityModel.getTopSports().size(); i++) {
                            myDartSportModel = new MyDartSportModel();
                            myDartSportModel.setTest_coordinator_id(Constant.TEST_COORDINATOR_ID);
                            myDartSportModel.setSchool_id(Constant.SCHOOL_ID);
                            myDartSportModel.setCurrent_class(sports_class_spin_data.get(class_spin.getSelectedItemPosition()));
                            myDartSportModel.setSport(viewActivityModel.getTopSports().get(i).getTestName());
                            myDartSportModel.setSport_id(viewActivityModel.getTopSports().get(i).getTestID());
                            DBManager.getInstance().insertTables(DBManager.TBL_LP_MY_DART_SPORT_TABLE, myDartSportModel);
                            Object ob = DBManager.getInstance().getAllTableData(this, DBManager.TBL_LP_MY_DART_SPORT_TABLE, "current_school_id", Constant.SCHOOL_ID, "current_class", sports_class_spin_data.get(class_spin.getSelectedItemPosition())).get(i);
                            if (ob instanceof MyDartSportModel) {
                                sports_list.add(((MyDartSportModel) ob).getSport());
                                sports_id_list.add(((MyDartSportModel) ob).getSport_id());
                            }
                        }

                        shape_sports_other_spin_data.clear();
                        shape_sports_other_spin_data.add(getString(R.string.sports_technique));
                        shape_sports_other_spin_data.addAll(sports_list);
                        shape_sports_other_spin.setAdapter(shape_sports_other_spin_array);

                    }


            }


        } else if (obj instanceof ActivityStudentModel) {
            ActivityStudentModel viewActivityModel = (ActivityStudentModel) obj;

            if (viewActivityModel.getMessage().equalsIgnoreCase("success")) {


//                studentActivityHeaderList.clear();
                ArrayList myDartStudentList = null;
                ActivityLogChildModel activityLogChildModel;
                MyDartStudentModel myDartStudentModel;
                if (sports_rb.isChecked()) {
                    studentActivityChildList1.clear();
                    DBManager.getInstance().deleteDartStudentRows(this, DBManager.TBL_LP_MY_DART_STUDENT_TABLE, "current_school_id", Constant.SCHOOL_ID, "current_class", sports_class_spin_data.get(class_spin.getSelectedItemPosition()), "sport_skill_id", sports_id_list.get(shape_sports_other_spin.getSelectedItemPosition() - 1));

                    for (ActivityStudentModel.SALBean salBean : viewActivityModel.getSAL()) {
                        myDartStudentModel = new MyDartStudentModel();
                        myDartStudentModel.setTest_coordinator_id(Constant.TEST_COORDINATOR_ID);
                        myDartStudentModel.setSchool_id(Constant.SCHOOL_ID);
                        myDartStudentModel.setCurrent_class(sports_class_spin_data.get(class_spin.getSelectedItemPosition()));
                        myDartStudentModel.setSport_skill_id(sports_id_list.get(shape_sports_other_spin.getSelectedItemPosition() - 1));
                        myDartStudentModel.setStudent_name(salBean.getStudentName());
                        myDartStudentModel.setStudent_id(salBean.getStudentId());
                        myDartStudentModel.setStudent_grade_id(salBean.getGradeid());
                        DBManager.getInstance().insertTables(DBManager.TBL_LP_MY_DART_STUDENT_TABLE, myDartStudentModel);
                    }
                    myDartStudentList = DBManager.getInstance().getAllTableDartStudentData(this,"current_school_id", Constant.SCHOOL_ID, "current_class", sports_class_spin_data.get(class_spin.getSelectedItemPosition()), "sport_skill_id", sports_id_list.get(shape_sports_other_spin.getSelectedItemPosition() - 1));
                    for (int i = 0; i < myDartStudentList.size(); i++) {
                        Object ob = myDartStudentList.get(i);
                        if (ob instanceof MyDartStudentModel) {
                            activityLogChildModel = new ActivityLogChildModel();
                            activityLogChildModel.setStudent_id(((MyDartStudentModel) ob).getStudent_id());
                            activityLogChildModel.setStudent_name(((MyDartStudentModel) ob).getStudent_name());
                            activityLogChildModel.setStudent_grade_id(((MyDartStudentModel) ob).getStudent_grade_id());
                            studentActivityChildList1.add(activityLogChildModel);
                        }
                    }

                    if (studentActivityChildList1.size() != 0) {


//              ActivityLogHeaderModel activityLogHeaderModel=new ActivityLogHeaderModel();
//                activityLogHeaderModel.setStudent_class("Class: "+class_spin_data.get(class_spin.getSelectedItemPosition()));
//                activityLogHeaderModel.setPeriod("Period: "+period_spin_data.get(period_spin.getSelectedItemPosition()));

                        //studentActivityHeaderList.add(activityLogHeaderModel);
                        //studentActivityMap.put(activityLogHeaderModel,studentActivityChildList);
                        log_lt.setVisibility(View.VISIBLE);
                        class_tv.setText("Class: " + sports_class_spin_data.get(class_spin.getSelectedItemPosition()));
                        period_tv.setText("Period: " + period_spin_data.get(period_spin.getSelectedItemPosition()));
                        activityLogAdapter1 = new ActivityLogListAdapter(this, studentActivityChildList1);
                        log_list_view.setAdapter(activityLogAdapter1);
                       // Utility.setListViewHeightBasedOnChildren(log_list_view);
                        new LoadListOnThread().execute();

                    } else {
                        progressDialogUtility.dismissProgressDialog();
                        log_lt.setVisibility(View.GONE);
                        //Toast.makeText(this, R.string.no_student_found, Toast.LENGTH_LONG).show();
                    }
                    class_position_student_list1 = class_spin.getSelectedItemPosition();
                    sport_position = shape_sports_other_spin.getSelectedItemPosition();
                } else {
                    studentActivityChildList2.clear();
                    DBManager.getInstance().deleteDartStudentRows(this, DBManager.TBL_LP_MY_DART_STUDENT_TABLE, "current_school_id", Constant.SCHOOL_ID, "current_class", class_spin_data.get(class_spin.getSelectedItemPosition()), "sport_skill_id", "");
                    for (ActivityStudentModel.SALBean salBean : viewActivityModel.getSAL()) {
                        myDartStudentModel = new MyDartStudentModel();
                        myDartStudentModel.setTest_coordinator_id(Constant.TEST_COORDINATOR_ID);
                        myDartStudentModel.setSchool_id(Constant.SCHOOL_ID);
                        myDartStudentModel.setCurrent_class(class_spin_data.get(class_spin.getSelectedItemPosition()));
                        myDartStudentModel.setSport_skill_id("");
                        myDartStudentModel.setStudent_name(salBean.getStudentName());
                        myDartStudentModel.setStudent_id(salBean.getStudentId());
                        myDartStudentModel.setStudent_grade_id(salBean.getGradeid());
                        DBManager.getInstance().insertTables(DBManager.TBL_LP_MY_DART_STUDENT_TABLE, myDartStudentModel);
                    }
                    myDartStudentList = DBManager.getInstance().getAllTableDartStudentData(this, "current_school_id", Constant.SCHOOL_ID, "current_class", class_spin_data.get(class_spin.getSelectedItemPosition()), "sport_skill_id", "");
                    for (int i = 0; i < myDartStudentList.size(); i++) {
                        Object ob = myDartStudentList.get(i);
                        if (ob instanceof MyDartStudentModel) {
                            activityLogChildModel = new ActivityLogChildModel();
                            activityLogChildModel.setStudent_id(((MyDartStudentModel) ob).getStudent_id());
                            activityLogChildModel.setStudent_name(((MyDartStudentModel) ob).getStudent_name());
                            activityLogChildModel.setStudent_grade_id(((MyDartStudentModel) ob).getStudent_grade_id());
                            studentActivityChildList2.add(activityLogChildModel);
                        }
                    }
                    if (studentActivityChildList2.size() != 0) {

                        log_lt.setVisibility(View.VISIBLE);
                        class_tv.setText("Class: " + class_spin_data.get(class_spin.getSelectedItemPosition()));
                        period_tv.setText("Period: " + period_spin_data.get(period_spin.getSelectedItemPosition()));
                        activityLogAdapter2 = new ActivityLogListAdapter(this, studentActivityChildList2);
                        log_list_view.setAdapter(activityLogAdapter2);
                       // Utility.setListViewHeightBasedOnChildren(log_list_view);
                        new LoadListOnThread().execute();

                    } else {
                        progressDialogUtility.dismissProgressDialog();
                        log_lt.setVisibility(View.GONE);
                       // Toast.makeText(this, R.string.no_student_found, Toast.LENGTH_LONG).show();
                    }
                    class_position_student_list2 = class_spin.getSelectedItemPosition();
                }


            }


        } else if (obj instanceof InsertActivityModel) {
            InsertActivityModel insertActivityModel = (InsertActivityModel) obj;

            if (insertActivityModel.getIsSuccess().equalsIgnoreCase("true")) {
                progressDialogUtility.dismissProgressDialog();
                Toast.makeText(this, R.string.data_save_success, Toast.LENGTH_SHORT).show();
                reset();
            }

        }
        else
            progressDialogUtility.dismissProgressDialog();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {

            case R.id.class_spin:

                if (shape_rb.isChecked()) {

                    loadShapeDropdown(position);

                }else if(sports_rb.isChecked())
                    loadSportsDropdown(position);
                if (!other_rb.isChecked()) {

                    loadStudentList(position, period_spin.getSelectedItemPosition(), shape_sports_other_spin.getSelectedItemPosition());
                }

                break;

            case R.id.period_spin:
                if (!other_rb.isChecked()) {
                    loadStudentList(class_spin.getSelectedItemPosition(), position, shape_sports_other_spin.getSelectedItemPosition());
                }

                break;

            case R.id.shape_sports_other_spin:
                if (sports_rb.isChecked()) {

                    loadSportSkillDropDown(shape_sports_other_spin.getSelectedItemPosition());
                    loadStudentList(class_spin.getSelectedItemPosition(), period_spin.getSelectedItemPosition(), position);
                }

                break;


        }

    }

    private void loadSportSkillDropDown(int sportPosition) {

        if (sportPosition != 0) {
            ArrayList sportSkillArrayList = DBManager.getInstance().getAllTableData(this, DBManager.TBL_LP_MY_DART_SPORT_SKILL_TABLE, "current_school_id", Constant.SCHOOL_ID, "sport_test_id", sports_id_list.get(shape_sports_other_spin.getSelectedItemPosition() - 1));

            if (connectionDetector.isConnectingToInternet()) {
                ViewActivityRequest viewActivityRequest = new ViewActivityRequest(this, Constant.TEST_COORDINATOR_ID, "", Constant.SCHOOL_ID,"", sports_id_list.get(sportPosition - 1),Constant.CAMP_ID,"", this);
                viewActivityRequest.hitUserRequest();

            } else if (sportSkillArrayList.size() != 0) {
                sports_skill_spin_data.clear();
                sports_skill_id_list.clear();
                sports_skill_technique_id_list.clear();
                for (int i = 0; i < sportSkillArrayList.size(); i++) {
                    Object ob = sportSkillArrayList.get(i);
                    if (ob instanceof MyDartSportSkillModel) {
                        sports_skill_spin_data.add(((MyDartSportSkillModel) ob).getSport_skill());
                        sports_skill_id_list.add(((MyDartSportSkillModel) ob).getSport_skill_id());
                        sports_skill_technique_id_list.add(((MyDartSportSkillModel) ob).getTechnique_id());
                    }

                }
                sports_skill_spin_rlt.setVisibility(View.VISIBLE);
                sports_skill_spin.setAdapter(sports_skill_spin_array);

            } else {
                sports_skill_spin_data.clear();
                sports_skill_id_list.clear();
                sports_skill_technique_id_list.clear();
                sports_skill_spin_rlt.setVisibility(View.GONE);
                Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void loadStudentList(int classPosition, int periodPosition, int lessonOrSportPosition) {
        if (classPosition != 0 && periodPosition != 0) {
            if (sports_rb.isChecked() && lessonOrSportPosition != 0) {
                if (class_position_student_list1 != classPosition || sport_position != lessonOrSportPosition) {
                    if (connectionDetector.isConnectingToInternet()) {
                        progressDialogUtility.showProgressDialog();
                        StudentActivityRequest studentActivityRequest = new StudentActivityRequest(this, Constant.SCHOOL_ID, sports_class_spin_data.get(classPosition), sports_id_list.get(lessonOrSportPosition - 1), this);
                        studentActivityRequest.hitUserRequest();

                    } else {
                        studentActivityChildList1.clear();
                        ArrayList myDartStudentList = DBManager.getInstance().getAllTableDartStudentData(this,  "current_school_id", Constant.SCHOOL_ID, "current_class", sports_class_spin_data.get(class_spin.getSelectedItemPosition()), "sport_skill_id", sports_id_list.get(lessonOrSportPosition - 1));
                        for (int i = 0; i < myDartStudentList.size(); i++) {
                            Object ob = myDartStudentList.get(i);
                            if (ob instanceof MyDartStudentModel) {
                                ActivityLogChildModel activityLogChildModel = new ActivityLogChildModel();
                                activityLogChildModel.setStudent_id(((MyDartStudentModel) ob).getStudent_id());
                                activityLogChildModel.setStudent_name(((MyDartStudentModel) ob).getStudent_name());
                                activityLogChildModel.setStudent_grade_id(((MyDartStudentModel) ob).getStudent_grade_id());
                                studentActivityChildList1.add(activityLogChildModel);
                            }
                        }
                        if (studentActivityChildList1.size() != 0) {
                            log_lt.setVisibility(View.VISIBLE);
                            class_tv.setText("Class: " + sports_class_spin_data.get(class_spin.getSelectedItemPosition()));
                            period_tv.setText("Period: " + period_spin_data.get(period_spin.getSelectedItemPosition()));
                            activityLogAdapter1 = new ActivityLogListAdapter(this, studentActivityChildList1);
                            log_list_view.setAdapter(activityLogAdapter1);
                            progressDialogUtility.showProgressDialog();
                            new LoadListOnThread().execute();
                           //Utility.setListViewHeightBasedOnChildren(log_list_view);

                        } else {
                              log_lt.setVisibility(View.GONE);
                            //Toast.makeText(this, R.string.no_student_found, Toast.LENGTH_SHORT).show();
                        }
                        class_position_student_list1 = class_spin.getSelectedItemPosition();
                        sport_position = shape_sports_other_spin.getSelectedItemPosition();
                    }
                } else if (studentActivityChildList1.size() != 0) {
                    log_lt.setVisibility(View.VISIBLE);
                    class_tv.setText("Class: " + sports_class_spin_data.get(class_spin.getSelectedItemPosition()));
                    period_tv.setText("Period: " + period_spin_data.get(period_spin.getSelectedItemPosition()));
                    activityLogAdapter1.notifyDataSetChanged();
                }

            } else if (shape_rb.isChecked()) {
                if (class_position_student_list2 != classPosition) {
                    if (connectionDetector.isConnectingToInternet()) {
                        progressDialogUtility.showProgressDialog();
                        StudentActivityRequest studentActivityRequest = new StudentActivityRequest(this, Constant.SCHOOL_ID, class_spin_data.get(classPosition), "", this);
                        studentActivityRequest.hitUserRequest();

                    } else {
                        studentActivityChildList2.clear();
                        ArrayList myDartStudentList = DBManager.getInstance().getAllTableDartStudentData(this, "current_school_id", Constant.SCHOOL_ID, "current_class", class_spin_data.get(class_spin.getSelectedItemPosition()), "sport_skill_id", "");
                        for (int i = 0; i < myDartStudentList.size(); i++) {
                            Object ob = myDartStudentList.get(i);
                            if (ob instanceof MyDartStudentModel) {
                                ActivityLogChildModel activityLogChildModel = new ActivityLogChildModel();
                                activityLogChildModel.setStudent_id(((MyDartStudentModel) ob).getStudent_id());
                                activityLogChildModel.setStudent_name(((MyDartStudentModel) ob).getStudent_name());
                                activityLogChildModel.setStudent_grade_id(((MyDartStudentModel) ob).getStudent_grade_id());
                                studentActivityChildList2.add(activityLogChildModel);
                            }
                        }
                        if (studentActivityChildList2.size() != 0) {
                            log_lt.setVisibility(View.VISIBLE);
                            class_tv.setText("Class: " + class_spin_data.get(class_spin.getSelectedItemPosition()));
                            period_tv.setText("Period: " + period_spin_data.get(period_spin.getSelectedItemPosition()));
                            activityLogAdapter2 = new ActivityLogListAdapter(this, studentActivityChildList2);
                            log_list_view.setAdapter(activityLogAdapter2);
                            progressDialogUtility.showProgressDialog();
                            new LoadListOnThread().execute();
                          //  Utility.setListViewHeightBasedOnChildren(log_list_view);

                        } else {
                            log_lt.setVisibility(View.GONE);
                           // Toast.makeText(this, R.string.no_student_found, Toast.LENGTH_SHORT).show();
                        }

                        class_position_student_list2 = class_spin.getSelectedItemPosition();

                    }
                } else if (studentActivityChildList2.size() != 0) {
                    log_lt.setVisibility(View.VISIBLE);
                    class_tv.setText("Class: " + class_spin_data.get(class_spin.getSelectedItemPosition()));
                    period_tv.setText("Period: " + period_spin_data.get(period_spin.getSelectedItemPosition()));
                    activityLogAdapter2.notifyDataSetChanged();


                }

            }
        }


    }


    private void loadShapeDropdown(int classPosition) {

        if (classPosition != 0) {
            ArrayList arrayLessonsList = DBManager.getInstance().getAllTableData(this, DBManager.TBL_LP_MY_DART_LESSON_PLAN_TABLE, "current_school_id", Constant.SCHOOL_ID, "class_id", class_ids.get(classPosition - 1));

            if (connectionDetector.isConnectingToInternet()) {
                ViewActivityRequest viewActivityRequest = new ViewActivityRequest(this, Constant.TEST_COORDINATOR_ID, "", Constant.SCHOOL_ID, class_ids.get(classPosition - 1),"",Constant.CAMP_ID,"", this);
                viewActivityRequest.hitUserRequest();
            } else if (arrayLessonsList.size() != 0) {
                lesson_list.clear();
                lesson_id_list.clear();
                for (int i = 0; i < arrayLessonsList.size(); i++) {
                    Object ob = arrayLessonsList.get(i);
                    if (ob instanceof MyDartLessonPlanModel) {
                        lesson_list.add(((MyDartLessonPlanModel) ob).getLesson());
                        lesson_id_list.add(((MyDartLessonPlanModel) ob).getLesson_id());
                    }

                }
            } else {
                lesson_list.clear();
                lesson_id_list.clear();
                Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show();
           }
        }
        shape_sports_other_spin_data.clear();
        shape_sports_other_spin_data.add(getString(R.string.lesson_plan));
        shape_sports_other_spin_data.addAll(lesson_list);
        shape_sports_other_spin.setAdapter(shape_sports_other_spin_array);
        log_lt.setVisibility(View.GONE);

    }

    private void loadSportsDropdown(int classPosition) {

        if (classPosition != 0) {
            ArrayList arraySportsList = DBManager.getInstance().getAllTableData(this, DBManager.TBL_LP_MY_DART_SPORT_TABLE, "current_school_id", Constant.SCHOOL_ID, "current_class", sports_class_spin_data.get(classPosition ));

            if (connectionDetector.isConnectingToInternet()) {
                ViewActivityRequest viewActivityRequest = new ViewActivityRequest(this, Constant.TEST_COORDINATOR_ID, "", Constant.SCHOOL_ID, "","",Constant.CAMP_ID,sports_class_spin_data.get(classPosition ), this);
                viewActivityRequest.hitUserRequest();
            } else if (arraySportsList.size() != 0) {
                sports_list.clear();
                sports_id_list.clear();
                for (int i = 0; i < arraySportsList.size(); i++) {
                    Object ob = arraySportsList.get(i);
                    if (ob instanceof MyDartSportModel) {
                        sports_list.add(((MyDartSportModel) ob).getSport());
                        sports_id_list.add(((MyDartSportModel) ob).getSport_id());
                    }

                }
            } else {
                sports_list.clear();
                sports_id_list.clear();
                Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show();
            }
        }
        shape_sports_other_spin_data.clear();
        shape_sports_other_spin_data.add(getString(R.string.sports_technique));
        shape_sports_other_spin_data.addAll(sports_list);
        shape_sports_other_spin.setAdapter(shape_sports_other_spin_array);
        log_lt.setVisibility(View.GONE);
        sports_skill_spin_rlt.setVisibility(View.GONE);
        studentActivityChildList1.clear();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }




    private class LoadListOnThread extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialogUtility.dismissProgressDialog();

        }
    }

    private void showDataSaveDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        final String Student_ActivityID;
        TextView textView;
        if (sports_rb.isChecked()) {
            textView = new TextView(this);
            textView.setTextSize(Utility.spToPx(this,6));
            textView.setPadding(Utility.dpToPx(this,10),Utility.dpToPx(this,10),Utility.dpToPx(this,10),Utility.dpToPx(this,10));
            textView.setTextColor(getResources().getColor(R.color.black));
            textView.setMovementMethod(LinkMovementMethod.getInstance());
            textView.setText(R.string.data_save_message_sports_alert);
            alertDialog.setView(textView);
            Student_ActivityID="2";
}else {
    textView = new TextView(this);
    textView.setTextSize(Utility.dpToPx(this,6));
    textView.setPadding(Utility.dpToPx(this,10),Utility.dpToPx(this,10),Utility.dpToPx(this,10),Utility.dpToPx(this,10));
    textView.setTextColor(getResources().getColor(R.color.black));
    textView.setMovementMethod(LinkMovementMethod.getInstance());
    textView.setText(R.string.data_save_message_shape_alert);
    alertDialog.setView(textView);
    Student_ActivityID="1";
}

                alertDialog.setPositiveButton("PROCEED", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                try {

                        JSONArray jsonArray = new JSONArray();
                        JSONObject jsonObject = new JSONObject();

                        jsonObject.put("TrainerID", Constant.TEST_COORDINATOR_ID);
                        jsonObject.put("Class",  sports_class_spin_data.get(class_spin.getSelectedItemPosition()));
                        jsonObject.put("Period", period_spin_data.get(period_spin.getSelectedItemPosition()));
                        jsonObject.put("TermId", Constant.CAMP_ID);
                        jsonObject.put("ActivityID", "1");
                        jsonObject.put("Student_ActivityID",  Student_ActivityID);
                        jsonObject.put("CreatedBy", Constant.TEST_COORDINATOR_ID);
                        if(selected_date.equals(Utility.getDate(System.currentTimeMillis(),"dd-MMM-yyyy"))){

                        jsonObject.put("Created_On", selected_date+Utility.getDate(System.currentTimeMillis()," HH:mm:ss:SSS"));
                        jsonObject.put("ModifiedOn", selected_date+Utility.getDate(System.currentTimeMillis()," HH:mm:ss:SSS"));
                        } else{
                            jsonObject.put("Created_On", Constant.getDateTime());
                            jsonObject.put("ModifiedOn", Constant.getDateTime());
                        }
                        if(shape_rb.isChecked())
                            jsonObject.put("LessionID", lesson_id_list.get(shape_sports_other_spin.getSelectedItemPosition() - 1));
                        jsonObject.put("Modifiedby", Constant.TEST_COORDINATOR_ID);
                        jsonObject.put("Remarks", remark_edt.getText().toString());
                        jsonObject.put("SchoolID", Constant.SCHOOL_ID);
                        jsonObject.put("Boys", "");
                        jsonObject.put("Girl", "");
                        jsonArray.put(jsonObject);


                        if (connectionDetector.isConnectingToInternet()) {
                            progressDialogUtility.showProgressDialog();
                            InsertActivityRequest     insertActivityRequest = new InsertActivityRequest(LogActivity.this, jsonArray.toString(), "1",  Student_ActivityID, LogActivity.this);
                            insertActivityRequest.hitUserRequest();
                        } else {
                            MyDartInsertActivityModel  myDartInsertActivityModel = new MyDartInsertActivityModel();
                            myDartInsertActivityModel.setTest_coordinator_id(Constant.TEST_COORDINATOR_ID);
                            myDartInsertActivityModel.setSchool_id(Constant.SCHOOL_ID);
                            myDartInsertActivityModel.setData(jsonArray.toString());
                            myDartInsertActivityModel.setActivity_id("1");
                            myDartInsertActivityModel.setStudent_activity_id(Student_ActivityID);
                            DBManager.getInstance().insertTables(DBManager.TBL_LP_MY_DART_INSERT_ACTIVITY_TABLE, myDartInsertActivityModel);
                            // if( DBManager.getInstance().getAllTableData(this,DBManager.TBL_LP_MY_DART_INSERT_ACTIVITY_TABLE,"current_school_id",Constant.SCHOOL_ID,"","").size()>0)
                            Toast.makeText(LogActivity.this, R.string.data_save_success, Toast.LENGTH_SHORT).show();
                            reset();
                        }


                    dialog.dismiss();
                }catch (Exception e){
                    e.printStackTrace();

                }
            }});
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();


        }

}
