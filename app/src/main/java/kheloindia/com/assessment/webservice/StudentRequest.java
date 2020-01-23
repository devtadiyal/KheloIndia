package kheloindia.com.assessment.webservice;

import android.app.Activity;
import android.util.Log;

import java.util.HashMap;

import kheloindia.com.assessment.model.StudentModel;
import kheloindia.com.assessment.util.ProgressDialogUtility;
import kheloindia.com.assessment.util.ResponseListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by PC10 on 05/30/2017.
 */

public class StudentRequest implements Callback<StudentModel> {

    private ProgressDialogUtility progressDialogUtility;
    private String school_id;
    private ResponseListener responseListener;
    private Activity activity;
    private String modified_date;
    private String forRetest;


    public StudentRequest(Activity activity, String schoolId, String modified_date,ResponseListener responseListener,String forRetest) {
        this.school_id=schoolId;
        this.responseListener=responseListener;
        this.activity=activity;
        this.modified_date=modified_date;
        this.forRetest = forRetest;
    }


    public void hitUserRequest() {
        ApiRequest apiService =
                ApiClient.getClient(activity).create(ApiRequest.class);
        Call<StudentModel> call;
        if (forRetest.equalsIgnoreCase("1"))
            call = apiService.getRetestStudentDetails(getRequestBody());
        else {
            call = apiService.getStudentDetails(getRequestBody());
        }


      //  progressDialogUtility=new ProgressDialogUtility(activity);
     //   progressDialogUtility.showProgressDialog();
        call.enqueue(this);
    }


    private HashMap<String, Object> getRequestBody(){
        HashMap hashMap=new HashMap<String, Object>();
        hashMap.put("schoolid",school_id);
        hashMap.put("ModifiedDate",modified_date);

        Log.e("StudentRequest","hashmap==> "+hashMap);

        return hashMap;
    }


    @Override
    public void onResponse(Call<StudentModel> call, Response<StudentModel> response) {
      //  progressDialogUtility.dismissProgressDialog();
        try {
            responseListener.onResponse(response.body());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(Call<StudentModel> call, Throwable t) {
      //  progressDialogUtility.dismissProgressDialog();
        responseListener.onResponse(null);
    }
}
