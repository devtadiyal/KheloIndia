package kheloindia.com.assessment;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import kheloindia.com.assessment.adapter.SubScreenAdapter;
import kheloindia.com.assessment.model.ScreenMasterModel;
import kheloindia.com.assessment.util.DBManager;
import kheloindia.com.assessment.util.ItemClickListener;

import kheloindia.com.assessment.functions.Constant;

/**
 * Created by PC10 on 16-Mar-18.
 */

public class ShowSubScreenActivity extends AppCompatActivity implements View.OnClickListener,
        ItemClickListener {

    RecyclerView recycler_view;
    ArrayList<HashMap<String,String>> subScreenList = new ArrayList<HashMap<String,String>>();
    SubScreenAdapter scAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    String TAG = "ShowSubScreenActivity";
    Toolbar toolbar;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sub_screen_fragment);

        init();
    }

    private void init() {
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle(Constant.SCREEN_NAME);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        mLayoutManager= new LinearLayoutManager(this);                // Creating a layout Manager
        recycler_view.setLayoutManager(mLayoutManager);

        recycler_view.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));

        recycler_view.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


        FetchList();

    }

    private void FetchList() {
        ArrayList<Object> objectArrayList = DBManager.getInstance().getAllTableDataTest(this, DBManager.TBL_LP_SCREEN_MASTER, "parent_id", Constant.SCREEN_ID, "", "");
        Log.e(TAG, "screenlist size==> " + objectArrayList.size());
        if (objectArrayList.size() == 0) {
            Toast.makeText(this, "No data available in database. Please enable your internet to get the data directly from the server.", Toast.LENGTH_LONG).show();
        } else {
            subScreenList.clear();
            for (Object o : objectArrayList) {

                HashMap<String, String> map = new HashMap<String, String>();
                ScreenMasterModel testModel = (ScreenMasterModel) o;

                map.put("partner_id", String.valueOf(testModel.getPartner_id()));
                map.put("screen_id", String.valueOf(testModel.getScreen_id()));
                map.put("screen_name", "" + testModel.getScreen_name());
                map.put("web_url", "" + testModel.getWeb_url());

                subScreenList.add(map);


            }


            Log.e(TAG,"subScreenList=> " +subScreenList);
            scAdapter = new SubScreenAdapter(this, subScreenList);
            recycler_view.setAdapter(scAdapter);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(View view, int position) {

    }
}
