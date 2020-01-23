package kheloindia.com.assessment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import kheloindia.com.assessment.service.OnClearFromRecentService;

public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context mContext, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent mIntent = new Intent(mContext, OnClearFromRecentService.class);
            mIntent.putExtra("maxCountValue", 1000);
            OnClearFromRecentService.enqueueWork(mContext, mIntent);
        }
    }
}