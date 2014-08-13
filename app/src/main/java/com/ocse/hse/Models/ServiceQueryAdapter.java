package com.ocse.hse.Models;

import android.app.Activity;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.ocse.hse.R;

/**
 * Created by leehaining on 14-4-16.
 */
public class ServiceQueryAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater mInflater;
    private TypedArray serviceIconData;
    private String[] serviceNameData;
    private String[] serviceDescriptionData;
    public ServiceQueryAdapter(Activity activity)
    {
        this.activity=activity;
        mInflater=LayoutInflater.from(this.activity);
        serviceIconData = activity.getResources().obtainTypedArray(R.array.service_icon_array);
        serviceNameData=activity.getResources().getStringArray(R.array.service_name_array);
        serviceDescriptionData=activity.getResources().getStringArray(R.array.service_description_array);
    }
    @Override
    public int getCount() {
        return serviceNameData.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
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
            convertView=mInflater.inflate(R.layout.row_service_row,null);
            viewHolder.serviceIcon=(ImageView)convertView.findViewById(R.id.serviceIcon);
            viewHolder.txtServiceName=(TextView)convertView.findViewById(R.id.serviceName);
            viewHolder.txtServiceDescription=(TextView)convertView.findViewById(R.id.serviceDescription);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder=(ViewHolder)convertView.getTag();
        }

        viewHolder.serviceIcon.setImageResource(serviceIconData.getResourceId(position,-1));
        viewHolder.txtServiceName.setText(serviceNameData[position]);
        viewHolder.txtServiceDescription.setText(serviceDescriptionData[position]);

        return convertView;
    }

    static class ViewHolder{
        ImageView serviceIcon;
        TextView txtServiceName;
        TextView txtServiceDescription;
    }
}
