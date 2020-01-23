package kheloindia.com.assessment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;


import kheloindia.com.assessment.fragments.DartFragment;

/**
 * Created by PC10 on 13-Nov-17.
 */

public class TrainerHomeScreen extends AppCompatActivity implements View.OnClickListener{

    LinearLayout dart_layout, assessment_layout, student_connect_layout, liveplus_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_home_screen);
        init();
    }

    private void init() {
        liveplus_layout = (LinearLayout) findViewById(R.id.liveplus_layout);
        student_connect_layout = (LinearLayout) findViewById(R.id.student_connect_layout);
        assessment_layout = (LinearLayout) findViewById(R.id.assessment_layout);
        dart_layout = (LinearLayout) findViewById(R.id.dart_layout);

        liveplus_layout.setOnClickListener(this);
        student_connect_layout.setOnClickListener(this);
        assessment_layout.setOnClickListener(this);
        dart_layout.setOnClickListener(this);

        setDefaultFragment();
    }

    private void setDefaultFragment() {
        Fragment fragment = new DartFragment();
        CallFragment(fragment);
    }

    @Override
    public void onClick(View v) {

        if(v==dart_layout){

            Fragment fragment = new DartFragment();
            CallFragment(fragment);
        }
    }

    private void CallFragment(Fragment fragment) {
        FragmentManager fm = this.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (fragment != null) {
            ft.replace(R.id.frame_layout, fragment);
        } else {
            ft.add(R.id.frame_layout, fragment);
        }
        //ft.addToBackStack(null);
        ft.commit();
    }
}
