package com.ocse.hse.Fragments;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ocse.hse.Activities.CheckDetailActivity;
import com.ocse.hse.Models.CheckItemInfo;
import com.ocse.hse.Models.CheckListAdapter;
import com.ocse.hse.Models.CheckRuleSection;
import com.ocse.hse.Models.CheckRulesInfo;
import com.ocse.hse.R;
import com.ocse.hse.app.AppLog;
import com.ocse.hse.app.ApplicationConstants;
import com.ocse.hse.app.ApplicationController;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CheckListFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class CheckListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mParam3;

    private ListView listView;
    private ArrayList<String> ruleLv3List;
    private ArrayList<CheckRuleSection> ruleSections;
    private CheckListAdapter adapter;
    String organ_id;
    String task_id;
    private String rule_lv1;
    private String rule_lv2;
    private int currentVisibleIndex=0;
    private boolean isJustAfterCreated;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @param param3 Parameter 3.
     * @return A new instance of fragment CheckListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CheckListFragment newInstance(String param1, String param2,String param3) {
        CheckListFragment fragment = new CheckListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }
    public CheckListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rule_lv1="";
        rule_lv2="";
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3 = getArguments().getString(ARG_PARAM3);
            rule_lv1=mParam2;
            rule_lv2=mParam3;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_check_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view,savedInstanceState);
        /**
        TextView txtTitle=(TextView)view.findViewById(R.id.txtTitle);
        txtTitle.setText(mParam1+" "+mParam2+" "+mParam3);
         **/
        listView=(ListView)view.findViewById(R.id.listView);
        AppLog.i(mParam1+" "+mParam2+" "+mParam3);
        organ_id= ApplicationController.getCurrentOrganID();
        task_id=ApplicationController.getCurrentTaskID();

        ruleLv3List= CheckRulesInfo.getLv3List(task_id, mParam2, mParam3);//mParam2=rule_lv1 mParam3=rule_lv2;
        ruleSections=new ArrayList<CheckRuleSection>();

        int total=ruleLv3List.size();
        for(int i=0;i<total;i++)
        {
            String rule_lv3=ruleLv3List.get(i);
            ruleSections.add(new CheckRuleSection(task_id,organ_id, rule_lv1,rule_lv2,rule_lv3));
        }

        adapter=new CheckListAdapter(getActivity(),ruleSections);
        listView.setAdapter(adapter);
        isJustAfterCreated=true;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckItemInfo item=(CheckItemInfo)adapter.getItem(position);
                if(!item.getIsHeader())
                {
                    //非section header
                    AppLog.i("task:"+item.getTaskID()+" organ:"+item.getOrganID()+" lv1:"+item.getRuleLv1()+" lv2:"+item.getRuleLv2()+" lv3:"+item.getRuleLv3()+" content:"+item.getRuleContent()+" status:"+item.getRuleStatus());
                    goToCheckDetail(item);
                }
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        currentVisibleIndex=listView.getFirstVisiblePosition();
        //updateRulesStatus();
        if(!isJustAfterCreated)
        {
            int index = listView.getFirstVisiblePosition();
            View v = listView.getChildAt(0);
            int top = (v == null) ? 0 : v.getTop();
            updateRulesStatus();
            listView.setSelectionFromTop(index, top);
        }
        else
        {
            isJustAfterCreated=false;
        }
    }

    private void updateRulesStatus(){
        //ruleSections.clear();
        /**
        ArrayList<CheckRuleSection> tmpList=new ArrayList<CheckRuleSection>();
        boolean needUpdate=false;
        int total=ruleLv3List.size();
        for(int i=0;i<total;i++)
        {
            String rule_lv3=ruleLv3List.get(i);
            CheckRuleSection section=new CheckRuleSection(task_id,organ_id, rule_lv1,rule_lv2,rule_lv3);
            tmpList.add(section);

            CheckRuleSection oldSection=ruleSections.get(i);
            int rulesTotal=section.getSectionRuleList().size();
            for(int k=0;k<rulesTotal;k++)
            {

            }
        }
         **/
        ruleSections.clear();
        int total=ruleLv3List.size();
        for(int i=0;i<total;i++)
        {
            String rule_lv3=ruleLv3List.get(i);
            ruleSections.add(new CheckRuleSection(task_id,organ_id, rule_lv1,rule_lv2,rule_lv3));
        }
        adapter.refillData(ruleSections);
        if(currentVisibleIndex>0) {
            listView.smoothScrollToPosition(currentVisibleIndex);
        }

    }
    private void goToCheckDetail(CheckItemInfo item)
    {
        Intent intent=new Intent(getActivity(), CheckDetailActivity.class);
        intent.putExtra(ApplicationConstants.APP_BUNDLE_CHECK_RULE_ITEM,item);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.in_push_right_to_left, R.anim.push_down);
    }

}
