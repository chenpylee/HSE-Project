package com.ocse.hse.Models;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ocse.hse.R;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by leehaining on 14-7-6.
 */
public class CheckLv1Adapter extends BaseAdapter {
    public static final int HDR_POS1 = 0;
    private static final Integer LIST_HEADER = 0;
    private static final Integer LIST_ITEM = 1;
    private Activity activity;
    private String organ_id;
    private final Context mContext;
    private ArrayList<String> dataArrayList;
    public CheckLv1Adapter(Activity activity,ArrayList<String> Lv1List,String organ_id) {
        mContext = activity;
        this.activity=activity;
        this.organ_id=organ_id;
        dataArrayList=new ArrayList<String>();
        dataArrayList.add("检查分类");
        Iterator<String> it = Lv1List.iterator();
        while(it.hasNext())
        {
            String item = it.next();
            dataArrayList.add(item);
        }

    }
    @Override
    public int getCount() {
        return dataArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    public String getLv1(int position)
    {
        return dataArrayList.get(position);
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
        final String rule_lv1=dataArrayList.get(position);
        header.setText(rule_lv1);
        /**
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> lv2List=CheckRulesInfo.getLv2List(organ_id,rule_lv1);
                if(lv2List.size()>0)
                {
                    AppLog.i("Has next level");
                }
                else
                {
                    AppLog.i("NO next level");
                }
            }
        });
         **/
        return item;
    }

    private String getHeader(int position) {

        if(position == HDR_POS1) {
            return dataArrayList.get(position);
        }
        return null;
    }


}
