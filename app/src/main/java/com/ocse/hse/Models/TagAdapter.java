package com.ocse.hse.Models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ocse.hse.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by leehaining on 14-5-28.
 */
public class TagAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<TagBasicInfo> dataArrayList;
    public TagAdapter(Context context,ArrayList<TagBasicInfo> inputList)
    {
        this.context=context;
        this.mInflater=LayoutInflater.from(context);

        this.dataArrayList=new ArrayList<TagBasicInfo>();
        Iterator<TagBasicInfo> it = inputList.iterator();
        while(it.hasNext())
        {
            TagBasicInfo item = it.next();
            this.dataArrayList.add(item);
        }
    }
    public void refillData(ArrayList<TagBasicInfo> inputList)
    {
        this.dataArrayList.clear();
        Iterator<TagBasicInfo> it = inputList.iterator();
        while(it.hasNext())
        {
            TagBasicInfo item = it.next();
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
            convertView=mInflater.inflate(R.layout.row_card_list,null);
            viewHolder.imgTagType=(ImageView)convertView.findViewById(R.id.imgTagType);
            viewHolder.txtTagName=(TextView)convertView.findViewById(R.id.txtTagName);
            viewHolder.txtTagTime=(TextView)convertView.findViewById(R.id.txtTagTime);
            viewHolder.txtTagCompany=(TextView)convertView.findViewById(R.id.txtTagCompany);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder=(ViewHolder)convertView.getTag();
        }
        TagBasicInfo item=this.dataArrayList.get(position);
        String tagTime=item.getTagTime();
        String tagJson=item.getCardJson();
        String tagType="ERROR";
        String tagName="";
        String tagCompany="";
        try{
            JSONObject contentObject=new JSONObject(tagJson);
            JSONObject cardInfo=contentObject.optJSONObject("basic");
            if(cardInfo!=null) {
                tagType = cardInfo.optString("CARD_TYPE", "ERROR");
                String CARD_NAME = cardInfo.optString("CARD_NAME", "");
                String CARD_TEAM = cardInfo.optString("CARD_TEAM", "");
                if (tagType.equals("JSY")) {
                    tagName = CARD_NAME;
                    tagCompany = CARD_TEAM;
                }
                if (tagType.equals("CBS")) {
                    tagName = CARD_TEAM;
                    JSONObject teamInfo=contentObject.optJSONObject("teamInfo");
                    if(teamInfo!=null)
                    {
                        tagName=teamInfo.optString("SGDMC","");
                    }
                    tagCompany = CARD_NAME;
                }
            }
            else
            {
                tagType="ERROR";
                tagName=item.getTagID();
                tagCompany="在HSE数据库中无此卡信息";
            }

        }catch(JSONException jsonException)
        {

        }
        if(tagType.equals("ERROR"))
        {
            viewHolder.imgTagType.setImageResource(R.drawable.ic_action_error);
        }
        else if(tagType.equals("JSY"))
        {
            viewHolder.imgTagType.setImageResource(R.drawable.ic_action_person);
        }
        else if(tagType.equals("CBS"))
        {
            viewHolder.imgTagType.setImageResource(R.drawable.ic_action_group);
        }
        viewHolder.txtTagCompany.setText(tagCompany);
        viewHolder.txtTagName.setText(tagName);
        viewHolder.txtTagTime.setText(tagTime);

        return convertView;
    }
    static class ViewHolder{
        ImageView imgTagType;
        TextView txtTagName,txtTagTime,txtTagCompany;
    }
}
