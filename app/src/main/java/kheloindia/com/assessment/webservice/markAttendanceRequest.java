package kheloindia.com.assessment.webservice;

import android.app.Activity;
import android.util.Log;

import java.util.HashMap;

import kheloindia.com.assessment.model.MarkAttendanceModel;
import kheloindia.com.assessment.util.ProgressDialogUtility;
import kheloindia.com.assessment.util.ResponseListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by PC10 on 13-Mar-18.
 */

public class markAttendanceRequest implements Callback<MarkAttendanceModel> {

    private ProgressDialogUtility progressDialogUtility;
    private String coordinator_id,school_id,activity_id, created_on, lat, lng, day_status, address, distance;
    private ResponseListener responseListener;
    private Activity activity;

    public markAttendanceRequest(Activity activity,String coordinator_id,String school_id,
                                 String activity_id,String created_on,String lat,String lng,
                                 String day_status, String address, String distance,
                                 ResponseListener responseListener){
        this.coordinator_id=coordinator_id;
        this.school_id=school_id;
        this.activity_id=activity_id;
        this.created_on=created_on;
        this.lat=lat;
        this.lng=lng;
        this.responseListener=responseListener;
        this.activity=activity;
        this.day_status = day_status;
        this.address = address;
        this.distance = distance;

    }

    public void hitAttendanceRequest() {
        ApiRequest apiService =
                ApiClient.getClient(activity).create(ApiRequest.class);
        Call<MarkAttendanceModel> call = apiService.MarkAttendance(getRequestBody());

        progressDialogUtility=new ProgressDialogUtility(activity);
        progressDialogUtility.showProgressDialog();
        call.enqueue(this);
    }

   /* {
        "Trainer_Id":"15421",
            "School_Id":"51",
            "Activity_Id":"1",
            "Created_On":"07 Feb 2017 1:16:10:196",
            "Latitude":"23.00",
            "Longitude":"52.00"
    }*/

    private HashMap<String, Object> getRequestBody(){
        HashMap hashMap=new HashMap<String, Object>();
        hashMap.put("Trainer_Id",coordinator_id);
        hashMap.put("School_Id",school_id);
        hashMap.put("Activity_Id",activity_id);
        hashMap.put("Created_On",created_on);
        hashMap.put("Latitude",lat);
        hashMap.put("Longitude",lng);
        hashMap.put("Day_Status",day_status);
        hashMap.put("distance_From_Destination",distance);
        hashMap.put("Current_address",address);

        Log.e("markAttendanceRequest","hashmap==> "+hashMap);

        return hashMap;
    }


    @Override
    public void onResponse(Call<MarkAttendanceModel> call, Response<MarkAttendanceModel> response) {
        progressDialogUtility.dismissProgressDialog();
        try {
            responseListener.onResponse(response.body());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(Call<MarkAttendanceModel> call, Throwable t) {
        progressDialogUtility.dismissProgressDialog();
        responseListener.onResponse(null);
    }

}
