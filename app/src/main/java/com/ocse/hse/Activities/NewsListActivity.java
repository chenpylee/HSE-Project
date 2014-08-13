package com.ocse.hse.Activities;

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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ocse.hse.Models.NewsInfo;
import com.ocse.hse.Models.NewsInfoAdapter;
import com.ocse.hse.R;
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

public class NewsListActivity extends Activity {
    ListView listView;
    ActionBar actionBar;
    int startIndex;
    final static int newsLength=20;
    Boolean canLoadMore;
    private ArrayList<NewsInfo> dataArray;
    private NewsInfoAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);
        actionBar=getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setIcon(R.drawable.icon_hse_actionbar);
        actionBar.setTitle("华北局HSE新闻");
        initLoadNews();
        listView=(ListView)findViewById(R.id.listView);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                final int lastItem = firstVisibleItem + visibleItemCount;
                if(lastItem == totalItemCount){
                   //当scroll至最后一行时
                    if(canLoadMore) {
                        loadNews();
                    }
                }
            }
        });
        dataArray=new ArrayList<NewsInfo>();
        adapter=new NewsInfoAdapter(this,dataArray);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsInfo item=(NewsInfo)adapter.getItem(position);
                Intent intent=new Intent(NewsListActivity.this,NewsDetailActivity.class);
                intent.putExtra(ApplicationConstants.APP_BUNDLE_NEWS_INFO,item);
                startActivity(intent);
                overridePendingTransition(R.anim.in_push_right_to_left,R.anim.push_down);

            }
        });
        loadNews();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.news_list, menu);
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
    private void initLoadNews()
    {
        startIndex=0;
        canLoadMore=true;
    }
    private void loadNews()
    {
        int start=startIndex;
        int end=startIndex+newsLength+1;
        canLoadMore=false;
        RequestQueue mRequestQueue= VolleySingleton.getInstance().getRequestQueue();
        final ProgressDialog progressDialog = ProgressDialog.show(this, "HSE新闻", "加载中...");
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
                        if(result==1)
                        {
                            JSONArray listArray=response.optJSONArray("content");
                            ArrayList<NewsInfo> list=new ArrayList<NewsInfo>();
                            if(listArray.length()>0)
                            {
                                canLoadMore=true;
                                startIndex=startIndex+newsLength;
                            }
                            else{
                                canLoadMore=false;
                            }
                            for (int i=0; i<listArray.length(); i++) {
                                JSONObject item = listArray.getJSONObject(i);
                                int OC_XWXXB_ID=0;
                                String BT,SFTPXW, TJSJ, TPLJ, XWLB;
                                int id=0;
                                BT="";
                                SFTPXW="";
                                TJSJ="";
                                TPLJ="";
                                XWLB="";
                                if(!item.isNull("OC_XWXXB_ID"))
                                {
                                    OC_XWXXB_ID=item.optInt("OC_XWXXB_ID", 0);
                                }
                                if(!item.isNull("BT"))
                                {
                                    BT=item.optString("BT", "");
                                }

                                if(!item.isNull("SFTPXW"))
                                {
                                    SFTPXW=item.optString("SFTPXW", "");
                                }
                                if(!item.isNull("TJSJ"))
                                {
                                    TJSJ=item.optString("TJSJ", "");
                                }
                                if(!item.isNull("TPLJ"))
                                {
                                    TPLJ=item.optString("TPLJ", "");
                                }
                                if(!item.isNull("XWLB"))
                                {
                                    XWLB=item.optString("XWLB", "");
                                }

                                NewsInfo info=new NewsInfo( OC_XWXXB_ID, BT, SFTPXW, TJSJ, TPLJ, XWLB);
                                list.add(info);
                            }
                            adapter.addItems(list);

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
                canLoadMore=true;
                //Toast.makeText(NewsListActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        };


        HashMap<String,String> params=new HashMap<String, String>();
        params.put("start", Integer.toString(startIndex));
        params.put("length", Integer.toString(newsLength));

        String baseURL="http://"+ ApplicationController.getServerIP();
        String urlString=baseURL;
        Uri.Builder builder = new Uri.Builder();
        try {
            URL url = new URL(baseURL);
            builder = new Uri.Builder()
                    .scheme(url.getProtocol())
                    .encodedAuthority(url.getAuthority())
                    .appendEncodedPath(ApplicationConstants.APP_URL_API)
                    .appendEncodedPath(ApplicationConstants.APP_URL_NEWS_INFO);
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
            AppLog.d("star loading news");
            mRequestQueue.start();
        }
    }
}
