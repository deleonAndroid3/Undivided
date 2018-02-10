package com.training.android.undivided.SmsGroup.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Hillary Briones on 2/7/2018.
 */

public class Message {
    @SerializedName("title")
    private String mTitle;

    @SerializedName("topic")
    private String mTopic;

    @SerializedName("body")
    private String mBody;

    @SerializedName("selected")
    private boolean checked = true;

    public Message(String title, String topic, String body) {
        mTitle = title;
        mTopic = topic;
        mBody = body;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getTopic() {
        return mTopic;
    }

    public String getBody() {
        return mBody;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public void setTopic(String topic) {
        this.mTopic = topic;
    }

    public void setBody(String body) {
        this.mBody = body;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }
}
