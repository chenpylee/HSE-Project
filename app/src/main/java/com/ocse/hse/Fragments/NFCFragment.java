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

import com.ocse.hse.Activities.CBSCardActivity;
import com.ocse.hse.Activities.JSYCardActivity;
import com.ocse.hse.Interfaces.OnFragmentInteractionListener;
import com.ocse.hse.Models.TagAdapter;
import com.ocse.hse.Models.TagBasicInfo;
import com.ocse.hse.R;
import com.ocse.hse.app.AppLog;
import com.ocse.hse.app.ApplicationConstants;
import com.ocse.hse.app.ApplicationController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NFCFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class NFCFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private ListView cardListView;
    private ArrayList<TagBasicInfo> dataArray;
    private TagAdapter dataAdapter;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NFCFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NFCFragment newInstance(String param1, String param2) {
        NFCFragment fragment = new NFCFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public NFCFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        AppLog.i("NFCFragment created.");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_nfc, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            //mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        cardListView=(ListView)getActivity().findViewById(R.id.cardList);
        dataArray=new ArrayList<TagBasicInfo>();
        dataAdapter=new TagAdapter(getActivity(),dataArray);
        cardListView.setAdapter(dataAdapter);
        cardListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TagBasicInfo item=(TagBasicInfo)dataAdapter.getItem(position);
                if(!item.getCardJson().equals("{}"))
                {
                    String jsonString=item.getCardJson();
                    String type="";
                    try{
                        JSONObject contentObject=new JSONObject(jsonString);
                        type=contentObject.optString("type","");
                    }catch (JSONException jsonException)
                    {

                    }
                    if(type.equals("JSY"))
                    {
                        Intent intent=new Intent(getActivity(), JSYCardActivity.class);
                        intent.putExtra(ApplicationConstants.APP_BUNDLE_CARD_INFO_JSON_KEY,jsonString);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.in_push_right_to_left,R.anim.push_down);
                    }
                    else if(type.equals("CBS"))
                    {
                        Intent intent=new Intent(getActivity(), CBSCardActivity.class);
                        intent.putExtra(ApplicationConstants.APP_BUNDLE_CARD_INFO_JSON_KEY,jsonString);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.in_push_right_to_left, R.anim.push_down);
                    }
                }

            }
        });
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
    public void onSaveInstanceState(Bundle outState) {
        // Workaround to avoid NPE from support library bug:
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        AppLog.i("NFCFragment onResume");
        ArrayList<TagBasicInfo> cardList=TagBasicInfo.printAllTagsInDBByTaskAndOrgan(ApplicationController.getCurrentTaskID(),ApplicationController.getCurrentOrganID());
        if(cardList.size()!=dataArray.size())
        {
            dataArray.clear();
            Iterator<TagBasicInfo> it = cardList.iterator();
            while(it.hasNext())
            {
                TagBasicInfo item = it.next();
                dataArray.add(item);
            }
            dataAdapter.refillData(dataArray);
            if(dataArray.size()>0) {
                cardListView.smoothScrollToPosition(0);
            }
        }
    }
    public void updateCardList()
    {
        ArrayList<TagBasicInfo> cardList=TagBasicInfo.printAllTagsInDBByTaskAndOrgan(ApplicationController.getCurrentTaskID(),ApplicationController.getCurrentOrganID());
        if(cardList.size()!=dataArray.size()&&cardListView!=null)
        {
            dataArray.clear();
            Iterator<TagBasicInfo> it = cardList.iterator();
            while(it.hasNext())
            {
                TagBasicInfo item = it.next();
                dataArray.add(item);
            }
            dataAdapter.refillData(dataArray);
            if(dataArray.size()>0) {
                cardListView.smoothScrollToPosition(0);
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



}
