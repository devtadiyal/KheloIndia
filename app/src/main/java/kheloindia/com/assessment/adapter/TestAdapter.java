package kheloindia.com.assessment.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import kheloindia.com.assessment.R;
import kheloindia.com.assessment.util.ItemClickListener;

/**
 * Created by CT13 on 2017-05-11.
 */

public class TestAdapter extends BaseAdapter{


    private Context mContext;
    private final ArrayList<HashMap<String, String>> test;
    private ItemClickListener mClickListener;
    String TAG = "TestAdapter";


    public TestAdapter(Context c, ArrayList<HashMap<String, String>> test) {
        mContext = c;
        this.test = test;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return test.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ViewHolder viewHolder;
        if (convertView == null) {

            convertView = inflater.inflate(R.layout.test_item, null);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {

            viewHolder=(ViewHolder) convertView.getTag();

        }
        Typeface font_med = Typeface.createFromAsset(mContext.getAssets(),
                "fonts/Barlow-Medium.ttf");
        viewHolder.test_txt.setTypeface(font_med);
        viewHolder.test_txt.setText(test.get(position).get("test_name"));

        String path = mContext.getResources().getString(R.string.image_base_url)+test.get(position).get("test_img_path")+test.get(position).get("test_image");

        Log.e(TAG,"path==> "+path);
        Picasso.get().load(path).into(viewHolder.test_img);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null )
                    mClickListener.onItemClick(v, position);
            }
        });

        return convertView;
    }


    private class ViewHolder{
        TextView test_txt;
        ImageView test_img;

         ViewHolder(View v){
             test_txt = (TextView) v.findViewById(R.id.test_txt);
             test_img = (ImageView)v.findViewById(R.id.test_img);
        }

    }


    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
}
