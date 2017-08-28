package com.training.android.undivided;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.training.android.undivided.AutoReply.AutoReplyActivity;
import com.training.android.undivided.NavigationMode.Navigation;
import com.training.android.undivided.NavigationMode.SearchDestination;

public class MainActivity extends AppCompatActivity {


    AlertDialog ModeDialog;
    private ImageView mIvStart;
    public static final int  MY_PERMISSIONS_REQUEST_CALL_PHONE=11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},
                MY_PERMISSIONS_REQUEST_CALL_PHONE);


        mIvStart = (ImageView) findViewById(R.id.ivDriveStart);


        /**
         *  SEND SMS FUNCTION FOR SPEECH TO TEXT REPLY.
         *  (IMPLEMENTED WITH BUTTON RIGHT NOW)
         */


        mIvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseMode();
            }
        });

        mIvStart.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent emergency = new Intent(MainActivity.this, Emergency.class);
                startActivity(emergency);
                return false;
            }
        });

    }

    protected void onDestroy() {
//        Intent intent = new Intent(this, BackgroundService.class);
//        startService(intent);
        super.onDestroy();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                Intent settings = new Intent(MainActivity.this, Settings.class);
                startActivity(settings);
                break;

            case R.id.menu_functions:
                Intent functions = new Intent(MainActivity.this, Functions.class);
                startActivity(functions);
                break;

        }


        return super.onOptionsItemSelected(item);
    }

    public void chooseMode() {

        final CharSequence[] modes = {"Safe Mode", "Navigation Mode", "Passenger Mode"};

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Driving Mode");
        builder.setSingleChoiceItems(modes, -1, null);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                int position = ModeDialog.getListView().getCheckedItemPosition();

                switch (position) {
                    case 0:
                        Toast.makeText(MainActivity.this, "Safe Mode Selected", Toast.LENGTH_SHORT).show();

                        break;
                    case 1:
                        Intent navi = new Intent(MainActivity.this, SearchDestination.class);
                        startActivity(navi);
                        Toast.makeText(MainActivity.this, "Navigation Mode Selected", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(MainActivity.this, "Passenger Mode Selected", Toast.LENGTH_SHORT).show();

                        break;
                }


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ModeDialog.dismiss();
            }
        });
        builder.setCancelable(false);
        ModeDialog = builder.create();
        ModeDialog.show();

    }

}
