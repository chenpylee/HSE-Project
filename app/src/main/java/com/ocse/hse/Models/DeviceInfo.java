package com.ocse.hse.Models;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.ocse.hse.app.ApplicationConstants;

/**
 * Created by leehaining on 5/5/14.
 */
public class DeviceInfo {
    private String devicePhoneNumber;
    private String deviceIMEI;
    private String deviceModel;
    private String deviceOS;
    private String deviceVersion;
    private String deviceHolder;
    public DeviceInfo(TelephonyManager tMgr)
    {
        this.deviceHolder="";
        this.deviceOS=ApplicationConstants.OS_NAME;
        this.deviceModel= Build.BRAND+" "+Build.MODEL;
        this.deviceVersion=Build.VERSION.RELEASE;
        this.deviceIMEI="";
        this.devicePhoneNumber="";
        if(tMgr.getLine1Number()!=null)
        {
            this.devicePhoneNumber=tMgr.getLine1Number();
        }

        this.deviceIMEI=tMgr.getDeviceId();
    }
    public DeviceInfo(String deviceHolder,String deviceOS, String deviceModel, String deviceVersion,String deviceIMEI, String devicePhoneNumber)
    {
        this.deviceHolder=deviceHolder;
        this.deviceOS=deviceOS;
        this.deviceModel=deviceModel;
        this.deviceVersion=deviceVersion;
        this.deviceIMEI=deviceIMEI;
        this.devicePhoneNumber=devicePhoneNumber;
    }
    public void setHolder(String holder)
    {
        this.deviceHolder=holder;
    }
    public void setPhoneNumer(String phoneNumer)
    {
        this.devicePhoneNumber=phoneNumer;
    }
    public static DeviceInfo getSavedDeviceInfo(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences(ApplicationConstants.APP_PREFERENCE, Context.MODE_PRIVATE);
        String deviceHolder=preferences.getString(ApplicationConstants.APP_PREFERENCE_DEVICE_HOLDER,"");
        String deviceOS=preferences.getString(ApplicationConstants.APP_PREFERENCE_DEVICE_OS,"android");
        String deviceModel=preferences.getString(ApplicationConstants.APP_PREFERENCE_DEVICE_MODEL,"");
        String deviceVersion=preferences.getString(ApplicationConstants.APP_PREFERENCE_DEVICE_VERSION,"");
        String deviceIMEI=preferences.getString(ApplicationConstants.APP_PREFERENCE_DEVICE_IMEI,"");
        String devicePhoneNumber=preferences.getString(ApplicationConstants.APP_PREFERENCE_DEVICE_PHONE_NUMBER,"");
        return new DeviceInfo( deviceHolder, deviceOS,  deviceModel,  deviceVersion, deviceIMEI,  devicePhoneNumber);
    }
    public static Boolean isRegisted(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences(ApplicationConstants.APP_PREFERENCE, Context.MODE_PRIVATE);
        Boolean registed=preferences.getBoolean(ApplicationConstants.APP_PREFERENCE_DEVICE_REG,false);
        return registed;
    }
    public static void clearRegistration(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences(ApplicationConstants.APP_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(ApplicationConstants.APP_PREFERENCE_DEVICE_REG, false);
        editor.commit();
    }
    public  void saveDeviceAndRegStatus(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences(ApplicationConstants.APP_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ApplicationConstants.APP_PREFERENCE_DEVICE_HOLDER, this.deviceHolder);
        editor.putString(ApplicationConstants.APP_PREFERENCE_DEVICE_OS, this.deviceOS);
        editor.putString(ApplicationConstants.APP_PREFERENCE_DEVICE_MODEL, this.deviceModel);
        editor.putString(ApplicationConstants.APP_PREFERENCE_DEVICE_VERSION, this.deviceVersion);
        editor.putString(ApplicationConstants.APP_PREFERENCE_DEVICE_IMEI, this.deviceIMEI);
        editor.putString(ApplicationConstants.APP_PREFERENCE_DEVICE_PHONE_NUMBER, this.devicePhoneNumber);
        editor.putBoolean(ApplicationConstants.APP_PREFERENCE_DEVICE_REG, true);
        editor.commit();

    }
    public void saveDeviceInfo(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences(ApplicationConstants.APP_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ApplicationConstants.APP_PREFERENCE_DEVICE_HOLDER, this.deviceHolder);
        editor.putString(ApplicationConstants.APP_PREFERENCE_DEVICE_OS, this.deviceOS);
        editor.putString(ApplicationConstants.APP_PREFERENCE_DEVICE_MODEL, this.deviceModel);
        editor.putString(ApplicationConstants.APP_PREFERENCE_DEVICE_VERSION, this.deviceVersion);
        editor.putString(ApplicationConstants.APP_PREFERENCE_DEVICE_IMEI, this.deviceIMEI);
        editor.putString(ApplicationConstants.APP_PREFERENCE_DEVICE_PHONE_NUMBER, this.devicePhoneNumber);
        editor.commit();
    }
    public String getDevicePhoneNumber()
    {
        return this.devicePhoneNumber;
    }
    public String getDeviceID()
    {
        return this.deviceIMEI;
    }
    public String getDeviceIMEI(){
        return this.deviceIMEI;
    }
    public String getDeviceHolder(){
        return this.deviceHolder;
    }
    public String getDeviceModel()
    {
        return this.deviceModel;
    }
    public String getDeviceOS(){
        return this.deviceOS;
    }
    public String getDeviceVersion(){
        return this.deviceVersion;
    }

    public String getParameters(){
        //IMEI=1233211223&holder=大灰狼&OS=android&version=4.1.2&model=htc_0299&phone_number=1344443333
        return "holder="+this.deviceHolder+"&OS="+this.deviceOS+"&model="+this.deviceModel+"&version="+this.deviceVersion+"&IMEI="+this.deviceIMEI+"&phone_number="+this.devicePhoneNumber;
    }
}
