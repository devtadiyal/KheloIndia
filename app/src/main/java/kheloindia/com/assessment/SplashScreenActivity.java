package kheloindia.com.assessment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.crashlytics.android.Crashlytics;


import kheloindia.com.assessment.functions.Constant;
import io.fabric.sdk.android.Fabric;
import kheloindia.com.assessment.util.AppConfig;
import kheloindia.com.assessment.util.ConnectionDetector;
import kheloindia.com.assessment.util.DBManager;

/**
 * Created by PC10 on 05/29/2017.
 */

public class SplashScreenActivity extends AppCompatActivity {

    boolean isLogin = false;
    boolean inSchoolActivity = false;
    boolean isStudentDetailGet = false;
    SharedPreferences sp;
    String user_type = "";
    private DBManager db;
    private ConnectionDetector connectionDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash_acreen);
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        db = DBManager.getInstance();
        isLogin = sp.getBoolean(AppConfig.IS_LOGIN, false);
        inSchoolActivity = sp.getBoolean("inSchoolActivity", false);
        isStudentDetailGet = sp.getBoolean(AppConfig.IS_STUDENT_DETAILS_GET, false);
        user_type = sp.getString("user_type","");
        Constant.USER_TYPE = user_type;
        connectionDetector = new ConnectionDetector(this);

             Runnable r=    new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (isLogin) {
                         /*if(inSchoolActivity){
                             if(isStudentDetailGet){
                                 Intent i = new Intent(SplashScreenActivity.this, TakeTestActivity.class);
                                 startActivity(i);
                                 finish();
                             } else {
                                 Intent i = new Intent(SplashScreenActivity.this, SchoolActivity.class);
                                 startActivity(i);
                                 finish();
                             }

                         } else {
                             Intent i = new Intent(SplashScreenActivity.this, SchoolActivity.class);
                             startActivity(i);
                             finish();
                         }*/


                        if(user_type.equals("4")) {
System.out.println("DEV 1"+isLogin);
                            if(isStudentDetailGet){
                                System.out.println("DEV 2");

                                Intent i = new Intent(SplashScreenActivity.this, TakeTestActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                System.out.println("DEV 3");

                                Intent i = new Intent(SplashScreenActivity.this, SchoolActivity.class);
                                startActivity(i);
                                finish();
                            }

                        } else if (user_type.equals("5")||user_type.equals("3")||user_type.equals("1")){
                            System.out.println("DEV 4");

                            Constant.END_URL="?UserName="+sp.getString("test_coordinator_name", "")+"&Password="+sp.getString("test_coordinator_password", "");
                            Constant.USER_URl = "http://23.253.243.116:92/MyHomePage.aspx"+Constant.END_URL;
                            Log.e("Splash","URL===> "+Constant.USER_URl);
                            Intent i = new Intent(SplashScreenActivity.this, DashBoardStudentActivity.class);
                            startActivity(i);
                            finish();
                        } /*else if (user_type.equals("3")){
                             Constant.END_URL="?UserName="+sp.getString("test_coordinator_name", "")+"&Password="+sp.getString("test_coordinator_password", "");
                             Constant.USER_URl = "http://23.253.243.116:92/Fitness365SchoolDashboard.aspx"+Constant.END_URL;
                             Log.e("Splash","URL===> "+Constant.USER_URl);
                             Intent i = new Intent(SplashScreenActivity.this, DashBoardStudentActivity.class);
                             startActivity(i);
                             finish();
                         }*/

                    }


                    else {
                        System.out.println("DEV 5");

                        Intent i = new Intent(SplashScreenActivity.this, KheloDashBoardActivity.class);
                        startActivity(i);
                        finish();

                    }
                    }
            }  ;
            new Thread(r).start();
//            if(connectionDetector.isConnectingToInternet()) {
//                try {
//                    new GetVersionCode(this).execute();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }else{

            //}

        }




    }







