package kheloindia.com.assessment.webservice;

import android.app.Activity;
import android.util.Log;

import java.util.HashMap;

import kheloindia.com.assessment.model.TestTypeModel;
import kheloindia.com.assessment.util.ProgressDialogUtility;
import kheloindia.com.assessment.util.ResponseListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by PC10 on 06/05/2017.
 */

public class TestTypesRequest implements Callback<TestTypeModel> {

    private ProgressDialogUtility progressDialogUtility;
    private String testid;
    private String modifiedDateCatagory;
    private String modifiedDateType;
    private String modifiedDateSkill;
    private ResponseListener responseListener;
    private Activity activity;
    private String TAG  ="TestTypesRequest";


    public TestTypesRequest(Activity activity, String testId, String modifiedDateCatagory, String modifiedDateType, String modifiedDateSkill, ResponseListener responseListener) {

        this.testid=testId;
        this.modifiedDateCatagory=modifiedDateCatagory;
        this.modifiedDateType=modifiedDateType;
        this.modifiedDateSkill=modifiedDateSkill;
        this.responseListener=responseListener;
        this.activity=activity;
    }

    public void hitUserRequest() {
        ApiRequest apiService =
                ApiClient.getClient(activity).create(ApiRequest.class);
        Call<TestTypeModel>    call = apiService.getTestDetails(getRequestBody());

//        progressDialogUtility=new ProgressDialogUtility(activity);
//        progressDialogUtility.showProgressDialog();
        call.enqueue(this);
    }


    private HashMap<String, Object> getRequestBody(){
        HashMap hashMap=new HashMap<String, Object>();
        hashMap.put("testID",testid);
        hashMap.put("ModifiedDateCatagory",modifiedDateCatagory);
        hashMap.put("ModifiedDateType",modifiedDateType);
        hashMap.put("ModifiedDateSkill",modifiedDateSkill);

        /*"ModifiedDateCatagory":"",

                "ModifiedDateType":"",
                "Modi ApiRequest apiService =
                ApiClient.getClient(activity).create(ApiRequest.class);
        Call<TestTypeModel>    call = apiService.getTestDetails(getRequestBody());

//        progressDialogUtility=new ProgressDialogUtility(activity);
//        progressDialogUtility.showProgressDialog();
        call.enqueue(this);fiedDateSkill":""*/

        Log.e(TAG,"hashmap=> "+hashMap);

        return hashMap;
    }


    @Override
    public void onResponse(Call<TestTypeModel> call, Response<TestTypeModel> response) {
       //progressDialogUtility.dismissProgressDialog();
        try {
            responseListener.onResponse(response.body());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(Call<TestTypeModel> call, Throwable t) {
       //progressDialogUtility.dismissProgressDialog();
        responseListener.onResponse(null);
    }
}
