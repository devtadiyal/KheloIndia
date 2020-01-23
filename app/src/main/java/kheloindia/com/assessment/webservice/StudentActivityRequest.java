package kheloindia.com.assessment.webservice;

import android.app.Activity;

import java.util.HashMap;

import kheloindia.com.assessment.model.ActivityStudentModel;
import kheloindia.com.assessment.util.ProgressDialogUtility;
import kheloindia.com.assessment.util.ResponseListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by CT13 on 2018-01-03.
 */

public class StudentActivityRequest implements Callback<ActivityStudentModel> {

    private ProgressDialogUtility progressDialogUtility;
    private String school_id,current_class;
    private ResponseListener responseListener;
    private Activity activity;
    private String skill_id;


    public StudentActivityRequest(Activity activity, String school_id, String current_class,String skill_id, ResponseListener responseListener) {
        this.school_id=school_id;
        this.current_class=current_class;
        this.skill_id=skill_id;
        this.responseListener=responseListener;
        this.activity=activity;
    }

    public void hitUserRequest() {
        ApiRequest apiService =
                ApiClient.getClient(activity).create(ApiRequest.class);
        Call<ActivityStudentModel> call = apiService.getActivityStudentDetails(getRequestBody());

//        progressDialogUtility=new ProgressDialogUtility(activity);
//        progressDialogUtility.showProgressDialog();
        call.enqueue(this);
    }

    private HashMap<String, Object> getRequestBody(){
        HashMap hashMap=new HashMap<String, Object>();
        hashMap.put("current_school_id",school_id);
        hashMap.put("Current_Class",current_class);
        hashMap.put("skillid",skill_id);
        return hashMap;
    }


    @Override
    public void onResponse(Call<ActivityStudentModel> call, Response<ActivityStudentModel> response) {
      //  progressDialogUtility.dismissProgressDialog();
        try {
            responseListener.onResponse(response.body());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(Call<ActivityStudentModel> call, Throwable t) {
       // progressDialogUtility.dismissProgressDialog();
        responseListener.onResponse(null);
    }
}
