package kheloindia.com.assessment.util;

import android.content.Context;
import android.preference.PreferenceManager;

import static kheloindia.com.assessment.util.DBManager.TBL_LP_FITNESS_RETEST_RESULT;
import static kheloindia.com.assessment.util.DBManager.TBL_LP_FITNESS_TEST_RESULT;

/**
 * Created by CT13 on 2017-09-18.
 */

public class TestReportQuery {

    public static String GetReportQuery(Context iContext,String CampID, String SchoolID, String TestCategoryID, String ClassID, String TestCompleted)
    {
        //checklist_item_id integer not null, item_name text not null, test_type_id

        String testTableName = TBL_LP_FITNESS_TEST_RESULT;
        if (PreferenceManager.getDefaultSharedPreferences(iContext).getString(AppConfig.FOR_RETEST,"0")
                .equalsIgnoreCase("1"))
            testTableName = TBL_LP_FITNESS_RETEST_RESULT;

        String report_query = "SELECT "+
                "distinct LSM.student_registration_num, LSM.Student_Name, LSM.Current_School_ID, LSM.Student_ID,LSM.Gender,\n" +
                "LTC.Test_Name ,CASE WHEN FTRA.Score IS NULL THEN 2 ELSE 1 END as 'TestCompleted', \n" +
                "\t\tLFTT.sub_test_ID AS SubTestTypeID,LFTT.Test_Name AS SubTestName,FTRA.Score,FTRA.Total,LFTT.Tests_Applicable,LSM.class_id  FROM \n" +
                "\t\t(\n" +
                "\t\t\tselect student_registration_num, Student_Name, Current_School_ID,Student_ID,\n" +
                "\t\t\tCase when Gender = '0' then 'M'  else 'f' end as Gender,\n" +
                "\t\t\tLP_Student_Master.Class_ID ,\n" +
                "\t\t\tLP_Fitness_Test_Types.Test_Type_ID,current_class from LP_Student_Master,LP_Fitness_Test_Types where Current_School_ID = "+SchoolID+"\n" +
                "\t\t\tand camp_id = "+CampID+"\n" +
                "\t\t\tand student_registration_num is not null\n" +
                "\t\t) AS LSM \n" +
                "\t\tINNER JOIN LP_Fitness_Test_Category as LFTT ON LSM.Test_Type_ID = lftt.Test_ID  \n" +
                "\t\tLEFT JOIN \n" +
                "\t\t(SELECT [Student_ID],[Camp_ID],[Test_Type_ID],[Score], 0 as Total  FROM "+ testTableName+" where camp_id = "+CampID+" \n" +
                "\t\t\tunion all select Student_ID,Camp_ID,Test_Type_ID,sum(case  when score = 1  then 1 else 0 end) AS Score,count(score) as total from \n" +
                "\t\t\tlp_fitness_skill_test_result where camp_id = "+CampID+" \n" +
                "\t\t\tgroup by Student_ID,Camp_ID,Test_Type_ID)\n" +
                "\t\tAS FTRA ON LSM.Student_ID= FTRA.Student_ID  \n" +
                "\t\tAND  FTRA.Test_Type_ID = LFTT.Sub_test_ID\n" +
                "\t\tLEFT JOIN LP_Fitness_Test_Types AS LTC ON ltc.Test_Type_ID = LFTT.Test_ID \n" +
                "\t\t where ('"+ClassID.replace("'","")+"' = 'All' OR  lsm.current_class IN ("+ClassID+") )\n" +
                "\t\t AND  ('"+TestCategoryID.replace("'","")+"' = '0' OR LFTT.Sub_test_ID  IN ("+TestCategoryID+") )  \n" +
                "\t\t AND  ("+TestCompleted+" = 0 OR ifnull(FTRA.Test_Type_ID,0)="+TestCompleted+"-2  OR (ifnull(FTRA.Test_Type_ID,0) >0 AND "+TestCompleted+"=1)) \n" +

                "\t\t ORDER BY LSM.Student_Name,LSM.student_registration_num ";
       // report_query = " select skill_score_id,student_id,test_type_id,score from lp_fitness_skill_test_result ";

        return report_query;
    }
    /*public static String report_query = "SELECT "+
            "distinct LSM.student_registration_num, LSM.Student_Name, LSM.Current_School_ID, LSM.Student_ID,LSM.Gender,\n" +
            "LTC.Test_Name ,CASE WHEN FTRA.Score IS NULL THEN 2 ELSE 1 END as 'TestCompleted', \n" +
            "\t\tLFTT.sub_test_ID AS SubTestTypeID,LFTT.Test_Name AS SubTestName FROM \n" +
            "\t\t(\n" +
            "\t\t\tselect student_registration_num, Student_Name, Current_School_ID,Student_ID,\n" +
            "\t\t\tCase when Gender = '0' then 'M'  else 'f' end as Gender,\n" +
            "\t\t\tLP_Student_Master.Class_ID ,\n" +
            "\t\t\tLP_Fitness_Test_Types.Test_Type_ID from LP_Student_Master,LP_Fitness_Test_Types where Current_School_ID = 45\n" +
            "\t\t\tand student_registration_num is not null\n" +
            "\t\t) AS LSM \n" +
            "\t\tINNER JOIN LP_Fitness_Test_Category as LFTT ON LSM.Test_Type_ID = lftt.Test_ID  \n" +
            "\t\tLEFT JOIN \n" +
            "\t\t(SELECT [Student_ID],[Camp_ID],[Test_Type_ID],[Score] FROM LP_Fitness_Test_Result where camp_id = 129\n" +
            "\t\t\tunion all select Student_ID,Camp_ID,Test_Type_ID,count( case  when score = 1  then 1 else 0 end) AS Score from \n" +
            "\t\t\tlp_fitness_skill_test_result where camp_id = 129 \n" +
            "\t\t\tgroup by Student_ID,Camp_ID,Test_Type_ID)\n" +
            "\t\tAS FTRA ON LSM.Student_ID= FTRA.Student_ID  \n" +
            "\t\tAND  FTRA.Test_Type_ID = LFTT.Sub_test_ID\n" +
            "\t\tLEFT JOIN LP_Fitness_Test_Types AS LTC ON ltc.Test_Type_ID = LFTT.Test_ID \n" +
            "\t\t ORDER BY LSM.Student_ID ";
            */

}
