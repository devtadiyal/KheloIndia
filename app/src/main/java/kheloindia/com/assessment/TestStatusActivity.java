package kheloindia.com.assessment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


import kheloindia.com.assessment.model.CampTestMapping;
import kheloindia.com.assessment.model.FitnessTestCategoryModel;
import kheloindia.com.assessment.model.FitnessTestTypesModel;
import kheloindia.com.assessment.model.ReportModel;
import kheloindia.com.assessment.model.StudentMasterModel;
import kheloindia.com.assessment.model.TestReportModel;
import kheloindia.com.assessment.util.AppConfig;
import kheloindia.com.assessment.util.CSVWriter;
import kheloindia.com.assessment.util.DBManager;
import kheloindia.com.assessment.util.DownloadDataToExport;
import kheloindia.com.assessment.util.MultiSpinner;
import kheloindia.com.assessment.util.ProgressDialogUtility;
import kheloindia.com.assessment.util.ResponseListener;
import kheloindia.com.assessment.util.Utility;

import kheloindia.com.assessment.functions.Constant;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


/**
 * Created by CT13 on 2017-05-15.
 */

public class TestStatusActivity extends AppCompatActivity implements View.OnClickListener, ResponseListener, MultiSpinner.MultiSpinnerListener, RadioGroup.OnCheckedChangeListener {

    private MultiSpinner class_spin, test_spin;
    private AppCompatRadioButton all_btn, incomplete_btn, complete_btn;
    private Button search_btn, csv_btn, download_xls_btn;
    private Toolbar toolbar;
    private ArrayList<String> test_spin_data = new ArrayList<>();
    private HashMap<String, String> class_ids = new HashMap<>();
    private HashMap<String, String> test_ids = new HashMap<>();
    private ArrayList<String> class_spin_data = new ArrayList<String>();

    private Intent in;
    private ArrayList<String> statusList, statusIDList;
    private boolean validationSuccess;
    private TextView school_tv, exportDataLabel;
    private ArrayList<Object> studentList;
    private HashMap<String, ArrayList<String>> campTestiDList;
    private ArrayList<Object> testList;
    private boolean all, complete;
    private String[] selected_class_text;
    private String[] selected_test_text;
    private ArrayList<String> filteredTestList = new ArrayList<>();
    private RadioGroup status_rg_grp;
    //    private ImageView download_data_to_export_iv;
    private ProgressDialogUtility dialogUtility;
    String forReTest, testTableName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_status_screen);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        init();
    }


    private void init() {
        dialogUtility = new ProgressDialogUtility(this);
        dialogUtility.setMessage(getString(R.string.progress_loader));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        Utility.showActionBar(this, toolbar, "");
        TextView title = toolbar.findViewById(R.id.toolbar_title);
        class_spin = (MultiSpinner) findViewById(R.id.class_spin);
        test_spin = (MultiSpinner) findViewById(R.id.test_spin);
        all_btn = (AppCompatRadioButton) findViewById(R.id.all_btn);
        incomplete_btn = (AppCompatRadioButton) findViewById(R.id.incomplete_btn);
        complete_btn = (AppCompatRadioButton) findViewById(R.id.complete_bn);
        search_btn = (Button) findViewById(R.id.search_btn);
        school_tv = (TextView) findViewById(R.id.school_tv);
        csv_btn = (Button) findViewById(R.id.csv_btn);
        status_rg_grp = (RadioGroup) findViewById(R.id.status_rg_grp);
        download_xls_btn = (Button) findViewById(R.id.download_xls_btn);
        exportDataLabel = (TextView) findViewById(R.id.export_data_label);

        // all_btn.setBackgroundResource(R.drawable.reset_button);
        Typeface font_semi_bold = Typeface.createFromAsset(getAssets(),
                "fonts/Barlow-SemiBold.ttf");
        Typeface font_reg = Typeface.createFromAsset(getAssets(),
                "fonts/Barlow-Regular.ttf");
        search_btn.setTypeface(font_semi_bold);
        csv_btn.setTypeface(font_semi_bold);
        all_btn.setTypeface(font_reg);
        incomplete_btn.setTypeface(font_reg);
        complete_btn.setTypeface(font_reg);
        school_tv.setTypeface(font_reg);
        title.setText(getString(R.string.test_status));
        title.setTypeface(font_reg);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        forReTest = sp.getString(AppConfig.FOR_RETEST, "0");
        testTableName = DBManager.TBL_LP_FITNESS_TEST_RESULT;
        if (forReTest.equalsIgnoreCase("1"))
            testTableName = DBManager.TBL_LP_FITNESS_RETEST_RESULT;


        Constant.statusIDAllList.clear();
        statusList = new ArrayList<>();
//        statusList.add(0, "");
//        statusList.add(1, "");
//        statusList.add(2, all_btn.getText().toString());

        statusIDList = new ArrayList<String>();
//        statusIDList.add(0, "");
//        statusIDList.add(1, "");
//        statusIDList.add(2, "0");

        school_tv.setText(Constant.SCHOOL_NAME);
//        all_btn.setBackgroundResource(R.drawable.reset_button);
//        all_btn.setOnClickListener(this);
//        incomplete_btn.setOnClickListener(this);
//        complete_btn.setOnClickListener(this);
        search_btn.setOnClickListener(this);
        csv_btn.setOnClickListener(this);
        download_xls_btn.setOnClickListener(this);
        exportDataLabel.setOnClickListener(this);
        status_rg_grp.setOnCheckedChangeListener(this);
        all_btn.setChecked(true);


        int cut_off = Integer.parseInt(Constant.SENIORS_STARTING_FROM);

        if (Constant.seniorList.isEmpty()) {
            for (int j = cut_off; j <= 12; j++) {

                Constant.seniorList.add(String.valueOf(j));
            }
        }

        ArrayList<Object> arrayCampList = DBManager.getInstance().getAllTableData(TestStatusActivity.this, DBManager.TBL_LP_CAMP_TEST_MAPPING, "camp_id", Constant.CAMP_ID, "school_id", Constant.SCHOOL_ID);

        campTestiDList = new HashMap<>();
        String class_idss;
        List<String> class_ids;
        ArrayList<String> class_list;
        for (int i = 0; i < arrayCampList.size(); i++) {
            CampTestMapping campTestMapping = (CampTestMapping) arrayCampList.get(i);
            class_list = new ArrayList<>();
            class_idss = campTestMapping.getClass_ID();
            class_ids = Arrays.asList(class_idss.split("\\s*,\\s*"));
            for (int j = 0; j < class_ids.size(); j++) {
                class_list.add("" + class_ids.get(j));
            }
            campTestiDList.put("" + campTestMapping.getTest_type_id(), class_list);

        }

        if (Constant.classList.size() == 0 || Constant.testList.size() == 0) {
            new LoadClassesAndTests().execute();
        } else {
            //   int studentListSize=  DBManager.getInstance().getTotalCount(TestStatusActivity.this, DBManager.TBL_LP_STUDENT_MASTER, "current_school_id", Constant.SCHOOL_ID, "", "");
            ArrayAdapter class_spin_array = new ArrayAdapter<String>(TestStatusActivity.this, android.R.layout.simple_spinner_item, Constant.classList);
            class_spin.setTitle(getString(R.string.select_class));
            class_spin.setDefaultText(getString(R.string.all));
            class_spin.setAllText(getString(R.string.all));
            class_spin.setAdapter(class_spin_array, false, this);
            ArrayAdapter test_spin_array = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Constant.testList);

            test_spin.setTitle(getString(R.string.select_test));
            test_spin.setDefaultText(getString(R.string.all));
            test_spin.setAllText(getString(R.string.all));
            test_spin.setAdapter(test_spin_array, false, new MultiSpinner.MultiSpinnerListener() {
                @Override
                public void onItemsSelected(boolean[] selected) {

                }
            });

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

//            case R.id.all_btn:
//                all_btn.setBackgroundResource(R.drawable.reset_button);
//                incomplete_btn.setBackgroundResource(android.R.color.transparent);
//                complete_btn.setBackgroundResource(android.R.color.transparent);
//                all_btn.setTextColor(getResources().getColor(R.color.white));
//                incomplete_btn.setTextColor(getResources().getColor(R.color.grey));
//                complete_btn.setTextColor(getResources().getColor(R.color.grey));
//                all=true;
//                complete=false;
////                statusList.set(2, all_btn.getText().toString());
////                statusIDList.set(2, "0");
//
//                break;
//
//            case R.id.incomplete_btn:
//                all_btn.setBackgroundResource(android.R.color.transparent);
//                incomplete_btn.setBackgroundResource(R.drawable.reset_button);
//                complete_btn.setBackgroundResource(android.R.color.transparent);
//                all_btn.setTextColor(getResources().getColor(R.color.grey));
//                incomplete_btn.setTextColor(getResources().getColor(R.color.white));
//                complete_btn.setTextColor(getResources().getColor(R.color.grey));
////                statusList.set(2, incomplete_btn.getText().toString());
////                statusIDList.set(2, "2");
//                all=false;
//                complete=false;
//
//                break;
//
//            case R.id.complete_btn:
//                all_btn.setBackgroundResource(android.R.color.transparent);
//                incomplete_btn.setBackgroundResource(android.R.color.transparent);
//                complete_btn.setBackgroundResource(R.drawable.reset_button);
//                all_btn.setTextColor(getResources().getColor(R.color.grey));
//                incomplete_btn.setTextColor(getResources().getColor(R.color.grey));
//                complete_btn.setTextColor(getResources().getColor(R.color.white));
////                statusList.set(2, complete_btn.getText().toString());
////                statusIDList.set(2, "1");
//                all=false;
//                complete=true;
//
//                break;

            case R.id.search_btn:
//               if (validate()) {
//                    statusList.set(0,class_spin.getText().toString().replaceAll(", ",  "  AND "));
//                    statusList.set(1, test_spin.getText().toString().replaceAll(", ",  "  AND "));
                selected_class_text = class_spin.getText().toString().split(", ");
                selected_test_text = test_spin.getText().toString().split(", ");
                System.out.println("VIEW1 "+selected_class_text+" "+selected_test_text);

                statusList.clear();
                statusIDList.clear();

                for (int i = 0; i < selected_class_text.length; i++) {
                    statusList.add(selected_class_text[i]);
                    if (!selected_class_text[i].equalsIgnoreCase("All")) {
                        statusIDList.add(Constant.classIdList.get(selected_class_text[i].substring(0, selected_class_text[i].indexOf(" "))));
                        System.out.println("VIEW2 "+statusIDList);

                    } else {
                        statusIDList.add(Constant.classIdList.get(selected_class_text[i]));
                        System.out.println("VIEW3 "+statusIDList);

                    }
                    //selected_class_id_text.append(i<selected_class_text.length-1?Constant.classIdList.get(selected_class_text[i])+"  AND ":Constant.classIdList.get(selected_class_text[i]));

                }
                for (int i = 0; i < selected_test_text.length; i++) {
                    // selected_test_id_text.append(i<selected_test_text.length-1?test_ids.get(selected_test_text[i])+"  AND ":test_ids.get(selected_test_text[i]));

                    statusList.add(selected_test_text[i]);
                    statusIDList.add(Constant.testIdList.get(selected_test_text[i]));
                    System.out.println("VIEW4 "+statusList+" ,,,  "+statusIDList);

                }


                if (all) {
                    statusList.add(all_btn.getText().toString());
                    statusIDList.add("0");
                    System.out.println("VIEW5 "+statusList);

                } else if (complete) {
                    statusList.add(statusList.size(), complete_btn.getText().toString());
                    statusIDList.add("1");
                    System.out.println("VIEW6 "+statusList);

                } else {
                    statusList.add(statusList.size(), incomplete_btn.getText().toString());
                    statusIDList.add("2");
                    System.out.println("VIEW7 "+statusList);

                }


                in = new Intent(TestStatusActivity.this, TestStatusReportActivity.class);
                in.putExtra("selected_class_length", selected_class_text.length);
                in.putStringArrayListExtra("status", statusList);
                in.putStringArrayListExtra("status_id", statusIDList);
                startActivity(in);

                // }
                break;

            case R.id.csv_btn:
                //if (validate()) {

                selected_class_text = class_spin.getText().toString().split(", ");
                selected_test_text = test_spin.getText().toString().split(", ");
                statusList.clear();
                statusIDList.clear();

                for (int i = 0; i < selected_class_text.length; i++) {
                    if (!selected_class_text[i].equalsIgnoreCase("All")) {
                        statusList.add(selected_class_text[i].substring(0, selected_class_text[i].indexOf(" ")));
                        statusIDList.add(Constant.classIdList.get(selected_class_text[i].substring(0, selected_class_text[i].indexOf(" "))));
                    } else {
                        statusList.add(selected_class_text[i]);
                        statusIDList.add(Constant.classIdList.get(selected_class_text[i]));
                    }
                }
                for (int i = 0; i < selected_test_text.length; i++) {
                    // selected_test_id_text.append(i<selected_test_text.length-1?test_ids.get(selected_test_text[i])+"  AND ":test_ids.get(selected_test_text[i]));
                    statusList.add(selected_test_text[i]);
                    statusIDList.add(Constant.testIdList.get(selected_test_text[i]));
                }


                if (all) {
                    statusList.add(all_btn.getText().toString());
                    statusIDList.add("0");
                } else if (complete) {
                    statusList.add(complete_btn.getText().toString());
                    statusIDList.add("1");
                } else {
                    statusList.add(incomplete_btn.getText().toString());
                    statusIDList.add("2");
                }

                checkPermissions(0);

//                if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE)
//                        != PackageManager.PERMISSION_GRANTED ||
//                        ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
//                    ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 0);
//             else   new ExportDatabaseCSVTaskOffline().execute();


                break;


            case R.id.download_xls_btn:
                //showExportDialog();
                checkPermissions(1);
                break;

            case R.id.export_data_label:
                showTooltip();
                break;

        }
    }


    private void showTooltip() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setCancelable(false);

        alertDialog.setMessage(getString(R.string.data_upload_tooltip_text));

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.alarm);

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });

        alertDialog.show();
    }

    private void checkPermissions(int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, requestCode);
            else {
                if (requestCode == 0)
                    new ExportDatabaseCSVTaskOffline().execute();
                else if (requestCode == 1)
                    checkRecordsAndDownloadDataXLS();
            }


//            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
//            }

        } else {
            if (requestCode == 0)
                new ExportDatabaseCSVTaskOffline().execute();
            else if (requestCode == 1)
                checkRecordsAndDownloadDataXLS();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//                new ExportDatabaseCSVTaskOffline().execute();
            if (requestCode == 0)
                new ExportDatabaseCSVTaskOffline().execute();
            else if (requestCode == 1)
                checkRecordsAndDownloadDataXLS();
        } else
            Utility.showPermissionDialog(this, getString(R.string.accept_permission, "Storage"));

    }

    private void checkRecordsAndDownloadDataXLS() {
        DBManager db = new DBManager();
        int exported_count = db.getTableCount(testTableName, AppConfig.SYNCED, "false");
        if (exported_count > 0) {
            //dialogUtility = new ProgressDialogUtility(this);
            new DownloadDataToExport(this, dialogUtility, testTableName).execute();
        } else
            Toast.makeText(this, "No Data available to export!", Toast.LENGTH_SHORT).show();
    }

//    private void showExportDialog(){
//        final Dialog openDialog = new Dialog(TestStatusActivity.this);
//        openDialog.setContentView(R.layout.dialog_data_to_export);
//
//        Button no_btn=openDialog.findViewById(R.id.no_btn);
//        Button yes_btn =openDialog.findViewById(R.id.yes_btn);
//
//        no_btn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                openDialog.dismiss();
//
//            }
//        });
//
//        yes_btn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                openDialog.dismiss();
//                if (ContextCompat.checkSelfPermission(TestStatusActivity.this, WRITE_EXTERNAL_STORAGE)
//                        != PackageManager.PERMISSION_GRANTED ||
//                        ContextCompat.checkSelfPermission(TestStatusActivity.this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
//                    ActivityCompat.requestPermissions(TestStatusActivity.this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 1);
//
//                else
//                    new DownloadDataToExport1(TestStatusActivity.this,true).execute();
//
//            }
//        });
//
//        openDialog.show();
//    }

    private boolean validate() {

        if (class_spin.getText().toString().equalsIgnoreCase(getString(R.string.select_class))) {
            validationSuccess = false;
            Toast.makeText(this, R.string.validate_class_drop_down, Toast.LENGTH_SHORT).show();
        } else if (test_spin.getText().toString().equalsIgnoreCase(getString(R.string.select_test))) {
            validationSuccess = false;
            Toast.makeText(this, R.string.validate_test_drop_down, Toast.LENGTH_SHORT).show();
        } else
            validationSuccess = true;

        return validationSuccess;

    }

    //    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        if (requestCode == 0) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//                new ExportDatabaseCSVTaskOffline().execute();
//            } else
//                Utility.showPermissionDialog(this,getString(R.string.accept_permission,"Storage"));
//
//        }else{
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//                new DownloadDataToExport1(TestStatusActivity.this,true).execute();
//            } else
//                Utility.showPermissionDialog(this,getString(R.string.accept_permission,"Storage"));
//        }
//    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Constant.statusIDAllList.size() != 0) {
//            if (Constant.testStatusIDAllList.get(0).equals("0")) {
//                class_spin.setText(getString(R.string.all));
//                statusList.set(0, "All");
//                statusIDList.set(0, "0");
//            }
//            if (Constant.testStatusIDAllList.get(1).equals("0")) {
//                test_spin.setText(getString(R.string.all));
//                statusList.set(1, "All");
//                statusIDList.set(1, "0");
//            }


            for (int i = 0; i < Constant.statusIDAllList.size() - 1; i++) {

                if (Constant.statusIDAllList.get(i).equalsIgnoreCase("0")) {
                    if (i < selected_class_text.length) {

                        if (!selected_class_text[i].equalsIgnoreCase("all"))
                            class_spin.getSelected()[Constant.classList.indexOf(selected_class_text[i])] = false;

                    } else {
                        if (!selected_test_text[i - selected_class_text.length].equalsIgnoreCase("all"))
                            test_spin.getSelected()[filteredTestList.size() == 0 ? Constant.testList.indexOf(selected_test_text[i - selected_class_text.length]) : filteredTestList.indexOf(selected_test_text[i - selected_class_text.length])] = false;


                    }

                }

            }

            class_spin.setSelected(class_spin.getSelected());
            test_spin.setSelected(test_spin.getSelected());

            if (Constant.statusIDAllList.get(Constant.statusIDAllList.size() - 1).equals("0")) {
//                all = true;
//                all_btn.setBackgroundResource(R.color.colorAccent);
//                incomplete_btn.setBackgroundResource(android.R.color.transparent);
//                complete_btn.setBackgroundResource(android.R.color.transparent);
//                all_btn.setTextColor(getResources().getColor(R.color.white));
//                incomplete_btn.setTextColor(getResources().getColor(R.color.grey));
//                complete_btn.setTextColor(getResources().getColor(R.color.grey));
//                all_btn.setHighlightColor(getResources().getColor(R.color.colorPrimary));
//                incomplete_btn.setHighlightColor(getResources().getColor(R.color.grey));
//                complete_btn.setHighlightColor(getResources().getColor(R.color.grey));
                all_btn.setChecked(true);

            }

        }

    }

    @Override
    public void onResponse(Object obj) {
        if (obj instanceof ReportModel) {
            new ExportDatabaseCSVTaskOnline().execute((ReportModel) obj);
        }
    }

    @Override
    public void onItemsSelected(boolean[] selected) {
        filterTestsOnClasses(selected);

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (radioGroup.getCheckedRadioButtonId()) {

            case R.id.all_btn:
//                all_btn.setBackgroundResource(R.drawable.reset_button);
//                incomplete_btn.setBackgroundResource(android.R.color.transparent);
//                complete_btn.setBackgroundResource(android.R.color.transparent);
//                all_btn.setTextColor(getResources().getColor(R.color.white));
//                incomplete_btn.setTextColor(getResources().getColor(R.color.grey));
//                complete_btn.setTextColor(getResources().getColor(R.color.grey));
//                all_btn.setHighlightColor(getResources().getColor(R.color.colorPrimary));
//                incomplete_btn.setHighlightColor(getResources().getColor(R.color.grey));
//                complete_btn.setHighlightColor(getResources().getColor(R.color.grey));

//                Utility.setAppCompatRadioButtonColor(all_btn,R.color.grey,R.color.colorPrimary);
//                Utility.setAppCompatRadioButtonColor(incomplete_btn,R.color.grey,R.color.colorPrimary);
//                Utility.setAppCompatRadioButtonColor(complete_btn,R.color.grey,R.color.colorPrimary);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    all_btn.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimary)));
                } else
                    all_btn.setHighlightColor(getResources().getColor(R.color.colorPrimary));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    incomplete_btn.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey)));
                } else
                    incomplete_btn.setHighlightColor(getResources().getColor(R.color.grey));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    complete_btn.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey)));
                } else
                    complete_btn.setHighlightColor(getResources().getColor(R.color.grey));
                all_btn.setTextColor(getResources().getColor(R.color.colorPrimary));
                incomplete_btn.setTextColor(getResources().getColor(R.color.grey));
                complete_btn.setTextColor(getResources().getColor(R.color.grey));
                all = true;
                complete = false;
//                statusList.set(2, all_btn.getText().toString());
//                statusIDList.set(2, "0");

                break;

            case R.id.incomplete_btn:
//                all_btn.setBackgroundResource(android.R.color.transparent);
//                incomplete_btn.setBackgroundResource(R.drawable.reset_button);
//                complete_btn.setBackgroundResource(android.R.color.transparent);
//                all_btn.setTextColor(getResources().getColor(R.color.grey));
//                incomplete_btn.setTextColor(getResources().getColor(R.color.white));
//                complete_btn.setTextColor(getResources().getColor(R.color.grey));
//                statusList.set(2, incomplete_btn.getText().toString());
//                statusIDList.set(2, "2");
//                all_btn.setHighlightColor(getResources().getColor(R.color.grey));
//                incomplete_btn.setHighlightColor(getResources().getColor(R.color.colorPrimary));
//                complete_btn.setHighlightColor(getResources().getColor(R.color.grey));

//                Utility.setAppCompatRadioButtonColor(all_btn,R.color.grey,R.color.colorPrimary);
//                Utility.setAppCompatRadioButtonColor(incomplete_btn,R.color.grey,R.color.colorPrimary);
//                Utility.setAppCompatRadioButtonColor(complete_btn,R.color.grey,R.color.colorPrimary);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    all_btn.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey)));
                } else
                    all_btn.setHighlightColor(getResources().getColor(R.color.grey));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    incomplete_btn.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimary)));
                } else
                    incomplete_btn.setHighlightColor(getResources().getColor(R.color.colorPrimary));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    complete_btn.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey)));
                } else
                    complete_btn.setHighlightColor(getResources().getColor(R.color.grey));
                all_btn.setTextColor(getResources().getColor(R.color.grey));
                incomplete_btn.setTextColor(getResources().getColor(R.color.colorPrimary));
                complete_btn.setTextColor(getResources().getColor(R.color.grey));

                all = false;
                complete = false;

                break;

            case R.id.complete_bn:
//                all_btn.setBackgroundResource(android.R.color.transparent);
//                incomplete_btn.setBackgroundResource(android.R.color.transparent);
//                complete_btn.setBackgroundResource(R.drawable.reset_button);
//                all_btn.setTextColor(getResources().getColor(R.color.grey));
//                incomplete_btn.setTextColor(getResources().getColor(R.color.grey));
//                complete_btn.setTextColor(getResources().getColor(R.color.white));
//                statusList.set(2, complete_btn.getText().toString());
//                statusIDList.set(2, "1");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    all_btn.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey)));
                } else
                    all_btn.setHighlightColor(getResources().getColor(R.color.grey));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    incomplete_btn.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey)));
                } else
                    incomplete_btn.setHighlightColor(getResources().getColor(R.color.grey));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    complete_btn.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimary)));
                } else

                    complete_btn.setHighlightColor(getResources().getColor(R.color.colorPrimary));
                all_btn.setTextColor(getResources().getColor(R.color.grey));
                incomplete_btn.setTextColor(getResources().getColor(R.color.grey));
                complete_btn.setTextColor(getResources().getColor(R.color.colorPrimary));
                all = false;
                complete = true;

                break;
        }

    }


    public class ExportDatabaseCSVTaskOnline extends AsyncTask<ReportModel, Void, Boolean> {
        //private final ProgressDialogUtility dialogUtility = new ProgressDialogUtility(TestStatusActivity.this);
        private String completed;

        @Override
        protected void onPreExecute() {
            dialogUtility.setMessage(getString(R.string.csv_message_loader));
            dialogUtility.showProgressDialog();
        }

        protected Boolean doInBackground(final ReportModel... args) {

            File exportDir = new File(Environment.getExternalStorageDirectory(), "");

            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }

            File file = new File(exportDir, "TestReportFile.csv");
            try {
                file.createNewFile();
                CSVWriter csvWrite = new CSVWriter(new FileWriter(file));


                String arrStrTitle[] = {"Student Name", "Roll Number", "Test Name", "Completed"};
                csvWrite.writeNext(arrStrTitle);

//

//                List<ReportModel.ResultBean> resultBeanList = args[0].getResult();
//                for (int i = 0; i < resultBeanList.size(); i++) {
//                    ReportModel.ResultBean resultBean = resultBeanList.get(i);
//
//                    for (int j = 0; j < resultBean.getTest().size(); j++) {
//                        ReportModel.ResultBean.TestBean testBean = resultBean.getTest().get(j);
//                        String arrSubStr[] = {resultBean.getStudentName(), resultBean.getStudentRegistrationNum(), testBean.getTestName(), testBean.getTestCompleted().equalsIgnoreCase("1") ? "Yes" : "No"};
//                        csvWrite.writeNext(arrSubStr);
//                    }
//
//                }


                csvWrite.close();
                Utility.openGmail(TestStatusActivity.this, file);

                return true;
            } catch (Exception e) {
                return false;
            }
        }


        protected void onPostExecute(final Boolean success) {
            dialogUtility.dismissProgressDialog();
            if (success) {
                Toast.makeText(TestStatusActivity.this, "Export successful!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(TestStatusActivity.this, "Export failed", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public class ExportDatabaseCSVTaskOffline extends AsyncTask<Void, Void, Boolean> {
        // private final ProgressDialogUtility dialogUtility = new ProgressDialogUtility(TestStatusActivity.this);
        private String completed;

        @Override
        protected void onPreExecute() {
            dialogUtility.setMessage("Exporting data please wait...");
            dialogUtility.showProgressDialog();
        }

        protected Boolean doInBackground(Void... params) {

            File exportDir = new File(Environment.getExternalStorageDirectory(), "");
            ArrayList title = new ArrayList<String>();
            title.add("Student Name");
            title.add("Class");
            title.add("Registration Number");


            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }

            File file = new File(exportDir, "TestReportFile.csv");
            try {
//                if(file.exists()){
//                    file.delete();
//                }
//            file.createNewFile();
                CSVWriter csvWrite = new CSVWriter(new FileWriter(file));

                StringBuilder class_buffer = new StringBuilder("");
                StringBuilder test_buffer = new StringBuilder("");

                for (int i = 0; i < selected_class_text.length; i++) {

                    class_buffer.append("'" + statusList.get(i) + "',");

                }

                for (int i = selected_class_text.length; i < statusIDList.size() - 1; i++) {

                    test_buffer.append("'" + statusIDList.get(i) + "',");
                }

                ArrayList<Object> arrayReportList = DBManager.getInstance().getTestReport(TestStatusActivity.this, Constant.CAMP_ID, Constant.SCHOOL_ID, test_buffer.toString().substring(0, test_buffer.length() - 1), class_buffer.toString().substring(0, class_buffer.length() - 1), statusIDList.get(statusIDList.size() - 1));
                //   String arrStrTitle[] = {"Student Name","Class", "Roll Number", "Test Name","Score"};

//                ArrayList<Object> arrayTestTypeList = DBManager.getInstance().getAllTableData(TestStatusActivity.this, DBManager.TBL_LP_FITNESS_TEST_TYPES, "test_type_id", test_buffer.toString().substring(0,test_buffer.length()-1), "", "");
//
//
//                for (int i = 0; i < arrayTestTypeList.size(); i++) {
//                    FitnessTestTypesModel temp = (FitnessTestTypesModel) arrayTestTypeList.get(i);
//
//                    ArrayList<Object> arrayTestCategoryList = DBManager.getInstance().getAllTableData(TestStatusActivity.this,
//                            DBManager.TBL_LP_FITNESS_TEST_CATEGORY, "test_id", "" + temp.getTest_type_id(), "", "");
//                    for (int i= 0; i < statusIDList.size(); i++) {
//                   //     FitnessTestCategoryModel tmp = (FitnessTestCategoryModel) arrayTestCategoryList.get(j);
//                        if (campTestiDList.containsKey(statusIDList.get(i))) {
//
//                            if (!statusIDList.get(selected_class_text.length-1).equals("0")) {
//                                for(int k=0;k<selected_class_text.length;k++){
//                                    if(!title.contains(statusList.get(i))) {
//                                       if(campTestiDList.get(statusIDList.get(i-selected_class_text.length)).contains(statusIDList.get(k))){
//                                       // if (tmp.getTest_applicable().equals("1") && !Constant.seniorList.contains(statusIDList.get(k))) {
//                                            title.add(temp.getTest_name() + " (" + tmp.getTest_name() + ")");
//
////                                        } else if (tmp.getTest_applicable().equals("2") && Constant.seniorList.contains(statusIDList.get(k))) {
////                                            title.add(temp.getTest_name() + " (" + tmp.getTest_name() + ")");
////
////                                        } else if (tmp.getTest_applicable().equals("3")) {
////                                            title.add(temp.getTest_name() + " (" + tmp.getTest_name() + ")");
////
////                                        }
//                                    }
//                                    }
//
//                                }
//
//
//                            } else {

                // }
                if (statusIDList.get(selected_class_text.length).equals("0")) {
                    if (filteredTestList.size() != 0) {
                        for (int i = 0; i < filteredTestList.size(); i++) {
                            title.add(filteredTestList.get(i));
                        }

                    } else {
                        for (int i = 0; i < Constant.testList.size(); i++) {
                            title.add(Constant.testList.get(i));
                        }

                    }

                } else {
                    for (int i = selected_class_text.length; i < statusList.size() - 1; i++) {
                        title.add(statusList.get(i));

                    }
                }


                String[] arrStrTitle = (String[]) title.toArray(new String[title.size()]);

                csvWrite.writeNext(arrStrTitle);

//                if(statusList.get(2).equalsIgnoreCase("incomplete"))
//                    completed="false";
//
//                else  if(statusList.get(2).equalsIgnoreCase("complete"))
//                    completed="true";
//                else
//                    completed="all";

                String test_score = "";
                String reg_num = "";
                // String roll_num = "";
                String class_id = "";
                String student_name = "";
                String student_class = "";

                ArrayList testScoresSubTitle = new ArrayList<String>();
                ArrayList subTitle = new ArrayList<String>();
                ArrayList testNames = new ArrayList<String>();
                HashMap testNameWithScore = new HashMap<String, String>();


                for (int i = 0; i < arrayReportList.size(); i++) {
                    TestReportModel testReportModel = (TestReportModel) arrayReportList.get(i);

                    if (testReportModel.getScore() != null) {
                        if (testReportModel.getTest_name().equalsIgnoreCase("agility") || testReportModel.getTest_name().equalsIgnoreCase("cardiovascular endurance") || testReportModel.getTest_name().equalsIgnoreCase("speed") || testReportModel.getTest_name().equalsIgnoreCase("coordination")) {

                            test_score = Utility.convertMilliSecondsToMiSecMs(TestStatusActivity.this, Long.parseLong(testReportModel.getScore()));
                        } else if (testReportModel.getSubTestName().equalsIgnoreCase("weight")) {
                            double score = (Double.parseDouble(testReportModel.getScore())) / 1000;
                            test_score = score + getResources().getString(R.string.kg);
                        } else if (testReportModel.getSubTestName().contains("Ruler") || testReportModel.getSubTestName().equalsIgnoreCase("height") || testReportModel.getTest_name().equalsIgnoreCase("flexibility") || testReportModel.getTest_name().equalsIgnoreCase("power")) {
                            double score = (Double.parseDouble(testReportModel.getScore())) / 10;
                            test_score = Utility.convertCentiMetreToMtCmMm(TestStatusActivity.this, score);
                        } else if (testReportModel.getTest_name().contains("Manipulative") || testReportModel.getTest_name().contains("Body Management") || testReportModel.getTest_name().contains("Locomotor")) {

                            test_score = testReportModel.getScore() + "|" + testReportModel.getTotal();

                        } else if (testReportModel.getSubTestName().contains("Flamingo") && testReportModel.getScore().equalsIgnoreCase("1000")) {
                            test_score = getString(R.string.disqualified);
                        } else {

                            test_score = testReportModel.getScore();

                        }
                    } else {
                        test_score = getResources().getString(R.string.nill_score_excel_sheet);
                    }


                    if (!testReportModel.getStudent_registration_num().equalsIgnoreCase(reg_num)) {

                        if (i != 0) {

                            // String arrSubStr[] = new String[]{student_name, student_class,roll_num, test_names, test_scores};
                            subTitle.add(student_name);
                            subTitle.add(student_class);
                            //subTitle.add(roll_num);
                            subTitle.add(reg_num);

                            for (int j = 3; j < title.size(); j++) {

                                if (testNames.contains(title.get(j)))
                                    testScoresSubTitle.add(testNameWithScore.get(title.get(j)));
//                                else if(Constant.seniorList.contains(class_id)&& Constant.juniorTestList.contains(title.get(j)))
//                                    testScoresSubTitle.add("N.A.");
//                                else if(!Constant.seniorList.contains(class_id) && Constant.seniorTestList.contains(title.get(j)))
//                                    testScoresSubTitle.add("N.A.");
                                else if (!campTestiDList.get(Constant.testIdList.get(title.get(j))).contains(class_id))
                                    testScoresSubTitle.add("N.A.");
                                else
                                    testScoresSubTitle.add("");
                            }


                            for (int j = 0; j < testScoresSubTitle.size(); j++) {
                                subTitle.add(testScoresSubTitle.get(j));
                            }


                            String[] arrSubTitle = (String[]) subTitle.toArray(new String[title.size()]);
                            csvWrite.writeNext(arrSubTitle);


                            testNames.clear();
                            testNameWithScore.clear();
                            testScoresSubTitle.clear();
                            subTitle.clear();


                        }

                    }


                    testNames.add(testReportModel.getTest_name() + " (" + testReportModel.getSubTestName() + ")");
                    testNameWithScore.put(testReportModel.getTest_name() + " (" + testReportModel.getSubTestName() + ")", test_score);

                    student_name = testReportModel.getStudent_name();
                    reg_num = testReportModel.getStudent_registration_num();
                    StudentMasterModel studentMasterModel = (StudentMasterModel) DBManager.getInstance().getAllTableData(TestStatusActivity.this, DBManager.TBL_LP_STUDENT_MASTER, "student_registration_num", reg_num, "", "").get(0);
                    student_class = studentMasterModel.getCurrent_class();
                    class_id = studentMasterModel.getClass_id();
                    //    roll_num=studentMasterModel.getCurrent_roll_num();

                }
                subTitle.add(student_name);
                subTitle.add(student_class);
                //  subTitle.add(roll_num);
                subTitle.add(reg_num);

                for (int j = 3; j < title.size(); j++) {

                    if (testNames.contains(title.get(j)))
                        testScoresSubTitle.add(testNameWithScore.get(title.get(j)));
//                    else if(Constant.seniorList.contains(class_id)&& Constant.juniorTestList.contains(title.get(j)))
//                        testScoresSubTitle.add("N.A.");
//                    else if(!Constant.seniorList.contains(class_id) && Constant.seniorTestList.contains(title.get(j)))
//                        testScoresSubTitle.add("N.A.");
                    else if (!campTestiDList.get(Constant.testIdList.get(title.get(j))).contains(class_id))
                        testScoresSubTitle.add("N.A.");
                    else
                        testScoresSubTitle.add("");
                }

                for (int j = 0; j < testScoresSubTitle.size(); j++) {
                    subTitle.add(testScoresSubTitle.get(j));
                }


                String[] arrSubTitle = (String[]) subTitle.toArray(new String[title.size()]);
                csvWrite.writeNext(arrSubTitle);
                csvWrite.close();
                Utility.openGmail(TestStatusActivity.this, file);

                return true;
            } catch (Exception e) {
                return false;
            }
        }


        protected void onPostExecute(final Boolean success) {
            dialogUtility.dismissProgressDialog();
            Constant.statusIDAllList.clear();
            if (success) {
                Toast.makeText(TestStatusActivity.this, R.string.export_success, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(TestStatusActivity.this, R.string.export_failed, Toast.LENGTH_SHORT).show();
            }
        }
    }


    public class LoadClassesAndTests extends AsyncTask<Void, Void, Void> {
        //private final ProgressDialogUtility dialogUtility = new ProgressDialogUtility(TestStatusActivity.this);
        private String completed;

        @Override
        protected void onPreExecute() {
//            this.dialogUtility.setMessage("...");
            dialogUtility.setMessage(getString(R.string.progress_loader));
            dialogUtility.showProgressDialog();
        }

        protected Void doInBackground(Void... params) {
            //studentList = DBManager.getInstance().getAllTableData(TestStatusActivity.this, DBManager.TBL_LP_STUDENT_MASTER, "current_school_id", Constant.SCHOOL_ID, "", "");
            studentList = DBManager.getInstance().getAllTableData(TestStatusActivity.this, DBManager.TBL_LP_STUDENT_MASTER, AppConfig.CAMP_ID, Constant.CAMP_ID, "", "");
            class_ids.put(getString(R.string.all), "0");
            if (studentList.size() == 0) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(TestStatusActivity.this, "No data available in database.", Toast.LENGTH_LONG).show();

                    }
                });
            } else {

                for (Object o : studentList) {
                    StudentMasterModel studentModel = (StudentMasterModel) o;
                    if (!class_ids.containsKey(studentModel.getCurrent_class())) {
                        //int class_strength_count=    DBManager.getInstance().getTotalCount(TestStatusActivity.this, DBManager.TBL_LP_STUDENT_MASTER, "current_school_id", Constant.SCHOOL_ID, "current_class", studentModel.getCurrent_class(),"","");
                        int class_strength_count = DBManager.getInstance().getTotalCount(TestStatusActivity.this, DBManager.TBL_LP_STUDENT_MASTER, AppConfig.CAMP_ID, Constant.CAMP_ID, "current_class", studentModel.getCurrent_class(), "", "");
                        class_spin_data.add(studentModel.getCurrent_class() + " (" + class_strength_count + ")");
                        class_ids.put(studentModel.getCurrent_class(), studentModel.getClass_id());
                    }

                }
            }
            testList = DBManager.getInstance().getAllTableDataTest(TestStatusActivity.this, DBManager.TBL_LP_FITNESS_TEST_TYPES, "", "", "", "");
            test_ids.put(getString(R.string.all), "0");
            if (testList.size() == 0) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(TestStatusActivity.this, "No data available in database. Please enable your internet to get the data directly from the server.", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                for (int i = 0; i < testList.size(); i++) {
                    FitnessTestTypesModel temp = (FitnessTestTypesModel) testList.get(i);
                    ArrayList<Object> arrayTestCategoryList = DBManager.getInstance().getAllTableData(TestStatusActivity.this,
                            DBManager.TBL_LP_FITNESS_TEST_CATEGORY, "test_id", "" + temp.getTest_type_id(), "", "");
                    for (int j = 0; j < arrayTestCategoryList.size(); j++) {
                        FitnessTestCategoryModel tmp = (FitnessTestCategoryModel) arrayTestCategoryList.get(j);
                        if (campTestiDList.containsKey("" + tmp.getSub_test_id())) {
                            //  if(!test_spin_data.contains(temp.getTest_name())) {
                            test_spin_data.add(temp.getTest_name() + " (" + tmp.getTest_name() + ")");
                            test_ids.put(temp.getTest_name() + " (" + tmp.getTest_name() + ")", "" + tmp.getSub_test_id());
                            //}

//                            if (tmp.getTest_applicable().equals("1")){
//                                Constant.juniorTestList.add(temp.getTest_name() + " (" + tmp.getTest_name() + ")");
//                            }
//                            else if (tmp.getTest_applicable().equals("2") ){
//                                Constant.seniorTestList.add(temp.getTest_name() + " (" + tmp.getTest_name() + ")");
//                            }

                        }

                    }
                }
            }

            return null;
        }


        protected void onPostExecute(Void result) {
            dialogUtility.dismissProgressDialog();

            Constant.classList = class_spin_data;
            Constant.classIdList = class_ids;
            Constant.testList = test_spin_data;
            Constant.testIdList = test_ids;


//            Log.e("juniorList",Constant.juniorTestList.toString());
//            Log.e("seniorList",Constant.seniorTestList.toString());
            ArrayAdapter class_spin_array = new ArrayAdapter<String>(TestStatusActivity.this, android.R.layout.simple_spinner_item, Constant.classList);
            class_spin.setDefaultText(getString(R.string.all));
            class_spin.setTitle(getString(R.string.select_class));
            class_spin.setAllText(getString(R.string.all));
            class_spin.setAdapter(class_spin_array, false, TestStatusActivity.this);

            ArrayAdapter test_spin_array = new ArrayAdapter<String>(TestStatusActivity.this, android.R.layout.simple_spinner_item, Constant.testList);

            test_spin.setTitle(getString(R.string.select_test));
            test_spin.setDefaultText(getString(R.string.all));
            test_spin.setAllText(getString(R.string.all));
            test_spin.setAdapter(test_spin_array, false, new MultiSpinner.MultiSpinnerListener() {
                @Override
                public void onItemsSelected(boolean[] selected) {

                }
            });
        }
    }


    private void filterTestsOnClasses(boolean[] selected) {
        filteredTestList.clear();
        for (int i = 0; i < Constant.testList.size(); i++) {
//                ArrayList<Object> arrayTestCategoryList = DBManager.getInstance().getAllTableData(TestStatusActivity.this,
//                    DBManager.TBL_LP_FITNESS_TEST_CATEGORY, "sub_test_id", "" + Constant.testIdList.get(Constant.testList.get(i)), "", "");
            //  for (int j = 0; j < arrayTestCategoryList.size(); j++) {
            // FitnessTestCategoryModel tmp = (FitnessTestCategoryModel) Constant.testList.get(i);
            String sub_test = Constant.testList.get(i);
            String sub_test_id = Constant.testIdList.get(sub_test);
            for (int k = 0; k < selected.length; k++) {
                if (selected[k] && !filteredTestList.contains(sub_test)) {
                    String class_id = Constant.classIdList.get(Constant.classList.get(k).substring(0, Constant.classList.get(k).indexOf(" ")));
                    if (campTestiDList.get(sub_test_id).contains(class_id)) {
                        //if (Constant.STUDENT_CATEGORY.equalsIgnoreCase("junior") && !Constant.seniorList.contains(class_id)) {
                        filteredTestList.add(sub_test);

                        //} else if (Constant.STUDENT_CATEGORY.equalsIgnoreCase("senior") && Constant.seniorList.contains(class_id)) {
//                                filteredTestList.add(Constant.testList.get(i));
//
//                            } else if (Constant.STUDENT_CATEGORY.equalsIgnoreCase("senior_junior")) {
//                                filteredTestList.add(Constant.testList.get(i));
//
//                            }


                    }
                }


            }


            //   }
        }

        ArrayAdapter test_spin_array = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, filteredTestList.size() == 0 ? Constant.testList : filteredTestList);

        test_spin.setTitle(getString(R.string.select_test));
        test_spin.setDefaultText(getString(R.string.all));
        test_spin.setAllText(getString(R.string.all));
        test_spin.setAdapter(test_spin_array, false, new MultiSpinner.MultiSpinnerListener() {
                    @Override
                    public void onItemsSelected(boolean[] selected) {

                    }
                }
        );


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialogUtility != null)
            dialogUtility.dismissProgressDialog();
    }
}
