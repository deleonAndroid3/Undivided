package com.training.android.undivided.GroupSender.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.training.android.undivided.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragmentAbstract extends Fragment {


    public ListFragmentAbstract() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_fragment_abstract, container, false);
    }

}
