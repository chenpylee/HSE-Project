package com.ocse.hse.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ocse.hse.DeviceSettingsActivity;
import com.ocse.hse.MainActivity;
import com.ocse.hse.MainOfflineActivity;
import com.ocse.hse.R;
import com.ocse.hse.ResultUploadActivity;
import com.ocse.hse.app.ApplicationController;

import java.util.Timer;
import java.util.TimerTask;

public class MainFunctionGrid extends Activity {

    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_function_grid);
        actionBar=getActionBar();
        /**
        actionBar.setTitle(ApplicationConstants.APP_ACTION_BAR_TITLE);
        actionBar.setIcon(R.drawable.icon_hse_actionbar);
         **/
        actionBar.hide();
        ApplicationController.setAndCheckDirectories();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_function_grid, menu);
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
    public void showCheckActivity(View view)
    {
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_push_right_to_left,R.anim.push_down);
    }
    public void showCheckOfflineActivity(View view)
    {
        Intent intent=new Intent(this,MainOfflineActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_push_right_to_left,R.anim.push_down);
    }
    public void showDeviceSettingsActivity(View view)
    {
        Intent intent=new Intent(this,DeviceSettingsActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_push_right_to_left,R.anim.push_down);
    }
    public void showCardCBSActivity(View view)
    {
        Intent intent=new Intent(this,CardCBSActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_push_right_to_left,R.anim.push_down);
    }
    public void showCardJSYActivity(View view)
    {
        Intent intent=new Intent(this,CardJSYActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_push_right_to_left,R.anim.push_down);
    }
    public void showNewsListActivity(View view)
    {
        Intent intent=new Intent(this,NewsListActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_push_right_to_left,R.anim.push_down);
    }
    public void showReportAcitivity(View view)
    {
        Toast.makeText(this,"暂无报表数据",Toast.LENGTH_SHORT).show();
    }
    public void showUploadActivity(View view)
    {
        Intent intent=new Intent(this,ResultUploadActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_push_right_to_left,R.anim.push_down);
    }

}
