package com.training.android.undivided.GroupSender.Objects;

import android.graphics.Color;

/**
 * Created by Hillary Briones on 1/28/2018.
 */

public class ContactSend {
    public static final int STATE_NOT_SEND = Color.WHITE;
    public static final int STATE_SEND_SUCCESS = Color.GREEN;
    public static final int STATE_SEND_FAILED = Color.RED;

    private String mPhone;

    private int mState = STATE_NOT_SEND;

    private boolean mLoading = false;

    public ContactSend() {
    }

    public ContactSend(String phone) {
        mPhone = phone;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

    public int getState() {
        return mState;
    }

    public void setState(int state) {
        mState = state;
    }


    public boolean isLoading() {
        return mLoading;
    }

    public void setLoading(boolean loading) {
        mLoading = loading;
    }
}
