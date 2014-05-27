package com.ocse.hse.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.LruCache;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.ocse.hse.R;
import com.ocse.hse.app.AppLog;
import com.ocse.hse.app.ApplicationConstants;
import com.ocse.hse.app.ApplicationController;
import com.ocse.hse.app.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JSYCardActivity extends Activity {

    ActionBar actionBar;
    JSONObject contentJsonObject;
    NetworkImageView imgRYZP;
    TextView txtXM,txtXB,txtSFZH;
    String img_person_url,strXM,strXB,strSFZH;

    TextView txtGZDW,txtGZLB,txtGZ;
    String strGZDW,strGZLB,strGZ;

    TextView txtStatusNBZJZ,txtZJLB,txtBH,txtZJCX,txtLZSJ,txtHZSJ;
    String strStatusNBZJZ,strZJLB,strBH,strZJCX,strLZSJ,strHZSJ;

    //驾驶证
    TextView txtStatusJSZ,txtJSZH,txtJSZDAH,txtJSZJCX,txtYXQSRQ,txtHZRQ;
    String strStatusJSZ,strJSZH,strJSZDAH,strJSZJCX,strYXQSRQ,strHZRQ;
    //从业资格证
    TextView txtStatusCYZGZ,txtCYZGZHM,txtCYZGLB,txtXZJFZSJ,txtXCZJYXQ;
    String strStatusCYZGZ,strCYZGZHM,strCYZGLB,strXZJFZSJ,strXCZJYXQ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jsycard);
        actionBar=getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setIcon(R.drawable.icon_hse_actionbar);
        actionBar.setTitle("机动车准驾证");
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
                    Toast.makeText(this,jsonException.getMessage().toString(),Toast.LENGTH_SHORT).show();
                }
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.jsycard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_person_about) {
            if(contentJsonObject!=null) {
                Intent detailIntent = new Intent(JSYCardActivity.this, JSYDetailActivity.class);
                detailIntent.putExtra(ApplicationConstants.APP_BUNDLE_CARD_INFO_JSON_KEY, contentJsonObject.toString());
                startActivity(detailIntent);
                overridePendingTransition(R.anim.in_push_right_to_left, R.anim.push_down);
            }
            return true;
        }
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
    private void initContents()
    {
        imgRYZP=(NetworkImageView)findViewById(R.id.imgRYZP);
        txtXM=(TextView)findViewById(R.id.txtXM);
        txtXB=(TextView)findViewById(R.id.txtXB);
        txtSFZH=(TextView)findViewById(R.id.txtSFZH);
        txtGZDW=(TextView)findViewById(R.id.txtGZDW);
        txtGZ=(TextView)findViewById(R.id.txtGZ);
        txtGZLB=(TextView)findViewById(R.id.txtGZLB);
        //内部准驾证信息
        txtStatusNBZJZ=(TextView)findViewById(R.id.txtStatusNBZJZ);
        txtZJLB=(TextView)findViewById(R.id.txtZJLB);
        txtBH=(TextView)findViewById(R.id.txtBH);
        txtZJCX=(TextView)findViewById(R.id.txtZJCX);
        txtLZSJ=(TextView)findViewById(R.id.txtLZSJ);
        txtHZSJ=(TextView)findViewById(R.id.txtHZSJ);
        //驾驶证信息
        txtStatusJSZ=(TextView)findViewById(R.id.txtStatusJSZ);
        txtJSZH=(TextView)findViewById(R.id.txtJSZH);
        txtJSZDAH=(TextView)findViewById(R.id.txtJSZDAH);
        txtJSZJCX=(TextView)findViewById(R.id.txtJSZJCX);
        txtYXQSRQ=(TextView)findViewById(R.id.txtYXQSRQ);
        txtHZRQ=(TextView)findViewById(R.id.txtHZRQ);
        //从业资格证信息
        txtStatusCYZGZ=(TextView)findViewById(R.id.txtStatusCYZGZ);
        txtCYZGZHM=(TextView)findViewById(R.id.txtCYZGZHM);
        txtCYZGLB=(TextView)findViewById(R.id.txtCYZGLB);
        txtXZJFZSJ=(TextView)findViewById(R.id.txtXZJFZSJ);
        txtXCZJYXQ=(TextView)findViewById(R.id.txtXCZJYXQ);

        img_person_url="";
        strXM="";
        strXB="";
        strSFZH="";
        strGZDW="";
        strGZ="";
        strGZLB="";
        //内部准驾证信息
        strZJLB="";
        strBH="";
        strZJCX="";
        strLZSJ="";
        strHZSJ="";
        strStatusNBZJZ="";
        //驾驶证信息
        strStatusJSZ="";
        strJSZH="";
        strJSZDAH="";
        strJSZJCX="";
        strYXQSRQ="";
        strHZRQ="";
        //从业资格证信息
        strStatusCYZGZ="";
        strCYZGZHM="";
        strCYZGLB="";
        strXZJFZSJ="";
        strXCZJYXQ="";
        fillContents();
    }
    private void fillContents()
    {
        if(img_person_url.length()>5) {
            //profileAvatarView.setImageDrawable(null);
            if(!img_person_url.startsWith("http")) {
                img_person_url = "http://"+ ApplicationController.getServerIP()+img_person_url;
            }
            AppLog.i("照片："+img_person_url);
            showImageByNetworkImageView(img_person_url);
        }
        txtXM.setText(strXM);
        txtXB.setText(strXB);
        txtSFZH.setText(strSFZH);

        txtGZDW.setText(strGZDW);
        txtGZ.setText(strGZ);
        txtGZLB.setText(strGZLB);

        //内部准驾证信息
        txtStatusNBZJZ.setText(strStatusNBZJZ);
        txtZJLB.setText(strZJLB);
        txtBH.setText(strBH);
        txtZJCX.setText(strZJCX);
        txtLZSJ.setText(strLZSJ);
        txtHZSJ.setText(strHZSJ);
        //驾驶证信息
        txtStatusJSZ.setText(strStatusJSZ);
        txtJSZH.setText(strJSZH);
        txtJSZDAH.setText(strJSZDAH);
        txtJSZJCX.setText(strJSZJCX);
        txtYXQSRQ.setText(strYXQSRQ);
        txtHZRQ.setText(strHZRQ);
        //从业资格证
        txtStatusCYZGZ.setText(strStatusCYZGZ);
        txtCYZGZHM.setText(strCYZGZHM);
        txtCYZGLB.setText(strCYZGLB);
        txtXZJFZSJ.setText(strXZJFZSJ);
        txtXCZJYXQ.setText(strXCZJYXQ);

    }
    private void validateCards()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View alertView=inflater.inflate(R.layout.alert_jsy_validation, null);
        TextView txtAlertStatusNBZJZ=(TextView)alertView.findViewById(R.id.txtAlertStatusNBZJZ);
        TextView txtAlertStatusJSZ=(TextView)alertView.findViewById(R.id.txtAlertStatusJSZ);
        TextView txtAlertStatusCYZGZ=(TextView)alertView.findViewById(R.id.txtAlertStatusCYZGZ);
        String strAlertStatusNBZJZ="";
        String strAlertStatusJSZ="";
        String strAlertStatusCYZGZ="";
        Date nowDate=new Date();
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        //内部准驾证
        if(strHZSJ.length()>5) {
            try {
                Date hzsj = dateFormat.parse(strHZSJ);
                int result=hzsj.compareTo(nowDate);
                if(result<0)//证件已经过期
                {
                    strStatusNBZJZ="（已过期）";
                    txtStatusNBZJZ.setTextColor(getResources().getColor(R.color.card_info_invalid_text_color));
                    strAlertStatusNBZJZ="已过期";
                    txtAlertStatusNBZJZ.setTextColor(getResources().getColor(R.color.card_info_invalid_text_color));
                }
                else
                {
                    strStatusNBZJZ="（有效）";
                    txtStatusNBZJZ.setTextColor(getResources().getColor(R.color.card_info_valid_text_color));
                    strAlertStatusNBZJZ="有效";
                    txtAlertStatusNBZJZ.setTextColor(getResources().getColor(R.color.card_info_valid_text_color));
                }
            }catch (ParseException parseException)
            {
                //
            }
        }
        else
        {
            strStatusNBZJZ="";
            strAlertStatusNBZJZ="无";
        }
        txtStatusNBZJZ.setText(strStatusNBZJZ);
        txtAlertStatusNBZJZ.setText(strAlertStatusNBZJZ);
        //驾驶证
        if(strHZRQ.length()>5) {
            try {
                Date hzrq = dateFormat.parse(strHZRQ);
                int result=hzrq.compareTo(nowDate);
                if(result<0)//证件已经过期
                {
                    strStatusJSZ="（已过期）";
                    txtStatusJSZ.setTextColor(getResources().getColor(R.color.card_info_invalid_text_color));
                    strAlertStatusJSZ="已过期";
                    txtAlertStatusJSZ.setTextColor(getResources().getColor(R.color.card_info_invalid_text_color));
                }
                else
                {
                    strStatusJSZ="（有效）";
                    txtStatusJSZ.setTextColor(getResources().getColor(R.color.card_info_valid_text_color));
                    strAlertStatusJSZ="有效";
                    txtAlertStatusJSZ.setTextColor(getResources().getColor(R.color.card_info_valid_text_color));
                }
            }catch (ParseException parseException)
            {
                //
            }
        }
        else
        {
            strStatusJSZ="";
            strAlertStatusJSZ="无";
        }
        txtStatusJSZ.setText(strStatusJSZ);
        txtAlertStatusJSZ.setText(strAlertStatusJSZ);

        if(strXCZJYXQ.length()>5) {
            try {
                Date hzrq = dateFormat.parse(strXCZJYXQ);
                int result=hzrq.compareTo(nowDate);
                if(result<0)//证件已经过期
                {
                    strStatusCYZGZ="（已过期）";
                    txtStatusCYZGZ.setTextColor(getResources().getColor(R.color.card_info_invalid_text_color));
                    strAlertStatusCYZGZ="已过期";
                    txtAlertStatusCYZGZ.setTextColor(getResources().getColor(R.color.card_info_invalid_text_color));
                }
                else
                {
                    strStatusCYZGZ="（有效）";
                    txtStatusCYZGZ.setTextColor(getResources().getColor(R.color.card_info_valid_text_color));
                    strAlertStatusCYZGZ="有效";
                    txtAlertStatusCYZGZ.setTextColor(getResources().getColor(R.color.card_info_valid_text_color));
                }
            }catch (ParseException parseException)
            {
                //
            }
        }
        else
        {
            strStatusCYZGZ="";
            strAlertStatusCYZGZ="无";
        }
        txtStatusCYZGZ.setText(strStatusCYZGZ);
        txtAlertStatusCYZGZ.setText(strAlertStatusCYZGZ);
        builder.setView(alertView);
        builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();

    }
    private void showImageByNetworkImageView(String imgUrl){
        final String imageUrl=imgUrl;
        if(imageUrl.length()<1)
            return;
        RequestQueue mRequestQueue= VolleySingleton.getInstance().getRequestQueue();
        final LruCache<String, Bitmap> lruCache = new LruCache<String, Bitmap>(20);
        ImageLoader.ImageCache imageCache = new ImageLoader.ImageCache() {
            @Override
            public void putBitmap(String key, Bitmap value) {
                lruCache.put(key, value);
            }

            @Override
            public Bitmap getBitmap(String key) {
                return lruCache.get(key);
            }
        };
        ImageLoader imageLoader = new ImageLoader(mRequestQueue, imageCache);
        if(imgRYZP!=null) {
            imgRYZP.setTag("url");
            imgRYZP.setImageUrl(imageUrl, imageLoader);
            imgRYZP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent detailIntent=new Intent(JSYCardActivity.this,PhotoDetailActivity.class);
                    detailIntent.putExtra(ApplicationConstants.APP_BUNDLE_PHOTO_URL_KEY,imageUrl);
                    startActivity(detailIntent);
                    overridePendingTransition(R.anim.in_push_right_to_left,R.anim.push_down);
                }
            });
        }
    }
    private void parseContents(JSONObject object)
    {
        try {
            JSONObject driverInfo = object.getJSONObject("driverInfo");
            if(driverInfo!=null) {
                strXM = driverInfo.optString("XM", "");
                strXB = "性别：" + driverInfo.optString("XB", "");
                strSFZH = driverInfo.optString("SFZH", "");
                img_person_url = driverInfo.optString("RYZP", "");
                strGZDW = driverInfo.optString("GZDW", "");
                strGZLB = driverInfo.optString("GZLB", "");
                strGZ = driverInfo.optString("GZ", "");
                //内部准驾证信息
                strZJLB=driverInfo.optString("ZJLB", "");
                strBH=driverInfo.optString("BH", "");
                strZJCX=driverInfo.optString("ZJCX", "");
                strLZSJ=driverInfo.optString("LZSJ", "");
                strHZSJ=driverInfo.optString("HZSJ", "");
                //驾驶证信息
                strJSZH=driverInfo.optString("JSZH", "");
                strJSZDAH=driverInfo.optString("JSZDAH", "");
                strJSZJCX=driverInfo.optString("JSZJCX", "");
                strYXQSRQ=driverInfo.optString("YXQSRQ", "");
                strHZRQ=driverInfo.optString("HZRQ", "");
                //从业资格证信息

                strCYZGZHM=driverInfo.optString("CYZGZHM", "");
                strCYZGLB=driverInfo.optString("CYZGLB", "");
                strXZJFZSJ=driverInfo.optString("XZJFZSJ", "");
                strXCZJYXQ=driverInfo.optString("XCZJYXQ", "");
            }
            fillContents();
            validateCards();
        }catch (JSONException jsonException)
        {
            Toast.makeText(this,jsonException.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }
    }
}
