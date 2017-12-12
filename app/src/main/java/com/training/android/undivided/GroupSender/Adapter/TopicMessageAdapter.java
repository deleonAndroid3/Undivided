package com.training.android.undivided.GroupSender.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.training.android.undivided.GroupSender.Model.Message;
import com.training.android.undivided.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hillary Briones on 11/1/2017.
 */

public class TopicMessageAdapter  extends  RecyclerView.Adapter<TopicMessageAdapter.ViewHolder> {

    private List<Message> messageList = new ArrayList<>();

    public TopicMessageAdapter(List<Message> list) {
        this.messageList = list;
    }

    @Override
    public TopicMessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.topic_message_item, parent, false);
        return new TopicMessageAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TopicMessageAdapter.ViewHolder holder, final int position) {
        Message item = messageList.get(position);
        holder.messageTitle.setText("Title : " + item.getTitle());
        if (item.getBody().length() < 30) {
            holder.messageBody.setText(item.getBody().substring(0, item.getBody().length()));
        } else {
            holder.messageBody.setText(item.getBody().substring(0, 30));
        }
        holder.checkBox.setChecked(item.isChecked());
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    /**
     * ViewHolder for Contact item
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * contact layout
         */
        public LinearLayout layout;

        /**
         * message title
         */
        public TextView messageTitle;

        public TextView messageBody;

        public CheckBox checkBox;

        /**
         * ViewHolder for Contact item
         *
         * @param v
         */
        public ViewHolder(View v) {
            super(v);
            messageTitle = v.findViewById(R.id.message_title);
            messageBody = v.findViewById(R.id.message_body);
            checkBox = v.findViewById(R.id.contact_check);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    messageList.get(getPosition()).setChecked(isChecked);
                }
            });
            layout = v.findViewById(R.id.group_layout);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkBox.toggle();
                }
            });
        }
    }

}
