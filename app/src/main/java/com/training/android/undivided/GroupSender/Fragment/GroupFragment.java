package com.training.android.undivided.GroupSender.Fragment;


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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


import com.training.android.undivided.GroupSender.Adapter.GroupAdapter;
import com.training.android.undivided.GroupSender.BaseActivity;
import com.training.android.undivided.GroupSender.Interface.IDeletionListener;
import com.training.android.undivided.GroupSender.Interface.IFragment;
import com.training.android.undivided.GroupSender.Interface.IViewHolderClickListener;
import com.training.android.undivided.GroupSender.SimpleDividerItemDecoration;
import com.training.android.undivided.R;


public class GroupFragment extends ListFragmentAbstract implements IFragment {

    private GroupItemFragment mFragment;

    public GroupFragment() {
    }

    protected FragmentActivity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.group_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setDeletionListener();

        mEmptyFrame = view.findViewById(R.id.waiting_frame);
        mDisplayFrame = view.findViewById(R.id.display_frame);

        if (getRootActivity().getGroupList().size() > 0) {
            mEmptyFrame.setVisibility(View.GONE);
            mDisplayFrame.setVisibility(View.VISIBLE);
        }

        mGroupListView = view.findViewById(R.id.group_list);

        mGroupList = getRootActivity().getGroupList();

        mGroupAdapter = new GroupAdapter(getRootActivity(), mGroupList, getActivity(), new IViewHolderClickListener() {
            @Override
            public void onClick(View view) {
                final int index = mGroupListView.getChildAdapterPosition(view);
                if (!mGroupAdapter.isSelected(index)) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mFragment = new GroupItemFragment();
                            Bundle args = new Bundle();
                            args.putInt("index", index);
                            mFragment.setArguments(args);

                            final FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.fragment_frame, mFragment, "GroupItem");
                            ft.addToBackStack(null);
                            ft.commit();
                        }
                    }, 200);
                } else {
                    mGroupAdapter.unselect();
                }
            }
        });

        //set layout manager
        mGroupListView.setLayoutManager(new GridLayoutManager(getActivity(), 1, LinearLayoutManager.VERTICAL, false));

        //set line decoration
        mGroupListView.addItemDecoration(new SimpleDividerItemDecoration(
                getActivity().getApplicationContext()
        ));

        mGroupListView.setAdapter(mGroupAdapter);

        //setup swipe refresh
        mSwipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mGroupList = ((BaseActivity) getActivity()).getGroupList();
                mGroupAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        setDeletionListener();
        setTitle(getString(R.string.title_group));

        getRootActivity().hideMenuButton();
        getRootActivity().getToolbar().getMenu().findItem(R.id.button_add_group).setVisible(true);

        mGroupList = ((BaseActivity) getActivity()).getGroupList();
        mGroupAdapter.notifyDataSetChanged();
    }

    private void setDeletionListener() {
        getRootActivity().setDeletionListener(new IDeletionListener() {
            @Override
            public void onDelete() {
                getRootActivity().deleteGroup(mGroupList.get(mGroupAdapter.getSelectedItem()).getName());
                getRootActivity().saveGroup();
                mGroupList = ((BaseActivity) getActivity()).getGroupList();
                mGroupAdapter.notifyDataSetChanged();

                if (getRootActivity().getGroupList().size() > 0) {
                    mEmptyFrame.setVisibility(View.GONE);
                    mDisplayFrame.setVisibility(View.VISIBLE);
                } else {
                    mEmptyFrame.setVisibility(View.VISIBLE);
                    mDisplayFrame.setVisibility(View.GONE);
                }
                getRootActivity().getToolbar().getMenu().findItem(R.id.button_delete).setVisible(false);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem buttonCreate = getRootActivity().getToolbar().getMenu().findItem(R.id.button_add_group);
        buttonCreate.setVisible(true);
        buttonCreate.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                final FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
                mFragment = new GroupItemFragment();
                ft.replace(R.id.fragment_frame, mFragment, "GroupItem");
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
