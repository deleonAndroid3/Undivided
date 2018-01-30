package com.training.android.undivided.Group.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.training.android.undivided.Group.Model.GroupModel;
import com.training.android.undivided.R;

import java.util.List;

/**
 * Created by Dyste on 10/4/2017.
 */

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {

    private GroupModel currentgm;
    private List<GroupModel> listgm;
    private ViewHolder holder;
    private Context context;
    private int layout;

    public GroupAdapter() {
    }

    public GroupAdapter(List<GroupModel> listgm, Context context, int layout) {
        this.listgm = listgm;
        this.context = context;
        this.layout = layout;
    }

    @Override
    public GroupAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();
        final View itemLayoutView = LayoutInflater.from(context).inflate(layout, parent, false);
        holder = new ViewHolder(itemLayoutView);


        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        currentgm = listgm.get(position);

        holder.mtvName.setText(currentgm.getGroupName());
        holder.mtvDesc.setText(currentgm.getGroupDesc());
        holder.mtvGroupMessage.setText(currentgm.getGroupMessage());
        holder.mtvRule1.setText(currentgm.getRule1() + "");
        holder.mtvRule2.setText(currentgm.getRule2() + "");
        holder.mtvRule3.setText(currentgm.getRule3() + "");
        holder.mtvRule4.setText(currentgm.getRule4() + "");
        holder.mtvRule5.setText(currentgm.getRule5() + "");
        holder.mtvRule6.setText(currentgm.getRule6() + "");
        holder.mtvRule7.setText(currentgm.getRule7() + "");


    }


    @Override
    public int getItemCount() {
        return listgm.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

//    public boolean isSelected(int index) {
//    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mtvName;
        TextView mtvDesc;
        TextView mtvGroupMessage;
        TextView mtvRule1;
        TextView mtvRule2;
        TextView mtvRule3;
        TextView mtvRule4;
        TextView mtvRule5;
        TextView mtvRule6;
        TextView mtvRule7;

        public ViewHolder(View itemView) {
            super(itemView);

            mtvName = itemView.findViewById(R.id.tvName);
            mtvDesc = itemView.findViewById(R.id.tvDesc);
            mtvGroupMessage = itemView.findViewById(R.id.tvGroupMessage);
            mtvRule1 = itemView.findViewById(R.id.tvRule1);
            mtvRule2 = itemView.findViewById(R.id.tvRule2);
            mtvRule3 = itemView.findViewById(R.id.tvRule3);
            mtvRule4 = itemView.findViewById(R.id.tvRule4);
            mtvRule5 = itemView.findViewById(R.id.tvRule5);
            mtvRule6 = itemView.findViewById(R.id.tvRule6);
            mtvRule7 = itemView.findViewById(R.id.tvRule7);
        }
    }

}
