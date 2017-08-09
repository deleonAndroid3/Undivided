package com.training.android.undivided;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Settings extends AppCompatActivity {

    private Switch mAutoStartSwitch;
    private Switch mAutoDeclineCalls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAutoStartSwitch = (Switch) findViewById(R.id.swAutoStart);
        mAutoDeclineCalls = (Switch) findViewById(R.id.swAutoDeclineCalls);

        onClickListeners();

    }

    public void onClickListeners() {
        /**
         * BACKGROUND FUNCTION WITH AUTO START ( INCLUDING ON DESTROY )
         * (IMPLEMENTED WITH SWITCH RIGHT NOW)
         */
        mAutoStartSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Intent i = new Intent(Settings.this, BackgroundService.class);
                if (b) {
                    startService(i);
                    Toast.makeText(Settings.this, "Auto Start enabled", Toast.LENGTH_SHORT).show();
                } else {
                    stopService(i);
                    Toast.makeText(Settings.this, "Auto Start disabled", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mAutoDeclineCalls.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){

                }
            }
        });

    }
}
