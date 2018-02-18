package com.training.android.undivided.DriveHistory;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.CallLog;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;
import android.widget.TextView;

import com.training.android.undivided.CallLog.Log;
import com.training.android.undivided.CallLog.LogAdapter;
import com.training.android.undivided.Database.DBHandler;
import com.training.android.undivided.DriveHistory.Adapter.DriveAdapter;
import com.training.android.undivided.DriveHistory.Model.DriveModel;
import com.training.android.undivided.R;

import java.util.ArrayList;
import java.util.Date;

public class DriveHistory extends AppCompatActivity {

    private TextView mTvCallLog;
    ArrayList<DriveModel> dataModels;
    ListView listView;
    private static DriveAdapter adapter;
    DBHandler dbHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive_history);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Drive History");

        dbHandler = new DBHandler(this);

//        mRecyclerView = findViewById(R.id.rvDriveHistory);

//
//        mAdapter = new DriveAdapter(dbHandler.getDriveHistory(), this, R.layout.view_drive_history);
//        mRecyclerView.setAdapter(mAdapter);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(DriveHistory.this));

        listView=(ListView)findViewById(R.id.rvDriveHistory);

        adapter= new DriveAdapter(dbHandler.getDriveHistory(),getApplicationContext());
        listView.setAdapter(adapter);

//        getDriveHistory();
    }

    private void getDriveHistory(){
        dataModels= new ArrayList<>();

        Cursor managedCursor = managedQuery(Uri.parse(dbHandler.DRIVE_HISTORY), null,
                null, null, null);


        int type = managedCursor.getColumnIndex(dbHandler.DRIVE_TYPE);
        int start = managedCursor.getColumnIndex(dbHandler.DRIVE_START);
        int end = managedCursor.getColumnIndex(dbHandler.DRIVE_END);

        while (managedCursor.moveToNext()) {

                dataModels.add(new DriveModel(managedCursor.getString(type), managedCursor.getString(start),
                        managedCursor.getString(end)));
            }
        managedCursor.close();


        return;
    }
}
