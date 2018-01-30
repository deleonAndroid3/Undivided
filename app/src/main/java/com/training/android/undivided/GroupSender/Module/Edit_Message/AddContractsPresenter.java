package com.training.android.undivided.GroupSender.Module.Edit_Message;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;

import com.training.android.undivided.GroupSender.Objects.ContactAdd;
import com.training.android.undivided.R;

/**
 * Created by Hillary Briones on 1/28/2018.
 */

public class AddContractsPresenter implements AddContractsContract.Presenter {


    public static final int MODE_PICK_FROM_PHONE = 1;
    public static final int MODE_PICK_FROM_INPUT = 2;

    private AddContractsContract.View mView;

    public AddContractsPresenter(AddContractsContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void switchMode(Context context, String mode) {
        String[] modes = context.getResources().getStringArray(R.array.pick_mode_spinner);
        if (mode.equals(modes[0])) {
            mView.setPickMode(MODE_PICK_FROM_PHONE);
        } else {
            mView.setPickMode(MODE_PICK_FROM_INPUT);
        }
    }

    @Override
    public ContactAdd requestContracts(Context context, Intent data) {
        ContactAdd contactAdd = new ContactAdd();
        Uri contractUri = data.getData();
        String[] queryFields = new String[] {
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };
        Cursor cursor = context.getContentResolver()
                .query(contractUri, queryFields, null, null, null);

        try {
            cursor.moveToFirst();
            /* get the data of column 0, that is contact' name */
            contactAdd.setName(cursor.getString(0));
            /* get the data of column 1, that is contact' phone number */
            String phoneNumber = cursor.getString(1);
            phoneNumber = phoneNumber.replace("-", "");
            phoneNumber = phoneNumber.replace(" ", "");
            contactAdd.setPhone(phoneNumber);
        }finally {
            cursor.close();
        }
        return contactAdd;
    }

    @Override
    public void showTheLastOf(RecyclerView recyclerView) {

    }

    @Override
    public void attach() {

    }

    @Override
    public void detach() {

    }

}
