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

import com.ocse.hse.Models.SelfServiceQueryAdapter;
import com.ocse.hse.R;

public class SelfServiceFunctionListActivity extends Activity {

    ActionBar actionBar;
    ListView serviceListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_service_function_list);
        actionBar=getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setIcon(R.drawable.icon_hse_actionbar);
        actionBar.setTitle("日常安全检查");
        serviceListView=(ListView)findViewById(R.id.serviceListView);
        SelfServiceQueryAdapter serviceAdapter=new SelfServiceQueryAdapter(this);
        serviceListView.setAdapter(serviceAdapter);
        serviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position)
                {
                    case 0:
                    {
                        goSelfCheckActivity();
                        break;
                    }
                    case 1:
                    {
                        goSelfCheckHistory();
                        break;
                    }
                    case 2:
                    {
                        //showCardJSYActivity();
                        break;
                    }

                    default:
                        break;

                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.self_service_function_list, menu);
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
    private void goSelfCheckActivity()
    {
        Intent intent=new Intent(this, SelfCheckActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_push_right_to_left, R.anim.push_down);
    }
    private void goSelfCheckHistory()
    {
        Intent intent=new Intent(this, SelfCheckHistoryActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_push_right_to_left, R.anim.push_down);
    }
}
