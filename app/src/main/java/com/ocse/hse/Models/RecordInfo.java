package com.ocse.hse.Models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ocse.hse.app.AppLog;
import com.ocse.hse.app.ApplicationController;
import com.ocse.hse.app.Utilities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by leehaining on 14-6-5.
 */
public class RecordInfo implements Serializable {
    private int recordID;
    private String taskID;
    private String organID;
    private String rule_lv1,rule_lv2,rule_lv3,rule_content;
    private String description;
    private ArrayList<String> imagePathList;
    private String imagePathString;
    private String contact;
    private String phone;
    private String created;
    private String updated;
    public RecordInfo(String taskID,String organID,String description,ArrayList<String> imagePathList,String contact,String phone,String created,String updated)
    {
        this.recordID=0;
        this.taskID=taskID;
        this.organID=organID;
        this.description=description;
        this.imagePathString="";
        this.imagePathList=new ArrayList<String>();
        Iterator<String> it = imagePathList.iterator();
        while(it.hasNext())
        {
            String item = it.next();
            this.imagePathList.add(item);
            this.imagePathString=this.imagePathString+item+";";
        }
        this.contact=contact;
        this.phone=phone;
        if(created!=null)
        {
            this.created=created;
        }
        else
        {
            this.created= Utilities.getCurrentTime();
        }
        if(updated!=null)
        {
            this.updated=updated;
        }
        else
        {
            this.updated=Utilities.getCurrentTime();
        }
    }
    public RecordInfo(CheckItemInfo ruleItem,String description,ArrayList<String> imagePathList,String contact,String phone,String created,String updated)
    {
        this.recordID=0;
        this.taskID=ruleItem.getTaskID();
        this.organID= ruleItem.getOrganID();
        this.rule_lv1=ruleItem.getRuleLv1();
        this.rule_lv2=ruleItem.getRuleLv2();
        this.rule_lv3=ruleItem.getRuleLv3();
        this.rule_content=ruleItem.getRuleContent();
        this.description=description;
        this.imagePathString="";
        this.imagePathList=new ArrayList<String>();
        Iterator<String> it = imagePathList.iterator();
        while(it.hasNext())
        {
            String item = it.next();
            this.imagePathList.add(item);
            this.imagePathString=this.imagePathString+item+";";
        }
        this.contact=contact;
        this.phone=phone;
        if(created!=null)
        {
            this.created=created;
        }
        else
        {
            this.created= Utilities.getCurrentTime();
        }
        if(updated!=null)
        {
            this.updated=updated;
        }
        else
        {
            this.updated=Utilities.getCurrentTime();
        }
    }
    public RecordInfo(String organID,CheckItemInfo ruleItem,String description,ArrayList<String> imagePathList,String contact,String phone,String created,String updated)
    {
        this.recordID=0;
        this.taskID=ruleItem.getTaskID();
        this.organID= organID;
        this.rule_lv1=ruleItem.getRuleLv1();
        this.rule_lv2=ruleItem.getRuleLv2();
        this.rule_lv3=ruleItem.getRuleLv3();
        this.rule_content=ruleItem.getRuleContent();
        this.description=description;
        this.imagePathString="";
        this.imagePathList=new ArrayList<String>();
        Iterator<String> it = imagePathList.iterator();
        while(it.hasNext())
        {
            String item = it.next();
            this.imagePathList.add(item);
            this.imagePathString=this.imagePathString+item+";";
        }
        this.contact=contact;
        this.phone=phone;
        if(created!=null)
        {
            this.created=created;
        }
        else
        {
            this.created= Utilities.getCurrentTime();
        }
        if(updated!=null)
        {
            this.updated=updated;
        }
        else
        {
            this.updated=Utilities.getCurrentTime();
        }
    }
    public RecordInfo(int recordID,String taskID,String organID,String description,ArrayList<String> imagePathList,String contact,String phone,String created,String updated)
    {
        this.recordID=recordID;
        this.taskID=taskID;
        this.organID=organID;
        this.description=description;
        this.imagePathString="";
        this.imagePathList=new ArrayList<String>();
        Iterator<String> it = imagePathList.iterator();
        while(it.hasNext())
        {
            String item = it.next();
            this.imagePathList.add(item);
            this.imagePathString=this.imagePathString+item+";";
        }
        this.contact=contact;
        this.phone=phone;
        if(created!=null)
        {
            this.created=created;
        }
        else
        {
            this.created= Utilities.getCurrentTime();
        }
        if(updated!=null)
        {
            this.updated=updated;
        }
        else
        {
            this.updated=Utilities.getCurrentTime();
        }
    }
    public RecordInfo(int recordID,String taskID,String organID,String rule_lv1,String rule_lv2,String rule_lv3,String rule_content,String description,ArrayList<String> imagePathList,String contact,String phone,String created,String updated)
    {
        this.recordID=recordID;
        this.taskID=taskID;
        this.organID=organID;

        this.rule_lv1=rule_lv1;
        this.rule_lv2=rule_lv2;
        this.rule_lv3=rule_lv3;
        this.rule_content=rule_content;

        this.description=description;
        this.imagePathString="";
        this.imagePathList=new ArrayList<String>();
        Iterator<String> it = imagePathList.iterator();
        while(it.hasNext())
        {
            String item = it.next();
            this.imagePathList.add(item);
            this.imagePathString=this.imagePathString+item+";";
        }
        this.contact=contact;
        this.phone=phone;
        if(created!=null)
        {
            this.created=created;
        }
        else
        {
            this.created= Utilities.getCurrentTime();
        }
        if(updated!=null)
        {
            this.updated=updated;
        }
        else
        {
            this.updated=Utilities.getCurrentTime();
        }
    }
    public int getIntRecordID()
    {
        return this.recordID;
    }
    public String getRecordID()
    {
        String strRecordID=Integer.toString(this.recordID);
        return strRecordID;
    }
    public String getTaskID(){
        return this.taskID;
    }
    public String getOrganID(){
        return this.organID;
    }
    public String getRuleLv1()
    {
        return this.rule_lv1;
    }
    public String getRuleLv2()
    {
        return this.rule_lv2;
    }
    public String getRuleLv3()
    {
        return this.rule_lv3;
    }
    public String getRuleContent()
    {
        return this.rule_content;
    }
    public String getDescription()
    {
        return this.description;
    }
    public String getContact()
    {
        if(this.contact.length()<1)
        {
            return "无";
        }
        else
        {
            return this.contact;
        }
    }
    public String getPhone()
    {
        if(this.phone.length()<1)
        {
            return "无";
        }
        else
        {
            return this.phone;
        }
    }
    public ArrayList<String> getImagePathList()
    {
        return this.imagePathList;
    }
    public String getCreated()
    {
        return this.created;
    }

    public Boolean hasImages()
    {
        Boolean isHasImages=false;
        if(this.imagePathList.size()>0)
            isHasImages=true;
        return isHasImages;

    }
    public void saveToCheckRecordDB()
    {
        SQLiteDatabase db= ApplicationController.getSqLiteDatabase();
        ContentValues values = new ContentValues();
        //values.put("task_id", "-1");
        values.put("task_id", this.taskID);
        values.put("organ_id", this.organID);
        values.put("rule_lv1", this.rule_lv1);
        values.put("rule_lv2", this.rule_lv2);
        values.put("rule_lv3", this.rule_lv3);
        values.put("rule_content", this.rule_content);

        values.put("description", this.description);
        values.put("images",this.imagePathString);
        values.put("contact", this.contact);
        values.put("phone", this.phone);
        values.put("created", this.created);
        values.put("updated", this.updated);
        long result=db.insert(HSESQLiteHelper.TABLE_RECORDS, null, values);
        AppLog.d("insert into RecordDB result:"+Long.toString(result));
        saveContact("0",contact,phone);
    }
    public void saveToDB()
    {
        /**
         *"task_id TEXT,"+
         "organ_id TEXT,"+
         "description TEXT,"+
         "images TEXT,"+
         "contact TEXT,"+
         "phone TEXT,"+
         "created TEXT,"+
         "updated TEXT"+
         ")";
         */

        SQLiteDatabase db= ApplicationController.getSqLiteDatabase();
        ContentValues values = new ContentValues();
        values.put("task_id", this.taskID);
        values.put("organ_id", this.organID);
        values.put("description", this.description);
        values.put("images",this.imagePathString);
        values.put("contact", this.contact);
        values.put("phone", this.phone);
        values.put("created", this.created);
        values.put("updated", this.updated);
        long result=db.insert(HSESQLiteHelper.TABLE_RECORDS, null, values);
        saveContact("0",contact,phone);
        AppLog.i("insert TABLE_RECORDS result:" + Long.toString(result));
        getAllRecordsInDB();
    }
    private void saveContact(String organID,String contact,String phone)
    {
        /**
         * "organ_id TEXT,"+
         "contact TEXT,"+
         "phone TEXT,"+
         "created TEXT"+
         ")";
         */
        organID=ApplicationController.getCurrentTaskID();
        contact=contact.trim();
        phone=phone.trim();
        if(contact.equals(""))
            return;
        int _id=0;
        Boolean isExist=false;
        String old_contact="";
        String old_phone="";
        SQLiteDatabase db = ApplicationController.getSqLiteDatabase();
        String selection="organ_id=? AND contact=?";
        String[] selectionArgs=new String[]{organID,contact};
        Cursor cursor = db.query(HSESQLiteHelper.TABLE_CONTACTS,null,selection,selectionArgs,null,null,null);
        while(cursor.moveToNext()){
            _id=cursor.getInt(cursor.getColumnIndex("_id"));
            old_phone=cursor.getString(cursor.getColumnIndex("phone"));
            isExist=true;
            break;
        }
        cursor.close();
        if(!isExist)//插入值
        {
            ContentValues values = new ContentValues();
            values.put("organ_id", organID);
            values.put("contact", this.contact);
            values.put("phone", this.phone);
            values.put("created", this.created);
            long result=db.insert(HSESQLiteHelper.TABLE_CONTACTS, null, values);
            AppLog.i("insert TABLE_CONTACTS result:" + Long.toString(result));
        }
        else
        {
            if(phone.length()>5)
            {
                if(!old_phone.equals(phone)) {
                    ContentValues values = new ContentValues();
                    values.put("phone", this.phone);
                    String whereClause = "_id=?";
                    String[] whereArgs = new String[]{Integer.toString(_id)};
                    long result = db.update(HSESQLiteHelper.TABLE_CONTACTS, values, whereClause, whereArgs);
                    AppLog.i("update TABLE_CONTACTS result:" + Long.toString(result));
                }
                else
                {
                    AppLog.i("Same Phone and Contact, No Need to Update");
                }
            }
        }
    }
    private ArrayList<RecordInfo> getAllRecordsInDB()
    {
        SQLiteDatabase db= ApplicationController.getSqLiteDatabase();
        Cursor cursor = db.query(HSESQLiteHelper.TABLE_RECORDS,null,null,null,null,null,null);
        ArrayList<RecordInfo> recordList=new ArrayList<RecordInfo>();
        /* 使用游标---获取游标中的数据 */
        while(cursor.moveToNext()){
            String taskID=cursor.getString(cursor.getColumnIndex("task_id"));
            String organID=cursor.getString(cursor.getColumnIndex("organ_id"));
            String description=cursor.getString(cursor.getColumnIndex("description"));
            String images=cursor.getString(cursor.getColumnIndex("images"));
            String contact=cursor.getString(cursor.getColumnIndex("contact"));
            String phone=cursor.getString(cursor.getColumnIndex("phone"));
            String created=cursor.getString(cursor.getColumnIndex("created"));
            String updated=cursor.getString(cursor.getColumnIndex("updated"));
            AppLog.i("Record in DB: taskID="+taskID+" organID="+organID+" description="+description+" images="+images+" contact="+contact+" phone="+phone+" created="+created+" updated="+updated);
            String[] imageArray=images.split(";");
            int total=imageArray.length;
            ArrayList<String> imagePathList=new ArrayList<String>();
            for(int i=0;i<total;i++)
            {
                String path=imageArray[i];
                if(path.length()>2)
                {
                    imagePathList.add(path);
                }
            }
            RecordInfo item=new RecordInfo( taskID, organID, description,imagePathList, contact, phone, created, updated);
            recordList.add(item);
        }
        cursor.close();
        return recordList;
    }
    public static void removeRecord(String recordID,String taskID,String organID)
    {
        SQLiteDatabase db= ApplicationController.getSqLiteDatabase();
        String whereClause = "_id=? AND task_id=? AND organ_id=?";
        String[] whereArgs = new String[]{recordID,taskID,organID};
        db.delete(HSESQLiteHelper.TABLE_RECORDS,whereClause,whereArgs);
    }
    public static ArrayList<RecordInfo> getRecordsFromDBByOrganID(String taskID,String organID)
    {
        SQLiteDatabase db= ApplicationController.getSqLiteDatabase();
        String selection="task_id=?";
        String[] selectionArgs=new String[]{taskID};
        if(organID==null)
        {
            selection=null;
            selectionArgs=null;
        }
        String orderBy="_id DESC";
        Cursor cursor = db.query(HSESQLiteHelper.TABLE_RECORDS,null,selection,selectionArgs,null,null,orderBy);
        ArrayList<RecordInfo> recordList=new ArrayList<RecordInfo>();
        /* 使用游标---获取游标中的数据 */
        while(cursor.moveToNext()){
            int recordID=cursor.getInt(cursor.getColumnIndex("_id"));
            organID=cursor.getString(cursor.getColumnIndex("organ_id"));
            String rule_lv1=cursor.getString(cursor.getColumnIndex("rule_lv1"));
            String rule_lv2=cursor.getString(cursor.getColumnIndex("rule_lv2"));
            String rule_lv3=cursor.getString(cursor.getColumnIndex("rule_lv3"));
            String rule_content=cursor.getString(cursor.getColumnIndex("rule_content"));

            String description=cursor.getString(cursor.getColumnIndex("description"));
            String images=cursor.getString(cursor.getColumnIndex("images"));
            String contact=cursor.getString(cursor.getColumnIndex("contact"));
            String phone=cursor.getString(cursor.getColumnIndex("phone"));
            String created=cursor.getString(cursor.getColumnIndex("created"));
            String updated=cursor.getString(cursor.getColumnIndex("updated"));
            AppLog.i("getRecordsFromDBByOrganID: taskID="+taskID+" organID="+organID+" description="+description+" images="+images+" contact="+contact+" phone="+phone+" created="+created+" updated="+updated);
            String[] imageArray=images.split(";");
            int total=imageArray.length;
            ArrayList<String> imagePathList=new ArrayList<String>();
            for(int i=0;i<total;i++)
            {
                String path=imageArray[i];
                if(path.length()>2)
                {
                    imagePathList.add(path);
                }
            }
            RecordInfo item=new RecordInfo( recordID,taskID, organID, rule_lv1,rule_lv2,rule_lv3,rule_content,description,imagePathList, contact, phone, created, updated);
            recordList.add(item);
        }
        cursor.close();
        return recordList;
    }
    public static ArrayList<RecordInfo> getRecordsFromDBByRule(CheckItemInfo ruleItem)
    {
        String taskID=ruleItem.getTaskID();
        String organID=ruleItem.getOrganID();
        SQLiteDatabase db= ApplicationController.getSqLiteDatabase();

        String rule_lv1=ruleItem.getRuleLv1();
        String rule_lv2=ruleItem.getRuleLv2();
        String rule_lv3=ruleItem.getRuleLv3();
        String rule_content=ruleItem.getRuleContent();

        String selection="task_id=? AND rule_lv1=? AND rule_lv2=? AND rule_lv3=? AND rule_content=?";
        String[] selectionArgs=new String[]{taskID,rule_lv1,rule_lv2,rule_lv3,rule_content};
        /**
        if(organID==null)
        {
            selection=null;
            selectionArgs=null;
        }
         **/
        String orderBy="_id DESC";
        Cursor cursor = db.query(HSESQLiteHelper.TABLE_RECORDS,null,selection,selectionArgs,null,null,orderBy);
        ArrayList<RecordInfo> recordList=new ArrayList<RecordInfo>();
        /* 使用游标---获取游标中的数据 */
        while(cursor.moveToNext()){
            int recordID=cursor.getInt(cursor.getColumnIndex("_id"));
            organID=cursor.getString(cursor.getColumnIndex("organ_id"));
            String description=cursor.getString(cursor.getColumnIndex("description"));
            String images=cursor.getString(cursor.getColumnIndex("images"));
            String contact=cursor.getString(cursor.getColumnIndex("contact"));
            String phone=cursor.getString(cursor.getColumnIndex("phone"));
            String created=cursor.getString(cursor.getColumnIndex("created"));
            String updated=cursor.getString(cursor.getColumnIndex("updated"));
            AppLog.i("getRecordsFromDBByOrganID: taskID="+taskID+" organID="+organID+" description="+description+" images="+images+" contact="+contact+" phone="+phone+" created="+created+" updated="+updated);
            String[] imageArray=images.split(";");
            int total=imageArray.length;
            ArrayList<String> imagePathList=new ArrayList<String>();
            for(int i=0;i<total;i++)
            {
                String path=imageArray[i];
                if(path.length()>2)
                {
                    imagePathList.add(path);
                }
            }
            RecordInfo item=new RecordInfo( recordID,taskID, organID, rule_lv1,rule_lv2,rule_lv3,rule_content,description,imagePathList, contact, phone, created, updated);
            recordList.add(item);
        }
        cursor.close();
        return recordList;
    }

    public void updateToDB()
    {
        SQLiteDatabase db = ApplicationController.getSqLiteDatabase();
        ContentValues values = new ContentValues();
        values.put("description", this.description);
        values.put("images",this.imagePathString);
        values.put("contact", this.contact);
        values.put("phone", this.phone);
        values.put("updated", this.updated);
        String whereClause = "_id=?";
        String[] whereArgs = new String[]{this.getRecordID()};
        long result = db.update(HSESQLiteHelper.TABLE_RECORDS, values, whereClause, whereArgs);
        AppLog.i("update TABLE_RECORDS result:" + Long.toString(result));
        saveContact("0",contact,phone);
    }

    public static void syncRuleStatus(String taskID)
    {
        SQLiteDatabase db= ApplicationController.getSqLiteDatabase();
        String selection="task_id=?";
        String[] selectionArgs=new String[]{taskID};

        String orderBy="_id DESC";
        Cursor cursor = db.query(HSESQLiteHelper.TABLE_RECORDS,null,selection,selectionArgs,null,null,orderBy);
        ArrayList<RecordInfo> recordList=new ArrayList<RecordInfo>();
        /* 使用游标---获取游标中的数据 */
        while(cursor.moveToNext()){
            int recordID=cursor.getInt(cursor.getColumnIndex("_id"));
            String rule_lv1=cursor.getString(cursor.getColumnIndex("rule_lv1"));
            String rule_lv2=cursor.getString(cursor.getColumnIndex("rule_lv2"));
            String rule_lv3=cursor.getString(cursor.getColumnIndex("rule_lv3"));
            String rule_content=cursor.getString(cursor.getColumnIndex("rule_content"));

            String description=cursor.getString(cursor.getColumnIndex("description"));
            String images=cursor.getString(cursor.getColumnIndex("images"));
            String contact=cursor.getString(cursor.getColumnIndex("contact"));
            String phone=cursor.getString(cursor.getColumnIndex("phone"));
            String created=cursor.getString(cursor.getColumnIndex("created"));
            String updated=cursor.getString(cursor.getColumnIndex("updated"));
            AppLog.i("getRecordsFromDBByOrganID: taskID="+taskID+" description="+description+" images="+images+" contact="+contact+" phone="+phone+" created="+created+" updated="+updated);
            String[] imageArray=images.split(";");
            int total=imageArray.length;
            ArrayList<String> imagePathList=new ArrayList<String>();
            for(int i=0;i<total;i++)
            {
                String path=imageArray[i];
                if(path.length()>2)
                {
                    imagePathList.add(path);
                }
            }
            RecordInfo item=new RecordInfo( recordID,taskID, "", rule_lv1,rule_lv2,rule_lv3,rule_content,description,imagePathList, contact, phone, created, updated);
            recordList.add(item);
        }
        cursor.close();

        int total=recordList.size();
        for(int i=0;i<total;i++)
        {
            RecordInfo item=recordList.get(i);
            String task_id=item.getTaskID();
            String organ_id=item.getOrganID();
            String rule_lv1=item.getRuleLv1();
            String rule_lv2=item.getRuleLv2();
            String rule_lv3=item.getRuleLv3();
            String rule_content=item.getRuleContent();
            String rule_status="no";
            CheckRecordInfo.updateCheckRuleStatus(task_id, organ_id, rule_lv1, rule_lv2, rule_lv3, rule_content, rule_status);
        }
    }
}
