package com.ocse.hse.Models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ocse.hse.app.AppLog;
import com.ocse.hse.app.ApplicationController;
import com.ocse.hse.app.Utilities;

import java.util.ArrayList;

/**
 * Created by leehaining on 14-5-26.
 */
public class TagBasicInfo {
    private String taskID,organID,tagTime,cardJson;
    private String tagID;
    public TagBasicInfo(String taskID,String organID,String tagID,String cardJson)
    {
        this.tagID=tagID;
        this.taskID=taskID;
        this.organID=organID;
        this.cardJson=cardJson;
        this.tagTime=Utilities.getCurrentTime();
    }
    public TagBasicInfo(String taskID,String organID,String tagID,String cardJson,String tagTime)
    {
        this.tagID=tagID;
        this.taskID=taskID;
        this.organID=organID;
        this.cardJson=cardJson;
        this.tagTime=tagTime;
    }

    public String getTagID()
    {
        return this.tagID;
    }
    public String getOrganID()
    {
        return this.organID;
    }
    public String getTaskID(){
        return this.taskID;
    }
    public String getCardJson(){
        return this.cardJson;
    }
    public String getTagTime(){
        return this.tagTime;
    }

    public void saveToDB()
    {
        /**
         * "task_id TEXT,"+
         "organ_id TEXT,"+
         "tag_id TEXT,"+
         "card_info TEXT,"+
         "tag_time TEXT"+
         */
        SQLiteDatabase db= ApplicationController.getSqLiteDatabase();
        ContentValues values = new ContentValues();
        values.put("task_id", this.getTaskID());
        values.put("organ_id", this.getOrganID());
        values.put("tag_id", this.getTagID());
        values.put("card_info", this.getCardJson());
        values.put("tag_time", this.getTagTime());
        long result=db.insert(HSESQLiteHelper.TABLE_CARD_READ, null, values);
        AppLog.i("insert result:"+Long.toString(result));
        //printAllTagsInDB();
    }
    public static ArrayList<TagBasicInfo> printAllTagsInDB()
    {
        SQLiteDatabase db= ApplicationController.getSqLiteDatabase();
        Cursor cursor = db.query(HSESQLiteHelper.TABLE_CARD_READ,null,null,null,null,null,null);
        ArrayList<TagBasicInfo> tagList=new ArrayList<TagBasicInfo>();
        /* 使用游标---获取游标中的数据 */
        while(cursor.moveToNext()){
            AppLog.i("tagID:"+cursor.getString(cursor.getColumnIndex("task_id"))+" readTime:"+cursor.getString(cursor.getColumnIndex("tag_time")));
            String taskID=cursor.getString(cursor.getColumnIndex("task_id"));
            String organID=cursor.getString(cursor.getColumnIndex("organ_id"));
            String tagID=cursor.getString(cursor.getColumnIndex("tag_id"));
            String cardInfo=cursor.getString(cursor.getColumnIndex("card_info"));
            String tagTime=cursor.getString(cursor.getColumnIndex("tag_time"));
            TagBasicInfo tag=new TagBasicInfo(taskID,organID,tagID,cardInfo,tagTime);
            tagList.add(tag);
        }
        cursor.close();
        return tagList;
    }
    public static ArrayList<TagBasicInfo> printAllTagsInDBByTaskAndOrgan(String taskID,String organID)
    {
        SQLiteDatabase db= ApplicationController.getSqLiteDatabase();
        //query (String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy)
        String selection="task_id=? AND organ_id=?";
        String[] selectionArgs=new String[]{taskID,organID};
        String orderBy="_id DESC";
        Cursor cursor = db.query(HSESQLiteHelper.TABLE_CARD_READ,null,selection,selectionArgs,null,null,orderBy);
        ArrayList<TagBasicInfo> tagList=new ArrayList<TagBasicInfo>();
        /* 使用游标---获取游标中的数据 */
        while(cursor.moveToNext()){
            AppLog.i("tagID:"+cursor.getString(cursor.getColumnIndex("task_id"))+" readTime:"+cursor.getString(cursor.getColumnIndex("tag_time")));
            String task_id=cursor.getString(cursor.getColumnIndex("task_id"));
            String organ_id=cursor.getString(cursor.getColumnIndex("organ_id"));
            String tag_id=cursor.getString(cursor.getColumnIndex("tag_id"));
            String card_info=cursor.getString(cursor.getColumnIndex("card_info"));
            String tag_time=cursor.getString(cursor.getColumnIndex("tag_time"));
            TagBasicInfo tag=new TagBasicInfo(task_id,organ_id,tag_id,card_info,tag_time);
            tagList.add(tag);
        }
        cursor.close();
        return tagList;
    }

}
