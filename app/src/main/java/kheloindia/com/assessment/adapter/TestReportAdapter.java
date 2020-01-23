package kheloindia.com.assessment.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kheloindia.com.assessment.R;
import kheloindia.com.assessment.model.FitnessSkillTestResultModel;
import kheloindia.com.assessment.model.SkillTestTypeModel;
import kheloindia.com.assessment.model.TestReportHeaderModel;
import kheloindia.com.assessment.model.TestReportModel;
import kheloindia.com.assessment.util.DBManager;
import kheloindia.com.assessment.util.Utility;

/**
 * Created by CT13 on 2017-05-15.
 */

public class TestReportAdapter extends BaseExpandableListAdapter {


    private Context mContext;
    private Typeface font_reg;
    private List<TestReportHeaderModel> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<TestReportHeaderModel, List<TestReportModel>> _listDataChild;


    public TestReportAdapter(Context context, List<TestReportHeaderModel> listDataHeader,
                             HashMap<TestReportHeaderModel, List<TestReportModel>> listChildData) {
        this. mContext = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.font_reg = Typeface.createFromAsset( mContext .getAssets(),
                "fonts/Barlow-Regular.ttf");
    }


    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolderGroup viewHolderGroup;
        if (convertView == null) {

            convertView = inflater.inflate(R.layout.test_status_report_item, null);
            viewHolderGroup=new ViewHolderGroup(convertView);
            convertView.setTag(viewHolderGroup);

        } else {

            viewHolderGroup=(ViewHolderGroup) convertView.getTag();
        }
        ExpandableListView mExpandableListView = (ExpandableListView) parent;
        mExpandableListView.expandGroup(groupPosition);

        TestReportHeaderModel testReport=(TestReportHeaderModel) getGroup(groupPosition);
        viewHolderGroup.gender_txt.setText(testReport.getStudent_name());
        viewHolderGroup.gender_txt.setTypeface(font_reg);
        viewHolderGroup.roll_no_txt.setText(testReport.getStudent_registration_num());
        viewHolderGroup.roll_no_txt.setTypeface(font_reg);
        if(testReport.getGender().equalsIgnoreCase("F"))
            viewHolderGroup.gender_img.setImageResource(R.drawable.girl_blue_i);
        else
            viewHolderGroup.gender_img.setImageResource(R.drawable.boy_i);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolderChild viewHolderChild;
        if (convertView == null) {

            convertView = inflater.inflate(R.layout.test_status_report_list_item, null);
            viewHolderChild=new ViewHolderChild(convertView);
            convertView.setTag(viewHolderChild);

        } else {

            viewHolderChild=(ViewHolderChild) convertView.getTag();

        }
        final TestReportModel testReportItem=(TestReportModel)getChild(groupPosition,childPosition);

        viewHolderChild.test_txt.setTypeface(font_reg);
        viewHolderChild.test_txt.setText(testReportItem.getTest_name()+" ("+testReportItem.getSubTestName()+")");
        viewHolderChild.score_txt.setTypeface(font_reg);
        viewHolderChild.score_txt.setBackgroundResource(android.R.color.transparent);

        if(testReportItem.getScore()!=null) {
            if (testReportItem.getTest_name().equalsIgnoreCase("agility") || testReportItem.getTest_name().equalsIgnoreCase("cardiovascular endurance") || testReportItem.getTest_name().equalsIgnoreCase("speed")|| testReportItem.getTest_name().equalsIgnoreCase("coordination"))
            {

                viewHolderChild.score_txt.setText(Utility.convertMilliSecondsToMiSecMs(this. mContext,Long.parseLong(testReportItem.getScore())));
            }
            else if(testReportItem.getSubTestName().equalsIgnoreCase("weight")){
                double score=(Double.parseDouble(testReportItem.getScore()))/1000;
                viewHolderChild.score_txt.setText(score+mContext.getString(R.string.kg));
            }
            else if(testReportItem.getSubTestName().contains("Ruler")||testReportItem.getSubTestName().equalsIgnoreCase("height")||testReportItem.getTest_name().equalsIgnoreCase("flexibility")||testReportItem.getTest_name().equalsIgnoreCase("power")){
                double score=(Double.parseDouble(testReportItem.getScore()))/10;
                viewHolderChild.score_txt.setText(Utility.convertCentiMetreToMtCmMm(this.mContext,score));
            }
            else if(testReportItem.getTest_name().contains("Manipulative")||testReportItem.getTest_name().contains("Body Management")||testReportItem.getTest_name().contains("Locomotor")){

                viewHolderChild.score_txt.setBackgroundResource(R.drawable.green_ring);
                viewHolderChild.score_txt.setText(testReportItem.getScore()+"/"+testReportItem.getTotal());
            }else if(testReportItem.getSubTestName().contains("Flamingo") && testReportItem.getScore().equalsIgnoreCase("1000")){
                viewHolderChild.score_txt.setText(R.string.disqualified);
            }

            else{
                viewHolderChild.score_txt.setText(testReportItem.getScore());
            }
        }
        else{
            viewHolderChild.score_txt.setText(R.string.nill_score);
        }

        if(testReportItem.getTestCompleted().equals("1"))
            viewHolderChild.test_img.setImageResource(R.drawable.complete);
        else{
            viewHolderChild.test_img.setImageResource(R.drawable.incomplete);
        }

        viewHolderChild.score_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(testReportItem.getTest_name().contains("Manipulative")||testReportItem.getTest_name().contains("Body Management")||testReportItem.getTest_name().contains("Locomotor")){
                    showPurposeDialog(testReportItem);
//                    int score=   Integer.parseInt(testReportItem.getScore());
//                if(score>2){
//                    showPurposeDialog(testReportItem);
//                }
//
//                else if(score>4)
//                {
//
//
//                }

                }
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }


    private class ViewHolderGroup{
        TextView gender_txt;
        ImageView gender_img;
        TextView roll_no_txt;


        ViewHolderGroup(View v){
            gender_txt = (TextView) v.findViewById(R.id.gender_txt);
            gender_img = (ImageView)v.findViewById(R.id.gender_img);
            roll_no_txt= (TextView) v.findViewById(R.id.roll_no_txt);

        }

    }
    private class ViewHolderChild{
        TextView test_txt,score_txt;
        ImageView test_img;

        ViewHolderChild(View v){
            test_txt = (TextView) v.findViewById(R.id.test_txt);
            test_img = (ImageView)v.findViewById(R.id.test_done_or_not_img);
            score_txt=(TextView)v.findViewById(R.id.score_txt);
        }

    }




    private void showPurposeDialog(TestReportModel testReportItem) {

        ArrayList<Object>arrayTypeList= DBManager.getInstance().getAllTableData(mContext,DBManager.TBL_LP_SKILL_TEST_TYPE, "test_type_id",testReportItem.getTest_type_id(),"","") ;
        ArrayList<Object>arrayResultList= DBManager.getInstance().getAllTableData(mContext,DBManager.TBL_LP_FITNESS_SKILL_TEST_RESULT,"student_id", testReportItem.getStudent_id(), "test_type_id",testReportItem.getTest_type_id()) ;
        String message="";
        boolean score=false;
        int count=0;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setCancelable(false);
        alertDialog.setTitle(testReportItem.getStudent_name()+" - " + testReportItem.getSubTestName());
        for(int i=0;i<arrayTypeList.size();i++)
        {
            SkillTestTypeModel skillTestTypeModel=(SkillTestTypeModel)arrayTypeList.get(i);
            message = message +(i+1)+ "."+ skillTestTypeModel.getItem_name().trim();

            for(int j=0;j<arrayResultList.size();j++) {

                FitnessSkillTestResultModel fitnessSkillTestResultModel = (FitnessSkillTestResultModel) arrayResultList.get(j);
                if(skillTestTypeModel.getChecklist_item_id()==fitnessSkillTestResultModel.getChecklist_item_id())
                {
                    if (fitnessSkillTestResultModel.getScore().equalsIgnoreCase("True")
                            ||
                            fitnessSkillTestResultModel.getScore().equalsIgnoreCase("1"))
                    {
                        score=true;
                        break;
                    }
                }
                score=false;
            }
            if(score)
                message = message + " (Y) ";
            message = message + " \n\n ";


        }
        // alertDialog.setTitle(Constant.SUB_TEST_TYPE);

//        String test_description = itemsList.get(pos).get("test_description");
//        String purpose = itemsList.get(pos).get("purpose");
//        String Administrative_Suggestions = itemsList.get(pos).get("Administrative_Suggestions");
//        String Equipment_Required = itemsList.get(pos).get("Equipment_Required");
//        String scoring = itemsList.get(pos).get("scoring");

//        String description_heading = String.valueOf(Html.fromHtml("<b>" + "Test Description: " + "</b>"));
//        String purpose_heading = String.valueOf(Html.fromHtml("<b>" + "Purpose: " + "</b>"));
//        String Administrative_Suggestions_heading = String.valueOf(Html.fromHtml("<b>" + "Administrative Suggestions: " + "</b>"));
//        String Equipment_Required_heading = String.valueOf(Html.fromHtml("<b>" + "Equipment Required: " + "</b>"));
//        String scoring_heading = String.valueOf(Html.fromHtml("<b>" + "Scoring: " + "</b>"));
//

        alertDialog.setMessage(message);

        // Setting Icon to Dialog


        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });

        alertDialog.show();

    }


}
