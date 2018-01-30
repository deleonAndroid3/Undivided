package com.training.android.undivided.GroupSender.Module.Send_Message;

import com.training.android.undivided.GroupSender.DB.message.MessageManager;
import com.training.android.undivided.GroupSender.Objects.ContactSend;
import com.training.android.undivided.GroupSender.Objects.Message;

import java.util.HashMap;

/**
 * Created by Hillary Briones on 1/28/2018.
 */

public class SendMessagePresenter implements SendMessageContract.Presenter {
    private SendMessageContract.View mView;
    private HashMap<SendMessageViewHolder, ContactSend> mHashMap = new HashMap<>();
    private HashMap<ContactSend, SendMessageViewHolder> mReverseHashMap = new HashMap<>();


    public SendMessagePresenter(SendMessageContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void registerObserver(SendMessageViewHolder viewHolder, ContactSend contactSend) {
        mHashMap.put(viewHolder, contactSend);
        mReverseHashMap.put(contactSend, viewHolder);
    }

    @Override
    public void removeObserver(SendMessageViewHolder viewHolder) {
        mHashMap.remove(viewHolder);
    }

    @Override
    public void setFailureState(SendMessageViewHolder viewHolder) {
        ContactSend contactSend = mHashMap.get(viewHolder);
        if (null != contactSend) {
            contactSend.setState(ContactSend.STATE_SEND_FAILED);
            if (!viewHolder.isRecycled())
                viewHolder.setFailedState();
        }
    }

    @Override
    public void setSuccessState(SendMessageViewHolder viewHolder) {
        ContactSend contactSend = mHashMap.get(viewHolder);
        if (null != contactSend) {
            contactSend.setState(ContactSend.STATE_SEND_SUCCESS);
            if (!viewHolder.isRecycled())
                viewHolder.setSuccessState();
        }
    }

    @Override
    public void setNotSendState(SendMessageViewHolder viewHolder) {

    }

    @Override
    public SendMessageViewHolder getViewHolder(ContactSend contactSend) {
        return mReverseHashMap.get(contactSend);
    }

    @Override
    public void saveMessage(Message message) {
        MessageManager manager = MessageManager.getInstance();
        manager.addMessage(message);
    }

    @Override
    public void attach() {

    }

    @Override
    public void detach() {

    }

}
