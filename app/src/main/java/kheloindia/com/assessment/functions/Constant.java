package kheloindia.com.assessment.functions;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import kheloindia.com.assessment.R;

/**
 * Created by PC10 on 05/15/2017.
 */

public class Constant {
    public static String STUDENT_REGISTRATION_NO = "0";
    public static String STUDENT_CATEGORY = "";
    public static String STUDENT_CATEGORY_OLD = "";
    public static String SCHOOL_CHAIN_ID = "";
    public static String TEST_APPLICABLE = "";
    public static String WEIGHT_SUB_TEST_ID = "";
    public static String HEIGHT_SUB_TEST_ID = "";
    public static String SCHOOL_IMAGE_PATH = "";
    public static String TEST_TYPE = "Scan";
    public static String SUB_TEST_TYPE = "the student";
    public static String SCHOOL_NAME = "";
    public static String SENIORS_STARTING_FROM = "0";
    public static String SCHOOL_ID = "";
    public static String STUDENT_NAME = "";
    public static String STUDENT_CLASS = "";
    public static String STUDENT_ROLL_NO = "0";
    public static String STUDENT_SECTION = "";
    public static String STUDENT_DOB = "";
    public static String STUDENT_GENDER = "";
    public static String STUDENT_ID = "";
    public static String COORDINATOR_ID = "";
    public static String CAMP_ID = "";
    public static String CLASS_ID = "";
    public static String TEST_ID = "";
    public static String SUB_TEST_ID = "";
    public static String SCORE_MEASUREMENT = "";
    public static String SCORE_UNIT = "";
    public static String MULTIPLE_LANE = "";
    public static String TIMER_TYPE = "";
    public static String FINAL_POSITION = "";
    public static String TEST_NAME = "";
    public static String TEST_COORDINATOR_ID = "";
    public static String USER_TYPE = "";
    public static Boolean ISComingFromTestScreen = false;
    //public static String STUDENT_USER_LOGIN_ID="";
    public static String TITLE = "";
    public static String WEBVIEW_URL = "";

    public static String SCREEN_ID = "";
    public static String SCREEN_NAME = "";
    public static String SCREEN_URL = "";
    public static String VIDEO_LINK = "";

    public static File SELFIE_FILE;
    public static Bitmap SELFIE_BITMAP;

    public static Boolean SHOW_BLUE_EDIT_CIRCLES = false;

    public static Boolean SHOW_TIMER = false;

    public static ArrayList<HashMap<String, String>> AttendanceList = new ArrayList<HashMap<String, String>>();
    public static ArrayList<HashMap<String, Integer>> TempCurrentWeekList = new ArrayList<HashMap<String, Integer>>();
    public static ArrayList<HashMap<String, Integer>> CurrentWeek = new ArrayList<HashMap<String, Integer>>();
    public static ArrayList<String> statusIDAllList = new ArrayList<>();
    public static ArrayList<String> seniorList = new ArrayList<>();
    public static ArrayList<String> classList = new ArrayList<>();
    public static ArrayList<String> rollNoList = new ArrayList<>();
    public static HashMap<String, String> classIdList = new HashMap<>();
    public static HashMap<String, String> testIdList = new HashMap<>();
    public static ArrayList<String> student_spin_data = new ArrayList<String>();
    public static ArrayList<String> classList1 = new ArrayList<>();
    public static ArrayList<String> classIdList1 = new ArrayList<>();
    public static ArrayList<String> classList2 = new ArrayList<>();
    public static ArrayList<String> classIdList2 = new ArrayList<>();
    public static String END_URL = "";

    public static String LOAD_URL = "";

    public static String SCAN_CLASS_ID = "";
    public static String SCAN_CURRENT_CLASS = "";
    public static int SCAN_POSITION = 0;

    public static String USER_URl = "";


    public static int currentMonth = 1;

    public static int currentMonthIndex = 0;

    public static int current_Month;
    public static int selecte_dMonth;

    public static double SOURCE_LAT;

    public static double SOURCE_LNG;

    /*public static double DESTINATION_LAT ;

    public static double DESTINATION_LNG ;*/

    public static float GEOFENCE_RADIUS = 250;

    public static int count = 1;
    public static String NEW_STATUS = "";
    public static String OLD_STATUS = "";

    public static String TRACKING_SCHOOL_NAME = "";
    public static String TRACKING_SCHOOL_ID = "";
    public static String TRACKING_LATITUDE = "";
    public static String TRACKING_LONGITUDE = "";
    public static String GOING_TO = "";

    // public static boolean FIRST_TIME ;

    String TAG = "Constant";

    public static String inputFormat = "dd MMM yyyy HH:mm:ss:SSS";

    public static String inputFormat2 = "yyyy-MM-dd HH:mm:ss";

    // String outputFormat = "dd MMM yyyy HH:mm:ss:SSS";
    public static ArrayList<String> testList = new ArrayList<String>();
    public static ArrayList<String> classListTemp1 = new ArrayList<>();
    public static ArrayList<String> classIdListTemp1 = new ArrayList<>();
    public static ArrayList<Object> fitnessTestResultModels = new ArrayList<>();
    public static ArrayList<Object> fitnessSkillTestResultModels = new ArrayList<>();
    public static boolean test_running = false, skill_test_running = false;
    public static boolean checked_incomp = true;
    public static int imported_count = 0;


    public static String getDateTimeServer() {

        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat(inputFormat);
        Date dt = new Date(0);
        String formattedDate = df.format(dt);
        return formattedDate;
    }

    public static String getDateTimeServer1() {

        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat(inputFormat);
        Date dt = new Date(1);
        String formattedDate = df.format(dt);
        return formattedDate;
    }

    public static String getDateTime() {

        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat(inputFormat);
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    public static String getDate() {

        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    public static long convertDateTomilliSec(String date) {
        long timeInMilliseconds = 0;
        SimpleDateFormat sdf = new SimpleDateFormat(inputFormat);
        try {
            Date mDate = sdf.parse(date);
            timeInMilliseconds = mDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeInMilliseconds;
    }


    public static boolean verify(String paramString, Context ctx) {
        return paramString
                .matches(ctx.getResources().getString(R.string.email_verification_symbols));
    }

    public static double COnvertToMiliseconds(String min_txt, String sec_txt, String mili_txt) {

        double min = Double.parseDouble(min_txt);
        double sec = Double.parseDouble(sec_txt);
        double mili = Double.parseDouble(mili_txt);

        sec = sec + (min * 60);
        mili = mili + (sec * 1000);


        return mili;
    }


    public static double COnvertToMilimeter(String meter_txt, String cm_txt, String mm_txt) {

        double meter = Double.parseDouble(meter_txt);
        double cm = Double.parseDouble(cm_txt);
        double mm = Double.parseDouble(mm_txt);

        cm = cm + (meter * 100);
        mm = mm + (cm * 10);

        return mm;
    }

    public static String getCurrentDate() {

        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    public static String DateConverter(String dateToConvert) {

        String dateConvert = dateToConvert;
        String DateConverted = "";

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date myDate = new Date();
        try {
            myDate = dateFormat.parse(dateConvert);
            Log.e("***** myDate *****", "" + myDate);
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
            DateConverted = formatter.format(myDate);
            Log.e("*conveted date*", "" + DateConverted);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return DateConverted;

    }


    public static String DateConverter2(String dateToConvert) {

        String dateConvert = dateToConvert;
        String DateConverted = "";

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS");
        Date myDate = new Date();
        try {
            myDate = dateFormat.parse(dateConvert);
            Log.e("***** myDate *****", "" + myDate);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            DateConverted = formatter.format(myDate);
            Log.e("*conveted date*", "" + DateConverted);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return DateConverted;

    }


    // Convert date String from one format to another

    public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate) {

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {
            Log.e("TAG Date", "ParseException - dateFormat");
        }

        return outputDate;

    }


    public static boolean isWithinRange(Date current_date, Date start_date, Date end_date) {

        return (current_date.after(start_date) && current_date.before(end_date));
    }


    public static String ConvertDateToString(Date datetime) {
        String reportDate = "";
        try {
            DateFormat df = new SimpleDateFormat(inputFormat);

            reportDate = df.format(datetime);

        } catch (Exception e) {

        }

        return reportDate;
    }

    public static Date ConvertStringTODate(String currentDate) {

        Date date = null;

        String dtStart = currentDate;
        SimpleDateFormat format = new SimpleDateFormat(inputFormat);
        try {
            date = format.parse(dtStart);
        } catch (Exception e) {
            //   e.printStackTrace();
            System.err.println(e.getMessage());

        }

        return date;
    }

    public static Date ConvertStringTODate1(String currentDate) {

        Date date = null;

        String dtStart = currentDate;
        SimpleDateFormat format = new SimpleDateFormat(inputFormat2);
        try {
            date = format.parse(dtStart);
        } catch (ParseException e) {
            //   e.printStackTrace();
        }

        return date;
    }

    public static Date ConvertStringTODate2(String currentDate) {

        Date date = null;

        String dtStart = currentDate;
        SimpleDateFormat format = new SimpleDateFormat(inputFormat2);
        try {
            date = format.parse(dtStart);
        } catch (ParseException e) {
            //   e.printStackTrace();
        }

        return date;
    }


    public static String ConvertMilisToTime(double v) {
        long millis = (long) v;
        String hms = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)),
                TimeUnit.MILLISECONDS.toMillis(millis) - TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(millis)));
        System.out.println(hms);
        return hms;
    }
}
