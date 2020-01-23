package kheloindia.com.assessment.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import kheloindia.com.assessment.HomeActivity;
import kheloindia.com.assessment.R;
import kheloindia.com.assessment.TakeTestActivity;
import kheloindia.com.assessment.functions.Constant;
/**
 * Created by PC10 on 05/11/2017.
 */

public class SchoolListAdapter extends RecyclerView.Adapter<SchoolListAdapter.MyViewHolder> {

    ArrayList<String> itemsList = new ArrayList<String>();
    Context context;
  Typeface font_light, font_regular, font_medium;

    public SchoolListAdapter(HomeActivity ctx, ArrayList<String> list) {
        itemsList = list;
        context = ctx;
        font_light = Typeface.createFromAsset(context.getAssets(),
                "fonts/Barlow-Light.ttf");
        font_regular = Typeface.createFromAsset(context.getAssets(),
                "fonts/Barlow-Regular.ttf");
        font_medium = Typeface.createFromAsset(context.getAssets(),
                "fonts/Barlow-Medium.ttf");
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_school, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.school_name_tv.setTag(position);
        holder.linear_layout.setTag(position);
        holder.img.setTag(position);

        holder.school_name_tv.setTypeface(font_regular);

        holder.school_name_tv.setText(itemsList.get(position));

        holder.linear_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int pos = (Integer) v.getTag();

                Log.e("TAG","pos==> "+pos);

                    holder.school_name_tv.setTextColor(context.getResources().getColor(R.color.colorAccent));
                    holder.img.setImageResource(R.mipmap.ic_launcher);

                Constant.SCHOOL_NAME = itemsList.get(position);

                Intent i = new Intent(context, TakeTestActivity.class );
                context.startActivity(i);

            }
        });

    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView school_name_tv;
            LinearLayout linear_layout;
            ImageView img;

        public  MyViewHolder(View v){
            super(v);
            school_name_tv = (TextView) v.findViewById(R.id.school_name_tv);
            linear_layout = (LinearLayout) v.findViewById(R.id.linear_layout);
            img = (ImageView) v.findViewById(R.id.img);
        }
    }
}



