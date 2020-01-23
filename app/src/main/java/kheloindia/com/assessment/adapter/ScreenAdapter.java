package kheloindia.com.assessment.adapter;

import android.content.Context;
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
 * Created by PC10 on 15-Mar-18.
 */

public class ScreenAdapter extends BaseAdapter {


    private Context mContext;
    private final ArrayList<HashMap<String, String>> screen;
    private ItemClickListener mClickListener;
    String TAG = "ScreenAdapter";


    public ScreenAdapter(Context c, ArrayList<HashMap<String, String>> screen) {
        mContext = c;
        this.screen = screen;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return screen.size();
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
        viewHolder.test_txt.setText(screen.get(position).get("screen_name"));

        String path = mContext.getResources().getString(R.string.image_base_url)+screen.get(position).get("icon_path")+screen.get(position).get("icon_image_name");

        Log.e(TAG,"path==> "+path);
        Picasso.with(mContext).load(path).into(viewHolder.test_img);

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