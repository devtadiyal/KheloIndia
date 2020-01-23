package kheloindia.com.assessment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import kheloindia.com.assessment.R;
import kheloindia.com.assessment.model.ActivityLogChildModel;
import kheloindia.com.assessment.model.ActivityLogHeaderModel;

import kheloindia.com.assessment.util.ItemClickListener;

import static kheloindia.com.assessment.functions.Constant.student_spin_data;

/**
 * Created by CT13 on 2017-11-16.
 */

public class ActivityLogAdapter
        extends BaseExpandableListAdapter implements ItemClickListener

    {

        private Context _context;
        private List<ActivityLogHeaderModel> _listDataHeader; // header titles
        // child data in format of header title, child title
        private HashMap<ActivityLogHeaderModel, List<ActivityLogChildModel>> _listDataChild;
        private ViewHolderChild viewHolderChild;
        private ViewHolderGroup viewHolderGroup;
        private  ArrayAdapter student_spin_array;
        private ItemClickListener mClickListener;




    public ActivityLogAdapter(Context context, List<ActivityLogHeaderModel> listDataHeader,
            HashMap<ActivityLogHeaderModel, List<ActivityLogChildModel>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
       // student_spin_data.add(context.getString(R.string.student_select));
       if(student_spin_data.size()==0){
           student_spin_data.add(context.getString(R.string.exemplary));
           student_spin_data.add(context.getString(R.string.proficient));
           student_spin_data.add(context.getString(R.string.learning));
           student_spin_data.add(context.getString(R.string.absent));

        student_spin_array = new ArrayAdapter<String>(context, R.layout.spinner_item,student_spin_data );
        student_spin_array.setDropDownViewResource(R.layout.spinner_dropdown_item);
       }

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
            convertView = infalInflater.inflate(R.layout.log_child, null);
            viewHolderChild=new ViewHolderChild(convertView);
            convertView.setTag(viewHolderChild);
        }
        else {
            viewHolderChild=(ViewHolderChild) convertView.getTag();
        }

            ActivityLogChildModel myActivityLogBoardItemModel=(ActivityLogChildModel)getChild(groupPosition,childPosition);
            viewHolderChild.student_tv.setText(myActivityLogBoardItemModel.getStudent_name());
            viewHolderChild.student_spin.setAdapter(student_spin_array);

//            viewHolderChild.student_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    if(mClickListener!=null)
//                        mClickListener.onItemClick(view, childPosition);
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//
//                }
//            });

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
            convertView = infalInflater.inflate(R.layout.log_group, null);
            viewHolderGroup=new ViewHolderGroup(convertView);
            convertView.setTag(viewHolderGroup);
        }
        else {
            viewHolderGroup=(ViewHolderGroup) convertView.getTag();
        }

        ExpandableListView mExpandableListView = (ExpandableListView) parent;
        mExpandableListView.expandGroup(groupPosition);
            ActivityLogHeaderModel myActivityLogBoardModel= (ActivityLogHeaderModel) getGroup(groupPosition);
            viewHolderGroup.class_tv.setText(myActivityLogBoardModel.getStudent_class());
            viewHolderGroup.period_tv.setText(myActivityLogBoardModel.getPeriod());

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
        public void onItemClick(View view, int position) {

            Spinner spinner=(Spinner)view;
           if(spinner.getSelectedItemPosition()==0) {



           }
        }


        private class ViewHolderChild {

            TextView student_tv;
            Spinner student_spin;

            ViewHolderChild(View v) {
                student_tv=(TextView)v.findViewById(R.id.student_tv);
                student_spin=(Spinner)v.findViewById(R.id.student_spin);
            }


        }

        private class ViewHolderGroup {

            TextView class_tv,period_tv;

            ViewHolderGroup(View v) {
                class_tv=(TextView)v.findViewById(R.id.class_tv);
                period_tv=(TextView)v.findViewById(R.id.period_tv);
            }

        }


        // allows clicks events to be caught
        public void setClickListener(ItemClickListener itemClickListener) {
            this.mClickListener = itemClickListener;
        }




    }
