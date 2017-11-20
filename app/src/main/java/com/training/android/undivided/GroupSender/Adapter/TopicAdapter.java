package com.training.android.undivided.GroupSender.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.training.android.undivided.GroupSender.Interface.IBaseActivity;
import com.training.android.undivided.GroupSender.Interface.IViewHolderClickListener;
import com.training.android.undivided.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hillary Briones on 11/1/2017.
 */

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.ViewHolder> {
    List<String> topicList = new ArrayList<>();

    /**
     * Android context
     */
    private Context context = null;

    /**
     * click listener
     */
    private IViewHolderClickListener mListener;

    private IBaseActivity mActivity;

    public TopicAdapter(List<String> list, Context context, IBaseActivity activity, IViewHolderClickListener listener) {
        this.topicList = list;
        this.context = context;
        this.mListener = listener;
        this.mActivity = activity;
    }

    @Override
    public TopicAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.topic_item, parent, false);
        return new ViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String item = topicList.get(position);
        holder.topicName.setText(item + " (" +
                mActivity.getMessagesForTopic(item).size() +
                new String(Character.toChars(0x2709))
                + ")");
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return topicList.size();
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
         * topic name
         */
        public TextView topicName;

        /**
         * click listener
         */
        public IViewHolderClickListener mListener;

        /**
         * ViewHolder for Contact item
         *
         * @param v
         * @param listener
         */
        public ViewHolder(View v, IViewHolderClickListener listener) {
            super(v);
            mListener = listener;
            topicName = (TextView) v.findViewById(R.id.topic_name);
            layout = (LinearLayout) v.findViewById(R.id.topic_layout);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v);
        }
    }
}
