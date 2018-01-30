package com.training.android.undivided.GroupSender.Module.Edit_Message;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.training.android.undivided.GroupSender.Base.BaseFragment;
import com.training.android.undivided.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditMessageFragment extends BaseFragment implements EditMessageContract.View  {

    private static final int REQUEST_SEND_MESSAGE = 1;
    private static final String KEY_SMS = "com.training.android.undivided.GroupSender.Module.Edit_Message.sms";
    private static final String KEY_CONTACT = "com.training.android.undivided.GroupSender.Module.Edit_Message.contact";

    private EditText mSmsEditText;
    private DrawerLayout mDrawer;
    private Button mOrganizationButton;
    private Button mSignatureButton;

    private EditMessageContract.Presenter mPresenter;

    private List<String> mOrganizationList;
    private List<String> mSignatureList;

    public static EditMessageFragment newInstance(String sms, List<String> contact) {

        Bundle args = new Bundle();
        if (null != sms)
            args.putString(KEY_SMS, sms);
        if (null != contact)
            args.putStringArrayList(KEY_CONTACT, (ArrayList<String>) contact);
        EditMessageFragment fragment = new EditMessageFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        setRetainInstance(true);

        mPresenter = new EditMessagePresenter(getActivity(), this);

        mOrganizationList = mPresenter.getOrganizationList();
        mSignatureList = mPresenter.getSignatureList();

    }

    @Override
    protected void createView(View view, Bundle savedInstanceState) {

        mDrawer = (DrawerLayout) find(R.id.drawer_layout);
        mSmsEditText = (EditText) find(R.id.type_message_edit_text);
        mOrganizationButton = (Button) find(R.id.organization_button);
        mSignatureButton = (Button) find(R.id.signature_button);
    }

    @Override
    public void onDestroyView() {
        mPresenter.saveOrganizationAndSignature(mOrganizationList, mSignatureList);
        super.onDestroyView();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_edit_message, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AddContactFragment fragment =
                (AddContactFragment) getChildFragmentManager()
                        .findFragmentById(R.id.add_contracts_fragment);
        switch (item.getItemId()) {
            case R.id.menu_item_send_message:
                if (TextUtils.isEmpty(mSmsEditText.getText().toString())) {
                    Snackbar.make(mSmsEditText,
                            R.string.message_cannot_be_empty,
                            Snackbar.LENGTH_SHORT).show();
                    return false;
                }

                if (fragment.getContractsPhone().isEmpty()) {
                    Snackbar.make(mSmsEditText,
                            R.string.contact_cannot_be_empty,
                            Snackbar.LENGTH_SHORT).show();
                    return false;
                }

                /* Send message to all contact */
                Intent intent = mPresenter.sendMessage(fragment.getContractsPhone());
                startActivityForResult(intent, REQUEST_SEND_MESSAGE);
                return true;
            case R.id.menu_item_add_contracts:
                List<String> contact = getArguments().getStringArrayList(KEY_CONTACT);
                if (null != contact) {
                    fragment.setPhoneNumberList(contact);
                }
                mDrawer.openDrawer(GravityCompat.END);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public boolean onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.END)) {
            mDrawer.closeDrawer(GravityCompat.END);
            return false;
        }
        return true;
    }

    @Override
    public void setPresenter(EditMessageContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_edit_message;
    }



    @Override
    protected void initEvent() {
        String sms = getArguments().getString(KEY_SMS);
        if (null != sms) {
            mSmsEditText.setText(sms);
        }

        mOrganizationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.showPopup(getActivity(), mOrganizationButton, mOrganizationList);
            }
        });

        mSignatureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.showPopup(getActivity(), mSignatureButton, mSignatureList);
            }
        });
    }

    @Override
    public String getOrganization() {
        String organization = mOrganizationButton.getText().toString();
        if (organization.equals(getResources().getString(R.string.unselected)))
            return "";
        return organization;
    }

    @Override
    public String getSignature() {
        String signature = mSignatureButton.getText().toString();
        if (signature.equals(getResources().getString(R.string.unselected)))
            return "";
        return signature;
    }

    @Override
    public String getSMS() {
        return mSmsEditText.getText().toString();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_SEND_MESSAGE) {
            getActivity().setResult(Activity.RESULT_OK, data);
            getActivity().finish();
        }
    }
}
