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
public class JCNRInfo implements Serializable {
    public int JR_ID;
    public int EF_ID;
    public String EF_MC;
    public int YF_ID;
    public String YF_MC;
    public String JCK_FL;
    public String JCK_NR;
    public JCNRInfo(int jr_id,int ef_id,String ef_mc,int yf_id,String yf_mc,String jck_fl,String jck_nr)
    {
        JR_ID=jr_id;
        EF_ID=ef_id;
        EF_MC=ef_mc;
        YF_ID=yf_id;
        YF_MC=yf_mc;
        JCK_FL=jck_fl;
        JCK_NR=jck_nr;
    }
    public int getJR_ID()
    {
        return JR_ID;
    }
    public int getEF_ID()
    {
        return EF_ID;
    }
    public String getEF_MC()
    {
        return EF_MC;
    }
    public String getYF_MC()
    {
        return YF_MC;
    }
    public int getYF_ID()
    {
        return YF_ID;
    }
    public String getJCK_FL()
    {
        return JCK_FL;
    }
    public String getJCK_NR()
    {
        return JCK_NR;
    }
    public static ArrayList<JCNRInfo> getJCNRFromDB(int jr_id)
    {
        SQLiteDatabase db= ApplicationController.getSqLiteDatabase();
        String orderBy="";
        String selection="JR_ID=?";
        String[] selectionArgs=new String[]{Integer.toString(jr_id)};
        Cursor cursor = db.query(HSESQLiteHelper.TABLE_JCNR,null,selection,selectionArgs,null,null,null);
        ArrayList<JCNRInfo> nrList=new ArrayList<JCNRInfo>();
        /* 使用游标---获取游标中的数据 */
        while(cursor.moveToNext()){
            String ef_mc=cursor.getString(cursor.getColumnIndex("EF_MC"));
            String yf_mc=cursor.getString(cursor.getColumnIndex("YF_MC"));
            int ef_id=cursor.getInt(cursor.getColumnIndex("EF_ID"));
            int yf_id=cursor.getInt(cursor.getColumnIndex("YF_ID"));
            String jck_fl=cursor.getString(cursor.getColumnIndex("JCK_FL"));
            String jck_nr=cursor.getString(cursor.getColumnIndex("JCK_NR"));

            AppLog.i("检查内容 in DB: jr_id=" + jr_id + " ef_id=" + ef_id + " ef_mc=" + ef_mc + " yf_id=" + yf_id + " yf_mc=" + yf_mc+" jck_fl="+jck_fl+" jck_nr="+jck_nr);
            JCNRInfo jcnr=new JCNRInfo( jr_id, ef_id, ef_mc, yf_id, yf_mc, jck_fl, jck_nr);
            nrList.add(jcnr);
        }
        cursor.close();
        return nrList;
    }
    public static void clearJCNRInDB(int jr_id)
    {
        SQLiteDatabase db= ApplicationController.getSqLiteDatabase();
        String whereClause = "JR_ID=?";
        String[] whereArgs = new String[]{Integer.toString(jr_id)};
        db.delete(HSESQLiteHelper.TABLE_JCNR,whereClause,whereArgs);
    }
    public  void updateJCNRInDB()
    {
        Boolean isExist=false;
        SQLiteDatabase db = ApplicationController.getSqLiteDatabase();
        ContentValues values = new ContentValues();
        values.put("JR_ID", this.JR_ID);
        values.put("EF_ID", this.EF_ID);
        values.put("YF_ID", this.YF_ID);
        values.put("EF_MC", this.EF_MC);
        values.put("YF_MC", this.YF_MC);
        values.put("JCK_FL", this.JCK_FL);
        values.put("JCK_NR", this.JCK_NR);
        long result=db.insert(HSESQLiteHelper.TABLE_JCNR, null, values);
        AppLog.i("insert TABLE_JCNR result:" + Long.toString(result));
    }
    public static void updateCheckRulesInDB(ArrayList<JCNRInfo> inputList)
    {

        SQLiteDatabase db = ApplicationController.getSqLiteDatabase();
        for(int i=0;i<inputList.size();i++) {
            JCNRInfo item=inputList.get(i);
            ContentValues values = new ContentValues();
            values.put("task_id", item.JR_ID);
            values.put("rule_lv2", item.EF_MC);
            values.put("rule_lv1", item.YF_MC);
            values.put("rule_lv3", item.JCK_FL);
            values.put("rule_content", item.JCK_NR);
            long result = db.insert(HSESQLiteHelper.TABLE_CHECK_RULES, null, values);
            //AppLog.i("insert TABLE_JCNR result:" + Long.toString(result));
        }
    }
    public static void clearCheckRulesInDB(int jr_id)
    {
        SQLiteDatabase db= ApplicationController.getSqLiteDatabase();
        String whereClause = "task_id=?";
        String[] whereArgs = new String[]{Integer.toString(jr_id)};
        db.delete(HSESQLiteHelper.TABLE_CHECK_RULES,whereClause,whereArgs);
    }
}
