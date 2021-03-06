package com.training.android.undivided.SmsGroup.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Hillary Briones on 2/7/2018.
 */

public class Group {
    @SerializedName("name")
    private String mName;

    @SerializedName("contacts")
    private List<Contact> mContactList;

    public Group(String name, List<Contact> contactList) {
        mName = name;
        mContactList = contactList;
    }

    public String getName() {
        return mName;
    }

    public List<Contact> getContactList() {
        return mContactList;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public void setContacts(List<Contact> contacts) {
        this.mContactList = contacts;
    }
}
