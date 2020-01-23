package kheloindia.com.assessment;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.Base64;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import kheloindia.com.assessment.model.AttendancetrackModel;
import kheloindia.com.assessment.model.MarkAttendanceModel;
import kheloindia.com.assessment.model.MarkAttendanceModel1;
import kheloindia.com.assessment.model.ProfileImageModel;
import kheloindia.com.assessment.model.TrackModel;
import kheloindia.com.assessment.service.GetLocationService;
import kheloindia.com.assessment.util.ConnectionDetector;
import kheloindia.com.assessment.util.ResponseListener;
import kheloindia.com.assessment.util.Utility;
import kheloindia.com.assessment.functions.Constant;
import kheloindia.com.assessment.functions.GPSTracker1;
import kheloindia.com.assessment.webservice.ApiClient;
import kheloindia.com.assessment.webservice.ApiRequest;
import kheloindia.com.assessment.webservice.AttendanceTrackRequest;
import kheloindia.com.assessment.webservice.markAttendanceRequest;
import kheloindia.com.assessment.webservice.markAttendanceRequest1;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/*import im.delight.android.location.SimpleLocation;*/

/**
 * Created by PC10 on 06-Mar-18.
 */

public class AttendanceGeofencingActivity extends AppCompatActivity implements View.OnClickListener,
        OnMapReadyCallback, GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener, ResponseListener {

    Toolbar toolbar;
    TextView school_name_tv;
    ImageView profile_img1;
    TextView distance_tv;
    private GoogleMap map;
    Context ctx;
    private MapFragment mapFragment;
    private IntentFilter mIntentFilter;
    private Marker locationMarker;
    Boolean alreadyZoom = false;
    private Marker geoFenceMarker;
    private Circle geoFenceLimits;
    private ConnectionDetector connectionDetector;
    public static final String mBroadcastLatLngAction = "Geofence Location";
    SharedPreferences sp;
    private Dialog selfieDialog;
    double distanceInMeters = 0;
    double distanceInkm = 0;
    public static Uri mUri;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final String IMAGE_DIRECTORY_NAME = "Fitness365_Images";
    public static Bitmap mPhoto;
    File imageFile;
    String Base64Image;
    ArrayList<HashMap<String, String>> TrackingList = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> TrackingListMorning = new ArrayList<HashMap<String, String>>();
    TextView profile_text_tv;

    ImageView profile_img;

    MarkerOptions markerOptions1;
    GPSTracker1 gps;

    String school_id = "";
    String school_name = "";
    double latitude;
    double longitude;

    TextView date_tv;
    Button mark_attendance_btn, day_end_btn;

    String address = "";
    String diatnce_from_destination = "0";

    String day_start = "0", day_end = "0";
    String current_date = "";

    LinearLayout history_layout, history_layout1, ll1;

    RelativeLayout relative_layout, relative_layout1;

    String TAG = "AttendanceGeofencingActivity";

  //  SimpleLocation simpleLocation;

    // Here Receiving Broadcast sent through service
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(mBroadcastLatLngAction)) {
                Log.e(TAG, "********* on Receive (AttendanceGeofencingActivity) *******");
                Constant.SOURCE_LAT = intent.getDoubleExtra("lat", 0.0);
                Constant.SOURCE_LNG = intent.getDoubleExtra("long", 0.0);
                Log.e(TAG, "lat==> " + Constant.SOURCE_LAT + " long==> " + Constant.SOURCE_LNG);
                // lat_tv.setText("Lat: " + SOURCE_LAT);
                // lon_tv.setText("Long: " + SOURCE_LNG);

              /*  gps = new GPSTracker1(getApplicationContext());
                Constant.SOURCE_LAT = gps.getLatitude();
                Constant.SOURCE_LNG = gps.getLongitude();*/

                // Setting marker on Current Loc
                markerLocation(new LatLng(Constant.SOURCE_LAT, Constant.SOURCE_LNG));

                markerForGeofence();


            }
        }
    };

    private void markerForGeofence() {
        //28.6008043,77.0988298

        // Define marker options

       /* Constant.DESTINATION_LAT = latitude;
        Constant.DESTINATION_LNG = longitude;*/

        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        Constant.TRACKING_LATITUDE = sp.getString("tracking_latitude", "0");
        Constant.TRACKING_LONGITUDE = sp.getString("tracking_longitude", "0");

        String title = getAddress(Double.parseDouble(Constant.TRACKING_LATITUDE), Double.parseDouble(Constant.TRACKING_LONGITUDE));

        LatLng latLng = new LatLng(Double.parseDouble(Constant.TRACKING_LATITUDE), Double.parseDouble(Constant.TRACKING_LONGITUDE));

        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .title(title);
        if (map != null) {
            // Remove last geoFenceMarker
            if (geoFenceMarker != null)
                geoFenceMarker.remove();

            geoFenceMarker = map.addMarker(markerOptions);
          //  geoFenceMarker.showInfoWindow();
          //  geoFenceMarker.showInfoWindow();
          //  geoFenceMarker.showInfoWindow();

        }


        drawGeofence();
        CalculateDistance(Constant.SOURCE_LAT, Constant.SOURCE_LNG, Double.parseDouble(Constant.TRACKING_LATITUDE), Double.parseDouble(Constant.TRACKING_LONGITUDE));
    }




    private void CalculateDistance(double sourceLat, double sourceLng, double destinationLat, double destinationLng) {
        Location loc1 = new Location("");
        loc1.setLatitude(sourceLat);
        loc1.setLongitude(sourceLng);

        Location loc2 = new Location("");
        loc2.setLatitude(destinationLat);
        loc2.setLongitude(destinationLng);

        LatLng location1 = new LatLng(sourceLat, sourceLng);
        LatLng location2 = new LatLng(destinationLat, destinationLng);

        distanceInMeters = loc1.distanceTo(loc2);

        distanceInkm = distanceInMeters / 1000;
        distanceInMeters = Math.round(distanceInMeters * 100.0) / 100.0;

        distanceInkm = Math.round(distanceInkm * 100.0) / 100.0;
        diatnce_from_destination = String.valueOf(distanceInMeters);


        Log.e(TAG, "sourceLat==> " + sourceLat);
        Log.e(TAG, "sourceLng==> " + sourceLng);
        Log.e(TAG, "destinationLat==> " + destinationLat);
        Log.e(TAG, "destinationLng==> " + destinationLng);


        distance_tv.setText("You are " + distanceInkm + " km away from the " + Constant.GOING_TO + ".");

        //  Toast.makeText(getApplicationContext(),"distance==> "+distanceInMeters+" meters",Toast.LENGTH_LONG).show();
    }


    private String getAddress(double lat, double lng) {
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());
        String address = "";

        try {
            addresses = geocoder.getFromLocation(lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();

            //  Log.e(TAG,"address==> "+address);
            //  Log.e(TAG,"city==> "+city);
            //  Log.e(TAG,"state==> "+state);
            //  Log.e(TAG,"country==> "+country);
            //  Log.e(TAG,"postalCode==> "+postalCode);
            //  Log.e(TAG,"knownName==> "+knownName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return address;
    }

    // this method used to draw geofencing on map
    private void drawGeofence() {
        Log.d(TAG, "drawGeofence()");

        if (geoFenceLimits != null)
            geoFenceLimits.remove();
        CircleOptions circleOptions = new CircleOptions()
                .center(geoFenceMarker.getPosition())
                .strokeColor(Color.argb(50, 70, 70, 70))
                .fillColor(Color.argb(100, 150, 150, 150))
                .radius(Constant.GEOFENCE_RADIUS);
        geoFenceLimits = map.addCircle(circleOptions);
    }

    private void markerLocation(LatLng latLng) {
        Log.i(TAG, "markerLocation(" + latLng + ")");

        String day_start = sp.getString("day_start"+Constant.TEST_COORDINATOR_ID, "0");
        String day_end = sp.getString("day_end"+Constant.TEST_COORDINATOR_ID, "0");
        String END = sp.getString("END", "0");

        if (school_id.equals(sp.getString("tracking_school_id", "0"))) {

            Log.e(TAG, " if  if  if");

            if (day_start.equals(current_date)) {
                if (day_end.equals(current_date)) {
                    mark_attendance_btn.setVisibility(View.GONE);
                    day_end_btn.setVisibility(View.GONE);
                } else {
                    mark_attendance_btn.setVisibility(View.GONE);
                    day_end_btn.setVisibility(View.VISIBLE);
                }

            } else {
                mark_attendance_btn.setVisibility(View.VISIBLE);
                day_end_btn.setVisibility(View.GONE);
            }
        } else if (END.equals("1")) {
            Log.e(TAG, "else if else if else if");
            mark_attendance_btn.setVisibility(View.VISIBLE);
            day_end_btn.setVisibility(View.GONE);
        } else {

            Log.e(TAG, "else else else");
            if (day_start.equals(current_date)) {
                mark_attendance_btn.setVisibility(View.GONE);
                day_end_btn.setVisibility(View.GONE);
            } else {
                mark_attendance_btn.setVisibility(View.VISIBLE);
                day_end_btn.setVisibility(View.GONE);
            }

            //      Toast.makeText(getApplicationContext(),"You have already started your day in some other school. Please mark your day end to start the day again in this school.", Toast.LENGTH_LONG).show();
        }

            /*else {
                mark_attendance_btn.setVisibility(View.VISIBLE);
                day_end_btn.setVisibility(View.GONE);
            }*/
        //    }

       /* else {
            mark_attendance_btn.setVisibility(View.VISIBLE);
            day_end_btn.setVisibility(View.GONE);
        }*/


      /*  String title = getAddress(latLng.latitude, latLng.longitude);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title(title);*/

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setCompassEnabled(true);


        map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                String title = getAddress(ll.latitude, ll.longitude);



                 markerOptions1 = new MarkerOptions()
                        .position(ll)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                        .title(title);


                if (locationMarker != null)
                    locationMarker.remove();
                   locationMarker = map.addMarker(markerOptions1);
                locationMarker.showInfoWindow();


               /* float zoom = 14f;
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(ll, zoom);
                map.animateCamera(cameraUpdate);
                map.getUiSettings().setCompassEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
                map.getUiSettings().setZoomControlsEnabled(true);*/

                String dateText = Utility.DisplayDateInParticularFormat("EEE dd MMM yyyy hh:mm a");
                date_tv.setText(dateText);


                Constant.SOURCE_LAT = location.getLatitude();
                Constant.SOURCE_LNG = location.getLongitude();
                CalculateDistance(Constant.SOURCE_LAT, Constant.SOURCE_LNG, Double.parseDouble(Constant.TRACKING_LATITUDE), Double.parseDouble(Constant.TRACKING_LONGITUDE));

                distance_tv.setText("You are " + distanceInkm + " km away from the " + Constant.GOING_TO + ".");
            }
        });

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        if (map != null) {
          /*  if (locationMarker != null)
                locationMarker.remove();
            locationMarker = map.addMarker(markerOptions);
            locationMarker.showInfoWindow();*/
            float zoom = 14f;
            if (!alreadyZoom) {
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
                map.animateCamera(cameraUpdate);
                map.getUiSettings().setCompassEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
                map.getUiSettings().setZoomControlsEnabled(true);

                alreadyZoom = true;
            }

        }

    }

    @Override
    protected void onResume() {

        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String imagePath = sp.getString("selfie_pic_url","");
        if (imagePath.length()>1){
            String path = getApplicationContext().getResources().getString(R.string.image_base_url)+imagePath;
            Log.e(TAG,"path==> "+path);
            Picasso.with(ctx).load(path).into(profile_img);
        }


        Constant.TRACKING_LATITUDE = sp.getString("tracking_latitude","0");
        Constant.TRACKING_LONGITUDE = sp.getString("tracking_longitude","0");

        school_name = Constant.TRACKING_SCHOOL_NAME;
        school_id = Constant.TRACKING_SCHOOL_ID;
        latitude = Double.parseDouble(Constant.TRACKING_LATITUDE);
        longitude = Double.parseDouble(Constant.TRACKING_LONGITUDE);


        current_date = new SimpleDateFormat("dd").format(new Date());

        String day_start = sp.getString("day_start"+Constant.TEST_COORDINATOR_ID, "0");
        String day_end = sp.getString("day_end"+Constant.TEST_COORDINATOR_ID, "0");
        String END = sp.getString("END","0");

        if (school_id.equals(sp.getString("tracking_school_id", "0"))) {

            if (day_start.equals(current_date)) {
                if (day_end.equals(current_date)) {
                    mark_attendance_btn.setVisibility(View.GONE);
                    day_end_btn.setVisibility(View.GONE);
                } else {
                    mark_attendance_btn.setVisibility(View.GONE);
                    day_end_btn.setVisibility(View.VISIBLE);
                }

            } else {
                mark_attendance_btn.setVisibility(View.VISIBLE);
                day_end_btn.setVisibility(View.GONE);
            }

           /* else {
                mark_attendance_btn.setVisibility(View.VISIBLE);
                day_end_btn.setVisibility(View.GONE);
            }*/
        } else if(END.equals("1")){
            mark_attendance_btn.setVisibility(View.VISIBLE);
            day_end_btn.setVisibility(View.GONE);
        } else {
            if (day_start.equals(current_date)) {
                mark_attendance_btn.setVisibility(View.GONE);
                day_end_btn.setVisibility(View.GONE);
            } else {
                mark_attendance_btn.setVisibility(View.VISIBLE);
                day_end_btn.setVisibility(View.GONE);
            }

          //  Toast.makeText(getApplicationContext(),"You have already started your day in some other school. Please mark your day end to start the day again in this school.", Toast.LENGTH_LONG).show();
        }

      /*  else {
           // mark_attendance_btn.setVisibility(View.VISIBLE);
            day_end_btn.setVisibility(View.GONE);
        }*/


       /* if (Constant.SELFIE_FILE != null) {
          // Picasso.with(AttendanceGeofencingActivity.this).load(Constant.SELFIE_FILE).transform(new CircleTransformWhite()).into(profile_img);

            Picasso.with(AttendanceGeofencingActivity.this).load(Constant.SELFIE_FILE).into(profile_img);
            Log.e("Constant.SELFIE_FILE ==>", "" + Constant.SELFIE_FILE);
            profile_text_tv.setText("You look great today !");
        } else {
            profile_text_tv.setText("Upload your pic !");
        }*/


        if (Constant.SELFIE_BITMAP != null) {
            // Picasso.with(AttendanceGeofencingActivity.this).load(Constant.SELFIE_FILE).transform(new CircleTransformWhite()).into(profile_img);

           profile_img.setImageBitmap(Constant.SELFIE_BITMAP);
          //  Log.e("Constant.SELFIE_BITMAP ==>", "" + Constant.SELFIE_BITMAP);
            profile_text_tv.setText("You look great today !");
        } else {
            profile_img.setImageResource(R.drawable.pro_pic);
            profile_text_tv.setText("Upload your pic !");
        }


        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        setContentView(R.layout.activity_school_geofencing);

        current_date = new SimpleDateFormat("dd").format(new Date());

        gps = new GPSTracker1(getApplicationContext());

       // simpleLocation = new SimpleLocation(this);

        // if we can't access the location yet
      /*  if (!simpleLocation.hasLocationEnabled()) {
            // ask the user to enable location access
            SimpleLocation.openSettings(this);
        }
*/

        init();
    }

    private void init() {
        ctx = AttendanceGeofencingActivity.this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        profile_img1 = (ImageView) findViewById(R.id.profile_img1);
        school_name_tv = (TextView) findViewById(R.id.school_name_tv);
        date_tv = (TextView) findViewById(R.id.date_tv);
        mark_attendance_btn = (Button) findViewById(R.id.mark_attendance_btn);
        day_end_btn = (Button) findViewById(R.id.day_end_btn);
        distance_tv = (TextView) findViewById(R.id.distance_tv);
        history_layout = (LinearLayout) findViewById(R.id.history_layout);
        history_layout1 = (LinearLayout) findViewById(R.id.history_layout1);
        profile_img = (ImageView) findViewById(R.id.profile_img);
        profile_text_tv = (TextView) findViewById(R.id.profile_text_tv);
        relative_layout = (RelativeLayout) findViewById(R.id.relative_layout);
        relative_layout1 = (RelativeLayout) findViewById(R.id.relative_layout1);
        ll1 = (LinearLayout) findViewById(R.id.ll1);

      //  ll1.setVisibility(View.GONE);
        relative_layout1.setVisibility(View.GONE);
        relative_layout.setVisibility(View.GONE);
        mark_attendance_btn.setVisibility(View.GONE);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

         /*school_name = getIntent().getStringExtra("school_name");
         school_id = getIntent().getStringExtra("school_id");

        String lat = getIntent().getStringExtra("latitude");
        String lng = getIntent().getStringExtra("longitude");

        latitude = Double.parseDouble(getIntent().getStringExtra("latitude"));
        longitude = Double.parseDouble(getIntent().getStringExtra("longitude"));*/


        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        Constant.TRACKING_LATITUDE = sp.getString("tracking_latitude","0");
        Constant.TRACKING_LONGITUDE = sp.getString("tracking_longitude","0");


        school_name = Constant.TRACKING_SCHOOL_NAME;
        school_id = Constant.TRACKING_SCHOOL_ID;

        try {
            latitude = Double.parseDouble(Constant.TRACKING_LATITUDE);
            longitude = Double.parseDouble(Constant.TRACKING_LONGITUDE);
        } catch (Exception e){

        }


        Log.e(TAG, "school_name==> " + school_name + " school_id==> " + school_id);
        Log.e(TAG, "Lat==> " + latitude + " Lng==> " + longitude);


        school_name_tv.setText(school_name);

        String day_start = sp.getString("day_start"+Constant.TEST_COORDINATOR_ID, "0");
        String day_end = sp.getString("day_end"+Constant.TEST_COORDINATOR_ID, "0");

        if (day_start.equals(current_date)) {
            if (day_end.equals(current_date)) {
                mark_attendance_btn.setVisibility(View.GONE);
                day_end_btn.setVisibility(View.GONE);
            } else {
                mark_attendance_btn.setVisibility(View.GONE);
              //  day_end_btn.setVisibility(View.VISIBLE);
            }

        } else {
         //   mark_attendance_btn.setVisibility(View.VISIBLE);
            day_end_btn.setVisibility(View.GONE);
        }


        String dateText = Utility.DisplayDateInParticularFormat("EEE dd MMM yyyy hh:mm a");

        //  date_tv.setVisibility(View.GONE);
        date_tv.setText(dateText);

        checkPermission();

        mark_attendance_btn.setOnClickListener(this);
        profile_img1.setOnClickListener(this);
        day_end_btn.setOnClickListener(this);

        LocationManager lm = (LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}


        /*if (!((LocationManager) getSystemService("location"))
                .isProviderEnabled("gps")) {
            // Displaying Dialog to open gps
            Utility.showGPSDisabledAlertToUser(ctx);
        }*/

        if (gps_enabled || network_enabled) {
            // Displaying Dialog to open gps

        } else {
            Utility.showGPSDisabledAlertToUser(ctx);
        }

        initGMaps();
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(mBroadcastLatLngAction);

        Intent serviceIntent = new Intent(this, GetLocationService.class);
        startService(serviceIntent);

        connectionDetector = new ConnectionDetector(this);

        if (connectionDetector.isConnectingToInternet()) {
           /* "Trainer_id":"12563",
                    "School_id":"54",
                    "Created_On":"2017-02-07 01:16:10.197"*/

            String Day_Status = "0";

            String activity_id = "1";

            String inputFormat = "yyyy-MM-dd HH:mm:ss:SSS";
            String currentDate = Utility.DisplayDateInParticularFormat(inputFormat);

            String school_name = getIntent().getStringExtra("school_name");
            String school_id = getIntent().getStringExtra("school_id");

            AttendanceTrackRequest request = new AttendanceTrackRequest(this, Constant.TEST_COORDINATOR_ID,
                    school_id, currentDate, this);
            request.hitAttendanceRequest();
        } else {
            Toast.makeText(getApplicationContext(), "Internet not available", Toast.LENGTH_SHORT).show();
        }

    }


    private void checkPermission() {

        // Ask for permission if it wasn't granted yet

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
    }

    // Initialize GoogleMaps
    private void initGMaps() {
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.profile_img1:
                Intent i = new Intent(AttendanceGeofencingActivity.this, AttendanceProfileActivity.class);
                i.putExtra("school_name", school_name);
                i.putExtra("school_id", school_id);
                startActivity(i);
                break;

            case R.id.mark_attendance_btn:
               /* SharedPreferences.Editor e = sp.edit();
                e.putBoolean("day_start", true);
                e.commit();

                mark_attendance_btn.setVisibility(View.GONE);
                day_end_btn.setVisibility(View.VISIBLE);*/


                connectionDetector = new ConnectionDetector(this);
                if (connectionDetector.isConnectingToInternet()) {
                    /*"Trainer_Id":"15421",
                            "School_Id":"51",
                            "Activity_Id":"1",
                            "Created_On":"07 Feb 2017 1:16:10:196",
                            "Latitude":"23.00",
                            "Longitude":"52.00"*/

                    String Day_Status = "0";

                    String activity_id = "1";

                    String currentDate = Utility.DisplayDateInParticularFormat(Constant.inputFormat);

                    address = getAddress(Constant.SOURCE_LAT, Constant.SOURCE_LNG);

                    if(distanceInkm<=0.5) {
                        markAttendanceRequest request = new markAttendanceRequest(this, Constant.TEST_COORDINATOR_ID,
                                school_id, activity_id, currentDate, "" + Constant.SOURCE_LAT,
                                "" + Constant.SOURCE_LNG, Day_Status, address, diatnce_from_destination, this);
                        request.hitAttendanceRequest();
                    } else {

                        Toast.makeText(getApplicationContext(), "You are "+distanceInkm+" km away from the school. please reach school to mark the attendance.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Internet not available", Toast.LENGTH_SHORT).show();
                }



                break;

            case R.id.day_end_btn:

                connectionDetector = new ConnectionDetector(this);
                if (connectionDetector.isConnectingToInternet()) {
                    /*"Trainer_Id":"15421",
                            "School_Id":"51",
                            "Activity_Id":"1",
                            "Created_On":"07 Feb 2017 1:16:10:196",
                            "Latitude":"23.00",
                            "Longitude":"52.00"*/

                    String Day_Status = "1";

                    String activity_id = "1";

                    String currentDate = Utility.DisplayDateInParticularFormat(Constant.inputFormat);

                    address = getAddress(Constant.SOURCE_LAT, Constant.SOURCE_LNG);

                    if(distanceInkm<=0.5) {
                        markAttendanceRequest1 request1 = new markAttendanceRequest1(this, Constant.TEST_COORDINATOR_ID,
                                school_id, activity_id, currentDate, "" + Constant.SOURCE_LAT,
                                "" + Constant.SOURCE_LNG, Day_Status, address, diatnce_from_destination, this);
                        request1.hitAttendanceRequest();
                    }else {

                        Toast.makeText(getApplicationContext(), "You are "+distanceInkm+" km away from the school. please be in school to end your day.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Internet not available", Toast.LENGTH_SHORT).show();
                }

               /* SharedPreferences.Editor e1 = sp.edit();
                e1.putBoolean("day_start", false);
                e1.commit();

                mark_attendance_btn.setVisibility(View.VISIBLE);
                day_end_btn.setVisibility(View.GONE);*/

                break;
        }

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.e(TAG, "onMapReady()");
        Log.e(TAG,"********* onMapReady *******");
        Log.e(TAG,"Constant.SOURCE_LAT ==> "+Constant.SOURCE_LNG);
        Log.e(TAG,"Constant.SOURCE_LNG ==> "+Constant.SOURCE_LNG);
        map = googleMap;
        map.setOnMapClickListener(this);
        map.setOnMarkerClickListener(this);

   //     mark_attendance_btn.setVisibility(View.VISIBLE);

        Toast.makeText(getApplicationContext(), "Please wait while we are fetching your location", Toast.LENGTH_LONG).show();

        Constant.SOURCE_LAT = gps.getLatitude();
        Constant.SOURCE_LNG = gps.getLongitude();

       /* Constant.SOURCE_LAT = simpleLocation.getLatitude();
        Constant.SOURCE_LNG = simpleLocation.getLongitude();*/

        // red marker
        markerLocation(new LatLng(Constant.SOURCE_LAT, Constant.SOURCE_LNG));

        markerForGeofence();

    }


    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, mIntentFilter);
    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        }


    }

    @Override
    public void onResponse(Object obj) {
        if (obj instanceof MarkAttendanceModel) {

            MarkAttendanceModel model = (MarkAttendanceModel) obj;

            if (model.getIsSuccess().equalsIgnoreCase("true")) {

                // Toast.makeText(getApplicationContext(), model.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "Day started successfully.", Toast.LENGTH_SHORT).show();

                SharedPreferences.Editor e = sp.edit();
                e.putString("day_start"+Constant.TEST_COORDINATOR_ID, current_date);
                e.putString("tracking_school_id", Constant.TRACKING_SCHOOL_ID);
                e.putString("END","0");
                e.commit();

                mark_attendance_btn.setVisibility(View.GONE);
                day_end_btn.setVisibility(View.VISIBLE);

                showUploadSelfieDialog();

                callApiForGettngHistory();


            } else {
                Toast.makeText(getApplicationContext(), "Something went wrong, please try again.", Toast.LENGTH_SHORT).show();
            }
        } else if (obj instanceof MarkAttendanceModel1) {
            MarkAttendanceModel1 model = (MarkAttendanceModel1) obj;

            if (model.getIsSuccess().equalsIgnoreCase("true")) {

                //Toast.makeText(getApplicationContext(), model.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "Day end successfully.", Toast.LENGTH_SHORT).show();

                SharedPreferences.Editor e1 = sp.edit();
                e1.putString("day_end"+Constant.TEST_COORDINATOR_ID, current_date);
                e1.putString("tracking_school_id", Constant.TRACKING_SCHOOL_ID);
                e1.putString("END","1");
                e1.putString("selfie_pic_url","");
                e1.commit();

                Constant.SELFIE_FILE = null;
                Constant.SELFIE_BITMAP = null;

                mark_attendance_btn.setVisibility(View.GONE);
                day_end_btn.setVisibility(View.GONE);

                callApiForGettngHistory();

                //    stopService(serviceIntent);
            } else {
                Toast.makeText(getApplicationContext(), "Something went wrong, please try again.", Toast.LENGTH_SHORT).show();
            }
        }

        else if (obj instanceof AttendancetrackModel) {

            AttendancetrackModel model = (AttendancetrackModel) obj;

            Log.e(TAG,"success==> "+model.getIsSuccess());

            if (model.getIsSuccess().equalsIgnoreCase("true")) {

                TrackingList.clear();
                TrackingListMorning.clear();
                history_layout.removeAllViews();
                history_layout1.removeAllViews();

                final List<AttendancetrackModel.TrackerBean> list = model.getTracker();
                final TrackModel trackModel = new TrackModel();

                if(list.size()>0){
                    relative_layout.setVisibility(View.VISIBLE);
                }

                for (int i = 0; i < list.size(); i++) {
                    AttendancetrackModel.TrackerBean trackerBean = list.get(i);

                    trackModel.setActivity_id(trackerBean.getActivity_Id());
                    trackModel.setCreated_on(trackerBean.getCreated_On());
                    trackModel.setCurrent_address(trackerBean.getCurrent_address());
                    trackModel.setDay_status(trackerBean.getDay_Status());
                    trackModel.setLatitude(trackerBean.getLatitude());
                    trackModel.setLongitude(trackerBean.getLongitude());
                    trackModel.setSchool_id(trackerBean.getSchool_Id());
                    trackModel.setTrainer_id(String.valueOf(trackerBean.getTrainer_Id()));
                    trackModel.setDistance_From_Destination(trackerBean.getDistance_From_Destination());

                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("activity_id", trackerBean.getActivity_Id());
                    map.put("created_on", trackerBean.getCreated_On());
                    map.put("current_address", trackerBean.getCurrent_address());
                    map.put("day_status", trackerBean.getDay_Status());
                    map.put("latitude", trackerBean.getLatitude());
                    map.put("longitude", trackerBean.getLongitude());
                    map.put("school_id", trackerBean.getSchool_Id());
                    map.put("trainer_id", String.valueOf(trackerBean.getTrainer_Id()));
                    map.put("distance_from", trackerBean.getDistance_From_Destination());

                    TrackingList.add(map);

                    AddViewToLayout(i);

                }

             /*   int size = TrackingList.size();

                if(size==0){

                } else {
                    size = size-1;
                }*/
                final List<AttendancetrackModel.TrackerMBean> Mlist = model.getTracker_M();

                if(Mlist.size()>0){
                    relative_layout1.setVisibility(View.VISIBLE);
                }

                Log.e(TAG,"Morning size==> "+Mlist.size());
                for (int j = 0; j < Mlist.size(); j++) {
                    AttendancetrackModel.TrackerMBean trackerMBean = Mlist.get(j);

                    trackModel.setCreated_on(trackerMBean.getCreated_On());
                    trackModel.setLatitude(trackerMBean.getLatitude());
                    trackModel.setLongitude(trackerMBean.getLongitude());

                    double lat = Double.parseDouble(trackerMBean.getLatitude());
                    double lng = Double.parseDouble(trackerMBean.getLongitude());

                    String currentAddress = getAddress(lat, lng);

                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("activity_id", "");
                    map.put("created_on", trackerMBean.getCreated_On());
                    map.put("current_address", currentAddress);
                    map.put("day_status", "");
                    map.put("latitude", trackerMBean.getLatitude());
                    map.put("longitude", trackerMBean.getLongitude());
                    map.put("school_id", "");
                    map.put("trainer_id", "");
                    map.put("distance_from", "");

                    TrackingListMorning.add(map);


                    /*double trainer_lat = Double.parseDouble(trackerMBean.getLatitude());
                    double trainer_lng = Double.parseDouble( trackerMBean.getLongitude());

                    Location loc1 = new Location("");
                    loc1.setLatitude(trainer_lat);
                    loc1.setLongitude(trainer_lng);

                    Location loc2 = new Location("");
                    loc2.setLatitude(Constant.DESTINATION_LAT);
                    loc2.setLongitude(Constant.DESTINATION_LNG);

                    distanceInMeters = loc1.distanceTo(loc2);

                    double distanceInKM = distanceInMeters * 0.001;

                    if(distanceInKM<=500){
                        AddViewToLayout1(j+size);
                    }*/

                    AddViewToLayout1(j);

                }


                /*tAdapter = new TrackingAdapter(this, TrackingList);
                listview.setAdapter(tAdapter);*/


               // Toast.makeText(getApplicationContext(), model.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
              //  Toast.makeText(getApplicationContext(), model.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void callApiForGettngHistory() {
        if (connectionDetector.isConnectingToInternet()) {
           /* "Trainer_id":"12563",
                    "School_id":"54",
                    "Created_On":"2017-02-07 01:16:10.197"*/

            String Day_Status = "0";

            String activity_id = "1";

            String inputFormat = "yyyy-MM-dd HH:mm:ss:SSS";
            String currentDate = Utility.DisplayDateInParticularFormat(inputFormat);

            String school_name = getIntent().getStringExtra("school_name");
            String school_id = getIntent().getStringExtra("school_id");

            AttendanceTrackRequest request = new AttendanceTrackRequest(this, Constant.TEST_COORDINATOR_ID,
                    school_id, currentDate, this);
            request.hitAttendanceRequest();
        } else {
            Toast.makeText(getApplicationContext(), "Internet not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void AddViewToLayout(final int pos) {
        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.attendance_track_listitem, null);

        TextView text = (TextView) v.findViewById(R.id.text);
        LinearLayout linear_layout = (LinearLayout) v.findViewById(R.id.linear_layout);

        String current_address = TrackingList.get(pos).get("current_address");
        String created_on = TrackingList.get(pos).get("created_on");
        double trainer_lat = Double.parseDouble(TrackingList.get(pos).get("latitude"));
        double trainer_lng = Double.parseDouble(TrackingList.get(pos).get("longitude"));

        Location loc1 = new Location("");
        loc1.setLatitude(trainer_lat);
        loc1.setLongitude(trainer_lng);

        Location loc2 = new Location("");
        loc2.setLatitude(Double.parseDouble(Constant.TRACKING_LATITUDE));
        loc2.setLongitude(Double.parseDouble(Constant.TRACKING_LONGITUDE));

        distanceInMeters = loc1.distanceTo(loc2);

        double distanceInKM = distanceInMeters * 0.001;

        distanceInKM = Math.round(distanceInKM * 100.0) / 100.0;

        //    text.setText(created_on+": "+"You were at "+current_address);

        if(pos==0){
            if (current_address.length() > 1) {
                text.setText("Day Start: "+created_on + "\n"+ distanceInKM + " km from the "+ Constant.GOING_TO+"( " + current_address + " ).");
            } else {
                text.setText("Day Start: "+created_on + "\n" + distanceInKM + " km from the "+ Constant.GOING_TO+".");
            }
        } else if(pos==1){
            if (current_address.length() > 1) {
                text.setText("Day End: "+created_on + "\n" + distanceInKM + " km from the "+ Constant.GOING_TO+" ( " + current_address + " ).");
            } else {
                text.setText("Day End: "+created_on + "\n"+ distanceInKM + " km from the "+ Constant.GOING_TO+".");
            }
        } else {
            linear_layout.setVisibility(View.GONE);
        }

        linear_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MapActivity.class);
                i.putExtra("lat", TrackingList.get(pos).get("latitude"));
                i.putExtra("lng", TrackingList.get(pos).get("longitude"));
                startActivity(i);
            }
        });

        history_layout.addView(v);
    }

    private void AddViewToLayout1(final int pos) {
        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.attendence_track_listitem_morning, null);

        TextView text = (TextView) v.findViewById(R.id.text);
        LinearLayout linear_layout = (LinearLayout) v.findViewById(R.id.linear_layout);

        String current_address = TrackingListMorning.get(pos).get("current_address");
        String created_on = TrackingListMorning.get(pos).get("created_on");
        double trainer_lat = Double.parseDouble(TrackingListMorning.get(pos).get("latitude"));
        double trainer_lng = Double.parseDouble(TrackingListMorning.get(pos).get("longitude"));

        Location loc1 = new Location("");
        loc1.setLatitude(trainer_lat);
        loc1.setLongitude(trainer_lng);

        Location loc2 = new Location("");
        loc2.setLatitude(Double.parseDouble(Constant.TRACKING_LATITUDE));
        loc2.setLongitude(Double.parseDouble(Constant.TRACKING_LONGITUDE));

        Log.e("TAG","trainer_lat=> "+trainer_lat);
        Log.e("TAG","trainer_lng=> "+trainer_lng);
        Log.e("TAG","TRACKING_LATITUDE=> "+Double.parseDouble(Constant.TRACKING_LATITUDE));
        Log.e("TAG","TRACKING_LONGITUDE=> "+Double.parseDouble(Constant.TRACKING_LONGITUDE));

        distanceInMeters = loc1.distanceTo(loc2);

        double distanceInKM = distanceInMeters * 0.001;

        distanceInKM = Math.round(distanceInKM * 100.0) / 100.0;


        if(distanceInKM>=500.00){
            text.setText(created_on + ": " +"Didn't get the location.");
        } else {
            text.setText(created_on + ": " + distanceInKM + " km from the school.");
        }


        linear_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MapActivity.class);
                i.putExtra("lat", TrackingListMorning.get(pos).get("latitude"));
                i.putExtra("lng", TrackingListMorning.get(pos).get("longitude"));
                startActivity(i);
            }
        });




        history_layout1.addView(v);
    }

    private void showUploadSelfieDialog() {
        selfieDialog = new Dialog(AttendanceGeofencingActivity.this);
        selfieDialog.setCancelable(true);
        selfieDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        selfieDialog.getWindow().setFormat(PixelFormat.TRANSLUCENT);

        Drawable d = new ColorDrawable(Color.BLACK);
        d.setAlpha(0);
        selfieDialog.getWindow().setBackgroundDrawable(d);

        selfieDialog.setContentView(R.layout.dialog_selfie);


        TextView name_tv, distance_tv, text1;
        Button selfie_btn;

        selfie_btn = (Button) selfieDialog.findViewById(R.id.selfie_btn);
        name_tv = (TextView) selfieDialog.findViewById(R.id.name_tv);
        distance_tv = (TextView) selfieDialog.findViewById(R.id.distance_tv);
        text1 = (TextView) selfieDialog.findViewById(R.id.text1);

        String coordinator_name = sp.getString("test_coordinator_name", "ABC");

        name_tv.setText("Hi " + coordinator_name + ",");

        text1.setText(Constant.GOING_TO+" distance from your current location");

        distance_tv.setText(distanceInkm + " km");

        selfie_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeSelfie();
            }
        });


        selfieDialog.show();
    }




    private void takeSelfie() {
        /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        mUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);

        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);*/

        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);
        cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);
        cameraIntent.putExtra("android.intent.extras.LENS_FACING_FRONT", android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);

        startActivityForResult(cameraIntent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create " + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
            //  mediaFile = new File(mediaStorageDir.getPath() + File.separator + Constant.TEST_COORDINATOR_ID+ ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    @Override
    protected void onActivityResult(int RequestCode, int ResultCode, Intent Data) {
        super.onActivityResult(RequestCode, ResultCode, Data);

        // ******* Camera ********
        if (RequestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE
                && ResultCode == RESULT_OK) {

            try {
                selfieDialog.dismiss();
              //  LoadCaptureImage(Data);

                Bitmap photo = (Bitmap) Data.getExtras().get("data");
                Constant.SELFIE_BITMAP = photo;


                if (Constant.SELFIE_BITMAP != null) {
                    profile_img.setImageBitmap(Constant.SELFIE_BITMAP);
                   // Log.e("Constant.SELFIE_FILE ==>", "" + Constant.SELFIE_BITMAP);

                    mark_attendance_btn.setVisibility(View.GONE);
                    day_end_btn.setVisibility(View.VISIBLE);

                    File file =  Utility.ConvertBitmapToFile(photo, ctx);

                    uploadImage(file);
                }

             /*   if (Constant.SELFIE_FILE != null) {
                    Picasso.with(AttendanceGeofencingActivity.this).load(Constant.SELFIE_FILE).transform(new CircleTransformWhite()).into(profile_img);
                    Log.e("Constant.SELFIE_FILE ==>", "" + Constant.SELFIE_FILE);

                    mark_attendance_btn.setVisibility(View.GONE);
                    day_end_btn.setVisibility(View.VISIBLE);
                }*/

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void uploadImage(File file) {
        RequestBody fbody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", Constant.TEST_COORDINATOR_ID+".jpg", fbody);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), sp.getString("test_coordinator_id",""));


        ApiRequest apiService =
                ApiClient.getClient(AttendanceGeofencingActivity.this).create(ApiRequest.class);
        Call<ProfileImageModel> call = apiService.postImageSelfie(body, name);
        call.enqueue(new Callback<ProfileImageModel>() {
            @Override
            public void onResponse(Call<ProfileImageModel> call, Response<ProfileImageModel> response) {

                Log.e("responseee===> ","success "+response.toString());


                if (response.isSuccessful()) {

                    ProfileImageModel responseBody = response.body();
                    Log.e("on responseee===> ","success "+responseBody.getIsSuccess());
                    Log.e("on responseee===> ","message "+responseBody.getMessage());

                    SharedPreferences.Editor e = sp.edit();
                    e.putString("selfie_pic_url",responseBody.getMessage());
                    e.commit();
                } else {
                    ProfileImageModel responseBody = response.body();
                    Log.e("on responseee===> ","success "+responseBody.getIsSuccess());
                    Log.e("on responseee===> ","message "+responseBody.getMessage());
                }

            }

            @Override
            public void onFailure(Call<ProfileImageModel> call, Throwable t) {
                Log.e("on failure===> ","failure "+t.toString());
            }
        });
    }


    private void LoadCaptureImage(Intent data) {

        InputStream is = null;
        try {
            is = getContentResolver().openInputStream(data.getData());

        } catch (Exception e) {
            e.printStackTrace();
        }


        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        Log.e("ProfileActivity", "path=> " + mUri.getPath());

        try {
            mPhoto = getBitmapFromUri(mUri);

            Log.e("ProfileActivity", "mPhoto=> " + mPhoto);
            Log.e("ProfileActivity", "mUri=> " + mUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int height = mPhoto.getHeight();
        int width = mPhoto.getWidth();


        Log.e("ProfileActivity", "orientation==> " + getOrientation(mUri.getPath()));

        mPhoto = rotateImage(
                mPhoto,
                getOrientation(mUri.getPath()),
                width, height);

        getContentResolver().notifyChange(mUri, null);

        imageFile = new File(compressImage(bitmapToUriConverter(mPhoto).getPath()));

        int file_size = Integer.parseInt(String.valueOf(imageFile.length() / 1024));

        Log.e("file_size==>", "" + file_size);

        Base64Image = encodeImage(mPhoto);

        Constant.SELFIE_FILE = imageFile;

        /*String fileName = imageFile.getName();
        Log.e("Base64Image==>", "" + Base64Image);
        Picasso.with(AttendanceProfileActivity.this).load(imageFile).transform(new CircleTransformWhite()).into(profile_img);
        Log.e("imageFile==>", "" + imageFile);*/
    }

    // ***********************************************************************//

    protected Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    public static int getOrientation(String imagePath) {
        int rotate = 0;
        try {
            File imageFile = new File(imagePath);
            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            Log.e("ProfileActivity", "***orientation*** " + orientation);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    Log.e("ProfileActivity", "***degree*** " + 270);
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    Log.e("ProfileActivity", "***degree*** " + 180);
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    Log.e("ProfileActivity", "***degree*** " + 90);
                    rotate = 90;
                    break;
                case ExifInterface.ORIENTATION_UNDEFINED:
                    Log.e("ProfileActivity", "***degree***  undeifned");
                    rotate = 360;
                    break;
            }

            Log.v("", "Exif orientation: " + orientation);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }

    public Bitmap rotateImage(Bitmap bitmap, int angle, int width, int height) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);

        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }


    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;
            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image
        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);


        if (scaledBitmap != null) {
            Canvas canvas = new Canvas(scaledBitmap);
            canvas.setMatrix(scaleMatrix);
            canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2,
                    new Paint(Paint.FILTER_BITMAP_FLAG));
        }


//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            if (scaledBitmap != null) {
                scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                        scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                        true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            if (scaledBitmap != null) {
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 20, out);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return filename;

    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "Fitness365/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;
    }

    protected String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public Uri bitmapToUriConverter(Bitmap mBitmap) {
        Uri uri = null;
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            // Calculate inSampleSize

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
           /* Bitmap newBitmap = Bitmap.createScaledBitmap(mBitmap, 450, 350,
                    true);*/
            File file = new File(this.getFilesDir(), "Image"
                    + new Random().nextInt() + ".jpeg");
            FileOutputStream out = this.openFileOutput(file.getName(),
                    Context.MODE_WORLD_READABLE);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 40, out);
            out.flush();
            out.close();
            //get absolute path
            String realPath = file.getAbsolutePath();
            File f = new File(realPath);
            uri = Uri.fromFile(f);

        } catch (Exception e) {
            Log.e("Your Error Message", e.getMessage());
        }
        return uri;
    }

    private String encodeImage(Bitmap mPhoto) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        mPhoto.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        // String encImage = Base64.encodeToString(b, Base64.NO_WRAP);

        encImage = encImage.replaceAll("(?:\\r\\n|\\n\\r|\\n|\\r)", "");

        return encImage;
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    //{Day_Status=0, Longitude=0.0, distance_From_Destination=0, Activity_Id=1, Current_address=, Latitude=0.0, Trainer_Id=33280, School_Id=23, Created_On=19 Apr 2018 14:26:40:956}
    // hashmap==> {School_id=23, Trainer_id=33280, Created_On=2018-04-19 14:26:41:543}
}
