package kheloindia.com.assessment.webservice;

import android.app.Activity;
import android.util.Log;

import java.util.HashMap;

import kheloindia.com.assessment.model.SkillTransactionModel;
import kheloindia.com.assessment.util.ProgressDialogUtility;
import kheloindia.com.assessment.util.ResponseListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by PC10 on 21-Sep-17.
 */

public class TransactionSkillRequest implements Callback<SkillTransactionModel> {

    private ProgressDialogUtility progressDialogUtility;
    private HashMap<String, String> map;
    private ResponseListener responseListener;
    private Activity activity;
    String TAG = "TransactionSkillRequest";


    public TransactionSkillRequest(Activity activity,HashMap<String, String> map,ResponseListener responseListener){
        this.map=map;
        this.responseListener=responseListener;
        this.activity=activity;
    }

    public void hitUserRequest() {
        ApiRequest apiService =
                ApiClient.getClient(activity).create(ApiRequest.class);
        Call<SkillTransactionModel> call = apiService.submitSkillTestDetail(getRequestBody());

        progressDialogUtility=new ProgressDialogUtility(activity);
        progressDialogUtility.showProgressDialog();
        call.enqueue(this);
    }


    private HashMap<String, Object> getRequestBody(){
        HashMap hashMap=new HashMap<String, Object>();
        hashMap = map;
        Log.e(TAG,"hashmap=> "+hashMap);
        return hashMap;
    }

    @Override
    public void onResponse(Call<SkillTransactionModel> call, Response<SkillTransactionModel> response) {
        progressDialogUtility.dismissProgressDialog();
        try {
            responseListener.onResponse(response.body());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(Call<SkillTransactionModel> call, Throwable t) {
        progressDialogUtility.dismissProgressDialog();
        responseListener.onResponse(null);
    }
}


