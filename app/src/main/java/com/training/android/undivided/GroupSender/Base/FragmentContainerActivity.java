package com.training.android.undivided.GroupSender.Base;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by Hillary Briones on 1/28/2018.
 */

public abstract class FragmentContainerActivity extends BaseActivity {
    private FragmentManager mFragmentManager;
    private Fragment mCurrentFragment;

    protected abstract Fragment createFragment();

    @LayoutRes
    protected abstract int getLayoutResId();

    @IdRes
    protected abstract int getFragmentContainerId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        mFragmentManager = getSupportFragmentManager();
        mCurrentFragment = mFragmentManager.findFragmentById(getFragmentContainerId());

        if (null == mCurrentFragment) {
            mCurrentFragment = createFragment();
            mFragmentManager.beginTransaction()
                    .add(getFragmentContainerId(), mCurrentFragment)
                    .commit();
        }
    }

    public void switchFragment(Fragment fragment) {
        if (mCurrentFragment == null
                || !fragment.getClass().getName().equals(mCurrentFragment.getClass().getName())) {
            FragmentTransaction fragmentTransaction = mFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out);
            if (!fragment.isAdded()) {

                fragmentTransaction
                        .hide(mCurrentFragment)
                        .add(getFragmentContainerId(), fragment)
                        .commit();
                mCurrentFragment = fragment;
            } else {

                fragmentTransaction
                        .hide(mCurrentFragment)
                        .show(fragment)
                        .commit();
                mCurrentFragment = fragment;
            }
        }
    }
}
