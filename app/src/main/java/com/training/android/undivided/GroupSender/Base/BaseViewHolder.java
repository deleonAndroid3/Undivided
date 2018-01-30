package com.training.android.undivided.GroupSender.Base;

import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Hillary Briones on 1/28/2018.
 */

public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {
    private View mItemView;

    public BaseViewHolder(View itemView) {
        super(itemView);
        mItemView = itemView;
    }
    public abstract void bindHolder(T model);

    public View find(@IdRes int id) {
        return mItemView.findViewById(id);
    }
}
