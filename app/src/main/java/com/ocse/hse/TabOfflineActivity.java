package com.ocse.hse;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.ocse.hse.Fragments.EvaluateFragment;
import com.ocse.hse.Fragments.HistoryFragment;
import com.ocse.hse.Fragments.RecordFragment;
import com.ocse.hse.Interfaces.OnFragmentInteractionListener;
import com.ocse.hse.Models.JcrwInfo;
import com.ocse.hse.Models.OrganInfo;
import com.ocse.hse.app.ApplicationConstants;
import com.ocse.hse.app.ApplicationController;

import java.util.Locale;


public class TabOfflineActivity extends Activity implements ActionBar.TabListener,OnFragmentInteractionListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v13.app.FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link android.support.v4.view.ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    JcrwInfo taskInfo;
    OrganInfo organInfo;
    //Fragments
    RecordFragment recordFragment=null;
    EvaluateFragment evaluateFragment=null;
    HistoryFragment historyFragment=null;

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

        ApplicationController.setAndCheckDirectories();
    }

    @Override
    protected void onResume(){
        super.onResume();


    }
    @Override
    protected void onPause() {
        super.onPause();

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
     * A {@link android.support.v13.app.FragmentPagerAdapter} that returns a fragment corresponding to
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
                    if(recordFragment==null)
                    {
                        recordFragment=RecordFragment.newInstance("","");
                    }
                    fragment=recordFragment;
                    break;
                }
                case 1: {
                    if(evaluateFragment==null)
                    {
                        evaluateFragment=EvaluateFragment.newInstance("","");
                    }
                    fragment=evaluateFragment;
                    break;
                }
                case 2: {
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
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section3).toUpperCase(l);
                case 2:
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
