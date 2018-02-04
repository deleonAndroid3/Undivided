package com.training.android.undivided.GroupSender.Module.Send_Message;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.training.android.undivided.GroupSender.Base.FragmentContainerActivity;
import com.training.android.undivided.GroupSender.Objects.Message;
import com.training.android.undivided.R;

import java.util.ArrayList;

public class SendMessageActivity extends FragmentContainerActivity {

    private static final String KEY_MESSAGE = "com.training.android.undivided.GroupSender.Module.Send_Message.message";
    private static final String KEY_SMS = "com.training.android.undivided.GroupSender.Module.Send_Message.sms";
    private static final String KEY_CONTRACTS = "com.training.android.undivided.GroupSender.Module.Send_Message.contact";


    public static Intent newIntent(Context context, Message message, String sms, ArrayList<String> contracts) {
        Intent intent = new Intent(context, SendMessageActivity.class);
        intent.putExtra(KEY_MESSAGE, message);
        intent.putExtra(KEY_SMS, sms);
        intent.putExtra(KEY_CONTRACTS, contracts);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        Message message = (Message) getIntent().getSerializableExtra(KEY_MESSAGE);
        String sms = getIntent().getStringExtra(KEY_SMS);
        ArrayList<String> contracts = getIntent().getStringArrayListExtra(KEY_CONTRACTS);
        return SendMessageFragment.newInstance(message, sms, contracts);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_send_message;
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.fragment_container;
    }

}
