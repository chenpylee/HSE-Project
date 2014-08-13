package com.ocse.hse.Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ocse.hse.app.AppLog;

import java.util.ArrayList;

/**
 * Created by leehaining on 14-5-26.
 */
public class HSESQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="HSEDB";
    private static final int DATABASE_VERSION=1;
    public static final String TABLE_JCRW="JCRW";
    public static final String TABLE_JCDW="JCDW";
    public static final String TABLE_JCFL="JCFL";
    public static final String TABLE_JCNR="JCNR";
    public static final String TABLE_YWWD="YWWD";
    public static final String TABLE_NFC_CARDS="NFC_CARDS";
    public static final String TABLE_CARD_READ="CARD_READ";
    public static final String TABLE_RECORDS="CHECK_RECORDS";//隐患记录 task_id=self 时为自查自纠
    public static final String TABLE_CONTACTS="CHECK_CONTACTS";
    public static final String TABLE_CHECK_RULES="CHECK_RULES";
    public static final String TABLE_CHECK_RULES_STATUS="CHECK_RULES_STATUS";//检查条目+状态 task_id=self 时为自查自纠
    private static HSESQLiteHelper dbHelper;
    private HSESQLiteHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    public static SQLiteDatabase getInstance(Context context)
    {
        if(dbHelper==null)
        {
            dbHelper=new HSESQLiteHelper(context);
        }
        return dbHelper.getWritableDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        AppLog.i("onCreate SQLite DB");
        final String CREATE_TABLE_JCRW="CREATE TABLE "+TABLE_JCRW+" (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "JR_ID INTEGER, "+
                "JR_MC TEXT, "+
                "JR_KSSJ TEXT, "+
                "JR_JSSJ TEXT "+
                ")";
        db.execSQL(CREATE_TABLE_JCRW);

        final String CREATE_TABLE_JCDW="CREATE TABLE "+TABLE_JCDW+" (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "JR_ID INTEGER, "+
                "JR_DEPTID INTEGER, "+
                "JR_DWMC TEXT"+
                ")";
        db.execSQL(CREATE_TABLE_JCDW);

        final String CREATE_TABLE_JCFL="CREATE TABLE "+TABLE_JCFL+" (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "JR_ID INTEGER, "+
                "EF_ID INTEGER, "+
                "EF_MC TEXT,"+
                "YF_ID INTEGER, "+
                "YF_MC TEXT"+
                ")";
        db.execSQL(CREATE_TABLE_JCFL);

        final String CREATE_TABLE_JCNR="CREATE TABLE "+TABLE_JCNR+" (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "JR_ID INTEGER, "+
                "EF_ID INTEGER, "+
                "EF_MC TEXT,"+
                "YF_ID INTEGER, "+
                "YF_MC TEXT, "+
                "JCK_FL TEXT, "+
                "JCK_NR TEXT"+
                ")";
        db.execSQL(CREATE_TABLE_JCNR);

        final String CREATE_TABLE_YWWD="CREATE TABLE "+TABLE_YWWD+" (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "YWID INTEGER, "+
                "ZLLB TEXT, "+
                "WDMC TEXT, "+
                "WDLJ TEXT"+
                ")";
        db.execSQL(CREATE_TABLE_YWWD);


        final String CREATE_TABLE_NFC_CARD="CREATE TABLE "+TABLE_NFC_CARDS+" (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "tag TEXT, "+
                "tag_time TEXT "+
                ")";

        db.execSQL(CREATE_TABLE_NFC_CARD);

        //创建card_read 表
        final String CREATE_TABLE_CARD_READ="CREATE TABLE "+TABLE_CARD_READ+"(_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "task_id TEXT,"+
                "organ_id TEXT,"+
                "tag_id TEXT,"+
                "card_type TEXT,"+
                "card_info TEXT,"+
                "tag_time TEXT"+
                ")";
        db.execSQL(CREATE_TABLE_CARD_READ);
        //创建 CHECK_RULES 表
        final String CREATE_TABLE_CHECK_RULES="CREATE TABLE "+TABLE_CHECK_RULES+"(_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "task_id TEXT,"+
                "rule_lv1 TEXT,"+
                "rule_lv2 TEXT,"+
                "rule_lv3 TEXT,"+
                "rule_content TEXT,"+
                "rule_updated TEXT"+
                ")";
        db.execSQL(CREATE_TABLE_CHECK_RULES);


        //创建 CHECK_RULES_STATUS 表
        final String CREATE_TABLE_CHECK_RULES_STATUS="CREATE TABLE "+TABLE_CHECK_RULES_STATUS+"(_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "task_id TEXT,"+
                "organ_id TEXT,"+
                "rule_lv1 TEXT,"+
                "rule_lv2 TEXT,"+
                "rule_lv3 TEXT,"+
                "rule_content TEXT,"+
                "rule_status TEXT"+//rule_status:YES,NO 如果在本表中没有 则NULL 未确认状态
                ")";
        db.execSQL(CREATE_TABLE_CHECK_RULES_STATUS);

        //创建 CHECK_RECORDS 表
        final String CREATE_TABLE_CHECK_RECORDS="CREATE TABLE "+TABLE_RECORDS+"(_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "task_id TEXT,"+
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
        db.execSQL(CREATE_TABLE_CHECK_RECORDS);
        //创建 CONTACTS 表
        final String CREATE_TABLE_CONTACTS="CREATE TABLE "+TABLE_CONTACTS+"(_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "organ_id TEXT,"+
                "contact TEXT,"+
                "phone TEXT,"+
                "created TEXT"+
                ")";
        db.execSQL(CREATE_TABLE_CONTACTS);

        //Insert Test Rules to DB
        ArrayList<CheckRulesInfo> checkList=new ArrayList<CheckRulesInfo>();
        checkList.add(new CheckRulesInfo("990","工业安全","采油专业","安全环保标志","应设在所指目标物附近的醒目地方"));
        int total=checkList.size();
        for(int i=0;i<total;i++)
        {
            CheckRulesInfo rule=checkList.get(i);
            ContentValues values = new ContentValues();
            values.put("organ_id", rule.getOrganID());
            values.put("rule_lv1", rule.getRuleLv1());
            values.put("rule_lv2", rule.getRuleLv2());
            values.put("rule_lv3", rule.getRuleLv3());
            values.put("rule_content", rule.getRuleContent());
            values.put("rule_updated",rule.getRuleUpdated());
            long result=db.insert(HSESQLiteHelper.TABLE_CHECK_RULES, null, values);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop older tables if existed
        final String DROP_TABLE_NFC_CARD="DROP TABLE IF EXISTS "+TABLE_NFC_CARDS;
        final String DROP_TABLE_CARD_READ="DROP TABLE IF EXISTS "+TABLE_CARD_READ;
        final String DROP_TABLE_CHECK_RECORDS="DROP TABLE IF EXISTS "+TABLE_RECORDS;
        final String DROP_TABLE_CONTACTS="DROP TABLE IF EXISTS "+TABLE_CONTACTS;
        db.execSQL(DROP_TABLE_NFC_CARD);
        db.execSQL(DROP_TABLE_CARD_READ);
        db.execSQL(DROP_TABLE_CHECK_RECORDS);
        db.execSQL(DROP_TABLE_CONTACTS);
        //create fresh tables
        this.onCreate(db);


    }
}
