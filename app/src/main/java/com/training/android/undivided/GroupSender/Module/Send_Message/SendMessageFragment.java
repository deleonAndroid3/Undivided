package com.training.android.undivided.GroupSender.Module.Send_Message;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.training.android.undivided.GroupSender.Base.BaseFragment;
import com.training.android.undivided.GroupSender.Objects.ContactSend;
import com.training.android.undivided.GroupSender.Objects.Message;
import com.training.android.undivided.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class SendMessageFragment extends BaseFragment
        implements SendMessageContract.View{

    public static final String KEY_MESSAGE = "com.training.android.undivided.GroupSender.Module.Send_Message.message";
    public static final String KEY_SMS = "com.training.android.undivided.GroupSender.Module.Send_Message.sms";
    public static final String KEY_CONTRACTS = "com.training.android.undivided.GroupSender.Module.Send_Message.contact";

    private String mSms;
    private ArrayList<ContactSend> mContractList;
    private ArrayList<String> mPhoneNumbers;
    private Message mMessage;

    private SendMessageAdapter mAdapter;
    private SendMessageContract.Presenter mPresenter;
    private Button mSendButton;
    private RecyclerView mContractsRecyclerView;
    private TextView mMessageTextView;
    private LinearLayoutManager mLinearLayoutManager;

    public static SendMessageFragment newInstance(Message message, String sms, ArrayList<String> contracts) {

        Bundle args = new Bundle();
        args.putSerializable(KEY_MESSAGE, message);
        args.putString(KEY_SMS, sms);
        args.putStringArrayList(KEY_CONTRACTS, contracts);
        SendMessageFragment fragment = new SendMessageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_send_message;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMessage = (Message) getArguments().getSerializable(KEY_MESSAGE);
        mSms = getArguments().getString(KEY_SMS);
        mPhoneNumbers = getArguments().getStringArrayList(KEY_CONTRACTS);
        mContractList = new ArrayList<>();
        for (String s : mPhoneNumbers) {
            mContractList.add(new ContactSend(s));
        }
        mPresenter = new SendMessagePresenter(this);
        mAdapter = new SendMessageAdapter(getActivity(), mContractList, (SendMessagePresenter) mPresenter);
    }

    @Override
    protected void createView(View view, Bundle savedInstanceState) {
        mMessageTextView = (TextView) find(R.id.message_text_view);
        mSendButton = (Button) find(R.id.send_button);
        mContractsRecyclerView = (RecyclerView) find(R.id.contracts_recycler_view);
    }

    @Override
    protected void initEvent() {
        mMessageTextView.setText(mSms);

        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mContractsRecyclerView.setLayoutManager(mLinearLayoutManager);
        mContractsRecyclerView.setAdapter(mAdapter);

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        debug();
        updateSubtitle(position);
    }

    /*** DEBUG **/
    Random random = new Random();
    int position = 0;


    private void debug() {
        find(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position < mAdapter.getList().size()) {
                    mLinearLayoutManager.scrollToPositionWithOffset(position, 0);
                    SendMessageViewHolder viewHolder =
                            mPresenter.getViewHolder(mAdapter.getList().get(position));
                    if (null != viewHolder && !viewHolder.isRecycled())
                        viewHolder.hide();
                }
            }
        });

        find(R.id.end).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position < mAdapter.getList().size()) {
                    mLinearLayoutManager.scrollToPositionWithOffset(position, 0);
                    SendMessageViewHolder viewHolder =
                            mPresenter.getViewHolder(mAdapter.getList().get(position));
                    if (null != viewHolder) {
                        if (random.nextInt() % 2 == 0) {
                            mPresenter.setSuccessState(viewHolder);
                        } else {
                            mPresenter.setFailureState(viewHolder);
                        }
                        updateSubtitle(position + 1);
                        if (!viewHolder.isRecycled())
                            viewHolder.show();
                        if (position < mAdapter.getList().size())
                            position++;
                        if (position == mAdapter.getList().size()) {
                            mPresenter.saveMessage(mMessage);
                            showDialog();
                        }
                    }
                }

            }
        });


        find(R.id.restart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = 0;
            }
        });
    }

    private void showDialog() {
        new AlertDialog.Builder(getActivity())
                .setTitle(getResources().getString(R.string.tips))
                .setMessage(getResources().getString(R.string.message_have_been_sent))
                .setCancelable(true)
                .setNegativeButton(getResources().getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        getActivity().setResult(Activity.RESULT_OK, intent);
                        getActivity().finish();
                    }
                })
                .create()
                .show();

    }

    /***/


    @Override
    public void setPresenter(SendMessageContract.Presenter presenter) {
        mPresenter = presenter;
    }


    private void updateSubtitle(int haveSent) {
        String subtitle = getString(R.string.send_message_subtitle,
                mContractList.size(), haveSent);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

}
