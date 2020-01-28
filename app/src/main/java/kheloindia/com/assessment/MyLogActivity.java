package kheloindia.com.assessment;

import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.ExpandableListView;
import android.widget.TextView;

import kheloindia.com.assessment.util.Utility;

import kheloindia.com.assessment.functions.Constant;

/**
 * Created by CT13 on 2017-07-03.
 */

public class MyLogActivity extends AppCompatActivity {


    private TextView school_tv,my_activity_log_tv,my_activity_log_title_tv;
    private Typeface font_light,font_medium;
    private Toolbar toolbar;
    private ExpandableListView my_activity_log_list_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_activity_log);
        init();
    }

    private void init(){

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        Utility.showActionBar(this,toolbar,"");
        school_tv=(TextView) findViewById(R.id.school_tv);
        my_activity_log_title_tv=(TextView)findViewById(R.id.my_activity_log_title_tv);
        my_activity_log_tv=(TextView) findViewById(R.id.my_activity_log_tv);
        my_activity_log_list_view=(ExpandableListView) findViewById(R.id.my_activity_log_list_view);

        font_light = Typeface.createFromAsset(getAssets(),
                "fonts/Roboto-Light" +
                        "_0.ttf");
        font_medium = Typeface.createFromAsset(getAssets(),
                "fonts/Roboto-Medium" +
                        "_0.ttf");
        school_tv.setTypeface(font_light);
        school_tv.setText(Constant.SCHOOL_NAME);
        my_activity_log_title_tv.setTypeface(font_medium);

        my_activity_log_list_view.setGroupIndicator(null);
        my_activity_log_list_view.setChildIndicator(null);
        my_activity_log_list_view.setChildDivider(null);
        my_activity_log_list_view.setDivider(null);
    }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
