package com.training.android.undivided.SmsGroup.Interface;

import android.app.Dialog;
import android.support.v7.widget.Toolbar;

import com.training.android.undivided.SmsGroup.Model.Contact;
import com.training.android.undivided.SmsGroup.Model.Group;
import com.training.android.undivided.SmsGroup.Model.Message;
import com.training.android.undivided.SmsGroup.Model.SendConfiguration;
import com.training.android.undivided.SmsGroup.Model.SmsTask;
import com.training.android.undivided.SmsGroup.Model.SmsTaskMode;

import java.util.List;

/**
 * Created by Hillary Briones on 2/7/2018.
 */

public interface IBaseActivity  {


    void setCurrentDialog(Dialog dialog);

    List<Group> getGroupList();

    List<Message> getMessageList();

    void saveGroup();

    void saveMessages();

    boolean checkDuplicateGroup(String name);

    Toolbar getToolbar();

    void deleteGroup(String name);

    void setToolbarTitle(String title);

    void deleteMessage(String title);

    boolean checkDuplicateMessage(String title);

    void setDeletionListener(IDeletionListener listener);

    void startSmsTask(SmsTaskMode mode, List<Message> messageList, List<Contact> contactList);

    void startSmsTask(SmsTask task);

    void setStatusListener(ISmsStatusListener listener);

    List<SmsTask> getOutboxList();

    void deleteOutbox();

    List<Message> getMessagesForTopic(String topic);

    void hideMenuButton();

    void openOutbox();

    void saveSendConfiguration(SendConfiguration configuration);

    List<SendConfiguration> getSendConfigurationList();
}
