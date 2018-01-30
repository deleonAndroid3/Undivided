package com.training.android.undivided.GroupSender.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.training.android.undivided.GroupSender.Interface.IBaseActivity;
import com.training.android.undivided.GroupSender.Interface.IViewHolderClickListener;
import com.training.android.undivided.GroupSender.Model.Message;
import com.training.android.undivided.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hillary Briones on 11/1/2017.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>
{
    List<Message> messageList = new ArrayList<>();

    private Context context = null;
    private IViewHolderClickListener mListener;

    private int selected_position = -1;

    private IBaseActivity mActivity;

    public MessageAdapter(IBaseActivity activity, List<Message> list, Context context, IViewHolderClickListener listener) {
        this.messageList = list;
        this.context = context;
        this.mActivity = activity;
        this.mListener = listener;
    }

    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
        return new ViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Message item = messageList.get(position);
        holder.messageTitle.setText("" + item.getTitle());
        holder.messageTopic.setText("" + item.getTopic());

        if (selected_position == position) {
            holder.itemView.setBackgroundColor(Color.parseColor("#e1e1e1"));
            mActivity.getToolbar().getMenu().findItem(R.id.button_delete).setVisible(true);
        } else {
            mActivity.getToolbar().getMenu().findItem(R.id.button_delete).setVisible(false);
            holder.itemView.setBackground(ContextCompat.getDrawable(context, R.drawable.ripple));
        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                notifyItemChanged(selected_position);
                selected_position = position;
                notifyItemChanged(selected_position);
                return true;
            }
        });
    }

    public boolean isSelected(int position) {
        return (selected_position == position);
    }

    public void unselect() {
        selected_position = -1;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public int getSelectedItem() {
        return selected_position;
    }

    /**
     * ViewHolder for Contact item
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /**
         * contact layout
         */
        public LinearLayout layout;

        /**
         * message title
         */
        public TextView messageTitle;

        /**
         * topic name
         */
        public TextView messageTopic;

        /**
         * click listener
         */
        public IViewHolderClickListener mListener;



        /**
         * ViewHolder for Contact item
         *
         *
         */
        public ViewHolder(View v, IViewHolderClickListener listener) {
            super(v);
            mListener = listener;
            messageTitle = (TextView) v.findViewById(R.id.message_title);
            messageTopic = (TextView) v.findViewById(R.id.message_topic);
            layout = (LinearLayout) v.findViewById(R.id.group_layout);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v);
        }
    }

}
