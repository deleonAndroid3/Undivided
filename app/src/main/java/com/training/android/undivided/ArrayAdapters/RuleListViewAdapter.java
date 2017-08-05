package com.training.android.undivided.ArrayAdapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.training.android.undivided.AutoReplyActivity;
import  com.training.android.undivided.MainActivity;
import  com.training.android.undivided.Objects.Rule;
import  com.training.android.undivided.R;

import java.util.ArrayList;

/**
 * Created by Hillary Briones on 8/3/2017.
 */

public class RuleListViewAdapter extends BaseAdapter {
    private String logTag = "RuleListViewAdapter";


    private Activity activity;
    private ArrayList<Rule> data;
    private static LayoutInflater inflater=null;
    public Resources res;
    Rule tempValue=null;
    private String tName;
    private String tText;

    public RuleListViewAdapter(Activity a, ArrayList<Rule> d,Resources resLocal) {

        // Store passed values into their respective fields
        activity = a;
        data=d;
        res = resLocal;

        // Get layout inflater
        inflater = ( LayoutInflater )activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public int getCount() {
        return (data.size() < 0) ? 0 : data.size();
    }

    public Rule getItem(int position) {
        return data.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    // Holder for view elements
    public static class ViewHolder{

        private TextView nameText;
        private TextView descriptionText;
        private ToggleButton statusToggle;
        private ImageView onlyContactsImage;
        private ImageView smsImage;
        private ImageView callImage;

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if(convertView==null){

            // Inflate the view (row)
            vi = inflater.inflate(R.layout.rule_list_item_table, parent, false);

            // Match the view elements in the holder to their counterparts in the layout
            holder = new ViewHolder();
            holder.nameText = (TextView) vi.findViewById(R.id.list_textView_name);
            holder.descriptionText =(TextView) vi.findViewById(R.id.list_textView_description);
            holder.statusToggle = (ToggleButton) vi.findViewById(R.id.list_toggleButton_status);
            holder.onlyContactsImage = (ImageView) vi.findViewById(R.id.list_onlyContacts_image);
            holder.smsImage = (ImageView) vi.findViewById(R.id.list_sms_image);
            holder.callImage = (ImageView) vi.findViewById(R.id.list_call_image);


            vi.setTag( holder );
        }
        else
            holder=(ViewHolder)vi.getTag();

        if(data.size()<=0) //No data in the given array
        {}
        else
        {
            // Get the current Rule from the ArrayList
            tempValue = data.get( position );
            tName = tempValue.getName();
            tText = tempValue.getText();

            // Configure the layout according to the current Rule
            holder.nameText.setText(tName);
            holder.descriptionText.setText(tText);
            holder.statusToggle.setChecked((tempValue.getStatus() == 1));
            if (tempValue.getOnlyContacts() == 0)
                holder.onlyContactsImage.setVisibility(View.INVISIBLE);
            switch (tempValue.getReplyTo()) {
                case 0:
                    holder.smsImage.setVisibility(View.VISIBLE);
                    holder.callImage.setVisibility(View.VISIBLE);
                    break;
                case 1: // 1 = SMS, so hide call
                    holder.smsImage.setVisibility(View.VISIBLE);
                    holder.callImage.setVisibility(View.INVISIBLE);
                    break;
                case 2: // 1 = Call, so hide SMS
                    holder.smsImage.setVisibility(View.INVISIBLE);
                    holder.callImage.setVisibility(View.VISIBLE);
                    break;
            }

            // Set onLongClick for the row and onClick for the ToggleButton
            vi.setOnLongClickListener(new OnItemLongClickListener(tName, position, tText));
            holder.statusToggle.setOnCheckedChangeListener(new onItemToggleChangedListener(tName, position));
        } //end of else
        return vi;
    }


    private class onItemToggleChangedListener implements CompoundButton.OnCheckedChangeListener {
        private String mName;
        private int mPosition;

        //implement constructor to enable passing the position
        onItemToggleChangedListener(String name, int position){
            mName = name;
            mPosition = position;
        }
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            AutoReplyActivity sct = (AutoReplyActivity) activity;

            sct.onItemToggleClicked(mName, mPosition, isChecked);
        }
    }


    private class OnItemLongClickListener implements View.OnLongClickListener {
        private String mName;
        private String mText;
        private int mPosition;

        OnItemLongClickListener(String name, int position, String text){
            mName = name;
            mText = text;
            mPosition = position;
        }

        @Override
        public boolean onLongClick(View v) {
            AutoReplyActivity sct = (AutoReplyActivity) activity;
            sct.onLongItemClick(mName, mPosition, mText);
            return true;
        }

    }
}
