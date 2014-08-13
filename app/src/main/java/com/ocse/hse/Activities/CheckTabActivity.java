package com.ocse.hse.Activities;

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

import com.ocse.hse.Fragments.CheckListFragment;
import com.ocse.hse.Fragments.Safety0Fragment;
import com.ocse.hse.Fragments.Safety1Fragment;
import com.ocse.hse.Interfaces.OnFragmentInteractionListener;
import com.ocse.hse.Models.CheckRulesInfo;
import com.ocse.hse.R;
import com.ocse.hse.app.ApplicationConstants;
import com.ocse.hse.app.ApplicationController;

import java.util.ArrayList;


public class CheckTabActivity extends Activity implements ActionBar.TabListener,OnFragmentInteractionListener {

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

    Safety0Fragment safety0Fragment=null;
    Safety1Fragment safety1Fragment=null;
    //NFC Reading
    String organ_id="";
    String task_id="";
    String rule_lv1="";
    ArrayList<String> checkLv2List;
    ArrayList<CheckListFragment> fragmentList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        Bundle bundle=getIntent().getExtras();

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setIcon(R.drawable.icon_hse_actionbar);


        if(bundle!=null)
        {
            //organ_id=bundle.getString(ApplicationConstants.APP_BUNDLE_CHECK_ORGAN_ID,"");
            rule_lv1=bundle.getString(ApplicationConstants.APP_BUNDLE_CHECK_RULE_LV1,"");
            ApplicationController.saveCurrentCheckLv1(rule_lv1);
        }

        actionBar.setTitle(rule_lv1);

        task_id=ApplicationController.getCurrentTaskID();
        checkLv2List= CheckRulesInfo.getLv2List(task_id,rule_lv1);


        fragmentList=new ArrayList<CheckListFragment>();
        for(int i=0;i<checkLv2List.size();i++)
        {
            fragmentList.add(null);
        }

        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
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
        getMenuInflater().inflate(R.menu.safety_tab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
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
            if(fragmentList.get(position)==null)
            {
                fragment=CheckListFragment.newInstance(organ_id,rule_lv1,checkLv2List.get(position));
                fragmentList.set(position,(CheckListFragment)fragment);
            }
            else
            {
                fragment=fragmentList.get(position);
            }
            /**
            switch (position)
            {
                case 0: {
                    if(safety0Fragment==null)
                    {
                        safety0Fragment=Safety0Fragment.newInstance("","");
                    }
                    fragment=safety0Fragment;
                    break;
                }
                case 1: {
                    if(safety1Fragment==null)
                    {
                        safety1Fragment=Safety1Fragment.newInstance("","");
                    }
                    fragment=safety1Fragment;
                    break;
                }

                default: {
                    break;
                }
            }
             **/

            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return checkLv2List.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return checkLv2List.get(position);
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
        overridePendingTransition(R.anim.in_just_show, R.anim.out_push_left_to_right);
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle=new Bundle();
        //Error occurs when call super.OnSaveInstanceState(bundle);
        //super.onSaveInstanceState(bundle);
        //More stuff
    }



}
