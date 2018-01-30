package com.training.android.undivided.GroupSender.Module.Contacts;


import android.os.Bundle;
//import  android.support.v4.app.Fragment;
import android.view.View;

import com.training.android.undivided.GroupSender.Base.BaseFragment;
import com.training.android.undivided.R;



public class GroupContactFragment extends BaseFragment {

    public static GroupContactFragment newInstance() {

        Bundle args = new Bundle();

        GroupContactFragment fragment = new GroupContactFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_group_contact;
    }

    @Override
    protected void createView(View view, Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {

    }
}
