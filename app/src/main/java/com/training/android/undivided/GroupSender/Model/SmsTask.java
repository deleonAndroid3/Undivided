package com.training.android.undivided.GroupSender.Model;

/**
 * Created by Hillary Briones on 9/15/2017.
 */

public class SmsTask {
    private Contact mContact;

    private Message mMessage;

    private MessageStatus mStatus;

    private String mId;

    public SmsTask(Contact contact, Message message, MessageStatus status) {
        mContact = contact;
        mMessage = message;
        mStatus = status;
    }

    public String getid() {
        return mId;
    }

    public Contact getContact() {
        return mContact;
    }

    public Message getMessage() {
        return mMessage;
    }

    public MessageStatus getStatus() {
        return mStatus;
    }

    public void setStatus(MessageStatus status) {
        this.mStatus = status;
    }

    public void setId(String id) {
        mId = id;
    }
}
