package com.ocse.hse.Models;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ocse.hse.R;
import com.ocse.hse.app.AppLog;

/**
 * Created by leehaining on 14-6-13.
 */

public class SafetySampleAdapter extends BaseAdapter {

    public static final String[] LIST = { "安全阀选型应正确", "安全阀应装设牢固的金属铭牌",
            "安全阀应当铅直安装于压力容器", "安全阀与压力容器之间不宜安装截止阀门", "安全阀每年至少校验一次", "安全阀在检验有效期以内",
            "安全阀铅封应完好", "铅封处应挂标牌，字迹清晰", "安全阀应为红色"};
    private static final Integer LIST_HEADER = 0;
    private static final Integer LIST_ITEM = 1;
    private Activity activity;
    public SafetySampleAdapter(Activity activity) {
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

        //list item
        View item = convertView;
        if(convertView == null) {
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
        ImageButton btnDetail=(ImageButton)item.findViewById(R.id.button);
        if(position==3||position==2) {
            btnDetail.setImageResource(R.drawable.ic_evaluation_nopass);
        }
        else
        {
            btnDetail.setImageResource(R.drawable.ic_evaluation_pass);
        }

        btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppLog.i("evaluation button clicked.");
                //goDetailActivity(LIST[position % LIST.length],SUBTEXTS[position % SUBTEXTS.length]);
            }
        });

        TextView header = (TextView)item.findViewById(R.id.lv_item_header);
        header.setText(LIST[position % LIST.length]);



        return item;
    }



    private final Context mContext;

}
