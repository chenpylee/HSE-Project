package com.ocse.hse.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ocse.hse.Models.JCDWInfo;
import com.ocse.hse.Models.RecordInfo;
import com.ocse.hse.R;
import com.ocse.hse.app.AppLog;
import com.ocse.hse.app.ApplicationConstants;
import com.ocse.hse.app.ApplicationController;

import java.io.File;
import java.util.ArrayList;

public class ViewRecordActivity extends Activity {

    ActionBar actionBar;
    RecordInfo recordInfo;
    TextView txtPicTotal;
    ImageView imgPreview1,imgPreview2,imgPreview3,imgPreview4,imgPreview5;
    FrameLayout imgContainer1,imgContainer2,imgContainer3,imgContainer4,imgContainer5;
    EditText txtCreated,txtDescription;
    EditText txtContact,txtPhone;

    ArrayList<String> imageFilepaths;
    ArrayList<Bitmap> imageBitmaps;
    ArrayList<ImageView> imageviews;
    ArrayList<FrameLayout> imageContainers;

    static final String DIR_RECORD_PREVIEW="records/preview";
    static final String DIR_RECORD_CONTENT="records/content";
    String record_id,record_task_id,record_organ_id,record_description,record_contact,record_phone,record_created,record_updated;
    EditText txtRuleLv3,txtRuleContent;
    //History
    Boolean isHistory;
    LinearLayout bottomBar;
    Button btnPass;
    Button btnNoPass;
    //检查单位
    EditText txtOrgan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_record);
        actionBar=getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setIcon(R.drawable.icon_hse_actionbar);
        actionBar.setTitle("查看隐患信息");
        imageFilepaths=new ArrayList<String>();
        isHistory=false;

        Bundle bundle=getIntent().getExtras();
        if(bundle!=null)
        {
            recordInfo= (RecordInfo) bundle.getSerializable(ApplicationConstants.APP_BUNDLE_RECORD);
            imageFilepaths=recordInfo.getImagePathList();//image name list
            isHistory=bundle.getBoolean(ApplicationConstants.APP_BUNDLE_IS_HISTORY,false);
        }
        bottomBar=(LinearLayout)findViewById(R.id.bottomBar);
        if(!isHistory)
        {
            bottomBar.setVisibility(View.GONE);
        }
        else
        {
            bottomBar.setVisibility(View.VISIBLE);
            btnPass=(Button)findViewById(R.id.btnPass);
            btnNoPass=(Button)findViewById(R.id.btnNoPass);
            btnPass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //quitActivity();
                    showUpdateHistoryRecordActivity();
                }
            });
            btnNoPass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    quitActivity();
                }
            });
        }
        txtRuleLv3=(EditText)findViewById(R.id.txtRuleLv3);
        txtRuleContent=(EditText)findViewById(R.id.txtRuleContent);
        if(recordInfo!=null)
        {
            txtRuleLv3.setText(recordInfo.getRuleLv3());
            txtRuleContent.setText(recordInfo.getRuleContent());
        }

        ArrayList<JCDWInfo> organList= JCDWInfo.getJCDWByJR(ApplicationController.getCurrentTaskID());
        txtOrgan=(EditText)findViewById(R.id.txtOrgan);
        for(int i=0;i<organList.size();i++)
        {
            int jcdw_id=organList.get(i).getJR_DEPTID();
            String jcdw_str_id=Integer.toString(jcdw_id);
            if(jcdw_str_id.equals(recordInfo.getOrganID()))
            {
                txtOrgan.setText(organList.get(i).getJR_DWMC());
                break;
            }
        }
        txtCreated=(EditText)findViewById(R.id.txtCreated);
        txtDescription=(EditText)findViewById(R.id.txtDescription);
        txtContact=(EditText)findViewById(R.id.txtContact);
        txtPhone=(EditText)findViewById(R.id.txtPhone);


        txtPicTotal=(TextView)findViewById(R.id.txtPicTotal);

        imgContainer1=(FrameLayout)findViewById(R.id.imgContainer1);
        imgContainer2=(FrameLayout)findViewById(R.id.imgContainer2);
        imgContainer3=(FrameLayout)findViewById(R.id.imgContainer3);
        imgContainer4=(FrameLayout)findViewById(R.id.imgContainer4);
        imgContainer5=(FrameLayout)findViewById(R.id.imgContainer5);
        imageContainers=new ArrayList<FrameLayout>();
        imageContainers.add(imgContainer1);
        imageContainers.add(imgContainer2);
        imageContainers.add(imgContainer3);
        imageContainers.add(imgContainer4);
        imageContainers.add(imgContainer5);

        imgPreview1=(ImageView)findViewById(R.id.imgPreview1);
        imgPreview2=(ImageView)findViewById(R.id.imgPreview2);
        imgPreview3=(ImageView)findViewById(R.id.imgPreview3);
        imgPreview4=(ImageView)findViewById(R.id.imgPreview4);
        imgPreview5=(ImageView)findViewById(R.id.imgPreview5);
        imageviews=new ArrayList<ImageView>();
        imageviews.add(imgPreview1);
        imageviews.add(imgPreview2);
        imageviews.add(imgPreview3);
        imageviews.add(imgPreview4);
        imageviews.add(imgPreview5);

        imageBitmaps=new ArrayList<Bitmap>();

        fillContent();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(!isHistory) {
            getMenuInflater().inflate(R.menu.view_record, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_record_edit) {
            editRecord();
            return true;
        }
        if (id == R.id.action_record_remove) {
            removeRecord(recordInfo.getRecordID());
            return true;
        }
        if (id==android.R.id.home){
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

    private void fillContent()
    {
        record_id="";
        record_task_id="";
        record_organ_id="";
        record_description="";
        record_contact="";
        record_phone="";
        record_created="";
        if(recordInfo!=null)
        {
            record_id=recordInfo.getRecordID();
            record_task_id=recordInfo.getTaskID();
            record_organ_id=recordInfo.getOrganID();
            record_description=recordInfo.getDescription();
            record_contact=recordInfo.getContact();
            record_phone=recordInfo.getPhone();
            record_created=recordInfo.getCreated();
        }
        txtCreated.setText(record_created);
        txtDescription.setText(record_description);
        txtContact.setText(record_contact);
        txtPhone.setText(record_phone);

        listSavedPreviewImages();
        loadImagesToPreview();
    }

    private void listSavedPreviewImages()
    {

        int total=imageFilepaths.size();
        if(total==0)
        {
            AppLog.i("No Saved Preview Images");
        }
        for(int i=0;i<total;i++)
        {
            File file=ApplicationController.getFile(DIR_RECORD_PREVIEW, imageFilepaths.get(i));
            Bitmap bitmap= BitmapFactory.decodeFile(file.getAbsolutePath().toString());
            imageBitmaps.add(bitmap);
        }
    }
    private void loadImagesToPreview()
    {
        int total=imageBitmaps.size();
        for(int i=0;i<total;i++)
        {
            imageContainers.get(i).setVisibility(View.VISIBLE);
            imageviews.get(i).setImageBitmap(imageBitmaps.get(i));
        }
        for(int i=4;i>=total;i--)
        {
            imageContainers.get(i).setVisibility(View.GONE);
            imageviews.get(i).setImageBitmap(null);
        }
        txtPicTotal.setText(total+"张");
    }

    public void onImageDetail(View view)
    {
        AppLog.i("Detail Image:"+view.getTag().toString());

        final int imgIndex=Integer.parseInt(view.getTag().toString());

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
        quitActivity();
    }

    private void editRecord()
    {
        Intent intent=new Intent(this, EditRecordActivity.class);
        intent.putExtra(ApplicationConstants.APP_BUNDLE_RECORD,recordInfo);
        startActivity(intent);
        overridePendingTransition(R.anim.in_push_right_to_left, R.anim.push_down);
        finish();
    }
    private void showUpdateHistoryRecordActivity()
    {
        Intent intent=new Intent(this, AddHistoryUpdateActivity.class);
        intent.putExtra(ApplicationConstants.APP_BUNDLE_RECORD,recordInfo);
        startActivity(intent);
        overridePendingTransition(R.anim.in_push_right_to_left, R.anim.push_down);
    }
}
