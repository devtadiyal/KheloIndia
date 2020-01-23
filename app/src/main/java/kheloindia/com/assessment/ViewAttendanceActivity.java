package kheloindia.com.assessment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import kheloindia.com.assessment.model.ViewAttendanceModel;
import kheloindia.com.assessment.util.ConnectionDetector;
import kheloindia.com.assessment.util.ProgressDialogUtility;
import kheloindia.com.assessment.util.ResponseListener;
import kheloindia.com.assessment.util.Utility;

import kheloindia.com.assessment.functions.Constant;
import kheloindia.com.assessment.robotocalendar.RobotoCalendarView;
import kheloindia.com.assessment.webservice.ViewAttendanceRequest;

/**
 * Created by PC10 on 06/28/2017.
 */

public class ViewAttendanceActivity extends AppCompatActivity implements View.OnClickListener,
        RobotoCalendarView.RobotoCalendarListener, ResponseListener {

    private Calendar currentCalendar;
    RobotoCalendarView robotoCalendarView;
    Toolbar toolbar;
    private ConnectionDetector connectionDetector;
    private ProgressDialogUtility progressDialogUtility;
    String month = "";
    String year = "";
    String TAG = "ViewAttendanceActivity";
    TextView school_tv;

    boolean callAPI = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_attendance);
        init();
    }

    private void init() {

        Constant.SHOW_BLUE_EDIT_CIRCLES = true;

        progressDialogUtility = new ProgressDialogUtility(this);
        robotoCalendarView = (RobotoCalendarView) findViewById(R.id.robotoCalendarPicker);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        school_tv = (TextView) findViewById(R.id.school_tv);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        school_tv.setText(Constant.SCHOOL_NAME);

        getList();
    }

    private void getList() {
        connectionDetector = new ConnectionDetector(this);
        if (connectionDetector.isConnectingToInternet()) {


            Calendar c = Calendar.getInstance();
            year = String.valueOf(c.get(Calendar.YEAR));
            month = String.valueOf(c.get(Calendar.MONTH));


            Constant.current_Month = c.get(Calendar.MONTH) + 1;
            Constant.selecte_dMonth = c.get(Calendar.MONTH) + 1;

            putValueInCurrentWeek(c);

            progressDialogUtility.showProgressDialog();
            callAPI = true;
            ViewAttendanceRequest attendanceRequest = new ViewAttendanceRequest(this, Constant.TEST_COORDINATOR_ID, "" + Constant.current_Month, year, this);
            attendanceRequest.hitUserRequest();
        }
    }

    private void putValueInCurrentWeek(Calendar c) {
        SimpleDateFormat format = new SimpleDateFormat("dd");

        String[] days = new String[7];
        int delta = -c.get(GregorianCalendar.DAY_OF_WEEK) + 2; //add 2 if your week start on monday
        c.add(Calendar.DAY_OF_MONTH, delta);
        for (int i = 0; i < 7; i++) {
            days[i] = format.format(c.getTime());
            c.add(Calendar.DAY_OF_MONTH, 1);

            Log.e("dateTag", "" + (c.get(Calendar.MONTH) + 1));

            HashMap<String, Integer> map = new HashMap<String, Integer>();

            map.put("day", Integer.parseInt(days[i]));
            if(Integer.parseInt(days[i])==31){
                map.put("month", (c.get(Calendar.MONTH) ));
            } else {
                map.put("month", (c.get(Calendar.MONTH) + 1));
            }

            Constant.CurrentWeek.add(map);

        }

        Constant.TempCurrentWeekList.clear();

      //  Log.e("Constant.CurrentWeek=== ", "" +Constant.CurrentWeek);
       // Log.e("Constant.current_Month=== ", "" +Constant.current_Month);

        for(int n = 0; n < Constant.CurrentWeek.size(); n++){



            HashMap<String, Integer> map = new HashMap<String, Integer>();
            int month = Constant.CurrentWeek.get(n).get("month");
            int date = Constant.CurrentWeek.get(n).get("day");

            Log.e("n", "" +n);
            Log.e("month", "" +month);
            Log.e("date", "" +date);


            if(month==Constant.current_Month){
                map.put("month",month);
                map.put("day",date);
                Constant.TempCurrentWeekList.add(map);
            }
        }

       /* Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd.MM.yyyy");

        for (int i = 0; i < 7; i++) {
            Log.i("dateTag", sdf.format(cal.getTime()));
            cal.add(Calendar.DAY_OF_WEEK, 1);

        }*/


    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDateSelected(Date date) {


        SimpleDateFormat formatter = new SimpleDateFormat("dd");
        Date date1 = new Date();

        String todayDate = formatter.format(date1);

        int todayDateInt = Integer.parseInt(todayDate);

        Intent i1 = new Intent(ViewAttendanceActivity.this, LogActivity.class);

        String dayDate = Utility.getDate(date.getTime(), "d");

        int DateSelected = Integer.parseInt(dayDate);

        int temp = -1;

        ArrayList<String> tempList = new ArrayList<String>();


        for (int i = 0; i < Constant.CurrentWeek.size(); i++) {
            tempList.add("" + Constant.CurrentWeek.get(i).get("day"));
        }

        Log.e(TAG, "date clicked==> " + dayDate);
        Log.e(TAG, "tempList==> " + tempList);


        if (Constant.current_Month == Constant.selecte_dMonth) {

            for(int i = 0; i < Constant.CurrentWeek.size(); i++){

                if(dayDate.equals(String.valueOf(Constant.CurrentWeek.get(i).get("day")))){
                    temp = i;
                }
            }

                if (tempList.contains(dayDate)) {
                    if(Constant.CurrentWeek.get(temp).get("month")==Constant.selecte_dMonth) {
                    if (DateSelected <= todayDateInt) {
                        i1.putExtra("current_date", Utility.getDate(date.getTime(), "d MMM yyyy"));
                        startActivity(i1);
                    }
                } else {
                        Log.e(TAG, "date clicked for viewing dart board==> " + dayDate);
                        Intent in = new Intent(this, MyLogBoardActivity.class);
                        in.putExtra("date_month_year", Utility.getDate(date.getTime(), "d MMM yyyy"));
                        startActivity(in);
                    }
            }
            else {
                Log.e(TAG, "date clicked for viewing dart board==> " + dayDate);
                Intent in = new Intent(this, MyLogBoardActivity.class);
                in.putExtra("date_month_year", Utility.getDate(date.getTime(), "d MMM yyyy"));
                startActivity(in);
            }

        } else {
            Log.e(TAG, "date clicked for viewing dart board==> " + dayDate);
            Intent in = new Intent(this, MyLogBoardActivity.class);
            in.putExtra("date_month_year", Utility.getDate(date.getTime(), "d MMM yyyy"));
            startActivity(in);
        }

    }

    @Override
    public void onRightButtonClick() {

        callAPI = false;
        Constant.currentMonthIndex++;
        Constant.currentMonth++;
        Clear_updateCalendar();
    }

    private void Clear_updateCalendar() {

        System.out.println("insde the updatecalendar");
        currentCalendar = Calendar.getInstance(Locale.getDefault());
        currentCalendar.add(Calendar.MONTH, Constant.currentMonthIndex);

        Log.e(TAG, "****Clear_updateCalendar****==> ");


        robotoCalendarView.Clear_initializeCalendar(currentCalendar);
        if (Constant.currentMonthIndex == 0) {
            robotoCalendarView.markDayAsCurrentDay(currentCalendar.getTime());
        }


        String year = String.valueOf(currentCalendar.get(Calendar.YEAR));
        String month = String.valueOf(currentCalendar.get(Calendar.MONTH) + 1);

        Constant.selecte_dMonth = currentCalendar.get(Calendar.MONTH) + 1;

        /*Log.e(TAG, "****current_month****==> "+Constant.current_Month);
        Log.e(TAG, "****selecte_dMonth****==> "+Constant.selecte_dMonth);*/


        if (!callAPI) {
            if (connectionDetector.isConnectingToInternet()) {
                callAPI = true;
                progressDialogUtility.showProgressDialog();
                ViewAttendanceRequest attendanceRequest = new ViewAttendanceRequest(this, Constant.TEST_COORDINATOR_ID, month, year, this);
                attendanceRequest.hitUserRequest();
            } else {
                Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void updateCalendar() {
        Log.e(TAG, "****updateCalendar****==> ");
        currentCalendar = Calendar.getInstance(Locale.getDefault());
        currentCalendar.add(Calendar.MONTH, Constant.currentMonthIndex);
        robotoCalendarView.initializeCalendar(currentCalendar);
        if (Constant.currentMonthIndex == 0) {
            robotoCalendarView.markDayAsCurrentDay(currentCalendar.getTime());
        }


        String year = String.valueOf(currentCalendar.get(Calendar.YEAR));
        String month = String.valueOf(currentCalendar.get(Calendar.MONTH) + 1);

        Constant.selecte_dMonth = currentCalendar.get(Calendar.MONTH) + 1;

        /*Log.e(TAG, "****current_month****==> "+Constant.current_Month);
        Log.e(TAG, "****selecte_dMonth****==> "+Constant.selecte_dMonth);*/


    }

    @Override
    public void onLeftButtonClick() {

        callAPI = false;

        Constant.currentMonthIndex--;
        Constant.currentMonth--;
        Clear_updateCalendar();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onResponse(Object obj) {
        if (obj instanceof ViewAttendanceModel) {
            progressDialogUtility.dismissProgressDialog();
            ViewAttendanceModel model;
            model = (ViewAttendanceModel) obj;

            if (model.getIsSuccess().equalsIgnoreCase("true")) {

                Constant.AttendanceList.clear();

                List<ViewAttendanceModel.ResultBean> attendanceList = model.getResult();

                if (attendanceList.size() < 1) {
                    Toast.makeText(this, "No Data found", Toast.LENGTH_SHORT).show();
                }

                for (int i = 0; i < attendanceList.size(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("Day", attendanceList.get(i).getDay());
                    map.put("Month", attendanceList.get(i).getMonth());
                    map.put("Activity_id", attendanceList.get(i).getActivityId());
                    map.put("School_Name", attendanceList.get(i).getSchoolName());

                    Constant.AttendanceList.add(map);

                    Log.e(TAG, "attendance list==> " + Constant.AttendanceList);
                }
                robotoCalendarView.setRobotoCalendarListener(this);

                currentCalendar = Calendar.getInstance(Locale.getDefault());
                robotoCalendarView.markDayAsCurrentDay(currentCalendar.getTime());

                updateCalendar();

                callAPI = false;
            } else {
                progressDialogUtility.dismissProgressDialog();
                Toast.makeText(this, model.getMessage(), Toast.LENGTH_SHORT).show();

                callAPI = false;
            }
        }
    }
}
