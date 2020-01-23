package kheloindia.com.assessment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import kheloindia.com.assessment.R;
import kheloindia.com.assessment.model.TestReportItemModel;
import kheloindia.com.assessment.util.ItemClickListener;

/**
 * Created by CT13 on 2017-05-15.
 */

public class TestReportItemAdapter extends BaseAdapter {
    private Context mContext;
    private List<TestReportItemModel> testReportItemList;
    private ItemClickListener mClickListener;


    public TestReportItemAdapter(Context c,List<TestReportItemModel> testReportItemList) {
        mContext = c;
        this.testReportItemList = testReportItemList;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return testReportItemList.size();
    }
    @Override
    public Object getItem(int position) {
        return testReportItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ViewHolder viewHolder;
        if (convertView == null) {

            convertView = inflater.inflate(R.layout.test_status_report_list_item, null);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {

            viewHolder=(ViewHolder) convertView.getTag();

        }
         TestReportItemModel testReportItem=(TestReportItemModel)getItem(position);


        viewHolder.test_txt.setText(testReportItem.getTest_name());
if(testReportItem.isTested())
    viewHolder.test_img.setImageResource(R.drawable.complete);
        else
    viewHolder.test_img.setImageResource(R.drawable.incomplete);
        return convertView;
    }


    private class ViewHolder{
        TextView test_txt;
        ImageView test_img;

        ViewHolder(View v){
            test_txt = (TextView) v.findViewById(R.id.test_txt);
            test_img = (ImageView)v.findViewById(R.id.test_done_or_not_img);
        }

    }


    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
}
