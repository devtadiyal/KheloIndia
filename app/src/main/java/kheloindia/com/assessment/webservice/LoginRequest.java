package kheloindia.com.assessment.webservice;

import android.app.Activity;
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

public class LoginRequest implements Callback<UserModel> {

    private ProgressDialogUtility progressDialogUtility;
    private String userid, password, modified_date, imei, lat, lng;
    private ResponseListener responseListener;
    private Activity activity;

    public LoginRequest(Activity activity, String userid, String password, String modified_date,String imei, String lat, String lng, ResponseListener responseListener) {
        this.userid = userid;
        this.password = password;
        this.modified_date = modified_date;
        this.imei = imei;
        this.lat = lat;
        this.lng = lng;
        this.responseListener = responseListener;
        this.activity = activity;
    }

    public void hitUserRequest() {
        ApiRequest apiService =
                ApiClient.getClient(activity).create(ApiRequest.class);
        Call<UserModel> call = apiService.getUserDetails(getRequestBody());
        progressDialogUtility = new ProgressDialogUtility(activity);
        progressDialogUtility.showProgressDialog();
        call.enqueue(this);
    }


    private HashMap<String, Object> getRequestBody() {
        HashMap hashMap=new HashMap<String, Object>();
        Crypto crypto = new Crypto(AppConfig.security_key);
        hashMap.put("userid", crypto.encryptAsBase64(userid));
        hashMap.put("password", crypto.encryptAsBase64(password));
        hashMap.put("ModifiedDate",modified_date);
        hashMap.put("Latitude",lat);
        hashMap.put("Longitude",lng);
        hashMap.put("ImeiNo","");
        Log.e("LoginRequest","hashmap==> "+hashMap);
        return hashMap;
    }


    @Override
    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
        progressDialogUtility.dismissProgressDialog();
        try {
            responseListener.onResponse(response.body());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(Call<UserModel> call, Throwable t) {
        progressDialogUtility.dismissProgressDialog();
        responseListener.onResponse(null);
    }

}
