package kheloindia.com.assessment.webservice;

import android.content.Context;

import java.util.HashMap;

import kheloindia.com.assessment.model.SkillTransactionModel;
import kheloindia.com.assessment.util.ProgressDialogUtility;
import kheloindia.com.assessment.util.ResponseListener;
import kheloindia.com.assessment.functions.Constant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by CT13 on 2017-09-21.
 */

public class SyncTransactionSkillRequest  implements Callback<SkillTransactionModel> {

    private ProgressDialogUtility progressDialogUtility;
    private HashMap<String, String> map;
    private ResponseListener responseListener;
    private Context activity;


    public SyncTransactionSkillRequest(Context activity, HashMap<String, String> map, ResponseListener responseListener){
        this.map=map;
        this.responseListener=responseListener;
        this.activity=activity;
    }

    public void hitUserRequest() {
        ApiRequest apiService =
                ApiClient.getClient(activity).create(ApiRequest.class);
        Call<SkillTransactionModel> call = apiService.submitSyncSkillTestDetail(getRequestBody());

//        progressDialogUtility=new ProgressDialogUtility(activity);
//        progressDialogUtility.showProgressDialog();
        call.enqueue(this);
    }


    private HashMap<String, Object> getRequestBody(){
        HashMap hashMap=new HashMap<String, Object>();
        hashMap = map;
        return hashMap;
    }

    @Override
    public void onResponse(Call<SkillTransactionModel> call, Response<SkillTransactionModel> response) {
        //progressDialogUtility.dismissProgressDialog();
        try {
            if(response.body()!=null)
                responseListener.onResponse(response.body());
            else {
                Constant.skill_test_running = false;
                responseListener.onResponse(null);
            }
        } catch(Exception e){
            Constant.skill_test_running=false;
            responseListener.onResponse(null);
        }
    }

    @Override
    public void onFailure(Call<SkillTransactionModel> call, Throwable t) {
        //   progressDialogUtility.dismissProgressDialog();
        Constant.skill_test_running=false;
        responseListener.onResponse(null);
    }
}
