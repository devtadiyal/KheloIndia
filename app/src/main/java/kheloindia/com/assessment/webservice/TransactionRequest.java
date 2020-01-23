package kheloindia.com.assessment.webservice;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashMap;

import kheloindia.com.assessment.model.TransactionModel;
import kheloindia.com.assessment.util.AppConfig;
import kheloindia.com.assessment.util.ProgressDialogUtility;
import kheloindia.com.assessment.util.ResponseListener;
import kheloindia.com.assessment.functions.Constant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by PC10 on 06/09/2017.
 */

public class TransactionRequest implements Callback<TransactionModel> {

    private ProgressDialogUtility progressDialogUtility;
    private HashMap<String, String> map;
    private ResponseListener responseListener;
    private Context activity;
    SharedPreferences sharedPreferences;


    public TransactionRequest(Context activity, HashMap<String, String> map, ResponseListener responseListener){
        this.map=map;
        this.responseListener=responseListener;
        this.activity=activity;
    }

    public void hitUserRequest() {
        ApiRequest apiService =
                ApiClient.getClient(activity).create(ApiRequest.class);
        Call<TransactionModel> call;
        if (PreferenceManager.getDefaultSharedPreferences(activity).getString(AppConfig.FOR_RETEST,"0").equalsIgnoreCase("0"))
            call = apiService.submitTestDetail(getRequestBody());
        else
            call = apiService.submitRetestDetail(getRequestBody());

   //     progressDialogUtility=new ProgressDialogUtility(activity);
    //    progressDialogUtility.showProgressDialog();
        call.enqueue(this);
    }


    private HashMap<String, Object> getRequestBody(){
        HashMap hashMap=new HashMap<String, Object>();
        hashMap = map;
        return hashMap;
    }

    @Override
    public void onResponse(Call<TransactionModel> call, Response<TransactionModel> response) {
//       progressDialogUtility.dismissProgressDialog();
        try {
            if(response.body()!=null)
                responseListener.onResponse(response.body());
            else{
                Constant.test_running=false;
                responseListener.onResponse(null);
            }
        } catch(Exception e){
            Constant.test_running=false;
            responseListener.onResponse(null);
        }
    }

    @Override
    public void onFailure(Call<TransactionModel> call, Throwable t) {
        //    progressDialogUtility.dismissProgressDialog();
        Constant.test_running=false;
        responseListener.onResponse(null);
    }
}

