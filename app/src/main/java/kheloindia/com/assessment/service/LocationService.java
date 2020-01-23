package kheloindia.com.assessment.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.IBinder;
import android.preference.PreferenceManager;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import kheloindia.com.assessment.functions.Constant;
import kheloindia.com.assessment.functions.GPSTracker1;
import kheloindia.com.assessment.model.LocationTrackingModel;
import kheloindia.com.assessment.model.UpdateLocationModel;
import kheloindia.com.assessment.util.ConnectionDetector;
import kheloindia.com.assessment.util.DBManager;
import kheloindia.com.assessment.util.DateUtils;
import kheloindia.com.assessment.util.ResponseListener;
import kheloindia.com.assessment.util.Utility;
import kheloindia.com.assessment.webservice.UpdateLocationRequest;

/**
 * Created by PC10 on 17-Apr-18.
 */

public class LocationService extends Service implements ResponseListener {

    private ConnectionDetector connectionDetector;
    private Context context;
    GPSTracker1 gps;
    String TAG = "LocationService";
    SharedPreferences sp;
    DBManager db;


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = getApplicationContext();
        db = DBManager.getInstance();
        gps = new GPSTracker1(context);
        connectionDetector = new ConnectionDetector(context);
       // Toast.makeText(getApplicationContext(),"Service Started", Toast.LENGTH_SHORT).show();
        sendLocationToServerAtFixedInterval();
        return START_STICKY;
    }

    private void sendLocationToServerAtFixedInterval() {
        checkTimeisBetween();
    }

    private void checkTimeisBetween() {
        try {

          /*  Date time1 = new SimpleDateFormat("HH:mm:ss").parse(string1);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(time1);


            Date time2 = new SimpleDateFormat("HH:mm:ss").parse(string2);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(time2);
            calendar2.add(Calendar.DATE, 1);

            String current_time = new SimpleDateFormat("HH:mm:ss").format(new Date());
            Date d = new SimpleDateFormat("HH:mm:ss").parse(current_time);
            Calendar calendar3 = Calendar.getInstance();
            calendar3.setTime(d);
            calendar3.add(Calendar.DATE, 1);

            Date x = calendar3.getTime();

            Log.e(TAG,"current_time .... "+current_time);
            Log.e(TAG,"x.... "+x);

            Log.e(TAG,"time1 .... "+time1);
            Log.e(TAG,"calendar1 .... "+calendar1.getTime());

            Log.e(TAG,"time2 .... "+time2);
            Log.e(TAG,"calendar2 .... "+calendar2.getTime());


            if (x.after(calendar1.getTime()) && x.before(calendar2.getTime())) {
                // Toast.makeText(context,"time check.... true", Toast.LENGTH_LONG).show();
                //checkes whether the current time is between 06:00:00 and 16:00:00.
                Log.e(TAG,"time check.... true");
                System.out.println(true);
                CallLocationApi();
            } else {
                Log.e(TAG,"time check.... false");
                //Toast.makeText(context,"time check.... false", Toast.LENGTH_LONG).show();
            }*/

            String now = DateUtils.getCurrentHour();
            String start = "06:00";
            String end   = "15:00";

            boolean isTrue = DateUtils.isHourInInterval(now,start,end);

            Log.e(TAG,now + " between " + start + "-" + end + "?");
            Log.e(TAG,""+DateUtils.isHourInInterval(now,start,end));

            if(isTrue){
                CallLocationApi();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void CallLocationApi() {

        if (connectionDetector.isConnectingToInternet()) {
                    /*"Trainer_Id":"15421",
                            "School_Id":"51",
                            "Activity_Id":"1",
                            "Created_On":"07 Feb 2017 1:16:10:196",
                            "Latitude":"23.00",
                            "Longitude":"52.00"*/

            String Day_Status = "0";

            String activity_id = "1";


            Log.e(TAG,"***CallLocationApi***");

            gps = new GPSTracker1(context);

            CallAPi();

            /*final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 30 minutes = 1800 seconds

                    CallAPi();

                }
            }, 20*1000);*/


        }

        else {
            context = getApplicationContext();
            db = DBManager.getInstance();
            gps = new GPSTracker1(context);
            sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            final String currentDate = Utility.DisplayDateInParticularFormat(Constant.inputFormat);
            SaveinDatabase(gps.getLatitude(),gps.getLongitude(),"",currentDate,sp.getString("test_coordinator_id",""));
        }
    }

    private void SaveinDatabase(double latitude, double longitude, String s, String currentDate, String test_coordinator_id) {

        LocationTrackingModel model  = new LocationTrackingModel();
        model.setLat(""+latitude);
        model.setLng(""+longitude);
        model.setCurrent_address(s);
        model.setCreated_on(currentDate);
        model.setTrainer_id(test_coordinator_id);

        Log.e(TAG,"***db== "+db);

        db.insertTables1(db.TBL_LP_MORNING_TRACKING, model, context);

        Log.e(TAG,"***location saved successfully in db***");

    }

    private void CallAPi() {
        Log.e(TAG,"***CallAPi***");

        final String address = "";

        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

      /* Location loc =  gps.getLocation();
        double lat = loc.getLatitude();
        double lng = loc.getLongitude();*/

        double lat1 = gps.getLatitude();
        double lng1 = gps.getLongitude();


        Log.e(TAG,"***lat1***   "+lat1);
        Log.e(TAG,"***lng1***   "+lng1);




        final String currentDate = Utility.DisplayDateInParticularFormat(Constant.inputFormat);
        JSONArray jArray = new JSONArray();
        JSONObject jObj;
        jObj = new JSONObject();

        try {
            jObj.put("Trainer_Id",sp.getString("test_coordinator_id",""));
            jObj.put("Created_On", "" +currentDate);
            jObj.put("Latitude", "" + gps.getLatitude());
            jObj.put("Longitude", "" +gps.getLongitude());

            jArray.put(jObj);

        } catch (Exception e1) {
            e1.printStackTrace();
        }

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("data", jArray.toString());

        UpdateLocationRequest request = new UpdateLocationRequest(context,map, this);
        request.hitAttendanceRequest();
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        Toast.makeText(this, "Service Stopped", Toast.LENGTH_LONG).show();
        Intent intent = new Intent("com.android.techtrainner");
        intent.putExtra("yourvalue", "torestore");
        intent.putExtra("lat", gps.getLatitude());
        intent.putExtra("long", gps.getLongitude());
        sendBroadcast(intent);

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onResponse(Object obj) {

        if(obj instanceof UpdateLocationModel) {
            UpdateLocationModel model = (UpdateLocationModel) obj;
            if (model.getIsSuccess().equalsIgnoreCase("true")) {
                String message = model.getMessage();
                Log.e(TAG,message+"....neha");
                // Toast.makeText(context,message+"....neha", Toast.LENGTH_LONG).show();
            } else {
                Log.e(TAG,"....erro neha");
                // Toast.makeText(context,"....erro neha", Toast.LENGTH_LONG).show();
            }

        }
    }
}

