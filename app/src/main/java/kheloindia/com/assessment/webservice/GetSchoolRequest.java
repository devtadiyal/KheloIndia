package kheloindia.com.assessment.webservice;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;

import java.util.HashMap;

import kheloindia.com.assessment.model.UserModel;
import kheloindia.com.assessment.util.AppConfig;
import kheloindia.com.assessment.util.Crypto;
import kheloindia.com.assessment.util.ProgressDialogUtility;
import kheloindia.com.assessment.util.ResponseListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by PC10 on 16-Feb-18.
 */

public class GetSchoolRequest implements Callback<UserModel> {

    //private ProgressDialogUtility progressDialogUtility;
    private String coordinatorId, password, modified_date, imei, lat, lng;
    private ResponseListener responseListener;
    private Activity activity;

    public GetSchoolRequest(Activity activity, String coordinatorId, ResponseListener responseListener) {
        this.coordinatorId = coordinatorId;
        this.responseListener = responseListener;
        this.activity = activity;
    }

    public void hitSchoolRequest() {
        ApiRequest apiService =
                ApiClient.getClient(activity).create(ApiRequest.class);
        Call<UserModel> call = apiService.getSchools(getRequestBody());
        call.enqueue(this);
    }


    private HashMap<String, Object> getRequestBody() {
        HashMap hashMap=new HashMap<String, Object>();
        hashMap.put(AppConfig.USER_ID, new Crypto(AppConfig.security_key).encryptAsBase64(coordinatorId));
        Log.e("GetSchoolRequest","hashmap==> "+hashMap);
        return hashMap;
    }


    @Override
    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
        try {
            responseListener.onResponse(response.body());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(Call<UserModel> call, Throwable t) {
        responseListener.onResponse(null);
    }

}
