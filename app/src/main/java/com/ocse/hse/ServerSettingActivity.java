package com.ocse.hse;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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


public class ServerSettingActivity extends Activity {
    private EditText txtServerIP;
    private Button btnServerOK;
    private String serverIP;
    private String deviceIMEI;
    private ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_setting);

        actionBar=getActionBar();
        actionBar.setTitle(ApplicationConstants.APP_ACTION_BAR_TITLE);
        actionBar.setIcon(R.drawable.icon_hse_actionbar);

        txtServerIP=(EditText)findViewById(R.id.txtServerIP);
        btnServerOK=(Button)findViewById(R.id.btnServerOK);

        txtServerIP.setText(ApplicationController.getServerIP());
        btnServerOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDeviceRegistrationInfo();
            }
        });

        serverIP="";
        deviceIMEI=ApplicationController.getDeviceInfo().getDeviceIMEI();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.server_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id==android.R.id.home)
        {
            quitActivity();
            return true;
        }
        if (id == R.id.action_settings) {
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

    private void getDeviceRegistrationInfo()
    {
        serverIP=txtServerIP.getText().toString().trim();
        ApplicationController.saveServerIP(serverIP);
        /**
        if(true) {
            goMainActivity();//在家用网络中无法直接连接OCSE服务器，所以直接跳过服务器验证步骤，进入任务列表界面
            return;
        }
         **/


        RequestQueue mRequestQueue= VolleySingleton.getInstance().getRequestQueue();
        final ProgressDialog progressDialog = ProgressDialog.show(this, "HSE服务器", "连接中...");
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

                            String msg=response.optString("message");
                            Toast.makeText(ServerSettingActivity.this, msg, Toast.LENGTH_SHORT).show();

                            String holder=content.getString("holder");
                            String model=content.getString("model");
                            String version=content.getString("version");
                            String IMEI=content.getString("IMEI");
                            String OS=content.getString("OS");
                            String phoneNumber=content.getString("phoneNumber");
                            DeviceInfo deviceInfo=new DeviceInfo(holder,OS, model, version,IMEI, phoneNumber);
                            //deviceInfo.saveDeviceInfo(ServerSettingActivity.this);
                            deviceInfo.saveDeviceAndRegStatus(ServerSettingActivity.this);
                            //Go To Main
                            //goMainActivity();
                        }
                        else if(result==-1)
                        {
                            String msg=response.optString("message");
                            Toast.makeText(ServerSettingActivity.this, msg, Toast.LENGTH_SHORT).show();
                            //Go To DeviceInfo Setting
                            DeviceInfo.clearRegistration(ServerSettingActivity.this);
                            goDeviceSettingActivity();
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
                Toast.makeText(ServerSettingActivity.this,"连接服务器失败，进入开发模式",Toast.LENGTH_SHORT).show();
                //goMainActivity();
            }
        };


        HashMap<String,String> params=new HashMap<String, String>();
        params.put("IMEI",deviceIMEI);

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

    private void goDeviceSettingActivity()
    {
        Intent intent=new Intent(this,DeviceSettingsActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.in_push_right_to_left,R.anim.push_down);
    }

}
