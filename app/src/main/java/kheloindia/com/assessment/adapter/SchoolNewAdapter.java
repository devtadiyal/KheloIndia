package kheloindia.com.assessment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import kheloindia.com.assessment.R;
import kheloindia.com.assessment.util.ItemClickListener;

/**
 * Created by PC10 on 18-Aug-17.
 */

public class SchoolNewAdapter extends BaseAdapter {

    private Context mContext;
    private boolean isCheck[];
    private ItemClickListener mClickListener;
    private ArrayList<HashMap<String, String>> schoolList;

    public SchoolNewAdapter(Context c,ArrayList<HashMap<String, String>> schoolList) {
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

            convertView = inflater.inflate(R.layout.activity_school_listitem, null);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {

            viewHolder=(ViewHolder) convertView.getTag();

        }
        viewHolder.school_name_tv.setText(schoolList.get(position).get("school_name"));


        return convertView;
    }


    private class ViewHolder {
        TextView school_name_tv;

        ViewHolder(View v) {
            school_name_tv = (TextView) v.findViewById(R.id.school_name_tv);
        }


    }


    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }


}

