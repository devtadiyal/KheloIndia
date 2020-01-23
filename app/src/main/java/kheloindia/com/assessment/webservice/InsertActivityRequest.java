package kheloindia.com.assessment.webservice;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;

import kheloindia.com.assessment.model.InsertActivityModel;
import kheloindia.com.assessment.util.ProgressDialogUtility;
import kheloindia.com.assessment.util.ResponseListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by CT13 on 2018-01-09.
 */

public class InsertActivityRequest implements Callback<InsertActivityModel> {


    private ProgressDialogUtility progressDialogUtility;
    private Context activity;
    private String data;
    private ResponseListener responseListener;
    private String activity_id;
    private String student_activity_id;
    String TAG = "InsertActivityRequest";

    public InsertActivityRequest(Context activity, String data, String activity_id, String student_activity_id, ResponseListener responseListener){

        this.data=data;
        this.activity_id=activity_id;
        this.student_activity_id=student_activity_id;
        this.responseListener=responseListener;
        this.activity=activity;

    }


    public void hitUserRequest() {
        ApiRequest apiService =
            ApiClient.getClient(activity).create(ApiRequest.class);
        Call<InsertActivityModel> call = apiService.getInsertActivityDetails(getRequestBody());

//        progressDialogUtility=new ProgressDialogUtility(activity);
//        progressDialogUtility.showProgressDialog();
        call.enqueue(this);
}


    private HashMap<String, Object> getRequestBody(){
        HashMap hashMap=new HashMap<String, Object>();
        hashMap.put("data",data);
        hashMap.put("ActivityID",activity_id);
        hashMap.put("Student_ActivityId",student_activity_id);

        Log.e(TAG,"hashmap==> "+hashMap);

        return hashMap;
    }


    @Override
    public void onResponse(Call<InsertActivityModel> call, Response<InsertActivityModel> response) {

        //progressDialogUtility.dismissProgressDialog();
        try {
            responseListener.onResponse(response.body());
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onFailure(Call<InsertActivityModel> call, Throwable t) {
        //progressDialogUtility.dismissProgressDialog();
        responseListener.onResponse(null);
    }
}
