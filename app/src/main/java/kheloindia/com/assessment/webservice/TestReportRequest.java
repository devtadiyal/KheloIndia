package kheloindia.com.assessment.webservice;

import android.content.Context;

import java.util.HashMap;
import kheloindia.com.assessment.model.ReportModel;
import kheloindia.com.assessment.util.ProgressDialogUtility;
import kheloindia.com.assessment.util.ResponseListener;
import kheloindia.com.assessment.functions.Constant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by CT13 on 2017-07-17.
 */

public class TestReportRequest implements Callback<ReportModel> {

    private ProgressDialogUtility progressDialogUtility;
    private String campID,lastModifiedOn,testCoordinator;
    private ResponseListener responseListener;
    private Context activity;

    int[] arr = new int[20];


    public TestReportRequest(Context activity, String campID,String lastModifiedOn,String testCoordinator, ResponseListener responseListener) {
        this.campID=campID;
        this.lastModifiedOn=lastModifiedOn;
        this.testCoordinator=testCoordinator;
        this.responseListener=responseListener;
        this.activity=activity;
        }


    public void hitUserRequest() {
                ApiRequest apiService =
                ApiClient.getClient(activity).create(ApiRequest.class);
                Call<ReportModel> call = apiService.getReport(getRequestBody());

//             progressDialogUtility=new ProgressDialogUtility(activity);
//             progressDialogUtility.showProgressDialog();
                call.enqueue(this);
    }

    private HashMap<String, Object> getRequestBody() {
        HashMap hashMap=new HashMap<String, Object>();
        hashMap.put("Camp_ID",campID);
        hashMap.put("Last_Modified_On",lastModifiedOn);
        hashMap.put("Test_Coordinator_ID",testCoordinator);

        return hashMap;
    }

    @Override
    public void onResponse(Call<ReportModel> call, Response<ReportModel> response) {
        // progressDialogUtility.dismissProgressDialog();
        try {
            if(response.body()!=null)
                responseListener.onResponse(response.body());
            else{
                Constant.test_running=false;
                responseListener.onResponse(null);
            }
        } catch(Exception e){
            Constant.test_running=false;
            responseListener.onResponse(null);
        }
    }

    @Override
    public void onFailure(Call<ReportModel> call, Throwable t) {
        // progressDialogUtility.dismissProgressDialog();
        Constant.test_running=false;
        responseListener.onResponse(null);
    }
}
