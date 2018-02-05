package com.training.android.undivided.GroupSender.Module.Message_List;

import android.view.View;
import android.widget.TextView;

import com.training.android.undivided.GroupSender.Base.BaseViewHolder;
import com.training.android.undivided.GroupSender.Objects.Message;
import com.training.android.undivided.R;

/**
 * Created by Hillary Briones on 1/28/2018.
 */

public class MessageViewHolder extends BaseViewHolder<Message> {


    private TextView mDateTextView;
    private TextView mTitleTextView;
    private TextView mSmsTextView;
    private Message mMessage;

    public MessageViewHolder(View itemView) {
        super(itemView);
        mDateTextView = (TextView) find(R.id.date_text_view);
        mTitleTextView = (TextView) find(R.id.title_text_view);
        mSmsTextView = (TextView) find(R.id.sms_text_view);
    }

    @Override
    public void bindHolder(Message model) {
        mMessage = model;
        mDateTextView.setText(model.getDate());
        mTitleTextView.setText(model.getTitle());
        mSmsTextView.setText(model.getSms());
    }

    public Message getModel(){
        return mMessage;
    }

}
