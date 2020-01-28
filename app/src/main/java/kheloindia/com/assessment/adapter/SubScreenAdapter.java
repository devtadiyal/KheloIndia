package kheloindia.com.assessment.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import kheloindia.com.assessment.OpenScreenUrlActivity;
import kheloindia.com.assessment.R;
import kheloindia.com.assessment.ShowSubOfSubScreenActivity;
import kheloindia.com.assessment.functions.Constant;
import kheloindia.com.assessment.util.DBManager;

/**
 * Created by PC10 on 16-Mar-18.
 */

public class SubScreenAdapter extends RecyclerView.Adapter<SubScreenAdapter.MyViewHolder> {

    ArrayList<HashMap<String, String>> itemsList = new ArrayList<HashMap<String, String>>();
    Context context;
    String TAG = "SubScreenAdapter";
    DBManager db;
    SharedPreferences sp;

    public SubScreenAdapter(Context ctx, ArrayList<HashMap<String, String>> list) {
        itemsList = list;
        context = ctx;
        db = DBManager.getInstance();
        sp = PreferenceManager.getDefaultSharedPreferences(context);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_subcategory, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.subcat_tv.setTag(position);
        holder.relative_layout.setTag(position);

        holder.subcat_tv.setText(itemsList.get(position).get("screen_name"));


            holder.description_img.setVisibility(View.GONE);


        holder.relative_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Constant.END_URL="?UserName="+sp.getString("test_coordinator_name", "")+"&Password="+sp.getString("test_coordinator_password", "");


                int pos = (Integer) v.getTag();
                Constant.SCREEN_NAME =   itemsList.get(pos).get("screen_name");
                Constant.LOAD_URL = itemsList.get(pos).get("web_url");
                if(Constant.LOAD_URL.length()>0){
                    Constant.LOAD_URL = context.getString(R.string.image_base_url)+Constant.LOAD_URL+Constant.END_URL;
                    Log.e(TAG,"load url==> "+Constant.LOAD_URL);

                    //Toast.makeText(context,Constant.LOAD_URL,Toast.LENGTH_LONG).show();

                    Intent i = new Intent(context, OpenScreenUrlActivity.class);
                    context.startActivity(i);
                } else {

                    // Toast.makeText(context,"Empty url", Toast.LENGTH_SHORT).show();

                    Constant.SCREEN_ID = itemsList.get(pos).get("screen_id");
                    Constant.SCREEN_NAME = itemsList.get(pos).get("screen_name");


                    Intent i = new Intent(context, ShowSubOfSubScreenActivity.class);
                    context.startActivity(i);
                }


            }
        });

    }


    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView subcat_tv;
        RelativeLayout relative_layout;
        ImageView description_img;

        public  MyViewHolder(View v){
            super(v);
            subcat_tv = (TextView) v.findViewById(R.id.subcat_tv);
            relative_layout = (RelativeLayout) v.findViewById(R.id.relative_layout);
            description_img = (ImageView) v.findViewById(R.id.description_img);
        }
    }
}
