package kheloindia.com.assessment.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import kheloindia.com.assessment.ShowSubScreenActivity;
import kheloindia.com.assessment.adapter.ScreenAdapter;
import kheloindia.com.assessment.functions.Constant;
import kheloindia.com.assessment.model.ActiveModel;
import kheloindia.com.assessment.model.ScreenMasterModel;
import kheloindia.com.assessment.model.UserScreenMapModel;
import kheloindia.com.assessment.util.ConnectionDetector;
import kheloindia.com.assessment.util.DBManager;
import kheloindia.com.assessment.util.ItemClickListener;
import kheloindia.com.assessment.util.ResponseListener;
import kheloindia.com.assessment.webservice.ScreenMasterRequest;
import kheloindia.com.assessment.R;


/**
 * Created by PC10 on 15-Mar-18.
 */

public class ActiveConnectFragment extends Fragment implements View.OnClickListener,
        ResponseListener, ItemClickListener {

    View rootView;
    GridView gridview;
    private ConnectionDetector connectionDetector;
    String TAG = "ActiveConnectFragment";
    ScreenAdapter screenAdapter;
    ArrayList<HashMap<String, String>> screenList = new ArrayList<HashMap<String, String>>();
    List<String> screen_id_list = new ArrayList<String>();
    List<String> screens_to_show_list = new ArrayList<String>();
    private DBManager db;
    String ARRAY_DIVIDER = "#a1r2ra5yd2iv1i9der";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.activity_active_connect, container, false);


        init();

        return rootView;
    }

    private void init() {
        connectionDetector = new ConnectionDetector(getActivity());
        db = DBManager.getInstance();

        gridview = (GridView) rootView.findViewById(R.id.gridview);

        HashMap<String, String> map = null;
        try {

            Log.e(TAG, "user_type_id=> " + Constant.USER_TYPE);
            map = db.getParticularRow(getActivity(), DBManager.TBL_LP_USER_SCREEN_MAP, "user_type_id", "" + Constant.USER_TYPE, "", "");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (map.size() == 0) {
            if (connectionDetector.isConnectingToInternet()) {
                ScreenMasterRequest request = new ScreenMasterRequest(getActivity(), Constant.USER_TYPE, this);
                request.hitViewUrlRequest();
            } else {
                Toast.makeText(getActivity(), "Internet not available", Toast.LENGTH_SHORT).show();
            }
        } else {
            FetchFromDB();

        }

    }


    @Override
    public void onClick(View v) {

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
                        map = db.getParticularRow(getActivity(), DBManager.TBL_LP_SCREEN_MASTER, "screen_id", "" + urlBean.getScreen_id(), "", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (map.size() == 0) {
                        db.insertTables(db.TBL_LP_SCREEN_MASTER, model);


                    } else {
                        db.deleteTestRow(getActivity(), DBManager.TBL_LP_SCREEN_MASTER,
                                "screen_id", "" + urlBean.getScreen_id());
                        db.insertTables(db.TBL_LP_SCREEN_MASTER, model);
                    }

                }

                ScreenUserMapping(screenArray);

                FetchFromDB();


                Log.e(TAG, "getMessage==> " +  Activemodel.getMessage());
                //Toast.makeText(getActivity(), Activemodel.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), Activemodel.getMessage(), Toast.LENGTH_SHORT).show();
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
            map = db.getParticularRow(getActivity(), DBManager.TBL_LP_USER_SCREEN_MAP, "user_type_id", "" + Constant.USER_TYPE, "", "");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (map.size() == 0) {
            db.insertTables(db.TBL_LP_USER_SCREEN_MAP, model);


        } else {
            db.deleteTestRow(getActivity(), DBManager.TBL_LP_USER_SCREEN_MAP,
                    "user_type_id", "" + Constant.USER_TYPE);
            db.insertTables(db.TBL_LP_USER_SCREEN_MAP, model);
        }
    }


    private void FetchFromDB() {

        HashMap<String, String> map_user = null;
        try {
            map_user = db.getParticularRow(getActivity(), DBManager.TBL_LP_USER_SCREEN_MAP, "user_type_id", "" + Constant.USER_TYPE, "", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String screen_ids_to_show = map_user.get("screen_id");

        Log.e(TAG, "screen_ids_to_show==> " + screen_ids_to_show);


        screens_to_show_list = Arrays.asList(screen_ids_to_show.split("\\s*,\\s*"));
        Log.e(TAG, "screens_id_list==> " + screens_to_show_list);


        ArrayList<Object> objectArrayList = DBManager.getInstance().getAllTableDataTest(getActivity(), DBManager.TBL_LP_SCREEN_MASTER, "screen_id", screen_ids_to_show, "", "");
        Log.e(TAG, "screenlist size==> " + objectArrayList.size());
        if (objectArrayList.size() == 0) {
            Toast.makeText(getActivity(), "No data available in database. Please enable your internet to get the data directly from the server.", Toast.LENGTH_LONG).show();
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

            screenAdapter = new ScreenAdapter(getActivity(), screenList);
            gridview.setAdapter(screenAdapter);
               screenAdapter.setClickListener(this);
        }

    }

    @Override
    public void onItemClick(View view, int position) {

        Constant.SCREEN_ID = screenList.get(position).get("screen_id");
        Constant.SCREEN_NAME = screenList.get(position).get("screen_name");


        Intent i = new Intent(getActivity(), ShowSubScreenActivity.class);
        startActivity(i);

    }

}
