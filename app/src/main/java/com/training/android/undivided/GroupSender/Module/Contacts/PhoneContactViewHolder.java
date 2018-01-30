package com.training.android.undivided.GroupSender.Module.Contacts;

import android.view.View;
import android.widget.TextView;

import com.training.android.undivided.GroupSender.Base.BaseViewHolder;
import com.training.android.undivided.GroupSender.Objects.Contact;
import com.training.android.undivided.R;

/**
 * Created by Hillary Briones on 1/28/2018.
 */

public class PhoneContactViewHolder extends BaseViewHolder<Contact> {


    private TextView mNameTextView;
    private TextView mPhoneTextView;

    public PhoneContactViewHolder(View itemView) {
        super(itemView);
        mNameTextView = (TextView) find(R.id.contact_name_text_view);
        mPhoneTextView = (TextView) find(R.id.contact_phone_text_view);
    }
    @Override
    public void bindHolder(Contact model) {
        mNameTextView.setText(model.getName());
        mPhoneTextView.setText(model.getPhones().toString());
    }
}
