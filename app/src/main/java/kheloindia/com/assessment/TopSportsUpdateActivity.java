package kheloindia.com.assessment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;

import kheloindia.com.assessment.adapter.TopSportsUpdateAdapter;
import kheloindia.com.assessment.model.StudentMasterModel;
import kheloindia.com.assessment.model.TopSportsGetSkillModel;
import kheloindia.com.assessment.model.TopSportsUpdateItemModel;
import kheloindia.com.assessment.model.TopSportsUpdateItemSkillModel;
import kheloindia.com.assessment.model.TopSportsUpdateModel;
import kheloindia.com.assessment.model.TopSportsUpdateSecondModel;
import kheloindia.com.assessment.util.CSVWriter;
import kheloindia.com.assessment.util.ConnectionDetector;
import kheloindia.com.assessment.util.DBManager;
import kheloindia.com.assessment.util.ProgressDialogUtility;
import kheloindia.com.assessment.util.ResponseListener;
import kheloindia.com.assessment.util.Utility;

import kheloindia.com.assessment.functions.Constant;
import kheloindia.com.assessment.webservice.ChangeSportRequest;
import kheloindia.com.assessment.webservice.TopSportsGetSkillRequest;
import kheloindia.com.assessment.webservice.TopSportsUpdateRequest;

/**
 * Created by CT13 on 2018-05-08.
 */

public class TopSportsUpdateActivity extends AppCompatActivity implements View.OnClickListener, ResponseListener, AdapterView.OnItemSelectedListener {

    public static Button email_btn,save_btn,download_btn;
    private ConnectionDetector connectionDetector;
    private Toolbar toolbar;
    private ListView update_sport_list_view;
    private Spinner class_spin;
    private ArrayList<TopSportsUpdateItemModel> topSportsUpdateList, topSportsUpdateCompleteList,topSportsUpdateInCompleteList;
    private ArrayList<TopSportsUpdateItemSkillModel> topSportsUpdateSkillList ;
    private  ArrayList<Object> studentList;
    private TopSportsUpdateAdapter topSportsUpdateAdapter;
    private boolean email;
    private ArrayList<String> sportsList;
    private ArrayList<String> sportsIdList;
    private TextView school_tv;
    private ProgressDialogUtility progressDialogUtility;
    private Button all_btn,incomplete_btn,complete_btn;
    private boolean all,complete;
    private HashMap<String,ArrayList<TopSportsUpdateItemModel>> topSportsUpdateMap=new HashMap<>();
    private Typeface font_light;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_sports_update);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        Utility.showActionBar(this, toolbar, "Update TOP Sports");
        save_btn=(Button)findViewById(R.id.save_btn);
        email_btn=(Button)findViewById(R.id.email_btn);
        download_btn=(Button)findViewById(R.id.download_btn);
        school_tv=(TextView)findViewById(R.id.school_tv);
        update_sport_list_view=(ListView)findViewById(R.id.update_sport_list_view);
        class_spin=(Spinner)findViewById(R.id.class_spin);
        all_btn=(Button)findViewById(R.id.all_btn);
        incomplete_btn=(Button)findViewById(R.id.incomplete_btn);
        complete_btn=(Button)findViewById(R.id.complete_btn);
        font_light = Typeface.createFromAsset(getAssets(),
                "fonts/Roboto-Light" +
                        "_0.ttf");
        school_tv.setTypeface(font_light);
        school_tv.setText(Constant.SCHOOL_NAME);
        save_btn.setVisibility(View.GONE);
        email_btn.setVisibility(View.GONE);
        download_btn.setVisibility(View.GONE);
        connectionDetector=new ConnectionDetector(this);
        save_btn.setOnClickListener(this);
        email_btn.setOnClickListener(this);
        download_btn.setOnClickListener(this);
        all_btn.setOnClickListener(this);
        incomplete_btn.setOnClickListener(this);
        complete_btn.setOnClickListener(this);
        if(Constant.classList2.size()==0 ||Constant.classIdList2.size()==0)
        new LoadClasses().execute();
        else{
            ArrayAdapter class_spin_array = new ArrayAdapter<String>(TopSportsUpdateActivity.this, R.layout.spinner_item, Constant.classList2);
            class_spin_array.setDropDownViewResource(R.layout.spinner_dropdown_item);
            class_spin.setAdapter(class_spin_array);
        }
        all_btn.setBackgroundResource(R.color.green);
        all_btn.setTextColor(getResources().getColor(R.color.white));
        class_spin.setOnItemSelectedListener(this);
        all=true;
        complete=false;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        Constant.CAMP_ID = sp.getString("camp_id", "");
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.save_btn:
                if(class_spin.getSelectedItemPosition()!=0) {
                    if (connectionDetector.isConnectingToInternet()) {

                        JSONArray jsonArray = new JSONArray();
                        JSONObject jsonObject;
                        if (all) {
                            for (int i = 0; i < topSportsUpdateList.size(); i++) {
                                jsonObject = new JSONObject();
                                try {

                                    jsonObject.put("Student_Id", topSportsUpdateList.get(i).getStudent_id());
                                    jsonObject.put("Class_Id", Constant.classIdList2.get(class_spin.getSelectedItemPosition() - 1));
                                    jsonObject.put("Class", Constant.classList2.get(class_spin.getSelectedItemPosition()));
                                    jsonObject.put("SkillID", sportsIdList.get(topSportsUpdateAdapter.selectedItems.get(i)));
                                    jsonObject.put("Created_On", Utility.getDate(System.currentTimeMillis(), "dd-MMM-yyyy"));
                                    jsonObject.put("Created_By", Constant.TEST_COORDINATOR_ID);
                                    jsonObject.put("TermiD", Constant.CAMP_ID);

                                    jsonArray.put(jsonObject);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        } else if (complete) {
                            for (int i = 0; i < topSportsUpdateCompleteList.size(); i++) {
                                jsonObject = new JSONObject();
                                try {

                                    jsonObject.put("Student_Id", topSportsUpdateCompleteList.get(i).getStudent_id());
                                    jsonObject.put("Class_Id",  Constant.classIdList2.get(class_spin.getSelectedItemPosition() - 1));
                                    jsonObject.put("Class", Constant.classList2.get(class_spin.getSelectedItemPosition()));
                                    jsonObject.put("SkillID", sportsIdList.get(topSportsUpdateAdapter.selectedItems.get(i)));
                                    jsonObject.put("Created_On", Utility.getDate(System.currentTimeMillis(), "dd-MMM-yyyy"));
                                    jsonObject.put("Created_By", Constant.TEST_COORDINATOR_ID);
                                    jsonObject.put("TermiD", Constant.CAMP_ID);
                                    jsonArray.put(jsonObject);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        } else {
                            for (int i = 0; i < topSportsUpdateInCompleteList.size(); i++) {
                                jsonObject = new JSONObject();
                                try {

                                    jsonObject.put("Student_Id", topSportsUpdateInCompleteList.get(i).getStudent_id());
                                    jsonObject.put("Class_Id",  Constant.classIdList2.get(class_spin.getSelectedItemPosition() - 1));
                                    jsonObject.put("Class", Constant.classList2.get(class_spin.getSelectedItemPosition()));
                                    jsonObject.put("SkillID", sportsIdList.get(topSportsUpdateAdapter.selectedItems.get(i)));
                                    jsonObject.put("Created_On", Utility.getDate(System.currentTimeMillis(), "dd-MMM-yyyy"));
                                    jsonObject.put("Created_By", Constant.TEST_COORDINATOR_ID);
                                    jsonObject.put("TermiD", Constant.CAMP_ID);
                                    jsonArray.put(jsonObject);
                                } catch (JSONException e) {
                                    e.printStackTrace();

                                }
                            }


                        }
                        ChangeSportRequest changeSportRequest = new ChangeSportRequest(this, jsonArray.toString(), this);
                        changeSportRequest.hitUserRequest();

                    } else
                        Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(this, R.string.validate_class_drop_down, Toast.LENGTH_SHORT).show();
                break;

            case R.id.email_btn:

                if(class_spin.getSelectedItemPosition()!=0){
                    email = true;
                    new ExportDatabaseCSVTaskUpdateSports().execute();
                } else
                    Toast.makeText(this, R.string.validate_class_drop_down, Toast.LENGTH_SHORT).show();

                break;

            case R.id.download_btn:

                if(class_spin.getSelectedItemPosition()!=0){
                    email = false;
                    new ExportDatabaseCSVTaskUpdateSports().execute();
                }else
                    Toast.makeText(this, R.string.validate_class_drop_down, Toast.LENGTH_SHORT).show();

                break;


            case R.id.all_btn:
                all_btn.setBackgroundResource(R.color.green);
                incomplete_btn.setBackgroundResource(android.R.color.transparent);
                complete_btn.setBackgroundResource(android.R.color.transparent);
                all_btn.setTextColor(getResources().getColor(R.color.white));
                incomplete_btn.setTextColor(getResources().getColor(R.color.grey));
                complete_btn.setTextColor(getResources().getColor(R.color.grey));
                all=true;
                complete=false;
                loadStudentListTopSports(class_spin.getSelectedItemPosition());

                break;

            case R.id.incomplete_btn:
                all_btn.setBackgroundResource(android.R.color.transparent);
                incomplete_btn.setBackgroundResource(R.color.green);
                complete_btn.setBackgroundResource(android.R.color.transparent);
                all_btn.setTextColor(getResources().getColor(R.color.grey));
                incomplete_btn.setTextColor(getResources().getColor(R.color.white));
                complete_btn.setTextColor(getResources().getColor(R.color.grey));
                all=false;
                complete=false;
                loadStudentListTopSports(class_spin.getSelectedItemPosition());

                break;

            case R.id.complete_btn:
                all_btn.setBackgroundResource(android.R.color.transparent);
                incomplete_btn.setBackgroundResource(android.R.color.transparent);
                complete_btn.setBackgroundResource(R.color.green);
                all_btn.setTextColor(getResources().getColor(R.color.grey));
                incomplete_btn.setTextColor(getResources().getColor(R.color.grey));
                complete_btn.setTextColor(getResources().getColor(R.color.white));
                all=false;
                complete=true;
                loadStudentListTopSports(class_spin.getSelectedItemPosition());

                break;
        }
    }

//private boolean validate(){
//    boolean some_sport_updated=false;
//    if(all){
//    for(int i=0;i<topSportsUpdateList.size();i++){
//        if(!sportsList.get(topSportsUpdateAdapter.selectedItems.get(i)).equalsIgnoreCase(getString(R.string.sports_technique))) {
//           some_sport_updated = true;
//            break;
//        }
//    }
//    }else if(!complete){
//        for(int i=0;i<topSportsUpdateInCompleteList.size();i++){
//            if(!sportsList.get(topSportsUpdateAdapter.selectedItems.get(i)).equalsIgnoreCase(getString(R.string.sports_technique))) {
//                some_sport_updated = true;
//                break;
//            }
//        }
//
//    }else{
//        for(int i=0;i<topSportsUpdateCompleteList.size();i++){
//            if(!sportsList.get(topSportsUpdateAdapter.selectedItems.get(i)).equalsIgnoreCase(getString(R.string.sports_technique))) {
//               some_sport_updated = true;
//                break;
//            }
//        }
//
//    }
//    return some_sport_updated;
//}

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onResponse(Object obj) {
        if(obj instanceof TopSportsUpdateModel){
            TopSportsUpdateModel topSportsUpdateModel=(TopSportsUpdateModel)obj;
            TopSportsUpdateItemModel topSportsUpdateItemModel;

            if(topSportsUpdateModel.getIsSuccess().equalsIgnoreCase("true")) {
                sportsList=new ArrayList<>();
                sportsIdList=new ArrayList<>();
                topSportsUpdateList = new ArrayList<>();
                sportsList.add(getString(R.string.sports_technique));
                sportsIdList.add("0");

                for(int i=0;i<topSportsUpdateModel.getTopSport().size();i++){
                    sportsList.add(topSportsUpdateModel.getTopSport().get(i).getTestName());
                    sportsIdList.add(topSportsUpdateModel.getTopSport().get(i).getTestID());
                }

              for(int i=0;i<topSportsUpdateModel.getDetail().size();i++){
                 topSportsUpdateItemModel=new TopSportsUpdateItemModel();
                  topSportsUpdateItemModel.setStudent_name( topSportsUpdateModel.getDetail().get(i).getStudentName());
                  topSportsUpdateItemModel.setStudent_roll_no(topSportsUpdateModel.getDetail().get(i).getCurrentRollNum());
                  topSportsUpdateItemModel.setStudent_id(topSportsUpdateModel.getDetail().get(i).getStudentID());
                  for(int j=0;j<topSportsUpdateSkillList.size();j++){
                      if(topSportsUpdateSkillList.get(j).getStudent_id().equalsIgnoreCase( topSportsUpdateItemModel.getStudent_id())){
                      topSportsUpdateItemModel.setSport_name(topSportsUpdateSkillList.get(j).getSport_name());
                          break;
                      }
                  }
                  topSportsUpdateItemModel.setSportsList(sportsList);
                  topSportsUpdateList.add(topSportsUpdateItemModel);
              }
//              Collections.sort(topSportsUpdateList, new Comparator<TopSportsUpdateItemModel>() {
//                  @Override
//                  public int compare(TopSportsUpdateItemModel o1, TopSportsUpdateItemModel o2) {
//                      return o1.getStudent_name().compareToIgnoreCase(o2.getStudent_name());
//                  }
//              });
                if(topSportsUpdateList.size()>0) {
                      topSportsUpdateMap.put(Constant.classList2.get(class_spin.getSelectedItemPosition()),topSportsUpdateList);

                }
if(all)
                topSportsUpdateAdapter = new TopSportsUpdateAdapter(this, topSportsUpdateList);

                else if(complete){
                                  topSportsUpdateCompleteList=new  ArrayList<>();
                                  for(int i=0;i<topSportsUpdateList.size();i++){
                                 if(topSportsUpdateList.get(i).getSport_name()!=null){
                                     topSportsUpdateItemModel=new TopSportsUpdateItemModel();
                                     topSportsUpdateItemModel.setStudent_name( topSportsUpdateList.get(i).getStudent_name());
                                     topSportsUpdateItemModel.setStudent_roll_no(topSportsUpdateList.get(i).getStudent_roll_no());
                                     topSportsUpdateItemModel.setStudent_id(topSportsUpdateList.get(i).getStudent_id());
                                     topSportsUpdateItemModel.setSport_name(topSportsUpdateList.get(i).getSport_name());
                                     topSportsUpdateItemModel.setSportsList(topSportsUpdateList.get(i).getSportsList());
                                     topSportsUpdateCompleteList.add(topSportsUpdateItemModel);
        }
    }
    topSportsUpdateAdapter = new TopSportsUpdateAdapter(this, topSportsUpdateCompleteList);
    if(topSportsUpdateCompleteList.size()==0) {
        Toast.makeText(this, R.string.incomplete_mapping_for_all, Toast.LENGTH_SHORT).show();

    }

}else{
    topSportsUpdateInCompleteList=new  ArrayList<>();
    for(int i=0;i<topSportsUpdateList.size();i++){
        if(topSportsUpdateList.get(i).getSport_name()==null){
            topSportsUpdateItemModel=new TopSportsUpdateItemModel();
            topSportsUpdateItemModel.setStudent_name( topSportsUpdateList.get(i).getStudent_name());
            topSportsUpdateItemModel.setStudent_roll_no(topSportsUpdateList.get(i).getStudent_roll_no());
            topSportsUpdateItemModel.setStudent_id(topSportsUpdateList.get(i).getStudent_id());
            topSportsUpdateItemModel.setSportsList(topSportsUpdateList.get(i).getSportsList());
            topSportsUpdateInCompleteList.add(topSportsUpdateItemModel);
        }
    }
    topSportsUpdateAdapter = new TopSportsUpdateAdapter(this, topSportsUpdateInCompleteList);
    if(topSportsUpdateInCompleteList.size()==0) {
        Toast.makeText(this, R.string.complete_mapping_for_all, Toast.LENGTH_SHORT).show();

    }
}
                new LoadListOnThread().execute();
                update_sport_list_view.setAdapter(topSportsUpdateAdapter);


            }
        }else if(obj instanceof TopSportsUpdateSecondModel){
            TopSportsUpdateSecondModel topSportsUpdateModel=(TopSportsUpdateSecondModel)obj;
            if(topSportsUpdateModel.getIsSuccess().equalsIgnoreCase("true")) {
                Toast.makeText(this,topSportsUpdateModel.getMessage(),Toast.LENGTH_SHORT).show();
                save_btn.setVisibility(View.GONE);
                email_btn.setVisibility(View.GONE);
                download_btn.setVisibility(View.GONE);
                topSportsUpdateMap.remove(Constant.classList2.get(class_spin.getSelectedItemPosition()));
                topSportsUpdateList.clear();
                topSportsUpdateAdapter = new TopSportsUpdateAdapter(this, topSportsUpdateList);
                update_sport_list_view.setAdapter(topSportsUpdateAdapter);
                class_spin.setSelection(0);
            }
        }else if(obj instanceof TopSportsGetSkillModel){
            TopSportsGetSkillModel topSportsGetSkillModel=(TopSportsGetSkillModel) obj;
            TopSportsUpdateItemSkillModel topSportsUpdateItemSkillModel;

if(topSportsGetSkillModel.getIsSuccess().equalsIgnoreCase("true")) {
    topSportsUpdateSkillList=new ArrayList<>();
    for(int i=0;i<topSportsGetSkillModel.getTu1().size();i++){
        topSportsUpdateItemSkillModel =new TopSportsUpdateItemSkillModel();
        topSportsUpdateItemSkillModel.setStudent_id(topSportsGetSkillModel.getTu1().get(i).getStudentID());
        topSportsUpdateItemSkillModel.setSport_name(topSportsGetSkillModel.getTu1().get(i).getTestName());
        topSportsUpdateSkillList.add(topSportsUpdateItemSkillModel);
    }

    TopSportsUpdateRequest topSportsUpdateRequest = new TopSportsUpdateRequest(this, Constant.SCHOOL_ID, Constant.classList2.get(class_spin.getSelectedItemPosition()), this);
    topSportsUpdateRequest.hitUserRequest();

}
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        loadStudentListTopSports(position);

    }

    private void loadStudentListTopSports(int position) {
        if(position!=0){
        if (topSportsUpdateMap.get(Constant.classList2.get(position))==null) {
            if (connectionDetector.isConnectingToInternet()) {
                progressDialogUtility = new ProgressDialogUtility(this);
                progressDialogUtility.showProgressDialog();
                TopSportsGetSkillRequest topSportsGetSkillRequest = new TopSportsGetSkillRequest(this, Constant.SCHOOL_ID, Constant.classList2.get(position), this);
                topSportsGetSkillRequest.hitUserRequest();

            } else
                Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show();
        }else {

            if (all) {
                topSportsUpdateList=topSportsUpdateMap.get(Constant.classList2.get(position));
                topSportsUpdateAdapter = new TopSportsUpdateAdapter(this,topSportsUpdateList );
            } else if (complete) {
                topSportsUpdateCompleteList = new ArrayList<>();
                TopSportsUpdateItemModel topSportsUpdateItemModel;
                topSportsUpdateList = topSportsUpdateMap.get(Constant.classList2.get(position));
                for (int i = 0; i < topSportsUpdateList.size(); i++) {
                    if (topSportsUpdateList.get(i).getSport_name() != null) {
                        topSportsUpdateItemModel = new TopSportsUpdateItemModel();
                        topSportsUpdateItemModel.setStudent_name(topSportsUpdateList.get(i).getStudent_name());
                        topSportsUpdateItemModel.setStudent_roll_no(topSportsUpdateList.get(i).getStudent_roll_no());
                        topSportsUpdateItemModel.setStudent_id(topSportsUpdateList.get(i).getStudent_id());
                        topSportsUpdateItemModel.setSport_name(topSportsUpdateList.get(i).getSport_name());
                        topSportsUpdateItemModel.setSportsList(topSportsUpdateList.get(i).getSportsList());
                        topSportsUpdateCompleteList.add(topSportsUpdateItemModel);
                    }
                }
                topSportsUpdateAdapter = new TopSportsUpdateAdapter(this, topSportsUpdateCompleteList);
                if (topSportsUpdateCompleteList.size() == 0) {
                    Toast.makeText(this, R.string.incomplete_mapping_for_all, Toast.LENGTH_SHORT).show();
                }
            } else {

                topSportsUpdateInCompleteList = new ArrayList<>();
                TopSportsUpdateItemModel topSportsUpdateItemModel;
                topSportsUpdateList = topSportsUpdateMap.get(Constant.classList2.get(position));
                for (int i = 0; i < topSportsUpdateList.size(); i++) {
                    if (topSportsUpdateList.get(i).getSport_name() == null) {
                        topSportsUpdateItemModel = new TopSportsUpdateItemModel();
                        topSportsUpdateItemModel.setStudent_name(topSportsUpdateList.get(i).getStudent_name());
                        topSportsUpdateItemModel.setStudent_roll_no(topSportsUpdateList.get(i).getStudent_roll_no());
                        topSportsUpdateItemModel.setStudent_id(topSportsUpdateList.get(i).getStudent_id());
                        topSportsUpdateItemModel.setSportsList(topSportsUpdateList.get(i).getSportsList());
                        topSportsUpdateInCompleteList.add(topSportsUpdateItemModel);
                    }
                }
                topSportsUpdateAdapter = new TopSportsUpdateAdapter(this, topSportsUpdateInCompleteList);
                if (topSportsUpdateInCompleteList.size() == 0) {
                    Toast.makeText(this,R.string.complete_mapping_for_all, Toast.LENGTH_SHORT).show();
                }
            }
            progressDialogUtility.showProgressDialog();
            new LoadListOnThread().execute();
            update_sport_list_view.setAdapter(topSportsUpdateAdapter);

        }

        }
        }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private class LoadClasses extends AsyncTask<Void, Void, Void> {
        private final ProgressDialogUtility dialogUtility = new ProgressDialogUtility(TopSportsUpdateActivity.this);

        @Override
        protected void onPreExecute() {
            this.dialogUtility.showProgressDialog();
        }

        protected Void doInBackground(Void... params) {
            studentList = DBManager.getInstance().getAllTableData(TopSportsUpdateActivity.this, DBManager.TBL_LP_STUDENT_MASTER, "current_school_id", Constant.SCHOOL_ID, "", "");
            Constant.classList2.add("Class");

            if (studentList.size() == 0) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(TopSportsUpdateActivity.this, "No data available in database.", Toast.LENGTH_LONG).show();
                    }
                });

            } else {

                for (Object o : studentList) {
                    StudentMasterModel studentModel = (StudentMasterModel) o;
                   if(!(studentModel.getClass_id().charAt(0)>='A' && studentModel.getClass_id().charAt(0)<='Z')&&Integer.parseInt(studentModel.getClass_id())>=4) {
                       if (!Constant.classList2.contains(studentModel.getCurrent_class())) {
                           Constant.classList2.add(studentModel.getCurrent_class());
                           Constant.classIdList2.add(studentModel.getClass_id());
                       }
                   }
                }

            }
            return null;
        }

        protected void onPostExecute(Void result) {
            this.dialogUtility.dismissProgressDialog();
            //Collections.sort(Constant.classList2);
            ArrayAdapter class_spin_array = new ArrayAdapter<String>(TopSportsUpdateActivity.this, R.layout.spinner_item, Constant.classList2);
            class_spin_array.setDropDownViewResource(R.layout.spinner_dropdown_item);
            class_spin.setAdapter(class_spin_array);
        }

    }
    private class ExportDatabaseCSVTaskUpdateSports extends AsyncTask<Void, Void, Boolean> {

        private final ProgressDialogUtility dialogUtility = new ProgressDialogUtility(TopSportsUpdateActivity.this);

        @Override
        protected void onPreExecute() {
            this.dialogUtility.setMessage(getString(R.string.csv_message_loader));
            this.dialogUtility.showProgressDialog();
        }

        protected Boolean doInBackground(Void... params) {

            File exportDir = new File(Environment.getExternalStorageDirectory(), "");

            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }

            File file = new File(exportDir, "StudentToSportsMapping.csv");
            try {
                file.createNewFile();
                CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
                String arrStrTitle[] = {"Student Name", "Class","Registration Number", "Sport Name","Created On"};
                csvWrite.writeNext(arrStrTitle);
                if(all){
                for(int i=0;i<topSportsUpdateList.size();i++){
                       String arrSubStr[] = {topSportsUpdateList.get(i).getStudent_name(),Constant.classList2.get(class_spin.getSelectedItemPosition()) ,Constant.SCHOOL_ID+topSportsUpdateList.get(i).getStudent_roll_no(),topSportsUpdateAdapter.selectedItems.get(i)==0?getString(R.string.nill_score_excel_sheet):sportsList.get(topSportsUpdateAdapter.selectedItems.get(i)),Utility.getDate(System.currentTimeMillis(), "dd-MMM-yyyy")};
                       csvWrite.writeNext(arrSubStr);
                }
                }else if(complete){
                    for(int i=0;i<topSportsUpdateCompleteList.size();i++){
                        String arrSubStr[] = {topSportsUpdateCompleteList.get(i).getStudent_name(),Constant.classList2.get(class_spin.getSelectedItemPosition()) ,Constant.SCHOOL_ID+topSportsUpdateCompleteList.get(i).getStudent_roll_no(),topSportsUpdateAdapter.selectedItems.get(i)==0?getString(R.string.nill_score_excel_sheet):sportsList.get(topSportsUpdateAdapter.selectedItems.get(i)),Utility.getDate(System.currentTimeMillis(), "dd-MMM-yyyy")};
                        csvWrite.writeNext(arrSubStr);
                    }

                }else{
                    for(int i=0;i<topSportsUpdateInCompleteList.size();i++){
                        String arrSubStr[] = {topSportsUpdateInCompleteList.get(i).getStudent_name(),Constant.classList2.get(class_spin.getSelectedItemPosition()) ,Constant.SCHOOL_ID+topSportsUpdateInCompleteList.get(i).getStudent_roll_no(),topSportsUpdateAdapter.selectedItems.get(i)==0?getString(R.string.nill_score_excel_sheet):sportsList.get(topSportsUpdateAdapter.selectedItems.get(i)),Utility.getDate(System.currentTimeMillis(), "dd-MMM-yyyy")};
                        csvWrite.writeNext(arrSubStr);
                    }

                }
                csvWrite.close();
                if(email)
               Utility. openGmail(TopSportsUpdateActivity.this,file);
                return true;
            } catch (Exception e) {
                return false;
            }
        }


        protected void onPostExecute(final Boolean success) {
            this.dialogUtility.dismissProgressDialog();
            if (success) {
                Toast.makeText(TopSportsUpdateActivity.this, "Download successful!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(TopSportsUpdateActivity.this, "Download failed", Toast.LENGTH_SHORT).show();
            }
        }
    }



    private class LoadListOnThread extends AsyncTask<Void, Void, Void> {
//        ProgressDialogUtility progressDialogUtility=new ProgressDialogUtility(TopSportsUpdateActivity.this);
//
//        @Override
//        protected void onPreExecute() {
//            progressDialogUtility.showProgressDialog();
//        }

        @Override
            protected Void doInBackground(Void... voids) {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                progressDialogUtility.dismissProgressDialog();
                save_btn.setVisibility(View.GONE);
                if(all){
                    if(topSportsUpdateList.size()==0){
                        email_btn.setVisibility(View.GONE);
                        download_btn.setVisibility(View.GONE);
                    }else{
                        email_btn.setVisibility(View.VISIBLE);
                        download_btn.setVisibility(View.VISIBLE);
                    }
                }else if(complete){
                    if(topSportsUpdateCompleteList.size()==0){
                        email_btn.setVisibility(View.GONE);
                        download_btn.setVisibility(View.GONE);
                    }else{
                        email_btn.setVisibility(View.VISIBLE);
                        download_btn.setVisibility(View.VISIBLE);
                    }
                }else{
                    if(topSportsUpdateInCompleteList.size()==0){
                        email_btn.setVisibility(View.GONE);
                        download_btn.setVisibility(View.GONE);
                    }else{
                        email_btn.setVisibility(View.VISIBLE);
                        download_btn.setVisibility(View.VISIBLE);
                    }
                }


            }
        }

    @Override
    public void onBackPressed() {
        if(save_btn.getVisibility()==View.VISIBLE)
        showDataSaveDialog();
        else
            super.onBackPressed();
    }

    private void showDataSaveDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setCancelable(false);

        alertDialog.setMessage(R.string.data_save_message_alert);

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.alarm);

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
             dialog.dismiss();
            }});
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialog, int which) {
        finish();
    }
});
        alertDialog.show();


    }
}
