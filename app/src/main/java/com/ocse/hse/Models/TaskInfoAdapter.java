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
public class TaskInfoAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<TaskInfo> dataArrayList;
    public TaskInfoAdapter(Context context,ArrayList<TaskInfo> inputList)
    {
        this.context=context;
        this.mInflater=LayoutInflater.from(context);

        this.dataArrayList=new ArrayList<TaskInfo>();
        Iterator<TaskInfo> it = inputList.iterator();
        while(it.hasNext())
        {
            TaskInfo item = it.next();
            this.dataArrayList.add(item);
        }
    }
    public void refillTaskList(ArrayList<TaskInfo> inputList)
    {
        this.dataArrayList.clear();
        Iterator<TaskInfo> it = inputList.iterator();
        while(it.hasNext())
        {
            TaskInfo item = it.next();
            this.dataArrayList.add(item);
        }
        notifyDataSetChanged();
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
            convertView=mInflater.inflate(R.layout.row_task_list,null);
            viewHolder.txtTaskName=(TextView)convertView.findViewById(R.id.txtTaskName);
            viewHolder.txtTaskPeriod=(TextView)convertView.findViewById(R.id.txtTaskPeriod);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder=(ViewHolder)convertView.getTag();
        }
        TaskInfo item=this.dataArrayList.get(position);
        String taskName=item.getTaskName();
        String taskPeirod=item.getTaskStartDateString()+" ~ "+item.getTaskEndDateString();
        viewHolder.txtTaskName.setText(item.getTaskName());
        viewHolder.txtTaskPeriod.setText(taskPeirod);
        return convertView;
    }

    static class ViewHolder{
        TextView txtTaskName;
        TextView txtTaskPeriod;
    }
}
