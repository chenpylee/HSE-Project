package com.ocse.hse.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ocse.hse.Models.YWWDInfo;
import com.ocse.hse.R;
import com.ocse.hse.app.AppLog;
import com.ocse.hse.app.ApplicationConstants;
import com.ocse.hse.app.ApplicationController;
import com.ocse.hse.app.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CBSCardActivity extends Activity {

    ActionBar actionBar;
    JSONObject contentJsonObject;
    TextView txtCBSMC,txtSGDMC,txtSGFW,txtFBDW,txtSGQY;//施工队信息
    String strCBSMC,strSGDMC,strSGDBH,strSGFW,strFBDW,strSGQY;

    TextView txtStatusGSZZ,txtGSZZBH,txtDQSJ;//工商执照
    String strStatusGSZZ,strGSZZBH,strDQSJ;

    TextView txtStatusHSEZGZ,txtHSEBH,txtLZSJ,txtHZSJ;//承包商HSE资格确认证
    String strStatusHSEZGZ,strHSEBH,strLZSJ,strHZSJ;

    TextView txtStatusZRZ,txtZSBH,txtFZJG,txtDJ,txtSGYWFW,txtZSDQSJ;//华北市场准入证
    String strStatusZRZ,strZSBH,strFZJG,strDJ,strSGYWFW,strZSDQSJ;
    String strYWID;
    //证件图片
    Button btnShowGallery;
    int cbsYWID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cbscard);
        actionBar=getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setIcon(R.drawable.icon_hse_actionbar);
        actionBar.setTitle("承包商HSE资格确认证");
        btnShowGallery=(Button)findViewById(R.id.btnShowGallery);
        btnShowGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //goCertificateListActivity();
                //cbsYWID=3131;
                getCertsListFromServer(cbsYWID);
            }
        });
        initContents();
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null)
        {
            String contentJson=bundle.getString(ApplicationConstants.APP_BUNDLE_CARD_INFO_JSON_KEY);
            AppLog.i("contentJson:" + contentJson);
            if(contentJson.length()>1) {
                try {
                    contentJsonObject = new JSONObject(contentJson);
                    if(contentJsonObject!=null)
                    {
                        parseContents(contentJsonObject);
                    }
                }
                catch (JSONException jsonException)
                {
                    Toast.makeText(this, jsonException.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cbscard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_cbs_about) {
            if(contentJsonObject!=null) {
                Intent detailIntent = new Intent(CBSCardActivity.this, CBSDetailActivity.class);
                detailIntent.putExtra(ApplicationConstants.APP_BUNDLE_CARD_INFO_JSON_KEY, contentJsonObject.toString());
                startActivity(detailIntent);
                overridePendingTransition(R.anim.in_push_right_to_left, R.anim.push_down);
            }
            return true;
        }
        if (id==android.R.id.home){
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

    private void initContents() {
        //施工队信息
        txtCBSMC = (TextView) findViewById(R.id.txtCBSMC);
        txtSGDMC = (TextView) findViewById(R.id.txtSGDMC);
        txtSGFW = (TextView) findViewById(R.id.txtSGFW);
        txtFBDW = (TextView) findViewById(R.id.txtFBDW);
        txtSGQY = (TextView) findViewById(R.id.txtSGQY);
        //工商执照
        txtStatusGSZZ = (TextView) findViewById(R.id.txtStatusGSZZ);
        txtGSZZBH = (TextView) findViewById(R.id.txtGSZZBH);
        txtDQSJ = (TextView) findViewById(R.id.txtDQSJ);
        //HSE证书
        txtStatusHSEZGZ = (TextView) findViewById(R.id.txtStatusHSEZGZ);
        txtHSEBH = (TextView) findViewById(R.id.txtHSEBH);
        txtLZSJ = (TextView) findViewById(R.id.txtLZSJ);
        txtHZSJ = (TextView) findViewById(R.id.txtHZSJ);
        //市场准入证
        txtStatusZRZ = (TextView) findViewById(R.id.txtStatusZRZ);
        txtZSBH = (TextView) findViewById(R.id.txtZSBH);
        txtFZJG = (TextView) findViewById(R.id.txtFZJG);
        txtDJ = (TextView) findViewById(R.id.txtDJ);
        txtSGYWFW = (TextView) findViewById(R.id.txtSGYWFW);
        txtZSDQSJ = (TextView) findViewById(R.id.txtZSDQSJ);


        //施工队信息
        strCBSMC="";
        strSGDMC="";
        strSGDBH="";
        strSGFW="";
        strFBDW="";
        strSGQY="";
        //工商执照
        strStatusGSZZ="";
        strGSZZBH="";
        strDQSJ="";
        //HSE证书
        strStatusHSEZGZ="";
        strHSEBH="";
        strLZSJ="";
        strHZSJ="";
        //华北市场准入证
        strStatusZRZ="";
        strZSBH="";
        strFZJG="";
        strDJ="";
        strSGYWFW="";
        strZSDQSJ="";
        strYWID="";
        fillContents();
    }
    private void fillContents()
    {
        //施工队信息
        txtCBSMC.setText(strCBSMC);
        txtSGDMC.setText(strSGDMC);
        txtSGFW.setText(strSGFW);
        txtFBDW.setText(strFBDW);
        txtSGQY.setText(strSGQY);
        //工商执照
        txtStatusGSZZ.setText(strStatusGSZZ);
        txtGSZZBH.setText(strGSZZBH);
        txtDQSJ.setText(strDQSJ);
        //HSE证书
        txtStatusHSEZGZ.setText(strStatusHSEZGZ);
        txtHSEBH.setText(strHSEBH);
        txtLZSJ.setText(strLZSJ);
        txtHZSJ.setText(strHZSJ);
        //市场准入证
        txtStatusZRZ.setText(strStatusZRZ);
        txtZSBH.setText(strZSBH);
        txtFZJG.setText(strFZJG);
        txtDJ.setText(strDJ);
        txtSGYWFW.setText(strSGYWFW);
        txtZSDQSJ.setText(strZSDQSJ);
        if(strYWID.length()>0)
        {
            btnShowGallery.setVisibility(View.VISIBLE);
        }
        else
        {
            btnShowGallery.setVisibility(View.GONE);
        }
    }
    private void validateCards()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View alertView=inflater.inflate(R.layout.alert_cbs_validation, null);
        TextView txtAlertStatusGSZZ=(TextView)alertView.findViewById(R.id.txtAlertStatusGSZZ);
        TextView txtAlertStatusHSEQRZ=(TextView)alertView.findViewById(R.id.txtAlertStatusHSEQRZ);
        TextView txtAlertStatusZRZZ=(TextView)alertView.findViewById(R.id.txtAlertStatusZRZZ);
        String strAlertStatusGSZZ="";
        String strAlertStatusHSEQRZ="";
        String strAlertStatusZRZZ="";
        Date nowDate=new Date();
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        //内部准驾证
        if(strDQSJ.length()>5) {
            try {
                Date hzsj = dateFormat.parse(strDQSJ);
                int result=hzsj.compareTo(nowDate);
                //计算过期时间-差额天数
                Calendar cal = Calendar.getInstance();
                cal.setTime(hzsj);
                long time1 = cal.getTimeInMillis();
                cal.setTime(nowDate);
                long time2 = cal.getTimeInMillis();
                long between_days=(time2-time1)/(1000*3600*24);
                String str_between_days=Long.toString(between_days);

                if(result<0)//证件已经过期
                {
                    strStatusGSZZ="（已过期"+str_between_days+"天）";
                    txtStatusGSZZ.setTextColor(getResources().getColor(R.color.card_info_invalid_text_color));
                    strAlertStatusGSZZ="已过期"+str_between_days+"天";
                    txtAlertStatusGSZZ.setTextColor(getResources().getColor(R.color.card_info_invalid_text_color));
                }
                else
                {
                    strStatusGSZZ="（有效期内）";
                    txtStatusGSZZ.setTextColor(getResources().getColor(R.color.card_info_valid_text_color));
                    strAlertStatusGSZZ="有效期内";
                    txtAlertStatusGSZZ.setTextColor(getResources().getColor(R.color.card_info_valid_text_color));
                }
            }catch (ParseException parseException)
            {
                //
            }
        }
        else
        {
            strStatusGSZZ="";
            strAlertStatusGSZZ="未注明有效时间";
        }
        txtStatusGSZZ.setText(strStatusGSZZ);
        txtAlertStatusGSZZ.setText(strAlertStatusGSZZ);

        //HSE证件
        if(strLZSJ.length()>5) {
            try {
                Calendar c = Calendar.getInstance();
                c.setTime(dateFormat.parse(strLZSJ));
                c.add(Calendar.YEAR,1);
                c.add(Calendar.DATE,-1);
                Date hzrq=new Date(c.getTimeInMillis());
                txtHZSJ.setText(dateFormat.format(hzrq));
                int result=hzrq.compareTo(nowDate);
                //计算过期时间-差额天数
                Calendar cal = Calendar.getInstance();
                cal.setTime(hzrq);
                long time1 = cal.getTimeInMillis();
                cal.setTime(nowDate);
                long time2 = cal.getTimeInMillis();
                long between_days=(time2-time1)/(1000*3600*24);
                String str_between_days=Long.toString(between_days);

                if(result<0)//证件已经过期
                {
                    strStatusHSEZGZ="（已过期"+str_between_days+"天）";
                    txtAlertStatusHSEQRZ.setTextColor(getResources().getColor(R.color.card_info_invalid_text_color));
                    strAlertStatusHSEQRZ="已过期"+str_between_days+"天";
                    txtAlertStatusHSEQRZ.setTextColor(getResources().getColor(R.color.card_info_invalid_text_color));
                }
                else
                {
                    strStatusHSEZGZ="（有效期内）";
                    txtAlertStatusHSEQRZ.setTextColor(getResources().getColor(R.color.card_info_valid_text_color));
                    strAlertStatusHSEQRZ="有效期内";
                    txtAlertStatusHSEQRZ.setTextColor(getResources().getColor(R.color.card_info_valid_text_color));
                }
            }catch (ParseException parseException)
            {
                //
            }
        }
        else
        {
            strStatusHSEZGZ="";
            strAlertStatusHSEQRZ="未注明有效时间";
        }
        txtStatusHSEZGZ.setText(strStatusHSEZGZ);
        txtAlertStatusHSEQRZ.setText(strAlertStatusHSEQRZ);

        if(strZSDQSJ.length()>5) {
            try {
                Date hzrq = dateFormat.parse(strZSDQSJ);
                int result=hzrq.compareTo(nowDate);
                Calendar cal = Calendar.getInstance();
                cal.setTime(hzrq);
                long time1 = cal.getTimeInMillis();
                cal.setTime(nowDate);
                long time2 = cal.getTimeInMillis();
                long between_days=(time2-time1)/(1000*3600*24);
                String str_between_days=Long.toString(between_days);

                if(result<0)//证件已经过期
                {
                    strStatusZRZ="（已过期"+str_between_days+"天）";
                    txtStatusZRZ.setTextColor(getResources().getColor(R.color.card_info_invalid_text_color));
                    strAlertStatusZRZZ="已过期"+str_between_days+"天";
                    txtAlertStatusZRZZ.setTextColor(getResources().getColor(R.color.card_info_invalid_text_color));
                }
                else
                {
                    strStatusZRZ="（有效期内）";
                    txtStatusZRZ.setTextColor(getResources().getColor(R.color.card_info_valid_text_color));
                    strAlertStatusZRZZ="有效期内";
                    txtAlertStatusZRZZ.setTextColor(getResources().getColor(R.color.card_info_valid_text_color));
                }
            }catch (ParseException parseException)
            {
                //
            }
        }
        else
        {
            strStatusZRZ="";
            strAlertStatusZRZZ="未注明有效时间";
        }
        txtStatusZRZ.setText(strStatusZRZ);
        txtAlertStatusZRZZ.setText(strAlertStatusZRZZ);

        builder.setView(alertView);
        builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();

    }
    private void parseContents(JSONObject object)
    {
        try{
            JSONObject basicInfo=object.getJSONObject("basic");
            strLZSJ=basicInfo.optString("CARD_DATE","");
        }catch (JSONException basicJsonException)
        {
            Toast.makeText(this,basicJsonException.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }
        try {

            JSONObject teamInfo = object.getJSONObject("teamInfo");
            if(teamInfo!=null) {
                //施工队信息
                strCBSMC = teamInfo.optString("CBSMC", "");
                strSGDMC = teamInfo.optString("SGDMC", "");
                strSGDBH = teamInfo.optString("SGDBH", "");
                strSGFW = teamInfo.optString("SGFW", "");
                strFBDW = teamInfo.optString("FBDW", "");
                strSGQY = teamInfo.optString("SGQY", "");
                //工商执照
                strGSZZBH=teamInfo.optString("GSZZBH","");
                strDQSJ=teamInfo.optString("DQSJ","");
                //HSE证件
                strHSEBH=teamInfo.optString("BH","");//数据库中没有NCC-LSZ-00302格式的HSE编号
                strHZSJ=teamInfo.optString("HZSJ","");//数据库中没有CBS卡换证时间
                //华北市场准入证
                strZSBH=teamInfo.optString("ZSBH","");
                strDJ=teamInfo.optString("DJ","");
                strFZJG=teamInfo.optString("FZJG","");
                strSGYWFW=strSGFW;
                strZSDQSJ=teamInfo.optString("ZSDQSJ","");
                cbsYWID=teamInfo.optInt("YWID",0);
                strYWID=Integer.toString(cbsYWID);
            }
            fillContents();
            validateCards();
        }catch (JSONException jsonException)
        {
            Toast.makeText(this,jsonException.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }
    }
    private void goCertificateListActivity()
    {

        Intent detailIntent = new Intent(CBSCardActivity.this, CertificateListActivity.class);
        //detailIntent.putExtra(ApplicationConstants.APP_BUNDLE_CARD_INFO_JSON_KEY, contentJsonObject.toString());
        detailIntent.putExtra(ApplicationConstants.APP_BUNDLE_CBS_YWID_KEY, cbsYWID);
        startActivity(detailIntent);
        overridePendingTransition(R.anim.in_push_right_to_left, R.anim.push_down);


    }

    private void getCertsListFromServer(final int YWID)
    {
        RequestQueue mRequestQueue= VolleySingleton.getInstance().getRequestQueue();
        final ProgressDialog progressDialog = ProgressDialog.show(this, "HSE服务器", "正在获取证件照片...");
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
                            ArrayList<YWWDInfo> wdList=new ArrayList<YWWDInfo>();
                            JSONObject content=response.getJSONObject(ApplicationConstants.JSON_CONTENT);
                            JSONArray certArray=content.getJSONArray("certs_list");
                            if(certArray.length()>0)
                            {
                                YWWDInfo.clearYWWDInDB(YWID);
                            }
                            for (int i=0; i<certArray.length(); i++) {
                                JSONObject item = certArray.getJSONObject(i);
                                String ZLLB = "", WDMC = "", WDLJ = "";

                                if (!item.isNull("ZLLB")) {
                                    ZLLB = item.optString("ZLLB", "");
                                }
                                if (!item.isNull("WDMC")) {
                                    WDMC = item.optString("WDMC", "");
                                }
                                if (!item.isNull("WDLJ")) {
                                    WDLJ = item.optString("WDLJ", "");
                                }
                                AppLog.d("YWID:"+YWID+" ZLLB:"+ZLLB+" WDMC:"+WDMC+" WDLJ:"+WDLJ);
                                if(YWWDInfo.isPic(WDLJ)) {
                                    YWWDInfo ywwd = new YWWDInfo(YWID, ZLLB, WDMC, WDLJ);
                                    ywwd.updateJCDWInDB();
                                    wdList.add(ywwd);
                                }
                            }
                            //JcrwInfo.getAllJcrwInDB();
                            if(wdList.size()>0)
                            {
                                //Go to Cert Type List
                                goCertificateListActivity();
                            }
                            else
                            {
                                //POP UP Alert
                                showMessage("承包商证件图片","暂无此承包商证件图片资料");
                            }
                        }
                        else if(result==-1)
                        {
                            String msg=response.optString("message");
                            //Toast.makeText(CBSCardActivity.this, msg, Toast.LENGTH_SHORT).show();
                            showMessage("承包商证件图片","暂无此承包商证件图片资料");
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
                Toast.makeText(CBSCardActivity.this,"连接服务器失败",Toast.LENGTH_SHORT).show();
            }
        };


        HashMap<String,String> params=new HashMap<String, String>();
        params.put("ywid",Integer.toString(YWID));

        String baseURL="http://"+ApplicationController.getServerIP();
        String urlString=baseURL;
        Uri.Builder builder = new Uri.Builder();
        try {
            URL url = new URL(baseURL);

            builder = new Uri.Builder()
                    .scheme(url.getProtocol())
                    .encodedAuthority(url.getAuthority())
                    .appendPath("api")
                    .appendPath("get_cbs_pics.php");
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

    private void showMessage(String title,String message)
    {
        AlertDialog dialog= new AlertDialog.Builder(this).setNeutralButton("确定", null).create();
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.show();
    }
}
