package com.training.android.undivided.GroupSender.Module.Contacts;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.training.android.undivided.GroupSender.Base.BaseAdapter;
import com.training.android.undivided.GroupSender.Objects.Message;
import com.training.android.undivided.R;

/**
 * Created by Hillary Briones on 1/28/2018.
 */

public class HistoryContactAdapter extends BaseAdapter<HistoryContactViewHolder, Message> {

    public HistoryContactAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemLayoutResId() {
        return R.layout.item_contact_history;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder createViewHolder(ViewGroup parent, int viewType, View view) {
        return new HistoryContactViewHolder(view);
    }
}
