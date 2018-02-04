package com.training.android.undivided.GroupSender.Module.Search_Message;


import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.training.android.undivided.GroupSender.Base.BaseFragment;
import com.training.android.undivided.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchMessageFragment extends BaseFragment {

    private EditText mSearchView;

    public static SearchMessageFragment newInstance() {

        Bundle args = new Bundle();
        SearchMessageFragment fragment = new SearchMessageFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search_message;
    }

    @Override
    protected void createView(View view, Bundle savedInstanceState) {
        mSearchView = (EditText) find(R.id.search_edit_text);

    }

    @Override
    protected void initEvent() {
        mSearchView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Drawable drawable = mSearchView.getCompoundDrawables()[0];
                if (null == drawable) {
                    return false;
                }
                if (event.getAction() != MotionEvent.ACTION_UP) {
                    return false;
                }
                if (event.getX() < drawable.getIntrinsicWidth()) {
                    if (Build.VERSION.SDK_INT >= 21) {
                        getActivity().finishAfterTransition();
                    } else {
                        getActivity().finish();
                    }
                }
                return false;
            }
        });
    }

}
