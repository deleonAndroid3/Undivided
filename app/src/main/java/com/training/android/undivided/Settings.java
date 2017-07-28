package com.training.android.undivided;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class Settings extends AppCompatActivity {

    Switch mAutoStartSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAutoStartSwitch = (Switch) findViewById(R.id.swAutoStart);

        mAutoStartSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Intent i = new Intent(Settings.this, BackgroundService.class);
                    startService(i);
                    Toast.makeText(Settings.this, "Auto Start enabled", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Settings.this, "Auto Start disabled", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
