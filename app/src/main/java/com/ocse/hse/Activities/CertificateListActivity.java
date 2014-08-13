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

import java.util.ArrayList;

public class CertificateListActivity extends Activity {

    ActionBar actionBar;
    private ListView listView;
    private ArrayList<String> typeList;
    private CertificateInfoAdapter adapter;
    private int cbsYWID=0;
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
        }
        listView=(ListView)findViewById(R.id.listView);
        typeList=new ArrayList<String>();
        typeList= YWWDInfo.getZLLBFromDB(cbsYWID);
        adapter=new CertificateInfoAdapter("证件类别",this,typeList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position>0) {
                    //showSampleImage(position);
                    showWDList(position);
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

    private void showWDList(int position)
    {
        if(position>0)
        {
            String zllb=typeList.get(position-1);
            AppLog.d("YWID:"+cbsYWID+" zllb:"+zllb);
            Intent detailIntent = new Intent(CertificateListActivity.this, CertificateWDListActivity.class);
            detailIntent.putExtra(ApplicationConstants.APP_BUNDLE_CBS_YWID_KEY, cbsYWID);
            detailIntent.putExtra(ApplicationConstants.APP_BUNDLE_CBS_YWWD_ZLLB, zllb);
            startActivity(detailIntent);
            overridePendingTransition(R.anim.in_push_right_to_left, R.anim.push_down);
        }
    }
}
