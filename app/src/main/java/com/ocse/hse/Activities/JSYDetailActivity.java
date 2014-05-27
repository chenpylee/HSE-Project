package com.ocse.hse.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.LruCache;
import android.view.KeyEvent;
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

public class JSYDetailActivity extends Activity {

    ActionBar actionBar;
    JSONObject contentJsonObject;
    NetworkImageView imgRYZP;
    TextView txtXM,txtXB,txtJG,txtCSRQ,txtPRBDWSJ;
    String img_person_url,strXM,strXB,strJG,strPRBDWSJ,strCSRQ;
    TextView txtYDDH,txtJTDH,txtJTZZ;
    String strYDDH,strJTDH,strJTZZ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jsydetail);
        actionBar=getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setIcon(R.drawable.icon_hse_actionbar);
        actionBar.setTitle("驾驶员个人信息");
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
        getMenuInflater().inflate(R.menu.jsydetail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
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
        txtJG=(TextView)findViewById(R.id.txtJG);
        txtCSRQ=(TextView)findViewById(R.id.txtCSRQ);
        txtPRBDWSJ=(TextView)findViewById(R.id.txtPRBDWSJ);
        txtYDDH=(TextView)findViewById(R.id.txtYDDH);
        txtJTDH=(TextView)findViewById(R.id.txtJTDH);
        txtJTZZ=(TextView)findViewById(R.id.txtJTZZ);

        img_person_url="";
        strXM="";
        strXB="";
        strJG="";
        strCSRQ="";
        strPRBDWSJ="";
        strJTDH="";
        strJTZZ="";
        strJTDH="";
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
        txtJG.setText(strJG);
        txtCSRQ.setText(strCSRQ);
        txtPRBDWSJ.setText(strPRBDWSJ);
        txtYDDH.setText(strYDDH);
        txtJTDH.setText(strJTDH);
        txtJTZZ.setText(strJTZZ);
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
                    Intent detailIntent=new Intent(JSYDetailActivity.this,PhotoDetailActivity.class);
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
                img_person_url = driverInfo.optString("RYZP", "");
                strJG=driverInfo.optString("JG", "");
                strCSRQ=driverInfo.optString("CSRQ", "");
                strPRBDWSJ=driverInfo.optString("PRBDWSJ", "");
                strJTDH=driverInfo.optString("JTDH", "");
                strYDDH=driverInfo.optString("YDDH", "");
                strJTZZ=driverInfo.optString("JTZZ", "");

            }
            fillContents();

        }catch (JSONException jsonException)
        {
            Toast.makeText(this,jsonException.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }
    }
}
