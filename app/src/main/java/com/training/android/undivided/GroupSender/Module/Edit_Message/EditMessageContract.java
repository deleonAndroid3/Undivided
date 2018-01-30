package com.training.android.undivided.GroupSender.Module.Edit_Message;

import android.content.Context;
import android.content.Intent;
import android.widget.Button;

import com.training.android.undivided.GroupSender.Base.BasePresenter;
import com.training.android.undivided.GroupSender.Base.BaseView;

import java.util.List;

/**
 * Created by Hillary Briones on 1/28/2018.
 */

public class EditMessageContract {
    public interface Presenter extends BasePresenter {

        Intent sendMessage(List<String> contracts);

        void saveOrganizationAndSignature(List<String> organizations, List<String> signatures);

        List<String> getOrganizationList();

        List<String> getSignatureList();

        void showPopup(Context context, Button button, List<String> dataList);

    }

    public interface View extends BaseView<Presenter> {

        String getOrganization();

        String getSignature();

        String getSMS();

    }
}
