package com.training.android.undivided.Group;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.training.android.undivided.Group.Adapter.GroupAdapter;
import com.training.android.undivided.Group.Database.DBHandler;
import com.training.android.undivided.R;

public class ViewGroup extends AppCompatActivity {

    DBHandler dbHandler;
    private GroupAdapter mAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_group);

        mRecyclerView = (RecyclerView) findViewById(R.id.rvGroups);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Groups");

        dbHandler = new DBHandler(this);

        if (dbHandler.EmergencyGroupExists()) {
            //PID Found
            mAdapter = new GroupAdapter(dbHandler.getAllGroups(), this, R.layout.card_groups);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(ViewGroup.this));
        } else {
            //PID Not Found
            Intent addGroupIntent = new Intent(ViewGroup.this, AddGroup.class);
            addGroupIntent.putExtra("Title", "Emergency");
            addGroupIntent.putExtra("Desc", "This Contacts will automatically receive a text message if you press the emergency button");
            addGroupIntent.putExtra("Message", "I am involved in a car accident. Call me Immediately");
            startActivity(addGroupIntent);
        }

        /**
         * Swipe Left to Delete
         * */
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition(); //get position which is swipe

                if (direction == ItemTouchHelper.LEFT) {

                    if (position == 0) {
                        Snackbar.make(findViewById(R.id.activity_viewgroup),"Emeregency Contact cannot be deleted", Snackbar.LENGTH_SHORT).show();
                        mAdapter.notifyItemRemoved(position + 1);
                        mAdapter.notifyItemRangeChanged(position, mAdapter.getItemCount());
                    } else {

                        AlertDialog.Builder builder = new AlertDialog.Builder(ViewGroup.this);
                        builder.setMessage("Are you sure to delete?");

                        builder.setPositiveButton("REMOVE", new DialogInterface.OnClickListener() { //when click on DELETE
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dbHandler.deleteGroup(dbHandler.getAllGroups().get(position).getGroupName());
                                mAdapter.notifyItemRemoved(position);

                                recreate();
                            }
                        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mAdapter.notifyItemRemoved(position + 1);
                                mAdapter.notifyItemRangeChanged(position, mAdapter.getItemCount());
                            }
                        }).show();  //show alert dialog
                    }
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView); //set swipe to recylcerview
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_group_id:
                Intent i = new Intent(ViewGroup.this, AddGroup.class);
                startActivity(i);
                finish();
                break;
            case android.R.id.home:
                onBackPressed();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_group, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        recreate();
    }
}
