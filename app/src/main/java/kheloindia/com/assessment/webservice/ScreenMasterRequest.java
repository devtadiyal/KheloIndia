package kheloindia.com.assessment.webservice;

import android.app.Activity;
import android.util.Log;

import java.util.HashMap;

import kheloindia.com.assessment.model.ActiveModel;
import kheloindia.com.assessment.util.ProgressDialogUtility;
import kheloindia.com.assessment.util.ResponseListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by PC10 on 15-Mar-18.
 */

public class ScreenMasterRequest implements Callback<ActiveModel> {

    private ProgressDialogUtility progressDialogUtility;
    private String user_type;
    private ResponseListener responseListener;
    private Activity activity;

    public ScreenMasterRequest(Activity activity,String user_type,ResponseListener responseListener){
        this.responseListener=responseListener;
        this.activity=activity;
        this.user_type= user_type;

    }


    public void hitViewUrlRequest() {
        ApiRequest apiService =
                ApiClient.getClient(activity).create(ApiRequest.class);
        Call<ActiveModel> call = apiService.ViewUrl(getRequestBody());

        progressDialogUtility=new ProgressDialogUtility(activity);
        progressDialogUtility.showProgressDialog();
        call.enqueue(this);
    }


    private HashMap<String, Object> getRequestBody(){
        HashMap hashMap=new HashMap<String, Object>();
        hashMap.put("User_Type_ID",user_type);

        Log.e("ScreenMasterRequest","hashmap==> "+hashMap);

        return hashMap;
    }


    @Override
    public void onResponse(Call<ActiveModel> call, Response<ActiveModel> response) {
        progressDialogUtility.dismissProgressDialog();
        try {
            responseListener.onResponse(response.body());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(Call<ActiveModel> call, Throwable t) {
        t.printStackTrace();
        Log.e("ScreenMasterRequest","error==> "+t.toString());
        progressDialogUtility.dismissProgressDialog();
        responseListener.onResponse(null);
    }

}

