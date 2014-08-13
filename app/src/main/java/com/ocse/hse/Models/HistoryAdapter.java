package com.ocse.hse.Models;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ocse.hse.Activities.HistoryDetail;
import com.ocse.hse.R;
import com.ocse.hse.app.AppLog;
import com.ocse.hse.app.ApplicationConstants;
import com.ocse.hse.app.ApplicationController;

/**
 * Created by leehaining on 14-6-13.
 */

public class HistoryAdapter extends BaseAdapter {
    public static final int HDR_POS1 = 0;
    public static final int HDR_POS2 = 5;
    public static final String[] LIST = { "2013-05-26 华北局安全检查","灭火器在有效期内","线路无裸露"};
    public static final String[] SUBTEXTS = { null, "检查有效期","检查线路是否有暴露情况"};

    private static final Integer LIST_HEADER = 0;
    private static final Integer LIST_ITEM = 1;
    private Activity activity;
    public HistoryAdapter(Activity activity) {
        mContext = activity;
        this.activity=activity;
    }
    public void updateStatus()
    {
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return LIST.length;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        String headerText = getHeader(position);
        if(headerText != null) { //Header

            View item = convertView;
            if(convertView == null || convertView.getTag() == LIST_ITEM) {

                item = LayoutInflater.from(mContext).inflate(
                        R.layout.lv_header_layout, parent, false);
                item.setTag(LIST_HEADER);

            }

            TextView headerTextView = (TextView)item.findViewById(R.id.lv_list_hdr);
            headerTextView.setText(headerText);
            return item;
        }

        //list item
        View item = convertView;
        if(convertView == null || convertView.getTag() == LIST_HEADER) {
            item = LayoutInflater.from(mContext).inflate(
                    R.layout.lv_layout, parent, false);
            item.setTag(LIST_ITEM);
        }
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppLog.i("evaluation row clicked.");
                goDetailActivity(LIST[position % LIST.length],SUBTEXTS[position % SUBTEXTS.length]);
            }
        });
        ImageButton btnDetail=(ImageButton)item.findViewById(R.id.button);
        String key="History-"+LIST[position % LIST.length];
        if(ApplicationController.getEvaluationResult(key)=="yes")
        {
            btnDetail.setImageResource(R.drawable.ic_evaluation_pass);
        }
        else
        {
            btnDetail.setImageResource(R.drawable.ic_evaluation_nopass);
        }

        btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppLog.i("evaluation button clicked.");
                goDetailActivity(LIST[position % LIST.length],SUBTEXTS[position % SUBTEXTS.length]);
            }
        });

        TextView header = (TextView)item.findViewById(R.id.lv_item_header);
        header.setText(LIST[position % LIST.length]);

        TextView subtext = (TextView)item.findViewById(R.id.lv_item_subtext);
        subtext.setText(SUBTEXTS[position % SUBTEXTS.length]);

        //Set last divider in a sublist invisible
        View divider = item.findViewById(R.id.item_separator);
        if(position == HDR_POS2 -1) {
            divider.setVisibility(View.INVISIBLE);
        }


        return item;
    }

    private String getHeader(int position) {

        if(position == HDR_POS1  || position == HDR_POS2) {
            return LIST[position];
        }

        return null;
    }

    private final Context mContext;

    private void goDetailActivity(String title,String description)
    {
        Intent intent=new Intent(mContext, HistoryDetail.class);
        intent.putExtra(ApplicationConstants.APP_BUNDLE_EVALUATION_TITLE,title);
        intent.putExtra(ApplicationConstants.APP_BUNDLE_EVALUATION_DESCRIPTION,description);
        this.activity.startActivity(intent);
        this.activity.overridePendingTransition(R.anim.in_push_right_to_left, R.anim.push_down);

    }
}
