package kheloindia.com.assessment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import kheloindia.com.assessment.functions.Constant;
import kheloindia.com.assessment.functions.GPSTracker;

/**
 * Created by PC10 on 05/16/2017.
 */

public class BalanceActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView dashboard_img;
    Toolbar toolbar;
    Button test_another_btn, reset_btn;
    TextView minutes_tv, seconds_tv, mili_seconds_tv, text_tv;
    Button start_stop_timer_btn;
    LinearLayout linear_layout;
    Boolean isStart = false;

    TextView test_name_tv, sub_test_name_tv;

    private Handler customHandler = new Handler();
    private long startTime = 0L;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;

    TextView school_tv;

    GPSTracker gps;
    double lat, lng ;
    Button save_btn;

    ImageView boy_or_girl_img;

    String TAG = "BalanceActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_agility);

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();

        sub_test_name_tv.setText(Constant.SUB_TEST_TYPE);
        text_tv.setText("Enter "+Constant.SUB_TEST_TYPE+" Test Score");
    }

    private void init() {

        gps = new GPSTracker(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        dashboard_img = (ImageView) toolbar.findViewById(R.id.dashboard_img);
       // test_another_btn = (Button) findViewById(R.id.test_another_btn);
        reset_btn = (Button) findViewById(R.id.reset_btn);
        mili_seconds_tv = (TextView) findViewById(R.id.mili_seconds_tv);
        seconds_tv = (TextView) findViewById(R.id.seconds_tv);
        minutes_tv = (TextView) findViewById(R.id.minutes_tv);
        linear_layout = (LinearLayout) findViewById(R.id.linear_layout);
      //  start_stop_timer_btn = (Button) findViewById(R.id.start_stop_timer_btn);
        test_name_tv = (TextView) toolbar.findViewById(R.id.test_name_tv);
        sub_test_name_tv = (TextView) toolbar.findViewById(R.id.sub_test_name_tv);
        school_tv = (TextView) findViewById(R.id.school_tv);
        save_btn = (Button)findViewById(R.id.save_btn);
        text_tv  = (TextView) findViewById(R.id.text_tv);
        boy_or_girl_img = (ImageView) findViewById(R.id.boy_or_girl_img);


        school_tv.setText(Constant.SCHOOL_NAME);
        start_stop_timer_btn.setText("Start Timer");
        linear_layout.setVisibility(View.INVISIBLE);


        toolbar.setTitle(Constant.TEST_TYPE);
        test_name_tv.setText(Constant.TEST_TYPE);
        sub_test_name_tv.setText(Constant.SUB_TEST_TYPE);
        text_tv.setText("Enter "+Constant.SUB_TEST_TYPE+" Test Score");

        test_another_btn.setOnClickListener(this);
        reset_btn.setOnClickListener(this);
        start_stop_timer_btn.setOnClickListener(this);
        save_btn.setOnClickListener(this);
        sub_test_name_tv.setOnClickListener(this);

        dashboard_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BalanceActivity.this, TestActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
           /* case R.id.test_another_btn :
                Intent i = new Intent(BalanceActivity.this, ScanActivity.class);
                startActivity(i);
                finish();
                break;*/

            case R.id.reset_btn:
                isStart = false;
                minutes_tv.setText("00");
                seconds_tv.setText("00");
                mili_seconds_tv.setText("00");
                break;

           /* case R.id.start_stop_timer_btn:

                if(!isStart){
                    isStart = true;
                    start_stop_timer_btn.setText("Stop TImer");
                    linear_layout.setVisibility(View.INVISIBLE);
                    startTimer();
                } else {
                    isStart = false;
                    start_stop_timer_btn.setText("Start TImer");
                    linear_layout.setVisibility(View.VISIBLE);
                    stopTimer();
                }

                break;*/

            case R.id.save_btn:
                lat = gps.getLatitude();
                lng = gps.getLongitude();
                Log.e(TAG,"lat=> "+lat+"   lng=> "+lng);
                String DateTime = Constant.getDateTimeServer();

                if(minutes_tv.getText().toString().length()>0 ||
                        seconds_tv.getText().toString().length()>0 ||
                        mili_seconds_tv.getText().toString().length()>0){

                    double miliseconds = Constant.COnvertToMiliseconds(minutes_tv.getText().toString(),
                            seconds_tv.getText().toString(), mili_seconds_tv.getText().toString());
                    Log.e(TAG, "miliseconds=> " + miliseconds);
                    Toast.makeText(getApplicationContext(),""+miliseconds,Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.sub_test_name_tv:
                Intent i1 = new Intent(BalanceActivity.this, ShowSubTestActivity.class);
                startActivity(i1);
                break;
        }
    }

    private void stopTimer() {
        customHandler.removeCallbacks(updateTimerThread);
    }

    private void startTimer() {

        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);
    }

    Runnable updateTimerThread = new Runnable() {

        public void run() {
            //
            // timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            // updatedTime = timeSwapBuff + timeInMilliseconds;

            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;

            int mins = (int) ((updatedTime / (1000 * 60)) % 60);
            int hr = (int) ((updatedTime / (1000 * 60 * 60)) % 24);
            int secs = (int) (updatedTime / 1000);
            // int mins = secs / 60;
            // int hr = mins / 60;

            int milliseconds = (int) (updatedTime % 1000);

            secs = secs % 60;

            minutes_tv.setText(String.format("%02d", mins));
            seconds_tv.setText(String.format("%02d", secs));
            mili_seconds_tv.setText(String.format("%02d", milliseconds));

           /* timer.setText("" + String.format("%02d", hr) + ":"
                    + String.format("%02d", mins) + ":"
                    + String.format("%02d", secs));*/
            customHandler.postDelayed(this, 0);
        }

    };

    public void onBackPressed() {
        showExitDialog();
        //moveTaskToBack(true);

    }

    private void showExitDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(BalanceActivity.this);
        alertDialog.setCancelable(false);
        alertDialog.setMessage("Are you sure you want to Exit?");

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.alarm);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {

                dialog.cancel();
                SharedPreferences sp = PreferenceManager
                        .getDefaultSharedPreferences(getApplicationContext());
                sp.edit().clear().commit();
                Intent i = new Intent(BalanceActivity.this, ScanActivity.class);
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

}
