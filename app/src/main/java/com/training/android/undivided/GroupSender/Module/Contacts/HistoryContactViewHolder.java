package com.training.android.undivided.GroupSender.Module.Contacts;

import android.view.View;
import android.widget.TextView;

import com.training.android.undivided.GroupSender.Base.BaseViewHolder;
import com.training.android.undivided.GroupSender.Objects.Message;
import com.training.android.undivided.R;

/**
 * Created by Hillary Briones on 1/28/2018.
 */

public class HistoryContactViewHolder extends BaseViewHolder<Message> {
    private TextView mDateTextView;
    private TextView mContactTextView;

    public HistoryContactViewHolder(View itemView) {
        super(itemView);

        mDateTextView = (TextView) itemView.findViewById(R.id.date_text_view);
        mContactTextView = (TextView) itemView.findViewById(R.id.contact_text_view);
    }

    @Override
    public void bindHolder(Message model) {
        mDateTextView.setText(model.getDate() + " " + model.getTime());
        mContactTextView.setText(model.getPhoneNumbers());
    }
}
