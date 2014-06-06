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
public class RecordInfoAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<RecordInfo> dataArrayList;
    public RecordInfoAdapter(Context context,ArrayList<RecordInfo> inputList)
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
            convertView=mInflater.inflate(R.layout.row_record_list,null);
            viewHolder.txtCreated=(TextView)convertView.findViewById(R.id.txtCreated);
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
        viewHolder.txtCreated.setText(created);
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
        TextView txtCreated;
        LinearLayout imgContainer;
        ImageView imgPreview;
        TextView txtDescription,txtContact,txtPhone;
    }
}
