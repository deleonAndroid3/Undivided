package com.training.android.undivided.GroupSender.Module.Message_Detail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.training.android.undivided.GroupSender.Base.BaseAdapter;
import com.training.android.undivided.R;

import java.util.List;

/**
 * Created by Hillary Briones on 1/28/2018.
 */

public class MessageDetailAdapter extends BaseAdapter<MessageDetailViewHolder, String> {

    public MessageDetailAdapter(Context context, List<String> list) {

        super(context, list);
    }

    @Override
    public int getItemLayoutResId() {
        return R.layout.item_phone_number;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder createViewHolder(ViewGroup parent, int viewType, View view) {
        return new MessageDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageDetailViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        /* make animation for item view*/
        Animation animation =
                AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim_up_to_down);
        animation.setStartOffset(position * 100);
        animation.setInterpolator(new AccelerateInterpolator());
        holder.itemView.startAnimation(animation);
    }

    @Override
    public void onViewDetachedFromWindow(MessageDetailViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }
}
