package kheloindia.com.assessment.webservice;

import android.app.Activity;

import java.util.HashMap;

import kheloindia.com.assessment.model.OptionModel;
import kheloindia.com.assessment.util.ProgressDialogUtility;
import kheloindia.com.assessment.util.ResponseListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by PC10 on 14-Sep-17.
 */

public class OptionsRequest implements Callback<OptionModel> {

    private ProgressDialogUtility progressDialogUtility;
    private String sub_test_id,test_id;
    private ResponseListener responseListener;
    private Activity activity;


    public OptionsRequest(Activity activity, String sub_test_id, String test_id, ResponseListener responseListener){
        this.sub_test_id=sub_test_id;
        this.test_id=test_id;
        this.responseListener=responseListener;
        this.activity=activity;
    }

    public void hitUserRequest() {
        ApiRequest apiService =
                ApiClient.getClient(activity).create(ApiRequest.class);
        Call<OptionModel> call = apiService.getOptionDetails(getRequestBody());

        progressDialogUtility=new ProgressDialogUtility(activity);
        progressDialogUtility.showProgressDialog();
        call.enqueue(this);
    }


    private HashMap<String, Object> getRequestBody(){
        HashMap hashMap=new HashMap<String, Object>();
        hashMap.put("Test_Type_ID",sub_test_id);

        return hashMap;
    }


    @Override
    public void onResponse(Call<OptionModel> call, Response<OptionModel> response) {
        progressDialogUtility.dismissProgressDialog();
        try {
            responseListener.onResponse(response.body());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(Call<OptionModel> call, Throwable t) {
        progressDialogUtility.dismissProgressDialog();
        responseListener.onResponse(null);
    }
}
