package com.ocse.hse;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ocse.hse.Activities.CBSCardActivity;
import com.ocse.hse.Activities.JSYCardActivity;
import com.ocse.hse.Fragments.EvaluateFragment;
import com.ocse.hse.Fragments.HistoryFragment;
import com.ocse.hse.Fragments.NFCFragment;
import com.ocse.hse.Fragments.RecordFragment;
import com.ocse.hse.Interfaces.OnFragmentInteractionListener;
import com.ocse.hse.Models.JcrwInfo;
import com.ocse.hse.Models.OrganInfo;
import com.ocse.hse.Models.TagBasicInfo;
import com.ocse.hse.app.AppLog;
import com.ocse.hse.app.ApplicationConstants;
import com.ocse.hse.app.ApplicationController;
import com.ocse.hse.app.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class TabActivity extends Activity implements ActionBar.TabListener,OnFragmentInteractionListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    JcrwInfo taskInfo;
    OrganInfo organInfo;
    //Fragments
    NFCFragment nfcFragment=null;
    RecordFragment recordFragment=null;
    EvaluateFragment evaluateFragment=null;
    HistoryFragment historyFragment=null;
    //NFC Reading
    NfcAdapter mAdapter;
    PendingIntent mPendingIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null)
        {
            taskInfo=(JcrwInfo)bundle.getSerializable(ApplicationConstants.APP_BUNDLE_TASK_INFO_KEY);
            //organInfo=(OrganInfo)bundle.getSerializable(ApplicationConstants.APP_BUNDLE_ORGAN_INFO_KEY);
        }
        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setIcon(R.drawable.icon_hse_actionbar);

        if(taskInfo!=null) {
            actionBar.setTitle(taskInfo.getJR_MC());
        }
        if(organInfo!=null) {
            actionBar.setSubtitle("受检单位:"+organInfo.getOrganName());
        }
        if(taskInfo!=null)
        {
            ApplicationController.saveCurrentTaskAndOrganID(Integer.toString(taskInfo.getJR_ID()),"0");
        }
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
        mAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mAdapter == null) {
            showMessage(R.string.error, R.string.no_nfc);
            //finish();
            return;
        }
        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, ((Object) this).getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        processIntent(getIntent());
        ApplicationController.setAndCheckDirectories();
    }

    @Override
    protected void onResume(){
        super.onResume();
        if (mAdapter != null) {
            if (!mAdapter.isEnabled()) {
                showWirelessSettingsDialog();
            }
            mAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
        }

    }
    @Override
    protected void onPause() {
        super.onPause();
        if (mAdapter != null) {
            mAdapter.disableForegroundDispatch(this);

        }
    }
    //NFC Card Reading
    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent);//只有setIntent后，才能通过getIntent获得new Intent 否则仍为OnCreate时传入的旧 Intent
        processIntent(intent);
    }
    private void showMessage(int title, int message)
    {
        AlertDialog dialog= new AlertDialog.Builder(this).setNeutralButton("确定", null).create();
        dialog.setTitle(title);
        dialog.setMessage(getText(message));
        dialog.show();
    }
    private void showMessage(String title,String message)
    {
        AlertDialog dialog= new AlertDialog.Builder(this).setNeutralButton("确定", null).create();
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.show();
    }
    //如果本机支持NFC卡，但尚未在设置中打开，则提示用户打开NFC设置
    private void showWirelessSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.nfc_disabled);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.create().show();
        return;
    }

    void processIntent(Intent intent) {
        if(intent==null)
            return;
        if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction()))
        {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            if(tag==null)
                return;
            String tagId=getHex(tag.getId());
            String tagInfo=tag.toString();
            long tagDecId=getDec(tag.getId());
            tagId=Long.toString(tagDecId);
            //JSY 2491012570 HSE数据库中无数据
            //JSY 2491406618 鄢友渝

            if(tagId.equals("2491012570"))
            {
                //tagId="2490449290";//王新平
                //tagId="2491406618";//
                tagId="2490992986";//内部准驾证，驾驶证，从业资格证 存在过期证件
            }
            if(tagId.equals("2491433066"))
            {
                tagId="2491433066";
                //tagId="2736026811";
            }


            //CBS 2491433066 有数据
            getCardInformation(tagId);
        }
        //hideSoftKeyboard();

    }
    private String getHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = bytes.length - 1; i >= 0; --i) {
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
            if (i > 0) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    private long getDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = 0; i < bytes.length; ++i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

    private long getReversed(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = bytes.length - 1; i >= 0; --i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }
    //获取卡信息
    private void getCardInformation(final String tagID)
    {
        RequestQueue mRequestQueue= VolleySingleton.getInstance().getRequestQueue();
        final ProgressDialog progressDialog = ProgressDialog.show(this, "HSE证件", "获取卡信息...");
        Response.Listener<JSONObject> listener=new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(progressDialog!=null) {
                        progressDialog.dismiss();
                    }
                    AppLog.d(response.toString());
                    int result = -1;
                    if(response.has(ApplicationConstants.JSON_RESULT))
                    {
                        if(response.isNull("content"))
                        {
                            TagBasicInfo tag = new TagBasicInfo(ApplicationController.getCurrentTaskID(), ApplicationController.getCurrentOrganID(), tagID, "{}");
                            tag.saveToDB();
                        }
                        else {
                            TagBasicInfo tag = new TagBasicInfo(ApplicationController.getCurrentTaskID(), ApplicationController.getCurrentOrganID(), tagID, response.getJSONObject("content").toString());
                            tag.saveToDB();
                        }

                        result=response.getInt(ApplicationConstants.JSON_RESULT);
                        if(result==1) {
                            String msg=response.optString("message");
                            if(!response.isNull("content"))
                            {
                                JSONObject content=response.getJSONObject("content");
                                String type=content.optString("type");
                                if(type.equals("JSY"))
                                {
                                    fillJSYContent(content);
                                }
                                else if(type.equals("CBS"))
                                {
                                    fillCBSContent(content);
                                }
                            }

                        }
                        else if(result==-1)
                        {
                            String msg=response.optString("message");
                            Toast.makeText(TabActivity.this, msg, Toast.LENGTH_SHORT).show();
                            fillNoDataContent(tagID);
                        }


                    }
                }catch (JSONException e)
                {
                    AppLog.e(e.getMessage());
                }
            }
        };
        Response.ErrorListener errorListener= new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppLog.e(error.getMessage());
                if(progressDialog!=null) {
                    progressDialog.dismiss();
                }
            }
        };


        HashMap<String,String> params=new HashMap<String, String>();
        params.put("tag",tagID);

        String baseURL="http://"+ ApplicationController.getServerIP();
        String urlString=baseURL;
        Uri.Builder builder = new Uri.Builder();
        try {
            URL url = new URL(baseURL);
            builder = new Uri.Builder()
                    .scheme(url.getProtocol())
                    .encodedAuthority(url.getAuthority())
                    .appendEncodedPath(ApplicationConstants.APP_URL_API)
                    .appendEncodedPath(ApplicationConstants.APP_URL_CARD_INFO);
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.appendQueryParameter(entry.getKey(), entry.getValue());
            }
            urlString=builder.build().toString();
        }catch (Exception e)
        {
            AppLog.e(e.getMessage());
        }

        AppLog.d(urlString);
        JsonObjectRequest mJsonObjectRequest=new JsonObjectRequest(urlString,null,listener,errorListener);

        if(mRequestQueue!=null) {
            mRequestQueue.add(mJsonObjectRequest);
            AppLog.d("star loading");
            mRequestQueue.start();
        }


    }
    //填充驾驶员、承包商、无数据 内容
    private void fillCBSContent(JSONObject contentObject)
    {
        /**
        containerLayout.removeAllViews();
        View contentView=getLayoutInflater().inflate(R.layout.module_card_team,null);
        containerLayout.addView(contentView);
        AppLog.i("承包商卡");
         **/
        //Toast.makeText(this,"承包商卡",Toast.LENGTH_SHORT).show();

        Intent intent=new Intent(this, CBSCardActivity.class);
        intent.putExtra(ApplicationConstants.APP_BUNDLE_CARD_INFO_JSON_KEY,contentObject.toString());
        startActivity(intent);
        overridePendingTransition(R.anim.in_push_right_to_left,R.anim.push_down);
    }
    private void fillJSYContent(JSONObject contentObject)
    {
        /**
        containerLayout.removeAllViews();
        View contentView=getLayoutInflater().inflate(R.layout.module_card_driver,null);
        containerLayout.addView(contentView);
        AppLog.i("驾驶员卡");
         **/
        //Toast.makeText(this,"驾驶员卡",Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(this, JSYCardActivity.class);
        intent.putExtra(ApplicationConstants.APP_BUNDLE_CARD_INFO_JSON_KEY,contentObject.toString());
        startActivity(intent);
        overridePendingTransition(R.anim.in_push_right_to_left,R.anim.push_down);
    }
    private void fillNoDataContent(String tagID)
    {
        /**
        containerLayout.removeAllViews();
        View contentView=getLayoutInflater().inflate(R.layout.module_card_no_data,null);
        containerLayout.addView(contentView);
        AppLog.i("无数据");
         **/
        //Toast.makeText(this,"无数据",Toast.LENGTH_SHORT).show();
        showMessage("无效卡号","在HSE数据库中没有找到卡号("+tagID+")的证件信息。");
        if(nfcFragment!=null) {
            nfcFragment.updateCardList();
        }
    }
    //End NFC Card Reading
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        /**
        if (id == R.id.action_settings) {
            return true;
        }
         **/
        if(id==android.R.id.home)
        {
            quitActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onFragmentInteraction(Bundle bundle) {

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //return PlaceholderFragment.newInstance(position + 1);
            Fragment fragment=null;
            switch (position)
            {
                case 0: {
                    if(nfcFragment==null)
                    {
                        nfcFragment=NFCFragment.newInstance("","");
                    }
                    fragment=nfcFragment;
                    break;
                }
                case 1: {
                    if(recordFragment==null)
                    {
                        recordFragment=RecordFragment.newInstance("","");
                    }
                    fragment=recordFragment;
                    break;
                }
                case 2: {
                    if(evaluateFragment==null)
                    {
                        evaluateFragment=EvaluateFragment.newInstance("","");
                    }
                    fragment=evaluateFragment;
                    break;
                }
                case 3: {
                    if(historyFragment==null)
                    {
                        historyFragment=HistoryFragment.newInstance("","");
                    }
                    fragment=historyFragment;
                    break;
                }
                default: {
                    break;
                }
            }
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
                case 3:
                    return getString(R.string.title_section4).toUpperCase(l);
            }
            return null;
        }
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
        overridePendingTransition(R.anim.in_just_show, R.anim.out_push_down);
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle=new Bundle();
        //Error occurs when call super.OnSaveInstanceState(bundle);
        //super.onSaveInstanceState(bundle);
        //More stuff
    }



}
