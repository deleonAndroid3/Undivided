package com.training.android.undivided.SmsGroup.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Hillary Briones on 2/7/2018.
 */

public class Contact {

    @SerializedName("displayName")
    private String mDisplayName;

    @SerializedName("phoneNumberSpinner")
    private List<String> mPhoneNumber;

    @SerializedName("selectedPhoneNumber")
    private String mSelectedPhoneNumber;

    @SerializedName("checked")
    private boolean mChecked;

    public Contact(String displayName, List<String> phoneNumber, boolean check, String selectedPhoneNumber) {
        mDisplayName = displayName;
        mPhoneNumber = phoneNumber;
        mChecked = check;
        mSelectedPhoneNumber = selectedPhoneNumber;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public List<String> getPhoneNumber() {
        return mPhoneNumber;
    }

    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean checked) {
        this.mChecked = checked;
    }

    public String getSelectedPhoneNumber() {
        return mSelectedPhoneNumber;
    }

    public void setSelectedPhoneNumber(String selectedPhoneNumber) {
        mSelectedPhoneNumber = selectedPhoneNumber;
    }
}
