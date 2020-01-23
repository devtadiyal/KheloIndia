package kheloindia.com.assessment.adapter;

import android.content.Context;

import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import kheloindia.com.assessment.R;
import kheloindia.com.assessment.functions.Constant;
import kheloindia.com.assessment.model.ActivityLogChildModel;
import kheloindia.com.assessment.model.MyDartGradeModel;
import kheloindia.com.assessment.util.DBManager;

import static kheloindia.com.assessment.functions.Constant.student_spin_data;


/**
 * Created by CT13 on 2018-01-04.
 */

public class ActivityLogListAdapter extends BaseAdapter {

    private Context context;
    private List<ActivityLogChildModel>activityLogModelList;
    private  ArrayAdapter student_spin_array;
    public  SparseIntArray selectedItems;


    public ActivityLogListAdapter(Context c, List<ActivityLogChildModel>activityLogModelList){
        this.context=c;
        this.activityLogModelList=activityLogModelList;
        if(student_spin_data.size()==0) {
                ArrayList arrayGradeList = DBManager.getInstance().getAllTableData(context, DBManager.TBL_LP_MY_DART_GRADE_TABLE, "current_school_id", Constant.SCHOOL_ID, "", "");
                for (int i = 0; i < arrayGradeList.size(); i++) {
                    Object ob = arrayGradeList.get(i);
                    if (ob instanceof MyDartGradeModel)
                        student_spin_data.add(((MyDartGradeModel) ob).getGrade());
                }
                }
        student_spin_array = new ArrayAdapter<String>(context, R.layout.simple_spinner_item,student_spin_data );
        student_spin_array.setDropDownViewResource(R.layout.spinner_dropdown_item);
        selectedItems  = new SparseIntArray();
    }


    @Override
    public int getCount() {
        return activityLogModelList.size();
    }


    @Override
    public Object getItem(int position) {
        return activityLogModelList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ViewHolder viewHolder;

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.log_child, null);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {

            viewHolder=(ViewHolder) convertView.getTag();

        }

        ActivityLogChildModel activityLogModel=(ActivityLogChildModel)getItem(position);
        viewHolder.student_tv.setText(activityLogModel.getStudent_name());
        viewHolder.student_spin.setAdapter(student_spin_array);

        if(!activityLogModel.getStudent_grade_id().equalsIgnoreCase(""))
        viewHolder.student_spin.setSelection(Integer.parseInt(activityLogModel.getStudent_grade_id())-1 );
else
            viewHolder.student_spin.setSelection(1);
            viewHolder.student_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int arg2, long id) {
                selectedItems.put( position, arg2 );
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return convertView;
    }



    private static class ViewHolder {

        private TextView student_tv;
        private Spinner student_spin;


        ViewHolder(View v) {
            student_tv=(TextView)v.findViewById(R.id.student_tv) ;
            student_spin=(Spinner)v.findViewById(R.id.student_spin);
            }
    }
}
