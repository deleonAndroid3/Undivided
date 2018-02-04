package com.training.android.undivided.GroupSender.Module.Contacts;

import android.content.Context;

import com.training.android.undivided.GroupSender.Base.BasePresenter;
import com.training.android.undivided.GroupSender.Base.BaseView;
import com.training.android.undivided.GroupSender.Objects.Contact;

import java.util.List;

/**
 * Created by Hillary Briones on 1/28/2018.
 */

public class PhoneContactContract {
    public interface Presenter extends BasePresenter {
        void loadingPhoneContact(Context context);
    }

    public interface View extends BaseView<Presenter> {

        void showPhoneContact(List<Contact> contacts);

        void setLoadingIndicator(boolean active);

        void showEmptyOrError(String error);

        void setPullUpLoadingState(boolean state);

    }
}
