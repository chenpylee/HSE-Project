package com.ocse.hse.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.ocse.hse.R;
import com.ocse.hse.app.AppLog;
import com.ocse.hse.app.ApplicationConstants;

import org.json.JSONException;
import org.json.JSONObject;

public class CBSDetailActivity extends Activity {

    ActionBar actionBar;
    JSONObject contentJsonObject;
    TextView txtCBSMC,txtZCDZ,txtAXZBH,txtFRDB,txtCBSLB,txtQYXZ,txtGSZZBH,txtHSEFZR,txtDH,txtLXR,txtLXRDH;
    String strCBSMC,strZCDZ,strAXZBH,strFRDB,strCBSLB,strQYXZ,strGSZZBH,strHSEFZR,strDH,strLXR,strLXRDH;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cbsdetail);
        actionBar=getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setIcon(R.drawable.icon_hse_actionbar);
        actionBar.setTitle("承包商信息");
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
        getMenuInflater().inflate(R.menu.cbsdetail, menu);
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
        txtCBSMC = (TextView) findViewById(R.id.txtCBSMC);
        txtZCDZ = (TextView) findViewById(R.id.txtZCDZ);
        txtAXZBH= (TextView) findViewById(R.id.txtAXZBH);
        txtFRDB= (TextView) findViewById(R.id.txtFRDB);
        txtCBSLB=(TextView) findViewById(R.id.txtCBSLB);
        txtQYXZ=(TextView) findViewById(R.id.txtQYXZ);

        txtGSZZBH=(TextView) findViewById(R.id.txtGSZZBH);
        txtHSEFZR=(TextView) findViewById(R.id.txtHSEFZR);
        txtDH=(TextView) findViewById(R.id.txtDH);
        txtLXR=(TextView) findViewById(R.id.txtLXR);
        txtLXRDH=(TextView) findViewById(R.id.txtLXRDH);

        strCBSMC="";
        strZCDZ="";
        strAXZBH="";
        strFRDB="";
        strCBSLB="";
        strQYXZ="";
        strGSZZBH="";
        strHSEFZR="";
        strDH="";
        strLXR="";
        strLXRDH="";


        fillContents();
    }
    private void fillContents()
    {
        txtCBSMC.setText(strCBSMC);
        txtZCDZ.setText(strZCDZ);
        txtAXZBH.setText(strAXZBH);
        txtFRDB.setText(strFRDB);
        txtCBSLB.setText(strCBSLB);
        txtQYXZ.setText(strQYXZ);
        txtGSZZBH.setText(strGSZZBH);
        txtHSEFZR.setText(strHSEFZR);
        txtDH.setText(strDH);
        txtLXR.setText(strLXR);
        txtLXRDH.setText(strLXRDH);
    }
    private void parseContents(JSONObject object)
    {
        try{
            JSONObject basicInfo=object.getJSONObject("basic");
        }catch (JSONException basicJsonException)
        {
            Toast.makeText(this,basicJsonException.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }
        try {

            JSONObject teamInfo = object.getJSONObject("teamInfo");
            if(teamInfo!=null) {
                //施工队信息
                strCBSMC = teamInfo.optString("CBSMC", "");
                strZCDZ = teamInfo.optString("ZCDZ", "");
                strAXZBH= teamInfo.optString("AXZBH", "");
                strFRDB=teamInfo.optString("FRDB", "");
                strCBSLB=teamInfo.optString("CBSLB", "");
                strQYXZ=teamInfo.optString("QYXZ", "");
                strGSZZBH=teamInfo.optString("GSZZBH", "");
                strHSEFZR=teamInfo.optString("HSEFZR", "");
                strDH=teamInfo.optString("DH", "");
                strLXR=teamInfo.optString("LXR", "");
                strLXRDH=teamInfo.optString("LXRDH", "");
            }
            fillContents();
        }catch (JSONException jsonException)
        {
            Toast.makeText(this,jsonException.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }
    }
}
