package kheloindia.com.assessment.robotocalendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import kheloindia.com.assessment.R;
import kheloindia.com.assessment.functions.Constant;


public class RobotoCalendarView extends LinearLayout {

    int j = 0;
    // ************************************************************************************************************************************************************************
    // * Attributes
    // ************************************************************************************************************************************************************************
    // View
    private Context context;
    private TextView dateTitle;
    private ImageView leftButton;
    private ImageView rightButton;
    private View view;
    private int flag = 0;
    // Class
    private RobotoCalendarListener robotoCalendarListener;
    private Calendar currentCalendar;
    private Locale locale;
    ImageView dayOfMonthCircle;

    // Style
    private int monthTitleColor;
    private int monthTitleFont;
    private int dayOfWeekColor;
    private int dayOfWeekFont;
    private int dayOfMonthColor;
    private int dayOfMonthFont;

    public static final int RED_CIRCLE = R.drawable.red_circle;
    public static final int GREEN_CIRCLE = R.drawable.green_circle;
    public static final int BLUE_CIRCLE = R.drawable.blue_circle;
    private Calendar auxCalendar;


    String TAG = "RobotoCalendarView";

    // ************************************************************************************************************************************************************************
    // * Initialization methods
    // ************************************************************************************************************************************************************************

    public RobotoCalendarView(Context context) {
        super(context);
        this.context = context;
        onCreateView();
    }

    public RobotoCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        if (isInEditMode()) {
            return;
        }
        getAttributes(context, attrs);
        onCreateView();
    }

    @SuppressWarnings("ResourceAsColor")
    private void getAttributes(Context context, AttributeSet attrs) {

        try {
            TypedArray typedArray = context.obtainStyledAttributes(attrs,
                    R.styleable.RobotoCalendarView, 0, 0);
            monthTitleColor = typedArray.getColor(
                    R.styleable.RobotoCalendarView_monthTitleColor,
                    getResources().getColor(R.color.monthTitleColor));
            monthTitleFont = typedArray.getInt(
                    R.styleable.RobotoCalendarView_monthTitleFont,
                    R.string.monthTitleFont);
            dayOfWeekColor = typedArray.getColor(
                    R.styleable.RobotoCalendarView_dayOfWeekColor,
                    getResources().getColor(R.color.dayOfWeekColor));
            dayOfWeekFont = typedArray.getInt(
                    R.styleable.RobotoCalendarView_dayOfWeekFont,
                    R.string.dayOfWeekFont);
            dayOfMonthColor = typedArray.getColor(
                    R.styleable.RobotoCalendarView_dayOfMonthColor,
                    getResources().getColor(R.color.dayOfMonthColor));
            dayOfMonthFont = typedArray.getInt(
                    R.styleable.RobotoCalendarView_dayOfMonthFont,
                    R.string.dayOfMonthFont);
            typedArray.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public View onCreateView() {

        LayoutInflater inflate = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflate.inflate(R.layout.roboto_calendar_picker_layout, this,
                true);

        findViewsById(view);


       /* Constants.currentMonthIndex = 0;
        Constants.currentMonth = 1;*/

        updateCalendar();

        initializeEventListeners();

        initializeComponentBehavior();


        return view;
    }

    private void findViewsById(View view) {
        leftButton = (ImageView) view.findViewById(R.id.leftButton);
        rightButton = (ImageView) view.findViewById(R.id.rightButton);
        dateTitle = (TextView) view.findViewWithTag("dateTitle");
        currentCalendar = Calendar.getInstance(Locale.getDefault());
/*
        if(Constant.SHOW_BLUE_EDIT_CIRCLES){
            rightButton.setVisibility(View.GONE);
            leftButton.setVisibility(View.GONE);
        }
        else {*/
            rightButton.setVisibility(View.VISIBLE);
            leftButton.setVisibility(View.VISIBLE);
    //    }

    }

    private void initializeEventListeners() {

        leftButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (robotoCalendarListener == null) {
                    throw new IllegalStateException(
                            "You must assing a valid RobotoCalendarListener first!");
                }
                robotoCalendarListener.onLeftButtonClick();
            }
        });

        rightButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (robotoCalendarListener == null) {
                    throw new IllegalStateException(
                            "You must assing a valid RobotoCalendarListener first!");
                }
                robotoCalendarListener.onRightButtonClick();
            }
        });
    }

    private void initializeComponentBehavior() {
        // Initialize calendar for current month
        Locale locale = context.getResources().getConfiguration().locale;
        Calendar currentCalendar = Calendar.getInstance(locale);
        initializeCalendar(currentCalendar);
    }

    // ************************************************************************************************************************************************************************
    // * Private auxiliary methods
    // ************************************************************************************************************************************************************************

    @SuppressLint("DefaultLocale")
    private void initializeTitleLayout() {
        // Apply styles
        String font = getResources().getString(monthTitleFont);
       /* Typeface robotoTypeface = RobotoTypefaceManager
                .obtaintTypefaceFromString(context, font);*/
        int color = getResources().getColor(R.color.monthTitleColor);
   //     dateTitle.setTypeface(robotoTypeface, Typeface.BOLD);
        dateTitle.setTextColor(color);

        String dateText = new DateFormatSymbols(locale).getMonths()[currentCalendar
                .get(Calendar.MONTH)].toString();
        dateText = dateText.substring(0, 1).toUpperCase()
                + dateText.subSequence(1, dateText.length());
        dateTitle.setText(dateText + " " + currentCalendar.get(Calendar.YEAR));
    }

    @SuppressLint("DefaultLocale")
    private void initializeWeekDaysLayout() {

        // Apply styles
        String font = getResources().getString(dayOfWeekFont);
      /*  Typeface robotoTypeface = RobotoTypefaceManager
                .obtaintTypefaceFromString(context, font);*/
        int color = getResources().getColor(R.color.dayOfWeekColor);

        TextView dayOfWeek;
        String dayOfTheWeekString;
        String[] weekDaysArray = new DateFormatSymbols(locale)
                .getShortWeekdays();
        for (int i = 1; i < weekDaysArray.length; i++) {
            dayOfWeek = (TextView) view.findViewWithTag("dayOfWeek"
                    + getWeekIndex(i, currentCalendar));
            dayOfTheWeekString = weekDaysArray[i];

            // Check it for languages with only one week day lenght
            if (dayOfTheWeekString.length() > 1) {
                dayOfTheWeekString = dayOfTheWeekString.substring(0, 1)
                        .toUpperCase() + dayOfTheWeekString.subSequence(1, 2);
            }
          //  dayOfWeek.setText(dayOfTheWeekString);

            // Apply styles
         //   dayOfWeek.setTypeface(robotoTypeface);
            dayOfWeek.setTextColor(color);
        }
    }

    private void initializeDaysOfMonthLayout() {

        // Apply styles
        String font = getResources().getString(dayOfMonthFont);
       /* Typeface robotoTypeface = RobotoTypefaceManager
                .obtaintTypefaceFromString(context, font);*/
        int color = getResources().getColor(R.color.dayOfMonthColor);
        TextView dayOfMonthText;
        ImageView dayOfMonthImage;
        ViewGroup dayOfMonthContainer;

        for (int i = 1; i < 43; i++) {

            dayOfMonthContainer = (ViewGroup) view
                    .findViewWithTag("dayOfMonthContainer" + i);
            dayOfMonthText = (TextView) view.findViewWithTag("dayOfMonthText"
                    + i);
            dayOfMonthImage = (ImageView) view
                    .findViewWithTag("dayOfMonthImage" + i);

            dayOfMonthText.setVisibility(View.INVISIBLE);
            dayOfMonthImage.setVisibility(View.GONE);

            // Apply styles
         //   dayOfMonthText.setTypeface(robotoTypeface);
            dayOfMonthText.setTextColor(color);
            dayOfMonthText.setBackgroundResource(android.R.color.transparent);

            dayOfMonthContainer
                    .setBackgroundResource(android.R.color.transparent);
            dayOfMonthContainer.setOnClickListener(null);
        }
    }


    private void Clear_setDaysInCalendar() {
        auxCalendar = Calendar.getInstance(locale);
        int cc = auxCalendar.get(Calendar.DAY_OF_MONTH);
        int cm = auxCalendar.get(Calendar.MONTH);

        int d = auxCalendar.get(Calendar.DAY_OF_MONTH);
        int m = auxCalendar.get(Calendar.MONTH);
        int y = auxCalendar.get(Calendar.YEAR);



        auxCalendar.setTime(currentCalendar.getTime());
        auxCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfMonth = auxCalendar.get(Calendar.DAY_OF_WEEK);


        TextView dayOfMonthText;
        ViewGroup dayOfMonthContainer;
        ViewGroup schoolContainer;
        TextView school_tv;

        // Calculate dayOfMonthIndex
        int dayOfMonthIndex = getWeekIndex(firstDayOfMonth, auxCalendar);

        Log.e(TAG, "***********   clear circles  **************** ");

        for (int i = 1; i <= 32; i++, dayOfMonthIndex++) {


            dayOfMonthContainer = (ViewGroup) view
                    .findViewWithTag("dayOfMonthContainer" + dayOfMonthIndex);
            schoolContainer = (ViewGroup) view
                    .findViewWithTag("schoolContainer" + dayOfMonthIndex);
            dayOfMonthText = (TextView) view.findViewWithTag("dayOfMonthText"
                    + dayOfMonthIndex);
            school_tv = (TextView) view.findViewWithTag("school_tv_"
                    + dayOfMonthIndex);

            schoolContainer.setVisibility(View.VISIBLE);
            school_tv.setText("");

            dayOfMonthCircle = (ImageView) view
                    .findViewWithTag("dayOfMonthImage" + dayOfMonthIndex);

            if (dayOfMonthText == null) {
                break;
            }

            dayOfMonthCircle.setVisibility(View.GONE);
            dayOfMonthContainer.setOnClickListener(onDayOfMonthClickListener);
            dayOfMonthText.setVisibility(View.VISIBLE);


            if (i <= auxCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                dayOfMonthText.setText(String.valueOf(i));


                dayOfMonthContainer.setBackgroundResource(R.drawable.grey_circle);
                dayOfMonthText.setTextColor(Color.parseColor("#000000"));


                    dayOfMonthContainer.setBackgroundResource(R.drawable.grey_circle);
                    dayOfMonthText.setTextColor(Color.parseColor("#000000"));
                    schoolContainer.setVisibility(View.INVISIBLE);
                    school_tv.setText("");



                if (Constant.SHOW_BLUE_EDIT_CIRCLES) {

                  //  if (Constant.current_Month==Constant.selecte_dMonth) {

                        SimpleDateFormat formatter = new SimpleDateFormat("dd");
                        Date date1 = new Date();

                        String todayDate = formatter.format(date1);

                        int dateInt = Integer.parseInt(todayDate);

                        for (int n = 0; n < Constant.CurrentWeek.size(); n++) {
                            int date = Constant.CurrentWeek.get(n).get("day");
                            int month = Constant.CurrentWeek.get(n).get("month");
                            if (i == date) {
                                if(month==Constant.selecte_dMonth) {
                                    if (i <= dateInt) {
                                        dayOfMonthContainer.setBackgroundResource(R.drawable.light_blue);
                                        dayOfMonthText.setTextColor(Color.parseColor("#000000"));
                                    }
                                }
                            }
                        }
                //   }

                }
            }
            else {
                j++;

                dayOfMonthText.setText("");

            }

        }
        // If the last week row has no visible days, hide it or show it in case
        ViewGroup weekRow = (ViewGroup) view.findViewWithTag("weekRow6");
        dayOfMonthText = (TextView) view.findViewWithTag("dayOfMonthText36");
        if (dayOfMonthText.getVisibility() == INVISIBLE) {
            weekRow.setVisibility(GONE);
        } else {
            weekRow.setVisibility(VISIBLE);
        }

    }

    private void setDaysInCalendar() {
        Log.e(TAG, "****setDaysInCalendar****==> ");
        //auxCalendar = Calendar.getInstance(locale);
        auxCalendar = Calendar.getInstance(TimeZone.getDefault(),Locale.US); auxCalendar.setFirstDayOfWeek(Calendar.SUNDAY);
        int cc = auxCalendar.get(Calendar.DAY_OF_MONTH);
        int cm = auxCalendar.get(Calendar.MONTH);

        int d = auxCalendar.get(Calendar.DAY_OF_MONTH);
        int m = auxCalendar.get(Calendar.MONTH);
        int y = auxCalendar.get(Calendar.YEAR);



        auxCalendar.setTime(currentCalendar.getTime());
        auxCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfMonth = auxCalendar.get(Calendar.DAY_OF_WEEK);


          TextView dayOfMonthText;
          ViewGroup dayOfMonthContainer;
          ViewGroup schoolContainer;
        TextView school_tv;

        // Calculate dayOfMonthIndex
        int dayOfMonthIndex = getWeekIndex(firstDayOfMonth, auxCalendar);

        Log.e(TAG, "dayOfMonthIndex=> "+dayOfMonthIndex);


      /*  for (int i = 1; i <= 32; i++, dayOfMonthIndex++){
            schoolContainer = (ViewGroup) view
                    .findViewWithTag("schoolContainer" + dayOfMonthIndex);
            school_tv = (TextView) view.findViewWithTag("school_tv_"
                    + dayOfMonthIndex);

            schoolContainer.setVisibility(View.INVISIBLE);
            school_tv.setVisibility(View.INVISIBLE);
        }*/

        for (int i = 1; i <= 32; i++, dayOfMonthIndex++) {


            dayOfMonthContainer = (ViewGroup) view
                    .findViewWithTag("dayOfMonthContainer" + dayOfMonthIndex);
            schoolContainer = (ViewGroup) view
                    .findViewWithTag("schoolContainer" + dayOfMonthIndex);
            dayOfMonthText = (TextView) view.findViewWithTag("dayOfMonthText"
                    + dayOfMonthIndex);
            school_tv = (TextView) view.findViewWithTag("school_tv_"
                    + dayOfMonthIndex);


            dayOfMonthCircle = (ImageView) view
                    .findViewWithTag("dayOfMonthImage" + dayOfMonthIndex);

            if (dayOfMonthText == null) {
                break;
            }

            dayOfMonthCircle.setVisibility(View.GONE);
            dayOfMonthContainer.setOnClickListener(onDayOfMonthClickListener);

          /*  dayOfMonthText.setVisibility(View.VISIBLE);
            dayOfMonthContainer.setBackgroundResource(R.drawable.grey_circle);
            dayOfMonthText.setTextColor(Color.parseColor("#000000"));*/


            if (i <= auxCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                dayOfMonthText.setText(String.valueOf(i));

                dayOfMonthText.setVisibility(View.VISIBLE);
                dayOfMonthContainer.setBackgroundResource(R.drawable.grey_circle);
                dayOfMonthText.setTextColor(Color.parseColor("#000000"));


                if(Constant.AttendanceList.size()>0){

                   /* school_tv.setVisibility(View.INVISIBLE);
                    schoolContainer.setVisibility(View.INVISIBLE);*/


                    for (int n = 0; n < Constant.AttendanceList.size(); n++) {

                        int date = Integer.parseInt(Constant.AttendanceList.get(n).get("Day"));
                        String status = Constant.AttendanceList.get(n).get("Activity_id");


                        if (status.equalsIgnoreCase("2")) {
                            if (i == date) {
                                Log.e(TAG, "****red circle i==****==> "+i);
                                dayOfMonthContainer.setBackgroundResource(R.drawable.light_red);
                                dayOfMonthText.setTextColor(Color.parseColor("#FE2B2D"));

                                schoolContainer.setVisibility(View.VISIBLE);
                                school_tv.setText(Constant.AttendanceList.get(n).get("School_Name"));
                            }
                        }
                        else if (status.equalsIgnoreCase("1")) {

                            if (i == date) {
                                Log.e(TAG, "****green circle i==****==> "+i);
                                dayOfMonthContainer.setBackgroundResource(R.drawable.light_green);
                                dayOfMonthText.setTextColor(Color.parseColor("#65A245"));

                                schoolContainer.setVisibility(View.VISIBLE);
                                school_tv.setText(Constant.AttendanceList.get(n).get("School_Name"));

                            } else {
                                //  schoolContainer.setVisibility(View.INVISIBLE);
                                // school_tv.setText("");
                            }
                        }
                        else {
                              /*  Log.e(TAG, "****grey circle i==****==> "+i);
                                dayOfMonthContainer.setBackgroundResource(R.drawable.grey_circle);
                                dayOfMonthText.setTextColor(Color.parseColor("#000000"));
                                school_tv.setText("");
                                schoolContainer.setVisibility(View.INVISIBLE);*/

                        }
                    }
                }
                else {
                    dayOfMonthContainer.setBackgroundResource(R.drawable.grey_circle);
                    dayOfMonthText.setTextColor(Color.parseColor("#000000"));
                    schoolContainer.setVisibility(View.INVISIBLE);
                    school_tv.setText("");
                   // school_tv.setVisibility(View.INVISIBLE);

                }

                if (Constant.SHOW_BLUE_EDIT_CIRCLES) {

                //    if (Constant.current_Month==Constant.selecte_dMonth) {

                        SimpleDateFormat formatter = new SimpleDateFormat("dd");
                    Date date1 = new Date();

                    String todayDate = formatter.format(date1);

                    int dateInt = Integer.parseInt(todayDate);

                        Log.e(TAG,"Constant.current_Month=> "+Constant.current_Month);
                        Log.e(TAG,"Constant.selecte_dMonth=> "+Constant.selecte_dMonth);



                    for (int n = 0; n < Constant.TempCurrentWeekList.size(); n++) {
                        int date = Constant.TempCurrentWeekList.get(n).get("day");
                        int month = Constant.TempCurrentWeekList.get(n).get("month");


                        // n= 5 saturday, n= 6 sunday, chk for sat sun and dont show circle for sat sun
                        Log.e(TAG,"i ===> "+i+"  date ==> "+date+"   dateInt==> "+dateInt);

                        //Log.e(TAG,"Constant.CurrentWeek.get(n).get(\"day\")=> "+Constant.CurrentWeek.get(n).get("day"));
                        if (i == date) {
                           // if(month==Constant.selecte_dMonth) {
                                if(Constant.current_Month==Constant.selecte_dMonth) {
                                if (i <= dateInt) {

                                    if(n==6 || n==5){

                                    } else {
                                        dayOfMonthContainer.setBackgroundResource(R.drawable.light_blue);
                                        dayOfMonthText.setTextColor(Color.parseColor("#000000"));
                                    }
                                }
                            }
                        }
                    }
           //     }

                }
            }
             else {
                j++;

                dayOfMonthText.setText("");

            }

        }
        // If the last week row has no visible days, hide it or show it in case
        ViewGroup weekRow = (ViewGroup) view.findViewWithTag("weekRow6");
        dayOfMonthText = (TextView) view.findViewWithTag("dayOfMonthText36");
        if (dayOfMonthText.getVisibility() == INVISIBLE) {
            weekRow.setVisibility(GONE);
        } else {
            weekRow.setVisibility(VISIBLE);
        }

    }

    private void clearDayOfMonthContainerBackground() {
        ViewGroup dayOfMonthContainer;
        for (int i = 1; i < 43; i++) {
            dayOfMonthContainer = (ViewGroup) view
                    .findViewWithTag("dayOfMonthContainer" + i);
            dayOfMonthContainer
                    .setBackgroundResource(android.R.color.transparent);
        }
    }

    private ViewGroup getDayOfMonthContainer(Calendar currentCalendar) {
        int monthOffset = getMonthOffset(currentCalendar);
        int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
        ViewGroup dayOfMonthContainer = (ViewGroup) view
                .findViewWithTag("dayOfMonthContainer"
                        + (currentDay + monthOffset));
        return dayOfMonthContainer;
    }

    private TextView getDayOfMonthText(Calendar currentCalendar) {
        int monthOffset = getMonthOffset(currentCalendar);
        int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
        TextView dayOfMonth = (TextView) view.findViewWithTag("dayOfMonthText"
                + (currentDay + monthOffset));
        return dayOfMonth;
    }

    private TextView getSundayOfMonthText(Calendar currentCalendar) {
        int monthOffset = getMonthOffset(currentCalendar);
        int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
        TextView dayOfMonth = (TextView) view.findViewWithTag("dayOfMonthText"
                + (currentDay + monthOffset));
        return dayOfMonth;
    }

    private ImageView getDayOfMonthImage(Calendar currentCalendar) {
        int monthOffset = getMonthOffset(currentCalendar);
        int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
        ImageView dayOfMonth = (ImageView) view
                .findViewWithTag("dayOfMonthImage" + (currentDay + monthOffset));
        return dayOfMonth;
    }

    private int getMonthOffset(Calendar currentCalendar) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentCalendar.getTime());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayWeekPosition = calendar.getFirstDayOfWeek();
        int dayPosition = calendar.get(Calendar.DAY_OF_WEEK);

        if (firstDayWeekPosition == 1) {
            return dayPosition - 1;
        } else {

            if (dayPosition == 1) {
                return 6;
            } else {
                return dayPosition - 2;
            }
        }
    }

    private int getWeekIndex(int weekIndex, Calendar currentCalendar) {
        int firstDayWeekPosition = currentCalendar.getFirstDayOfWeek();

        if (firstDayWeekPosition == 1) {
            return weekIndex;
        } else {

            if (weekIndex == 1) {
                return 7;
            } else {
                return weekIndex - 1;
            }
        }
    }


    private OnClickListener onDayOfMonthClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            // Extract day selected
            ViewGroup dayOfMonthContainer = (ViewGroup) view;
            String tagId = (String) dayOfMonthContainer.getTag();
            tagId = tagId.substring(19, tagId.length());
            TextView dayOfMonthText = (TextView) view
                    .findViewWithTag("dayOfMonthText" + tagId);

            // Fire event
            try {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(currentCalendar.getTime());
                calendar.set(Calendar.DAY_OF_MONTH,
                        Integer.valueOf(dayOfMonthText.getText().toString()));


                if (robotoCalendarListener == null) {
                    throw new IllegalStateException(
                            "You must assign a valid RobotoCalendarListener first!");
                } else {
                    robotoCalendarListener.onDateSelected(calendar.getTime());
                }
            } catch (Exception e) {

                e.printStackTrace();
            }

        }
    };

    public interface RobotoCalendarListener {

        public void onDateSelected(Date date);

        public void onRightButtonClick();

        public void onLeftButtonClick();
    }

    public void setRobotoCalendarListener(
            RobotoCalendarListener robotoCalendarListener) {
        this.robotoCalendarListener = robotoCalendarListener;
    }


    @SuppressLint("DefaultLocale")
    public void Clear_initializeCalendar(Calendar currentCalendar) {
        this.currentCalendar = currentCalendar;
        locale = context.getResources().getConfiguration().locale;
        // Set date title
        initializeTitleLayout();
        // Set weeks days titles
        initializeWeekDaysLayout();
        // Initialize days of the month
        initializeDaysOfMonthLayout();
        // Set days in calendar
        Clear_setDaysInCalendar();
    }


    @SuppressLint("DefaultLocale")
    public void initializeCalendar(Calendar currentCalendar) {
        Log.e(TAG, "****initializeCalendar****==> ");
        this.currentCalendar = currentCalendar;
        locale = context.getResources().getConfiguration().locale;
        // Set date title
        initializeTitleLayout();
        // Set weeks days titles
        initializeWeekDaysLayout();
        // Initialize days of the month
        initializeDaysOfMonthLayout();
        // Set days in calendar
        setDaysInCalendar();
    }

    public void markDayAsCurrentDay(Date currentDate) {
        Locale locale = context.getResources().getConfiguration().locale;
        Calendar currentCalendar = Calendar.getInstance(locale);
        currentCalendar.setTime(currentDate);
        TextView dayOfMonth = getDayOfMonthText(currentCalendar);

       // dayOfMonth.setBackgroundResource(R.drawable.red_wheel);
        //dayOfMonth.setPadding(30, 10, 30, 10);
        // dayOfMonth.setTextColor(Color.WHITE);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void markDayAsSelectedDay(Date currentDate, int month, int year, String event_type) {
        // Clear previous marks

        //clearDayOfMonthContainerBackground();
        Locale locale = context.getResources().getConfiguration().locale;
        Calendar currentCalendar = Calendar.getInstance(locale);
        currentCalendar.setTime(currentDate);
        ViewGroup dayOfMonthContainer = getDayOfMonthContainer(currentCalendar);

        int currentMonth = auxCalendar.get(Calendar.MONTH) + 1;
        int currentYear = auxCalendar.get(Calendar.YEAR);


        Log.i("Current Year : ", " " + currentYear);
        Log.e("Current Month :  ", "" + "" + currentMonth);

        Log.i("PHP Year : ", " " + year);
        Log.e("PHP Month :  ", "" + "" + month);

        if (currentMonth == month && currentYear == year && event_type.equalsIgnoreCase("Public")) {
            dayOfMonthContainer.setBackgroundResource(R.drawable.green_circle);
        } else if (currentMonth == month && currentYear == year && event_type.equalsIgnoreCase("Private")) {
            dayOfMonthContainer.setBackgroundResource(R.drawable.red_circle);
        } else {
            dayOfMonthContainer.setBackground(null);

        }

    }

    public void markDayWithStyle(int style, Date currentDate) {
        Locale locale = context.getResources().getConfiguration().locale;
        Calendar currentCalendar = Calendar.getInstance(locale);
        currentCalendar.setTime(currentDate);
        ImageView dayOfMonthImage = getDayOfMonthImage(currentCalendar);
        // Draw day with style
        dayOfMonthImage.setVisibility(View.VISIBLE);
        dayOfMonthImage.setImageDrawable(null);
        dayOfMonthImage.setBackgroundResource(style);
    }

    public void updateCalendar() {
        System.out.println("inside the updatecalendar");
        currentCalendar = Calendar.getInstance(Locale.getDefault());
     //   currentCalendar.add(Calendar.MONTH, Constants.currentMonthIndex);
        initializeCalendar(currentCalendar);
      //  if (Constants.currentMonthIndex == 0) {
            markDayAsCurrentDay(currentCalendar.getTime());
      //  }
    }
}
