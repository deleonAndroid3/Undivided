package com.training.android.undivided.GroupSender.Module.Message_List;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.training.android.undivided.GroupSender.Base.BaseAdapter;
import com.training.android.undivided.GroupSender.Objects.Message;
import com.training.android.undivided.R;

import java.util.List;

/**
 * Created by Hillary Briones on 1/28/2018.
 */

public class MessageAdapter extends BaseAdapter<MessageViewHolder, Message> {
    public MessageAdapter(Context context, List<Message> list) {
        super(context, list);
    }

    @Override
    public int getItemLayoutResId() {
        return R.layout.item_message;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder createViewHolder(ViewGroup parent, int viewType, View view) {
        return new MessageViewHolder(view);
    }
}
