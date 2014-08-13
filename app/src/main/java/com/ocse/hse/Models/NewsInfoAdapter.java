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
 * Created by leehaining on 14-4-21.
 */
public class NewsInfoAdapter extends BaseAdapter {
    private ArrayList<NewsInfo> dataArrayList;
    private Context context;
    private LayoutInflater mInflater;
    private Boolean hasLastRow;
    public NewsInfoAdapter(Context context, ArrayList<NewsInfo> inputList)
    {
        this.context=context;
        mInflater=LayoutInflater.from(context);
        this.dataArrayList=new ArrayList<NewsInfo>();
        Iterator<NewsInfo> it = inputList.iterator();
        while(it.hasNext())
        {
            NewsInfo item = it.next();
            this.dataArrayList.add(item);
        }
        hasLastRow=true;
    }
    public void addItems(ArrayList<NewsInfo> inputlist)
    {
        Iterator<NewsInfo> it = inputlist.iterator();
        while(it.hasNext())
        {
            NewsInfo item = it.next();
            this.dataArrayList.add(item);
        }
        notifyDataSetChanged();
    }
    public void refillData(ArrayList<NewsInfo> inputlist)
    {
        this.dataArrayList.clear();
        Iterator<NewsInfo> it = inputlist.iterator();
        while(it.hasNext())
        {
            NewsInfo item = it.next();
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

    public NewsInfo getNews(int position){
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
            convertView=mInflater.inflate(R.layout.row_news_info,null);
            viewHolder.txtNewsTitle=(TextView)convertView.findViewById(R.id.txtNewsTitle);
            viewHolder.txtNewsUpdateTime=(TextView)convertView.findViewById(R.id.txtNewsUpdateTime);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder=(ViewHolder)convertView.getTag();
        }
        NewsInfo item=this.dataArrayList.get(position);
        viewHolder.txtNewsTitle.setText(item.getTitle());
        viewHolder.txtNewsUpdateTime.setText(item.getUpdatetime());
        return convertView;
    }

    static class ViewHolder{
        TextView txtNewsTitle;
        TextView txtNewsUpdateTime;
    }
}
