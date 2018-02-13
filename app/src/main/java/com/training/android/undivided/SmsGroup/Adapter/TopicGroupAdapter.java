package com.training.android.undivided.SmsGroup.Adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.training.android.undivided.R;
import com.training.android.undivided.SmsGroup.Common.SimpleDividerItemDecoration;
import com.training.android.undivided.SmsGroup.Interface.IViewHolderCheckListener;
import com.training.android.undivided.SmsGroup.Model.Contact;
import com.training.android.undivided.SmsGroup.Model.Group;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hillary Briones on 2/7/2018.
 */

public class TopicGroupAdapter extends RecyclerView.Adapter<TopicGroupAdapter.ViewHolder> {
    private List<Group> groupList = new ArrayList<>();

    /**
     * Android context
     */
    private Context context = null;

    public TopicGroupAdapter(List<Group> list, Context context) {
        this.groupList = list;
        this.context = context;
    }

    @Override
    public TopicGroupAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.topic_contact_item, parent, false);
        return new TopicGroupAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TopicGroupAdapter.ViewHolder holder, final int position) {
        Group item = groupList.get(position);
        holder.groupName.setText("Group : " + item.getName());

        holder.contactList = item.getContactList();

        holder.adapter = new ContactAdapter(holder.contactList, context, new IViewHolderCheckListener() {
            @Override
            public void onContactsChecked(Contact contact, boolean checked) {
                List<Contact> contactList = groupList.get(position).getContactList();

                for (int i = 0; i < contactList.size(); i++) {
                    if (contactList.get(i).getDisplayName().equals(contact.getDisplayName()) &&
                            contactList.get(i).getPhoneNumber().equals(contact.getPhoneNumber())) {
                        contactList.get(i).setChecked(checked);
                    }
                }

            }
        });

        holder.contactListView.setLayoutManager(new GridLayoutManager(context, 1, LinearLayoutManager.VERTICAL, false));

        holder.contactListView.addItemDecoration(new SimpleDividerItemDecoration(
                context.getApplicationContext()
        ));

        holder.contactListView.setAdapter(holder.adapter);

        holder.contactListView.setNestedScrollingEnabled(false);
        holder.contactListView.setHasFixedSize(true);
    }

    public List<Group> getGroupList() {
        return groupList;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        public LinearLayout layout;


        public TextView groupName;

        public RecyclerView contactListView;

        public ContactAdapter adapter;

        public List<Contact> contactList;


        public ViewHolder(View v) {
            super(v);
            groupName = v.findViewById(R.id.group_name);
            layout = v.findViewById(R.id.group_layout);
            contactListView = v.findViewById(R.id.contact_list);
        }
    }
}
