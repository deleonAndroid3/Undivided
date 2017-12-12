package com.training.android.undivided.GroupSender.Fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.training.android.undivided.GroupSender.Adapter.TopicAdapter;
import com.training.android.undivided.GroupSender.BaseActivity;
import com.training.android.undivided.GroupSender.Interface.IFragment;
import com.training.android.undivided.GroupSender.Interface.IViewHolderClickListener;
import com.training.android.undivided.GroupSender.Model.Message;
import com.training.android.undivided.GroupSender.SimpleDividerItemDecoration;
import com.training.android.undivided.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class TopicFragment extends MainFragmentAbstr implements IFragment {
    private RecyclerView mTopicListView;

    private TopicAdapter mTopicAdapter;

    private List<String> mTopicList;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private FrameLayout mEmptyFrame;

    private RelativeLayout mDisplayFrame;

    private TopicItemFragment mFragment;

    private FragmentActivity mActivity;

    public TopicFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_topic, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEmptyFrame = view.findViewById(R.id.waiting_frame);
        mDisplayFrame = view.findViewById(R.id.display_frame);

        mTopicListView = view.findViewById(R.id.topic_list);

        refreshTopicList();

        if (mTopicList.size() > 0) {
            mEmptyFrame.setVisibility(View.GONE);
            mDisplayFrame.setVisibility(View.VISIBLE);
        }

        mTopicAdapter = new TopicAdapter(mTopicList, getActivity(), getRootActivity(), new IViewHolderClickListener() {
            @Override
            public void onClick(View view) {
                final int index = mTopicListView.getChildAdapterPosition(view);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mFragment = new TopicItemFragment();
                        Bundle args = new Bundle();
                        args.putString("topic", mTopicList.get(index));
                        mFragment.setArguments(args);

                        final FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.fragment_frame, mFragment, "TopicItem");
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                }, 200);
            }
        });

        //set layout manager
        mTopicListView.setLayoutManager(new GridLayoutManager(getActivity(), 1, LinearLayoutManager.VERTICAL, false));

        //set line decoration
        mTopicListView.addItemDecoration(new SimpleDividerItemDecoration(
                getActivity().getApplicationContext()
        ));

        mTopicListView.setAdapter(mTopicAdapter);

        //setup swipe refresh
        mSwipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshTopicList();
                mTopicAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            mActivity = (FragmentActivity) context;
        }
    }

    private void refreshTopicList() {
        Set<String> topicSet = new TreeSet<>();
        List<Message> messageList = ((BaseActivity) getActivity()).getMessageList();
        for (Message message : messageList) {
            topicSet.add(message.getTopic());
        }
        mTopicList = new ArrayList(topicSet);
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle(getString(R.string.title_topic));
        getRootActivity().hideMenuButton();
        refreshTopicList();
        mTopicAdapter.notifyDataSetChanged();
    }

    private void setTitle(String title) {
        getRootActivity().setToolbarTitle(title);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (mFragment != null) {
            mFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
