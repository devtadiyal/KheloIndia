package kheloindia.com.assessment.app;


import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.SystemClock;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import java.util.Calendar;

import kheloindia.com.assessment.receiver.AutoSyncReceiver;
import kheloindia.com.assessment.service.LocationService;
import kheloindia.com.assessment.util.ConnectivityReceiverListener;

/**
 * Created by CT13 on 2017-06-23.
 */

public class Fitness365App extends MultiDexApplication {

    private static Fitness365App mInstance;
    private static Application sApplication;
    public ConnectivityReceiverListener connectivityReceiverListener;
    public AlarmManager alarm;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        mInstance = this;
        sApplication = this;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(new AutoSyncReceiver(), intentFilter);
     //   TrackTrainer();

    }

    public void TrackTrainer() {
        long interval = 1000 * 60 * 15; // 15 minutes in milliseconds

        // hour = 36000 * 1000;

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 10);
        Intent intent = new Intent(getApplicationContext(), LocationService.class);
        PendingIntent pintent = PendingIntent.getService(getApplicationContext(), 0, intent,
                0);
     /*   PendingIntent pintent = PendingIntent.getBroadcast(DashBoardActivity.this, 0, intent,
                0);*/
        AlarmManager alarm = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
       /* alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                interval, pintent);*/
        alarm.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(),
                interval,pintent);
    }

    public static synchronized Fitness365App getInstance() {
            return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiverListener listener) {
       connectivityReceiverListener = listener;
    }

    public static Application getApplication() {
        return sApplication;
    }

    public static Context getContext() {
        return getApplication().getApplicationContext();
    }

}
