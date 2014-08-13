package com.ocse.hse.Models;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ocse.hse.R;
import com.ocse.hse.app.ApplicationController;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by leehaining on 14-7-6.
 */
public class CheckListAdapter extends BaseAdapter {
    private static final Integer LIST_HEADER = 0;
    private static final Integer LIST_ITEM = 1;
    private Activity activity;
    private final Context mContext;
    private ArrayList<CheckRuleSection> dataArrayList;
    private ArrayList<String> processedDataList;
    private ArrayList<Integer> headerPositionList;
    private ArrayList<CheckItemInfo> itemList;
    public CheckListAdapter(Activity activity,ArrayList<CheckRuleSection> inputDataList) {
        mContext = activity;
        this.activity=activity;
        dataArrayList=new ArrayList<CheckRuleSection>();

        Iterator<CheckRuleSection> it = inputDataList.iterator();
        while(it.hasNext())
        {
            CheckRuleSection item = it.next();
            dataArrayList.add(item);
        }
        processedDataList=new ArrayList<String>();
        itemList=new ArrayList<CheckItemInfo>();
        processSectionList();
    }
    public void processSectionList()
    {
        processedDataList.clear();
        itemList.clear();
        String task_id="";
        String organ_id="";
        String sectionName="";
        String rule_lv1="";
        String rule_lv2="";
        String rule_lv3="";
        String rule_content="";
        String rule_status="";
        int total=dataArrayList.size();
        int index=0;
        task_id= ApplicationController.getCurrentTaskID();
        organ_id=ApplicationController.getCurrentOrganID();
        for(int i=0;i<total;i++)
        {
            CheckRuleSection section=dataArrayList.get(i);
            sectionName=section.getSectionName();
            processedDataList.add(sectionName);
            itemList.add(new CheckItemInfo(true,sectionName, task_id, organ_id, rule_lv1, rule_lv2, rule_lv3, rule_content, rule_status));
            ArrayList<CheckRulesInfo> ruleList=section.getSectionRuleList();
            int rulesTotal=ruleList.size();
            for(int k=0;k<rulesTotal;k++)
            {
                CheckRulesInfo rule=ruleList.get(k);
                rule_lv1=rule.getRuleLv1();
                rule_lv2=rule.getRuleLv2();
                rule_lv3=rule.getRuleLv3();
                rule_content=rule.getRuleContent();
                rule_status=rule.getRuleStatus();
                itemList.add(new CheckItemInfo(false,sectionName, task_id, organ_id, rule_lv1, rule_lv2, rule_lv3, rule_content, rule_status));
                processedDataList.add(rule_content);
            }
        }

    }
    public void refillData(ArrayList<CheckRuleSection> inputDataList)
    {
        dataArrayList.clear();
        Iterator<CheckRuleSection> it = inputDataList.iterator();
        while(it.hasNext())
        {
            CheckRuleSection item = it.next();
            dataArrayList.add(item);
        }
        processSectionList();
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return processedDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String headerText = getHeader(position);
        if(headerText != null) { //Header

            View item = convertView;
            if(convertView == null || convertView.getTag() == LIST_ITEM) {

                item = LayoutInflater.from(mContext).inflate(
                        R.layout.lv_header_layout, parent, false);
                item.setTag(LIST_HEADER);
                //item.setClickable(false);

            }

            TextView headerTextView = (TextView)item.findViewById(R.id.lv_list_hdr);
            headerTextView.setText(headerText);
            return item;
        }
        View item = convertView;
        if(convertView == null || convertView.getTag() == LIST_HEADER) {
            item = LayoutInflater.from(mContext).inflate(
                    R.layout.lv_safety_row, parent, false);
            item.setTag(LIST_ITEM);
        }

        TextView header = (TextView)item.findViewById(R.id.lv_item_header);
        ImageView button=(ImageView)item.findViewById(R.id.button);
        CheckItemInfo checkItem=itemList.get(position);
        if(checkItem.getRuleStatus().toLowerCase().equals("yes"))
        {
            button.setImageResource(R.drawable.ic_evaluation_pass);
        }
        else if(checkItem.getRuleStatus().toLowerCase().equals("no"))
        {
            button.setImageResource(R.drawable.ic_evaluation_nopass);
        }
        else
        {
            button.setImageResource(R.drawable.ic_details);
        }

        String rule_content=checkItem.getRuleContent();
        header.setText(rule_content);
        return item;
    }

    private String getHeader(int position) {
        if(itemList.get(position).getIsHeader())
        {
            return itemList.get(position).getSectionName();
        }

        return null;
    }


}
