package com.ocse.hse.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.ocse.hse.R;
import com.ocse.hse.app.ApplicationConstants;
import com.ocse.hse.app.ApplicationController;

public class HistoryDetail extends Activity {

    ActionBar actionBar;
    String evaluationTitle,evaluationDescription;
    TextView txtIssue,txtDescription;
    WebView webView;
    String evaluationHtml;
    Button btnPass,btnNoPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detail);
        actionBar=getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setIcon(R.drawable.icon_hse_actionbar);
        //actionBar.setTitle("承包商证件");
        evaluationTitle="";
        evaluationDescription="";
        evaluationHtml="";
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null)
        {
            evaluationTitle=bundle.getString(ApplicationConstants.APP_BUNDLE_EVALUATION_TITLE,"");
            evaluationDescription=bundle.getString(ApplicationConstants.APP_BUNDLE_EVALUATION_DESCRIPTION,"");
        }
        actionBar.setTitle(evaluationTitle);
        initContents();
        fillContents();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.evaluation_detail, menu);
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

    private void initContents()
    {
        txtIssue=(TextView)findViewById(R.id.txtIssue);
        txtDescription=(TextView)findViewById(R.id.txtDescription);
        webView=(WebView)findViewById(R.id.webView);
        btnNoPass=(Button)findViewById(R.id.btnNoPass);
        btnNoPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEvaluationResult(HistoryDetail.this.evaluationTitle,"no");
            }
        });
        btnPass=(Button)findViewById(R.id.btnPass);
        btnPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEvaluationResult(HistoryDetail.this.evaluationTitle,"yes");
            }
        });

    }
    private void fillContents()
    {
        txtIssue.setText(evaluationTitle);
        txtDescription.setText(evaluationDescription);
        if(evaluationTitle.contains("线路")) {
            evaluationHtml = "<h2>裸露线路照片</h2><br><img src='data:image/jpeg;base64,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'/></br><p>3车间入口处有破损线路</p>";
        }
        else
        {
            evaluationHtml = "<h2>灭火器过期</h2><br>灭火器过期6个月</br>";
        }
        //webView.loadUrl();
        webView.loadDataWithBaseURL(null, evaluationHtml, "text/html", "utf-8", null);
    }
    private void saveEvaluationResult(String title,String pass)
    {
        title="History-"+title;
        ApplicationController.saveEvaluationResult(title,pass);
        quitActivity();
    }

}
