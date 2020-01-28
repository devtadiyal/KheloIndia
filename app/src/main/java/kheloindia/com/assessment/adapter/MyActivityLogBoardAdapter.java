package kheloindia.com.assessment.adapter;

import android.content.Context;
import android.graphics.Typeface;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import kheloindia.com.assessment.R;

import kheloindia.com.assessment.model.MyActivityLogBoardItemModel;
import kheloindia.com.assessment.model.MyActivityLogBoardModel;

/**
 * Created by CT13 on 2017-07-04.
 */

public class MyActivityLogBoardAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<MyActivityLogBoardModel> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<MyActivityLogBoardModel, List<MyActivityLogBoardItemModel>> _listDataChild;
    private ViewHolderGroup viewHolderGroup;
    private  Typeface font_light;
    private  AttendedByAdapter attendedByAdapter;


    public MyActivityLogBoardAdapter(Context context, List<MyActivityLogBoardModel> listDataHeader,
                                     HashMap<MyActivityLogBoardModel, List<MyActivityLogBoardItemModel>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final ViewHolderChild   viewHolderChild;

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.my_activity_log_board_child, null);
             viewHolderChild=new ViewHolderChild(convertView);
            convertView.setTag(viewHolderChild);
        }
        else {
            viewHolderChild=(ViewHolderChild) convertView.getTag();
        }
        font_light = Typeface.createFromAsset(this._context.getAssets(),
                "fonts/Roboto-Light" +
                        "_0.ttf");
        final MyActivityLogBoardItemModel myActivityLogBoardItemModel=(MyActivityLogBoardItemModel)getChild(groupPosition,childPosition);
        viewHolderChild.period_tv.setText("Period: "+myActivityLogBoardItemModel.getPeriod());
        viewHolderChild.boys_tv.setText("Boys: "+myActivityLogBoardItemModel.getBoys());
        viewHolderChild.girls_tv.setText("Girls: "+myActivityLogBoardItemModel.getGirls());
        viewHolderChild.class_tv.setText("Class: "+myActivityLogBoardItemModel.getClass_Name());
        viewHolderChild.my_log_tv.setText(myActivityLogBoardItemModel.getSkill_name());
        viewHolderChild.skill_area_tv.setText(myActivityLogBoardItemModel.getLessons_Others());
        viewHolderChild.chat_tv.setText(myActivityLogBoardItemModel.getRemarks());
        attendedByAdapter=new AttendedByAdapter(this._context,myActivityLogBoardItemModel.getAttendedByModelList());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this._context);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        viewHolderChild.student_details_rv.setLayoutManager(mLayoutManager);
        viewHolderChild.student_details_rv.setAdapter(attendedByAdapter);
        viewHolderChild.attended_by_tv.setTypeface(font_light);
        viewHolderChild.attended_by_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!myActivityLogBoardItemModel.isAttended_max()){
                    viewHolderChild.student_details_rv.setVisibility(View.VISIBLE);
                    viewHolderChild.attended_by_tv.setText(R.string.attended_min);
                    myActivityLogBoardItemModel.setAttended_max(true);
                }else{
                    viewHolderChild.student_details_rv.setVisibility(View.GONE);
                    viewHolderChild.attended_by_tv.setText(R.string.attended_max);
                    myActivityLogBoardItemModel.setAttended_max(false);
                }

            }
        });
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {


        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.my_activity_log_board_group, null);
            viewHolderGroup=new ViewHolderGroup(convertView);
            convertView.setTag(viewHolderGroup);
        }
        else {
            viewHolderGroup=(ViewHolderGroup) convertView.getTag();
        }


        ExpandableListView mExpandableListView = (ExpandableListView) parent;
        mExpandableListView.expandGroup(groupPosition);
        MyActivityLogBoardModel myActivityLogBoardModel= (MyActivityLogBoardModel) getGroup(groupPosition);
        viewHolderGroup.calendar_tv.setText(myActivityLogBoardModel.getDate());
        if(myActivityLogBoardModel.getAttendance()!=null) {
        viewHolderGroup.start_end_time_tv.setText(myActivityLogBoardModel.getStart_end_time());
            if (myActivityLogBoardModel.getAttendance().equalsIgnoreCase("Late"))
                viewHolderGroup.start_end_time_tv.setTextColor(this._context.getResources().getColor(R.color.red));
             else
                 viewHolderGroup.start_end_time_tv.setTextColor(this._context.getResources().getColor(R.color.green));

        }
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    private class ViewHolderChild {

        TextView my_log_tv,period_tv,boys_tv,class_tv,girls_tv,chat_tv,attended_by_tv,skill_area_tv;
        RecyclerView student_details_rv;

        ViewHolderChild(View v) {
            my_log_tv=(TextView)v.findViewById(R.id.my_log_tv);
            period_tv=(TextView)v.findViewById(R.id.period_tv);
            boys_tv=(TextView)v.findViewById(R.id.boys_tv);
            class_tv=(TextView)v.findViewById(R.id.class_tv);
            girls_tv=(TextView)v.findViewById(R.id.girls_tv);
            chat_tv=(TextView)v.findViewById(R.id.chat_tv);
            attended_by_tv=(TextView)v.findViewById(R.id.attended_by_tv) ;
            student_details_rv=(RecyclerView)v.findViewById(R.id.student_details_rv);
            skill_area_tv=(TextView)v.findViewById(R.id.skill_area_tv);
            }


    }

    private class ViewHolderGroup {

        TextView calendar_tv,start_end_time_tv;

        ViewHolderGroup(View v) {
            calendar_tv=(TextView)v.findViewById(R.id.calendar_tv);
            start_end_time_tv=(TextView)v.findViewById(R.id.start_end_time_tv);
        }


    }
}
