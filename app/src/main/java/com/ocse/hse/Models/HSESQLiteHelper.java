package com.ocse.hse.Models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by leehaining on 14-5-26.
 */
public class HSESQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="HSEDB";
    private static final int DATABASE_VERSION=1;
    public static final String TABLE_NFC_CARDS="NFC_CARDS";
    public static final String TABLE_CARD_READ="CARD_READ";
    public static final String TABLE_RECORDS="CHECK_RECORDS";
    public static final String TABLE_CONTACTS="CHECK_CONTACTS";
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
        //创建 CHECK_RECORDS 表
        final String CREATE_TABLE_CHECK_RECORDS="CREATE TABLE "+TABLE_RECORDS+"(_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "task_id TEXT,"+
                "organ_id TEXT,"+
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
