package com.ocse.hse.Models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ocse.hse.app.AppLog;
import com.ocse.hse.app.ApplicationController;

import java.util.ArrayList;

/**
 * Created by leehaining on 14-7-19.
 */
public class YWWDInfo {
    private int YWID;
    private String ZLLB;
    private String WDMC;
    private String WDLJ;
    public YWWDInfo(int ywid,String zllb,String wdmc,String wdlj)
    {
        this.YWID=ywid;
        this.ZLLB=zllb;
        this.WDMC=wdmc;
        this.WDLJ=wdlj;
    }
    public int getYWID()
    {
        return YWID;
    }
    public String getZLLB()
    {
        return ZLLB;
    }
    public String getWDMC()
    {
        return WDMC;
    }
    public String getWDLJ()
    {
        return WDLJ;
    }
    public static void clearYWWDInDB(int ywid)
    {
        SQLiteDatabase db= ApplicationController.getSqLiteDatabase();
        String whereClause = "YWID=?";
        String[] whereArgs = new String[]{Integer.toString(ywid)};
        db.delete(HSESQLiteHelper.TABLE_YWWD,whereClause,whereArgs);
    }
    public void updateJCDWInDB()
    {
        Boolean isExist=false;
        SQLiteDatabase db = ApplicationController.getSqLiteDatabase();
        ContentValues values = new ContentValues();
        values.put("YWID", this.YWID);
        values.put("ZLLB", this.ZLLB);
        values.put("WDMC", this.WDMC);
        values.put("WDLJ", this.WDLJ);
        long result=db.insert(HSESQLiteHelper.TABLE_YWWD, null, values);
        AppLog.i("insert TABLE_YWWD result:" + Long.toString(result));
    }
    public static ArrayList<YWWDInfo> getYWWDFromDB(int ywid,String zllb)
    {
        SQLiteDatabase db= ApplicationController.getSqLiteDatabase();
        String selection="YWID=? AND ZLLB=?";
        String[] selectionArgs=new String[]{Integer.toString(ywid),zllb};
        Cursor cursor = db.query(HSESQLiteHelper.TABLE_YWWD,null,selection,selectionArgs,null,null,null);
        ArrayList<YWWDInfo> wdList=new ArrayList<YWWDInfo>();
        /* 使用游标---获取游标中的数据 */
        while(cursor.moveToNext()){
            String WDMC =cursor.getString(cursor.getColumnIndex("WDMC"));
            String WDLJ =cursor.getString(cursor.getColumnIndex("WDLJ"));
            AppLog.i("业务文档 in DB: YWID=" + ywid + " ZLLB=" + zllb + " WDMC=" + WDMC+" WDLJ="+WDLJ);
            YWWDInfo wd=new YWWDInfo( ywid, zllb, WDMC, WDLJ);
            wdList.add(wd);
        }
        cursor.close();
        return wdList;
    }
    public static ArrayList<String> getZLLBFromDB(int ywid)
    {
        SQLiteDatabase db= ApplicationController.getSqLiteDatabase();
        String selection="YWID=?";
        String[] selectionArgs=new String[]{Integer.toString(ywid)};
        Cursor cursor = db.query(HSESQLiteHelper.TABLE_YWWD,null,selection,selectionArgs,null,null,null);
        ArrayList<String> zllbList=new ArrayList<String>();
        /* 使用游标---获取游标中的数据 */
        while(cursor.moveToNext()){
            String ZLLB =cursor.getString(cursor.getColumnIndex("ZLLB"));
            if(!zllbList.contains(ZLLB))
            {
                zllbList.add(ZLLB);
            }
        }
        cursor.close();
        return zllbList;
    }
    public static boolean isPic(String WDLJ)
    {
        boolean isIMG=false;
        if(WDLJ.toLowerCase().endsWith(".jpg"))
        {
            isIMG=true;
        }
        if(WDLJ.toLowerCase().endsWith(".jpeg"))
        {
            isIMG=true;
        }
        if(WDLJ.toLowerCase().endsWith(".png"))
        {
            isIMG=true;
        }
        if(WDLJ.toLowerCase().endsWith(".gif"))
        {
            isIMG=true;
        }
        return isIMG;
    }

}
