package kheloindia.com.assessment;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.Jsoup;

import kheloindia.com.assessment.util.AppConfig;
import kheloindia.com.assessment.util.GetVersionCode;
import kheloindia.com.assessment.util.ProgressDialogUtility;
import kheloindia.com.assessment.util.Utility;


public class KheloDashBoardActivity extends AppCompatActivity implements View.OnClickListener {

    private Button fitness_assess_btn, where_play_btn, how_play_btn;
    private String packageName = "com.kheloindia.mobile.app";
    private SharedPreferences sp;
    private ImageView khelo_logo_iv, gov_logo_iv;
    private ProgressDialogUtility progressDialogUtility;
    private TextView registerForTot, sop, adminManual, ecertificate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.khelo_dashboard_screen);
        init();
    }


    private void init() {
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        fitness_assess_btn = (Button) findViewById(R.id.fitness_assess_btn);
        where_play_btn = (Button) findViewById(R.id.how_play_btn);
        how_play_btn = (Button) findViewById(R.id.where_play_btn);
        khelo_logo_iv = (ImageView) findViewById(R.id.khelo_logo_iv);
        gov_logo_iv = (ImageView) findViewById(R.id.gov_logo_iv);
        //registerForTot = (TextView) findViewById(R.id.register_for_tot);
        sop = (TextView) findViewById(R.id.sop);
        ecertificate = (TextView) findViewById(R.id.ecertificate);
        adminManual = (TextView) findViewById(R.id.admin_manual);

        Typeface font_semi_bold = Typeface.createFromAsset(getAssets(),
                "fonts/Barlow-SemiBold.ttf");
        fitness_assess_btn.setTypeface(font_semi_bold);
        where_play_btn.setTypeface(font_semi_bold);
        how_play_btn.setTypeface(font_semi_bold);
        //registerForTot.setTypeface(font_semi_bold);
        sop.setTypeface(font_semi_bold);
        adminManual.setTypeface(font_semi_bold);

        fitness_assess_btn.setOnClickListener(this);
        where_play_btn.setOnClickListener(this);
        how_play_btn.setOnClickListener(this);
        //registerForTot.setOnClickListener(this);
        sop.setOnClickListener(this);
        adminManual.setOnClickListener(this);
        ecertificate.setOnClickListener(this);


        if (sp.getString("lan", "").equalsIgnoreCase("hi")) {
            khelo_logo_iv.setImageResource(R.drawable.kheloindia_hindi_i);
            gov_logo_iv.setImageResource(R.drawable.ministry_of_youth_hindi_i);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.fitness_assess_btn:
                new GetVersionCode(this, new Intent(this, LoginActivity.class)).execute();


                break;

            case R.id.how_play_btn:
                if (Utility.isAppInstalled(this, packageName)) {
                    Intent intent = new Intent();
                    intent.putExtra("lang", "en");
                    intent.setComponent(new ComponentName("com.kheloindia.mobile.app", "com.kheloindia.mobile.app.ativities.HowToPlayListingPage"));
                    startActivity(intent);

                } else {
                    // not installed
                    Intent resultIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.kheloindia.mobile.app"));
                    resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(resultIntent);
                }

                break;


            case R.id.where_play_btn:
                if (Utility.isAppInstalled(this, packageName)) {
                    Intent intent = new Intent();
                    // intent.putExtra("lang","en");
                    intent.setComponent(new ComponentName("com.kheloindia.mobile.app", "com.kheloindia.mobile.app.ativities.WhereToPlayListActivity"));
                    startActivity(intent);

                } else {
                    // not installed
                    Intent resultIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.kheloindia.mobile.app"));
                    resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(resultIntent);
                }
                break;

            case R.id.ecertificate:
                //Utility.openURLBrowser(this, AppConfig.ECERTIFICATE_URL);
                break;

            case R.id.sop:
                try {
                    Utility.openURLBrowser(this, AppConfig.SOP_URL);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.admin_manual:
                try {
                    Utility.openURLBrowser(this, AppConfig.ADMIN_MANUAL_URL);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

        }

    }

    private void showAppUpdateDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(KheloDashBoardActivity.this);

        alertDialog.setMessage(getResources().getString(R.string.notification_title) + ", " + getResources().getString(R.string.notification_content));
        alertDialog.setCancelable(false);
        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.alarm);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
                Intent resultIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + KheloDashBoardActivity.this.getPackageName()));
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(resultIntent);

            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                startActivity(new Intent(KheloDashBoardActivity.this, LoginActivity.class));
            }
        });
        if (!KheloDashBoardActivity.this.isFinishing())
            alertDialog.show();
    }

//    public class GetVersionCode extends AsyncTask<Void, String, String> {
//
//
//        private String currentVersion;
//        private Context context;
//
//
//        public GetVersionCode(Context context) {
//            this.context = context;
//            try {
//                currentVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
//            } catch (PackageManager.NameNotFoundException e) {
//                e.printStackTrace();
//            }
//            progressDialogUtility=new ProgressDialogUtility(KheloDashBoardActivity.this);
//            progressDialogUtility.setMessage(getString(R.string.app_update_message));
//            progressDialogUtility.showProgressDialog();
//        }
//
//
//        @Override
//        protected String doInBackground(Void... voids) {
//
//            String newVersion = null;
//            try {
//                newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + context.getPackageName()+ "&hl=en")
//                        .timeout(30000)
//                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
//                        .referrer("http://www.google.com")
//                        .get()
//                        .select(".xyOfqd .hAyfc:nth-child(4) .htlgb span")
//                        .get(0)
//                        .ownText();
//                return newVersion;
//            } catch (Exception e) {
//                return newVersion;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(String onlineVersion) {
//
//            if (onlineVersion != null && !onlineVersion.isEmpty()) {
//                if (!currentVersion.equals(onlineVersion)) {
//                    progressDialogUtility.dismissProgressDialog();
//                    showAppUpdateDialog();
//                }else {
//                    progressDialogUtility.dismissProgressDialog();
//                    startActivity(new Intent(KheloDashBoardActivity.this, LoginActivity.class));
//                }
//                }else {
//                progressDialogUtility.dismissProgressDialog();
//                startActivity(new Intent(KheloDashBoardActivity.this, LoginActivity.class));
//            }
//            Log.d("update", "Current version " + currentVersion + "playstore version " + onlineVersion);
//        }
//
//
//
//    }

}
