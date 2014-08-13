package com.ocse.hse.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.ocse.hse.Models.HistoryAdapter;
import com.ocse.hse.R;

public class SelfCheckHistoryActivity extends Activity {

    ActionBar actionBar;
    ListView listView;
    HistoryAdapter historyAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_check_history);
        actionBar=getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setIcon(R.drawable.icon_hse_actionbar);
        actionBar.setTitle("隐患追踪");
        listView=(ListView)findViewById(R.id.listView);
        historyAdapter=new HistoryAdapter(this);
        listView.setAdapter(historyAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.self_check_history, menu);
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
