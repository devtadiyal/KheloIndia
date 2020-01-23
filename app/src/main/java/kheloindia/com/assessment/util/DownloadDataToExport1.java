package kheloindia.com.assessment.util;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import kheloindia.com.assessment.R;
import kheloindia.com.assessment.functions.Constant;
import kheloindia.com.assessment.model.FitnessTestResultModel;
import kheloindia.com.assessment.model.StudentMasterModel;

// Not working
public class DownloadDataToExport1 extends AsyncTask<Void, Void, Boolean> {

    private Context context;
    private ProgressDialogUtility dialogUtility;
    private boolean alreadyExported;
    private ArrayList<Object> transactionsDataList;

   public DownloadDataToExport1(Context context, boolean alreadyExported){
        this.context = context;
        dialogUtility = new ProgressDialogUtility((Activity) context);
        this.alreadyExported=alreadyExported;
    }

    @Override
    protected void onPreExecute() {
        dialogUtility.showProgressDialog();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        return  downloadDataToExport();
    }


    protected void onPostExecute(Boolean aBoolean) {
        dialogUtility.dismissProgressDialog();
        if(aBoolean) {
            Toast.makeText(context, R.string.export_success, Toast.LENGTH_SHORT).show();
        }else
            Toast.makeText(context, R.string.export_failed, Toast.LENGTH_SHORT).show();
    }


    private boolean downloadDataToExport(){
        File exportDir = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
//            exportDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "");
//        }else
        exportDir = new File(Environment.getExternalStorageDirectory() ,"");
        ArrayList title = new ArrayList<String>();
        title.add("Student Id");
        title.add("School Id");
        title.add("Test Type Id");
        title.add("Test Score");
        title.add("Camp Id");
        title.add("Date");


        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }

        File file = new File(exportDir, "Export Data File.xls");

        try {

            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            String[] arrStrTitle = (String[]) title.toArray(new String[title.size()]);
            csvWrite.writeNext(arrStrTitle);
            if(alreadyExported)
                transactionsDataList = DBManager.getInstance().getFitnessTestResultData(context, "false", "true", 0);
            else{
                transactionsDataList = DBManager.getInstance().getFitnessTestResultData(context, "false", "", 0);
            }

            ArrayList subTitle = new ArrayList<String>();
            String test_score,school_id,test_type_id,student_id,camp_id;

            for(int i=0;i<transactionsDataList.size();i++){
                FitnessTestResultModel fitnessTestResultModel = ( FitnessTestResultModel ) transactionsDataList.get(i);
                //if (fitnessTestResultModel.getScore() != null) {
//                if (fitnessTestResultModel.getTestName().equalsIgnoreCase("agility") || fitnessTestResultModel.getTestName().equalsIgnoreCase("cardiovascular endurance") || fitnessTestResultModel.getTestName().equalsIgnoreCase("speed")|| fitnessTestResultModel.getTestName().equalsIgnoreCase("coordination")) {
//
//                    test_score = Utility.convertMilliSecondsToMiSecMs(context, Long.parseLong(fitnessTestResultModel.getScore()));
//                } else if (fitnessTestResultModel.getSubTestName().equalsIgnoreCase("weight")) {
//                    double score = (Double.parseDouble(fitnessTestResultModel.getScore())) / 1000;
//                    test_score = score + context.getResources().getString(R.string.kg);
//                } else if (fitnessTestResultModel.getSubTestName().contains("Ruler") || fitnessTestResultModel.getSubTestName().equalsIgnoreCase("height") || fitnessTestResultModel.getTestName().equalsIgnoreCase("flexibility") ||fitnessTestResultModel.getTestName().equalsIgnoreCase("power")) {
//                    double score = (Double.parseDouble(fitnessTestResultModel.getScore())) / 10;
//                    test_score = Utility.convertCentiMetreToMtCmMm(context, score);
//                }
////                    else if (fitnessTestResultModel.getTestName().contains("Manipulative") || fitnessTestResultModel.getTestName().contains("Body Management") || fitnessTestResultModel.getTestName().contains("Locomotor")) {
////
////                        test_score = fitnessTestResultModel.getScore() + "|" + fitnessTestResultModel.get;
////
////                    }
//                else if(fitnessTestResultModel.getSubTestName().contains("Flamingo") && fitnessTestResultModel.getScore().equalsIgnoreCase("1000")){
//                    test_score = context.getString(R.string.disqualified);
//                }else {

                    test_score = fitnessTestResultModel.getScore();

              //  }
                // }

                StudentMasterModel studentMasterModel = (StudentMasterModel) DBManager.getInstance().getAllTableData(context, DBManager.TBL_LP_STUDENT_MASTER, "student_id", ""+fitnessTestResultModel.getStudent_id(), "", "").get(0);
                school_id = studentMasterModel.getCurrent_school_id();
                test_type_id=""+fitnessTestResultModel.getTest_type_id();
                student_id=""+studentMasterModel.getStudent_id();
                camp_id=studentMasterModel.getCamp_id();

                subTitle.add(student_id);
                subTitle.add(school_id);
                subTitle.add(test_type_id);
               // subTitle.add(fitnessTestResultModel.getTestName() + " (" + fitnessTestResultModel.getSubTestName() + ")");
                subTitle.add(test_score);
                subTitle.add(camp_id);
                subTitle.add(Constant.ConvertDateToString(fitnessTestResultModel.getDevice_date()));


                String[] arrStrSubTitle = (String[]) subTitle.toArray(new String[subTitle.size()]);
                csvWrite.writeNext(arrStrSubTitle);
                DBManager db = new DBManager();
                db.updateFitnessTestTable("",fitnessTestResultModel.getTest_type_id(),fitnessTestResultModel.getStudent_id());
                subTitle.clear();
            }

            csvWrite.close();
            Utility.openGmail(context,file);
            return true;
        }catch(Exception e){
            return false;

        }
    }
}
