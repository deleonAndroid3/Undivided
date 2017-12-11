package com.training.android.undivided.GroupSender.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.training.android.undivided.GroupSender.Interface.IFragment;
import com.training.android.undivided.R;


public class MessageFragment extends ListFragmentAbstract implements IFragment{

    private MessageItemFragment mFragment;
    private FragmentActivity mActivity;

public MessageFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group, container, false);
    }

}
