package kheloindia.com.assessment.webservice;

import android.app.Activity;

import java.util.HashMap;

import kheloindia.com.assessment.model.TopSportsUpdateModel;
import kheloindia.com.assessment.util.ProgressDialogUtility;
import kheloindia.com.assessment.util.ResponseListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by CT13 on 2018-05-08.
 */

public class TopSportsUpdateRequest implements Callback<TopSportsUpdateModel> {

    private ProgressDialogUtility progressDialogUtility;
    private ResponseListener responseListener;
    private Activity activity;
    private String Current_school_id,Current_class;

    public TopSportsUpdateRequest(Activity activity, String Current_school_id,String Current_class, ResponseListener responseListener){
        this.Current_school_id=Current_school_id;
        this.Current_class=Current_class;
        this.responseListener=responseListener;
        this.activity=activity;
    }

    public void hitUserRequest() {
        ApiRequest apiService =
                ApiClient.getClient(activity).create(ApiRequest.class);
        Call<TopSportsUpdateModel> call = apiService.topSportsUpdate(getRequestBody());
//        progressDialogUtility=new ProgressDialogUtility(activity);
//        progressDialogUtility.showProgressDialog();
        call.enqueue(this);
    }


    private HashMap<String, Object> getRequestBody(){
        HashMap hashMap=new HashMap<String, Object>();
        hashMap.put("Current_school_id",Current_school_id);
        hashMap.put("Current_class",Current_class);

        return hashMap;
    }

    @Override
    public void onResponse(Call<TopSportsUpdateModel> call, Response<TopSportsUpdateModel> response) {
   //   progressDialogUtility.dismissProgressDialog();
        try {
            responseListener.onResponse(response.body());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(Call<TopSportsUpdateModel> call, Throwable t) {
      //  progressDialogUtility.dismissProgressDialog();
        responseListener.onResponse(null);
    }
}
