package com.training.android.undivided.GroupSender.Module.Send_Message;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.training.android.undivided.GroupSender.Base.BaseAdapter;
import com.training.android.undivided.GroupSender.Objects.ContactSend;
import com.training.android.undivided.R;

import java.util.List;

/**
 * Created by Hillary Briones on 1/28/2018.
 */

public class SendMessageAdapter extends BaseAdapter<SendMessageViewHolder, ContactSend> {

    private SendMessagePresenter mPresenter;

    public SendMessageAdapter(Context context, List<ContactSend> list) {
        super(context, list);
    }

    public SendMessageAdapter(Context context, List<ContactSend> contractsList, SendMessagePresenter presenter) {
        this(context, contractsList);
        mPresenter = presenter;
    }

    @Override
    public int getItemLayoutResId() {
        return R.layout.item_contact_send;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder createViewHolder(ViewGroup parent, int viewType, View view) {
        return new SendMessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SendMessageViewHolder holder, int position) {
        mPresenter.registerObserver(holder, getList().get(position));
        super.onBindViewHolder(holder, position);
    }

    @Override
    public void onViewAttachedToWindow(SendMessageViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.setRecycled(false);
    }

    @Override
    public void onViewDetachedFromWindow(SendMessageViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.setRecycled(true);
    }
}
