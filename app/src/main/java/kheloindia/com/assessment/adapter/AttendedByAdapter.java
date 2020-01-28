package kheloindia.com.assessment.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import kheloindia.com.assessment.R;
import kheloindia.com.assessment.model.AttendedByModel;

/**
 * Created by CT13 on 2018-03-15.
 */

public class AttendedByAdapter extends RecyclerView.Adapter<AttendedByAdapter.MyViewHolder> {

    private ArrayList<AttendedByModel> attendedByList = new ArrayList<AttendedByModel>();
    private Context context;

    public AttendedByAdapter(Context ctx, ArrayList<AttendedByModel> list) {
        attendedByList = list;
        context = ctx;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.attended_by_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.name_tv.setText(attendedByList.get(position).getName());
        holder.remark_tv.setText(attendedByList.get(position).getRemark());
        }

    @Override
    public int getItemCount() {
        return attendedByList.size();
    }

     class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView name_tv,remark_tv;

         private  MyViewHolder(View v){
             super(v);
            name_tv= (TextView) v.findViewById(R.id.name_tv);
            remark_tv=(TextView)v.findViewById(R.id.remark_tv);



        }
    }

}
