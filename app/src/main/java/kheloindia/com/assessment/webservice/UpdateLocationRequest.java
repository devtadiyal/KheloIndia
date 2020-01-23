package kheloindia.com.assessment.webservice;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;

import kheloindia.com.assessment.model.UpdateLocationModel;
import kheloindia.com.assessment.util.ResponseListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by PC10 on 16-Apr-18.
 */

public class UpdateLocationRequest implements Callback<UpdateLocationModel> {

 //   private ProgressDialogUtility progressDialogUtility;
    private HashMap<String, String> map;
    private ResponseListener responseListener;
    private Context activity;
    String TAG = "UpdateLocationRequest";

    public UpdateLocationRequest(Context activity, HashMap<String, String> map, ResponseListener responseListener){
        this.map=map;
        this.responseListener=responseListener;
        this.activity=activity;

    }

    public void hitAttendanceRequest() {
        ApiRequest apiService =
                ApiClient.getClient(activity).create(ApiRequest.class);
        Call<UpdateLocationModel> call = apiService.UpdateLocation(getRequestBody());

       /* progressDialogUtility=new ProgressDialogUtility(activity);
        progressDialogUtility.showProgressDialog();*/
        call.enqueue(this);
    }

   /* "Trainer_Id":"12563",


"Created_On":"27 march 2018 11:15:10:196",

	"Latitude":"23.00",

	"Longitude":"52.00",

	"Current_address":"gurugaoun ,
         Sector-49"
*/

    private HashMap<String, Object> getRequestBody(){

        HashMap hashMap=new HashMap<String, Object>();
        hashMap = map;

        Log.e(TAG,"hashMap==> "+hashMap);
        return hashMap;
    }


    @Override
    public void onResponse(Call<UpdateLocationModel> call, Response<UpdateLocationModel> response) {
      //  progressDialogUtility.dismissProgressDialog();
        try {
            responseListener.onResponse(response.body());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(Call<UpdateLocationModel> call, Throwable t) {
      //  progressDialogUtility.dismissProgressDialog();
        responseListener.onResponse(null);
    }

}
