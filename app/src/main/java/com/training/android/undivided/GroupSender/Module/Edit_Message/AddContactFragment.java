package com.training.android.undivided.GroupSender.Module.Edit_Message;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.training.android.undivided.GroupSender.Base.BaseFragment;
import com.training.android.undivided.GroupSender.Objects.ContactAdd;
import com.training.android.undivided.GroupSender.Utils.Events.ItemTouchHelperCallBack;
import com.training.android.undivided.GroupSender.Utils.Events.OnStartDragListener;
import com.training.android.undivided.GroupSender.Utils.Toasts;
import com.training.android.undivided.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddContactFragment extends BaseFragment
        implements AddContractsContract.View, OnStartDragListener {
    private static final int REQUEST_PICK_CONTRACT = 1;

    private AddContractsContract.Presenter mPresenter;
    private AddContractsAdapter mAdapter;
    private List<ContactAdd> mContactList;
    private ItemTouchHelper mItemTouchHelper;

    private Spinner mPickModeSpinner;
    private Button mPickFromPhoneButton;
    private LinearLayout mPickFromInputLayout;
    private EditText mInputEditText;
    private AppCompatImageView mAddContactView;
    private RecyclerView mContactRecyclerView;




    @Override
    protected int getLayoutId() {
        return R.layout.fragment_add_contact;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* init adapter */
        mContactList = new ArrayList<>(0);
        mAdapter = new AddContractsAdapter(getActivity(), mContactList);

        /* init itemTouchHelper */
        ItemTouchHelper.Callback callback = new ItemTouchHelperCallBack(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);

        /* init presenter */
        mPresenter = new AddContractsPresenter(this);
        setRetainInstance(true);
    }



    @Override
    protected void createView(View view, Bundle savedInstanceState) {
        mPickModeSpinner = (Spinner) find(R.id.pick_mode_spinner);
        mPickFromPhoneButton = (Button) find(R.id.add_from_phone_mode_button);
        mPickFromInputLayout = (LinearLayout) find(R.id.add_from_input_mode);
        mInputEditText = (EditText) find(R.id.input_edit_text);
        mAddContactView = (AppCompatImageView) find(R.id.add_contract_view);
        mContactRecyclerView = (RecyclerView) find(R.id.contracts_recycler_view);
    }

    @Override
    protected void initEvent() {
        mPickFromPhoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickContract = new Intent(Intent.ACTION_PICK,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(pickContract, REQUEST_PICK_CONTRACT);
            }
        });

        mAddContactView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = mInputEditText.getText().toString().trim();
                if (!TextUtils.isEmpty(phone)){
                    addContract(new ContactAdd(phone, ""));
                    mInputEditText.setText("");
                }
            }
        });

        String[] mItems = getResources().getStringArray(R.array.pick_mode_spinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_spinner_item, mItems);
        mPickModeSpinner.setAdapter(spinnerAdapter);

        mPickModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.switchMode(getActivity(), parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mContactRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mContactRecyclerView.setHasFixedSize(true);
        mContactRecyclerView.setAdapter(mAdapter);
        mItemTouchHelper.attachToRecyclerView(mContactRecyclerView);
    }
    public void setPhoneNumberList(List<String> phoneNumberList) {
        List<ContactAdd> list = new ArrayList<>();
        for (String phoneNumber : phoneNumberList) {
            list.add(new ContactAdd(phoneNumber, ""));
        }
        mContactList = list;
        mAdapter.setList(list);
    }

    @Override
    public void setPickMode(int mode) {
        if (mode == AddContractsPresenter.MODE_PICK_FROM_PHONE) {
            mPickFromPhoneButton.setVisibility(View.VISIBLE);
            mPickFromInputLayout.setVisibility(View.GONE);
        } else if (mode == AddContractsPresenter.MODE_PICK_FROM_INPUT) {
            mPickFromInputLayout.setVisibility(View.VISIBLE);
            mPickFromPhoneButton.setVisibility(View.GONE);
        }


    }

    @Override
    public void setPresenter(AddContractsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setInfoTips(String tips) {

    }

    @Override
    public List<String> getContractsPhone() {
        List<String> phone = new ArrayList<>();
        for (ContactAdd contactAdd : mContactList) {
            phone.add(contactAdd.getPhone());
        }
        return phone;
    }

    @Override
    public void addContract(ContactAdd contactAdd) {
        if (!mContactList.contains(contactAdd)) {
            mContactList.add(contactAdd);
            mAdapter.notifyDataSetChanged();
            mContactRecyclerView.smoothScrollToPosition(mContactList.size() - 1);
        } else {
            Toasts.showToast(getResources()
                    .getString(R.string.tips_you_have_added_this_contact));
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (REQUEST_PICK_CONTRACT == requestCode && null != data) {
            ContactAdd contactAdd = mPresenter.requestContracts(getActivity(), data);
            addContract(contactAdd);
        }
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

}
