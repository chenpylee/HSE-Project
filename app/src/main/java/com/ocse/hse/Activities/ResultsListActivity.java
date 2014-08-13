package com.ocse.hse.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ocse.hse.Models.JCDWInfo;
import com.ocse.hse.Models.JcrwInfo;
import com.ocse.hse.Models.RecordInfo;
import com.ocse.hse.Models.RecordInfoAdapter;
import com.ocse.hse.R;
import com.ocse.hse.app.AppLog;
import com.ocse.hse.app.ApplicationConstants;
import com.ocse.hse.app.ApplicationController;
import com.ocse.hse.app.VolleySingleton;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ResultsListActivity extends Activity {
    ActionBar actionBar;
    private ListView recordList;
    private ArrayList<RecordInfo> dataArray;
    private RecordInfoAdapter dataAdapter;
    private JcrwInfo taskInfo;
    //Pics
    static final String DIR_RECORD_PREVIEW="records/preview";
    private ArrayList<String> imagePathList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_record);
        actionBar=getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setIcon(R.drawable.icon_hse_actionbar);
        actionBar.setTitle("");
        imagePathList= new ArrayList<String>();
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null)
        {
            taskInfo=(JcrwInfo)bundle.getSerializable(ApplicationConstants.APP_BUNDLE_TASK_INFO_KEY);
            //organInfo=(OrganInfo)bundle.getSerializable(ApplicationConstants.APP_BUNDLE_ORGAN_INFO_KEY);
        }
        if(taskInfo!=null)
        {
            actionBar.setTitle("检查结果");
        }
        recordList=(ListView)findViewById(R.id.recordList);
        dataArray=new ArrayList<RecordInfo>();
        dataAdapter=new RecordInfoAdapter(this,dataArray);
        recordList.setAdapter(dataAdapter);
        recordList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final RecordInfo item = (RecordInfo) dataAdapter.getItem(position);
                Intent intent = new Intent(ResultsListActivity.this, ViewRecordActivity.class);
                intent.putExtra(ApplicationConstants.APP_BUNDLE_RECORD, item);
                startActivity(intent);
                overridePendingTransition(R.anim.in_push_right_to_left, R.anim.push_down);
            }
        });
        ApplicationController.saveCurrentTaskAndOrganID(Integer.toString(taskInfo.getJR_ID()),"0");
        ArrayList<RecordInfo> cardList=RecordInfo.getRecordsFromDBByOrganID(ApplicationController.getCurrentTaskID(),ApplicationController.getCurrentOrganID());
        //if(cardList.size()!=dataArray.size())
        {
            dataArray.clear();
            Iterator<RecordInfo> it = cardList.iterator();
            while(it.hasNext())
            {
                RecordInfo item = it.next();
                dataArray.add(item);
            }
            dataAdapter.refillData(dataArray);
        }
        showMessage("上传方法","请在手机连接3G或Wifi后，点击右上角上传图标上传检查结果");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_result_upload, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_task_upload) {
            uploadResults();
            return true;
        }
        if(id==android.R.id.home)
        {
            quitActivity();
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

    private void uploadResults()
    {
        ArrayList<JCDWInfo> organList= JCDWInfo.getJCDWByJR(Integer.toString(taskInfo.getJR_ID()));
        imagePathList.clear();
        uploadImageIndex=0;
        AppLog.d("Start Uploading");
        StringWriter xmlWriter = new StringWriter();
        try {
            // 创建XmlSerializer
            XmlPullParserFactory pullParserFactory = XmlPullParserFactory.newInstance();
            XmlSerializer xmlSerializer = pullParserFactory.newSerializer();

            xmlSerializer.setOutput(xmlWriter);

            // 写xml文件
            // <?xml version='1.0' encoding='UTF-8' standlone='yes' ?>
            xmlSerializer.startDocument("UTF-8", true);
            // <result jr_id='25'>
            xmlSerializer.startTag("", "result");
            xmlSerializer.attribute("", "jr_id", String.valueOf(taskInfo.getJR_ID()));
            xmlSerializer.attribute("", "jr_mc", taskInfo.getJR_MC());
            xmlSerializer.attribute("", "jr_kssj", taskInfo.getJR_KSSJ());
            xmlSerializer.attribute("", "jr_jssj", taskInfo.getJR_JSSJ());
            //<record>
            for(int i=0;i<dataArray.size();i++) {
                RecordInfo item=dataArray.get(i);
                xmlSerializer.startTag("", "record");

                xmlSerializer.startTag("", "jcdw");
                xmlSerializer.text(item.getOrganID());
                xmlSerializer.endTag("", "jcdw");

                String jcdw_mc="";
                for(int oi=0;oi<organList.size();oi++)
                {
                    int jcdw_id=organList.get(oi).getJR_DEPTID();
                    String jcdw_str_id=Integer.toString(jcdw_id);
                    if(jcdw_str_id.equals(item.getOrganID()))
                    {
                        jcdw_mc=organList.get(oi).getJR_DWMC();
                        break;
                    }
                }

                xmlSerializer.startTag("", "jcdw_mc");
                xmlSerializer.text(jcdw_mc);
                xmlSerializer.endTag("", "jcdw_mc");

                xmlSerializer.startTag("", "jcsj");
                xmlSerializer.text(item.getCreated());
                xmlSerializer.endTag("", "jcsj");

                xmlSerializer.startTag("", "jck_yf");
                xmlSerializer.text(item.getRuleLv1());
                xmlSerializer.endTag("", "jck_yf");

                xmlSerializer.startTag("", "jck_ef");
                xmlSerializer.text(item.getRuleLv2());
                xmlSerializer.endTag("", "jck_ef");

                xmlSerializer.startTag("", "jck_fl");
                xmlSerializer.text(item.getRuleLv3());
                xmlSerializer.endTag("", "jck_fl");

                xmlSerializer.startTag("", "jck_nr");
                xmlSerializer.text(item.getRuleContent());
                xmlSerializer.endTag("", "jck_nr");

                xmlSerializer.startTag("", "yh_nr");
                xmlSerializer.text(item.getDescription());
                xmlSerializer.endTag("", "yh_nr");

                xmlSerializer.startTag("", "yh_lxr");
                xmlSerializer.text(item.getContact());
                xmlSerializer.endTag("", "yh_lxr");

                xmlSerializer.startTag("", "yh_lxr_dh");
                xmlSerializer.text(item.getPhone());
                xmlSerializer.endTag("", "yh_lxr_dh");

                ArrayList<String> imgList=item.getImagePathList();
                String imgString="";
                for(int k=0;k<imgList.size();k++)
                {
                    imgString=imgString+imgList.get(k)+";";
                    imagePathList.add(imgList.get(k));
                }
                xmlSerializer.startTag("", "yh_pics");
                xmlSerializer.text(imgString);
                xmlSerializer.endTag("", "yh_pics");

                xmlSerializer.endTag("", "record");
            }

            xmlSerializer.endTag("", "result");

            xmlSerializer.endDocument();
        }catch (Exception e)
        {
                AppLog.e("XML error:"+e.toString());
        }
        String xmlContent=xmlWriter.toString();
        AppLog.i(xmlContent);
        String xmlFileName=Integer.toString(taskInfo.getJR_ID())+"_"+ApplicationController.getDeviceInfo().getDeviceIMEI()+".xml";
        uploadXMLData(xmlFileName,xmlContent);
    }
    int uploadImageIndex=0;

    private void uploadNextPic()
    {
        if(uploadImageIndex<imagePathList.size()) {
            uploadPicToServer();
        }
        else
        {
            showMessage("检查结果","检查结果上传完毕");
        }
    }
    private void uploadPicToServer()
    {
         new HttpMultipartPost(this,uploadImageIndex).execute("");
    }

    private void showMessage(String title,String message)
    {
        AlertDialog dialog= new AlertDialog.Builder(this).setNeutralButton("确定", null).create();
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.show();
    }
    private void uploadXMLData(String filename,String xmldata)
    {

        RequestQueue mRequestQueue= VolleySingleton.getInstance().getRequestQueue();
        final ProgressDialog progressDialog = ProgressDialog.show(this, "检查结果", "正在上传检查结果...");
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

                            String msg=response.optString("message");

                            Toast.makeText(ResultsListActivity.this, msg, Toast.LENGTH_SHORT).show();
                            uploadNextPic();
                        }
                        else if(result==-1)
                        {
                            String msg=response.optString("message");

                            Toast.makeText(ResultsListActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ResultsListActivity.this,"连接服务器失败,请检查网络状态",Toast.LENGTH_SHORT).show();
            }
        };


        HashMap<String,String> params=new HashMap<String, String>();
        params.put("filename",filename);
        params.put("xmldata",xmldata);

        String baseURL="http://"+ApplicationController.getServerIP();
        String urlString=baseURL;
        Uri.Builder builder = new Uri.Builder();
        try {
            URL url = new URL(baseURL);

            builder = new Uri.Builder()
                    .scheme(url.getProtocol())
                    .encodedAuthority(url.getAuthority())
                    .appendPath("api")
                    .appendPath("upload_xml.php");
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
    class HttpMultipartPost extends AsyncTask<String, Integer, Long> {
        String url = "http://218.28.88.188:8080/api/upload_img.php";
        Context context;
        int imageIndex;
        HttpClient client;
        HttpPost post;
        MultipartEntity mpEntity;
        final ProgressDialog progressDialog = ProgressDialog.show(ResultsListActivity.this, "检查图片", "正在上传检查图片...");
        public HttpMultipartPost(Context context,int imageIndex)
        {
            this.context=context;
            this.imageIndex=imageIndex;
            client = new DefaultHttpClient();
            post = new HttpPost(url);
            mpEntity=new MultipartEntity();
            progressDialog.show();
        }
        protected Long doInBackground(String... params) {
            File file = ApplicationController.getFile(DIR_RECORD_PREVIEW, imagePathList.get(this.imageIndex));
            //ContentBody cbFile = new FileBody(file, "image/jpeg");

            //Add the data to the multipart entity
            if (file != null && file.exists()) {
                mpEntity.addPart("file", new FileBody(file));
            }

            post.setEntity(mpEntity);
            //Execute the post request
            HttpResponse response1=null;
            try{
                response1=client.execute(post);
            }catch (Exception netE)
            {
                AppLog.e(netE.toString());
            }
            //Get the response from the server
            if(response1!=null) {
                HttpEntity resEntity = response1.getEntity();
                String Response = "";
                try {
                    Response = EntityUtils.toString(resEntity);
                } catch (Exception e) {
                    AppLog.e(e.toString());
                }
                AppLog.d("Response:" + Response);
            }
            client.getConnectionManager().shutdown();
            return file.getTotalSpace();
        }

        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute(Long result) {

            if(progressDialog!=null)
            {
                progressDialog.dismiss();
            }
            uploadImageIndex++;
            uploadNextPic();

        }
    }
}
