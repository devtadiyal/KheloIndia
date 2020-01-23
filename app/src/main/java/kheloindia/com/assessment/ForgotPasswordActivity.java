package kheloindia.com.assessment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import kheloindia.com.assessment.model.ForgotPasswordModel;
import kheloindia.com.assessment.util.ConnectionDetector;
import kheloindia.com.assessment.util.ResponseListener;
import kheloindia.com.assessment.functions.Constant;
import kheloindia.com.assessment.util.Utility;
import kheloindia.com.assessment.webservice.ForgotPasswordRequest;

/**
 * Created by PC10 on 05/10/2017.
 */

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener, ResponseListener {

    Toolbar toolbar;

    EditText email_edt, new_password_edt, confirm_password_edt;
    Button reset_password_btn;
    LinearLayout email_lt,new_password_lt,confirm_password_lt;
    private ConnectionDetector connectionDetector;

    TextView password_policy_tv, textView, password_policy_body_one_tv, password_policy_body_two_tv, password_policy_body_three_tv;

    Typeface font_light, font_regular, font_medium;
    private boolean validationSuccess;
    private String email, new_password="";
    private String requestType, otp;
    private Dialog otpDialog;
    private boolean resendOtp;
    LinearLayout password_policy_layout;
    private EditText otp_edt;
    boolean CheckForPassword = false;

    private static final String PASSWORD_PATTERN =
            "((?=.*\\d)(?=.*[_@#$%^&+=-]).{6,20})";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forgot_password);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        init();
    }

    private void init() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);

       // toolbar.setTitle("Forgot Password?");

//        setSupportActionBar(toolbar);
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Utility.showActionBar(this,toolbar,"");
        TextView title=   toolbar.findViewById(R.id.toolbar_title);
        title.setText("Forgot Password?");

        font_light = Typeface.createFromAsset(getAssets(),
                "fonts/Barlow-Light.ttf");
        font_regular = Typeface.createFromAsset(getAssets(),
                "fonts/Barlow-Regular.ttf");
        font_medium = Typeface.createFromAsset(getAssets(),
                "fonts/Barlow-Medium.ttf");


        reset_password_btn = (Button) findViewById(R.id.reset_password_btn);
        password_policy_tv = (TextView) findViewById(R.id.password_policy_tv);
        password_policy_body_one_tv = (TextView) findViewById(R.id.password_policy_body_one_tv);
        password_policy_body_two_tv = (TextView) findViewById(R.id.password_policy_body_two_tv);
        password_policy_body_three_tv = (TextView) findViewById(R.id.password_policy_body_three_tv);
        textView = (TextView) findViewById(R.id.textView);
        email_lt = (LinearLayout) findViewById(R.id.email_lt);
        new_password_lt = (LinearLayout) findViewById(R.id.new_password_lt);
        confirm_password_lt = (LinearLayout) findViewById(R.id.confirm_password_lt);
        email_edt = (EditText) findViewById(R.id.email_edt);
        new_password_edt = (EditText) findViewById(R.id.new_password_edt);
        confirm_password_edt = (EditText) findViewById(R.id.confirm_password_edt);
        password_policy_layout = (LinearLayout) findViewById(R.id.password_policy_layout);


        password_policy_tv.setTypeface(font_medium);
        password_policy_body_one_tv.setTypeface(font_regular);
        password_policy_body_two_tv.setTypeface(font_regular);
        password_policy_body_three_tv.setTypeface(font_regular);
        reset_password_btn.setTypeface(font_medium);
        textView.setTypeface(font_regular);
        email_edt.setTypeface(font_regular);
//        new_password_til.setTypeface(font_regular);
//        confirm_password_til.setTypeface(font_regular);
        email_edt.setTypeface(font_regular);
        new_password_edt.setTypeface(font_regular);
        confirm_password_edt.setTypeface(font_regular);

        new_password_edt.setVisibility(View.GONE);
        confirm_password_edt.setVisibility(View.GONE);
        new_password_lt.setVisibility(View.GONE);
        confirm_password_lt.setVisibility(View.GONE);
        password_policy_layout.setVisibility(View.GONE);
        reset_password_btn.setText("Submit");

        password_policy_tv.setOnClickListener(this);
        reset_password_btn.setOnClickListener(this);
        new_password_edt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                final int DRAWABLE_RIGHT = 2;
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= ( new_password_edt.getRight() -   new_password_edt.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                         if( new_password_edt.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                            new_password_edt.setInputType( InputType.TYPE_CLASS_TEXT |
                                    InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            new_password_edt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.pwd_show_i, 0);

                        }else {
                            new_password_edt.setInputType( InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD );
                            new_password_edt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.pwd_hide_i, 0);
                        }
                        //   password_edt.setSelection(password_edt.getText().length());
                        return true;
                    }
                }
                return false;
            }
        });

        confirm_password_edt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                final int DRAWABLE_RIGHT = 2;
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= ( confirm_password_edt.getRight() -  confirm_password_edt.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        if(confirm_password_edt.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                            confirm_password_edt.setInputType( InputType.TYPE_CLASS_TEXT |
                                    InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            confirm_password_edt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.pwd_show_i, 0);

                        }else {
                            confirm_password_edt.setInputType( InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD );
                            confirm_password_edt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.pwd_hide_i, 0);
                        }
                        //   password_edt.setSelection(password_edt.getText().length());
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {

        if (v == reset_password_btn) {

            if(!CheckForPassword){
                if (Validate()) {
                    resendOtp = false;
                    requestType = "0";
                    connectionDetector = new ConnectionDetector(this);
                    if (connectionDetector.isConnectingToInternet()) {
                        ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest(this, requestType, email, "", new_password, this);
                        forgotPasswordRequest.hitUserRequest();
                    } else {
                        Toast.makeText(getApplicationContext(),"No Internet Connection.", Toast.LENGTH_SHORT).show();
                    }


                }
            } else {
              if (ValidateWithpasswordField()) {
                    resendOtp = false;
                    requestType = "1";

                    ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest(ForgotPasswordActivity.this, requestType, email, otp_edt.getText().toString().trim(), new_password, ForgotPasswordActivity.this);
                    forgotPasswordRequest.hitUserRequest();

                  /*  if (!resendOtp) {
                        showOTPDialog();
                    }*/
                   /* ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest(this, requestType, email, "", new_password, this);
                    forgotPasswordRequest.hitUserRequest();*/

           }
            }


        }

    }

    private boolean Validate() {
        validationSuccess = false;

        email = email_edt.getText().toString().trim();
      //  new_password = new_password_edt.getText().toString();
       // String confirm_password = confirm_password_edt.getText().toString();


        email_edt.setError(null);
      //  new_password_til.setError(null);
       // confirm_password_til.setError(null);

        if (email.length() < 1) {
            email_edt.setError("Please enter email id");
            email_edt.requestFocus();
        } else if (!Constant.verify(email, ForgotPasswordActivity.this)) {
            email_edt.requestFocus();
            email_edt.setError("Please enter valid email id");
        }

      /*  else if (new_password.length() < 6) {
            new_password_til.requestFocus();
            new_password_til.setError(getResources().getString(R.string.password));
        } else if (!Pattern.compile(PASSWORD_PATTERN).matcher(new_password).matches()) {
            new_password_til.requestFocus();
            new_password_til.setError(getResources().getString(R.string.password_policy));
        } else if (confirm_password.length() < 1) {
            confirm_password_til.requestFocus();
            confirm_password_til.setError("Please enter confirm password");
        } else if (!new_password.equals(confirm_password)) {
            confirm_password_til.requestFocus();
            confirm_password_til.setError("Password did not match");
        } */

        else {
            email_edt.setError(null);
           // new_password_til.setError(null);
           // confirm_password_til.setError(null);
            validationSuccess = true;
        }
        return validationSuccess;

    }

    private boolean ValidateWithpasswordField() {
        validationSuccess = false;

       // email = email_edt.getText().toString();
          new_password = new_password_edt.getText().toString();
          String confirm_password = confirm_password_edt.getText().toString();


       // email_til.setError(null);
          new_password_edt.setError(null);
          confirm_password_edt.setError(null);

      /*  if (email.length() < 1) {
            email_til.setError("Please enter email id");
            email_til.requestFocus();
        } else if (!Constant.verify(email, ForgotPasswordActivity.this)) {
            email_til.requestFocus();
            email_til.setError("Please enter valid email id");
        }*/

         if (new_password.length() < 6) {
            new_password_edt.requestFocus();
            new_password_edt.setError(getResources().getString(R.string.password));
//        } else if (!Pattern.compile(PASSWORD_PATTERN).matcher(new_password).matches()) {
//            new_password_til.requestFocus();
//            new_password_til.setError(getResources().getString(R.string.password_policy));
//        } else if (confirm_password.length() < 1) {
            confirm_password_edt.requestFocus();
            confirm_password_edt.setError("Please enter confirm password");
        } else if (!new_password.equals(confirm_password)) {
            confirm_password_edt.requestFocus();
            confirm_password_edt.setError("Password did not match");
        }

        else {
           // email_til.setError(null);
             new_password_edt.setError(null);
             confirm_password_edt.setError(null);
             validationSuccess = true;
        }
        return validationSuccess;

    }

//    private void showEmailDialog() {
//        final Dialog dialog;
//        dialog = new Dialog(ForgotPasswordActivity.this);
//        dialog.setCancelable(false);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().setFormat(PixelFormat.TRANSLUCENT);
//
//        Drawable d = new ColorDrawable(Color.BLACK);
//        d.setAlpha(0);
//        dialog.getWindow().setBackgroundDrawable(d);
//
//        dialog.setContentView(R.layout.dialog_email_verification);
//
//        final EditText email_id_edt;
//        TextView retry_tv, text1, text2;
//
//        retry_tv = (TextView) dialog.findViewById(R.id.retry_tv);
//        text1 = (TextView) dialog.findViewById(R.id.text1);
//        text2 = (TextView) dialog.findViewById(R.id.text2);
//        email_id_edt = (EditText) dialog.findViewById(R.id.email_id_edt);
//
//        text1.setTypeface(font_medium);
//        text2.setTypeface(font_regular);
//        email_id_edt.setTypeface(font_regular);
//        retry_tv.setTypeface(font_regular);
//
//
//        retry_tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                String email = email_id_edt.getText().toString();
//                if (email.length() < 1 || !Constant.verify(email, ForgotPasswordActivity.this)) {
//                    email_id_edt.setError("Please enter valid email address.");
//                } else {
//                    showOTPDialog();
//                }
//
//            }
//        });
//        dialog.show();
//    }

    private void showOTPDialog(final String otpFromServer) {


        otpDialog = new Dialog(ForgotPasswordActivity.this);
        otpDialog.setCancelable(true);
        otpDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        otpDialog.getWindow().setFormat(PixelFormat.TRANSLUCENT);

        Drawable d = new ColorDrawable(Color.BLACK);
        d.setAlpha(0);
        otpDialog.getWindow().setBackgroundDrawable(d);

        otpDialog.setContentView(R.layout.dialog_otp);


        TextView resend_otp_tv, text2, email_tv;
        Button submit_btn;

        resend_otp_tv = (TextView) otpDialog.findViewById(R.id.resend_otp_tv);
        otp_edt = (EditText) otpDialog.findViewById(R.id.otp_edt);
        text2 = (TextView) otpDialog.findViewById(R.id.text2);
        email_tv = (TextView) otpDialog.findViewById(R.id.email_tv);
        submit_btn = (Button) otpDialog.findViewById(R.id.submit_btn);

        resend_otp_tv.setTypeface(font_regular);
        otp_edt.setTypeface(font_regular);
        // otp_edt.setText(otp);
        text2.setTypeface(font_regular);
        email_tv.setTypeface(font_regular);
        submit_btn.setTypeface(font_regular);
        email_tv.setText(email);

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String otp = otp_edt.getText().toString();
                if (otp.length() >0 && otp.equals(otpFromServer)) {
                    requestType = "1";
                    otpDialog.dismiss();
                    makePasswordEdittextVisible();
                    /*ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest(ForgotPasswordActivity.this, requestType, email, otp, new_password, ForgotPasswordActivity.this);
                    forgotPasswordRequest.hitUserRequest();*/
                } else {
                    otp_edt.setError("Please enter a valid OTP");

                }
            }
        });


        resend_otp_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendOtp = true;
                requestType = "0";
                otp_edt.setText("");
                otpDialog.dismiss();
                ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest(ForgotPasswordActivity.this, requestType, email, "", new_password, ForgotPasswordActivity.this);
                forgotPasswordRequest.hitUserRequest();
            }
        });
        otpDialog.show();
    }

    private void GoToHomePage() {

        Intent i = new Intent(ForgotPasswordActivity.this, SchoolActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        // onBackPressed();
        finish();
        return true;
    }

    public void onBackPressed() {
        //  super.onBackPressed();
        finish();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try
        {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                View view = getCurrentFocus();
                if (view!=null)
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        }
        catch(Exception e)
        {
            Log.e("FORGOTPASSACTIVITY",e.getMessage());
        }

        return true;
    }

    @Override
    public void onResponse(Object obj) {
        if (obj instanceof ForgotPasswordModel) {
            ForgotPasswordModel forgotPasswordModel = ((ForgotPasswordModel) obj);
            if (requestType.equalsIgnoreCase("0") && forgotPasswordModel.getMessage().equalsIgnoreCase("success")) {

                if(forgotPasswordModel.getIsSuccess().equalsIgnoreCase("true")){
                   // Toast.makeText(getApplicationContext(),"Enter your new password.",Toast.LENGTH_SHORT).show();
                   // makePasswordEdittextVisible();
                    showOTPDialog(forgotPasswordModel.getOtp());
                } else {
                    Toast.makeText(getApplicationContext(),"Email id does not exist. Please enter valid email id.",Toast.LENGTH_SHORT).show();
                }
                // otp=forgotPasswordModel.getResult();

//                else{
//                    otp_edt.setText(otp);}
            } else if (requestType.equalsIgnoreCase("1") && forgotPasswordModel.getMessage().equalsIgnoreCase("success")) {
                otpDialog.dismiss();
                finish();
                Toast.makeText(this, R.string.forgot_password_success, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, forgotPasswordModel.getMessage(), Toast.LENGTH_SHORT).show();
            }

        } else {

        }

    }

    private void makePasswordEdittextVisible() {
        email_edt.setEnabled(false);
        email_edt.setFocusableInTouchMode(false);
        email_edt.setClickable(false);
        email_edt.setFocusable(false);
        new_password_edt.setVisibility(View.VISIBLE);
        confirm_password_edt.setVisibility(View.VISIBLE);
        new_password_lt.setVisibility(View.VISIBLE);
        confirm_password_lt.setVisibility(View.VISIBLE);
        password_policy_layout.setVisibility(View.VISIBLE);

        CheckForPassword = true;

    }
}
