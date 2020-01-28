package kheloindia.com.assessment.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import kheloindia.com.assessment.DistanceActivity;
import kheloindia.com.assessment.FixedScoreActivity;
import kheloindia.com.assessment.OptionsActivity;
import kheloindia.com.assessment.TimeActivity;
import kheloindia.com.assessment.WeightActivity;
import kheloindia.com.assessment.YoutubeVideoActivity;
import kheloindia.com.assessment.model.TestCoordinatorMapping;
import kheloindia.com.assessment.util.DBManager;
import kheloindia.com.assessment.R;
import kheloindia.com.assessment.ShowSubTestActivity;
import kheloindia.com.assessment.functions.Constant;

/**
 * Created by PC10 on 05/11/2017.
 */

public class SubCatAdapter extends RecyclerView.Adapter<SubCatAdapter.MyViewHolder> {

    ArrayList<HashMap<String, String>> itemsList = new ArrayList<HashMap<String, String>>();
    Context context;
    String TAG = "SubCatAdapter";
    DBManager db;

    public SubCatAdapter(ShowSubTestActivity ctx, ArrayList<HashMap<String, String>> list) {
        itemsList = list;
        context = ctx;
        db = DBManager.getInstance();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_subcategory, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder,  int position) {

        holder.subcat_tv.setTag(position);
        holder.relative_layout.setTag(position);
        holder.video_img.setTag(position);
        Typeface font_reg = Typeface.createFromAsset(context.getAssets(),
                "fonts/Barlow-Regular.ttf");
        holder.subcat_tv.setText(itemsList.get(position).get("test_name"));
        holder.subcat_tv.setTypeface(font_reg);

        String purpose = itemsList.get(position).get("purpose");

        String test_description = itemsList.get(position).get("test_description");

        String Administrative_Suggestions = itemsList.get(position).get("Administrative_Suggestions");

        // if(test_description.length()>2){
        //holder.description_img.setVisibility(View.VISIBLE);
       /* } else {
            holder.description_img.setVisibility(View.GONE);
        }*/
        holder.video_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (Integer) view.getTag();
                Constant.VIDEO_LINK=itemsList.get(pos).get("video_link");
                Intent i=new Intent(context, YoutubeVideoActivity.class);
                context.startActivity(i);
            }
        });

        holder.description_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showPurposeDialog(holder.getAdapterPosition());
                //  Toast.makeText(context,itemsList.get(position).get("purpose"),Toast.LENGTH_LONG).show();
            }
        });

        holder.subcat_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int pos = (Integer) v.getTag();


                ArrayList<Object> objectArrayList1 = DBManager.getInstance().getAllTableData(context,
                        DBManager.TBL_LP_TEST_COORDINATOR_MAPPING,"camp_id", Constant.CAMP_ID,"school_id",Constant.SCHOOL_ID);

                ArrayList<String> test_type_id_list = new ArrayList<String>();

                for (Object o : objectArrayList1){
                    TestCoordinatorMapping testCoordinatorModel =(TestCoordinatorMapping) o;
                    test_type_id_list.add(""+testCoordinatorModel.getTest_type_id());
                }


                Log.e(TAG, "sub list size==> " + test_type_id_list.size());
                Log.e(TAG, "sub list ==> " + test_type_id_list);
                Log.e(TAG, "sub test clicked ==> " + itemsList.get(pos).get("sub_test_id"));

                //  if(test_type_id_list.contains(itemsList.get(pos).get("sub_test_id"))){
                Constant.SUB_TEST_TYPE = itemsList.get(pos).get("test_name");
                Constant.SUB_TEST_ID = itemsList.get(pos).get("sub_test_id");
                Constant.SCORE_MEASUREMENT = itemsList.get(pos).get("score_measurement");
                Constant.SCORE_UNIT = itemsList.get(pos).get("score_unit");
                Constant.MULTIPLE_LANE = itemsList.get(pos).get("multiple_lane");
                Constant.TIMER_TYPE = itemsList.get(pos).get("timer_type");
                //santosh

                Constant.FINAL_POSITION=itemsList.get(pos).get("final_position");
                //

                Log.e("TAG","pos==> "+pos);
                Log.e("TAG","SUB_TEST_TYPE==> "+Constant.SUB_TEST_TYPE);
                Log.e("TAG","SUB_TEST_ID==> "+Constant.SUB_TEST_ID);
                Log.e("TAG","SCORE_MEASUREMENT==> "+Constant.SCORE_MEASUREMENT);
                Log.e("TAG","SCORE_UNIT==> "+Constant.SCORE_UNIT);
                Log.e("TAG","MULTIPLE_LANE==> "+Constant.MULTIPLE_LANE);
                Log.e("TAG","TIMER_TYPE==> "+Constant.TIMER_TYPE);

                Log.e("TAG","itemsList====> "+itemsList);

                Log.e("TAG","pos==> "+pos);
                Log.e("TAG","SUB_TEST_TYPE==> "+Constant.SUB_TEST_TYPE);
                Log.e("TAG","SUB_TEST_ID==> "+Constant.SUB_TEST_ID);
                Log.e("TAG","SCORE_MEASUREMENT==> "+Constant.SCORE_MEASUREMENT);

               /* if(Constant.ISComingFromTestScreen){
                    Constant.ISComingFromTestScreen = false;
                    ((ShowSubTestActivity)context).finish();
                } else {
*/
                checkForTestANdNavigate(pos);

                    /*Intent i = new Intent(context, ScanActivity.class );
                    context.startActivity(i);
                    ((ShowSubTestActivity)context).finish();*/
                //   }


             /*   } else {7988732823
                    Toast.makeText(context,"You are not authorized to take this test.",Toast.LENGTH_SHORT).show();
                }*/


            }
        });

    }

    private void showPurposeDialog(int  pos) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setCancelable(false);

        String test_name = itemsList.get(pos).get("test_name");
        String test_description = itemsList.get(pos).get("test_description");
        String purpose = itemsList.get(pos).get("purpose");
        String Administrative_Suggestions = itemsList.get(pos).get("Administrative_Suggestions");
        String Equipment_Required = itemsList.get(pos).get("Equipment_Required");
        String scoring = itemsList.get(pos).get("scoring");

        String description_heading = String.valueOf(Html.fromHtml("<b>" + "Test Description: " + "</b>"));
        String purpose_heading = String.valueOf(Html.fromHtml("<b>" + "Purpose: " + "</b>"));
        String Administrative_Suggestions_heading = String.valueOf(Html.fromHtml("<b>" + "Administrative Suggestions: " + "</b>"));
        String Equipment_Required_heading = String.valueOf(Html.fromHtml("<b>" + "Equipment Required: " + "</b>"));
        String scoring_heading = String.valueOf(Html.fromHtml("<b>" + "Scoring: " + "</b>"));

        alertDialog.setTitle(test_name);
       if(purpose.equalsIgnoreCase("null") ||purpose.equalsIgnoreCase(""))
           purpose="N.A.";
        if(Administrative_Suggestions.equalsIgnoreCase("null") ||Administrative_Suggestions.equalsIgnoreCase(""))
            Administrative_Suggestions="N.A.";
        if(Equipment_Required.equalsIgnoreCase("null") ||Equipment_Required.equalsIgnoreCase(""))
            Equipment_Required="N.A.";
        if(scoring.equalsIgnoreCase("null") ||scoring.equalsIgnoreCase(""))
            scoring="N.A.";
        String message= description_heading+ test_description+ "\n\n"+
                purpose_heading+ purpose+ "\n\n"+  Administrative_Suggestions_heading+ Administrative_Suggestions+
                "\n\n"+ Equipment_Required_heading+ Equipment_Required+ "\n\n" +scoring_heading+ scoring;

        alertDialog.setMessage(message);

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.alarm);

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });

        alertDialog.show();

    }

    private void checkForTestANdNavigate(int pos) {

        Log.e(TAG,"score_measurement==> "+Constant.SCORE_MEASUREMENT);

        db.createNamedTable(context, db.TBL_LP_FITNESS_TEST_RESULT);

        String test_applicable = itemsList.get(pos).get("test_applicable");

        Constant.TEST_APPLICABLE = test_applicable;

        if(test_applicable.equals("1")){
            Constant.STUDENT_CATEGORY = "junior";
        }
        else  if(test_applicable.equals("2")){
            Constant.STUDENT_CATEGORY = "senior";
        }
        else  if(test_applicable.equals("3")){
            Constant.STUDENT_CATEGORY = "senior_junior";
        }

        if (Constant.SCORE_UNIT.equalsIgnoreCase("msec")) {
            // Constant.STUDENT_CATEGORY = "senior";
            Intent i = new Intent(context, TimeActivity.class);
            context.startActivity(i);
            ((ShowSubTestActivity)context).finish();
            System.out.println("DEV 1");
        } else if (Constant.SCORE_UNIT.equalsIgnoreCase("mm")) {
            // Constant.STUDENT_CATEGORY = "senior";
            Intent i = new Intent(context, DistanceActivity.class);
            context.startActivity(i);
            ((ShowSubTestActivity)context).finish();
            System.out.println("DEV 2");

        } else if (Constant.SCORE_UNIT.equalsIgnoreCase("gram")) {
            Constant.STUDENT_CATEGORY = "senior_junior";
            Intent i = new Intent(context, WeightActivity.class);
            context.startActivity(i);
            ((ShowSubTestActivity)context).finish();
            System.out.println("DEV 3");

        }  else if (Constant.SCORE_UNIT.equalsIgnoreCase("number")) {
            //  Constant.STUDENT_CATEGORY = "senior";
            Intent i = new Intent(context, FixedScoreActivity.class);

            String sub_test_name =  itemsList.get(pos).get("test_name");

            if(sub_test_name.equalsIgnoreCase("bent knee sit up")){
                Constant.SHOW_TIMER = true;
            } else {
                Constant.SHOW_TIMER = false;
            }

            context.startActivity(i);
            ((ShowSubTestActivity)context).finish();
            System.out.println("DEV 4");

        } else if(Constant.SCORE_UNIT.equalsIgnoreCase("skill")){
            // Constant.STUDENT_CATEGORY = "junior";
            Intent i = new Intent(context, OptionsActivity.class);
            context.startActivity(i);
            ((ShowSubTestActivity)context).finish();
            System.out.println("DEV 5");

        }
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView subcat_tv;
        RelativeLayout relative_layout;
        ImageView description_img,video_img;

        public  MyViewHolder(View v){
            super(v);
            subcat_tv = (TextView) v.findViewById(R.id.subcat_tv);
            relative_layout = (RelativeLayout) v.findViewById(R.id.relative_layout);
            description_img = (ImageView) v.findViewById(R.id.description_img);
            video_img=(ImageView)v.findViewById(R.id.video_img);
        }
    }


//    private void showCustomLanguageDialog(final int pos) {
//
//        final Dialog dialog = new Dialog(context);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setCancelable(false);
//        dialog.setContentView(R.layout.select_language_for_video_dialog);
//
//        TextView yes_tv =  dialog.findViewById(R.id.yes_tv);
//        TextView no_tv =  dialog.findViewById(R.id.no_tv);
//        final Switch language_switch=dialog.findViewById(R.id.language_switch);
//
//
//
//        yes_tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(!language_switch.isChecked()){
//                Constant.VIDEO_LINK=itemsList.get(pos).get("video_link");
//                Intent i=new Intent(context, YoutubeVideoActivity.class);
//                context.startActivity(i);}
//                else{
//                    Toast.makeText(context, "Hindi video unavailable for now.", Toast.LENGTH_SHORT).show();
//                }
//                dialog.dismiss();
//            }
//        });
//        no_tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
//        language_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if(b){
//                    language_switch.setText("Hindi");
//                    language_switch.setTextColor(context.getResources().getColor(R.color.colorPrimary));
//                }else{
//                    language_switch.setText("English");
//                    language_switch.setTextColor(context.getResources().getColor(R.color.colorPrimary));
//                }
//            }
//        });
//        dialog.show();
//
//
//    }
}

