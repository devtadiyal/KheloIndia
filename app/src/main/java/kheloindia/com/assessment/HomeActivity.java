package kheloindia.com.assessment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import java.util.ArrayList;

import kheloindia.com.assessment.adapter.SchoolListAdapter;


/**
 * Created by PC10 on 05/08/2017.
 */

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    ImageView logout_img;
    RecyclerView recycler_view;
    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<String> schoolList = new ArrayList<String>();
    SchoolListAdapter sclAdapter;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        init();
    }

    private void init() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        logout_img = (ImageView) toolbar.findViewById(R.id.logout_img);
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);

        mLayoutManager= new LinearLayoutManager(HomeActivity.this);                // Creating a layout Manager
        recycler_view.setLayoutManager(mLayoutManager);

        recycler_view.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));

        recycler_view.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


        schoolList.add("Rabindranath World School");
        schoolList.add("Salwan Public School");
        schoolList.add("St. Xavier convent");
        schoolList.add("Sacred Heart School");
        schoolList.add("DAV Public School");


        sclAdapter = new SchoolListAdapter(HomeActivity.this, schoolList);
        recycler_view.setAdapter(sclAdapter);


        logout_img.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==logout_img){
            showLogoutDialog();
        }
    }

    public void showLogoutDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity.this);

        alertDialog.setMessage("Are you sure you want to Logout?");
alertDialog.setCancelable(false);
        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.alarm);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {

                dialog.cancel();
                SharedPreferences sp = PreferenceManager
                        .getDefaultSharedPreferences(getApplicationContext());
                sp.edit().clear().commit();
                Intent i = new Intent(HomeActivity.this, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    public void onBackPressed() {
        //  super.onBackPressed();
        moveTaskToBack(true);

    }
}
