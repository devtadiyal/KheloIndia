package kheloindia.com.assessment.webservice;

import android.app.Activity;
import android.util.Log;

import java.util.HashMap;

import kheloindia.com.assessment.model.ViewAttendanceModel;
import kheloindia.com.assessment.util.ProgressDialogUtility;
import kheloindia.com.assessment.util.ResponseListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by PC10 on 17-Aug-17.
 */

public class ViewAttendanceRequest implements Callback<ViewAttendanceModel> {

    private ProgressDialogUtility progressDialogUtility;
    private String trainer_id;
    private String month;
    private String year;
    private ResponseListener responseListener;
    private Activity activity;


    public ViewAttendanceRequest(Activity activity, String trainerID, String month, String year, ResponseListener responseListener) {
        this.trainer_id=trainerID;
        this.month=month;
        this.year=year;
        this.responseListener=responseListener;
        this.activity=activity;
    }


    public void hitUserRequest() {
        ApiRequest apiService =
                ApiClient.getClient(activity).create(ApiRequest.class);
        Call<ViewAttendanceModel> call = apiService.getAttendanceDetails(getRequestBody());

        //  progressDialogUtility=new ProgressDialogUtility(activity);
        //   progressDialogUtility.showProgressDialog();
        call.enqueue(this);
    }


    private HashMap<String, Object> getRequestBody(){
        HashMap hashMap=new HashMap<String, Object>();
        hashMap.put("TrainerID",trainer_id);
        hashMap.put("Month",month);
        hashMap.put("Year",year);

        Log.e(TAG,"hashmap==> "+hashMap);

        return hashMap;
    }



    @Override
    public void onResponse(Call<ViewAttendanceModel> call, Response<ViewAttendanceModel> response) {
        //  progressDialogUtility.dismissProgressDialog();
        try {
            responseListener.onResponse(response.body());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(Call<ViewAttendanceModel> call, Throwable t) {
        //  progressDialogUtility.dismissProgressDialog();
        responseListener.onResponse(null);
    }
}
