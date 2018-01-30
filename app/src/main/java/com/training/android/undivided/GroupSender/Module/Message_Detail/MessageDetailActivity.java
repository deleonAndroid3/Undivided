package com.training.android.undivided.GroupSender.Module.Message_Detail;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.training.android.undivided.GroupSender.Base.FragmentContainerActivity;
import com.training.android.undivided.GroupSender.Objects.Message;
import com.training.android.undivided.R;

public class MessageDetailActivity extends FragmentContainerActivity {

    private static final String KEY_MESSAGE = "com.training.android.undivided.GroupSender.Module.Message_Detail.message";

    public static Intent newIntent(Context context, Message message) {
        Intent intent = new Intent(context, MessageDetailActivity.class);
        intent.putExtra(KEY_MESSAGE, message);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        Message message = (Message) getIntent().getSerializableExtra(KEY_MESSAGE);
        return MessageDetailFragment.newInstance(message);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_message_detail;
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.fragment_container;
    }

}
