package com.training.android.undivided.GroupSender.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.training.android.undivided.GroupSender.Interface.IFragment;
import com.training.android.undivided.GroupSender.Model.Group;
import com.training.android.undivided.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupItemFragment extends MainFragmentAbstr implements IFragment {


    private ContactFragment mFragment;

    private EditText mGroupNameEt;

    private int mGroupIndex = -1;

    public GroupItemFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_group_item, container, false);
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        mGroupIndex = (args != null) ? args.getInt("index", -1) : -1;

        mGroupNameEt = view.findViewById(R.id.group_name);
        mGroupNameEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        mFragment = new ContactFragment();

        if (mGroupIndex != -1) {
            getRootActivity().setToolbarTitle(getString(R.string.title_edit_group));
            Group group = getRootActivity().getGroupList().get(mGroupIndex);
            mGroupNameEt.setText(group.getName());
            Bundle ContactArgs = new Bundle();
            ContactArgs.putInt("index", mGroupIndex);
            mFragment.setArguments(ContactArgs);
        } else {
            getRootActivity().setToolbarTitle(getString(R.string.title_create_group));
        }
        getChildFragmentManager().beginTransaction().replace(R.id.contact_frame, mFragment).commit();

        Toolbar toolbar = getRootActivity().getToolbar();
        getRootActivity().hideMenuButton();
        MenuItem saveButton = toolbar.getMenu().findItem(R.id.button_save);
        saveButton.setVisible(true);

        saveButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                final String name = mGroupNameEt.getText().toString().trim();

                if (name.isEmpty()) {
                    Toast.makeText(getActivity(), "group name can't be empty", Toast.LENGTH_SHORT).show();
                } else if (mGroupIndex == -1 && getRootActivity().checkDuplicateGroup(name)) {
                    Toast.makeText(getActivity(), "group " + name + " already exist", Toast.LENGTH_SHORT).show();
                } else if (mGroupIndex == -1) {
                    getRootActivity().getGroupList().add(new Group(name, mFragment.getSelectedContact()));
                    getRootActivity().saveGroup();
                    Toast.makeText(getActivity(), "group " + name + " has been saved", Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                } else {
                    getRootActivity().getGroupList().get(mGroupIndex).setName(name);
                    getRootActivity().getGroupList().get(mGroupIndex).setContacts(mFragment.getSelectedContact());
                    getRootActivity().saveGroup();
                    Toast.makeText(getActivity(), "group " + name + " has been saved", Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                }
                return false;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (mFragment != null) {
            mFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}
