package kheloindia.com.assessment.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import kheloindia.com.assessment.R;
import kheloindia.com.assessment.model.UserModel;
import kheloindia.com.assessment.util.AppConfig;
import kheloindia.com.assessment.util.ItemClickListener;

/**
 * Created by CT13 on 2017-05-11.
 */

public class ScoolAdapter extends BaseAdapter {

    private Context mContext;
    private boolean isCheck[];
    private ItemClickListener mClickListener;
    private ArrayList<HashMap<String, String>> schoolList;

    public ScoolAdapter(Context c,ArrayList<HashMap<String, String>> schoolList) {
        mContext = c;
        this.schoolList=schoolList;
        boolean isCheck[]=new boolean[schoolList.size()];
        this.isCheck=isCheck;
    }

    @Override
    public int getCount() {
        return schoolList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ViewHolder viewHolder;


        if (convertView == null) {

            convertView = inflater.inflate(R.layout.school_item, null);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {

            viewHolder=(ViewHolder) convertView.getTag();

        }

        HashMap<String,String> school = schoolList.get(position);
        String forRetest = school.get(AppConfig.FOR_RETEST);
        Resources resources = mContext.getResources();
//        if (forRetest.equalsIgnoreCase("1"))
//            viewHolder.school_cbx.setText(school.get(AppConfig.SCHOOL_NAME)+" (For Re-Test)");
//        else
//            viewHolder.school_cbx.setText(school.get(AppConfig.SCHOOL_NAME));
        if (forRetest.equalsIgnoreCase("1"))
            viewHolder.reTestLabel.setText(resources.getString(R.string.potential_talent_identification));
        else
            viewHolder.reTestLabel.setText(resources.getString(R.string.all_children));
        viewHolder.school_cbx.setText(school.get(AppConfig.SCHOOL_NAME));
        viewHolder.school_cbx.setChecked(isCheck[position]);

        viewHolder.school_cbx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(!isCheck[position]){
                    for(int i=0;i<schoolList.size();i++){
                        isCheck[i]=false;

                    }
                    isCheck[position]=true;
                    notifyDataSetChanged();
                }
                else{
                    viewHolder.school_cbx.setChecked(isCheck[position]);
                }
                if (mClickListener != null )
                   mClickListener.onItemClick(v, position);
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isCheck[position]){
                    for(int i=0;i<schoolList.size();i++){
                        isCheck[i]=false;

                    }
                    isCheck[position]=true;
                    notifyDataSetChanged();
                }

                if (mClickListener != null)
                    mClickListener.onItemClick(v, position);
            }
        });

        viewHolder.school_status.setVisibility(View.VISIBLE);
        if (school.get(AppConfig.IS_ATTACHED).equalsIgnoreCase("0")) {
            viewHolder.school_status.setText(R.string.deactivated);
            viewHolder.school_status.setTextColor(resources.getColor(R.color.red));
        }
        else {
            viewHolder.school_status.setText(R.string.activated);
            viewHolder.school_status.setTextColor(resources.getColor(R.color.green));
        }
        return convertView;
    }


    private class ViewHolder {
        CheckBox school_cbx;
        TextView school_status,reTestLabel;

         ViewHolder(View v) {
             school_cbx = v.findViewById(R.id.school_cbx);
             school_status = v.findViewById(R.id.school_status);
             reTestLabel = v.findViewById(R.id.retest_label);
         }

    }


    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }


}
