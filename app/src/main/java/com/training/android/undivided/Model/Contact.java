package com.training.android.undivided.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Hillary Briones on 8/8/2017.
 */
//included in the group message sender
public class Contact
{
    @SerializedName("displayName")
    private String mDisplayName;

    @SerializedName("phoneNumber")
    private String mPhoneNumber;

    @SerializedName("checked")
    private boolean mChecked;

    public Contact(String displayName, String phoneNumber, boolean check) {
        mDisplayName = displayName;
        mPhoneNumber = phoneNumber;
        mChecked = check;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean checked) {

        this.mChecked = checked;
    }
}
