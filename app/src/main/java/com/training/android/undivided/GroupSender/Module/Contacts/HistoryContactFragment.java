package com.training.android.undivided.GroupSender.Module.Contacts;


import android.os.Bundle;
import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.training.android.undivided.GroupSender.Base.BaseFragment;
import com.training.android.undivided.GroupSender.Objects.Message;
import com.training.android.undivided.R;

import java.util.List;


public class HistoryContactFragment extends BaseFragment implements HistoryContactContract.View  {

    private RecyclerView mHistoryContactRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private View mEmptyView;
    private HistoryContactAdapter mAdapter;
    private HistoryContactContract.Presenter mPresenter;

    public static HistoryContactFragment newInstance() {
        Bundle args = new Bundle();

        HistoryContactFragment fragment = new HistoryContactFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_history_contact;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new HistoryContactAdapter(getActivity());
        mPresenter = new HistoryContactPresenter(this);
    }

    @Override
    protected void createView(View view, Bundle savedInstanceState) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) find(R.id.swipe_refresh_layout);
        mHistoryContactRecyclerView = (RecyclerView) find(R.id.history_contact_recycler_view);
        mEmptyView = find(R.id.empty_view);
    }

    @Override
    protected void initEvent() {
        mHistoryContactRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mHistoryContactRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadHistoryContact();
            }
        });

        mPresenter.loadHistoryContact();

    }



    @Override
    public void setPresenter(HistoryContactContract.Presenter presenter) {
        mPresenter = presenter;

    }

    @Override
    public void showHistoryContactList(List<Message> list) {
        mAdapter.setList(list);
    }

    @Override
    public void showEmpty(boolean state) {
        if (state) {
            mEmptyView.setVisibility(View.VISIBLE);
            mHistoryContactRecyclerView.setVisibility(View.GONE);
        } else {
            mEmptyView.setVisibility(View.GONE);
            mHistoryContactRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setLoadingIndicator(final boolean active) {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(active);
            }
        });
    }

    @Override
    public void setPullUpLoadingState(boolean state) {

    }
}
