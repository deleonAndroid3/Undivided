package com.training.android.undivided.CallLog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.training.android.undivided.R;

import java.util.ArrayList;

/**
 * Created by Maouusama on 12/18/2017.
 */

public class LogAdapter extends ArrayAdapter<Log> implements View.OnClickListener {

    private ArrayList<Log> dataSet;
    Context mContext;

    private static class ViewHolder {
        TextView txtNum;
        TextView txtType;
        TextView txtDate;
        TextView txtTime;
        TextView txtDuration;
    }

    public LogAdapter(ArrayList<Log> data, Context context) {
        super(context, R.layout.log_row, data);
        this.dataSet = data;
        this.mContext = context;

    }

    @Override
    public void onClick(View view) {

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Log dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.log_row, parent, false);
            viewHolder.txtNum = (TextView) convertView.findViewById(R.id.tvNum);
            viewHolder.txtType = (TextView) convertView.findViewById(R.id.tvType);
            viewHolder.txtDate = (TextView) convertView.findViewById(R.id.tvDate);
            viewHolder.txtTime = (TextView) convertView.findViewById(R.id.tvTime);
            viewHolder.txtDuration = (TextView) convertView.findViewById(R.id.tvDuration);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        if(position> lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, /*(position > lastPosition) ? R.anim.up_from_bottom :*/ R.anim.down_from_top);
            result.startAnimation(animation);
            lastPosition = position;
        }
        viewHolder.txtNum.setText(dataModel.getNum());
        viewHolder.txtType.setText(dataModel.getType());
        viewHolder.txtDate.setText(dataModel.getDate());
        viewHolder.txtTime.setText(dataModel.getTime().toString());
        viewHolder.txtDuration.setText(dataModel.getDuration());
        // Return the completed view to render on screen
        return convertView;
    }
}
