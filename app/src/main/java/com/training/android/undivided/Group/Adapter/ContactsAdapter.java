package com.training.android.undivided.Group.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.training.android.undivided.Database.DBHandler;
import com.training.android.undivided.Group.Model.ContactsModel;
import com.training.android.undivided.R;

import java.util.ArrayList;

public class ContactsAdapter extends ArrayAdapter<ContactsModel> {

    Context mcontext;
    private ArrayList<ContactsModel> cmList;
    DBHandler dbHandler;

    public ContactsAdapter(@NonNull Context context, int resource, @NonNull ArrayList<ContactsModel> objects) {
        super(context, resource, objects);

        this.mcontext = context;
        this.cmList = objects;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final ContactsModel cModel = getItem(position);
        ViewHolder viewHolder = new ViewHolder();

        dbHandler = new DBHandler(mcontext);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.contacts_listview, parent, false);
        viewHolder.mtvName = convertView.findViewById(R.id.tvName);
        viewHolder.mtvContactNo = convertView.findViewById(R.id.tvContactNumber);


        if (viewHolder != null && cModel != null) {
            viewHolder.mtvName.setText(cModel.getContactName());
            viewHolder.mtvContactNo.setText(cModel.getContactNumber());
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView mtvName, mtvContactNo;
    }
}
