package com.ocse.hse.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ocse.hse.Models.TagAdapter;
import com.ocse.hse.Models.TagBasicInfo;
import com.ocse.hse.R;
import com.ocse.hse.app.AppLog;
import com.ocse.hse.app.ApplicationConstants;
import com.ocse.hse.app.ApplicationController;
import com.ocse.hse.app.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CardCBSActivity extends Activity {

    ActionBar actionBar;
    //Card List
     ListView cardListView;
     ArrayList<TagBasicInfo> dataArray;
     TagAdapter dataAdapter;
    String selectedCardType;
    //NFC Reading
    NfcAdapter mAdapter;
    PendingIntent mPendingIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_cbs);
        actionBar=getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setIcon(R.drawable.icon_hse_actionbar);
        actionBar.setTitle("承包商证件");
        ImageView imgNFCTag=(ImageView)findViewById(R.id.imgNFCTag);
        imgNFCTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTestCBS();
            }
        });

        mAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mAdapter == null) {
            showMessage(R.string.error, R.string.no_nfc);
        }
        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, ((Object) this).getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        processIntent(getIntent());


        ApplicationController.saveCurrentTaskAndOrganID("0","0");
        selectedCardType="CBS";
        cardListView=(ListView)findViewById(R.id.cardList);
        dataArray=new ArrayList<TagBasicInfo>();
        dataAdapter=new TagAdapter(this,dataArray);
        cardListView.setAdapter(dataAdapter);
        cardListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TagBasicInfo item=(TagBasicInfo)dataAdapter.getItem(position);
                if(!item.getCardJson().equals("{}"))
                {
                    String jsonString=item.getCardJson();
                    String type="";
                    try{
                        JSONObject contentObject=new JSONObject(jsonString);
                        type=contentObject.optString("type","");
                    }catch (JSONException jsonException)
                    {

                    }
                    if(type.equals("JSY"))
                    {
                        Intent intent=new Intent(CardCBSActivity.this, JSYCardActivity.class);
                        intent.putExtra(ApplicationConstants.APP_BUNDLE_CARD_INFO_JSON_KEY,jsonString);
                        startActivity(intent);
                        overridePendingTransition(R.anim.in_push_right_to_left, R.anim.push_down);
                    }
                    else if(type.equals("CBS"))
                    {
                        Intent intent=new Intent(CardCBSActivity.this, CBSCardActivity.class);
                        intent.putExtra(ApplicationConstants.APP_BUNDLE_CARD_INFO_JSON_KEY,jsonString);
                        startActivity(intent);
                        overridePendingTransition(R.anim.in_push_right_to_left, R.anim.push_down);
                    }
                }

            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        if (mAdapter != null) {
            if (!mAdapter.isEnabled()) {
                showWirelessSettingsDialog();
            }
            mAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
        }
        selectedCardType="CBS";
        ArrayList<TagBasicInfo> cardList=TagBasicInfo.printAllTagsInDBByTaskAndOrgan(ApplicationController.getCurrentTaskID(),ApplicationController.getCurrentOrganID(),selectedCardType);
        if(cardList.size()!=dataArray.size())
        {
            dataArray.clear();
            Iterator<TagBasicInfo> it = cardList.iterator();
            while(it.hasNext())
            {
                TagBasicInfo item = it.next();
                dataArray.add(item);
            }
            dataAdapter.refillData(dataArray);
            if(dataArray.size()>0) {
                cardListView.smoothScrollToPosition(0);
            }
        }

    }

    public void updateCardList()
    {
        ArrayList<TagBasicInfo> cardList=TagBasicInfo.printAllTagsInDBByTaskAndOrgan(ApplicationController.getCurrentTaskID(),ApplicationController.getCurrentOrganID(),selectedCardType);
        if(cardList.size()!=dataArray.size()&&cardListView!=null)
        {
            dataArray.clear();
            Iterator<TagBasicInfo> it = cardList.iterator();
            while(it.hasNext())
            {
                TagBasicInfo item = it.next();
                dataArray.add(item);
            }
            dataAdapter.refillData(dataArray);
            if(dataArray.size()>0) {
                cardListView.smoothScrollToPosition(0);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAdapter != null) {
            mAdapter.disableForegroundDispatch(this);

        }
    }
    //NFC Card Reading
    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent);//只有setIntent后，才能通过getIntent获得new Intent 否则仍为OnCreate时传入的旧 Intent
        processIntent(intent);
    }
    private void showMessage(int title, int message)
    {
        AlertDialog dialog= new AlertDialog.Builder(this).setNeutralButton("确定", null).create();
        dialog.setTitle(title);
        dialog.setMessage(getText(message));
        dialog.show();
    }
    private void showMessage(String title,String message)
    {
        AlertDialog dialog= new AlertDialog.Builder(this).setNeutralButton("确定", null).create();
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.show();
    }
    //如果本机支持NFC卡，但尚未在设置中打开，则提示用户打开NFC设置
    private void showWirelessSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.nfc_disabled);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.create().show();
        return;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.card_cb, menu);
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

    void processIntent(Intent intent) {
        if(intent==null)
            return;
        if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction()))
        {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            if(tag==null)
                return;
            String tagId=getHex(tag.getId());
            String tagInfo=tag.toString();
            long tagDecId=getDec(tag.getId());
            tagId=Long.toString(tagDecId);
            getCardInformation(tagId);
        }
        //hideSoftKeyboard();

    }
    private String getHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = bytes.length - 1; i >= 0; --i) {
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
            if (i > 0) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    private long getDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = 0; i < bytes.length; ++i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

    private long getReversed(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = bytes.length - 1; i >= 0; --i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }
    //获取卡信息
    private void getCardInformation(final String tagID)
    {
        RequestQueue mRequestQueue= VolleySingleton.getInstance().getRequestQueue();
        final ProgressDialog progressDialog = ProgressDialog.show(this, "HSE证件", "获取卡信息...");
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
                        if(response.isNull("content"))
                        {
                            TagBasicInfo tag = new TagBasicInfo(ApplicationController.getCurrentTaskID(), ApplicationController.getCurrentOrganID(), tagID, "{}");
                            tag.saveToDB();
                        }
                        else {
                            TagBasicInfo tag = new TagBasicInfo(ApplicationController.getCurrentTaskID(), ApplicationController.getCurrentOrganID(), tagID, response.getJSONObject("content").toString());
                            tag.saveToDB();
                        }

                        result=response.getInt(ApplicationConstants.JSON_RESULT);
                        if(result==1) {
                            String msg=response.optString("message");
                            if(!response.isNull("content"))
                            {
                                JSONObject content=response.getJSONObject("content");
                                String type=content.optString("type");
                                if(type.equals("JSY"))
                                {
                                    //fillJSYContent(content);
                                    showMessage("读卡","此卡不是承包商卡");
                                }
                                else if(type.equals("CBS"))
                                {
                                    fillCBSContent(content);
                                }
                            }

                        }
                        else if(result==-1)
                        {
                            String msg=response.optString("message");
                            //Toast.makeText(CardCBSActivity.this, msg, Toast.LENGTH_SHORT).show();
                            fillNoDataContent(tagID);
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
        params.put("tag",tagID);

        String baseURL="http://"+ ApplicationController.getServerIP();
        String urlString=baseURL;
        Uri.Builder builder = new Uri.Builder();
        try {
            URL url = new URL(baseURL);
            builder = new Uri.Builder()
                    .scheme(url.getProtocol())
                    .encodedAuthority(url.getAuthority())
                    .appendEncodedPath(ApplicationConstants.APP_URL_API)
                    .appendEncodedPath(ApplicationConstants.APP_URL_CARD_INFO);
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
    //填充驾驶员、承包商、无数据 内容
    private void fillCBSContent(JSONObject contentObject)
    {
        /**
         containerLayout.removeAllViews();
         View contentView=getLayoutInflater().inflate(R.layout.module_card_team,null);
         containerLayout.addView(contentView);
         AppLog.i("承包商卡");
         **/
        //Toast.makeText(this,"承包商卡",Toast.LENGTH_SHORT).show();

        Intent intent=new Intent(this, CBSCardActivity.class);
        intent.putExtra(ApplicationConstants.APP_BUNDLE_CARD_INFO_JSON_KEY,contentObject.toString());
        startActivity(intent);
        overridePendingTransition(R.anim.in_push_right_to_left,R.anim.push_down);
    }
    private void fillNoDataContent(String tagID)
    {
        /**
         containerLayout.removeAllViews();
         View contentView=getLayoutInflater().inflate(R.layout.module_card_no_data,null);
         containerLayout.addView(contentView);
         AppLog.i("无数据");
         **/
        //Toast.makeText(this,"无数据",Toast.LENGTH_SHORT).show();
        showMessage("无效卡号","在HSE数据库中没有找到卡号("+tagID+")的证件信息。");
        updateCardList();
    }

    private void onTestCBS()
    {
        Toast.makeText(this, "调试模式：调用测试数据", Toast.LENGTH_SHORT).show();
        String tagId="2491433066";//
        getCardInformation(tagId);
    }
}
