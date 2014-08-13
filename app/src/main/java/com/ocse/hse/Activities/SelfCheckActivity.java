package com.ocse.hse.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.ocse.hse.Models.SelfCheckInfo;
import com.ocse.hse.Models.SelfCheckInfoAdapter;
import com.ocse.hse.R;

import java.util.ArrayList;

public class SelfCheckActivity extends Activity {

    ActionBar actionBar;
    ListView checkList;
    ArrayList<SelfCheckInfo> checkHistoryList;
    SelfCheckInfoAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_check);
        actionBar=getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setIcon(R.drawable.icon_hse_actionbar);
        actionBar.setTitle("例行检查");
        checkList=(ListView)findViewById(R.id.checkList);

        checkHistoryList=new ArrayList<SelfCheckInfo>();
        SelfCheckInfo check1=new SelfCheckInfo("第一采油厂",2,"2014-06-27");
        SelfCheckInfo check2=new SelfCheckInfo("第一采油厂",0,"2014-06-17");
        checkHistoryList.add(check1);
        checkHistoryList.add(check2);

        adapter=new SelfCheckInfoAdapter(this,checkHistoryList);
        checkList.setAdapter(adapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.self_check, menu);
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
}
