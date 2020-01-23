package kheloindia.com.assessment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
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

import kheloindia.com.assessment.adapter.TrackingAdapter;
import kheloindia.com.assessment.model.AttendancetrackModel;
import kheloindia.com.assessment.model.TrackModel;
import kheloindia.com.assessment.util.ConnectionDetector;
import kheloindia.com.assessment.util.ResponseListener;
import kheloindia.com.assessment.util.Utility;

import kheloindia.com.assessment.functions.CircleTransformWhite;
import kheloindia.com.assessment.functions.Constant;
import kheloindia.com.assessment.webservice.AttendanceTrackRequest;

/**
 * Created by PC10 on 07-Mar-18.
 */

public class AttendanceProfileActivity extends AppCompatActivity implements View.OnClickListener,
        ResponseListener {

    FloatingActionButton select_image_fab;
    ImageView profile_img;

    public static Uri mUri;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final String IMAGE_DIRECTORY_NAME = "Fitness365_Images";
    public static final int REQUEST_SELECT_PICTURE = 2;
    public static Bitmap mPhoto;
    File imageFile;
    String Base64Image;
    ListView listview;
    private ConnectionDetector connectionDetector;
    ArrayList<HashMap<String, String>> TrackingList = new ArrayList<HashMap<String, String>>();
    TrackingAdapter tAdapter;
    Toolbar toolbar;
    String TAG = "AttendanceProfileActivity";
    TextView welcome_student_tv;
    TextView school_tv;
    SharedPreferences sp;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.attendance_profile_activity);
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        init();
    }

    private void init() {
        select_image_fab = (FloatingActionButton) findViewById(R.id.select_image_fab);
        profile_img = (ImageView) findViewById(R.id.profile_img);
        listview = (ListView) findViewById(R.id.listview);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        school_tv = (TextView) findViewById(R.id.school_tv);
        welcome_student_tv = (TextView) findViewById(R.id.welcome_student_tv) ;

        school_tv.setText(Constant.SCHOOL_NAME);
        welcome_student_tv.setText("Welcome " + sp.getString("test_coordinator_name", "ABC")+", You are at:");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        select_image_fab.setOnClickListener(this);
        select_image_fab.setVisibility(View.GONE);

        if(Constant.SELFIE_FILE !=null){
        Picasso.with(AttendanceProfileActivity.this).load(Constant.SELFIE_FILE ).into(profile_img);
       // Log.e("Constant.SELFIE_FILE ==>", "" + Constant.SELFIE_FILE );
        }

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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.select_image_fab :
                selectImage();
                break;
        }

    }

    private void selectImage() {
        /*final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(AttendanceProfileActivity.this);
        builder.setTitle("Select or take a new Picture!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    mUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);

                    startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();*/

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        mUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);

        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
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
                LoadCaptureImage(Data);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

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

        int file_size = Integer.parseInt(String.valueOf(imageFile.length()/1024));

        Log.e("file_size==>", "" + file_size);

        Base64Image = encodeImage(mPhoto);

        String fileName = imageFile.getName();
        Log.e("Base64Image==>", "" + Base64Image);
        Picasso.with(AttendanceProfileActivity.this).load(imageFile).transform(new CircleTransformWhite()).into(profile_img);
        Log.e("imageFile==>", "" + imageFile);
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

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
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
    public void onResponse(Object obj) {
        if (obj instanceof AttendancetrackModel) {

            AttendancetrackModel model = (AttendancetrackModel) obj;

            if (model.getIsSuccess().equalsIgnoreCase("true")) {

                final List<AttendancetrackModel.TrackerBean> list = model.getTracker();
                final TrackModel trackModel = new TrackModel();
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
                    map.put("trainer_id",String.valueOf(trackerBean.getTrainer_Id()));
                    map.put("distance_from", trackerBean.getDistance_From_Destination());

                    TrackingList.add(map);

                }

                tAdapter = new TrackingAdapter(this, TrackingList);
                listview.setAdapter(tAdapter);


                Toast.makeText(getApplicationContext(),model.getMessage(),Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(),model.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
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
}
