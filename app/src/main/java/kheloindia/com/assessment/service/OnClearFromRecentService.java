package kheloindia.com.assessment.service;

import android.content.Context;
import android.content.Intent;
import androidx.core.app.JobIntentService;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import kheloindia.com.assessment.functions.Constant;
import kheloindia.com.assessment.util.DBManager;


public class OnClearFromRecentService extends JobIntentService {

    private int extra;
    private Intent in;

    public OnClearFromRecentService() {
        super();
    }

    private static final int JOB_ID = 2;

    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, OnClearFromRecentService.class, JOB_ID, intent);
    }

    @Override
    protected void onHandleWork(Intent intent) {
        System.out.println("Job Value " + intent.getIntExtra("fitnessTestResultModel", 0));
        if (intent != null)
            extra = intent.getIntExtra("fitnessTestResultModel", 0);
        if (extra == 1) {
            //  DBManager.getInstance().deleteAllStuff(OnClearFromRecentService.this, DBManager.TBL_LP_FITNESS_TEST_RESULT, Constant.fitnessTestResultModels,"student_id","test_type_id","camp_id");
            DBManager.getInstance().insertBatchTestData(DBManager.TBL_LP_FITNESS_TEST_RESULT, Constant.fitnessTestResultModels, this);
            Constant.fitnessTestResultModels.clear();
            Constant.test_running = false;
            Constant.imported_count = DBManager.getInstance().getTotalCount(OnClearFromRecentService.this,
                    DBManager.TBL_LP_FITNESS_TEST_RESULT, "tested", "true", "synced", "true", "camp_id", Constant.CAMP_ID) + DBManager.getInstance().getTotalCount(OnClearFromRecentService.this,
                    DBManager.TBL_LP_FITNESS_SKILL_TEST_RESULT, "tested", "true", "synced", "1", "camp_id", Constant.CAMP_ID);

            in = new Intent("intentKey");
            in.putExtra("extra", extra);
            LocalBroadcastManager.getInstance(OnClearFromRecentService.this).sendBroadcast(in);

            System.out.println("Job Execution 1");

        } else if (extra == 2) {
            //   DBManager.getInstance().deleteAllStuff(OnClearFromRecentService.this, DBManager.TBL_LP_FITNESS_SKILL_TEST_RESULT, Constant.fitnessSkillTestResultModels,"student_id","checklist_item_id","camp_id");
            DBManager.getInstance().insertBatchTestData(DBManager.TBL_LP_FITNESS_SKILL_TEST_RESULT, Constant.fitnessSkillTestResultModels, this);
            Constant.fitnessSkillTestResultModels.clear();
            Constant.skill_test_running = false;
            Constant.imported_count = DBManager.getInstance().getTotalCount(OnClearFromRecentService.this, DBManager.TBL_LP_FITNESS_TEST_RESULT, "tested", "true", "synced", "true", "camp_id", Constant.CAMP_ID) + DBManager.getInstance().getTotalCount(OnClearFromRecentService.this,
                    DBManager.TBL_LP_FITNESS_SKILL_TEST_RESULT, "tested", "true", "synced", "1", "camp_id", Constant.CAMP_ID);
            in = new Intent("intentKey");
            in.putExtra("extra", extra);
            LocalBroadcastManager.getInstance(OnClearFromRecentService.this).sendBroadcast(in);


        } else if (extra == 3) {
            // DBManager.getInstance().deleteAllStuff(OnClearFromRecentService.this, DBManager.TBL_LP_FITNESS_TEST_RESULT, Constant.fitnessTestResultModels,"student_id","test_type_id","camp_id");
            DBManager.getInstance().insertBatchTestData(DBManager.TBL_LP_FITNESS_TEST_RESULT, Constant.fitnessTestResultModels, this);
            Constant.fitnessTestResultModels.clear();
            in = new Intent("intentKey");
            in.putExtra("extra", extra);
            LocalBroadcastManager.getInstance(OnClearFromRecentService.this).sendBroadcast(in);

        } else if (extra == 4) {
            //DBManager.getInstance().deleteAllStuff(OnClearFromRecentService.this, DBManager.TBL_LP_FITNESS_SKILL_TEST_RESULT, Constant.fitnessSkillTestResultModels,"student_id","checklist_item_id","camp_id");
            DBManager.getInstance().insertBatchTestData(DBManager.TBL_LP_FITNESS_SKILL_TEST_RESULT, Constant.fitnessSkillTestResultModels, this);
            Constant.fitnessSkillTestResultModels.clear();
            in = new Intent("intentKey");
            in.putExtra("extra", extra);
            LocalBroadcastManager.getInstance(OnClearFromRecentService.this).sendBroadcast(in);

            System.out.println("Job Execution 4");

        }

    }


    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, startId, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("Job Execution Started");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("Job Execution Finished");

    }

}