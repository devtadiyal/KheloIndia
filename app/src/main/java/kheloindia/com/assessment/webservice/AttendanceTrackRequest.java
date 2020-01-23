package kheloindia.com.assessment.webservice;

import android.app.Activity;
import android.util.Log;

import java.util.HashMap;

import kheloindia.com.assessment.model.AttendancetrackModel;
import kheloindia.com.assessment.util.ProgressDialogUtility;
import kheloindia.com.assessment.util.ResponseListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by PC10 on 27-Mar-18.
 */

public class AttendanceTrackRequest implements Callback<AttendancetrackModel> {

    private ProgressDialogUtility progressDialogUtility;
    private String coordinator_id,school_id, created_on;
    private ResponseListener responseListener;
    private Activity activity;

    public AttendanceTrackRequest(Activity activity,String coordinator_id,String school_id,
                                 String created_on,ResponseListener responseListener){
        this.coordinator_id=coordinator_id;
        this.school_id=school_id;
        this.created_on=created_on;
        this.responseListener=responseListener;
        this.activity=activity;

    }

    public void hitAttendanceRequest() {
        ApiRequest apiService =
                ApiClient.getClient(activity).create(ApiRequest.class);
        Call<AttendancetrackModel> call = apiService.AttendanceTrack(getRequestBody());

        progressDialogUtility=new ProgressDialogUtility(activity);
        progressDialogUtility.showProgressDialog();
        call.enqueue(this);
    }

    /*{
        "Trainer_id":"12563",
            "School_id":"54",
            "Created_On":"2017-02-07 01:16:10.197"

    }*/

    private HashMap<String, Object> getRequestBody(){
        HashMap hashMap=new HashMap<String, Object>();
        hashMap.put("Trainer_id",coordinator_id);
        hashMap.put("School_id",school_id);
        hashMap.put("Created_On",created_on);

        Log.e("markAttendanceRequest","hashmap==> "+hashMap);

        return hashMap;
    }


    @Override
    public void onResponse(Call<AttendancetrackModel> call, Response<AttendancetrackModel> response) {
        progressDialogUtility.dismissProgressDialog();
        try {
            responseListener.onResponse(response.body());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(Call<AttendancetrackModel> call, Throwable t) {
        progressDialogUtility.dismissProgressDialog();
        responseListener.onResponse(null);
    }

}
