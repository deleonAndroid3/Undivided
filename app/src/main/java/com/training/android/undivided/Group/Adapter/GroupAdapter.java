package com.training.android.undivided.Group.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.training.android.undivided.Group.Model.GroupModel;
import com.training.android.undivided.Group.ViewCard;
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        currentgm = listgm.get(position);

        holder.mtvName.setText(currentgm.getGroupName());
        holder.mtvDesc.setText(currentgm.getGroupDesc());
        holder.mtvGroupMessage.setText(currentgm.getGroupMessage());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, position + "", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(context, ViewCard.class);
                i.putExtra("name", listgm.get(position).getGroupName());
                i.putExtra("desc", listgm.get(position).getGroupDesc());
                i.putExtra("message", listgm.get(position).getGroupMessage());
                i.putExtra("1", listgm.get(position).getRule1());
                i.putExtra("2", listgm.get(position).getRule2());
                i.putExtra("3", listgm.get(position).getRule3());
                i.putExtra("4", listgm.get(position).getRule4());

                context.startActivity(i);
            }
        });

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

        public ViewHolder(View itemView) {
            super(itemView);

            mtvName = itemView.findViewById(R.id.tvName);
            mtvDesc = itemView.findViewById(R.id.tvDesc);
            mtvGroupMessage = itemView.findViewById(R.id.tvGroupMessage);

        }
    }

}
