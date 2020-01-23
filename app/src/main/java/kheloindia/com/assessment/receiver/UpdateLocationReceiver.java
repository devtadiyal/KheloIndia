package kheloindia.com.assessment.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import kheloindia.com.assessment.functions.GPSTracker1;
import kheloindia.com.assessment.util.ConnectionDetector;

/**
 * Created by PC10 on 17-Apr-18.
 */

public class UpdateLocationReceiver extends BroadcastReceiver  {

    private ConnectionDetector connectionDetector;
    private Context context;
    GPSTracker1 gps;
    String TAG = "UpdateLocationReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;
        connectionDetector = new ConnectionDetector(context);

        Log.e(TAG,"********* on Receive (UpdateLocationReceiver) *******");

        /*long interval = 1000 * 60 * 1; // 15 minutes in milliseconds

        // hour = 36000 * 1000;

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 10);
        Intent i = new Intent(context, LocationService.class);
        PendingIntent pintent = PendingIntent.getService(context, 0, i,
                0);
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                interval, pintent);*/

    }
}
