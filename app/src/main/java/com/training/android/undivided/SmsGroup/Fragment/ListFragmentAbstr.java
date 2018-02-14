package com.training.android.undivided.SmsGroup.Fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.training.android.undivided.SmsGroup.Adapter.GroupAdapter;
import com.training.android.undivided.SmsGroup.Adapter.MessageAdapter;
import com.training.android.undivided.SmsGroup.Model.Group;
import com.training.android.undivided.SmsGroup.Model.Message;

import java.util.List;

/**
 * Created by Hillary Briones on 2/7/2018.
 */

public abstract class ListFragmentAbstr extends MainFragmentAbstr {
    protected RecyclerView mMessageListView;

    protected MessageAdapter mMessageAdapter;

    protected List<Message> mMessageList;

    protected RecyclerView mGroupListView;

    protected GroupAdapter mGroupAdapter;

    protected List<Group> mGroupList;

    protected SwipeRefreshLayout mSwipeRefreshLayout;

    protected FrameLayout mEmptyFrame;

    protected RelativeLayout mDisplayFrame;

    protected void setTitle(String title) {
        getRootActivity().setToolbarTitle(title);
    }
}
