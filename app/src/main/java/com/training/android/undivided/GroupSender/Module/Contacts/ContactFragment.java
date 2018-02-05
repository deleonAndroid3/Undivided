package com.training.android.undivided.GroupSender.Module.Contacts;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
//import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.training.android.undivided.GroupSender.Base.BaseFragment;
import com.training.android.undivided.R;


public class ContactFragment extends BaseFragment {


    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    public static ContactFragment newInstance() {

        Bundle args = new Bundle();

        ContactFragment fragment = new ContactFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {

        return R.layout.fragment_contact2;
    }

    @Override
    protected void createView(View view, Bundle savedInstanceState) {
        mViewPager = (ViewPager) find(R.id.view_pager);
        mTabLayout = (TabLayout) find(R.id.tab_layout);
    }

    @Override
    protected void initEvent() {
        String[] titles = {
                getString(R.string.phone_contact),
                getString(R.string.history_contact),
                getString(R.string.group_contact)
        };

        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager(), titles);


        HistoryContactFragment historyContactFragment = HistoryContactFragment.newInstance();
        PhoneContactFragment phoneContactFragment = PhoneContactFragment.newInstance();
        GroupContactFragment groupContactFragment = GroupContactFragment.newInstance();

        adapter.addFragment(phoneContactFragment);
        adapter.addFragment(historyContactFragment);
        adapter.addFragment(groupContactFragment);

        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mViewPager.setCurrentItem(0);
    }}
