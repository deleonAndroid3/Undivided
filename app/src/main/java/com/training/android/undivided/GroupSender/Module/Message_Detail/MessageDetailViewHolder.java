package com.training.android.undivided.GroupSender.Module.Message_Detail;

import android.view.View;
import android.widget.TextView;

import com.training.android.undivided.GroupSender.Base.BaseViewHolder;
import com.training.android.undivided.R;

/**
 * Created by Hillary Briones on 1/28/2018.
 */

public class MessageDetailViewHolder extends BaseViewHolder<String> {
    private TextView mPhoneNumberTextView;

    public MessageDetailViewHolder(View itemView) {
        super(itemView);
        mPhoneNumberTextView = (TextView) find(R.id.phone_number_text_view);
    }

    @Override
    public void bindHolder(String model) {
        mPhoneNumberTextView.setText(model);
    }
}
