package kheloindia.com.assessment.webservice;

import android.app.Activity;
import android.util.Log;

import java.util.HashMap;

import kheloindia.com.assessment.model.ViewActivityModel;
import kheloindia.com.assessment.util.ProgressDialogUtility;
import kheloindia.com.assessment.util.ResponseListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by CT13 on 2017-12-04.
 */

public class ViewActivityRequest implements Callback<ViewActivityModel> {

    private ProgressDialogUtility progressDialogUtility;
    private Activity activity;
    private String user_id;
    private String data_activity;
    private String school_id;
    private ResponseListener responseListener;
    private String class_id;
    private String test_id;
    private String term_id;
    private String current_class;

    public ViewActivityRequest(Activity activity, String user_id, String data_activity, String school_id,String class_id,String test_id,String term_id,String current_class, ResponseListener responseListener) {
        this.user_id=user_id;
        this.data_activity=data_activity;
        this.school_id=school_id;
        this.class_id=class_id;
        this.responseListener=responseListener;
        this.activity=activity;
        this.test_id=test_id;
        this.term_id=term_id;
        this.current_class=current_class;
    }

    public void hitUserRequest() {
           ApiRequest apiService =
                ApiClient.getClient(activity).create(ApiRequest.class);
           Call<ViewActivityModel> call = apiService.getViewActivityDetails(getRequestBody());
           progressDialogUtility=new ProgressDialogUtility(activity);
           progressDialogUtility.showProgressDialog();
           call.enqueue(this);
    }

    private HashMap<String, Object> getRequestBody(){
        HashMap hashMap=new HashMap<String, Object>();
        hashMap.put("User_ID",user_id);
        hashMap.put("Date_Activity",data_activity);
        hashMap.put("School_id",school_id);
        hashMap.put("ClassID",class_id);
        hashMap.put("TestID",test_id);
        hashMap.put("termid",term_id);
        hashMap.put("current_class",current_class);

        Log.e("ViewActivityRequest","hashmap=====> "+hashMap);
        return hashMap;
    }

    @Override
    public void onResponse(Call<ViewActivityModel> call, Response<ViewActivityModel> response) {
       progressDialogUtility.dismissProgressDialog();
        try {
            responseListener.onResponse(response.body());
            Log.e("ViewActivityRequest","response=====> "+response.body());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(Call<ViewActivityModel> call, Throwable t) {
        progressDialogUtility.dismissProgressDialog();
        responseListener.onResponse(null);

    }






}
