package com.ocse.hse.Models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ocse.hse.app.AppLog;
import com.ocse.hse.app.ApplicationController;
import com.ocse.hse.app.Utilities;

/**
 * Created by leehaining on 14-5-26.
 */
public class TagBasicInfo {
    private String tagID;
    private String tagTime;
    public TagBasicInfo(String tagID,String tagTime)
    {
        this.tagID=tagID;
        this.tagTime=tagTime;
    }
    public TagBasicInfo(String tagID)
    {
        this.tagID=tagID;
        this.tagTime= Utilities.getCurrentTime();
    }
    public String getTagID()
    {
        return this.tagID;
    }
    public String getTagTime()
    {
        return this.tagTime;
    }
    public void saveToDB()
    {
        SQLiteDatabase db= ApplicationController.getSqLiteDatabase();
        ContentValues values = new ContentValues();
        values.put("tag", this.getTagID());
        values.put("tag_time", this.getTagTime());
        long result=db.insert(HSESQLiteHelper.TABLE_NFC_CARDS, null, values);
        AppLog.i("insert result:"+Long.toString(result));
        printAllTagsInDB();
    }
    public static void printAllTagsInDB()
    {
        SQLiteDatabase db= ApplicationController.getSqLiteDatabase();
        Cursor cursor = db.query(HSESQLiteHelper.TABLE_NFC_CARDS,null,null,null,null,null,null);

        /* 使用游标---获取游标中的数据 */
        while(cursor.moveToNext()){
            AppLog.i("tagID:"+cursor.getString(cursor.getColumnIndex("tag"))+" readTime:"+cursor.getString(cursor.getColumnIndex("tag_time")));
        }
        cursor.close();
    }

}
