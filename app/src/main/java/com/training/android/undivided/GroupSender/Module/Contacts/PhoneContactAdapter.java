package com.training.android.undivided.GroupSender.Module.Contacts;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.training.android.undivided.GroupSender.Base.BaseAdapter;
import com.training.android.undivided.GroupSender.Objects.Contact;
import com.training.android.undivided.R;

/**
 * Created by Hillary Briones on 1/28/2018.
 */

public class PhoneContactAdapter extends BaseAdapter<PhoneContactViewHolder, Contact> {
    public PhoneContactAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemLayoutResId() {
        return R.layout.item_contact;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder createViewHolder(ViewGroup parent, int viewType, View view) {
        return new PhoneContactViewHolder(view);
    }

}
