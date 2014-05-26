package com.ocse.hse;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ocse.hse.Models.OrganInfo;
import com.ocse.hse.Models.TaskInfo;
import com.ocse.hse.Models.TaskInfoAdapter;
import com.ocse.hse.app.AppLog;
import com.ocse.hse.app.ApplicationConstants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {
    private TextView txtActivityID;
    private Button btnTagViewer;
    private ActionBar actionBar;
    private ListView taskListView;
    private TaskInfoAdapter taskAdapter;
    private ArrayList<TaskInfo> taskList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        taskListView=(ListView)findViewById(R.id.taskListView);

        actionBar=getActionBar();
        actionBar.setTitle(ApplicationConstants.APP_ACTION_BAR_TITLE);
        //actionBar.setSubtitle("检查任务");
        actionBar.setIcon(R.drawable.icon_hse_actionbar);
        /**
        txtActivityID=(TextView)findViewById(R.id.txtActivityID);
        btnTagViewer=(Button)findViewById(R.id.btnTagViewer);
        btnTagViewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,NFCReadActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_up,R.anim.push_down);
            }
        });
         **/
        taskList=new ArrayList<TaskInfo>();
        taskAdapter=new TaskInfoAdapter(this,taskList);
        taskListView.setAdapter(taskAdapter);
        getTaskList();
        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(MainActivity.this,OrganListActivity.class);
                TaskInfo task=taskList.get(position);
                intent.putExtra(ApplicationConstants.APP_BUNDLE_TASK_INFO_KEY,task);
                startActivity(intent);
                overridePendingTransition(R.anim.in_push_right_to_left,R.anim.push_down);
            }
        });

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        //txtActivityID.setText(this.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);

        //return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            showDeviceSettingsActivity();
            return true;
        }
        else if(id==R.id.action_task_refresh)
        {
            AppLog.i("Refreshing Task");
            getTaskList();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDeviceSettingsActivity()
    {
        Intent intent=new Intent(MainActivity.this,DeviceSettingsActivity.class);
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

    //Get Task List From Server or DB
    private void getTaskList()
    {
        //get task list
        String taskName="华北局安全生产大检查";
        String taskDescription="中石化系统组织的安全大检查，联系人：HSE办公室 0371-68977666";
        String startDate="2014-05-21";
        String endDate="2014-05-26";
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        Date taskStartDate=null;
        Date taskEndDate=null;

        try{
            taskStartDate=dateFormat.parse(startDate);
            taskEndDate=dateFormat.parse(endDate);
        }catch (ParseException e)
        {
            AppLog.e(e.getMessage().toString());
        }
        ArrayList<OrganInfo> taskOrgansList=new ArrayList<OrganInfo>();

        OrganInfo organ1=new OrganInfo("201","采油厂","002","华北石油局");
        OrganInfo organ2=new OrganInfo("202","勘探队","002","华北石油局");
        taskOrgansList.add(organ1);
        taskOrgansList.add(organ2);
        TaskInfo task1=new TaskInfo(taskName,taskStartDate,taskEndDate,taskDescription,taskOrgansList);

        taskName="钻井作业安全5月检查";
        taskDescription="各钻井队每月的定期HSE检查";
        startDate="2014-05-10";
        endDate="2014-05-15";
        try{
            taskStartDate=dateFormat.parse(startDate);
            taskEndDate=dateFormat.parse(endDate);

        }catch(ParseException e1)
        {
            AppLog.e(e1.getMessage().toString());
        }

        TaskInfo task2=new TaskInfo(taskName,taskStartDate,taskEndDate,taskDescription,taskOrgansList);
        taskList.clear();
        taskList.add(task1);
        taskList.add(task2);

        taskAdapter.refillTaskList(taskList);
    }

}
