package com.ocse.hse;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ocse.hse.Activities.ResultsListActivity;
import com.ocse.hse.Models.JCDWInfo;
import com.ocse.hse.Models.JCFLInfo;
import com.ocse.hse.Models.JCNRInfo;
import com.ocse.hse.Models.JcrwInfo;
import com.ocse.hse.Models.JcrwInfoAdapter;
import com.ocse.hse.app.AppLog;
import com.ocse.hse.app.ApplicationConstants;
import com.ocse.hse.app.ApplicationController;
import com.ocse.hse.app.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResultUploadActivity extends Activity {
    private TextView txtActivityID;
    private Button btnTagViewer;
    private ActionBar actionBar;
    private ListView taskListView;
    private JcrwInfoAdapter taskAdapter;
    private ArrayList<JcrwInfo> taskList;
    private ArrayList<Integer> jrList;
    private ArrayList<String> jrmcList;
    private JcrwInfo selectedJcrwInfo;
    private int downloadIndex=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_offline);
        taskListView=(ListView)findViewById(R.id.taskListView);
        actionBar=getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setIcon(R.drawable.icon_hse_actionbar);
        actionBar.setTitle("上传检查结果");
        /**
        txtActivityID=(TextView)findViewById(R.id.txtActivityID);
        btnTagViewer=(Button)findViewById(R.id.btnTagViewer);
        btnTagViewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,NFCReadActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_up,R.anim.push_down);
            }
        });
         **/
        taskList=new ArrayList<JcrwInfo>();
        taskAdapter=new JcrwInfoAdapter(this,taskList);
        taskListView.setAdapter(taskAdapter);

        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JcrwInfo task=taskAdapter.getJcrwInfo(position);
                selectedJcrwInfo=task;
                //getJCRWDetailFromServer(task.getJR_ID());
                goToTask();
                /**
                Intent intent=new Intent(MainActivity.this,OrganListActivity.class);
                TaskInfo task=taskList.get(position);
                intent.putExtra(ApplicationConstants.APP_BUNDLE_TASK_INFO_KEY,task);
                startActivity(intent);
                overridePendingTransition(R.anim.in_push_right_to_left,R.anim.push_down);
                 **/
            }
        });
        displayJCRWFromDB();

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        //txtActivityID.setText(this.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.main_upload, menu);
        return super.onCreateOptionsMenu(menu);

        //return false;
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
        else if(id==R.id.action_task_refresh)
        {
            AppLog.i("Refreshing Task");
            getTaskListFromServer();
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



    //Get Task List From Server or DB


    private void getTaskListFromServer()
    {
        String deviceIMEI= ApplicationController.getDeviceInfo().getDeviceIMEI();
        RequestQueue mRequestQueue= VolleySingleton.getInstance().getRequestQueue();
        final ProgressDialog progressDialog = ProgressDialog.show(this, "HSE服务器", "正在获取检查任务...");
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
                            JSONArray taskArray=content.getJSONArray("task_list");
                            if(taskArray.length()>0)
                            {
                                JcrwInfo.clearJcrwInDB();
                            }
                            for (int i=0; i<taskArray.length(); i++) {
                                JSONObject item = taskArray.getJSONObject(i);
                                String JR_MC = "", JR_KSSJ = "", JR_JSSJ = "";
                                int JR_ID=0;
                                if (!item.isNull("JR_ID")) {
                                    JR_ID = item.optInt("JR_ID", 0);
                                }
                                if (!item.isNull("JR_MC")) {
                                    JR_MC = item.optString("JR_MC", "");
                                }
                                if (!item.isNull("JR_KSSJ")) {
                                    JR_KSSJ = item.optString("JR_KSSJ", "");
                                }
                                if (!item.isNull("JR_JSSJ")) {
                                    JR_JSSJ = item.optString("JR_JSSJ", "");
                                }
                                AppLog.d("JR_ID:"+JR_ID+" JR_MC:"+JR_MC+" JR_KSSJ:"+JR_KSSJ+" JR_JSSJ:"+JR_JSSJ);
                                JcrwInfo jcrw=new JcrwInfo(JR_ID,JR_MC,JR_KSSJ,JR_JSSJ);
                                jcrw.updateJcrwInDB();
                            }
                            //JcrwInfo.getAllJcrwInDB();
                        }
                        else if(result==-1)
                        {
                            String msg=response.optString("message");
                            JcrwInfo.clearJcrwInDB();
                            Toast.makeText(ResultUploadActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    }
                }catch (JSONException e)
                {
                    AppLog.e(e.getMessage());
                }
                displayUpdatedJCRWFromDB();
            }
        };
        Response.ErrorListener errorListener= new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppLog.e(error.getMessage());
                if(progressDialog!=null) {
                    progressDialog.dismiss();
                }
                Toast.makeText(ResultUploadActivity.this,"连接服务器失败",Toast.LENGTH_SHORT).show();
            }
        };


        HashMap<String,String> params=new HashMap<String, String>();
        params.put("imei",deviceIMEI);

        String baseURL="http://"+ApplicationController.getServerIP();
        String urlString=baseURL;
        Uri.Builder builder = new Uri.Builder();
        try {
            URL url = new URL(baseURL);

            builder = new Uri.Builder()
                    .scheme(url.getProtocol())
                    .encodedAuthority(url.getAuthority())
                    .appendPath("api")
                    .appendPath("get_task_list.php");
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
            AppLog.d("star loading:"+urlString);
            mRequestQueue.start();
        }
    }
    private void displayUpdatedJCRWFromDB()
    {
        ArrayList<JcrwInfo> jcrwList=JcrwInfo.getAllJcrwInDB();
        taskAdapter.refillTaskList(jcrwList);
        jrList=new ArrayList<Integer>();
        jrmcList=new ArrayList<String>();
        for(int i=0;i<jcrwList.size();i++)
        {
            jrList.add(jcrwList.get(i).getJR_ID());
            jrmcList.add(jcrwList.get(i).getJR_MC());
        }
        downloadIndex=0;
        downloadNextTaskData();
    }
    private void downloadNextTaskData()
    {
        if(downloadIndex<jrList.size())
        {
            getJCRWDetailFromServer(jrList.get(downloadIndex));

        }
        else
        {
            showMessage("离线检查任务","离线数据下载完成");
        }
    }
    private void showMessage(String title,String message)
    {
        AlertDialog dialog= new AlertDialog.Builder(this).setNeutralButton("确定", null).create();
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.show();
    }
    private void displayJCRWFromDB()
    {
        ArrayList<JcrwInfo> jcrwList=JcrwInfo.getAllJcrwInDB();
        taskAdapter.refillTaskList(jcrwList);
    }

    private void getJCRWDetailFromServer(final int jr_id)
    {
        RequestQueue mRequestQueue= VolleySingleton.getInstance().getRequestQueue();
        String taskName=jrmcList.get(downloadIndex);
        final ProgressDialog progressDialog = ProgressDialog.show(this,taskName, "正在获取检查内容...");
        Response.Listener<JSONObject> listener=new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(progressDialog!=null) {
                        progressDialog.dismiss();
                    }
                    //AppLog.d(response.toString());
                    int result = -1;
                    if(response.has(ApplicationConstants.JSON_RESULT))
                    {

                        result=response.getInt(ApplicationConstants.JSON_RESULT);
                        if(result==1) {
                            JSONObject content=response.getJSONObject(ApplicationConstants.JSON_CONTENT);
                            JSONArray jcdwArray=content.getJSONArray("JCDW");
                            //Update 检查单位 JCDW
                            JCDWInfo.clearJCDWInDB(jr_id);
                            for (int i=0; i<jcdwArray.length(); i++) {
                                JSONObject item = jcdwArray.getJSONObject(i);
                                String JD_DWMC = "";
                                int JD_DEPTID=0;
                                if (!item.isNull("JD_DEPTID")) {
                                    JD_DEPTID = item.optInt("JD_DEPTID", 0);
                                }
                                if (!item.isNull("JD_DWMC")) {
                                    JD_DWMC = item.optString("JD_DWMC", "");
                                }
                                AppLog.d("JR_ID:"+jr_id+" JD_DEPTID:"+JD_DEPTID+" JD_DWMC:"+JD_DWMC);
                                JCDWInfo jcdw=new JCDWInfo(jr_id,JD_DEPTID,JD_DWMC);
                                jcdw.updateJCDWInDB();
                            }
                            //JCDWInfo.getAllJcdwInDB();
                            //Update 检查分类 JCFL
                            JSONArray jcflArray=content.getJSONArray("JCFL");
                            JCFLInfo.clearJCFLInDB(jr_id);
                            for(int i=0;i<jcflArray.length();i++)
                            {
                                JSONObject item=jcflArray.getJSONObject(i);
                                int EF_ID=item.optInt("EF_ID",0);
                                int YF_ID=item.optInt("YF_ID",0);
                                String EF_MC=item.optString("EF_MC");
                                String YF_MC=item.optString("YF_MC");
                                AppLog.d("JR_ID:"+jr_id+" EF_ID:"+EF_ID+" EF_MC:"+EF_MC+" YF_ID:"+YF_ID+" YF_MC:"+YF_MC);
                                JCFLInfo jcfl=new JCFLInfo( jr_id, EF_ID, EF_MC, YF_ID, YF_MC);
                                jcfl.updateJCFLInDB();
                            }
                            //JCFLInfo.getAllJCFLInDB();

                            //update 检查内容
                            JCNRInfo.clearJCNRInDB(jr_id);
                            JCNRInfo.clearCheckRulesInDB(jr_id);
                            ArrayList<JCNRInfo> nrList=new ArrayList<JCNRInfo>();
                            JSONArray jcnrArray=content.getJSONArray("JCNR");
                            int currentEFID=0;
                            int currentYFID=0;
                            String currentEFMC="";
                            String currentYFMC="";
                            for(int i=0;i<jcnrArray.length();i++)
                            {
                                JSONObject item=jcnrArray.getJSONObject(i);
                                int EF_ID=item.optInt("EF_ID",0);
                                int YF_ID=item.optInt("YF_ID",0);
                                if(currentEFID!=EF_ID)
                                {
                                    currentEFID=EF_ID;
                                    currentYFID=YF_ID;
                                    JCFLInfo fl=JCFLInfo.getJCFLFromDB(jr_id,currentEFID,currentYFID);
                                    currentEFMC=fl.getEF_MC();
                                    currentYFMC=fl.getYF_MC();
                                }

                                String EF_MC=currentEFMC;
                                String YF_MC=currentYFMC;
                                String JCK_FL=item.optString("JCK_FL");
                                String JCK_NR=item.optString("JCK_NR");
                                AppLog.d("JR_ID:"+jr_id+" EF_ID:"+EF_ID+" EF_MC:"+EF_MC+" YF_ID:"+YF_ID+" YF_MC:"+YF_MC+" JCK_FL:"+JCK_FL+" JCK_NR:"+JCK_NR);
                                JCNRInfo nr=new JCNRInfo( jr_id, EF_ID, EF_MC, YF_ID, YF_MC,JCK_FL,JCK_NR);
                                nrList.add(nr);
                                nr.updateJCNRInDB();
                            }
                            //update CheckRuleDB
                            JCNRInfo.updateCheckRulesInDB(nrList);
                            //JCNRInfo.getJCNRFromDB(jr_id);
                        }
                        else if(result==-1)
                        {
                            String msg=response.optString("message");

                            Toast.makeText(ResultUploadActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    }
                }catch (JSONException e)
                {
                    AppLog.e(e.getMessage());
                }
                //进入检查界面
                //goToTask();
                downloadIndex++;
                downloadNextTaskData();
            }
        };
        Response.ErrorListener errorListener= new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppLog.e(error.getMessage());
                if(progressDialog!=null) {
                    progressDialog.dismiss();
                }
                Toast.makeText(ResultUploadActivity.this,"连接服务器失败",Toast.LENGTH_SHORT).show();
            }
        };


        HashMap<String,String> params=new HashMap<String, String>();
        params.put("id",Integer.toString(jr_id));

        String baseURL="http://"+ApplicationController.getServerIP();
        String urlString=baseURL;
        Uri.Builder builder = new Uri.Builder();
        try {
            URL url = new URL(baseURL);

            builder = new Uri.Builder()
                    .scheme(url.getProtocol())
                    .encodedAuthority(url.getAuthority())
                    .appendPath("api")
                    .appendPath("get_task_detail.php");
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
            AppLog.d("star loading:"+urlString);
            mRequestQueue.start();
        }
    }
    public void goToTask()
    {
        Intent intent=new Intent(this, ResultsListActivity.class);
        intent.putExtra(ApplicationConstants.APP_BUNDLE_TASK_INFO_KEY,selectedJcrwInfo);
        startActivity(intent);
        overridePendingTransition(R.anim.in_push_right_to_left,R.anim.push_down);
    }
}
