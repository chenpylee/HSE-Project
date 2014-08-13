package com.ocse.hse.Models;

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
 * Created by leehaining on 14-7-3.
 */
public class SelfCheckInfoAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<SelfCheckInfo> dataArrayList;
    public SelfCheckInfoAdapter(Context context,ArrayList<SelfCheckInfo> inputList)
    {
        this.context=context;
        this.mInflater=LayoutInflater.from(context);

        this.dataArrayList=new ArrayList<SelfCheckInfo>();
        Iterator<SelfCheckInfo> it = inputList.iterator();
        while(it.hasNext())
        {
            SelfCheckInfo item = it.next();
            this.dataArrayList.add(item);
        }
    }

    @Override
    public int getCount() {
        return this.dataArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.dataArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if(convertView==null)
        {
            viewHolder=new ViewHolder();
            convertView=mInflater.inflate(R.layout.row_self_task_list,null);
            viewHolder.txtCheckName=(TextView)convertView.findViewById(R.id.txtCheckName);
            viewHolder.txtRecordsTotal=(TextView)convertView.findViewById(R.id.txtRecordsTotal);
            viewHolder.txtTaskPeriod=(TextView)convertView.findViewById(R.id.txtTaskPeriod);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder=(ViewHolder)convertView.getTag();
        }
        SelfCheckInfo item=this.dataArrayList.get(position);
        String checkName=item.getCheckName();
        String checkPeriod="检查时间："+item.getCheckTime();
        viewHolder.txtRecordsTotal.setText(item.getCheckRecordsTotal());
        viewHolder.txtTaskPeriod.setText(item.getCheckTime());
        return convertView;
    }

    static class ViewHolder{
        TextView txtCheckName;
        TextView txtRecordsTotal;
        TextView txtTaskPeriod;
    }

}
