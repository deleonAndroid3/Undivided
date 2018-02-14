package com.training.android.undivided.SmsGroup.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.training.android.undivided.R;
import com.training.android.undivided.SmsGroup.Interface.IBaseActivity;
import com.training.android.undivided.SmsGroup.Interface.IViewHolderClickListener;
import com.training.android.undivided.SmsGroup.Model.Group;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hillary Briones on 2/7/2018.
 */

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {
    List<Group> groupList = new ArrayList<>();


    private Context context = null;

    private IViewHolderClickListener mListener;

    private int selected_position = -1;

    private IBaseActivity mActivity;

    public GroupAdapter(IBaseActivity activity, List<Group> list, Context context, IViewHolderClickListener listener) {
        this.groupList = list;
        this.context = context;
        this.mActivity = activity;
        this.mListener = listener;
    }

    @Override
    public GroupAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_item, parent, false);
        return new ViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Group item = groupList.get(position);
        holder.groupName.setText(item.getName() + " (" +
                item.getContactList().size() +
                new String(Character.toChars(0x1F464))
                + ")");

        if (selected_position == position) {
            holder.itemView.setBackgroundColor(Color.parseColor("#e1e1e1"));
            mActivity.getToolbar().getMenu().findItem(R.id.button_delete).setVisible(true);
        } else {
            holder.itemView.setBackground(ContextCompat.getDrawable(context, R.drawable.ripple));
        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                notifyItemChanged(selected_position);
                selected_position = position;
                mActivity.getToolbar().getMenu().findItem(R.id.button_delete).setVisible(true);
                notifyItemChanged(selected_position);
                return true;
            }
        });
    }

    public boolean isSelected(int position) {
        return (selected_position == position);
    }

    public void unselect() {
        mActivity.getToolbar().getMenu().findItem(R.id.button_delete).setVisible(false);
        selected_position = -1;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    public int getSelectedItem() {
        return selected_position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public LinearLayout layout;


        public TextView groupName;


        public IViewHolderClickListener mListener;


        public ViewHolder(View v, IViewHolderClickListener listener) {
            super(v);
            mListener = listener;
            groupName = v.findViewById(R.id.group_name);
            layout = v.findViewById(R.id.group_layout);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v);
        }
    }
}
