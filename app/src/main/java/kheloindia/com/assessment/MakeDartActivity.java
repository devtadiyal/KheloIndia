package kheloindia.com.assessment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Arrays;
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
 * Created by CT13 on 2017-06-30.
 */

public class MakeDartActivity  extends AppCompatActivity implements RobotoCalendarView.RobotoCalendarListener,
        ResponseListener {

    private Toolbar toolbar;
    private TextView school_tv;
    private Calendar currentCalendar;
    private Typeface font_light;
    private ConnectionDetector connectionDetector;
    private ProgressDialogUtility progressDialogUtility;
    RobotoCalendarView robotoCalendarView;
    String month="";
    String year ="";
    String TAG = "MakeDartActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_dart);
        init();
    }

     private void init(){
         Constant.SHOW_BLUE_EDIT_CIRCLES = true;


         progressDialogUtility = new ProgressDialogUtility(this);
         robotoCalendarView = (RobotoCalendarView) findViewById(R.id.robotoCalendarPicker);

         toolbar = (Toolbar) findViewById(R.id.toolbar);
         Utility.showActionBar(this,toolbar,getString(R.string.make_my_dart));
         school_tv=(TextView)findViewById(R.id.school_tv);
         robotoCalendarView.setRobotoCalendarListener(this);
         font_light = Typeface.createFromAsset(getAssets(),
                 "fonts/Roboto-Light" +
                         "_0.ttf");
         school_tv.setTypeface(font_light);
         school_tv.setText(Constant.SCHOOL_NAME);

         getList();

     }

    private void getList() {
        connectionDetector = new ConnectionDetector(this);
        if (connectionDetector.isConnectingToInternet()) {


            Calendar c = Calendar.getInstance();
            year = String.valueOf(c.get(Calendar.YEAR));
            month = String.valueOf(c.get(Calendar.MONTH));

            Constant.current_Month = c.get(Calendar.MONTH)+1;
            Constant.selecte_dMonth = c.get(Calendar.MONTH)+1;

            Log.e(TAG,"TEST_COORDINATOR_ID==>  "+Constant.TEST_COORDINATOR_ID );
            Log.e(TAG,"year==>  "+year );
            Log.e(TAG,"month==>  "+month );


            SimpleDateFormat format = new SimpleDateFormat("dd");

            String[] days = new String[7];
            int delta = -c.get(GregorianCalendar.DAY_OF_WEEK) + 2; //add 2 if your week start on monday
            c.add(Calendar.DAY_OF_MONTH, delta );
            for (int i = 0; i < 7; i++)
            {
                days[i] = format.format(c.getTime());
                c.add(Calendar.DAY_OF_MONTH, 1);


                Log.e("dateTag", ""+(c.get(Calendar.MONTH)+1));

                HashMap<String, Integer> map = new HashMap<String, Integer>();

                map.put("day",Integer.parseInt(days[i]));
                map.put("month",(c.get(Calendar.MONTH)+1));

                    Constant.CurrentWeek.add(map);



                    Log.e(TAG,"Constant.CurrentWeek====> "+Constant.CurrentWeek);
            }


            Log.e(TAG,"Current Week====> "+Arrays.toString(days));




            progressDialogUtility.showProgressDialog();
            // ViewAttendanceRequest attendanceRequest = new ViewAttendanceRequest(this, "15419",month,year, this);

            ViewAttendanceRequest attendanceRequest = new ViewAttendanceRequest(this, Constant.TEST_COORDINATOR_ID,month,year, this);
            attendanceRequest.hitUserRequest();
        }
    }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public void onDateSelected(Date date) {

        SimpleDateFormat formatter = new SimpleDateFormat("dd");
        Date date1 = new Date();

        String todayDate = formatter.format(date1);

        int todayDateInt = Integer.parseInt(todayDate);

        Intent i1 = new Intent(MakeDartActivity.this, LogActivity.class);

        String  dayDate = Utility.getDate(date.getTime(),"dd");

        int DateSelected = Integer.parseInt(dayDate);

        Log.e(TAG,"date clicked==> "+dayDate);
        if(Constant.CurrentWeek.contains(Integer.parseInt(dayDate))) {
            if(DateSelected<=todayDateInt) {
                i1.putExtra("current_date", Utility.getDate(date.getTime(), "dd MMM yyyy"));
                startActivity(i1);
            }
        }

    }

    @Override
    public void onRightButtonClick() {

    }

    @Override
    public void onLeftButtonClick() {

    }


    @Override
    public void onResponse(Object obj) {
        if (obj instanceof ViewAttendanceModel) {
            progressDialogUtility.dismissProgressDialog();
            ViewAttendanceModel model;
            model = (ViewAttendanceModel) obj;

            if (model.getIsSuccess().equalsIgnoreCase("true")) {

                List<ViewAttendanceModel.ResultBean> attendanceList = model.getResult();

                if(attendanceList.size()<1){
                    Toast.makeText(this, "No Data found", Toast.LENGTH_SHORT).show();
                }

                for(int i=0;i<attendanceList.size();i++){
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("Day",attendanceList.get(i).getDay());
                    map.put("Month",attendanceList.get(i).getMonth());
                    map.put("Activity_id",attendanceList.get(i).getActivityId());
                    map.put("School_Name",attendanceList.get(i).getSchoolName());

                    Constant.AttendanceList.add(map);

                    Log.e(TAG,"attendance list==> "+Constant.AttendanceList);
                }
                robotoCalendarView.setRobotoCalendarListener(this);

                currentCalendar = Calendar.getInstance(Locale.getDefault());
                robotoCalendarView.markDayAsCurrentDay(currentCalendar.getTime());

                updateCalendar();
            } else {
                progressDialogUtility.dismissProgressDialog();
                Toast.makeText(this, model.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateCalendar() {
        System.out.println("insde the updatecalendar");
        currentCalendar = Calendar.getInstance(Locale.getDefault());
        currentCalendar.add(Calendar.MONTH, Constant.currentMonthIndex);
        robotoCalendarView.initializeCalendar(currentCalendar);
        if (Constant.currentMonthIndex == 0) {
            robotoCalendarView.markDayAsCurrentDay(currentCalendar.getTime());
        }
    }
}
