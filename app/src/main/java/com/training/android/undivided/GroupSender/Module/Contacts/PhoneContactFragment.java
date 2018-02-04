package com.training.android.undivided.GroupSender.Module.Contacts;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
//import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.training.android.undivided.GroupSender.Base.BaseFragment;
import com.training.android.undivided.GroupSender.Objects.Contact;
import com.training.android.undivided.GroupSender.Utils.RequestPermissions;
import com.training.android.undivided.R;

import java.util.List;


public class PhoneContactFragment extends BaseFragment implements PhoneContactContract.View{
    private PhoneContactAdapter mAdapter;
    private PhoneContactContract.Presenter mPresenter;

    private RecyclerView mPhoneContactRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public static PhoneContactFragment newInstance() {

        Bundle args = new Bundle();

        PhoneContactFragment fragment = new PhoneContactFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new PhoneContactAdapter(getActivity());
        mPresenter = new PhoneContactPresenter(this);
    }
    @Override
    public void setPresenter(PhoneContactContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showPhoneContact(List<Contact> contacts) {
        mAdapter.setList(contacts);
    }

    @Override
    public void setLoadingIndicator(final boolean active) {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(active);
            }
        });
    }

    @Override
    public void showEmptyOrError(String error) {

    }

    @Override
    public void setPullUpLoadingState(boolean state) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_phone_contact;
    }

    @Override
    protected void createView(View view, Bundle savedInstanceState) {
        mPhoneContactRecyclerView = (RecyclerView) find(R.id.phone_contact_recycler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) find(R.id.swipe_refresh_layout);
    }

    @Override
    protected void initEvent() {
        mPhoneContactRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mPhoneContactRecyclerView.setAdapter(mAdapter);


        mSwipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadingPhoneContact(getActivity());
            }
        });


        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            RequestPermissions.requestRuntimePermission(getActivity(),
                    new String[]{Manifest.permission.READ_CONTACTS},
                    new RequestPermissions.OnRequestPermissionsListener() {
                        @Override
                        public void onGranted() {
                            mPresenter.loadingPhoneContact(getActivity());
                        }

                        @Override
                        public void onDenied(List<String> deniedPermission) {
                            Snackbar.make(mPhoneContactRecyclerView,
                                    deniedPermission.toString() + " be denied",
                                    Snackbar.LENGTH_SHORT).show();
                        }
                    });
        } else {
            mPresenter.loadingPhoneContact(getActivity());
        }
    }
}
