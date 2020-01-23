package kheloindia.com.assessment.webservice;

import android.app.Activity;

import java.util.HashMap;

import kheloindia.com.assessment.model.TestCategoryModel;
import kheloindia.com.assessment.util.ProgressDialogUtility;
import kheloindia.com.assessment.util.ResponseListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by PC10 on 06/06/2017.
 */

public class TestCategoryRequest implements Callback<TestCategoryModel> {

    private ProgressDialogUtility progressDialogUtility;
    private String testid;
    private ResponseListener responseListener;
    private Activity activity;


    public TestCategoryRequest(Activity activity,String testid,ResponseListener responseListener){
        this.testid=testid;
        this.responseListener=responseListener;
        this.activity=activity;
    }

    public void hitUserRequest() {
        ApiRequest apiService =
                ApiClient.getClient(activity).create(ApiRequest.class);
        Call<TestCategoryModel> call = apiService.getSubTestDetails(getRequestBody());

        progressDialogUtility=new ProgressDialogUtility(activity);
        progressDialogUtility.showProgressDialog();
        call.enqueue(this);
    }


    private HashMap<String, Object> getRequestBody(){
        HashMap hashMap=new HashMap<String, Object>();
        hashMap.put("testID",testid);

        return hashMap;
    }



    @Override
    public void onResponse(Call<TestCategoryModel> call, Response<TestCategoryModel> response) {
        progressDialogUtility.dismissProgressDialog();
        try {
            responseListener.onResponse(response.body());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(Call<TestCategoryModel> call, Throwable t) {
        progressDialogUtility.dismissProgressDialog();
        responseListener.onResponse(null);
    }
}
