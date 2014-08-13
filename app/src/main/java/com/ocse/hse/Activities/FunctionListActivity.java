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
import android.widget.Toast;

import com.ocse.hse.DeviceSettingsActivity;
import com.ocse.hse.MainActivity;
import com.ocse.hse.Models.ServiceQueryAdapter;
import com.ocse.hse.R;
import com.ocse.hse.app.ApplicationConstants;

import java.util.Timer;
import java.util.TimerTask;

public class FunctionListActivity extends Activity {

    ListView serviceListView;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function_list);
        actionBar=getActionBar();
        actionBar.setTitle(ApplicationConstants.APP_ACTION_BAR_TITLE);
        actionBar.setIcon(R.drawable.icon_hse_actionbar);
        serviceListView=(ListView)findViewById(R.id.serviceListView);
        ServiceQueryAdapter serviceAdapter=new ServiceQueryAdapter(this);
        serviceListView.setAdapter(serviceAdapter);
        serviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position)
                {
                    case 0:
                    {
                        goMainActivity();
                        break;
                    }
                    case 1:
                    {
                        showCardCBSActivity();
                        break;
                    }
                    case 2:
                    {
                        showCardJSYActivity();
                        break;
                    }
                    case 3:
                    {
                        showNewsListActivity();
                        break;
                    }

                    case 4:
                    {
                        //showDeviceSettingsActivity();
                        showGridView();
                        break;
                    }
                    default:
                        break;

                }
            }
        });

        //DEBUG
        //Test CheckRules data in db
        //CheckRulesInfo.printCheckRules();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.function_list, menu);
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
        return super.onOptionsItemSelected(item);
    }
    private void goMainActivity()
    {
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_push_right_to_left,R.anim.push_down);
    }
    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            exitBy2Click();		//调用双击退出函数
        }
        return false;
    }
    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();
        }
    }

    private void showDeviceSettingsActivity()
    {
        Intent intent=new Intent(this,DeviceSettingsActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_push_right_to_left,R.anim.push_down);
    }
    private void showCardCBSActivity()
    {
        Intent intent=new Intent(this,CardCBSActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_push_right_to_left,R.anim.push_down);
    }
    private void showCardJSYActivity()
    {
        Intent intent=new Intent(this,CardJSYActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_push_right_to_left,R.anim.push_down);
    }
    private void showNewsListActivity()
    {
        Intent intent=new Intent(this,NewsListActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_push_right_to_left,R.anim.push_down);
    }
    private void showSafetyListActivity()
    {
        Intent intent=new Intent(this, SafetyDBTabActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_push_right_to_left, R.anim.push_down);
    }
    private void showSelfCheckListActivity()
    {
        Intent intent=new Intent(this, SelfCheckFinalActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_push_right_to_left, R.anim.push_down);
    }
    private void showGridView()
    {
        Intent intent=new Intent(this, MainFunctionGrid.class);
        startActivity(intent);
    }

}
