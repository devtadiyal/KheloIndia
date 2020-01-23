package kheloindia.com.assessment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import kheloindia.com.assessment.R;
import kheloindia.com.assessment.model.MyActivityLogItemModel;
import kheloindia.com.assessment.model.MyActivityLogModel;

/**
 * Created by CT13 on 2017-06-28.
 */

public class MyActivityLogAdapter
        extends BaseExpandableListAdapter implements View.OnClickListener

{

    private Context _context;
    private List<MyActivityLogModel> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<MyActivityLogModel, List<MyActivityLogItemModel>> _listDataChild;
    private MyViewHolderChild viewHolderChild;
    private MyViewHolderGroup viewHolderGroup;
    private ArrayAdapter student_spin_array;


    public MyActivityLogAdapter(Context context, List<MyActivityLogModel> listDataHeader,
                              HashMap<MyActivityLogModel, List<MyActivityLogItemModel>> listChildData) {
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


        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.my_activity_log_child, null);
            viewHolderChild = new MyViewHolderChild(convertView);
            convertView.setTag(viewHolderChild);
        } else {
            viewHolderChild = (MyViewHolderChild) convertView.getTag();
        }

        MyActivityLogItemModel myActivityLogBoardItemModel = (MyActivityLogItemModel) getChild(groupPosition, childPosition);
        viewHolderChild.my_log_tv.setText(myActivityLogBoardItemModel.getTitle());
        viewHolderChild.period_tv.setText(myActivityLogBoardItemModel.getPeriod());
        viewHolderChild.boys_tv.setText(myActivityLogBoardItemModel.getBoys());
        viewHolderChild.girls_tv.setText(myActivityLogBoardItemModel.getGirls());
        viewHolderChild.class_tv.setText(myActivityLogBoardItemModel.getClasss());
        viewHolderChild.chat_tv.setText(myActivityLogBoardItemModel.getChat());

        viewHolderChild.edit_tv.setOnClickListener(this);
        viewHolderChild.delete_tv.setOnClickListener(this);

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
            convertView = infalInflater.inflate(R.layout.my_activity_log_group, null);
            viewHolderGroup = new MyViewHolderGroup(convertView);
            convertView.setTag(viewHolderGroup);
        } else {
            viewHolderGroup = (MyViewHolderGroup) convertView.getTag();
        }

        ExpandableListView mExpandableListView = (ExpandableListView) parent;
        mExpandableListView.expandGroup(groupPosition);
        MyActivityLogModel myActivityLogBoardModel = (MyActivityLogModel) getGroup(groupPosition);
        viewHolderGroup.calendar_tv.setText(myActivityLogBoardModel.getDate());


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

    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.edit_tv:

                break;

            case R.id.delete_tv:

                break;
        }

    }


    private static class MyViewHolderChild  {

        TextView my_log_tv,period_tv,boys_tv,class_tv,delete_tv,edit_tv, girls_tv;
        TextView chat_tv;



        public  MyViewHolderChild(View v){


            my_log_tv= (TextView) v.findViewById(R.id.my_log_tv);
            period_tv= (TextView) v.findViewById(R.id.period_tv);
            boys_tv= (TextView) v.findViewById(R.id.boys_tv);
            class_tv= (TextView) v.findViewById(R.id.class_tv);
            girls_tv= (TextView) v.findViewById(R.id.girls_tv);
            delete_tv= (TextView) v.findViewById(R.id.delete_tv);
            edit_tv= (TextView) v.findViewById(R.id.edit_tv);
            chat_tv=(TextView)v.findViewById(R.id.chat_tv);
        }
    }

    private static class MyViewHolderGroup {

        TextView calendar_tv;


        public  MyViewHolderGroup(View v){

            calendar_tv = (TextView) v.findViewById(R.id.calendar_tv);

        }
    }



}
