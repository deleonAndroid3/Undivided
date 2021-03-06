package com.training.android.undivided.SmsGroup.Fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.training.android.undivided.R;
import com.training.android.undivided.SmsGroup.Adapter.OutboxAdapter;
import com.training.android.undivided.SmsGroup.Common.SimpleDividerItemDecoration;
import com.training.android.undivided.SmsGroup.Interface.ISmsStatusListener;
import com.training.android.undivided.SmsGroup.Model.SmsTask;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class OutboxFragment extends MainFragmentAbstr  {


    private RecyclerView mOutboxListView;

    private OutboxAdapter mOutboxAdapter;

    private List<SmsTask> mOutboxList;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private FrameLayout mEmptyFrame;

    private RelativeLayout mDisplayFrame;

    public OutboxFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       final View view=inflater.inflate(R.layout.fragment_outbox, container, false);
       return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mEmptyFrame = view.findViewById(R.id.waiting_frame);
        mDisplayFrame = view.findViewById(R.id.display_frame);

        if (getRootActivity().getOutboxList().size() > 0) {
            mEmptyFrame.setVisibility(View.GONE);
            mDisplayFrame.setVisibility(View.VISIBLE);
        }

        mOutboxListView = view.findViewById(R.id.outbox_list);

        mOutboxList = getRootActivity().getOutboxList();

        mOutboxAdapter = new OutboxAdapter(getRootActivity(), getActivity(), mOutboxList);

        //set layout manager
        mOutboxListView.setLayoutManager(new GridLayoutManager(getActivity(), 1, LinearLayoutManager.VERTICAL, false));

        //set line decoration
        mOutboxListView.addItemDecoration(new SimpleDividerItemDecoration(
                getActivity().getApplicationContext()
        ));

        mOutboxListView.setAdapter(mOutboxAdapter);

        //setup swipe refresh
        mSwipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mOutboxList = getRootActivity().getOutboxList();
                mOutboxAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        getRootActivity().setStatusListener(new ISmsStatusListener() {
            @Override
            public void onStatusChange(List<SmsTask> task) {
                mOutboxList = task;
                mOutboxAdapter.notifyDataSetChanged();
            }
        });

        getRootActivity().getToolbar().getMenu().findItem(R.id.button_replay).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                final SmsTask task = mOutboxList.get(mOutboxAdapter.getSelectedPosition());

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                getRootActivity().startSmsTask(task);
                                dialog.cancel();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.cancel();
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("replay message with title " + task.getMessage().getTitle() +
                        " to contact " + task.getContact().getDisplayName() + " (" + task.getContact().getSelectedPhoneNumber().replaceAll("\\s+", "") + ")")
                        .setPositiveButton("OK", dialogClickListener)
                        .setNegativeButton("Cancel", dialogClickListener).show();

                return false;
            }
        });
    }

    private void setTitle(String title) {
        getRootActivity().setToolbarTitle(title);
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle(getString(R.string.title_outbox));

        getRootActivity().hideMenuButton();

        mOutboxList = getRootActivity().getOutboxList();
        mOutboxAdapter.notifyDataSetChanged();
    }

}
