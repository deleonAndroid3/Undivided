package com.training.android.undivided.GroupSender.Module.Search_Message;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.training.android.undivided.GroupSender.Base.FragmentContainerActivity;
import com.training.android.undivided.R;


public class SearchMessageActivity extends FragmentContainerActivity {


    @Override
    protected Fragment createFragment() {

        return SearchMessageFragment.newInstance();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_search_message;
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.fragment_container;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
