package com.ocse.hse.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;

import com.ocse.hse.Models.DeviceInfo;

/**
 * Created by leehaining on 5/7/14.
 */
public class ApplicationController extends Application {
    public static Context context;
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
}
