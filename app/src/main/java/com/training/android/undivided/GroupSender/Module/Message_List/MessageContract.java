package com.training.android.undivided.GroupSender.Module.Message_List;

import com.training.android.undivided.GroupSender.Base.BasePresenter;
import com.training.android.undivided.GroupSender.Base.BaseView;
import com.training.android.undivided.GroupSender.Objects.Message;

import java.util.List;

/**
 * Created by Hillary Briones on 1/28/2018.
 */

public class MessageContract {


    public interface Presenter extends BasePresenter {

        void queryMessage();

        void removeMessage(Message message);

    }

    public interface View extends BaseView<Presenter> {

        void setEmptyViewState(boolean state);

        void showMessages(List<Message> messages);

    }
}
