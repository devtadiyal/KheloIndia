package kheloindia.com.assessment.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.location.Location;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import kheloindia.com.assessment.AttendanceGeofencingActivity;
import kheloindia.com.assessment.KheloDashBoardActivity;
import kheloindia.com.assessment.ProfileActivity;
import kheloindia.com.assessment.R;
import kheloindia.com.assessment.SchoolActivity;
import kheloindia.com.assessment.app.Fitness365App;
import kheloindia.com.assessment.functions.Constant;
import kheloindia.com.assessment.model.FitnessSkillTestResultModel;
import kheloindia.com.assessment.model.FitnessTestResultModel;
import kheloindia.com.assessment.model.LocationTrackingModel;
import kheloindia.com.assessment.webservice.SkillReportRequest;
import kheloindia.com.assessment.webservice.SyncTransactionSkillRequest;
import kheloindia.com.assessment.webservice.TestReportRequest;
import kheloindia.com.assessment.webservice.TransactionRequest;
import kheloindia.com.assessment.webservice.UpdateLocationRequest;

/**
 * Created by CT13 on 2017-05-11.
 */

public class Utility {

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final String IMAGE_DIRECTORY_NAME = "Fitness365_Images";
    public static final String INDIAN_TIME_ZONE = "GMT+5:30";
    public static final int REQUEST_SELECT_PICTURE = 2;
    static Context context = Fitness365App.getContext();
    String TAG = "Utility";
    SharedPreferences sp;

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth,
                        LinearLayout.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    public static void setExpandableListViewHeight(ExpandableListView listView,
                                                   int group) {
        ExpandableListAdapter listAdapter = listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();

                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();

    }

    public static void showActionBar(AppCompatActivity appCompatActivity,
                                     Toolbar toolbar, String title) {

        toolbar.setTitle(title);
        appCompatActivity.setSupportActionBar(toolbar);
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appCompatActivity.getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public static String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static void showLogoutDialog(final Activity context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setCancelable(false);
        alertDialog.setMessage("Are you sure you want to Logout?");

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.alarm);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
                logoutUser(context, false);
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    public static void logoutUser(Activity context, boolean forClearingPreferences) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        if (forClearingPreferences) {
            editor.clear();
            System.out.println("DEV LOGOUT IF");
        } else {
            editor.putBoolean(AppConfig.IS_STUDENT_DETAILS_GET, false);
            editor.putBoolean(AppConfig.IS_LOGIN, false);
            editor.apply();
            System.out.println("DEV LOGOUT ELSE");
            // sp.getString(AppConfig.IS_LOGIN, "false");

            editor.remove("IS_LOGIN");
            editor.remove("isStudentLogin");
            editor.remove("inSchoolActivity");
            editor.remove("isStudentDetailGet");
            editor.remove("fromTakeTest");
            editor.remove("got_all_camp");
            editor.remove("got_all_test");
        }
        // editor.remove("remember_checked");
        editor.apply();

        Constant.seniorList.clear();
        //sp.edit().clear().commit();
        Intent i = new Intent(context, KheloDashBoardActivity.class);

        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);

        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(i);
        context.finish();
    }

    public static String getMonthIndex(String month) {
        switch (month) {
            case "Jan":
                return "1";

            case "Feb":
                return "2";

            case "Mar":
                return "3";

            case "Apr":
                return "4";

            case "May":
                return "5";

            case "Jun":
                return "6";

            case "Jul":
                return "7";

            case "Aug":
                return "8";

            case "Sep":
                return "9";

            case "Oct":
                return "10";

            case "Nov":
                return "11";

            case "Dec":
                return "12";
        }

        return "All";
    }

    public static String DisplayDateInParticularFormat(String format) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(format);
        String formattedDate = df.format(c.getTime());

        return formattedDate;
    }

    public static String convertMilliSecondsToMiSecMs(Context c, long milliseconds) {
        long milli = milliseconds % 1000;
        int s = (int) (milliseconds / 1000) % 60;
        int m = (int) ((milliseconds / (1000 * 60)) % 60);

        return String.format("%02d" + "m %02d" + "s %03d" + "ms", m, s, milli);
    }

    public static String convertCentiMetreToMtCmMm(Context c, double cm) {
        String number = String.valueOf(cm);
        int mm = Integer.parseInt(number.substring(number.indexOf(".")).substring(1));
        int cms = (int) (cm % 100);
        int mt = (int) (cm / 100);

        return String.format("%02d" + "m %02d" + "cm %01d" + "mm", mt, cms, mm);
    }

    public static String convertMilliMetreToCmMm(Context c, double mms) {
        int mm = (int) (mms % 10);
        int cms = (int) (mms / 10);

        return String.format("%01d" + "cm, %01d" + "mm", cms, mm);
    }

    public static void showNotification(Context ctx) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ctx);
        mBuilder.setSmallIcon(R.drawable.dart);
        mBuilder.setContentTitle(ctx.getString(R.string.notification_title));
        mBuilder.setContentText(ctx.getString(R.string.notification_content));
        mBuilder.setAutoCancel(true);
        Intent resultIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + ctx.getPackageName()));
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);

// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

// notificationID allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());

    }


    public static void syncSkillTransactionAPI(Context context, ArrayList<Object> dataList, ResponseListener responseListener) {

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = null;
        FitnessSkillTestResultModel fitnessSkillTestResultModelPrevious = null;
        ArrayList<String> scoreList = new ArrayList<>();
        ArrayList<String> checkIdList = new ArrayList<>();

        for (Object o : dataList) {
            try {

                FitnessSkillTestResultModel fitnessSkillTestResultModel = (FitnessSkillTestResultModel) o;

                if (fitnessSkillTestResultModelPrevious != null && fitnessSkillTestResultModelPrevious.getStudent_id() != fitnessSkillTestResultModel.getStudent_id()) {
                    jsonObject = new JSONObject();
                    jsonObject.put("FK_Student_ID", fitnessSkillTestResultModelPrevious.getStudent_id());
                    jsonObject.put("FK_Camp_ID", "" + fitnessSkillTestResultModelPrevious.getCamp_id());
                    jsonObject.put("FK_Test_Type_ID", "" + fitnessSkillTestResultModelPrevious.getTest_type_id());
                    jsonObject.put("FK_Test_Coordinator_ID", "" + fitnessSkillTestResultModelPrevious.getCoordinator_id());
                    jsonObject.put("Created_On", "" + fitnessSkillTestResultModelPrevious.getCreated_on());
                    jsonObject.put("Longitude", "" + fitnessSkillTestResultModelPrevious.getLongitude());
                    jsonObject.put("Latitude", "" + fitnessSkillTestResultModelPrevious.getLatitude());
                    String dateStr = "" + fitnessSkillTestResultModelPrevious.getDevice_date();
                    DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
                    Date date = formatter.parse(dateStr);
                    String Last_Modified_On = Constant.ConvertDateToString(date);
                    jsonObject.put("Last_Modified_On", Last_Modified_On);
                    jsonObject.put("Score", TextUtils.join(",", scoreList));
                    jsonObject.put("FK_CheckListItem_ID", TextUtils.join(",", checkIdList));
                    jsonArray.put(jsonObject);
                    scoreList.clear();
                    checkIdList.clear();
                } else if (fitnessSkillTestResultModelPrevious != null && fitnessSkillTestResultModelPrevious.getStudent_id() == fitnessSkillTestResultModel.getStudent_id() && fitnessSkillTestResultModelPrevious.getTest_type_id() != fitnessSkillTestResultModel.getTest_type_id()) {
                    jsonObject = new JSONObject();
                    jsonObject.put("FK_Student_ID", fitnessSkillTestResultModelPrevious.getStudent_id());
                    jsonObject.put("FK_Camp_ID", "" + fitnessSkillTestResultModelPrevious.getCamp_id());
                    jsonObject.put("FK_Test_Type_ID", "" + fitnessSkillTestResultModelPrevious.getTest_type_id());
                    jsonObject.put("FK_Test_Coordinator_ID", "" + fitnessSkillTestResultModelPrevious.getCoordinator_id());
                    jsonObject.put("Created_On", "" + fitnessSkillTestResultModelPrevious.getCreated_on());
                    jsonObject.put("Longitude", "" + fitnessSkillTestResultModelPrevious.getLongitude());
                    jsonObject.put("Latitude", "" + fitnessSkillTestResultModelPrevious.getLatitude());
                    String dateStr = "" + fitnessSkillTestResultModelPrevious.getDevice_date();
                    DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
                    Date date = formatter.parse(dateStr);
                    String Last_Modified_On = Constant.ConvertDateToString(date);
                    jsonObject.put("Last_Modified_On", Last_Modified_On);
                    jsonObject.put("Score", TextUtils.join(",", scoreList));
                    jsonObject.put("FK_CheckListItem_ID", TextUtils.join(",", checkIdList));
                    jsonArray.put(jsonObject);
                    scoreList.clear();
                    checkIdList.clear();
                }
                scoreList.add(fitnessSkillTestResultModel.getScore());
                checkIdList.add(String.valueOf(fitnessSkillTestResultModel.getChecklist_item_id()));
                fitnessSkillTestResultModelPrevious = fitnessSkillTestResultModel;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        try {
            jsonObject = new JSONObject();
            jsonObject.put("FK_Student_ID", fitnessSkillTestResultModelPrevious.getStudent_id());
            jsonObject.put("FK_Camp_ID", "" + fitnessSkillTestResultModelPrevious.getCamp_id());
            jsonObject.put("FK_Test_Coordinator_ID", "" + fitnessSkillTestResultModelPrevious.getCoordinator_id());
            jsonObject.put("Created_On", "" + fitnessSkillTestResultModelPrevious.getCreated_on());
            jsonObject.put("Longitude", "" + fitnessSkillTestResultModelPrevious.getLongitude());
            jsonObject.put("Latitude", "" + fitnessSkillTestResultModelPrevious.getLatitude());
            jsonObject.put("FK_Test_Type_ID", fitnessSkillTestResultModelPrevious.getTest_type_id());
            String dateStr = "" + fitnessSkillTestResultModelPrevious.getDevice_date();
            DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
            Date date = formatter.parse(dateStr);
            String Last_Modified_On = Constant.ConvertDateToString(date);
            jsonObject.put("Last_Modified_On", Last_Modified_On);
            jsonObject.put("Score", TextUtils.join(",", scoreList));
            jsonObject.put("FK_CheckListItem_ID", TextUtils.join(",", checkIdList));
            jsonArray.put(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("data", jsonArray.toString());
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String test_coordinator_id = sp.getString("test_coordinator_id", "");
        map.put("AccessBy", test_coordinator_id);

        SyncTransactionSkillRequest transactionSkillRequest = new SyncTransactionSkillRequest(context, map, responseListener);
        transactionSkillRequest.hitUserRequest();

    }

    public static void CallAPItoSyncMorningTracking(Context context, ArrayList<Object> dataList,
                                                    ResponseListener responseListener) {


        JSONArray jArray = new JSONArray();
        JSONObject jObj;


        for (Object o : dataList) {
            LocationTrackingModel model = (LocationTrackingModel) o;
            jObj = new JSONObject();
            try {
                jObj.put("Trainer_Id", model.getTrainer_id());
                jObj.put("Created_On", model.getCreated_on());
                jObj.put("Latitude", model.getLat());
                jObj.put("Longitude", "" + model.getLng());

                jArray.put(jObj);

            } catch (Exception e1) {
                e1.printStackTrace();
            }

        }

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("data", jArray.toString());
        map.put("AccessBy", Constant.TEST_COORDINATOR_ID);

        UpdateLocationRequest request = new UpdateLocationRequest(context, map, responseListener);
        request.hitAttendanceRequest();

    }

    public static void openGmail(Context context, File file) {

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("*/*");
        // emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"email@example.com"});
// the mail subject
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Test Report " + Constant.SCHOOL_NAME);
        // the attachment
        Uri uriFile = FileProvider.getUriForFile(
                context, context.getPackageName() + ".provider", file);
        emailIntent.putExtra(Intent.EXTRA_STREAM, uriFile);
        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        context.startActivity(Intent.createChooser(emailIntent, "Send email..."));

    }


    public static void openSharingOptions(Context context, File file, String userName, String testCoordinatorName, String fileName) {

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("*/*");
        // emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"email@example.com"});
// the mail subject
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, fileName + " (Submitted for Manual Upload)");
        emailIntent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.export_email_body, userName, testCoordinatorName));
        // the attachment
        Uri uriFile = FileProvider.getUriForFile(
                context, context.getPackageName() + ".provider", file);
        emailIntent.putExtra(Intent.EXTRA_STREAM, uriFile);
        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        context.startActivity(Intent.createChooser(emailIntent, "Send email to Principal.."));

    }

    public static void openDownloadManager(Context context, File file) {


    }

    public static void syncTransactionsAPI(Context context, ArrayList<Object> dataList, ResponseListener responseListener) {


//        if(transactionsDataList.size()!=0) {
//            Log.e(TAG, "transaction table size==> " + transactionsDataList.size());
        JSONArray jsonArray = new JSONArray();
        for (Object o : dataList) {
            try {
                JSONObject jsonObject = new JSONObject();
                FitnessTestResultModel fitnessTestResultModel = (FitnessTestResultModel) o;
                jsonObject.put("Student_ID", fitnessTestResultModel.getStudent_id());
                jsonObject.put("Camp_ID", "" + fitnessTestResultModel.getCamp_id());
                jsonObject.put("Test_Type_ID", "" + fitnessTestResultModel.getTest_type_id());
                jsonObject.put("Test_Coordinator_ID", "" + fitnessTestResultModel.getTest_coordinator_id());
                jsonObject.put("Score", "" + fitnessTestResultModel.getScore());
                jsonObject.put("Percentile", "" + fitnessTestResultModel.getPercentile());
                jsonObject.put("Created_By", "" + fitnessTestResultModel.getCreated_by());
                jsonObject.put("Created_On", "" + fitnessTestResultModel.getCreated_on());
                jsonObject.put("SyncDateTime", "");
                jsonObject.put("Longitude", "" + fitnessTestResultModel.getLongitude());
                jsonObject.put("Latitude", "" + fitnessTestResultModel.getLatitude());

                String dateStr = "" + fitnessTestResultModel.getDevice_date();
                DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
                Date date = formatter.parse(dateStr);

                String Last_Modified_On = Constant.ConvertDateToString(date);

                jsonObject.put("Last_Modified_On", Last_Modified_On);
                jsonArray.put(jsonObject);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //syncTransactionItemAPI(transactionsDataList.get(sync_inc));
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("data", jsonArray.toString());
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String test_coordinator_id = sp.getString("test_coordinator_id", "");
        map.put("AccessBy", test_coordinator_id);
        TransactionRequest transactionRequest = new TransactionRequest(context, map, responseListener);
        transactionRequest.hitUserRequest();

    }


    public static void CallAPIToFetchAllReport(Context context, String max_date, ResponseListener responseListener) {


        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String camp_id = sp.getString("camp_id", "");
        String test_coordinator_id = sp.getString("test_coordinator_id", "");

        TestReportRequest testReportRequest = new TestReportRequest(context, camp_id, max_date, test_coordinator_id, responseListener);
        testReportRequest.hitUserRequest();
    }

    public static void CallAPIToFetchAllSkillReport(Context context, String max_date, ResponseListener responseListener) {


        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String camp_id = sp.getString("camp_id", "");
        String test_coordinator_id = sp.getString("test_coordinator_id", "");

        SkillReportRequest testReportRequest = new SkillReportRequest(context, camp_id, max_date, test_coordinator_id, responseListener);
        testReportRequest.hitUserRequest();
    }

    public static int dpToPx(Context context, int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

    public static int pxToDp(Context context, int px) {
        return (int) (px / context.getResources().getDisplayMetrics().density);
    }

    public static int spToPx(Context context, float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    public static void showGPSDisabledAlertToUser(final Context ctx) {
        final AlertDialog.Builder localBuilder = new AlertDialog.Builder(ctx);
        localBuilder
                .setMessage(
                        "GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface paramAnonymousDialogInterface,
                                    int paramAnonymousInt) {
                               /* Intent localIntent1 = new Intent(ctx,
                                        AttendanceGeofencingActivity.class);
                                ctx.startActivity(localIntent1);*/
                                Intent localIntent2 = new Intent(
                                        "android.settings.LOCATION_SOURCE_SETTINGS");
                                ctx.startActivity(localIntent2);

                                paramAnonymousDialogInterface.dismiss();

                            }
                        });
        localBuilder.create().show();
    }

    public static void CalculateDistance(double source_lat, double source_lng, double des_lat, double dest_lng, float radius) {
        Location startPoint = new Location("locationA");
        startPoint.setLatitude(source_lat);
        startPoint.setLongitude(source_lng);

        Location endPoint = new Location("locationA");
        endPoint.setLatitude(des_lat);
        endPoint.setLongitude(dest_lng);

        double distance = startPoint.distanceTo(endPoint);

        if (distance > radius) {
            Constant.NEW_STATUS = "EXIT";
            if (Constant.count == 1) {
                showGeofenceNotification(context, "Exiting Geofecing.");
            } else if (!Constant.NEW_STATUS.equalsIgnoreCase(Constant.OLD_STATUS)) {
                showGeofenceNotification(context, "Exiting Geofecing.");
            }

            Constant.count++;

            Constant.OLD_STATUS = Constant.NEW_STATUS;


        } else {
            Constant.NEW_STATUS = "ENTER";
            if (Constant.count == 1) {
                showGeofenceNotification(context, "Entering Geofecing.");
            } else if (!Constant.NEW_STATUS.equalsIgnoreCase(Constant.OLD_STATUS)) {
                showGeofenceNotification(context, "Entering Geofecing.");
            }

            Constant.count++;

            Constant.OLD_STATUS = Constant.NEW_STATUS;

        }
    }

    public static void showGeofenceNotification(Context ctx, String message) {

        //context = ctx;

        Intent myIntent = new Intent(ctx, AttendanceGeofencingActivity.class);
        @SuppressLint("WrongConstant") PendingIntent pendingIntent = PendingIntent.getActivity(
                ctx,
                0,
                myIntent,
                Intent.FLAG_ACTIVITY_NEW_TASK);


        PlaySound();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ctx);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle("Sentiance Geofencing");
        mBuilder.setContentText(message);
        mBuilder.setAutoCancel(true);
        Intent resultIntent = new Intent(ctx, AttendanceGeofencingActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);


// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

// notificationID allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());
    }

    private static void PlaySound() {

        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context, notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static File ConvertBitmapToFile(Bitmap bitmap, Context ctx) {
        File filesDir = ctx.getFilesDir();

        // code for getting timestamp
        Long tsLong = System.currentTimeMillis() / 1000;
        String name = tsLong.toString();

        File imageFile = new File(filesDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();

            Log.e("Utility", "imageFile==> " + imageFile);
        } catch (Exception e) {
            Log.e("Utility", "Error writing bitmap", e);
        }
        return imageFile;
    }

    public static void rotateFabForward(Context c, FloatingActionButton fab) {
        Animation rotation = AnimationUtils.loadAnimation(c, R.anim.rotate);
        rotation.setFillAfter(true);
        fab.startAnimation(rotation);
    }

    public static void rotateFabBackward(FloatingActionButton fab) {
        fab.clearAnimation();
    }


    public static boolean isAppInstalled(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }

    public static void changeLanguage(Context context, String lan) {
        Locale locale = new Locale(lan);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config,
                context.getResources().getDisplayMetrics());
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        edit.putString("lan", lan);
        edit.commit();
    }

    public static void showPermissionDialog(Activity activity, String title) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle(activity.getString(R.string.permission_denied));
        alertDialog.setMessage(title);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });
        alertDialog.show();

    }

    public static void openURLBrowser(Context context, String url) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void showStateCityUpdateDialog(final Activity context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        //alertDialog.setTitle(context.getResources().getString(R.string.app_update_message));
        alertDialog.setMessage(context.getResources().getString(R.string.update_state_city_msg));
        alertDialog.setCancelable(false);
        AppConfig.IS_STATE_UPDATION_DIALOG_SHOWING = true;

        alertDialog.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                AppConfig.IS_STATE_UPDATION_DIALOG_SHOWING = false;
                dialog.cancel();
                context.startActivity(new Intent(context, ProfileActivity.class));
            }
        });

        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AppConfig.IS_STATE_UPDATION_DIALOG_SHOWING = false;
                dialogInterface.cancel();
                context.finish();
            }
        });

        if (!context.isFinishing())
            alertDialog.show();
    }

    public static void showSchoolDeactivatedDialog(final Activity context) {
//        if (!AppConfig.IS_SCHOOL_DEACTIVATED_DIALOG_SHOWING) {
//
//        }
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        //alertDialog.setTitle(context.getResources().getString(R.string.app_update_message));
        alertDialog.setMessage(context.getResources().getString(R.string.deactivated_from_school_msg,
                PreferenceManager.getDefaultSharedPreferences(context).getString(AppConfig.SCHOOL_NAME, "")));
        alertDialog.setCancelable(false);
        //AppConfig.IS_SCHOOL_DEACTIVATED_DIALOG_SHOWING = true;

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //AppConfig.IS_SCHOOL_DEACTIVATED_DIALOG_SHOWING = false;
                dialog.cancel();
                //context.startActivity(new Intent(context, SchoolActivity.class));
                //context.finish();
            }
        });

//        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                AppConfig.IS_SCHOOL_DEACTIVATED_DIALOG_SHOWING = false;
//                dialogInterface.cancel();
//                context.finish();
//            }
//        });

        if (!context.isFinishing())
            alertDialog.show();
    }

    public static boolean checkForSchoolDeActivated(Context context, String schoolId) {
        boolean isDeactivated = false;
        HashMap<String, String> map = null;
        try {
            map = new DBManager().getParticularRow(context, DBManager.TBL_LP_SCHOOLS_MASTER,
                    AppConfig.SCHOOL_ID, schoolId, AppConfig.IS_ATTACHED, "1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (map != null && map.size() == 0)
            isDeactivated = true;
        return isDeactivated;
    }

    public static String getDateTimeString(long time) {
        String dateFormat = "ddMMM_HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(INDIAN_TIME_ZONE));
        return simpleDateFormat.format(time);
    }

    public static boolean hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            View view = activity.getCurrentFocus();

            if (view != null)
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        return true;
    }


//    public static Drawable resizeImage(Context context,int resId, int w, int h)
//    {
//        Bitmap BitmapOrg = BitmapFactory.decodeResource(context.getResources(), resId);
//        int width = BitmapOrg.getWidth();
//        int height = BitmapOrg.getHeight();
//        int newWidth = w;
//        int newHeight = h;
//        float scaleWidth = ((float) newWidth) / width;
//        float scaleHeight = ((float) newHeight) / height;
//        Matrix matrix = new Matrix();
//        matrix.postScale(scaleWidth, scaleHeight);
//        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0,width, height, matrix, true);
//        return new BitmapDrawable(resizedBitmap);
//    }


}
