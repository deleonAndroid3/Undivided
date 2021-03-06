package com.training.android.undivided.SmsGroup.Fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.training.android.undivided.R;
import com.training.android.undivided.SmsGroup.Activity.BaseActivity;
import com.training.android.undivided.SmsGroup.Adapter.MessageAdapter;
import com.training.android.undivided.SmsGroup.Common.SimpleDividerItemDecoration;
import com.training.android.undivided.SmsGroup.Interface.IDeletionListener;
import com.training.android.undivided.SmsGroup.Interface.IFragment;
import com.training.android.undivided.SmsGroup.Interface.IViewHolderClickListener;
import com.training.android.undivided.SmsGroup.Model.Message;

import java.util.Collections;
import java.util.Comparator;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends ListFragmentAbstr implements IFragment {

    private MessageItemFragment mFragment;

    private FragmentActivity mActivity;

    public MessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.fragment_message2, container, false);
        return view;
    }
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setDeletionListener();

        mEmptyFrame = view.findViewById(R.id.waiting_frame);
        mDisplayFrame = view.findViewById(R.id.display_frame);

        if (getRootActivity().getMessageList().size() > 0) {
            mEmptyFrame.setVisibility(View.GONE);
            mDisplayFrame.setVisibility(View.VISIBLE);
        }

        mMessageListView = view.findViewById(R.id.message_list);

        mMessageList = ((BaseActivity) getActivity()).getMessageList();

        //sort by topic
        if (mMessageList.size() > 0) {
            Collections.sort(mMessageList, new Comparator<Message>() {
                @Override
                public int compare(final Message object1, final Message object2) {
                    return object1.getTopic().compareTo(object2.getTopic());
                }
            });
        }

        mMessageAdapter = new MessageAdapter(getRootActivity(), mMessageList, getActivity(), new IViewHolderClickListener() {
            @Override
            public void onClick(View view) {
                final int index = mMessageListView.getChildAdapterPosition(view);
                if (!mMessageAdapter.isSelected(index)) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mFragment = new MessageItemFragment();
                            Bundle args = new Bundle();
                            args.putInt("index", index);
                            mFragment.setArguments(args);

                            final FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.fragment_frame, mFragment, "MessageItem");
                            ft.addToBackStack(null);
                            ft.commit();
                        }
                    }, 200);
                } else {
                    mMessageAdapter.unselect();
                }
            }
        });

        //set layout manager
        mMessageListView.setLayoutManager(new GridLayoutManager(getActivity(), 1, LinearLayoutManager.VERTICAL, false));

        //set line decoration
        mMessageListView.addItemDecoration(new SimpleDividerItemDecoration(
                getActivity().getApplicationContext()
        ));

        mMessageListView.setAdapter(mMessageAdapter);

        //setup swipe refresh
        mSwipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mMessageList = ((BaseActivity) getActivity()).getMessageList();
                mMessageAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        MenuItem buttonCreate = getRootActivity().getToolbar().getMenu().findItem(R.id.button_add_message);

        buttonCreate.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                mFragment = new MessageItemFragment();
                ft.replace(R.id.fragment_frame, mFragment, "MessageItem");
                ft.addToBackStack(null);
                ft.commit();
                return false;
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

    @Override
    public void onResume() {
        super.onResume();
        setDeletionListener();
        setTitle(getString(R.string.title_message));

        getRootActivity().hideMenuButton();
        getRootActivity().getToolbar().getMenu().findItem(R.id.button_add_message).setVisible(true);

        mMessageList = ((BaseActivity) getActivity()).getMessageList();
        mMessageAdapter.notifyDataSetChanged();
    }

    private void setDeletionListener() {

        getRootActivity().setDeletionListener(new IDeletionListener() {
            @Override
            public void onDelete() {
                getRootActivity().deleteMessage(mMessageList.get(mMessageAdapter.getSelectedItem()).getTitle());
                getRootActivity().saveMessages();
                mMessageList = ((BaseActivity) getActivity()).getMessageList();
                mMessageAdapter.notifyDataSetChanged();

                if (getRootActivity().getMessageList().size() > 0) {
                    mEmptyFrame.setVisibility(View.GONE);
                    mDisplayFrame.setVisibility(View.VISIBLE);
                } else {
                    mEmptyFrame.setVisibility(View.VISIBLE);
                    mDisplayFrame.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (mFragment != null) {
            mFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}
