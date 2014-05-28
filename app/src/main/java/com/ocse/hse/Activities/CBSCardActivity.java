package com.ocse.hse.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ocse.hse.R;
import com.ocse.hse.app.AppLog;
import com.ocse.hse.app.ApplicationConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
                if(result<0)//证件已经过期
                {
                    strStatusGSZZ="（已过期）";
                    txtStatusGSZZ.setTextColor(getResources().getColor(R.color.card_info_invalid_text_color));
                    strAlertStatusGSZZ="已过期";
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
                if(result<0)//证件已经过期
                {
                    strStatusHSEZGZ="（已过期）";
                    txtAlertStatusHSEQRZ.setTextColor(getResources().getColor(R.color.card_info_invalid_text_color));
                    strAlertStatusHSEQRZ="已过期";
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
                if(result<0)//证件已经过期
                {
                    strStatusZRZ="（已过期）";
                    txtStatusZRZ.setTextColor(getResources().getColor(R.color.card_info_invalid_text_color));
                    strAlertStatusZRZZ="已过期";
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
            }
            fillContents();
            validateCards();
        }catch (JSONException jsonException)
        {
            Toast.makeText(this,jsonException.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }
    }
}
