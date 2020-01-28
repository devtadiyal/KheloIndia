package kheloindia.com.assessment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import kheloindia.com.assessment.model.TransactionModel;
import kheloindia.com.assessment.util.ConnectionDetector;
import kheloindia.com.assessment.util.DBManager;
import kheloindia.com.assessment.util.ResponseListener;

import kheloindia.com.assessment.functions.Constant;
import kheloindia.com.assessment.functions.GPSTracker;
import kheloindia.com.assessment.model.FitnessTestResultModel;
import kheloindia.com.assessment.webservice.TransactionRequest;

/**
 * Created by PC10 on 06/07/2017.
 */
// This is not needed
public class HeightActivity extends AppCompatActivity implements View.OnClickListener, ResponseListener {

    ImageView test_another_img, dashboard_img;
    Toolbar toolbar;
    Button reset_btn, save_btn;
    EditText height_et;
    // EditText weight_et;

    TextView school_tv;

    TextView test_name_tv, sub_test_name_tv, text_tv;

    ImageView boy_or_girl_img;
    TextView student_name_tv, student_class_tv;

    GPSTracker gps;
    double lat, lng;

    DBManager db;

    //private SimpleLocation location;

    ConnectionDetector connectionDetector;

    String TAG = "HeightActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_height);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gps.getmGoogleApiClient().connect();
        sub_test_name_tv.setText(Constant.SUB_TEST_TYPE);
        text_tv.setText("Enter " + Constant.SUB_TEST_TYPE + " Test Score");

        height_et.setText("");
        height_et.setHint("00");


    }


    private void init() {

        db = DBManager.getInstance();
        gps = new GPSTracker(getApplicationContext());
        //location = new SimpleLocation(this);
        connectionDetector = new ConnectionDetector(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        dashboard_img = (ImageView) toolbar.findViewById(R.id.dashboard_img);
        test_another_img = (ImageView) findViewById(R.id.test_another_img);
        reset_btn = (Button) findViewById(R.id.reset_btn);
        height_et = (EditText) findViewById(R.id.height_et);
        // weight_et = (EditText) findViewById(R.id.weight_et);
        school_tv = (TextView) findViewById(R.id.school_tv);
        save_btn = (Button) findViewById(R.id.save_btn);
        boy_or_girl_img = (ImageView) findViewById(R.id.boy_or_girl_img);
        student_class_tv = (TextView) findViewById(R.id.student_class_tv);
        student_name_tv = (TextView) findViewById(R.id.student_name_tv);
        test_name_tv = (TextView) toolbar.findViewById(R.id.test_name_tv);
        sub_test_name_tv = (TextView) toolbar.findViewById(R.id.sub_test_name_tv);
        text_tv = (TextView) findViewById(R.id.text_tv);

        toolbar.setTitle(Constant.TEST_TYPE);

        school_tv.setText(Constant.SCHOOL_NAME);

        test_another_img.setOnClickListener(this);
        reset_btn.setOnClickListener(this);
        save_btn.setOnClickListener(this);
        sub_test_name_tv.setOnClickListener(this);

        student_name_tv.setText(Constant.STUDENT_NAME);
//        student_class_tv.setText("Class "+Constant.STUDENT_CLASS+", "+
//                Constant.SCHOOL_ID+Constant.STUDENT_ROLL_NO);
        test_name_tv.setText(Constant.TEST_TYPE);
        sub_test_name_tv.setText(Constant.SUB_TEST_TYPE);

        if (Constant.STUDENT_GENDER.equalsIgnoreCase("1")) {
            boy_or_girl_img.setImageResource(R.drawable.girl_blue_i);
        } else if (Constant.STUDENT_GENDER.equalsIgnoreCase("0")) {
            boy_or_girl_img.setImageResource(R.drawable.boy_i);
        }


        dashboard_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showExitDialog2();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
           /* case R.id.test_another_btn :
                Intent i = new Intent(HeightActivity.this, ScanActivity.class);
                startActivity(i);
                finish();
                break;*/

            case R.id.reset_btn:
                height_et.setText("");
                // weight_et.setText("");
                height_et.setHint("00");
                // weight_et.setHint("00");
                break;

            case R.id.test_another_img:
                Intent i1 = new Intent(HeightActivity.this, ScanActivity.class);
                startActivity(i1);
                finish();
                break;

            case R.id.save_btn:
                String height_txt = height_et.getText().toString();

                lat = gps.getLatitude();
                lng = gps.getLongitude();
                String DateTime = Constant.getDateTimeServer();

                Log.e(TAG, "lat=> " + lat + "   lng=> " + lng);
                Log.e(TAG, "DateTime=> " + DateTime);


                int height_count = Integer.parseInt(height_txt);


                if (height_count <= 0) {
                    Toast.makeText(getApplicationContext(), "Please fill the height field...", Toast.LENGTH_SHORT).show();
                } else {
                    insertTransactionData(height_txt, DateTime);
                }

                break;

            case R.id.sub_test_name_tv:
                showExitDialog1();
                break;
        }


    }

    private void insertTransactionData(String height, String date) {

        FitnessTestResultModel model = new FitnessTestResultModel();
        model.setStudent_id(Integer.parseInt(Constant.STUDENT_ID));
        model.setCamp_id(Integer.parseInt(Constant.CAMP_ID));
        model.setTest_type_id(Integer.parseInt(Constant.SUB_TEST_ID));
        //    model.setTest_coordinator_id(sp.getString("test_coordinator_name", ""));
        Double score_double = Double.parseDouble(height);
        model.setScore("" + score_double.intValue());
        model.setPercentile(0);
        model.setCreated_on(date);
        model.setCreated_by(Constant.TEST_COORDINATOR_ID);
        model.setLast_modified_by("");
        model.setLast_modified_by("");
        model.setLatitude(lat);
        model.setLongitude(lng);
        model.setSubTestName(Constant.TEST_TYPE);
        model.setTestedOrNot(true);

        HashMap<String, String> map = null;
        try {
            map = db.getParticularRow(getApplicationContext(), DBManager.TBL_LP_FITNESS_TEST_RESULT,
                    "student_id", Constant.STUDENT_ID, "test_type_id", Constant.SUB_TEST_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (map.size() == 0) {
            db.insertTables(db.TBL_LP_FITNESS_TEST_RESULT, model);
            if (connectionDetector.isConnectingToInternet()) {
                CallTransactionAPI();
            } else {
                Toast.makeText(getApplicationContext(), "Data saved successfully in local DB", Toast.LENGTH_SHORT).show();
                ClearFields();
            }

        } else {
            Toast.makeText(getApplicationContext(), "This Student has already given the test.", Toast.LENGTH_LONG).show();
        }
    }

    private void CallTransactionAPI() {

        HashMap<String, String> map_result = db.getParticularRow(getApplicationContext(), DBManager.TBL_LP_FITNESS_TEST_RESULT,
                "student_id", Constant.STUDENT_ID, "test_type_id", Constant.SUB_TEST_ID);

            /*ArrayList<Object> objectArrayList = DBManager.getInstance().getAllTableData(this,
                    db.TBL_LP_FITNESS_TEST_RESULT, "", "");

            Log.e(TAG, "transaction table size==> " + objectArrayList.size());*/

        // for (Object o : objectArrayList) {

        HashMap<String, String> map = new HashMap<String, String>();
        // FitnessTestResultModel model = (FitnessTestResultModel) o;

        map.put("Student_ID", map_result.get("student_id"));
        map.put("Camp_ID", map_result.get("camp_id"));
        map.put("Test_Type_ID", map_result.get("test_type_id"));
        map.put("Test_Cordinator", map_result.get("test_coordinator_id"));
        map.put("Score", map_result.get("score"));
        map.put("Percentile", map_result.get("percentile"));
        map.put("Created_by", map_result.get("created_by"));
        map.put("Created_on", map_result.get("created_on"));
        map.put("SyncDateTime", "");
        map.put("Longitude", map_result.get("longitude"));
        map.put("Latitude", map_result.get("latitude"));

        Log.e(TAG, "map==> " + map);


        TransactionRequest transactionRequest = new TransactionRequest(this, map, this);
        transactionRequest.hitUserRequest();
        //    }
    }

    private void ClearFields() {
        height_et.setText("");
        height_et.setHint("00");
    }

    public void onBackPressed() {
        showExitDialog();
        //moveTaskToBack(true);

    }

    private void showExitDialog1() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(HeightActivity.this);

        alertDialog.setMessage(getResources().getString(R.string.exit_from_test));

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.alarm);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                Constant.ISComingFromTestScreen = true;
                dialog.cancel();
                Intent i1 = new Intent(HeightActivity.this, ShowSubTestActivity.class);
                startActivity(i1);
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    private void showExitDialog2() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(HeightActivity.this);

        alertDialog.setMessage(getResources().getString(R.string.exit_from_test));

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.alarm);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
                Intent i1 = new Intent(HeightActivity.this, TestActivity.class);
                startActivity(i1);
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


    private void showExitDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(HeightActivity.this);

        alertDialog.setMessage(getResources().getString(R.string.exit_from_test));

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.alarm);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
                SharedPreferences sp = PreferenceManager
                        .getDefaultSharedPreferences(getApplicationContext());
                //  sp.edit().clear().commit();
                Intent i = new Intent(HeightActivity.this, ScanActivity.class);
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                View view = getCurrentFocus();
                if (view != null)
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {
            Log.e("HeightACTIVITY", e.getMessage());
        }

        return true;
    }

    @Override
    public void onResponse(Object obj) {
        if (obj instanceof TransactionModel) {

            TransactionModel model = (TransactionModel) obj;

            Log.e(TAG, "sucess==> " + model.getIsSuccess());
            Log.e(TAG, "message==> " + model.getMessage());

            if (model.getIsSuccess().equalsIgnoreCase("true")) {
                ClearFields();
                Toast.makeText(this, model.getMessage(), Toast.LENGTH_SHORT).show();
                db.deleteTransactionRow(this, db.TBL_LP_FITNESS_TEST_RESULT, "student_id", Constant.STUDENT_ID,
                        "test_type_id", Constant.SUB_TEST_ID, "camp_id", Constant.CAMP_ID);
            } else {
                Toast.makeText(this, model.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onPause() {
        // stop location updates (saves battery)
        //location.endUpdates();

        super.onPause();
    }
}

