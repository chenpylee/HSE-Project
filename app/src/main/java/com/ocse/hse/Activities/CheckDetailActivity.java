package com.ocse.hse.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.ocse.hse.Models.CheckItemInfo;
import com.ocse.hse.Models.CheckRecordInfo;
import com.ocse.hse.Models.RecordInfo;
import com.ocse.hse.Models.RecordInfoAdapter;
import com.ocse.hse.R;
import com.ocse.hse.app.AppLog;
import com.ocse.hse.app.ApplicationConstants;
import com.ocse.hse.app.ApplicationController;

import java.util.ArrayList;
import java.util.Iterator;

public class CheckDetailActivity extends Activity {

    ActionBar actionBar;
    CheckItemInfo ruleItem;
    String strRuleLv3,strRuleContent;
    TextView txtRuleLv3,txtRuleContent;
    Button btnNoPass,btnPass;

    //Record List
    private Button btnAddRecord;
    private ListView recordList;
    private ArrayList<RecordInfo> dataArray;
    private RecordInfoAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_detail);
        actionBar=getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setIcon(R.drawable.icon_hse_actionbar);
        actionBar.setTitle("");
        Bundle bundle=getIntent().getExtras();
        ruleItem=null;

        if(bundle!=null)
        {
            ruleItem=(CheckItemInfo)bundle.get(ApplicationConstants.APP_BUNDLE_CHECK_RULE_ITEM);
            actionBar.setTitle(ruleItem.getRuleLv3());
            strRuleLv3=ruleItem.getRuleLv3();
            strRuleContent=ruleItem.getRuleContent();
        }
        init();
        fillContents();
        /**
        btnAddRecord=(Button)findViewById(R.id.btnAddRecord);
        btnAddRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNoPass();
            }
        });
         **/
        recordList=(ListView)findViewById(R.id.recordList);
        dataArray=new ArrayList<RecordInfo>();
        dataAdapter=new RecordInfoAdapter(this,dataArray);
        recordList.setAdapter(dataAdapter);
        recordList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final RecordInfo item=(RecordInfo)dataAdapter.getItem(position);
                Intent intent=new Intent(CheckDetailActivity.this, ViewRecordActivity.class);
                intent.putExtra(ApplicationConstants.APP_BUNDLE_RECORD,item);
                startActivity(intent);
                overridePendingTransition(R.anim.in_push_right_to_left, R.anim.push_down);
            }
        });
        recordList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final RecordInfo item=(RecordInfo)dataAdapter.getItem(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(CheckDetailActivity.this);
                builder.setTitle("隐患记录")
                        .setItems(R.array.record_option_array, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                                switch (which)
                                {
                                    case 0://查看
                                    {
                                        Intent intent=new Intent(CheckDetailActivity.this, ViewRecordActivity.class);
                                        intent.putExtra(ApplicationConstants.APP_BUNDLE_RECORD,item);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.in_push_right_to_left, R.anim.push_down);
                                        break;
                                    }
                                    case 1://编辑
                                    {
                                        Intent intent=new Intent(CheckDetailActivity.this, EditRecordActivity.class);
                                        intent.putExtra(ApplicationConstants.APP_BUNDLE_RECORD,item);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.in_push_right_to_left, R.anim.push_down);
                                        break;
                                    }
                                    case 2://删除
                                    {
                                        removeRecord(item.getRecordID());
                                        break;
                                    }
                                    default:
                                        break;
                                }
                            }
                        });
                AlertDialog alertDialog=builder.create();
                alertDialog.show();
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        AppLog.i("RecordFragment onResume");
        ArrayList<RecordInfo> cardList=RecordInfo.getRecordsFromDBByRule(ruleItem);
        //if(cardList.size()!=dataArray.size())
        {
            dataArray.clear();
            Iterator<RecordInfo> it = cardList.iterator();
            while(it.hasNext())
            {
                RecordInfo item = it.next();
                dataArray.add(item);
            }
            dataAdapter.refillData(dataArray);
            if(dataArray.size()>0) {
                recordList.smoothScrollToPosition(0);
            }
        }
    }
    private void updateRecordList()
    {
        ArrayList<RecordInfo> cardList=RecordInfo.getRecordsFromDBByRule(ruleItem);
        if(cardList.size()!=dataArray.size())
        {
            dataArray.clear();
            Iterator<RecordInfo> it = cardList.iterator();
            while(it.hasNext())
            {
                RecordInfo item = it.next();
                dataArray.add(item);
            }
            dataAdapter.refillData(dataArray);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.check_detail, menu);
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
        RecordInfo.syncRuleStatus(ruleItem.getTaskID());
        finish();
        overridePendingTransition(R.anim.in_just_show, R.anim.out_push_left_to_right);
    }

    private void init()
    {
        txtRuleLv3=(TextView)findViewById(R.id.txtRuleLv3);
        txtRuleContent=(TextView)findViewById(R.id.txtRuleContent);
        btnPass=(Button)findViewById(R.id.btnPass);
        btnNoPass=(Button)findViewById(R.id.btnNoPass);
        btnPass.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {
                    checkPass();
                }
        });
        btnNoPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNoPass();
            }
        });
    }
    private void fillContents()
    {
        txtRuleLv3.setText(strRuleLv3);
        txtRuleContent.setText(strRuleContent);
    }

    private void checkPass()
    {
        //1.if has records, popup alert
        boolean hasRecords= CheckRecordInfo.getHasRecords(ruleItem.getTaskID(),ruleItem.getOrganID(),ruleItem.getRuleLv1(),ruleItem.getRuleLv2(),ruleItem.getRuleLv3(),ruleItem.getRuleContent());
        if(hasRecords)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("点击合格将会删除此检查内容关联的隐患记录，是否确认合格");
            builder.setPositiveButton("确认合格", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    confirmPass();
                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    //finish();
                }
            });
            builder.create().show();
        }
        else
        {
            confirmPass();
        }

    }
    private void confirmPass()
    {
        //1. delete all related records
        CheckRecordInfo.removeRuleRecords(ruleItem);
        //2.insert/update TABLE_CHECK_RULES_STATUS
        CheckRecordInfo.updateCheckRuleStatus(ruleItem.getTaskID(), ruleItem.getOrganID(), ruleItem.getRuleLv1(), ruleItem.getRuleLv2(), ruleItem.getRuleLv3(), ruleItem.getRuleContent(), "yes");
        //3.quit
        quitActivity();
    }
    private void checkNoPass()
    {
        Intent intent=new Intent(this, AddCheckRecordActivity.class);
        intent.putExtra(ApplicationConstants.APP_BUNDLE_CHECK_RULE_ITEM,ruleItem);
        startActivity(intent);
        overridePendingTransition(R.anim.in_push_right_to_left, R.anim.push_down);
    }



    private void removeRecord(final String recordID)
    {
        AlertDialog dialog= new AlertDialog.Builder(this).setPositiveButton("确定",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onRemoveConfirmed(recordID);
            }
        }).setNeutralButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create();

        dialog.setTitle("删除");
        dialog.setMessage("此隐患记录将被删除");
        dialog.show();
    }
    private void onRemoveConfirmed(String recordID)
    {
        RecordInfo.removeRecord(recordID,ApplicationController.getCurrentTaskID(),ApplicationController.getCurrentOrganID());
        updateRecordList();
    }

}
