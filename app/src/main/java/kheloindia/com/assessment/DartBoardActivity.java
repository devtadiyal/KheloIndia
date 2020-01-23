package kheloindia.com.assessment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import kheloindia.com.assessment.adapter.CalendarAdapter;
import kheloindia.com.assessment.util.ItemClickListener;
import kheloindia.com.assessment.util.Utility;

import kheloindia.com.assessment.functions.Constant;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by CT13 on 2017-06-28.
 */

public class DartBoardActivity extends AppCompatActivity {

    private ListView year_lv, month_lv, date_lv;
    private Toolbar toolbar;
    private TextView school_tv, date_tv, month_tv;
    private Typeface font_light;
    private Button search_btn;
    private boolean month_select, year_select, date_select;
    private boolean validation_success;


    private List<String> date_list, date_list_two, date_list_three, date_list_four, date_list_current, dates;


    private String year, month, date;


    private CalendarAdapter date_adapter;
    private Calendar calendar;
    private List<String> years_list;
    private List<String> months_list;
    private List<String> months_list_current, months;
    private CalendarAdapter month_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dart_board);
        init();
    }


    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        Utility.showActionBar(this, toolbar, getString(R.string.view_dart_board));
        year_lv = (ListView) findViewById(R.id.year_lv);
        month_lv = (ListView) findViewById(R.id.month_lv);
        date_lv = (ListView) findViewById(R.id.date_lv);
        school_tv = (TextView) findViewById(R.id.school_tv);
        search_btn = (Button) findViewById(R.id.search_btn);
        date_tv = (TextView) findViewById(R.id.date_tv);
        month_tv = (TextView) findViewById(R.id.month_tv);

        font_light = Typeface.createFromAsset(getAssets(),
                "fonts/Roboto-Light" +
                        "_0.ttf");
        school_tv.setTypeface(font_light);
        school_tv.setText(Constant.SCHOOL_NAME);


        months_list = Arrays.asList(getResources().getStringArray(R.array.months_array));
        date_list = Arrays.asList(getResources().getStringArray(R.array.dates_array));
        date_list_two = Arrays.asList(getResources().getStringArray(R.array.dates_array_two));
        date_list_three = Arrays.asList(getResources().getStringArray(R.array.dates_array_three));
        date_list_four = Arrays.asList(getResources().getStringArray(R.array.dates_array_four));
        calendar = Calendar.getInstance(TimeZone.getDefault());


        years_list = new ArrayList<String>();
        years_list.add("All");
        years_list.add("" + (calendar.get(Calendar.YEAR) - 1));
        years_list.add("" + calendar.get(Calendar.YEAR));


        CalendarAdapter year_adapter = new CalendarAdapter(this, years_list);
        year_lv.setAdapter(year_adapter);

        year_adapter.setClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                month_lv.setVisibility(View.VISIBLE);
                month_tv.setVisibility(View.GONE);
                date_lv.setVisibility(View.GONE);
                date_tv.setVisibility(View.VISIBLE);

                year_select = true;
                month_select = false;

                year = years_list.get(position);

                if (year.equalsIgnoreCase("" + calendar.get(Calendar.YEAR))) {
                    months_list_current = new ArrayList<String>();


                    for (int i = 0; i <= calendar.get(Calendar.MONTH) + 1; i++) {
                        months_list_current.add(months_list.get(i));


                        month_adapter = new CalendarAdapter(DartBoardActivity.this, months_list_current);
                        month_lv.setAdapter(month_adapter);
                        months = months_list_current;


                    }
// else if(year.equalsIgnoreCase("All")){
//                    months=new ArrayList<String>();
//                    months.add("All");
//                    month_adapter=new CalendarAdapter(DartBoardActivity.this, months);
//                    month_lv.setAdapter(month_adapter);
//
//                }

                } else {
                    month_adapter = new CalendarAdapter(DartBoardActivity.this, months_list);
                    month_lv.setAdapter(month_adapter);
                    months = months_list;
                }
                month_adapter.setClickListener(new ItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        month_select = true;
                        month = months.get(position);
                        setDate();

                    }

                });
                if (month_select)
                    setDate();

            }
        });


        search_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (validate()) {
                    Intent in = new Intent(DartBoardActivity.this, MyLogBoardActivity.class);
                    in.putExtra("date_month_year", date + " " + month + " " + year);
                    startActivity(in);
                }
            }
        });

    }


    private void setDate() {
        date_lv.setVisibility(View.VISIBLE);
        date_tv.setVisibility(View.GONE);
        date_select = false;
        if (year.equalsIgnoreCase("" + calendar.get(Calendar.YEAR)) && month.equalsIgnoreCase(months_list.get(calendar.get(Calendar.MONTH) + 1))) {
            date_list_current = new ArrayList<String>();

            date_list_current.add("All");

            for (int i = 1; i < calendar.get(Calendar.DAY_OF_MONTH) + 1; i++) {
                date_list_current.add("" + i);
            }


            date_adapter = new CalendarAdapter(DartBoardActivity.this, date_list_current);
            date_lv.setAdapter(date_adapter);

            dates = date_list_current;

        } else {
            if (month.equalsIgnoreCase("All") || month.equalsIgnoreCase("Jan") || month.equalsIgnoreCase("Mar") || month.equalsIgnoreCase("May") || month.equalsIgnoreCase("Jul")


                    || month.equalsIgnoreCase("Aug") || month.equalsIgnoreCase("Oct") || month.equalsIgnoreCase("Dec")) {
                date_adapter = new CalendarAdapter(DartBoardActivity.this, date_list);
                date_lv.setAdapter(date_adapter);
                dates = date_list;

            } else if (month.equalsIgnoreCase("Apr") || month.equalsIgnoreCase("Jun") || month.equalsIgnoreCase("Sep") || month.equalsIgnoreCase("Nov")) {
                date_adapter = new CalendarAdapter(DartBoardActivity.this, date_list_two);
                date_lv.setAdapter(date_adapter);
                dates = date_list_two;


            } else {
                if (year.equalsIgnoreCase("All") || Integer.parseInt(year) % 4 == 0) {
                    date_adapter = new CalendarAdapter(DartBoardActivity.this, date_list_three);
                    date_lv.setAdapter(date_adapter);
                    dates = date_list_three;

                } else {

                    date_adapter = new CalendarAdapter(DartBoardActivity.this, date_list_four);
                    date_lv.setAdapter(date_adapter);
                    dates = date_list_four;

                }
            }
        }


        date_adapter.setClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                date_select = true;
                date = dates.get(position);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private boolean validate() {
        validation_success = false;

        if (year_select && month_select && date_select) {
            validation_success = true;

        } else if (year_select && month_select) {
            Toast.makeText(this, R.string.date_choose
                    , Toast.LENGTH_SHORT).show();

        } else if (year_select) {
            Toast.makeText(this, R.string.month_choose, Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, R.string.year_choose, Toast.LENGTH_SHORT).show();
        }

        return validation_success;
    }
}
