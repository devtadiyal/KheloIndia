package kheloindia.com.assessment.webservice;

import android.app.Activity;
import android.util.Log;

import java.util.HashMap;

import kheloindia.com.assessment.model.ProfileImageModel;
import kheloindia.com.assessment.util.ProgressDialogUtility;
import kheloindia.com.assessment.util.ResponseListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by PC10 on 06-Dec-17.
 */

public class ProfileRequest implements Callback<ProfileImageModel> {

    private ProgressDialogUtility progressDialogUtility;
    private String user_id;
    private String user_type;
    private String image;
    private ResponseListener responseListener;
    private Activity activity;
    private String modified_date;


    public ProfileRequest(Activity activity, String user_id, String user_type,String image, ResponseListener responseListener) {
        this.user_id = user_id;
        this.user_type = user_type;
        this.image = image;
        this.responseListener = responseListener;
        this.activity = activity;
    }


    public void hitUserRequest() {
        ApiRequest apiService =
                ApiClient.getClient(activity).create(ApiRequest.class);
        Call<ProfileImageModel> call = apiService.uploadProfilepic(getRequestBody());

          progressDialogUtility=new ProgressDialogUtility(activity);
           progressDialogUtility.showProgressDialog();
        call.enqueue(this);



    }

    private HashMap<String, Object> getRequestBody() {
        HashMap hashMap = new HashMap<String, Object>();
        hashMap.put("user_login_id",  this.user_id );
        hashMap.put("user_type",    this.user_type);
        hashMap.put("Image_base64", this.image);


        return hashMap;
    }


    @Override
    public void onResponse(Call<ProfileImageModel> call, Response<ProfileImageModel> response) {
          progressDialogUtility.dismissProgressDialog();
        try {
            Log.e("ProfileRequest response",response.body().toString());
            responseListener.onResponse(response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(Call<ProfileImageModel> call, Throwable t) {
          progressDialogUtility.dismissProgressDialog();
        responseListener.onResponse(null);

        Log.e("ProfileRequest Failure",t.toString());

    }

}
