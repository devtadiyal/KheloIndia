package kheloindia.com.assessment.util;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import kheloindia.com.assessment.R;
import kheloindia.com.assessment.functions.Constant;

public class TestActivityFunctions {
    public static void setGender(TextView age_gender_tv, ImageView boy_or_girl_img, String gender, String age){
        if (gender.equalsIgnoreCase("1")) {

            age_gender_tv.setText(age+"/"+"F");

        } else if (gender.equalsIgnoreCase("0")) {

            age_gender_tv.setText(age+"/"+"M");
        }

        else if (gender.equalsIgnoreCase("2")){
            age_gender_tv.setText(age+"/"+"T");
        }

        if (gender.equalsIgnoreCase("1")) {
            boy_or_girl_img.setImageResource(R.drawable.girl_blue_i);
        } else if (gender.equalsIgnoreCase("0")) {
            boy_or_girl_img.setImageResource(R.drawable.boy_i);
        }
    }
}
