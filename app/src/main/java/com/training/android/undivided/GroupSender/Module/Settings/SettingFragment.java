package com.training.android.undivided.GroupSender.Module.Settings;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.training.android.undivided.GroupSender.Base.BaseFragment;
import com.training.android.undivided.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends BaseFragment {


    public static Fragment newInstance() {

        Bundle args = new Bundle();

        SettingFragment fragment = new SettingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void createView(View view, Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {

    }

}
