package com.ocse.hse.Models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ocse.hse.R;
import com.ocse.hse.app.ApplicationController;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by leehaining on 14-6-5.
 */
public class HistoryInfoAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<RecordInfo> dataArrayList;
    private ArrayList<JCDWInfo> organList;
    public HistoryInfoAdapter(Context context, ArrayList<RecordInfo> inputList)
    {
        this.context=context;
        this.mInflater=LayoutInflater.from(context);

        this.dataArrayList=new ArrayList<RecordInfo>();
        Iterator<RecordInfo> it = inputList.iterator();
        while(it.hasNext())
        {
            RecordInfo item = it.next();
            this.dataArrayList.add(item);
        }
        organList=JCDWInfo.getJCDWByJR(ApplicationController.getCurrentTaskID());
    }
    public void refillData(ArrayList<RecordInfo> inputList)
    {
        this.dataArrayList.clear();
        Iterator<RecordInfo> it = inputList.iterator();
        while(it.hasNext())
        {
            RecordInfo item = it.next();
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
            convertView=mInflater.inflate(R.layout.row_history_record_list,null);
            viewHolder.txtOrganName=(TextView)convertView.findViewById(R.id.txtOrganName);
            viewHolder.txtCreated=(TextView)convertView.findViewById(R.id.txtCreated);
            viewHolder.txtRuleLv1=(TextView)convertView.findViewById(R.id.txtRuleLv1);
            viewHolder.txtRuleLv2=(TextView)convertView.findViewById(R.id.txtRuleLv2);
            viewHolder.txtRuleLv3=(TextView)convertView.findViewById(R.id.txtRuleLv3);
            viewHolder.txtRuleContent=(TextView)convertView.findViewById(R.id.txtRuleContent);
            viewHolder.txtDescription=(TextView)convertView.findViewById(R.id.txtDescription);
            viewHolder.txtContact=(TextView)convertView.findViewById(R.id.txtContact);
            viewHolder.txtPhone=(TextView)convertView.findViewById(R.id.txtPhone);
            viewHolder.imgPreview=(ImageView)convertView.findViewById(R.id.imgPreview);
            viewHolder.imgContainer=(LinearLayout)convertView.findViewById(R.id.imgContainer);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder=(ViewHolder)convertView.getTag();
        }
        RecordInfo item=this.dataArrayList.get(position);
        String dwmc="";
        String organID=item.getOrganID();
        for(int i=0;i<organList.size();i++)
        {
            int jcdw_id=organList.get(i).getJR_DEPTID();
            String jcdw_str_id=Integer.toString(jcdw_id);
            if(jcdw_str_id.equals(organID))
            {
                dwmc=organList.get(i).getJR_DWMC();
                break;
            }
        }
        String ruleLv1=item.getRuleLv1();
        String ruleLv2=item.getRuleLv2();
        String ruleLv3=item.getRuleLv3();
        String ruleContent=item.getRuleContent();

        String description=item.getDescription();
        String contact=item.getContact();
        String phone=item.getPhone();
        ArrayList<String> imagePathList=item.getImagePathList();
        String created=item.getCreated();
        if(item.hasImages())
        {
            viewHolder.imgContainer.setVisibility(View.VISIBLE);
            viewHolder.imgPreview.setImageBitmap(null);
            if(imagePathList.size()>0) {
                String previewImageName = imagePathList.get(0);
                viewHolder.imgPreview.setImageBitmap(getBitmapImageFromFilePath(previewImageName));
            }
        }
        else
        {
            viewHolder.imgContainer.setVisibility(View.GONE);
        }
        viewHolder.txtOrganName.setText(dwmc);
        viewHolder.txtCreated.setText(created);
        viewHolder.txtRuleLv1.setText(ruleLv1);
        viewHolder.txtRuleLv2.setText(ruleLv2);
        viewHolder.txtRuleLv3.setText(ruleLv3);
        viewHolder.txtRuleContent.setText(ruleContent);
        viewHolder.txtDescription.setText(description);
        viewHolder.txtContact.setText(contact);
        viewHolder.txtPhone.setText(phone);

        return convertView;
    }

    private Bitmap getBitmapImageFromFilePath(String fileName)
    {
        Bitmap bitmap=null;
        File file=ApplicationController.getFile("records/preview",fileName);
        bitmap= BitmapFactory.decodeFile(file.getAbsolutePath());
        return bitmap;
    }
    static class ViewHolder{
        TextView txtOrganName;
        TextView txtCreated;
        TextView txtRuleLv1,txtRuleLv2,txtRuleLv3,txtRuleContent;
        LinearLayout imgContainer;
        ImageView imgPreview;
        TextView txtDescription,txtContact,txtPhone;
    }
}
