package com.ocse.hse;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ocse.hse.Models.DeviceInfo;
import com.ocse.hse.app.AppLog;
import com.ocse.hse.app.ApplicationConstants;
import com.ocse.hse.app.ApplicationController;
import com.ocse.hse.app.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class DeviceSettingsActivity extends Activity {

    DeviceInfo deviceInfo;
    EditText txtDeviceHolder,txtPhoneNumber,txtDeviceID;
    Button btnUpdateSettings;
    Boolean isReg;
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_settings);

        actionBar=getActionBar();
        if(DeviceInfo.isRegisted(this))
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setIcon(R.drawable.icon_hse_actionbar);
            actionBar.setTitle("设置终端信息");
            isReg=true;
        }
        else
        {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setIcon(R.drawable.icon_hse_actionbar);
            actionBar.setTitle("注册终端信息");
            isReg=false;
        }


        //
        txtDeviceHolder=(EditText)findViewById(R.id.txtDeviceHolder);
        txtPhoneNumber=(EditText)findViewById(R.id.txtPhoneNumber);
        txtDeviceID=(EditText)findViewById(R.id.txtDeviceID);
        btnUpdateSettings=(Button)findViewById(R.id.btnUpdateSettings);
        //get device information
        if(isReg)
        {
            deviceInfo=DeviceInfo.getSavedDeviceInfo(this);
        }
        else
        {
            deviceInfo=getDeviceInfomation();
        }

        String deviceID=deviceInfo.getDeviceID();
        String phoneNumber=deviceInfo.getDevicePhoneNumber();
        String deviceHolder=deviceInfo.getDeviceHolder();
        if(!deviceHolder.equals(""))
        {
            txtDeviceHolder.setText(deviceHolder);
        }

        if(!deviceID.equals("")) {
            txtDeviceID.setText(deviceInfo.getDeviceID());
        }
        else
        {
            txtDeviceID.setEnabled(true);
        }

        if(!phoneNumber.equals("")) {
            txtPhoneNumber.setText(deviceInfo.getDevicePhoneNumber());
        }
        else
        {
            txtPhoneNumber.setEnabled(true);
        }

        btnUpdateSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDeviceInfo();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.device_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            quitActivity();
            return true;
        }
        else if(id==R.id.action_settings_refresh)
        {
            getDeviceInfoFromServer();
            return true;
        }
        else if(id==R.id.action_done)
        {
            updateDeviceInfo();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                quitActivity();
                return true;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void quitActivity()
    {
        finish();
        overridePendingTransition(R.anim.in_just_show, R.anim.out_push_left_to_right);
    }
    private void goMainActivity()
    {
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.in_push_right_to_left,R.anim.push_down);
    }
    private DeviceInfo getDeviceInfomation()
    {
        TelephonyManager tMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        DeviceInfo mDeviceInfo=new DeviceInfo(tMgr);
        return mDeviceInfo;
    }

    private void fillContent()
    {
        deviceInfo=DeviceInfo.getSavedDeviceInfo(this);
        String deviceID=deviceInfo.getDeviceID();
        String phoneNumber=deviceInfo.getDevicePhoneNumber();
        String deviceHolder=deviceInfo.getDeviceHolder();
        if(!deviceHolder.equals(""))
        {
            txtDeviceHolder.setText(deviceHolder);
        }

        if(!deviceID.equals("")) {
            txtDeviceID.setText(deviceInfo.getDeviceID());
        }
        else
        {
            txtDeviceID.setEnabled(true);
        }

        if(!phoneNumber.equals("")) {
            txtPhoneNumber.setText(deviceInfo.getDevicePhoneNumber());
        }
        else
        {
            txtPhoneNumber.setEnabled(true);
        }
    }

    private void getDeviceInfoFromServer()
    {
        RequestQueue mRequestQueue= VolleySingleton.getInstance().getRequestQueue();
        final ProgressDialog progressDialog = ProgressDialog.show(this, "终端设备信息", "加载中...");
        Response.Listener<JSONObject> listener=new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(progressDialog!=null) {
                        progressDialog.dismiss();
                    }
                    AppLog.d(response.toString());
                    int result = -1;
                    if(response.has(ApplicationConstants.JSON_RESULT))
                    {

                        result=response.getInt(ApplicationConstants.JSON_RESULT);
                        if(result==1) {
                            JSONObject content=response.getJSONObject(ApplicationConstants.JSON_CONTENT);
                            /**
                             *
                             "content": {
                             "holder": "习近平",
                             "model": "htc_0299",
                             "version": "4.1.2",
                             "IMEI": "1233211223",
                             "OS": "android",
                             "phoneNumber": "1344443333"
                             },
                             */
                            String msg=response.optString("message");
                            Toast.makeText(DeviceSettingsActivity.this, msg, Toast.LENGTH_SHORT).show();

                            String holder=content.getString("holder");
                            String model=content.getString("model");
                            String version=content.getString("version");
                            String IMEI=content.getString("IMEI");
                            String OS=content.getString("OS");
                            String phoneNumber=content.getString("phoneNumber");
                            DeviceInfo deviceInfo=new DeviceInfo(holder,OS, model, version,IMEI, phoneNumber);
                            //deviceInfo.saveDeviceInfo(ServerSettingActivity.this);
                            deviceInfo.saveDeviceAndRegStatus(DeviceSettingsActivity.this);
                            //Go To Main
                            fillContent();

                        }
                        else if(result==-1)
                        {
                            String msg=response.optString("message");


                        }
                    }
                }catch (JSONException e)
                {
                    AppLog.e(e.getMessage());
                }
            }
        };
        Response.ErrorListener errorListener= new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppLog.e(error.getMessage());
                if(progressDialog!=null) {
                    progressDialog.dismiss();
                }
            }
        };


        HashMap<String,String> params=new HashMap<String, String>();
        params.put("IMEI",deviceInfo.getDeviceIMEI());

        String baseURL="http://"+ApplicationController.getServerIP();
        String urlString=baseURL;
        Uri.Builder builder = new Uri.Builder();
        try {
            URL url = new URL(baseURL);
            builder = new Uri.Builder()
                    .scheme(url.getProtocol())
                    .encodedAuthority(url.getAuthority())
                    .appendPath("api")
                    .appendPath("device_info.php");
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.appendQueryParameter(entry.getKey(), entry.getValue());
            }
            urlString=builder.build().toString();
        }catch (Exception e)
        {
            AppLog.e(e.getMessage());
        }

        AppLog.d(urlString);
        JsonObjectRequest mJsonObjectRequest=new JsonObjectRequest(urlString,null,listener,errorListener);

        if(mRequestQueue!=null) {
            mRequestQueue.add(mJsonObjectRequest);
            AppLog.d("star loading");
            mRequestQueue.start();
        }

    }
    private void updateDeviceInfo()
    {
        if(isReg)
        {
            deviceInfo=getDeviceInfomation();
        }
        deviceInfo.setHolder(txtDeviceHolder.getText().toString());
        deviceInfo.setPhoneNumer(txtPhoneNumber.getText().toString());


        RequestQueue mRequestQueue= VolleySingleton.getInstance().getRequestQueue();
        final ProgressDialog progressDialog = ProgressDialog.show(this, "终端设备信息", "更新中...");
        Response.Listener<JSONObject> listener=new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(progressDialog!=null) {
                        progressDialog.dismiss();
                    }
                    AppLog.d(response.toString());
                    int result = -1;
                    if(response.has(ApplicationConstants.JSON_RESULT))
                    {

                        result=response.getInt(ApplicationConstants.JSON_RESULT);
                        if(result==1) {
                            JSONObject content=response.getJSONObject(ApplicationConstants.JSON_CONTENT);
                            /**
                             *
                             "content": {
                             "holder": "习近平",
                             "model": "htc_0299",
                             "version": "4.1.2",
                             "IMEI": "1233211223",
                             "OS": "android",
                             "phoneNumber": "1344443333"
                             },
                             */
                            String msg=response.optString("message");
                            Toast.makeText(DeviceSettingsActivity.this, msg, Toast.LENGTH_SHORT).show();

                            String holder=content.getString("holder");
                            String model=content.getString("model");
                            String version=content.getString("version");
                            String IMEI=content.getString("IMEI");
                            String OS=content.getString("OS");
                            String phoneNumber=content.getString("phoneNumber");
                            DeviceInfo deviceInfo=new DeviceInfo(holder,OS, model, version,IMEI, phoneNumber);
                            //deviceInfo.saveDeviceInfo(ServerSettingActivity.this);
                            deviceInfo.saveDeviceAndRegStatus(DeviceSettingsActivity.this);
                            //Go To Main
                            quitActivity();
                            /**
                            if(isReg)
                            {
                                //back to main
                                quitActivity();
                            }
                            else
                            {
                                //go to main
                                goMainActivity();
                            }
                             **/
                        }
                        else if(result==-1)
                        {
                            String msg=response.optString("message");
                            Toast.makeText(DeviceSettingsActivity.this, msg, Toast.LENGTH_SHORT).show();
                            //Go To DeviceInfo Setting
                        }
                    }
                }catch (JSONException e)
                {
                    AppLog.e(e.getMessage());
                }
            }
        };
        Response.ErrorListener errorListener= new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppLog.e(error.getMessage());
                if(progressDialog!=null) {
                    progressDialog.dismiss();
                }
                Toast.makeText(DeviceSettingsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        /**
         * http://localhost/api/device_register.php?IMEI=1233211223&holder=%E4%B9%A0%E8%BF%91%E5%B9%B3&OS=android&version=4.1.2&model=htc_0299&phone_number=1344443333
         */

        HashMap<String,String> params=new HashMap<String, String>();
        params.put("IMEI",deviceInfo.getDeviceIMEI());
        params.put("holder",deviceInfo.getDeviceHolder());
        params.put("OS",deviceInfo.getDeviceOS());
        params.put("version",deviceInfo.getDeviceVersion());
        params.put("model",deviceInfo.getDeviceModel());
        params.put("phone_number",deviceInfo.getDevicePhoneNumber());

        String baseURL="http://"+ ApplicationController.getServerIP();
        String urlString=baseURL;
        Uri.Builder builder = new Uri.Builder();
        try {
            URL url = new URL(baseURL);
            builder = new Uri.Builder()
                    .scheme(url.getProtocol())
                    .encodedAuthority(url.getAuthority())
                    .appendPath(ApplicationConstants.SERVER_API_FOLDER)
                    .appendPath(ApplicationConstants.SERVER_DEVICE_REG);
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.appendQueryParameter(entry.getKey(), entry.getValue());
            }
            urlString=builder.build().toString();
        }catch (Exception e)
        {
            AppLog.e(e.getMessage());
        }

        AppLog.d(urlString);
        JsonObjectRequest mJsonObjectRequest=new JsonObjectRequest(urlString,null,listener,errorListener);

        if(mRequestQueue!=null) {
            mRequestQueue.add(mJsonObjectRequest);
            AppLog.d("star loading");
            mRequestQueue.start();
        }
    }
}
