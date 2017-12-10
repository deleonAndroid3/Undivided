//package com.training.android.undivided.GroupSender.Adapter;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.training.android.undivided.GroupSender.Interface.IBaseActivity;
//import com.training.android.undivided.GroupSender.Model.SmsTask;
//import com.training.android.undivided.R;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by Hillary Briones on 11/1/2017.
// */
//
//public class OutboxAdapter extends RecyclerView.Adapter<OutboxAdapter.ViewHolder> {
//
//    List<SmsTask> taskList = new ArrayList<>();
//
//    private int selected_position = -1;
//
//    private IBaseActivity mActivity;
//
//    private Context mContext;
//
//    public OutboxAdapter(IBaseActivity activity, Context context, List<SmsTask> list) {
//        this.taskList = list;
//        this.mActivity = activity;
//        this.mContext = context;
//    }
//
//    @Override
//    public OutboxAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.outbox_item, parent, false);
//        return new OutboxAdapter.ViewHolder(v);
//    }
//
//    @Override
//    public void onBindViewHolder(OutboxAdapter.ViewHolder holder, final int position) {
//        SmsTask item = taskList.get(position);
//
//        holder.contactName.setText(item.getContact().getDisplayName());
//        holder.contactPhoneNumber.setText(item.getContact().getSelectedPhoneNumber());
//
//        switch (item.getStatus()) {
//            case SMS_DELIVERED:
//                holder.smsStatus.setImageResource(R.drawable.ic_action_tick);
//                break;
//            case SMS_SENT:
//                holder.smsStatus.setImageResource(R.drawable.ic_trending_up);
//                break;
//            case IDLE:
//            case PENDING:
//                holder.smsStatus.setImageResource(R.drawable.ic_action_reload);
//                break;
//            default:
//                holder.smsStatus.setImageResource(R.drawable.ic_action_cancel);
//                break;
//        }
//
//        if (selected_position == position) {
//            holder.itemView.setBackgroundColor(Color.parseColor("#e1e1e1"));
//            mActivity.getToolbar().getMenu().findItem(R.id.button_replay).setVisible(true);
//        } else {
//            holder.itemView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ripple));
//        }
//
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (selected_position == position) {
//                    unselect();
//                } else {
//                    notifyItemChanged(selected_position);
//                    selected_position = position;
//                    mActivity.getToolbar().getMenu().findItem(R.id.button_replay).setVisible(true);
//                    notifyItemChanged(selected_position);
//                }
//            }
//        });
//    }
//
//    public void unselect() {
//        mActivity.getToolbar().getMenu().findItem(R.id.button_replay).setVisible(false);
//        selected_position = -1;
//        notifyDataSetChanged();
//    }
//
//    public int getSelectedPosition() {
//        return selected_position;
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public int getItemCount() {
//        return taskList.size();
//    }
//
//    /**
//     * ViewHolder for Contact item
//     */
//    public class ViewHolder extends RecyclerView.ViewHolder {
//
//        /**
//         * contact layout
//         */
//        public LinearLayout layout;
//
//        /**
//         * contact name
//         */
//        public TextView contactName;
//
//        /**
//         * contact phone number
//         */
//        public TextView contactPhoneNumber;
//
//        public ImageView smsStatus;
//
//        /**
//         * ViewHolder for Contact item
//         *
//         * @param v
//         */
//        public ViewHolder(View v) {
//            super(v);
//            contactName = v.findViewById(R.id.contact_name);
//            contactPhoneNumber = v.findViewById(R.id.contact_phone);
//            smsStatus = v.findViewById(R.id.status);
//            layout = v.findViewById(R.id.group_layout);
//        }
//    }
//}
