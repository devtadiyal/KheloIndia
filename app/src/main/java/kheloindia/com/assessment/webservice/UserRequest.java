package kheloindia.com.assessment.webservice;

import android.app.Activity;

import java.util.HashMap;

import kheloindia.com.assessment.model.UserModel;
import kheloindia.com.assessment.util.ProgressDialogUtility;
import kheloindia.com.assessment.util.ResponseListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by CT13 on 2017-05-26.
 */

public class UserRequest implements Callback<UserModel> {

    private ProgressDialogUtility progressDialogUtility;
    private String userid,password,modified_date,user_type;
    private ResponseListener responseListener;
    private Activity activity;


    public UserRequest(Activity activity,String userid,String password,String modified_date,String user_type,ResponseListener responseListener){
        this.userid=userid;
        this.password=password;
        this.modified_date=modified_date;
        this.user_type=user_type;
        this.responseListener=responseListener;
        this.activity=activity;
    }

    public void hitUserRequest() {
        ApiRequest apiService =
                ApiClient.getClient(activity).create(ApiRequest.class);
        Call<UserModel>    call = apiService.getUserDetails(getRequestBody());

        progressDialogUtility=new ProgressDialogUtility(activity);
        progressDialogUtility.showProgressDialog();
        call.enqueue(this);
    }


    private HashMap<String, Object> getRequestBody(){
        HashMap hashMap=new HashMap<String, Object>();
        hashMap.put("userid",userid);
        hashMap.put("password",password);
        hashMap.put("ModifiedDate",modified_date);
        hashMap.put("usertype",user_type);

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