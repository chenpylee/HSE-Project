package com.ocse.hse.Models;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ocse.hse.Activities.EvaluationDetail;
import com.ocse.hse.R;
import com.ocse.hse.app.AppLog;
import com.ocse.hse.app.ApplicationConstants;

/**
 * Created by leehaining on 14-6-13.
 */

public class Safety1Adapter extends BaseAdapter {
    public static final int HDR_POS1 = 0;
    public static final int HDR_POS2 = 5;
    public static final String[] LIST = { "压力容器", "反应容器",
            "换热容器", "分离容器", "贮运容器", "电源电器",
            "配电柜", "电源线路", "变压器", "应急电源","交流电源" };
    private static final Integer LIST_HEADER = 0;
    private static final Integer LIST_ITEM = 1;
    private Activity activity;
    public Safety1Adapter(Activity activity) {
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
                    R.layout.lv_safety_row, parent, false);
            item.setTag(LIST_ITEM);
        }
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppLog.i("evaluation row clicked.");
            }
        });
        /**
        ImageButton btnDetail=(ImageButton)item.findViewById(R.id.button);
        btnDetail.setImageResource(R.drawable.ic_details);

        btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppLog.i("evaluation button clicked.");
                //goDetailActivity(LIST[position % LIST.length],SUBTEXTS[position % SUBTEXTS.length]);
            }
        });
         **/

        TextView header = (TextView)item.findViewById(R.id.lv_item_header);
        header.setText(LIST[position % LIST.length]);


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
        Intent intent=new Intent(mContext, EvaluationDetail.class);
        intent.putExtra(ApplicationConstants.APP_BUNDLE_EVALUATION_TITLE,title);
        intent.putExtra(ApplicationConstants.APP_BUNDLE_EVALUATION_DESCRIPTION,description);
        this.activity.startActivity(intent);
        this.activity.overridePendingTransition(R.anim.in_push_right_to_left, R.anim.push_down);

    }
}
