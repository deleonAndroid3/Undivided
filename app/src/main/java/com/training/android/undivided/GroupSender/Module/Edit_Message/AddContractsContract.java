package com.training.android.undivided.GroupSender.Module.Edit_Message;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;

import com.training.android.undivided.GroupSender.Base.BasePresenter;
import com.training.android.undivided.GroupSender.Base.BaseView;
import com.training.android.undivided.GroupSender.Objects.ContactAdd;

import java.util.List;

/**
 * Created by Hillary Briones on 1/28/2018.
 */

public class AddContractsContract {

    public interface Presenter extends BasePresenter {

        /* the result after pick a contract */
        ContactAdd requestContracts(Context context, Intent data);

        /* show the last of list */
        void showTheLastOf(RecyclerView recyclerView);

        /* switch the mode of picking contact */
        void switchMode(Context context, String mode);
    }

    public interface View extends BaseView<Presenter> {

        /* set the pick mode */
        void setPickMode(int mode);

        /* set tips after pick a contract */
        void setInfoTips(String tips);

        /* get all contact that have picked */
        List<String> getContractsPhone();

        void addContract(ContactAdd contract);

    }
}
