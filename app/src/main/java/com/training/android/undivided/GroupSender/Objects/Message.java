package com.training.android.undivided.GroupSender.Objects;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by Hillary Briones on 1/28/2018.
 */

public class Message implements Serializable {
    private UUID mId;
    private String mDate;
    private String mTime;
    private String mTitle;
    private String mSms;
    private String mPhoneNumbers;

    public Message() {
        this(UUID.randomUUID());
    }

    public Message(UUID id) {
        mId = id;
        Date date = new Date();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Locale locale = Locale.getDefault(Locale.Category.FORMAT);

            mDate = new SimpleDateFormat("yyyy/MM/dd", locale).format(date);
            mTime = new SimpleDateFormat("EE HH:mm:ss", locale).format(date);
        } else {
            mDate = new SimpleDateFormat("yyyy/MM/dd").format(date);
            mTime = new SimpleDateFormat("EE HH:mm:ss").format(date);
        }

    }

    public static class Builder {

        private Message mMessage;

        public Builder() {
            mMessage = new Message();
        }

        public Builder(UUID uuid) {
            mMessage = new Message(uuid);
        }

        public Builder setTitle(String title) {
            mMessage.setTitle(title);
            return this;
        }

        public Builder setSms(String sms) {
            mMessage.setSms(sms);
            return this;
        }

        public Builder setPhoneNumbers(String phoneNumbers) {
            mMessage.setPhoneNumbers(phoneNumbers);
            return this;
        }

        public Message build() {
            return mMessage;
        }
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getSms() {
        return mSms;
    }

    public void setSms(String sms) {
        mSms = sms;
    }

    public String getPhoneNumbers() {
        return mPhoneNumbers;
    }

    public void setPhoneNumbers(String phoneNumbers) {
        mPhoneNumbers = phoneNumbers;
    }

    public UUID getId() {
        return mId;
    }


    public String getDate() {
        return mDate;
    }

    public String getTime() {
        return mTime;
    }

}
