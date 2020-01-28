package kheloindia.com.assessment.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;

import kheloindia.com.assessment.AttendanceGeofencingActivity;
import kheloindia.com.assessment.functions.Constant;
import kheloindia.com.assessment.util.Utility;


/**
 * Created by Sandeep Rai on 03-03-2018.
 */

public class GetLocationService extends Service {

    private static final String TAG = "GetLocationService";
    private static final int LOCATION_INTERVAL = 10000; // in miliseconds
    private static final float LOCATION_DISTANCE = 1f; // in meters
    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };
    private LocationManager mLocationManager = null;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
     //   Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
          //  Log.e(TAG, "onLocationChanged: " + location.getLatitude() + " + " + location.getLongitude());
            mLastLocation.set(location);

            try {
                if (Double.parseDouble(Constant.TRACKING_LATITUDE) > 0 && Double.parseDouble(Constant.TRACKING_LONGITUDE) > 0 && Constant.GEOFENCE_RADIUS > 0) {
                    Utility.CalculateDistance(location.getLatitude(), location.getLongitude(), Double.parseDouble(Constant.TRACKING_LATITUDE), Double.parseDouble(Constant.TRACKING_LONGITUDE), Constant.GEOFENCE_RADIUS);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction(AttendanceGeofencingActivity.mBroadcastLatLngAction);
            broadcastIntent.putExtra("lat", location.getLatitude());
            broadcastIntent.putExtra("long", location.getLongitude());
             sendBroadcast(broadcastIntent);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }


}
