package kheloindia.com.assessment.webservice;

import android.app.Activity;
import android.util.Log;
import java.util.HashMap;
import kheloindia.com.assessment.model.CampTestModel;
import kheloindia.com.assessment.util.ProgressDialogUtility;
import kheloindia.com.assessment.util.ResponseListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by PC10 on 13-Oct-17.
 */

public class CampTestMappingRequest implements Callback<CampTestModel> {

    private ProgressDialogUtility progressDialogUtility;
    private String campID,testCoordinator;
    private ResponseListener responseListener;
    private Activity activity;
    String TAG = "CampTestMappingRequest";


    public CampTestMappingRequest(Activity activity, String campID,String testCoordinator, ResponseListener responseListener) {
        this.campID=campID;
        this.testCoordinator=testCoordinator;
        this.responseListener=responseListener;
        this.activity=activity;
    }


    public void hitUserRequest() {
        ApiRequest apiService =
                ApiClient.getClient(activity).create(ApiRequest.class);
        Call<CampTestModel> call = apiService.getcamptestList(getRequestBody());

//        progressDialogUtility=new ProgressDialogUtility(activity);
//        progressDialogUtility.showProgressDialog();
        call.enqueue(this);
    }

    private HashMap<String, Object> getRequestBody(){
        HashMap hashMap=new HashMap<String, Object>();
        hashMap.put("CampID",campID);
        hashMap.put("TestCoordinatorID",testCoordinator);

        Log.e(TAG,"hashmap=> "+hashMap);

        return hashMap;
    }

    @Override
    public void onResponse(Call<CampTestModel> call, Response<CampTestModel> response) {
       // progressDialogUtility.dismissProgressDialog();
        try {
            responseListener.onResponse(response.body());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(Call<CampTestModel> call, Throwable t) {
     //   progressDialogUtility.dismissProgressDialog();
        responseListener.onResponse(null);
    }
}

