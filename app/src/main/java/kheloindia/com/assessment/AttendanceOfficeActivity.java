package kheloindia.com.assessment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import kheloindia.com.assessment.adapter.ScoolAdapter;
import kheloindia.com.assessment.model.SchoolsMasterModel;
import kheloindia.com.assessment.util.DBManager;
import kheloindia.com.assessment.util.ItemClickListener;
import kheloindia.com.assessment.util.Utility;
import kheloindia.com.assessment.functions.Constant;


public class AttendanceOfficeActivity extends AppCompatActivity implements ItemClickListener {

    ListView school_lst;
    Toolbar toolbar;
    SharedPreferences sp;
    private ArrayList<HashMap<String, String>> schoolList;
    private ScoolAdapter schoolAdapter;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.school_screen);

        init();
    }

    private void init() {

        school_lst = (ListView) findViewById(R.id.school_lst);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        Utility.showActionBar(this, toolbar, "Select Office");

        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        schoolList = new ArrayList<HashMap<String, String>>();

        Constant.TEST_COORDINATOR_ID = sp.getString("test_coordinator_id","");
        Log.e("AttendanceOfficeActivity","coordinator id=> "+Constant.TEST_COORDINATOR_ID);


        ArrayList<Object> objectArrayList = DBManager.getInstance().getAllTableData(this, DBManager.TBL_LP_SCHOOLS_MASTER, "trainer_coordinator_id", Constant.TEST_COORDINATOR_ID,"","");
        Log.e("SchoolActivity", "school size==> " + objectArrayList.size());
        schoolList.clear();

        for (Object o : objectArrayList) {

            HashMap<String, String> map = new HashMap<String, String>();
            SchoolsMasterModel schoolModel = (SchoolsMasterModel) o;

            String office = schoolModel.getOffice();

            if(office.equals("1")){
                map.put("school_name", schoolModel.getSchool_name());
                map.put("school_id", "" + schoolModel.getSchool_id());
                map.put("latitude", "" + schoolModel.getLatitude());
                map.put("longitude", "" + schoolModel.getLongitude());
                map.put("school_start_time", "" + schoolModel.getSchool_start_time());

                schoolList.add(map);
            }

        }

        schoolAdapter = new ScoolAdapter(this, schoolList);
        school_lst.setAdapter(schoolAdapter);
        schoolAdapter.setClickListener(this);
    }

    @Override
    public void onItemClick(View view, int position) {

        String school_name = schoolList.get(position).get("school_name");
        String school_id = schoolList.get(position).get("school_id");
        String latitude = schoolList.get(position).get("latitude");
        String longitude = schoolList.get(position).get("longitude");

        if(latitude.length()>0 && longitude.length()>0){
            Intent i = new Intent(AttendanceOfficeActivity.this, AttendanceGeofencingActivity.class);
            i.putExtra("school_name", school_name);
            i.putExtra("school_id", school_id);
            i.putExtra("latitude", latitude);
            i.putExtra("longitude", longitude);

            Constant.TRACKING_SCHOOL_NAME = school_name;
            Constant.TRACKING_SCHOOL_ID  = school_id;
            Constant.TRACKING_LATITUDE = latitude;
            Constant.TRACKING_LONGITUDE = longitude;

            SharedPreferences.Editor e = sp.edit();
            e.putString("tracking_latitude",Constant.TRACKING_LATITUDE);
            e.putString("tracking_longitude",Constant.TRACKING_LONGITUDE);
            e.commit();

            Log.e("SchoolActivity","Lat==> "+latitude+" Lng==> "+longitude);

        /*if (school_id.equals(sp.getString("tracking_school_id", "0"))) {
            String current_date = "";
            current_date = new SimpleDateFormat("dd").format(new Date());

            SharedPreferences.Editor e = sp.edit();
            e.putString("day_start", current_date);
            e.putString("day_end", current_date);
            e.commit();
        }*/

            startActivity(i);
        } else {
            Toast.makeText(getApplicationContext(),"No Location Found For This Office.", Toast.LENGTH_LONG).show();
        }


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
}
