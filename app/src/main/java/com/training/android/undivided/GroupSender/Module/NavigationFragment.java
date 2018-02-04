package com.training.android.undivided.GroupSender.Module;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.View;

import com.training.android.undivided.GroupSender.Base.BaseFragment;
import com.training.android.undivided.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationFragment extends BaseFragment
        implements View.OnClickListener {


    private OnNavigationItemSelectedListener mListener;

    private CardView mMessageList;
    private CardView mContracts;
    private CardView mSetting;

    public static Fragment newInstance() {

        Bundle args = new Bundle();

        NavigationFragment fragment = new NavigationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_navigation;
    }

    @Override
    protected void createView(View view, Bundle savedInstanceState) {
        mMessageList = (CardView) find(R.id.nav_message_list);
        mContracts = (CardView) find(R.id.nav_contracts);
        mSetting = (CardView) find(R.id.nav_setting);
    }

    @Override
    protected void initEvent() {
        mMessageList.setOnClickListener(this);
        mContracts.setOnClickListener(this);
        mSetting.setOnClickListener(this);
    }

    public void setNavigationItemSelectedListener(OnNavigationItemSelectedListener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (null != mListener) {
            mListener.onNavigationItemSelected(v);
        }
    }

    public interface OnNavigationItemSelectedListener {


        public void onNavigationItemSelected(@NonNull View item);
    }

}
