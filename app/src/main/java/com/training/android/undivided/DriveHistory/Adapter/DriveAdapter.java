package com.training.android.undivided.DriveHistory.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.training.android.undivided.CallLog.Log;
import com.training.android.undivided.CallLog.LogAdapter;
import com.training.android.undivided.DriveHistory.Model.DriveModel;
import com.training.android.undivided.Group.Adapter.GroupAdapter;
import com.training.android.undivided.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maouusama on 2/18/2018.
 */

public class DriveAdapter extends ArrayAdapter<DriveModel> implements View.OnClickListener {

    private DriveModel currentdm;
    private ArrayList<DriveModel> dataSet;
    private List<DriveModel> listgm;
    private ViewHolder holder;
    private Context context;
    private int layout;

//    public DriveAdapter() {
//    }
//
//    public DriveAdapter(List<DriveModel> driveModel, Context context, int layout) {
//        this.listgm = driveModel;
//        this.context = context;
//        this.layout = layout;
//    }
//
//    @Override
//    public DriveAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        context = parent.getContext();
//        final View itemLayoutView = LayoutInflater.from(context).inflate(layout, parent, false);
//        holder = new DriveAdapter.ViewHolder(itemLayoutView);
//
//        return holder;
//    }
//
//    private int lastPosition = -1;
//
//    @Override
//    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
//        currentdm = listgm.get(position);
//
//        holder.mtvType.setText(currentdm.getDriveType());
//        holder.mtvStart.setText(currentdm.getStart_time());
//        holder.mtvEnd.setText(currentdm.getEnd_time());
//
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return listgm.size();
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return super.getItemId(position);
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return position;
//    }
private static class ViewHolder {
    TextView txtType;
    TextView txtStart;
    TextView txtEnd;
    ImageView ivDisp;
}

    public DriveAdapter(ArrayList<DriveModel> data, Context context) {
        super(context, R.layout.view_drive_history, data);
        this.dataSet = data;
        this.context = context;
    }

    @Override
    public void onClick(View view) {

    }

    private int lastPosition = -1;

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DriveModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.view_drive_history, parent, false);
            viewHolder.txtType = (TextView) convertView.findViewById(R.id.tvDriveType);
            viewHolder.txtStart = (TextView) convertView.findViewById(R.id.tvDriveStart);
            viewHolder.txtEnd = convertView.findViewById(R.id.tvDriveEnd);
            viewHolder.ivDisp = convertView.findViewById(R.id.ivDisp);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        if(position> lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, /*(position > lastPosition) ? R.anim.up_from_bottom :*/ R.anim.down_from_top);
            result.startAnimation(animation);
            lastPosition = position;
        }

        String type = dataModel.getDriveType();
        if(type.equals("Navigation Mode")) {
            viewHolder.ivDisp.setImageResource(R.drawable.ic_map_black_24dp);
        }
        viewHolder.txtType.setText(dataModel != null ? dataModel.getDriveType() : "Unknown Type");
        viewHolder.txtType.setTextColor(R.color.bb_darkBackgroundColor);
//        viewHolder.txtDate.setText(dataModel.getDate());
        viewHolder.txtStart.setText(dataModel != null ? dataModel.getStart_time() : "Unknown Start Time");
        viewHolder.txtStart.setTextColor(R.color.bb_darkBackgroundColor);
        viewHolder.txtEnd.setText(dataModel != null ? dataModel.getEnd_time() : "Unknown End Time");
        viewHolder.txtEnd.setTextColor(R.color.bb_darkBackgroundColor);
        // Return the completed view to render on screen
        return convertView;
    }

    //    public boolean isSelected(int index) {
//    }
//    public class ViewHolder extends RecyclerView.ViewHolder{
//
//        TextView mtvType;
//        TextView mtvStart;
//        TextView mtvEnd;
//
//        public ViewHolder() {
//            super(itemView);
//
//            mtvType = itemView.findViewById(R.id.tvDriveType);
//            mtvStart = itemView.findViewById(R.id.tvDriveStart);
//            mtvEnd = itemView.findViewById(R.id.tvDriveEnd);
//        }
//    }
}
