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
import android.widget.Button;
import android.widget.ListView;

import com.ocse.hse.Activities.ViewRecordActivity;
import com.ocse.hse.Interfaces.OnFragmentInteractionListener;
import com.ocse.hse.Models.HistoryInfoAdapter;
import com.ocse.hse.Models.RecordInfo;
import com.ocse.hse.R;
import com.ocse.hse.app.AppLog;
import com.ocse.hse.app.ApplicationConstants;
import com.ocse.hse.app.ApplicationController;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A simple {@link android.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.ocse.hse.Interfaces.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link com.ocse.hse.Fragments.HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class HistoryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private Button btnAddRecord;
    private ListView recordList;
    private ArrayList<RecordInfo> dataArray;
    private HistoryInfoAdapter dataAdapter;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EvaluateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public HistoryFragment() {
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
        return inflater.inflate(R.layout.fragment_history, container, false);
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
        recordList=(ListView)view.findViewById(R.id.recordList);
        dataArray=new ArrayList<RecordInfo>();
        dataAdapter=new HistoryInfoAdapter(getActivity(),dataArray);
        recordList.setAdapter(dataAdapter);
        recordList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final RecordInfo item = (RecordInfo) dataAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), ViewRecordActivity.class);
                intent.putExtra(ApplicationConstants.APP_BUNDLE_RECORD, item);
                intent.putExtra(ApplicationConstants.APP_BUNDLE_IS_HISTORY,true);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_push_right_to_left, R.anim.push_down);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        ArrayList<RecordInfo> cardList=RecordInfo.getRecordsFromDBByOrganID(ApplicationController.getCurrentTaskID(),ApplicationController.getCurrentOrganID());
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

}
