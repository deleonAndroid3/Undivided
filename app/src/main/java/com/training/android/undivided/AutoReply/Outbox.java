package com.training.android.undivided.AutoReply;
//import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import  com.training.android.undivided.AutoReply.ArrayAdapters.HTMLTextArrayAdapter;
import  com.training.android.undivided.AutoReply.Database.DatabaseManager;
import  com.training.android.undivided.AutoReply.Objects.SMS;
import com.training.android.undivided.R;

import java.util.ArrayList;

public class Outbox extends AppCompatActivity {

    private Context context;
    private static DatabaseManager dbManager;
    private static ArrayList<SMS> smsArray;
    private OutboxListFragment listFragment;
    private static ArrayAdapter<SMS> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listFragment = new OutboxListFragment();
        getSupportFragmentManager().beginTransaction().replace(android.R.id.content, listFragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.outbox_menu2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_delete:
                onDeleteAllClicked();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onDeleteAllClicked() {
        new android.app.AlertDialog.Builder(this)
                .setTitle("Delete All")
                .setPositiveButton(R.string.outbox_dialog_deleteAll, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // clear DB
                        new Runnable() {
                            @Override
                            public void run() {
                                dbManager.clearOutbox();
                                Toast.makeText(getApplicationContext(), "Successfully deleted all entries.", Toast.LENGTH_SHORT).show();
                            }
                        }.run();
                        // Refresh the view
                        smsArray.clear();
                        adapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton(R.string.outbox_dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                    }
                })
                .setMessage("Are you sure you want to delete all entries from the Outbox?")
                .show();
    }

    public static class OutboxListFragment extends ListFragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.activity_outbox, container, false);
        }

        public void onActivityCreated(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            dbManager = new DatabaseManager(getActivity());
            smsArray = dbManager.getSMSArray();
            adapter = new HTMLTextArrayAdapter<SMS>(getActivity(), android.R.layout.simple_list_item_1, smsArray);
            setListAdapter(adapter);
        }

        public void onListItemClick(ListView listView, View view, int position, long id) {
        }
    }
}
