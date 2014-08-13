package com.ocse.hse.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.ocse.hse.Activities.AddRecordActivity;
import com.ocse.hse.Activities.EditRecordActivity;
import com.ocse.hse.Activities.ViewRecordActivity;
import com.ocse.hse.Interfaces.OnFragmentInteractionListener;
import com.ocse.hse.Models.RecordInfo;
import com.ocse.hse.Models.RecordInfoAdapter;
import com.ocse.hse.R;
import com.ocse.hse.app.AppLog;
import com.ocse.hse.app.ApplicationConstants;
import com.ocse.hse.app.ApplicationController;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecordFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class RecordFragment extends Fragment {
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
    private RecordInfoAdapter dataAdapter;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecordFragment newInstance(String param1, String param2) {
        RecordFragment fragment = new RecordFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public RecordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        //ApplicationController.printDirecotryInformation();
        AppLog.i("RecordFragment created.");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_record, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        btnAddRecord=(Button)view.findViewById(R.id.btnAddRecord);
        btnAddRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoAddRecordActivity();
            }
        });
        recordList=(ListView)view.findViewById(R.id.recordList);
        dataArray=new ArrayList<RecordInfo>();
        dataAdapter=new RecordInfoAdapter(getActivity(),dataArray);
        recordList.setAdapter(dataAdapter);
        recordList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final RecordInfo item=(RecordInfo)dataAdapter.getItem(position);
                Intent intent=new Intent(getActivity(), ViewRecordActivity.class);
                intent.putExtra(ApplicationConstants.APP_BUNDLE_RECORD,item);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_push_right_to_left, R.anim.push_down);
            }
        });
        recordList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final RecordInfo item=(RecordInfo)dataAdapter.getItem(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("隐患记录")
                        .setItems(R.array.record_option_array, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                                switch (which)
                                {
                                    case 0://查看
                                    {
                                        Intent intent=new Intent(getActivity(), ViewRecordActivity.class);
                                        intent.putExtra(ApplicationConstants.APP_BUNDLE_RECORD,item);
                                        startActivity(intent);
                                        getActivity().overridePendingTransition(R.anim.in_push_right_to_left, R.anim.push_down);
                                        break;
                                    }
                                    case 1://编辑
                                    {
                                        Intent intent=new Intent(getActivity(), EditRecordActivity.class);
                                        intent.putExtra(ApplicationConstants.APP_BUNDLE_RECORD,item);
                                        startActivity(intent);
                                        getActivity().overridePendingTransition(R.anim.in_push_right_to_left, R.anim.push_down);
                                        break;
                                    }
                                    case 2://删除
                                    {
                                        removeRecord(item.getRecordID());
                                        break;
                                    }
                                    default:
                                        break;
                                }
                            }
                        });
                AlertDialog alertDialog=builder.create();
                alertDialog.show();
                return false;
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        AppLog.i("RecordFragment onResume");
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
    private void updateRecordList()
    {
        ArrayList<RecordInfo> cardList=RecordInfo.getRecordsFromDBByOrganID(ApplicationController.getCurrentTaskID(),ApplicationController.getCurrentOrganID());
        if(cardList.size()!=dataArray.size())
        {
            dataArray.clear();
            Iterator<RecordInfo> it = cardList.iterator();
            while(it.hasNext())
            {
                RecordInfo item = it.next();
                dataArray.add(item);
            }
            dataAdapter.refillData(dataArray);
        }
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

    private void GoAddRecordActivity()
    {
        Intent intent=new Intent(getActivity(), AddRecordActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.in_push_right_to_left, R.anim.push_down);
    }

    private void removeRecord(final String recordID)
    {
        AlertDialog dialog= new AlertDialog.Builder(getActivity()).setPositiveButton("确定",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onRemoveConfirmed(recordID);
            }
        }).setNeutralButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create();

        dialog.setTitle("删除");
        dialog.setMessage("此隐患记录将被删除");
        dialog.show();
    }
    private void onRemoveConfirmed(String recordID)
    {
        RecordInfo.removeRecord(recordID,ApplicationController.getCurrentTaskID(),ApplicationController.getCurrentOrganID());
        updateRecordList();
    }

}
