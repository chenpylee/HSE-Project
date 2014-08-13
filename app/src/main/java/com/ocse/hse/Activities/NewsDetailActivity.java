package com.ocse.hse.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.LruCache;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.ocse.hse.Models.NewsInfo;
import com.ocse.hse.R;
import com.ocse.hse.app.AppLog;
import com.ocse.hse.app.ApplicationConstants;
import com.ocse.hse.app.ApplicationController;
import com.ocse.hse.app.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class NewsDetailActivity extends Activity {

    NewsInfo newsInfo;
    ActionBar actionBar;
    String newsID;
    Boolean refreshEnabled;
    TextView txtNewsUpdatetime;
    WebView webview;
    NetworkImageView imageview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        actionBar=getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setIcon(R.drawable.icon_hse_actionbar);
        actionBar.setTitle("新闻");
        Bundle bundle=getIntent().getExtras();
        newsInfo=null;
        newsID="";
        if(bundle!=null)
        {
            newsInfo=(NewsInfo)bundle.get(ApplicationConstants.APP_BUNDLE_NEWS_INFO);
            newsID=newsInfo.getID();
        }

        if(newsInfo!=null)
        {
            actionBar.setTitle(newsInfo.getTitle());
        }
        refreshEnabled=true;
        initContent();
        loadDetail();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.news_detail, menu);
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
        if(id==R.id.action_task_refresh)
        {
            if(refreshEnabled) {
                loadDetail();
            }
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
    private void initContent()
    {
        txtNewsUpdatetime=(TextView)findViewById(R.id.txtNewsUpdatetime);
        if(newsInfo!=null)
        {
            txtNewsUpdatetime.setText(newsInfo.getUpdatetime());
        }

        webview=(WebView)findViewById(R.id.webview);
        imageview=(NetworkImageView)findViewById(R.id.imageview);
        webview.setVisibility(View.GONE);
        imageview.setVisibility(View.GONE);
        //webview.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
    }
    private void fillContent(String content)
    {
        if(newsInfo.isImage())
        {
            showImageByNetworkImageView(newsInfo.getImageUrl());
            imageview.setVisibility(View.VISIBLE);
        }
        else {
            webview.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
            webview.setVisibility(View.VISIBLE);
        }
    }
    private void loadDetail()
    {
        if(newsInfo==null)
            return;
        if(newsInfo.isImage())
            fillContent("");
        refreshEnabled=false;
        RequestQueue mRequestQueue= VolleySingleton.getInstance().getRequestQueue();
        final ProgressDialog progressDialog = ProgressDialog.show(this, "HSE新闻", "加载中...");
        Response.Listener<JSONObject> listener=new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                refreshEnabled=true;
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
                            JSONObject item=response.optJSONObject("content");
                            String NR="";

                            if(!item.isNull("NR"))
                            {
                                NR=item.optString("NR", "");
                                NR="<h1>"+newsInfo.getTitle()+"</h1>"+NR;
                            }

                            fillContent(NR);
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
                refreshEnabled=true;
                //Toast.makeText(NewsListActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        };


        HashMap<String,String> params=new HashMap<String, String>();
        params.put("index", newsID);

        String baseURL="http://"+ ApplicationController.getServerIP();
        String urlString=baseURL;
        Uri.Builder builder = new Uri.Builder();
        try {
            URL url = new URL(baseURL);
            builder = new Uri.Builder()
                    .scheme(url.getProtocol())
                    .encodedAuthority(url.getAuthority())
                    .appendEncodedPath(ApplicationConstants.APP_URL_API)
                    .appendEncodedPath(ApplicationConstants.APP_URL_NEWS_DETAIL);
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
            AppLog.d("star loading news detail");
            mRequestQueue.start();
        }
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
        if(imageview!=null) {
            imageview.setTag("url");
            imageview.setImageUrl(imageUrl, imageLoader);
            imageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent detailIntent=new Intent(NewsDetailActivity.this,PhotoDetailActivity.class);
                    detailIntent.putExtra(ApplicationConstants.APP_BUNDLE_PHOTO_URL_KEY,imageUrl);
                    detailIntent.putExtra(ApplicationConstants.APP_BUNDLE_ACTION_BAR_TITLE_KEY,newsInfo.getTitle());
                    startActivity(detailIntent);
                    overridePendingTransition(R.anim.in_push_right_to_left,R.anim.push_down);
                }
            });
        }
    }
}
