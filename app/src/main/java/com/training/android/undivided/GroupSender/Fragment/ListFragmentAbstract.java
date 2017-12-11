package com.training.android.undivided.GroupSender.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.training.android.undivided.Group.Adapter.GroupAdapter;
import com.training.android.undivided.GroupSender.Adapter.MessageAdapter;
import com.training.android.undivided.GroupSender.Model.Group;
import com.training.android.undivided.GroupSender.Model.Message;
import com.training.android.undivided.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragmentAbstract extends MainFragmentAbstr {


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
