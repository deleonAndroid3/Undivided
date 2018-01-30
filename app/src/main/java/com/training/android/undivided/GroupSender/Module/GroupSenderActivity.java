package com.training.android.undivided.GroupSender.Module;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.View;

import com.training.android.undivided.GroupSender.Base.FragmentContainerActivity;
import com.training.android.undivided.GroupSender.Module.Contacts.ContactFragment;
import com.training.android.undivided.GroupSender.Module.Edit_Message.EditMessageActivity;
import com.training.android.undivided.GroupSender.Module.Message_List.MessageFragment;
import com.training.android.undivided.GroupSender.Module.Settings.SettingFragment;
import com.training.android.undivided.GroupSender.Utils.CircularAnimUtil;
import com.training.android.undivided.R;

public class GroupSenderActivity extends FragmentContainerActivity
        implements View.OnClickListener,
        NavigationFragment.OnNavigationItemSelectedListener {
    private static final int KEY_MESSAGE_LIST_FRAGMENT = 1;
    private static final int KEY_CONTRACTS_FRAGMENT = 2;
    private static final int KEY_SETTING_FRAGMENT = 3;
    private static final Integer REQUEST_EDIT_MESSAGE = 4;

    private SparseArray<Fragment> mFragmentSparseArray = new SparseArray<>();
    private DrawerLayout mDrawer;

    @Override
    protected Fragment createFragment() {
        return getFragmentFromCache(KEY_MESSAGE_LIST_FRAGMENT);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_group_sender;
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.fragment_container;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();
        fab.setOnClickListener(this);

        ((NavigationFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_fragment))
                .setNavigationItemSelectedListener(this);
    }

    private Fragment getFragmentFromCache(int key) {
        Fragment fragment = mFragmentSparseArray.get(key);
        if (null == fragment) {
            if (KEY_MESSAGE_LIST_FRAGMENT == key) {
                fragment = MessageFragment.newInstance();
            } else if (KEY_CONTRACTS_FRAGMENT == key) {
                fragment = ContactFragment.newInstance();
            } else if (KEY_SETTING_FRAGMENT == key) {
                fragment = SettingFragment.newInstance();
            }
            mFragmentSparseArray.put(key, fragment);
        }
        return fragment;
    }

    @Override
    public void onClick(View v) {
        CircularAnimUtil.startActivityForResult(this,
                EditMessageActivity.newIntent(this, null, null),
                REQUEST_EDIT_MESSAGE,
                v,
                R.color.colorAccent);
    }

    @Override
    public void onNavigationItemSelected(@NonNull View item) {

        switch (item.getId()) {
            case R.id.nav_message_list:
                switchFragment(getFragmentFromCache(KEY_MESSAGE_LIST_FRAGMENT));
                break;
            case R.id.nav_contracts:
                switchFragment(getFragmentFromCache(KEY_CONTRACTS_FRAGMENT));
                break;
            case R.id.nav_setting:
                switchFragment(getFragmentFromCache(KEY_SETTING_FRAGMENT));
                break;
            default:
                break;
        }
        mDrawer.closeDrawer(GravityCompat.START);
    }


    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
            return;
        }
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {

            mFragmentSparseArray.clear();
            super.onBackPressed();
            return;
        } else {
            Snackbar.make(mDrawer, R.string.click_again_to_exit, Snackbar.LENGTH_SHORT).show();
        }
        mBackPressed = System.currentTimeMillis();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_EDIT_MESSAGE) {
            switchFragment(getFragmentFromCache(KEY_MESSAGE_LIST_FRAGMENT));
        }
    }
}
