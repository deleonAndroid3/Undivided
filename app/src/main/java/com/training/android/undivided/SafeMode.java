package com.training.android.undivided;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class SafeMode extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_mode);

        imgView = findViewById(R.id.ivDriveStop);
        drawerLayout = findViewById(R.id.drawer_layout);

        drawerLayout.getBackground().setAlpha(80);

        Toast.makeText(SafeMode.this, "Safe Mode Selected", Toast.LENGTH_SHORT).show();
        ComponentName mDeviceAdmin;
        DevicePolicyManager myDevicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        mDeviceAdmin = new ComponentName(SafeMode.this, MainActivity.class);

        if (myDevicePolicyManager.isDeviceOwnerApp(SafeMode.this.getPackageName())) {
            // Device owner
            String[] packages = {SafeMode.this.getPackageName()};
            myDevicePolicyManager.setLockTaskPackages(mDeviceAdmin, packages);
        } else {
            Toast.makeText(SafeMode.this, "91826312", Toast.LENGTH_SHORT).show();
        }

        if (myDevicePolicyManager.isLockTaskPermitted(SafeMode.this.getPackageName())) {
            // Lock allowed
            // NOTE: locking device also disables notification
            startLockTask();
        } else {
            Toast.makeText(SafeMode.this, "123897123", Toast.LENGTH_SHORT).show();
            startLockTask();
        }

        imgView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent emergency = new Intent(SafeMode.this, Emergency.class);
                startActivity(emergency);
                return false;
            }
        });
    }
}
