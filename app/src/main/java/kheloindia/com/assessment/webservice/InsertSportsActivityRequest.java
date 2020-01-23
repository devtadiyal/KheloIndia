package kheloindia.com.assessment.webservice;

import android.app.Activity;

import java.util.HashMap;

import kheloindia.com.assessment.model.InsertActivityModel;
import kheloindia.com.assessment.util.ProgressDialogUtility;
import kheloindia.com.assessment.util.ResponseListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by CT13 on 2018-01-12.
 */

public class InsertSportsActivityRequest implements Callback<InsertActivityModel> {


    private ProgressDialogUtility progressDialogUtility;
    private Activity activity;
    private String data;
    private ResponseListener responseListener;

    public InsertSportsActivityRequest(Activity activity, String data, ResponseListener responseListener){

        this.data=data;
        this.responseListener=responseListener;
        this.activity=activity;
    }


    public void hitUserRequest() {
        ApiRequest apiService =
                ApiClient.getClient(activity).create(ApiRequest.class);
        Call<InsertActivityModel> call = apiService.getInsertActivitySportsDetails(getRequestBody());

        progressDialogUtility=new ProgressDialogUtility(activity);
        progressDialogUtility.showProgressDialog();
        call.enqueue(this);
    }


    private HashMap<String, Object> getRequestBody(){
        HashMap hashMap=new HashMap<String, Object>();
        hashMap.put("data",data);

        return hashMap;
    }


    @Override
    public void onResponse(Call<InsertActivityModel> call, Response<InsertActivityModel> response) {

        progressDialogUtility.dismissProgressDialog();
        try {
            responseListener.onResponse(response.body());
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onFailure(Call<InsertActivityModel> call, Throwable t) {
        progressDialogUtility.dismissProgressDialog();
        responseListener.onResponse(null);
    }
}
