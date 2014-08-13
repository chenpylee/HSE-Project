package com.ocse.hse.Models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ocse.hse.app.AppLog;
import com.ocse.hse.app.ApplicationController;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by leehaining on 14-7-18.
 */
public class JcrwInfo implements Serializable {
    private int JR_ID;//检查任务ID
    private String JR_MC;//检查任务名称
    private String JR_KSSJ;//开始时间
    private String JR_JSSJ;//结束时间
    public JcrwInfo(int jr_id,String jr_mc,String jr_kssj,String jr_jssj)
    {
        this.JR_ID=jr_id;
        this.JR_MC=jr_mc;
        this.JR_KSSJ=jr_kssj;
        this.JR_JSSJ=jr_jssj;
    }
    public int getJR_ID()
    {
        return this.JR_ID;
    }
    public String getJR_MC()
    {
        return this.JR_MC;
    }
    public String getJR_KSSJ()
    {
        return this.JR_KSSJ;
    }
    public String getJR_JSSJ()
    {
        return this.JR_JSSJ;
    }
    public static void clearJcrwInDB()
    {
        SQLiteDatabase db= ApplicationController.getSqLiteDatabase();
        /**
        String whereClause = "_id=? AND task_id=? AND organ_id=?";
        String[] whereArgs = new String[]{recordID,taskID,organID};
         **/
        db.delete(HSESQLiteHelper.TABLE_JCRW,null,null);
    }
    public static ArrayList<JcrwInfo> getAllJcrwInDB()
    {
        SQLiteDatabase db= ApplicationController.getSqLiteDatabase();
        String orderBy="JR_ID DESC";
        Cursor cursor = db.query(HSESQLiteHelper.TABLE_JCRW,null,null,null,null,null,orderBy);
        ArrayList<JcrwInfo> taskList=new ArrayList<JcrwInfo>();
        /* 使用游标---获取游标中的数据 */
        while(cursor.moveToNext()){
            int jr_id =cursor.getInt(cursor.getColumnIndex("JR_ID"));
            String jr_mc=cursor.getString(cursor.getColumnIndex("JR_MC"));
            String jr_kssj=cursor.getString(cursor.getColumnIndex("JR_KSSJ"));
            String jr_jssj=cursor.getString(cursor.getColumnIndex("JR_JSSJ"));

            AppLog.i("检查任务 in DB: jr_id=" + jr_id + " jr_mc=" + jr_mc + " jr_kssj=" + jr_kssj + " jr_jssj=" + jr_jssj );
            JcrwInfo task=new JcrwInfo(jr_id,jr_mc,jr_kssj,jr_jssj);
            taskList.add(task);
        }
        cursor.close();
        return taskList;
    }
    public void updateJcrwInDB()
    {
        Boolean isExist=false;
        SQLiteDatabase db = ApplicationController.getSqLiteDatabase();
        String selection="JR_ID=?";
        String[] selectionArgs=new String[]{Integer.toString(this.JR_ID)};
        Cursor cursor = db.query(HSESQLiteHelper.TABLE_JCRW,null,selection,selectionArgs,null,null,null);
        while(cursor.moveToNext()){
            isExist=true;
            break;
        }
        cursor.close();

        if(isExist)
        {
            //update
            ContentValues values = new ContentValues();
            values.put("JR_MC", this.JR_MC);
            values.put("JR_KSSJ", this.JR_KSSJ);
            values.put("JR_JSSJ", this.JR_JSSJ);
            String whereClause = "JR_ID=?";
            String[] whereArgs = new String[]{Integer.toString(this.JR_ID)};
            long result = db.update(HSESQLiteHelper.TABLE_JCRW, values, whereClause, whereArgs);
            AppLog.i("update TABLE_JCRW result:" + Long.toString(result));
        }
        else
        {
            //insert
            ContentValues values = new ContentValues();
            values.put("JR_ID", this.JR_ID);
            values.put("JR_MC", this.JR_MC);
            values.put("JR_KSSJ", this.JR_KSSJ);
            values.put("JR_JSSJ", this.JR_JSSJ);
            long result=db.insert(HSESQLiteHelper.TABLE_JCRW, null, values);
            AppLog.i("insert TABLE_JCRW result:" + Long.toString(result));
        }
    }
}
