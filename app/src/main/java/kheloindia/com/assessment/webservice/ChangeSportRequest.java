package kheloindia.com.assessment.webservice;

import android.app.Activity;
import android.util.Log;

import java.util.HashMap;

import kheloindia.com.assessment.model.TopSportsUpdateSecondModel;
import kheloindia.com.assessment.util.ProgressDialogUtility;
import kheloindia.com.assessment.util.ResponseListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by CT13 on 2018-05-09.
 */

public class ChangeSportRequest implements Callback<TopSportsUpdateSecondModel> {

    private ProgressDialogUtility progressDialogUtility;
    private ResponseListener responseListener;
    private Activity activity;
    private String data;


    public ChangeSportRequest(Activity activity, String data, ResponseListener responseListener){
        this.data=data;
        this.responseListener=responseListener;
        this.activity=activity;
    }

    public void hitUserRequest() {
        ApiRequest apiService =
                ApiClient.getClient(activity).create(ApiRequest.class);
        Call<TopSportsUpdateSecondModel> call = apiService.changeSport(getRequestBody());
        progressDialogUtility=new ProgressDialogUtility(activity);
        progressDialogUtility.showProgressDialog();
        call.enqueue(this);
    }


    private HashMap<String, Object> getRequestBody(){
        HashMap hashMap=new HashMap<String, Object>();
        hashMap.put("data",data);
        Log.e("ChangeSportRequest","hashmap==> "+hashMap);
        return hashMap;
    }

    @Override
    public void onResponse(Call<TopSportsUpdateSecondModel> call, Response<TopSportsUpdateSecondModel> response) {
        progressDialogUtility.dismissProgressDialog();
        try {
            responseListener.onResponse(response.body());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(Call<TopSportsUpdateSecondModel> call, Throwable t) {
        progressDialogUtility.dismissProgressDialog();
        responseListener.onResponse(null);
    }
}
