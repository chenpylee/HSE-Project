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
public class JCFLInfo implements Serializable {
    public int JR_ID;
    public int EF_ID;
    public String EF_MC;
    public int YF_ID;
    public String YF_MC;
    public JCFLInfo(int jr_id,int ef_id,String ef_mc,int yf_id,String yf_mc)
    {
        this.JR_ID=jr_id;
        this.EF_ID=ef_id;
        this.EF_MC=ef_mc;
        this.YF_ID=yf_id;
        this.YF_MC=yf_mc;
    }
    public int getJR_ID()
    {
        return  JR_ID;
    }
    public int getEF_ID()
    {
        return EF_ID;
    }
    public String getEF_MC()
    {
        return EF_MC;
    }
    public int getYF_ID()
    {
        return YF_ID;
    }
    public String getYF_MC()
    {
        return YF_MC;
    }
    public static void clearJCFLInDB(int jr_id)
    {
        SQLiteDatabase db= ApplicationController.getSqLiteDatabase();
        String whereClause = "JR_ID=?";
        String[] whereArgs = new String[]{Integer.toString(jr_id)};
        db.delete(HSESQLiteHelper.TABLE_JCFL,whereClause,whereArgs);
    }

    public static JCFLInfo getJCFLFromDB(int jr_id,int ef_id,int yf_id)
    {
        SQLiteDatabase db= ApplicationController.getSqLiteDatabase();
        String selection="JR_ID=? AND EF_ID=? AND YF_ID=?";
        String[] selectionArgs=new String[]{Integer.toString(jr_id),Integer.toString(ef_id),Integer.toString(yf_id)};
        Cursor cursor = db.query(HSESQLiteHelper.TABLE_JCFL,null,selection,selectionArgs,null,null,null);
        JCFLInfo jcflInfo=new JCFLInfo(0,0,"",0,"");
        /* 使用游标---获取游标中的数据 */
        while(cursor.moveToNext()){
            String ef_mc=cursor.getString(cursor.getColumnIndex("EF_MC"));
            String yf_mc=cursor.getString(cursor.getColumnIndex("YF_MC"));

            AppLog.i("检查分类 in DB: jr_id=" + jr_id + " ef_id=" + ef_id + " ef_mc=" + ef_mc+" yf_id="+yf_id+" yf_mc="+yf_mc);
            JCFLInfo fl=new JCFLInfo( jr_id, ef_id, ef_mc, yf_id, yf_mc);
            jcflInfo=fl;
        }
        cursor.close();
        return jcflInfo;
    }
    public static ArrayList<JCFLInfo> getAllJCFLInDB()
    {
        SQLiteDatabase db= ApplicationController.getSqLiteDatabase();
        String orderBy="EF_ID ASC";
        Cursor cursor = db.query(HSESQLiteHelper.TABLE_JCFL,null,null,null,null,null,orderBy);
        ArrayList<JCFLInfo> flList=new ArrayList<JCFLInfo>();
        /* 使用游标---获取游标中的数据 */
        while(cursor.moveToNext()){
            int jr_id =cursor.getInt(cursor.getColumnIndex("JR_ID"));
            int ef_id =cursor.getInt(cursor.getColumnIndex("EF_ID"));
            int yf_id =cursor.getInt(cursor.getColumnIndex("YF_ID"));
            String ef_mc=cursor.getString(cursor.getColumnIndex("EF_MC"));
            String yf_mc=cursor.getString(cursor.getColumnIndex("YF_MC"));

            AppLog.i("检查分类 in DB: jr_id=" + jr_id + " ef_id=" + ef_id + " ef_mc=" + ef_mc+" yf_id="+yf_id+" yf_mc="+yf_mc);
            JCFLInfo dw=new JCFLInfo( jr_id, ef_id, ef_mc, yf_id, yf_mc);
            flList.add(dw);
        }
        cursor.close();
        return flList;
    }
    public void updateJCFLInDB()
    {
        Boolean isExist=false;
        SQLiteDatabase db = ApplicationController.getSqLiteDatabase();
        ContentValues values = new ContentValues();
        values.put("JR_ID", this.JR_ID);
        values.put("EF_ID", this.EF_ID);
        values.put("YF_ID", this.YF_ID);
        values.put("EF_MC", this.EF_MC);
        values.put("YF_MC", this.YF_MC);
        long result=db.insert(HSESQLiteHelper.TABLE_JCFL, null, values);
        AppLog.i("insert TABLE_JCFL result:" + Long.toString(result));
    }
}
