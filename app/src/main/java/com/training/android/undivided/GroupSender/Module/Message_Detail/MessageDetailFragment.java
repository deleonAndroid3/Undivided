package com.training.android.undivided.GroupSender.Module.Message_Detail;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.training.android.undivided.GroupSender.Base.BaseFragment;
import com.training.android.undivided.GroupSender.DB.message.MessageManager;
import com.training.android.undivided.GroupSender.Module.Edit_Message.EditMessageActivity;
import com.training.android.undivided.GroupSender.Objects.Message;
import com.training.android.undivided.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageDetailFragment extends BaseFragment {


    private static final String KEY_MESSAGE = "io.innofang.autosms.module.message_detail.message";
    private static final int REQUEST_SEND_AGAIN = 1;

    private Message mMessage;
    private MessageDetailAdapter mAdapter;
    private List<String> mList;
    private RecyclerView mPhoneNumbersRecyclerView;
    private TextView mSmsTextView;

    public static MessageDetailFragment newInstance(Message message) {

        Bundle args = new Bundle();
        args.putSerializable(KEY_MESSAGE, message);
        MessageDetailFragment fragment = new MessageDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message_detail;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mMessage = (Message) getArguments().getSerializable(KEY_MESSAGE);
        mList = new ArrayList<>();
        mAdapter = new MessageDetailAdapter(getActivity(), mList);
    }

    @Override
    protected void createView(View view, Bundle savedInstanceState) {
        mSmsTextView = (TextView) find(R.id.sms_text_view);
        mPhoneNumbersRecyclerView = (RecyclerView) find(R.id.phone_number_recycler_view);
        NestedScrollView nestedScrollView = (NestedScrollView) find(R.id.nested_scroll_view);
        nestedScrollView.bringChildToFront(mSmsTextView);
        nestedScrollView.setSmoothScrollingEnabled(true);
    }

    @Override
    protected void initEvent() {
        String subtitle = mMessage.getDate() + " " + mMessage.getTime();
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        if (null != actionBar) {
            actionBar.setTitle(mMessage.getTitle());
            actionBar.setSubtitle(subtitle);
        }

        String sms = mMessage.getSms();
        mSmsTextView.setText(sms);

        String[] phoneNumbers = mMessage.getPhoneNumbers().split(",");
        for (String phoneNumber : phoneNumbers) {
            mList.add(phoneNumber);
        }

//        mPhoneNumbersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mPhoneNumbersRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mPhoneNumbersRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_message_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_send_again:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View view = View.inflate(getActivity(), R.layout.layout_send_again, null);
                final CheckBox reserved_sms = (CheckBox) view.findViewById(R.id.reserved_sms_check_box);
                final CheckBox reserved_contact = (CheckBox) view.findViewById(R.id.reserved_contact_check_box);
                AlertDialog dialog = builder
                        .setTitle(R.string.send_again)
                        .setView(view)
                        .setCancelable(true)
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivityForResult(EditMessageActivity.newIntent(getActivity(),
                                        reserved_sms.isChecked() ? mSmsTextView.getText().toString() : null,
                                        reserved_contact.isChecked() ? mList : null),
                                        REQUEST_SEND_AGAIN);
                            }
                        }).create();
                dialog.show();
                return true;
            case R.id.menu_item_delete:
                if (null != mMessage) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle(R.string.tips)
                            .setMessage(R.string.confirm_to_delete)
                            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    MessageManager.getInstance().delMessage(mMessage);
                                    getActivity().finish();
                                }
                            })
                            .create()
                            .show();

                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_SEND_AGAIN) {
            getActivity().setResult(Activity.RESULT_OK, data);
            getActivity().finish();
        }
    }

}
