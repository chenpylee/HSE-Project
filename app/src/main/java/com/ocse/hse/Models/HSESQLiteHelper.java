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
        final String CREATE_TABLE_NFC_CARD="CREATE TABLE "+"NFC_CARDS"+" (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "tag TEXT, "+
                "tag_time TEXT "+
                ")";
        db.execSQL(CREATE_TABLE_NFC_CARD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop older tables if existed
        final String DROP_TABLE_NFC_CARD="DROP TABLE IF EXISTS NFC_CARDS";
        db.execSQL(DROP_TABLE_NFC_CARD);

        //create fresh tables
        this.onCreate(db);


    }
}
