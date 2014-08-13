package com.ocse.hse.Models;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ocse.hse.app.AppLog;
import com.ocse.hse.app.ApplicationController;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by leehaining on 14-7-6.
 */
public class CheckRulesInfo implements Serializable {
    private int rule_id=0;
    private String task_id="";
    private String organ_id="";
    private String rule_lv1="";
    private String rule_lv2="";//职业卫生等无rule_lv2
    private String rule_lv3="";
    private String rule_content="";
    private String rule_updated="";

    private String rule_status="null";

    public CheckRulesInfo(String organ_id,String rule_lv1,String rule_lv2,String rule_lv3,String rule_content)
    {
        /**
         "organ_id TEXT,"+
         "rule_lv1 TEXT,"+
         "rule_lv2 TEXT,"+
         "rule_lv3 TEXT,"+
         "rule_content TEXT,"+
         "rule_updated TEXT"+
         */
        this.organ_id=organ_id;
        this.rule_lv1=rule_lv1;
        this.rule_lv2=rule_lv2;
        this.rule_lv3=rule_lv3;
        this.rule_content=rule_content;
        this.rule_updated= ApplicationController.getCurrentTime();
    }
    public CheckRulesInfo(String task_id,String organ_id,String rule_lv1,String rule_lv2,String rule_lv3,String rule_content,String rule_status)
    {
        /**
         "organ_id TEXT,"+
         "rule_lv1 TEXT,"+
         "rule_lv2 TEXT,"+
         "rule_lv3 TEXT,"+
         "rule_content TEXT,"+
         "rule_updated TEXT"+
         */
        this.task_id=task_id;
        this.organ_id=organ_id;
        this.rule_lv1=rule_lv1;
        this.rule_lv2=rule_lv2;
        this.rule_lv3=rule_lv3;
        this.rule_content=rule_content;
        this.rule_status=rule_status;
    }
    public String getTaskID()
    {
        return this.task_id;
    }
    public String getOrganID()
    {
        return this.organ_id;
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
    public String getRuleUpdated()
    {
        return this.rule_updated;
    }
    public String getRuleStatus(){
        return this.rule_status;
    }
    public static void printCheckRules()
    {
        SQLiteDatabase db= ApplicationController.getSqLiteDatabase();

        String orderBy="_id ASC";
        Cursor cursor = db.query(HSESQLiteHelper.TABLE_CHECK_RULES,null,null,null,null,null,orderBy);
        while(cursor.moveToNext()){
            int rule_id=cursor.getInt(cursor.getColumnIndex("_id"));
            String task_id=cursor.getString(cursor.getColumnIndex("task_id"));
            String rule_lv1=cursor.getString(cursor.getColumnIndex("rule_lv1"));
            String rule_lv2=cursor.getString(cursor.getColumnIndex("rule_lv2"));
            String rule_lv3=cursor.getString(cursor.getColumnIndex("rule_lv3"));
            String rule_content=cursor.getString(cursor.getColumnIndex("rule_content"));
            String rule_updated=cursor.getString(cursor.getColumnIndex("rule_updated"));
            AppLog.i("task_id:"+task_id+" lv1:"+rule_lv1+" lv2:"+rule_lv2+" lv3:"+rule_lv3+" content:"+rule_content+" updated:"+rule_updated);

        }
        cursor.close();
    }
    public static ArrayList<String> getLv1List(String task_id)
    {
        ArrayList<String> lv1List=new ArrayList<String>();
        SQLiteDatabase db= ApplicationController.getSqLiteDatabase();
        String selection="task_id=?";
        String[] selectionArgs=new String[]{task_id};
        String orderBy="_id ASC";
        Cursor cursor = db.query(HSESQLiteHelper.TABLE_CHECK_RULES,null,selection,selectionArgs,null,null,orderBy);
        while(cursor.moveToNext()){
            String rule_lv1=cursor.getString(cursor.getColumnIndex("rule_lv1"));
            if(!lv1List.contains(rule_lv1))
            {
                lv1List.add(rule_lv1);
            }
        }
        cursor.close();
        return lv1List;
    }
    public static ArrayList<String> getLv2List(String task_id,String rule_lv1)
    {
        ArrayList<String> lv2List=new ArrayList<String>();
        SQLiteDatabase db= ApplicationController.getSqLiteDatabase();
        String selection="rule_lv1=? AND task_id=?";
        String[] selectionArgs=new String[]{rule_lv1,task_id};
        String orderBy="_id ASC";
        Cursor cursor = db.query(HSESQLiteHelper.TABLE_CHECK_RULES,null,selection,selectionArgs,null,null,orderBy);
        while(cursor.moveToNext()){
            String rule_lv2=cursor.getString(cursor.getColumnIndex("rule_lv2"));
            if(!lv2List.contains(rule_lv2))
            {
                if(rule_lv2.length()>0) {
                    lv2List.add(rule_lv2);
                }
            }
        }
        cursor.close();
        return lv2List;
    }
    public static ArrayList<String> getLv3List(String task_id,String rule_lv1,String rule_lv2)
    {
        ArrayList<String> lv3List=new ArrayList<String>();
        SQLiteDatabase db= ApplicationController.getSqLiteDatabase();
        String selection="rule_lv1=? AND rule_lv2=? AND task_id=?";
        String[] selectionArgs=new String[]{rule_lv1,rule_lv2,task_id};
        String orderBy="_id ASC";
        Cursor cursor = db.query(HSESQLiteHelper.TABLE_CHECK_RULES,null,selection,selectionArgs,null,null,orderBy);
        while(cursor.moveToNext()){
            String ruleLv3=cursor.getString(cursor.getColumnIndex("rule_lv3"));
            if(!lv3List.contains(ruleLv3))
            {
                if(ruleLv3.length()>0) {
                    lv3List.add(ruleLv3);
                }
            }
        }
        cursor.close();
        return lv3List;
    }
    public static ArrayList<CheckRulesInfo> getLv3List(String task_id,String organ_id,String rule_lv1,String rule_lv2,String rule_lv3)
    {
        ArrayList<CheckRulesInfo> lv3List=new ArrayList<CheckRulesInfo>();

        ArrayList<String> lv3ListYES=new ArrayList<String>();
        ArrayList<String> lv3ListNO=new ArrayList<String>();
        SQLiteDatabase db= ApplicationController.getSqLiteDatabase();
        String statusSelection="task_id=?";
        String[] statusSelectionArgs=new String[]{task_id};
        Cursor statusCursor = db.query(HSESQLiteHelper.TABLE_CHECK_RULES_STATUS,null,statusSelection,statusSelectionArgs,null,null,null);
        while(statusCursor.moveToNext())
        {
            String status=statusCursor.getString(statusCursor.getColumnIndex("rule_status"));
            String rule_content=statusCursor.getString(statusCursor.getColumnIndex("rule_content"));
            if(status.toLowerCase().equals("yes"))
            {
                lv3ListYES.add(rule_lv1+rule_lv2+rule_lv3+rule_content);
            }
            else if(status.toLowerCase().equals("no"))
            {
                lv3ListNO.add(rule_lv1+rule_lv2+rule_lv3+rule_content);
            }
        }
        statusCursor.close();

        String selection="rule_lv1=? AND rule_lv2=? AND rule_lv3=? AND task_id=?";
        String[] selectionArgs=new String[]{rule_lv1,rule_lv2,rule_lv3,task_id};
        String orderBy="_id ASC";
        Cursor cursor = db.query(HSESQLiteHelper.TABLE_CHECK_RULES,null,selection,selectionArgs,null,null,orderBy);
        while(cursor.moveToNext()){
            String rule_content=cursor.getString(cursor.getColumnIndex("rule_content"));
            String rule_updated=cursor.getString(cursor.getColumnIndex("rule_updated"));
            //get status
            String rule_status="null";
            if(lv3ListYES.contains(rule_lv1+rule_lv2+rule_lv3+rule_content))
            {
                rule_status="yes";
            }
            if(lv3ListNO.contains(rule_lv1+rule_lv2+rule_lv3+rule_content))
            {
                rule_status="no";
            }
            //
            lv3List.add(new CheckRulesInfo(task_id,organ_id,rule_lv1,rule_lv2,rule_lv3,rule_content,rule_status));
        }
        cursor.close();
        return lv3List;
    }


}
