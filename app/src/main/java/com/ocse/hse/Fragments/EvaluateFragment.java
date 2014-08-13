package com.ocse.hse.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ocse.hse.Activities.CheckTabActivity;
import com.ocse.hse.Interfaces.OnFragmentInteractionListener;
import com.ocse.hse.Models.CheckLv1Adapter;
import com.ocse.hse.Models.CheckRulesInfo;
import com.ocse.hse.R;
import com.ocse.hse.app.AppLog;
import com.ocse.hse.app.ApplicationConstants;
import com.ocse.hse.app.ApplicationController;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EvaluateFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class EvaluateFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    ListView listView;
    CheckLv1Adapter lv1Adapter;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EvaluateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EvaluateFragment newInstance(String param1, String param2) {
        EvaluateFragment fragment = new EvaluateFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public EvaluateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        AppLog.i("EvaluateFragment created.");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_evaluate, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            //mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Workaround to avoid NPE from support library bug:
        super.onSaveInstanceState(outState);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        listView=(ListView)view.findViewById(R.id.listView);
        //lv1Adapter=new EvaluationAdapter(getActivity());
        //listView.setAdapter(lv1Adapter);
        ArrayList<String> lv1List=new ArrayList<String>();
        String taskID=ApplicationController.getCurrentTaskID();
        lv1List= CheckRulesInfo.getLv1List(taskID);
        lv1Adapter=new CheckLv1Adapter(getActivity(),lv1List,ApplicationController.getCurrentOrganID());
        listView.setAdapter(lv1Adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position>0) {
                    goNextLv(ApplicationController.getCurrentTaskID(), lv1Adapter.getLv1(position));
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        //evaluationAdapter.updateStatus();
    }

    public void goNextLv(String task_id,String check_lv1)
    {
        //区分有无二级目录
        ArrayList<String> lv2List=CheckRulesInfo.getLv2List(task_id,check_lv1);
        if(lv2List.size()>0)
        {
            //AppLog.i("Has next level");
            goCheckLv2(task_id,check_lv1);
        }
        else
        {
            //AppLog.i("NO next level");
        }
    }

    public void goCheckLv2(String organ_id,String check_lv1)
    {
        Intent intent=new Intent(getActivity(), CheckTabActivity.class);
        //intent.putExtra(ApplicationConstants.APP_BUNDLE_CHECK_ORGAN_ID,organ_id);
        intent.putExtra(ApplicationConstants.APP_BUNDLE_CHECK_RULE_LV1,check_lv1);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.in_push_right_to_left, R.anim.push_down);
    }


}
