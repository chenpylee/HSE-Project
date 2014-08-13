package com.ocse.hse.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ocse.hse.Models.CertificateInfoAdapter;
import com.ocse.hse.Models.YWWDInfo;
import com.ocse.hse.R;
import com.ocse.hse.app.AppLog;
import com.ocse.hse.app.ApplicationConstants;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class CertificateWDListActivity extends Activity {

    ActionBar actionBar;
    private ListView listView;
    private ArrayList<String> typeList;
    private ArrayList<YWWDInfo> wdList;
    private CertificateInfoAdapter adapter;
    private int cbsYWID=0;
    private String zllb="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificate_list);
        actionBar=getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setIcon(R.drawable.icon_hse_actionbar);
        actionBar.setTitle("承包商证件图片");
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null)
        {
            cbsYWID=bundle.getInt(ApplicationConstants.APP_BUNDLE_CBS_YWID_KEY,0);
            zllb=bundle.getString(ApplicationConstants.APP_BUNDLE_CBS_YWWD_ZLLB, "");
        }
        actionBar.setTitle(zllb);
        listView=(ListView)findViewById(R.id.listView);
        typeList=new ArrayList<String>();
        wdList=new ArrayList<YWWDInfo>();
        wdList=YWWDInfo.getYWWDFromDB(cbsYWID,zllb);
        for(int i=0;i<wdList.size();i++)
        {
            typeList.add(wdList.get(i).getWDMC());
        }
        adapter=new CertificateInfoAdapter("证件列表",this,typeList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position>0) {
                    //showSampleImage(position);
                    displayCertImg(position);
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.certificate_list, menu);
        return true;
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

    private void displayCertImg(int index)
    {
        if(index>0)
        {
            YWWDInfo item=wdList.get(index);
            AppLog.d("文档路径:"+item.getWDLJ());
            AppLog.d("文档名称:"+item.getWDMC());
            String wdlj=item.getWDLJ();
            int slash = wdlj.lastIndexOf("/");
            String fileName=wdlj.substring(slash);
            AppLog.d("文档名:"+fileName);
            String imageUrl="http://10.52.1.131/upload/"+fileName;
            try {
                imageUrl = URLEncoder.encode(imageUrl, "UTF-8");
            }catch (UnsupportedEncodingException e)
            {

            }
            imageUrl="http://218.28.88.188:8080/api/get_image.php?url="+imageUrl;
            Intent detailIntent=new Intent(this,PhotoDetailActivity.class);
            detailIntent.putExtra(ApplicationConstants.APP_BUNDLE_PHOTO_URL_KEY,imageUrl);
            detailIntent.putExtra(ApplicationConstants.APP_BUNDLE_ACTION_BAR_TITLE_KEY,item.getWDMC());
            startActivity(detailIntent);
            overridePendingTransition(R.anim.in_push_right_to_left,R.anim.push_down);

        }
    }
}
