package com.training.android.undivided.GroupSender.Module.Send_Message;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.training.android.undivided.GroupSender.Base.BaseViewHolder;
import com.training.android.undivided.GroupSender.Objects.ContactSend;
import com.training.android.undivided.GroupSender.Utils.CircularAnimUtil;
import com.training.android.undivided.R;

/**
 * Created by Hillary Briones on 1/28/2018.
 */

public class SendMessageViewHolder extends BaseViewHolder<ContactSend> {

    private TextView mContractsPhone;
    private ProgressBar mProgressBar;
    private CardView mPhoneCardView;
    private ContactSend mContactSend;
    private boolean mLoading;
    private boolean mRecycled;

    public SendMessageViewHolder(View itemView) {
        super(itemView);
        mContractsPhone = (TextView) find(R.id.contracts_phone_text_view);
        mProgressBar = (ProgressBar) find(R.id.progress_bar);
        mPhoneCardView = (CardView) find(R.id.phone_card_view);
    }

    @Override
    public void bindHolder(ContactSend model) {
        mContactSend = model;
        mContractsPhone.setText(model.getPhone());
        mProgressBar.setVisibility(View.INVISIBLE);
        mPhoneCardView.setCardBackgroundColor(model.getState());
    }

    public String getContent() {
        return mContractsPhone.getText().toString();
    }

    public void hide() {
        CircularAnimUtil.hide(mPhoneCardView);
        mProgressBar.setVisibility(View.VISIBLE);
    }


    public void show() {
        mProgressBar.setVisibility(View.INVISIBLE);
        CircularAnimUtil.show(mPhoneCardView);
    }

    public void setFailedState() {
        mPhoneCardView.setCardBackgroundColor(mContactSend.getState());
    }

    public void setSuccessState() {
        mPhoneCardView.setCardBackgroundColor(mContactSend.getState());
    }

    public boolean isLoading() {
        return mLoading;
    }

    public void setLoading(boolean loading) {
        mLoading = loading;
        mProgressBar.setVisibility(loading ? View.INVISIBLE : View.VISIBLE);
    }

    public boolean isRecycled() {
        return mRecycled;
    }

    public void setRecycled(boolean recycled) {
        mRecycled = recycled;
    }
}
