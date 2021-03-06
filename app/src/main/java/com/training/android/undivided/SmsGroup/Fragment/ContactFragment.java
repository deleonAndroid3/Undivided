package com.training.android.undivided.SmsGroup.Fragment;


import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.training.android.undivided.R;
import com.training.android.undivided.SmsGroup.Adapter.ContactAdapter;
import com.training.android.undivided.SmsGroup.Common.SimpleDividerItemDecoration;
import com.training.android.undivided.SmsGroup.Interface.IFragment;
import com.training.android.undivided.SmsGroup.Interface.IViewHolderCheckListener;
import com.training.android.undivided.SmsGroup.Model.Contact;
import com.training.android.undivided.SmsGroup.Model.Group;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class ContactFragment extends MainFragmentAbstr implements IFragment {

    private RecyclerView mContactListView;

    private ContactAdapter mContactAdapter;

    private List<Contact> mContactList;

    private final int REQUEST_PERMISSION_READ_CONTACTS = 1;

    private int mGroupIndex = -1;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    public ContactFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact3, container, false);
        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        mGroupIndex = (args != null) ? args.getInt("index", -1) : -1;

        mContactListView = view.findViewById(R.id.contact_list);

        mContactList = new ArrayList<>();

        mContactAdapter = new ContactAdapter(mContactList, getActivity(), new IViewHolderCheckListener() {
            @Override
            public void onContactsChecked(Contact contact, boolean checked) {

            }
        });

        //set layout manager
        mContactListView.setLayoutManager(new GridLayoutManager(getActivity(), 1, LinearLayoutManager.VERTICAL, false));

        //set line decoration
        mContactListView.addItemDecoration(new SimpleDividerItemDecoration(
                getActivity().getApplicationContext()
        ));

        mContactListView.setAdapter(mContactAdapter);

        //setup swipe refresh
        mSwipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getContacts();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                getActivity().requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_PERMISSION_READ_CONTACTS);
            } else {
                getContacts();
            }
        } else {
            getContacts();
        }
    }

    /**
     * Edit frame count text view and refresh adapter
     */
    private void notifyAdapter() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mContactAdapter.notifyDataSetChanged();
            }
        });
    }

    public void getContacts() {
        String phoneNumber = null;
        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor cursor = contentResolver.query(CONTENT_URI, null, null, null, null);

        mContactList = new ArrayList<>();

        if (cursor.getCount() > 0) {

            while (cursor.moveToNext()) {

                String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                String contact_id = cursor.getString(cursor.getColumnIndex(_ID));

                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[]{contact_id}, null);
                    List<String> phoneNumberList = new ArrayList<>();
                    while (phoneCursor.moveToNext()) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER)).replaceAll("\\s+", "");
                        boolean found = false;
                        for (int i = 0; i < phoneNumberList.size(); i++) {
                            if (phoneNumber.equals(phoneNumberList.get(i))) {
                                found = true;
                            }
                        }
                        if (!found) {
                            phoneNumberList.add(phoneNumber);
                        }
                    }
                    phoneCursor.close();
                    mContactList.add(new Contact(name, phoneNumberList, false, phoneNumberList.get(0)));
                }
            }

            //sort by topic
            if (mContactList.size() > 0) {
                Collections.sort(mContactList, new Comparator<Contact>() {
                    @Override
                    public int compare(final Contact object1, final Contact object2) {
                        return object1.getDisplayName().compareTo(object2.getDisplayName());
                    }
                });
            }

            if (mGroupIndex != -1) {
                Group group = getRootActivity().getGroupList().get(mGroupIndex);
                for (Contact groupContact : group.getContactList()) {
                    for (int i = 0; i < mContactList.size(); i++) {
                        if (groupContact.getDisplayName().equals(mContactList.get(i).getDisplayName()) &&
                                groupContact.getPhoneNumber().equals(mContactList.get(i).getPhoneNumber())) {
                            mContactList.get(i).setChecked(true);
                            mContactList.get(i).setSelectedPhoneNumber(groupContact.getSelectedPhoneNumber());
                            mContactList.add(0, mContactList.remove(i));
                        }
                    }
                }
            }
            mContactAdapter.setContactList(mContactList);
            notifyAdapter();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContacts();
                } else {
                    getActivity().finish();
                }
                break;
            }
        }
    }

    public List<Contact> getSelectedContact() {
        List<Contact> selectedContact = new ArrayList<>();

        for (Contact contact : mContactList) {
            if (contact.isChecked()) {
                selectedContact.add(contact);
            }
        }
        return selectedContact;
    }


}
