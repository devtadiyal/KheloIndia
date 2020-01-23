package kheloindia.com.assessment.util;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import org.jsoup.Jsoup;

import kheloindia.com.assessment.R;

public class GetVersionCode extends AsyncTask<Void, String, String> {


    private String currentVersion;
    private long lastUpdateTime;
    private Activity context;
    Intent intent;
    ProgressDialogUtility progressDialogUtility;
    Resources resources;


    public GetVersionCode(Activity context,Intent intent) {
        this.context = context;
        this.intent = intent;
        resources = context.getResources();
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(),0);
            currentVersion = packageInfo.versionName;
            lastUpdateTime = packageInfo.lastUpdateTime;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        progressDialogUtility = new ProgressDialogUtility(context);
        progressDialogUtility.setMessage(context.getResources().getString(R.string.app_update_message));
        if (intent!=null) {
            progressDialogUtility.showProgressDialog();
        }
    }


    @Override
    protected String doInBackground(Void... voids) {

        String newVersion = null;
        Log.w("check",context.getPackageName());
        try {
            newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + context.getPackageName()+ "&hl=en")
                    .timeout(30000)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get()
                    .select(".hAyfc .htlgb")
                    .get(7)
                    .ownText();

//                    .select(".xyOfqd .hAyfc:nth-child(4) .htlgb span")
//                    .get(0)
//                    .ownText();

//            newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + context.getPackageName()+ "&hl=en")
//                    .timeout(30000)
//                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
//                    .referrer("http://www.google.com")
//                    .get().select(".hAyfc .htlgb").get(4).ownText();
//                    .getElementsByClass("xyOfqd").select(".hAyfc")
//                    .get(3).child(1).child(0).child(0).ownText();
            return newVersion;
        } catch (Exception e) {
            return newVersion;
        }
    }

    @Override
    protected void onPostExecute(String onlineVersion) {

        if (onlineVersion != null && !onlineVersion.isEmpty()) {
            if (!currentVersion.equals(onlineVersion)) {
                if (intent!=null)
                    progressDialogUtility.dismissProgressDialog();
                    showAppUpdateDialog();
            }
            else {
//                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
//                sharedPreferences.edit().putBoolean(AppConfig.IS_APP_UPDATED,true).apply();
                startLoginActivity();
            }
        }
        else {
            startLoginActivity();
        }
        Log.d("update", "Current version " + currentVersion + "playstore version " + onlineVersion);
    }

    private void showAppUpdateDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(context.getResources().getString(R.string.notification_title));

        //alertDialog.setMessage(context.getResources().getString(R.string.notification_title)+", "+resources.getString(R.string.notification_content));
        //alertDialog.setMessage(getDialogMessage());
        alertDialog.setMessage(context.getResources().getString(R.string.notification_content));
        alertDialog.setCancelable(false);
        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.alarm);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
                Intent resultIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + context.getPackageName()));

                if (null != resultIntent.resolveActivity(context.getPackageManager())) {
                    resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(resultIntent);
                }
                else
                {
                    Toast.makeText(context,"Google Play Store not found",Toast.LENGTH_SHORT).show();
                }

            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
//                Utility.showStateCityUpdateDialog(context);
                if (intent!=null)
                    context.startActivity(intent);
            }
        });
        if(!context.isFinishing())
            alertDialog.show();
    }

    private void startLoginActivity(){
        if (intent!=null) {
            progressDialogUtility.dismissProgressDialog();
            context.startActivity(intent);
        }
    }

    private String getDialogMessage(){
        String msg = "";
        Log.w("check","current_time"+System.currentTimeMillis()+" and updated time "+lastUpdateTime);
        //lastUpdateTime = 1551361942;
        long timeDifference = (System.currentTimeMillis() - lastUpdateTime);
        long days = timeDifference/(60*60*24*1000);
        if (days>1){
            msg = "Your current app version is " + days + " days old.";
        }
        else {
            msg = "A new version of app is available. ";
        }
        msg = msg + context.getResources().getString(R.string.notification_content);
        return msg;
    }

}
