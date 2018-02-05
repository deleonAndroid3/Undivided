package com.training.android.undivided.GroupSender.Base;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.training.android.undivided.GroupSender.Utils.RequestPermissions;

/**
 * Created by Hillary Briones on 1/28/2018.
 */

public abstract class BaseFragment extends Fragment {
    private static final int PERMISSION_REQUEST_CODE = (1 << 5) - 10;

    protected View mView;

    @LayoutRes
    protected abstract int getLayoutId();

    protected abstract void createView(View view, Bundle savedInstanceState);

    protected abstract void initEvent();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(getLayoutId(), container, false);
        createView(mView, savedInstanceState);
        initEvent();
        return mView;
    }

    public View find(@IdRes int id) {
        return mView.findViewById(id);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        RequestPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
