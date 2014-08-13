package com.ocse.hse.Models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ocse.hse.app.ApplicationController;

/**
 * Created by leehaining on 14-7-6.
 */
public class CheckRecordInfo {
    private String task_id,organ_id;
    private String rule_lv1,rule_lv2,rule_lv3,rule_content;
    private String rule_status;
    public static void updateCheckRuleStatus(String task_id,String organ_id,String rule_lv1,String rule_lv2,String rule_lv3,String rule_content,String rule_status)
    {
        boolean hasRecord=false;
        SQLiteDatabase db= ApplicationController.getSqLiteDatabase();
        String selection="task_id=? AND rule_lv1=? AND rule_lv2=? AND rule_lv3=? AND rule_content=?";
        String[] selectionArgs=new String[]{task_id,rule_lv1,rule_lv2,rule_lv3,rule_content};
        Cursor cursor = db.query(HSESQLiteHelper.TABLE_CHECK_RULES_STATUS,null,selection,selectionArgs,null,null,null);
        while(cursor.moveToNext()){
            hasRecord=true;
            break;
        }
        cursor.close();

        if(hasRecord)
        {
            //update table
            ContentValues values = new ContentValues();
            values.put("rule_status",rule_status);
            String whereClause = "task_id=? AND rule_lv1=? AND rule_lv2=? AND rule_lv3=? AND rule_content=?";
            String[] whereArgs = new String[]{task_id,rule_lv1,rule_lv2,rule_lv3,rule_content};
            long result = db.update(HSESQLiteHelper.TABLE_CHECK_RULES_STATUS, values, whereClause, whereArgs);
        }
        else
        {
            //insert
            ContentValues values = new ContentValues();
            values.put("task_id", task_id);
            values.put("organ_id", organ_id);
            values.put("rule_lv1", rule_lv1);
            values.put("rule_lv2", rule_lv2);
            values.put("rule_lv3", rule_lv3);
            values.put("rule_content", rule_content);
            values.put("rule_status",rule_status);
            long result=db.insert(HSESQLiteHelper.TABLE_CHECK_RULES_STATUS, null, values);
        }
    }
    public static boolean getHasRecords(String task_id,String organ_id,String rule_lv1,String rule_lv2,String rule_lv3,String rule_content)
    {
        boolean hasRecords=false;
        /**
         * CHECK_RULES_STATUS
         "task_id TEXT,"+
         "organ_id TEXT,"+
         "rule_lv1 TEXT,"+
         "rule_lv2 TEXT,"+
         "rule_lv3 TEXT,"+
         "rule_content TEXT,"+
         "rule_status TEXT
         */

        /**
         * "task_id TEXT,"+
         "organ_id TEXT,"+
         "rule_lv1 TEXT,"+
         "rule_lv2 TEXT,"+
         "rule_lv3 TEXT,"+
         "rule_content TEXT,"+
         "description TEXT,"+
         "images TEXT,"+
         "contact TEXT,"+
         "phone TEXT,"+
         "created TEXT,"+
         "updated TEXT"+
         ")";
         */
        SQLiteDatabase db= ApplicationController.getSqLiteDatabase();
        String selection="task_id=? AND organ_id=? AND rule_lv1=? AND rule_lv2=? AND rule_lv3=? AND rule_content=?";
        String[] selectionArgs=new String[]{task_id,organ_id,rule_lv1,rule_lv2,rule_lv3,rule_content};
        Cursor cursor = db.query(HSESQLiteHelper.TABLE_RECORDS,null,selection,selectionArgs,null,null,null);
        while(cursor.moveToNext()){
            hasRecords=true;
            break;
        }
        cursor.close();

        return hasRecords;
    }
    public  static void removeRuleRecords(CheckItemInfo ruleItem)
    {
        SQLiteDatabase db= ApplicationController.getSqLiteDatabase();
        String taskID=ruleItem.getTaskID();
        String organID=ruleItem.getOrganID();
        String rule_lv1=ruleItem.getRuleLv1();
        String rule_lv2=ruleItem.getRuleLv2();
        String rule_lv3=ruleItem.getRuleLv3();
        String rule_content=ruleItem.getRuleContent();
        String whereClause="task_id=? AND organ_id=? AND rule_lv1=? AND rule_lv2=? AND rule_lv3=? AND rule_content=?";
        String[] whereArgs=new String[]{taskID,organID,rule_lv1,rule_lv2,rule_lv3,rule_content};
        db.delete(HSESQLiteHelper.TABLE_RECORDS,whereClause,whereArgs);
    }
}
