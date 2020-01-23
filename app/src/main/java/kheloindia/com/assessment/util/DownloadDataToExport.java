package kheloindia.com.assessment.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import kheloindia.com.assessment.R;
import kheloindia.com.assessment.functions.Constant;
import kheloindia.com.assessment.model.FitnessTestResultModel;

public class DownloadDataToExport extends AsyncTask<Void, Void, Boolean> {

    private Context context;
    DBManager db;
    String testCoordinatorName, userName,schoolId,testTableName;
    private ProgressDialogUtility progressDialogUtility;
    //private boolean alreadyExported;
    private ArrayList<Object> transactionsDataList,transactionsSkillDataList;
    private static String[] columns = { "Student Id", "School Id", "Test Type Id","Test Score","Camp Id",
            "Date","CreatedBy" };

   public DownloadDataToExport(Context context, ProgressDialogUtility progressDialogUtility,String testTableName){
        this.context = context;
        this.progressDialogUtility = progressDialogUtility;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        testCoordinatorName = sharedPreferences.getString(AppConfig.TEST_COORDINATOR_NAME,"");
        schoolId = sharedPreferences.getString(AppConfig.SCHOOL_ID,"");
        userName = sharedPreferences.getString(AppConfig.USER_NAME,"");
        this.testTableName = testTableName;
        db = new DBManager();
        //this.alreadyExported=alreadyExported;
    }

    @Override
    protected void onPreExecute() {
       progressDialogUtility.showProgressDialog();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        return  downloadDataToExport();
    }


    protected void onPostExecute(Boolean aBoolean) {
        progressDialogUtility.dismissProgressDialog();
        if(aBoolean) {
            Toast.makeText(context, R.string.export_success, Toast.LENGTH_SHORT).show();
        }else
            Toast.makeText(context, R.string.export_failed, Toast.LENGTH_SHORT).show();
    }


//    private boolean downloadDataToExport1(){
//        File exportDir = null;
////        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
////            exportDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "");
////        }else
//        exportDir = new File(Environment.getExternalStorageDirectory() ,"");
//        ArrayList title = new ArrayList<String>();
//        title.add("Student Id");
//        title.add("School Id");
//        title.add("Test Type Id");
//        title.add("Test Score");
//        title.add("Camp Id");
//        title.add("Date");
//
//
//        if (!exportDir.exists()) {
//            exportDir.mkdirs();
//        }
//
//        File file = new File(exportDir, "Export Data File1.xls");
//
//        try {
//
//            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
//            String[] arrStrTitle = (String[]) title.toArray(new String[title.size()]);
//            csvWrite.writeNext(arrStrTitle);
//            transactionsDataList = DBManager.getInstance().getAllTableData(context,
//                    DBManager.TBL_LP_FITNESS_TEST_RESULT, "tested", "true", "synced", "false");
//
//            transactionsSkillDataList = DBManager.getInstance().getAllTableData(context,
//                    DBManager.TBL_LP_FITNESS_SKILL_TEST_RESULT,  "tested", "true","last_modified_on", "0");
//
//            ArrayList subTitle = new ArrayList<String>();
//            String test_score,school_id,test_type_id,student_id,camp_id;
//
//            for(int i=0;i<transactionsDataList.size();i++){
//                FitnessTestResultModel fitnessTestResultModel = ( FitnessTestResultModel ) transactionsDataList.get(i);
//                //if (fitnessTestResultModel.getScore() != null) {
////                if (fitnessTestResultModel.getTestName().equalsIgnoreCase("agility") || fitnessTestResultModel.getTestName().equalsIgnoreCase("cardiovascular endurance") || fitnessTestResultModel.getTestName().equalsIgnoreCase("speed")|| fitnessTestResultModel.getTestName().equalsIgnoreCase("coordination")) {
////
////                    test_score = Utility.convertMilliSecondsToMiSecMs(context, Long.parseLong(fitnessTestResultModel.getScore()));
////                } else if (fitnessTestResultModel.getSubTestName().equalsIgnoreCase("weight")) {
////                    double score = (Double.parseDouble(fitnessTestResultModel.getScore())) / 1000;
////                    test_score = score + context.getResources().getString(R.string.kg);
////                } else if (fitnessTestResultModel.getSubTestName().contains("Ruler") || fitnessTestResultModel.getSubTestName().equalsIgnoreCase("height") || fitnessTestResultModel.getTestName().equalsIgnoreCase("flexibility") ||fitnessTestResultModel.getTestName().equalsIgnoreCase("power")) {
////                    double score = (Double.parseDouble(fitnessTestResultModel.getScore())) / 10;
////                    test_score = Utility.convertCentiMetreToMtCmMm(context, score);
////                }
//////                    else if (fitnessTestResultModel.getTestName().contains("Manipulative") || fitnessTestResultModel.getTestName().contains("Body Management") || fitnessTestResultModel.getTestName().contains("Locomotor")) {
//////
//////                        test_score = fitnessTestResultModel.getScore() + "|" + fitnessTestResultModel.get;
//////
//////                    }
////                else if(fitnessTestResultModel.getSubTestName().contains("Flamingo") && fitnessTestResultModel.getScore().equalsIgnoreCase("1000")){
////                    test_score = context.getString(R.string.disqualified);
////                }else {
//
//                    test_score = fitnessTestResultModel.getScore();
//
//              //  }
//                // }
//
//                StudentMasterModel studentMasterModel = (StudentMasterModel) DBManager.getInstance().getAllTableData(context, DBManager.TBL_LP_STUDENT_MASTER, "student_id", ""+fitnessTestResultModel.getStudent_id(), "", "").get(0);
//                school_id = studentMasterModel.getCurrent_school_id();
//                test_type_id=""+fitnessTestResultModel.getTest_type_id();
//                student_id=""+studentMasterModel.getStudent_id();
//                camp_id=studentMasterModel.getCamp_id();
//
//                subTitle.add(student_id);
//                subTitle.add(school_id);
//                subTitle.add(test_type_id);
//               // subTitle.add(fitnessTestResultModel.getTestName() + " (" + fitnessTestResultModel.getSubTestName() + ")");
//                subTitle.add(test_score);
//                subTitle.add(camp_id);
//                subTitle.add(Constant.ConvertDateToString(fitnessTestResultModel.getDevice_date()));
//
//
//                String[] arrStrSubTitle = (String[]) subTitle.toArray(new String[subTitle.size()]);
//                csvWrite.writeNext(arrStrSubTitle);
//                //DBManager db = new DBManager();
//                //db.updateFitnessTestTable(fitnessTestResultModel.getTest_type_id(),fitnessTestResultModel.getStudent_id());
//                subTitle.clear();
//            }
//
//
//            for(int i=0;i<transactionsSkillDataList.size();i++){
//                FitnessSkillTestResultModel fitnessSkillTestResultModel = ( FitnessSkillTestResultModel ) transactionsSkillDataList.get(i);
//                test_score = fitnessSkillTestResultModel.getScore();
//
//                StudentMasterModel studentMasterModel = (StudentMasterModel) DBManager.getInstance().getAllTableData(context, DBManager.TBL_LP_STUDENT_MASTER, "student_id", ""+fitnessSkillTestResultModel.getStudent_id(), "", "").get(0);
//                school_id = studentMasterModel.getCurrent_school_id();
//                test_type_id=""+fitnessSkillTestResultModel.getTest_type_id();
//                student_id=""+studentMasterModel.getStudent_id();
//                camp_id=studentMasterModel.getCamp_id();
//
//                subTitle.add(student_id);
//                subTitle.add(school_id);
//                subTitle.add(test_type_id);
//                // subTitle.add(fitnessTestResultModel.getTestName() + " (" + fitnessTestResultModel.getSubTestName() + ")");
//                subTitle.add(test_score);
//                subTitle.add(camp_id);
//                subTitle.add(Constant.ConvertDateToString(fitnessSkillTestResultModel.getDevice_date()));
//
//
//                String[] arrStrSubTitle = (String[]) subTitle.toArray(new String[subTitle.size()]);
//                csvWrite.writeNext(arrStrSubTitle);
//                //DBManager db = new DBManager();
//                //db.updateFitnessTestTable(fitnessTestResultModel.getTest_type_id(),fitnessTestResultModel.getStudent_id());
//                subTitle.clear();
//            }
//
//            csvWrite.close();
//            Utility.openSharingOptions(context,file);
//            return true;
//        }catch(Exception e){
//            return false;
//
//        }
//    }


    private boolean downloadDataToExport(){
       transactionsDataList = DBManager.getInstance().getAllTableData(context,
                testTableName, "tested", "true", "synced", "false");

//        transactionsSkillDataList = DBManager.getInstance().getAllTableData(context,
//                DBManager.TBL_LP_FITNESS_SKILL_TEST_RESULT,  "tested", "true","last_modified_on", "0");


        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("TestReportTOTSchool");

        // Create a Row
        Row headerRow = sheet.createRow(0);

        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }

        // Create Other rows and cells with contacts data
        int rowNum = 1;


        for(int i=0;i<transactionsDataList.size();i++){
            FitnessTestResultModel fitnessTestResultModel = ( FitnessTestResultModel ) transactionsDataList.get(i);

            //StudentMasterModel studentMasterModel = (StudentMasterModel) DBManager.getInstance().getAllTableData(context, DBManager.TBL_LP_STUDENT_MASTER, "student_id", ""+fitnessTestResultModel.getStudent_id(), "", "").get(0);
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(fitnessTestResultModel.getStudent_id());
            row.createCell(1).setCellValue(schoolId);
            row.createCell(2).setCellValue(fitnessTestResultModel.getTest_type_id());
            row.createCell(3).setCellValue(fitnessTestResultModel.getScore());
            row.createCell(4).setCellValue(fitnessTestResultModel.getCamp_id());
            row.createCell(5).setCellValue(Constant.ConvertDateToString(fitnessTestResultModel.getDevice_date()));
            row.createCell(6).setCellValue(fitnessTestResultModel.getTest_coordinator_id());
            //row.createCell(7).setCellValue(0);
            db.updateFitnessTestTable(testTableName,fitnessTestResultModel.getTest_type_id(),fitnessTestResultModel.getStudent_id());
        }

//        for(int i=0;i<transactionsSkillDataList.size();i++){
//            FitnessSkillTestResultModel fitnessSkillTestResultModel = ( FitnessSkillTestResultModel ) transactionsSkillDataList.get(i);
//
//            //StudentMasterModel studentMasterModel = (StudentMasterModel) DBManager.getInstance().getAllTableData(context, DBManager.TBL_LP_STUDENT_MASTER, "student_id", ""+fitnessSkillTestResultModel.getStudent_id(), "", "").get(0);
//            Row row = sheet.createRow(rowNum++);
//            row.createCell(0).setCellValue(fitnessSkillTestResultModel.getStudent_id());
//            row.createCell(1).setCellValue(schoolId);
//            row.createCell(2).setCellValue(fitnessSkillTestResultModel.getTest_type_id());
//            row.createCell(3).setCellValue(fitnessSkillTestResultModel.getScore());
//            row.createCell(4).setCellValue(fitnessSkillTestResultModel.getCamp_id());
//            row.createCell(5).setCellValue(Constant.ConvertDateToString(fitnessSkillTestResultModel.getDevice_date()));
//            row.createCell(6).setCellValue(fitnessSkillTestResultModel.getCoordinator_id());
//            row.createCell(7).setCellValue(fitnessSkillTestResultModel.getChecklist_item_id());
//            db.updateFitnessTestTable(DBManager.TBL_LP_FITNESS_SKILL_TEST_RESULT,fitnessSkillTestResultModel.getTest_type_id(),fitnessSkillTestResultModel.getStudent_id());
//
//        }

        File exportDir = new File(Environment.getExternalStorageDirectory() ,"");

        String fileName = schoolId+"_"+ testCoordinatorName +"_"+Utility.getDateTimeString(Calendar.getInstance().getTimeInMillis())+".xls";
        File file = new File(exportDir,fileName);
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }
        try {
            FileOutputStream fileOut = new FileOutputStream(file);
            workbook.write(fileOut);
            fileOut.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.w("check",e.getMessage());
        }catch (IOException e) {
            e.printStackTrace();
            Log.w("check",e.getMessage());
        }
        Utility.openSharingOptions(context,file, userName,testCoordinatorName,fileName);
        return true;
    }
}
