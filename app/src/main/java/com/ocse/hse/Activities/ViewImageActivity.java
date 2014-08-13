package com.ocse.hse.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.ocse.hse.R;
import com.ocse.hse.UI.ZoomFunctionality;
import com.ocse.hse.app.ApplicationConstants;

public class ViewImageActivity extends Activity {

    int imageIndex=0;
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_view_image);
        actionBar=getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setIcon(R.drawable.icon_hse_actionbar);
        actionBar.setTitle("");

        Bundle bundle=getIntent().getExtras();
        String title="";
        if(bundle!=null)
        {
            imageIndex=bundle.getInt(ApplicationConstants.APP_BUNDLE_SAMPLE_IMAGE_INDEX,0);
            title=bundle.getString(ApplicationConstants.APP_ACTION_BAR_TITLE,"");
        }
        actionBar.setTitle(title);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        //Bitmap bmp = BitmapFactory.decodeFile(filename, options);
        int imageId=R.drawable.sample_cbs_company_1;
        switch (imageIndex)
        {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:

                break;
            case 6: {
                imageId=R.drawable.sample_cbs_company_7;
                break;
            }
            default:
                break;
        }
        Bitmap bmp=BitmapFactory.decodeResource(getResources(),imageId,options);

        ZoomFunctionality img = new ZoomFunctionality(this);
        img.setImageBitmap(bmp);
        img.setMaxZoom(4f);
        setContentView(img);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.view_image, menu);
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
        if (id == android.R.id.home) {
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
