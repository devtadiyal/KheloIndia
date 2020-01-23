package kheloindia.com.assessment.webservice;

import android.app.Activity;

import java.util.HashMap;

import kheloindia.com.assessment.model.DartBoardModel;
import kheloindia.com.assessment.util.ProgressDialogUtility;
import kheloindia.com.assessment.util.ResponseListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by CT13 on 2017-08-17.
 */

public class DartBoardRequest implements Callback<DartBoardModel> {

    private ProgressDialogUtility progressDialogUtility;
    private String School_id,Day,Month,Year,User_ID;
    private ResponseListener responseListener;
    private Activity activity;

    public DartBoardRequest(Activity activity, String School_id,String Day,String Month,String Year,String User_ID, ResponseListener responseListener) {
        this.School_id=School_id;
        this.Day=Day;
        this.Month=Month;
        this.Year=Year;
        this.User_ID=User_ID;
        this.responseListener=responseListener;
        this.activity=activity;
    }

    public void hitUserRequest() {
        ApiRequest apiService =
                ApiClient.getClient(activity).create(ApiRequest.class);
        Call<DartBoardModel> call = apiService.getDartBoard(getRequestBody());

        progressDialogUtility=new ProgressDialogUtility(activity);
        progressDialogUtility.showProgressDialog();
        call.enqueue(this);
    }

    private HashMap<String, Object> getRequestBody() {
        HashMap hashMap=new HashMap<String, Object>();
        hashMap.put("schoolid",School_id);
        hashMap.put("Day",Day);
        hashMap.put("Month",Month);
        hashMap.put("Year",Year);
        hashMap.put("TrainerId",User_ID);
        return hashMap;
    }

    @Override
    public void onResponse(Call<DartBoardModel> call, Response<DartBoardModel> response) {
        progressDialogUtility.dismissProgressDialog();
        try {
            responseListener.onResponse(response.body());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(Call<DartBoardModel> call, Throwable t) {
        progressDialogUtility.dismissProgressDialog();
        responseListener.onResponse(null);
    }
}
