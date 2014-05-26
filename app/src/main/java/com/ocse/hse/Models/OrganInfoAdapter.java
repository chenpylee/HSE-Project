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
 * Created by leehaining on 14-5-22.
 */
public class OrganInfoAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<OrganInfo> dataArrayList;
    public OrganInfoAdapter(Context context,ArrayList<OrganInfo> inputList)
    {
        this.context=context;
        this.mInflater=LayoutInflater.from(context);

        this.dataArrayList=new ArrayList<OrganInfo>();
        Iterator<OrganInfo> it = inputList.iterator();
        while(it.hasNext())
        {
            OrganInfo item = it.next();
            this.dataArrayList.add(item);
        }
    }
    @Override
    public int getCount() {
        return dataArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataArrayList.get(position);
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
            convertView=mInflater.inflate(R.layout.row_organ_list,null);
            viewHolder.txtOrganName=(TextView)convertView.findViewById(R.id.txtOrganName);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder=(ViewHolder)convertView.getTag();
        }
        OrganInfo item=this.dataArrayList.get(position);
        String organName=item.getOrganParentName()+"-"+item.getOrganName();
        viewHolder.txtOrganName.setText(organName);
        return convertView;
    }

    static class ViewHolder{
        TextView txtOrganName;
    }
}
