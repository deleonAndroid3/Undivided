//package com.training.android.undivided.Group;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.app.ActionBar;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.widget.Toast;
//
//import com.training.android.undivided.Group.Adapter.GroupAdapter;
//import com.training.android.undivided.Group.Database.DBHandler;
//import com.training.android.undivided.R;
//
//public class ViewGroup extends AppCompatActivity {

//    DBHandler dbHandler;
//    private GroupAdapter mAdapter;
//    private RecyclerView mRecyclerView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_view_group);
//
//        mRecyclerView = (RecyclerView) findViewById(R.id.rvGroups);
//
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setTitle("Groups");
//
//        dbHandler = new DBHandler(this);
//
//
//        mAdapter = new GroupAdapter(dbHandler.getAllGroups(), this, R.layout.card_groups);
//        mRecyclerView.setAdapter(mAdapter);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(ViewGroup.this));
//
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.add_group_id:
//                startActivity(new Intent(ViewGroup.this, AddGroup.class));
//                break;
//            case android.R.id.home:
//                onBackPressed();
//                return true;
//
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.add_group, menu);
//        return true;
//    }
//
//
//}
//