package com.training.android.undivided.GroupSender.Module.Contacts;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;

import com.training.android.undivided.GroupSender.Objects.Contact;
import com.training.android.undivided.GroupSender.Utils.CloseUtil;

import java.util.ArrayList;
import java.util.List;
import io.reactivex.functions.Consumer;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Hillary Briones on 1/28/2018.
 */

public class PhoneContactPresenter implements PhoneContactContract.Presenter  {

    private PhoneContactContract.View mView;

    public PhoneContactPresenter(PhoneContactContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void attach() {

    }

    @Override
    public void detach() {

    }

    @Override
    public void loadingPhoneContact(final Context context) {
        mView.setLoadingIndicator(true);
        Observable.create(new ObservableOnSubscribe<List<Contact>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Contact>> e) throws Exception {
                List<Contact> list = new ArrayList<>();
                ContentResolver cr = context.getContentResolver();
                Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
                try {
                    cursor.moveToFirst();
                    while (cursor.moveToNext()) {
                        Contact contact = new Contact();
                        int nameFieldColumnIndex = cursor.getColumnIndex(
                                ContactsContract.PhoneLookup.DISPLAY_NAME);
                        String contactName = cursor.getString(nameFieldColumnIndex);
                        contact.setName(contactName);
                        String ContactId = cursor.getString(
                                cursor.getColumnIndex(ContactsContract.Contacts._ID));
                        Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId,
                                null, null);

                        while (phone.moveToNext()) {
                            String phoneNumber = phone.getString(
                                    phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            phoneNumber = phoneNumber.replace("-", "");
                            phoneNumber = phoneNumber.replace(" ", "");
                            contact.getPhones().add(phoneNumber);
                        }
                        list.add(contact);
                    }
                } finally {
                    CloseUtil.closeQuietly(cursor);
                }
                e.onNext(list);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Contact>>() {
                    @Override
                    public void accept(@NonNull List<Contact> contacts) throws Exception {
                        mView.showPhoneContact(contacts);
                        mView.setLoadingIndicator(false);
                        mView.setPullUpLoadingState(false);
                    }
                });
    }
}
