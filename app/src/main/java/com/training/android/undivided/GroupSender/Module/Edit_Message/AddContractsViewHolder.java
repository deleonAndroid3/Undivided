package com.training.android.undivided.GroupSender.Module.Edit_Message;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.training.android.undivided.GroupSender.Base.BaseViewHolder;
import com.training.android.undivided.GroupSender.Objects.ContactAdd;
import com.training.android.undivided.GroupSender.Utils.Events.ItemTouchHelperViewHolder;
import com.training.android.undivided.R;

/**
 * Created by Hillary Briones on 1/28/2018.
 */

public class AddContractsViewHolder extends BaseViewHolder<ContactAdd>
        implements ItemTouchHelperViewHolder {


    private TextView mContractPhoneTextView;
    private TextView mContractNameTextView;

    public AddContractsViewHolder(View itemView) {
        super(itemView);
        mContractPhoneTextView = (TextView) find(R.id.contracts_phone_text_view);
        mContractNameTextView = (TextView) find(R.id.contracts_name_text_view);
    }

    @Override
    public void bindHolder(ContactAdd model) {
        mContractPhoneTextView.setText(model.getPhone());
        mContractNameTextView.setText(model.getName());
        if ("".equals(model.getName())) {
            mContractNameTextView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onItemSelected(){
    itemView.setBackgroundColor(Color.LTGRAY);
    }

    @Override
    public void onItemClear() {

    itemView.setBackgroundColor(0);
    }
}
