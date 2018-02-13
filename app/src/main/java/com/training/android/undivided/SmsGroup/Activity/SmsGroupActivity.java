package com.training.android.undivided.SmsGroup.Activity;

import android.app.Dialog;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;
import com.training.android.undivided.R;
import com.training.android.undivided.SmsGroup.Fragment.GroupFragment;
import com.training.android.undivided.SmsGroup.Fragment.MessageFragment;
import com.training.android.undivided.SmsGroup.Fragment.OutboxFragment;
import com.training.android.undivided.SmsGroup.Fragment.TopicFragment;

public class SmsGroupActivity extends BaseActivity {

    private Dialog mDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setLayout(R.layout.activity_sms_group);
        super.onCreate(savedInstanceState);

        mFragment = new GroupFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_frame, mFragment).commit();

        mBottombar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                FragmentManager fm = getSupportFragmentManager();
                for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }
                fm.beginTransaction().remove(mFragment).commit();
                switch (tabId) {
                    case R.id.tab_group:
                        mFragment = new GroupFragment();
                        fm.beginTransaction().replace(R.id.fragment_frame, mFragment).commit();
                        break;
                    case R.id.tab_message:
                        mFragment = new MessageFragment();
                        fm.beginTransaction().replace(R.id.fragment_frame, mFragment).commit();
                        break;
                    case R.id.tab_send:
                        mFragment = new TopicFragment();
                        fm.beginTransaction().replace(R.id.fragment_frame, mFragment).commit();
                        break;
                    case R.id.tab_outbox:
                        mFragment = new OutboxFragment();
                        fm.beginTransaction().replace(R.id.fragment_frame, mFragment).commit();
                        break;
                }
            }
        });

        mBottombar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.tab_group:
                        break;
                    case R.id.tab_message:
                        break;
                    case R.id.tab_send:
                        break;
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }


    @Override
    public void setCurrentDialog(Dialog dialog) {
        mDialog = dialog;
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDialog != null) {
            mDialog.dismiss();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        mFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
