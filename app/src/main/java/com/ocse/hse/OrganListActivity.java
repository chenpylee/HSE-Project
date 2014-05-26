package com.ocse.hse;

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

import com.ocse.hse.Models.OrganInfo;
import com.ocse.hse.Models.OrganInfoAdapter;
import com.ocse.hse.Models.TaskInfo;
import com.ocse.hse.app.ApplicationConstants;

import java.util.ArrayList;


public class OrganListActivity extends Activity {

    private TaskInfo taskInfo;
    private OrganInfo selectedOrgan;
    private ActionBar actionBar;
    private ListView organListView;
    private ArrayList<OrganInfo> dataArrayList;
    private OrganInfoAdapter dataAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organ_list);
        dataArrayList=new ArrayList<OrganInfo>();
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null)
        {
            taskInfo= (TaskInfo) bundle.getSerializable(ApplicationConstants.APP_BUNDLE_TASK_INFO_KEY);
            dataArrayList=taskInfo.getTaskOrgansList();
        }
        actionBar=getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setIcon(R.drawable.icon_hse_actionbar);
        actionBar.setTitle(taskInfo.getTaskName());
        actionBar.setSubtitle("受检单位");
        organListView=(ListView)findViewById(R.id.organListView);

        dataAdapter=new OrganInfoAdapter(this,dataArrayList);
        organListView.setAdapter(dataAdapter);
        organListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedOrgan=dataArrayList.get(position);
                Intent intent=new Intent(OrganListActivity.this,TabActivity.class);
                intent.putExtra(ApplicationConstants.APP_BUNDLE_TASK_INFO_KEY,taskInfo);
                intent.putExtra(ApplicationConstants.APP_BUNDLE_ORGAN_INFO_KEY,selectedOrgan);
                startActivity(intent);
                overridePendingTransition(R.anim.push_up,R.anim.push_down);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.organ_list, menu);
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
        else if(id==R.id.action_about)
        {
            goTaskAbout();
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

    private void goTaskAbout()
    {
        Intent intent=new Intent(this,AboutTaskActivity.class);
        intent.putExtra(ApplicationConstants.APP_BUNDLE_TASK_INFO_KEY,taskInfo);
        startActivity(intent);
        overridePendingTransition(R.anim.in_push_right_to_left,R.anim.push_down);
    }
}
