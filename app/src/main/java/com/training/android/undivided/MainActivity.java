package com.training.android.undivided;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.training.android.undivided.BackgroundService.BackgroundService;
import com.training.android.undivided.CallLog.CallLogActivity;
import com.training.android.undivided.Database.DBHandler;
import com.training.android.undivided.Group.Model.ContactsModel;
import com.training.android.undivided.Group.ViewGroup;
import com.training.android.undivided.Models.EmergencyContactsModel;
import com.training.android.undivided.NavigationMode.Navigation;
import com.training.android.undivided.Models.TowingServicesModel;
import com.training.android.undivided.NavigationMode.SearchDestination;
import com.txusballesteros.bubbles.BubbleLayout;
import com.txusballesteros.bubbles.BubblesManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 11;
    AlertDialog ModeDialog, mAlertDialog;
    ArrayList<ContactsModel> cmodel;
    String message = "";
    private ImageView mIvStart;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private BubblesManager bubblesManager;
    private BubbleLayout bubbleView;
    private DBHandler dbHandler;

    private ArrayList<TowingServicesModel> tsmList = null;
    private ArrayList<EmergencyContactsModel> emcList;
    private boolean flag = false;

//    /*@Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch(requestCode)
//        {
//            case 1000:
//            {
//                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
//                    Toast.makeText(this, "Granted", Toast.LENGTH_SHORT).show();
//                else
//                    Toast.makeText(this, "Denied", Toast.LENGTH_SHORT).show();
//            }
//            return;
//        }
//    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Stetho.initializeWithDefaults(this);
        initializeBubblesManager();

        dbHandler = new DBHandler(this);
        cmodel = new ArrayList<>();


        message = dbHandler.getMessage("Emergency").getGroupMessage();

        Intent i = getIntent();
        if (i.getFlags() == Intent.FLAG_ACTIVITY_NEW_TASK)
            stopService(i);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE,
                        Manifest.permission.READ_CONTACTS},
                MY_PERMISSIONS_REQUEST_CALL_PHONE);
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
//                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
//                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//            requestPermissions(new String[]{
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    Manifest.permission.ACCESS_COARSE_LOCATION,
//                    Manifest.permission.ACCESS_FINE_LOCATION
//            },1000);
//        }
        drawerLayout = findViewById(R.id.drawer_layout);
        mIvStart = findViewById(R.id.ivDriveStart);

        drawerLayout.getBackground().setAlpha(80);

        initToolbar();
        setupDrawerLayout();


        mIvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ModeDialog != null && ModeDialog.isShowing()) {
                    Log.d("TAG", "Choose mode");
                } else {
                    chooseMode();
                }
            }
        });

        AddTowing();
    }

    @Override
    public void onBackPressed() {
        if (!flag)
            super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent(this, BackgroundService.class);
        startService(intent);
        super.onDestroy();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
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
                        Intent safeMode = new Intent(MainActivity.this, SafeMode.class);
                        startActivity(safeMode);
//                        if(!flag) {
//                            mIvStart.setImageResource(R.drawable.undivided_drivemode_logo_red);
//
//                            flag = true;
//                        }
//                        else {
//                            mIvStart.setImageResource(R.drawable.undivided_drivemode_logo);
//                            flag = false;
//                            stopLockTask();
//                            return;
//                        }
//                        Toast.makeText(MainActivity.this, "Safe Mode Selected", Toast.LENGTH_SHORT).show();
//                        ComponentName mDeviceAdmin;
//                        DevicePolicyManager myDevicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
//                        mDeviceAdmin = new ComponentName(MainActivity.this, MainActivity.class);
//
//                        if (myDevicePolicyManager.isDeviceOwnerApp(MainActivity.this.getPackageName())) {
//                            // Device owner
//                            String[] packages = {MainActivity.this.getPackageName()};
//                            myDevicePolicyManager.setLockTaskPackages(mDeviceAdmin, packages);
//                        } else {
//                            Toast.makeText(MainActivity.this, "Failed.", Toast.LENGTH_SHORT).show();
//                        }
//
//                        if (myDevicePolicyManager.isLockTaskPermitted(MainActivity.this.getPackageName())) {
//                            // Lock allowed
//                            // NOTE: locking device also disables notification
//                            startLockTask();
//                        } else {
//                            Toast.makeText(MainActivity.this, "2nd Failed.", Toast.LENGTH_SHORT).show();
//                            startLockTask();
//                        }
                        break;
                    case 1:
                        Intent navi = new Intent(MainActivity.this, Navigation.class);
                        startActivity(navi);
                        Toast.makeText(MainActivity.this, "Navigation Mode Selected", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(MainActivity.this, "Passenger Mode Selected", Toast.LENGTH_SHORT).show();
                        if (bubbleView == null) {
                            addNewBubble();
                        }

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
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupDrawerLayout() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.drawer_view_group:
                        menuItem.setChecked(false);
                        startActivity(new Intent(MainActivity.this, ViewGroup.class));
                        break;
                    case R.id.drawer_history:
                        menuItem.setChecked(false);
                        Intent callLog = new Intent(MainActivity.this, CallLogActivity.class);
                        startActivity(callLog);
                        break;
                }

                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    private void addNewBubble() {
        bubbleView = (BubbleLayout) LayoutInflater.from(MainActivity.this).inflate(R.layout.bubble_layout, null);

        bubbleView.setOnBubbleRemoveListener(new BubbleLayout.OnBubbleRemoveListener() {
            @Override
            public void onBubbleRemoved(BubbleLayout bubble) {

            }
        });

        bubbleView.setOnBubbleClickListener(new BubbleLayout.OnBubbleClickListener() {
            @Override
            public void onBubbleClick(BubbleLayout bubble) {
                if (mAlertDialog != null && mAlertDialog.isShowing()) {
                    Log.e("TAG", "onBubbleClick: ");
                } else {
                    showAlertSOS();
                }
            }
        });

        bubbleView.setShouldStickToWall(true);
        bubblesManager.addBubble(bubbleView, 60, 20);
    }

    private void initializeBubblesManager() {
        bubblesManager = new BubblesManager.Builder(this)
                .setTrashLayout(R.layout.bubble_trash_layout)
                .build();
        bubblesManager.initialize();
    }

    public void showAlertSOS() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Emergency SOS")
                .setMessage("Do you want to send Emergency SMS and call ERUF")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                cmodel = dbHandler.getEmergencyContacts();

                                int i = 0;

                                while (i != cmodel.size()) {
                                    try {
                                        SmsManager smsManager = SmsManager.getDefault();
                                        smsManager.sendTextMessage(cmodel.get(i).getContactNumber(), null, message, null, null);

                                        Toast.makeText(getApplicationContext(), "Sent!", Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {
                                        Toast.makeText(getApplicationContext(), "Failed.", Toast.LENGTH_SHORT).show();
                                    }
                                    i++;
                                }

                                String phone = "+639234152360";
                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                                startActivity(intent);

                                finish();
                            }
                        }, 1000);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                });

        mAlertDialog = builder.create();
        mAlertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        mAlertDialog.show();

    }

    public void AddTowing() {

        tsmList = new ArrayList<>();

        tsmList.add(new TowingServicesModel("Tri-J Service Center", "Sangi, Cebu City, 6046 Cebu", "10.248783,123.802212", "(032) 272 8975"));

        tsmList.add(new TowingServicesModel("Camel Towing Services", "Codoy Hernan Cortez, NS Cabahug St, Mandaue City, 6014 Cebu", "10.346091, 123.926189", "(032) 513 8180"));

        tsmList.add(new TowingServicesModel("A Plus Towing Services", "304 V Rama Ave, Cebu City, Cebu", "10.295458, 123.892519", "(032) 261 5050"));

        tsmList.add(new TowingServicesModel("Unocarshop", "6th Street, North Road, Cebu City, 6000 Cebu", "10.310610, 123.915508", "(032) 479 9601"));

        tsmList.add(new TowingServicesModel("JLM Towing Services", "Spolarium street Duljo Fatima, Cebu City, 6000 Cebu", "10.291477, 123.883772", "(032) 417 4790"));

        tsmList.add(new TowingServicesModel("Cinco Auto Care And Towing Service", "B.Suico, Mandaue City, Cebu", "10.357462, 123.935024", "0923 717 6572"));

        tsmList.add(new TowingServicesModel("Road Warriors Towing and Motors Services", "F. Jaca, Cebu City, 6000 Cebu", "10.275429,123.856725", " (032) 272 5575"));

        tsmList.add(new TowingServicesModel("KM Ace Towing", "L.Jaime, Mandaue City", "10.333875, 123.938365", "3437177"));

        tsmList.add(new TowingServicesModel("Tri-J Marketing, Inc.", "Guadalquiver, 380 N. Bacalso Ave Cebu South Road, Basak San, Cebu City, 6000 Cebu", "10.289422,123.869364", "(032) 418 3888"));

        tsmList.add(new TowingServicesModel("Tri J", "G. K Chua Bldg, M. J. Cuenco Ave, Cebu City, 6000 Cebu", "10.295697, 123.905560", "(032) 416 8884"));

        if (!dbHandler.checkTowingifEmpty()) {
            for (int i = 0; i < tsmList.size(); i++) {
                dbHandler.AddTowingServices(tsmList.get(i));
            }
        }
    }

    public void AddEmergencyContacts(){

        emcList = new ArrayList<>();

        emcList.add(new EmergencyContactsModel("", "", "", ""));
    }
}

