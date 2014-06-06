package com.ocse.hse.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.telephony.TelephonyManager;

import com.ocse.hse.Models.DeviceInfo;
import com.ocse.hse.Models.HSESQLiteHelper;

import java.io.File;

/**
 * Created by leehaining on 5/7/14.
 */
public class ApplicationController extends Application {
    public static Context context;
    public static SQLiteDatabase sqLiteDatabase;
    @Override
    public void onCreate(){
        super.onCreate();
        context=getApplicationContext();
        AppLog.d("ApplicationController Created");
    }
    public static void saveServerIP(String serverIP)
    {
        SharedPreferences preferences = context.getSharedPreferences(ApplicationConstants.APP_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ApplicationConstants.APP_PREFERENCE_SERVER_IP, serverIP);
        editor.commit();
    }
    public static String getServerIP()
    {
        SharedPreferences preferences = context.getSharedPreferences(ApplicationConstants.APP_PREFERENCE, Context.MODE_PRIVATE);
        String serverIP=preferences.getString(ApplicationConstants.APP_PREFERENCE_SERVER_IP,"218.28.88.188:8080");
        return serverIP;
    }
    public static DeviceInfo getDeviceInfo()
    {
        TelephonyManager tMgr = (TelephonyManager)context.getSystemService(TELEPHONY_SERVICE);
        DeviceInfo mDeviceInfo=new DeviceInfo(tMgr);
        return mDeviceInfo;
    }
    public static SQLiteDatabase getSqLiteDatabase()
    {
        if(sqLiteDatabase==null)
        {
            sqLiteDatabase=HSESQLiteHelper.getInstance(context);
        }
        return sqLiteDatabase;
    }
    public static void saveCurrentTaskAndOrganID(String taskID,String organID)
    {
        SharedPreferences preferences = context.getSharedPreferences(ApplicationConstants.APP_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ApplicationConstants.APP_PREFERENCE_CURRENT_TASK_ID, taskID);
        editor.putString(ApplicationConstants.APP_PREFERENCE_CURRENT_ORGAN_ID, organID);
        editor.commit();
    }
    public static String getCurrentTaskID(){
        SharedPreferences preferences = context.getSharedPreferences(ApplicationConstants.APP_PREFERENCE, Context.MODE_PRIVATE);
        String taskID=preferences.getString(ApplicationConstants.APP_PREFERENCE_CURRENT_TASK_ID,"0");
        return taskID;
    }
    public static String getCurrentOrganID(){
        SharedPreferences preferences = context.getSharedPreferences(ApplicationConstants.APP_PREFERENCE, Context.MODE_PRIVATE);
        String organID=preferences.getString(ApplicationConstants.APP_PREFERENCE_CURRENT_ORGAN_ID,"0");
        return organID;
    }
    public static void printDirecotryInformation(){
        File recordsDir = context.getDir("records", Context.MODE_PRIVATE);
        AppLog.i("Start setDirectories");
        if(!recordsDir.exists()) {
            recordsDir.mkdir();

        }
        else
        {
            AppLog.i("Directory Exists."+recordsDir.getPath().toString());
        }

        String[] files=recordsDir.list();
        AppLog.i("Total:"+Integer.toString(files.length));
        if(files!=null)
        {
            for(int i=0;i<files.length;i++)
            {
                AppLog.i("/data/data/com.ocse.hse/"+files[i]);
            }
        }

    }
    public static void setAndCheckDirectories()
    {
        //You can not pass a directory structure (e.g. a/b/c) to GetDir()
        //http://stackoverflow.com/questions/15141342/android-create-directory-hierarchy
        File dir = context.getFilesDir();
        File recordImagesPreviewFolder = new File(dir, "records/preview");
        createDirectory(recordImagesPreviewFolder);
        File recordImagesContentFolder=new File(dir,"records/content");
        createDirectory(recordImagesContentFolder);
    }
    private static void createDirectory(File dir)
    {
        if(!dir.exists())
        {
            try{
                Boolean done=dir.mkdirs();
                if(done)
                {
                    AppLog.i("Creating directory "+dir.getAbsolutePath().toString()+" OK");
                }
                else
                {
                    AppLog.i("Creating directory "+dir.getAbsolutePath().toString()+" Failed");
                }
            }catch (Exception e)
            {
                AppLog.i("Creating directory "+dir.getAbsolutePath().toString()+" failed."+e.getMessage().toString());
            }
        }
        else
        {
            AppLog.i(dir.getAbsolutePath().toString()+" already exists.");
        }
    }
    public static File getFile(String directory,String fileName)
    {
        File dir = context.getFilesDir();
        File resultFile = new File(dir, directory+File.separator+fileName);
        return resultFile;
    }
    public static File[] listFiles(String directory)
    {
        File dir = context.getFilesDir();
        File resultDir = new File(dir, directory);
        File[] files=resultDir.listFiles();
        return files;
    }
    public static Boolean fileExists(String directory,String fileName)
    {
        File dir = context.getFilesDir();
        File resultFile = new File(dir, directory+File.separator+fileName);
        return resultFile.exists();
    }

}
