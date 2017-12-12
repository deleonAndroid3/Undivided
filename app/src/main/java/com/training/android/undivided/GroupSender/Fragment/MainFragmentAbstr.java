package com.training.android.undivided.GroupSender.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.training.android.undivided.GroupSender.Interface.IBaseActivity;
import com.training.android.undivided.R;


public class MainFragmentAbstr extends android.support.v4.app.Fragment {
    protected IBaseActivity getRootActivity() {
        return (IBaseActivity) getActivity();
    }
}
