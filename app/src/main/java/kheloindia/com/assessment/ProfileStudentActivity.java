package kheloindia.com.assessment;

import android.content.Context;
import android.content.DialogInterface;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.Base64;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import kheloindia.com.assessment.model.ProfileImageModel;
import kheloindia.com.assessment.util.ProgressDialogUtility;
import kheloindia.com.assessment.util.ResponseListener;
import kheloindia.com.assessment.util.Utility;

import kheloindia.com.assessment.functions.CircleTransformWhite;
import kheloindia.com.assessment.functions.Constant;
import kheloindia.com.assessment.webservice.ProfileRequest;

/**
 * Created by CT13 on 2017-11-28.
 */

// not needed
public class ProfileStudentActivity  extends AppCompatActivity implements View.OnClickListener, ResponseListener {

    TextView name_tv,class_tv,dob_tv,gender_tv,parent_name_tv,mail_tv,phone_tv,address_tv,logout_tv;
    ImageView profile_img,back_img;
    FloatingActionButton select_image_fab;
    Toolbar toolbar;

    public static Bitmap mPhoto;
    File imageFile ;
    String Base64Image;

    private ProgressDialogUtility progressDialogUtility;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final String IMAGE_DIRECTORY_NAME = "Fitness365_Images";
    public static final int REQUEST_SELECT_PICTURE = 2;
    public static Uri mUri;

    SharedPreferences sp;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_student);
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        init();
    }


    private void init() {

        progressDialogUtility = new ProgressDialogUtility(this);


        name_tv=(TextView)findViewById(R.id.name_tv);
        class_tv=(TextView)findViewById(R.id.class_tv);
        dob_tv=(TextView)findViewById(R.id.dob_tv);
        gender_tv=(TextView)findViewById(R.id.gender_tv);
        parent_name_tv=(TextView)findViewById(R.id.parent_name_tv);
        mail_tv=(TextView)findViewById(R.id.mail_tv);
        phone_tv=(TextView)findViewById(R.id.phone_tv);
        address_tv=(TextView)findViewById(R.id.address_tv);
        profile_img=(ImageView)findViewById(R.id.profile_img);
        select_image_fab=(FloatingActionButton)findViewById(R.id.select_image_fab);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        back_img = (ImageView) toolbar.findViewById(R.id.back_img);
        logout_tv = (TextView) toolbar.findViewById(R.id.logout_tv);

//        ArrayList<Object> studentProfileList = DBManager.getInstance().getAllTableData(this, DBManager.TBL_LP_STUDENT_USER_MASTER, "", "","","");
//        StudentUserMasterModel studentUserMasterModel=(StudentUserMasterModel) studentProfileList.get(0);
//        name_tv.setText(studentUserMasterModel.getStudent_name());
//        class_tv.setText(studentUserMasterModel.getCurrent_class());
//        dob_tv.setText(studentUserMasterModel.getDate_of_birth());
//        gender_tv.setText(studentUserMasterModel.getGender());
//        parent_name_tv.setText(studentUserMasterModel.getGuardian_name());
//        mail_tv.setText(studentUserMasterModel.getUser_login_name());
//        phone_tv.setText(studentUserMasterModel.getPhone());
//        address_tv.setText(studentUserMasterModel.getAddress());


        back_img.setOnClickListener(this);
        logout_tv.setOnClickListener(this);
        select_image_fab.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.back_img:
                finish();
                break;

            case R.id.logout_tv:
                Utility.showLogoutDialog(this);
                break;

            case R.id.select_image_fab:
                selectImage();

        }

    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        //final CharSequence[] options = {"Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileStudentActivity.this);
        builder.setTitle("Select or take a new Picture!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    mUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);

                    startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

                } else if (options[item].equals("Choose from Gallery")) {

                    pickFromGallery();

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
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

    void pickFromGallery() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser
                (intent, "Select Picture"), REQUEST_SELECT_PICTURE);

    }

    protected Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
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
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
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

    public static int getOrientation(String imagePath) {
        int rotate = 0;
        try {
            File imageFile = new File(imagePath);
            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            Log.e("ProfileActivity","***orientation*** "+orientation);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    Log.e("ProfileActivity","***degree*** "+270);
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    Log.e("ProfileActivity","***degree*** "+180);
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    Log.e("ProfileActivity","***degree*** "+90);
                    rotate = 90;
                    break;
                case ExifInterface.ORIENTATION_UNDEFINED:
                    Log.e("ProfileActivity","***degree***  undeifned");
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

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
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
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
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

    @Override
    protected void onActivityResult(int RequestCode, int ResultCode, Intent Data) {
        super.onActivityResult(RequestCode, ResultCode, Data);

        // ******* Camera ********
        if (RequestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE
                && ResultCode == RESULT_OK) {

            try {
                LoadCaptureImage();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // ******* Gallery ********
        else if (RequestCode == REQUEST_SELECT_PICTURE && ResultCode == RESULT_OK) {

            mUri = Data.getData();

            LoadCaptureImage();

        }

    }

    private void LoadCaptureImage() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        Log.e("ProfileActivity", "path=> "+mUri.getPath());

        try {
            mPhoto = getBitmapFromUri(mUri);

            Log.e("ProfileActivity", "mPhoto=> "+mPhoto);
            Log.e("ProfileActivity", "mUri=> "+mUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int height = mPhoto.getHeight();
        int width = mPhoto.getWidth();


        Log.e("ProfileActivity","orientation==> "+getOrientation(mUri.getPath()));

        mPhoto = rotateImage(
                mPhoto,
                getOrientation(mUri.getPath()),
                width, height);

        getContentResolver().notifyChange(mUri, null);

        Log.e("ProfileActivity", "path*********=> "+bitmapToUriConverter(mPhoto).getPath());
        Log.e("ProfileActivity", "imageFile*********=> "+imageFile);

        imageFile = new File(compressImage(bitmapToUriConverter(mPhoto).getPath()));

        Base64Image = encodeImage(mPhoto);

        String fileName = imageFile.getName();
        Log.e("Base64Image==>",""+Base64Image);
        Picasso.get().load(imageFile).transform(new CircleTransformWhite()).into(profile_img);
        Log.e("imageFile==>",""+imageFile);
            /*  Picasso.with(ApplicationForm4Activity.this).load(imageFile).
                transform(new CircleTransformWhite(true)).into(student_img);*/
      /*  File newFile = new File(mediaStorageDir.getPath() + File.separator + Constant.TEST_COORDINATOR_ID+ ".jpg");
        imageFile.renameTo(newFile);*/
        //  CallUploadImageAPI(imageFile, Base64Image);


        ProfileRequest profileRequest = new ProfileRequest(this, Constant.TEST_COORDINATOR_ID, sp.getString("user_type", ""),Base64Image, this);
        profileRequest.hitUserRequest();
    }

    private String encodeImage(Bitmap mPhoto) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        mPhoto.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    @Override
    public void onResponse(Object obj) {
           if(obj instanceof ProfileImageModel){
            progressDialogUtility.dismissProgressDialog();
            ProfileImageModel model;
            model = (ProfileImageModel) obj;

            if (model.getIsSuccess().equalsIgnoreCase("true")) {
                Toast.makeText(getApplicationContext(),model.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(),model.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
