package kheloindia.com.assessment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import kheloindia.com.assessment.R;
import kheloindia.com.assessment.model.ListViewDialogModel;

/**
 * Created by shyju New System on 14-Nov-17.
 */

public class ListViewDialogAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ListViewDialogModel> modelArrayList;
    private ViewHolder viewHolder;

    public ListViewDialogAdapter(Context context, ArrayList<ListViewDialogModel> modelArrayList) {
        this.context = context;
        this.modelArrayList = modelArrayList;
    }

    private class ViewHolder{
        TextView listItemTitle,listItemSubtitle;
        ImageView listItemSelectedIcon;
    }

    @Override
    public int getCount() {
        return modelArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return modelArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null){
            viewHolder = new ViewHolder();
            view = layoutInflater.inflate(R.layout.list_view_dialog_layout,viewGroup,false);
            viewHolder.listItemTitle = view.findViewById(R.id.list_item_title);
            viewHolder.listItemSelectedIcon = view.findViewById(R.id.checked_image);
            viewHolder.listItemSubtitle = view.findViewById(R.id.list_item_subtitle);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }

        ListViewDialogModel listViewDialogModel = modelArrayList.get(i);

        viewHolder.listItemTitle.setText(listViewDialogModel.getListItemTitle());

        if (listViewDialogModel.isItemSelected()){
            viewHolder.listItemSelectedIcon.setImageResource(R.drawable.selected);
        }
        else {
            viewHolder.listItemSelectedIcon.setImageResource(R.drawable.circle);
        }

        if (listViewDialogModel.getListItemSubtitle()!=null){
            viewHolder.listItemSubtitle.setVisibility(View.VISIBLE);
            viewHolder.listItemSubtitle.setText("("+listViewDialogModel.getListItemSubtitle()+")");
        }


        return view;
    }
}
