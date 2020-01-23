package kheloindia.com.assessment.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
import kheloindia.com.assessment.R;
import kheloindia.com.assessment.TopSportsUpdateActivity;
import kheloindia.com.assessment.model.TopSportsUpdateItemModel;

/**
 * Created by CT13 on 2018-05-09.
 */

public class TopSportsUpdateAdapter extends BaseAdapter {


    private ArrayList<TopSportsUpdateItemModel> topSportsUpdateList = new ArrayList<TopSportsUpdateItemModel>();
    private Context context;
    public SparseIntArray selectedItems;
    private ArrayAdapter sport_spin_array;


    public  TopSportsUpdateAdapter(Context ctx, ArrayList<TopSportsUpdateItemModel> list) {
        topSportsUpdateList = list;
        context = ctx;
        selectedItems  = new SparseIntArray();

    }
    @Override
    public int getCount() {
        return topSportsUpdateList.size();
    }


    @Override
    public Object getItem(int position) {
        return topSportsUpdateList.get(position);
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

            convertView = inflater.inflate(R.layout.top_sports_update_item, null);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {

            viewHolder=(ViewHolder) convertView.getTag();

        }

        viewHolder.student_name_tv.setText( topSportsUpdateList.get(position).getStudent_name());
        viewHolder.student_roll_no_tv.setText( topSportsUpdateList.get(position).getStudent_roll_no());
        sport_spin_array = new ArrayAdapter<String>(context, R.layout.simple_spinner_item_2,topSportsUpdateList.get(position).getSportsList() );
        sport_spin_array.setDropDownViewResource(R.layout.spinner_dropdown_item);
        viewHolder.sports_spin.setAdapter(sport_spin_array);

        if(topSportsUpdateList.get(position).getSport_name()!=null)
        viewHolder.sports_spin.setSelection(topSportsUpdateList.get(position).getSportsList().indexOf(topSportsUpdateList.get(position).getSport_name()));
       else
            viewHolder.sports_spin.setSelection(0);
        viewHolder.sports_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int arg2, long id) {
                selectedItems.put(position, arg2 );

               if(topSportsUpdateList.get(position).getSportsList().indexOf(topSportsUpdateList.get(position).getSport_name())!=selectedItems.get(position)){
                   TopSportsUpdateActivity.save_btn.setVisibility(View.VISIBLE);
                   TopSportsUpdateActivity.email_btn.setVisibility(View.GONE);
                   TopSportsUpdateActivity.download_btn.setVisibility(View.GONE);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return convertView;
    }





    private static   class ViewHolder extends RecyclerView.ViewHolder {

        private TextView student_name_tv,student_roll_no_tv;
        private Spinner sports_spin;

        private  ViewHolder(View v){
            super(v);
            student_name_tv= (TextView) v.findViewById(R.id.student_name_tv);
            student_roll_no_tv=(TextView)v.findViewById(R.id.student_roll_no_tv);
            sports_spin=(Spinner)v.findViewById(R.id.sports_spin);

        }
    }





















}
