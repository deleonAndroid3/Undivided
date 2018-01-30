package com.training.android.undivided.GroupSender.Module.Edit_Message;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.training.android.undivided.GroupSender.Base.FragmentContainerActivity;
import com.training.android.undivided.R;

import java.util.ArrayList;
import java.util.List;

public class EditMessageActivity extends FragmentContainerActivity {

    private EditMessageFragment mFragment;
    private static final String KEY_SMS = "com.training.android.undivided.GroupSender.Module.Edit_Message.sms";
    private static final String KEY_CONTACT = "com.training.android.undivided.GroupSender.Module.Edit_Message.contact";


    public static Intent newIntent(Context context, String sms, List<String> contact) {
        Intent intent = new Intent(context, EditMessageActivity.class);
        if (null != sms)
            intent.putExtra(KEY_SMS, sms);
        if (null != contact)
            intent.putStringArrayListExtra(KEY_CONTACT, (ArrayList<String>) contact);
        return intent;
    }


    @Override
    protected Fragment createFragment() {
        String sms = getIntent().getStringExtra(KEY_SMS);
        List<String> contact = getIntent().getStringArrayListExtra(KEY_CONTACT);
        mFragment = EditMessageFragment.newInstance(sms, contact);
        return mFragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_edit_message;
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.fragment_container;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public void onBackPressed() {
        if (mFragment.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}
