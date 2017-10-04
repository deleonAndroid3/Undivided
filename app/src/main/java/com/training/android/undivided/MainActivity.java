package com.training.android.undivided;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.training.android.undivided.Group.ViewGroup;
import com.training.android.undivided.NavigationMode.Navigation;
import com.training.android.undivided.NavigationMode.SearchDestination;

public class MainActivity extends AppCompatActivity {


    public static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 11;
    AlertDialog ModeDialog;
    private ImageView mIvStart;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE,
                        Manifest.permission.READ_CONTACTS},
                MY_PERMISSIONS_REQUEST_CALL_PHONE);


        mIvStart = (ImageView) findViewById(R.id.ivDriveStart);

        initToolbar();
        setupDrawerLayout();


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
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
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
                        Intent navi = new Intent(MainActivity.this, Navigation.class);
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

    private void initToolbar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupDrawerLayout() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.drawer_profile :
                        break;
                    case R.id.drawer_view_group:
                        startActivity(new Intent(MainActivity.this, ViewGroup.class));
                        break;
                    case R.id.drawer_history:
                        break;
                }



                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

}
