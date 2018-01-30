package com.training.android.undivided.GroupSender.Module.Contacts;

import com.training.android.undivided.GroupSender.Base.BasePresenter;
import com.training.android.undivided.GroupSender.Base.BaseView;
import com.training.android.undivided.GroupSender.Objects.Message;

import java.util.List;

/**
 * Created by Hillary Briones on 1/28/2018.
 */

public class HistoryContactContract {

    interface Presenter extends BasePresenter {
        void loadHistoryContact();
    }


    interface View extends BaseView<Presenter> {

        void showHistoryContactList(List<Message> list);

        void showEmpty(boolean state);

        void setLoadingIndicator(boolean active);

        void setPullUpLoadingState(boolean state);
    }
}
