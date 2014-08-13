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
public class JCDWInfo implements Serializable{
    public int JR_ID;
    public int JR_DEPTID;
    public String JR_DWMC;
    public JCDWInfo(int jr_id,int jr_deptid,String jr_dwmc)
    {
        this.JR_ID=jr_id;
        this.JR_DEPTID=jr_deptid;
        this.JR_DWMC=jr_dwmc;
    }
    public int getJR_ID()
    {
        return JR_ID;
    }
    public int getJR_DEPTID()
    {
        return JR_DEPTID;
    }
    public String getJR_DWMC()
    {
        return JR_DWMC;
    }
    public static ArrayList<JCDWInfo> getJCDWByJR(String str_task_id)
    {
        SQLiteDatabase db= ApplicationController.getSqLiteDatabase();
        String selection="JR_ID=?";
        String[] selectionArgs=new String[]{str_task_id};
        Cursor cursor = db.query(HSESQLiteHelper.TABLE_JCDW,null,selection,selectionArgs,null,null,null);
        ArrayList<JCDWInfo> dwList=new ArrayList<JCDWInfo>();
        /* 使用游标---获取游标中的数据 */
        while(cursor.moveToNext()){
            int jr_id =cursor.getInt(cursor.getColumnIndex("JR_ID"));
            int jr_deptid =cursor.getInt(cursor.getColumnIndex("JR_DEPTID"));
            String jr_dwmc=cursor.getString(cursor.getColumnIndex("JR_DWMC"));

            AppLog.i("检查单位 in DB: jr_id=" + jr_id + " jr_deptid=" + jr_deptid + " jr_dwmc=" + jr_dwmc);
            JCDWInfo dw=new JCDWInfo(jr_id,jr_deptid,jr_dwmc);
            dwList.add(dw);
        }
        cursor.close();
        return dwList;
    }
    public static ArrayList<JCDWInfo> getAllJcdwInDB()
    {
        SQLiteDatabase db= ApplicationController.getSqLiteDatabase();
        String orderBy="JR_DEPTID DESC";
        Cursor cursor = db.query(HSESQLiteHelper.TABLE_JCDW,null,null,null,null,null,orderBy);
        ArrayList<JCDWInfo> dwList=new ArrayList<JCDWInfo>();
        /* 使用游标---获取游标中的数据 */
        while(cursor.moveToNext()){
            int jr_id =cursor.getInt(cursor.getColumnIndex("JR_ID"));
            int jr_deptid =cursor.getInt(cursor.getColumnIndex("JR_DEPTID"));
            String jr_dwmc=cursor.getString(cursor.getColumnIndex("JR_DWMC"));

            AppLog.i("检查单位 in DB: jr_id=" + jr_id + " jr_deptid=" + jr_deptid + " jr_dwmc=" + jr_dwmc);
            JCDWInfo dw=new JCDWInfo(jr_id,jr_deptid,jr_dwmc);
            dwList.add(dw);
        }
        cursor.close();
        return dwList;
    }
    public void updateJCDWInDB()
    {
        Boolean isExist=false;
        SQLiteDatabase db = ApplicationController.getSqLiteDatabase();
        ContentValues values = new ContentValues();
        values.put("JR_ID", this.JR_ID);
        values.put("JR_DEPTID", this.JR_DEPTID);
        values.put("JR_DWMC", this.JR_DWMC);
        long result=db.insert(HSESQLiteHelper.TABLE_JCDW, null, values);
        AppLog.i("insert TABLE_JCDW result:" + Long.toString(result));
    }
    public static void clearJCDWInDB(int jr_id)
    {
        SQLiteDatabase db= ApplicationController.getSqLiteDatabase();
         String whereClause = "JR_ID=?";
         String[] whereArgs = new String[]{Integer.toString(jr_id)};
        db.delete(HSESQLiteHelper.TABLE_JCDW,whereClause,whereArgs);
    }

}
