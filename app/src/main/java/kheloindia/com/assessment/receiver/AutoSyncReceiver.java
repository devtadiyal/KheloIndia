package kheloindia.com.assessment.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


import kheloindia.com.assessment.app.Fitness365App;
import kheloindia.com.assessment.functions.Constant;
import kheloindia.com.assessment.functions.GPSTracker;
import kheloindia.com.assessment.model.FitnessSkillTestResultModel;
import kheloindia.com.assessment.model.FitnessTestResultModel;
import kheloindia.com.assessment.model.InsertActivityModel;
import kheloindia.com.assessment.model.LocationTrackingModel;
import kheloindia.com.assessment.model.MyDartInsertActivityModel;
import kheloindia.com.assessment.model.ReportModel;
import kheloindia.com.assessment.model.SkillReportModel;
import kheloindia.com.assessment.model.SkillTransactionModel;
import kheloindia.com.assessment.model.TransactionModel;
import kheloindia.com.assessment.model.UpdateLocationModel;
import kheloindia.com.assessment.util.ConnectionDetector;
import kheloindia.com.assessment.util.ConnectivityReceiverListener;
import kheloindia.com.assessment.util.DBManager;
import kheloindia.com.assessment.util.ResponseListener;
import kheloindia.com.assessment.util.Utility;
import kheloindia.com.assessment.webservice.InsertActivityRequest;

/**
 * Created by CT13 on 2017-06-23.
 */

public class AutoSyncReceiver extends BroadcastReceiver implements ResponseListener {

    private ConnectionDetector connectionDetector;
    private  ArrayList<Object> transactionsDataList, transactionsSkillDataList,myDartActivityList, morningTrackingList;
    private  String max_test_date = "";
    private  String max_skill_date = "";
    private  Context context;
    private int activity_data_to_be_sent;
    private  GPSTracker gps;
    private  String TAG = "AutoSyncReceiver";
    public  AsyncTask longOperationReport,longOperationSkillReport;


    @Override
    public void onReceive(final Context context, Intent intent) {
        this.context = context;
        connectionDetector = new ConnectionDetector(context);
        ConnectivityReceiverListener connectivityReceiverListener= Fitness365App.getInstance().connectivityReceiverListener;
        if (connectionDetector.isConnectingToInternet()) {
            if (connectivityReceiverListener != null) {
               connectivityReceiverListener.onNetworkConnectionChanged();

            }  else {

//new SyncReceiver().execute();

            }
        }

        }




    /*private void sendLocationToServerAtFixedInterval() {
        checkTimeisBetween("06:00:00","16:00:00");
    }

    private void checkTimeisBetween(String string1, String string2) {
        try {

            Date time1 = new SimpleDateFormat("HH:mm:ss").parse(string1);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(time1);


            Date time2 = new SimpleDateFormat("HH:mm:ss").parse(string2);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(time2);
            calendar2.add(Calendar.DATE, 1);

            String current_time = new SimpleDateFormat("HH:mm:ss").format(new Date());
            Date d = new SimpleDateFormat("HH:mm:ss").parse(current_time);
            Calendar calendar3 = Calendar.getInstance();
            calendar3.setTime(d);
            calendar3.add(Calendar.DATE, 1);

            Date x = calendar3.getTime();
            if (x.after(calendar1.getTime()) && x.before(calendar2.getTime())) {
                Toast.makeText(context,"time check.... true", Toast.LENGTH_LONG).show();
                //checkes whether the current time is between 06:00:00 and 16:00:00.
                System.out.println(true);
                CallLocationApi();
            } else {
                Toast.makeText(context,"time check.... false", Toast.LENGTH_LONG).show();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }*/

 /*   private void CallLocationApi() {
        if (connectionDetector.isConnectingToInternet()) {
                    *//*"Trainer_Id":"15421",
                            "School_Id":"51",
                            "Activity_Id":"1",
                            "Created_On":"07 Feb 2017 1:16:10:196",
                            "Latitude":"23.00",
                            "Longitude":"52.00"*//*

            String Day_Status = "0";

            String activity_id = "1";



            gps = new GPSTracker(context);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 30 minutes = 1800 seconds

                    CallAPi();

                }
            }, 1800*1000);


        }
    }

    private void CallAPi() {
        final String address = "";

        final String currentDate = Utility.DisplayDateInParticularFormat(Constant.inputFormat);
        UpdateLocationRequest request = new UpdateLocationRequest(context, Constant.TEST_COORDINATOR_ID,
                currentDate,""+gps.getLatitude(),""+gps.getLongitude(), address, this);
        request.hitAttendanceRequest();
    }*/

    private  void sendMyDartActivityToServer(){

            if (myDartActivityList.size() != 0) {
                Object obj = myDartActivityList.get(0);
                if (obj instanceof MyDartInsertActivityModel) {
                    MyDartInsertActivityModel myDartInsertActivityModel = (MyDartInsertActivityModel) obj;
                    InsertActivityRequest insertActivityRequest = new InsertActivityRequest(context, myDartInsertActivityModel.getData(), myDartInsertActivityModel.getActivity_id(), myDartInsertActivityModel.getStudent_activity_id(),AutoSyncReceiver.this );
                    insertActivityRequest.hitUserRequest();
                }
            }

    }

    @Override
    public void onResponse( Object obj) {

        if(obj instanceof ReportModel) {
            if(longOperationReport!=null)
                longOperationReport=new LongOperation(obj).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

else
                longOperationReport.cancel(true);

        }
        else if(obj instanceof SkillReportModel) {
            if(longOperationSkillReport!=null)
                longOperationSkillReport=new LongOperation(obj).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            else
            longOperationSkillReport.cancel(true);
            }
        else
            new LongOperation(obj).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }


    public class SyncReceiver extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            transactionsDataList = DBManager.getInstance().getAllTableData(context,
                    DBManager.getInstance().TBL_LP_FITNESS_TEST_RESULT, "tested", "true", "synced", "false");

            morningTrackingList = DBManager.getInstance().getAllTableData(context,
                    DBManager.getInstance().TBL_LP_MORNING_TRACKING, "", "", "", "");

            transactionsSkillDataList = DBManager.getInstance().getAllTableData(context,
                    DBManager.getInstance().TBL_LP_FITNESS_SKILL_TEST_RESULT, "tested", "true", "last_modified_on", "0");
            Constant.SCHOOL_ID = PreferenceManager.getDefaultSharedPreferences(context).getString("school_id", "");
            myDartActivityList = DBManager.getInstance().getAllTableData(context, DBManager.TBL_LP_MY_DART_INSERT_ACTIVITY_TABLE, "current_school_id", Constant.SCHOOL_ID, "", "");
            try {
                max_test_date = DBManager.getInstance().getMaxDate(context, DBManager.TBL_LP_FITNESS_TEST_RESULT, "last_modified_on",Constant.CAMP_ID);
            } catch (Exception e1) {
                max_test_date = "";
                e1.printStackTrace();
            }

            if (morningTrackingList.size() > 0) {
                Utility.CallAPItoSyncMorningTracking(context, morningTrackingList, AutoSyncReceiver.this);
            }

            if (transactionsDataList.size() > 0) {
                Utility.syncTransactionsAPI(context, transactionsDataList, AutoSyncReceiver.this);

            } else {

                Utility.CallAPIToFetchAllReport(context, max_test_date, AutoSyncReceiver.this);
            }

            try {
                max_skill_date = DBManager.getInstance().getMaxDate(context, DBManager.TBL_LP_FITNESS_SKILL_TEST_RESULT, "last_modified_on",Constant.CAMP_ID);
            } catch (Exception e2) {
                max_skill_date = "";
            }

            if (transactionsSkillDataList.size() > 0) {
                Utility.syncSkillTransactionAPI(context, transactionsSkillDataList, AutoSyncReceiver.this);

            } else {
                Utility.CallAPIToFetchAllSkillReport(context, max_skill_date, AutoSyncReceiver.this);
            }
            sendMyDartActivityToServer();

            //sendLocationToServerAtFixedInterval();
            return null;
        }
        }

    private class LongOperation extends AsyncTask<Void, Void, Void> {
        private Object object;

        LongOperation(Object obj){
            object=obj;
        }
        @Override
        protected Void doInBackground(Void... params) {
            longOperation(object);
            return null;
        }
    }

    private void longOperation(Object obj){
        if (obj instanceof TransactionModel) {
            TransactionModel model = (TransactionModel) obj;

            if (model.getIsSuccess().equalsIgnoreCase("true")) {

                for (Object o : transactionsDataList) {


                    FitnessTestResultModel fitnessTestResultModel = (FitnessTestResultModel) o;
                    DBManager.getInstance().deleteTransactionRow(context, DBManager.getInstance().TBL_LP_FITNESS_TEST_RESULT, "student_id", "" + fitnessTestResultModel.getStudent_id(),
                            "test_type_id", "" + fitnessTestResultModel.getTest_type_id(),"camp_id",Constant.CAMP_ID);
                    fitnessTestResultModel.setTestedOrNot(true);
                    fitnessTestResultModel.setSyncedOrNot(true);
                    DBManager.getInstance().insertTables(DBManager.getInstance().TBL_LP_FITNESS_TEST_RESULT, fitnessTestResultModel);
                }
//                max_test_date = DBManager.getInstance().getMaxDate(context, DBManager.TBL_LP_FITNESS_TEST_RESULT, "last_modified_on");
//                Utility.CallAPIToFetchAllReport(context, max_test_date, this);
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
                editor.putString("last_synced_time", Utility.getDate(System.currentTimeMillis(), "dd MMM yy HH:mm:ss"));
                editor.commit();
                Utility.CallAPIToFetchAllReport(context, max_test_date, AutoSyncReceiver.this);
            }

        } else if (obj instanceof SkillTransactionModel) {

            SkillTransactionModel model = (SkillTransactionModel) obj;

            if (model.getIsSuccess().equalsIgnoreCase("true")) {
                for (Object o : transactionsSkillDataList) {


                    FitnessSkillTestResultModel fitnessTestResultModel = (FitnessSkillTestResultModel) o;
                    DBManager.getInstance().deleteTransactionRow(context, DBManager.getInstance().TBL_LP_FITNESS_SKILL_TEST_RESULT, "student_id", "" + fitnessTestResultModel.getStudent_id(),
                            "checklist_item_id", "" + fitnessTestResultModel.getChecklist_item_id(),"camp_id",Constant.CAMP_ID);
                    fitnessTestResultModel.setTested(true);
                    fitnessTestResultModel.setLast_modified_on(Constant.getDateTimeServer1());
                    DBManager.getInstance().insertTables(DBManager.getInstance().TBL_LP_FITNESS_SKILL_TEST_RESULT, fitnessTestResultModel);
                }
//                max_skill_date = DBManager.getInstance().getMaxDate(this.context, DBManager.TBL_LP_FITNESS_SKILL_TEST_RESULT, "last_modified_on");
//
//                Utility.CallAPIToFetchAllSkillReport(this.context, max_skill_date, this);
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
                editor.putString("last_synced_time", Utility.getDate(System.currentTimeMillis(), "dd MMM yy HH:mm:ss"));
                editor.commit();
                Utility.CallAPIToFetchAllSkillReport(context, max_skill_date, AutoSyncReceiver.this);
            }


        } else if (obj instanceof ReportModel) {
            ReportModel model = (ReportModel) obj;

            if (model.getIsSuccess().equalsIgnoreCase("true")) {
                final List<ReportModel.ResultBean.StudentBean> studentBeanList = ((ReportModel) obj).getResult().getStudent();


                for (int i = 0; i < studentBeanList.size(); i++) {
                    if(longOperationReport.isCancelled()) {
                        return;
                    }
                    ReportModel.ResultBean.StudentBean studentBean = studentBeanList.get(i);
                    FitnessTestResultModel fitnessTestResultModel = new FitnessTestResultModel();
                    fitnessTestResultModel.setStudent_id(Integer.parseInt(studentBean.getStudentID()));
                    fitnessTestResultModel.setCamp_id(Integer.parseInt(studentBean.getCampID()));
                    fitnessTestResultModel.setTest_type_id(Integer.parseInt(studentBean.getTestTypeID()));
                    fitnessTestResultModel.setTest_coordinator_id(Integer.parseInt(studentBean.getTestCoordinatorID()));
                    fitnessTestResultModel.setScore(studentBean.getScore());
                    fitnessTestResultModel.setPercentile(Double.valueOf(studentBean.getPercentile()));
                    fitnessTestResultModel.setCreated_on(studentBean.getCreatedOn());
                    fitnessTestResultModel.setCreated_by(studentBean.getCreatedBy());
                    fitnessTestResultModel.setLast_modified_by(studentBean.getLastModifiedBy());
                    fitnessTestResultModel.setLast_modified_on(studentBean.getLastModifiedOn());
                    fitnessTestResultModel.setDevice_date(studentBean.getCreatedOnDevice());
                    fitnessTestResultModel.setLatitude(0.0);
                    fitnessTestResultModel.setLongitude(0.0);
                    fitnessTestResultModel.setSubTestName("");
                    fitnessTestResultModel.setTestName("");
                    fitnessTestResultModel.setSyncedOrNot(true);
                    fitnessTestResultModel.setTestedOrNot(true);
//                            if(DBManager.getInstance().getAllTableData(context,DBManager.TBL_LP_FITNESS_TEST_RESULT,"student_id", "" + fitnessTestResultModel.getStudent_id(),
//                                    "test_type_id", "" + fitnessTestResultModel.getTest_type_id()).size()==0) {
                    DBManager.getInstance().deleteTransactionRow(AutoSyncReceiver.this.context, DBManager.getInstance().TBL_LP_FITNESS_TEST_RESULT, "student_id", "" + fitnessTestResultModel.getStudent_id(),
                            "test_type_id", "" + fitnessTestResultModel.getTest_type_id(),"camp_id",Constant.CAMP_ID);
                    DBManager.getInstance().insertTables(DBManager.getInstance().TBL_LP_FITNESS_TEST_RESULT, fitnessTestResultModel);
                    //}

                }

                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
                editor.putString("last_synced_time", Utility.getDate(System.currentTimeMillis(), "dd MMM yy HH:mm:ss"));
                editor.commit();


            }
        } else if (obj instanceof SkillReportModel) {
            SkillReportModel model = (SkillReportModel) obj;

            if (model.getIsSuccess().equalsIgnoreCase("true")) {
                final List<SkillReportModel.ResultBean.StudentBean> studentBeanList = ((SkillReportModel) obj).getResult().getStudent();

                for (int i = 0; i < studentBeanList.size(); i++) {
                    if(longOperationSkillReport.isCancelled()) {
                        return;
                    }
                    SkillReportModel.ResultBean.StudentBean studentBean = studentBeanList.get(i);
                    FitnessSkillTestResultModel fitnessTestResultModel = new FitnessSkillTestResultModel();
                    fitnessTestResultModel.setStudent_id(Integer.parseInt(studentBean.getFKStudentID()));
                    fitnessTestResultModel.setCamp_id(Integer.parseInt(studentBean.getFKCampID()));
                    fitnessTestResultModel.setTest_type_id(Integer.parseInt(studentBean.getFKTestTypeID()));
                    fitnessTestResultModel.setCoordinator_id(Integer.parseInt(studentBean.getFKTestCoordinatorID()));
                    fitnessTestResultModel.setScore(studentBean.getScore());
                    fitnessTestResultModel.setSkill_score_id(0);
                    fitnessTestResultModel.setCreated_on(studentBean.getCreatedOn());
                    fitnessTestResultModel.setLatitude("");
                    fitnessTestResultModel.setLongitude("");
                    fitnessTestResultModel.setChecklist_item_id(Integer.parseInt(studentBean.getFKCheckListItemID()));
                    fitnessTestResultModel.setLast_modified_on(studentBean.getLastModifiedOn());
                    fitnessTestResultModel.setTested(true);
                    fitnessTestResultModel.setSynced(true);
//                            if(DBManager.getInstance().getAllTableData(context,DBManager.TBL_LP_FITNESS_SKILL_TEST_RESULT,"student_id", "" + fitnessTestResultModel.getStudent_id(),
//                                    "checklist_item_id", "" + fitnessTestResultModel.getChecklist_item_id()).size()==0) {
                    DBManager.getInstance().deleteTransactionRow(AutoSyncReceiver.this.context, DBManager.getInstance().TBL_LP_FITNESS_SKILL_TEST_RESULT, "student_id", "" + fitnessTestResultModel.getStudent_id(),
                            "checklist_item_id", "" + fitnessTestResultModel.getChecklist_item_id(),"camp_id",Constant.CAMP_ID);

                    DBManager.getInstance().insertTables(DBManager.getInstance().TBL_LP_FITNESS_SKILL_TEST_RESULT, fitnessTestResultModel);

                    // }
                }


                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
                editor.putString("last_synced_time", Utility.getDate(System.currentTimeMillis(), "dd MMM yy HH:mm:ss"));
                editor.commit();

            }
        } else if (obj instanceof InsertActivityModel) {
            InsertActivityModel insertActivityModel = (InsertActivityModel) obj;

            if (insertActivityModel.getIsSuccess().equalsIgnoreCase("true")) {

                activity_data_to_be_sent++;
                if (activity_data_to_be_sent < myDartActivityList.size()) {
                    Object ob = myDartActivityList.get(activity_data_to_be_sent);
                    if (ob instanceof MyDartInsertActivityModel) {
                        MyDartInsertActivityModel myDartInsertActivityModel = (MyDartInsertActivityModel) ob;

                        InsertActivityRequest insertActivityRequest = new InsertActivityRequest(context, myDartInsertActivityModel.getData(), myDartInsertActivityModel.getActivity_id(), myDartInsertActivityModel.getStudent_activity_id(), AutoSyncReceiver.this);
                        insertActivityRequest.hitUserRequest();

                    }
                } else {

                    DBManager.getInstance().deleteAllRows(DBManager.TBL_LP_MY_DART_INSERT_ACTIVITY_TABLE);
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
                    editor.putString("last_synced_time", Utility.getDate(System.currentTimeMillis(), "dd MMM yy HH:mm:ss"));
                    editor.commit();

                }

            }
        } else if(obj instanceof LocationTrackingModel){
            UpdateLocationModel model = (UpdateLocationModel) obj;
            if (model.getIsSuccess().equalsIgnoreCase("true")) {
                Log.e(TAG,"Location syncing done :)");
            }

        }

        else if(obj instanceof UpdateLocationModel) {
            UpdateLocationModel model = (UpdateLocationModel) obj;
            if (model.getIsSuccess().equalsIgnoreCase("true")) {
                Log.e(TAG,"....success sync tracking neha");
                String message = model.getMessage();
                // delete all the entries from the table
                DBManager.getInstance().deleteAllRows(DBManager.TBL_LP_MORNING_TRACKING);
            } else {
                Log.e(TAG,"....erro sync tracking neha");

            }

        }

    }
}

