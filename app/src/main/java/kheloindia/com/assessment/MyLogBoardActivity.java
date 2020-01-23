package kheloindia.com.assessment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kheloindia.com.assessment.adapter.MyActivityLogBoardAdapter;
import kheloindia.com.assessment.model.AttendedByModel;
import kheloindia.com.assessment.model.DartBoardHeaderModel;
import kheloindia.com.assessment.model.DartBoardModel;
import kheloindia.com.assessment.model.MyActivityLogBoardItemModel;
import kheloindia.com.assessment.model.MyActivityLogBoardModel;
import kheloindia.com.assessment.util.ConnectionDetector;
import kheloindia.com.assessment.util.ResponseListener;
import kheloindia.com.assessment.util.Utility;

import kheloindia.com.assessment.functions.Constant;
import kheloindia.com.assessment.webservice.DartBoardHeaderRequest;
import kheloindia.com.assessment.webservice.DartBoardRequest;

/**
 * Created by CT13 on 2017-07-04.
 */

public class MyLogBoardActivity extends AppCompatActivity implements ResponseListener {

    private ExpandableListView my_activity_log_board_exp_lv;
    private Toolbar toolbar;
    private TextView school_tv;
    private Typeface font_light;
    private MyActivityLogBoardAdapter myActivityLogBoardAdapter;
    private List<MyActivityLogBoardModel> _listDataHeader;
    private HashMap<MyActivityLogBoardModel, List<MyActivityLogBoardItemModel>> _listDataChild;
    private ConnectionDetector connectionDetector;
    private String date, month, year;
    private TextView no_data_tv;
    private List<MyActivityLogBoardItemModel> myActivityLogBoardItemModelList;
    private String start_time,end_time,attendance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_activity_log_board);
        init();
    }


    private void init() {
        connectionDetector = new ConnectionDetector(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        Utility.showActionBar(this, toolbar, getString(R.string.my_activity_log));
        school_tv = (TextView) findViewById(R.id.school_tv);
        no_data_tv = (TextView) findViewById(R.id.no_data_tv);

        my_activity_log_board_exp_lv = (ExpandableListView) findViewById(R.id.my_activity_log_board_exp_lv);
        font_light = Typeface.createFromAsset(getAssets(),
                "fonts/Roboto-Light" +
                        "_0.ttf");

        school_tv.setTypeface(font_light);
        school_tv.setText(Constant.SCHOOL_NAME);
        my_activity_log_board_exp_lv.setGroupIndicator(null);
        my_activity_log_board_exp_lv.setChildIndicator(null);
        my_activity_log_board_exp_lv.setChildDivider(null);
        my_activity_log_board_exp_lv.setDivider(null);


        String[] date_month_year = getIntent().getStringExtra("date_month_year").split(" ");
        date = date_month_year[0];
        month = Utility.getMonthIndex(date_month_year[1]);
        year = date_month_year[2];
        prepareMyLogBoard();
    }

    private void prepareMyLogBoard() {

        if (connectionDetector.isConnectingToInternet()) {
            DartBoardHeaderRequest dartBoardHeaderRequest = new DartBoardHeaderRequest(this, Constant.SCHOOL_ID, date, month, year, Constant.TEST_COORDINATOR_ID, this);
            dartBoardHeaderRequest.hitUserRequest();

        } else {
            Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show();
            my_activity_log_board_exp_lv.setVisibility(View.GONE);
            no_data_tv.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public void onResponse(Object obj) {
        if (obj instanceof DartBoardModel) {
            DartBoardModel dartBoardModel = (DartBoardModel) obj;
            if (dartBoardModel.getIsSuccess().equalsIgnoreCase("true")) {

                List<DartBoardModel.TClassBean> tClassBeanList = ((DartBoardModel) obj).getTClass();

                _listDataHeader = new ArrayList<>();
                _listDataChild = new HashMap<>();
                MyActivityLogBoardModel myActivityLogBoardModel = new MyActivityLogBoardModel();
                myActivityLogBoardModel.setDate(date + "/" + month + "/" + year);
                myActivityLogBoardModel.setStart_end_time(start_time+" - "+end_time);
                myActivityLogBoardModel.setAttendance(attendance);


                _listDataHeader.add(myActivityLogBoardModel);
                 myActivityLogBoardItemModelList = new ArrayList<>();
                AttendedByModel attendedByModel;
                for (int j = 0; j < tClassBeanList.size(); j++) {
                    MyActivityLogBoardItemModel myActivityLogBoardItemModel = new MyActivityLogBoardItemModel();
                    DartBoardModel.TClassBean tClassBean = tClassBeanList.get(j);
                    myActivityLogBoardItemModel.setClass_Name( tClassBean.getClasss());
                    myActivityLogBoardItemModel.setPeriod(tClassBean.getPeriod());
                    List<DartBoardModel.TClassBean.MydartBean> mydartBeanList = tClassBean.getMydart();
                    ArrayList<AttendedByModel> attendedByModelList = new ArrayList<>();
                    int no_of_boys=0,no_of_girls=0;
                    for (int i = 0; i < mydartBeanList.size(); i++) {
                        DartBoardModel.TClassBean.MydartBean mydartBean = mydartBeanList.get(i);
                        if( mydartBean.getGender().equalsIgnoreCase("m"))
                            no_of_boys++;
                        else
                            no_of_girls++;

                        myActivityLogBoardItemModel.setBoys(String.valueOf(no_of_boys));
                        myActivityLogBoardItemModel.setGirls(String.valueOf(no_of_girls));
                        myActivityLogBoardItemModel.setRemarks(mydartBean.getRemarks());
                        if(mydartBean.getMyClass().contains("I")&& !mydartBean.getMyClass().contains("IV"))
                        myActivityLogBoardItemModel.setLessons_Others(mydartBean.getLessonName());
                        else
                            myActivityLogBoardItemModel.setLessons_Others(mydartBean.getSkill());

                        myActivityLogBoardItemModel.setSkill_name(mydartBean.getSSkillName());
                        attendedByModel = new AttendedByModel();
                        attendedByModel.setName(mydartBean.getStudentName());
                        attendedByModel.setClasss(mydartBean.getMyClass());
                        attendedByModel.setPeriod(mydartBean.getPeriod());
                        attendedByModel.setRemark(mydartBean.getGradeKey());
                        attendedByModelList.add(attendedByModel);
                        myActivityLogBoardItemModel.setAttendedByModelList(attendedByModelList);
                    }
                    myActivityLogBoardItemModelList.add(myActivityLogBoardItemModel);
                }

                _listDataChild.put(myActivityLogBoardModel, myActivityLogBoardItemModelList);

                if (tClassBeanList.size() != 0) {
                    my_activity_log_board_exp_lv.setVisibility(View.VISIBLE);
                    no_data_tv.setVisibility(View.GONE);
                    myActivityLogBoardAdapter = new MyActivityLogBoardAdapter(this, _listDataHeader, _listDataChild);
                    my_activity_log_board_exp_lv.setAdapter(myActivityLogBoardAdapter);
                } else {
                    my_activity_log_board_exp_lv.setVisibility(View.GONE);
                    no_data_tv.setVisibility(View.VISIBLE);

                }

            }
        }else if (obj instanceof DartBoardHeaderModel) {
            DartBoardHeaderModel dartBoardHeaderModel = (DartBoardHeaderModel) obj;
            if (dartBoardHeaderModel.getIsSuccess().equalsIgnoreCase("true")) {
               // for(int i=0;i<dartBoardHeaderModel.getResult().size();i++){

//                    if(resultBean.getDayStatus().equalsIgnoreCase("0")) {
                if(dartBoardHeaderModel.getResult().size()==0){
                    my_activity_log_board_exp_lv.setVisibility(View.GONE);
                    no_data_tv.setVisibility(View.VISIBLE);
                }else{
                        DartBoardHeaderModel.ResultBean resultBean=dartBoardHeaderModel.getResult().get(0);
                        start_time = resultBean.getDayStart();
                        if(start_time.equals(""))
                            start_time =getString(R.string.time_status_entry_empty);
                        attendance=    resultBean.getAttendance();
                        end_time = resultBean.getDayEnd();
                    if(end_time.equals(""))
                        end_time =getString(R.string.time_status_exit_empty);

                   // }
//                    else {


                //    }


               // }
                DartBoardRequest dartBoardRequest = new DartBoardRequest(this, Constant.SCHOOL_ID, date, month, year, Constant.TEST_COORDINATOR_ID, this);
                dartBoardRequest.hitUserRequest();
                }

            }



        }
        }
    }
