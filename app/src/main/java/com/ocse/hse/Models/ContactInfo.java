package com.ocse.hse.Models;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ocse.hse.app.AppLog;
import com.ocse.hse.app.ApplicationController;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by leehaining on 14-6-5.
 */
public class ContactInfo implements Serializable {
    private String contact;
    private String phone;
    public ContactInfo()
    {
        this.contact="";
        this.phone="";
    }
    public ContactInfo(String contact,String phone)
    {
        this.contact=contact;
        this.phone=phone;
    }
    public String getContact()
    {
        return this.contact;
    }
    public String getPhone()
    {
        return this.phone;
    }
    public static ArrayList<ContactInfo> getAllContactsInDB(String organID)
    {
        /**
         * "organ_id TEXT,"+
         "contact TEXT,"+
         "phone TEXT,"+
         "created TEXT"+
         */
        SQLiteDatabase db= ApplicationController.getSqLiteDatabase();
        String selection="organ_id=?";
        String[] selectionArgs=new String[]{organID};
        if(organID==null)
        {
            selection=null;
            selectionArgs=null;
        }
        String orderBy="_id DESC";
        Cursor cursor = db.query(HSESQLiteHelper.TABLE_CONTACTS,null,selection,selectionArgs,null,null,orderBy);
        ArrayList<ContactInfo> contactList=new ArrayList<ContactInfo>();
        /* 使用游标---获取游标中的数据 */
        while(cursor.moveToNext()){

            String contact=cursor.getString(cursor.getColumnIndex("contact"));
            String phone=cursor.getString(cursor.getColumnIndex("phone"));
            String created=cursor.getString(cursor.getColumnIndex("created"));
            AppLog.i("Contacts in DB:  contact=" + contact + " phone=" + phone + " created=" + created);

            ContactInfo item=new ContactInfo( contact, phone);
            contactList.add(item);
        }
        cursor.close();
        return contactList;
    }
}
