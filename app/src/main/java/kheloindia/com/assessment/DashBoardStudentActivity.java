package kheloindia.com.assessment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import kheloindia.com.assessment.adapter.ScreenAdapter;
import kheloindia.com.assessment.model.ActiveModel;
import kheloindia.com.assessment.model.ScreenMasterModel;
import kheloindia.com.assessment.model.UserScreenMapModel;
import kheloindia.com.assessment.util.ConnectionDetector;
import kheloindia.com.assessment.util.DBManager;
import kheloindia.com.assessment.util.ItemClickListener;
import kheloindia.com.assessment.util.ResponseListener;
import kheloindia.com.assessment.util.Utility;

import kheloindia.com.assessment.functions.Constant;
import kheloindia.com.assessment.webservice.ScreenMasterRequest;

/**
 * Created by CT13 on 2017-11-13.
 */

public class DashBoardStudentActivity extends AppCompatActivity implements View.OnClickListener, ResponseListener,
        ItemClickListener {

   /* LinearLayout insights_layout, top_sports_layout,shape_365_layout,utilities_layout,gallery_layout;
    TextView  welcome_student_tv;
    Typeface font_medium;
    ImageView profile_img;*/


    Toolbar toolbar;
    SharedPreferences sp;
    GridView gridview;
    TextView logout_tv;
    private ConnectionDetector connectionDetector;
    String TAG = "DashBoardStudentActivity";
    ScreenAdapter screenAdapter;
    ArrayList<HashMap<String, String>> screenList = new ArrayList<HashMap<String, String>>();
    List<String> screen_id_list = new ArrayList<String>();
    List<String> screens_to_show_list = new ArrayList<String>();
    private DBManager db;
    String ARRAY_DIVIDER = "#a1r2ra5yd2iv1i9der";




    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_student);
        init();


    }

    private void init() {
        connectionDetector = new ConnectionDetector(getApplicationContext());
        db = DBManager.getInstance();

        gridview = (GridView) findViewById(R.id.gridview);
        logout_tv = (TextView) findViewById(R.id.logout_tv);

        logout_tv.setOnClickListener(this);

        HashMap<String, String> map = null;
        try {

            Log.e(TAG, "user_type_id=> " + Constant.USER_TYPE);
            map = db.getParticularRow(getApplicationContext(), DBManager.TBL_LP_USER_SCREEN_MAP, "user_type_id", "" + Constant.USER_TYPE, "", "");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (map.size() == 0) {
            if (connectionDetector.isConnectingToInternet()) {
                ScreenMasterRequest request = new ScreenMasterRequest(this, Constant.USER_TYPE, this);
                request.hitViewUrlRequest();
            } else {
                Toast.makeText(getApplicationContext(), "Internet not available", Toast.LENGTH_SHORT).show();
            }
        } else {
            FetchFromDB();

        }

    }

    private void FetchFromDB() {

        HashMap<String, String> map_user = null;
        try {
            map_user = db.getParticularRow(getApplicationContext(), DBManager.TBL_LP_USER_SCREEN_MAP, "user_type_id", "" + Constant.USER_TYPE, "", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String screen_ids_to_show = map_user.get("screen_id");

        Log.e(TAG, "screen_ids_to_show==> " + screen_ids_to_show);


        screens_to_show_list = Arrays.asList(screen_ids_to_show.split("\\s*,\\s*"));
        Log.e(TAG, "screens_id_list==> " + screens_to_show_list);


        ArrayList<Object> objectArrayList = DBManager.getInstance().getAllTableDataTest(getApplicationContext(), DBManager.TBL_LP_SCREEN_MASTER, "screen_id", screen_ids_to_show, "", "");
        Log.e(TAG, "screenlist size==> " + objectArrayList.size());
        if (objectArrayList.size() == 0) {
            Toast.makeText(getApplicationContext(), "No data available in database. Please enable your internet to get the data directly from the server.", Toast.LENGTH_LONG).show();
        } else {
            screenList.clear();
            for (Object o : objectArrayList) {

                HashMap<String, String> map = new HashMap<String, String>();
                ScreenMasterModel testModel = (ScreenMasterModel) o;

                if (testModel.getPartner_id() == 0) {
                    map.put("partner_id", String.valueOf(testModel.getPartner_id()));
                    map.put("screen_id", String.valueOf(testModel.getScreen_id()));
                    map.put("screen_name", "" + testModel.getScreen_name());
                    map.put("web_url", "" + testModel.getWeb_url());
                    map.put("icon_image_name", "" + testModel.getIcon_image_name());
                    map.put("icon_path", "" + testModel.getIcon_path());

                    screenList.add(map);
                }

            }

            screenAdapter = new ScreenAdapter(getApplicationContext(), screenList);
            gridview.setAdapter(screenAdapter);
            screenAdapter.setClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {

        if(v== logout_tv){
            Utility.showLogoutDialog(this);
        }

    }

    @Override
    public void onResponse(Object obj) {
        Log.e(TAG, "obj==> " + obj);
        if (obj instanceof ActiveModel) {

            ActiveModel Activemodel = (ActiveModel) obj;

            if (Activemodel.getIsSuccess().equalsIgnoreCase("true")) {

                final List<ActiveModel.MyUrlBean> screenList = Activemodel.getMyUrl();

                final ScreenMasterModel model = new ScreenMasterModel();

                String[] screenArray = new String[screenList.size()];

                for (int i = 0; i < screenList.size(); i++) {
                    ActiveModel.MyUrlBean urlBean = screenList.get(i);

                    model.setPartner_id(Integer.parseInt(urlBean.getParent_id()));
                    model.setScreen_id(urlBean.getScreen_id());
                    model.setScreen_name(urlBean.getScreen_name());
                    model.setWeb_url(urlBean.getWeb_url());
                    model.setIcon_image_name(urlBean.getIcon_image_name());
                    model.setIcon_path(urlBean.getIcon_path());

                    screen_id_list.add(String.valueOf(urlBean.getScreen_id()));

                    screenArray[i] = String.valueOf(urlBean.getScreen_id());

                    HashMap<String, String> map = null;
                    try {
                        map = db.getParticularRow(getApplicationContext(), DBManager.TBL_LP_SCREEN_MASTER, "screen_id", "" + urlBean.getScreen_id(), "", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (map.size() == 0) {
                        db.insertTables(db.TBL_LP_SCREEN_MASTER, model);


                    } else {
                        db.deleteTestRow(getApplicationContext(), DBManager.TBL_LP_SCREEN_MASTER,
                                "screen_id", "" + urlBean.getScreen_id());
                        db.insertTables(db.TBL_LP_SCREEN_MASTER, model);
                    }

                }

                ScreenUserMapping(screenArray);

                FetchFromDB();


                Log.e(TAG, "getMessage==> " +  Activemodel.getMessage());
                //Toast.makeText(getActivity(), Activemodel.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), Activemodel.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void ScreenUserMapping(String[] arr) {

        final UserScreenMapModel model = new UserScreenMapModel();

        String screen_ids = android.text.TextUtils.join(",", screen_id_list);

        Log.e(TAG, "screen_ids==> " + screen_ids);

        model.setScreen_id(screen_ids);
        model.setUser_type_id(Integer.parseInt(Constant.USER_TYPE));


        HashMap<String, String> map = null;
        try {

            Log.e(TAG, "user_type_id=> " + Constant.USER_TYPE);
            map = db.getParticularRow(getApplicationContext(), DBManager.TBL_LP_USER_SCREEN_MAP, "user_type_id", "" + Constant.USER_TYPE, "", "");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (map.size() == 0) {
            db.insertTables(db.TBL_LP_USER_SCREEN_MAP, model);


        } else {
            db.deleteTestRow(getApplicationContext(), DBManager.TBL_LP_USER_SCREEN_MAP,
                    "user_type_id", "" + Constant.USER_TYPE);
            db.insertTables(db.TBL_LP_USER_SCREEN_MAP, model);
        }
    }

    @Override
    public void onItemClick(View view, int position) {

        Constant.SCREEN_ID = screenList.get(position).get("screen_id");
        Constant.SCREEN_NAME = screenList.get(position).get("screen_name");

System.out.println("BMI 2");
        Intent i = new Intent(getApplicationContext(), ShowSubScreenActivity.class);
        startActivity(i);

    }

/*
    private void init() {
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        Constant.SCHOOL_NAME = sp.getString("school_name","");
        Constant.SCHOOL_ID = sp.getString("school_id","");


        welcome_student_tv=(TextView)findViewById(R.id.welcome_student_tv);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        profile_img = (ImageView) toolbar.findViewById(R.id.profile_img);
        insights_layout=(LinearLayout)findViewById(R.id.insights_layout);
        top_sports_layout=(LinearLayout)findViewById(R.id.top_sports_layout);
        shape_365_layout=(LinearLayout)findViewById(R.id.shape_365_layout);
        utilities_layout=(LinearLayout)findViewById(R.id.utilities_layout);
        gallery_layout=(LinearLayout)findViewById(R.id.gallery_layout);


        profile_img.setOnClickListener(this);
        insights_layout.setOnClickListener(this);
        top_sports_layout.setOnClickListener(this);
        shape_365_layout.setOnClickListener(this);
        utilities_layout.setOnClickListener(this);
        gallery_layout.setOnClickListener(this);

        font_medium = Typeface.createFromAsset(getAssets(),
                "fonts/Roboto-Medium" +
                        "_0.ttf");
        welcome_student_tv.setTypeface(font_medium);

        welcome_student_tv.setText(getString(R.string.welcome) + " "+sp.getString("student_name", "ABC"));
        Constant.END_URL="?UserName="+sp.getString("test_coordinator_name", "")+"&Password="+sp.getString("test_coordinator_password", "");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.profile_img:

                Intent i = new Intent(DashBoardStudentActivity.this, ProfileStudentActivity.class);
                startActivity(i);
                break;


            case R.id.school_tv:

                Intent i1 = new Intent(DashBoardStudentActivity.this, SchoolActivity.class);
                startActivity(i1);

                break;


            case R.id. insights_layout:
                String insights[]={getString(R.string.school_performance),getString(R.string.fitness_dashboard),getString(R.string.classwise_performance),getString(R.string.latest_student_report),getString(R.string.top_performers),getString(R.string.consistent_performers)};
                showDialogInsightsList(this,insights);

                break;

            case R.id.top_sports_layout:
                String top_sports[]={getString(R.string.throw_ball),getString(R.string.kabaddi),getString(R.string.football),getString(R.string.kho_kho),getString(R.string.hand_ball),getString(R.string.cricket),getString(R.string.basket_ball),getString(R.string.volley_ball),getString(R.string.athletics),getString(R.string.gymnastics_aerobics),getString(R.string.badminton),getString(R.string.squash)};
                showDialogSportsList(this,top_sports);
                break;

            case R.id.shape_365_layout:
                Constant.TITLE = "SHAPE 365";
                Intent i2 = new Intent(this, WebViewActivity.class);
                Constant.WEBVIEW_URL = "http://staging.fitness365.me/Shape365.aspx"+Constant.END_URL;
                startActivity(i2);
                break;

            case R.id.utilities_layout:
                String utilities[]={getString(R.string.utility_parents),getString(R.string.utility_children)};
                showDialogUtilitiesList(this,utilities);

                break;


            case R.id.gallery_layout:
                Constant.TITLE = "Gallery";
                Intent i3 = new Intent(this, WebViewActivity.class);
                Constant.WEBVIEW_URL = "http://staging.fitness365.me/Gallery.aspx"+Constant.END_URL;
                startActivity(i3);
                break;
        }
    }


    private void showDialogInsightsList(Context context, final String[] items){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Constant.TITLE = items[which];
                Intent i = new Intent(DashBoardStudentActivity.this, WebViewActivity.class);
                switch (which) {
                    case 0: Constant.WEBVIEW_URL = "http://staging.fitness365.me/Fitness365SchoolDashboard.aspx"+Constant.END_URL;
                        startActivity(i);
                        break;

                    case 1: Constant.WEBVIEW_URL = "http://staging.fitness365.me/MyhomePage.aspx"+Constant.END_URL;
                        startActivity(i);
                        break;

                    case 2: Constant.WEBVIEW_URL = "http://staging.fitness365.me/ClasswiseDashboard.aspx"+Constant.END_URL;
                        startActivity(i);
                        break;

                    case 3: Constant.WEBVIEW_URL = "http://staging.fitness365.me/StudentReport.aspx"+Constant.END_URL;
                        startActivity(i);
                        break;

                    case 4: Constant.WEBVIEW_URL = "http://staging.fitness365.me/Toppers.aspx"+Constant.END_URL;
                        startActivity(i);
                        break;

                    case 5: Constant.WEBVIEW_URL = "http://staging.fitness365.me/Topconsisientperformers.aspx"+Constant.END_URL;
                        startActivity(i);
                        break;
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showDialogUtilitiesList(Context context, final String[] items){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Constant.TITLE = items[which];
                Intent i = new Intent(DashBoardStudentActivity.this, WebViewActivity.class);

                switch (which) {
                    case 0:  Constant.WEBVIEW_URL = "http://staging.fitness365.me/DisplayWidgetsTitles_Parents.aspx"+Constant.END_URL;
                        startActivity(i);
                        break;

                    case 1:
                        Constant.WEBVIEW_URL = "http://staging.fitness365.me/DisplayWidgetsTitles_Students.aspx"+Constant.END_URL;
                        startActivity(i);
                        break;
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void showDialogSportsList(Context context,final String[] items){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        alertDialog.setCancelable(false);

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Constant.TITLE = items[which];
                Intent i = new Intent(DashBoardStudentActivity.this, WebViewActivity.class);

                switch (which) {
                    case 0: Constant.WEBVIEW_URL = "http://staging.fitness365.me/Top_Sports.aspx?id=1&sports=Throw%20Ball"+Constant.END_URL;
                        startActivity(i);
                        break;

                    case 1:  Constant.WEBVIEW_URL = "http://staging.fitness365.me/Top_Sports.aspx?id=2&sports=Kabaddi"+Constant.END_URL;
                        startActivity(i);
                        break;

                    case 2:  Constant.WEBVIEW_URL = "http://staging.fitness365.me/Top_Sports.aspx?id=3&sports=Football"+Constant.END_URL;
                        startActivity(i);
                        break;

                    case 3:  Constant.WEBVIEW_URL = "http://staging.fitness365.me/Top_Sports.aspx?id=4&sports=Kho%20Kho"+Constant.END_URL;
                        startActivity(i);
                        break;

                    case 4:  Constant.WEBVIEW_URL = "http://staging.fitness365.me/Top_Sports.aspx?id=5&sports=Handball"+Constant.END_URL;
                        startActivity(i);
                        break;

                    case 5:  Constant.WEBVIEW_URL = "http://staging.fitness365.me/Top_Sports.aspx?id=6&sports=Cricket"+Constant.END_URL;
                        startActivity(i);
                        break;

                    case 6:  Constant.WEBVIEW_URL = "http://staging.fitness365.meTop_Sports.aspx?id=7&sports=Basketball"+Constant.END_URL;
                        startActivity(i);
                        break;

                    case 7:  Constant.WEBVIEW_URL = "http://staging.fitness365.me/Top_Sports.aspx?id=8&sports=Volleyball"+Constant.END_URL;
                        startActivity(i);
                        break;

                    case 8: Constant.WEBVIEW_URL = "http://staging.fitness365.me/Top_Sports.aspx?id=9&sports=Athletics"+Constant.END_URL;
                        startActivity(i);
                        break;

                    case 9:  Constant.WEBVIEW_URL = "http://staging.fitness365.me/Top_Sports.aspx?id=10&sports=Gymnastics/Aerobics"+Constant.END_URL;
                        startActivity(i);
                        break;

                    case 10:  Constant.WEBVIEW_URL = "http://staging.fitness365.me/Top_Sports.aspx?id=13&sports=Badminton"+Constant.END_URL;
                        startActivity(i);
                        break;

                    case 11:  Constant.WEBVIEW_URL = "http://staging.fitness365.me/Top_Sports.aspx?id=15&sports=Squash"+Constant.END_URL;
                        startActivity(i);
                        break;


                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }*/

}
