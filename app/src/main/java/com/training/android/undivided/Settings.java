package com.training.android.undivided;

import android.*;
import android.Manifest;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.audiofx.BassBoost;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.lang.reflect.Method;

import com.google.android.gms.location.LocationServices;
import com.training.android.undivided.BackgroundService.BackgroundService;

public class Settings extends AppCompatActivity {

    private Switch mAutoStartSwitch;
    private Switch mAutoDeclineCalls;
    BackgroundService myService;
    static boolean status;
    LocationManager locationManager;
    public static long startTime, endTime;
    public static int p=0;

    private ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            BackgroundService.LocalBinder binder = (BackgroundService.LocalBinder)iBinder;
            myService = binder.getService();
            status = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            status = false;
        }
    };

    public void onDestroy(){
        if(status == true)
        unbindService();
        super.onDestroy();
    }

    private void unbindService() {
        if (status == false)
            return;
        Intent i = new Intent(getApplicationContext(), LocationServices.class);
        unbindService(sc);
        status=false;
    }

    @Override
    public void onBackPressed() {
        if(status==false)
            super.onBackPressed();
        else
            moveTaskToBack(true);
    }

    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode)
        {
            case 1000:
            {
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "GRANTED", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "DENIED", Toast.LENGTH_SHORT).show();
            } return;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAutoStartSwitch = (Switch) findViewById(R.id.swAutoStart);

        if(ContextCompat.checkSelfPermission(Settings.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(Settings.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(Settings.this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {

            requestPermissions(new String[] {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, 1000);
        }

        onClickListeners();

    }


    public void onClickListeners() {
        /**
         * BACKGROUND FUNCTION WITH AUTO START ( INCLUDING ON DESTROY )
         * (IMPLEMENTED WITH SWITCH RIGHT NOW)
         */
        mAutoStartSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    checkGPS();
                    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                    if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                        return;
                    if(status==false)
                        bindService();
                    
                }
                else
                {
                    if(status==true)
                        unbindService();

                }
                /*Intent i = new Intent(Settings.this, BackgroundService.class);
                if (b) {
                    startService(i);
                    Toast.makeText(Settings.this, "Auto Start enabled", Toast.LENGTH_SHORT).show();
                } else {
                    stopService(i);
                    Toast.makeText(Settings.this, "Auto Start disabled", Toast.LENGTH_SHORT).show();
                }*/
            }
        });

    }

    private void checkGPS() {
        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            showGPSDisabledAlert();

    }

    private void showGPSDisabledAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Enable GPS to Allow Auto Start")
                .setCancelable(false)
                .setPositiveButton("Enable GPS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void bindService() {
        if(status == true)
            return;
        Intent i = new Intent(getApplicationContext(),BackgroundService.class);
        bindService(i,sc,BIND_AUTO_CREATE);
        status= true;
        startTime = System.currentTimeMillis();
    }

    public void disconnectCall() {

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);


    }
}
