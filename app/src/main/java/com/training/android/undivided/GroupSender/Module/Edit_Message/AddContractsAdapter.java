package com.training.android.undivided.GroupSender.Module.Edit_Message;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.training.android.undivided.GroupSender.Base.BaseAdapter;
import com.training.android.undivided.GroupSender.Objects.ContactAdd;
import com.training.android.undivided.GroupSender.Utils.Events.ItemTouchHelperAdapter;
import com.training.android.undivided.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by Hillary Briones on 1/28/2018.
 */

public class AddContractsAdapter extends BaseAdapter<AddContractsViewHolder, ContactAdd>
        implements ItemTouchHelperAdapter {
    public AddContractsAdapter(Context context, List<ContactAdd> list) {
        super(context, list);
    }

    @Override
    public int getItemLayoutResId() {
        return R.layout.item_contact_add;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder createViewHolder(ViewGroup parent, int viewType, View view) {
        return new AddContractsViewHolder(view);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(getList(), fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        getList().remove(position);
        notifyItemRemoved(position);
    }



}
