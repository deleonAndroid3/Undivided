package com.training.android.undivided.SmsGroup.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Hillary Briones on 2/7/2018.
 */

public class SendConfiguration {

    @SerializedName("topic")
    private String mTopic;

    @SerializedName("groups")
    private List<Group> mGroup;

    @SerializedName("messages")
    private List<Message> mMessages;

    @SerializedName("mode")
    private SmsTaskMode mMode;

    public SendConfiguration(String topic, List<Group> group, List<Message> messages, SmsTaskMode mode) {
        mTopic = topic;
        mGroup = group;
        mMessages = messages;
        mMode = mode;
    }

    public List<Group> getGroups() {
        return mGroup;
    }

    public List<Message> getMessages() {
        return mMessages;
    }

    public SmsTaskMode getMode() {
        return mMode;
    }

    public String getTopic() {
        return mTopic;
    }
}
