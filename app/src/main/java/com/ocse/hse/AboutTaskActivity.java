package com.ocse.hse;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.ocse.hse.Models.TaskInfo;
import com.ocse.hse.app.ApplicationConstants;


public class AboutTaskActivity extends Activity {

    private ActionBar actionBar;
    private EditText txtTaskName,txtTaskDescription,txtTaskPeriod;
    private TaskInfo taskInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_task);
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null)
        {
            taskInfo=(TaskInfo)bundle.getSerializable(ApplicationConstants.APP_BUNDLE_TASK_INFO_KEY);
        }
        txtTaskName=(EditText)findViewById(R.id.txtTaskName);
        txtTaskPeriod=(EditText)findViewById(R.id.txtTaskPeriod);
        txtTaskDescription=(EditText)findViewById(R.id.txtTaskDescription);

        actionBar=getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setIcon(R.drawable.icon_hse_actionbar);
        actionBar.setTitle(ApplicationConstants.ACTIVITY_TITLE_ABOUT_TASK);

        if(taskInfo!=null)
        {
            txtTaskName.setText(taskInfo.getTaskName());
            String taskPeirod=taskInfo.getTaskStartDateString()+" ~ "+taskInfo.getTaskEndDateString();
            txtTaskPeriod.setText(taskPeirod);
            txtTaskDescription.setText(taskInfo.getTaskDescription());
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.about_task, menu);
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
        else if(id==android.R.id.home)
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
