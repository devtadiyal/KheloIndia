package kheloindia.com.assessment.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import kheloindia.com.assessment.MapActivity;
import kheloindia.com.assessment.R;
import kheloindia.com.assessment.util.ItemClickListener;

/**
 * Created by PC10 on 27-Mar-18.
 */

public class TrackingAdapter extends BaseAdapter {

    private Context mContext;
    private ItemClickListener mClickListener;
    private ArrayList<HashMap<String, String>> trackList;

    public TrackingAdapter(Context c,ArrayList<HashMap<String, String>> list) {
        mContext = c;
        this.trackList =list;

    }

    @Override
    public int getCount() {
        return trackList.size();
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

            convertView = inflater.inflate(R.layout.attendance_track_listitem, null);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {

            viewHolder=(ViewHolder) convertView.getTag();

        }



        String current_address = trackList.get(position).get("current_address");
        final String lat  = trackList.get(position).get("latitude");
        String created_on  = trackList.get(position).get("created_on");
        final String lng = trackList.get(position).get("longitude");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            Date d = sdf.parse("20130526160000");
        } catch (ParseException ex) {
         ex.printStackTrace();
        }

        //  viewHolder.text.setText(current_address+" latitude= "+lat+" longitude= "+lng);
        viewHolder.text.setText(created_on+": "+"You were at "+current_address);

        viewHolder.linear_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, MapActivity.class);
                i.putExtra("lat",lat);
                i.putExtra("lng",lng);
                mContext.startActivity(i);
            }
        });


        return convertView;
    }


    private class ViewHolder {
        TextView text;
        LinearLayout linear_layout;

        ViewHolder(View v) {
            text = (TextView) v.findViewById(R.id.text);
            linear_layout = (LinearLayout) v.findViewById(R.id.linear_layout);
        }


    }


    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }


}
