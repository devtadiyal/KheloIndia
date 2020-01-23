package kheloindia.com.assessment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import kheloindia.com.assessment.R;
import kheloindia.com.assessment.util.ItemClickListener;

/**
 * Created by CT13 on 2017-06-28.
 */

public class CalendarAdapter extends BaseAdapter {

    private Context mContext;
    private ItemClickListener mClickListener;
    private List<String> yearOrMonthOrDateList;
    private int border[];
    private int text_color[];
    private  boolean click;

    public CalendarAdapter(Context c, List<String> yearOrMonthOrDateList) {
        mContext = c;
        this.yearOrMonthOrDateList=yearOrMonthOrDateList;
        int border[]=new int[yearOrMonthOrDateList.size()];
        int text_color[]=new int[yearOrMonthOrDateList.size()];
        this.border=border;
        this.text_color=text_color;


    }
    @Override
    public int getCount() {
        return yearOrMonthOrDateList.size();
    }

    @Override
    public Object getItem(int position) {
        return yearOrMonthOrDateList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
     ViewHolder viewHolder;



        if (convertView == null) {

            convertView = inflater.inflate(R.layout.calendar_item, null);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {

            viewHolder=(ViewHolder) convertView.getTag();

        }
        if(!click){
            border[position]=4;
        text_color[position]=R.color.grey;
        }
        viewHolder.year_or_month_or_date_tv.setText(yearOrMonthOrDateList.get(position));
        viewHolder.select_vw_top.setVisibility(border[position]);
        viewHolder.select_vw_bottom.setVisibility(border[position]);
        viewHolder.year_or_month_or_date_tv.setTextColor(mContext.getResources().getColor(text_color[position]));

        viewHolder.year_or_month_or_date_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click=true;
                for(int i=0;i<yearOrMonthOrDateList.size();i++){
                    border[i]=4;
                    text_color[i]=R.color.grey;
                }
                border[position]=0;
                text_color[position]=R.color.black;
                notifyDataSetChanged();

                if (mClickListener != null )
                    mClickListener.onItemClick(v, position);
            }
        });

        return convertView;
    }


    private static class ViewHolder {
       View select_vw_top,select_vw_bottom;
        TextView year_or_month_or_date_tv;

        ViewHolder(View v) {

            select_vw_top=v.findViewById(R.id.select_vw_top);
            select_vw_bottom=v.findViewById(R.id.select_vw_bottom);
            year_or_month_or_date_tv=(TextView)v.findViewById(R.id.year_or_month_tv);



        }


    }


    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

}
