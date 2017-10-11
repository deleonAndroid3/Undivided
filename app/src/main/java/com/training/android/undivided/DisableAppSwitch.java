package com.training.android.undivided;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class DisableAppSwitch extends AppCompatActivity {

    private Button mbtnSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disable_app_switch);

        mbtnSwitch = (Button) findViewById(R.id.btnSwitch);
        ComponentName mDeviceAdmin;
        DevicePolicyManager myDevicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        mDeviceAdmin = new ComponentName(this, DisableAppSwitch.class);

        if (myDevicePolicyManager.isDeviceOwnerApp(this.getPackageName())) {
            // Device owner
            String[] packages = {this.getPackageName()};
            myDevicePolicyManager.setLockTaskPackages(mDeviceAdmin, packages);
        } else {
            Toast.makeText(this, "Failed.", Toast.LENGTH_SHORT).show();
        }

        if (myDevicePolicyManager.isLockTaskPermitted(this.getPackageName())) {
            // Lock allowed 
            // NOTE: locking device also disables notification
            startLockTask();
        } else {
            Toast.makeText(this, "2nd Failed.", Toast.LENGTH_SHORT).show();
            startLockTask();
        }

        mbtnSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DisableAppSwitch.this, Functions.class);
                stopLockTask();
                startActivity(i);
            }
        });


    }
}
