package com.training.android.undivided.GroupSender.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.training.android.undivided.GroupSender.Adapter.TopicGroupAdapter;
import com.training.android.undivided.GroupSender.Adapter.TopicMessageAdapter;
import com.training.android.undivided.GroupSender.Interface.IFragment;
import com.training.android.undivided.GroupSender.Model.Contact;
import com.training.android.undivided.GroupSender.Model.Group;
import com.training.android.undivided.GroupSender.Model.Message;
import com.training.android.undivided.GroupSender.Model.SendConfiguration;
import com.training.android.undivided.GroupSender.Model.SmsTaskMode;
import com.training.android.undivided.GroupSender.SimpleDividerItemDecoration;
import com.training.android.undivided.R;

import java.util.ArrayList;
import java.util.List;


public class TopicItemFragment extends MainFragmentAbstr implements IFragment {
    private String mTopic;

    private RecyclerView mGroupListView;

    private RecyclerView mMessageListView;

    private TopicGroupAdapter mGroupAdapter;

    private TopicMessageAdapter mMessageAdapter;

    private List<Group> mGroupList;

    private List<Message> mMessageList;

    private final static int REQUEST_PERMISSION_SEND_SMS = 2;

    private CheckBox mCheckbox;

    public TopicItemFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_topic_item, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        mTopic = (args != null) ? args.getString("topic", "") : "";

        getRootActivity().setToolbarTitle(getString(R.string.title_topic_send));

        ImageButton modeInfo = view.findViewById(R.id.one_to_one_info);

        modeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                dialog.cancel();
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Checking this mode will send the first message to the first contact, second message to second contact etc... It will loop on the contacts until all messages are sent")
                        .setPositiveButton("OK", dialogClickListener).show();
            }
        });

        mCheckbox = view.findViewById(R.id.one_to_one_cb);

        mGroupList = getRootActivity().getGroupList();

        TextView title = view.findViewById(R.id.topic_title);
        title.setText("Topic : " + mTopic);

        mGroupListView = view.findViewById(R.id.group_list);

        mGroupAdapter = new TopicGroupAdapter(mGroupList, getActivity());

        //set layout manager
        mGroupListView.setLayoutManager(new GridLayoutManager(getActivity(), 1, LinearLayoutManager.VERTICAL, false));

        //set line decoration
        mGroupListView.addItemDecoration(new SimpleDividerItemDecoration(
                getActivity().getApplicationContext()
        ));

        mGroupListView.setAdapter(mGroupAdapter);

        mGroupListView.setNestedScrollingEnabled(false);
        mGroupListView.setHasFixedSize(true);

        mMessageList = new ArrayList<>();

        List<Message> messageTemp = getRootActivity().getMessageList();

        for (Message message : messageTemp) {
            if (message.getTopic().equals(mTopic)) {
                mMessageList.add(message);
            }
        }

        mMessageListView = view.findViewById(R.id.message_list);

        mMessageAdapter = new TopicMessageAdapter(mMessageList);

        //set layout manager
        mMessageListView.setLayoutManager(new GridLayoutManager(getActivity(), 1, LinearLayoutManager.VERTICAL, false));

        //set line decoration
        mMessageListView.addItemDecoration(new SimpleDividerItemDecoration(
                getActivity().getApplicationContext()
        ));

        mMessageListView.setAdapter(mMessageAdapter);

        mMessageListView.setNestedScrollingEnabled(false);
        mMessageListView.setHasFixedSize(true);
    }

    private void retrieveConfig() {
        List<SendConfiguration> configurationList = getRootActivity().getSendConfigurationList();

        for (SendConfiguration config : configurationList) {

            if (config.getTopic().equals(mTopic)) {
                //populate fields according to saved config
                if (config.getMode() == SmsTaskMode.ONE_TO_ONE) {
                    mCheckbox.setChecked(true);
                }

                for (Group group : config.getGroups()) {
                    for (Group groupItem : mGroupList) {
                        // get common group
                        if (group.getName().equals(groupItem.getName())) {

                            //get commong contacts info
                            for (Contact contact : groupItem.getContactList()) {
                                for (Contact contactItem : group.getContactList()) {
                                    if (contactItem.getDisplayName().equals(contact.getDisplayName()) &&
                                            contactItem.getPhoneNumber().equals(contact.getPhoneNumber())) {
                                        contact.setChecked(contactItem.isChecked());
                                    }
                                }
                            }

                        }
                    }
                }
                for (Message message : config.getMessages()) {
                    for (Message messageItem : mMessageList) {
                        // get common message
                        if (message.getTitle().equals(messageItem.getTitle())) {
                            messageItem.setChecked(message.isChecked());
                        }
                    }
                }
                break;
            }
        }
        mMessageAdapter.notifyDataSetChanged();
        mGroupAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();

        getRootActivity().hideMenuButton();

        Toolbar toolbar = getRootActivity().getToolbar();
        MenuItem sendButton = toolbar.getMenu().findItem(R.id.button_send);
        sendButton.setVisible(true);

        sendButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (getActivity().checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                        getActivity().requestPermissions(new String[]{Manifest.permission.SEND_SMS}, REQUEST_PERMISSION_SEND_SMS);
                    } else {
                        sendTask();
                    }
                } else {
                    sendTask();
                }
                return false;
            }
        });

        retrieveConfig();
    }

    private List<Contact> getSelectedContacts() {
        List<Contact> contactSelected = new ArrayList<Contact>();

        List<Group> contactList = mGroupAdapter.getGroupList();

        for (int i = 0; i < contactList.size(); i++) {
            for (int j = 0; j < contactList.get(i).getContactList().size(); j++) {
                if (contactList.get(i).getContactList().get(j).isChecked()) {
                    contactSelected.add(contactList.get(i).getContactList().get(j));
                }
            }
        }
        return contactSelected;
    }

    private List<Message> getMessageToSend() {
        final List<Message> messageToSend = new ArrayList<Message>();
        List<Message> messageList = mMessageAdapter.getMessageList();

        for (Message message : messageList) {
            if (message.isChecked()) {
                messageToSend.add(message);
            }
        }
        return messageToSend;
    }

    private void sendTask() {

        final List<Contact> contactSelected = getSelectedContacts();
        final List<Message> messageToSend = getMessageToSend();

        if (contactSelected.size() == 0) {
            Toast.makeText(getActivity(), "you must select at least one contact", Toast.LENGTH_SHORT).show();
        } else if (messageToSend.size() == 0) {
            Toast.makeText(getActivity(), "you must select at least one message", Toast.LENGTH_SHORT).show();
        } else {

            final SmsTaskMode mode = mCheckbox.isChecked() ? SmsTaskMode.ONE_TO_ONE : SmsTaskMode.ALL_TO_ALL;

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            getRootActivity().deleteOutbox();
                            getRootActivity().startSmsTask(mode, messageToSend, contactSelected);
                            getRootActivity().openOutbox();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            String message = "";

            switch (mode) {
                case ALL_TO_ALL:
                    message = (messageToSend.size() * contactSelected.size()) + " messages will be sent";
                    break;
                case ONE_TO_ONE:
                    message = messageToSend.size() + " messages will be sent";
                    break;
            }
            builder.setMessage(message).setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        final List<Group> contactSelected = mGroupAdapter.getGroupList();
        final List<Message> messageToSend = mMessageAdapter.getMessageList();
        SmsTaskMode mode = mCheckbox.isChecked() ? SmsTaskMode.ONE_TO_ONE : SmsTaskMode.ALL_TO_ALL;

        SendConfiguration configuration = new SendConfiguration(mTopic, contactSelected, messageToSend, mode);
        getRootActivity().saveSendConfiguration(configuration);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_SEND_SMS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendTask();
                } else {
                    getActivity().finish();
                }
                break;
            }
        }
    }
}
