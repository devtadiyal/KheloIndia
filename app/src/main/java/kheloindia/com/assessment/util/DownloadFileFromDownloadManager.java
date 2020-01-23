package kheloindia.com.assessment.util;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import java.io.File;
import java.util.ArrayList;

import kheloindia.com.assessment.R;

public class DownloadFileFromDownloadManager {
    Context context;
    private Bundle bundle;
    private ArrayList<Long> downloadsList = new ArrayList<>();
    private int trainerId;
    String appName;

    public DownloadFileFromDownloadManager(Context context, Bundle bundle) {
        this.context = context;
        this.bundle = bundle;
        appName = context.getString(R.string.app_name);
        downloadFile();
        createReciever();
    }

    private void downloadFile(){
        Log.w(AppConfig.TAG,"down file called");
        trainerId = bundle.getInt(AppConfig.TRAINER_ID);
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
//        Uri Download_Uri = Uri.parse(bundle.getString(AppConfig.HTML_FILE));
//
//        DownloadManager.Request request = new DownloadManager.re;
//        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
//        request.setTitle(trainerId + ".pdf");
//        request.setDescription("Downloading " + trainerId + ".pdf");
//        request.setVisibleInDownloadsUi(true);
//        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/"+appName+"/"  + "/" + trainerId + ".pdf");


//        assert downloadManager != null;
//        long refid = downloadManager.enqueue(request);
//        Log.w(AppConfig.TAG,refid+"");
//        downloadsList.add(refid);
    }

    private void createReciever(){
        BroadcastReceiver onComplete = new BroadcastReceiver() {
            public void onReceive(Context ctxt, Intent intent) {
                Log.w(AppConfig.TAG,"rec called");
                // get the refid from the download manager
                long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                // remove it from our downloadsList
                downloadsList.remove(referenceId);
                // if downloadsList is empty means all downloads completed
                if (downloadsList.isEmpty())
                    createNotification();
            }
        };
        context.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private void createNotification(){
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS), PendingIntent.FLAG_ONE_SHOT);;
       // Log.e("INSIDE", "" + referenceId);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);

        // for the versions marshmallow and nougat, there is a need of a white icon, bit for other versions, there is no need.
        if (Build.VERSION.SDK_INT==Build.VERSION_CODES.M || Build.VERSION.SDK_INT == Build.VERSION_CODES.N)
            mBuilder.setSmallIcon(R.drawable.dart_logo).setColor(context.getResources().getColor(R.color.colorPrimary));
        else
            mBuilder.setSmallIcon(R.drawable.dart_logo);

        //mBuilder.setSmallIcon(R.mipmap.app_logo);

        mBuilder.setContentTitle(appName).setContentText("Download completed").setContentIntent(pendingIntent).setAutoCancel(true);


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager!=null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //mBuilder.setChannelId(AppConfig.NOTIFICATION_CHANNEL_ID);
                notificationManager.createNotificationChannel(createNotificationChannelForOreo());
            }
            notificationManager.notify(455, mBuilder.build());
        }
    }

    @SuppressLint("NewApi")
    private NotificationChannel createNotificationChannelForOreo(){
        String notificationChannelName = "KheloIndia";
        @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel("abc", notificationChannelName,NotificationManager.IMPORTANCE_DEFAULT);
        notificationChannel.setLightColor(Color.GREEN);
        return notificationChannel;
    }

    private PendingIntent createPendingIntent(){
        File file = new File(Environment.DIRECTORY_DOWNLOADS+"/"+ trainerId +".pdf");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        return PendingIntent.getActivity(context, 0, new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS), PendingIntent.FLAG_ONE_SHOT);
    }
}
