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

public class EvaluationDetail extends Activity {

    ActionBar actionBar;
    String evaluationTitle,evaluationDescription;
    TextView txtIssue,txtDescription;
    WebView webView;
    String evaluationHtml;
    Button btnPass,btnNoPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation_detail);
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
                saveEvaluationResult(EvaluationDetail.this.evaluationTitle,"no");
            }
        });
        btnPass=(Button)findViewById(R.id.btnPass);
        btnPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEvaluationResult(EvaluationDetail.this.evaluationTitle,"yes");
            }
        });

    }
    private void fillContents()
    {
        txtIssue.setText(evaluationTitle);
        txtDescription.setText(evaluationDescription);
        if(evaluationTitle.contains("线路")) {
            evaluationHtml = "<h2>线路标准</h2><br><img src='http://c.hiphotos.baidu.com/baike/w%3D268/sign=ce43d88ee6cd7b89e96c3d8537254291/b21bb051f81986182c3e37c04aed2e738ad4b31c8701c645.jpg'/></br><p>电源线的结构并不是十分复杂，电源线电源线但是也不要从表面就简单的可以一下子看穿它，如果好好的去研究电源线的话，有的地方还是需要专业的去了解电源线的结构的。电源线的结构主要要外护套、内护套、导体，常见的传输导体有铜、铝材质的金属丝等。</p>";
        }
        else
        {
            evaluationHtml = "<h2>灭火器构造</h2><br><img src='http://t10.baidu.com/it/u=990562101,1623665221&fm=58'/></br><p>干粉灭火器按充入的干粉药剂分类,有碳酸氢钠干粉灭火器,也称BC干粉灭火器;磷酸铵盐干粉灭火器,也称ABC干粉灭火器;按加压方式分类有储气瓶式和储压式;</p>";
        }
        //webView.loadUrl();
        webView.loadDataWithBaseURL(null, evaluationHtml, "text/html", "utf-8", null);
    }
    private void saveEvaluationResult(String title,String pass)
    {
        title=ApplicationController.getCurrentTaskID()+"-"+ApplicationController.getCurrentOrganID()+"-"+title;
        ApplicationController.saveEvaluationResult(title,pass);
        quitActivity();
    }

}
