package com.training.android.undivided.GroupSender.Module.Message_List;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.training.android.undivided.GroupSender.Base.BaseFragment;
import com.training.android.undivided.GroupSender.Module.Message_Detail.MessageDetailActivity;
import com.training.android.undivided.GroupSender.Module.Search_Message.SearchMessageActivity;
import com.training.android.undivided.GroupSender.Objects.Message;
import com.training.android.undivided.GroupSender.Utils.Events.OnRecyclerItemClickListener;
import com.training.android.undivided.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends BaseFragment
        implements View.OnClickListener, MessageContract.View  {

    private MessageContract.Presenter mPresenter;
    private MessageAdapter mAdapter;
    private List<Message> mMessageList;

    private TextView mSearchView;
    private LinearLayout mEmptyView;
    private RecyclerView mMessagesRecyclerView;

    public static Fragment newInstance() {

        Bundle args = new Bundle();

        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMessageList = new ArrayList<>();
        mAdapter = new MessageAdapter(getActivity(), mMessageList);
        mPresenter = new MessagePresenter(this);
    }

    @Override
    protected void createView(View view, Bundle savedInstanceState) {
        mSearchView = (TextView) find(R.id.search_edit_text);
        mMessagesRecyclerView = (RecyclerView) find(R.id.message_recycler_view);
        mEmptyView = (LinearLayout) find(R.id.empty_view);
    }

    @Override
    protected void initEvent() {
        mSearchView.setOnClickListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mMessagesRecyclerView.setLayoutManager(layoutManager);
        mMessagesRecyclerView.setAdapter(mAdapter);
        mMessagesRecyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(mMessagesRecyclerView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
                MessageViewHolder viewHolder = (MessageViewHolder) vh;
                Message message = viewHolder.getModel();
                Intent intent = MessageDetailActivity.newIntent(getActivity(), message);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(RecyclerView.ViewHolder vh) {
                MessageViewHolder viewHolder = (MessageViewHolder) vh;
                Message message = viewHolder.getModel();
                int index = mMessageList.indexOf(message);
                mPresenter.removeMessage(message);
                mMessageList.remove(index);
                mAdapter.notifyItemRemoved(index);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.queryMessage();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_edit_text:
                startActivity(new Intent(
                                getContext(), SearchMessageActivity.class),
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                                getActivity(),
                                mSearchView,
                                getResources().getString(R.string.transition_name_search)
                        ).toBundle());
                break;
        }
    }

    @Override
    public void setPresenter(MessageContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setEmptyViewState(boolean state) {
        if (state) {
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mEmptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void showMessages(List<Message> messages) {
        mMessageList = messages;
        mAdapter.setList(messages);
    }

}
