package com.training.android.undivided.GroupSender.Module.Send_Message;

import com.training.android.undivided.GroupSender.Base.BasePresenter;
import com.training.android.undivided.GroupSender.Base.BaseView;
import com.training.android.undivided.GroupSender.Objects.ContactSend;
import com.training.android.undivided.GroupSender.Objects.Message;

/**
 * Created by Hillary Briones on 1/28/2018.
 */

public class SendMessageContract {

    interface Presenter extends BasePresenter {

        void registerObserver(SendMessageViewHolder viewHolder, ContactSend contactSend);

        void removeObserver(SendMessageViewHolder viewHolder);

        void setFailureState(SendMessageViewHolder viewHolder);

        void setSuccessState(SendMessageViewHolder viewHolder);

        void setNotSendState(SendMessageViewHolder viewHolder);

        SendMessageViewHolder getViewHolder(ContactSend contactSend);

        void saveMessage(Message message);
    }

    interface View extends BaseView<Presenter> {

    }
}
