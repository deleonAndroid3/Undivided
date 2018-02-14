package com.training.android.undivided.SmsGroup.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.training.android.undivided.R;
import com.training.android.undivided.SmsGroup.Interface.IBaseActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragmentAbstr extends android.support.v4.app.Fragment{


    protected IBaseActivity getRootActivity() {
        return (IBaseActivity) getActivity();
    }

}
